package com.example.zadanie.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "notes")
public class Note {
    @Id
    @GeneratedValue
    private Integer id;
    private NoteType type;
    private LocalDate date;
    @ManyToOne
    @JoinColumn(name = "CLIENT_ID", referencedColumnName = "id", nullable = false)
    @JsonIgnore
    private Customer customer;
    private String title;
    private String content;
}
