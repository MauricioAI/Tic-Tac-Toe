package pt.ipc.estgoh.jogoGalo.principal;

import pt.ipc.estgoh.jogoGalo.atividade.Atividade;
import pt.ipc.estgoh.jogoGalo.atividade.GereAtividade;
import pt.ipc.estgoh.jogoGalo.atividade.GereInformacaoGenerica;
import pt.ipc.estgoh.jogoGalo.atividade.InformacaoGenerica;
import pt.ipc.estgoh.jogoGalo.jogadores.Computador;
import pt.ipc.estgoh.jogoGalo.jogadores.GereJogador;
import pt.ipc.estgoh.jogoGalo.jogadores.Jogador;
import pt.ipc.estgoh.jogoGalo.jogo.GereJogo;
import pt.ipc.estgoh.jogoGalo.jogo.Jogadas;
import pt.ipc.estgoh.jogoGalo.jogo.Jogo;
import pt.ipc.estgoh.jogoGalo.jogo.Tabuleiro;
import pt.ipc.estgoh.jogoGalo.leituraDados.LeituraDados;
import pt.ipc.estgoh.jogoGalo.notificacoes.GereNotificacoes;
import pt.ipc.estgoh.jogoGalo.notificacoes.Notificacoes;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.*;
import java.util.*;

/**
 * A classe Interface vai estabelecer uma interação com o utilizador em que após
 * a execução do programa serão apresentadas mesangens ao utilziador a orientá-lo
 * para a utilização da aplicação. Apresenta também os menus aos utilizadores para realizar
 * novos jogos ou para fazer as consultas que desejarem.
 *
 * @author José Mauricio
 * @version 2.2
 */
public class Interface {

    private static LeituraDados lerDados = new LeituraDados();
    private static GereJogador gereJogador = new GereJogador();
    private static GereAtividade gereAtividade = new GereAtividade();
    private static GereNotificacoes gereNotificacoes = new GereNotificacoes();
    private static GereJogo gereJogo = new GereJogo();
    private static Socket socket;
    private static DataInputStream inputStream;
    private static DataOutputStream outputStream;
    private static ArrayList<Jogadas> logJogadas;
    private static ArrayList<Long> tempoTotalJogadas;
    private static int numeroExecucoes;
    private static String nickname;
    private static int idJogador;
    private static Date dataInicioExecucao;

    /**
     * Permite que o programa seja executado de modo a que o utilizador consiga
     * interagir com o mesmo.
     *
     * @param args Recebe os valores introduzidos na linha de comandos
     */
    public static void main(String[] args) {

        dataInicioExecucao = new Date();

        GereInformacaoGenerica gereInformacaoGenerica = new GereInformacaoGenerica();

        File file = new File("bin/ficheiros/credenciais.txt");

        try {
            if (file.exists()) {

                InformacaoGenerica informacaoGenerica = gereInformacaoGenerica.visualizarInformacaoGenerica();

                if (informacaoGenerica.getNumeroExecucoes() == 0) {
                    System.out.println("\n == Esta aplicação foi executada 0 vezes até ao momento! ==");
                } else {
                    System.out.println("\n== " + informacaoGenerica.toString() + " ==");
                    numeroExecucoes = informacaoGenerica.getNumeroExecucoes();
                }

                numeroExecucoes++;

                gereInformacaoGenerica.registarInformacaoGenerica(new InformacaoGenerica(numeroExecucoes, new Date()));

                menuBd(file);

            } else {

                System.out.println("!! O ficheiro com as credenciais de acesso à base de dados não existe, deve criar um novo !!\n");


                registarCredenciaisBd(file);    //Regista as novas credenciais de acesso à bd

                registarNickname();     //Regista o nickaname do jogador

                Jogador jogadorLocal = new Jogador(nickname, 0, 0, 0);
                jogadorLocal.setId(idJogador);

                System.out.println("\nBem-vindo [" + nickname + "]");

                int opc;
                do {
                    opc = menuPrincipal();
                    executarMenuPrincipal(opc, jogadorLocal);
                } while ((opc > 0) || (opc <= 9));

            }
        } catch (IOException ioe) {
            System.out.println("IOException: " + ioe);
        }

    }

    /**
     * Apresenta o menu principal da aplicação depois de a aplicação ter sido executada e
     * depois de o utilizador ter indicado se pretende registar ou não um nickname. Depois de ter escolhido
     * uma das opções do menu, esse valor é guardado numa variável do tipo inteiro.
     * Que no final da execução do método será devolvido.
     *
     * @return Retorna um inteiro com a opção escolhida por o utilizador
     */
    public static int menuPrincipal() {

        int opcao;

        System.out.println("\n*******************************************");
        System.out.println("* 1 - Jogar contra o computador           *");
        System.out.println("* 2 - Jogador vs Jogador                  *");
        System.out.println("* 3 - Jogar contra jogador virtual        *");
        System.out.println("* 4 - Retomar jogo em pausa               *");
        System.out.println("* 5 - Simular jogo                        *");
        System.out.println("* 6 - Consultar notificações              *");
        System.out.println("* 7 - Listagens e Pesquisas               *");
        System.out.println("* 8 - Visualizar o registo de atividade   *");
        System.out.println("* 9 - Sair                                *");
        System.out.println("*******************************************");
        System.out.println("Seleccione uma opcao: ");

        opcao = lerDados.lerInteiros();

        return opcao;
    }

