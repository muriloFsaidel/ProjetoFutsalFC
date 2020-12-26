package cursoandroid.whatsappandroid.com.futsalfc.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import cursoandroid.whatsappandroid.com.futsalfc.R;
import cursoandroid.whatsappandroid.com.futsalfc.activity.TabelaJogos;
import cursoandroid.whatsappandroid.com.futsalfc.activity.estatisticas_gerais;
import cursoandroid.whatsappandroid.com.futsalfc.config.ConfiguracaoFirebase;
import cursoandroid.whatsappandroid.com.futsalfc.helper.Preferencias;

/**
 * A simple {@link Fragment} subclass.
 */
public class JogosEquipeJogadorUsuario extends Fragment {

    private Button botaoJogos;
    private Button botaoEstatisticas;
    private String nomeJogadorUsuarioFirebaseExtra;
    private String  emailResponsavelFirebaseExtra;
    private TextView textView;
    private TextView verificarEmail;
    private FirebaseAuth autenticacao;

    public JogosEquipeJogadorUsuario(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //monta a view a partir do xml do fragment
        View view = inflater.inflate(R.layout.fragment_jogos_equipe_jogador_usuario, container, false);

        botaoJogos = (Button) view.findViewById(R.id.btJogosJogadorUsuario);
        botaoEstatisticas = (Button) view.findViewById(R.id.btEstatisticasJogadorUsuario);
        textView = (TextView) view.findViewById(R.id.textViewJogadorUsuarioJogos);
        verificarEmail = (TextView) view.findViewById(R.id.TvVerificarEmailEquipeJogadorUsuarioId);

        Preferencias preferencias = new Preferencias(getActivity());
        nomeJogadorUsuarioFirebaseExtra = preferencias.getNomeJogador();

        textView.setText("Bem-vindo ao FutsalFC "+nomeJogadorUsuarioFirebaseExtra);

        autenticacao = ConfiguracaoFirebase.getAutenticacao();
        FirebaseUser user = autenticacao.getCurrentUser();

        if(user.isEmailVerified()){
            verificarEmail.setText("");
        }else{
            verificarEmail.setText("Favor confirmar email cadastrado na sua caixa de entrada pessoal");
            user.sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>(){
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

        botaoJogos.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getActivity(), TabelaJogos.class);
                String mainActivity2 = "MainActivity2";
                intent.putExtra("MainActivity2",mainActivity2);
                startActivity(intent);
            }
        });

        botaoEstatisticas.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v1){
                Intent intent = new Intent(getActivity(), estatisticas_gerais.class);
                String mainActivity2 = "MainActivity2";
                intent.putExtra("MainActivity2", mainActivity2);
                startActivity(intent);
            }
        });
        return view;


    }

     /*Bundle retornoLogin = getActivity().getIntent().getExtras();

        if(retornoLogin != null){
            nomeJogadorUsuarioFirebaseExtra = retornoLogin.getString("nomeJogador");
            emailResponsavelFirebaseExtra = retornoLogin.getString("emailResponsavel");
        }*/
}
