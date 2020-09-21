package cursoandroid.whatsappandroid.com.futsalfc.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.nio.file.ProviderNotFoundException;
import java.util.ArrayList;

import cursoandroid.whatsappandroid.com.futsalfc.R;
import cursoandroid.whatsappandroid.com.futsalfc.config.ConfiguracaoFirebase;
import cursoandroid.whatsappandroid.com.futsalfc.helper.Preferencias;
import cursoandroid.whatsappandroid.com.futsalfc.template.DadosPartida;

public class RetornoDadosPartida extends AppCompatActivity {

    private TextView partida;
    private String idPartida;

    private ListView LVDados;
    private ArrayAdapter<String> AADados;
    private ArrayList<String> ALDados;

    private String data;
    private String oponente;
    private int quadro;

    private DatabaseReference firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retorno_dados_partida);

        LVDados = (ListView) findViewById(R.id.LVRetornoId);
        partida = (TextView) findViewById(R.id.TVPartidaId);

        Bundle dadosRecebidos = getIntent().getExtras();

        ALDados = new ArrayList<>();
        AADados = new ArrayAdapter<String>(
                getApplicationContext(),
                R.layout.lista_personalizada,
                ALDados
        );
        LVDados.setAdapter(AADados);

        Preferencias preferencias = new Preferencias(RetornoDadosPartida.this);
        String idEquipe = preferencias.getIdentificadorEquipe();
        String idUsuarioLogado = preferencias.getIdentificadorUsuario();

        if(dadosRecebidos != null){
           String equipe = dadosRecebidos.getString("equipe");
           String placarCasa = dadosRecebidos.getString("placarEquipe");
           String placarOponente = dadosRecebidos.getString("placarOponente");
           data = dadosRecebidos.getString("data");
           oponente = dadosRecebidos.getString("oponente");
           quadro = dadosRecebidos.getInt("quadro");

           partida.setText(equipe+" \n"+placarCasa+" X "+placarOponente+"\n "+oponente+"\n\n Total de Gols "+equipe+": "+placarCasa);
        }

        //Toast.makeText(getApplicationContext(),idPartida,Toast.LENGTH_LONG).show();

        firebase = ConfiguracaoFirebase.getFirebase().child("dadosPartidas").child(idEquipe).child(idUsuarioLogado);

        firebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dados: snapshot.getChildren()){
                    DadosPartida dadosPartida = dados.getValue(DadosPartida.class);
                    if(dadosPartida.getData().equals(data) && dadosPartida.getOponente().equals(oponente) && dadosPartida.getQuadro() == quadro ){
                        ALDados.add(dadosPartida.getJogador()+" "+dadosPartida.getGols()+" "+dadosPartida.getAssistencia()+" "+dadosPartida.getCartaoAmarelo()+" "+dadosPartida.getCartaoVermelho());
                    }

                }
                AADados.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void voltar5(View view){
        Intent intent = new Intent(RetornoDadosPartida.this, TabelaJogos.class);
        startActivity(intent);
        finish();
    }
}

//idPartida = dadosRecebidos.getString("idPartida");