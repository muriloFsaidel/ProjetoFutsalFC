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
import cursoandroid.whatsappandroid.com.futsalfc.template.Jogador;

public class JogadorAdaptador extends ArrayAdapter<Jogador> {

    private ArrayList<Jogador>  jogadores;
    private Context contexto;

    public JogadorAdaptador(@NonNull Context c, @NonNull ArrayList<Jogador> objects){
        super(c,0,objects);
        this.contexto = c;
        this.jogadores = objects;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){

        View view = null;

        if(jogadores != null){

            LayoutInflater inflater = (LayoutInflater) contexto.getSystemService(contexto.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.lista_jogador,parent, false);

            TextView nomeJogador = (TextView) view.findViewById(R.id.tv_nome_jogador_manter);
            TextView posicaoJogador = (TextView) view.findViewById(R.id.tv_posicao_jogador_manter);
            TextView ativoJogador = (TextView) view.findViewById(R.id.tv_ativo_jogador_manter);

            Jogador jogador = jogadores.get(position);
            nomeJogador.setText("Nome: "+jogador.getNome());
            posicaoJogador.setText("Posição: "+jogador.getPosicao());
            ativoJogador.setText("Ativo: "+jogador.getAtivo());
        }

        return view;

    }
}
