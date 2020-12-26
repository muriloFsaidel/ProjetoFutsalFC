package cursoandroid.whatsappandroid.com.futsalfc.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import cursoandroid.whatsappandroid.com.futsalfc.R;
import cursoandroid.whatsappandroid.com.futsalfc.config.ConfiguracaoFirebase;
import cursoandroid.whatsappandroid.com.futsalfc.helper.Base64Custom;
import cursoandroid.whatsappandroid.com.futsalfc.helper.Preferencias;
import cursoandroid.whatsappandroid.com.futsalfc.template.Equipe;
import cursoandroid.whatsappandroid.com.futsalfc.template.Jogador;
import cursoandroid.whatsappandroid.com.futsalfc.template.JogadorUsuario;

public class CadastroJogadorUsuario extends AppCompatActivity {

    private EditText nome;
    private EditText emailResponsavel;
    private EditText emailJogador;
    private EditText senha;
    private Button botaoCadastrar;
    private boolean responsavelConfirmado;
    private boolean equipeConfirmada;
    private boolean jogadorConfirmado;
    private Preferencias preferencias;

    private FirebaseAuth autenticacao;
    private DatabaseReference firebase;
    private JogadorUsuario jogadorUsuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_jogador_usuario);

        nome = (EditText) findViewById(R.id.etNomeCadastroJogadorUsuarioId);
        emailResponsavel = (EditText) findViewById(R.id.etEmailResponsavelCadastroJogadorUsuarioId);
        emailJogador = (EditText) findViewById(R.id.etEmailJogadorCadastroJogadorUsuarioId);
        senha = (EditText) findViewById(R.id.etSenhaJogadorCadastroJogadorUsuarioId);
        botaoCadastrar = (Button) findViewById(R.id.botaoCadastrarJogadorUsuarioId);


        botaoCadastrar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                if(nome.getText().toString().equals("") || emailJogador.getText().toString().equals("") || emailResponsavel.getText().toString().equals("") || senha.getText().toString().equals("")){
                    Toast.makeText(CadastroJogadorUsuario.this,"Favor preencher todos os dados",Toast.LENGTH_LONG).show();
                }else{

                    String nomeJogador = nome.getText().toString().toLowerCase();
                    String sEmailJogador = emailJogador.getText().toString().toLowerCase();
                    String sEmailResponsavel = emailResponsavel.getText().toString().toLowerCase();
                    String sSenha = senha.getText().toString();


                    //Instanciando classe entidade
                    jogadorUsuario = new JogadorUsuario();
                    //usando o set de cada atributo da classe entidade
                    jogadorUsuario.setNome(nomeJogador);
                    jogadorUsuario.setEmailJogador(sEmailJogador);
                    jogadorUsuario.setEmailResponsavel(sEmailResponsavel);
                    jogadorUsuario.setSenha(sSenha);

                    cadastrarJogadorUsuario();
                }
            }
        });
    }

    public void cadastrarJogadorUsuario(){
        autenticacao = ConfiguracaoFirebase.getAutenticacao();

        autenticacao.createUserWithEmailAndPassword(
                jogadorUsuario.getEmailJogador(),
                jogadorUsuario.getSenha()
         //disparando evento de verificação de cadastro
        ).addOnCompleteListener( CadastroJogadorUsuario.this, new OnCompleteListener<AuthResult>(){
            @Override
            public void onComplete(@NonNull Task<AuthResult> task){
                //se o cadastro foi realizado com sucesso
                if(task.isSuccessful()){

                    //ResponsavelExistente(emailResponsavel.getText().toString().toLowerCase());
                    //EquipeExistente(emailResponsavel.getText().toString().toLowerCase());
                    //jogadorExistente(nome.getText().toString().toLowerCase());

                    preferencias = new Preferencias(CadastroJogadorUsuario.this);
                    String idresponsavel = Base64Custom.codificarParaBase64(jogadorUsuario.getEmailResponsavel());
                    preferencias.salvarDadosJogadorUsuario(idresponsavel);
                    preferencias.salvarDadosNomeJogador(jogadorUsuario.getNome());
                    preferencias.salvarDadosEmailResponsavelJogadorUsuario(jogadorUsuario.getEmailResponsavel());


                        Toast.makeText(CadastroJogadorUsuario.this,"Sucesso ao cadastrar jogador como usuário",Toast.LENGTH_LONG).show();

                        //conervertendo email do jogador para base64
                        String idJogadorUsuario = Base64Custom.codificarParaBase64(jogadorUsuario.getEmailJogador());

                        // e atribuindo como identificador
                        jogadorUsuario.setIdJogadorUsuario(idJogadorUsuario);

                        //salvando todos os dados no firebase
                        jogadorUsuario.salvar();

                        abrirLoginJogadorUsuario();

                    }

                else{
                    String erro;
                    try{
                        //lança a excessão
                        throw task.getException();
                    }catch(FirebaseAuthWeakPasswordException e){
                        erro = "Digite uma senha mais forte, contendo mais caracteres com letras e números";
                    }
                    catch(FirebaseAuthInvalidCredentialsException e){
                        erro = "o email informado é inválido, digite um novo email";
                    }
                    catch(FirebaseAuthUserCollisionException e){
                        erro = "esse email já está em uso no app";
                    }
                    catch(Exception e){
                        erro ="erro ao efetuar o cadastro";
                        e.printStackTrace();
                    }
                    Toast.makeText(CadastroJogadorUsuario.this, "Erro: "+ erro,Toast.LENGTH_LONG).show();
                }
            }

        });

    }

    public void abrirLoginJogadorUsuario(){
        //voltando para o login
        Intent intent = new Intent(CadastroJogadorUsuario.this, LoginActivity.class);

        //criando String para redirecionar do login para a MainActivity2
        String mainActivity2 = "mainActivity2";
        intent.putExtra("jogadorUsuario",mainActivity2);

        //indo para Login
        startActivity(intent);

        //encerrando CadastroJogadorUsuario da memória RAM
        finish();
    }

    /*public void ResponsavelExistente(final String emailResponsavel){

        //convertendo email para base64
        String idResponsavel = Base64Custom.codificarParaBase64(emailResponsavel);

        //acessando o nó do usuario responsável
        firebase = ConfiguracaoFirebase.getFirebase().child("usuarios").child(idResponsavel);

        //disparando evento de consulta
        firebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //se houver dados, o responsável está confirmado
                if(snapshot.getValue() != null){
                    Preferencias preferencias = new Preferencias(CadastroJogadorUsuario.this);

                    String idResponsavel = Base64Custom.codificarParaBase64(jogadorUsuario.getEmailResponsavel());
                    //salvando o identificador do responsável pelo time para resgatar jogos e estatísticas
                    preferencias.salvarDadosJogadorUsuario(idResponsavel);
                    preferencias.salvarDadosEmailResponsavelJogadorUsuario(jogadorUsuario.getEmailResponsavel());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void EquipeExistente(final String emailResponsavel){
            String idResponsavel = Base64Custom.codificarParaBase64(emailResponsavel);
            firebase = ConfiguracaoFirebase.getFirebase().child("equipes").child(idResponsavel);

            firebase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    //capturando o identificador da equipe
                    for(DataSnapshot dados: snapshot.getChildren()){
                        //recuperando dados de acordo com os atributos da classe entidade
                        Equipe equipe = dados.getValue(Equipe.class);
                        Preferencias preferencias = new Preferencias(CadastroJogadorUsuario.this);
                        preferencias.salvarDadosEquipe(equipe.getIdentificadorEquipe());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
    }

    public void jogadorExistente(final String nomeJogador){

            final Preferencias preferencias = new Preferencias(CadastroJogadorUsuario.this);
            String idEquipe = preferencias.getIdentificadorEquipe();
            String idResponsavel = preferencias.getIdentificadorUsuario();

            firebase = ConfiguracaoFirebase.getFirebase().child("jogadores").child(idEquipe).child(idResponsavel);

            firebase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    //varrendo jogadores
                    for(DataSnapshot dados: snapshot.getChildren()){
                        Jogador jogador = dados.getValue(Jogador.class);
                        String nombreJuador = jogador.getNome().toString();
                        if(nomeJogador.equals(nombreJuador)){
                            preferencias.salvarDadosJogador(jogador.getIdentificadorJogador());
                            preferencias.salvarDadosNomeJogador(jogador.getNome());
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }*/
}
