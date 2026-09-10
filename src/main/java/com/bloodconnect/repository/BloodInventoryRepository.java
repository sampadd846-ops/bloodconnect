package com.bloodconnect.repository;

import com.bloodconnect.entity.BloodInventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BloodInventoryRepository extends JpaRepository<BloodInventory, Integer> {

    List<BloodInventory> findByBloodGroupIgnoreCaseAndBloodBank_CityIgnoreCase(
            String bloodGroup,
            String city
    );

    Optional<BloodInventory> findByBloodBank_IdAndBloodGroupIgnoreCase(
            Integer bloodBankId,
            String bloodGroup
    );
    List<BloodInventory> findByBloodBank_Id(Integer bloodBankId);
    
}
