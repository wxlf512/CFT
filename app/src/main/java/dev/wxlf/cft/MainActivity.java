package dev.wxlf.cft;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private TextView mTxt;
    ArrayList<Currency> currencies = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTxt = findViewById(R.id.txt);

        OkHttpClient client = new OkHttpClient();
        Request req = new Request.Builder().get().url("https://www.cbr-xml-daily.ru/daily_json.js").build();
        Call call = client.newCall(req);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast toast = new Toast(getApplicationContext());
                toast.setText("Получить не удалось :(");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                assert response.body() != null;
                final String responseStr = response.body().string();
                runOnUiThread(() -> mTxt.setText(responseStr));

            }
        });
        setTestData();
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        CurrencyAdapter adapter = new CurrencyAdapter(this, currencies);
        recyclerView.setAdapter(adapter);
    }

    private void setTestData() {
        currencies.add(new Currency("USD", "Американский доллар", 99.9999));
        currencies.add(new Currency("EUR", "Евро", 109.9999));
        currencies.add(new Currency("BYN", "Беларусский рубль", 33.0903));
    }
}