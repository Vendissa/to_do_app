package com.example.first;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import androidx.appcompat.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import com.google.android.material.button.MaterialButton;

import com.example.first.Adapter.ToDoAdapter;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.example.first.Model.ToDoModel;
import com.example.first.Utils.DatabaseHandler;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
public class MainActivity extends AppCompatActivity implements DialogCloseListener{

    private DatabaseHandler db;
    private RecyclerView tasksRecyclerView;
    private ToDoAdapter tasksAdapter;
    //private FloatingActionButton fab,fab_ote;
    private ExtendedFloatingActionButton fab,fab_clearAll;
    private List<ToDoModel> taskList;


    @NonNull
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            Objects.requireNonNull(getSupportActionBar()).hide();
        }catch (NullPointerException e){
            e.printStackTrace();
        }


        db = new DatabaseHandler(this);
        db.openDatabase();

        tasksRecyclerView = findViewById(R.id.tasksRecyclerView);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        tasksAdapter = new ToDoAdapter(db,MainActivity.this);
        tasksRecyclerView.setAdapter(tasksAdapter);

        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new RecyclerItemTouchHelper(tasksAdapter));
        itemTouchHelper.attachToRecyclerView(tasksRecyclerView);


        fab = findViewById(R.id.fab);
        fab_clearAll = findViewById(R.id.fab_note);


        taskList = db.getAllTasks();
        Collections.reverse(taskList);

        tasksAdapter.setTasks(taskList);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewTask.newInstance().show(getSupportFragmentManager(), AddNewTask.TAG);
            }
        });
        fab_clearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showClearAllConfirmationDialog();
            }
        });


        MaterialButton materialButton = findViewById(R.id.smileyBtn);

        try{
            materialButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, JokeActivity.class);
                    startActivity(intent);
                }
            });
        }catch (RuntimeException e){
            e.printStackTrace();
        }
        // Set click listener if needed
    }

    private void showClearAllConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Clear All Tasks");
        builder.setMessage("Are you sure you want to clear all tasks?");
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                clearAllTasks();
            }
        });
        builder.setNegativeButton(android.R.string.cancel, null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void clearAllTasks() {
        db.deleteAllTask();
        taskList.clear();
        tasksAdapter.notifyDataSetChanged();
    }
    @Override
    public void handleDialogClose(DialogInterface dialog){
        taskList = db.getAllTasks();
        Collections.reverse(taskList);
        tasksAdapter.setTasks(taskList);
        tasksAdapter.notifyDataSetChanged();
    }
}