package com.example.mhmd.motepad;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mhmd.motepad.Model.Note;
import com.orm.SugarContext;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NoteActivity extends AppCompatActivity {
    @BindView(R.id.title_ed)
    EditText titleEd;
    @BindView(R.id.note_text_ed)
    EditText noteEd;
    @BindView(R.id.menu_save)
    ImageView saveIv;
    @BindView(R.id.menu_back)
    ImageView backIv;
    @BindView(R.id.time_area)
    TextView timeArea;


    private Context context;
    private Long itemIndex;
    private String dateString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        ButterKnife.bind(this);
        context = this;
        Intent intent = getIntent();
        itemIndex = intent.getLongExtra("itemIndex", -1);


        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat month_format = new SimpleDateFormat("MMM");
        String month = month_format.format(Calendar.getInstance().getTime());
        dateString = date.getDay() + " " + month + " " + date.getHours() + ":" + date.getMinutes();

        if (itemIndex != -1) {
            SugarContext.init(context);
            Note tempNote = Note.findById(Note.class, itemIndex);
            titleEd.setText(tempNote.getTitle());
            noteEd.setText(tempNote.getNote());
            timeArea.setText(tempNote.getDate());
            SugarContext.terminate();
        } else {
            timeArea.setText(dateString);
        }

        saveIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Note note;
                SugarContext.init(context);
                String tempTitle = titleEd.getText().toString();
                String tempNote = noteEd.getText().toString();

                if (itemIndex != -1) {
                    note = Note.findById(Note.class, itemIndex);
                    note.setTitle(tempTitle);
                    note.setNote(tempNote);
                    note.setDate(dateString);
                    note.save();
                    Toast.makeText(context, "Updated", Toast.LENGTH_SHORT).show();
                } else {
                    note = new Note(tempTitle, tempNote, dateString);
                    note.save();
                    Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();
                }
                SugarContext.terminate();
                finish();
            }
        });

        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

}
