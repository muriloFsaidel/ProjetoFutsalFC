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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

import cursoandroid.whatsappandroid.com.futsalfc.R;
import cursoandroid.whatsappandroid.com.futsalfc.config.ConfiguracaoFirebase;
import cursoandroid.whatsappandroid.com.futsalfc.helper.Base64Custom;
import cursoandroid.whatsappandroid.com.futsalfc.helper.Preferencias;
import cursoandroid.whatsappandroid.com.futsalfc.template.Usuario;

public class CadastroUsuarioActivity extends Activity {

    //atributos genéricos
    private EditText nome;
    private EditText email;
    private EditText senha;
    private Button botaoCadastrar;

    // objetos de manutenção
    private FirebaseAuth autenticacao;
    private Usuario usu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);

        //apontando para os objetos na tela
        nome = (EditText) findViewById(R.id.ETNomeId);
        email = (EditText) findViewById(R.id.ETEmailId);
        senha = (EditText) findViewById(R.id.ETSenhaId);
        botaoCadastrar = (Button) findViewById(R.id.BTCadastrarId);

        //disparando evento de clique
        botaoCadastrar.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View v){

               //instanciando usuário
                usu = new Usuario();
                //atribuindo valores aos atributos
                usu.setNome(nome.getText().toString());
                usu.setEmail(email.getText().toString());
                usu.setSenha(senha.getText().toString());
                cadastrarUsuario();
           }
        });
    }

    public void cadastrarUsuario(){
        //instância de autenticação do firebase
        autenticacao = ConfiguracaoFirebase.getAutenticacao();

        autenticacao.createUserWithEmailAndPassword(
                usu.getEmail(),
                usu.getSenha()
        ).addOnCompleteListener(CadastroUsuarioActivity.this, new OnCompleteListener<AuthResult>(){
           @Override
           public void onComplete(@NonNull Task<AuthResult> task){
               //se usuário foi cadastrado com sucesso
                if(task.isSuccessful()){
                    Toast.makeText(CadastroUsuarioActivity.this, "Sucesso ao cadastrar usuário", Toast.LENGTH_LONG).show();

                    //convertendo email para base64
                    String idUsuario = Base64Custom.codificarParaBase64(usu.getEmail());

                    //atribuindo email codificado como Id
                    usu.setId(idUsuario);

                    //salva usu no banco de dados
                    usu.salvar();

                    Preferencias preferencias = new Preferencias(CadastroUsuarioActivity.this);
                    preferencias.salvarDadosUsuario(idUsuario);

                    abrirLoginUsuario();


                }else{
                    String erro;
                    try {
                        throw task.getException();
                    }catch(FirebaseAuthWeakPasswordException e){
                            erro = "Digite uma senha mais forte, contendo mais caracteres e com letras e números";
                    }
                    catch(FirebaseAuthInvalidCredentialsException e){
                            erro = "o email informado é inválido, digite um novo email";
                    }
                    catch(FirebaseAuthUserCollisionException e){
                            erro = "esse email já está em uso no app";
                    }
                    catch(Exception e){
                        erro = "erro ao efetuar o cadastro";
                        e.printStackTrace();
                    }
                    Toast.makeText(CadastroUsuarioActivity.this,"Erro:" + erro ,Toast.LENGTH_LONG).show();
                }
           }
        });
    }

    public void abrirLoginUsuario(){
        Intent intent = new Intent(CadastroUsuarioActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}

/*resgatando usuário cadastrado
                    FirebaseUser firebaseUser = task.getResult().getUser();
                    usu.setId(firebaseUser.getUid());*/

//desconectando usuário
//autenticacao.signOut();

//encerra a activity da memória
//finish();