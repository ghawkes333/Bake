package com.example.bake.json;

import android.content.Context;
import android.util.Log;

import com.example.bake.R;
import com.example.bake.objects.Recipe;
import com.example.bake.objects.Step;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JSONUtils {

    public static List<Recipe> parseJSON(JSONArray array, Context context){
        List<Recipe> recipes = new ArrayList<>();
        try {
            for(int index = 0; index < array.length(); index++){
                JSONObject recipeJSON = array.getJSONObject(index);
                String name = recipeJSON.getString(RecipeValues.RESPONSE_META_NAME);
                List<String> ingredients = parseIngredients(recipeJSON.getJSONArray(RecipeValues.RESPONSE_META_INGREDIENTS), context);
                List<Step> steps = parseSteps(recipeJSON.getJSONArray(RecipeValues.RESPONSE_META_STEPS));
                Log.d("JSONUtils", index + name + ingredients + steps.get(0));
                Recipe recipe = new Recipe(index, name, steps, ingredients);
                recipes.add(recipe);
            }
        } catch (JSONException e){
            e.printStackTrace();
        }
        return recipes;
    }

    private static List<Step> parseSteps(JSONArray stepsJSON) throws JSONException {
        List<Step> stepsList = new ArrayList<>();
        for(int i = 0; i < stepsJSON.length(); i++){
            JSONObject stepJSON = stepsJSON.getJSONObject(i);
            String shortDescription = stepJSON.getString(RecipeValues.RESPONSE_STEPS_SHORT_DESCRIPTION);
            String description = stepJSON.getString(RecipeValues.RESPONSE_STEPS_LONG_DESCRIPTION);
            String videoURL = stepJSON.getString(RecipeValues.RESPONSE_STEPS_VIDEO_URL);
            String imageURL = stepJSON.getString(RecipeValues.RESPONSE_STEPS_IMAGE_URL);


            Step step = new Step(i, shortDescription, description, videoURL, imageURL);
            stepsList.add(step);
        }

        return stepsList;
    }

    /*
    * @param ingredientsJSON is the JSONArray of ingredients returned from the network call
    * */
    private static List<String> parseIngredients(JSONArray ingredientsJSON, Context context) throws JSONException {
        List<String> ingredientsList = new ArrayList<>();
        for(int i = 0; i < ingredientsJSON.length(); i++){
            JSONObject ingredient = ingredientsJSON.getJSONObject(i);
            String quantity = ingredient.getString(RecipeValues.RESPONSE_INGREDIENTS_QUANTITY);
            String measure = ingredient.getString(RecipeValues.RESPONSE_INGREDIENTS_MEASURE);
            String ingredientName = ingredient.getString(RecipeValues.RESPONSE_INGREDIENTS_INGREDIENT);

            String unit = measuresReadable(measure, context);
            String of = context.getString(R.string.of);
            String space = context.getString(R.string.space);

            String ingredientItem = quantity + space + unit + space + of + space + ingredientName;
            ingredientsList.add(ingredientItem);
        }

        return ingredientsList;
    }

    /*
    * Converts a JSON-response unit to a human-readable string
    * */
    private static String measuresReadable(String measure, Context context){
        String string = context.getString(R.string.string);
        int resId = context.getResources().getIdentifier(measure, string, context.getPackageName());
        if(resId > 0){
            return context.getString(resId);
        } else return measure;
    }
}
