package cursoandroid.whatsappandroid.com.futsalfc.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;



import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import cursoandroid.whatsappandroid.com.futsalfc.R;
import cursoandroid.whatsappandroid.com.futsalfc.config.ConfiguracaoFirebase;
import cursoandroid.whatsappandroid.com.futsalfc.helper.Preferencias;
import cursoandroid.whatsappandroid.com.futsalfc.template.Equipe;
import cursoandroid.whatsappandroid.com.futsalfc.template.Jogador;

public class VisualizarJogador extends AppCompatActivity {

    private TextView nomeTime;
    private TextView nomeJogadorUsuarioTextView;
    private TextView posicaoJogador;
    private TextView ativoJogador;
    private DatabaseReference firebase;
    private DatabaseReference firebase2;
    private Toolbar toolbar;
    private String nomeJogadorUsuario;
    private String emailResponsavelUsuario;
    private String nomeTimeJogadorUsuario;
    private String nomeDoJogador;
    private String nomeEquipeFirebaseExtra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_jogador);

        nomeTime = (TextView) findViewById(R.id.tv_nome_time_jogador_Usuario);
        nomeJogadorUsuarioTextView = (TextView) findViewById(R.id.tv_nome_jogador_usuario);
        posicaoJogador = (TextView) findViewById(R.id.tv_posicao_jogador_usuario);
        ativoJogador = (TextView) findViewById(R.id.tv_ativo_jogador_usuario);
        toolbar = (Toolbar) findViewById(R.id.toolbar_principal_Visualizar_Jogador);

        toolbar.setTitle("Visualizar Jogador");
        //toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
        setSupportActionBar(toolbar);

        /*Bundle retornoFragment = getIntent().getExtras();

        if(retornoFragment != null){
            nomeTime.setText(retornoFragment.getString("nomeEquipe"));
        }*/

        Preferencias preferencias = new Preferencias(VisualizarJogador.this);

        if(preferencias.getIdentificadorEquipe() == null || preferencias.getJogadorUsuario() == null ){
            Toast.makeText(VisualizarJogador.this,"Favor atualizar email do responsável e nome do jogador ", Toast.LENGTH_LONG).show();
        }else{
            Preferencias preferencias2 = new Preferencias(VisualizarJogador.this);
            String idEquipe = preferencias2.getIdentificadorEquipe();
            String idResponsavel = preferencias2.getJogadorUsuario();
            nomeDoJogador = preferencias2.getNomeJogador();
            jogadorExistente(idEquipe, idResponsavel);
            preencheNomeEquipe();
        }


        //String idJogador = preferencias.getIdentificadorJogador();

        /*firebase = ConfiguracaoFirebase.getFirebase().child("jogadores").child(idEquipe).child(idResponsavel).child(idJogador);

        firebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.getValue()!= null){
                    Jogador jogador = snapshot.getValue(Jogador.class);
                    nomeJogadorUsuarioTextView.setText(jogador.getNome());
                    //nomeJogadorUsuario = jogador.getNome();
                    posicaoJogador.setText(jogador.getPosicao());
                    ativoJogador.setText(jogador.getAtivo());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/

    }

    public void voltar6(View view){

           Bundle retorno = getIntent().getExtras();

           if(retorno != null){
               Intent intent = new Intent(VisualizarJogador.this, MainActivity2.class);
               intent.putExtra("MainActivity2", "MainActivity2");
               startActivity(intent);
           }else{
               Intent intent = new Intent(VisualizarJogador.this, MainActivity.class);
               startActivity(intent);
           }


    }

    public void jogadorExistente(String idEquipe, String idResponsavel){

        firebase2 = ConfiguracaoFirebase.getFirebase().child("jogadores").child(idEquipe).child(idResponsavel);

        firebase2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //varrendo jogadores
                for(DataSnapshot dados: snapshot.getChildren()){
                    Jogador jogador = dados.getValue(Jogador.class);
                    //String nombreJuador = jogador.getNome().toString();
                    //nomeJogadorFirebaseExtra = nombreJuador;
                    if(nomeDoJogador.equals(jogador.getNome())) {
                        Preferencias preferencias = new Preferencias(VisualizarJogador.this);
                        preferencias.salvarDadosJogador(jogador.getIdentificadorJogador());
                        nomeJogadorUsuarioTextView.setText(jogador.getNome());
                        //nomeJogadorUsuario = jogador.getNome();
                        posicaoJogador.setText(jogador.getPosicao());
                        ativoJogador.setText(jogador.getAtivo());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void preencheNomeEquipe(){

        Preferencias preferencias = new Preferencias(VisualizarJogador.this);

        String idResponsavel = preferencias.getJogadorUsuario();
        String idEquipe = preferencias.getIdentificadorEquipe();

        firebase = ConfiguracaoFirebase.getFirebase().child("equipes").child(idResponsavel).child(idEquipe);

        firebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue() != null){
                    Equipe equipe = snapshot.getValue(Equipe.class);
                    nomeEquipeFirebaseExtra = equipe.getNome();
                    nomeTime.setText(nomeEquipeFirebaseExtra);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
