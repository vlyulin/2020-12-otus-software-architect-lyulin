package ru.otus.spring.library.docker.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "books")
public class Book {
    private static final String GENRES = "GENRES";
    private static final String LANGUAGE = "RU";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id", updatable = false, nullable = false)
    private long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "publishing_year")
    private int publishingYear;
    @Column(name = "pages")
    private int pages;


    @ManyToOne(targetEntity = LookupValue.class, fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @JoinColumnsOrFormulas({
            @JoinColumnOrFormula(formula=@JoinFormula(value="'"+GENRES+"'", referencedColumnName = "lookup_type")),
            @JoinColumnOrFormula(formula = @JoinFormula(value="'"+LANGUAGE+"'", referencedColumnName="language")),
            @JoinColumnOrFormula(column = @JoinColumn(name="genre", referencedColumnName = "lookup_code"))
    })
    private LookupValue genre;

    @ManyToOne(targetEntity = Author.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id")
    private Author author;
    @ManyToOne(targetEntity = PublishingHouse.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "publishing_house_id")
    private PublishingHouse publishingHouse;

    // «0+», «6+», «12+», «16+», «18+»
    @Column(name = "age_limit")
    private String ageLimit;
}