    /**
     * Executa o menu principal que foi apresentado ao utilizador quando
     * a aplicação é executada. Após receber a opção escolhida por o utilizador
     * é verificada a tarefa associada àquela opção através de um ciclo switch.
     *
     * @param aOpcao Recebe um inteiro com a opção escolhida por o utilizador
     * @param aJogador Recebe um objeto da classe Jogador com as informações sobre o jogador que está
     *                 a executar a aplicação
     */
    public static void executarMenuPrincipal(int aOpcao, Jogador aJogador) {
        //Notifica o jogador que ultrapassou múltiplos de 10 vitórias
        gereNotificacoes.registarNotificacoesVitorias(nickname, idJogador);
        gereNotificacoes.registarNotificacaoVitorioso(nickname);  //Notifica o jogador que tem mais vitórias

        switch (aOpcao) {
            case 1:

                int player = 0;
                logJogadas = new ArrayList<>();
                tempoTotalJogadas = new ArrayList<>();
                Date inicioJogo;
                Computador computer = gereJogador.registaComputador();

                Jogo jogo = new Jogo(new Tabuleiro(new char[3][3]), aJogador, computer);
                jogo.getTabuleiro().mostraTabuleiro();

                int numjogadasJogador = 0;
                inicioJogo = new Date();
                long fimJogada;

                //Inicia um novo jogo contra o computador
                while (true) {

                    if (player % 2 == 0) {

                        System.out.println("\n!!  Agora é a vez do jogador " + nickname + " jogar  !!\n");

                        System.out.println("###  Pretende: (A) Pedir Ajuda; (D) Desistir; (C) Continuar;  ###");
                        String estado = lerDados.lerStrings();

                        if (estado.equalsIgnoreCase("A")) {
                            dica(jogo);

                            long inicioJogada = new Date().getTime();   //Quando inicia a jogada

                            int[] coordenadas = realizarJogada(jogo);

                            if (jogo.getTabuleiro().verificaPosicao(coordenadas)) {
                                jogo.getTabuleiro().atualizarTabuleiro('X', coordenadas);
                                player++;
                                numjogadasJogador++;
                                fimJogada = new Date().getTime();

                                registarJogadas(fimJogada, coordenadas, "X");

                                jogo.getTabuleiro().mostraTabuleiro();

                                long duracaojogada = fimJogada - inicioJogada;  //Registar a duração da jogada
                                mostrarInformacaoJogada(duracaojogada, numjogadasJogador);
                                int vencedor = vencedorJogoComputador(jogo, inicioJogo, logJogadas);

                                //Se houver vencedor ou empate termina o jogo
                                if (vencedor == -1 || vencedor == 1 || vencedor == -2)
                                    break;

                            } else {
                                numjogadasJogador++;
                                System.out.println("\n??  Coordenadas erradas. Tente novamente.  ??");
                            }
                        } else if (estado.equalsIgnoreCase("D")) {

                            long fimTemporario = new Date().getTime();
                            long tempoDecorrido = fimTemporario - inicioJogo.getTime();

                            aJogador.setNumeroJogos();
                            aJogador.setTempoTotalJogo(tempoDecorrido);

                            if (!nickname.equalsIgnoreCase("Anónimo")) {

                                jogo.setJogadas(logJogadas);
                                jogo.setEstado("Em Pausa");
                                jogo.setTempoDecorrido(tempoDecorrido);

                                if (gereJogo.registarJogo(jogo) && gereJogador.registarEstatistica(aJogador))
                                    System.out.println("!!  Você desistiu do jogo, mas pode retomar mais tarde  !!");

                            } else
                                System.out.println("??  Lamentamos, que tenha desistido do jogo  ??\n");

                            mostrarInformacaoJogo(aJogador);
                            break;
                        } else if (estado.equalsIgnoreCase("C")) {

                            long inicioJogada = new Date().getTime();   //Quando inicia a jogada

                            int[] coordenadas = realizarJogada(jogo);

                            if (jogo.getTabuleiro().verificaPosicao(coordenadas)) {
                                jogo.getTabuleiro().atualizarTabuleiro('X', coordenadas);
                                player++;
                                numjogadasJogador++;
                                fimJogada = new Date().getTime();

                                registarJogadas(fimJogada, coordenadas, "X");

                                jogo.getTabuleiro().mostraTabuleiro();

                                long duracaojogada = fimJogada - inicioJogada;  //Registar a duração da jogada
                                mostrarInformacaoJogada(duracaojogada, numjogadasJogador);
                                int vencedor = vencedorJogoComputador(jogo, inicioJogo, logJogadas);

                                //Se houver vencedor ou empate termina o jogo
                                if (vencedor == -1 || vencedor == 1 || vencedor == -2)
                                    break;

                            } else {
                                numjogadasJogador++;
                                System.out.println("\n??  Coordenadas erradas. Tente novamente.  ??");
                            }
                        }

                    } else {

                        int[] move = computer.jogar();

                        System.out.println("\n!!  Agora é a vez do computador jogar  !!\n");

                        if (jogo.getTabuleiro().verificaPosicao(move)) {
                            jogo.getTabuleiro().atualizarTabuleiro('O', move);
                            player++;

                            registarJogadas(new Date().getTime(), move, "O");    //Regista-se as jogadas do computador

                            jogo.getTabuleiro().mostraTabuleiro();
                            int vencedor = vencedorJogoComputador(jogo, inicioJogo, logJogadas);

                            //Se houver vencedor ou empate termina o jogo
                            if (vencedor == -1 || vencedor == 1 || vencedor == -2)
                                break;

                        } else {
                            System.out.println("\n??  Coordenadas erradas. Tente novamente.  ??");
                        }
                    }
                }

                gereAtividade.registarAtividade(new Atividade("Efetuou um novo jogo contra o computador.",
                        new Date(), new Date(), aJogador));
                break;
            case 2:
                Jogador jogador2;
                String nicknameJogador2;

                System.out.println("\nPretende registar um nickname para jogar?(s/n) ");
                String verificacao = lerDados.lerStrings();

                if (verificacao.equalsIgnoreCase("s")) {

                    System.out.println("\nIntroduza o nickname do adversário: ");
                    nicknameJogador2 = lerDados.lerStrings();

                    jogador2 = new Jogador(nicknameJogador2, 0, 0, 0);

                    if (gereJogador.registarJogador(jogador2) != 0)
                        System.out.println("!!  Adversário registado com sucesso  !!");
                } else {
                    nicknameJogador2 = "Anónimo";
                    jogador2 = new Jogador(nicknameJogador2, 0, 0, 0);
                }

                int vezJogador = 0;
                Date inicioJogoLocal = new Date();
                int jogadaJogador1 = 0, jogadaJogador2 = 0;
                long fimJogada1 = 0;
                tempoTotalJogadas = new ArrayList<>();

                Jogo jogoLocal = new Jogo(new Tabuleiro(new char[3][3]), aJogador, jogador2);
                jogoLocal.getTabuleiro().mostraTabuleiro();

                while (true) {

                    if (vezJogador % 2 == 0) {
                        System.out.println("\n!!  Agora é a vez do jogador " + nickname + " jogar  !!\n");

                        System.out.println("###  Pretende: (A) Pedir Ajuda; (D) Desistir; (C) Continuar;  ###");
                        String estado = lerDados.lerStrings();

                        if (estado.equalsIgnoreCase("A")) {
                            dica(jogoLocal);

                            long inicioJogada = new Date().getTime();   //Quando inicia a jogada

                            int[] coordenadas = realizarJogada(jogoLocal);

                            if (jogoLocal.getTabuleiro().verificaPosicao(coordenadas)) {
                                jogoLocal.getTabuleiro().atualizarTabuleiro('X', coordenadas);
                                vezJogador++;
                                jogadaJogador1++;
                                fimJogada1 = new Date().getTime();

                                long duracaojogada = fimJogada1 - inicioJogada;  //Registar a duração da jogada
                                mostrarInformacaoJogada(duracaojogada, jogadaJogador1);
                                jogoLocal.getTabuleiro().mostraTabuleiro();

                                int verifica = vencedorJogoLocal(jogoLocal, inicioJogoLocal);

                                //Se houver vencedor ou empate termina o jogo
                                if (verifica == -1 || verifica == 1 || verifica == -2)
                                    break;

                            } else {
                                System.out.println("\n??  Coordenadas erradas. Tente novamente.  ??");
                                jogadaJogador1++;
                            }

                        } else if (estado.equalsIgnoreCase("D")) {

                            long fimTemporario = new Date().getTime();
                            long tempoDecorrido = fimTemporario - inicioJogoLocal.getTime();

                            aJogador.setNumeroJogos();
                            aJogador.setTempoTotalJogo(tempoDecorrido);

                            jogador2.setTempoTotalJogo(tempoDecorrido);
                            jogador2.setNumeroJogos();

                            if (!nickname.equalsIgnoreCase("Anónimo") && !nicknameJogador2.equalsIgnoreCase("Anónimo")) {
                                if (gereJogador.registarEstatistica(aJogador) && gereJogador.registarEstatistica(jogador2))
                                    System.out.println("!!  Informação estatística guardada com sucesso  !!");
                            }

                            System.out.println("!!  Informação estatística do jogador " + aJogador.getNickname() + "  !!\n");
                            mostrarInformacaoJogo(aJogador);

                            System.out.println("!!  Informação estatística do jogador " + jogador2.getNickname() + "  !!\n");
                            mostrarInformacaoJogo(jogador2);
                            break;
                        } else {

                            long inicioJogada = new Date().getTime();   //Quando inicia a jogada

                            int[] coordenadas = realizarJogada(jogoLocal);

                            if (jogoLocal.getTabuleiro().verificaPosicao(coordenadas)) {
                                jogoLocal.getTabuleiro().atualizarTabuleiro('X', coordenadas);
                                vezJogador++;
                                jogadaJogador1++;
                                fimJogada1 = new Date().getTime();

                                long duracaojogada = fimJogada1 - inicioJogada;  //Registar a duração da jogada
                                mostrarInformacaoJogada(duracaojogada, jogadaJogador1);

                                jogoLocal.getTabuleiro().mostraTabuleiro();
                                int verifica = vencedorJogoLocal(jogoLocal, inicioJogoLocal);

                                //Se houver vencedor ou empate termina o jogo
                                if (verifica == -1 || verifica == 1 || verifica == -2)
                                    break;

                            } else {
                                System.out.println("\n??  Coordenadas erradas. Tente novamente. ??");
                                jogadaJogador1++;
                            }
                        }
                    } else {
                        //Adversário
                        System.out.println("\n!!  Agora é a vez do jogador " + nicknameJogador2 + " jogar  !!\n");

                        System.out.println("###  Pretende: (A) Pedir Ajuda; (D) Desistir; (C) Continuar;  ###");
                        String estado = lerDados.lerStrings();

                        if (estado.equalsIgnoreCase("A")) {
                            dica(jogoLocal);

                            long inicioJogada = new Date().getTime();   //Quando inicia a jogada

                            int[] coordenadas = realizarJogada(jogoLocal);

                            if (jogoLocal.getTabuleiro().verificaPosicao(coordenadas)) {
                                jogoLocal.getTabuleiro().atualizarTabuleiro('O', coordenadas);
                                vezJogador++;
                                jogadaJogador2++;
                                fimJogada1 = new Date().getTime();

                                long duracaojogada = fimJogada1 - inicioJogada;  //Registar a duração da jogada
                                mostrarInformacaoJogada(duracaojogada, jogadaJogador2);

                                jogoLocal.getTabuleiro().mostraTabuleiro();
                                int verifica = vencedorJogoLocal(jogoLocal, inicioJogoLocal);

                                //Se houver vencedor ou empate termina o jogo
                                if (verifica == -1 || verifica == 1 || verifica == -2)
                                    break;

                            } else
                                System.out.println("\n??  Coordenadas erradas. Tente novamente.  ??");

                        } else if (estado.equalsIgnoreCase("D")) {

                            long fimTemporario = new Date().getTime();
                            long tempoDecorrido = fimTemporario - inicioJogoLocal.getTime();

                            aJogador.setNumeroJogos();
                            aJogador.setTempoTotalJogo(tempoDecorrido);

                            jogador2.setTempoTotalJogo(tempoDecorrido);
                            jogador2.setNumeroJogos();

                            if (!nickname.equalsIgnoreCase("Anónimo") && !nicknameJogador2.equalsIgnoreCase("Anónimo")) {
                                if (gereJogador.registarEstatistica(aJogador) && gereJogador.registarEstatistica(jogador2))
                                    System.out.println("!!  Informação estatística guardada com sucesso  !!");
                            }

                            System.out.println("!!  Informação estatística do jogador " + jogador2.getNickname() + "  !!\n");
                            mostrarInformacaoJogo(jogador2);

                            System.out.println("!!  Informação estatística do jogador " + aJogador.getNickname() + "  !!\n");
                            mostrarInformacaoJogo(aJogador);
                            break;
                        } else {

                            long inicioJogada = new Date().getTime();   //Quando inicia a jogada

                            int[] coordenadas = realizarJogada(jogoLocal);

                            if (jogoLocal.getTabuleiro().verificaPosicao(coordenadas)) {
                                jogoLocal.getTabuleiro().atualizarTabuleiro('O', coordenadas);
                                vezJogador++;
                                jogadaJogador2++;
                                fimJogada1 = new Date().getTime();

                                long duracaojogada = fimJogada1 - inicioJogada;  //Registar a duração da jogada
                                mostrarInformacaoJogada(duracaojogada, jogadaJogador2);

                                jogoLocal.getTabuleiro().mostraTabuleiro();
                                int verifica = vencedorJogoLocal(jogoLocal, inicioJogoLocal);

                                //Se houver vencedor ou empate termina o jogo
                                if (verifica == -1 || verifica == 1 || verifica == -2)
                                    break;

                            } else
                                System.out.println("\n??  Coordenadas erradas. Tente novamente.  ??");

                        }
                    }
                }

                gereAtividade.registarAtividade(new Atividade("Efetuou um novo jogo contra um novo jogador local.",
                        new Date(), new Date(), aJogador));
                break;
            case 3:
                String enderecoIp;
                int porto;
                tempoTotalJogadas = new ArrayList<>();

                System.out.println("\nPretende ser 'servidor' ou 'cliente'? ");

                switch (lerDados.lerStrings()) {
                    case "servidor":
                        gereAtividade.registarAtividade(new Atividade("Iniciou um novo jogo como servidor.",
                                new Date(), new Date(), aJogador));

                        System.out.println("\nDeve introduzir o porto no qual aceitará uma nova ligação: ");
                        porto = lerDados.lerInteiros();

                        aceitaPedidoLigacoes(porto);
                        break;
                    case "cliente":
                        gereAtividade.registarAtividade(new Atividade("Iniciou um novo jogo como cliente.",
                                new Date(), new Date(), aJogador));

                        System.out.println("Introduza o endereço de IP do servidor: ");
                        enderecoIp = lerDados.lerStrings();

                        System.out.println("\nDeve introduzir o porto do servidor: ");
                        porto = lerDados.lerInteiros();

                        iniciarLigacao(enderecoIp, porto);
                        break;
                    default:
                        System.out.println("?? Não existe nenhum modo de utilizador igual a esse ??\n");
                        break;
                }
                break;
            case 4:
                ArrayList<Jogo> retomarJogo = gereJogo.obterJogoPausa();

                if (retomarJogo == null)
                    System.out.println("\n??  Ainda não existem jogos em pausa para retomar  ??");

                else {

                    int registos = 1;

                    System.out.println("\n+-------------------------------------------------------+");
                    System.out.println("+   #   |   ID do jogo      |   Tempo Total de Jogo     +");
                    System.out.println("+-------------------------------------------------------+");

                    //À medida que vai iterando o ArrayList com os jogos em Pausa vai mostrar uma tabela
                    //Com todos os registos e cada registo terá uma referência na tabela
                    for (int i = 0; i < retomarJogo.size(); i++) {
                        registos++;
                        System.out.println("|   " + i + "   " + retomarJogo.get(i).toString());

                        if (registos % 10 == 0) {
                            System.out.println("\nDeseja continuar a ver (s/n)? ");
                            String continuar = Interface.lerDados.lerStrings();

                            //verificar continuaçao da listagem
                            if (!continuar.equalsIgnoreCase("s"))
                                break;
                        }
                    }

                    System.out.println("\nSelecione o jogo que pretende retomar: ");
                    int refJogo = lerDados.lerInteiros();

                    Jogo jogoRetomado = retomarJogo.get(refJogo);

                    ArrayList<Jogadas> jogadasJogo = gereJogo.obterConteudoJogoSimulacao(jogoRetomado.getId());

                    Tabuleiro tab = new Tabuleiro(new char[3][3]);
                    int proximo = 0;

                    for (Jogadas jogadas : jogadasJogo) {
                        int[] coordenadas = {jogadas.getCoordenadaX(), jogadas.getCoordenadaY()};

                        if (tab.verificaPosicao(coordenadas)) {
                            tab.atualizarTabuleiro(jogadas.getCaractereJogada().charAt(0), coordenadas);
                            tab.mostraTabuleiro();
                            proximo++;  //Incrementa um variável do tipo inteiro para determinar a vez do próximo jogador
                        }
                    }

                    jogoRetomado.setTabuleiro(tab);
                    jogoRetomado.setJogador(aJogador);

                    retomarJogoPausa(jogoRetomado, proximo);
                }

                gereAtividade.registarAtividade(new Atividade("Retomou um jogo que estava em pausa.",
                        new Date(), new Date(), aJogador));
                break;
            case 5:
                //Simular o jogo já terminado

                ArrayList<Jogo> jogosSimulacao = gereJogo.obterJogosSimulacao();

                if (jogosSimulacao == null)
                    System.out.println("??  Ainda não existem jogos para simulação  ??");
                else {

                    int registos = 1;

                    System.out.println("\n+-----------------------------------------------+");
                    System.out.println("+   ID do jogo      |   Tempo Total de Jogo     +");
                    System.out.println("+-----------------------------------------------+");

                    for (Jogo jogos : jogosSimulacao) {
                        registos++;
                        System.out.println(jogos.toString());

                        if (registos % 10 == 0) {
                            System.out.println("\nDeseja continuar a ver (s/n)? ");
                            String continuar = Interface.lerDados.lerStrings();

                            //verificar continuaçao da listagem
                            if (!continuar.equalsIgnoreCase("s"))
                                break;
                        }
                    }

                    System.out.println("\nSelecione o jogo que pretende simular: ");
                    int idJogo = lerDados.lerInteiros();

                    ArrayList<Jogadas> jogadasJogo = gereJogo.obterConteudoJogoSimulacao(idJogo);

                    Tabuleiro tab = new Tabuleiro(new char[3][3]);

                    for (Jogadas jogadas : jogadasJogo) {
                        int[] coordenadas = {jogadas.getCoordenadaX(), jogadas.getCoordenadaY()};

                        if (tab.verificaPosicao(coordenadas)) {
                            tab.atualizarTabuleiro(jogadas.getCaractereJogada().charAt(0), coordenadas);
                            tab.mostraTabuleiro();
                            System.out.println("\nPrima um tecla qualquer para continuar...");
                            lerDados.lerStrings();
                        }

                    }

                    System.out.println("!!  Simulação do jogo terminada  !!\n");
                }

                gereAtividade.registarAtividade(new Atividade("Simulou um jogo já concluido.",
                        new Date(), new Date(), aJogador));
                break;
            case 6:

                ArrayList<Notificacoes> listaNotificacoes = gereNotificacoes.mostrarNoficacoes(aJogador.getId());
                listarNotificacoes(listaNotificacoes);

                gereNotificacoes.atualizarEstadoNotificacao(aJogador.getId());

                gereAtividade.registarAtividade(new Atividade("Consultou as suas notificações.",
                        new Date(), new Date(), aJogador));
                break;
            case 7:
                int opc;

                //Encaminha o utilizador para o submenu da aplicação para realizar as devidas consultas
                do {
                    opc = submenuListagens();
                    executaSubmenuListagens(opc, aJogador);

                } while ((opc > 0) || (opc <= 7));

                break;
            case 8:
                listarLogPrivado();
                gereAtividade.registarAtividade(new Atividade("Listou o registo de atividade da aplicação.",
                        new Date(), new Date(), aJogador));
                break;
            case 9:
                System.out.println("\nAdeus [" + nickname + "]");
                execucaoAplicacao();    //Apresenta a informação sobre a duração que a aplicação esteve em execução

                gereAtividade.registarAtividade(new Atividade("Terminou a sessão na aplicação.",
                        new Date(), new Date(), aJogador));
                System.exit(0);
                break;
            default:
                System.out.println("Opcao introduzida erradamente.");
                break;
        }

    }

