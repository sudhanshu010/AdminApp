package com.example.adminapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.adminapp.ManagerProfileActivity;
import com.example.adminapp.R;
import com.example.adminapp.models.Manager;
import org.parceler.Parcels;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ManagerBasicAdapter extends RecyclerView.Adapter<ManagerBasicAdapter.MyHolder> implements Filterable {

    private List<Manager> userList;
    private List<Manager> userListFull;
    Context c;

    public ManagerBasicAdapter(List<Manager>userlist, Context c)
    {
        this.c = c;
        this.userList = userlist;
        this.userListFull = new ArrayList<>(userlist);
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.manager_list_item,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
            holder.bind(userList.get(position));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            List<Manager>filteredList = new ArrayList<Manager>();

            if(constraint==null||constraint.length()==0)
            {
                filteredList.addAll(userListFull);
            }
            else
            {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for(Manager m : userListFull)
                {
                    if(m.getUserName().toLowerCase().contains(filterPattern))
                        filteredList.add(m);
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            userList.clear();
            userList.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };

    public class MyHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        CircleImageView profilepic;
        TextView managerName,buttonViewOption;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_item);
            profilepic = itemView.findViewById(R.id.manager_pic);
            managerName = itemView.findViewById(R.id.manager_name);
            buttonViewOption =  itemView.findViewById(R.id.textViewOptions);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Manager manager  = userList.get(getLayoutPosition());

                    if(manager!=null && manager.getUserName()!=null) {
                        Intent intent = new Intent(c, ManagerProfileActivity.class);
                        intent.putExtra("manager", Parcels.wrap(manager));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        c.getApplicationContext().startActivity(intent);
                    }
                }
            });
        }
        public void bind(Manager model) {

            managerName.setText(model.getUserName());
            Glide.with(itemView)
                    .load(model.getProfilePicLink())
                    .fitCenter()
                    .placeholder(R.drawable.profilepicdemo)
                    .into(profilepic);
            buttonViewOption.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popup = new PopupMenu(c,buttonViewOption);
                    popup.inflate(R.menu.option_menu);
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.menu1:
                                    //handle menu1 click
                                    break;
                                case R.id.menu2:
                                    //handle menu2 click
                                    break;
                            }
                            return false;
                        }
                    });

                    popup.show();
                }
            });
        }
    }



}