package cursoandroid.whatsappandroid.com.futsalfc.template;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import cursoandroid.whatsappandroid.com.futsalfc.config.ConfiguracaoFirebase;
import cursoandroid.whatsappandroid.com.futsalfc.helper.Preferencias;

//classe entidade
public class JogadorUsuario {

    private String idJogadorUsuario;
    private String nome;
    private String emailResponsavel;
    private String emailJogador;
    private String senha;


    public JogadorUsuario(){

    }

    public void salvar(){
        DatabaseReference firebase = ConfiguracaoFirebase.getFirebase();
        firebase.child("jogadoresUsuarios").child( getIdJogadorUsuario()).setValue(this);
    }

    public void setIdJogadorUsuario(String idJogadorUsuario){
        this.idJogadorUsuario = idJogadorUsuario;
    }

    public String getIdJogadorUsuario(){
        return  idJogadorUsuario;
    }

    public void setNome(String nome){
        this.nome =  nome;
    }

    public String getNome(){
        return nome;
    }

    public void setEmailResponsavel(String emailResponsavel){
        this.emailResponsavel = emailResponsavel;
    }

    public String getEmailResponsavel(){
        return emailResponsavel;
    }

    public void setEmailJogador(String emailJogador){
        this.emailJogador = emailJogador;
    }

    public String getEmailJogador(){
        return emailJogador;
    }

    public void setSenha(String senha){
        this.senha = senha;
    }

    @Exclude
    public String getSenha(){
        return senha;
    }





}
