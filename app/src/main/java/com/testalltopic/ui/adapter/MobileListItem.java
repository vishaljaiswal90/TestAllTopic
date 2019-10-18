package com.testalltopic.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.testalltopic.R;
import com.testalltopic.databinding.MobileListItemBinding;
import com.testalltopic.model.MobileContact;

import java.util.ArrayList;

public class MobileListItem extends RecyclerView.Adapter<MobileListItem.MyViewHolder>{

    Context context;
    ArrayList<MobileContact> list;

    public MobileListItem(Context context, ArrayList<MobileContact> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MobileListItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.mobile_list_item,parent,false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.binding.textName.setText(list.get(position).getName());
        holder.binding.textPhone.setText(list.get(position).getMobile());
        holder.binding.textEmail.setText(list.get(position).getEmail());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        MobileListItemBinding binding;
        public MyViewHolder(@NonNull MobileListItemBinding binding) {
            super(binding.getRoot());
            this.binding =  binding;
        }
    }
}
