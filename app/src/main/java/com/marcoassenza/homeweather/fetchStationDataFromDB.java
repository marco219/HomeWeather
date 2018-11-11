package com.marcoassenza.homeweather;

import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by marcoassenza on 13/10/2017.
 */

public class fetchStationDataFromDB extends AsyncTask<Integer, Void, Boolean> {

    List<Entry> entries = new ArrayList<>();
    List<Entry> reversedEntries = new ArrayList<>();
    List<String> values = new ArrayList<>();

    @Override
    protected Boolean doInBackground(Integer...params) {
        BufferedReader reader;

        try {
            URL url = new URL("http://lis.ddns.net/getStationData.php?station_id=station_" + params[0]);
            Log.d("url",url.toString());
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            conn.setConnectTimeout(5000); //set timeout to 5 seconds
            conn.setReadTimeout(5000);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.flush();

            // Get the server response
            SimpleDateFormat sqlFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            SimpleDateFormat simpleTime = new SimpleDateFormat("HH:mm - dd/MM");
            SimpleDateFormat graphTime = new SimpleDateFormat("HH:mm");
            DecimalFormat decimalFormat = new DecimalFormat("#.#");
            decimalFormat.setRoundingMode(RoundingMode.CEILING);

            DetailsActivity.details_cards =new ArrayList<>();


            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                JSONArray jsonArray = new JSONArray(line);

                for (int i = 0; (i < jsonArray.length()-1) && (i < 48); i++) { //1 jour avec deux mesure/heure
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Date date = sqlFormat.parse(jsonObject.getString("time"));
                    float temp  = jsonObject.getInt("temperature");
                    String temperature = decimalFormat.format(temp/1000);

                    DetailsActivity.details_cards.add(new DetailsCard(temperature, simpleTime.format(date)));
                    entries.add(new Entry(i,Float.valueOf(temperature.replace(",","."))));
                    values.add(0,graphTime.format(date));

                }
            }
            return true;

        }catch (java.net.SocketTimeoutException e) {
            //message = getString(R.string.no_internet_connection);
            return false;
        }catch (java.net.UnknownHostException e) {
            //message = getString(R.string.no_internet_connection);
            return false;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

        @Override
    protected void onPostExecute(final Boolean success) {
        if (success) {

            int j = 0;
            for(int i=entries.size()-1;i>=0;i--,j++){
                reversedEntries.add(new Entry(j,entries.get(i).getY()));
            }
            String[] valuesArray = values.toArray(new String[values.size()]);
            XAxis xAxis = DetailsActivity.chart.getXAxis();
            xAxis.setValueFormatter(new MyXAxisValueFormatter(valuesArray));
            LineDataSet dataSet = new LineDataSet(reversedEntries, "Température en °C");


            dataSet.setDrawCircles(true);
            dataSet.setDrawFilled(true);
            dataSet.setDrawValues(false);
            dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            dataSet.setHighlightEnabled(true); // allow highlighting for DataSet

            // set this to false to disable the drawing of highlight indicator (lines)
            dataSet.setDrawHighlightIndicators(true);
            dataSet.setHighLightColor(ContextCompat.getColor(DetailsActivity.details_context, R.color.colorPrimary)); // color for highlight indicator
            dataSet.setFillColor(ContextCompat.getColor(DetailsActivity.details_context, R.color.colorAccent));
            dataSet.setCircleColor(ContextCompat.getColor(DetailsActivity.details_context, R.color.colorAccent));
            dataSet.setColor(ContextCompat.getColor(DetailsActivity.details_context, R.color.colorAccent));

            LineData lineData = new LineData(dataSet);
            DetailsActivity.chart.setData(lineData);
            DetailsActivity.chart.invalidate(); // refresh

            DetailsActivity.details_rv.setAdapter(DetailsActivity.details_ca);
            DetailsActivity.details_ca = new DetailsCardsAdapter(DetailsActivity.details_cards);
            } else {
        }
    }
}
