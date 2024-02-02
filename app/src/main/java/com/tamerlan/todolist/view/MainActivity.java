package com.tamerlan.todolist.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tamerlan.todolist.R;
import com.tamerlan.todolist.viewmodel.AddNoteViewModel;
import com.tamerlan.todolist.viewmodel.MainViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerViewNotes;
    private FloatingActionButton buttonAddNote;
    private NotesAdapter notesAdapter;
    private MainViewModel viewModel;

    private ConstraintLayout constraintLayoutAddNote;
    private EditText editTextInputNote;
    private RadioGroup radioGroupPriority;
    private RadioButton radioButtonLow;
    private RadioButton radioButtonMedium;
    private Button buttonSave;
    private AddNoteViewModel addNoteViewModel;

    /* starting point of application*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        /* ViewModels initialization */
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        addNoteViewModel = new ViewModelProvider(this).get(AddNoteViewModel.class);

        viewModel.getNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                notesAdapter.setNotes(notes);
            }
        });

        notesAdapter = new NotesAdapter();
        recyclerViewNotes.setAdapter(notesAdapter);

        /* Swipe realization */
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(
                        0,
                        ItemTouchHelper.LEFT
                ) {
                    @Override
                    public boolean onMove(
                            @NonNull RecyclerView recyclerView,
                            @NonNull RecyclerView.ViewHolder viewHolder,
                            @NonNull RecyclerView.ViewHolder target
                    ) {
                        return false;
                    }

                    @Override
                    public void onSwiped(
                            @NonNull RecyclerView.ViewHolder viewHolder,
                            int direction
                    ) {
                        int position = viewHolder.getAdapterPosition();
                        Note note = notesAdapter.getNotes().get(position);
                        viewModel.remove(note);
                    }
                });
        itemTouchHelper.attachToRecyclerView(recyclerViewNotes);

        /* Work with buttons. (onClickListeners) */
        buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNoteViewModel.makeVisible();

//                Intent intent = AddNoteActivity.newIntent(MainActivity.this);
//                startActivity(intent);
            }
        });

        /*not used onClickListener*/
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();
            }
        });

        /*Work with ViewModels*/
        addNoteViewModel.getAddNoteVisibility().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean addNoteShouldBeVisible) {
                if (addNoteShouldBeVisible){
                    if (constraintLayoutAddNote.getVisibility() == View.GONE) {
                        constraintLayoutAddNote.setVisibility(View.VISIBLE);
                    } else {
                        constraintLayoutAddNote.setVisibility(View.GONE);
                    }
                }
            }
        });
        addNoteViewModel.getShouldCloseAddNoteTab().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean shouldClose) {
                if (shouldClose){
//                    finish(); // used in case when we use Intent and is needed to close AddNoteActivity
                    viewModel.refreshData();
                    editTextInputNote.setText(null);
                    radioButtonLow.setChecked(true);
                }

            }
        });

        /* additional feature. Used to change color of addNote field */
        radioGroupPriority.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int colorResId; // здесь мы получаем id цвета
                switch (getPriority()) {
                    case 0:
                        colorResId = R.drawable.rounded_blue_back_addnote;
                        break;
                    case 1:
                        colorResId = R.drawable.rounded_yellow_back_addnote;
                        break;
                    default:
                        colorResId = R.drawable.rounded_red_back_addnote;
                }
                Drawable color = ContextCompat.getDrawable(constraintLayoutAddNote.getContext(), colorResId); // здесь получаем сам цвет из id
                constraintLayoutAddNote.setBackground(color);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.refreshData();

    }

    private void saveNote() {
        String text;
        if (editTextInputNote.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, R.string.error_field_empty, Toast.LENGTH_SHORT).show();
        } else {
            text = editTextInputNote.getText().toString().trim();
            Toast.makeText(this, R.string.success, Toast.LENGTH_SHORT).show();
            int priority = getPriority();
            Note note = new Note(text, priority);
            addNoteViewModel.saveNote(note);
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

    /* Initialization of Views */
    private void initViews() {
        recyclerViewNotes = findViewById(R.id.recyclerViewNotes);
        buttonAddNote = findViewById(R.id.buttonAddNote);
        constraintLayoutAddNote = findViewById(R.id.constraintLayoutAddNote);

        editTextInputNote = findViewById(R.id.editTextInputNote);
        radioGroupPriority = findViewById(R.id.radioGroupPriority);
        radioButtonLow = findViewById(R.id.radioButtonLow);
        radioButtonMedium = findViewById(R.id.radioButtonMedium);
        buttonSave = findViewById(R.id.buttonSave);
    }
}