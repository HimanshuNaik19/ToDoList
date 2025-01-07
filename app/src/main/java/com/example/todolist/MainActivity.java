package com.example.todolist;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TaskDatabase database;
    private RecyclerView recyclerView;
    private TaskAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = TaskDatabase.getInstance(this);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> openAddTaskDialog());

        loadTasks();
    }

    private void loadTasks() {
        List<Task> tasks = database.taskDao().getAllTasks();
        adapter = new TaskAdapter(tasks, task -> {
            // Add code to handle update/delete
        });
        recyclerView.setAdapter(adapter);
    }

    private void openAddTaskDialog() {
        AddTaskDialog dialog = new AddTaskDialog(task -> {
            database.taskDao().insert(task);
            loadTasks();
        });
        dialog.show(getSupportFragmentManager(), "Add Task");
    }
}
