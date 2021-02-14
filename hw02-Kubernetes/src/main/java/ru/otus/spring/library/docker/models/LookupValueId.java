package ru.otus.spring.library.docker.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class LookupValueId implements Serializable {

    @Column(name = "lookup_type")
    private String lookupType;
    @Column(name = "lookup_code")
    private String lookupCode;
    @Column(name = "language")
    private String language;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LookupValueId)) return false;
        LookupValueId that = (LookupValueId) o;
        return lookupType.equals(that.lookupType) &&
                lookupCode.equals(that.lookupCode) &&
                language.equals(that.language);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lookupType, lookupCode, language);
    }
}
