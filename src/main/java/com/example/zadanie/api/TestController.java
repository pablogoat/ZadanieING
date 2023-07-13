package com.example.zadanie.api;

import com.example.zadanie.dto.ResponseData;
import com.example.zadanie.models.OneDayData;
import com.example.zadanie.service.FetchingDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TestController {

    private final FetchingDataService fetchingDataService;

    @GetMapping
    public ResponseEntity<String> fetch() {
        try {
            fetchingDataService.fetchNewData();
            return ResponseEntity.ok("Data fetched");
        }
        catch (IOException e){
            e.printStackTrace();
        }

        return ResponseEntity.ok("error fetching data");
    }

    @GetMapping(path = "/customer/{customer}")
    public ResponseEntity<Optional<ResponseData>> newestDataByCustomer(
            @PathVariable("customer") String customerId,
            @RequestParam Optional<LocalDate> date
            ) {
        return ResponseEntity.ok(fetchingDataService.getNewestDataByCustomer(customerId, date));
    }
}
