package pt.ipc.estgoh.jogoGalo.jogo;

import pt.ipc.estgoh.jogoGalo.jogadores.Computador;
import pt.ipc.estgoh.jogoGalo.jogadores.Jogador;

import java.util.ArrayList;

/**
 * A classe Jogo representa a estrutura que irá armzenar a informação sobre um
 * determinado jogo Tic-Tac-Toe. Dentro desta classe encontram-se os atributos
 * que caracterizam o jogo e os métodos que permitem manipular a informação armazenada.
 *
 * @author José Mauricio
 * @version 1.4
 */
public class Jogo {

    private int id;
    private long tempoDecorrido;
    private String estado;
    private Tabuleiro tabuleiro;
    private Jogador jogador;
    private Jogador adversario;
    private Computador bot;
    private ArrayList<Jogadas> jogadas;

    /**
     * Construtor por defeito para a classe GereNotificacoes
     */
    public Jogo () {}

    /**
     * Construtor que permite instaciar um novo objeto da classe Jogo aquando de um novo
     * jogo entre computador e jogador humano.
     *
     * @param aTabuleiro Recebe um objeto da classe Tabuleiro com o tabuleiro do jogo
     * @param aJogador Recebe um objeto da classe Jogador com o jogador humano
     * @param aBot Recebe um objeto da classe Computador com o jogador computador
     */
    public Jogo(Tabuleiro aTabuleiro, Jogador aJogador, Computador aBot) {
        this.jogador = aJogador;
        this.tabuleiro = aTabuleiro;
        this.bot = aBot;
        this.jogadas = new ArrayList<>();
    }

    /**
     * Construtor que permite inicializar os atributos da classe Jogo aquando de um novo jogo
     * entre dois jogadore humanos.
     *
     * @param aTabuleiro Recebe um objeto da classe Tabuleiro com o tabuleiro para o jogo
     * @param aJogador Recebe como parâmetro um objeto da classe Jogador
     * @param aJogador2 Recebe como parâmetro um objeto da classe Jogador
     */
    public Jogo(Tabuleiro aTabuleiro, Jogador aJogador, Jogador aJogador2) {
        this.jogador = aJogador;
        this.tabuleiro = aTabuleiro;
        this.adversario = aJogador2;
    }

    /**
     * Devolve o objeto que representa um jogador humano que está a jogar
     * contra o jogador humano local.
     *
     * @return Retorna um objeto da classe Jogador com a informação sobre o funcionário.
     */
    public Jogador getAdversario() {
        return this.adversario;
    }

    /**
     * Altera o valor do tempo de duração de um jogo do Tic-Tac-Toe. Ou seja, ao valor
     * do tempo acumulado no atributo tempoDecorrido é adicionado o valor recebido por parâmetro.
     *
     * @param aTempoDecorrido Recebe um long com o novo valor relativamente ao tempo de duração do jogo.
     */
    public void setTempoDecorrido(long aTempoDecorrido) {
        this.tempoDecorrido += aTempoDecorrido;
    }

    /**
     * Obtêm-se o tempo total de jogo após um determinado jogo ter terminado.
     *
     * @return Retorna um long com o tempo total do jogo
     */
    public long getTempoDecorrido() {
        return this.tempoDecorrido;
    }

    /**
     * Regista um novo tabuleiro para o objeto da classe Jogo que foi inicializado.
     *
     * @param aTabuleiro Recebe como parâmetro um objeto da classe Tabuleiro com o novo tabuleiro
     */
    public void setTabuleiro(Tabuleiro aTabuleiro) {
        this.tabuleiro = aTabuleiro;
    }

    /**
     * Obtém-se um objeto da classe Tabuleiro com a informação sobre o tabuleiro que foi
     * atribuido ao jogo em questão.
     *
     * @return Retorna um objeto da classe Tabuleiro com a informação sobre o tabuleiro de jogo.
     */
    public Tabuleiro getTabuleiro() {
        return this.tabuleiro;
    }

    /**
     * Regista um novo objeto da classe Jogador no atributo jogador da classe Jogo.
     *
     * @param aJogador Recebe como parâmetro um objeto da classe Jogador.
     */
    public void setJogador(Jogador aJogador) {
        this.jogador = aJogador;
    }

