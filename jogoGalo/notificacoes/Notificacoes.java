package pt.ipc.estgoh.jogoGalo.notificacoes;

/**
 * A classe Notificacoes representa o objeto notificacoes da aplicacao. Possuindo
 * os atributos que caracterizam uma notificação. Assim como os métodos que permitirão
 * aceder à informação registada.
 *
 * @author José Mauricio
 * @version 0.4
 */
public class Notificacoes {

    private String mensagem;

    /**
     * Contrutor por defeito da classe Notificacoes
     */
    public Notificacoes() {}

    /**
     * Registra a mensagem a ser mostrada para uma notificação.
     *
     * @param aMensagem    String com a nova mensagem
     */
    public void setMensagem(String aMensagem) {

        this.mensagem = aMensagem;
    }

    /**
     * Mostra a mensagem para cada objeto da classe Notificacao. Irá
     * apresentar o conteúdo das notificacoes enviadas oa utilizador.
     *
     * @return      Retorna uma String com a mensagem da notificacao
     */
    public String toString() {

        return "| "+this.mensagem+" |";
    }
}
