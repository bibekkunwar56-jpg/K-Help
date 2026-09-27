package com.example.demo.service;

import com.example.demo.dto.CreateHouseRequest;
import com.example.demo.dto.HouseResponse;
import com.example.demo.entity.House;
import com.example.demo.entity.User;
import com.example.demo.repository.HouseRepository;
import com.example.demo.repository.UserRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class HouseService {

    private final HouseRepository houseRepository;
    private final UserRepository userRepository;

    public HouseService(HouseRepository houseRepository, UserRepository userRepository) {
        this.houseRepository = houseRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<HouseResponse> list(String location, Integer maxRent, Long maxDeposit) {
        String loc = blankToNull(location);

        List<House> list;
        if (loc != null && maxRent != null && maxDeposit != null) {
            list = houseRepository.findByLocationAndBudget(loc, maxRent, maxDeposit);
        } else if (maxRent != null && maxDeposit != null) {
            list = houseRepository.findByBudget(maxRent, maxDeposit);
        } else if (loc != null) {
            list = houseRepository.findByLocation(loc);
        } else {
            list = houseRepository.findAllAvailableWithLandlord();
        }

        return list.stream().map(HouseResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public HouseResponse get(UUID id) {
        House house = houseRepository
                .findById(id)
                .filter(House::isAvailable)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Housing listing not found"));
        return HouseResponse.from(house);
    }

    @Transactional
    public HouseResponse create(UUID landlordId, CreateHouseRequest req) {
        User landlord = userRepository
                .findById(landlordId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        House house = new House();
        house.setLandlord(landlord);
        house.setTitle(req.getTitle().trim());
        house.setHousingType(req.getHousingType() != null ? req.getHousingType().trim() : "ONE_ROOM");
        house.setLocation(req.getLocation().trim());
        house.setDepositKrw(req.getDepositKrw());
        house.setMonthlyRentKrw(req.getMonthlyRentKrw());
        house.setMaintenanceFeeKrw(req.getMaintenanceFeeKrw() != null ? req.getMaintenanceFeeKrw() : 0);
        house.setFloorLevel(blankToNull(req.getFloorLevel()));
        house.setDescription(req.getDescription().trim());
        house.setContactPhone(blankToNull(req.getContactPhone()));
        house.setAvailable(true);

        return HouseResponse.from(houseRepository.save(house));
    }

    @Transactional
    public void delete(UUID id, UUID userId) {
        House house = houseRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Housing listing not found"));

        if (!house.getLandlord().getId().equals(userId)) {
            User actor = userRepository.findById(userId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
            if (!"ADMIN".equals(actor.getRole())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only the landlord or admin can delete this listing");
            }
        }

        house.setAvailable(false);
        houseRepository.save(house);
    }

    private static String blankToNull(String val) {
        return (val == null || val.isBlank()) ? null : val.trim();
    }
}
