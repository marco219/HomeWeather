package com.marcoassenza.homeweather;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.List;

public class HomeActivity extends AppCompatActivity {

    public static RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    public static HomeCardsAdapter adapter;
    public static List<HomeCard> homeCards;
    private TextView noInternetTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        noInternetTextView= (TextView) findViewById(R.id.no_internet);
        updateData();
        recyclerView = (RecyclerView)findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);

        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new HomeCardsAdapter(homeCards);


        adapter.setOnItemClickListener(new HomeCardsAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Log.d("postion",""+position);
                Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);
                intent.putExtra("station_id", position);
                startActivity(intent);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateData();
             }
        });
    }


    public void updateData(){
        if(checkInternetConnection()){
            noInternetTextView.setVisibility(View.GONE);
            new fetchLastDataFromDB().execute();
        }
        else {
            noInternetTextView.setVisibility(View.VISIBLE);
            Snackbar.make(findViewById(R.id.content_home), "Aucune connexion internet", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();}

    }

    private boolean checkInternetConnection() {
        ConnectivityManager cm = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if(activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            TelephonyManager mTelephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
            int networkType = mTelephonyManager.getNetworkType();
            switch (networkType) {
                case TelephonyManager.NETWORK_TYPE_GPRS:
                case TelephonyManager.NETWORK_TYPE_EDGE:
                case TelephonyManager.NETWORK_TYPE_CDMA:
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                case TelephonyManager.NETWORK_TYPE_IDEN:
                    return false;
                default:
                    return true;
            }
        }

        return false;

    }

}
