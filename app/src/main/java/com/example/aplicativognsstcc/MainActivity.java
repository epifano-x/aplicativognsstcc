package com.example.aplicativognsstcc;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import com.example.aplicativognsstcc.View.Ponto;
import com.example.aplicativognsstcc.View.PontoAdapter;

public class MainActivity extends AppCompatActivity {

    private TextView pontoSelecionadoTextView;
    private PontoAdapter pontoAdapter;
    private Ponto pontoSelecionado;
    private List<Ponto> pontos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pontoSelecionadoTextView = findViewById(R.id.PontoSelecionado);
        RecyclerView recyclerView = findViewById(R.id.ListaPontos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        pontos = new ArrayList<>();
        pontos.add(new Ponto("Ponto 1"));
        pontos.add(new Ponto("Ponto 2"));
        pontos.add(new Ponto("Ponto 3"));
        pontos.add(new Ponto("Ponto 4"));

        pontoAdapter = new PontoAdapter(pontos, new PontoAdapter.OnPontoClickListener() {
            @Override
            public void onPontoClick(Ponto ponto) {
                pontoSelecionado = ponto;
                pontoSelecionadoTextView.setText(ponto.getNome());
            }
        });

        recyclerView.setAdapter(pontoAdapter);

        Button selecionarButton = findViewById(R.id.Selecionar);
        selecionarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirListaPontos();
            }
        });
    }

    private void abrirListaPontos() {
        // Criar uma lista de nomes de pontos para exibir na caixa de diálogo
        String[] nomesPontos = new String[pontos.size()];
        for (int i = 0; i < pontos.size(); i++) {
            nomesPontos[i] = pontos.get(i).getNome();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Selecione um ponto");
        builder.setItems(nomesPontos, (dialog, which) -> {
            pontoSelecionado = pontos.get(which);
            pontoSelecionadoTextView.setText(pontoSelecionado.getNome());
            Toast.makeText(MainActivity.this, "Ponto selecionado: " + pontoSelecionado.getNome(), Toast.LENGTH_SHORT).show();
        });
        builder.show();
    }
}
