package com.udacity.sandwichclub;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView ingredientsIv = findViewById(R.id.image_iv);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        Resources res = getResources();
        Drawable backup = res.getDrawable(R.drawable.placeholder_img);

        populateUI(sandwich);
        Picasso.with(this)
                .load(sandwich.getImage())
                .error(backup)
                .into(ingredientsIv);

        setTitle(sandwich.getMainName().subSequence(0, sandwich.getMainName().length()));
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Sandwich sw) {
        List<String> alsoknownlist = sw.getAlsoKnownAs();
        List<String> ingredientlist = sw.getIngredients();
        String alsoknownstr = "";
        String ingredientstr = "";
        String comma = ", ";

        if ( sw.getPlaceOfOrigin() != "" ) {
            TextView originlabelTv = findViewById(R.id.origin_label_tv);
            originlabelTv.setVisibility(View.VISIBLE);
            TextView originTv = findViewById(R.id.origin_tv);
            originTv.setText(sw.getPlaceOfOrigin());
            originTv.setVisibility(View.VISIBLE);

        }
        if (sw.getDescription() != "") {
            TextView descriptionlabelTv = findViewById(R.id.description_label_tv);
            descriptionlabelTv.setVisibility(View.VISIBLE);
            TextView descriptionTv = findViewById(R.id.description_tv);
            descriptionTv.setText(sw.getDescription());
            descriptionTv.setVisibility(View.VISIBLE);
        }

        for (int i = 0; i < alsoknownlist.size(); i++){
            alsoknownstr = alsoknownstr.concat(alsoknownlist.get(i));
            if (i < alsoknownlist.size() - 1){
                alsoknownstr = alsoknownstr.concat(comma);
            }
        }
        if (alsoknownstr.length() > 0) {
            TextView alsoknownlabelTv = findViewById(R.id.also_known_label_tv);
            alsoknownlabelTv.setVisibility(View.VISIBLE);
            TextView alsoknownTv = findViewById(R.id.also_known_tv);
            alsoknownTv.setText(alsoknownstr);
            alsoknownTv.setVisibility(View.VISIBLE);
        }

        for (int i = 0; i < ingredientlist.size(); i++){
            ingredientstr = ingredientstr.concat(ingredientlist.get(i));
            if (i < ingredientlist.size() - 1){
                ingredientstr = ingredientstr.concat(comma);
            }
        }
        if (ingredientstr.length() > 0) {
            TextView ingredientslabelTv = findViewById(R.id.ingredients_label_tv);
            ingredientslabelTv.setVisibility(View.VISIBLE);
            TextView ingredientsTv = findViewById(R.id.ingredients_tv);
            ingredientsTv.setText(ingredientstr);
            ingredientsTv.setVisibility(View.VISIBLE);
        }
    }
}