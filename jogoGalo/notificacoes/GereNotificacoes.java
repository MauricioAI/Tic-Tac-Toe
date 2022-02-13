package pt.ipc.estgoh.jogoGalo.notificacoes;

import pt.ipc.estgoh.jogoGalo.baseDados.BaseDados;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * A classe GereNotificacoes será util para gerir a ligacao à base de dados
 * de modo a realizar a manutenção na tabela notificacoes.
 *
 * @author José Mauricio
 * @version 0.4
 */
public class GereNotificacoes {

    private BaseDados bd = new BaseDados();
    private PreparedStatement ps = null;
    private ResultSet rs = null;

    /**
     * Construtor por defeito para a classe GereNotificacoes
     */
    public GereNotificacoes() {
    }

    /**
     * Insere novas notificacoes direcionadas ao jogador que iniciou a sessão e que apresenta um número
     * de vitórias múltiplo de 10. Comeca por fazer um select à tabela jogador para extrair a soma de
     * vitórias desse jogador caso exista. Com esse valor verifica se é múltiplo de 10, mas que não seja igual
     * a 0.
     *
     * @param aNickname  Recebe como parâmetro o nickname do jogador para verificar se existe registado na base de dados
     * @param aIdJogador Recebe como parâmetro o id do jogador que tem a sessão ativa
     */
    public void registarNotificacoesVitorias(String aNickname, int aIdJogador) {

        try {

            ps = bd.conectarBd().prepareStatement("SELECT SUM(JG_NUMERO_VITORIAS) FROM jogador WHERE JG_NICKNAME = ?;");

            ps.setString(1, aNickname);

            rs = ps.executeQuery();     //Extrai o total de vitorias para o jogador até ao momento

            while (rs.next()) {

                //Garante que o total de vitórias é múltiplo de 10 e não é 0
                if (rs.getInt(1) % 10 == 0 && rs.getInt(1) != 0) {

                    ps = bd.conectarBd().prepareStatement("INSERT notificacoes (JG_ID, N_NOTIFICACAO, N_ESTADO)" +
                            "VALUES (?, ?, ?);");

                    String mensagem = "Parabéns! O seu número de vitórias é superior a múltiplos de 10.";
                    String estado = "Por ler";

                    ps.setInt(1, aIdJogador);   //Envia a notificação ao último registo do jogador
                    ps.setString(2, mensagem);
                    ps.setString(3, estado);

                    ps.execute();
                }
            }
        } catch (SQLException sqe) {
            System.out.println("Exception: " + sqe);
        } finally {
            bd.terminarConeccao();      //Encerra a ligação à base de dados

            if (ps != null) {

                try {

                    ps.close();

                } catch (SQLException sqe) {
                    System.out.println("Exception: " + sqe);
                }
            }

            if (rs != null) {

                try {
                    rs.close();
                } catch (SQLException sqe) {
                    System.out.println("Exception: " + sqe);
                }
            }
        }
    }

