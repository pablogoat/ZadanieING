package com.example.zadanie.dto;

import com.example.zadanie.models.Customer;
import com.example.zadanie.models.NoteType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class NoteDTO {
    protected NoteType type;
    protected String title;
    protected String content;
}
