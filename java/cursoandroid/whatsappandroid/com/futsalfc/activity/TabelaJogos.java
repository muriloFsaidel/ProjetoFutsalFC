package cursoandroid.whatsappandroid.com.futsalfc.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import cursoandroid.whatsappandroid.com.futsalfc.R;
import cursoandroid.whatsappandroid.com.futsalfc.config.ConfiguracaoFirebase;
import cursoandroid.whatsappandroid.com.futsalfc.helper.Preferencias;
import cursoandroid.whatsappandroid.com.futsalfc.template.Partida;

public class TabelaJogos extends AppCompatActivity {

    private EditText ETdate;
    private Button botaoConsultar;
    private CheckBox CHReload;

    private ListView LVAll;
    private ArrayAdapter<String> AAAll;
    private ArrayList<String> ALAll;
    private ArrayList<String> ALIdPartida;
    private ArrayList<Integer> ALPlacarCasa;
    private ArrayList<Integer> ALPlacarOponente;
    private ArrayList<String> ALEquipe;
    private ArrayList<String> ALOponente;
    private ArrayList<String> ALData;
    private ArrayList<Integer> ALQuadro;

    private DatabaseReference firebase;
    private String idEquipe;
    private String idUsuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabela_jogos);

        ETdate = (EditText) findViewById(R.id.ETDateId);
        LVAll = (ListView) findViewById(R.id.LVAllId);
        botaoConsultar = (Button) findViewById(R.id.botaoConsultarId);
        CHReload = (CheckBox) findViewById(R.id.CHReloadId);

        SimpleMaskFormatter formato = new SimpleMaskFormatter("NN/NN/NNNN");
        MaskTextWatcher mascara = new MaskTextWatcher(ETdate,formato);
        ETdate.addTextChangedListener(mascara);

        recoverMatch();

        botaoConsultar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                try{
                    if(ETdate.getText().toString().equals("")){
                        Toast.makeText(getApplicationContext(),"data não preenchida, favor completá-la", Toast.LENGTH_LONG).show();
                    }
                    else{
                        String data =  ETdate.getText().toString();
                        searchByDate(data);
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });

        CHReload.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                recoverMatch();
            }
        });

        LVAll.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id){


                String equipe = ALEquipe.get(position);
                String placarEquipe = String.valueOf(ALPlacarCasa.get(position));
                String placarOponente = String.valueOf(ALPlacarOponente.get(position));
                String oponente = String.valueOf(ALOponente.get(position));
                String data = ALData.get(position);
                int quadro = ALQuadro.get(position);

                Intent intent = new Intent(TabelaJogos.this,RetornoDadosPartida.class);
                intent.putExtra("equipe",equipe);
                intent.putExtra("placarEquipe",placarEquipe);
                intent.putExtra("placarOponente",placarOponente);
                intent.putExtra("oponente",oponente);
                intent.putExtra("data",data);
                intent.putExtra("quadro",quadro);


                startActivity(intent);
            }
        });


    }

    public void recoverMatch(){

        ALAll = new ArrayList<>();
        ALIdPartida = new ArrayList<>();
        ALPlacarCasa = new ArrayList<>();
        ALPlacarOponente = new ArrayList<>();
        ALEquipe = new ArrayList<>();
        ALOponente= new ArrayList<>();
        ALData = new ArrayList<>();
        ALQuadro = new ArrayList<>();

        AAAll = new ArrayAdapter<>(
                getApplicationContext(),
                R.layout.lista_personalizada,
                ALAll
        );

        LVAll.setAdapter(AAAll);

        Preferencias preferencias = new Preferencias(TabelaJogos.this);
        idEquipe = preferencias.getIdentificadorEquipe();
        idUsuario = preferencias.getIdentificadorUsuario();

        firebase = ConfiguracaoFirebase.getFirebase().child("partidas").child(idEquipe).child(idUsuario);

        firebase.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot ){

                for(DataSnapshot dados: snapshot.getChildren()){
                    Partida partida = dados.getValue(Partida.class);
                    String placarCasa = String.valueOf(partida.getPlacarDaCasa());
                    String placarOponente = String.valueOf(partida.getPlacarOponente());

                    ALAll.add("                      "+partida.getData()+"\n\n "+partida.getEquipe()+" "+placarCasa+" X "+placarOponente+" "+partida.getOponente()+"\n\n                       Quadro: "+partida.getQuadro());
                    ALIdPartida.add(partida.getIdentificadorPartida());
                    ALEquipe.add(partida.getEquipe());
                    ALPlacarCasa.add(partida.getPlacarDaCasa());
                    ALPlacarOponente.add(partida.getPlacarOponente());
                    ALOponente.add(partida.getOponente());
                    ALData.add(partida.getData());
                    ALQuadro.add(partida.getQuadro());
                }
                AAAll.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError){

            }
        });
    }

    public void Voltar4(View view){
        Intent intent = new Intent(TabelaJogos.this, MainActivity.class);
        startActivity(intent);
    }

    public void searchByDate(final String date){
        ALAll = new ArrayList<>();
        AAAll = new ArrayAdapter<String>(
                getApplicationContext(),
                R.layout.lista_personalizada,
                ALAll
        );
        LVAll.setAdapter(AAAll);

        firebase = ConfiguracaoFirebase.getFirebase().child("partidas").child(idEquipe).child(idUsuario);

        firebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dados: snapshot.getChildren()){
                    Partida partida = dados.getValue(Partida.class);
                    if(date.equals(partida.getData())){
                        String placarCasa = String.valueOf(partida.getPlacarDaCasa());
                        String placarAdversario = String.valueOf(partida.getPlacarOponente());

                        ALAll.add("                      "+partida.getData()+"\n\n "+partida.getEquipe()+" "+placarCasa+" X "+placarAdversario+" "+partida.getOponente()+"\n\n                       Quadro: "+partida.getQuadro());
                    }
                    AAAll.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

          ETdate.setText("");
    }
}
