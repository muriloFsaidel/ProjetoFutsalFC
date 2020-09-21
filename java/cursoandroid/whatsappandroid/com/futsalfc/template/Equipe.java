package cursoandroid.whatsappandroid.com.futsalfc.template;

//classe entidade
public class Equipe {

    private String identificadorEquipe;
    private String nome;
    private String liga;

    public Equipe(){

    }

    public void setIdentificadorEquipe(String identificadorEquipe){
        this.identificadorEquipe = identificadorEquipe;
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    public void setLiga(String liga){
        this.liga = liga;
    }

    public String getIdentificadorEquipe(){
        return identificadorEquipe;
    }

    public String getNome(){
        return nome;
    }

    public String getLiga(){
        return liga;
    }


}
