//https://stormatics.tech/blogs/postgres-roles-and-privileges-simplified
/*package org.ditio.backend.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "quarantine_status")
public class QuarantineStatus {
    @Id
    @Column(name = "quarantine_status_id")
    private String QuarantineStatus_Id;

    @Column (name = "quarantine_status_true_or_false")
    private boolean QuarantineStatus_true_or_false;

    //bidireksjonal 1:1 forhold
    @OneToOne(mappedBy = "quarantine_status", fetch = FetchType.LAZY)
    private User user;

    @OneToOne
    private Quarantine quarantine; 

} 

//Jeg legger til settere og gettere for q_status_id + true/false senere
*/