    /**
     * Retoma o jogo de Tic-Tac-Toe que foi escolhido por o utilizador da aplicação. Depois
     * de o utilizador ter visualizado todos os jogos em pausa, deve escolher o jogo que pertende
     * retomar.
     *
     * @param aJogoRetomado Recebe como parâmetro um objeto da classe Jogo com a referência ao jogo escolhido
     * @param aPlayer Recebe um inteiro com a indicação do próximo jogador a jogar com base na sequência de
     *                jogadas.
     */
    public static void retomarJogoPausa(Jogo aJogoRetomado, int aPlayer) {

        Computador computer = new Computador();
        tempoTotalJogadas = new ArrayList<>();
        logJogadas = new ArrayList<>();

        aJogoRetomado.setBot(computer);     //Regista um novo computador para realizar novamente o jogo
        aJogoRetomado.getTabuleiro().mostraTabuleiro();

        Date inicioJogo = new Date();
        int numjogadasJogador = 0;
        long fimJogada;

        while (true) {

            if (aPlayer % 2 == 0) {

                System.out.println("\n!!  Agora é a vez do jogador " + nickname + " jogar  !!\n");

                System.out.println("###  Pretende: (A) Pedir Ajuda; (D) Desistir; (C) Continuar;  ###");
                String estado = lerDados.lerStrings();

                if (estado.equalsIgnoreCase("A")) {
                    dica(aJogoRetomado);

                    long inicioJogada = new Date().getTime();   //Quando inicia a jogada

                    int[] coordenadas = realizarJogada(aJogoRetomado);

                    if (aJogoRetomado.getTabuleiro().verificaPosicao(coordenadas)) {
                        aJogoRetomado.getTabuleiro().atualizarTabuleiro('X', coordenadas);
                        aPlayer++;
                        numjogadasJogador++;
                        fimJogada = new Date().getTime();

                        registarJogadas(fimJogada, coordenadas, "X");

                        long duracaojogada = fimJogada - inicioJogada;  //Registar a duração da jogada
                        mostrarInformacaoJogada(duracaojogada, numjogadasJogador);

                        aJogoRetomado.getTabuleiro().mostraTabuleiro();
                        int verificar = vencedorJogoRetomado(aJogoRetomado, inicioJogo);

                        //Se houver vencedor ou empate termina o jogo
                        if (verificar == -1 || verificar == 1 || verificar == -2)
                            break;

                    } else {
                        numjogadasJogador++;
                        System.out.println("\n??  Coordenadas erradas. Tente novamente.  ??");
                    }
                } else if (estado.equalsIgnoreCase("D")) {

                    long fimTemporario = new Date().getTime();
                    long tempoDecorrido = fimTemporario - inicioJogo.getTime();

                    aJogoRetomado.getJogador().setNumeroJogos();
                    aJogoRetomado.getJogador().setTempoTotalJogo(tempoDecorrido);

                    if (!nickname.equalsIgnoreCase("Anónimo")) {

                        aJogoRetomado.setJogadas(logJogadas);
                        aJogoRetomado.setEstado("Em Pausa");
                        aJogoRetomado.setTempoDecorrido(tempoDecorrido);

                        if (gereJogo.atualizaJogoRetomado(aJogoRetomado) && gereJogador.registarEstatistica(aJogoRetomado.getJogador()))
                            System.out.println("!!  Você desistiu do jogo, mas pode retomar mais tarde  !!");

                    } else
                        System.out.println("??  Lamentamos, que tenha desistido do jogo  ??\n");

                    mostrarInformacaoJogo(aJogoRetomado.getJogador());
                    break;
                } else {

                    long inicioJogada = new Date().getTime();   //Quando inicia a jogada

                    int[] coordenadas = realizarJogada(aJogoRetomado);

                    if (aJogoRetomado.getTabuleiro().verificaPosicao(coordenadas)) {
                        aJogoRetomado.getTabuleiro().atualizarTabuleiro('X', coordenadas);
                        aPlayer++;
                        numjogadasJogador++;
                        fimJogada = new Date().getTime();

                        registarJogadas(fimJogada, coordenadas, "X");

                        long duracaojogada = fimJogada - inicioJogada;  //Registar a duração da jogada
                        mostrarInformacaoJogada(duracaojogada, numjogadasJogador);

                        aJogoRetomado.getTabuleiro().mostraTabuleiro();
                        int verificar = vencedorJogoRetomado(aJogoRetomado, inicioJogo);

                        //Se houver vencedor ou empate termina o jogo
                        if (verificar == -1 || verificar == 1 || verificar == -2)
                            break;

                    } else {
                        numjogadasJogador++;
                        System.out.println("\n??  Coordenadas erradas. Tente novamente.  ??");
                    }
                }

            } else {

                int[] move = computer.jogar();

                System.out.println("\n!!  Agora é a vez do computador jogar  !!\n");

                if (aJogoRetomado.getTabuleiro().verificaPosicao(move)) {
                    aJogoRetomado.getTabuleiro().atualizarTabuleiro('O', move);
                    aPlayer++;

                    registarJogadas(new Date().getTime(), move, "O");    //Regista-se as jogadas do computador

                    aJogoRetomado.getTabuleiro().mostraTabuleiro();
                    int verificar = vencedorJogoRetomado(aJogoRetomado, inicioJogo);

                    //Se houver vencedor ou empate termina o jogo
                    if (verificar == -1 || verificar == 1 || verificar == -2)
                        break;

                } else {
                    System.out.println("\n??  Coordenadas erradas. Tente novamente.  ??");
                }
            }
        }

    }

    /**
     * Apresenta a informação referente à duração que o jgador dispõe para cada jogada que
     * é realizada, ou seja, após receber a duração da jogada o mesmo valor é guardado no
     * ArrayList do tipo Long para mais tarde poder mostrar ao jogador qual é a média das
     * jogadas e o número total de jogadas que realizou até ao momento.
     *
     * @param aDuracaoJogada Recebe um long com o valor referente à duração da jogada atual
     * @param aNumeroTotalJogadas Recebe um inteiro com o número de jogadas realizadas
     */
    public static void mostrarInformacaoJogada(long aDuracaoJogada, int aNumeroTotalJogadas) {

        long tempoTotal = 0, tempoMedio = 0;
        NumberFormat format = new DecimalFormat("##");

        //Regista no ArrayList todas as durações de cada uma das jogadas
        if (tempoTotalJogadas != null) {
            tempoTotalJogadas.add(aDuracaoJogada);
        }

        if (tempoTotalJogadas != null && tempoTotalJogadas.size() > 0) {
            for (Long duracao : tempoTotalJogadas) {
                tempoTotal += duracao.longValue();  //Obtém o total desse valores armazenados no ArrayList
            }
        }

        tempoMedio = tempoTotal / tempoTotalJogadas.size();     //Calcula a média da duração das jogadas

        //Formata a duração da jogada que foi efetuada no preciso momento
        String duracaoMili = format.format(aDuracaoJogada / 1000);
        String duracaoSeg = format.format(aDuracaoJogada / (60 * 1000));
        String duracaoMin = format.format(aDuracaoJogada / (60 * 60 * 1000));

        //Formata o valor relativamente ao total gasto em todas as jogadas
        String tempoTotalMili = format.format(tempoTotal / 1000);
        String tempoTotalSeg = format.format(tempoTotal / (60 * 1000));
        String tempoTotalMin = format.format(tempoTotal / (60 * 60 * 1000));

        //Formata o valor referente à media da duração das jogadas
        String tempoMedioMili = format.format(tempoMedio / 1000);
        String tempoMedioSeg = format.format(tempoMedio / (60 * 1000));
        String tempoMedioMin = format.format(tempoMedio / (60 * 60 * 1000));


        System.out.println("\n#=============================================================#");
        System.out.println("#Esta jogada demorou: " + duracaoMili + " segundos; " + duracaoSeg + " minutos; " + duracaoMin + " horas;         #");
        System.out.println("#Em média demora a jogar: " + tempoMedioMili + " segundos; " + tempoMedioSeg + " minutos; " + tempoMedioMin + " horas;     #");
        System.out.println("#O tempo total das jogadas é: " + tempoTotalMili + " segundos; " + tempoTotalSeg + " minutos; " + tempoTotalMin + " horas; #");
        System.out.println("#Até ao momento efetuou um total de: " + aNumeroTotalJogadas + " jogadas.               #");
        System.out.println("#=============================================================#");
    }

    /**
     * Apresenta no final de cada jogo a informação estatística referente ao jogador que está
     * a executar a aplicação. Ou seja, depois de receber por parâmetro o objeto referente ao jogador
     * será recalculado novamento qual o número de vitórias que o jogador conseguiu, o total de jogos que
     * realizou até ao momento e o tempo total que gastou a jogar na aplicação.
     *
     * @param aJogador Recebe um objeto da classe com a informação referente ao jogador
     */
    public static void mostrarInformacaoJogo(Jogador aJogador) {

        NumberFormat format = new DecimalFormat("##");

        //Formata a duração do jogo em segundos, minutos e horas
        String duracaoMili = format.format(aJogador.getTempoTotalJogo() / 1000);
        String duracaoSeg = format.format(aJogador.getTempoTotalJogo() / (60 * 1000));
        String duracaoMin = format.format(aJogador.getTempoTotalJogo() / (60 * 60 * 1000));

        System.out.println("\n#=======================================================================#");
        System.out.println("#Até ao momento efetuou: " + aJogador.getNumeroJogos() + " jogos;                                       #");
        System.out.println("#Tendo obtido: " + aJogador.getTotalVitorias() + " vitórias;                                              #");
        System.out.println("#No total de jogos realizados consumiu: " + duracaoMili + " segundos; " + duracaoSeg + " minutos, " + duracaoMin + " horas; #");
        System.out.println("#=======================================================================#");

    }

    /**
     * Devolve um array unidimensional com as coordenadas introduzidas por o jogador na aplicação.
     * Depois de terem sido formatadas por invocação do método jogada do objeto Jogo.
     *
     * @param aJogo Recebe um objeto da classe Jogo
     *
     * @return Retorna um array de inteiros com as coordenadas formatas para serem aplicadas no tabuleiro
     */
    public static int[] realizarJogada(Jogo aJogo) {
        int[] jogada;

        System.out.println("Introduza a coordenada x: ");
        int x = lerDados.lerInteiros();

        System.out.println("Introduza a coordenada y: ");
        int y = lerDados.lerInteiros();

        jogada = aJogo.jogada(x, y);

        return jogada;
    }

    /**
     * Armazena todas as jogadas que forem realizadas ao longo do decorrer do jogo num ArrayList
     * declarado de forma global. Para posteriormente ser redirecionado e armazenado na base de
     * dados.
     *
     * @param aMomentoJogada Recebe um long com  o valor temporal em que a jogada foi executada
     * @param aJogadas Recebe como parâmetro um array de inteiros com as coordenadas da jogada
     * @param aCaractereJogada Recebe uma string com o caractere que que foi aplicado na jogada
     */
    public static void registarJogadas(long aMomentoJogada, int[] aJogadas, String aCaractereJogada) {

        if (logJogadas != null)
            logJogadas.add(new Jogadas(aJogadas[0], aJogadas[1], aCaractereJogada, aMomentoJogada));

    }

    /**
     * Sugere possiveis jogadas ao jogador que invocar este método. Ou seja, depois de obter a
     * referência ao objeto do jogo que está a decorrer e vai percorrer as linhas e colunas do
     * tabuleiro do jogo e verificar quais as posições que estão livres.
     *
     * @param aJogo Recebe como parâmetro um objeto da classe Jogo com a referência do jogo
     */
    public static void dica(Jogo aJogo) {

        char[][] tabuleiro = aJogo.getTabuleiro().getTabuleiro();

        for (int i = 0; i < tabuleiro.length; i++) {
            for (int j = 0; j < tabuleiro[0].length; j++) {

                if (tabuleiro[i][j] == 0) {
                    int x = i + 1;
                    int y = j + 1;
                    System.out.println("Inseria as coordenadas <" + x + "," + y + ">");
                }
            }
        }
    }

