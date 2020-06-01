package com.example.bake;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.bake.IdlingResource.SimpleIdlingResource;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;


/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */



@RunWith(AndroidJUnit4.class)
public class RecipeInstrumentedTest {
    private SimpleIdlingResource mIdlingResource;
    private int numSteps = 0;
    private static final String TAG = RecipeInstrumentedTest.class.getSimpleName();


    private boolean mTwoPane = true;


    @Before
    public void initiateIdlingResource(){
        ActivityScenario activityScenario = ActivityScenario.launch(RecipeListActivity.class);
        activityScenario.onActivity(new ActivityScenario.ActivityAction() {
            @Override
            public void perform(Activity activity) {
                RecipeListActivity recipeListActivity = (RecipeListActivity) activity;
                mIdlingResource = recipeListActivity.getIdlingResource();
                IdlingRegistry.getInstance().register(mIdlingResource);
            }
        });
    }

    @Test
    public void scrollToEachStep_checkHasDescription() {
        // Context of the app under test.
        Context appContext = ApplicationProvider.getApplicationContext();

        ActivityScenario.launch(RecipeListActivity.class);

        String emptyText = "";

        int numRecipes = 4;

        //Loop through each recipe
        for(int recipeListPosition = 0; recipeListPosition < numRecipes; recipeListPosition++) {
            //Get the recipe recyclerview
            ViewInteraction recipeRecyclerView = onView(withId(R.id.recipe_list));
            //Click on the recipe list item
            recipeRecyclerView.perform(RecyclerViewActions.actionOnItemAtPosition(recipeListPosition, click()));

            //Get the steps recyclerview
            ViewInteraction stepRecyclerView = onView(withId(R.id.step_list_frag_recyclerview));


            stepRecyclerView.check(matches(hasNumChildren(appContext)));

            //Loop through each step
            for (int stepListPosition = 0; stepListPosition < numSteps; stepListPosition++) {
                try {
                    Log.d(TAG, "Attempting recipe index " + recipeListPosition + " and step index " + stepListPosition);

                    //Click on the correct step
                    stepRecyclerView.perform(RecyclerViewActions.actionOnItemAtPosition(stepListPosition, click()));
                    //Get the description view
                    ViewInteraction descriptionTextview = onView(allOf(
                            withId(R.id.description_tv)));

                    //Check for a description text
                    descriptionTextview.check(matches(not(withText(emptyText))));


                } catch (Error e){
                    Log.e(TAG,"FAILED: recipe index " + recipeListPosition + " and step index " + stepListPosition );
                    e.printStackTrace();
                }

                if(!mTwoPane){
                    //Go back to the steps fragment
                    pressBack();
                }

            }

            //Go back to the recipes list
            pressBack();
        }
    }

    @After
    public void unRegisterIdlingResource(){
        if(mIdlingResource != null){
            IdlingRegistry.getInstance().unregister(mIdlingResource);
        }
    }

    public Matcher<View> hasNumChildren(Context context){
        return new BoundedMatcher<View, RecyclerView>(RecyclerView.class) {

            @Override
            protected boolean matchesSafely(RecyclerView item) {
                if(item.getAdapter() != null){
                    numSteps = item.getAdapter().getItemCount();
                }
                return true;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText(context.getString(R.string.matcher_description));
            }

        };
    }


}
