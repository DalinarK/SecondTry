package com.dusty.secondtry;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class MyActivity extends Activity{

    public final static String EXTRA_MESSAGE = "com.dustin.secondtry.MESSAGE";
    String costOfVacation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my, menu);
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

    public void sendMessage(View view) {
        EditText editText = (EditText) findViewById(R.id.vacation_spot);

        editText = (EditText) findViewById(R.id.vacation_name);
        String vacation_name = editText.getText().toString();
        editText = (EditText) findViewById(R.id.vacation_spot);
        String vacation_spot = editText.getText().toString();
        editText = (EditText) findViewById(R.id.number_days);
        String number_days = editText.getText().toString();

        new JSONTask().execute("http://ec2-54-213-159-144.us-west-2.compute.amazonaws.com:3001/vacationlist", vacation_name, vacation_spot, number_days);
        Intent intent = new Intent(this, DisplayMessageActivity.class);
//        String message = editText.getText().toString();
//        intent.putExtra(EXTRA_MESSAGE, message);

        startActivity(intent);
    }

//    http://developer.android.com/guide/topics/ui/controls/radiobutton.html
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.costCheap:
                if (checked)
                    costOfVacation = "$";
                    break;
            case R.id.costMed:
                if (checked)
                    costOfVacation = "$$";
                    break;
            case R.id.costExpensive:
                if (checked)
                    costOfVacation = "$$$";
                    break;
        }
    }

    public class JSONTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            StringBuffer buffer = new StringBuffer();

            HttpURLConnection postConnection = null;

            try{
//
                URL url = new URL(params[0]);

                connection = (HttpURLConnection)url.openConnection();
//            //sets the connection up for sending data
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");
                connection.setUseCaches(false);
                connection.setConnectTimeout(10000);
                connection.setReadTimeout(10000);
                connection.setRequestProperty("Content-Type","application/json");
                connection.connect();
                // give it 15 seconds to respond
//          connection.setReadTimeout(15*1000)
                JSONObject postInfo = new JSONObject();
                postInfo.put("location", params[1]);
                String JSONstring = postInfo.toString();

                byte[] outputInBytes = JSONstring.getBytes("UTF-8");
                OutputStream os = connection.getOutputStream();
                os.write( outputInBytes );
                os.close();

                InputStream inStream = connection.getInputStream();

                return null;
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
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }
    }

//    public void onCheckboxClicked(View view) {
//        // Is the view now checked?
//        boolean checked = ((CheckBox) view).isChecked();
//
//        // Check which checkbox was clicked
//        switch(view.getId()) {
//            case R.id.checkbox_meat:
//                if (checked)
//                // Put some meat on the sandwich
//                break;
//            case R.id.checkbox_cheese:
//                if (checked)
//                // Cheese me
//                break;
//            // TODO: Veggie sandwich
//        }
//    }

//    //this is for the spinner
//    spinner = (Spinner) findViewById(R.id.cost);
//
//    ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.cost_choices, android.R.layout.simple_spanner_item);
//    spinner.setAdapter(adapter);
//    spinner.setOnItemSelectedListener(this);
//
//    public void onItemSelected (AdapterView<?> adapterView, View view, int i, long l){
//        TextView myText = (TextView) view;
//        Toast.makeText(this, "You Selected " + myText.getText(), Toast.LENGTH_SHORT).show();
//    }
}