    /**
     * Verifica para o jogo retomado se existe algum vencedor ou se estamos perante um empate.
     * Com base na referência do objeto da classe Jogo obtida é invocado o método verificaVencedor
     * que irá verificar em background se existe vencedor ou não. No caso de não se verificar nenhuma
     * das situações (i.e. empate, vencedor X, vencedor O) é retornado 0;
     *
     * @param aJogo Recebe como parâmetro um objeto da classe Jogo
     * @param aInicio Recebe como parâmetro o instante que o jogo foi iniciado
     *
     * @return Retorna 1 no caso de se verificar o X como vencedor, -1 O como vencedor e -2 em caso de empate
     */
    public static int vencedorJogoRetomado(Jogo aJogo, Date aInicio) {

        if (aJogo.verificaVencedor() == 1) {
            System.out.println("\nO vencedor do jogo é o " + nickname);

            long fim = new Date().getTime();
            long duracao = fim - aInicio.getTime();

            aJogo.getJogador().setTempoTotalJogo(duracao);
            aJogo.getJogador().setNumeroJogos();
            aJogo.getJogador().setTotalVitorias(1);

            if (!nickname.equalsIgnoreCase("Anónimo")) {

                aJogo.setJogadas(logJogadas);
                aJogo.setEstado("Concluido");
                aJogo.setTempoDecorrido(duracao);

                if (gereJogo.atualizaJogoRetomado(aJogo) && gereJogador.registarEstatistica(aJogo.getJogador()))
                    System.out.println("!!  Informações sobre o jogo guardadas com sucesso  !!\n");
            }

            mostrarInformacaoJogo(aJogo.getJogador());

            return 1;
        } else if (aJogo.verificaVencedor() == -1) {

            System.out.println("\nO vencedor do jogo é o Computador.");

            long fim = new Date().getTime();
            long duracao = fim - aInicio.getTime();

            aJogo.getJogador().setTempoTotalJogo(duracao);
            aJogo.getJogador().setNumeroJogos();
            aJogo.getJogador().setTotalVitorias(-1);

            if (!nickname.equalsIgnoreCase("Anónimo")) {

                aJogo.setJogadas(logJogadas);
                aJogo.setEstado("Concluido");
                aJogo.setTempoDecorrido(duracao);

                if (gereJogo.atualizaJogoRetomado(aJogo) && gereJogador.registarEstatistica(aJogo.getJogador()))
                    System.out.println("!!  Informações sobre o jogo guardadas com sucesso  !!\n");
            }

            mostrarInformacaoJogo(aJogo.getJogador());
            return -1;
        } else if (aJogo.verificaVencedor() == -2) {
            System.out.println("!!  Neste jogo não houve vencedores  !!\n");

            long fim = new Date().getTime();
            long duracao = fim - aInicio.getTime();

            aJogo.getJogador().setTempoTotalJogo(duracao);
            aJogo.getJogador().setNumeroJogos();

            if (!nickname.equalsIgnoreCase("Anónimo")) {

                aJogo.setJogadas(logJogadas);
                aJogo.setEstado("Concluido");
                aJogo.setTempoDecorrido(duracao);

                if (gereJogo.atualizaJogoRetomado(aJogo) && gereJogador.registarEstatistica(aJogo.getJogador()))
                    System.out.println("!!  Informações sobre o jogo guardadas com sucesso  !!\n");
            }

            mostrarInformacaoJogo(aJogo.getJogador());
            return -2;
        }
        return 0;
    }

    /**
     * Verifica para o jogo contra o computador se existe algum vencedor ou se estamos perante um empate.
     * Com base na referência do objeto da classe Jogo obtida é invocado o método verificaVencedor
     * que irá verificar em background se existe vencedor ou não. No caso de não se verificar nenhuma
     * das situações (i.e. empate, vencedor X, vencedor O) é retornado 0.
     *
     * @param aJogo Recebe como parâmetro um objeto da classe Jogo
     * @param aInicio Recebe como parâmetro o instante que o jogo foi iniciado
     * @param aLogJogadas Recebe como parâmetro um ArrayList da classe Jogadas com todas as jogadas realizadas
     *                    por ambos os jogadores
     *
     * @return Retorna 1 no caso de se verificar o X como vencedor, -1 O como vencedor e -2 em caso de empate
     */
    public static int vencedorJogoComputador(Jogo aJogo, Date aInicio, ArrayList<Jogadas> aLogJogadas) {

        if (aJogo.verificaVencedor() == 1) {
            System.out.println("\nO vencedor do jogo é o " + nickname);

            long fim = new Date().getTime();
            long duracao = fim - aInicio.getTime();

            aJogo.getJogador().setTempoTotalJogo(duracao);
            aJogo.getJogador().setNumeroJogos();
            aJogo.getJogador().setTotalVitorias(1);

            if (!nickname.equalsIgnoreCase("Anónimo")) {

                aJogo.setJogadas(aLogJogadas);
                aJogo.setEstado("Concluido");
                aJogo.setTempoDecorrido(duracao);

                if (gereJogo.registarJogo(aJogo) && gereJogador.registarEstatistica(aJogo.getJogador()))
                    System.out.println("!!  Informações sobre o jogo guardadas com sucesso  !!\n");
            }

            mostrarInformacaoJogo(aJogo.getJogador());

            return 1;
        } else if (aJogo.verificaVencedor() == -1) {

            System.out.println("\nO vencedor do jogo é o Computador.");

            long fim = new Date().getTime();
            long duracao = fim - aInicio.getTime();

            aJogo.getJogador().setTempoTotalJogo(duracao);
            aJogo.getJogador().setNumeroJogos();
            aJogo.getJogador().setTotalVitorias(-1);

            if (!nickname.equalsIgnoreCase("Anónimo")) {

                aJogo.setJogadas(aLogJogadas);
                aJogo.setEstado("Concluido");
                aJogo.setTempoDecorrido(duracao);

                if (gereJogo.registarJogo(aJogo) && gereJogador.registarEstatistica(aJogo.getJogador()))
                    System.out.println("!!  Informações sobre o jogo guardadas com sucesso  !!\n");
            }

            mostrarInformacaoJogo(aJogo.getJogador());
            return -1;
        } else if (aJogo.verificaVencedor() == -2) {
            System.out.println("!!  Neste jogo não houve vencedores  !!\n");

            long fim = new Date().getTime();
            long duracao = fim - aInicio.getTime();

            aJogo.getJogador().setTempoTotalJogo(duracao);
            aJogo.getJogador().setNumeroJogos();

            if (!nickname.equalsIgnoreCase("Anónimo")) {

                aJogo.setJogadas(aLogJogadas);
                aJogo.setEstado("Concluido");
                aJogo.setTempoDecorrido(duracao);

                if (gereJogo.registarJogo(aJogo) && gereJogador.registarEstatistica(aJogo.getJogador()))
                    System.out.println("!!  Informações sobre o jogo guardadas com sucesso  !!\n");
            }

            mostrarInformacaoJogo(aJogo.getJogador());
            return -2;
        }
        return 0;
    }

    /**
     * Verifica para o jogo contra outro jogador na mesma máquina se existe algum vencedor ou se
     * estamos perante um empate. Com base na referência do objeto da classe Jogo obtida é invocado
     * o método verificaVencedor que irá verificar em background se existe vencedor ou não. No caso
     * de não se verificar nenhuma das situações (i.e. empate, vencedor X, vencedor O) é retornado 0.
     *
     * @param aJogo Recebe como parâmetro um objeto da classe Jogo
     * @param aInicio Recebe como parâmetro o instante que o jogo foi iniciado
     *
     * @return Retorna 1 no caso de se verificar o X como vencedor, -1 O como vencedor e -2 em caso de empate
     */
    public static int vencedorJogoLocal(Jogo aJogo, Date aInicio) {

        if (aJogo.verificaVencedor() == 1) {
            System.out.println("\nO vencedor do jogo é o " + nickname);

            long fim = new Date().getTime();
            long duracao = fim - aInicio.getTime();

            //Atualiza a informação estatística que iniciou a aplicação
            aJogo.getJogador().setTempoTotalJogo(duracao);
            aJogo.getJogador().setNumeroJogos();
            aJogo.getJogador().setTotalVitorias(1);

            //Atualiza a informação estatística do jogado adversário
            aJogo.getAdversario().setTotalVitorias(-1);
            aJogo.getAdversario().setTempoTotalJogo(duracao);
            aJogo.getAdversario().setNumeroJogos();

            if (!nickname.equalsIgnoreCase("Anónimo") || !aJogo.getAdversario().getNickname().equalsIgnoreCase("Anónimo")) {

                if (gereJogador.registarEstatistica(aJogo.getJogador()) && gereJogador.registarEstatistica(aJogo.getAdversario()))
                    System.out.println("!!  Informações sobre o jogo guardadas com sucesso  !!\n");
            }

            System.out.println("!!  Informação estatística do jogador " + aJogo.getJogador().getNickname() + "  !!\n");
            mostrarInformacaoJogo(aJogo.getJogador());

            System.out.println("!!  Informação estatística do jogador " + aJogo.getAdversario().getNickname() + "  !!\n");
            mostrarInformacaoJogo(aJogo.getAdversario());

            return 1;
        } else if (aJogo.verificaVencedor() == -1) {

            System.out.println("\nO vencedor do jogo é o jogador " + aJogo.getAdversario().getNickname());

            long fim = new Date().getTime();
            long duracao = fim - aInicio.getTime();

            //Atualiza a informação estatística que iniciou a aplicação
            aJogo.getJogador().setTempoTotalJogo(duracao);
            aJogo.getJogador().setNumeroJogos();
            aJogo.getJogador().setTotalVitorias(-1);

            //Atualiza a informação estatística do jogado adversário
            aJogo.getAdversario().setTotalVitorias(1);
            aJogo.getAdversario().setTempoTotalJogo(duracao);
            aJogo.getAdversario().setNumeroJogos();

            if (!nickname.equalsIgnoreCase("Anónimo") || !aJogo.getAdversario().getNickname().equalsIgnoreCase("Anónimo")) {

                if (gereJogador.registarEstatistica(aJogo.getJogador()) && gereJogador.registarEstatistica(aJogo.getAdversario()))
                    System.out.println("!!  Informações sobre o jogo guardadas com sucesso  !!\n");
            }

            System.out.println("!!  Informação estatística do jogador " + aJogo.getAdversario().getNickname() + "  !!\n");
            mostrarInformacaoJogo(aJogo.getAdversario());

            System.out.println("!!  Informação estatística do jogador " + aJogo.getJogador().getNickname() + "  !!\n");
            mostrarInformacaoJogo(aJogo.getJogador());
            return -1;
        } else if (aJogo.verificaVencedor() == -2) {
            System.out.println("!!  Neste jogo não houve vencedores  !!\n");

            long fim = new Date().getTime();
            long duracao = fim - aInicio.getTime();

            //Atualiza a informação estatística que iniciou a aplicação
            aJogo.getJogador().setTempoTotalJogo(duracao);
            aJogo.getJogador().setNumeroJogos();

            //Atualiza a informação estatística do jogado adversário
            aJogo.getAdversario().setTempoTotalJogo(duracao);
            aJogo.getAdversario().setNumeroJogos();

            if (!nickname.equalsIgnoreCase("Anónimo") || !aJogo.getAdversario().getNickname().equalsIgnoreCase("Anónimo")) {

                if (gereJogador.registarEstatistica(aJogo.getJogador()) && gereJogador.registarEstatistica(aJogo.getAdversario()))
                    System.out.println("!!  Informações sobre o jogo guardadas com sucesso  !!\n");
            }

            System.out.println("!!  Informação estatística do jogador " + aJogo.getJogador().getNickname() + "  !!\n");
            mostrarInformacaoJogo(aJogo.getJogador());

            System.out.println("!!  Informação estatística do jogador " + aJogo.getAdversario().getNickname() + "  !!\n");
            mostrarInformacaoJogo(aJogo.getAdversario());
            return -2;
        }
        return 0;
    }

    /**
     * Apresenta ao utilizador que está a interagir com aplicação o submenu de listagens, que
     * lhe vai permitir listar todos os jogadores de forma ordenada e reduzir o resultado da
     * listagem efetuando pesquisa por o nickname do jogador.
     *
     * @return Retorna um inteiro com a opção introduzida por o utilziador
     */
    public static int submenuListagens() {

        int opcao;

        System.out.println("\n*************************************************************");
        System.out.println("* 1 - Listar todos os jogadores por ordem alfabética        *");
        System.out.println("* 2 - Pesquisar jogadores por nickname                      *");
        System.out.println("* 3 - Listar jogadores ordenados por número de jogos        *");
        System.out.println("* 4 - Listar jogadores ordenados por número de vitórias     *");
        System.out.println("* 5 - Listar jogadores ordenados por tempo total de jogo    *");
        System.out.println("* 6 - Pesquisar registos de atividade por nickname          *");
        System.out.println("* 7 - Voltar ao menu principal                              *");
        System.out.println("*************************************************************");
        System.out.println("Seleccione uma opcao: ");

        opcao = lerDados.lerInteiros();

        return opcao;

    }

