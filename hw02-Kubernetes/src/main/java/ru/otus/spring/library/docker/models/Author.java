package ru.otus.spring.library.docker.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "authors")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "author_id", updatable = false, nullable = false)
    private long id;

    @Column(name = "country")
    private String country;
    @Column(name = "sex")
    private char sex;
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;
    @Column(name = "name", nullable = false)
    private String name;
}
