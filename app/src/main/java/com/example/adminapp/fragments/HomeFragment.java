package com.example.adminapp.fragments;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.adminapp.AddEmployeeActivity;
import com.example.adminapp.BroadcastActivity;
import com.example.adminapp.GenerateQRActivity;
import com.example.adminapp.ManagerListActivity;
import com.example.adminapp.MechanicListActivity;
import com.example.adminapp.R;
import com.example.adminapp.StockRecordActivity;
import com.example.adminapp.UsersActivity;
import com.example.adminapp.ZoomCenterCardLayoutManager;
import com.example.adminapp.adapters.ManagerHomepageListAdapter;
import com.example.adminapp.adapters.MechanicHomepageListAdapter;
import com.example.adminapp.adapters.ViewPagerAdapter;
import com.example.adminapp.models.Manager;
import com.example.adminapp.models.Mechanic;
import com.firebase.ui.database.paging.DatabasePagingOptions;
import com.github.lzyzsd.circleprogress.ArcProgress;
import com.github.lzyzsd.circleprogress.CircleProgress;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;


public class HomeFragment extends Fragment {

    Activity activity;
    FirebaseDatabase firebaseDatabase;
    ZoomCenterCardLayoutManager HorizontalLayout,Horizontallayout1;
    LinearLayout linearLayout1,linearLayout2,stockrecord;
    TextView total, working;
    long total_machine, faulted;
    ProgressBar progress;
    ArcProgress circleProgress;
    int percentage;


    DatabaseReference totalMachineReference, faultMachineReference;

    public HomeFragment() {
        activity = getActivity();
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view= inflater.inflate(R.layout.fragment_home, container, false);
        activity = getActivity();

        final Toolbar toolbar=view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayUseLogoEnabled(true);
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setLogo(R.drawable.ic_home_toolbar);
        toolbar.setTitleTextAppearance(getActivity(),R.style.TitleTextAppearance);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                view.findViewById(R.id.horizontal_progress_bar).setVisibility(View.GONE);
            }
        },4000);

        firebaseDatabase = FirebaseDatabase.getInstance();
        totalMachineReference = firebaseDatabase.getReference("TotalMachines");
        faultMachineReference = firebaseDatabase.getReference("FaultMachines");

        totalMachineReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                total_machine = (long) dataSnapshot.getValue();
                total.setText(String.valueOf(total_machine));

                faultMachineReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        faulted = (long) dataSnapshot.getValue();
                        working.setText(String.valueOf(total_machine-faulted) + "working");

                        if(total_machine!=0) {
                            percentage = (int) (((total_machine - faulted)*100) / total_machine);
                        }
                        else
                        {
                            percentage = 50;
                        }

                        Log.i("total", String.valueOf(total_machine));


                        progress.setProgress(percentage);
                        circleProgress.setProgress(percentage);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        ImageView generateQr = view.findViewById(R.id.generate_qr);
        ImageView addEmployee = view.findViewById(R.id.add_employee);
        ImageView broadcast = view.findViewById(R.id.broadcast);
        stockrecord = view.findViewById(R.id.stock_record_ll);
        total = view.findViewById(R.id.total_machine);
        working = view.findViewById(R.id.working);
        progress = view.findViewById(R.id.progress_bar_1);
        circleProgress = view.findViewById(R.id.arc_progress1);



        stockrecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity.getApplicationContext(), StockRecordActivity.class);
                startActivity(i);
            }
        });

        generateQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(activity.getApplicationContext(), GenerateQRActivity.class);
                startActivity(i);
            }
        });

        addEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity.getApplicationContext(), AddEmployeeActivity.class);
                startActivity(i);

            }
        });
        //Image Slider
        SliderView sliderView = view.findViewById(R.id.imageSlider);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity());

        sliderView.setSliderAdapter(adapter);

        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.CUBEINDEPTHTRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.parseColor("#275F73"));
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(2); //set scroll delay in seconds :
        sliderView.startAutoCycle();

        broadcast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity.getApplicationContext(), BroadcastActivity.class);
                startActivity(i);
            }
        });

        RecyclerView recyclerView_mechanic,recyclerView_manager;

        recyclerView_mechanic=view.findViewById(R.id.mechanic_list_rv);
        HorizontalLayout = new ZoomCenterCardLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        recyclerView_mechanic.setLayoutManager(HorizontalLayout);
        firebaseDatabase = FirebaseDatabase.getInstance();

        Query baseQuery = firebaseDatabase.getReference("Users").child("Mechanic");
        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPrefetchDistance(10)
                .setPageSize(20)
                .build();

        DatabasePagingOptions<Mechanic> options = new DatabasePagingOptions.Builder<Mechanic>()
                .setLifecycleOwner(this)
                .setQuery(baseQuery,config,Mechanic.class)
                .build();

        MechanicHomepageListAdapter mechanicHomepageListAdapter= new MechanicHomepageListAdapter(options,getActivity().getApplicationContext());
        recyclerView_mechanic.setAdapter(mechanicHomepageListAdapter);
        mechanicHomepageListAdapter.startListening();

        recyclerView_manager = view.findViewById(R.id.manager_list_rv);
        Horizontallayout1 =  new ZoomCenterCardLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        recyclerView_manager.setLayoutManager(Horizontallayout1);
        Query baseQuery1 = firebaseDatabase.getReference("Users").child("Manager");
        PagedList.Config config1 = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPrefetchDistance(10)
                .setPageSize(20)
                .build();

        DatabasePagingOptions<Manager> options1 = new DatabasePagingOptions.Builder<Manager>()
                .setLifecycleOwner(this)
                .setQuery(baseQuery1,config1,Manager.class)
                .build();
        ManagerHomepageListAdapter managerHomepageListAdapter = new ManagerHomepageListAdapter(options1,getActivity().getApplicationContext());
        recyclerView_manager.setAdapter(managerHomepageListAdapter);
        managerHomepageListAdapter.startListening();

        linearLayout1 = view.findViewById(R.id.view_list_manager);
        linearLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), ManagerListActivity.class);
                startActivity(intent);
            }
        });

        linearLayout2 = view.findViewById(R.id.view_list_mechanic);
        linearLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), MechanicListActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }


}