    /**
     * Executa o submenu das listagens da aplicação, ou seja, após receber por parâmetro o
     * valor da opção introduzida por o utilizador o programa através de um ciclo switch
     * interpreta qual a opção que o utilizador escolheu e qual a tarefa que lhe está a
     * associada.
     *
     * @param aOpcao Recebe um inteiro com a opção introduzida por o utilizador
     * @param aJogador Recebe um objeto da classe Jogador com a referência ao jogador em atividade
     */
    public static void executaSubmenuListagens(int aOpcao, Jogador aJogador) {
        ArrayList<Jogador> listaJogador;

        switch (aOpcao) {
            case 1:
                listaJogador = gereJogador.listarJogadoresNome();

                listarJogadoresAlfabeticamente(listaJogador);

                gereAtividade.registarAtividade(new Atividade("Listou todos os jogadores por ordem alfabética.",
                        new Date(), new Date(), aJogador));
                break;
            case 2:
                System.out.println("Introduza o nickname do jogador a pesquisar: ");
                String login = lerDados.lerStrings();

                listaJogador = gereJogador.pesquisarJogador(login);
                pesquisarJogadores(listaJogador);

                gereAtividade.registarAtividade(new Atividade("Pesquisou jogadores por o nickname.",
                        new Date(), new Date(), aJogador));
                break;
            case 3:
                listaJogador = gereJogador.listarJogadoresNumJogos();
                listarJogadoresTotalJogos(listaJogador);

                gereAtividade.registarAtividade(new Atividade("Listou jogadores pelo número total de jogos.",
                        new Date(), new Date(), aJogador));
                break;
            case 4:
                listaJogador = gereJogador.listarJogadoresNumVitoria();
                listarJogadoresVitorias(listaJogador);

                gereAtividade.registarAtividade(new Atividade("Listou jogadores com o número de vitórias.",
                        new Date(), new Date(), aJogador));
                break;
            case 5:
                listaJogador = gereJogador.listarJogadoresTempoJogo();
                listarJogadoresTempoTotal(listaJogador);

                gereAtividade.registarAtividade(new Atividade("Listou jogadores com o tempo total de jogo.",
                        new Date(), new Date(), aJogador));
                break;
            case 6:
                System.out.println("Indique o nickname do utilizador para pesquisar a atividade: ");
                String nome = Interface.lerDados.lerStrings();

                ArrayList<Atividade> listarLog = Interface.gereAtividade.pesquisarAtividadeUtilizador(nome);
                pesquisarLogNome(listarLog);

                gereAtividade.registarAtividade(new Atividade("Pesquisou registos de atividade pelo nickname do jogador",
                        new Date(), new Date(), aJogador));
                break;
            case 7:
                int opc;

                //Reencaminha de volta o utilizador para o menu principal
                do {
                    opc = menuPrincipal();
                    executarMenuPrincipal(opc, aJogador);
                } while ((opc > 0) || (opc <= 8));

                gereAtividade.registarAtividade(new Atividade("Voltou ao menu principal.",
                        new Date(), new Date(), aJogador));
                break;
            default:
                System.out.println("Opcao introduzida erradamente.");
                break;
        }
    }

    /**
     * Coloca o servidor à escuta de novas ligações no porto cujo o valor foi recebido por parâmetro
     * no momento de invocação do método. Depois de inicializado o socket passivo com o porto indicado
     * por o utilizador, o servidor fica à escuta de novas ligações nesse porto. No momento que o servidor
     * receber uma nova ligação de um cliente é invocado o método accept para aceitar a ligação e inciar a
     * troca de mensagens com o cliente.
     *
     * @param aPorto Recebe um inteiro com o porto em que o servidor estará à escuta de novas ligações
     */
    public static void aceitaPedidoLigacoes(int aPorto) {

        try {
            String ipLocal = InetAddress.getLocalHost().getHostAddress();

            System.out.println("\n==  O endereço local do servidor é: " + ipLocal + "  ==");

            ServerSocket servidor = new ServerSocket(aPorto);   //Inicializa o socket no qual vai ficar à escuta de ligações
            socket = servidor.accept();     //Aceita novas ligações

            inputStream = new DataInputStream(socket.getInputStream());     //Inicializa a stream de leitura do socket ativo
            outputStream = new DataOutputStream(socket.getOutputStream());  //Inicializa a stream de escrita no socket ativo

            outputStream.writeUTF("<" + nickname + "> <hello>;");

            //Onde serão interpretadas todas as flags enviadas por o cliente
            //Processando-se o jogo entre servidor e cliente
            gestaoMensagensServidor();

        } catch (IOException ioe) {
            System.out.println("??  A ligação foi encerrada por o cliente  ??\n");
            //Encerra as ligações remotas no caso de ser levantada uma exceção
            encerrarLigacoes();
        }
    }

    /**
     *
     * @param aEnderecoIp Recebe uma string com o endereço de IP do servidor
     * @param aPorto Recebe um inteiro com o número do porto que o servidor está à escuta
     */
    public static void iniciarLigacao(String aEnderecoIp, int aPorto) {

        try {
            String ipLocal = InetAddress.getLocalHost().getHostAddress();

            System.out.println("\n==  O endereço local do cliente é: " + ipLocal + "  ==");

            Socket clienteScoket = new Socket(aEnderecoIp, aPorto);

            inputStream = new DataInputStream(clienteScoket.getInputStream());
            outputStream = new DataOutputStream(clienteScoket.getOutputStream());

            //Onde serão interpretadas todas as flags enviadas por o servidor
            //Processando-se o jogo entre cliente e servidor
            gestaoMensagensCliente();


        } catch (UnknownHostException uhe) {
            System.out.println("??  Endereço de IP introduzido erradamente  ??\n");

        } catch (IOException ioe) {
            System.out.println("??  A ligação foi encerrada por o servidor ??\n");

            //Encerra as ligações remotas no caso de ser levantada uma exceção
            encerrarLigacoes();
        }
    }

    /**
     * Desiste do jogo remoto atual que o jogador está inserido, isto, após a emissão
     * do comando que permite desistir do jogo. Acabando por guardar as informações sobre
     * o jogo e o jogador local relativamente àquela sessão.
     *
     * @param aJogoRemoto Recebe um objeto da classe Jogo com a referência do jogo em questão
     * @param aInicioJogo Recebe um long com instante temporal que o jogo foi iniciado
     */
    public static void desistirJogoRemoto(Jogo aJogoRemoto, long aInicioJogo) {

        long duracao = new Date().getTime() - aInicioJogo;

        aJogoRemoto.getJogador().setNumeroJogos();
        aJogoRemoto.getJogador().setTotalVitorias(-1);
        aJogoRemoto.getJogador().setTempoTotalJogo(duracao);

        //Verifica se o nickname do jogador local é diferente de Anónimo para guardar as informações
        //Do jogo e a estatistica do jogador
        if (!aJogoRemoto.getJogador().getNickname().equalsIgnoreCase("Anónimo")) {
            if (gereJogador.registarEstatistica(aJogoRemoto.getJogador()))
                System.out.println("\n!!  As informações estatísticas foram guardadas  !!");
        }

    }

    /**
     * Verifica para o jogo remoto se existe algum vencedor ou se estamos perante um empate.
     * Com base na referência do objeto da classe Jogo obtida é invocado o método verificaVencedor
     * que irá verificar em background se existe vencedor ou não. No caso de não se verificar nenhuma
     * das situações (i.e. empate, vencedor X, vencedor O) é retornado 0;
     *
     * @param aJogoRemoto Recebe como parâmetro um objeto da classe Jogo
     * @param aInicioJogo Recebe como parâmetro o instante que o jogo foi iniciado
     * @param aFlag Recebe um string com o último parâmetro da mensagem recebida do cliente para verificar
     *              se não é ack
     * @throws IOException Lança uma exceção no caso de tentar escrever uma nova mensagem no socket e o mesmo
     *                      já não existir.
     *
     * @return Retorna 1 no caso de se verificar o X como vencedor, -1 O como vencedor e -2 em caso de empate
     */
    public static void verificarVencedorRemoto(Jogo aJogoRemoto, Date aInicioJogo, String aFlag) throws IOException {
        long duracaoJogo = new Date().getTime() - aInicioJogo.getTime();

        if (aJogoRemoto.verificaVencedor() == 1) {
            aJogoRemoto.getJogador().setTempoTotalJogo(duracaoJogo);
            aJogoRemoto.getJogador().setNumeroJogos();
            aJogoRemoto.getJogador().setTotalVitorias(1);

            if (!aJogoRemoto.getJogador().getNickname().equalsIgnoreCase("Anónimo")) {
                if (gereJogador.registarEstatistica(aJogoRemoto.getJogador()))
                    System.out.println("\n!!  As informações estatísticas foram guardadas  !!");
            }

            outputStream.writeUTF("<" + nickname + "> <status> <lose>;");
        } else if (aJogoRemoto.verificaVencedor() == -1) {

            aJogoRemoto.getJogador().setTempoTotalJogo(duracaoJogo);
            aJogoRemoto.getJogador().setNumeroJogos();
            aJogoRemoto.getJogador().setTotalVitorias(-1);

            if (!aJogoRemoto.getJogador().getNickname().equalsIgnoreCase("Anónimo")) {
                if (gereJogador.registarEstatistica(aJogoRemoto.getJogador()))
                    System.out.println("\n!!  As informações estatísticas foram guardadas  !!");
            }

            outputStream.writeUTF("<" + nickname + "> <status> <win>;");
        } else if (aJogoRemoto.verificaVencedor() == -2) {
            aJogoRemoto.getJogador().setTempoTotalJogo(duracaoJogo);
            aJogoRemoto.getJogador().setNumeroJogos();

            if (!aJogoRemoto.getJogador().getNickname().equalsIgnoreCase("Anónimo")) {
                if (gereJogador.registarEstatistica(aJogoRemoto.getJogador()))
                    System.out.println("\n!!  As informações estatísticas foram guardadas  !!");
            }

            outputStream.writeUTF("<" + nickname + "> <status> <draw>;");
        } else {

            if (!aFlag.equalsIgnoreCase("<ack>;"))
                outputStream.writeUTF("<" + nickname + "> <result> <ack>;");
        }
    }

