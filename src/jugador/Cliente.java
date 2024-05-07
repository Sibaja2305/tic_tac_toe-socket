package jugador;

import java.awt.Image;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;

/**
 *
 * @author JKalith
 */
public class Cliente implements Runnable{
    //conexion y comunicacion
    private Socket cliente;
    private DataOutputStream out;
    private DataInputStream in;
 
    private int puerto = 8000;
//localhost
    private String host = "192.168.3.212";
    

    private String mensaje;
    private Main frame;
    private JButton[][] botones;
    private ActionListener ac;
    
    
    private Image X;
    private Image O;
    
    private boolean turno;
    
  
    public Cliente(Main frame){
        try {
            this.frame = frame;
            //Cargamos las imagenes de la X y O
            X = ImageIO.read(getClass().getResource("Jona.jpg"));
            O = ImageIO.read(getClass().getResource("James.png"));
            
            
         
            cliente = new Socket(host,puerto);
            in = new DataInputStream(cliente.getInputStream());
            out = new DataOutputStream(cliente.getOutputStream());
    
            botones = this.frame.getBotones();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    public void desconectar() {
        try {
            // Enviamos un mensaje especial al servidor para indicar que el cliente se desconectará
            out.writeUTF("DESCONECTAR");
            cliente.close(); // Cerramos la conexión del cliente
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
   
    
    @Override
    public void run() {
        try{
     
            mensaje =  in.readUTF();
            String split[] = mensaje.split(";");
            frame.cambioTexto(split[0]);
            String XO = split[0].split(" ")[1];
            turno = Boolean.valueOf(split[1]);
            
   
            while(true){
        
                mensaje = in.readUTF();
                
                String[] mensajes = mensaje.split(";");
                int xo = Integer.parseInt(mensajes[0]);
                int f = Integer.parseInt(mensajes[1]);
                int c = Integer.parseInt(mensajes[2]);
                
      
                if(xo == 1)
                    botones[f][c].setIcon(new ImageIcon(X));
                else
                    botones[f][c].setIcon(new ImageIcon(O));
      
                botones[f][c].removeActionListener(botones[f][c].getActionListeners()[0]);
                turno = !turno;
                
             
                if(XO.equals(mensajes[3])){
                    JOptionPane.showMessageDialog(frame, "FELICITACIONES, GANASTE LA PARTIDA :)");
                    new Main().setVisible(true);
                    frame.dispose();
                }else  if("EMPATE".equals(mensajes[3])){
                    JOptionPane.showMessageDialog(frame, "EMPATE!");
                    new Main().setVisible(true);
                    frame.dispose();
                }
                else  if(!"NADIE".equals(mensajes[3]) && !mensajes[3].equals(mensajes[0])){
                    JOptionPane.showMessageDialog(frame, "LAMENTABLEMNTE, PERDISTE LA PARTIDA :(");
                    new Main().setVisible(true);
                    frame.dispose();
                }
                
                
              
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    //envia jugada servidor
    public void enviarTurno(int f,int c){
 
        try {
            if(turno){
                String  datos = "";
                datos += f + ";";
                datos += c + ";";
                out.writeUTF(datos);
            }
            else{
                JOptionPane.showMessageDialog(frame, "Espera tu turno");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    } 
}
