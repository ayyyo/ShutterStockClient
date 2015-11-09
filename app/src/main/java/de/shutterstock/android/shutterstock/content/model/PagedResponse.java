package de.shutterstock.android.shutterstock.content.model;

import java.util.List;

/**
 * Created by emanuele on 01.11.15.
 */
public class PagedResponse<T> {

    private int page;
    private int per_page;
    private int total_count;
    public String search_id;

    private List<T> data;

    public List<T> getData() {
        return data;
    }

    public boolean isLastPage() {
        return page *
                per_page >= total_count;
    }

    public boolean isFirstPage() {
        return page == 1;
    }

    public int getCurrentPage() {
        return page;
    }
}
