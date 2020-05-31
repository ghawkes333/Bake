package com.example.bake;

import android.app.Application;
import android.content.Context;

import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.bake.IdlingResource.SimpleIdlingResource;
import com.example.bake.json.JSONUtils;
import com.example.bake.objects.Recipe;

import org.json.JSONArray;

import java.util.List;

public class RecipeViewModel extends AndroidViewModel {
    private MutableLiveData<List<Recipe>> mRecipes;

    @VisibleForTesting
    private static SimpleIdlingResource mIdlingResource;

    public RecipeViewModel(Application application) {
        super(application);
        setRecipes(application.getApplicationContext());
    }

    public LiveData<List<Recipe>> getRecipes(){
        return mRecipes;
    }

    private void setRecipes(final Context context){
        //Pause testing
        if(mIdlingResource != null){
            mIdlingResource.setIdleState(false);
        }

        //Set Recipes to dummy data for now
        mRecipes = new MutableLiveData<>();

        String url = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
        final JsonArrayRequest request = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                mRecipes.setValue(JSONUtils.parseJSON(response, context));

                //Continue testing
                if(mIdlingResource != null) {
                    mIdlingResource.setIdleState(true);
                }
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

    void setIdlingResource(SimpleIdlingResource resource){
        mIdlingResource = resource;
    }

    public static SimpleIdlingResource getIdlingResource(){
        return mIdlingResource;
    }
}
