package org.ditio.backend.Services;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.ditio.backend.TimeBasedOnetimePassword;
import org.ditio.backend.Entities.Event;
import org.ditio.backend.Entities.EventReg;
import org.ditio.backend.Entities.User;
import org.ditio.backend.Entities.Enums.Attendance_Values;
import org.ditio.backend.Repositories.EventRegRepository;
import org.ditio.backend.Repositories.EventRepository;
import org.ditio.backend.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class EventRegService {

    private static final int STEP_SECONDS = 30;
    private final Clock clock = Clock.systemUTC();

    private final EventRegRepository repository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final String secretBase32;

    public EventRegService(
            EventRegRepository repository,
            UserRepository userRepository,
            EventRepository eventRepository,
            // gjør Clock injiserbar for enklere testing
            @Value("${otp.secret:OTP_CONFIG}") String secretConfig,
            @Value("${otp.secret.isBase32:false}") boolean isBase32
    ) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.secretBase32 = isBase32 ? secretConfig : TimeBasedOnetimePassword.encodeBase32(secretConfig);
    }

        
    public List<EventReg> findAll() {
        return repository.findAll();
    }
 
    public EventReg findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "EventReg2 not found"));
    }
    
    public void applyDeadline(EventReg reg) {
    UUID eventId = reg.getEventId();
    if (eventId == null) {
        throw new IllegalArgumentException("event_id må settes på EventReg2 før deadline kan beregnes");
    }

    Event event = eventRepository.findById(eventId)
            .orElseThrow(() -> new EntityNotFoundException("Event ikke funnet: " + eventId));

    LocalDateTime start = event.getStartTime();
    if (start == null) {
        throw new IllegalStateException("Event.startTime er null for event: " + eventId);
    }

    // Velg regel:
    // 1) Deadline ved slutten av samme dag som eventets start:
    LocalDateTime deadline = start.toLocalDate().atTime(23, 59, 59);
    reg.setDeadline(deadline);
}

    public void add_quarantine_until(EventReg reg) {
     UUID eventId = reg.getEventId();
        if (eventId == null) {
            throw new IllegalArgumentException("event_id må settes på EventReg2 før deadline kan beregnes");
        }

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event ikke funnet: " + eventId));

        if (event.getStartTime() == null) {
            throw new IllegalStateException("Event.startTime er null for event: " + eventId);
        }

        LocalDateTime quarantine_end_set_to_null = null;
        reg.setQuarantine_end(quarantine_end_set_to_null);
}

    // Blokker registrering hvis bruker er i karantene
    private void blockIfUserInQuarantine(EventReg reg) {
        if (reg.getUserId() == null) return;

        // Eksempel: hent bruker og sjekk karantene til (tilpass etter din User-modell)
        var userOpt = userRepository.findById(reg.getUserId());
        userOpt.ifPresent(user -> {
            var quarantineEnd = user.getQuarantine_until(); // eller tilsvarende felt
            if (quarantineEnd != null && quarantineEnd.isAfter(LocalDateTime.now(clock))) {
                throw new ResponseStatusException(FORBIDDEN, "Bruker er i karantene til " + quarantineEnd);
            }
        });
    }

   private void check_if_user_already_registered_to_event(EventReg newReg_input){
    List<EventReg> list_of_exisiting_eventRegs = repository.findAll();
    
    for (EventReg i: list_of_exisiting_eventRegs){
        boolean sameUser = Objects.equals(i.getUserId(), newReg_input.getUserId());
        boolean sameEvent = Objects.equals(i.getEventId(), newReg_input.getEventId());

        if (sameUser && sameEvent) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bruker er allerede registrert til arrangement");
            
        }
    }
}
// ------- WORK IN PROGESS ---- Needs working foreign keys
    /*private void check_max_attendance(EventReg2 new_eventReg){
    
        //List<EventReg2> list_of_exisiting_eventRegs = repository.findAll();
        List <EventReg2> confirmed_counts = repository.findAllByAttStatusNot(Attendance_Values.confirmed);
        List <EventReg2> waitlist_counts = repository.findAllByAttStatusNot(Attendance_Values.waitlist);
        
        LocalDateTime now = LocalDateTime.now();
        int maximum_people = event.getMaxAttendees(); //<----- Event-EventReg need to be connected
                                                    //  for EventReg.getMaxAttendees() to work
        
        if(now.isBefore(event.getStartTime())){
            if((confirmed_counts.size() > maximum_people) && (waitlist_counts.size() == 0)){
                return;
            }
            if(confirmed_counts.size()==maximum_people){
                new_eventReg.setAttStatus(Attendance_Values.waitlist);
            }
            if ((confirmed_counts.size() > maximum_people) && (waitlist_counts.size() < 0)){
                for (EventReg2 i : waitlist_counts){
                    i.setAttStatus(Attendance_Values.confirmed);
                }
            repository.save(new_eventReg);
            }
        }
        else return;
            
    } */

    @Transactional
    public EventReg create(EventReg reg) {
        // 1) Valider domene-regler
        //If now is before start_time
        applyDeadline(reg);
        add_quarantine_until(reg);
        blockIfUserInQuarantine(reg);
        check_if_user_already_registered_to_event(reg);
        //check_max_attendance(reg);


        // 2) Initier status hvis nødvendig
        if (reg.getAttStatus() == null) {
            reg.setAttStatus(Attendance_Values.confirmed);
        }
        return repository.save(reg);
    }

    // Cron-format: "sekunder minutter timer dager måneder ukedager"
    // "0 0 0 * * *" betyr hver eneste dag kl. 00:00:00
    //Hvis du vil teste det raskt uten å vente til midnatt, 
    // kan du endre det til f.eks. 0 */5 * * * * (hvert 5. minutt).
   @Scheduled(cron = "0 */1 * * * *" )
   //Should it be private?
    public void Auto_midnight_put_students_in_quarantine() {
        LocalDateTime now = LocalDateTime.now();

        List<EventReg> overdueList = repository.findByDeadlineBeforeAndAttStatusNot(now, Attendance_Values.no_show);

        if (overdueList.isEmpty()) {
            return; // Ingen ting å gjøre
        }

        for (EventReg att : overdueList) {
            change_status_to_quarantine_with_date(att);
        }
        
        System.out.println("Automatisk sjekk fullført. Behandlet " + overdueList.size() + " rader.");
    }


    // Selve logikken som endrer status og setter karantene-dato
    private void change_status_to_quarantine_with_date(EventReg att) {
        att.setAttStatus(Attendance_Values.no_show);
        att.setQuarantine_end(att.getDeadline().plusDays(30)); // Eksempel: 30 dager karantene
        repository.save(att);
    }

    //"1 0 * * *" = 1 min after midnight every day
    @Scheduled(cron = "0 */2 * * * *")
    @Transactional
    public void checkIfQuarantineIsOver() {
        LocalDateTime now = LocalDateTime.now();

        // Mest presist: hent kun de som faktisk har utløpt karantene
        List<User> expired = userRepository.findAllByQuarantineUntilBefore(now);
        if (expired.isEmpty()) {
            return;
        }

        for (User quarantinedUser : expired) {
            quarantinedUser.setQuarantine_until(null);
        }


        // saveAll er greit, eller stol på @Transactional + dirty checking
        userRepository.saveAll(expired);
    }


    // Merk som no_show hvis fristen er passert og status ikke er attended/waitlist
    @Transactional
    public EventReg Manually_mark_student_as_quarantined_if_noshow(UUID id) {
        var reg = findById(id);
        LocalDateTime now = LocalDateTime.now(clock);

        if (reg.getDeadline() == null) {
            throw new ResponseStatusException(CONFLICT, "Registreringen mangler deadline");
        }

        if (now.isAfter(reg.getDeadline())
                && reg.getAttStatus() != Attendance_Values.attended
                && reg.getAttStatus() != Attendance_Values.waitlist) {

            reg.setAttStatus(Attendance_Values.no_show);
            // Oppgi karantene som 30 dager etter deadline
            reg.setQuarantine_end(reg.getDeadline().plusDays(30));
            return repository.save(reg);
        }

        // Ingen endring å gjøre
        return reg;
    }
    
    // DTO som kan serialiseres direkte av Spring
    public record OtpResponse(String code, Instant expiresAt) {}

    public OtpResponse currentOtp() {
        long now = Instant.now(clock).getEpochSecond();
        String code = TimeBasedOnetimePassword.generateTOTP(secretBase32);
        long remaining = STEP_SECONDS - (now % STEP_SECONDS);
        Instant expiresAt = Instant.ofEpochSecond(now + remaining);
        return new OtpResponse(code, expiresAt);
    }

    // Bekreft oppmøte med OTP
    @Transactional
    public EventReg confirmAttendance(UUID id, String otpInput) {

        boolean valid = TimeBasedOnetimePassword.validateTOTP(secretBase32, otpInput);
        var reg = findById(id);
        LocalDateTime now = LocalDateTime.now(clock);

        if (!valid) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Feil kode");
        }

        if (reg.getAttStatus() != Attendance_Values.confirmed) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Du er ikke registrert til arrangementet, og kan ikke melde oppmøte.");
        }

        if (reg.getDeadline() != null && now.isAfter(reg.getDeadline())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Du møtte ikke opp på arrangementet, og kan ikke melde oppmøte.");
        }

        else{
            reg.setAttStatus(Attendance_Values.attended);
            reg.setQuarantine_end(null);
            return repository.save(reg);
        }
        
    }


    @Transactional
    public EventReg update(EventReg reg) {
        // Legg inn ønskede valideringer og regelverk
        return repository.save(reg);
    }

    @Transactional
    public EventReg delete(UUID id) {
        var reg = findById(id);
        repository.delete(reg);
        return reg;
    }

    @Transactional
    public List<EventReg> deleteAll(){
        var all_regs = findAll();
        repository.deleteAll(all_regs);
            return all_regs;


    }
}