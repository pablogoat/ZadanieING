package com.example.zadanie.dto;

import com.example.zadanie.models.Note;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ResponseNoteDTO extends NoteDTO {
    private Integer id;

    public ResponseNoteDTO(Note note){
        id = note.getId();
        type = note.getType();
        title = note.getTitle();
        content = note.getContent();
    }
}
