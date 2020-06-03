package com.vb.notes.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "note_table")
public class Note implements Serializable{

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "note_title")
    private  String title;

    @ColumnInfo(name = "note_description")
    private String note;

    @ColumnInfo(name = "note_time")
    private String time;

    @ColumnInfo(name = "note_date")
    private String date;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