    /**
     * Faz a gestão das mesangens que recebe do cliente através de um ciclo switch que vai verificar
     * qual foi a flag que foi enviada por o cliente e assim saber qual a função que tem a desempenhar
     * perante aquela mensagem recebida. O objetivo é extrair a parâmetro que vem no meio da mensagem e
     * a partir daí fazer as devidas comparações com as flags que é suposto obter do cliente.
     *
     * @throws IOException Lança uma exceção no caso do servidor ativo ter sido encerrado por uma das
     * partes da ligação.
     */
    public static void gestaoMensagensServidor() throws IOException {
        String nicknameCliente = null;
        int linha = 0, coluna = 0, status, numJogadas = 0;
        long duracaoJogada = 0;
        Date inicioJogo = null;
        Jogo jogoRemoto = null;
        Jogador jogadorServidor = null;

        while (true) {
            String mensagem = inputStream.readUTF();
            String[] estrutura = mensagem.split(" ");

            System.out.println(mensagem);

            if (estrutura[1].equalsIgnoreCase("<bye>;")) {
                encerrarLigacoes();
                System.out.println("!!  Ligações encerradas com sucesso  !!");
                break;
            }

            switch (estrutura[1]) {
                case "<hello>;":
                    String login = estrutura[0];
                    nicknameCliente = login.substring(1, login.length() - 1);

                    System.out.println("O nickname do servidor é: " + nickname);
                    System.out.println("O nickname do cliente é: " + nicknameCliente);

                    jogadorServidor = new Jogador(nickname, 0, 0, 0);
                    jogadorServidor.setId(idJogador);

                    jogoRemoto = new Jogo(new Tabuleiro(new char[3][3]), jogadorServidor, new Jogador(nicknameCliente, 0, 0, 0));
                    jogoRemoto.getTabuleiro().mostraTabuleiro();

                    inicioJogo = new Date();

                    status = (int) (Math.random() * 2);
                    outputStream.writeUTF("<" + nickname + "> <start> <" + status + ">;");
                    break;
                case "<start>":

                    if (estrutura[2].equalsIgnoreCase("<ok>;")) {

                        System.out.println("!!  O servidor deve iniciar o jogo  !!\n");

                        System.out.println("###  Pretende: (A) Pedir Ajuda; (D) Desistir; (C) Continuar;  ###");
                        String estado = lerDados.lerStrings();

                        if (estado.equalsIgnoreCase("A")) {
                            long inicioJogada = new Date().getTime();
                            dica(jogoRemoto);

                            numJogadas++;
                            System.out.println("Introduza a coordenada x: ");
                            linha = lerDados.lerInteiros();

                            System.out.println("Introduza a coordenada y: ");
                            coluna = lerDados.lerInteiros();

                            duracaoJogada = new Date().getTime() - inicioJogada;

                            mostrarInformacaoJogada(duracaoJogada, numJogadas);     //Depois de jogar mostra ao utilizador a info sobre a jogada
                            outputStream.writeUTF("<" + nickname + "> <move> <" + linha + "> <" + coluna + ">;");
                        } else if (estado.equalsIgnoreCase("D")) {

                            desistirJogoRemoto(jogoRemoto, inicioJogo.getTime());

                            mostrarInformacaoJogo(jogadorServidor);

                            outputStream.writeUTF("<" + nickname + "> <withdraw>;");
                        } else if (estado.equalsIgnoreCase("C")) {
                            long inicioJogada = new Date().getTime();

                            numJogadas++;
                            System.out.println("Introduza a coordenada x: ");
                            linha = lerDados.lerInteiros();

                            System.out.println("Introduza a coordenada y: ");
                            coluna = lerDados.lerInteiros();

                            duracaoJogada = new Date().getTime() - inicioJogada;
                            
                            mostrarInformacaoJogada(duracaoJogada, numJogadas);     //Depois de jogar mostra ao utilizador a info sobre a jogada
                            outputStream.writeUTF("<" + nickname + "> <move> <" + linha + "> <" + coluna + ">;");
                        }
                    }
                    break;
                case "<move>":

                    String jogadaX = estrutura[estrutura.length - 2];
                    String jogadaY = estrutura[estrutura.length - 1];

                    int coordenadaX = Integer.parseInt(jogadaX.substring(1, jogadaX.length() - 1));  //Extrai da mensagem a coordenada x
                    int coordenadaY = Integer.parseInt(jogadaY.substring(1, jogadaY.length() - 2));  //Extari da mensagem a coordenada y

                    int[] coordenadas = jogoRemoto.jogada(coordenadaX, coordenadaY); // Transforma as coordenadas recebidas no formato correcto

                    if (jogoRemoto.getTabuleiro().verificaPosicao(coordenadas)) {
                        jogoRemoto.getTabuleiro().atualizarTabuleiro('O', coordenadas);

                        jogoRemoto.getTabuleiro().mostraTabuleiro();

                        outputStream.writeUTF("<" + nickname + "> <result> <valid>;");

                    } else {
                        System.out.println("\n??  Introduza uma nova jogada  ??");
                        outputStream.writeUTF("<" + nickname + "> <result> <notvalid>;");
                    }

                    break;
                case "<result>":

                    if (estrutura[2].equalsIgnoreCase("<valid>;")) {
                        int[] jogadas = jogoRemoto.jogada(linha, coluna);

                        if (jogoRemoto.getTabuleiro().verificaPosicao(jogadas)) {
                            jogoRemoto.getTabuleiro().atualizarTabuleiro('X', jogadas);
                            jogoRemoto.getTabuleiro().mostraTabuleiro();

                            verificarVencedorRemoto(jogoRemoto, inicioJogo, estrutura[2]);
                        }

                    } else if (estrutura[2].equalsIgnoreCase("<notvalid>;")) {

                        System.out.println("??  A sua jogada não é válida, deve tentar de novo  ??\n");

                        System.out.println("###  Pretende: (A) Pedir Ajuda; (D) Desistir; (C) Continuar;  ###");
                        String estado = lerDados.lerStrings();

                        if (estado.equalsIgnoreCase("A")) {
                            long inicioJogada = new Date().getTime();
                            dica(jogoRemoto);

                            numJogadas++;
                            System.out.println("Introduza a coordenada x: ");
                            linha = lerDados.lerInteiros();

                            System.out.println("Introduza a coordenada y: ");
                            coluna = lerDados.lerInteiros();

                            duracaoJogada = new Date().getTime() - inicioJogada;

                            mostrarInformacaoJogada(duracaoJogada, numJogadas);     //Depois de jogar mostra ao utilizador a info sobre a jogada
                            outputStream.writeUTF("<" + nickname + "> <move> <" + linha + "> <" + coluna + ">;");
                        } else if (estado.equalsIgnoreCase("D")) {

                            desistirJogoRemoto(jogoRemoto, inicioJogo.getTime());

                            mostrarInformacaoJogo(jogadorServidor);

                            outputStream.writeUTF("<" + nickname + "> <withdraw>;");
                        } else if (estado.equalsIgnoreCase("C")) {
                            long inicioJogada = new Date().getTime();

                            numJogadas++;
                            System.out.println("Introduza a coordenada x: ");
                            linha = lerDados.lerInteiros();

                            System.out.println("Introduza a coordenada y: ");
                            coluna = lerDados.lerInteiros();

                            duracaoJogada = new Date().getTime() - inicioJogada;

                            mostrarInformacaoJogada(duracaoJogada, numJogadas);     //Depois de jogar mostra ao utilizador a info sobre a jogada
                            outputStream.writeUTF("<" + nickname + "> <move> <" + linha + "> <" + coluna + ">;");
                        }
                    } else {
                        verificarVencedorRemoto(jogoRemoto, inicioJogo, estrutura[2]);    //Depois de receber a confirmação do cliente envia se ganhou ou não

                        System.out.println("!!  O cliente recebeu o resultado da jogada. Agora é a sua vez  !!\n");

                        System.out.println("###  Pretende: (A) Pedir Ajuda; (D) Desistir; (C) Continuar;  ###");
                        String estado = lerDados.lerStrings();

                        if (estado.equalsIgnoreCase("A")) {
                            long inicioJogada = new Date().getTime();
                            dica(jogoRemoto);

                            numJogadas++;
                            System.out.println("Introduza a coordenada x: ");
                            linha = lerDados.lerInteiros();

                            System.out.println("Introduza a coordenada y: ");
                            coluna = lerDados.lerInteiros();

                            duracaoJogada = new Date().getTime() - inicioJogada;
                            mostrarInformacaoJogada(duracaoJogada, numJogadas);     //Depois de jogar mostra ao utilizador a info sobre a jogada

                            outputStream.writeUTF("<" + nickname + "> <move> <" + linha + "> <" + coluna + ">;");
                        } else if (estado.equalsIgnoreCase("D")) {

                            desistirJogoRemoto(jogoRemoto, inicioJogo.getTime());

                            mostrarInformacaoJogo(jogadorServidor);

                            outputStream.writeUTF("<" + nickname + "> <withdraw>;");
                        } else if (estado.equalsIgnoreCase("C")) {
                            long inicioJogada = new Date().getTime();

                            numJogadas++;
                            System.out.println("Introduza a coordenada x: ");
                            linha = lerDados.lerInteiros();

                            System.out.println("Introduza a coordenada y: ");
                            coluna = lerDados.lerInteiros();

                            duracaoJogada = new Date().getTime() - inicioJogada;

                            mostrarInformacaoJogada(duracaoJogada, numJogadas);     //Depois de jogar mostra ao utilizador a info sobre a jogada
                            outputStream.writeUTF("<" + nickname + "> <move> <" + linha + "> <" + coluna + ">;");
                        }
                    }

                    break;
                case "<status>":

                    if (estrutura[2].equalsIgnoreCase("<ok>;")) {
                        
                        mostrarInformacaoJogo(jogadorServidor);

                        //Questionar o cliente sobre realizar um novo jogo
                        outputStream.writeUTF("<" + nickname + "> <new> <?>;");
                    }

                    break;
                case "<withdraw>;":

                    jogoRemoto.getJogador().setTotalVitorias(1);
                    jogoRemoto.getJogador().setNumeroJogos();
                    jogoRemoto.setTempoDecorrido(0);

                    if (!jogoRemoto.getJogador().getNickname().equalsIgnoreCase("Anónimo")) {
                        if (gereJogador.registarEstatistica(jogoRemoto.getJogador()))
                            System.out.println("\n!!  Informações estatísticas armazenadas com sucesso  !!");
                    }

                    outputStream.writeUTF("<" + nickname + "> <withdraw> <ack>;");

                    break;
                case "<withdraw>":

                    if (estrutura[2].equalsIgnoreCase("<ack>;"))
                        //Perguntar ao cliente se pretende iniciar um novo jogo
                        outputStream.writeUTF("<" + nickname + "> <new> <?>;");

                    break;
                case "<new>":

                    if (estrutura[2].equalsIgnoreCase("<y>;")) {

                        jogadorServidor = new Jogador(nickname, 0, 0, 0);
                        jogadorServidor.setId(idJogador);

                        //Começa um novo jogo
                        jogoRemoto = new Jogo(new Tabuleiro(new char[3][3]), jogadorServidor, new Jogador(nicknameCliente, 0, 0, 0));
                        inicioJogo = new Date();
                        jogoRemoto.getTabuleiro().mostraTabuleiro();

                        outputStream.writeUTF("<" + nickname + "> <hello>;");
                    } else {
                        //Depede-se do cliente e encerra as ligações remotas
                        outputStream.writeUTF("<" + nickname + "> <bye>;");
                        encerrarLigacoes();
                    }
                    break;
                case "<ready>;":
                    outputStream.writeUTF("<" + nickname + "> <new> <?>;");

                    break;
                default:
                    System.out.println("??  A mensagem recebida do cliente não foi a esperada  ??");
                    outputStream.writeUTF("<" + nickname + "> <hello>;");
                    break;
            }
        }
    }

