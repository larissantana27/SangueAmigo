package com.example.SangueAmigo.model.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressInfoRepository extends JpaRepository<AddressInformation, String> {

    AddressInformation findById(int id);
}
