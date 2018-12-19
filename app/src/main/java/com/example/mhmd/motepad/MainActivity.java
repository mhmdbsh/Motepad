package com.example.mhmd.motepad;

import android.app.AlertDialog;
import android.app.assist.AssistContent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.example.mhmd.motepad.Adapter.NoteAdapter;
import com.example.mhmd.motepad.Model.Note;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.melnykov.fab.FloatingActionButton;
import com.orm.SugarContext;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.searchBar)
    MaterialSearchBar searchBar;
    @BindView(R.id.fab_add)
    FloatingActionButton add_btn;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private NoteAdapter noteAdapter;
    private List<Note> noteList;
    private List<Note> searchedList;
    private NoteAdapter searchedAdapter;
    private Context context;
    private boolean inSearch = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        context = this;

        setData();
        setRecyclerView();

        searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                if (enabled) {
                    inSearch = true;
                } else if (!enabled) {
                    recyclerView.setAdapter(noteAdapter);
                    searchedList = null;
                    inSearch = false;
                }
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                searchedList = new ArrayList<>();
                for (int i = 0; i < noteList.size(); i++) {
                    if (noteList.get(i).getTitle().contains(text)) {
                        searchedList.add(noteList.get(i));
                    }
                }

                searchedAdapter = new NoteAdapter(searchedList, context);
                recyclerView.setAdapter(searchedAdapter);

            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NoteActivity.class);
                startActivity(intent);
            }
        });
        add_btn.attachToRecyclerView(recyclerView);


    }


    private void setData() {
        SugarContext.init(context);
        noteList = Note.listAll(Note.class);
        SugarContext.terminate();
    }

    private void setRecyclerView() {
        noteAdapter = new NoteAdapter(noteList, this);
        noteAdapter.notifyDataSetChanged();
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(noteAdapter);
    }

    private void deleteItem(int itemIndex) {
        SugarContext.init(context);
        Note note = Note.findById(Note.class, noteList.get(itemIndex).getId());
        note.delete();
        noteList.remove(itemIndex);
        noteAdapter.notifyDataSetChanged();
        SugarContext.terminate();
    }

    @Subscribe
    public void onMessageEvent(MessageEvent event) {

        if (event.message.equals("deleteItem")) {
            deleteItem(event.itemIndex);
        } else if (event.message.equals("openItem")) {
            Long itemIndex = 0L;
            if (!inSearch) {
                itemIndex = noteList.get(event.itemIndex).getId();
            } else {
                itemIndex = searchedList.get(event.itemIndex).getId();
            }

            Intent intent = new Intent(MainActivity.this, NoteActivity.class);
            intent.putExtra("itemIndex", itemIndex);
            startActivity(intent);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        SugarContext.init(context);
        setData();
        setRecyclerView();
        if (searchBar.isSearchEnabled()) {
            recyclerView.setAdapter(searchedAdapter);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        SugarContext.init(this);
        setData();
        setRecyclerView();
        EventBus.getDefault().register(this);

    }

    @Override
    protected void onStop() {
        super.onStop();
        SugarContext.terminate();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SugarContext.terminate();
    }

    @Override
    public void onProvideAssistContent(AssistContent outContent) {
        super.onProvideAssistContent(outContent);
        SugarContext.terminate();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }
}
