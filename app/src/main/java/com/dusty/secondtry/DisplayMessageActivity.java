package com.dusty.secondtry;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dusty.secondtry.tripmodel.tripmodel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DisplayMessageActivity extends Activity {

    private TextView outputView;
    ListView lvVacations;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);
        TextView textView = new TextView(this);
        Intent intent = getIntent();
        String message = intent.getStringExtra(MyActivity.EXTRA_MESSAGE);
        textView.setTextSize(40);
        textView.setText(message);
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.content);
        layout.addView(textView);

        outputView = (TextView) findViewById(R.id.postOutput);

        lvVacations = (ListView)findViewById(R.id.lvVacation);
        new JSONTask().execute("http://ec2-54-213-159-144.us-west-2.compute.amazonaws.com:3001/vacationlist");

    }

    public class JSONTask extends AsyncTask<String, String,List <tripmodel> > {

        @Override
        protected List <tripmodel> doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            StringBuffer buffer = new StringBuffer();

            try{
//
                URL url = new URL(params[0]);
                connection = (HttpURLConnection)url.openConnection();
//            //sets the connection up for sending data
//            connection.setDoOutput(true);
//            connection.setChunkedStreamingMode(0);
//            connection.setRequestMethod("GET");
                // give it 15 seconds to respond
//          connection.setReadTimeout(15*1000);
                connection.connect();
                InputStream inStream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(inStream));


                String line ="";
                while((line = reader.readLine())!=null)
                {
                    buffer.append(line);
                }
//
                String finalJSON = buffer.toString();
//                JSONObject parentObject = new JSONObject(finalJSON);
                JSONArray parentArray = new JSONArray(finalJSON);

                List <tripmodel> vacationModelList = new ArrayList<>();

                String locationRec = "";
                String idRec = "";
                String nameRec = "";
                String daysRec = "";
                String costRec = "";
                String demographic ="";

                for (int i = 0; i < parentArray.length(); i++)
                {
                    tripmodel tripModel = new tripmodel();
                    JSONObject finalObject = parentArray.getJSONObject(i);
//                    http://stackoverflow.com/questions/10588763/android-json-and-null-values
                    if(finalObject.isNull("_id")) {
                        idRec = null;
                    } else {
                        idRec = finalObject.getString("_id");
                    }

                    if(finalObject.isNull("name")) {
                        nameRec = null;
                    } else {
                        nameRec = finalObject.getString("name");
                    }

                    if(finalObject.isNull("location")) {
                        locationRec = null;
                    } else {
                        locationRec = "Location: " + finalObject.getString("location");
                    }

                    if(finalObject.isNull("days")) {
                        daysRec = null;
                    } else {
                        daysRec = "Duration: " + finalObject.getString("days");
                    }

                    if(finalObject.isNull("cost")) {
                        costRec = null;
                    } else {
                        costRec = "Cost: " + finalObject.getString("cost");
                    }

                    tripModel.set_id(idRec);
                    tripModel.setLocation(locationRec);
                    tripModel.setName(nameRec);
                    tripModel.setDays(daysRec);
                    tripModel.setCost(costRec);
////                    Need to implement the actual activities list after finishing this.
                    vacationModelList.add(tripModel);
                }

//                tripmodel testModel = vacationModelList.get(0);
//                String output = testModel.getLocation();

                return vacationModelList;

            }
            catch(MalformedURLException e){
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if(connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null){
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(List <tripmodel> result) {
            super.onPostExecute(result);
            vacationAdapter adapter = new vacationAdapter(getApplicationContext(), R.layout.vacationrow, result);
            lvVacations.setAdapter(adapter);
            outputView.setText("filler");
        }
    }

    public class vacationAdapter extends ArrayAdapter
    {

        private List<tripmodel> vacationModelList;
        private int resource;
        private LayoutInflater inflater;

        public vacationAdapter(Context context, int resource, List objects) {
            super(context, resource, objects);
            vacationModelList = objects;
            this.resource = resource;
            inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null){
                convertView = inflater.inflate(R.layout.vacationrow, null);
            }

            TextView xlocation;
            TextView xname;
            TextView xdays;
            TextView xcost;

            xlocation = (TextView)convertView.findViewById(R.id.xlocation);
            xname = (TextView)convertView.findViewById(R.id.xname);
            xdays = (TextView)convertView.findViewById(R.id.xdays);
            xcost = (TextView)convertView.findViewById(R.id.xcost);

            xlocation.setText(vacationModelList.get(position).getLocation());
            xname.setText(vacationModelList.get(position).getName());
            xdays.setText(vacationModelList.get(position).getDays());
            xcost.setText(vacationModelList.get(position).getCost());

            return convertView;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_display_message, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);

    }
}
