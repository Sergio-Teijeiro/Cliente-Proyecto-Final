package Vista;

import java.awt.BorderLayout;
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

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import Controlador.HiloCliente;

public class OperacionesComics {

	JFrame frmComics;
	private String[] opciones = { "Sí", "No" };
	private String mensajeSalir = "¿Deseas salir de la aplicación?", cerrarPrograma = "Cerrar programa";
	private String tituloPantalla = "Gestión de cómics";
	private JMenu menuComics, menuColecciones, menuInformes;
	private JMenuItem itemBusqueda,itemComics,itemColecciones,itemInformes;
	private JTable tbComics;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					OperacionesComics window = new OperacionesComics(null);
					window.frmComics.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public OperacionesComics(Socket skCliente) {
		initialize(skCliente);
	}

	/**
	 * Initialize the contents of the frame.
	 * @param skCliente 
	 */
	private void initialize(Socket skCliente) {
		frmComics = new JFrame();
		frmComics.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// dialogo confirmacion
				ImageIcon icono = new ImageIcon(PantallaPrincipal.class.getResource("/img/app_icon.png"));
				ImageIcon iconoEscala = new ImageIcon(
						icono.getImage().getScaledInstance(50, 50, java.awt.Image.SCALE_FAST));
				int respuesta = JOptionPane.showOptionDialog(frmComics, mensajeSalir,
						cerrarPrograma, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, iconoEscala,
						opciones, opciones[1]);

				if (respuesta == 1) { // si elige no cerrar, especifico que no haga nada
					frmComics.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

				} else if (respuesta == 0) {
					frmComics.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					
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
					frmComics.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				}
			}
		});
		
		frmComics.setMinimumSize(new Dimension(890, 760));
		frmComics.setTitle(tituloPantalla);
		frmComics.setBounds(0, 0, 450, 300);
		frmComics.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmComics.setLocationRelativeTo(null);
		frmComics.setIconImage(
				Toolkit.getDefaultToolkit().getImage(PantallaPrincipal.class.getResource("/img/app_icon.png")));
		
		JMenuBar menuBar = new JMenuBar();
		frmComics.setJMenuBar(menuBar);
		
		menuComics = new JMenu("Cómics");
		menuComics.setFont(new Font("Caladea", Font.PLAIN, 16));
		menuBar.add(menuComics);
		
		itemBusqueda = new JMenuItem("Consultar cómics");
		itemBusqueda.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmComics.dispose();

				PantallaBusqueda p = new PantallaBusqueda(skCliente);
				p.frmBusqueda.setVisible(true);
			}
		});
		itemBusqueda.setFont(new Font("Caladea", Font.PLAIN, 16));
		menuComics.add(itemBusqueda);
		
		itemComics = new JMenuItem("Gestión cómics");
		itemComics.setFont(new Font("Caladea", Font.PLAIN, 16));
		menuComics.add(itemComics);
		
		menuColecciones = new JMenu("Colecciones");
		menuColecciones.setFont(new Font("Caladea", Font.PLAIN, 16));
		menuBar.add(menuColecciones);
		
		itemColecciones = new JMenuItem("Gestión colecciones");
		itemColecciones.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

			}
		});
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
		
		JScrollPane cabeceraTabla = new JScrollPane();
		cabeceraTabla.setBounds(0, 0, 594, 299);

		tbComics = new JTable();
		tbComics.setPreferredScrollableViewportSize(new Dimension(450, 250));
		tbComics.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
		tbComics.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tbComics.setBounds(60, 23, 506, 209);
		tbComics.getTableHeader().setReorderingAllowed(false); // impedir mover columnas

		JPanel panelTabla = new JPanel();
		frmComics.getContentPane().add(panelTabla, BorderLayout.SOUTH);
		panelTabla.setLayout(new BorderLayout(0, 0));

		tbComics.setFillsViewportHeight(true);
		cabeceraTabla.setViewportView(tbComics);

		panelTabla.add(cabeceraTabla, BorderLayout.CENTER);

		JPanel panelPrincipal = new JPanel();
		frmComics.getContentPane().add(panelPrincipal, BorderLayout.CENTER);
		panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
	}

}
