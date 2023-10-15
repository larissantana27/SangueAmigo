package com.example.SangueAmigo.model.donation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface DonationRepository extends JpaRepository<Donation, String> {

    @Query("SELECT MAX(d.date) FROM Donation d WHERE d.user_id = :userId")
    Date findLastDonationDateByUserId(@Param("userId") int userId);

    @Query("SELECT COUNT(*) FROM Donation d WHERE d.user_id = :userId")
    int getDonationQuantityByUserId(@Param("userId") int userId);
}