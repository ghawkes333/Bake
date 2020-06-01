package com.example.bake;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bake.objects.Step;

import java.util.List;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.ViewHolder> {
    private static final String TAG = StepAdapter.class.getSimpleName();
    private final StepListActivity mParentActivity;
    private final boolean mTwoPane;
    private List<Step> mSteps;
    private int recipeId;


    public StepAdapter(StepListActivity parentActivity, boolean twoPane, List<Step> steps, int recipeId) {
        this.mParentActivity = parentActivity;
        this.mTwoPane = twoPane;
        this.mSteps = steps;
        this.recipeId = recipeId;
    }

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Step step = (Step) view.getTag();

            if (mTwoPane) {
                Bundle arguments = new Bundle();
                arguments.putInt(StepDetailFragment.ARG_STEP_INDEX, step.getIndex());
                arguments.putInt(StepDetailFragment.ARG_RECIPE_INDEX, recipeId);
                StepDetailFragment fragment = new StepDetailFragment();
                fragment.setArguments(arguments);
                mParentActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.step_detail_container, fragment)
                        .commit();
            } else {
                Context context = view.getContext();
                Intent intent = new Intent(context, StepDetailActivity.class);
                intent.putExtra(StepDetailFragment.ARG_STEP_INDEX, step.getIndex());
                intent.putExtra(StepDetailFragment.ARG_RECIPE_INDEX, recipeId);

                context.startActivity(intent);
            }
        }
    };

    @NonNull
    @Override
    public StepAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.step_list_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepAdapter.ViewHolder holder, int position) {
        holder.mTextView.setText(mSteps.get(position).getShortDescription());
        holder.mTextView.setOnClickListener(mOnClickListener);
        holder.mTextView.setTag(mSteps.get(position));
    }

    @Override
    public int getItemCount() {
        return mSteps.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        final TextView mTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.step_list_content_tv);
        }
    }
}
