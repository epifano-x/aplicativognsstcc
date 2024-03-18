package com.example.aplicativognsstcc;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aplicativognsstcc.View.Ponto;
import com.example.aplicativognsstcc.View.PontoAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //Pontos
    private TextView pontoSelecionadoTextView;
    private PontoAdapter pontoAdapter;
    private Ponto pontoSelecionado;
    private List<Ponto> pontos;

    //Iniciar
    private Button iniciarButton;
    private Button selecionarButton; // Botão para selecionar pontos
    private TextView tempoTextView;
    private CountDownTimer countDownTimer;
    private boolean isTimerRunning = false;

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
                // Permitir alterar o ponto apenas se o contador não estiver em execução
                if (!isTimerRunning) {
                    pontoSelecionado = ponto;
                    pontoSelecionadoTextView.setText(ponto.getNome());
                }
            }
        });

        recyclerView.setAdapter(pontoAdapter);

        selecionarButton = findViewById(R.id.Selecionar);
        selecionarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isTimerRunning) { // Permitir abrir a lista de pontos apenas se o contador não estiver em execução
                    abrirListaPontos();
                }
            }
        });

        // Inicializar botão de iniciar e texto de tempo
        iniciarButton = findViewById(R.id.Iniciar);
        tempoTextView = findViewById(R.id.Tempo);

        iniciarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pontoSelecionado != null) {
                    if (isTimerRunning) {
                        // Parar e zerar o contador de tempo
                        stopTimer();
                    } else {
                        // Iniciar o contador de tempo
                        startTimer();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Selecione um ponto antes de iniciar o contador.", Toast.LENGTH_SHORT).show();
                }
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
            if (!isTimerRunning) { // Permitir selecionar um novo ponto apenas se o contador não estiver em execução
                pontoSelecionado = pontos.get(which);
                pontoSelecionadoTextView.setText(pontoSelecionado.getNome());
            }
        });
        builder.show();
    }

    private void startTimer() {
        // Definir a duração do contador (em milissegundos)
        long durationMillis = Long.MAX_VALUE;

        // Criar um novo CountDownTimer
        countDownTimer = new CountDownTimer(durationMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Atualizar o TextView do tempo restante a cada segundo
                updateTimerText(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                // O código dentro deste método não será executado porque o contador não será finalizado
                // A menos que seja interrompido manualmente pelo usuário
            }
        };

        // Iniciar o contador de tempo
        countDownTimer.start();
        isTimerRunning = true;
        iniciarButton.setText("Parar");
        selecionarButton.setEnabled(false); // Desativar o botão de selecionar pontos enquanto o contador estiver em execução
    }

    private void stopTimer() {
        // Parar e zerar o contador de tempo
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        tempoTextView.setText("00:00");
        isTimerRunning = false;
        iniciarButton.setText("Iniciar");
        selecionarButton.setEnabled(true); // Reativar o botão de selecionar pontos quando o contador for parado
    }

    private void updateTimerText(long millisUntilFinished) {
        // Converter o tempo restante para minutos e segundos
        int minutes = (int) ((Long.MAX_VALUE - millisUntilFinished) / 1000) / 60;
        int seconds = (int) ((Long.MAX_VALUE - millisUntilFinished) / 1000) % 60;

        // Formatar a string de tempo restante (por exemplo, "01:30")
        String timeLeftFormatted = String.format("%02d:%02d", minutes, seconds);

        // Atualizar o TextView com o tempo restante
        tempoTextView.setText(timeLeftFormatted);
    }

}
