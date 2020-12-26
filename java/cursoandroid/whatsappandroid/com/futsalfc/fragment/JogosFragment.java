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

import org.w3c.dom.Text;

import cursoandroid.whatsappandroid.com.futsalfc.R;
import cursoandroid.whatsappandroid.com.futsalfc.activity.Marcar_partida;
import cursoandroid.whatsappandroid.com.futsalfc.activity.TabelaJogos;
import cursoandroid.whatsappandroid.com.futsalfc.activity.estatisticas_gerais;
import cursoandroid.whatsappandroid.com.futsalfc.config.ConfiguracaoFirebase;

/**
 * A simple {@link Fragment} subclass.
 */
public class JogosFragment extends Fragment {

    private Button botaoEstatisticas;
    private Button botaoCadastrarJogos;
    private Button botaoJogos;
    private FirebaseAuth autenticacao;
    private TextView verificarEmail;

    public JogosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_jogos, container, false);

        botaoEstatisticas = (Button) view.findViewById(R.id.botaoEstatisticasId);
        botaoCadastrarJogos = (Button) view.findViewById(R.id.botaoMarcarPartidaId);
        botaoJogos = (Button) view.findViewById(R.id.botaoJogosId);
        verificarEmail = (TextView) view.findViewById(R.id.TvVerificarEmailJogosADMId);

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

        botaoEstatisticas.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getActivity(), estatisticas_gerais.class);
                startActivity(intent);
            }
        });

        botaoCadastrarJogos.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getActivity(), Marcar_partida.class);
                startActivity(intent);
            }
        });

        botaoJogos.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getActivity(), TabelaJogos.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
