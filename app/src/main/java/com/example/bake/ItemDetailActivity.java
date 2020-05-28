package com.example.bake;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.bake.json.JSONUtils;
import com.example.bake.objects.Recipe;

import org.json.JSONArray;

import java.util.List;

/**
 * An activity representing a single Item detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a [ItemListActivity].
 */

public class ItemDetailActivity extends AppCompatActivity {

    private String TAG = ItemDetailActivity.class.getSimpleName();
    private List<Recipe> mRecipes = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        Log.d(TAG, "Start");


//        setSupportActionBar(detail_toolbar);

//        fab.setOnClickListener { view ->
//                Snackbar.make(view, "Replace with your own detail action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//        }

        // Show the Up button in the action bar.
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
//            ItemDetailFragment itemDetailFragment = new ItemDetailFragment();
//            Bundle bundle = new Bundle();
//            bundle.putString("KEY", ItemDetailFragment.ARG_ITEM_ID);
//            itemDetailFragment.
//            Fragment fragment = itemDetailFragment {
//                arguments = Bundle().apply {
//                    putString(ItemDetailFragment.ARG_ITEM_ID,
//                            intent.getStringExtra(ItemDetailFragment.ARG_ITEM_ID));
//                }
//            }
//
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.item_detail_container, fragment)
//                    .commit()
        }

        setRecipes();


    }

    private void setRecipes(){
        String url = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
        JsonArrayRequest request = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                mRecipes = JSONUtils.parseJSON(response, ItemDetailActivity.this);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }




    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.home) {
                // This ID represents the Home or Up button. In the case of this
                // activity, the Up button is shown. For
                // more details, see the Navigation pattern on Android Design:
                //
                // http://developer.android.com/design/patterns/navigation.html#up-vs-back


                Intent intent = new Intent(this, ItemListActivity.class);




        } else super.onOptionsItemSelected(item);
        return super.onOptionsItemSelected(item);
    }
}
