package com.example.bake;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.bake.json.JSONUtils;
import com.example.bake.objects.Recipe;

import org.json.JSONArray;

import java.util.List;

public class RecipeViewModel extends AndroidViewModel {
    private MutableLiveData<List<Recipe>> mRecipes;

    public RecipeViewModel(Application application) {
        super(application);
        setRecipes(application.getApplicationContext());
    }

    public LiveData<List<Recipe>> getRecipes(){
        return mRecipes;
    }

    private void setRecipes(final Context context){
        //Set Recipes to dummy data for now
        mRecipes = new MutableLiveData<>();

        String url = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
        final JsonArrayRequest request = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                mRecipes.setValue(JSONUtils.parseJSON(response, context));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }
}
