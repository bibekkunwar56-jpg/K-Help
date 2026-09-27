package com.example.demo.controller;

import com.example.demo.dto.CreateHouseRequest;
import com.example.demo.dto.HouseResponse;
import com.example.demo.security.CurrentUser;
import com.example.demo.service.HouseService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/housing")
public class HouseController {

    private final HouseService houseService;

    public HouseController(HouseService houseService) {
        this.houseService = houseService;
    }

    @GetMapping
    public List<HouseResponse> list(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Integer maxRent,
            @RequestParam(required = false) Long maxDeposit) {
        return houseService.list(location, maxRent, maxDeposit);
    }

    @GetMapping("/{id}")
    public HouseResponse get(@PathVariable UUID id) {
        return houseService.get(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public HouseResponse create(@Valid @RequestBody CreateHouseRequest req, Authentication authentication) {
        return houseService.create(CurrentUser.required(authentication), req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id, Authentication authentication) {
        houseService.delete(id, CurrentUser.required(authentication));
    }
}
