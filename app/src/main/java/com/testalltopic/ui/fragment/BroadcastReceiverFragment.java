package com.testalltopic.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.testalltopic.R;
import com.testalltopic.databinding.BroadcastReceiverFragmentBinding;
import com.testalltopic.model.User;

public class BroadcastReceiverFragment extends Fragment {

    BroadcastReceiverFragmentBinding binding;
    public static final String NODE_USER = "user";

    FirebaseAuth auth;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.broadcast_receiver_fragment,container,false);
        auth =  FirebaseAuth.getInstance();
//        FirebaseMessaging.getInstance().subscribeToTopic("Updates");

        getFirebaseToken();
        return binding.getRoot();

    }

    private void getFirebaseToken(){

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (task.isSuccessful()){
                            String token =  task.getResult().getToken();
//                            binding.editInput.setText(token);
                            saveToken(token);
                        }else {
//                            binding.editInput.setText(task.getException().getMessage());;
                        }
                    }
                });

    }

    private void saveToken(String token) {
        String email =  auth.getCurrentUser().getEmail();
        User user =  new User(email,token);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(NODE_USER);

        databaseReference.child(auth.getCurrentUser().getUid())
                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(getActivity(), "Token save", Toast.LENGTH_SHORT).show();
                }else {

                }
            }
        });
                

    }

    @Override
    public void onStart() {
        super.onStart();
        if (auth.getCurrentUser() == null){
            getActivity().onBackPressed();
        }
    }
}
