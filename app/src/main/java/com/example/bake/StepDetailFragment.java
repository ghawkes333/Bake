package com.example.bake;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.bake.objects.Recipe;
import com.example.bake.objects.Step;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

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
    private Context mContext;

    private SimpleExoPlayer mPlayer;

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
        mContext = activity.getApplicationContext();


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


        if(mStepIndex == -1 || mRecipeIndex == -1){
            Log.e(TAG, "Step index is " + mStepIndex + ". Recipe index is " + mRecipeIndex + ". Will not get recipes");
            return rootView;
        }

        RecipeViewModel viewModel = new ViewModelProvider(this).get(RecipeViewModel.class);
        viewModel.getRecipes().observe(getViewLifecycleOwner(), new Observer<List<Recipe>>() {
            @Override
            public void onChanged(List<Recipe> recipes) {
                Step step = recipes.get(mRecipeIndex).getSteps().get(mStepIndex);
                descriptionTextView.setText(step.getDescription());

                if(!step.getVideoUrl().isEmpty()){
                    //Show the video
                    PlayerView playerView = rootView.findViewById(R.id.video_view);
                    playerView.setVisibility(View.VISIBLE);

                    playVideo(playerView, step.getVideoUrl());
                } else if(!step.getImageUrl().isEmpty()){
                    //Show the image
                    ImageView imageView = rootView.findViewById(R.id.step_detail_iv);
                    imageView.setVisibility(View.VISIBLE);
                    Picasso.get().load(step.getImageUrl());
                }
                Log.d(TAG, String.valueOf(mRecipeIndex));
                Log.d(TAG,  String.valueOf(mStepIndex));

            }
        });

        return rootView;
    }

    private void playVideo(PlayerView playerView, String videoUrl){
        mPlayer = ExoPlayerFactory.newSimpleInstance(mContext);
        playerView.setPlayer(mPlayer);

        Uri uri = Uri.parse(videoUrl);

        String userAgent = Util.getUserAgent(mContext, getString(R.string.app_name));
        MediaSource mediaSource = new ExtractorMediaSource(uri, new DefaultDataSourceFactory(mContext, userAgent), new DefaultExtractorsFactory(), null, null);

        mPlayer.prepare(mediaSource);
        mPlayer.setPlayWhenReady(true);
    }

    private void releaseMedia(){
        if(mPlayer != null){
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        releaseMedia();
    }
}
