package pt.ipc.estgoh.jogoGalo.jogadores;

import pt.ipc.estgoh.jogoGalo.baseDados.BaseDados;

import java.sql.*;
import java.util.ArrayList;

/**
 * A classe GereJogador será util para gerir a ligacao à base de dados
 * de modo a realizar a manutenção na tabela jogador.
 *
 * @author José Mauricio
 * @version 0.3
 */
public class GereJogador {

    private BaseDados bd = new BaseDados();
    private PreparedStatement ps = null;
    private ResultSet rs = null;

    /**
     * Contrutor por defeito da classe Notificacoes
     */
    public GereJogador() {
    }

    /**
     * Regista na tabela jogador da base de dados um novo jogador, isto, no momento que o jogador
     * regista um nickanme na aplicação. Depois de a ação ter sido efetuada com sucesso é returnado o
     * id que foi atribuido ao registo daquele jogador. Isto, para que sempre que sejam efetuadas ações
     * na aplicação durante a sua atividade existir o elemento que o diferencie dos outros jogadores, visto
     * que, podem haver vários jogadores com o mesmo nickname.
     *
     * @param aJogador  Recebe por parâmetro um objeto da classe Jogador com a infromação do mesmo
     *
     * @return Retorna um inteiro com o id do jogador em caso de sucesso, e 0 em caso de
     *         insucesso
     */
    public int registarJogador(Jogador aJogador) {

        try {

            ps = bd.conectarBd().prepareStatement("INSERT INTO jogador (JG_NICKNAME, JG_NUMERO_JOGOS, JG_NUMERO_VITORIAS, JG_TEMPO_TOTAL) " +
                    "VALUES(?,?,?,?);", Statement.RETURN_GENERATED_KEYS);

            String nickname = aJogador.getNickname();
            ps.setString(1, nickname);
            ps.setInt(2, aJogador.getNumeroJogos());
            ps.setInt(3, aJogador.getTotalVitorias());
            ps.setLong(4, aJogador.getTempoTotalJogo());

            ps.execute();

            int id = -1;

            rs = ps.getGeneratedKeys();

            if (rs.next()) {

                id = rs.getInt(1);

                return id;
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

        return 0;
    }

    /**
     * Seleciona da tabela jogador todos os jogadores que foram registados na aplicação. Depois de
     * ter sido executado o comando select para obter a informação desejada sobre os jogadores, o
     * resultado obtido é armazenado num ArrayList da classe Jogador. Esta informação é guardada no
     * ArrayList ordenada por o nickname de forma ascendente.
     *
     * @return  Retorna um ArrayList da classe Jogador com a informação sobre os jogadores.
     */
    public ArrayList<Jogador> listarJogadoresNome() {

        ArrayList<Jogador> listaJogador = new ArrayList<>();

        try {

            ps = bd.conectarBd().prepareStatement("SELECT JG_NICKNAME, JG_NUMERO_JOGOS, JG_NUMERO_VITORIAS, JG_TEMPO_TOTAL " +
                                                "FROM jogador ORDER by JG_NICKNAME;");

            rs = ps.executeQuery();

            while (rs.next()) {
                listaJogador.add(new Jogador(rs.getString(1), rs.getInt(3), rs.getInt(4), rs.getInt(2)));
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
        return listaJogador;
    }

    /**
     * Extrai da tabela jogador todos os jogadores que foram registados na aplicação ordenados
     * por ordem ascendente com base no número total de jogos efetuados. Para isto é executado um
     * comando select que vai obter os dados desejados sobre os devidos jogadores e armazena o resultado
     * num ArrayList da classe Jogador.
     *
     * @return  Retorna um ArrayList da classe Jogador com toda a informação
     */
    public ArrayList<Jogador> listarJogadoresNumJogos() {
        ArrayList<Jogador> listaJogador = new ArrayList<>();

        try {

            ps = bd.conectarBd().prepareStatement("SELECT JG_NICKNAME, JG_NUMERO_JOGOS, JG_NUMERO_VITORIAS, JG_TEMPO_TOTAL " +
                    "FROM jogador ORDER by JG_NUMERO_JOGOS");

            rs = ps.executeQuery();

            while (rs.next()) {
                listaJogador.add(new Jogador(rs.getString(1), rs.getInt(3), rs.getInt(4), rs.getInt(2)));
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
        return listaJogador;
    }

    /**
     * Extrai da tabela jogador todos os jogadores que foram registados na aplicação ordenados
     * por ordem ascendente com base no número de vitórias conseguidas em todos os ojogos realizados.
     * Para isto é executado um comando select que vai obter os dados desejados sobre os devidos
     * jogadores e armazena o resultado num ArrayList da classe Jogador.
     *
     * @return  Retorna um ArrayList da classe Jogador com toda a informação
     */
    public ArrayList<Jogador> listarJogadoresNumVitoria() {

        ArrayList<Jogador> listaJogador = new ArrayList<>();

        try {

            ps = bd.conectarBd().prepareStatement("SELECT JG_NICKNAME, JG_NUMERO_JOGOS, JG_NUMERO_VITORIAS, JG_TEMPO_TOTAL " +
                    "FROM jogador ORDER by JG_NUMERO_VITORIAS");

            rs = ps.executeQuery();

            while (rs.next()) {
                listaJogador.add(new Jogador(rs.getString(1), rs.getInt(3), rs.getInt(4), rs.getInt(2)));
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
        return listaJogador;

    }

    /**
     * Extrai da tabela jogador todos os jogadores que foram registados na aplicação ordenados
     * por ordem ascendente com base no tempo total gasto a jogar. Para isto é executado um
     * comando select que vai obter os dados desejados sobre os devidos jogadores e armazena o resultado
     * num ArrayList da classe Jogador.
     *
     * @return  Retorna um ArrayList da classe Jogador com toda a informação
     */
    public ArrayList<Jogador> listarJogadoresTempoJogo() {

        ArrayList<Jogador> listaJogador = new ArrayList<>();

        try {

            ps = bd.conectarBd().prepareStatement("SELECT JG_NICKNAME, JG_NUMERO_JOGOS, JG_NUMERO_VITORIAS, JG_TEMPO_TOTAL " +
                    "FROM jogador ORDER by JG_TEMPO_TOTAL");

            rs = ps.executeQuery();

            while (rs.next()) {
                listaJogador.add(new Jogador(rs.getString(1), rs.getInt(3), rs.getInt(4), rs.getInt(2)));
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
        return listaJogador;
    }

    /**
     * Pesquisa toda a infromação sobre o jogador cujo o seu nickname é igual ao nickname
     * que foi recebido por parâmetro. O resultado obtido encontra-se ordenado de forma ascendente
     * tendo por base o seu número de jogos.
     *
     * @param aNickname Recebe uma string com o nickname do jogador a ser pesquisado
     *
     * @return Retorna uma ArrayList da classe Jogador com a informação dos jogadores
     */
    public ArrayList<Jogador> pesquisarJogador(String aNickname) {
        ArrayList<Jogador> listaJogador = new ArrayList<>();

        try {

            ps = bd.conectarBd().prepareStatement("SELECT JG_NICKNAME, JG_NUMERO_JOGOS, JG_NUMERO_VITORIAS, JG_TEMPO_TOTAL " +
                    "FROM jogador WHERE JG_NICKNAME LIKE ? OR JG_NICKNAME LIKE ? OR JG_NICKNAME LIKE ? ORDER by JG_NUMERO_JOGOS");

            ps.setString(1, aNickname);
            ps.setString(2, "%"+aNickname);
            ps.setString(3, aNickname+"%");

            rs = ps.executeQuery();

            while (rs.next()) {
                listaJogador.add(new Jogador(rs.getString(1), rs.getInt(3), rs.getInt(4), rs.getInt(2)));
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
        return listaJogador;
    }

    /**
     * Atualiza a informação estatistíca do jogador após o jogo que esteja a decorrer tenha terminado.
     * Desta forma recebe por parâmetro um objeto da classe Jogador com os novos dados relacionados com
     * o jogador em ação.
     *
     * @param aJogador Recebe um objeto da classe Jogador com a informação sobre a estítica do jogador
     *
     * @return Returna true se ação tenha sido realizada com sucesso, false caso contrário
     */
    public boolean registarEstatistica(Jogador aJogador) {
        try {

            ps = bd.conectarBd().prepareStatement("UPDATE jogador SET JG_NUMERO_JOGOS = ?, JG_NUMERO_VITORIAS = ?, " +
                    "JG_TEMPO_TOTAL = ? WHERE JG_ID = ?;");

            ps.setInt(1, aJogador.getNumeroJogos());
            ps.setInt(2, aJogador.getTotalVitorias());
            ps.setLong(3, aJogador.getTempoTotalJogo());
            ps.setInt(4, aJogador.getId());

            ps.executeUpdate();

            return true;

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

        return false;
    }

    /**
     * Regista na tabela computador o novo computador que esteve envolvido num novo
     * jogador com um jogador humano. No final da execução do comando insert é obtido
     * o id do computador que foi registado.
     *
     * @return  Return um objeto da classe computador com o id atribuido ao computador
     */
    public Computador registaComputador() {

        try {

            ps = bd.conectarBd().prepareStatement("INSERT INTO computador () " +
                    "VALUES();", Statement.RETURN_GENERATED_KEYS);

            ps.execute();

            int id = -1;

            rs = ps.getGeneratedKeys();

            if (rs.next()) {
                id = rs.getInt(1);
                return new Computador(id);
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
        return null;
    }
}
