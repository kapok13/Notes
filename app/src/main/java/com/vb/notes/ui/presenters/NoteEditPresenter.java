package com.vb.notes.ui.presenters;

import com.vb.notes.data.Note;
import com.vb.notes.data.NoteRepository;
import com.vb.notes.ui.fragments.INoteEditFragment;

public class NoteEditPresenter {

    private INoteEditFragment nMoteEdit;
    private NoteRepository mNoteRep;

    public NoteEditPresenter(INoteEditFragment nMoteEdit, NoteRepository mNoteRep) {
        this.nMoteEdit = nMoteEdit;
        this.mNoteRep = mNoteRep;
    }

    public void updNote(Note note){
        mNoteRep.update(note);
    }
}
