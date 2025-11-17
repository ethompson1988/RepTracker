package com.example.views;

import com.example.WorkoutEntry;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Route("")   // Landing page
public class HomeView extends VerticalLayout {

    private final TextField exerciseField = new TextField("Exercise");
    private final IntegerField setsField = new IntegerField("Number of sets");

    // Holds dynamically created rows: [Reps field] [Weight field]
    private final VerticalLayout setsLayout = new VerticalLayout();

    private final Button addButton = new Button("Add Workout");

    // In-memory history
    private final List<WorkoutEntry> entries = new ArrayList<>();

    // Grid to view history
    private final Grid<WorkoutEntry> grid = new Grid<>(WorkoutEntry.class, false);

    public HomeView() {

        exerciseField.setPlaceholder("e.g. Bench Press");
        exerciseField.setClearButtonVisible(true);

        setsField.setMin(1);
        setsField.setMax(20);
        setsField.setPlaceholder("0");

        // When the number of sets changes → regenerate inputs
        setsField.addValueChangeListener(event -> updateSetInputs());

        // Layout for form
        HorizontalLayout formLayout = new HorizontalLayout(exerciseField, setsField);
        add(formLayout);
        add(setsLayout);
        add(addButton);

        // Configure grid (history table)
        grid.addColumn(WorkoutEntry::getDate).setHeader("Date");
        grid.addColumn(WorkoutEntry::getExercise).setHeader("Exercise");
        grid.addColumn(entry ->
                entry.getRepsPerSet() == null ? "" : entry.getRepsPerSet().toString()
        ).setHeader("Reps per set");
        grid.addColumn(entry ->
                entry.getWeightsPerSet() == null ? "" : entry.getWeightsPerSet().toString()
        ).setHeader("Weight per set (lb)");

        grid.setItems(entries);
        grid.setWidthFull();
        add(grid);

        addButton.addClickListener(e -> submitWorkout());
    }

    private void updateSetInputs() {

        setsLayout.removeAll(); // clear existing rows

        Integer count = setsField.getValue();
        if (count == null || count <= 0) {
            return;
        }

        // For each set: [Reps field] [Weight field]
        for (int i = 1; i <= count; i++) {
            IntegerField repsField = new IntegerField("Reps Set " + i);
            repsField.setMin(1);
            repsField.setMax(100);

            IntegerField weightField = new IntegerField("Weight Set " + i + " (lb)");
            weightField.setMin(0);
            weightField.setMax(1000);

            HorizontalLayout row = new HorizontalLayout(repsField, weightField);
            row.setDefaultVerticalComponentAlignment(Alignment.END);

            setsLayout.add(row);
        }
    }

    private void submitWorkout() {

        String exercise = exerciseField.getValue();
        if (exercise == null || exercise.isBlank()) {
            Notification.show("Exercise name required.");
            return;
        }

        Integer count = setsField.getValue();
        if (count == null || count <= 0) {
            Notification.show("Enter number of sets.");
            return;
        }

        List<Integer> repsPerSet = new ArrayList<>();
        List<Integer> weightsPerSet = new ArrayList<>();

        // For each row (set): grab reps + weight
        for (Component rowComponent : setsLayout.getChildren().toList()) {
            if (rowComponent instanceof HorizontalLayout row) {
                // Assuming exactly two components: [0] reps, [1] weight
                Component c0 = row.getComponentAt(0);
                Component c1 = row.getComponentAt(1);

                if (!(c0 instanceof IntegerField repsField) ||
                        !(c1 instanceof IntegerField weightField)) {
                    continue; // should not happen, but safe guard
                }

                if (repsField.isEmpty() || weightField.isEmpty()) {
                    Notification.show("Fill out all reps and weight fields.");
                    return;
                }

                repsPerSet.add(repsField.getValue());
                weightsPerSet.add(weightField.getValue());
            }
        }

        // Create and store entry
        WorkoutEntry entry = new WorkoutEntry(
                LocalDate.now(),
                exercise,
                repsPerSet,
                weightsPerSet
        );
        entries.add(entry);

        // Refresh grid
        grid.getDataProvider().refreshAll();

        // Optional: toast
        Notification.show("Logged: " + exercise);

        // Clear form
        exerciseField.clear();
        setsField.clear();
        setsLayout.removeAll();
    }
}
