package cursoandroid.whatsappandroid.com.futsalfc.template;

//classe entidade
public class DadosPartida {

    //campos
    private String identificadorDadosPartida;
    private String jogador;
    private int gols;
    private int assistencia;
    private int cartaoVermelho;
    private int cartaoAmarelo;
    private String data;
    private String oponente;
    private int quadro;

    public DadosPartida(){

    }

    public String getIdentificadorDadosPartida() {
        return identificadorDadosPartida;
    }

    public void setIdentificadorDadosPartida(String identificadorDadosPartida) {
        this.identificadorDadosPartida = identificadorDadosPartida;
    }

    public String getJogador() {
        return jogador;
    }

    public void setJogador(String jogador) {
        this.jogador = jogador;
    }

    public int getGols() {
        return gols;
    }

    public void setGols(int gols) {
        this.gols = gols;
    }

    public int getAssistencia() {
        return assistencia;
    }

    public void setAssistencia(int assistencia) {
        this.assistencia = assistencia;
    }

    public int getCartaoVermelho() {
        return cartaoVermelho;
    }

    public void setCartaoVermelho(int cartaoVermelho) {
        this.cartaoVermelho = cartaoVermelho;
    }

    public int getCartaoAmarelo() {
        return cartaoAmarelo;
    }

    public void setCartaoAmarelo(int cartaoAmarelo) {
        this.cartaoAmarelo = cartaoAmarelo;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getOponente() {
        return oponente;
    }

    public void setOponente(String oponente) {
        this.oponente = oponente;
    }

    public int getQuadro() {
        return quadro;
    }

    public void setQuadro(int quadro) {
        this.quadro = quadro;
    }
}
