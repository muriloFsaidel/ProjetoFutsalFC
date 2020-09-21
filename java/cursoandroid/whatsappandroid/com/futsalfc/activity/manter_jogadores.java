package cursoandroid.whatsappandroid.com.futsalfc.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cursoandroid.whatsappandroid.com.futsalfc.R;
import cursoandroid.whatsappandroid.com.futsalfc.config.ConfiguracaoFirebase;
import cursoandroid.whatsappandroid.com.futsalfc.helper.Base64Custom;
import cursoandroid.whatsappandroid.com.futsalfc.helper.Preferencias;
import cursoandroid.whatsappandroid.com.futsalfc.template.DadosPartida;
import cursoandroid.whatsappandroid.com.futsalfc.template.Equipe;
import cursoandroid.whatsappandroid.com.futsalfc.template.Jogador;
import cursoandroid.whatsappandroid.com.futsalfc.template.Partida;

public class manter_jogadores extends AppCompatActivity {

    private ListView lvjogadores;
    private ArrayAdapter adapterJogadores;
    private ArrayList<String> alJogadores;
    private ArrayList<String> alNomeJogadores;
    private EditText etnomeJogador;
    private Button botaoAtualizar;
    private DatabaseReference firebase;
    public String identificadorEquipe ="";
    private String identificadorUsuarioLogado ="";
    private String identificadorJogador="";
    private String nomeJogadorSelecionado ="";

