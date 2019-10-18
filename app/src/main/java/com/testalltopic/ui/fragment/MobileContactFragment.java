package com.testalltopic.ui.fragment;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.testalltopic.R;
import com.testalltopic.databinding.MobileContctFragmentBinding;
import com.testalltopic.model.MobileContact;
import com.testalltopic.ui.adapter.MobileListItem;

import java.util.ArrayList;

public class MobileContactFragment extends Fragment {
    MobileContctFragmentBinding binding;

    private static final int REQUEST_CODE = 1;
    Cursor all_cursor;
    int start = 0, last = 30, total_count = 0;
    int cluster_size = 80;
    ArrayList<MobileContact> list;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.mobile_contct_fragment, container, false);


        checkRunTimePermission();
//        initAdapter(mobileContacts);

        return binding.getRoot();
    }

    // Read mobile contact permission
    private void checkRunTimePermission() {

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            if (!hasPhoneContactPermission(Manifest.permission.READ_CALENDAR)) {
                requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CODE);
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_CODE:
                new MyContactAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                break;
        }
    }

    private boolean hasPhoneContactPermission(String permission) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            int hasPermission = ContextCompat.checkSelfPermission(getActivity(), permission);

            return hasPermission == PackageManager.PERMISSION_GRANTED;
        } else {
            return true;
        }
    }

    private void initAdapter(ArrayList<MobileContact> mobileContacts) {
        LinearLayoutManager linearLayoutManager =  new LinearLayoutManager(getActivity());
        MobileListItem  adapter = new MobileListItem(getActivity(),mobileContacts);
        binding.listView.setLayoutManager(linearLayoutManager);
        binding.listView.setAdapter(adapter);
    }



    class MyContactAsyncTask extends AsyncTask<String, Void, ArrayList<MobileContact>>{

        @Override
        protected ArrayList<MobileContact> doInBackground(String... strings) {

            try {
                all_cursor = getContactList_cursor();
                initMobileDataWithLimit(all_cursor,start,last);
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<MobileContact> mobileContacts) {
            initAdapter(list);
        }
    }


    private void initMobileDataWithLimit(Cursor all_cursor, int starting,int limit){
        ArrayList<MobileContact> mobileContactsList =  new ArrayList<>();
        list =  new ArrayList<>();
        ContentResolver contentResolver = getActivity().getContentResolver();

        MobileContact mobileContact;
        all_cursor.moveToPosition(starting);

        for (int i=starting;i<limit; i++){
            mobileContact =  new MobileContact();
            if (all_cursor.moveToPosition(i)) {
                total_count++;

                String id = all_cursor.getString(all_cursor.getColumnIndex(ContactsContract.Contacts._ID));


                String name = all_cursor.getString(all_cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                if (name == null){
                    mobileContact.setName("");
                }else {
                    mobileContact.setName(name);
                }


                Cursor cursor = contentResolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI
                        ,null, ContactsContract.CommonDataKinds.Email.CONTACT_ID +"= ?",new String[]{id},null);

                if (cursor != null && cursor.moveToFirst()){
                    String email = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));

                    if (email == null){
                        mobileContact.setEmail("");
                    }else {
                        mobileContact.setEmail(email);
                    }
                    cursor.close();
                }

                if (all_cursor.getInt(all_cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))>0){
                    Cursor pCur = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));


                        if (phoneNo == null){
                            mobileContact.setMobile("");
                        }else {
                            mobileContact.setMobile(phoneNo);
                        }
                        Log.e("Name", "Name: " + name);
                        Log.e("Phone", "Phone Number: " + phoneNo);
                    }
                }



                mobileContactsList.add(mobileContact);
                list.add(mobileContact);



            }

        }
        start = limit;
        limit = limit+cluster_size;

        if (limit>= all_cursor.getCount())
            limit = all_cursor.getCount() -1;

         last = limit;
        start= starting;

        if (total_count< all_cursor.getCount()-1){
            new MyContactAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }


    }



    private Cursor getContactList_cursor() throws Exception {
        ArrayList<MobileContact> arrayList = new ArrayList<>();
        ContentResolver cr = getActivity().getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");

        return cur;
    }
}
