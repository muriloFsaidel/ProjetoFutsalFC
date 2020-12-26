package cursoandroid.whatsappandroid.com.futsalfc.Adapter;

import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import cursoandroid.whatsappandroid.com.futsalfc.R;
import cursoandroid.whatsappandroid.com.futsalfc.template.Usuario;

public class PerfilAdminAdaptador extends ArrayAdapter<Usuario> {

    private ArrayList<Usuario> usuarioAdmin;
    private Context context;

    public PerfilAdminAdaptador(@NonNull Context c, @NonNull ArrayList<Usuario> objects){
            super(c,0, objects);
            this.context = c;
            this.usuarioAdmin = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){

        View view = null;

        if(usuarioAdmin != null){

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.lista_perfil_admin, parent, false);

            TextView nomeAdmin = (TextView) view.findViewById(R.id.tv_nome_perfil_admin);
            TextView emailAdmin = (TextView) view.findViewById(R.id.tv_email_perfil_admin);

            Usuario usuario = usuarioAdmin.get(position);
            nomeAdmin.setText("Nome administrador: \n"+usuario.getNome()+"\n");
            emailAdmin.setText("Email administrador: \n"+usuario.getEmail()+"\n");
        }

        return view;
    }
}
