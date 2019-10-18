package com.testalltopic.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.testalltopic.R;
import com.testalltopic.databinding.FcmFragmentBinding;

import static com.testalltopic.util.App.CHANNEL_ID;

public class FcmFragment extends Fragment {

    FcmFragmentBinding binding;

    private FirebaseAuth auth;
    /*
    Create notification we need three things
    1- Notification channel -> it's need when your app run on os o version o or gareter
    2- NotificationBuilder  ->  create notification
    3 - Notification manager  ->
    */

    // Notification Channel

   /* public static final String CHANNEL_ID = "NotificationTest";
    public static final String CHANNEL_NAME= "Notification Test";
    public static final String CHANNEL_DESC= "Notification Test CREATE";*/

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fcm_fragment, container, false);

        auth = FirebaseAuth.getInstance();
//        createNotificationChannel();
        binding.btnStartService.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
//                getFirebaseToken();

                createUser();
            }
        });

        return binding.getRoot();
    }

    private void createUser() {
        if (binding.editInput.getText().toString().isEmpty()){
            binding.editInput.setError("Email required");
            binding.editInput.requestFocus();
            return;
        }else if (binding.editPassword.getText().toString().isEmpty()){
            binding.editPassword.setError("Password required");
            binding.editPassword.requestFocus();
            return;
        }else if (binding.editPassword.getText().toString().length()<6){
            binding.editPassword.setError("Password should be at least 6 digit");
            binding.editPassword.requestFocus();
            return;
        }
        binding.progressCircular.setVisibility(View.VISIBLE);
        auth.createUserWithEmailAndPassword(binding.editInput.getText().toString(),binding.editInput.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        binding.progressCircular.setVisibility(View.GONE);
                        if (task.isSuccessful()){
                            ProfileActivity();
                        }else {
                            if (task.getException() instanceof FirebaseAuthUserCollisionException){
                                userLogin(binding.editInput.getText().toString(),binding.editPassword.getText().toString());
                            }else {
                                binding.progressCircular.setVisibility(View.GONE);
                                Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void userLogin(String email, String password) {
        auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            ProfileActivity();
                        }else {
                            binding.progressCircular.setVisibility(View.GONE);
                            Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();


                        }
                    }
                });
    }

    private void ProfileActivity(){
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new BroadcastReceiverFragment()).commit();
    }

    private void getFirebaseToken(){
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (task.isSuccessful()){
                            String token =  task.getResult().getToken();
                            binding.editInput.setText(token);
                        }else {
                            binding.editInput.setText(task.getException().getMessage());;
                        }
                    }
                });

    }

    private void displayNotification() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant") Notification.Builder builder = new Notification.Builder(getActivity(), CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_android)
                    .setContentTitle("Hello Welcome To notification")
                    .setContentText("hello")
                    .setPriority(NotificationCompat.PRIORITY_HIGH);
            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(getActivity());
            managerCompat.notify(1, builder.build());
        } else {
            Toast.makeText(getActivity(), "not working", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        if (auth.getCurrentUser() != null){
            ProfileActivity();
        }
    }
}
