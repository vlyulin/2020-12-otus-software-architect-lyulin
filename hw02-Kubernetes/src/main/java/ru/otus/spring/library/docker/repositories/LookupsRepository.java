package ru.otus.spring.library.docker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.otus.spring.library.docker.models.LookupValue;
import ru.otus.spring.library.docker.models.LookupValueId;

import java.util.List;

public interface LookupsRepository extends JpaRepository<LookupValue, Long> {
    List<LookupValue> findByKey(LookupValueId key);

//    @Query("select l.key from LookupValue l where l.meaning = :meaning and l.key.language = :language")
//    List<LookupValueId> findByMeaningAndKeyLanguage(@Param("meaning") String meaning, @Param("language") String language);

    @Query("select l from LookupValue l where l.key.lookupType = :lookupType " +
            "and l.key.language = :language")
    List<LookupValue> findByLookupTypeLanguage(@Param("lookupType")String lookupType,
                                                   @Param("language") String language);

    @Query("select l from LookupValue l where l.key.lookupType = :lookupType " +
            "and l.key.lookupCode = :lookupCode and l.key.language = :language")
    LookupValue findByLookupTypeLookupCodeLanguage(@Param("lookupType")String lookupType,
                                                 @Param("lookupCode")String lookupCode,
                                                 @Param("language") String language);

    // Все значения для указаного языка
    List<LookupValue> findByKeyLanguage(String language);
}
