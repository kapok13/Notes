package com.vb.notes.data;

import android.util.Log;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class NoteRepository {

    NoteDao mNoteDao;
    Flowable<List<Note>> noteList;

    public NoteRepository() {
        mNoteDao = App.getInstance().getDatabase().getNoteDao();
        noteList = mNoteDao.selectAllNotes();
    }

    public Flowable<List<Note>> getNoteList(){
        return noteList;
    }

    public void insert(Note note){
        Single<Note> inNote = Single.just(note);
        inNote.subscribeOn(Schedulers.io())
            .subscribe(new DisposableSingleObserver<Note>() {
                @Override
                public void onSuccess(Note note) {
                    mNoteDao.insertNote(note);
                }

                @Override
                public void onError(Throwable e) {
                    Log.d(getClass().getSimpleName(), "Error with insert");
                }
            });
    }

    public void deleteItem(Note note){
        Single<Note> delNote = Single.just(note);
        delNote.subscribeOn(Schedulers.io())
                .subscribe(new DisposableSingleObserver<Note>() {
                    @Override
                    public void onSuccess(Note note) {
                        mNoteDao.deleteNote(note);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(getClass().getSimpleName(), "Error with delete");
                    }
                });
    }

    public void update(Note note){
        Single<Note> updNote = Single.just(note);
        updNote.subscribeOn(Schedulers.io())
                .subscribe(new DisposableSingleObserver<Note>() {
                    @Override
                    public void onSuccess(Note note) {
                        mNoteDao.updateNote(note);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(getClass().getSimpleName(), "Error with updating");
                    }
                });
    }

}
