package cursoandroid.whatsappandroid.com.futsalfc.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import cursoandroid.whatsappandroid.com.futsalfc.Adapter.PerfilAdminAdaptador;
import cursoandroid.whatsappandroid.com.futsalfc.R;
import cursoandroid.whatsappandroid.com.futsalfc.config.ConfiguracaoFirebase;
import cursoandroid.whatsappandroid.com.futsalfc.helper.Preferencias;
import cursoandroid.whatsappandroid.com.futsalfc.template.JogadorUsuario;
import cursoandroid.whatsappandroid.com.futsalfc.template.Usuario;

public class PerfilAdmin extends AppCompatActivity {

    private EditText nomeAdmin;
    private EditText emailAdmin;
    private ListView lvAdmin;
    private ArrayAdapter adapter;
    private ArrayList<Usuario> alDados;
    private Button botaoAtualizarAdmin;
    private Toolbar toolbar;
    private DatabaseReference firebase;
    private String sNomeAdmin;
    private String semailAdmin;
    private String sIdAdmin;
    private String sEmailResposavel;
    private FirebaseAuth autenticacao;
    private String senhaAdm;
    private EditText etSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_admin);

        nomeAdmin = (EditText) findViewById(R.id.etNomeAdminId);
        emailAdmin = (EditText) findViewById(R.id.etEmailAdminId);
        lvAdmin = (ListView) findViewById(R.id.listView_Perfil_adm);
        botaoAtualizarAdmin = (Button) findViewById(R.id.botaoAtualizarAdminId);
        toolbar = (Toolbar) findViewById(R.id.toolbar_principal_perfil_admin);

        toolbar.setTitle("Perfil Administrador");
        setSupportActionBar(toolbar);

        alDados = new ArrayList<>();

        adapter = new PerfilAdminAdaptador(PerfilAdmin.this,alDados);

        lvAdmin.setAdapter(adapter);

        Preferencias preferencias = new Preferencias(PerfilAdmin.this);
        sIdAdmin = preferencias.getIdentificadorUsuario();

        firebase = ConfiguracaoFirebase.getFirebase().child("usuarios").child(sIdAdmin);

        firebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                alDados.clear();

                if(snapshot.getValue() != null){
                    Usuario usuario = snapshot.getValue(Usuario.class);
                    sEmailResposavel = usuario.getEmail();
                    alDados.add(usuario);
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        botaoAtualizarAdmin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
              sNomeAdmin =  nomeAdmin.getText().toString().toLowerCase().replaceAll("([\\n\\r])","");
              semailAdmin =  emailAdmin.getText().toString().toLowerCase().replaceAll("([\\n\\r])","");
              if(sNomeAdmin.equals("") || semailAdmin.equals("")){
                  Toast.makeText(PerfilAdmin.this," Favor preencher os campos", Toast.LENGTH_LONG).show();
              }else{
                  atualizarEmailResponsavelJogadorUsuario();
                  atualizarEmailAdmin();
                  atualizaEmailAdmAutenticacao();
                  Intent intent = new Intent(PerfilAdmin.this, MainActivity.class);
                  intent.putExtra("SignOut","SignOut");
                  startActivity(intent);
              }

            }
        });

    }

    public void voltar9(View view){
        Intent intent = new Intent(PerfilAdmin.this, MainActivity.class);
        finish();
    }

    private void atualizarEmailResponsavelJogadorUsuario(){

        firebase = ConfiguracaoFirebase.getFirebase().child("jogadoresUsuarios");

        firebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dados: snapshot.getChildren()){
                    JogadorUsuario jogadorUsuario = dados.getValue(JogadorUsuario.class);

                    if(sEmailResposavel.equals(jogadorUsuario.getEmailResponsavel())){

                        firebase = ConfiguracaoFirebase.getFirebase();

                        firebase = firebase.child("jogadoresUsuarios")
                                            .child(jogadorUsuario.getIdJogadorUsuario())
                                             .child("emailResponsavel");
                        firebase.setValue(semailAdmin);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void atualizarEmailAdmin(){

        firebase = ConfiguracaoFirebase.getFirebase().child("usuarios").child(sIdAdmin);

        firebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Usuario usuario = snapshot.getValue(Usuario.class);

                firebase = ConfiguracaoFirebase.getFirebase();

                firebase = firebase.child("usuarios")
                                    .child(usuario.getId());

                Usuario usuario1 = new Usuario();
                usuario1.setId(usuario.getId());
                usuario1.setNome(sNomeAdmin);
                usuario1.setEmail(semailAdmin);

                firebase.setValue(usuario1);

                Preferencias preferencias = new Preferencias(PerfilAdmin.this);
                //preferencias.salvarDadosEmailResponsavelJogadorUsuario(semailAdmin);
                //preferencias.salvarDadosJogadorUsuario(usuario.getId());
                preferencias.salvarDadosUsuario(usuario.getId());

                nomeAdmin.setText("");
                emailAdmin.setText("");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    private void atualizaEmailAdmAutenticacao(){

        autenticacao = ConfiguracaoFirebase.getAutenticacao();
        FirebaseUser user2 = autenticacao.getCurrentUser();
        user2.updateEmail(semailAdmin)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(PerfilAdmin.this,"Email atualizado com sucesso", Toast.LENGTH_LONG).show();
                        }else{
                            String erro;
                            try{
                                throw task.getException();
                            }catch(FirebaseAuthInvalidCredentialsException e){
                                erro = "o email informado é inválido, digite um novo email";
                            }catch(FirebaseAuthUserCollisionException e){
                                erro = "esse email já está em uso no app";
                            }catch(Exception e){
                                erro = "erro ao atualizar email";
                                e.printStackTrace();
                            }
                            Toast.makeText(PerfilAdmin.this, "Erro: "+ erro, Toast.LENGTH_LONG).show();
                        }
                    }
                });


    }

    //usar no MainAtivity user2.isEmailVerified();
}
