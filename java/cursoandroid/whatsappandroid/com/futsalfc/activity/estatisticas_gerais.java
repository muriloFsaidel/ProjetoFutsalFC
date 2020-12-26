package cursoandroid.whatsappandroid.com.futsalfc.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.solver.widgets.Snapshot;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Comparator;

import cursoandroid.whatsappandroid.com.futsalfc.Adapter.EstatisticasAdaptador;
import cursoandroid.whatsappandroid.com.futsalfc.R;
import cursoandroid.whatsappandroid.com.futsalfc.config.ConfiguracaoFirebase;
import cursoandroid.whatsappandroid.com.futsalfc.helper.Preferencias;
import cursoandroid.whatsappandroid.com.futsalfc.template.Equipe;
import cursoandroid.whatsappandroid.com.futsalfc.template.Estatistica;
import cursoandroid.whatsappandroid.com.futsalfc.template.Jogador;

public class estatisticas_gerais extends AppCompatActivity {

    private ListView lvAll;
    private ArrayAdapter adapterAll;
    private ArrayList<Estatistica> alAll;
    private ImageView voltar;
    private DatabaseReference firebase;
    private ValueEventListener valueEventListenerEstatisticas;
    private Toolbar toolbar;
    private TextView textView;
    private String idEquipe;
    private String idJogadorUsuario;


    @Override
    public void onStart(){
        super.onStart();
        firebase.addValueEventListener(valueEventListenerEstatisticas);
        Log.i("EventEstatistica","onStart");
    }

    @Override
    public void onStop(){
        super.onStop();
        firebase.removeEventListener(valueEventListenerEstatisticas);
        Log.i("EventEstatistica", "onStop");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estatisticas_gerais);

        lvAll = (ListView) findViewById(R.id.LVAllId);
        voltar = (ImageView) findViewById(R.id.BotaoVoltarEstatisticasGeraisId);
        toolbar = (Toolbar) findViewById(R.id.tbEstatisticasGerais);
        textView = (TextView) findViewById(R.id.tvNomeTime);

        toolbar.setTitle("Estatísticas");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
        setSupportActionBar(toolbar);

        alAll = new ArrayList<>();

        /*padrão
        adapterAll = new ArrayAdapter(
                getApplicationContext(),
                R.layout.lista_personalizada,
                alAll
        );*/

        //personalizado
        adapterAll = new EstatisticasAdaptador(getApplicationContext(), alAll);

        lvAll.setAdapter(adapterAll);

        Bundle retorno = getIntent().getExtras();

        if(retorno != null){
            preencherNomeDoTimeJogadorUsuario();
            Preferencias preferencias = new Preferencias(estatisticas_gerais.this);
            idEquipe = preferencias.getIdentificadorEquipe();
            idJogadorUsuario = preferencias.getJogadorUsuario();

            firebase = ConfiguracaoFirebase.getFirebase().child("estatisticas").child(idEquipe).child(idJogadorUsuario);

            valueEventListenerEstatisticas = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    alAll.clear();

                    for (DataSnapshot dados: snapshot.getChildren()){

                        Estatistica estatistica = dados.getValue(Estatistica.class);

                        alAll.add(estatistica);
                        //alAll.add(estatistica.getJogador()+" "+gols+" "+ass+" "+cartaoAmarelo+" "+cartaoVermelho);
                    }
                    adapterAll.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            };
        }else {
            preencherNomeDoTime();

            Preferencias preferencias = new Preferencias(estatisticas_gerais.this);
            String idEquipe = preferencias.getIdentificadorEquipe();
            String idUsuario = preferencias.getIdentificadorUsuario();

            firebase = ConfiguracaoFirebase.getFirebase().child("estatisticas").child(idEquipe).child(idUsuario);

            valueEventListenerEstatisticas = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    alAll.clear();

                    for (DataSnapshot dados: snapshot.getChildren()){

                        Estatistica estatistica = dados.getValue(Estatistica.class);

                        alAll.add(estatistica);
                        //alAll.add(estatistica.getJogador()+" "+gols+" "+ass+" "+cartaoAmarelo+" "+cartaoVermelho);
                    }
                    adapterAll.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            };
        }


    }

    public void voltarParaTelaPrincipal(View view){

        Bundle retornoFragment = getIntent().getExtras();

        if(retornoFragment != null){
            Intent intent = new Intent(estatisticas_gerais.this, MainActivity2.class);
            intent.putExtra("MainActivity2", "MainActivity2");
            startActivity(intent);
        }else{
            Intent intent = new Intent(estatisticas_gerais.this, MainActivity.class);
            startActivity(intent);
        }

    }

    public void preencherNomeDoTime(){
        Preferencias preferencias = new Preferencias(estatisticas_gerais.this);
        String idUsuario = preferencias.getIdentificadorUsuario();

        firebase = ConfiguracaoFirebase.getFirebase().child("equipes").child(idUsuario);

        firebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dados: snapshot.getChildren()){
                    Equipe equipe = dados.getValue(Equipe.class);
                    textView.setText(equipe.getNome());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void preencherNomeDoTimeJogadorUsuario(){
        Preferencias preferencias = new Preferencias(estatisticas_gerais.this);
        String idUsuario = preferencias.getJogadorUsuario();

        firebase = ConfiguracaoFirebase.getFirebase().child("equipes").child(idUsuario);

        firebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dados: snapshot.getChildren()){
                    Equipe equipe = dados.getValue(Equipe.class);
                    textView.setText(equipe.getNome());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    };
}
