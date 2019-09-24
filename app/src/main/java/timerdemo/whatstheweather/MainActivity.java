package timerdemo.whatstheweather;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    ImageView beratImageView;
    ImageView weatherImageView;
    TextView textView;
    EditText cityName;
    Button weatherButton;
    EditText resultText;


    public void splash(View view) {
        beratImageView.setVisibility(View.INVISIBLE);
        textView.setVisibility(View.INVISIBLE);
        weatherImageView.setVisibility(View.VISIBLE);
        weatherButton.setVisibility(View.VISIBLE);
        cityName.setVisibility(View.VISIBLE);
        resultText.setVisibility(View.VISIBLE);

    }

    public void findWeather(View view) {

        Log.i("cityName", cityName.getText().toString());

        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(cityName.getWindowToken(), 0);

        try {


            DownloadTask task = new DownloadTask();

            task.execute("http://api.openweathermap.org/data/2.5/weather?q=" + cityName.getText().toString() + "&appid=46bffb94d055673d0f2ce6c180fe64eb");
        } catch (Exception e) {

            e.printStackTrace();

        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        beratImageView = (ImageView) findViewById(R.id.berat);
        weatherImageView = (ImageView) findViewById(R.id.weatherImageView);

        textView = (TextView) findViewById(R.id.textView);
        cityName = (EditText) findViewById(R.id.cityName);
        resultText = (EditText) findViewById(R.id.resultText);

        weatherButton = (Button) findViewById(R.id.weatherButton);


    }

    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL(urls[0]);

                urlConnection = (HttpURLConnection) url.openConnection();

                InputStream in = urlConnection.getInputStream();

                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();

                while (data != -1) {

                    char current = (char) data;

                    result += current;

                    data = reader.read();

                }

                return result;

            } catch (Exception e) {

                Toast.makeText(getApplicationContext(), "Could not find weather", Toast.LENGTH_LONG);

            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {


                String temperature = "";

                JSONObject jsonObject = new JSONObject(result);

                JSONObject main = jsonObject.getJSONObject("main");

                temperature = main.getString("temp");


                if (temperature != "") {

                        resultText.setText(temperature);


                    } else {

                        Toast.makeText(getApplicationContext(), "Could not find weather", Toast.LENGTH_LONG);
                    }



                }catch (Exception e) {

                Toast.makeText(getApplicationContext(), "Could not find weather", Toast.LENGTH_LONG);

            }



        }
    }



}