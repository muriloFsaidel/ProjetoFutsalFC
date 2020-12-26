package cursoandroid.whatsappandroid.com.futsalfc.helper;

import android.content.Context;
import android.content.SharedPreferences;
//chamar salvarDadosUsuário no CadastroUsuario&LoginActivity
//chamar salvarDadosEquipe no CadastroEquipe

//classe para criação de arquivos de preferências
public class Preferencias {

    //atributo para criação do arquivo
    private Context context;
    //representa o arquivo criado
    private SharedPreferences preferences;
    //editor do arquivo criado
    private SharedPreferences.Editor editor;
    private final String NOME_ARQUIVO = "usuariopreferencias";
    //modo de leitura local
    private final int MODE = 0;
    private final String CHAVE_USUARIO = "identificadorUsuario";
    private final String CHAVE_EQUIPE = "identificadorEquipe";
    private final String CHAVE_JOGADOR = "identificadorJogador";
    private final String CHAVE_NOME_JOGADOR_USUARIO = "nomeJogador";
    private final String CHAVE_EMAIL_RESPONSAVEL_JOGADOR_USUARIO = "emailResponsavel";
    private final String CHAVE_JOGADOR_USUARIO = "idResponsavel";

    // Passando Activity como parâmetro
    public Preferencias(Context contexto){
        context = contexto;
        preferences = context.getSharedPreferences(NOME_ARQUIVO,MODE);
        //recebendo permissão para edição
        editor = preferences.edit();
    }

    public void salvarDadosUsuario(String identificadorUsuario){
        //coloca texto com a chave de acesso e com o valor do parâmetro
        editor.putString(CHAVE_USUARIO, identificadorUsuario);
        editor.commit();
    }

    public void salvarDadosEquipe(String identificadorEquipe){
        editor.putString(CHAVE_EQUIPE, identificadorEquipe);
        //salva o arquivo
        editor.commit();
    }

    public void salvarDadosJogador(String identificadorJogador){
        editor.putString(CHAVE_JOGADOR, identificadorJogador);
        editor.commit();
    }

    public void salvarDadosNomeJogador(String nomeJogador){
        editor.putString(CHAVE_NOME_JOGADOR_USUARIO, nomeJogador);
        editor.commit();
    }

    public void salvarDadosJogadorUsuario(String idResponsavel){
        editor.putString(CHAVE_JOGADOR_USUARIO, idResponsavel);
        editor.commit();
    }

    public void salvarDadosEmailResponsavelJogadorUsuario(String emailResponsalvel){
        editor.putString(CHAVE_EMAIL_RESPONSAVEL_JOGADOR_USUARIO, emailResponsalvel);
        editor.commit();
    }

    public String getIdentificadorUsuario(){
        // retornando texto do arquivo com a chave de acesso
        return preferences.getString(CHAVE_USUARIO, null);
    }

    public String getIdentificadorEquipe(){
        return preferences.getString(CHAVE_EQUIPE, CHAVE_EQUIPE);
    }

    public String getIdentificadorJogador(){
        return preferences.getString(CHAVE_JOGADOR, null);
    }

    public String getNomeJogador(){
        return preferences.getString(CHAVE_NOME_JOGADOR_USUARIO, null);
    }

    public String getJogadorUsuario(){
        return preferences.getString(CHAVE_JOGADOR_USUARIO, null);
    }

    public String getEmailResponsavelJogadorUsuario(){
        return preferences.getString(CHAVE_EMAIL_RESPONSAVEL_JOGADOR_USUARIO,CHAVE_EMAIL_RESPONSAVEL_JOGADOR_USUARIO);
    }








}
