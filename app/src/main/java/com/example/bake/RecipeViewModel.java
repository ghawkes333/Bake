package com.example.bake;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

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
import com.example.bake.json.RecipeValues;
import com.example.bake.objects.Recipe;

import org.json.JSONArray;

import java.io.IOException;
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

        if(!isOnline()){
            Toast.makeText(context, context.getString(R.string.no_internet), Toast.LENGTH_LONG).show();
            Log.w(RecipeViewModel.class.getSimpleName(), "No internet");
            return;
        }

        String url = RecipeValues.RECIPE_NETWORK_URL;
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


    /*
    * Found on
    * https://stackoverflow.com/questions/1560788/how-to-check-internet-access-on-android-inetaddress-never-times-out
    * on 6/1/20
    * */
    private boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        }
        catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }

        return false;
    }
}
