package com.example.zadanie.service;

import com.example.zadanie.dao.DAO;
import com.example.zadanie.dto.NoteDTO;
import com.example.zadanie.dto.ResponseNoteDTO;
import com.example.zadanie.models.Customer;
import com.example.zadanie.models.Note;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class NotesService {
    private final DAO dataAccessService;

    public NotesService(@Qualifier("postgres") DAO dataAccessService){
        this.dataAccessService = dataAccessService;
    }

    public String addNote(String customerId, NoteDTO note){
        Optional<Customer> customer = dataAccessService.getCustomer(customerId);

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
        dataAccessService.saveNote(newNote);

        return "Note added successfully";
    }

    public List<ResponseNoteDTO> getNotes(String customerId, LocalDate since, LocalDate until) {
        return dataAccessService.getNotes(customerId, since, until);
    }
}
