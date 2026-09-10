package com.bloodconnect.service;

import com.bloodconnect.entity.BloodInventory;
import com.bloodconnect.entity.User;
import com.bloodconnect.repository.BloodInventoryRepository;
import com.bloodconnect.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BloodService {

    private final BloodInventoryRepository bloodInventoryRepository;
    private final UserRepository userRepository;

    public BloodService(
            BloodInventoryRepository bloodInventoryRepository,
            UserRepository userRepository) {

        this.bloodInventoryRepository = bloodInventoryRepository;
        this.userRepository = userRepository;
    }

    public List<BloodInventory> searchBlood(String bloodGroup, String city) {

        return bloodInventoryRepository
                .findByBloodGroupIgnoreCaseAndBloodBank_CityIgnoreCase(
                        bloodGroup,
                        city
                )
                .stream()
                .filter(inventory -> inventory.getUnitsAvailable() > 0)
                .toList();
    }

    // Admin can update any blood bank
    public BloodInventory updateUnits(
            Integer bankId,
            String bloodGroup,
            Integer units) {

        if (units < 0) {
            throw new IllegalArgumentException("Units cannot be negative");
        }

        BloodInventory inventory = bloodInventoryRepository
                .findByBloodBank_IdAndBloodGroupIgnoreCase(
                        bankId,
                        bloodGroup
                )
                .orElseThrow(() ->
                        new RuntimeException("Inventory not found"));

        inventory.setUnitsAvailable(units);

        return bloodInventoryRepository.save(inventory);
    }

    // Manager can update only their own blood bank
    public BloodInventory updateUnitsForManager(
            String username,
            String bloodGroup,
            Integer units) {

        if (units < 0) {
            throw new IllegalArgumentException(
                    "Units cannot be negative");
        }

        User manager = userRepository
                .findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("Manager not found"));

        if (!"MANAGER".equalsIgnoreCase(manager.getRole())) {
            throw new RuntimeException(
                    "Only blood bank managers can use this operation");
        }

        if (manager.getBloodBank() == null) {
            throw new RuntimeException(
                    "No blood bank assigned to this manager");
        }

        Integer bankId = manager.getBloodBank().getId();

        BloodInventory inventory = bloodInventoryRepository
                .findByBloodBank_IdAndBloodGroupIgnoreCase(
                        bankId,
                        bloodGroup
                )
                .orElseThrow(() ->
                        new RuntimeException("Inventory not found"));

        inventory.setUnitsAvailable(units);

        return bloodInventoryRepository.save(inventory);
    }
   public List<BloodInventory> getAllInventory() {

        return bloodInventoryRepository.findAll();
}
   public List<BloodInventory> getInventoryForBank(Integer bankId) {

    return bloodInventoryRepository
            .findByBloodBank_Id(bankId);
}
}