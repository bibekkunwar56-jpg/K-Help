package com.example.demo.dto;

import com.example.demo.entity.House;
import java.time.Instant;
import java.util.UUID;

public class HouseResponse {

    private UUID id;
    private String title;
    private String housingType;
    private String location;
    private Long depositKrw;
    private Integer monthlyRentKrw;
    private Integer maintenanceFeeKrw;
    private String floorLevel;
    private String description;
    private String contactPhone;
    private String landlordNickname;
    private UUID landlordId;
    private Instant createdAt;

    public static HouseResponse from(House house) {
        HouseResponse r = new HouseResponse();
        r.id = house.getId();
        r.title = house.getTitle();
        r.housingType = house.getHousingType();
        r.location = house.getLocation();
        r.depositKrw = house.getDepositKrw();
        r.monthlyRentKrw = house.getMonthlyRentKrw();
        r.maintenanceFeeKrw = house.getMaintenanceFeeKrw();
        r.floorLevel = house.getFloorLevel();
        r.description = house.getDescription();
        r.contactPhone = house.getContactPhone();
        r.landlordNickname = house.getLandlord().getNickname();
        r.landlordId = house.getLandlord().getId();
        r.createdAt = house.getCreatedAt();
        return r;
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getHousingType() {
        return housingType;
    }

    public String getLocation() {
        return location;
    }

    public Long getDepositKrw() {
        return depositKrw;
    }

    public Integer getMonthlyRentKrw() {
        return monthlyRentKrw;
    }

    public Integer getMaintenanceFeeKrw() {
        return maintenanceFeeKrw;
    }

    public String getFloorLevel() {
        return floorLevel;
    }

    public String getDescription() {
        return description;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public String getLandlordNickname() {
        return landlordNickname;
    }

    public UUID getLandlordId() {
        return landlordId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
