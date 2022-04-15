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

import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextArea;

import Modelo.Coleccion;
import Modelo.Numero;
import Modelo.TablaComics;
import Vista.PantallaBusqueda;

import java.util.logging.Level;
import java.util.logging.Logger;

public class HiloCliente extends Thread {
	
	Socket socketCliente;
    String peticion;
    Object objeto;
    ObjectOutputStream objeto_salida;
    ObjectInputStream objeto_entrada;
    Coleccion coleccion;
    JTable tbComics;
    DefaultComboBoxModel<Coleccion> modeloCombo;
    
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
    
    public HiloCliente(Socket socketCliente, String peticion, Object obj, Coleccion col) {
        this.socketCliente = socketCliente;
        this.peticion = peticion;
        this.objeto = obj;
        this.coleccion = col;
 
        try {
            objeto_salida = new ObjectOutputStream(socketCliente.getOutputStream());
        } catch (IOException ex) {
            // Logger.getLogger(HiloCliente.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullPointerException e) {
            JOptionPane.showMessageDialog(null, "Servidor desconectado", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public HiloCliente(Socket socketCliente, String peticion, Object obj, JTable tbComic) {
        this.socketCliente = socketCliente;
        this.peticion = peticion;
        this.objeto = obj;
        tbComics = tbComic;

        try {
            objeto_salida = new ObjectOutputStream(socketCliente.getOutputStream());
        } catch (IOException ex) {
            // Logger.getLogger(HiloCliente.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullPointerException e) {
            JOptionPane.showMessageDialog(null, "Servidor desconectado", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public HiloCliente(Socket socketCliente, String peticion, DefaultComboBoxModel<Coleccion> modeloCombo) {
        this.socketCliente = socketCliente;
        this.peticion = peticion;
        this.modeloCombo = modeloCombo;

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

                    if (mensaje.contains("existe")) {
                        JOptionPane.showMessageDialog(null, mensaje, "Prueba", JOptionPane.ERROR_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, mensaje, "Inserción completada", JOptionPane.INFORMATION_MESSAGE);
                    }
                    break;
                case "baja":
                    break;
                case "modificar":
                    break;
                case "cargarComics": ArrayList<Numero> comics = (ArrayList<Numero>) objeto_entrada.readObject();
                
                		PantallaBusqueda.listaComics = comics;
                		
                		tbComics.setModel(new TablaComics(comics,socketCliente));;
                    break;    
                case "colByComic": objeto_salida.writeObject(objeto);
                
                		coleccion = (Coleccion) objeto_entrada.readObject();
                		
                		TablaComics.coleccion = coleccion;
                		
                	break;
                case "cargarComicsPorCol": String nombreCol = (String) objeto;
                		
                		flujo_salida.writeUTF(nombreCol);
                
                		ArrayList<Numero> comicsColeccion = (ArrayList<Numero>) objeto_entrada.readObject();
                
        				PantallaBusqueda.listaComics = comicsColeccion;
        
        				tbComics.setModel(new TablaComics(comicsColeccion,socketCliente));
                	break;
                case "cargarComicsPorTitulo": String titulo = (String) objeto;
        		
        				flujo_salida.writeUTF(titulo);
        
        				ArrayList<Numero> comicsTitulo = (ArrayList<Numero>) objeto_entrada.readObject();
        
        				PantallaBusqueda.listaComics = comicsTitulo;
        				
        				tbComics.setModel(new TablaComics(comicsTitulo,socketCliente));
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
