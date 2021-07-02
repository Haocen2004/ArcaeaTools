package xyz.hellocraft.arcaeatools;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import xyz.hellocraft.arcaeatools.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonReadSongs.setOnClickListener(v -> {
            for (String s : databaseList()) {
                Log.d(TAG,s);
            }
            //Log.d(TAG,databaseList().toString());
        });
    }


}