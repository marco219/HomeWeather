package com.marcoassenza.homeweather;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;

import java.util.List;


public class DetailsActivity extends AppCompatActivity {

    public static RecyclerView details_rv;
    private LinearLayoutManager details_lm;
    public static DetailsCardsAdapter details_ca;
    public static List<DetailsCard> details_cards;
    public static Context details_context;
    private TextView noInternetTextView;

    public static LineChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        noInternetTextView= (TextView) findViewById(R.id.no_internet);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        final int station_id = intent.getIntExtra("station_id",0);
        setTitle(HomeCardsAdapter.stations[station_id]);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        details_context = getBaseContext();
        details_rv = (RecyclerView)findViewById(R.id.weather_details_rv);
        details_rv.setHasFixedSize(true);

        details_lm = new LinearLayoutManager(this);
        details_rv.setLayoutManager(details_lm);
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(details_rv.getContext(), details_lm.getOrientation());
        details_rv.addItemDecoration(mDividerItemDecoration);

        details_ca = new DetailsCardsAdapter(details_cards);

        updateData(station_id);

        chart = (LineChart) findViewById(R.id.chart);
        MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);
        mv.setChartView(chart); // For bounds control
        chart.setMarker(mv); // Set the marker to the chart
        chart.setNoDataText("");
        chart.getDescription().setEnabled(false);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            details_rv.setVisibility(View.GONE);
            chart.getLayoutParams().height = RecyclerView.LayoutParams.MATCH_PARENT;
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                updateData(station_id);
            }
        });
    }

    public void updateData(int station_id){
        if(checkInternetConnection()){
            noInternetTextView.setVisibility(View.GONE);
            new fetchStationDataFromDB().execute(station_id);
        }
        else {
            noInternetTextView.setVisibility(View.VISIBLE);
            Snackbar.make(findViewById(R.id.content_weather_details), "Aucune connexion internet", Snackbar.LENGTH_LONG)
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
