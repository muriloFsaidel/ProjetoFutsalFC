package cursoandroid.whatsappandroid.com.futsalfc.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.solver.widgets.Snapshot;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import cursoandroid.whatsappandroid.com.futsalfc.R;
import cursoandroid.whatsappandroid.com.futsalfc.config.ConfiguracaoFirebase;
import cursoandroid.whatsappandroid.com.futsalfc.helper.Preferencias;
import cursoandroid.whatsappandroid.com.futsalfc.template.Estatistica;
import cursoandroid.whatsappandroid.com.futsalfc.template.Jogador;

public class estatisticas_gerais extends Activity {

    private ListView lvAll;
    private ArrayAdapter adapterAll;
    private ArrayList<String> alAll;
    private ImageView voltar;
    private DatabaseReference firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estatisticas_gerais);

        lvAll = (ListView) findViewById(R.id.LVAllId);
        voltar = (ImageView) findViewById(R.id.BotaoVoltarEstatisticasGeraisId);

        alAll = new ArrayList<>();

        adapterAll = new ArrayAdapter(
                getApplicationContext(),
                R.layout.lista_personalizada,
                alAll
        );

        lvAll.setAdapter(adapterAll);

        Preferencias preferencias = new Preferencias(estatisticas_gerais.this);
        String idEquipe = preferencias.getIdentificadorEquipe();
        String idUsuario = preferencias.getIdentificadorUsuario();

        firebase = ConfiguracaoFirebase.getFirebase().child("estatisticas").child(idEquipe).child(idUsuario);

        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                alAll.clear();

                for (DataSnapshot dados: snapshot.getChildren()){

                    Estatistica estatistica = dados.getValue(Estatistica.class);
                    String gols = String.valueOf(estatistica.getGols());
                    String ass = String.valueOf(estatistica.getAssistencia());
                    String cartaoAmarelo = String.valueOf(estatistica.getCartaoAmarelo());
                    String cartaoVermelho = String.valueOf(estatistica.getCartaoVermelho());

                    alAll.add(estatistica.getJogador()+" "+gols+" "+ass+" "+cartaoAmarelo+" "+cartaoVermelho);
                }
                adapterAll.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void voltarParaTelaPrincipal(View view){
        Intent intent = new Intent(estatisticas_gerais.this, MainActivity.class);
        startActivity(intent);
    }
}
