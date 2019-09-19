package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddTrackActivity extends AppCompatActivity {

    TextView textViewArtistName;
    EditText editTextTrackNmae;
    SeekBar seekBarRating;

    Button buttonAddTrack;

    ListView listViewTacks;

    List<Track> trackList;

    DatabaseReference databaseTracks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_track);

        textViewArtistName = (TextView)findViewById(R.id.textViewArtisName);
        editTextTrackNmae = (EditText) findViewById(R.id.editTrackName);
        seekBarRating = (SeekBar) findViewById(R.id.seekbarRating);
        buttonAddTrack = (Button) findViewById(R.id.buttonAddTrack);

        listViewTacks = (ListView)findViewById(R.id.container_listViewTrack);

        trackList = new ArrayList<>();

        Intent inten = getIntent();

        String id = inten.getStringExtra(MainActivity.ARTIST_ID);
        String name = inten.getStringExtra(MainActivity.ARTIST_NAME);

        textViewArtistName.setText(name);

        databaseTracks = FirebaseDatabase.getInstance().getReference("Tracks").child(id);

        buttonAddTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveTrack();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseTracks.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

               trackList.clear();

                for (DataSnapshot artisSnapshot : dataSnapshot.getChildren()){
                    Track track = artisSnapshot.getValue(Track.class);

                    trackList.add(track);
                }

                TrackList adapter = new TrackList(AddTrackActivity.this, trackList);
                listViewTacks.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void saveTrack(){
        String trackNmae = editTextTrackNmae.getText().toString().trim();
        int rating = seekBarRating.getProgress();

        if (!TextUtils.isEmpty(trackNmae)){
            String id = databaseTracks.push().getKey();

            Track track = new Track(id, trackNmae, rating);

            databaseTracks.child(id).setValue(track);

            Toast.makeText(this, "Track Saved Successfully", Toast.LENGTH_LONG).show();

        } else{
            Toast.makeText(this, "Track Name Should Not Be Empty", Toast.LENGTH_LONG).show();
        }

    }
}
