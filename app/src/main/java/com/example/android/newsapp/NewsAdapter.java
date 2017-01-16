package com.example.android.newsapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by carlosblanco on 12/20/16.
 */

public class NewsAdapter extends ArrayAdapter<News> {

    public NewsAdapter(Context context, ArrayList<News> books) {
        super(context, 0, books);
    }

    /**
     * Returns a list item view that displays information about the book at the given position
     * in the list of books.
     */
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.news_list_item, parent, false);
        }

        // Find the book at the given position in the list of books
        News currentNew = getItem(position);

        // Find the TextView with the ID authors_view
        TextView newsTitleView = (TextView) listItemView.findViewById(R.id.news_title_view);
        // Display the author of the given Book
        newsTitleView.setText(currentNew.getTitle());

        String datetimeString = currentNew.getDate();

        String dateString = datetimeString.substring(0, 10);
        String timeString = datetimeString.substring(11, 16);

        TextView newsDateView = (TextView) listItemView.findViewById(R.id.news_date_view);
        newsDateView.setText(dateString);

        TextView newsTimeView = (TextView) listItemView.findViewById(R.id.news_time_view);
        newsTimeView.setText(timeString);

        TextView newsSectionView = (TextView) listItemView.findViewById(R.id.news_section_view);
        newsSectionView.setText(currentNew.getSection());


        // Return the list item view that is now showing the appropriate data
        return listItemView;
    }

    /**
     * Return the formatted date string (i.e. "Mar 3, 1984") from a Date object.
     */
    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }

    /**
     * Return the formatted date string (i.e. "4:30 PM") from a Date object.
     */
    private String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }
}
