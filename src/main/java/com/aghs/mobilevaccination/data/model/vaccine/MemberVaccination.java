package com.aghs.mobilevaccination.data.model.vaccine;

import com.aghs.mobilevaccination.data.model.Member;
import com.aghs.mobilevaccination.data.model.location.Spot;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.Date;
import java.util.Random;

@Entity
@Table
public class MemberVaccination {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(length = 6)
    private String pin;
    @ManyToOne
    private VaccineDrive vaccineDrive;
    @ManyToOne
    private Spot vaccinationSpot;
    @ManyToOne
    private Member recipient;
    @Column
    private Date registeredAt;
    @Column
    private LocalDate selectedDate;
    @Column
    private Date vaccinatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public VaccineDrive getVaccineDrive() {
        return vaccineDrive;
    }

    public void setVaccineDrive(VaccineDrive vaccineDrive) {
        this.vaccineDrive = vaccineDrive;
    }

    public Spot getVaccinationSpot() {
        return vaccinationSpot;
    }

    public void setVaccinationSpot(Spot vaccinationSpot) {
        this.vaccinationSpot = vaccinationSpot;
    }

    public Member getRecipient() {
        return recipient;
    }

    public void setRecipient(Member recipient) {
        this.recipient = recipient;
    }

    public Date getRegisteredAt() {
        return registeredAt;
    }

    public void setRegisteredAt(Date registeredAt) {
        this.registeredAt = registeredAt;
    }

    public LocalDate getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(LocalDate selectedDate) {
        this.selectedDate = selectedDate;
    }

    public Date getVaccinatedAt() {
        return vaccinatedAt;
    }

    public void setVaccinatedAt(Date vaccinatedAt) {
        this.vaccinatedAt = vaccinatedAt;
    }

    public void generatePIN(int length) {
        Random random = new Random();
        pin = "";
        for(int i=0; i < length; i++) {
            pin += String.valueOf(random.nextInt(10));
        }
    }

    public boolean hasCompletedVaccinationInterval() {
        if( this.getVaccinatedAt() != null) {
            LocalDate vaccinatedDate = this.getVaccinatedAt().toInstant().atZone(ZoneId.systemDefault())
                    .toLocalDate();
            Long interval = this.getVaccineDrive().getVaccine().getIntervalInDays();
            return LocalDate.now().plusDays(interval).compareTo(vaccinatedDate) > 0;
        }
        return true;
    }
}