    /**
     * Faz a gestão das mesangens que recebe do servidor através de um ciclo switch que vai verificar
     * qual foi a flag que foi enviada por o servidor e assim saber qual a função que tem a desempenhar
     * perante aquela mensagem recebida. Após receber a mesnagem <bye> a ligação é encerrada.
     *
     * @throws IOException Lança uma exceção no caso do servidor ativo ter sido encerrado por uma das
     * partes da ligação.
     */
    public static void gestaoMensagensCliente() throws IOException {
        String nicknameServidor = null;
        int linha = 0, coluna = 0, numJogadas = 0;
        long duracaoJogada = 0;
        Date inicioJogo = null;
        Jogo jogoRemoto = null;
        Jogador jogadorCliente = null;

        while (true) {
            String mensagem = inputStream.readUTF();    //Recebe a mensagem enviado por o servidor
            String[] estrutura = mensagem.split(" ");

            System.out.println(mensagem);

            //Verifica se a mensagem recebida por o servidor é igual a bye para encerrar a ligação
            if (estrutura[1].equalsIgnoreCase("<bye>;")) {
                encerrarLigacoes();
                System.out.println("!!  Ligações encerradas com sucesso  !!");
                break;
            }

            switch (estrutura[1]) {
                case "<hello>;":
                    String login = estrutura[0];
                    nicknameServidor = login.substring(1, login.length() - 1);

                    System.out.println("O nickname do cliente é: " + nickname);
                    System.out.println("O nickname do servidor é: " + nicknameServidor);

                    jogadorCliente = new Jogador(nickname, 0, 0, 0);
                    jogadorCliente.setId(idJogador);

                    jogoRemoto = new Jogo(new Tabuleiro(new char[3][3]), jogadorCliente, new Jogador(nicknameServidor, 0, 0, 0));
                    inicioJogo = new Date();
                    numJogadas = 0;

                    outputStream.writeUTF("<" + nickname + "> <hello>;");
                    break;
                case "<start>":

                    if (estrutura[2].equalsIgnoreCase("<1>;")) {
                        System.out.println("\n!!  É a vez do servidor jogar  !!");
                        outputStream.writeUTF("<" + nickname + "> <start> <ok>;");

                    } else {
                        System.out.println("\n!!  É a sua vez de jogar  !!");

                        jogoRemoto.getTabuleiro().mostraTabuleiro();

                        System.out.println("###  Pretende: (A) Pedir Ajuda; (D) Desistir; (C) Continuar;  ###");
                        String estado = lerDados.lerStrings();

                        if (estado.equalsIgnoreCase("A")) {
                            long inicioJogada = new Date().getTime();
                            dica(jogoRemoto);

                            numJogadas++;
                            System.out.println("Introduza a coordenada x: ");
                            linha = lerDados.lerInteiros();

                            System.out.println("Introduza a coordenada y: ");
                            coluna = lerDados.lerInteiros();

                            duracaoJogada = new Date().getTime() - inicioJogada;
                            outputStream.writeUTF("<" + nickname + "> <move> <" + linha + "> <" + coluna + ">;");
                        } else if (estado.equalsIgnoreCase("D")) {

                            desistirJogoRemoto(jogoRemoto, inicioJogo.getTime());

                            mostrarInformacaoJogo(jogadorCliente);

                            outputStream.writeUTF("<" + nickname + "> <withdraw>;");
                        } else if (estado.equalsIgnoreCase("C")) {
                            long inicioJogada = new Date().getTime();

                            numJogadas++;

                            System.out.println("Introduza a coordenada x: ");
                            linha = lerDados.lerInteiros();

                            System.out.println("Introduza a coordenada y: ");
                            coluna = lerDados.lerInteiros();

                            duracaoJogada = new Date().getTime() - inicioJogada;

                            mostrarInformacaoJogada(duracaoJogada, numJogadas);     //Depois de jogar mostra ao utilizador a info sobre a jogada
                            outputStream.writeUTF("<" + nickname + "> <move> <" + linha + "> <" + coluna + ">;");
                        }
                    }

                    break;
                case "<move>":
                    String jogadaX = estrutura[estrutura.length - 2];
                    String jogadaY = estrutura[estrutura.length - 1];

                    int coordenadaX = Integer.parseInt(jogadaX.substring(1, jogadaX.length() - 1));
                    int coordenadaY = Integer.parseInt(jogadaY.substring(1, jogadaY.length() - 2));

                    int[] coordenadas = jogoRemoto.jogada(coordenadaX, coordenadaY);

                    if (jogoRemoto.getTabuleiro().verificaPosicao(coordenadas)) {

                        jogoRemoto.getTabuleiro().atualizarTabuleiro('X', coordenadas);
                        jogoRemoto.getTabuleiro().mostraTabuleiro();

                        outputStream.writeUTF("<" + nickname + "> <result> <valid>;");
                    } else {
                        System.out.println("\n??  Coordenada erradas.  ??");
                        outputStream.writeUTF("<" + nickname + "> <result> <notvalid>;");
                    }
                    break;
                case "<result>":

                    if (estrutura[2].equalsIgnoreCase("<valid>;")) {

                        int[] jogadas = jogoRemoto.jogada(linha, coluna);

                        if (jogoRemoto.getTabuleiro().verificaPosicao(jogadas)) {
                            jogoRemoto.getTabuleiro().atualizarTabuleiro('O', jogadas);
                            jogoRemoto.getTabuleiro().mostraTabuleiro();

                        } else
                            System.out.println("\n??  Coordenadas erradas.  ??");

                        outputStream.writeUTF("<" + nickname + "> <result> <ack>;");
                    } else if (estrutura[2].equalsIgnoreCase("<notvalid>;")) {
                        System.out.println("??  A sua jogada está errada deve tentar de novo  ??\n");

                        mostrarInformacaoJogada(duracaoJogada, numJogadas);

                        System.out.println("\n###  Pretende: (A) Pedir Ajuda; (D) Desistir; (C) Continuar;  ###");
                        String estado = lerDados.lerStrings();

                        if (estado.equalsIgnoreCase("A")) {
                            long inicioJogada = new Date().getTime();
                            dica(jogoRemoto);

                            numJogadas++;
                            System.out.println("Introduza a coordenada x: ");
                            linha = lerDados.lerInteiros();

                            System.out.println("Introduza a coordenada y: ");
                            coluna = lerDados.lerInteiros();

                            duracaoJogada = new Date().getTime() - inicioJogada;

                            mostrarInformacaoJogada(duracaoJogada, numJogadas);     //Depois de jogar mostra ao utilizador a info sobre a jogada
                            outputStream.writeUTF("<" + nickname + "> <move> <" + linha + "> <" + coluna + ">;");
                        } else if (estado.equalsIgnoreCase("D")) {

                            desistirJogoRemoto(jogoRemoto, inicioJogo.getTime());

                            mostrarInformacaoJogo(jogadorCliente);
                            outputStream.writeUTF("<" + nickname + "> <withdraw>;");
                        } else if (estado.equalsIgnoreCase("C")) {
                            long inicioJogada = new Date().getTime();

                            numJogadas++;
                            System.out.println("Introduza a coordenada x: ");
                            linha = lerDados.lerInteiros();

                            System.out.println("Introduza a coordenada y: ");
                            coluna = lerDados.lerInteiros();

                            duracaoJogada = new Date().getTime() - inicioJogada;

                            mostrarInformacaoJogada(duracaoJogada, numJogadas);     //Depois de jogar mostra ao utilizador a info sobre a jogada
                            outputStream.writeUTF("<" + nickname + "> <move> <" + linha + "> <" + coluna + ">;");
                        }
                    } else {
                        System.out.println("!!  É a sua vez de jogar  !!\n");

                        System.out.println("\n###  Pretende: (A) Pedir Ajuda; (D) Desistir; (C) Continuar;  ###");
                        String estado = lerDados.lerStrings();

                        if (estado.equalsIgnoreCase("A")) {
                            long inicioJogada = new Date().getTime();
                            dica(jogoRemoto);

                            numJogadas++;
                            System.out.println("Introduza a coordenada x: ");
                            linha = lerDados.lerInteiros();

                            System.out.println("Introduza a coordenada y: ");
                            coluna = lerDados.lerInteiros();

                            duracaoJogada = new Date().getTime() - inicioJogada;

                            mostrarInformacaoJogada(duracaoJogada, numJogadas);     //Depois de jogar mostra ao utilizador a info sobre a jogada
                            outputStream.writeUTF("<" + nickname + "> <move> <" + linha + "> <" + coluna + ">;");
                        } else if (estado.equalsIgnoreCase("D")) {

                            desistirJogoRemoto(jogoRemoto, inicioJogo.getTime());
                            mostrarInformacaoJogo(jogadorCliente);

                            outputStream.writeUTF("<" + nickname + "> <withdraw>;");
                        } else if (estado.equalsIgnoreCase("C")) {
                            long inicioJogada = new Date().getTime();

                            numJogadas++;
                            System.out.println("Introduza a coordenada x: ");
                            linha = lerDados.lerInteiros();

                            System.out.println("Introduza a coordenada y: ");
                            coluna = lerDados.lerInteiros();

                            duracaoJogada = new Date().getTime() - inicioJogada;

                            mostrarInformacaoJogada(duracaoJogada, numJogadas);     //Depois de jogar mostra ao utilizador a info sobre a jogada
                            outputStream.writeUTF("<" + nickname + "> <move> <" + linha + "> <" + coluna + ">;");
                        }
                    }

                    break;
                case "<status>":

                    if (estrutura[2].equalsIgnoreCase("<win>;")) {
                        long duracao = new Date().getTime() - inicioJogo.getTime();

                        jogoRemoto.getJogador().setTotalVitorias(1);
                        jogoRemoto.getJogador().setNumeroJogos();
                        jogoRemoto.getJogador().setTempoTotalJogo(duracao);

                        if (!jogoRemoto.getJogador().getNickname().equalsIgnoreCase("Anónimo")) {
                            if (gereJogador.registarEstatistica(jogoRemoto.getJogador())) {
                                System.out.println("\n!!  As informações estatísticas foram guardadas com sucesso  !!");
                                outputStream.writeUTF("<" + nickname + "> <status> <ok>;");
                            }
                        }

                    } else if (estrutura[2].equalsIgnoreCase("<draw>;")) {
                        long duracao = new Date().getTime() - inicioJogo.getTime();

                        jogoRemoto.getJogador().setNumeroJogos();
                        jogoRemoto.getJogador().setTempoTotalJogo(duracao);

                        if (!jogoRemoto.getJogador().getNickname().equalsIgnoreCase("Anónimo")) {
                            if (gereJogador.registarEstatistica(jogoRemoto.getJogador())) {
                                System.out.println("\n!!  As informações estatísticas foram guardadas com sucesso  !!");
                                outputStream.writeUTF("<" + nickname + "> <status> <ok>;");
                            }
                        }
                    } else {
                        long duracao = new Date().getTime() - inicioJogo.getTime();

                        jogoRemoto.getJogador().setTotalVitorias(-1);
                        jogoRemoto.getJogador().setNumeroJogos();
                        jogoRemoto.getJogador().setTempoTotalJogo(duracao);

                        if (!jogoRemoto.getJogador().getNickname().equalsIgnoreCase("Anónimo")) {
                            if (gereJogador.registarEstatistica(jogoRemoto.getJogador())) {
                                System.out.println("\n!!  As informações estatísticas foram guardadas com sucesso  !!");
                                outputStream.writeUTF("<" + nickname + "> <status> <ok>;");
                            }
                        }
                    }

                    break;
                case "<withdraw>;":

                    long duracao = new Date().getTime() - inicioJogo.getTime();

                    jogoRemoto.getJogador().setTotalVitorias(1);
                    jogoRemoto.getJogador().setNumeroJogos();
                    jogoRemoto.getJogador().setTempoTotalJogo(duracao);

                    if (!jogoRemoto.getJogador().getNickname().equalsIgnoreCase("Anónimo")) {
                        if (gereJogador.registarEstatistica(jogoRemoto.getJogador()))
                            System.out.println("\n!!  As informações estatísticas foram guardadas com sucesso  !!");
                    }

                    //Mensagem de confirmação enviada depois de receber um pedido de desistência
                    outputStream.writeUTF("<" + nickname + "> <withdraw> <ack>;");
                    break;
                case "<withdraw>":

                    if (estrutura[2].equalsIgnoreCase("<ack>;"))
                        outputStream.writeUTF("<" + nickname + "> <ready>;");

                    break;
                case "<new>":

                    if (estrutura[2].equalsIgnoreCase("<?>;")) {

                        System.out.println("\nPretende realizar um novo jogo?(s/n) ");
                        String verificacao = lerDados.lerStrings();

                        if (verificacao.equalsIgnoreCase("s")) {
                            outputStream.writeUTF("<" + nickname + "> <new> <y>;");
                        } else {
                            outputStream.writeUTF("<" + nickname + "> <new> <n>;");
                        }
                    }
                    break;
                default:
                    System.out.println("??  Lamentamos, mas a mensagem enviada do servidor está errada  ??");
                    break;
            }
        }
    }

    /**
     * Encerra as ligações das streams e do socket ativo no momento que a ligação entre
     * servidor e cliente tenha sido terminado. Seja de forma brusca ao fechar a janela da
     * aplicação ou por ter enviado a flag <bye> a uma das partes do jogo.
     */
    public static void encerrarLigacoes() {

        try {

            if (outputStream != null)

                outputStream.close();

            if (inputStream != null)
                inputStream.close();

            if (socket != null)
                socket.close();

        } catch (IOException ioe) {
            System.out.println("Exception: " + ioe);
        }
    }

    /**
     * Apresenta ao utilziador o resultado da pesquisa efetuada por o nickname do jogador.
     * Este método ao receber um ArrayList com os resultados da pesquisa irá apresentá-los
     * no ecrã de 10 em 10 registos.
     *
     * @param aListaJogadores Recebe como parâmetro um ArrayList da classe Jogador com o resultado
     *                        da pesquisa
     */
    public static void pesquisarJogadores(ArrayList<Jogador> aListaJogadores) {
        int numeroRegistos = 0;

        System.out.println("\n+-----------------------------------------------------------------------------------------------------------+");
        System.out.println("+   Nickname    |       Total de Jogos      |       Tempo total de Jogo     |       Total de vitórias       +");
        System.out.println("+-----------------------------------------------------------------------------------------------------------+");

        for (Jogador jog : aListaJogadores) {
            numeroRegistos++;
            System.out.println(jog.toString());

            if (numeroRegistos % 10 == 0) {
                System.out.println("\nDeseja continuar a listar (s/n)? ");
                String continuar = lerDados.lerStrings();

                //verificar continuaçao da listagem
                if (!continuar.equalsIgnoreCase("s"))
                    break;
            }
        }
    }

    /**
     * Mostra ao utilizador todos os jogadores registado na base de dados, ordenados de forma ascendente
     * com base no nickname do jogador. No inicio de cada listagem é apresentado
     * um cabeçalho com a informação que o utilizador está a consultar do jogadores.
     *
     * @param aListaJogadores Recebe como parâmetro um ArrayList da classe Jogador
     */
    public static void listarJogadoresAlfabeticamente(ArrayList<Jogador> aListaJogadores) {

        int numeroRegistos = 0;

        System.out.println("\n+-----------------------------------------------------------------------------------------------------------+");
        System.out.println("+   Nickname    |       Total de Jogos      |       Tempo total de Jogo     |       Total de vitórias       +");
        System.out.println("+-----------------------------------------------------------------------------------------------------------+");

        for (Jogador jog : aListaJogadores) {
            numeroRegistos++;
            System.out.println(jog.toString());

            if (numeroRegistos % 10 == 0) {
                System.out.println("\nDeseja continuar a listar (s/n)? ");
                String continuar = lerDados.lerStrings();

                //verificar continuaçao da listagem
                if (!continuar.equalsIgnoreCase("s"))
                    break;
            }
        }
    }

