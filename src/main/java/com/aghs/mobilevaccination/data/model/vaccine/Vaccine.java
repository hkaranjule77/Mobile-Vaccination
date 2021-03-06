package com.aghs.mobilevaccination.data.model.vaccine;

import com.aghs.mobilevaccination.data.model.Disease;
import com.aghs.mobilevaccination.data.model.Staff;
import com.aghs.mobilevaccination.data.repository.vaccine.VaccineCategoryRepository;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table
public class Vaccine {
    @Id
    @Column
    private String name;
    @Column(unique = true, length = 3)
    private String code;
    // To take another vaccine
    @Column
    private Long intervalInDays;
    @ManyToOne
    private Disease ofDisease;
    @ManyToOne
    private Staff addedBy;
    @Column
    private Date addedAt;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getIntervalInDays() {
        return intervalInDays;
    }

    public void setIntervalInDays(Long intervalInDays) {
        this.intervalInDays = intervalInDays;
    }

    public Disease getOfDisease() {
        return ofDisease;
    }

    public void setOfDisease(Disease ofDisease) {
        this.ofDisease = ofDisease;
    }

    public Staff getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(Staff addedBy) {
        this.addedBy = addedBy;
    }

    public Date getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(Date addedAt) {
        this.addedAt = addedAt;
    }

    public List<VaccineCategory> getMandatoryCategories(VaccineCategoryRepository categoryRepository, int age) {
        List<VaccineCategory> categories = categoryRepository.findByVaccine(this);
        List<VaccineCategory> mandatoryCategory = new ArrayList<>();
        for(VaccineCategory category: categories) {
            if(category.isMandatory() && category.getMinAgeLimit() <= age && age <= category.getMaxAgeLimit()) {
                mandatoryCategory.add(category);
            }
        }
        return mandatoryCategory;
    }
}
