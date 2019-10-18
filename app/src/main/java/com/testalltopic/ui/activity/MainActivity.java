package com.testalltopic.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;

import com.testalltopic.R;
import com.testalltopic.databinding.ActivityMainBinding;
import com.testalltopic.util.ExampleBroadcastReceiver;

public class MainActivity extends AppCompatActivity {


    ActivityMainBinding binding;
    IntentFilter intentFilter;
    ExampleBroadcastReceiver receiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);


         receiver = new ExampleBroadcastReceiver();


           getSupportFragmentManager().beginTransaction().replace(R.id.container,new Action()).commit();
    }

    @Override
    protected void onStart() {
        super.onStart();

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(receiver,filter);
    }


    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(receiver);
    }

}
