package com.example.zadanie.service;

import com.example.zadanie.dto.NoteDTO;
import com.example.zadanie.dto.ResponseNoteDTO;
import com.example.zadanie.models.Customer;
import com.example.zadanie.models.Note;
import com.example.zadanie.repository.OneDayDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotesService {
    private final OneDayDataRepository oneDayDataRepository;

    public String addNote(String customerId, NoteDTO note){
        Optional<Customer> customer = oneDayDataRepository.getCustomer(customerId);

        if (customer.isEmpty()){
            return "Customer does not exist";
        }

        var newNote = Note.builder()
                .type(note.getType())
                .date(LocalDate.now())
                .customer(customer.get())
                .title(note.getTitle())
                .content(note.getContent())
                .build();
        oneDayDataRepository.saveNote(newNote);

        return "Note added successfully";
    }

    public List<ResponseNoteDTO> getNotes(String customerId, LocalDate since, LocalDate until) {
        return oneDayDataRepository.getNotes(customerId, since, until);
    }
}
