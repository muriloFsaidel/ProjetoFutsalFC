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
import cursoandroid.whatsappandroid.com.futsalfc.template.Partida;

public class TabelaJogosAdaptador extends ArrayAdapter<Partida> {

    private ArrayList<Partida> partidas;
    private Context contexto;

    public TabelaJogosAdaptador (@NonNull Context c, @NonNull ArrayList<Partida> objects){
        super(c,0, objects);
        this.contexto = c;
        this.partidas = objects;
    }

    @NonNull
    @Override
    public View getView (int position, @Nullable View convertView, @NonNull ViewGroup parent){

        View view = null;

        if(partidas != null){
            LayoutInflater inflater =  (LayoutInflater) contexto.getSystemService(contexto.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.lista_tabela_jogos,parent, false);

            TextView data = view.findViewById(R.id.tv_data_tabela_jogos);
            TextView placarTotal = view.findViewById(R.id.tv_placar_tabela_jogos);
            TextView quadro = view.findViewById(R.id.tv_quadro_tabela_jogos);


            Partida partida = partidas.get(position);
            String placarCasa = String.valueOf(partida.getPlacarDaCasa());
            String placarOponente = String.valueOf(partida.getPlacarOponente());

            data.setText("Data: "+partida.getData());
            placarTotal.setText(partida.getEquipe()+" "+placarCasa+" X "+placarOponente+" "+partida.getOponente());
            quadro.setText("Quadro: "+partida.getQuadro());
        }




        return view;
    }
}
