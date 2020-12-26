package cursoandroid.whatsappandroid.com.futsalfc.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import cursoandroid.whatsappandroid.com.futsalfc.R;
import cursoandroid.whatsappandroid.com.futsalfc.config.ConfiguracaoFirebase;
import cursoandroid.whatsappandroid.com.futsalfc.fragment.DadosJogadorUsuario;
import cursoandroid.whatsappandroid.com.futsalfc.helper.Base64Custom;
import cursoandroid.whatsappandroid.com.futsalfc.helper.Preferencias;
import cursoandroid.whatsappandroid.com.futsalfc.template.Equipe;
import cursoandroid.whatsappandroid.com.futsalfc.template.Jogador;
import cursoandroid.whatsappandroid.com.futsalfc.template.JogadorUsuario;
import cursoandroid.whatsappandroid.com.futsalfc.template.Usuario;

public class LoginActivity extends Activity {

    private EditText email;
    private EditText senha;
    private Button botaoLogar;
    private Button botaoLogarJogadorUsuario;
    private Usuario usu;
    private JogadorUsuario usuJogador;
    private DatabaseReference firebase;
    private DatabaseReference firebase2;
    private FirebaseAuth autenticacao;
    private Preferencias preferencias;
    private String emailResponsavel;
    private String nomeDoJogador;
    private String sMainActivity2 = "MainActivity2";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        verificarUsuarioLogado();
        email = (EditText) findViewById(R.id.ETEmailId);
        senha = (EditText) findViewById(R.id.ETSenhaId);
        botaoLogar = (Button) findViewById(R.id.BTLogarId);
        botaoLogarJogadorUsuario = (Button) findViewById(R.id.BTLogarUsuarioJogadorId);

