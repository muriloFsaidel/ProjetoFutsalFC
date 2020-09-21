package cursoandroid.whatsappandroid.com.futsalfc.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import cursoandroid.whatsappandroid.com.futsalfc.R;
import cursoandroid.whatsappandroid.com.futsalfc.activity.Marcar_partida;
import cursoandroid.whatsappandroid.com.futsalfc.activity.TabelaJogos;
import cursoandroid.whatsappandroid.com.futsalfc.activity.estatisticas_gerais;

/**
 * A simple {@link Fragment} subclass.
 */
public class JogosFragment extends Fragment {

    private Button botaoEstatisticas;
    private Button botaoCadastrarJogos;
    private Button botaoJogos;

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
