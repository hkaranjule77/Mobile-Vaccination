package com.aghs.mobilevaccination.data.model.location;

import javax.persistence.*;

@Entity
@Table
public class City {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column
    private String name;
    @ManyToOne
    private District district;
}
