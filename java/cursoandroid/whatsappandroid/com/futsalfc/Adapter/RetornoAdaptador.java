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
import cursoandroid.whatsappandroid.com.futsalfc.template.DadosPartida;

public class RetornoAdaptador extends ArrayAdapter<DadosPartida> {

    private ArrayList<DadosPartida> dadosPartidas;
    private Context contexto;

    public RetornoAdaptador (Context c, ArrayList<DadosPartida> objects){
        super(c,0,objects);
        this.contexto = c;
        this.dadosPartidas = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){

        View view = null;

        if(dadosPartidas != null){

            LayoutInflater  inflater = (LayoutInflater) contexto.getSystemService(contexto.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.lista_retorno,parent,false);

            TextView nomeJogador = view.findViewById(R.id.tv_nome_jogador_retorno);
            TextView golsJogador = view.findViewById(R.id.tv_gols_retorno);

            DadosPartida dadosPartida = dadosPartidas.get(position);

            nomeJogador.setText("Jogador: "+dadosPartida.getJogador());
            golsJogador.setText("Gols: "+dadosPartida.getGols());

        }

        return view;

    }
}
