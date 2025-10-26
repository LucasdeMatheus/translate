package com.myproject.translator.domain.translation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TranslationRepository extends JpaRepository<Translation, Long> {
    List<Translation> findByTextIn(List<String> termList);

    @Query("SELECT t FROM Translation t WHERE LOWER(t.text) LIKE LOWER(CONCAT('%', :term, '%'))")
    List<Translation> findByTextLike(@Param("term") String term);


}
