package com.example.zadanie.api;

import com.example.zadanie.dto.NoteDTO;
import com.example.zadanie.dto.ResponseData;
import com.example.zadanie.dto.ResponseNoteDTO;
import com.example.zadanie.service.FetchingDataService;
import com.example.zadanie.service.NotesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class DataNotesController {

    private final FetchingDataService fetchingDataService;
    private final NotesService notesService;

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

    @PostMapping(path = "/customer/{customer}/note")
    public ResponseEntity<String> addNote(
            @PathVariable("customer") String customerId,
            @NonNull @RequestBody NoteDTO note){
        return ResponseEntity.ok(notesService.addNote(customerId, note));
    }

    @GetMapping(path = "/customer/{customer}/note")
    public ResponseEntity<List<ResponseNoteDTO>> getNotes(
            @PathVariable("customer") String customerId,
            @RequestParam Optional<LocalDate> since,
            @RequestParam Optional<LocalDate> until
    ){
        if(since.isEmpty() || until.isEmpty()){
            return ResponseEntity.ok(List.of());
        }

        return ResponseEntity.ok(notesService.getNotes(customerId, since.get(), until.get()));
    }
}
