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
//        recyclerViewNotes.setLayoutManager(new LinearLayoutManager(this));

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
        recyclerViewNotes.removeAllViews();
        for (Note note : database.getNotes()
        ) {
            View view = getLayoutInflater().inflate(
                    R.layout.note_item,
                    recyclerViewNotes,
                    false
            );
            /* При помощи getLayoutInflater и метода inflate мы из макета note_item создадим View элемент ViewGroup. Поскольку он им не был и был xml файлом в корне.*/

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    database.remove(note.getId());
                    showNotes();
                }
            });

            TextView textViewNote = view.findViewById(R.id.textViewNote);
            textViewNote.setText(note.getText());

            int colorResId; // здесь мы получаем id цвета
            switch (note.getPriority()) {
                case 0:
                    colorResId = R.drawable.rounded_blue_background;
                    break;
                case 1:
                    colorResId = R.drawable.rounded_yellow_background;
                    break;
                default:
                    colorResId = R.drawable.rounded_red_background;
            }
            Drawable color = ContextCompat.getDrawable(this, colorResId); // здесь получаем сам цвет из id
            textViewNote.setBackground(color);

            recyclerViewNotes.addView(view);

        }
    }
}