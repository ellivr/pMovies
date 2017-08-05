package com.example.android.pmovies.tools;

import java.util.List;

/**
 * Created by ref on 8/4/2017.
 */

public class TrailersList {
    private String id;
    private List<Trailer> results;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Trailer> getResults() {
        return results;
    }

    public void setResults(List<Trailer> results) {
        this.results = results;
    }
}
