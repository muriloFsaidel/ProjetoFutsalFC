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
import cursoandroid.whatsappandroid.com.futsalfc.template.Equipe;

public class EquipeAdaptador extends ArrayAdapter<Equipe> {

    private ArrayList<Equipe> equipes;
    private Context context;

    public EquipeAdaptador(@NonNull Context c, @NonNull ArrayList<Equipe> objects) {
        super(c, 0, objects);
        this.context = c;
        this.equipes = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){

        View view = null;

        if(equipes != null){

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.lista_equipe,parent, false);

            TextView nomeEquipe = (TextView) view.findViewById(R.id.tv_nome);
            TextView ligaEquipe = (TextView) view.findViewById(R.id.tv_liga);

            Equipe equipe = equipes.get(position);
            nomeEquipe.setText("Nome: "+equipe.getNome());
            ligaEquipe.setText("Liga: "+equipe.getLiga());

        }

        return view;

    }



}
