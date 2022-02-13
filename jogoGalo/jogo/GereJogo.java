package pt.ipc.estgoh.jogoGalo.jogo;

import pt.ipc.estgoh.jogoGalo.baseDados.BaseDados;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * A classe GereJogo representa a estrutura que desempenha a função de estabelecer
 * a ligação à base de dados e manipular os dados acerca do jogo.
 *
 * @author José Mauricio
 * @version 0.3
 */
public class GereJogo {

    private BaseDados bd = new BaseDados();
    private PreparedStatement ps = null;
    private ResultSet rs = null;

    /**
     * Construtor por defeito para a classe GereNotificacoes
     */
    public GereJogo() {}

    /**
     * Regista as informações sobre o jogo que foi terminado. Após um jogo ser terminado
     * as informaçãoes sobre as jogadas efetuadas durante o jogo, os jogadores que estiveram
     * presentes no jogo são guardados na base de dados.
     *
     * @param aJogo Recebe um objeto da classe Jogo com a informações à cerca do jogo
     *
     * @return Retorna true se ação foir efetuada com sucesso, false caso contrário
     */
    public boolean registarJogo(Jogo aJogo) {

        ResultSet rs1 = null;
        PreparedStatement ps1 = null;

        try {

            ps = bd.conectarBd().prepareStatement("INSERT INTO jogo (J_ESTADO, J_TEMPO_DECORRIDO) " +
                    "VALUES (?,?);", Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, aJogo.getEstado());
            ps.setLong(2, aJogo.getTempoDecorrido());

            ps.execute();

            int idJogo = -1;

            rs = ps.getGeneratedKeys(); //Obtém o id do jogo registado na tabela jogo

            if (rs.next()) {
                idJogo = rs.getInt(1);
                registarJogadoresJogo(idJogo, aJogo.getJogador().getId(), aJogo.getBot().getIdComputador());
            }

            if (aJogo.getJogadas() != null && aJogo.getJogadas().size() > 0) {
                for (Jogadas jo : aJogo.getJogadas()) {

                    ps = bd.conectarBd().prepareStatement("INSERT INTO jogadas (JO_COORDENADAX, JO_COORDENADAY, JO_JOGADA, JO_MOMENTO) " +
                            "VALUES (?,?,?,?);", Statement.RETURN_GENERATED_KEYS);

                    ps.setInt(1, jo.getCoordenadaX());
                    ps.setInt(2, jo.getCoordenadaY());
                    ps.setString(3, jo.getCaractereJogada());
                    ps.setLong(4, jo.getMomentoJogada());

                    ps.execute();

                    int idJogada = -1;

                    rs1 = ps.getGeneratedKeys();    //Obtém o id das jogadas que foram efetuadas no jogo

                    while (rs1.next()) {
                        idJogada = rs1.getInt(1);

                        ps1 = bd.conectarBd().prepareStatement("INSERT INTO jogo_jogadas (J_ID, JO_ID) " +
                                "VALUES (?,?);");

                        ps1.setInt(1, idJogo);
                        ps1.setInt(2, idJogada);

                        ps1.execute();
                    }
                }
            }
            return true;

        } catch (SQLException sqe) {
            System.out.println("Exception: "+sqe);
        } finally {

            bd.terminarConeccao();

            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException sqe) {
                    System.out.println("Exception: " + sqe);
                }
            }

            if (ps1 != null) {
                try {
                    ps1.close();
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

            if (rs1 != null) {
                try {
                    rs1.close();
                } catch (SQLException sqe) {
                    System.out.println("Exception: " + sqe);
                }
            }

        }
        return false;
    }

