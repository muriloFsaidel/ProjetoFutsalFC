package cursoandroid.whatsappandroid.com.futsalfc.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public final class ConfiguracaoFirebase {

    private static DatabaseReference referenciaFirebase;
    private static FirebaseAuth  autenticacao;

    public static DatabaseReference getFirebase(){
        if(referenciaFirebase == null){
            //conexão com o banco de dados pela raiz
            referenciaFirebase = FirebaseDatabase.getInstance().getReference();
        }
        return referenciaFirebase;
    }

    public static FirebaseAuth getAutenticacao(){
        if(autenticacao ==  null){
            autenticacao = FirebaseAuth.getInstance();
        }
        return autenticacao;
    }



}
