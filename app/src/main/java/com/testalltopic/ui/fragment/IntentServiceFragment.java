package com.testalltopic.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.testalltopic.R;
import com.testalltopic.databinding.IntentServiceFragmentBinding;
import com.testalltopic.util.ExampleIntentService;
import com.testalltopic.util.ExampleService;

public class IntentServiceFragment extends Fragment implements View.OnClickListener {
    IntentServiceFragmentBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.intent_service_fragment,container,false);

        binding.btnStartService.setOnClickListener(this);
        binding.btnStopService.setOnClickListener(this);
        return binding.getRoot();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_start_service:
                if (!binding.editInput.getText().toString().isEmpty()) {
                    Intent serviceIntent = new Intent(getActivity(), ExampleIntentService.class);
                    serviceIntent.putExtra("inputData", binding.editInput.getText().toString());
                    getActivity().startService(serviceIntent);
                }else {
                    Toast.makeText(getActivity(), "Please Enter Input", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_stop_service:

                Intent stopService = new Intent(getActivity(), ExampleIntentService.class);
                getActivity().stopService(stopService);


                break;

        }
    }
}
