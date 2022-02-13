package pt.ipc.estgoh.jogoGalo.atividade;

import pt.ipc.estgoh.jogoGalo.baseDados.BaseDados;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * A classe GereInformacaoGenerica vai fazer a ligaçao entre a base de dados e a aplicação
 * de modo a registar e obter a informação genérica em tempo real sobre a aplicação.
 *
 * @author José Mauricio
 * @version 0.2
 */
public class GereInformacaoGenerica {

    private BaseDados bd = new BaseDados();
    private PreparedStatement ps = null;
    private ResultSet rs = null;

    /**
     * Construtor por defeito da classe GereInformacaoGenerica
     */
    public GereInformacaoGenerica () {}

    /**
     * Regista na tabela informacao_generica o número total de execuções e a data da última execução.
     * No momento que a aplicação for executada por o utilizador. Recebe como parâmetro um objeto
     * da classe InformacaoGenerica com a informação genérica da aplicação
     *
     * @param aInformacaoGenerica   Recebe como parâmetro um objeto da classe InformacaoGenerica
     */
    public void registarInformacaoGenerica(InformacaoGenerica aInformacaoGenerica) {

        try {

            ps = bd.conectarBd().prepareStatement(
                    "SELECT IG_ID FROM informacao_generica;");

            rs = ps.executeQuery();

            /*
            Verifica se já existe algum registo na tabela informacao_generica. Se não existir regista
            um novo registo. Se existir atualiza esse mesmo registo.
             */
            if (!rs.next()) {

                ps = bd.conectarBd().prepareStatement(
                        "INSERT INTO informacao_generica (IG_NUMEROEXECUCOES, IG_TEMPOEXECUCAO)" +
                                "VALUES (?,?);");

                int numeroExecucoes = aInformacaoGenerica.getNumeroExecucoes();
                Date ultimaExecucao = new Date (aInformacaoGenerica.getDataUltimaExecucao().getTime());

                ps.setInt(1,numeroExecucoes);
                ps.setDate(2, ultimaExecucao);

                ps.execute();

            } else {

                ps = bd.conectarBd().prepareStatement(
                        "UPDATE informacao_generica SET IG_NUMEROEXECUCOES = ?, IG_TEMPOEXECUCAO = ?" +
                                "WHERE IG_ID = ?;");

                int numeroExecucoes = aInformacaoGenerica.getNumeroExecucoes();
                Date ultimaExecucao = new Date (aInformacaoGenerica.getDataUltimaExecucao().getTime());

                ps.setInt(1,numeroExecucoes);
                ps.setDate(2, ultimaExecucao);
                ps.setInt(3, rs.getInt(1));

                ps.executeUpdate();

            }

        } catch (SQLException sqe) {
            System.out.println("Exception: "+sqe);
        } finally {
            bd.terminarConeccao();

            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException sqe) {
                    System.out.println("Exception: "+sqe);
                }
            }

            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqe) {
                    System.out.println("Exception: "+sqe);
                }
            }
        }

    }

    /**
     * Devolve um objeto da classe InformacaoGenerica com a informação registada na tabela
     * informacao_generica até ao momento da execução deste método. Após a executação deste
     * método é verificado se já existe informação armazenada na tabela. Se não existir o objeto
     * retornado vai ter o número total de execuções igual a 0.
     *
     * @return Retorna um objeto da classe InformacaoGenerica
     */
    public InformacaoGenerica visualizarInformacaoGenerica() {

        InformacaoGenerica atividadeGenerica = null;
        try {

            ps = bd.conectarBd().prepareStatement(
                    "SELECT IG_NUMEROEXECUCOES, IG_TEMPOEXECUCAO FROM informacao_generica;");

            rs = ps.executeQuery();

            if (rs.next()) {

                atividadeGenerica = new InformacaoGenerica(rs.getInt(1), rs.getDate(2));
            } else {
                atividadeGenerica = new InformacaoGenerica();
                atividadeGenerica.setNumeroExecucoes(0);
            }

        } catch (SQLException sqe) {
            System.out.println("Exception: "+sqe);
        } finally {
            bd.terminarConeccao();

            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException sqe) {
                    System.out.println("Exception: "+sqe);
                }
            }

            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqe) {
                    System.out.println("Exception: "+sqe);
                }
            }
        }
        return atividadeGenerica;
    }

}
