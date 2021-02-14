package ru.otus.spring.library.docker.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Data
@NoArgsConstructor
@Entity
@Table(name = "comments")
public class Comment implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id", updatable = false, nullable = false)
    private long id;
    @Column(name = "book_id", updatable = false, nullable = false)
    private long bookId;
    @Column(name = "comment", nullable = false)
    private String comment;
    @OneToOne(targetEntity = User.class)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;
    @Column(name = "creation_date")
    LocalDate creationDate;
    @OneToOne(targetEntity = User.class)
    @JoinColumn(name = "last_updated_by")
    private User lastUpdatedBy;
    @Column(name = "last_update_date")
    private LocalDate lastUpdateDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comment)) return false;
        Comment comment = (Comment) o;
        return id == comment.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
