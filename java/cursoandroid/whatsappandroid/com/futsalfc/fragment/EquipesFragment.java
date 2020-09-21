package cursoandroid.whatsappandroid.com.futsalfc.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import cursoandroid.whatsappandroid.com.futsalfc.R;
import cursoandroid.whatsappandroid.com.futsalfc.activity.CriarEquipeActivity;
import cursoandroid.whatsappandroid.com.futsalfc.activity.CriarJogadorActivity;
import cursoandroid.whatsappandroid.com.futsalfc.activity.MainActivity;
import cursoandroid.whatsappandroid.com.futsalfc.activity.Manter_Equipe;
import cursoandroid.whatsappandroid.com.futsalfc.activity.manter_jogadores;
import cursoandroid.whatsappandroid.com.futsalfc.config.ConfiguracaoFirebase;
import cursoandroid.whatsappandroid.com.futsalfc.helper.Preferencias;
import cursoandroid.whatsappandroid.com.futsalfc.template.Equipe;

/**
 * A simple {@link Fragment} subclass.
 */
public class EquipesFragment extends Fragment {

    private Button botaoCriarEquipe;
    private Button botaoInserirJogador;
    private Button botaoManterEquipe;
    private Button botaoManterJogador;
    private DatabaseReference firebase;
    private String idEquipeFirebase="";

    public EquipesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_equipes, container, false);

        botaoCriarEquipe = (Button) view.findViewById(R.id.botaoCriarEquipeFragmentId);
        botaoInserirJogador = (Button) view.findViewById(R.id.botaoInserirJogadorFragmentId);
        botaoManterEquipe = (Button) view.findViewById(R.id.botaoManterEquipeId);
        botaoManterJogador = (Button) view.findViewById(R.id.botaoManterJogadorId);

        Preferencias preferencias = new Preferencias(getActivity());
        String identificadorUsuario = preferencias.getIdentificadorUsuario();
        //String identificadorEquipe = preferencias.getIdentificadorEquipe();

        //Toast.makeText(getContext(),"Equipe:"+ identificadorEquipe, Toast.LENGTH_LONG).show();

        //Log.i("IdentificadorEquipe2",identificadorEquipe);

        firebase = ConfiguracaoFirebase.getFirebase().child("equipes").child(identificadorUsuario);

        //consultando os dados das equipes uma única vez
        firebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //se houver dados
                if(snapshot.getValue()!= null){
                    //desativa o botão
                    botaoCriarEquipe.setEnabled(false);
                    botaoCriarEquipe.setTextColor(Color.GRAY);
                    Toast.makeText(getContext(),"Usuário já detem uma equipe", Toast.LENGTH_LONG).show();
                    setEquipeFirebase();
                }
                else{
                    botaoCriarEquipe.setEnabled(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        botaoCriarEquipe.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getActivity(), CriarEquipeActivity.class);
                startActivity(intent);
            }
        });

        botaoInserirJogador.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getActivity(), CriarJogadorActivity.class);
                startActivity(intent);
            }
        });

        botaoManterEquipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Manter_Equipe.class);
                startActivity(intent);
            }
        });

        botaoManterJogador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), manter_jogadores.class);
                startActivity(intent);
            }
        });


        return view;
    }

    public void setEquipeFirebase(){
        final Preferencias preferencias = new Preferencias(getActivity());
        final String idUsuarioLogado = preferencias.getIdentificadorUsuario();

        firebase = ConfiguracaoFirebase.getFirebase();

        firebase = firebase.child("equipes").child(idUsuarioLogado);

        firebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                for(DataSnapshot dados: snapshot.getChildren()){
                    Equipe equipe= dados.getValue(Equipe.class);
                    idEquipeFirebase = equipe.getIdentificadorEquipe();
                    preferencias.salvarDadosEquipe(idEquipeFirebase);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //Toast.makeText(getApplicationContext(),idEquipeFirebase,Toast.LENGTH_LONG).show();
        //Toast.makeText(getApplicationContext(),getEquipeFirebase(),Toast.LENGTH_LONG).show();
    }
}
