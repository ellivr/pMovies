package com.example.android.pmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.android.pmovies.tools.GlobalVar;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putParcelable(GlobalVar.MOVIE_TAG,
                    getIntent().getParcelableExtra(GlobalVar.MOVIE_TAG));

            DetailActivityFragment fragment = new DetailActivityFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_detail_container, fragment)
                    .commit();
        }
    }

}
