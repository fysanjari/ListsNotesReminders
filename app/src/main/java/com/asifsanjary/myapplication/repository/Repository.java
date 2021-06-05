package com.asifsanjary.myapplication.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.asifsanjary.myapplication.repository.database.AppDatabase;
import com.asifsanjary.myapplication.repository.database.TodoDao;
import com.asifsanjary.myapplication.repository.database.entity.Note;
import com.asifsanjary.myapplication.repository.database.NoteDao;
import com.asifsanjary.myapplication.repository.database.entity.Todo;

import java.util.List;

public class Repository {
    private NoteDao noteDao;
    private TodoDao todoDao;
    private LiveData<List<Note>> noteList;
    private LiveData<List<Todo>> todoList;

    private Repository(Application application) {
        initDb(application);
    }

    private static Repository REPOSITORY_INSTANCE;

    public static Repository getRepositoryInstance(Application application) {
        if(REPOSITORY_INSTANCE == null) {
            synchronized (Repository.class) {
                if(REPOSITORY_INSTANCE == null) {
                    REPOSITORY_INSTANCE = new Repository(application);
                }
            }
        }
        return REPOSITORY_INSTANCE;
    }

    private void initDb(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        noteDao = db.noteDao();
        todoDao = db.todoDao();
        noteList = noteDao.getAllNotes();
        todoList = todoDao.getTodoList();
    }

    public LiveData<List<Todo>> getTodoList() {
        return todoList;
    }

    public void insertTodo(Todo todo) {
        AppDatabase.databaseWriteExecutor.execute(() -> todoDao.insertTodos(todo));
    }

    public void updateTodo(Todo todo) {
        AppDatabase.databaseWriteExecutor.execute(() -> todoDao.updateTodos(todo));
    }

    public LiveData<List<Note>> getNoteList() {
        return noteList;
    }

    public void insertNote(Note note) {
        AppDatabase.databaseWriteExecutor.execute(() -> noteDao.insertNotes(note));
    }

    public void updateNote(Note note) {
        AppDatabase.databaseWriteExecutor.execute(() -> noteDao.updateNotes(note));
    }
}
