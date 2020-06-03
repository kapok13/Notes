package com.vb.notes.ui.presenters;

import com.vb.notes.data.Note;
import com.vb.notes.data.NoteRepository;
import com.vb.notes.ui.fragments.INoteListFragment;

public class NoteListPresenter {

    private INoteListFragment mNoteFragment;
    private NoteRepository mNoteRep;

    public NoteListPresenter(INoteListFragment mNoteFragment, NoteRepository mNoteRep) {
        this.mNoteFragment = mNoteFragment;
        this.mNoteRep = mNoteRep;
    }

    public void setList(){
        mNoteFragment.setNotesList(mNoteRep.getNoteList());
    }

    public void deleteItem(Note note){
        mNoteRep.deleteItem(note);
    }
}
