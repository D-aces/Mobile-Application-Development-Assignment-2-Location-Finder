package com.example.locationfinder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

public class newAddress extends AppCompatActivity {

    private DatabaseHandler dbHandler;
    private TextInputEditText address, latitude, longitude;
    private Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_new_address);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize
        dbHandler = new DatabaseHandler(this);
        address = findViewById(R.id.inputAddress);
        latitude = findViewById(R.id.inputLatitude);
        longitude = findViewById(R.id.inputLongitude);
        location = getIntent().getParcelableExtra("Location");
        if(location != null){
            address.setText(location.getFullAddress());
            latitude.setText(String.valueOf(location.getLatitude()));
            longitude.setText(String.valueOf(location.getLongitude()));
        }
    }

    public void onSubmit(View view) {
        if (!address.toString().isEmpty() && !latitude.toString().isEmpty() && !longitude.toString().isEmpty()) {
            if(location == null) {
                dbHandler.newAddress(address.getText().toString(), Double.valueOf(latitude.getText().toString()), Double.valueOf(longitude.getText().toString()));
                Toast.makeText(this, "Successfully added entry", Toast.LENGTH_SHORT).show();
            }
            else{
                location.setFullAddress(address.getText().toString());
                location.setLatitude(Double.valueOf(latitude.getText().toString()));
                location.setLongitude(Double.valueOf(longitude.getText().toString()));
                dbHandler.updateLocation(location);
            }
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);

        }
        else{
            Toast.makeText(this, "Please enter values for all fields", Toast.LENGTH_SHORT).show();
        }
    }
    public void deleteNote(View view){
        if(location != null) {
            dbHandler.deleteLocation(location);
        }
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}