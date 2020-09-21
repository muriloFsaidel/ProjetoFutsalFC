package cursoandroid.whatsappandroid.com.futsalfc.template;

//classe entidade
public class Partida {

    //campos
    private String identificadorPartida;
    private String data;
    private String equipe;
    private String oponente;
    private int placarDaCasa;
    private int placarOponente;
    private int quadro;

    public Partida(){

    }

    public String getIdentificadorPartida() {
        return identificadorPartida;
    }

    public void setIdentificadorPartida(String identificadorPartida) {
        this.identificadorPartida = identificadorPartida;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getEquipe() {
        return equipe;
    }

    public void setEquipe(String equipe) {
        this.equipe = equipe;
    }

    public String getOponente() {
        return oponente;
    }

    public void setOponente(String oponente) {
        this.oponente = oponente;
    }

    public int getPlacarDaCasa() {
        return placarDaCasa;
    }

    public void setPlacarDaCasa(int placarDaCasa) {
        this.placarDaCasa = placarDaCasa;
    }

    public int getPlacarOponente() {
        return placarOponente;
    }

    public void setPlacarOponente(int placarOponente) {
        this.placarOponente = placarOponente;
    }

    public int getQuadro() {
        return quadro;
    }

    public void setQuadro(int quadro) {
        this.quadro = quadro;
    }
}
