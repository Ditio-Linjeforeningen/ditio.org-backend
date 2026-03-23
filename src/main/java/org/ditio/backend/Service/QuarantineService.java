package org.ditio.backend.Service;

import jakarta.persistence.EntityNotFoundException;
import org.ditio.backend.Entities.Quarantine;
import org.ditio.backend.Entities.User;
import org.ditio.backend.Repositories.QuarantineRepository;
import org.ditio.backend.Repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class QuarantineService {

    private final UserRepository userRepository;
    private final QuarantineRepository quarantineRepository;

    public QuarantineService(UserRepository userRepository,
                             QuarantineRepository quarantineRepository) {
        this.userRepository = userRepository;
        this.quarantineRepository = quarantineRepository;
    }

    @Transactional(readOnly = true)
    public List<Quarantine> getAll() {
        return quarantineRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Quarantine getById(UUID id) {
        return quarantineRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Quarantine not found: " + id));
    }

    // Opprett/koble karantene til bruker (User er eier)
    @Transactional
    public Quarantine startQuarantine(String feideId, LocalDate quarantineEnd) {
        User user = userRepository.findById(feideId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + feideId));

        Quarantine q = user.getQuarantine();
        if (q == null) {
            q = new Quarantine(true, quarantineEnd);
            user.setQuarantine(q);  // eier-siden
            q.setUser(user);        // hold objektgrafen konsistent
        } else {
            q.setDoes_quarantine_exist(true);
            q.setQuarantine_end(quarantineEnd);
        }

        // Cascade.ALL på user.quarantine persisterer endringene i Quarantine
        userRepository.save(user);
        return q;
    }

    // Avslutt karantene for en gitt bruker (nuller FK; orphanRemoval sletter raden)
    @Transactional
    public void endQuarantineForUser(String feideId) {
        User user = userRepository.findById(feideId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + feideId));

        if (user.getQuarantine() != null) {
            user.setQuarantine(null); // orphanRemoval -> DELETE av Quarantine på commit
        }
    }

    // Avslutt/slett karantene gitt quarantineId
    @Transactional
    public void endQuarantineByQuarantineId(UUID quarantineId) {
        // Finn brukeren som peker på denne karantenen (FK ligger i users)
        userRepository.findByQuarantine_Quarantine_id(quarantineId)
                .ifPresentOrElse(user -> {
                    user.setQuarantine(null); // orphanRemoval -> sletter raden i quarantine
                }, () -> {
                    // Fallback: hvis ingen user peker på den (uventet i Alt A), slett direkte
                    quarantineRepository.findById(quarantineId)
                            .ifPresent(quarantineRepository::delete);
                });
    }
}