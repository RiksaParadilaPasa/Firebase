package com.example.firebase;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class TrackList extends ArrayAdapter<Track> {

    private Activity context;
    private List<Track> tracktList;

    public TrackList(Activity context , List<Track> tracktList){
        super(context,R.layout.list_layout,tracktList );
        this.context = context;
        this.tracktList = tracktList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();

        View ListViewItem =inflater.inflate(R.layout.list_layout,null,true);

        TextView textViewName = (TextView)ListViewItem.findViewById(R.id.textViewName);
        TextView textViewGenre = (TextView)ListViewItem.findViewById(R.id.textViewGenre);

        Track track = tracktList.get(position);

        textViewName.setText(track.getTracksName());
        textViewGenre.setText(String.valueOf(track.getTracksRating()));

        return ListViewItem;

    }

}
