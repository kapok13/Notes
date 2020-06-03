package com.vb.notes.data;

import android.app.Application;

import androidx.room.Room;

/*
    Singleton бд
 */
public class App extends Application {

    public static App instance;
    private NoteDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        database = Room.databaseBuilder(this,
                NoteDatabase.class,
                "note database").build();
    }

    public static App getInstance() {
        return instance;
    }

    public NoteDatabase getDatabase() {
        return database;
    }
}
