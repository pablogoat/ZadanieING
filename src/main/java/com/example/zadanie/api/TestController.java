package com.example.zadanie.api;

import com.example.zadanie.dao.NewDataDAO;
import com.example.zadanie.models.OneDayData;
import com.example.zadanie.service.FetchingDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TestController {

    private final FetchingDataService fetchingDataService;

    @GetMapping(path = "/customer/{customer}")
    public ResponseEntity<List<OneDayData>> hello(
            @PathVariable("customer") String customerId
    ) {
        try {
            fetchingDataService.fetchNewData();
            return ResponseEntity.ok(fetchingDataService.getCustomerData(customerId,30));
        }
        catch (IOException e){
            e.printStackTrace();
        }

        return ResponseEntity.ok(List.of());
    }
}
