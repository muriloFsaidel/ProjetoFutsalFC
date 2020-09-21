package cursoandroid.whatsappandroid.com.futsalfc.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
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
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

import cursoandroid.whatsappandroid.com.futsalfc.R;
import cursoandroid.whatsappandroid.com.futsalfc.config.ConfiguracaoFirebase;
import cursoandroid.whatsappandroid.com.futsalfc.helper.Base64Custom;
import cursoandroid.whatsappandroid.com.futsalfc.helper.Preferencias;
import cursoandroid.whatsappandroid.com.futsalfc.template.Usuario;

public class LoginActivity extends Activity {

    private EditText email;
    private EditText senha;
    private Button botaoLogar;
    private Usuario usu;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        verificarUsuarioLogado();
        email = (EditText) findViewById(R.id.ETEmailId);
        senha = (EditText) findViewById(R.id.ETSenhaId);
        botaoLogar = (Button) findViewById(R.id.BTLogarId);

        botaoLogar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                usu = new Usuario();
                usu.setEmail(email.getText().toString());
                usu.setSenha(senha.getText().toString());
                validarLogin();
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
                   Preferencias preferencias = new Preferencias(LoginActivity.this);
                   //convertendo email para base64
                   String identificadorUsuarioLogado = Base64Custom.codificarParaBase64(usu.getEmail());
                   //salvando identificador no arquivo
                   preferencias.salvarDadosUsuario(identificadorUsuarioLogado);

                   abrirTelaPrincipal();
                   Toast.makeText(LoginActivity.this,"Sucesso ao fazer login", Toast.LENGTH_LONG).show();
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

    public void abrirTelaPrincipal(){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void abrirCadastroUsuario(View view){
        Intent intent = new Intent(LoginActivity.this, CadastroUsuarioActivity.class);
        startActivity(intent);
    }



}
