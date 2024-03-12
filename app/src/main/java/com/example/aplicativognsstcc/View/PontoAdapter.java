package com.example.aplicativognsstcc.View;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aplicativognsstcc.R;

import java.util.List;

public class PontoAdapter extends RecyclerView.Adapter<PontoAdapter.PontoViewHolder> {

    private List<Ponto> pontos;
    private OnPontoClickListener listener;

    public PontoAdapter(List<Ponto> pontos, OnPontoClickListener listener) {
        this.pontos = pontos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PontoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_main, parent, false);
        return new PontoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PontoViewHolder holder, int position) {
        Ponto ponto = pontos.get(position);
        holder.bind(ponto);
    }

    @Override
    public int getItemCount() {
        return pontos.size();
    }

    public class PontoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView textView;
        private Ponto ponto;

        public PontoViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.PontoSelecionado);
            itemView.setOnClickListener(this);
        }

        public void bind(Ponto ponto) {
            this.ponto = ponto;
            textView.setText(ponto.getNome());
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onPontoClick(ponto);
            }
        }
    }

    public interface OnPontoClickListener {
        void onPontoClick(Ponto ponto);
    }
}

