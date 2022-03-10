package com.aghs.mobilevaccination.data.repository.vaccine;

import com.aghs.mobilevaccination.data.model.Member;
import com.aghs.mobilevaccination.data.model.location.Spot;
import com.aghs.mobilevaccination.data.model.vaccine.MemberVaccination;
import com.aghs.mobilevaccination.data.model.vaccine.VaccineDrive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MemberVaccinationRepository extends JpaRepository<MemberVaccination, Long> {
    List<MemberVaccination> findByVaccinationSpotAndSelectedDate(Spot VaccinationSpot, LocalDate selectedDate);
    List<MemberVaccination> findByVaccinationSpotAndSelectedDateAndVaccineDrive(Spot VaccinationSpot,
                                                                                LocalDate selectedDate,
                                                                                VaccineDrive vaccineDrive);
    List<MemberVaccination> findByRecipient(Member recipient);
}
