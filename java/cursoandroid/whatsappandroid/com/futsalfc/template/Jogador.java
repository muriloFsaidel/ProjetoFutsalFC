package cursoandroid.whatsappandroid.com.futsalfc.template;

//classe entidade
public class Jogador {

    //campos
    private String identificadorJogador;
    private String nome;
    private String ativo;
    private String posicao;

    public Jogador(){

    }

    public void setIdentificadorJogador(String identificadorJogador){
        this.identificadorJogador = identificadorJogador;
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    public void setAtivo(String ativo){
        this.ativo = ativo;
    }

    public void setPosicao(String posicao){
        this.posicao = posicao;
    }

    public String getIdentificadorJogador(){
        return identificadorJogador;
    }

    public String getNome() {
        return nome;
    }

    public String getAtivo(){
        return ativo;
    }

    public String getPosicao(){
        return posicao;
    }
}
