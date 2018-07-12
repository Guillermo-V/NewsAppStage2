package com.example.android.newsappstage2;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class NewsAdapter extends ArrayAdapter<News> {

    public NewsAdapter(Context context, List<News> Newss) {
        super(context, 0, Newss);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.news_list_item, parent, false);
        }

        News currentNews = getItem(position);

        TextView section = (TextView) listItemView.findViewById(R.id.section);
        String msection = currentNews.getmsection();
        section.setText(msection);

        TextView title = (TextView) listItemView.findViewById(R.id.title);
        String mtitle = currentNews.getmTitle();
        title.setText(mtitle);

        TextView author = (TextView) listItemView.findViewById(R.id.author);
        String mAuthor = currentNews.getmAuthor();
        author.setText(mAuthor);


        String rawDate = currentNews.getmWebPublicationDate().replace("T", " ")
                .substring(0, Math.min(currentNews.getmWebPublicationDate().length(), 16));

        TextView dateView = (TextView) listItemView.findViewById(R.id.date);

        dateView.setText("" + rawDate);

        return listItemView;
    }
}