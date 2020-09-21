package cursoandroid.whatsappandroid.com.futsalfc.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import com.google.firebase.auth.FirebaseAuth;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usuarioAutenticacao = ConfiguracaoFirebase.getAutenticacao();

        toolbar = findViewById(R.id.toolbar_principal);

        toolbar.setTitle("FutsalFC");
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
                abrirPerfil();
                return true;
            case R.id.action_redoPassw:
                redefinirSenha();
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

    }


    private void redefinirSenha(){

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


    }






}
