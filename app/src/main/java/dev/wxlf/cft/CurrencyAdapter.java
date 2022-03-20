package dev.wxlf.cft;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.StatefulAdapter;

import java.util.List;

public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyAdapter.ViewHolder> {

    interface OnCurrencyClickListener {
        void onCurrencyClick(Currency currency, int position);
    }

    private final OnCurrencyClickListener onClickListener;
    private final LayoutInflater inflater;
    private final List<Currency> currencies;

    CurrencyAdapter(Context context, List<Currency> currencies, OnCurrencyClickListener onClickListener) {
        this.onClickListener = onClickListener;
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
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Currency currency = currencies.get(position);
        holder.nameView.setText(currency.getName());
        holder.charCodeView.setText(currency.getCharCode());
        holder.valueView.setText(currency.getValue());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onCurrencyClick(currency, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return currencies.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView charCodeView, nameView, valueView;
        final ConstraintLayout mCL;
        ViewHolder(View view) {
            super(view);
            charCodeView = view.findViewById(R.id.charCode);
            nameView = view.findViewById(R.id.name);
            valueView = view.findViewById(R.id.value);
            mCL = view.findViewById(R.id.cl);
        }
    }
}


