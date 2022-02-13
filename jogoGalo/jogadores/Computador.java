package pt.ipc.estgoh.jogoGalo.jogadores;

/**
 * A classe Computador representa o próprio computador que vai jogar
 * contra o utilizador. Desse modosempre que for iniciado um novo
 * jogo contra o computador é criado um novo objeto da classe Computador.
 *
 * @author José Mauricio
 * @version 0.3
 */
public class Computador {

    /**
     * Identificador do computador que foi registado na bd
     */
    private int idComputador;

    /**
     * Construtor por defeito para a classe GereNotificacoes
     */
    public Computador() {}

    /**
     * Construtor que permite inicializar o atributo do computador com um novo valor.
     * Isto sempre que o objeto da classe for inicializado e seja passado por parâmetro
     * algum valor.
     *
     * @param aIdComputador Recebe um inteiro com o id do computador que foi registado na
     *                      base de dados para um novo jogo.
     */
    public Computador(int aIdComputador) {
        this.idComputador = aIdComputador;
    }

    /**
     * Apresenta o valor armazenado no atributo id que corresponde ao
     * identificador do computador na base de dados.
     *
     * @return      Retorna um inteiro com o id do computador
     */
    public int getIdComputador() {
        return this.idComputador;
    }

    /**
     * Gera aleatóriamente dois valores destinados às coordenadas da jogada do
     * computador. Após gerar os valores guarda-os num array unidimensional de inteiros.
     *
     * @return  Devolve um array de inteiros com as coordenada emitidas por o computador
     */
    public int[] jogar() {
        int[] coordenadas = new int[2];

        coordenadas[0] = (int) (Math.random() * 3);
        coordenadas[1] = (int) (Math.random() * 3);

        return coordenadas;
    }

}
