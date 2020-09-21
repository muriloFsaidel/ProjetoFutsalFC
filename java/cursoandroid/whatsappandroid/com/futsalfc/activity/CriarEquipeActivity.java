package cursoandroid.whatsappandroid.com.futsalfc.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;

import cursoandroid.whatsappandroid.com.futsalfc.R;
import cursoandroid.whatsappandroid.com.futsalfc.config.ConfiguracaoFirebase;
import cursoandroid.whatsappandroid.com.futsalfc.fragment.EquipesFragment;
import cursoandroid.whatsappandroid.com.futsalfc.helper.Base64Custom;
import cursoandroid.whatsappandroid.com.futsalfc.helper.Preferencias;
import cursoandroid.whatsappandroid.com.futsalfc.template.Equipe;

public class CriarEquipeActivity extends AppCompatActivity {

    private EditText nomeEquipe;
    private EditText liga;
    private Button botaoCriarEquipe;
    private DatabaseReference firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_equipe);

        nomeEquipe = (EditText) findViewById(R.id.NomeEquipeId);
        liga = (EditText) findViewById(R.id.ligaId);
        botaoCriarEquipe = (Button) findViewById(R.id.botaoCriarEquipeId);

        botaoCriarEquipe.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                if(nomeEquipe.getText().toString().equals("") || liga.getText().toString().equals("") ){
                        Toast.makeText(getApplicationContext(),"Favor preencher nome & liga",Toast.LENGTH_LONG).show();
                }
                else{
                    Preferencias preferencias = new Preferencias(CriarEquipeActivity.this);
                    //recuperando o idUser em base64
                    String identificadorUsuario = preferencias.getIdentificadorUsuario();

                    //codificando o nome da equipe para base64
                    String identificadorEquipe = Base64Custom.codificarParaBase64(nomeEquipe.getText().toString());
                    preferencias.salvarDadosEquipe(identificadorEquipe);

                    //recuperando instância do firebase a partir da raiz
                    firebase = ConfiguracaoFirebase.getFirebase();

                /*
                     +equipes
                        +murilodahora@hotmail.com(em base64)
                         +Pés na Rocha(em base64)
                 */

                    //criando nós
                    firebase = firebase.child("equipes")
                            .child(identificadorUsuario)
                            .child(identificadorEquipe);

                    //criando registros com o objeto equipe
                    Equipe equipe = new Equipe();
                    equipe.setIdentificadorEquipe(identificadorEquipe);
                    equipe.setNome(nomeEquipe.getText().toString());
                    equipe.setLiga(liga.getText().toString());

                    firebase.setValue(equipe);

                 /*
                    +Pés na Rocha(em base64)
                     -IdentificadorEquipe: "fmusfhusfhu7ewrf88"
                     -nome: "Pés na Rocha"
                     -liga: "Série A"
                 */

                    voltarParaFragmento();
                }

            }
        });
    }

    private void voltarParaFragmento(){
        Toast.makeText(CriarEquipeActivity.this,"",Toast.LENGTH_LONG).show();
        Intent intent = new Intent(CriarEquipeActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