    private Spinner spPosicao;
    private Spinner spAtivo;
    private ArrayAdapter adapterPosicao;
    private ArrayAdapter adapterAtivo;
    private ArrayList<String> alPosicao;
    private ArrayList<String> alPosicaoFirebase;
    private ArrayList<String> alAtivo;
    private ArrayList<String> alAtivoFirebase;
    private ArrayList<String> alIdJogador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manter_jogadores);

        lvjogadores = (ListView) findViewById(R.id.listViewJogadoresId);
        etnomeJogador = (EditText) findViewById(R.id.editTextNomeId);
        botaoAtualizar = (Button) findViewById(R.id.botaoAtualizarJogadoresId);
        spPosicao = (Spinner) findViewById(R.id.spinnerPosicaoId);
        spAtivo = (Spinner) findViewById(R.id.spinnerAtivo);

        alJogadores = new ArrayList<>();
        alNomeJogadores = new ArrayList<>();
        alPosicao = new ArrayList<>();

        alPosicao.add("Goleiro");
        alPosicao.add("Fixo");
        alPosicao.add("Ala");
        alPosicao.add("Pivô");

        alAtivo = new ArrayList<>();

        alAtivo.add("SIM");
        alAtivo.add("NÃO");

        alIdJogador = new ArrayList<>();
        alPosicaoFirebase = new ArrayList<>();
        alAtivoFirebase = new ArrayList<>();

        adapterJogadores = new ArrayAdapter(
                getApplicationContext(),
                R.layout.lista_personalizada,
                alJogadores
        );

        lvjogadores.setAdapter(adapterJogadores);

        adapterPosicao = new ArrayAdapter(
                getApplicationContext(),
                android.R.layout.simple_dropdown_item_1line,
                alPosicao
        );
        adapterPosicao.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spPosicao.setAdapter(adapterPosicao);

        adapterAtivo = new ArrayAdapter(
                getApplicationContext(),
                android.R.layout.simple_dropdown_item_1line,
                alAtivo
        );
        adapterAtivo.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spAtivo.setAdapter(adapterAtivo);

        Preferencias preferencias = new Preferencias(manter_jogadores.this);
        identificadorEquipe = preferencias.getIdentificadorEquipe();
        identificadorUsuarioLogado = preferencias.getIdentificadorUsuario();

        firebase = ConfiguracaoFirebase.getFirebase().child("jogadores").child(identificadorEquipe).child(identificadorUsuarioLogado);

        //consulta dos jogadores
        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                alJogadores.clear();
                alNomeJogadores.clear();
                alIdJogador.clear();
                alPosicaoFirebase.clear();
                alAtivoFirebase.clear();


                for(DataSnapshot dados: snapshot.getChildren()){
                    Jogador jogador =  dados.getValue(Jogador.class);
                    alJogadores.add(jogador.getNome() +"  "+jogador.getPosicao()+ "  "+ jogador.getAtivo());
                    alIdJogador.add(jogador.getIdentificadorJogador());
                    alNomeJogadores.add(jogador.getNome());
                    alPosicaoFirebase.add(jogador.getPosicao());
                    alAtivoFirebase.add(jogador.getAtivo());
                }
                adapterJogadores.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        lvjogadores.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int posicao = position;
                identificadorJogador = alIdJogador.get(posicao);
                nomeJogadorSelecionado = alNomeJogadores.get(posicao);
                //Toast.makeText(getApplicationContext(),identificadorJogador,Toast.LENGTH_LONG).show();
                preencherCampos(alNomeJogadores.get(posicao), alPosicaoFirebase.get(posicao), alAtivoFirebase.get(posicao));
            }

        });

        botaoAtualizar.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View v){
               if(etnomeJogador.getText().toString().equals("")){
                   Toast.makeText(getApplicationContext(),"Favor Verificar nome do jogador", Toast.LENGTH_LONG).show();
               }else{
                   String nomeJogador = etnomeJogador.getText().toString();
                   String posicao = (String) spPosicao.getSelectedItem();
                   String ativo = (String) spAtivo.getSelectedItem();
                   atualizarJogador(identificadorJogador,nomeJogador,posicao,ativo);


               }
           }
        });
    }


    public void voltarDois(View view){
        Intent intent = new Intent(manter_jogadores.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void preencherCampos(String nomeJogador, String posicao, String ativo){

        alPosicao.clear();
        alAtivo.clear();

        etnomeJogador.setText(nomeJogador);
        switch(posicao){

            case "Goleiro":
                alPosicao.add(posicao);
                alPosicao.add("Fixo");
                alPosicao.add("Ala");
                alPosicao.add("Pivô");
                adapterPosicao = new ArrayAdapter(
                        getApplicationContext(),
                        R.layout.lista_personalizada,
                        alPosicao
                );
                spPosicao.setAdapter(adapterPosicao);
                break;

            case "Fixo":
                alPosicao.add(posicao);
                alPosicao.add("Goleiro");
                alPosicao.add("Ala");
                alPosicao.add("Pivô");
                adapterPosicao = new ArrayAdapter(
                        getApplicationContext(),
                        R.layout.lista_personalizada,
                        alPosicao
                );
                spPosicao.setAdapter(adapterPosicao);
                break;

            case "Ala":
                alPosicao.add(posicao);
                alPosicao.add("Goleiro");
                alPosicao.add("Fixo");
                alPosicao.add("Pivô");
                adapterPosicao = new ArrayAdapter(
                        getApplicationContext(),
                        R.layout.lista_personalizada,
                        alPosicao
                );
                spPosicao.setAdapter(adapterPosicao);
                break;

            case "Pivô":
                alPosicao.add(posicao);
                alPosicao.add("Goleiro");
                alPosicao.add("Fixo");
                alPosicao.add("Ala");
                adapterPosicao = new ArrayAdapter(
                        getApplicationContext(),
                        R.layout.lista_personalizada,
                        alPosicao
                );
                spPosicao.setAdapter(adapterPosicao);
                break;

            default:
                break;

        }

        switch(ativo){

            case "SIM":
                alAtivo.add(ativo);
                alAtivo.add("NÃO");
                adapterAtivo = new ArrayAdapter(
                        getApplicationContext(),
                        R.layout.lista_personalizada,
                        alAtivo
                );
                spAtivo.setAdapter(adapterAtivo);
                break;

            case "NÃO":
                alAtivo.add(ativo);
                alAtivo.add("SIM");
                adapterAtivo = new ArrayAdapter(
                        getApplicationContext(),
                        R.layout.lista_personalizada,
                        alAtivo
                );
                spAtivo.setAdapter(adapterAtivo);
                break;

            default:
                break;
        }




    }
    private void atualizarJogador(String idJogador, String NovoNomeJogador,String Novoposicao, String Novoativo){

        firebase =  ConfiguracaoFirebase.getFirebase();

        firebase = firebase.child("jogadores")
                            .child(identificadorEquipe)
                             .child(identificadorUsuarioLogado)
                              .child(idJogador);

        Jogador jogador = new Jogador();
        jogador.setIdentificadorJogador(idJogador);
        jogador.setPosicao(Novoposicao);
        jogador.setAtivo(Novoativo);
        jogador.setNome(NovoNomeJogador);

        firebase.setValue(jogador);

        atualizarEstatistica(NovoNomeJogador);
        atualizarDadosPartida(NovoNomeJogador);

        etnomeJogador.setText("");
        alPosicao.clear();
        alPosicao.add("Goleiro");
        alPosicao.add("Fixo");
        alPosicao.add("Ala");
        alPosicao.add("Pivô");

        alAtivo.clear();
        alAtivo.add("SIM");
        alAtivo.add("NÃO");

        adapterPosicao = new ArrayAdapter(
                getApplicationContext(),
                android.R.layout.simple_dropdown_item_1line,
                alPosicao
        );
        adapterPosicao.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spPosicao.setAdapter(adapterPosicao);

        adapterAtivo = new ArrayAdapter(
                getApplicationContext(),
                android.R.layout.simple_dropdown_item_1line,
                alAtivo
        );
        adapterAtivo.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spAtivo.setAdapter(adapterAtivo);
    }

    private void atualizarEstatistica(String NovoNomeJogadorEstatistica){

        firebase = ConfiguracaoFirebase.getFirebase();

        firebase = firebase.child("estatisticas")
                            .child(identificadorEquipe)
                             .child(identificadorUsuarioLogado)
                              .child(identificadorJogador)
                               .child("jogador");

        firebase.setValue(NovoNomeJogadorEstatistica);

        //Jeito 2
        //Map<String,Object> Nomejogador = new HashMap<>();
        //Nomejogador.put("jogador",NovoNomeJogadorEstatistica);

        //firebase.updateChildren(Nomejogador);
                //setValue(NovoNomeJogadorEstatistica);



    }

    private void atualizarDadosPartida(final String NovoNomeJogadorDadosPartida){

        firebase = ConfiguracaoFirebase.getFirebase().child("dadosPartidas").child(identificadorEquipe).child(identificadorUsuarioLogado);

        firebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dados: snapshot.getChildren()){
                    DadosPartida dadosPartida = dados.getValue(DadosPartida.class);
                    if(nomeJogadorSelecionado.equals(dadosPartida.getJogador())){

                        firebase = ConfiguracaoFirebase.getFirebase();
                        firebase = firebase.child("dadosPartidas")
                                            .child(identificadorEquipe)
                                            .child(identificadorUsuarioLogado)
                                            .child(dadosPartida.getIdentificadorDadosPartida())
                                            .child("jogador");
                        firebase.setValue(NovoNomeJogadorDadosPartida);


                    }
                    //Toast.makeText(getApplicationContext(),"",Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
}
