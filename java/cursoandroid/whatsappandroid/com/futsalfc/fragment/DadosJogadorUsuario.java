package cursoandroid.whatsappandroid.com.futsalfc.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.solver.widgets.Snapshot;
import androidx.fragment.app.Fragment;

import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import cursoandroid.whatsappandroid.com.futsalfc.R;
import cursoandroid.whatsappandroid.com.futsalfc.activity.ConfirmarJogador;
import cursoandroid.whatsappandroid.com.futsalfc.activity.LoginActivity;
import cursoandroid.whatsappandroid.com.futsalfc.activity.VisualizarJogador;
import cursoandroid.whatsappandroid.com.futsalfc.config.ConfiguracaoFirebase;
import cursoandroid.whatsappandroid.com.futsalfc.helper.Base64Custom;
import cursoandroid.whatsappandroid.com.futsalfc.helper.Preferencias;
import cursoandroid.whatsappandroid.com.futsalfc.template.Equipe;
import cursoandroid.whatsappandroid.com.futsalfc.template.Jogador;
import cursoandroid.whatsappandroid.com.futsalfc.template.Usuario;

/**
 * A simple {@link Fragment} subclass.
 */
public class DadosJogadorUsuario extends Fragment {

    private TextView  textView;
    private String nomeJogadorFirebase;
    private String emailResponsavelFirebase;
    private DatabaseReference firebase;
    private Button botaoConfirmarJogadorUsuario;
    private Button botaoVisualiarJogador;
    private String nomeEquipeFirebaseExtra;
    private FirebaseAuth autenticacao;
    private String nomeDoJogador;
    private String emailResponsavel;
    private String idResponsavel;
    private TextView verificarEmail;
    //private Preferencias preferencias;

