package com.example.bake.objects;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Recipe implements Parcelable {
    int index;
    String name;
    List<Step> steps;
    List<String> ingredients;

    public Recipe(int index, String name, List<Step> steps, List<String> ingredients) {
        this.index = index;
        this.name = name;
        this.steps = steps;
        this.ingredients = ingredients;
    }


    private Recipe(Parcel in){
        index = in.readInt();
        name = in.readString();
        in.readStringList(ingredients);
        in.readList(steps, Step.class.getClassLoader());
    }

    public int getIndex() {
        return index;
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


    @Override
    public int describeContents() {
        return 0;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public static final Parcelable.Creator<Recipe> CREATOR = new Parcelable.Creator<Recipe>(){

        @Override
        public Recipe createFromParcel(Parcel parcel) {
            return new Recipe(parcel);
        }

        @Override
        public Recipe[] newArray(int i) {
            return new Recipe[i];
        }
    };

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(index);
        parcel.writeString(name);
        parcel.writeStringList(ingredients);
        parcel.writeList(steps);
    }
}
