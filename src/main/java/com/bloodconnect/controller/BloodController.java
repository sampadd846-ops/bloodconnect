package com.bloodconnect.controller;

import com.bloodconnect.entity.BloodInventory;
import com.bloodconnect.service.BloodService;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/blood")
@CrossOrigin(origins = "*")
public class BloodController {

    private final BloodService bloodService;

    public BloodController(BloodService bloodService) {
        this.bloodService = bloodService;
    }


    /* =========================
       PUBLIC BLOOD SEARCH
    ========================== */

    @GetMapping("/search")
    public List<BloodInventory> searchBlood(
            @RequestParam("group") String group,
            @RequestParam("city") String city) {

        return bloodService.searchBlood(
                group,
                city
        );
    }


    /* =========================
       ADMIN UPDATE
    ========================== */

    @PutMapping("/update")
    public BloodInventory updateBlood(
            @RequestParam("bankId") Integer bankId,
            @RequestParam("group") String group,
            @RequestParam("units") Integer units,
            HttpSession session) {

        String username =
                (String) session.getAttribute("username");

        String role =
                (String) session.getAttribute("role");

        if (username == null) {
            throw new RuntimeException(
                    "Please login first"
            );
        }

        if (!"ADMIN".equalsIgnoreCase(role)) {
            throw new RuntimeException(
                    "Admin access required"
            );
        }

        return bloodService.updateUnits(
                bankId,
                group,
                units
        );
    }


    /* =========================
       MANAGER UPDATE
       Uses SERVER SESSION
    ========================== */

    @PutMapping("/manager-update")
    public BloodInventory updateBloodByManager(
            @RequestParam("group") String group,
            @RequestParam("units") Integer units,
            HttpSession session) {

        // Get logged-in username from server session
        String username =
                (String) session.getAttribute("username");

        if (username == null) {
            throw new RuntimeException(
                    "Please login first"
            );
        }

        // Check role
        String role =
                (String) session.getAttribute("role");

        if (!"MANAGER".equalsIgnoreCase(role)) {
            throw new RuntimeException(
                    "Manager access required"
            );
        }

        return bloodService.updateUnitsForManager(
                username,
                group,
                units
        );
    }


    /* =========================
       ADMIN INVENTORY
       Shows ALL BLOOD BANKS
    ========================== */

    @GetMapping("/inventory")
    public List<BloodInventory> getAllInventory(
            HttpSession session) {

        String username =
                (String) session.getAttribute("username");

        String role =
                (String) session.getAttribute("role");

        if (username == null) {
            throw new RuntimeException(
                    "Please login first"
            );
        }

        if (!"ADMIN".equalsIgnoreCase(role)) {
            throw new RuntimeException(
                    "Admin access required"
            );
        }

        return bloodService.getAllInventory();
    }


    /* =========================
       MANAGER INVENTORY
       Shows ONLY OWN BANK
    ========================== */

    @GetMapping("/manager-inventory")
    public List<BloodInventory> getManagerInventory(
            HttpSession session) {

        String username =
                (String) session.getAttribute("username");

        String role =
                (String) session.getAttribute("role");

        if (username == null) {
            throw new RuntimeException(
                    "Please login first"
            );
        }

        if (!"MANAGER".equalsIgnoreCase(role)) {
            throw new RuntimeException(
                    "Manager access required"
            );
        }

        Integer bloodBankId =
                (Integer) session.getAttribute(
                        "bloodBankId"
                );

        if (bloodBankId == null) {
            throw new RuntimeException(
                    "No blood bank assigned to this manager"
            );
        }

        return bloodService.getInventoryForBank(
                bloodBankId
        );
    }

}