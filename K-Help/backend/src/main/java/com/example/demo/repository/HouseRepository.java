package com.example.demo.repository;

import com.example.demo.entity.House;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface HouseRepository extends JpaRepository<House, UUID> {

    @Query("""
            SELECT h FROM House h
            JOIN FETCH h.landlord
            WHERE h.available = true
            ORDER BY h.createdAt DESC
            """)
    List<House> findAllAvailableWithLandlord();

    @Query("""
            SELECT h FROM House h
            JOIN FETCH h.landlord
            WHERE h.available = true
              AND LOWER(h.location) LIKE LOWER(CONCAT('%', :location, '%'))
            ORDER BY h.createdAt DESC
            """)
    List<House> findByLocation(@Param("location") String location);

    @Query("""
            SELECT h FROM House h
            JOIN FETCH h.landlord
            WHERE h.available = true
              AND h.monthlyRentKrw <= :maxRent
              AND h.depositKrw <= :maxDeposit
            ORDER BY h.createdAt DESC
            """)
    List<House> findByBudget(@Param("maxRent") Integer maxRent, @Param("maxDeposit") Long maxDeposit);

    @Query("""
            SELECT h FROM House h
            JOIN FETCH h.landlord
            WHERE h.available = true
              AND LOWER(h.location) LIKE LOWER(CONCAT('%', :location, '%'))
              AND h.monthlyRentKrw <= :maxRent
              AND h.depositKrw <= :maxDeposit
            ORDER BY h.createdAt DESC
            """)
    List<House> findByLocationAndBudget(
            @Param("location") String location,
            @Param("maxRent") Integer maxRent,
            @Param("maxDeposit") Long maxDeposit);
}
