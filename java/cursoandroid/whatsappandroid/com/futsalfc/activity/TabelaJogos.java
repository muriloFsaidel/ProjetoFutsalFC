package cursoandroid.whatsappandroid.com.futsalfc.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import cursoandroid.whatsappandroid.com.futsalfc.Adapter.TabelaJogosAdaptador;
import cursoandroid.whatsappandroid.com.futsalfc.R;
import cursoandroid.whatsappandroid.com.futsalfc.config.ConfiguracaoFirebase;
import cursoandroid.whatsappandroid.com.futsalfc.helper.Preferencias;
import cursoandroid.whatsappandroid.com.futsalfc.template.Partida;

public class TabelaJogos extends AppCompatActivity {

    private EditText ETdate;
    private Button botaoConsultar;
    private CheckBox CHReload;

    private ListView LVAll;
    private ArrayAdapter AAAll;
    private ArrayList<Partida> ALAll;
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
    private String idJogadorUsuario;
    private ValueEventListener valueEventListenerJogos;
    private Toolbar toolbar;

    private String nomeJogadorUsuario;
    private String emailJogadorUsuario;

    @Override
    public void onStart(){
        super.onStart();
        firebase.addListenerForSingleValueEvent(valueEventListenerJogos);
        Log.i("EventJogos","onStart");
    }


    @Override
    public void onStop(){
        super.onStop();
        //Para a consulta quando a activity for pausada
        firebase.removeEventListener(valueEventListenerJogos);
        Log.i("EventJogos","onStop");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabela_jogos);

        ETdate = (EditText) findViewById(R.id.ETDateId);
        LVAll = (ListView) findViewById(R.id.LVAllId);
        botaoConsultar = (Button) findViewById(R.id.botaoConsultarId);
        CHReload = (CheckBox) findViewById(R.id.CHReloadId);
        toolbar = (Toolbar) findViewById(R.id.tbTabelaJogos);

        toolbar.setTitle("Tabela de Jogos");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
        setSupportActionBar(toolbar);

        SimpleMaskFormatter formato = new SimpleMaskFormatter("NN/NN/NNNN");
        MaskTextWatcher mascara = new MaskTextWatcher(ETdate,formato);
        ETdate.addTextChangedListener(mascara);

        Bundle retorno = getIntent().getExtras();

        if(retorno != null){
            recoverMatchJogadorUsuario();
        }else{
            recoverMatch();
        }



        botaoConsultar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                try{
                    if(ETdate.getText().toString().equals("")){
                        Toast.makeText(getApplicationContext(),"data não preenchida, favor completá-la", Toast.LENGTH_LONG).show();
                    }
                    else{
                        Bundle retorno = getIntent().getExtras();
                        String data =  ETdate.getText().toString();

                        if(retorno != null){
                            searchByDateJogadorUsuario(data);
                        }else{
                            searchByDate(data);
                        }


                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });

        CHReload.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Bundle retorno = getIntent().getExtras();

