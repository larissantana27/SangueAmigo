package com.example.SangueAmigo.model.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    UserDetails findByEmail(String email);

    @Modifying
    @Query("UPDATE User u SET u.password = :newPassword WHERE u.id = :userId")
    void setPassword(@Param("userId") int userId, @Param("newPassword") String newPassword);

    @Query("SELECT password FROM User u WHERE u.id = :userId")
    String getPassword(@Param("userId") int userId);

    @Query("SELECT bi.rhFactor FROM BiologicalInformation bi " +
            "INNER JOIN User u ON u.biologicalInfoId = bi.id " +
            "WHERE u.id = :userId")
    String getRhFactorType(@Param("userId") int userId);

    @Query("SELECT CONCAT(bi.bloodType, :signal) FROM BiologicalInformation bi " +
        "INNER JOIN User u ON u.biologicalInfoId = bi.id " +
        "WHERE u.id = :userId")
    String getBloodType(@Param("userId") int userId, @Param("signal") String signal);
}