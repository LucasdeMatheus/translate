package com.myproject.translator.domain.dialogue;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DialogueRepository extends JpaRepository<Dialogue, Long> {
}
