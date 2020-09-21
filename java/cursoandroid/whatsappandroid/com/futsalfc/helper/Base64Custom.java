package cursoandroid.whatsappandroid.com.futsalfc.helper;

import android.util.Base64;

public class Base64Custom {

    public static String codificarParaBase64(String texto){
        //converte email (texto) para 0&1 e depois para base64 padrão, sem quebra de linha ou espaço
        return Base64.encodeToString(texto.getBytes(), Base64.DEFAULT).replaceAll("(\\n|\\r)","");
    }

    public static String decodificarDaBase64(String textoCodificado){
        // converte email em base64 para String
        return new String(Base64.decode(textoCodificado,Base64.DEFAULT));
    }
}
