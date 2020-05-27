package com.example.bake.objects;

import java.util.List;

public class Recipe {
    int id;
    String name;
    List<Step> steps;
    List<String> ingredients;

    public Recipe(int id, String name, List<Step> steps, List<String> ingredients) {
        this.id = id;
        this.name = name;
        this.steps = steps;
        this.ingredients = ingredients;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }



}