    public DadosJogadorUsuario(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //monta a view a partir do xml do fragment
        View view = inflater.inflate(R.layout.fragment_dados_jogador_usuario, container, false);

        textView = (TextView) view.findViewById(R.id.textViewJogadorUsuario);
        botaoConfirmarJogadorUsuario = (Button) view.findViewById(R.id.btConfirmarJogadorUsuario);
        botaoVisualiarJogador = (Button) view.findViewById(R.id.btVisualizarJogador);
        verificarEmail = (TextView) view.findViewById(R.id.TvVerificarEmailDadosJogadorUsuarioId);
        Bundle retornoLogin = getActivity().getIntent().getExtras();

        if(retornoLogin != null){
            String MainActivity2 = retornoLogin.getString("MainActivity2");
        }

        Preferencias preferencias = new Preferencias(getActivity());
        nomeDoJogador = preferencias.getNomeJogador();
        emailResponsavel = preferencias.getEmailResponsavelJogadorUsuario();
        //String idJogador = preferencias.getIdentificadorJogador();

        String idEquipe = preferencias.getIdentificadorEquipe();
        String idResponsavel = preferencias.getJogadorUsuario();

        ResponsavelExistente(emailResponsavel);
        //EquipeExistente();

        autenticacao = ConfiguracaoFirebase.getAutenticacao();
        FirebaseUser user = autenticacao.getCurrentUser();

        if(user.isEmailVerified()){
            verificarEmail.setText("");
        }else{
            verificarEmail.setText("Favor confirmar email cadastrado na sua caixa de entrada pessoal");
            user.sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task){
                            if(task.isSuccessful()){
                                Toast.makeText(getActivity(), "Email de verificação enviado", Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(getActivity(), "Erro ao enviar email de verificação", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }




        //preencheNomeEquipe();

        //Log.i("idEquipe",idEquipe);
        //Log.i("idResponsavel",idResponsavel);
        //Log.i("IdJogador",idJogador);
        //Toast.makeText(getActivity(), "IdJogador"+idJogador, Toast.LENGTH_LONG).show();
        //emailResponsavelFirebase = Base64Custom.decodificarDaBase64(idResponsavel);

        //Toast.makeText(getActivity(), "\nIdEquipe:"+idEquipe+"\nIdResponsavel:"+idResponsavel, Toast.LENGTH_LONG).show();
        //Toast.makeText(getActivity(), "\nIdEquipe:"+idEquipe+"\nIdResponsavel:"+idResponsavel+"\nIdJogador:"+idJogador, Toast.LENGTH_LONG).show();
        Toast.makeText(getActivity(), "Aviso, caso haja falha ao consultar os dados de jogador ou equipe, favor atualizar dados do jogador usuário", Toast.LENGTH_LONG).show();
        nomeJogadorFirebase = nomeDoJogador;

        botaoConfirmarJogadorUsuario.setEnabled(true);
        botaoConfirmarJogadorUsuario.setTextColor(Color.WHITE);

        botaoConfirmarJogadorUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmaNomeJogadorEmailResponsavel();
            }
        });

        botaoVisualiarJogador.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getActivity(), VisualizarJogador.class);
                intent.putExtra("MainActivity2","MainActivity2");
                //intent.putExtra("nomeEquipe", nomeEquipeFirebaseExtra);
                startActivity(intent);
            }
        });

        textView.setText("Bem-vindo ao FutsalFC "+nomeJogadorFirebase);

        return view;
    }

    public void ResponsavelExistente(final String emailResponsavel){

        //convertendo email para base64
        //String idResponsavel = Base64Custom.codificarParaBase64(emailResponsavel);

        //acessando o nó do usuario responsável
        firebase = ConfiguracaoFirebase.getFirebase().child("usuarios");

        //disparando evento de consulta
        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //varrendo para encontrar o responsável correto
                for(DataSnapshot dados: snapshot.getChildren()){
                    Usuario usuario = dados.getValue(Usuario.class);

                    if(emailResponsavel.equals(usuario.getEmail())){
                        Preferencias preferencias = new Preferencias(getActivity());
                        //salvando o identificador do responsável pelo time para resgatar jogos e estatísticas
                        preferencias.salvarDadosJogadorUsuario(usuario.getId());
                        //idResponsavel = usuario.getId();
                        EquipeExistente(usuario.getId());
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
                    Preferencias preferencias = new Preferencias(getActivity());
                    preferencias.salvarDadosEquipe(equipe.getIdentificadorEquipe());
                    String idEquipe = equipe.getIdentificadorEquipe();
                    String idResponsavel = preferencias.getJogadorUsuario();
                    //Toast.makeText(getActivity(), "idEquipe:"+idEquipe+"\n\n idResponsavel:"+idResponsavel, Toast.LENGTH_LONG).show();
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

    /*public void ResponsavelExistente2(final String emailResponsavel){

        //convertendo email para base64
        //String idResponsavel = Base64Custom.codificarParaBase64(emailResponsavel);

        //acessando o nó do usuario responsável
        firebase = ConfiguracaoFirebase.getFirebase().child("usuarios");

        //disparando evento de consulta
        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //varrendo para encontrar o responsável correto
                for(DataSnapshot dados: snapshot.getChildren()){
                    Usuario usuario = dados.getValue(Usuario.class);

                    if(emailResponsavel.equals(usuario.getEmail())){
                       Preferencias preferencias = new Preferencias(getActivity());
                        //salvando o identificador do responsável pelo time para resgatar jogos e estatísticas
                        preferencias.salvarDadosJogadorUsuario(usuario.getId());
                        EquipeExistente2(usuario.getId());
                    }
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void EquipeExistente2(String idResponsavel){

        //String idResponsavel = Base64Custom.codificarParaBase64(emailResponsavel);

             //final Preferencias preferencias = new Preferencias(getActivity());
              //String idResponsavel = preferencias.getJogadorUsuario();

             if(idResponsavel != null) {
                 //pós acertar a atualização do email do usuário adm em perfilADM

                 firebase = ConfiguracaoFirebase.getFirebase().child("equipes").child(idResponsavel);

                 firebase.addValueEventListener(new ValueEventListener() {
                     @Override
                     public void onDataChange(@NonNull DataSnapshot snapshot) {

                         //capturando o identificador da equipe
                         for (DataSnapshot dados : snapshot.getChildren()) {
                             //recuperando dados de acordo com os atributos da classe entidade
                             Equipe equipe = dados.getValue(Equipe.class);
                             Preferencias preferencias = new Preferencias(getActivity());
                             preferencias.salvarDadosEquipe(equipe.getIdentificadorEquipe());
                         }
                     }

                     @Override
                     public void onCancelled(@NonNull DatabaseError error) {

                     }
                 });
             }else{
                 Toast.makeText(getActivity(),".",Toast.LENGTH_SHORT).show();
             }
    }*/

    private void confirmaNomeJogadorEmailResponsavel(){

        AlertDialog.Builder  confirma = new AlertDialog.Builder(getActivity());

        confirma.setTitle("após consultar o administrador, confirmar os dados abaixo");
        confirma.setCancelable(false);

        LinearLayout layout = new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText nomeJogador = new EditText(getActivity());
        nomeJogador.setHint("nome do jogador");
        layout.addView(nomeJogador);

        final EditText emailResponsavel2 = new EditText(getActivity());
        emailResponsavel2.setHint("email do responsavel");
        layout.addView(emailResponsavel2);

        confirma.setView(layout);

        confirma.setPositiveButton("confirmar", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int which){
                    Preferencias preferencias3 = new Preferencias(getActivity());
                    preferencias3.salvarDadosNomeJogador(nomeJogador.getText().toString().toLowerCase());
                    preferencias3.salvarDadosEmailResponsavelJogadorUsuario(emailResponsavel2.getText().toString().toLowerCase());

                    nomeDoJogador = nomeJogador.getText().toString().toLowerCase();
                    emailResponsavel = emailResponsavel2.getText().toString().toLowerCase();

                if(nomeDoJogador.equals("") || emailResponsavel.equals("")) {
                    Toast.makeText(getContext(), "Favor preencher os dados acima", Toast.LENGTH_LONG).show();
                }else{

                   ResponsavelExistente(emailResponsavel);
                   //EquipeExistente();
                   //Toast.makeText(getContext(), "Nome do Jogador: "+nomeDoJogador+"\n Email responsável:"+emailResponsavel, Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(getActivity(), ConfirmarJogador.class);
                    intent.putExtra("MainActivity2","MainActivity2");
                    startActivity(intent);
                }
            }
        });

        confirma.create();
        confirma.show();

    }



}

