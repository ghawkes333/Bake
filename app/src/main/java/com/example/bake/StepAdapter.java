package com.example.bake;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bake.objects.Recipe;
import com.example.bake.objects.Step;

import java.util.List;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.ViewHolder> {
    private static final String TAG = StepAdapter.class.getSimpleName();
    private final StepListActivity mParentActivity;
    private final boolean mTwoPane;
    private List<Step> mSteps;

    public StepAdapter(StepListActivity parentActivity, boolean twoPane, List<Step> steps) {
        this.mParentActivity = parentActivity;
        this.mTwoPane = twoPane;
        this.mSteps = steps;
    }

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.d("StepAdapter", "Click!");
            Recipe item = (Recipe) view.getTag();
            if (mTwoPane) {
                Bundle arguments = new Bundle();
                arguments.putInt(StepListFragment.ARG_ITEM_ID, item.getId());
                StepListFragment fragment = new StepListFragment();
                fragment.setArguments(arguments);
                mParentActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.step_detail_container, fragment)
                        .commit();
            } else {
                Context context = view.getContext();
                Intent intent = new Intent(context, StepListActivity.class);
                intent.putExtra(StepListFragment.ARG_ITEM_ID, item.getId());

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
        Log.d(TAG, "onBindViewHolder");
        holder.mTextView.setText(mSteps.get(position).getShortDescription());
        holder.mTextView.setOnClickListener(mOnClickListener);
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
