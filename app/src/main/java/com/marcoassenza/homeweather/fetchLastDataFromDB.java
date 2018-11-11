package com.marcoassenza.homeweather;

import android.os.AsyncTask;

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

import static com.marcoassenza.homeweather.HomeActivity.adapter;
import static com.marcoassenza.homeweather.HomeActivity.homeCards;


/**
 * Created by marcoassenza on 13/10/2017.
 */

public class fetchLastDataFromDB extends AsyncTask<Void, Void, Boolean> {
    @Override
    protected Boolean doInBackground(Void...voids) {
        BufferedReader reader;
        // Send data
        try {
            URL url = new URL("http://lis.ddns.net/getLastData.php");
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            conn.setConnectTimeout(5000); //set timeout to 5 seconds
            conn.setReadTimeout(5000);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.flush();

            // Get the server response
            SimpleDateFormat sqlFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            DecimalFormat decimalFormat = new DecimalFormat("#.#");
            decimalFormat.setRoundingMode(RoundingMode.CEILING);

            homeCards =new ArrayList<>();
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                JSONArray jsonArray = new JSONArray(line);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    Date date = sqlFormat.parse(jsonObject.getString("time"));
                    float temp  = jsonObject.getInt("temperature");
                    String temperature = decimalFormat.format(temp/1000);

                    homeCards.add(new HomeCard(i, temperature, date));
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
            HomeActivity.recyclerView.setAdapter(adapter);
            adapter = new HomeCardsAdapter(homeCards);
            } else {
        }
    }
}
