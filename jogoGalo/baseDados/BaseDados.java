package pt.ipc.estgoh.jogoGalo.baseDados;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * A classe BaseDados representa a ligação à base de dados
 * criada para dar suporte a esta aplicação.
 *
 * @author José Mauricio
 * @version 0.3
 */
public class BaseDados {

    private Connection conn = null;

    /**
     * Construtor da classe BaseDados por defeito.
     */
    public BaseDados() {
    }

    /**
     * Devolve um objeto da classe Connection.
     * O que permitirá a partir daqui realizar consultas ou modificações de dados
     * das várias tabelas da base de dados.
     *
     * @return Retorna uma nova conecção à base de dados
     */
    public Connection conectarBd() {

        Properties credenciais = new Properties();

        try {

            Class.forName("com.mysql.jdbc.Driver");     //Carrega o Driver de acesso à base de dados

            credenciais.load(new FileInputStream("bin/ficheiros/credenciais.txt"));     //Carrega as credenciais de acesso à bd

            conn = DriverManager.getConnection("jdbc:mysql://" + credenciais.getProperty("Ip") + ":"
                    + credenciais.getProperty("porto") + "/" + credenciais.getProperty("nomeBd") + "?useSSL=false" +
                    "&user=" + credenciais.getProperty("login") + "&password=" + credenciais.getProperty("password"));

        } catch (SQLException sqe) {
            System.out.println("Sql Exception: " + sqe);
        } catch (FileNotFoundException ffe) {
            System.out.println("\nException: " + ffe);
        } catch (IOException ioe) {

            System.out.println("\nExceptio IO: " + ioe);
        } catch (ClassNotFoundException cnfe) {
            System.out.println("Exception: "+cnfe);
        }

        return conn;
    }

    /**
     * Encerra a ligação à base de dados que foi anteriormente estabelecida. Este
     * método deverá ser invocado dentro da clásula finally do cilo try{ } catch.
     */
    public void terminarConeccao() {
        try {

            if (conn != null) {

                try {

                    conn.close();

                } catch (Exception e) {
                    System.out.println("Exception: " + e);
                }
            }

        } catch (Exception e) {
            System.out.println("Execeção: " + e);
        }
    }

}