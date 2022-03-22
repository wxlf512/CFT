package dev.wxlf.cft;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
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

    private String selectedPos;
    final private ArrayList<Currency> currencies = new ArrayList<>();
    final private String[] curList = {"AUD", "AZN", "GBP", "AMD", "BYN", "BGN", "BRL", "HUF", "HKD",
            "DKK", "USD", "EUR", "INR", "KZT", "CAD", "KGS", "CNY", "MDL", "NOK", "PLN", "RON",
            "XDR", "SGD", "TJS", "TRY", "TMT", "UZS", "UAH", "CZK", "SEK", "CHF", "ZAR", "KRW", "JPY"};
    private CurrencyAdapter adapter;
    private JSONObject jsonCurrencies;
    private EditText rubText;
    private TextView valText, rvalText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rubText = findViewById(R.id.rub);
        valText = findViewById(R.id.val);
        rvalText = findViewById(R.id.rval);

        rubText.setOnKeyListener(new View.OnKeyListener()
                                  {
                                      @SuppressLint({"SetTextI18n", "DefaultLocale"})
                                      public boolean onKey(View v, int keyCode, KeyEvent event)
                                      {
                                          if(event.getAction() == KeyEvent.ACTION_UP)
                                          {
                                              if (rubText.getText().length() != 0)
                                                  try {
                                                      double cur = jsonCurrencies.getJSONObject(selectedPos).getDouble("Value");
                                                      double rub = Double.parseDouble(rubText.getText().toString());
                                                      valText.setText(String.format("%.2f", rub / cur));
                                                  } catch (JSONException e) {
                                                      e.printStackTrace();
                                                  }
                                              else
                                                  valText.setText("0.00");
                                              rvalText.setText("RUB/" + selectedPos);
                                              valText.setHint(selectedPos);
                                              return true;
                                          }
                                          return false;
                                      }
                                  }
        );
        getInfoFromCB();
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        CurrencyAdapter.OnCurrencyClickListener currencyClickListener = new CurrencyAdapter.OnCurrencyClickListener() {
            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            @Override
            public void onCurrencyClick(Currency currency, int position) {
                //Toast.makeText(getApplicationContext(), "Выбран " + currency.getName(), Toast.LENGTH_SHORT).show();
                selectedPos = currency.getCharCode();
                if (rubText.getText().length() != 0)
                    try {
                        double cur = jsonCurrencies.getJSONObject(selectedPos).getDouble("Value");
                        double rub = Double.parseDouble(rubText.getText().toString());
                        valText.setText(String.format("%.2f", rub / cur));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                else
                    valText.setText("0.00");
                rvalText.setText("RUB/" + selectedPos);
                valText.setHint(selectedPos);
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
                            jsonCurrencies = jsonRoot.getJSONObject("Valute");
                            for (int i = 0; i < curList.length; i++) {
                                JSONObject currency = jsonCurrencies.getJSONObject(curList[i]);
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

    @SuppressLint("NotifyDataSetChanged")
    public void updateList(View view) {
        getInfoFromCB();
        adapter.notifyDataSetChanged();
    }
}