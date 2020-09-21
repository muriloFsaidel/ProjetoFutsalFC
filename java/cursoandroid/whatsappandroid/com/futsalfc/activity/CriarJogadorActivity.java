package cursoandroid.whatsappandroid.com.futsalfc.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import cursoandroid.whatsappandroid.com.futsalfc.R;
import cursoandroid.whatsappandroid.com.futsalfc.config.ConfiguracaoFirebase;
import cursoandroid.whatsappandroid.com.futsalfc.helper.Base64Custom;
import cursoandroid.whatsappandroid.com.futsalfc.helper.Preferencias;
import cursoandroid.whatsappandroid.com.futsalfc.template.Equipe;
import cursoandroid.whatsappandroid.com.futsalfc.template.Estatistica;
import cursoandroid.whatsappandroid.com.futsalfc.template.Jogador;

public class CriarJogadorActivity extends Activity {

    private EditText nomeJogador;
    private RadioGroup rgAtivo;
    private RadioButton rbAtivo;
    private Spinner spPosicao;
    private ArrayList<String> alPosicoes;
    private ArrayAdapter<String> aaPosicoes;
    private Button botaoInserir;
    private DatabaseReference firebase;
    private String[] posicoes = {"Goleiro","Fixo","Ala","Pivô"};
    private boolean existente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_jogador);

        nomeJogador = (EditText) findViewById(R.id.nomeApelidoId);
        rgAtivo = (RadioGroup) findViewById(R.id.radioGroupId);
        spPosicao = (Spinner) findViewById(R.id.spinnerPosicaoId);
        botaoInserir = (Button) findViewById(R.id.botaoInserirId);

        //preenchendo spinner com as posições
        alPosicoes = new ArrayList();

        for(int i = 0; i <= 3; i++){
            alPosicoes.add(posicoes[i]);
        }
        aaPosicoes = new ArrayAdapter<String>(
                getApplicationContext(),
                android.R.layout.simple_dropdown_item_1line,
                alPosicoes
        );
        aaPosicoes.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spPosicao.setAdapter(aaPosicoes);

        botaoInserir.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View v){
               if(nomeJogador.getText().toString().equals("")){
                   Toast.makeText(getApplicationContext(),"Favor preencher o campo nome/apelido",Toast.LENGTH_LONG).show();
               }
               else{
                       capturarDados();
                   }
           }
        });

    }

    private void inserirEstatistica(String nomeJogadorEstatistica, String equipe, String usuario, String idJogador){

        firebase = ConfiguracaoFirebase.getFirebase();

        //cria nó de estatisticas
        firebase = firebase.child("estatisticas")
                            .child(equipe)
                             .child(usuario)
                             .child(idJogador);

        //criando registros com o objeto estatistica
        Estatistica estatistica = new Estatistica();
        estatistica.setIdentificadorEstatistica(idJogador);
        estatistica.setJogador(nomeJogadorEstatistica);
        estatistica.setGols(0);
        estatistica.setAssistencia(0);
        estatistica.setCartaoAmarelo(0);
        estatistica.setCartaoVermelho(0);

        firebase.setValue(estatistica);

        Toast.makeText(getApplicationContext(),"Jogador: "+ nomeJogadorEstatistica+" cadastrado com sucesso",Toast.LENGTH_LONG).show();


        Intent intent = new Intent(CriarJogadorActivity.this, MainActivity.class);
        finish();

    }

    private void inserirJogador(String jogador, String ativo, String posicao){

        // recuperando identificadores a partir das Preferências
        Preferencias preferencias = new Preferencias(CriarJogadorActivity.this);
        String identificadorUsuario = preferencias.getIdentificadorUsuario();
        String identificadorEquipe = preferencias.getIdentificadorEquipe();

        //converte o nome do jogador para base 64
        String identificadorJogador = Base64Custom.codificarParaBase64(jogador);

        //recuperando instância do firebase a partir da raiz
        firebase = ConfiguracaoFirebase.getFirebase();

        //cria o nó de jogadores
        firebase = firebase.child("jogadores")
                            .child(identificadorEquipe)
                            .child(identificadorUsuario)
                            .child(identificadorJogador);

        //criando registros com o objeto Jogador
        Jogador jogadorX = new Jogador();
        jogadorX.setIdentificadorJogador(identificadorJogador);
        jogadorX.setNome(jogador);
        jogadorX.setAtivo(ativo);
        jogadorX.setPosicao(posicao);

        firebase.setValue(jogadorX);

        inserirEstatistica(jogador, identificadorEquipe, identificadorUsuario, identificadorJogador);

    }

    private void capturarDados(){

        String jogadorSelecionado = nomeJogador.getText().toString();

        // capturando radioButton selecionado
        int identificadorAtivo = rgAtivo.getCheckedRadioButtonId();
        if(identificadorAtivo > 0){
            rbAtivo = (RadioButton) findViewById(identificadorAtivo);
        }
        String ativoSelecionado = rbAtivo.getText().toString();

        //armazenando posição selecionada
        String posicaoSelecionada = (String) spPosicao.getSelectedItem();

        if(ativoSelecionado.equals("") || posicaoSelecionada.equals("")){
            Toast.makeText(getApplicationContext(),"Favor escolher posição & ativo",Toast.LENGTH_LONG).show();
        }
        else{
            jogadorExistente(jogadorSelecionado,ativoSelecionado,posicaoSelecionada);
        }
    }

    private void jogadorExistente(final String jogadorSelecionado, final String ativoSelecionado, final String posicaoSelecionada){

        Preferencias preferencias = new Preferencias(CriarJogadorActivity.this);
        String idEquipe = preferencias.getIdentificadorEquipe();
        String idUsuario = preferencias.getIdentificadorUsuario();

        String idJogadorSelecionado = Base64Custom.codificarParaBase64(jogadorSelecionado);

        firebase = ConfiguracaoFirebase.getFirebase();

        firebase = firebase.child("jogadores").child(idEquipe).child(idUsuario).child(idJogadorSelecionado);

        firebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue()!= null){
                    existente = true;
                    Toast.makeText(getApplicationContext(),"Jogador já existente, favor digitar outro", Toast.LENGTH_LONG).show();
                }
                else{
                    existente = false;
                    inserirJogador(jogadorSelecionado, ativoSelecionado, posicaoSelecionada);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


}