    /**
     * Insere novas notificacoes direcionadas ao jogador que iniciou a sessão e que apresenta um maior número
     * de vitórias. Comeca por fazer um select à tabela jogador para extrair a soma de vitórias dos jogadores que
     * não seja o jogador que está ativo. Com esse valor verifica se a soma de vitórias de todos os registos é superior
     * ao que foi obtido dos outros registos. No caso de se verificar a condição é obtido o último id que foi atribuído
     * ao utilizador com o nickname recebido por parâmetro.
     *
     * @param aNickname Recebe como parâmetro o nickname do jogador que iniciou a sessão para verificar a existência
     *                  na base de dados
     */
    public void registarNotificacaoVitorioso(String aNickname) {

        try {

            ps = bd.conectarBd().prepareStatement("SELECT MAX(JG_ID) FROM jogador WHERE JG_NICKNAME = ? HAVING SUM(JG_NUMERO_VITORIAS) > " +
                    "(SELECT SUM(JG_NUMERO_VITORIAS) FROM jogador WHERE JG_NICKNAME != ? ORDER BY SUM(JG_NUMERO_VITORIAS));");

            ps.setString(1, aNickname);
            ps.setString(2, aNickname);

            rs = ps.executeQuery();

            if (rs.next()) {

                ps = bd.conectarBd().prepareStatement("INSERT notificacoes (JG_ID, N_NOTIFICACAO, N_ESTADO)" +
                        "VALUES (?, ?, ?);");

                String mensagem = "Parabéns! Você contém o maior número de vitórias.";
                String estado = "Por ler";

                ps.setInt(1, rs.getInt(1));   //Envia a notificação ao último registo do jogador
                ps.setString(2, mensagem);
                ps.setString(3, estado);

                ps.execute();
            }

        } catch (
                SQLException sqe) {
            System.out.println("Exception: " + sqe);
        } finally {
            bd.terminarConeccao();      //Encerra a ligação à base de dados

            if (ps != null) {

                try {

                    ps.close();

                } catch (SQLException sqe) {
                    System.out.println("Exception: " + sqe);
                }
            }

            if (rs != null) {

                try {
                    rs.close();
                } catch (SQLException sqe) {
                    System.out.println("Exception: " + sqe);
                }
            }
        }

    }

    /**
     * Extrai da tabela notificacoes todos os registos que existem atribuidos ao jogador que ainda não
     * foram lidas até ao momento que este método for invocado. Recebe como parâmtro o id do jogador a quem
     * foram atribuídas as notificações.
     *
     * @param aId Recebe um inteiro com o id do jogador que mantém a sessão ativa
     * @return Retorna um ArrayList da classe Notificacoes com todas as notificacoes não lidas
     */
    public ArrayList<Notificacoes> mostrarNoficacoes(int aId) {

        ArrayList<Notificacoes> notificacoes = new ArrayList<>();

        try {

            ps = bd.conectarBd().prepareStatement("SELECT  N_NOTIFICACAO FROM notificacoes " +
                    "WHERE JG_ID = ? AND N_ESTADO = ?;");

            ps.setInt(1, aId);
            ps.setString(2, "Por ler");

            rs = ps.executeQuery();

            while (rs.next()) {
                Notificacoes notificar = new Notificacoes();

                notificar.setMensagem(rs.getString(1));
                notificacoes.add(notificar);
            }


        } catch (SQLException sqe) {
            System.out.println("Exception: " + sqe);
        } finally {
            bd.terminarConeccao();

            if (ps != null) {

                try {

                    ps.close();

                } catch (SQLException sqe) {
                    System.out.println("Exception: " + sqe);
                }
            }

            if (rs != null) {

                try {
                    rs.close();
                } catch (SQLException sqe) {
                    System.out.println("Exception: " + sqe);
                }
            }
        }
        return notificacoes;
    }

    /**
     * Atualiza o estado da notificacao a partir do momento que o jogador lê todas as
     * notificacoes que tem por ler. Recebe como parâmetro o id do jogador que
     * está a executar esta tarefas de modo a fazer uma sub-consulta para que seja atualizado
     * o estado de todas as notificacoes, no qual a chave forasteira seja igual ao id do jogador.
     *
     * @param aIdJogador Recebe um inteiro com o id do jogador que está a executar o método
     */
    public void atualizarEstadoNotificacao(int aIdJogador) {

        try {

            ps = bd.conectarBd().prepareStatement("UPDATE notificacoes SET N_ESTADO = ? " +
                    "WHERE JG_ID = ? AND N_ESTADO = ?;");

            ps.setString(1, "Lida");
            ps.setInt(2, aIdJogador);
            ps.setString(3, "Por ler");

            ps.executeUpdate();

        } catch (SQLException sqe) {
            System.out.println("Exception: " + sqe.getMessage());
        } finally {
            bd.terminarConeccao();
            if (ps != null) {

                try {

                    ps.close();

                } catch (SQLException sqe) {
                    sqe.printStackTrace();
                }
            }
        }
    }
}
