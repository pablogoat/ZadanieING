package com.example.zadanie.api;

import com.example.zadanie.dao.NewDataDAO;
import com.example.zadanie.service.FetchingDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TestController {

    private final FetchingDataService fetchingDataService;

    @GetMapping
    public ResponseEntity<List<NewDataDAO>> hello() {
        try {
            return ResponseEntity.ok(fetchingDataService.fetchNewData());
        }
        catch (IOException e){
            e.printStackTrace();
        }

        return ResponseEntity.ok(List.of());
    }
}
