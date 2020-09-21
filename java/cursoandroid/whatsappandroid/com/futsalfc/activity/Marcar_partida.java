package cursoandroid.whatsappandroid.com.futsalfc.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import cursoandroid.whatsappandroid.com.futsalfc.R;
import cursoandroid.whatsappandroid.com.futsalfc.config.ConfiguracaoFirebase;
import cursoandroid.whatsappandroid.com.futsalfc.helper.Base64Custom;
import cursoandroid.whatsappandroid.com.futsalfc.helper.Preferencias;
import cursoandroid.whatsappandroid.com.futsalfc.template.DadosPartida;
import cursoandroid.whatsappandroid.com.futsalfc.template.Equipe;
import cursoandroid.whatsappandroid.com.futsalfc.template.Estatistica;
import cursoandroid.whatsappandroid.com.futsalfc.template.Jogador;
import cursoandroid.whatsappandroid.com.futsalfc.template.Partida;

public class Marcar_partida extends AppCompatActivity {

    //atributos genéricos
    //Dados de inserção para tabela partida
    private EditText ETdate;
    private EditText ETmyTeam;
    private EditText ETopponent;
    private RadioGroup Rg;
    private RadioButton RbChosen;
    private Spinner SPhome;
    private Spinner SPaway;


    //adaptadores e arrayList para preencher os spinners
    private ArrayAdapter<Integer> AAHome;
    private ArrayAdapter<Integer> AAAway;
    private ArrayList<Integer> ALHomeScore;
    private ArrayList<Integer> ALAwayScore;


    //dados de inserção para tabela de estatísticas
    private Spinner SPplayers;
    private Spinner SPplayers2;
    private Spinner SPplayers3;
    private Spinner SPplayers4;
    private Spinner SPplayers5;
    private Spinner SPplayers6;


    private ArrayAdapter<String> AAPlayers;
    private ArrayAdapter<String> AAPlayers2;
    private ArrayAdapter<String> AAPlayers3;
    private ArrayAdapter<String> AAPlayers4;
    private ArrayAdapter<String> AAPlayers5;
    private ArrayAdapter<String> AAPlayers6;


    private ArrayList<String> ALPlayers;
    private ArrayList<String> ALPlayers2;
    private ArrayList<String> ALPlayers3;
    private ArrayList<String> ALPlayers4;
    private ArrayList<String> ALPlayers5;
    private ArrayList<String> ALPlayers6;

    private EditText gols1;
    private EditText gols2;
    private EditText gols3;
    private EditText gols4;
    private EditText gols5;
    private EditText gols6;

    private EditText ass1;
    private EditText ass2;
    private EditText ass3;
    private EditText ass4;
    private EditText ass5;
    private EditText ass6;

    private EditText ca1;
    private EditText ca2;
    private EditText ca3;
    private EditText ca4;
    private EditText ca5;
    private EditText ca6;

    private EditText cv1;
    private EditText cv2;
    private EditText cv3;
    private EditText cv4;
    private EditText cv5;
    private EditText cv6;

    private ImageView botaoVoltar;

    private DatabaseReference firebase;
    private String idEquipe;
    private String idUsuarioLogado;
    private String idPartida;
    private String idDadosPartida;

