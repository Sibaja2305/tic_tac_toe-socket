package servidor;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class Servidor {

    //Inicializamos el puerto
    private final int puerto = 8000;
    //Numero maximo de conexiones (el tictactoe es un juego para 2)
    private final int noConexiones = 2;
    //Creamos una lista de sockets para guardar el socket de cada jugador
    private LinkedList<Socket> usuarios = new LinkedList<Socket>();
    //Variable para controlar el turno de cada jugador
    private Boolean turno = true;
    //Matriz donde se guardan los movimientos 
    private int G[][] = new int[3][3];
    //Numero de veces que se juega...para controlar las X y O
    private int turnos = 1;

    public void escuchar() {
        try {

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    G[i][j] = -1;
                }
            }

            ServerSocket servidor = new ServerSocket(puerto, noConexiones);

            System.out.println("ESPERANDO CONEXION CON LOS JUGADORES");
            while (true) {

                Socket cliente = servidor.accept();

                usuarios.add(cliente);

                int xo = turnos % 2 == 0 ? 1 : 0;
                turnos++;

                Runnable run = new HiloServidor(cliente, usuarios, xo, G);
                Thread hilo = new Thread(run);
                hilo.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Funcion main para correr el servidor
    public static void main(String[] args) {
        Servidor servidor = new Servidor();
        servidor.escuchar();
    }
}
