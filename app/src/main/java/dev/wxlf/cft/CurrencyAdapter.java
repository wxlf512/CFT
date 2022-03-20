package dev.wxlf.cft;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.StatefulAdapter;

import java.util.List;

public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyAdapter.ViewHolder> {

    private final LayoutInflater inflater;
    private final List<Currency> currencies;

    CurrencyAdapter(Context context, List<Currency> currencies) {
        this.currencies = currencies;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Currency currency = currencies.get(position);
        holder.nameView.setText(currency.getName());
        holder.charCodeView.setText(currency.getCharCode());
        holder.valueView.setText(currency.getValue().toString());
    }

    @Override
    public int getItemCount() {
        return currencies.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView charCodeView, nameView, valueView;
        ViewHolder(View view) {
            super(view);
            charCodeView = view.findViewById(R.id.charCode);
            nameView = view.findViewById(R.id.name);
            valueView = view.findViewById(R.id.value);
        }
    }
}


