package cursoandroid.whatsappandroid.com.futsalfc.template;

//classe entidade Estatística geral
public class Estatistica {

    //campos
    private String identificadorEstatistica;
    private String jogador;
    private int gols;
    private int assistencia;
    private int cartaoAmarelo;
    private int cartaoVermelho;

    public Estatistica(){

    }

    public void setIdentificadorEstatistica(String identificadorEstatistica){
        this.identificadorEstatistica = identificadorEstatistica;
    }

    public void setJogador(String jogador){
        this.jogador = jogador;
    }

    public void setGols(int gols){
        this.gols = gols;
    }

    public void setAssistencia(int assistencia){
        this.assistencia = assistencia;
    }

    public void setCartaoAmarelo(int cartaoAmarelo){
        this.cartaoAmarelo = cartaoAmarelo;
    }

    public void setCartaoVermelho(int cartaoVermelho){
        this.cartaoVermelho = cartaoVermelho;
    }

    public String getIdentificadorEstatistica(){
        return identificadorEstatistica;
    }

    public String getJogador(){
        return jogador;
    }

    public int getGols(){
        return gols;
    }

    public int getAssistencia(){
        return assistencia;
    }

    public int getCartaoAmarelo(){
        return cartaoAmarelo;
    }

    public int getCartaoVermelho(){
        return cartaoVermelho;
    }
}
