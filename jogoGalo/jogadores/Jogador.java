package pt.ipc.estgoh.jogoGalo.jogadores;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * A classe Jogador representa um novo jogador presente no jogo Tic-Tac-Toe.
 * Por isso esta classe possui atributos que caracterizam um jogador na aplicação
 * e atributos que permitem aceder à informação armazenadas nas variáveis que
 * caracterizam a classe.
 *
 * @author José Mauricio
 * @version 0.5
 */
public class Jogador {

    private int id;
    private String nickname;
    private int totalVitorias;
    private int numeroJogos;
    private long tempoTotalJogo;

    /**
     * Contrutor por defeito da classe Notificacoes
     */
    public Jogador() {}

    /**
     * Construtor que permite incializar as variáveis que caracterizam o objeto da classe
     * Jogador. Isto, ao passar os valores certos por parâmetro no momento da inicialização
     * do objeto.
     *
     * @param aNickname Recebe como parâmetro uma string com o nickname do joagdor
     * @param aTotalVitorias Recebe um inteiro com o número de vitórias
     * @param aTempoTotal Recebe um long que iniciará a variável que armazena o tempo de jogo
     * @param aNumeroJogos Recebe um inteiro com o número de jogos
     */
    public Jogador(String aNickname, int aTotalVitorias, long aTempoTotal, int aNumeroJogos) {
        this.nickname = aNickname;
        this.totalVitorias = aTotalVitorias;
        this.numeroJogos = aNumeroJogos;
        this.tempoTotalJogo = aTempoTotal;
    }

    /**
     * Apresenta o valor do id armazenada no atributo id.
     *
     * @return  Retorna um inteiro com o id do jogador
     */
    public int getId() {
        return this.id;
    }

    /**
     * Apresenta o valor referente ao nickname do jogador que foi
     * armazenado no atributo nickname.
     *
     * @return Retorna uma string com o nickname do jogador
     */
    public String getNickname() {
        return this.nickname;
    }

    /**
     * Apresenta o número total de vitórias que o jogador obteve em todos
     * os jogos que realizou.
     *
     * @return Retorna um inteiro com o número total de vitórias
     */
    public int getTotalVitorias() {
        return this.totalVitorias;
    }

    /**
     * Apresenta o número total de jogos que o jogador realizaou. Este valor
     * encontra-se armazenado no atributo numeroJogos.
     *
     * @return Retorna um inteiro com o número total de jogos
     */
    public int getNumeroJogos() {
        return this.numeroJogos;
    }

    /**
     * Apresenta o tempo total que o jogador acumulou em todos os jogos que
     * realizou.
     *
     * @return  Retorna um long com o tempo total consumido em jogo
     */
    public long getTempoTotalJogo() {
        return this.tempoTotalJogo;
    }

    /**
     * Regista um novo valor no atributo id da classe Jogador referente
     * ao identificador do jogador.
     *
     * @param aId   Recebe um inteiro com o id do jogador
     */
    public void setId(int aId) {
        this.id = aId;
    }

    /**
     * Regista um novo nickname no atirbuto nickname que foi
     * indicado por o utilizador.
     *
     * @param aNickname Recebe uma string com o novo nickname do jogador
     */
    public void setNickname(String aNickname) {
        this.nickname = aNickname;
    }

    /**
     * Incrementa o valor armazendo no atributo numeroJogos sempre que
     * o jogador concretizar mais um jogo.
     */
    public void setNumeroJogos() {
        this.numeroJogos += 1;
    }

    /**
     * Adiciona ao valor registado no atirbuto tempoTotalJogo um novo valor
     * que foi recebido por parâmetro.
     *
     * @param aTempoTotalJogo Recebe por parâmetro um novo valor a ser adicionado ao
     *                        valor já registado
     */
    public void setTempoTotalJogo (long aTempoTotalJogo) {
        this.tempoTotalJogo += aTempoTotalJogo;
    }

    /**
     * Adiciona ao valor registado no atributo totalVitorias um novo valor
     * que foi recebido por parâmetro. No caso de o valor recebido ser negativo
     * e o valor registado ser igual a zero, o valor do atributo totalVitorias
     * mantém-se inalterado.
     *
     * @param aNumeroVitorias Recebe um inteiro com o número de vitórias a adicionar ao
     *                        número já registado.
     */
    public void setTotalVitorias (int aNumeroVitorias) {

        if (aNumeroVitorias < 0 && this.totalVitorias <= 0)
            this.totalVitorias = 0;
        else
            this.totalVitorias += aNumeroVitorias;
    }

    /**
     * Apresenta uma string com toda a informação armazenada nas variáveis de um
     * objeto da classe Jogador.
     *
     * @return Retorna uma string com a informação do objeto.
     */
    public String toString() {
        NumberFormat format = new DecimalFormat("##");
        String tempoSegundos = format.format(this.tempoTotalJogo / 1000);   //Formata o tempo total de jogo em segundos
        String tempoMinutos = format.format(this.tempoTotalJogo / (60 * 1000));     //Formata o tempo total de jogo em minutos
        String tempoHoras = format.format(this.tempoTotalJogo / (60 * 60 * 1000));  //Formata o tempo total de jogo em horas

        return "|   "+this.nickname+"   |   "+this.numeroJogos+"    |   "+tempoHoras+" horas; "+tempoMinutos+"minutos; "+tempoSegundos+
                "segundos     |   "+this.totalVitorias+"  |";
    }
}
