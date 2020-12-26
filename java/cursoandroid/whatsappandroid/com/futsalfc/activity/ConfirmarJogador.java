package cursoandroid.whatsappandroid.com.futsalfc.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
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
import cursoandroid.whatsappandroid.com.futsalfc.template.Usuario;

public class ConfirmarJogador extends AppCompatActivity {

    private EditText etnomeJogadorUsuario;
    private EditText etemailResponsavel;
    private DatabaseReference firebase;
    private DatabaseReference firebase2;
    private FirebaseAuth autenticacao;
    private Toolbar toolbar;
    private Button botaoConfirmarDados;
    private String sNomeJogador;
    private String sEmailResponsavel;
    //private Preferencias preferencias;
    private String idResponsavel;
    private String idEquipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmar_jogador);

        etnomeJogadorUsuario = (EditText) findViewById(R.id.etNomeJogadorUsuario);
        etemailResponsavel = (EditText) findViewById(R.id.etEmailResponsavelConfirmar);
        botaoConfirmarDados = (Button) findViewById(R.id.bt_confirmar_dados_jogador_usuario);

        Preferencias preferencias = new Preferencias(ConfirmarJogador.this);
        etnomeJogadorUsuario.setText(preferencias.getNomeJogador());
        etemailResponsavel.setText(preferencias.getEmailResponsavelJogadorUsuario());

        sNomeJogador = etnomeJogadorUsuario.getText().toString().toLowerCase();
        sEmailResponsavel = etemailResponsavel.getText().toString().toLowerCase();

        toolbar = (Toolbar) findViewById(R.id.toolbar_confirmar_jogador);

        toolbar.setTitle("Atualizar dados jogador usuário");
        setSupportActionBar(toolbar);

        //Toast.makeText(ConfirmarJogador.this, "antes de preencher, consulte o administrador do seu time e atualize tanto Nome quanto Email Responsável", Toast.LENGTH_LONG).show();

        String idResponsavel = preferencias.getJogadorUsuario();
        EquipeExistente(idResponsavel);
        //idEquipe = preferencias.getIdentificadorEquipe();




        botaoConfirmarDados.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                confirmarNomeJogadorUsuarioFirebase(sNomeJogador, sEmailResponsavel);
            }
        });

    }

    public void confirmarNomeJogadorUsuarioFirebase(final String NomeJogador, final String EmailResponsavel){

        autenticacao = ConfiguracaoFirebase.getAutenticacao();

        final Preferencias preferencias = new Preferencias(ConfirmarJogador.this);
        String idEquipe = preferencias.getIdentificadorEquipe();
        String idResponsavel = preferencias.getJogadorUsuario();
        //String idJogador = preferencias.getIdentificadorJogador();

        firebase = ConfiguracaoFirebase.getFirebase().child("jogadores").child(idEquipe).child(idResponsavel);

        firebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //if(snapshot.getValue()!= null){

                for(DataSnapshot dados: snapshot.getChildren()){
                    Jogador jogador = dados.getValue(Jogador.class);

                    if(NomeJogador.equals(jogador.getNome())) {

                        firebase = ConfiguracaoFirebase.getFirebase();

                        String idJogadorUsuario = Base64Custom.codificarParaBase64(autenticacao.getCurrentUser().getEmail().toString());

                        firebase = firebase.child("jogadoresUsuarios")
                                .child(idJogadorUsuario);

                        JogadorUsuario jogadorUsuario = new JogadorUsuario();
                        jogadorUsuario.setIdJogadorUsuario(Base64Custom.codificarParaBase64(autenticacao.getCurrentUser().getEmail().toString()));
                        jogadorUsuario.setEmailJogador(autenticacao.getCurrentUser().getEmail().toString());
                        jogadorUsuario.setEmailResponsavel(EmailResponsavel);
                        jogadorUsuario.setNome(NomeJogador);

                        firebase.setValue(jogadorUsuario);

                        preferencias.salvarDadosJogador(jogador.getIdentificadorJogador());
                        preferencias.salvarDadosNomeJogador(jogador.getNome());

                        Intent intent = new Intent(ConfirmarJogador.this, MainActivity2.class);
                        intent.putExtra("MainActivity2", "MainActivity2");
                        startActivity(intent);

                        //preferencias.salvarDadosNomeJogador();
                       } else{
                        Toast.makeText(ConfirmarJogador.this,"Favor preencher o campo nome corretamente", Toast.LENGTH_LONG).show();
                        etnomeJogadorUsuario.setText("");
                         }
                    }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void EquipeExistente( String idResponsavel){

        //String idResponsavel = Base64Custom.codificarParaBase64(emailResponsavel);

        //Preferencias preferencias = new Preferencias(getActivity());
        //preferencias.getJogadorUsuario() != null
        //if(idResponsavel != null){
            //pós acertar a atualização do email do usuário adm em perfilADM
            //String idResponsavel = preferencias.getJogadorUsuario();
            firebase = ConfiguracaoFirebase.getFirebase().child("equipes").child(idResponsavel);

            firebase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    //capturando o identificador da equipe
                    for(DataSnapshot dados: snapshot.getChildren()){
                    //if(snapshot.getValue() != null){
                        //recuperando dados de acordo com os atributos da classe entidade
                        Equipe equipe = dados.getValue(Equipe.class);
                        Preferencias preferencias = new Preferencias(ConfirmarJogador.this);
                        preferencias.salvarDadosEquipe(equipe.getIdentificadorEquipe());
                        idEquipe = equipe.getIdentificadorEquipe();
                        String idResponsavel = preferencias.getJogadorUsuario();
                        //Toast.makeText(ConfirmarJogador.this, "idEquipe:"+idEquipe+"\n\n idResponsavel:"+idResponsavel, Toast.LENGTH_LONG).show();
                    //}


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        //}else{
            //Toast.makeText(ConfirmarJogador.this,".",Toast.LENGTH_SHORT).show();
        //}



    }

    public void voltar8(View view){
        Bundle retorno = getIntent().getExtras();

        if(retorno != null){
            Intent intent = new Intent(ConfirmarJogador.this, MainActivity2.class);
            intent.putExtra("MainActivity2","MainActivity2");
            startActivity(intent);
        }
    }


    /*public void ResponsavelExistente(){

        //convertendo email para base64
        //String idResponsavel = Base64Custom.codificarParaBase64(sEmailResponsavel);

        //acessando o nó do usuario responsável
        firebase = ConfiguracaoFirebase.getFirebase().child("usuarios");

        //disparando evento de consulta
        firebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //varrendo usuarios adm
                for(DataSnapshot dados: snapshot.getChildren()){

                    Usuario usuario = dados.getValue(Usuario.class);

                    if(sEmailResponsavel.equals(usuario.getEmail())){
                        Preferencias preferencias = new Preferencias(ConfirmarJogador.this);

                        //String idResponsavel = Base64Custom.codificarParaBase64(sEmailResponsavel);
                        //salvando o identificador do responsável pelo time para resgatar jogos e estatísticas
                        preferencias.salvarDadosJogadorUsuario(usuario.getId());
                        idResponsavel = usuario.getId();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }*/

    /*public void EquipeExistente(){
        //String idResponsavel = Base64Custom.codificarParaBase64(sEmailResponsavel);

        //final Preferencias preferencias = new Preferencias(ConfirmarJogador.this);
        firebase = ConfiguracaoFirebase.getFirebase().child("equipes").child(idResponsavel);

        firebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //capturando o identificador da equipe
                for(DataSnapshot dados: snapshot.getChildren()){
                    //recuperando dados de acordo com os atributos da classe entidade
                    Equipe equipe = dados.getValue(Equipe.class);
                    Preferencias preferencias = new Preferencias(ConfirmarJogador.this);
                    preferencias.salvarDadosEquipe(equipe.getIdentificadorEquipe());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }*/




    //se houver dados, o responsável está confirmado
                //if(snapshot.getValue() != null){


    /*

            //ResponsavelExistente();
            //EquipeExistente();
            //jogadorExistente();
     */

    /*private void confirmarEmailResponsavelJogadorUsuarioFirebase(final String EmailResponsavel){
     firebase não dispara mais de uma consulta ao mesmo tempo
        autenticacao = ConfiguracaoFirebase.getAutenticacao();

        firebase2 = ConfiguracaoFirebase.getFirebase().child("usuarios");

        firebase2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //varrendo usuários
                for(DataSnapshot dados: snapshot.getChildren()){
                    Usuario usuario = dados.getValue(Usuario.class);
                    if(EmailResponsavel.equals(usuario.getEmail())){

                        firebase2 = ConfiguracaoFirebase.getFirebase();

                        String idJogadorUsuario = Base64Custom.codificarParaBase64(autenticacao.getCurrentUser().getEmail().toString());

                        firebase2 = firebase2.child("jogadoresUsuarios")
                                            .child(idJogadorUsuario)
                                             .child("emailResponsavel");

                        firebase2.setValue(EmailResponsavel);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }*/
    /*
    JogadorUsuario jogadorUsuario = new JogadorUsuario();
    jogadorUsuario.setEmailJogador(autenticacao.getCurrentUser().getEmail().toString());
    jogadorUsuario.setEmailResponsavel(EmailResponsavel);
    jogadorUsuario.setNome(NomeJogador);
    */
    /*
     public void jogadorExistente(){

        preferencias = new Preferencias(ConfirmarJogador.this);
        String idEquipe = preferencias.getIdentificadorEquipe();
        String idResponsavel = preferencias.getJogadorUsuario();

        firebase = ConfiguracaoFirebase.getFirebase().child("jogadores").child(idEquipe).child(idResponsavel);

        firebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //varrendo jogadores
                for(DataSnapshot dados: snapshot.getChildren()){
                    Jogador jogador = dados.getValue(Jogador.class);
                    if(sNomeJogador.equals(jogador.getNome())){
                        preferencias.salvarDadosJogador(jogador.getIdentificadorJogador());
                        preferencias.salvarDadosNomeJogador(jogador.getNome());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    */

    //String idEquipe = preferencias.getIdentificadorEquipe();
    //String idResponsavel = preferencias.getIdentificadorUsuario();
    //String idJogador = preferencias.getIdentificadorJogador();

    //if(idEquipe.equals("") || idResponsavel.equals("") || idJogador.equals("VAZIO")){
    //Toast.makeText(ConfirmarJogador.this, "Favor preencher os dados corretamente, \nIdEquipe:"+idEquipe+"\nIdResponsavel:"+idResponsavel+"\nIdJogador:"+idJogador, Toast.LENGTH_LONG).show();
    //}else{

    //}
}
