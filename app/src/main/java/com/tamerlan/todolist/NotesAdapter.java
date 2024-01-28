package com.tamerlan.todolist;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> {

    private ArrayList<Note> notes = new ArrayList<>();

    public void setNotes(ArrayList<Note> notes) {
        this.notes = notes;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.note_item,
                parent,
                false
        );
        return new NotesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NotesViewHolder viewHolder, int position) {
        Note note = notes.get(position);
        viewHolder.textViewNote.setText(note.getText());

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
        Drawable color = ContextCompat.getDrawable(viewHolder.itemView.getContext(), colorResId); // здесь получаем сам цвет из id
        viewHolder.textViewNote.setBackground(color);

    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    class NotesViewHolder extends RecyclerView.ViewHolder{
        private TextView textViewNote;
        public NotesViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNote = itemView.findViewById(R.id.textViewNote);
        }
    }
}
