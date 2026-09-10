package com.bloodconnect.repository;

import com.bloodconnect.entity.BloodBank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BloodBankRepository extends JpaRepository<BloodBank, Integer> {

    List<BloodBank> findByCityIgnoreCase(String city);
}