    //Botões para cadastrar os dados
    private Button registerMatch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marcar_partida);

        //apontando para os objetos na tela
        //partida
        ETdate = (EditText) findViewById(R.id.dataId);
        ETmyTeam = (EditText) findViewById(R.id.edMeuTimeId);
        ETopponent = (EditText) findViewById(R.id.edAdversarioId);

        Rg = (RadioGroup) findViewById(R.id.RGQuadroId);

        SPhome = (Spinner) findViewById(R.id.spinnerGolsDaCasaId);
        SPaway = (Spinner) findViewById(R.id.spinnerGolsAdversarioId);

        botaoVoltar = (ImageView) findViewById(R.id.VoltarMarcarPartidaId);


        //goals
        gols1 = (EditText) findViewById(R.id.Gol1Id);
        gols2 = (EditText) findViewById(R.id.Gol2Id);
        gols3 = (EditText) findViewById(R.id.Gol3Id);
        gols4 = (EditText) findViewById(R.id.Gol4Id);
        gols5 = (EditText) findViewById(R.id.Gol5Id);
        gols6 = (EditText) findViewById(R.id.Gol6Id);

        SimpleMaskFormatter formatoGols1 = new SimpleMaskFormatter("NN");
        SimpleMaskFormatter formatoGols2 = new SimpleMaskFormatter("NN");
        SimpleMaskFormatter formatoGols3 = new SimpleMaskFormatter("NN");
        SimpleMaskFormatter formatoGols4 = new SimpleMaskFormatter("NN");
        SimpleMaskFormatter formatoGols5 = new SimpleMaskFormatter("NN");
        SimpleMaskFormatter formatoGols6 = new SimpleMaskFormatter("NN");

        MaskTextWatcher mascaraGols1 = new MaskTextWatcher(gols1, formatoGols1);
        MaskTextWatcher mascaraGols2 = new MaskTextWatcher(gols2, formatoGols2);
        MaskTextWatcher mascaraGols3 = new MaskTextWatcher(gols3, formatoGols3);
        MaskTextWatcher mascaraGols4 = new MaskTextWatcher(gols4, formatoGols4);
        MaskTextWatcher mascaraGols5 = new MaskTextWatcher(gols5, formatoGols5);
        MaskTextWatcher mascaraGols6 = new MaskTextWatcher(gols6, formatoGols6);

        gols1.addTextChangedListener(mascaraGols1);
        gols2.addTextChangedListener(mascaraGols2);
        gols3.addTextChangedListener(mascaraGols3);
        gols4.addTextChangedListener(mascaraGols4);
        gols5.addTextChangedListener(mascaraGols5);
        gols6.addTextChangedListener(mascaraGols6);

        //ass
        ass1 = (EditText) findViewById(R.id.Ass1Id);
        ass2 = (EditText) findViewById(R.id.Ass2Id);
        ass3 = (EditText) findViewById(R.id.Ass3Id);
        ass4 = (EditText) findViewById(R.id.Ass4Id);
        ass5 = (EditText) findViewById(R.id.Ass5Id);
        ass6 = (EditText) findViewById(R.id.Ass6Id);

        SimpleMaskFormatter formatoAss1 = new SimpleMaskFormatter("NN");
        SimpleMaskFormatter formatoAss2 = new SimpleMaskFormatter("NN");
        SimpleMaskFormatter formatoAss3 = new SimpleMaskFormatter("NN");
        SimpleMaskFormatter formatoAss4 = new SimpleMaskFormatter("NN");
        SimpleMaskFormatter formatoAss5 = new SimpleMaskFormatter("NN");
        SimpleMaskFormatter formatoAss6 = new SimpleMaskFormatter("NN");

        MaskTextWatcher mascaraAss1 = new MaskTextWatcher(ass1, formatoAss1);
        MaskTextWatcher mascaraAss2 = new MaskTextWatcher(ass2, formatoAss2);
        MaskTextWatcher mascaraAss3 = new MaskTextWatcher(ass3, formatoAss3);
        MaskTextWatcher mascaraAss4 = new MaskTextWatcher(ass4, formatoAss4);
        MaskTextWatcher mascaraAss5 = new MaskTextWatcher(ass5, formatoAss5);
        MaskTextWatcher mascaraAss6 = new MaskTextWatcher(ass6, formatoAss6);

        ass1.addTextChangedListener(mascaraAss1);
        ass2.addTextChangedListener(mascaraAss2);
        ass3.addTextChangedListener(mascaraAss3);
        ass4.addTextChangedListener(mascaraAss4);
        ass5.addTextChangedListener(mascaraAss5);
        ass6.addTextChangedListener(mascaraAss6);

        //Cartão Amarelo
        ca1 = (EditText) findViewById(R.id.CA1Id);
        ca2 = (EditText) findViewById(R.id.CA2Id);
        ca3 = (EditText) findViewById(R.id.CA3Id);
        ca4 = (EditText) findViewById(R.id.CA4Id);
        ca5 = (EditText) findViewById(R.id.CA5Id);
        ca6 = (EditText) findViewById(R.id.CA6Id);

        SimpleMaskFormatter formatoCa1 = new SimpleMaskFormatter("NN");
        SimpleMaskFormatter formatoCa2 = new SimpleMaskFormatter("NN");
        SimpleMaskFormatter formatoCa3 = new SimpleMaskFormatter("NN");
        SimpleMaskFormatter formatoCa4 = new SimpleMaskFormatter("NN");
        SimpleMaskFormatter formatoCa5 = new SimpleMaskFormatter("NN");
        SimpleMaskFormatter formatoCa6 = new SimpleMaskFormatter("NN");

        MaskTextWatcher mascaraCa1 = new MaskTextWatcher(ca1, formatoCa1);
        MaskTextWatcher mascaraCa2 = new MaskTextWatcher(ca2, formatoCa2);
        MaskTextWatcher mascaraCa3 = new MaskTextWatcher(ca3, formatoCa3);
        MaskTextWatcher mascaraCa4 = new MaskTextWatcher(ca4, formatoCa4);
        MaskTextWatcher mascaraCa5 = new MaskTextWatcher(ca5, formatoCa5);
        MaskTextWatcher mascaraCa6 = new MaskTextWatcher(ca6, formatoCa6);

        ca1.addTextChangedListener(mascaraCa1);
        ca2.addTextChangedListener(mascaraCa2);
        ca3.addTextChangedListener(mascaraCa3);
        ca4.addTextChangedListener(mascaraCa4);
        ca5.addTextChangedListener(mascaraCa5);
        ca6.addTextChangedListener(mascaraCa6);

        //Cartão Vermelho
        cv1 = (EditText) findViewById(R.id.CV1Id);
        cv2 = (EditText) findViewById(R.id.CV2Id);
        cv3 = (EditText) findViewById(R.id.CV3Id);
        cv4 = (EditText) findViewById(R.id.CV4Id);
        cv5 = (EditText) findViewById(R.id.CV5Id);
        cv6 = (EditText) findViewById(R.id.CV6Id);

        SimpleMaskFormatter formatocv1 = new SimpleMaskFormatter("NN");
        SimpleMaskFormatter formatocv2 = new SimpleMaskFormatter("NN");
        SimpleMaskFormatter formatocv3 = new SimpleMaskFormatter("NN");
        SimpleMaskFormatter formatocv4 = new SimpleMaskFormatter("NN");
        SimpleMaskFormatter formatocv5 = new SimpleMaskFormatter("NN");
        SimpleMaskFormatter formatocv6 = new SimpleMaskFormatter("NN");

        MaskTextWatcher mascaracv1 = new MaskTextWatcher(cv1, formatocv1);
        MaskTextWatcher mascaracv2 = new MaskTextWatcher(cv2, formatocv2);
        MaskTextWatcher mascaracv3 = new MaskTextWatcher(cv3, formatocv3);
        MaskTextWatcher mascaracv4 = new MaskTextWatcher(cv4, formatocv4);
        MaskTextWatcher mascaracv5 = new MaskTextWatcher(cv5, formatocv5);
        MaskTextWatcher mascaracv6 = new MaskTextWatcher(cv6, formatocv6);

        //cartão vermelho
        cv1.addTextChangedListener(mascaracv1);
        cv2.addTextChangedListener(mascaracv2);
        cv3.addTextChangedListener(mascaracv3);
        cv4.addTextChangedListener(mascaracv4);
        cv5.addTextChangedListener(mascaracv5);
        cv6.addTextChangedListener(mascaracv6);

        //instanciando ArrayList
        ALHomeScore = new ArrayList<Integer>();
        //preenchendo ArrayList
        for (int i = 0; i <= 20; i++) {
            ALHomeScore.add(i);
        }
        //instanciando ArrayAdapter e colocando o ArrayList como fonte de dados
        AAHome = new ArrayAdapter<Integer>(
                getApplicationContext(),
                R.layout.lista_personalizada,
                ALHomeScore
        );
        //atribuindo o adaptador ao spinner
        SPhome.setAdapter(AAHome);

        ALAwayScore = new ArrayList<Integer>();
        for(int i = 0; i <=20; i++){
            ALAwayScore.add(i);
        }
        AAAway = new ArrayAdapter<Integer>(
                getApplicationContext(),
                R.layout.lista_personalizada,
                ALAwayScore
        );
        SPaway.setAdapter(AAAway);

        Preferencias preferencias = new Preferencias(Marcar_partida.this);
        idEquipe = preferencias.getIdentificadorEquipe();
        idUsuarioLogado = preferencias.getIdentificadorUsuario();

        //acessando o nó da equipe do usuário logado
        firebase = ConfiguracaoFirebase.getFirebase().child("equipes").child(idUsuarioLogado).child(idEquipe);

        //consultando dados da equipe apenas uma vez
        firebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Equipe equipe = snapshot.getValue(Equipe.class);
                ETmyTeam.setText(equipe.getNome());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        registerMatch = (Button) findViewById(R.id.bCadastrarDadosId);
        //COLOCAR EVENTO DE CLIQUE NO BOTAO
        registerMatch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                try{
                    // se a data | o oponente | o quadro não estiver preenchido
                    if(ETdate.getText().toString().equals("") || ETopponent.getText().toString().equals("") || Rg.getCheckedRadioButtonId() == 0 ){
                        Toast.makeText(getApplicationContext(),"data ou quadro ou adversário não preenchido, favor preencher",Toast.LENGTH_LONG).show();
                    }else{
                        captureDataMatch();
                        captureDataMatchsStatistics();
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });


        //dados para recuperação para estatística
        SPplayers = (Spinner) findViewById(R.id.spinnerJogadoresId);
        SPplayers2 = (Spinner) findViewById(R.id.spinnerJogadores2Id);
        SPplayers3 = (Spinner) findViewById(R.id.spinnerJogadores3Id);
        SPplayers4 = (Spinner) findViewById(R.id.spinnerJogadores4Id);
        SPplayers5 = (Spinner) findViewById(R.id.spinnerJogadores5Id);
        SPplayers6 = (Spinner) findViewById(R.id.spinnerJogadores6Id);


        ALPlayers = new ArrayList<String>();
        ALPlayers2 = new ArrayList<String>();
        ALPlayers3 = new ArrayList<String>();
        ALPlayers4 = new ArrayList<String>();
        ALPlayers5 = new ArrayList<String>();
        ALPlayers6 = new ArrayList<String>();

        //acessando nós de jogadores da equipe do usuário logado
        firebase = ConfiguracaoFirebase.getFirebase().child("jogadores").child(idEquipe).child(idUsuarioLogado);

        //consultando dados dos jogadores
        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //varrendo os nomes do jogadores
                for(DataSnapshot dados: snapshot.getChildren()){
                    Jogador jogador = dados.getValue(Jogador.class);
                    ALPlayers.add(jogador.getNome());
                    ALPlayers2.add(jogador.getNome());
                    ALPlayers3.add(jogador.getNome());
                    ALPlayers4.add(jogador.getNome());
                    ALPlayers5.add(jogador.getNome());
                    ALPlayers6.add(jogador.getNome());
                }
                AAPlayers.notifyDataSetChanged();
                AAPlayers2.notifyDataSetChanged();
                AAPlayers3.notifyDataSetChanged();
                AAPlayers4.notifyDataSetChanged();
                AAPlayers5.notifyDataSetChanged();
                AAPlayers6.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        AAPlayers = new ArrayAdapter<String>(
                getApplicationContext(),
                R.layout.lista_personalizada,
                ALPlayers
        );
        SPplayers.setAdapter(AAPlayers);

        AAPlayers2 = new ArrayAdapter<String>(
                getApplicationContext(),
                R.layout.lista_personalizada,
                ALPlayers2
        );
        SPplayers2.setAdapter(AAPlayers2);

        AAPlayers3 = new ArrayAdapter<String>(
                getApplicationContext(),
                R.layout.lista_personalizada,
                ALPlayers3
        );
        SPplayers3.setAdapter(AAPlayers3);

        AAPlayers4 = new ArrayAdapter<String>(
                getApplicationContext(),
                R.layout.lista_personalizada,
                ALPlayers4
        );
        SPplayers4.setAdapter(AAPlayers4);

        AAPlayers5 = new ArrayAdapter<String>(
                getApplicationContext(),
                R.layout.lista_personalizada,
                ALPlayers5
        );
        SPplayers5.setAdapter(AAPlayers5);

        AAPlayers6 = new ArrayAdapter<String>(
                getApplicationContext(),
                R.layout.lista_personalizada,
                ALPlayers6
        );
        SPplayers6.setAdapter(AAPlayers6);

        //configurando Mask do campo de Data

        SimpleMaskFormatter formatoData = new SimpleMaskFormatter("NN/NN/NNNN");
        MaskTextWatcher mascaraData = new MaskTextWatcher(ETdate,formatoData);
        ETdate.addTextChangedListener(mascaraData);

    }

    public void voltar3(View view){
        Intent intent = new Intent(Marcar_partida.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void insertMatch( String data, String minhaEquipe, String oponente, int placarCasa, int placarOponente, int quadro){

        Preferencias preferencias = new Preferencias(Marcar_partida.this);
        idEquipe = preferencias.getIdentificadorEquipe();
        idUsuarioLogado = preferencias.getIdentificadorUsuario();

        //String quadro2 = "|"+String.valueOf(quadro)+"|";

        idPartida = Base64Custom.codificarParaBase64(data)+""+Base64Custom.codificarParaBase64(String.valueOf(quadro));

        firebase = ConfiguracaoFirebase.getFirebase();

        firebase = firebase.child("partidas")
                            .child(idEquipe)
                             .child(idUsuarioLogado)
                              .child(idPartida);

        Partida partida = new Partida();
        partida.setIdentificadorPartida(idPartida);
        partida.setData(data);
        partida.setEquipe(minhaEquipe);
        partida.setOponente(oponente);
        partida.setPlacarDaCasa(placarCasa);
        partida.setPlacarOponente(placarOponente);
        partida.setQuadro(quadro);

        firebase.setValue(partida);

        Toast.makeText(getApplicationContext(),"Dados salvos com sucesso", Toast.LENGTH_LONG).show();


    }
    private void captureDataMatch(){

        String data = ETdate.getText().toString();
        String minhaEquipe = ETmyTeam.getText().toString();
        String oponente = ETopponent.getText().toString();
        int placarCasa = Integer.parseInt(SPhome.getSelectedItem().toString());
        int placarOponente = Integer.parseInt(SPaway.getSelectedItem().toString());
        int quadro = 0;
        //capturando endereço de memória do radiobutton selecionado
        int idRadioButtonSelecionado = Rg.getCheckedRadioButtonId();
        if(idRadioButtonSelecionado > 0){
            RbChosen = (RadioButton) findViewById(idRadioButtonSelecionado);
            quadro = Integer.parseInt(RbChosen.getText().toString());
        }

        if(quadro > 0 && !oponente.equals("")){
            insertMatch(data,minhaEquipe,oponente,placarCasa,placarOponente,quadro);
        }

        if(quadro == 0){
            Toast.makeText(Marcar_partida.this, "quadro está incompleto, favor preencher", Toast.LENGTH_SHORT).show();
        }

        if(oponente.equals("")){
            Toast.makeText(Marcar_partida.this, "oponente está incompleto, favor preencher", Toast.LENGTH_SHORT).show();
        }
    }

    private void captureDataMatchsStatistics(){
        int line;
        try{
            String date = ETdate.getText().toString();
            String adversario = ETopponent.getText().toString();
            int quadro = 0;
            int radioButtonId = Rg.getCheckedRadioButtonId();

            if(radioButtonId > 0){
                RbChosen = (RadioButton) findViewById(radioButtonId);
                quadro = Integer.parseInt(RbChosen.getText().toString());
            }

            //dados das linhas
            //1
            String jogador1 = SPplayers.getSelectedItem().toString();
            int goals1 = Integer.parseInt(gols1.getText().toString());
            int assistance1 = Integer.parseInt(ass1.getText().toString());
            int redCard1 = Integer.parseInt(cv1.getText().toString());
            int yellowCard1 = Integer.parseInt(ca1.getText().toString());

            if (goals1 > 0 || assistance1 > 0 || redCard1 > 0 || yellowCard1 > 0) {
                line = 1;
                insertMatchsStatistics(jogador1, goals1, assistance1, redCard1, yellowCard1, date, adversario, quadro, line);
                UpdateStatistic(jogador1, goals1, assistance1, yellowCard1, redCard1,line);
            }

            //2
            String jogador2 = SPplayers2.getSelectedItem().toString();
            int goals2 = Integer.parseInt(gols2.getText().toString());
            int assistance2 = Integer.parseInt(ass2.getText().toString());
            int redCard2 = Integer.parseInt(cv2.getText().toString());
            int yellowCard2 = Integer.parseInt(ca2.getText().toString());

            if (goals2 > 0 || assistance2 > 0 || redCard2 > 0 || yellowCard2 > 0) {
                line = 2;
                insertMatchsStatistics(jogador2, goals2, assistance2, redCard2, yellowCard2, date, adversario, quadro, line);
                UpdateStatistic(jogador2, goals2, assistance2, yellowCard2, redCard2,line);
            }

            //3
            String jogador3 = SPplayers3.getSelectedItem().toString();
            int goals3 = Integer.parseInt(gols3.getText().toString());
            int assistance3 = Integer.parseInt(ass3.getText().toString());
            int redCard3 = Integer.parseInt(cv3.getText().toString());
            int yellowCard3 = Integer.parseInt(ca3.getText().toString());

            if (goals3 > 0 || assistance3 > 0 || redCard3 > 0 || yellowCard3 > 0) {
                line = 3;
                insertMatchsStatistics(jogador3, goals3, assistance3, redCard3, yellowCard3, date, adversario, quadro, line);
                UpdateStatistic(jogador3, goals3, assistance3, yellowCard3, redCard3,line);

            }

            //4
            String jogador4 = SPplayers4.getSelectedItem().toString();
            int goals4 = Integer.parseInt(gols4.getText().toString());
            int assistance4 = Integer.parseInt(ass4.getText().toString());
            int redCard4 = Integer.parseInt(cv4.getText().toString());
            int yellowCard4 = Integer.parseInt(ca4.getText().toString());

            if (goals4 > 0 || assistance4 > 0 || redCard4 > 0 || yellowCard4 > 0) {
                line = 4;
                insertMatchsStatistics(jogador4, goals4, assistance4, redCard4, yellowCard4, date, adversario, quadro, line);
                UpdateStatistic(jogador4, goals4, assistance4, yellowCard4, redCard4,line);
            }

            //5
            String jogador5 = SPplayers5.getSelectedItem().toString();
            int goals5 = Integer.parseInt(gols5.getText().toString());
            int assistance5 = Integer.parseInt(ass5.getText().toString());
            int redCard5 = Integer.parseInt(cv5.getText().toString());
            int yellowCard5 = Integer.parseInt(ca5.getText().toString());

            if (goals5 > 0 || assistance5 > 0 || redCard5 > 0 || yellowCard5 > 0) {
                line = 5;
                insertMatchsStatistics(jogador5, goals5, assistance5, redCard5, yellowCard5, date, adversario, quadro, line);
                UpdateStatistic(jogador5, goals5, assistance5, yellowCard5, redCard5,line);
            }

            //6
            String jogador6 = SPplayers6.getSelectedItem().toString();
            int goals6 = Integer.parseInt(gols6.getText().toString());
            int assistance6 = Integer.parseInt(ass6.getText().toString());
            int redCard6 = Integer.parseInt(cv6.getText().toString());
            int yellowCard6 = Integer.parseInt(ca6.getText().toString());

            if(goals6 > 0 || assistance6 > 0 || redCard6 > 0 || yellowCard6 > 0){
                line = 6;
                insertMatchsStatistics(jogador6, goals6,assistance6,redCard6,yellowCard6,date,adversario,quadro,line);
                UpdateStatistic(jogador6,goals6,assistance6,yellowCard6,redCard6,line);
            }


        }catch(Exception e){
            e.printStackTrace();
        }

    }

    private void insertMatchsStatistics(String jogador, int goalsX, int assistanceX, int redCardX, int yellowCardX, String date, String opponentx, int squareX, int line ){

        firebase = ConfiguracaoFirebase.getFirebase();

        idDadosPartida = idPartida+Base64Custom.codificarParaBase64(jogador);

        firebase = firebase.child("dadosPartidas")
                            .child(idEquipe)
                             .child(idUsuarioLogado)
                              .child(idDadosPartida);

        DadosPartida dadosPartida = new DadosPartida();
        dadosPartida.setIdentificadorDadosPartida(idDadosPartida);
        dadosPartida.setJogador(jogador);
        dadosPartida.setGols(goalsX);
        dadosPartida.setAssistencia(assistanceX);
        dadosPartida.setCartaoVermelho(redCardX);
        dadosPartida.setCartaoAmarelo(yellowCardX);
        dadosPartida.setData(date);
        dadosPartida.setOponente(opponentx);
        dadosPartida.setQuadro(squareX);

        firebase.setValue(dadosPartida);

        ETdate.setText("");
        ETopponent.setText("");
        ALAwayScore.clear();
        ALHomeScore.clear();

        for (int z = 0; z <= 20; z++){
            ALHomeScore.add(z);
        }
        //instanciando ArrayAdapter e colocando o ArrayList como fonte de dados
        AAHome = new ArrayAdapter<Integer>(
                getApplicationContext(),
                R.layout.lista_personalizada,
                ALHomeScore
        );
        //atribuindo o adaptador ao spinner
        SPhome.setAdapter(AAHome);

        for (int x = 0; x <= 20; x++){
            ALAwayScore.add(x);
        }

        AAAway = new ArrayAdapter<Integer>(
                getApplicationContext(),
                R.layout.lista_personalizada,
                ALAwayScore
        );

        SPaway.setAdapter(AAAway);

        int idRadioButtonSelecionado = Rg.getCheckedRadioButtonId();
        if(idRadioButtonSelecionado > 0){
            RbChosen = (RadioButton) findViewById(idRadioButtonSelecionado);
            RbChosen.setChecked(false);
        }

    }

    private void UpdateStatistic(final String player, final int goals, final int ass, final int yellowCard, final int redCard, final int line){
        // spPlayer.getSelectedItemPosition() == position do listView (alIdJogador.get(position)

        //Toast.makeText(getApplicationContext(),idUsuarioLogado,Toast.LENGTH_LONG).show();

        firebase = ConfiguracaoFirebase.getFirebase().child("estatisticas").child(idEquipe).child(idUsuarioLogado);

        firebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dados: snapshot.getChildren()){

                    Estatistica estatistica = dados.getValue(Estatistica.class);
                    if(player.equals(estatistica.getJogador())){
                        String playerF = estatistica.getJogador();
                        int goalsF = estatistica.getGols();
                        int assF = estatistica.getAssistencia();
                        int yellowCardF = estatistica.getCartaoAmarelo();
                        int redCardF = estatistica.getCartaoVermelho();

                        int totalGoals = goals+goalsF;
                        int totalAss = ass+assF;
                        int totalYellowCard = yellowCard+yellowCardF;
                        int totalRedCard = redCard+redCardF;

                        //Toast.makeText(getApplicationContext(),"Jogador:"+playerF+"\ntotalGoals: "+totalGoals+"\n totalAss:"+totalAss+"\n totalYellowCard: "+totalYellowCard+"\n totalRedCard:"+totalRedCard, Toast.LENGTH_LONG).show();

                        String idEstatistica = estatistica.getIdentificadorEstatistica();

                        firebase = ConfiguracaoFirebase.getFirebase();

                        firebase = firebase.child("estatisticas")
                                            .child(idEquipe)
                                             .child(idUsuarioLogado)
                                              .child(idEstatistica);

                        Estatistica estatistica1 = new Estatistica();
                        estatistica1.setIdentificadorEstatistica(idEstatistica);
                        estatistica1.setJogador(playerF);
                        estatistica1.setGols(totalGoals);
                        estatistica1.setAssistencia(totalAss);
                        estatistica1.setCartaoAmarelo(totalYellowCard);
                        estatistica1.setCartaoVermelho(totalRedCard);

                        firebase.setValue(estatistica1);

                        switch(line){
                            case 1:
                                gols1.setText("0");
                                ass1.setText("0");
                                cv1.setText("0");
                                ca1.setText("0");
                                break;

                            case 2:
                                gols2.setText("0");
                                ass2.setText("0");
                                cv2.setText("0");
                                ca2.setText("0");
                                break;

                            case 3:
                                gols3.setText("0");
                                ass3.setText("0");
                                cv3.setText("0");
                                ca3.setText("0");
                                break;

                            case 4:
                                gols4.setText("0");
                                ass4.setText("0");
                                cv4.setText("0");
                                ca4.setText("0");
                                break;

                            case 5:
                                gols5.setText("0");
                                ass5.setText("0");
                                cv5.setText("0");
                                ca5.setText("0");
                                break;

                            case 6:
                                gols6.setText("0");
                                ass6.setText("0");
                                cv6.setText("0");
                                ca6.setText("0");
                                break;

                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        //quando terminar a activity jogos e clique da listview, ir em manter_jogadores -> atualizarDadosPartida()
    }
}
