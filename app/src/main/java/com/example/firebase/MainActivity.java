package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String ARTIST_NAME = "artistname";
    public static final String ARTIST_ID = "artistid";

    EditText editTextName;
    Button buttonAdd;
    Spinner spinnerGenres;

    DatabaseReference databaseArtist;

    ListView listViewArtis;

    List<Artist> artistList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseArtist = FirebaseDatabase.getInstance().getReference("Artists");

        editTextName = (EditText)findViewById(R.id.editTextName);
        spinnerGenres = (Spinner)findViewById(R.id.spinnerGenres);
        buttonAdd = (Button)findViewById(R.id.buttonAddArtist);

        listViewArtis = (ListView)findViewById(R.id.container_listViewArtist);

        artistList = new ArrayList<>();

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addArtist();
            }
        });

        listViewArtis.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Artist artist = artistList.get(i);

                Intent intent = new Intent(getApplicationContext(), AddTrackActivity.class);
                intent.putExtra(ARTIST_ID, artist.getArtistId());
                intent.putExtra(ARTIST_NAME, artist.getArtistName());

                startActivity(intent);

            }
        });

        listViewArtis.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Artist artist = artistList.get(i);

                showUpdateDialog(artist.getArtistId(), artist.getArtistName());

                return false;
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseArtist.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                artistList.clear();

                for (DataSnapshot artisSnapshot : dataSnapshot.getChildren()){
                    Artist artist = artisSnapshot.getValue(Artist.class);

                    artistList.add(artist);
                }

                ArtistList adapter = new ArtistList(MainActivity.this, artistList);
                listViewArtis.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void showUpdateDialog(final String artisId, String artisName){
        AlertDialog.Builder dialoBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.update_dialog,null);

        dialoBuilder.setView(dialogView);

        final EditText editTextName = (EditText)dialogView.findViewById(R.id.editTextName);
        final Button ButtonUpdate = (Button) dialogView.findViewById(R.id.buttonUpdate);
        final Spinner spinnerGenres = (Spinner) dialogView.findViewById(R.id.editGenres);
        final Button ButtonDelete = (Button) dialogView.findViewById(R.id.buttonDelete);


        dialoBuilder.setTitle("Updating Artist "+artisName);

        final AlertDialog alertDialog = dialoBuilder.create();
        alertDialog.show();

        ButtonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextName.getText().toString().trim();
                String genre = spinnerGenres.getSelectedItem().toString();

                if (TextUtils.isEmpty(name)){
                    editTextName.setError("Name Required");
                    return;
                }

                updateArtist(artisId,name,genre);
                alertDialog.dismiss();
            }
        });

        ButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteArtist(artisId);
                alertDialog.dismiss();
            }
        });

    }

    private void deleteArtist(String artisId) {
        DatabaseReference drArtist = FirebaseDatabase.getInstance().getReference("Artists").child(artisId);
        DatabaseReference drTrack = FirebaseDatabase.getInstance().getReference("Tracks").child(artisId);

        drArtist.removeValue();
        drTrack.removeValue();

        Toast.makeText(this, "Artist Is Deleted", Toast.LENGTH_LONG).show();
    }



    private boolean updateArtist(String id, String name, String genre){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Artists").child(id);

        Artist artist = new Artist(id,name,genre);

        databaseReference.setValue(artist);
        Toast.makeText(this, "Artis Updated Successfully", Toast.LENGTH_LONG).show();

        return false;
    }

    private void addArtist(){
        String name = editTextName.getText().toString().trim();
        String genres = spinnerGenres.getSelectedItem().toString();


        if (!TextUtils.isEmpty(name)){

            String id = databaseArtist.push().getKey();

            Artist artist = new Artist(id,name,genres);

            databaseArtist.child(id).setValue(artist);

            Toast.makeText(this, "Artist Added", Toast.LENGTH_LONG).show();


        }else{
            Toast.makeText(this, "you should enter a name", Toast.LENGTH_LONG).show();
        }
    }

}
