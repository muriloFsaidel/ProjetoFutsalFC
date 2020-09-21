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

    public String getIdentificadorUsuario(){
        // retornando texto do arquivo com a chave de acesso
        return preferences.getString(CHAVE_USUARIO, null);
    }

    public String getIdentificadorEquipe(){
        return preferences.getString(CHAVE_EQUIPE, null);
    }








}
