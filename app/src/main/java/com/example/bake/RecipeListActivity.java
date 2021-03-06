package com.example.bake;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bake.IdlingResource.SimpleIdlingResource;
import com.example.bake.objects.Recipe;

import java.util.List;

/**
 * An activity representing a list of Recipes. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link StepListActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class RecipeListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    public LiveData<List<Recipe>> mRecipes;

    public static Recipe mCurrentRecipe;

    @VisibleForTesting
    private SimpleIdlingResource mIdlingResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());


        if (findViewById(R.id.recipe_list).getTag().equals(getString(R.string.in_two_pane))) {
            //Running on a tablet
            mTwoPane = true;
        }




    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpViewModel();
    }

    private void setUpViewModel(){
        mIdlingResource = new SimpleIdlingResource();

        RecipeViewModel viewModel = new ViewModelProvider(this).get(RecipeViewModel.class);
        if(mIdlingResource != null){
            viewModel.setIdlingResource(mIdlingResource);
        }
        viewModel.getRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(List<Recipe> recipes) {
                View recyclerView = findViewById(R.id.recipe_list);
                assert recyclerView != null;
                setupRecyclerView((RecyclerView) recyclerView, recipes);

            }
        });
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView, List<Recipe> recipes) {
        recyclerView.setAdapter(new RecipeAdapter(this, recipes, mTwoPane));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        updateWidget(this, mCurrentRecipe);
    }

    private void updateWidget(Context context, Recipe recipe){
        if(recipe == null) return;
        List<String> ingredients = recipe.getIngredients();
        String ingredientsString = stringListToString(ingredients);

        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        int[] ids = manager.getAppWidgetIds(new ComponentName(context.getPackageName(), RecipeAppWidgetProvider.class.getName()));
        for(int id : ids){
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_appwidget);
            views.setTextViewText(R.id.widget_tv, ingredientsString);
            manager.updateAppWidget(id, views);
        }


    }

    private String stringListToString(List<String> strings){
        StringBuilder string = new StringBuilder();
        for(int i = 0; i < strings.size(); i++){
            string.append(getResources().getString(R.string.new_line));
            string.append(strings.get(i));
        }

        return string.toString();
    }

    public SimpleIdlingResource getIdlingResource(){
        return mIdlingResource;
    }
}
