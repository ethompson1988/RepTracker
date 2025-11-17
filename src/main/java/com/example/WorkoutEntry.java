package com.example;

import java.time.LocalDate;
import java.util.List;

public class WorkoutEntry {

    private LocalDate date;
    private String exercise;
    private List<Integer> repsPerSet;
    private List<Integer> weightsPerSet;

    public WorkoutEntry() {}

    public WorkoutEntry(LocalDate date, String exercise,
                        List<Integer> repsPerSet,
                        List<Integer> weightsPerSet) {
        this.date = date;
        this.exercise = exercise;
        this.repsPerSet = repsPerSet;
        this.weightsPerSet = weightsPerSet;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getExercise() {
        return exercise;
    }

    public List<Integer> getRepsPerSet() {
        return repsPerSet;
    }

    public void setRepsPerSet(List<Integer> repsPerSet) {
        this.repsPerSet = repsPerSet;
    }

    public List<Integer> getWeightsPerSet() {
        return weightsPerSet;
    }

    public void setWeightsPerSet(List<Integer> weightsPerSet) {
        this.weightsPerSet = weightsPerSet;
    }
}
