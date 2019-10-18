package com.testalltopic.ui.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.testalltopic.R;
import com.testalltopic.databinding.ActivityActionBinding;
import com.testalltopic.ui.fragment.FcmFragment;
import com.testalltopic.ui.fragment.IntentServiceFragment;
import com.testalltopic.ui.fragment.MobileContactFragment;
import com.testalltopic.ui.fragment.ServiceFragment;

public class Action extends Fragment implements View.OnClickListener {


    ActivityActionBinding binding;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding =  DataBindingUtil.inflate(inflater,R.layout.activity_action,container,false);


        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,new MobileContactFragment()).commit();

            }
        });

        binding.btnBroadcastReceive.setOnClickListener(this);
        binding.btnFcm.setOnClickListener(this);
        binding.btnIntentService.setOnClickListener(this);
        binding.btnService.setOnClickListener(this);
        binding.btnSubmit.setOnClickListener(this);

        return binding.getRoot();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_broadcast_receive:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,new MobileContactFragment()).commit();
                break;
            case R.id.btn_fcm:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,new FcmFragment()).commit();

                break;
             case R.id.btn_service:
                 getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,new ServiceFragment()).commit();

                 break;
             case R.id.btn_intent_service:
                 getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,new IntentServiceFragment()).commit();

                 break;
             case R.id.btn_submit:
                 getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,new MobileContactFragment()).commit();
                 break;

        }
    }
}
