package pt.ipc.estgoh.jogoGalo.jogo;

/**
 * A classe Jogadas representa a jogadas realizadas durante um jogo Tic-Tac-Toe.
 * Pelo que possui atributos que caracterizam as jogadas e métodos que permitem
 * aceder à informação guardada.
 *
 * @author José Mauricio
 * @version 0.3
 */
public class Jogadas {


    private int coordenadaX;
    private int coordenadaY;
    private String caractereJogada;
    private long momentoJogada;

    /**
     * Construtor que permite instaciar um novo objeto da classe Jogadas, inicializando os
     * valores dos atributos da classe.
     *
     * @param aCoordenadaX Recebe um inteiro com a coordenada x da jogada
     * @param aCoordenadaY  Recebe um inteiro com a coordenada y da jogada
     * @param aCaractereJogada Recebe uma string com o caractere da jogada
     * @param aMomentoJogada Recebe um long com o momento temporal que a jogada foi realizada
     */
    public Jogadas(int aCoordenadaX, int aCoordenadaY, String aCaractereJogada, long aMomentoJogada) {

        this.coordenadaX = aCoordenadaX;
        this.coordenadaY = aCoordenadaY;
        this.caractereJogada = aCaractereJogada;
        this.momentoJogada =  aMomentoJogada;
    }

    /**
     * Apresenta a coordenada horizontal do tabuleiro que se
     * encontra armazenada no atributo coordenadaX.
     *
     * @return  Retorna um inteiro com a coordenada x
     */
    public int getCoordenadaX() {
        return this.coordenadaX;
    }

    /**
     * Apresenta a coordenada vertical do tabuleiro que se
     * encontra armazenada no atributo coordenadaY.
     *
     * @return  Retorna um inteiro com a coordenada y
     */
    public int getCoordenadaY() {
        return this.coordenadaY;
    }

    /**
     * Apresenta o caractere atribuido à jogada que foi registada. Este
     * valor está guardado no atributo caractereJogada.
     *
     * @return Retorna uma string com o caractere atribuido à jogada
     */
    public String getCaractereJogada() {
        return this.caractereJogada;
    }

    /**
     * Apresenta o momento temporal em que a jogada foi realizada. Este valor
     * encontra-se registado no atributo momentoJogada.
     *
     * @return Retorna um long com o valor sobre o momento temporal da jogada.
     */
    public long getMomentoJogada() {
        return this.momentoJogada;
    }
}