//pós acertar a atualização do email do usuário adm em perfilADM
// firebase = ConfiguracaoFirebase.getFirebase().child("usuarios");
//for(Snapshot dados: snapshot.getChildren()){
// Usuario usuario = dados.getValue(Usuario.class);
// if(usuario.getEmail() == emailResponsavel){
// preferencias.salvarDadosJogadorUsuario(dados.getId())
//    }
// }

//se houver dados, o responsável está confirmado
//if(snapshot.getValue() != null){
//String idResponsavel = Base64Custom.codificarParaBase64(emailResponsavel);

 /*
    if (!nomeJogador.equals(nombreJuador)){}
    //nomeJogadorFirebaseExtra = retornoLogin.getString("nomeJogador");
    //emailResponsavelFirebaseExtra = retornoLogin.getString("emailResponsavel");
        codificação para o fragment da aba jogador


        private TextView retornoPreferencias;


        retornoPreferencias = (TextView) findViewById(R.id.textView6);

        retornoPreferencias.setText("idResponsavel: "+idResponsavel+"\nidEquipe: "+idEquipe+"\nidJogador: "+idJogador);



         */

 /* it didn't work
        if(idJogador.equals("VAZIO")) {
            Toast.makeText(getActivity(), "Inconsistência de dados, favor corrigir nome do jogador e confirmar o email do responsavel no botão confirmar dados de jogador usuário,\ncaso contrário não será possível consultar os dados da equipe e do jogador logado, \nIdEquipe:"+idEquipe+"\nIdResponsavel:"+idResponsavel+"\nIdJogador:"+idJogador, Toast.LENGTH_LONG).show();
            //habilitar o botão de confirmar dados (nome||email do responsável) (fragment)



        } else if(idResponsavel.equals("") || idEquipe.equals("")){
            Toast.makeText(getActivity(), "Inconsistência de dados, favor corrigir email do responsável e confirmar nome do jogador no botão confirmar dados de jogador usuário,\ncaso contrário não será possível consultar os dados da equipe e do jogador logado", Toast.LENGTH_LONG).show();
            //habilitar o botão de confirmar dados (nome||email do responsável) (fragment)
            botaoConfirmarJogadorUsuario.setEnabled(true);
            botaoConfirmarJogadorUsuario.setTextColor(Color.WHITE);

            botaoConfirmarJogadorUsuario.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ConfirmarJogador.class);
                    intent.putExtra("MainActivity2", "MainActivity2");
                    startActivity(intent);
                }
            });
            }

        else{
            //desabilitar o botão de confirmar dados (nome||email do responsável) (fragment)
            botaoConfirmarJogadorUsuario.setEnabled(false);
            botaoConfirmarJogadorUsuario.setTextColor(Color.GRAY);
            Toast.makeText(getActivity(), "Jogador usuário: "+ nomeJogadorFirebase+" confirmado", Toast.LENGTH_LONG).show();


        }*/