    /**
     * Regista na tabela computador_jogo da base de dados as chaves forasteiras que indicam qual o
     * jogador que esteve presente num determinado jogo. Para isso é recebido por parâmetro o id que
     * foi atribuido aos vários elementos (i.e. jogo, jogador, computador) para no momento da consulta
     * o utilizador possa visualizar os jogadores que estiveram oresentes naquele jogo.
     *
     * @param aIdJogo Recebe um inteiro com o id do jogo realizado
     * @param aIdJogador    Recebe um inteiro com o id do jogador humano que esteve envolvido no jogo
     * @param aIdComputador Recebe um inteiro com o id do computador que esteve a jogar
     */
    public void registarJogadoresJogo(int aIdJogo, int aIdJogador, int aIdComputador) {

        try {

            ps = bd.conectarBd().prepareStatement("INSERT INTO computador_jogo (J_ID, JG_ID, C_ID) " +
                    "VALUES (?, ?, ?);");

            ps.setInt(1, aIdJogo);
            ps.setInt(2, aIdJogador);
            ps.setInt(3, aIdComputador);

            ps.execute();

        } catch (SQLException sqe) {
            System.out.println("Exception: "+sqe);
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
     * Extrai da tabela jogo a informação geral sobre o todos os jogos cujo o estado se
     * apresenta como "Em  Pausa", isto, para que o utilizador que está a interagir com a
     * aplicação possa retomar o jogo que ficou pendente.
     *
     * @return Retorna um ArrayList da classe Jogo com a informação sobre os jogos
     */
    public ArrayList<Jogo> obterJogoPausa() {

        ArrayList<Jogo> retomarJogo = new ArrayList<>();

        try {

            ps = bd.conectarBd().prepareStatement("SELECT J_ID, J_TEMPO_DECORRIDO " +
                    "FROM jogo WHERE J_ESTADO = ? ORDER BY J_ID;");

            ps.setString(1, "Em Pausa");

            rs = ps.executeQuery();

            while(rs.next()) {
                Jogo jogo = new Jogo();
                jogo.setId(rs.getInt(1));
                jogo.setTempoDecorrido(rs.getLong(2));

                retomarJogo.add(jogo);
            }

        } catch (SQLException sqe) {
            System.out.println("Exception: "+sqe);
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
        return retomarJogo;
    }

    /**
     * Extrai da tabela jogo a informação geral sobre o todos os jogos cujo o estado se
     * apresenta como "Concluido", isto, para que o utilizador que está a interagir com a
     * aplicação possa simular um jogo que já tenha sido terminado.
     *
     * @return Retorna um ArrayList da classe Jogo com a informação sobre os jogos
     */
    public ArrayList<Jogo> obterJogosSimulacao() {

        ArrayList<Jogo> listaJogos = new ArrayList<>();

        try {

            ps = bd.conectarBd().prepareStatement("SELECT J_ID, J_TEMPO_DECORRIDO " +
                    "FROM jogo WHERE J_ESTADO = ? ORDER BY J_ID;");

            ps.setString(1, "Concluido");

            rs = ps.executeQuery();

            while(rs.next()) {

                Jogo jogo = new Jogo();
                jogo.setId(rs.getInt(1));
                jogo.setTempoDecorrido(rs.getLong(2));

                listaJogos.add(jogo);
            }

        } catch (SQLException sqe) {
            System.out.println("Exception: "+sqe);
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
        return listaJogos;
    }

    /**
     * Obtém com base no id do jogo recebido por parâmetro as coordenadas da jogada e o caractere
     * que foi aplicado nessa jogada. Este método é útil para que o utilizador possa ver as jogadas que
     * foram realizadas no momento que estiver a simular ou a retomar um jogo. As jogadas são obtidas por
     * a ordem que foram realizadas desde o inicio do jogo, isto, através da ordenadação da listagem por o
     * momento que a mesma foi efetuada de modo ascendente.
     *
     * @param aIdJogo   Recebe um inteiro com o id do jogo que o utilizador escolheu para simular ou retomar
     *
     * @return  Retorna um ArrayList da classe Jogadas com todas as jogadas pertencentes a esse jogo
     */
    public ArrayList<Jogadas> obterConteudoJogoSimulacao(int aIdJogo) {

        ArrayList<Jogadas> listaCoordenadas = new ArrayList<>();

        try {

            ps = bd.conectarBd().prepareStatement("SELECT JO_COORDENADAX, JO_COORDENADAY, JO_JOGADA " +
                    "FROM jogadas jo, jogo_jogadas jj WHERE jo.JO_ID = jj.JO_ID AND J_ID = ? ORDER BY JO_MOMENTO;");

            ps.setInt(1, aIdJogo);
            rs = ps.executeQuery();

            while(rs.next()) {

                listaCoordenadas.add(new Jogadas(rs.getInt(1), rs.getInt(2), rs.getString(3), 0));
            }

        } catch (SQLException sqe) {
            System.out.println("Exception: "+sqe);
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
        return listaCoordenadas;

    }

    /**
     * Atualiza as informações relativamente a um determinado jogo que tenha sido retomado por
     * o jogador. Começa por atualizar a informação sobre o jogo na tabela jogo e depois vai registar
     * todas as novas jogadas que foram realizadas na retomação do jogo.
     *
     * @param aJogo Recebe como parâmetro um onjeto da classe Jogo com as informações sobre o jogo
     *
     * @return Retorna true em case de sucesso na realização da ação, false caso contrário
     */
    public boolean atualizaJogoRetomado(Jogo aJogo) {
        ResultSet rs1 = null;
        PreparedStatement ps1 = null;

        try {

            ps = bd.conectarBd().prepareStatement("UPDATE jogo SET J_ESTADO = ?, J_TEMPO_DECORRIDO = ? WHERE J_ID = ?;");

            ps.setString(1, aJogo.getEstado());
            ps.setLong(2, aJogo.getTempoDecorrido());
            ps.setInt(3, aJogo.getId());

            ps.executeUpdate();

            if (aJogo.getJogadas() != null && aJogo.getJogadas().size() > 0) {
                for (Jogadas jo : aJogo.getJogadas()) {

                    ps = bd.conectarBd().prepareStatement("INSERT INTO jogadas (JO_COORDENADAX, JO_COORDENADAY, JO_JOGADA, JO_MOMENTO) " +
                            "VALUES (?,?,?,?);", Statement.RETURN_GENERATED_KEYS);

                    ps.setInt(1, jo.getCoordenadaX());
                    ps.setInt(2, jo.getCoordenadaY());
                    ps.setString(3, jo.getCaractereJogada());
                    ps.setLong(4, jo.getMomentoJogada());

                    ps.execute();

                    int idJogada = -1;

                    rs1 = ps.getGeneratedKeys();

                    if (rs1.next()) {
                        idJogada = rs1.getInt(1);

                        ps1 = bd.conectarBd().prepareStatement("INSERT INTO jogo_jogadas (J_ID, JO_ID) " +
                                "VALUES (?,?);");

                        ps1.setInt(1, aJogo.getId());
                        ps1.setInt(2, idJogada);

                        ps1.execute();
                    }
                }
            }
            return true;

        } catch (SQLException sqe) {
            System.out.println("Exception: "+sqe);
        } finally {

            bd.terminarConeccao();

            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException sqe) {
                    System.out.println("Exception: " + sqe);
                }
            }

            if (ps1 != null) {
                try {
                    ps1.close();
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

            if (rs1 != null) {
                try {
                    rs1.close();
                } catch (SQLException sqe) {
                    System.out.println("Exception: " + sqe);
                }
            }

        }
        return false;
    }

}
