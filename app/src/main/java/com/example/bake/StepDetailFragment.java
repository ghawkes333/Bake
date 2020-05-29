package com.example.bake;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.bake.objects.Recipe;
import com.example.bake.objects.Step;

import java.security.InvalidParameterException;
import java.util.List;

/**
 * A fragment representing a single Recipe detail screen.
 * This fragment is either contained in a {@link RecipeListActivity}
 * in two-pane mode (on tablets) or a {@link StepListActivity}
 * on handsets.
 */
public class StepDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_RECIPE_INDEX = "recipe_index_step_detail_frag";
    public static final String ARG_STEP_INDEX = "step_index_step_detail_frag";
    private int mRecipeIndex = -1;
    private int mStepIndex = -1;

    private List<Recipe> mRecipes;

    private boolean mTwoPane;

    private List<Step> mSteps;

    private String TAG = StepDetailFragment.class.getSimpleName();

    /**
     * The dummy content this fragment is presenting.
     */
//    private DummyContent.DummyItem mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public StepDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Activity activity =  this.getActivity();


        if (getArguments().containsKey(ARG_RECIPE_INDEX) && getArguments().containsKey(ARG_STEP_INDEX)) {
            mRecipeIndex = getArguments().getInt(ARG_RECIPE_INDEX);
            mStepIndex = getArguments().getInt(ARG_STEP_INDEX);
        } else throw new InvalidParameterException("Step Index is " + mStepIndex + "Recipe Index is " + mRecipeIndex);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.step_detail_frag, container, false);
        TextView descriptionTextView = rootView.findViewById(R.id.description_tv);


        RecipeViewModel viewModel = new ViewModelProvider(this).get(RecipeViewModel.class);
        viewModel.getRecipes().observe(getViewLifecycleOwner(), new Observer<List<Recipe>>() {
            @Override
            public void onChanged(List<Recipe> recipes) {
                Step step = recipes.get(mRecipeIndex).getSteps().get(mStepIndex);
                descriptionTextView.setText(step.getDescription());
                Log.d(TAG, String.valueOf(mRecipeIndex));
                Log.d(TAG,  String.valueOf(mStepIndex));
//                List<Step> stepList = recipes.get(mRecipeIndex).getSteps();
//                Step step = stepList.get(mStepIndex);
//                descriptionTextView.setText(step.getDescription());
            }
        });



//        StepAdapter stepAdapter = new StepAdapter(this.getActivity(), false);
        // Show the dummy content as text in a TextView.
//        if (mItem != null) {
//            ((TextView) rootView.findViewById(R.id.step_list_frag)).setText(mItem.details);
//        }

        return rootView;
    }
}
