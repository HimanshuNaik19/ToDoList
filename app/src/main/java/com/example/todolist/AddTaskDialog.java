package com.example.todolist;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class AddTaskDialog extends DialogFragment {

    private EditText editTaskName, editTaskDescription;
    private Button btnPickDate, btnPickTime, btnCancel, btnSave;

    private String selectedDate = "";
    private String selectedTime = "";

    private final OnTaskAddedListener listener;

    public interface OnTaskAddedListener {
        void onTaskAdded(Task task);
    }

    public AddTaskDialog(OnTaskAddedListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_add_task, container, false);

        editTaskName = view.findViewById(R.id.editTaskName);
        editTaskDescription = view.findViewById(R.id.editTaskDescription);
        btnPickDate = view.findViewById(R.id.btnPickDate);
        btnPickTime = view.findViewById(R.id.btnPickTime);
        btnCancel = view.findViewById(R.id.btnCancel);
        btnSave = view.findViewById(R.id.btnSave);

        btnPickDate.setOnClickListener(v -> showDatePicker());
        btnPickTime.setOnClickListener(v -> showTimePicker());

        btnCancel.setOnClickListener(v -> dismiss());

        btnSave.setOnClickListener(v -> saveTask());

        return view;
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    btnPickDate.setText(selectedDate);
                },
                year, month, day
        );

        datePickerDialog.show();
    }

    private void showTimePicker() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                requireContext(),
                (view, selectedHour, selectedMinute) -> {
                    selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute);
                    btnPickTime.setText(selectedTime);
                },
                hour, minute, true
        );

        timePickerDialog.show();
    }

    private void saveTask() {
        String taskName = editTaskName.getText().toString().trim();
        String taskDescription = editTaskDescription.getText().toString().trim();

        if (TextUtils.isEmpty(taskName)) {
            Toast.makeText(getContext(), "Task name is required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(selectedDate) || TextUtils.isEmpty(selectedTime)) {
            Toast.makeText(getContext(), "Please select both date and time", Toast.LENGTH_SHORT).show();
            return;
        }

        Task newTask = new Task(taskName, taskDescription, selectedDate, selectedTime);
        listener.onTaskAdded(newTask);
        dismiss();
    }
}
