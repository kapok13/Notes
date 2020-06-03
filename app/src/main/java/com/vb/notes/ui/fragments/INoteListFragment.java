package com.vb.notes.ui.fragments;

import com.vb.notes.data.Note;

import java.util.List;

import io.reactivex.Flowable;

public interface INoteListFragment {
    void setNotesList(Flowable<List<Note>> notesList);
    void deleteBySwipe(Note note);
}
