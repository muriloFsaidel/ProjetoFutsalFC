package cursoandroid.whatsappandroid.com.futsalfc.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;


import cursoandroid.whatsappandroid.com.futsalfc.R;
import cursoandroid.whatsappandroid.com.futsalfc.template.Estatistica;

public class EstatisticasAdaptador extends ArrayAdapter<Estatistica> {

    private ArrayList<Estatistica> estatisticasGerais;
    private Context contexto;

    public EstatisticasAdaptador(@NonNull Context c, @NonNull ArrayList<Estatistica> objects){
        super(c,0,objects);
        this.contexto = c;
        this.estatisticasGerais = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){

        View view = null;

        if(estatisticasGerais != null){

            LayoutInflater inflater = (LayoutInflater) contexto.getSystemService(contexto.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.lista_estatistica,parent,false);

            TextView nomeJogador = view.findViewById(R.id.tv_nome_jogador_estatistica);
            TextView golsJogador = view.findViewById(R.id.tv_gols_jogador_estistica);
            TextView assJogador = view.findViewById(R.id.tv_ass_jogador_estatistica);
            TextView caJogador = view.findViewById(R.id.tv_ca_jogador_estatistica);
            TextView cvJogador = view.findViewById(R.id.tv_cv_jogador_estatistica);

            Estatistica estatistica = estatisticasGerais.get(position);

            String gols = String.valueOf(estatistica.getGols());
            String ass = String.valueOf(estatistica.getAssistencia());
            String cartaoAmarelo = String.valueOf(estatistica.getCartaoAmarelo());
            String cartaoVermelho = String.valueOf(estatistica.getCartaoVermelho());

            nomeJogador.setText("Nome: "+estatistica.getJogador());
            golsJogador.setText("Gols: "+gols);
            assJogador.setText("Assistência: "+ass);
            caJogador.setText("CA: "+cartaoAmarelo);
            cvJogador.setText("CV: "+cartaoVermelho);

        }

        return view;
    }
}
