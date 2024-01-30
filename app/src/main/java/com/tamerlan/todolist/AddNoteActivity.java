package com.tamerlan.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class AddNoteActivity extends AppCompatActivity {
    private EditText editTextInputNote;
    private RadioGroup radioGroupPriority;
    private RadioButton radioButtonLow;
    private RadioButton radioButtonMedium;
    private RadioButton radioButtonHigh;
    private Button buttonSave;
    private Handler handler = new Handler(Looper.getMainLooper());

    //        private Database database = Database.getInstance();
    private NoteDatabase noteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        noteDatabase = NoteDatabase.getInstance(getApplication());
        initViews();
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();
            }
        });


    }

    private void initViews() {
        editTextInputNote = findViewById(R.id.editTextInputNote);
        radioGroupPriority = findViewById(R.id.radioGroupPriority);
        radioButtonLow = findViewById(R.id.radioButtonLow);
        radioButtonMedium = findViewById(R.id.radioButtonMedium);
        radioButtonHigh = findViewById(R.id.radioButtonHigh);
        buttonSave = findViewById(R.id.buttonSave);
    }

    private void saveNote() {
        String text;
        if (editTextInputNote.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, R.string.error_field_empty, Toast.LENGTH_SHORT).show();
        } else {
            text = editTextInputNote.getText().toString().trim();
            Toast.makeText(this, R.string.success, Toast.LENGTH_SHORT).show();
            int priority = getPriority();
//            int id = database.getNotes().size(); // временное добавление id для теста виртуальной базы данных для теста приложения
            Note note = new Note(text, priority);
//            database.add(note);
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    noteDatabase.notesDao().add(note);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    });
                }
            });
            thread.start();



        }

    }

    private int getPriority() {
        int priority;
        if (radioButtonLow.isChecked()) {
            priority = 0;
        } else if (radioButtonMedium.isChecked()) {
            priority = 1;
        } else {
            priority = 2;
        }
        return priority;
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, AddNoteActivity.class);
    }

}