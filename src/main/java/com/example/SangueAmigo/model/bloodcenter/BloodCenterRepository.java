package com.example.SangueAmigo.model.bloodcenter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BloodCenterRepository extends JpaRepository<BloodCenter, String> {

    @Query("SELECT bc.name FROM BloodCenter bc " +
            "INNER JOIN BloodStock bs ON bs.bloodCenterId = bc.id " +
            "WHERE bs.type = :userBloodType " +
            "ORDER BY bs.quantity ASC, bc.id ASC LIMIT 1")
    String getMostNeedingBloodCenter(@Param("userBloodType") String userBloodType);

    @Query("SELECT bs.type FROM BloodStock bs ORDER BY bs.quantity ASC LIMIT 1")
    String getMostNeedingBloodType();
}