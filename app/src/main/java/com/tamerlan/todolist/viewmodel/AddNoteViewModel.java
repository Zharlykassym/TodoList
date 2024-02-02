package com.tamerlan.todolist.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.tamerlan.todolist.view.Note;
import com.tamerlan.todolist.model.NoteDatabase;
import com.tamerlan.todolist.model.NotesDao;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AddNoteViewModel extends AndroidViewModel {

    NotesDao notesDao;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    private MutableLiveData<Boolean> shouldCloseAddNoteTab = new MutableLiveData<>();
    private MutableLiveData<Boolean> addNoteShouldBeVisible = new MutableLiveData<>();

    /* Singleton initialization */
    public AddNoteViewModel(@NonNull Application application) {
        super(application);
        notesDao = NoteDatabase.getInstance(application).notesDao();
    }
    /* creation of LiveData to call them from View layer*/
    public LiveData<Boolean> getShouldCloseAddNoteTab() {
        return shouldCloseAddNoteTab;
    }

    public void saveNote(Note note) {
        Disposable disposable = saveNoteRx(note) // Completable add()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Throwable {

                        Log.d("AddNoteViewModel","subscribe");
                        shouldCloseAddNoteTab.setValue(true);
                    }
                });
        compositeDisposable.add(disposable);
    }

    /* creation of LiveData to call them from View layer*/
    public LiveData<Boolean> getAddNoteVisibility() {
        return addNoteShouldBeVisible;
    }
    public void makeVisible(){
        addNoteShouldBeVisible.setValue(true);
    }

    /* Converting add() to Completable to start RxJava thread */
    private Completable saveNoteRx(Note note){
        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Throwable {
                notesDao.add(note);
            }
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}
