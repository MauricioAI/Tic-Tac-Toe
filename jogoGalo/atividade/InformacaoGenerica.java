package pt.ipc.estgoh.jogoGalo.atividade;

import java.util.Date;

/**
 * Esta classe representa a informação genérica sobre o sistema. Cada vez
 * que um utilizador executa a aplicação é armazenado o número total de
 * execuções, assim como a data da última execução.
 *
 * @author José Mauricio
 * version 0.2
 */
public class InformacaoGenerica {

    private int numeroExecucoes;
    private Date dataUltimaExecucao;

    /**
     * Construtor de um novo registo de informação genérica sem especificar
     * o número de excuções e a data da última execução.
     */
    public InformacaoGenerica() {}

    /**
     * Constroi um novo registo de execução da aplicação. Especifica o número de
     * execuções até ao momento incluindo o novo registo e específica a data em
     * que a execução atual está a ocorrer.
     *
     * @param aNumeroExecucoes      Número total de execuções + 1
     * @param aDataUltimaExecucao   Data em que a aplicação está a ser executada
     */
    public InformacaoGenerica (int aNumeroExecucoes, Date aDataUltimaExecucao) {
        this.dataUltimaExecucao = aDataUltimaExecucao;
        this.numeroExecucoes = aNumeroExecucoes;
    }

    /**
     * Obtêm o número total de exceuções da aplicação registado no atributo
     * numeroExecucoes.
     *
     * @return  Retorna o número total de execuções da aplicação
     */
    public int getNumeroExecucoes() {

        return this.numeroExecucoes;
    }

    /**
     * Regista o número total de execuções da aplicação no atributo
     * numeroExecucoes.
     *
     * @param aNumeroExecucoes Novo número de execuções do sistema
     */
    public void setNumeroExecucoes(int aNumeroExecucoes) {

        this.numeroExecucoes = aNumeroExecucoes;
    }

    /**
     * Obtêm o valor da data da última execução que está guardada
     * na variável dataExecucao.
     *
     * @return  Devolve a data da última execução da aplicação
     */
    public Date getDataUltimaExecucao() {

        return this.dataUltimaExecucao;
    }

    /**
     * Retorna uma String com toda a informação que caracteriza a classe InformacaoGenerica.
     *
     * @return  Retorna a informação genérica
     */
    public String toString() {

        return "Esta aplicação foi executada "+this.numeroExecucoes+" vezes, sendo "+this.dataUltimaExecucao+" a data" +
                " da última execução!";
    }
}
