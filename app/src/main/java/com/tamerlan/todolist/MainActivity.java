package com.tamerlan.todolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewNotes;
    private FloatingActionButton buttonAddNote;
    private NotesAdapter notesAdapter;
    private Database database = Database.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        notesAdapter = new NotesAdapter();
        recyclerViewNotes.setAdapter(notesAdapter);
//        recyclerViewNotes.setLayoutManager(new LinearLayoutManager(this)); // можно настроить через xml

        buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = AddNoteActivity.newIntent(MainActivity.this);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        showNotes();
    }

    private void initViews() {
        recyclerViewNotes = findViewById(R.id.recyclerViewNotes);
        buttonAddNote = findViewById(R.id.buttonAddNote);
    }

    private void showNotes() {
        notesAdapter.setNotes(database.getNotes());
    }
}