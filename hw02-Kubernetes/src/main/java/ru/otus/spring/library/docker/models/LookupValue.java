package ru.otus.spring.library.docker.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "lookup_values")
public class LookupValue implements Serializable {

    @Transient
    public static final String GENRES_LOOKUP_TYPE = "GENRES";
    @Transient
    public static final String US = "US";
    @Transient
    public static final String RU = "RU";

    @EmbeddedId
    LookupValueId key;
    @Column(name = "enabled_flag")
    private char enabledFlag;
    @Column(name = "start_date_active")
    private LocalDate startDateActive = LocalDate.MIN;
    @Column(name = "end_date_active")
    private LocalDate endDateActive = LocalDate.MAX;
    @Column(name = "meaning", nullable = false)
    private String meaning;
    @Column(name = "description")
    private String description;
}
