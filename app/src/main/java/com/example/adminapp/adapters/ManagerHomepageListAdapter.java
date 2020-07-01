package com.example.adminapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adminapp.R;
import com.example.adminapp.models.Manager;
import com.example.adminapp.models.Mechanic;
import com.firebase.ui.database.paging.DatabasePagingOptions;
import com.firebase.ui.database.paging.FirebaseRecyclerPagingAdapter;
import com.firebase.ui.database.paging.LoadingState;

import de.hdodenhof.circleimageview.CircleImageView;

public class ManagerHomepageListAdapter extends FirebaseRecyclerPagingAdapter<Manager,ManagerHomepageListAdapter.MyHolder> {
    Context c;

    public ManagerHomepageListAdapter(@NonNull DatabasePagingOptions<Manager> options, Context c) {
        super(options);
        this.c = c;
    }



    @Override
    protected void onBindViewHolder(@NonNull MyHolder viewHolder, int position, @NonNull Manager model) {
        viewHolder.bind(model);
    }

    @Override
    protected void onLoadingStateChanged(@NonNull LoadingState state) {

    }

    @NonNull
    @Override
    public ManagerHomepageListAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.manager_list_item, null);
        return new ManagerHomepageListAdapter.MyHolder(view);
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        CircleImageView profilepic;
        TextView managerName;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_item);
            profilepic = itemView.findViewById(R.id.manager_pic);
            managerName = itemView.findViewById(R.id.manager_name);
        }
        public void bind(Manager model) {
            managerName.setText(model.getUserName());
        }
    }
}