    /**
     * Mostra ao utilizador todos os jogadores registados na base de dados, ordenados de forma ascendente
     * com base no número de vitórias que o jogador foi alcançando em todos os jogos que realizou. No inicio
     * de cada listagem é apresentado um cabeçalho com a informação que o utilizador está a consultar do jogadores.
     *
     * @param aListaJogadores Recebe como parâmetro um ArrayList da classe Jogador
     */
    public static void listarJogadoresVitorias(ArrayList<Jogador> aListaJogadores) {

        int numeroRegistos = 0;

        System.out.println("\n+-----------------------------------------------------------------------------------------------------------+");
        System.out.println("+   Nickname    |       Total de Jogos      |       Tempo total de Jogo     |       Total de vitórias       +");
        System.out.println("+-----------------------------------------------------------------------------------------------------------+");

        for (Jogador jog : aListaJogadores) {
            numeroRegistos++;
            System.out.println(jog.toString());

            if (numeroRegistos % 10 == 0) {
                System.out.println("\nDeseja continuar a listar (s/n)? ");
                String continuar = lerDados.lerStrings();

                //verificar continuaçao da listagem
                if (!continuar.equalsIgnoreCase("s"))
                    break;
            }
        }
    }

    /**
     * Mostra ao utilizador todos os jogadores registados na base de dados, ordenados de forma ascendente
     * com base no total de jogos que realizaram. No inicio de cada listagem é apresentado
     * um cabeçalho com a informação que o utilizador está a consultar dos jogadores.
     *
     * @param aListaJogadores Recebe como parâmetro um ArrayList da classe Jogador
     */
    public static void listarJogadoresTotalJogos(ArrayList<Jogador> aListaJogadores) {

        int numeroRegistos = 0;

        System.out.println("\n+-----------------------------------------------------------------------------------------------------------+");
        System.out.println("+   Nickname    |       Total de Jogos      |       Tempo total de Jogo     |       Total de vitórias       +");
        System.out.println("+-----------------------------------------------------------------------------------------------------------+");

        for (Jogador jog : aListaJogadores) {
            numeroRegistos++;
            System.out.println(jog.toString());

            if (numeroRegistos % 10 == 0) {
                System.out.println("\nDeseja continuar a listar (s/n)? ");
                String continuar = lerDados.lerStrings();

                //verificar continuaçao da listagem
                if (!continuar.equalsIgnoreCase("s"))
                    break;
            }
        }
    }

    /**
     * Mostra ao utilizador todos os jogadores registado na base de dados, ordenados de forma ascendente
     * com base no tempo total de jogo. No inicio de cada listagem é apresentado
     * um cabeçalho com a informação que o utilizador está a consultar dos jogadores.
     *
     * @param aListaJogadores Recebe como parâmetro um ArrayList da classe Jogador
     */
    public static void listarJogadoresTempoTotal(ArrayList<Jogador> aListaJogadores) {

        int numeroRegistos = 0;

        System.out.println("\n+-----------------------------------------------------------------------------------------------------------+");
        System.out.println("+   Nickname    |       Total de Jogos      |       Tempo total de Jogo     |       Total de vitórias       +");
        System.out.println("+-----------------------------------------------------------------------------------------------------------+");

        for (Jogador jog : aListaJogadores) {
            numeroRegistos++;
            System.out.println(jog.toString());

            if (numeroRegistos % 10 == 0) {
                System.out.println("\nDeseja continuar a listar (s/n)? ");
                String continuar = lerDados.lerStrings();

                //verificar continuaçao da listagem
                if (!continuar.equalsIgnoreCase("s"))
                    break;
            }
        }
    }

    /**
     * Mostra ao utilizador todas as notificações que lhe foram enviadas por os outros
     * utilizadores ou por o próprio sistema. No inicio de cada listagem é apresentado
     * um cabeçalho com a informação que o utilizador está a consultar as suas notificações.
     *
     * @param aNotificacoes Recebe um ArrayList da classe Notificacoes com todas as notificacoes
     */
    public static void listarNotificacoes(ArrayList<Notificacoes> aNotificacoes) {


        if (!aNotificacoes.isEmpty()) {

            int count = 0;

            System.out.println("\n+----------------------------------------------------------+");
            System.out.println("+                              Mensagem                    +");
            System.out.println("+----------------------------------------------------------+");
            for (Notificacoes mensagem : aNotificacoes) {

                count++;
                System.out.println(mensagem.toString());

                if (count % 10 == 0) {
                    System.out.println("\nDeseja continuar a listar (s/n)? ");
                    String continuar = lerDados.lerStrings();

                    //verificar continuaçao da listagem
                    if (!continuar.equalsIgnoreCase("s"))
                        break;
                }
            }

        } else {
            System.out.println("\nAinda não existem notificações!");
        }
    }

    /**
     * Pesquisa os registos de atividade na aplicação por o nome do utilizador. Este método
     * ao receber um ArrayList com os resultados da pesquisa irá apresentá-los ao gestor de
     * 10 em 10 registos.
     *
     * @param aListarLog Recebe um ArrayList da classe Atividade com todos os resultados
     */
    public static void pesquisarLogNome(ArrayList<Atividade> aListarLog) {

        int registos = 1;

        if (aListarLog.isEmpty()) {
            System.out.println("Não existem utilizadores para mostrar.");
        } else {
            System.out.println("\n<Data da Ação>    <Hora>      <Ação Realizada>    <Nome do utilizador>");

            for (Atividade act : aListarLog) {

                registos++;
                System.out.println(act.toString());

                if (registos % 10 == 0) {
                    System.out.println("\nDeseja continuar a ver (s/n)? ");
                    String continuar = Interface.lerDados.lerStrings();

                    //verificar continuaçao da listagem
                    if (!continuar.equalsIgnoreCase("s"))
                        break;
                }
            }
        }

    }

    /**
     * Apresenta ao utilizador que tem sessão iniciada o registo de atividade
     * na aplicação. Deste modo é possível o utilizador conseguir visualizar
     * todas as atividades que realizou na aplicação.
     */
    public static void listarLogPrivado() {

        ArrayList<Atividade> atividades = gereAtividade.listarTodaAtividade();
        int numeroRegistos = 0;

        System.out.println("<Data da acção>    <Hora>      <Ação Realizada>    <Nome do utilizador>");

        for (Atividade act : atividades) {
            numeroRegistos++;
            System.out.println(act.toString());

            if (numeroRegistos % 10 == 0) {
                System.out.println("\nDeseja continuar a listar (s/n)? ");
                String continuar = lerDados.lerStrings();

                //verificar continuaçao da listagem
                if (!continuar.equalsIgnoreCase("s"))
                    break;
            }
        }

    }

    /**
     * Regista o nickname do jogador no momento de execução da aplicação, isto, se o jogador
     * pretender registar um nickname. Caso contrário o utilizador fica identificado como
     * "Anónimo".
     */
    public static void registarNickname() {

        System.out.println("\nPretende registar um nickname para jogar?(s/n) ");
        String identJogador = lerDados.lerStrings();

        if (identJogador.equalsIgnoreCase("n"))
            nickname = "Anónimo";   //No caso do jogador quere manter o anonimato o registo é guardado na bd
        else {

            System.out.println("\nIntroduza um nickname: ");
            nickname = lerDados.lerStrings();

            idJogador = gereJogador.registarJogador(new Jogador(nickname, 0, 0, 0));

            if (idJogador != 0)
                System.out.println("\n!!  O seu pedido de registo foi efetuado com sucesso  !!");
            else
                System.out.println("\n ??  Lamentamos, mas ocorreu um erro a efetuar o seu registo como jogador  ??");
        }
    }

    /**
     * Apresenta ao utilziador um menu que permite manipular o ficheiro com as credenciais de acesso
     * à base de dados. Isto é, ao ser invocado este método vai permitir perguntar ao utilizador se
     * pretende alterar as credenciais de acesso à base de dados ou manter as mesmas.
     *
     * @param aFile Recebe um objeto da classe File com o ficheiro que contêm as credenciais de acesso à bd
     */
    public static void menuBd(File aFile) {
        int opcao, opc;
        Jogador jogador;

        try {

            System.out.println("\n*******************************************************************");
            System.out.println("*1 - Pretende alterar as credenciais de acesso à base de dados?   *");
            System.out.println("*2 - Pretende utilizar as credenciais já registadas?              *");
            System.out.println("*******************************************************************");
            System.out.println("Seleccione uma opcao: ");

            opcao = lerDados.lerInteiros();

            switch (opcao) {
                case 1:
                    System.out.println("Deve introduzir novas credenciais para o acesso a base de dados.\n");
                    registarCredenciaisBd(aFile);

                    registarNickname();

                    jogador = new Jogador();
                    jogador.setId(idJogador);
                    jogador.setNickname(nickname);

                    System.out.println("\nBem-vindo [" + nickname + "]");

                    do {
                        opc = menuPrincipal();
                        executarMenuPrincipal(opc, jogador);
                    } while ((opc > 0) || (opc <= 8));
                    break;
                case 2:

                    registarNickname();
                    jogador = new Jogador();
                    jogador.setId(idJogador);
                    jogador.setNickname(nickname);

                    System.out.println("\nBem-vindo [" + nickname + "]");

                    do {
                        opc = menuPrincipal();
                        executarMenuPrincipal(opc, jogador);
                    } while ((opc > 0) || (opc <= 8));
                    break;
                default:
                    System.out.println("Opcao introduzida está incorrecta.");
                    break;
            }


        } catch (IOException ioe) {
            System.out.println(ioe);
        }
    }


    /**
     * Calcula o tempo de execução da aplicação e apresenta ao utilziador quando terminar sessão
     * a informação sobre o inicio da sessão, a informação sobre o fim da sessão e o tempo que
     * a aplicação esteve em execução.
     */
    public static void execucaoAplicacao() {

        NumberFormat formato = new DecimalFormat("##");

        Date dataFim = new Date();

        String inicio = new SimpleDateFormat("EEE; yyyy-MM-dd HH:mm:ss").format(dataInicioExecucao);
        String fim = new SimpleDateFormat("EEE; yyyy-MM-dd HH:mm:ss").format(dataFim);
        Long duracao = (dataFim.getTime() - dataInicioExecucao.getTime());
        String segundos = formato.format(duracao / 1000);
        String minutos = formato.format(duracao / (1000 * 60));
        String horas = formato.format(duracao / (1000 * 60 * 60));

        System.out.println("Inicio da execucao: " + inicio + "" + "\nFim da execucao: " + fim + ""
                + "\nTempo de Execucao: " + duracao + " Milisegundos (" + segundos + " segundos; " + minutos
                + " minutos; " + horas + " horas;)");
    }

    /**
     * Regista as credenciais que forem introduzidas por utilizador de acesso à base de dados
     * num ficheiro de texto. Depois de ter o utilizador introduzir todas a informação necessária
     * de acesso à base de dados é registada a informação num ficheiro de texto com recurso a
     * properties.
     *
     * @param aFile Recebe uma string com o caminho do ficheiro a registar a informação
     * @throws IOException Lança um exceção caso falhe alguma operação de I/O
     */
    public static void registarCredenciaisBd(File aFile) throws IOException {

        Properties credenciais = new Properties();

        System.out.println("Introduza o endereco o IP da Base de Dados: ");
        String ip = lerDados.lerStrings();

        System.out.println("\nIntroduza o porto da Base de Dados: ");
        String porto = lerDados.lerStrings();

        System.out.println("\nIntroduza o nome da Base de Dados em uso: ");
        String nomeBd = lerDados.lerStrings();

        System.out.println("\nIntroduza o username de acesso a Base de dados: ");
        String login = lerDados.lerStrings();

        System.out.println("\nIntroduza a respectiva palavra-pass: ");
        String passwd = lerDados.lerStrings();

        credenciais.setProperty("Ip", ip);
        credenciais.setProperty("porto", porto);
        credenciais.setProperty("nomeBd", nomeBd);
        credenciais.setProperty("login", login);
        credenciais.setProperty("password", passwd);

        credenciais.store(new FileOutputStream(aFile), "Credenciais de acesso a base de dados.");
        System.out.println("\n*  Credenciais registadas   *\n");
    }
}