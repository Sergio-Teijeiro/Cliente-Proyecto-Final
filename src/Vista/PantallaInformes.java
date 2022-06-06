package Vista;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import Controlador.HiloCliente;
import Modelo.Coleccion;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;

/**
 * Pantalla para generar y consultar informes creados a partir de los datos de la base de datos, algunos de ellos con filtros proporcionados por el usuario
 * @author sergio
 *
 */
public class PantallaInformes {

	JFrame frmInformes;
	private String[] opciones = { "Sí", "No" };
	private String mensajeSalir = "¿Deseas salir de la aplicación?", cerrarPrograma = "Cerrar programa", errorConexion = "No se ha podido conectar con el servidor";
	private String tituloPantalla = "Creación de informes";
	private JMenu menuComics, menuColecciones, menuInformes;
	private JMenuItem itemBusqueda,itemComics,itemColecciones,itemInformes;
	private JPanel panelPrincipal;
	private JButton btnInformeColecciones;
	private JPanel panelInforme2;
	private JLabel lblColeccion;
	private JComboBox cmbColecciones;
	private JButton btnInformeColeccionesNombre;
	private JPanel panelInforme3;
	private JButton btnInformeComics;
	private JPanel panelInforme4;
	private JLabel lblColeccion2;
	private JComboBox cmbColecciones2;
	private JButton btnInformeComicsCol;
	private DefaultComboBoxModel<Coleccion> modeloCombo = new DefaultComboBoxModel<Coleccion>();
	private DefaultComboBoxModel<Coleccion> modeloCombo2 = new DefaultComboBoxModel<Coleccion>();
	JLabel lblError = new JLabel(errorConexion);
	JLabel lblPregunta = new JLabel(mensajeSalir);
	private String rutaAyuda = "informes";
	private int offset = 0;
	private String errorConectar = "Error al conectar", gestionComics = "Gestión cómics", colecciones = "Colecciones", gestionColecciones = "Gestión colecciones",error = "Error";
	private String btnInformeColeccionesValor = "Informe de todas las colecciones", btnInformeColeccionesNombreValor = "Informe de colecciones por nombre";
	private String seleccionarCol = "Debes seleccionar una colección", btnInformeComicsValor = "Informe de todos los cómics";
	private JLabel lblErrorCol = new JLabel(seleccionarCol);

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PantallaInformes window = new PantallaInformes(null);
					window.frmInformes.setVisible(true);
				} catch (Exception e) {
					//e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Constructor de la pantalla que, antes de inicializar la pantalla, reconecta con el servidor si se perdió la conexión
	 * @param skCliente Socket del cliente
	 */
	public PantallaInformes(Socket skCliente) {
		//reconectar con el servidor si se desconecto
		if (skCliente.isClosed()) {
			try {
				skCliente = new Socket("192.168.56.101", 2000);
			} catch (Exception ex) {
	            if (ex.getClass().getName().equals("java.net.ConnectException")) {
	            	lblError.setFont(new Font("Caladea", Font.PLAIN, 20));
	            	JOptionPane.showMessageDialog(frmInformes,lblError, errorConectar,
	            			JOptionPane.ERROR_MESSAGE);
	            }
			}
		}
		
		initialize(skCliente);
	}

	/**
	 * Inicializa la pantalla
	 * @param skCliente Socket del cliente
	 */
	private void initialize(Socket skCliente) {
		frmInformes = new JFrame();
		frmInformes.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// dialogo confirmacion
				ImageIcon icono = new ImageIcon(PantallaPrincipal.class.getResource("/img/app_icon.png"));
				ImageIcon iconoEscala = new ImageIcon(
						icono.getImage().getScaledInstance(50, 50, java.awt.Image.SCALE_FAST));
				
				lblPregunta.setFont(new Font("Caladea", Font.PLAIN, 20));
				int respuesta = JOptionPane.showOptionDialog(frmInformes, lblPregunta,
						cerrarPrograma, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, iconoEscala,
						opciones, opciones[1]);

				if (respuesta == 1) { // si elige no cerrar, especifico que no haga nada
					frmInformes.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

				} else if (respuesta == 0) {
					frmInformes.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					
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
			            //Logger.getLogger(PantallaBusqueda.class.getName()).log(Level.SEVERE, null, ex);
			        } catch (InterruptedException ex) {
			            //Logger.getLogger(PantallaBusqueda.class.getName()).log(Level.SEVERE, null, ex);
			        }
					
				} else { // si cierra el dialogo de confirmacion, no cierra la ventana principal
					frmInformes.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				}
			}
		});
		
		frmInformes.setMinimumSize(new Dimension(890, 760));
		frmInformes.setTitle(tituloPantalla);
		frmInformes.setBounds(0, 0, 450, 300);
		frmInformes.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmInformes.setLocationRelativeTo(null);
		frmInformes.setIconImage(
				Toolkit.getDefaultToolkit().getImage(PantallaPrincipal.class.getResource("/img/app_icon.png")));
		
		JMenuBar menuBar = new JMenuBar();
		frmInformes.setJMenuBar(menuBar);
		
		menuComics = new JMenu("Cómics");
		menuComics.setFont(new Font("Caladea", Font.PLAIN, 16));
		menuBar.add(menuComics);
		
		itemBusqueda = new JMenuItem("Consultar cómics");
		itemBusqueda.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmInformes.dispose();

				PantallaBusqueda p = new PantallaBusqueda(skCliente);
				p.frmBusqueda.setVisible(true);
			}
		});
		itemBusqueda.setFont(new Font("Caladea", Font.PLAIN, 16));
		menuComics.add(itemBusqueda);
		
		itemComics = new JMenuItem(gestionComics);
		itemComics.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmInformes.dispose();

				OperacionesComics o = new OperacionesComics(skCliente);
				o.frmComics.setVisible(true);
			}
		});		
		itemComics.setFont(new Font("Caladea", Font.PLAIN, 16));
		menuComics.add(itemComics);
		
		menuColecciones = new JMenu("Colecciones");
		menuColecciones.setFont(new Font("Caladea", Font.PLAIN, 16));
		menuBar.add(menuColecciones);
		
		itemColecciones = new JMenuItem(gestionColecciones);
		itemColecciones.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmInformes.dispose();

				PantallaColecciones p = new PantallaColecciones(skCliente);
				p.frmColecciones.setVisible(true);
			}
		});
		itemColecciones.setFont(new Font("Caladea", Font.PLAIN, 16));
		menuColecciones.add(itemColecciones);
		
		menuInformes = new JMenu("Informes");
		menuInformes.setFont(new Font("Caladea", Font.PLAIN, 16));
		menuBar.add(menuInformes);
		
		itemInformes = new JMenuItem("Crear informes");
		itemInformes.setFont(new Font("Caladea", Font.PLAIN, 16));
		menuInformes.add(itemInformes);
		
		panelPrincipal = new JPanel();
		frmInformes.getContentPane().add(panelPrincipal, BorderLayout.CENTER);
		panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
		
		JPanel panelInforme1 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panelInforme1.getLayout();
		flowLayout.setVgap(25);
		flowLayout.setHgap(25);
		flowLayout.setAlignment(FlowLayout.LEFT);
		panelPrincipal.add(panelInforme1);
		
		btnInformeColecciones = new JButton(btnInformeColeccionesValor);
		btnInformeColecciones.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				HiloCliente hilo = new HiloCliente(skCliente, "informeColecciones", null);
				hilo.start();
				
				try {
					hilo.join();
				} catch (InterruptedException e1) {
					//e1.printStackTrace();
				}
			}
		});
		btnInformeColecciones.setFont(new Font("Caladea", Font.PLAIN, 20));
		panelInforme1.add(btnInformeColecciones);
		
		panelInforme2 = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panelInforme2.getLayout();
		flowLayout_1.setVgap(25);
		flowLayout_1.setHgap(25);
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		panelPrincipal.add(panelInforme2);
		
		lblColeccion = new JLabel("Colecci\u00F3n");
		lblColeccion.setFont(new Font("Caladea", Font.PLAIN, 20));
		panelInforme2.add(lblColeccion);
		
		cmbColecciones = new JComboBox();
		
		cmbColecciones.setModel(modeloCombo);
		cmbColecciones.setFont(new Font("Caladea", Font.PLAIN, 18));
		
		panelInforme2.add(cmbColecciones);
		
		btnInformeColeccionesNombre = new JButton(btnInformeColeccionesNombreValor);
		btnInformeColeccionesNombre.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (cmbColecciones.getSelectedItem() == null) {
	            	lblErrorCol.setFont(new Font("Caladea", Font.PLAIN, 20));
	            	JOptionPane.showMessageDialog(frmInformes,lblErrorCol,error,
	            			JOptionPane.ERROR_MESSAGE);
				} else {
					
					Coleccion coleccion = (Coleccion) cmbColecciones.getSelectedItem();
					
					HiloCliente hilo = new HiloCliente(skCliente, "informeColPorNombre", coleccion);
					hilo.start();
					
					try {
						hilo.join();
					} catch (InterruptedException e1) {
						//e1.printStackTrace();
					}
				}
			}
		});
		btnInformeColeccionesNombre.setFont(new Font("Caladea", Font.PLAIN, 20));
		panelInforme2.add(btnInformeColeccionesNombre);
		
		panelInforme3 = new JPanel();
		FlowLayout flowLayout_2 = (FlowLayout) panelInforme3.getLayout();
		flowLayout_2.setVgap(25);
		flowLayout_2.setHgap(25);
		flowLayout_2.setAlignment(FlowLayout.LEFT);
		panelPrincipal.add(panelInforme3);
		
		btnInformeComics = new JButton(btnInformeComicsValor);
		btnInformeComics.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					
				HiloCliente hilo = new HiloCliente(skCliente, "informeComics", null);
				hilo.start();

				try {
					hilo.join();
				} catch (InterruptedException e1) {
					// e1.printStackTrace();
				}
			}
		});
		btnInformeComics.setFont(new Font("Caladea", Font.PLAIN, 20));
		panelInforme3.add(btnInformeComics);
		
		panelInforme4 = new JPanel();
		FlowLayout flowLayout_3 = (FlowLayout) panelInforme4.getLayout();
		flowLayout_3.setVgap(25);
		flowLayout_3.setHgap(25);
		flowLayout_3.setAlignment(FlowLayout.LEFT);
		panelPrincipal.add(panelInforme4);
		
		lblColeccion2 = new JLabel("Colecci\u00F3n");
		lblColeccion2.setFont(new Font("Caladea", Font.PLAIN, 20));
		panelInforme4.add(lblColeccion2);
		
		cmbColecciones2 = new JComboBox();
		
		cmbColecciones2.setModel(modeloCombo2);
		cmbColecciones2.setFont(new Font("Caladea", Font.PLAIN, 18));
		
		panelInforme4.add(cmbColecciones2);
		
		btnInformeComicsCol = new JButton("Informe de c\u00F3mics por colecci\u00F3n");
		btnInformeComicsCol.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (cmbColecciones2.getSelectedItem() == null) {
					lblErrorCol.setFont(new Font("Caladea", Font.PLAIN, 20));
					JOptionPane.showMessageDialog(frmInformes, lblErrorCol, error, JOptionPane.ERROR_MESSAGE);
				} else {
					Coleccion coleccion = (Coleccion) cmbColecciones2.getSelectedItem();

					HiloCliente hilo = new HiloCliente(skCliente, "informeComicsPorCol", coleccion);
					hilo.start();

					try {
						hilo.join();
					} catch (InterruptedException e1) {
						//e1.printStackTrace();
					}
				}
			}
		});
		btnInformeComicsCol.setFont(new Font("Caladea", Font.PLAIN, 20));
		panelInforme4.add(btnInformeComicsCol);
		
		JPanel panel = new JPanel();
		FlowLayout flowLayout_4 = (FlowLayout) panel.getLayout();
		flowLayout_4.setVgap(165);
		panelPrincipal.add(panel);
		
		cargarColecciones(skCliente);
		
		cmbColecciones.setPrototypeDisplayValue("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
		cmbColecciones2.setPrototypeDisplayValue("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
		
		PantallaPrincipal.helpBroker.enableHelpKey(btnInformeColecciones, rutaAyuda, PantallaPrincipal.helpSet);
		PantallaPrincipal.helpBroker.enableHelpKey(btnInformeColeccionesNombre, rutaAyuda, PantallaPrincipal.helpSet);
		PantallaPrincipal.helpBroker.enableHelpKey(btnInformeComics, rutaAyuda, PantallaPrincipal.helpSet);
		PantallaPrincipal.helpBroker.enableHelpKey(btnInformeComicsCol, rutaAyuda, PantallaPrincipal.helpSet);
		
		traducir();
	}

	/**
	 * Traduce la pantalla actual según el idioma establecido por el usuario.
	 */
	private void traducir() {
		ResourceBundle rb = ResourceBundle.getBundle("traduccion");
		
		mensajeSalir = rb.getString("mensajeSalir");
		opciones[0] = rb.getString("si");
		opciones[1] = rb.getString("no");
		cerrarPrograma = rb.getString("cerrarPrograma");
		errorConexion = rb.getString("errorConexion");
		errorConectar = rb.getString("errorConectar");
		gestionComics = rb.getString("gestionComics");
		colecciones = rb.getString("colecciones");
		gestionColecciones = rb.getString("gestionColecciones");
		error = rb.getString("error");
		btnInformeColeccionesValor = rb.getString("btnInformeColeccionesValor");
		btnInformeColeccionesNombreValor = rb.getString("btnInformeColeccionesNombreValor");
		seleccionarCol = rb.getString("seleccionarCol");
		btnInformeComicsValor = rb.getString("btnInformeComicsValor");
		
		lblPregunta.setText(mensajeSalir);
		lblError.setText(errorConexion);
		itemComics.setText(gestionComics);
		menuColecciones.setText(colecciones);
		itemColecciones.setText(gestionColecciones);
		btnInformeColecciones.setText(btnInformeColeccionesValor);
		btnInformeColeccionesNombre.setText(btnInformeColeccionesNombreValor);
		lblErrorCol.setText(seleccionarCol);
		btnInformeComics.setText(btnInformeComicsValor);
		
	}

	/**
	 * Carga todas las colecciones en los combos
	 * @param skCliente Socket del cliente
	 */
	private void cargarColecciones(Socket skCliente) {
		modeloCombo.removeAllElements();
		modeloCombo2.removeAllElements();
		
		HiloCliente hilo = new HiloCliente(skCliente,"cargarColecciones",modeloCombo);
		hilo.start();
		
		try {
			hilo.join();
		} catch (InterruptedException e) {
			//e.printStackTrace();
		}
		
		for (int i=0;i<modeloCombo.getSize();i++) {
			Coleccion col = modeloCombo.getElementAt(i);
			
			modeloCombo2.addElement(col);
		}
		
	}

}