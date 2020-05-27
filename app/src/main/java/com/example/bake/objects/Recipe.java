package com.example.bake.objects;

import java.util.List;

public class Recipe {
    List<Step> steps;
    List<String> ingredients;

    public Recipe(List<Step> steps, List<String> ingredients) {
        this.steps = steps;
        this.ingredients = ingredients;
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
