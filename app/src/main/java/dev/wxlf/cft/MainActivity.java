package dev.wxlf.cft;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    public int selectedPos;
    ArrayList<Currency> currencies = new ArrayList<>();
    final private String[] curList = {"AUD", "AZN", "GBP", "AMD", "BYN", "BGN", "BRL", "HUF", "HKD",
            "DKK", "USD", "EUR", "INR", "KZT", "CAD", "KGS", "CNY", "MDL", "NOK", "PLN", "RON",
            "XDR", "SGD", "TJS", "TRY", "TMT", "UZS", "UAH", "CZK", "SEK", "CHF", "ZAR", "KRW", "JPY"};
    private CurrencyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getInfoFromCB();
        //setTestData();
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        CurrencyAdapter.OnCurrencyClickListener currencyClickListener = new CurrencyAdapter.OnCurrencyClickListener() {
            @Override
            public void onCurrencyClick(Currency currency, int position) {
                Toast.makeText(getApplicationContext(), "Выбран " + currency.getName(), Toast.LENGTH_SHORT).show();
                selectedPos = position;
            }
        };
        adapter = new CurrencyAdapter(this, currencies, currencyClickListener);
        recyclerView.setAdapter(adapter);
    }

    private void getInfoFromCB() {
        OkHttpClient client = new OkHttpClient();
        Request req = new Request.Builder().get().url("https://www.cbr-xml-daily.ru/daily_json.js").build();
        Call call = client.newCall(req);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(getApplicationContext(),"Получить не удалось :(", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                assert response.body() != null;
                final String responseStr = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonRoot = new JSONObject(responseStr);
                            JSONObject jsonCurrencies = jsonRoot.getJSONObject("Valute");
                            for (int i = 0; i < curList.length; i++) {
                                JSONObject currency = jsonCurrencies.getJSONObject(curList[i]);
                                Log.i(curList[i], jsonCurrencies.getString(curList[i]));
                                currencies.add(new Currency(currency.getString("CharCode"),
                                        currency.getString("Name"),
                                        currency.getString("Value")));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    public void updateList(View view) {
        getInfoFromCB();
        adapter.notifyDataSetChanged();
    }
}