    /**
     * Regista um novo objeto da classe Computador no atributo bot da classe Jogo.
     *
     * @param aBot Recebe como parâmetro um objeto da classe Computador
     */
    public void setBot(Computador aBot) {
        this.bot = aBot;
    }

    /**
     * Apresenta a informação sobre o jogador humano local que está a jogar contra outro
     * jogador humano ou contra o computador.
     *
     * @return Retorna um objeto da classe Jogador com a informação do jogador
     */
    public Jogador getJogador() {
        return this.jogador;
    }

    /**
     * Devolve a informação sobre o Computador que está a jogar contra o jogador humano local.
     *
     * @return Retorna um objeto da classe Computador com a informação sobre o computador
     */
    public Computador getBot() {
        return this.bot;
    }

    /**
     * Regista um novo valor no atributo id da classe Jogo. Valor esse que foi
     * recebido como parâmetro.
     *
     * @param aId Retorna um inteiro com o id que identifica o jogo em causa.
     */
    public void setId(int aId) {
        this.id = aId;
    }

    /**
     * Obtém o id que identifica o jogo que se encontra a decorrer.
     *
     * @return Retorna um id com o id que identifica o jogo
     */
    public int getId() {
        return this.id;
    }

    /**
     * Regista no atributo estado o estado em que o jogo se encontra. Que poderá estar
     * no estado "Concluido" ou "Em Pausa".
     *
     * @param aEstado Recebe uma string com o estado a ser registado
     */
    public void setEstado(String aEstado) {
        this.estado = aEstado;
    }

    /**
     * Obtém o estado em que o jogo se encontra no preciso momento.
     *
     * @return Retorna uma string com o estado do jogo
     */
    public String getEstado() {
        return this.estado;
    }

    /**
     * Regista o conjunto de jogadas que foram realizadas ao longo de todo o jogo.
     *
     * @param aJogadas Recebe um ArrayList da classe Jogadas com toda a informação sobre as jogadas.
     */
    public void setJogadas(ArrayList<Jogadas> aJogadas) {
        this.jogadas = new ArrayList<>(aJogadas);
    }

    /**
     * Obtém todas as jogadas que foram registadas no decorrer do jogo.
     *
     * @return Retorna um ArrayList da classe Jogadas com as jogadas realizadas
     */
    public ArrayList<Jogadas> getJogadas() {
        return this.jogadas;
    }

    /**
     * Transforma as coordenadas introduzidas por o jogador em posições mais indicadas
     * para concretizar a jogada no tabuleiro. Ou seja, decrementa uma unidade sobre os valores
     * introduzidos por o jogador, para que o programa consiga intrepertar qual a posiçãoq eu o
     * jogador pretende ocupar do tabuleiro.
     *
     * @param aPosicaoX Recebe um inteiro com a posição X do tabuleiro
     * @param aPosicaoY Recebe um inteiro com a posição Y do tabuleiro
     *
     * @return Retorna um arrayunidimensional com as coordenadas transformadas.
     */
    public int[] jogada(int aPosicaoX, int aPosicaoY) {
        int[] coordenadas = new int[2];

        coordenadas[0] = aPosicaoX - 1;
        coordenadas[1] = aPosicaoY - 1;

        return coordenadas;
    }

    /**
     * Analisa todas as linhas do tabuleiro de modo a comprovar se existe algum vencedor no
     * jogo. Pelo que serão percorridas todas as linhas do array multidimensional que representa
     * o tabuleiro e é verificado se as posições consecutivas da mesma linha o caractere é
     * igual.
     *
     * @return Retorna 1 no caso de ser o X o vencedor, -1 no caso de ser a O a vencedora e zero
     *         se não existir vencedor na linha.
     */
    public int verificaLinhas() {
        char[][] tabuleiro = this.tabuleiro.getTabuleiro();

        for (int i = 0;i < tabuleiro.length;i++ ) {

            if (tabuleiro[i][0] == 'X' && tabuleiro[i][1] == 'X' && tabuleiro[i][2] == 'X')
                return 1;

            else if (tabuleiro[i][0] == 'O' && tabuleiro[i][1] == 'O' && tabuleiro[i][2] == 'O')
                return  -1;

        }
        return 0;
    }

