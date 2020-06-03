package com.vb.notes.ui.presenters;

import com.vb.notes.data.Note;
import com.vb.notes.data.NoteRepository;
import com.vb.notes.ui.activities.IMainActivity;

public class MainActivityPresenter{

    private IMainActivity mainActivityView;
    private NoteRepository mNoteRep;

    public MainActivityPresenter(IMainActivity mainActivityView, NoteRepository mNoteRep) {
        this.mainActivityView = mainActivityView;
        this.mNoteRep = mNoteRep;
    }

    public void insertItem(Note note){
        mNoteRep.insert(note);
    }
}
