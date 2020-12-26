package cursoandroid.whatsappandroid.com.futsalfc.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

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

import cursoandroid.whatsappandroid.com.futsalfc.Adapter.TabAdapterJogadorUsuario;
import cursoandroid.whatsappandroid.com.futsalfc.R;
import cursoandroid.whatsappandroid.com.futsalfc.config.ConfiguracaoFirebase;
import cursoandroid.whatsappandroid.com.futsalfc.helper.Base64Custom;
import cursoandroid.whatsappandroid.com.futsalfc.helper.Preferencias;
import cursoandroid.whatsappandroid.com.futsalfc.helper.SlidingTabLayout;
import cursoandroid.whatsappandroid.com.futsalfc.template.Equipe;
import cursoandroid.whatsappandroid.com.futsalfc.template.Jogador;
import cursoandroid.whatsappandroid.com.futsalfc.template.JogadorUsuario;

public class MainActivity2 extends AppCompatActivity {

    private FirebaseAuth autenticacao;
    private Toolbar toolbar;
    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;
    private String senhaJogadorUsuario;
    private EditText senhaAtual;
    private EditText novaSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        autenticacao = ConfiguracaoFirebase.getAutenticacao();

        toolbar = findViewById(R.id.toolbar_principal_Jogador_Usuario);

        toolbar.setTitle("FutsalFC - Jogador Usuário");
        setSupportActionBar(toolbar);

        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.stl_tab_Jogador_Usuario);
        viewPager = (ViewPager) findViewById(R.id.vp_pagina_Jogador_Usuario);

        //distribui as guias igualmente
        slidingTabLayout.setDistributeEvenly(true);

        //atribui a cor do indicador da guia selecionada
        slidingTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(this,R.color.colorAccent));

        //instanciando adaptador para viewPager
        TabAdapterJogadorUsuario tabAdapter2 = new TabAdapterJogadorUsuario(getSupportFragmentManager());

        viewPager.setAdapter(tabAdapter2);
        slidingTabLayout.setViewPager(viewPager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        //monta o menu sobre a toolbar da MainActivity2
        inflater.inflate(R.menu.menu_main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){

        //resgatando endereço de memória do item selecionado
        int itemSelecionado = item.getItemId();

        switch(itemSelecionado){

            case R.id.Redefinir_Senha_Jogador_Usuario:
                confirmarSenhaAtual();
                return true;

            case R.id.action_exit_Jogador_Usuario:
                deslogarUsuario();
                return true;

            // caso não seja nenhum dos anteriores
            default:
                //retorne a implementação padrão
                return super.onOptionsItemSelected(item);
        }
    }

    private void deslogarUsuario(){
        autenticacao.signOut();
        Intent intent = new Intent(MainActivity2.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void confirmarSenhaAtual(){

        AlertDialog.Builder confirmaSenha = new AlertDialog.Builder(MainActivity2.this);

        confirmaSenha.setTitle("ConfirmarSenha");
        confirmaSenha.setMessage("Senha Atual:");
        confirmaSenha.setCancelable(false);

        senhaAtual = new EditText(MainActivity2.this);
        senhaAtual.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        confirmaSenha.setView(senhaAtual);

        confirmaSenha.setPositiveButton("Confirmar", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                if(senhaAtual.getText().toString() != null) {
                    reautenticacao2();
                }else{
                    Toast.makeText(MainActivity2.this, "favor digitar a senha atual", Toast.LENGTH_LONG).show();
                }
            }
        });

        confirmaSenha.create();
        confirmaSenha.show();
    }

    private void AlertRedefinirSenha(){
        AlertDialog.Builder redefinirSenhaDireto = new AlertDialog.Builder(MainActivity2.this);

        redefinirSenhaDireto.setTitle("Redefinir Senha");
        redefinirSenhaDireto.setMessage("Nova Senha:");
        redefinirSenhaDireto.setCancelable(false);

        novaSenha = new EditText(MainActivity2.this);
        novaSenha.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        redefinirSenhaDireto.setView(novaSenha);

        redefinirSenhaDireto.setPositiveButton("Atualizar", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                if(novaSenha.getText().toString() != null) {
                    redefinirSenha(novaSenha.getText().toString());
                }else{
                    Toast.makeText(MainActivity2.this, "favor digitar a nova senha", Toast.LENGTH_LONG).show();
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
                            Toast.makeText(MainActivity2.this, "Senha Atualizada com sucesso", Toast.LENGTH_SHORT).show();
                            deslogarUsuario();
                        }else{
                            Toast.makeText(MainActivity2.this, "Erro ao atualizar senha", Toast.LENGTH_SHORT).show();
                        }
                    }
                });




    }

    private void reautenticacao2(){

        senhaJogadorUsuario = senhaAtual.getText().toString();

        autenticacao = ConfiguracaoFirebase.getAutenticacao();

        FirebaseUser user = autenticacao.getCurrentUser();

        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), senhaJogadorUsuario);

        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>(){
                    @Override
                    public void onComplete(@NonNull Task<Void> task){
                        if(task.isSuccessful()){
                            Toast.makeText(MainActivity2.this, "Senha confirmada", Toast.LENGTH_LONG).show();
                            AlertRedefinirSenha();

                        }else{
                            Toast.makeText(MainActivity2.this, "Senha Incorreta, favor corrigir", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    /*
    AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity2.this);

        alert.setTitle("Redefinir Senha");
        alert.setMessage("favor confimar endereço de email ");
        alert.setCancelable(false);

        EditText editText = new EditText(MainActivity2.this);
        editText.setText(autenticacao.getCurrentUser().getEmail());
        alert.setView(editText);

        alert.setNegativeButton("Fechar", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                Toast.makeText(MainActivity2.this,"Caixa de diálogo encerrada",Toast.LENGTH_LONG).show();
            }
        });

        alert.setPositiveButton("Solicitar", new DialogInterface.OnClickListener(){
           @Override
           public void onClick(DialogInterface dialog, int which){
               //Configurar redefinição de senha

               Toast.makeText(MainActivity2.this,"Redefinição de senha solicitada com sucesso, favor verificar email",Toast.LENGTH_LONG).show();
           }
        });

        alert.create();
        alert.show();
     */

    }
