package Controlador;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HiloCliente extends Thread {
	
	Socket socketCliente;
    String peticion;
    Object objeto;
    ObjectOutputStream objeto_salida;
    ObjectInputStream objeto_entrada;
    
    public HiloCliente(Socket socketCliente, String peticion, Object obj) {
        this.socketCliente = socketCliente;
        this.peticion = peticion;
        this.objeto = obj;

        try {
            objeto_salida = new ObjectOutputStream(socketCliente.getOutputStream());
        } catch (IOException ex) {
            // Logger.getLogger(HiloCliente.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullPointerException e) {
            JOptionPane.showMessageDialog(null, "Servidor desconectado", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    @Override
    public void run() {
        try {
            String mensaje = "";
            OutputStream out = socketCliente.getOutputStream();
            DataOutputStream flujo_salida = new DataOutputStream(out);
            InputStream aux = socketCliente.getInputStream();
            DataInputStream flujo_entrada = new DataInputStream(aux);
            ObjectInputStream objeto_entrada = new ObjectInputStream(socketCliente.getInputStream());

            flujo_salida.writeUTF(peticion);
            // flujo_salida.flush();

            switch (peticion) {
                case "alta":
                    //objeto_salida.writeObject(objeto);
                    //objeto_salida.flush();

                    mensaje = flujo_entrada.readUTF();

                   // if (mensaje.contains("existe")) {
                        JOptionPane.showMessageDialog(null, mensaje, "Prueba", JOptionPane.INFORMATION_MESSAGE);
                  /*  } else {
                        JOptionPane.showMessageDialog(null, mensaje, "Inserción completada", JOptionPane.INFORMATION_MESSAGE);
                    }*/
                    break;
                case "baja":
                    break;
                case "modificar":
                    break;
                case "consultar":
                    break;                  
                default:
                    break;
            }

        } catch (Exception ex) {
            //Si hay errores, cierro el cliente (p.ej., si se desconecta el servidor)

            try {
                if (socketCliente != null) {
                    socketCliente.close();
                }
            } catch (IOException ex1) {
                Logger.getLogger(HiloCliente.class.getName()).log(Level.SEVERE, null, ex1);
            }

            Logger.getLogger(HiloCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
}
