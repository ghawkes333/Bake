package com.example.bake;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.bake.objects.Recipe;

import java.util.List;

public class RecipeAdapter
        extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    private final RecipeListActivity mParentActivity;
    private final boolean mTwoPane;
    private List<Recipe> mRecipes;

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Recipe item = (Recipe) view.getTag();
            setCurrentRecipe(item);

            if (mTwoPane) {
                Bundle arguments = new Bundle();
                arguments.putInt(StepListFragment.ARG_ITEM_ID, item.getId());
                StepListFragment fragment = new StepListFragment();
                fragment.setArguments(arguments);
                mParentActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.recipe_detail_container, fragment)
                        .commit();
            } else {
                Context context = view.getContext();
                Intent intent = new Intent(context, StepListActivity.class);
                intent.putExtra(StepListFragment.ARG_ITEM_ID, item.getId());

                context.startActivity(intent);
            }
        }
    };

    RecipeAdapter (RecipeListActivity parent, List<Recipe> items, boolean twoPane) {
        mRecipes = items;
        mParentActivity = parent;
        mTwoPane = twoPane;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_list_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mIdView.setText(String.valueOf(mRecipes.get(position).getId()));
        holder.mContentView.setText(mRecipes.get(position).getName());

        holder.itemView.setTag(mRecipes.get(position));
        holder.itemView.setOnClickListener(mOnClickListener);
    }

    @Override
    public int getItemCount() {
        return mRecipes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView mIdView;
        final TextView mContentView;

        ViewHolder(View view) {
            super(view);
            mIdView = (TextView) view.findViewById(R.id.id_text);
            mContentView = (TextView) view.findViewById(R.id.content);
        }
    }

    private void setCurrentRecipe(Recipe recipe){
        RecipeListActivity.mCurrentRecipe = recipe;
    }
}
