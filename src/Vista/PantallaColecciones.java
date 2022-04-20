package Vista;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTable;

import Controlador.HiloCliente;

public class PantallaColecciones {

	JFrame frmColecciones;
	private String[] opciones = { "Sí", "No" };
	private String mensajeSalir = "¿Deseas salir de la aplicación?", cerrarPrograma = "Cerrar programa";
	private String tituloPantalla = "Gestión de colecciones";
	private JMenu menuComics, menuColecciones, menuInformes;
	private JMenuItem itemBusqueda,itemComics,itemColecciones,itemInformes;
	private JTable tbColecciones;	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PantallaColecciones window = new PantallaColecciones(null);
					window.frmColecciones.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public PantallaColecciones(Socket skCliente) {
		initialize(skCliente);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(Socket skCliente) {
		frmColecciones = new JFrame();
		frmColecciones.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// dialogo confirmacion
				ImageIcon icono = new ImageIcon(PantallaPrincipal.class.getResource("/img/app_icon.png"));
				ImageIcon iconoEscala = new ImageIcon(
						icono.getImage().getScaledInstance(50, 50, java.awt.Image.SCALE_FAST));
				JLabel lblPregunta = new JLabel(mensajeSalir);
				lblPregunta.setFont(new Font("Caladea", Font.PLAIN, 16));
				int respuesta = JOptionPane.showOptionDialog(frmColecciones, lblPregunta,
						cerrarPrograma, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, iconoEscala,
						opciones, opciones[1]);

				if (respuesta == 1) { // si elige no cerrar, especifico que no haga nada
					frmColecciones.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

				} else if (respuesta == 0) {
					frmColecciones.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					
					//si cierra el programa, se cierra la conexion del cliente y se envia la orden de salir
					try {
			            if (skCliente != null) {
			                //mando orden salir y cierro conexion
			                HiloCliente hilo = new HiloCliente(skCliente, "fin", null);
			                hilo.start();
			                hilo.join();

			                skCliente.close();
			            }
			        } catch (IOException ex) {
			            Logger.getLogger(PantallaBusqueda.class.getName()).log(Level.SEVERE, null, ex);
			        } catch (InterruptedException ex) {
			            Logger.getLogger(PantallaBusqueda.class.getName()).log(Level.SEVERE, null, ex);
			        }
					
				} else { // si cierra el dialogo de confirmacion, no cierra la ventana principal
					frmColecciones.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				}
			}
		});
		
		frmColecciones.setMinimumSize(new Dimension(890, 760));
		frmColecciones.setTitle(tituloPantalla);
		frmColecciones.setBounds(0, 0, 450, 300);
		frmColecciones.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmColecciones.setLocationRelativeTo(null);
		frmColecciones.setIconImage(
				Toolkit.getDefaultToolkit().getImage(PantallaPrincipal.class.getResource("/img/app_icon.png")));
		
		JMenuBar menuBar = new JMenuBar();
		frmColecciones.setJMenuBar(menuBar);
		
		menuComics = new JMenu("Cómics");
		menuComics.setFont(new Font("Caladea", Font.PLAIN, 16));
		menuBar.add(menuComics);
		
		itemBusqueda = new JMenuItem("Consultar cómics");
		itemBusqueda.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmColecciones.dispose();

				PantallaBusqueda p = new PantallaBusqueda(skCliente);
				p.frmBusqueda.setVisible(true);
			}
		});
		itemBusqueda.setFont(new Font("Caladea", Font.PLAIN, 16));
		menuComics.add(itemBusqueda);
		
		itemComics = new JMenuItem("Gestión cómics");
		itemComics.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmColecciones.dispose();

				OperacionesComics o = new OperacionesComics(skCliente);
				o.frmComics.setVisible(true);
			}
		});		
		itemComics.setFont(new Font("Caladea", Font.PLAIN, 16));
		menuComics.add(itemComics);
		
		menuColecciones = new JMenu("Colecciones");
		menuColecciones.setFont(new Font("Caladea", Font.PLAIN, 16));
		menuBar.add(menuColecciones);
		
		itemColecciones = new JMenuItem("Gestión colecciones");
		itemColecciones.setFont(new Font("Caladea", Font.PLAIN, 16));
		menuColecciones.add(itemColecciones);
		
		menuInformes = new JMenu("Informes");
		menuInformes.setFont(new Font("Caladea", Font.PLAIN, 16));
		menuBar.add(menuInformes);
		
		itemInformes = new JMenuItem("Crear informes");
		itemInformes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

			}
		});
		itemInformes.setFont(new Font("Caladea", Font.PLAIN, 16));
		menuInformes.add(itemInformes);
	}

}
