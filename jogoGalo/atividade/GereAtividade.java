package pt.ipc.estgoh.jogoGalo.atividade;

import pt.ipc.estgoh.jogoGalo.baseDados.BaseDados;
import pt.ipc.estgoh.jogoGalo.jogadores.Jogador;

import java.sql.*;
import java.util.ArrayList;

/**
 * A classe GereAtividade será útil para realizar a manutenção dos dados contidos na
 * tabela atividade da base de dados. Ou seja, esta classe irá fazer a monitorização da
 * atividade dos jogadores na aplicação.
 *
 * @author José Mauricio
 * @version 0.3
 */
public class GereAtividade {

    private BaseDados bd = new BaseDados();
    private PreparedStatement ps = null;
    private ResultSet rs = null;

    /**
     * Construtor por defeito da classe GereAtividade
     */
    public GereAtividade() {
    }

    /**
     * Regista todas ações realizados por cada um dos jogadores da aplicação na
     * tabela atividade da base de dados. Recebe como parâmetro um objeto da classe
     * Atividade com a ação realizada e o jogador que a realizou.
     *
     * @param aAtividade Recebe um objeto da classe Atividade
     */
    public void registarAtividade(Atividade aAtividade) {


        try {

            if (!aAtividade.getJogador().getNickname().equalsIgnoreCase("Anónimo")) {

                ps = bd.conectarBd().prepareStatement("insert into atividade (JG_ID, A_ACAO, A_DATAACAO, A_TEMPOACAO) " +
                        "VALUES(?,?,?,?);");

                int idUtilizador = aAtividade.getJogador().getId();
                String acaoRealizada = aAtividade.getAcao();
                Date dataAcao = new Date(aAtividade.getData().getTime());
                Time hora = new Time(aAtividade.getHora().getTime());

                ps.setInt(1, idUtilizador);
                ps.setString(2, acaoRealizada);
                ps.setDate(3, dataAcao);
                ps.setTime(4, hora);

                ps.execute();
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
        }

    }

    /**
     * Devolve um ArrayList da classe Atividade com todos os registos da tabela atividade.
     * Este método será excutado por qualquer jogador que executar a aplicação.
     *
     * @return Retorna um ArrayList da classe Atividade
     */
    public ArrayList<Atividade> listarTodaAtividade() {

        ArrayList<Atividade> listaLog = new ArrayList<>();

        try {

            ps = bd.conectarBd().prepareStatement("SELECT  A_ACAO, A_DATAACAO, A_TEMPOACAO,j.JG_NICKNAME " +
                    "FROM atividade a, jogador j WHERE a.JG_ID = j.JG_ID ORDER BY JG_NICKNAME;");

            rs = ps.executeQuery();

            while (rs.next()) {

                Jogador jogador = new Jogador();
                jogador.setNickname(rs.getString(4));

                listaLog.add(new Atividade(rs.getString(1), rs.getDate(2),
                        rs.getTime(3), jogador));
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
        return listaLog;
    }

    /**
     * Retorna um ArrayList com todos os registos referentes ao jogador cujo o nickname seja
     * igual ao valor recebido como parâmetro. O utilizador comum sempre que pretender pesquisar registos
     * de atividade por jogador deve indicar o nickname do jogador, que será passado por parâmetro
     * e devolve os registos referentes a esse jogador.
     *
     * @param aNome Recebe uma string com o nickname do jogador
     * @return Retorna um ArrayList da classe Atividade com os registos do jogador
     */
    public ArrayList<Atividade> pesquisarAtividadeUtilizador(String aNome) {

        ArrayList<Atividade> listaLog = new ArrayList<>();

        try {

            ps = bd.conectarBd().prepareStatement("SELECT  A_ACAO, A_DATAACAO, A_TEMPOACAO,j.JG_NICKNAME " +
                    "FROM atividade a, jogador j WHERE a.JG_ID = j.JG_ID " +
                    "AND j.JG_NICKNAME like ? OR j.JG_NICKNAME like ? OR j.JG_NICKNAME like ? order by A_TEMPOACAO;");

            ps.setString(1, "%" + aNome);
            ps.setString(2, aNome + "%");
            ps.setString(3, aNome);

            rs = ps.executeQuery();

            while (rs.next()) {

                Jogador user = new Jogador();
                user.setNickname(rs.getString(4));

                listaLog.add(new Atividade(rs.getString(1), rs.getDate(2),
                        rs.getTime(3), user));
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
        return listaLog;
    }


}