package pt.ipc.estgoh.jogoGalo.jogo;

/**
 * A classe Tabuleiro representa a estrutura que armazena o tabuleiro do
 * jogo. Desse modo terá como atributo um array multidimensional do tipo char
 * que representa a dimensação do tabuleiro.
 *
 * @author José Mauricio
 * @version 0.3
 */
public class Tabuleiro {

    private char[][] tabuleiro;

    /**
     * Construtor que permite inicializar o atributo da classe Tabuleiro no momento
     * que um novo objeto da classe Tabuleiro é instaciado.
     *
     * @param aTabuleiro Recebe um arrat multidimensional do tipo char
     */
    public Tabuleiro(char[][] aTabuleiro) {
        this.tabuleiro = aTabuleiro;
    }

    /**
     * Apresenta o conteúdo do tabuleiro do jogo armazenado no atributo tabuleiro.
     *
     * @return Retorna uma array multidimensional do tipo char com o conteúdo do tabuleiro
     */
    public char[][] getTabuleiro() {

        return this.tabuleiro;
    }

    /**
     * Imprime o tabuleiro no ecrã no momento do jogo. Desse modo este método permite
     * iterar o tabuleiro e apresentar o mesmo formatado para facilitar ao jogador
     * a sua visualização.
     */
    public void mostraTabuleiro() {
        int linhas = this.tabuleiro.length;
        int colunas = this.tabuleiro[0].length;

        if (this.tabuleiro != null) {

            for (int i = 0; i < linhas; i++) {
                if (i != 0)
                    System.out.println("\n------");
                for (int j = 0; j < colunas; j++) {

                    if (this.tabuleiro[i][j] == 0)
                        System.out.print(" ");
                    else
                        System.out.print(this.tabuleiro[i][j]);

                    if (j != colunas - 1)
                        System.out.print("|");

                }
            }
            System.out.println();
        }
    }

    /**
     * Atualiza o conteúdo do tabuelrio à media que as jogadas vão sendo realizadas.
     * Percorrendo o array dimensional do tipo char e colocando numa determinada posição
     * com as coordenadas indicadas por o jogador X ou O.
     *
     * @param aCaractere    Recebe um char com o caractere a ser colocado na posição em questão
     * @param aJogada Recebe um array unidimensional com as coordenadas que será colocado o caractere
     */
    public void atualizarTabuleiro(char aCaractere, int[] aJogada) {
        int linhas = aJogada[0];
        int colunas = aJogada[1];

        if (this.tabuleiro != null) {

            for (int i = 0; i < this.tabuleiro.length; i++) {
                for (int j = 0; j < this.tabuleiro[0].length; j++) {

                    this.tabuleiro[linhas][colunas] = aCaractere;
                }
            }

        }
    }

    /**
     * Verifica se as coordenadas de jogada são válidas ou não, ou seja, verifica se para uma
     * determinada posição o tabuleiro do jogo está vazio ou se as coordenadas introduzidas por
     * o jogador ultrapassam os limites do tabuleiro.
     *
     * @param aJogada Recebe um array unidimensional do tipo inteiros com as coordenadas
     *
     * @return Retorna true se a posição escolhida estiver vazia, e false se não for possível concretizar
     *         essa posição de jogada.
     */
    public boolean verificaPosicao(int[] aJogada) {
        int aCoordenadaX = aJogada[0];
        int aCoordenadaY = aJogada[1];

        try {

            if (this.tabuleiro[aCoordenadaX][aCoordenadaY] == 0)
                return true;

        } catch (ArrayIndexOutOfBoundsException aieb) {
            return false;
        }

        return false;
    }
}
