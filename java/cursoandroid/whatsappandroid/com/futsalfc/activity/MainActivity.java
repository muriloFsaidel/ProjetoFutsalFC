package cursoandroid.whatsappandroid.com.futsalfc.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import cursoandroid.whatsappandroid.com.futsalfc.Adapter.TabAdapter;
import cursoandroid.whatsappandroid.com.futsalfc.R;
import cursoandroid.whatsappandroid.com.futsalfc.config.ConfiguracaoFirebase;
import cursoandroid.whatsappandroid.com.futsalfc.helper.Preferencias;
import cursoandroid.whatsappandroid.com.futsalfc.helper.SlidingTabLayout;
import cursoandroid.whatsappandroid.com.futsalfc.template.Equipe;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth usuarioAutenticacao;
    private Toolbar toolbar;
    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;
    private String senhaAdm;
    private EditText etSenha;
    private EditText senhaAtual;
    private EditText novaSenha;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle retorno = getIntent().getExtras();

        if(retorno!= null){
            if(retorno.getString("SignOut") != null){
                AlertDialog.Builder confirmaSenha2 = new AlertDialog.Builder(MainActivity.this);

                confirmaSenha2.setTitle("Atualização efetuada com sucesso");
                confirmaSenha2.setCancelable(false);

                confirmaSenha2.setPositiveButton("fazer login novamente", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which){
                        autenticacao = ConfiguracaoFirebase.getAutenticacao();
                        autenticacao.signOut();
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                });


                confirmaSenha2.create();
                confirmaSenha2.show();
            }
        }

        usuarioAutenticacao = ConfiguracaoFirebase.getAutenticacao();

        toolbar = findViewById(R.id.toolbar_principal);

        toolbar.setTitle("FutsalFC - Administrador da equipe");
        setSupportActionBar(toolbar);

        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.stl_tab);
        viewPager = (ViewPager) findViewById(R.id.vp_pagina);

        //distribui as guias por igual
        slidingTabLayout.setDistributeEvenly(true);

        //definindo a cor(color file) do indicador da aba
        slidingTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(this,R.color.colorAccent));

        //instanciando o adaptador de guias
        TabAdapter tabAdapter = new TabAdapter(getSupportFragmentManager());

        viewPager.setAdapter(tabAdapter);
        slidingTabLayout.setViewPager(viewPager);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){

        int itemMenuSelecionado = item.getItemId();

        switch(itemMenuSelecionado){

            case R.id.action_exit:
                deslogarUsuario();
                return true;
            case R.id.action_profile:
                verificarEmail();
                abrirPerfil();
                return true;
            case R.id.action_redoPassw:
                confirmarSenhaAtual();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void deslogarUsuario(){
        usuarioAutenticacao.signOut();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void abrirPerfil(){

        AlertDialog.Builder confirmaSenha = new AlertDialog.Builder(MainActivity.this);

        confirmaSenha.setTitle("Favor confirmar Senha atual");
        confirmaSenha.setMessage("Senha");
        confirmaSenha.setCancelable(false);

        etSenha = new EditText(MainActivity.this);
        etSenha.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        confirmaSenha.setView(etSenha);

        confirmaSenha.setPositiveButton("Confirmar", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int which){
                if(etSenha.getText().toString() != null){
                    reautenticacao();
                }else{
                    Toast.makeText(MainActivity.this, "Favor digitar senha atual", Toast.LENGTH_LONG).show();
                }

            }
        });

        confirmaSenha.create();
        confirmaSenha.show();
    }

    private void confirmarSenhaAtual(){

        AlertDialog.Builder confirmaSenha = new AlertDialog.Builder(MainActivity.this);

        confirmaSenha.setTitle("ConfirmarSenha");
        confirmaSenha.setMessage("Senha Atual:");
        confirmaSenha.setCancelable(false);

        senhaAtual = new EditText(MainActivity.this);
        senhaAtual.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        confirmaSenha.setView(senhaAtual);

        confirmaSenha.setPositiveButton("Confirmar", new DialogInterface.OnClickListener(){
           @Override
           public void onClick(DialogInterface dialog, int which){
               if(senhaAtual.getText().toString() != null) {
                   reautenticacao2();
               }else{
                   Toast.makeText(MainActivity.this, "favor digitar a senha atual", Toast.LENGTH_LONG).show();
               }
           }
        });

        confirmaSenha.create();
        confirmaSenha.show();
    }

    private void AlertRedefinirSenha(){
        AlertDialog.Builder redefinirSenhaDireto = new AlertDialog.Builder(MainActivity.this);

        redefinirSenhaDireto.setTitle("Redefinir Senha");
        redefinirSenhaDireto.setMessage("Nova Senha:");
        redefinirSenhaDireto.setCancelable(false);

        novaSenha = new EditText(MainActivity.this);
        novaSenha.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        redefinirSenhaDireto.setView(novaSenha);

        redefinirSenhaDireto.setPositiveButton("Atualizar", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                if(novaSenha.getText().toString() != null) {
                    redefinirSenha(novaSenha.getText().toString());
                }else{
                    Toast.makeText(MainActivity.this, "favor digitar a nova senha", Toast.LENGTH_LONG).show();
                }

            }
        });

        redefinirSenhaDireto.create();
        redefinirSenhaDireto.show();
    }

    private void redefinirSenha(String novaSenha){
      autenticacao = ConfiguracaoFirebase.getAutenticacao();
      FirebaseUser user = autenticacao.getCurrentUser();

      user.updatePassword(novaSenha)
              .addOnCompleteListener(new OnCompleteListener<Void>(){
                 @Override
                 public void onComplete(@NonNull Task<Void> task){
                     if(task.isSuccessful()){
                         Toast.makeText(MainActivity.this, "Senha Atualizada com sucesso", Toast.LENGTH_SHORT).show();
                        deslogarUsuario();
                     }else{
                         Toast.makeText(MainActivity.this, "Erro ao atualizar senha", Toast.LENGTH_SHORT).show();
                     }
                 }
              });




    }

    private void reautenticacao(){

        senhaAdm = etSenha.getText().toString();

        autenticacao = ConfiguracaoFirebase.getAutenticacao();

        FirebaseUser user = autenticacao.getCurrentUser();

        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(),senhaAdm);

        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(MainActivity.this, "Senha confirmada", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(MainActivity.this, PerfilAdmin.class);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(MainActivity.this, "Senha Incorreta, favor corrigir", Toast.LENGTH_LONG).show();
                            //Intent intent = new Intent(MainActivity.this, MainActivity.class);
                            //startActivity(intent);
                        }

                    }
                });
    }

    private void reautenticacao2(){

        senhaAdm = senhaAtual.getText().toString();

        autenticacao = ConfiguracaoFirebase.getAutenticacao();

        FirebaseUser user = autenticacao.getCurrentUser();

        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), senhaAdm);

        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>(){
                    @Override
                    public void onComplete(@NonNull Task<Void> task){
                        if(task.isSuccessful()){
                            Toast.makeText(MainActivity.this, "Senha confirmada", Toast.LENGTH_LONG).show();
                            AlertRedefinirSenha();

                        }else{
                            Toast.makeText(MainActivity.this, "Senha Incorreta, favor corrigir", Toast.LENGTH_LONG).show();
                        }
                    }
        });
    }

    private void verificarEmail(){

    }

/*
AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

        alertDialog.setTitle("Redefinir Senha");
        alertDialog.setMessage("favor confirmar seu endereço de email ");
        alertDialog.setCancelable(false);

        EditText editText = new EditText(MainActivity.this);
        editText.setText(usuarioAutenticacao.getCurrentUser().getEmail());
        alertDialog.setView(editText);

        alertDialog.setNegativeButton("Fechar", new DialogInterface.OnClickListener(){
           @Override
           public void onClick(DialogInterface dialog, int which){
               Toast.makeText(MainActivity.this,"Caixa de Diálogo encerrada", Toast.LENGTH_LONG).show();
           }
        });

        alertDialog.setPositiveButton("Solicitar", new DialogInterface.OnClickListener(){
           @Override
           public void onClick(DialogInterface dialog, int which){
               //Configurar redefinição de senha


               Toast.makeText(MainActivity.this, "Redefinição de senha solicitada com sucesso, favor verificar email", Toast.LENGTH_LONG).show();
           }
        });

        alertDialog.create();
        alertDialog.show();
 */


}
