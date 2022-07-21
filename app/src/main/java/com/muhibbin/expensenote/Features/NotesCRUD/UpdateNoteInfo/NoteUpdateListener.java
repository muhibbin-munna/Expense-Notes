package com.muhibbin.expensenote.Features.NotesCRUD.UpdateNoteInfo;

import com.muhibbin.expensenote.Features.NotesCRUD.CreateNote.Note;

public interface NoteUpdateListener {
    void onNoteInfoUpdated(Note note, int position);
}
