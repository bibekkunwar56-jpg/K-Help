package com.example.demo.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreateHouseRequest {

    @NotBlank
    @Size(min = 3, max = 200)
    private String title;

    private String housingType = "ONE_ROOM";

    @NotBlank
    @Size(min = 2, max = 150)
    private String location;

    @NotNull
    @Min(0)
    private Long depositKrw;

    @NotNull
    @Min(0)
    private Integer monthlyRentKrw;

    @Min(0)
    private Integer maintenanceFeeKrw = 0;

    @Size(max = 30)
    private String floorLevel;

    @NotBlank
    @Size(min = 10, max = 10000)
    private String description;

    @Size(max = 50)
    private String contactPhone;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHousingType() {
        return housingType;
    }

    public void setHousingType(String housingType) {
        this.housingType = housingType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Long getDepositKrw() {
        return depositKrw;
    }

    public void setDepositKrw(Long depositKrw) {
        this.depositKrw = depositKrw;
    }

    public Integer getMonthlyRentKrw() {
        return monthlyRentKrw;
    }

    public void setMonthlyRentKrw(Integer monthlyRentKrw) {
        this.monthlyRentKrw = monthlyRentKrw;
    }

    public Integer getMaintenanceFeeKrw() {
        return maintenanceFeeKrw;
    }

    public void setMaintenanceFeeKrw(Integer maintenanceFeeKrw) {
        this.maintenanceFeeKrw = maintenanceFeeKrw;
    }

    public String getFloorLevel() {
        return floorLevel;
    }

    public void setFloorLevel(String floorLevel) {
        this.floorLevel = floorLevel;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }
}
