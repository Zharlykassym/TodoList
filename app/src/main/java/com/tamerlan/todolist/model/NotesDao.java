package com.tamerlan.todolist.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.tamerlan.todolist.view.Note;

import java.util.List;

@Dao
public interface NotesDao { //DataAccessObject
    @Query("SELECT * FROM notes ORDER BY id DESC")
    LiveData<List<Note>> getNotes();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void add(Note note);

    @Query("DELETE FROM notes WHERE id = :id")
    void remove(int id);
}
