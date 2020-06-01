package com.example.bake;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bake.objects.Recipe;
import com.example.bake.objects.Step;

import java.util.List;

/**
 * A fragment representing a single Recipe detail screen.
 * This fragment is either contained in a {@link RecipeListActivity}
 * in two-pane mode (on tablets) or a {@link StepListActivity}
 * on handsets.
 */
public class StepListFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";
    public static final String ARG_TWO_PANE = "two_pane_arg";

    private int mIndex;

    private boolean mTwoPane;

    /**
     * The dummy content this fragment is presenting.
     */

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public StepListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.

            Activity activity =  this.getActivity();
            mIndex = getArguments().getInt(ARG_ITEM_ID);




        }

        if(getArguments().containsKey(ARG_TWO_PANE)){
            mTwoPane = getArguments().getBoolean(ARG_TWO_PANE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.step_list_frag, container, false);
        RecyclerView stepRecyclerView = rootView.findViewById(R.id.step_list_frag_recyclerview);


        RecipeViewModel viewModel = new ViewModelProvider(this).get(RecipeViewModel.class);
        viewModel.getRecipes().observe(getViewLifecycleOwner(), new Observer<List<Recipe>>() {
            @Override
            public void onChanged(List<Recipe> recipes) {
                List<Step> steps = recipes.get(mIndex).getSteps();

                StepAdapter stepAdapter = new StepAdapter((StepListActivity) StepListFragment.this.getActivity(), mTwoPane, steps, mIndex);

                stepRecyclerView.setHasFixedSize(true);
                stepRecyclerView.setLayoutManager(new LinearLayoutManager(StepListFragment.this.getActivity().getApplicationContext()));
                stepRecyclerView.setAdapter(stepAdapter);
            }
        });

        return rootView;
    }
}