        botaoLogar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                usu = new Usuario();
                usu.setEmail(email.getText().toString().toLowerCase());
                usu.setSenha(senha.getText().toString());
                validarLogin();
            }
        });

        botaoLogarJogadorUsuario.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                usuJogador = new JogadorUsuario();
                usuJogador.setEmailJogador(email.getText().toString().toLowerCase());
                usuJogador.setSenha(senha.getText().toString());
                validarLoginUsuJogador();
            }
        });


    }

    public void verificarUsuarioLogado(){
        autenticacao = ConfiguracaoFirebase.getAutenticacao();

        //se o usuário já estiver conectado
        if(autenticacao.getCurrentUser() != null){
            abrirTelaPrincipal();
        }
    }

    public void validarLogin(){
        autenticacao = ConfiguracaoFirebase.getAutenticacao();
        autenticacao.signInWithEmailAndPassword(
                usu.getEmail(),
                usu.getSenha()
        ).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>(){
           @Override
           public void onComplete(@NonNull Task<AuthResult> task){
               // se o usuário for válido
               if(task.isSuccessful()){

                   firebase = ConfiguracaoFirebase.getFirebase().child("usuarios");

                   firebase.addListenerForSingleValueEvent(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot snapshot) {

                           for(DataSnapshot dados: snapshot.getChildren()){
                               Usuario usuario = dados.getValue(Usuario.class);
                               if(usu.getEmail().toString().equals(usuario.getEmail())){
                                   Preferencias preferencias = new Preferencias(LoginActivity.this);
                                   String identificadorUsuarioLogado = usuario.getId();
                                   //salvando identificador no arquivo
                                   preferencias.salvarDadosUsuario(identificadorUsuarioLogado);

                                   abrirTelaPrincipal();
                                   Toast.makeText(LoginActivity.this,"Sucesso ao fazer login", Toast.LENGTH_LONG).show();
                               }
                           }

                       }

                       @Override
                       public void onCancelled(@NonNull DatabaseError error) {

                       }
                   });
               }
               else{
                   String erro;
                   try{
                        throw task.getException();
                   }
                   catch(FirebaseAuthInvalidUserException e){
                       erro = "email inválido, tente novamente";
                   }
                   catch(FirebaseAuthInvalidCredentialsException e){
                       erro = "Senha incorreta, tente novamente";
                   }
                   catch(Exception e){
                       erro = "Erro ao fazer Login";
                       e.printStackTrace();
                   }
                   Toast.makeText(LoginActivity.this, "Erro: "+erro, Toast.LENGTH_LONG).show();
               }
           }
        });
    }

    public void validarLoginUsuJogador(){
        autenticacao = ConfiguracaoFirebase.getAutenticacao();

        autenticacao.signInWithEmailAndPassword(
                usuJogador.getEmailJogador(),
                usuJogador.getSenha()
        ).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>(){
            @Override
            public void onComplete(@NonNull Task<AuthResult> task){
                if(task.isSuccessful()){
                    String idJogadorUsuario = Base64Custom.codificarParaBase64(usuJogador.getEmailJogador());

                    firebase = ConfiguracaoFirebase.getFirebase().child("jogadoresUsuarios").child(idJogadorUsuario);

                    firebase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            //for(DataSnapshot dados: snapshot.getChildren()){
                                JogadorUsuario jogadorUsuario = snapshot.getValue(JogadorUsuario.class);
                                //se o email informado for igual ao email do banco de dados
                                //if(usuJogador.getEmailJogador().equals(jogadorUsuario.getEmailJogador())){
                                    preferencias = new Preferencias(LoginActivity.this);
                                    //String idresponsavel = Base64Custom.codificarParaBase64(jogadorUsuario.getEmailResponsavel());
                                    //preferencias.salvarDadosJogadorUsuario(idresponsavel);
                                    preferencias.salvarDadosNomeJogador(jogadorUsuario.getNome());
                                    preferencias.salvarDadosEmailResponsavelJogadorUsuario(jogadorUsuario.getEmailResponsavel());

                                    //emailResponsavel = jogadorUsuario.getEmailResponsavel();
                                    //nomeDoJogador = jogadorUsuario.getNome();

                                    //ResponsavelExistente();
                                    //EquipeExistente();
                                    //String idEquipe = preferencias.getIdentificadorEquipe();
                                    //String idResponsavel = preferencias.getJogadorUsuario();
                                    //Log.i("IdEquipe",idEquipe);
                                    //Log.i("IdResponsavel",idResponsavel);
                                    //jogadorExistente();


                                    //String idJogador = preferencias.getIdentificadorJogador();

                                    //Toast.makeText(LoginActivity.this,"IdEquipe:"+idEquipe+"\nIdResponsavel:"+idResponsavel+"\nIdJogador:"+idJogador, Toast.LENGTH_LONG).show();

                                    //Log.i("IdJogador",idJogador);
                                    //Log.i("NomeDoJogador",nomeDoJogador);
                                    //Intent intent = new Intent(LoginActivity.this, MainActivity2.class);
                                    //intent.putExtra("MainActivity2", "MainActivity2");
                                    sMainActivity2 = "mainActivity2";
                                    abrirTelaPrincipalJogadorUsuarioLogado();
                                    Toast.makeText(LoginActivity.this,"Sucesso ao fazer Login", Toast.LENGTH_LONG).show();
                                    //startActivity(intent);

                                //}
                            //}
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }else{
                    String erro;
                    try{
                        throw task.getException();
                    }catch(FirebaseAuthInvalidUserException e){
                        erro = "email inválido, tente novamente";
                    }catch(FirebaseAuthInvalidCredentialsException e){
                        erro = "senha inválida, tente novamente";
                    }catch(Exception e){
                        erro = "erro ao fazer login";
                        e.printStackTrace();
                    }
                    Toast.makeText(LoginActivity.this,"Erro: "+ erro,Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void abrirTelaPrincipal(){

        Bundle jogadorUsuario = getIntent().getExtras();

        //se houver dado é o jogador usuário recem-cadastrado
        if( jogadorUsuario != null){
            //instanciando intent da loginActivity para mainActivity2
            Intent intent = new Intent(LoginActivity.this, MainActivity2.class);
            intent.putExtra("MainActivity2",sMainActivity2);
            startActivity(intent);
        }else{
            //senão houver é o administrador
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    public void abrirTelaPrincipalJogadorUsuarioLogado(){
        Intent intent = new Intent(LoginActivity.this, MainActivity2.class);
        intent.putExtra("MainActivity2", sMainActivity2);
        startActivity(intent);
        finish();
    }

    public void abrirCadastroUsuario(View view){

        AlertDialog.Builder dialog = new AlertDialog.Builder(LoginActivity.this);

        dialog.setTitle("Novo Cadastro");
        dialog.setMessage("cadastrar como:");
        dialog.setCancelable(false);

        final Spinner spinner = new Spinner(LoginActivity.this);
        ArrayList ALCadastro = new ArrayList<>();
        ALCadastro.add("administrador");
        ALCadastro.add("jogador");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter(
                LoginActivity.this,
                android.R.layout.simple_list_item_1,
                ALCadastro
        );

        spinner.setAdapter(arrayAdapter);

        //adicionando o spinner na caixa de diálogo
        dialog.setView(spinner);

        dialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){

            }
        });

        dialog.setPositiveButton("Cadastrar", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                String escolha = (String) spinner.getSelectedItem();

                switch(escolha){

                    case "administrador":
                        Intent intent = new Intent(LoginActivity.this, CadastroUsuarioActivity.class);
                        startActivity(intent);
                        break;

                    case "jogador":
                        Intent intent1 = new Intent(LoginActivity.this, CadastroJogadorUsuario.class);
                        startActivity(intent1);
                        break;

                    default:
                        Toast.makeText(LoginActivity.this,"Erro ao processar escolha",Toast.LENGTH_LONG).show();

                }
            }
        });

        dialog.create();
        dialog.show();


    }

    public void redefinirSenhaPorEmail(View view){

        autenticacao = ConfiguracaoFirebase.getAutenticacao();

        AlertDialog.Builder redefinirSenha = new AlertDialog.Builder(LoginActivity.this);

        redefinirSenha.setTitle("Esqueceu a senha?");
        redefinirSenha.setMessage("Email:");
        redefinirSenha.setCancelable(false);

        final EditText emailRedefinicao = new EditText(LoginActivity.this);
        emailRedefinicao.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        redefinirSenha.setView(emailRedefinicao);



        redefinirSenha.setPositiveButton("Solicitar Redefinição de senha", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String sEmailRedefinicao = emailRedefinicao.getText().toString().toLowerCase();

                //Toast.makeText(LoginActivity.this,sEmailRedefinicao, Toast.LENGTH_LONG).show();

                autenticacao.sendPasswordResetEmail(sEmailRedefinicao)
                        .addOnCompleteListener(new OnCompleteListener<Void>(){
                            @Override
                            public void onComplete(@NonNull Task<Void> task){
                                if(task.isSuccessful()){
                                    Toast.makeText(LoginActivity.this,"Email de redefinição de senha enviado com sucesso, verifique sua caixa de spam/ caixa de entrada", Toast.LENGTH_LONG).show();
                                }else{
                                    Toast.makeText(LoginActivity.this,"Favor digitar email novamente", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });

        redefinirSenha.setNegativeButton("Fechar", new DialogInterface.OnClickListener(){
           @Override
           public void onClick(DialogInterface dialog, int which){

           }
        });
        redefinirSenha.create();
        redefinirSenha.show();

    }


    //intent.putExtra("nomeJogador",jogadorUsuario.getNome());
    //intent.putExtra("emailResponsavel",jogadorUsuario.getEmailResponsavel());

    /*else{
                        //Toast.makeText(LoginActivity.this, "Favor confirmar nome do jogador", Toast.LENGTH_LONG).show();
                        preferencias.salvarDadosJogador("VAZIO");
                    }*/
    //else if (!nomeDoJogador.equals(jogador.getNome())){
    //Toast.makeText(LoginActivity.this, "Favor confirmar nome do jogador", Toast.LENGTH_LONG).show();
    //preferencias.salvarDadosJogador("");
    //}

}
