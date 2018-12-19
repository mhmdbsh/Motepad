package com.example.mhmd.motepad.Model;

import com.orm.SugarRecord;

public class Note extends SugarRecord {

    public Note(String title, String note, String date) {
        this.title = title;
        this.note = note;
        this.date = date;
    }

    private String title;
    private String note;
    private String date;

    public Note() {
    }

    public Note(String title, String note) {
        this.title = title;
        this.note = note;
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
