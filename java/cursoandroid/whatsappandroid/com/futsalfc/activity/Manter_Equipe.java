package cursoandroid.whatsappandroid.com.futsalfc.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import cursoandroid.whatsappandroid.com.futsalfc.Adapter.EquipeAdaptador;
import cursoandroid.whatsappandroid.com.futsalfc.R;
import cursoandroid.whatsappandroid.com.futsalfc.config.ConfiguracaoFirebase;
import cursoandroid.whatsappandroid.com.futsalfc.helper.Preferencias;
import cursoandroid.whatsappandroid.com.futsalfc.template.Equipe;

public class Manter_Equipe extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter adapter;
    private ArrayList<Equipe> nomeAndliga;
    private EditText liga;
    private Button botaoAtualizaLiga;
    private String idEquipe;
    private String nomeEquipe;
    private DatabaseReference firebaseConsulta;
    private ValueEventListener valueEventListenerConsulta;
    private DatabaseReference firebaseAtualiza;
    private ImageView voltar;
    private Toolbar toolbar;


    @Override
    public void onStart(){
        super.onStart();
        firebaseConsulta.addValueEventListener(valueEventListenerConsulta);
        Log.i("ValueEventListener","onStart");
    }

    @Override
    public void onStop(){
        super.onStop();
        //Para a consulta quando a activity for pausada
        firebaseConsulta.removeEventListener(valueEventListenerConsulta);
        Log.i("ValueEventListener","onStop");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manter__equipe);

        listView = (ListView) findViewById(R.id.listViewNomeLigaId);
        liga = (EditText) findViewById(R.id.editTextLigaId);
        botaoAtualizaLiga = (Button) findViewById(R.id.botaoAtualizaLiga);
        voltar = (ImageView) findViewById(R.id.VoltarId);
        toolbar = (Toolbar) findViewById(R.id.tbManterEquipe);

        toolbar.setTitle("Manter Equipe");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
        setSupportActionBar(toolbar);

        nomeAndliga = new ArrayList<>();

        /* implementação padrão
        adapter = new ArrayAdapter(
                getApplicationContext(),
                R.layout.lista_personalizada,
                nomeAndliga
        )*/;

        //personalizada
        adapter = new EquipeAdaptador(getApplicationContext(),nomeAndliga);


        listView.setAdapter(adapter);

        //recuperando id do usuário
        Preferencias preferencias = new Preferencias(Manter_Equipe.this);
        final String identificadorUsuario = preferencias.getIdentificadorUsuario();

        firebaseConsulta = ConfiguracaoFirebase.getFirebase().child("equipes").child(identificadorUsuario);

        valueEventListenerConsulta = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                nomeAndliga.clear();

                for(DataSnapshot dados: snapshot.getChildren()){

                    Equipe equipe = dados.getValue(Equipe.class);
                    nomeAndliga.add(equipe);
                    idEquipe = equipe.getIdentificadorEquipe();
                    nomeEquipe = equipe.getNome();
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        botaoAtualizaLiga.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(liga.getText().toString().equals("") ){
                    Toast.makeText(getApplicationContext(),"Favor preencher o campo Liga",Toast.LENGTH_LONG).show();
                }else{
                    atualizarLiga(identificadorUsuario,idEquipe, nomeEquipe,liga.getText().toString());
                }

            }
        });
    }

    public void atualizarLiga(String idUsuario, String idEquipe,String nomeEquipe, String novaLiga){

        firebaseAtualiza = ConfiguracaoFirebase.getFirebase();

       firebaseAtualiza =  firebaseAtualiza.child("equipes")
                           .child(idUsuario)
                           .child(idEquipe);

        Equipe equipe = new Equipe();
        equipe.setNome(nomeEquipe);
        equipe.setLiga(novaLiga);
        equipe.setIdentificadorEquipe(idEquipe);

        firebaseAtualiza.setValue(equipe);

        liga.setText("");

    }

    public void voltar(View view){
        Intent intent = new Intent(Manter_Equipe.this, MainActivity.class);
        startActivity(intent);
    }
}
