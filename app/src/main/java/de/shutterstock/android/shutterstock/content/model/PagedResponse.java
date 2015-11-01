package de.shutterstock.android.shutterstock.content.model;

import java.util.List;

/**
 * Created by emanuele on 01.11.15.
 */
public class PagedResponse<T> {

    public int page;
    public int per_page;
    public int total_count;

    private List<T> data;

    public List<T> getData() {
        return data;
    }
}
