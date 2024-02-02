package com.tamerlan.todolist.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.tamerlan.todolist.view.Note;
import com.tamerlan.todolist.model.NoteDatabase;
import com.tamerlan.todolist.model.NotesDao;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AddNoteViewModel extends AndroidViewModel {

    NotesDao notesDao;

    CompositeDisposable compositeDisposable = new CompositeDisposable();


    private MutableLiveData<Boolean> shouldCloseScreen = new MutableLiveData<>();
    private MutableLiveData<Boolean> shouldBeVisible = new MutableLiveData<>();

    public AddNoteViewModel(@NonNull Application application) {
        super(application);
        notesDao = NoteDatabase.getInstance(application).notesDao();
    }

    public LiveData<Boolean> getShouldCloseScreen() {
        return shouldCloseScreen;
    }
    public LiveData<Boolean> getVisibility() {
        return shouldBeVisible;
    }

    public void makeVisible(){
        shouldBeVisible.setValue(true);
    }

    public void saveNote(Note note) {
        Disposable disposable = notesDao.add(note)
                .delay(5, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Throwable {
                        Log.d("AddNoteViewModel","subscribe");
                        shouldCloseScreen.setValue(true);
                    }
                });
        compositeDisposable.add(disposable);

    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}