    /**
     * Analisa todas as colunas do tabuleiro de modo a comprovar se existe algum vencedor no
     * jogo. Pelo que serão percorridas todas as colunas do arraymultiidimensional que representa
     * o tabuleiro e é verificado se as posições consecutivas da mesma coluna o caractere é
     * igual.
     *
     * @return Retorna 1 no caso de ser o X o vencedor, -1 no caso de ser a O a vencedora e zero
     *         se não existir vencedor na linha.
     */
    public int verificaColunas() {
        char[][] tabuleiro = this.tabuleiro.getTabuleiro();

        for (int i = 0;i < tabuleiro[0].length;i++ ) {

            if (tabuleiro[0][i] == 'X' && tabuleiro[1][i] == 'X' && tabuleiro[2][i] == 'X')
                return  1;

            else if (tabuleiro[0][i] == 'O' && tabuleiro[1][i] == 'O' && tabuleiro[2][i] == 'O')
                return  -1;

        }

        return 0;
    }

    /**
     * Analisa a diagonais do tabuleiro de modo a comprovar se existe algum vencedor no
     * jogo. Pelo que é verificado se as posições consecutivas da diagonal crescente e decrescente
     * o caractere é igual.
     *
     * @return Retorna 1 no caso de ser o X o vencedor, -1 no caso de ser a O a vencedora e zero
     *         se não existir vencedor na linha.
     */
    public int verificaDiagonais() {

        char[][] tabuleiro = this.tabuleiro.getTabuleiro();

        if (tabuleiro[0][0] == 'X' && tabuleiro[1][1] == 'X' && tabuleiro[2][2] == 'X')
            return  1;

        else if (tabuleiro[0][0] == 'O' && tabuleiro[1][1] == 'O' && tabuleiro[2][2] == 'O')
            return  -1;

        else if (tabuleiro[0][2] == 'O' && tabuleiro[1][1] == 'O' && tabuleiro[2][0] == 'O')
            return  -1;

        else if (tabuleiro[0][2] == 'X' && tabuleiro[1][1] == 'X' && tabuleiro[2][0] == 'X')
            return  1;

        return 0;
    }

    /**
     * Verifica a existência de um empate no decorrer do jogo. De modo que o array multidimensional
     * que representa o tabuleiro de jogo é percorrido e é verificado se todas as posições do tabuleiro
     * estão ocupadas. No caso de se verificar que todas as posições estão ocupados é verificado a
     * existência de empate.
     *
     * @return Returna false no caso de se verificar que o tabuleiro está cheio, true caso contrário
     */
    public boolean verificaEmpate() {

        char[][] tabuleiro = this.tabuleiro.getTabuleiro();

        for (int i = 0;i < tabuleiro.length;i++) {
            for (int j = 0;j < tabuleiro[0].length;j++) {
                if (tabuleiro[i][j] == 0)
                    return true;
            }
        }

        return false;
    }

    /**
     * Verifica qual o vencedor do jogo ou se não existe vencedor. Ou seja, vai verificar
     * se o vencedor do jogo está presente nas linhas do tabuleiro, ou se está presente nas
     * colunas do tabuleiro ou mesmo nas diagonais do tabuleiro.
     * No caso de não se verificar vencedor é verificado se há existência de um empate.
     *
     * @return Retorna 1 no caso de vitoria do caractere X, -1 no caso de vitoria do caractere
     *         Y e -2 no caso de empate.
     */
    public int verificaVencedor() {

        if (verificaLinhas() == 1 || verificaColunas() == 1 || verificaDiagonais() == 1)
            return 1;

        else if (verificaLinhas() == -1 || verificaColunas() == -1 || verificaDiagonais() == -1)
            return  -1;

        else if (!verificaEmpate())
            return -2;

        return 0;
    }

    /**
     * Apresenta uma string com a informação geral sobre o jogo. Isto, é obtém um string
     * com o id do jogo e o tempo que o mesmo levou até ser concluido.
     *
     * @return Retorna uma string com a informação genérica sobre o jogo
     */
    public String toString() {

        return "|   "+this.id+"     |   "+this.tempoDecorrido+"     |";
    }

}
