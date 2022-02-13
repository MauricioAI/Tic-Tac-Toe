package pt.ipc.estgoh.jogoGalo.atividade;

import pt.ipc.estgoh.jogoGalo.jogadores.Jogador;
import java.util.Date;

/**
 * A classe Atividade representa uma nova atividade realizada por o
 * utilizador que será armazenada numa base de dados relacional.
 *
 * @author José Mauricio
 * @version 0.2
 */
public class Atividade {

	private Jogador jogador;
	private String acao;
	private Date data;
	private Date hora;

	/**
	 * No construtor da classe é especificado a ação realizada, a
	 * data em que está a ser realizada, a hora, e um objeto da
	 * classe Jogador que representa o jogador que realmente efetuou aquela ação.
	 *
	 * @param aAcao			Ação realizada por o utilizador
	 * @param aData			Data em que foi realizada a ação
	 * @param aHora		   	Hora em que o utilizador executou uma ação
	 * @param aJogador		Jogador que realizou determinada ação
	 */
	public Atividade(String aAcao, Date aData, Date aHora, Jogador aJogador) {

		this.acao = aAcao;
		this.data = aData;
		this.hora = aHora;
		this.jogador = aJogador;
	}

	/**
	 * O método get irá mostrar a ação que foi registada por o
	 * sistema.
	 *
	 * @return		Retorna uma String com a ação realizada
	 */
	public String getAcao() {

		return this.acao;
	}

	/**
	 * Irá devolver a data da a ação que foi registada naquele
	 * preciso momento.
	 *
	 * @return		Retorna a Data da respetiva ação
	 */
	public Date getData() {

		return this.data;
	}

	/**
	 * Permite visualizar o jogador que realizou a ação.
	 *
	 * @return		Retorna um objeto da classe Jogador.
	 */
	public Jogador getJogador() {

		return this.jogador;
	}

	/**
	 * Este método serve para que seja possível aceder à hora de uma
	 * respetiva ação.
	 *
	 * @return		Retorna a hora em que a a acao foi inserida no sistema
	 */
	public Date getHora() {

		return this.hora;
	}

	/**
	 * Permite visualizar toda a informação sobre cada registo de
	 * atividade que é feito por o sistema.
	 *
	 * @return		Retorna uma string com toda a informação formatada
	 */
	public String toString() {

		return "<"+this.data+"> <"+this.hora+"> <"+this.acao+"> <"+this.jogador.getNickname()+">";
	}

}