                if(retorno != null){
                    recoverMatchJogadorUsuario();
                }else{
                    recoverMatch();
                }
            }
        });

        LVAll.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id){


                Bundle retorno = getIntent().getExtras();

                if(retorno != null){
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
                    intent.putExtra("MainActivity2", "MainActivity2");
                    startActivity(intent);
                }else{
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

                //intent.putExtra("nomeJogador", nomeJogadorUsuario);
                //intent.putExtra("emailResponsavel", emailJogadorUsuario);

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

        /*padrão
        AAAll = new ArrayAdapter<>(
                getApplicationContext(),
                R.layout.lista_personalizada,
                ALAll
        );

         */
        //personalizado
        AAAll = new TabelaJogosAdaptador(getApplicationContext(),ALAll);

        LVAll.setAdapter(AAAll);

        Preferencias preferencias = new Preferencias(TabelaJogos.this);
        idEquipe = preferencias.getIdentificadorEquipe();
        idUsuario = preferencias.getIdentificadorUsuario();

        firebase = ConfiguracaoFirebase.getFirebase().child("partidas").child(idEquipe).child(idUsuario);

        valueEventListenerJogos = new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot ){

                ALAll.clear();

                for(DataSnapshot dados: snapshot.getChildren()){
                    Partida partida = dados.getValue(Partida.class);

                    ALAll.add(partida);

                    //ALAll.add("                      "+partida.getData()+"\n\n "+partida.getEquipe()+" "+placarCasa+" X "+placarOponente+" "+partida.getOponente()+"\n\n                       Quadro: "+partida.getQuadro());
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
        };

        firebase.addListenerForSingleValueEvent(valueEventListenerJogos);
    }

    public void Voltar4(View view){

        Bundle retornoFragment = getIntent().getExtras();

        if(retornoFragment!= null){
            Intent intent = new Intent(TabelaJogos.this, MainActivity2.class);
            intent.putExtra("MainActivity2", "MainActivity2");
            startActivity(intent);
        }else{
            Intent intent = new Intent(TabelaJogos.this, MainActivity.class);
            startActivity(intent);
        }


    }

    public void searchByDate(final String date){
        ALAll = new ArrayList<>();
        //personalizado
        AAAll = new TabelaJogosAdaptador(getApplicationContext(),ALAll);
        LVAll.setAdapter(AAAll);

        firebase = ConfiguracaoFirebase.getFirebase().child("partidas").child(idEquipe).child(idUsuario);

        firebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dados: snapshot.getChildren()){
                    Partida partida = dados.getValue(Partida.class);
                    if(date.equals(partida.getData())){

                        ALAll.add(partida);
                        //ALAll.add("                      "+partida.getData()+"\n\n "+partida.getEquipe()+" "+placarCasa+" X "+placarAdversario+" "+partida.getOponente()+"\n\n                       Quadro: "+partida.getQuadro());
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

    public void recoverMatchJogadorUsuario(){
        ALAll = new ArrayList<>();
        ALIdPartida = new ArrayList<>();
        ALPlacarCasa = new ArrayList<>();
        ALPlacarOponente = new ArrayList<>();
        ALEquipe = new ArrayList<>();
        ALOponente= new ArrayList<>();
        ALData = new ArrayList<>();
        ALQuadro = new ArrayList<>();

        /*padrão
        AAAll = new ArrayAdapter<>(
                getApplicationContext(),
                R.layout.lista_personalizada,
                ALAll
        );

         */
        //personalizado
        AAAll = new TabelaJogosAdaptador(getApplicationContext(),ALAll);

        LVAll.setAdapter(AAAll);

        Preferencias preferencias = new Preferencias(TabelaJogos.this);
        idEquipe = preferencias.getIdentificadorEquipe();
        idJogadorUsuario = preferencias.getJogadorUsuario();

        firebase = ConfiguracaoFirebase.getFirebase().child("partidas").child(idEquipe).child(idJogadorUsuario);

        valueEventListenerJogos = new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot ){

                ALAll.clear();

                for(DataSnapshot dados: snapshot.getChildren()){
                    Partida partida = dados.getValue(Partida.class);

                    ALAll.add(partida);

                    //ALAll.add("                      "+partida.getData()+"\n\n "+partida.getEquipe()+" "+placarCasa+" X "+placarOponente+" "+partida.getOponente()+"\n\n                       Quadro: "+partida.getQuadro());
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
        };

        firebase.addListenerForSingleValueEvent(valueEventListenerJogos);
    }

    public void searchByDateJogadorUsuario(final String date){
        ALAll = new ArrayList<>();
        //personalizado
        AAAll = new TabelaJogosAdaptador(getApplicationContext(),ALAll);
        LVAll.setAdapter(AAAll);

        firebase = ConfiguracaoFirebase.getFirebase().child("partidas").child(idEquipe).child(idJogadorUsuario);

        firebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dados: snapshot.getChildren()){
                    Partida partida = dados.getValue(Partida.class);
                    if(date.equals(partida.getData())){

                        ALAll.add(partida);
                        //ALAll.add("                      "+partida.getData()+"\n\n "+partida.getEquipe()+" "+placarCasa+" X "+placarAdversario+" "+partida.getOponente()+"\n\n                       Quadro: "+partida.getQuadro());
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

    /*Bundle retornoFragment = getIntent().getExtras();

        if(retornoFragment != null){
            nomeJogadorUsuario =  retornoFragment.getString("nomeJogador");
            emailJogadorUsuario = retornoFragment.getString("emailResponsavel");
        }*/
}
