package pt.ipc.estgoh.jogoGalo.leituraDados;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 * ESta classe vai permitir realizar leitura de dados a partir dos
 * dispositivos de entrada. Esta classe foi inspirada no código
 * disponibilizado por o professor Marco Veloso.
 *
 * @author José Mauricio
 * @version 0.2
 */
public class LeituraDados {

	/**
	 * Construtor por defeito da classe LeituraDados.
	 */
	public LeituraDados() {}

	/**
	 *	Este método vai ler dos dispositivos de entrada um valor
	 *	do tipo String.
	 *
	 * @return	Retorna a String que foi lida
	 */
	public String lerStrings() {
		String s = "";

		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in), 1);
			s = in.readLine();
		} catch (IOException e) {

			System.out.println("Erro. O valor introduzido não é uma string.");
		}

		return s;
	}

	/**
	 * Este método vai ler dos dispositivos de entrada o valor inteiro.
	 *
	 * @return	Retorna um valor inteiro
	 */
	public int lerInteiros() {
		while (true) {
			try {
				return Integer.valueOf(lerStrings().trim()).intValue();
			} catch (Exception e) {

				System.out.println("Erro. O valor introduzido não é um inteiro.");
			}
		}
	}


	/**
	 * Este método vai ler dos dispositivos de entrada um valor do tipo float.
	 *
	 * @return	Retorna um valor do tipo float
	 */
	public float lerFloats() {
		while (true) {
			try {
				return Float.valueOf(lerStrings().trim()).floatValue();
			} catch (Exception e) {

				System.out.println("Erro. Este valor não é um float.");
			}
		}
	}

}