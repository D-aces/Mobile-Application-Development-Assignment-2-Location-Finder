package com.example.locationfinder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DatabaseHandler dbHandler;
    private List<Double> coordinatesList;
    private Location location;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Set up window insets for proper layout in full-screen mode
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize
        dbHandler = new DatabaseHandler(this);
        CardView coordinatesCard = findViewById(R.id.coordinatesCard);
        TextView longitude = findViewById(R.id.longitude);
        TextView latitude = findViewById(R.id.latitude);
        FloatingActionButton addAddress = findViewById(R.id.addAddress);

        // Set up the SearchView for address search
        SearchView searchAddress = findViewById(R.id.searchAddress);
        searchAddress.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                location = dbHandler.getLocation(query);
                if(location != null){
                    latitude.setText(String.valueOf(location.getLatitude()));
                    longitude.setText(String.valueOf(location.getLongitude()));
                    coordinatesCard.setVisibility(View.VISIBLE);
                }
                else{
                    coordinatesCard.setVisibility(View.INVISIBLE);
                }
                return false;
            }
        });
    }

    // On Button Tap
    public void addNewAddress(View v){
        Intent i = new Intent(this, newAddress.class);
        startActivity(i);
    }

    public void editAddress(View v){
        if(location != null) {
            Intent i = new Intent(this, newAddress.class);
            i.putExtra("Location", location);
            startActivity(i);
        }
    }
}
