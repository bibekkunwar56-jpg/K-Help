package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "houses")
public class House {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "landlord_id", nullable = false)
    private User landlord;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(name = "housing_type", nullable = false, length = 50)
    private String housingType = "ONE_ROOM";

    @Column(nullable = false, length = 150)
    private String location;

    @Column(name = "deposit_krw", nullable = false)
    private Long depositKrw;

    @Column(name = "monthly_rent_krw", nullable = false)
    private Integer monthlyRentKrw;

    @Column(name = "maintenance_fee_krw")
    private Integer maintenanceFeeKrw = 0;

    @Column(name = "floor_level", length = 30)
    private String floorLevel;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "contact_phone", length = 50)
    private String contactPhone;

    @Column(name = "is_available", nullable = false)
    private boolean available = true;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @PrePersist
    void onCreate() {
        Instant now = Instant.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public User getLandlord() {
        return landlord;
    }

    public void setLandlord(User landlord) {
        this.landlord = landlord;
    }

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

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
