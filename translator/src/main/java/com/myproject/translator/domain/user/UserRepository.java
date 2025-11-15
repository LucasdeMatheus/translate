package com.myproject.translator.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {


    @Query("""
       SELECT new com.myproject.translator.domain.user.UserDTO(
           u.id,
           u.email,
           u.name
       )
       FROM User u
       WHERE u.id = :id
       """)
    UserDTO findLightById(Long id);

}
