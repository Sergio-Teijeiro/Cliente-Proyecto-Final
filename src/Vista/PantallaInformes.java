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

public class PantallaInformes {

	JFrame frmInformes;
	private String[] opciones = { "Sí", "No" };
	private String mensajeSalir = "¿Deseas salir de la aplicación?", cerrarPrograma = "Cerrar programa";
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
	
	private ArrayList<String> nombresCols = new ArrayList<String>();

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PantallaInformes window = new PantallaInformes(null);
					window.frmInformes.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public PantallaInformes(Socket skCliente) {
		//reconectar con el servidor si se desconecto
		if (skCliente.isClosed()) {
			try {
				skCliente = new Socket("192.168.56.101", 2000);
			} catch (Exception ex) {
	            if (ex.getClass().getName().equals("java.net.ConnectException")) {
	            	JLabel lblError = new JLabel("No se ha podido conectar con el servidor");
	            	lblError.setFont(new Font("Caladea", Font.PLAIN, 20));
	            	JOptionPane.showMessageDialog(frmInformes,lblError, "Error al conectar",
	            			JOptionPane.ERROR_MESSAGE);
	            }
			}
		}
		
		initialize(skCliente);
	}

	/**
	 * Initialize the contents of the frame.
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
				JLabel lblPregunta = new JLabel(mensajeSalir);
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
			            Logger.getLogger(PantallaBusqueda.class.getName()).log(Level.SEVERE, null, ex);
			        } catch (InterruptedException ex) {
			            Logger.getLogger(PantallaBusqueda.class.getName()).log(Level.SEVERE, null, ex);
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
		
		itemComics = new JMenuItem("Gestión cómics");
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
		
		itemColecciones = new JMenuItem("Gestión colecciones");
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
		
		btnInformeColecciones = new JButton("Informe de todas las colecciones");
		btnInformeColecciones.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				HiloCliente hilo = new HiloCliente(skCliente, "informeColecciones", null);
				hilo.start();
				
				try {
					hilo.join();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
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
		
		btnInformeColeccionesNombre = new JButton("Informe de colecciones por nombre");
		btnInformeColeccionesNombre.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (cmbColecciones.getSelectedItem() == null) {
	            	JLabel lblError = new JLabel("Debes seleccionar una colección");
	            	lblError.setFont(new Font("Caladea", Font.PLAIN, 20));
	            	JOptionPane.showMessageDialog(frmInformes,lblError, "Error",
	            			JOptionPane.ERROR_MESSAGE);
				} else {
					
					Coleccion coleccion = (Coleccion) cmbColecciones.getSelectedItem();
					
					coleccion.setNombre(nombresCols.get(cmbColecciones.getSelectedIndex()));
					
					HiloCliente hilo = new HiloCliente(skCliente, "informeColPorNombre", coleccion);
					hilo.start();
					
					try {
						hilo.join();
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
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
		
		btnInformeComics = new JButton("Informe de todos los c\u00F3mics");
		btnInformeComics.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				HiloCliente hilo = new HiloCliente(skCliente, "informeComics", null);
				hilo.start();
				
				try {
					hilo.join();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
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
		btnInformeComicsCol.setFont(new Font("Caladea", Font.PLAIN, 20));
		panelInforme4.add(btnInformeComicsCol);
		
		JPanel panel = new JPanel();
		FlowLayout flowLayout_4 = (FlowLayout) panel.getLayout();
		flowLayout_4.setVgap(165);
		panelPrincipal.add(panel);
		
		cargarColecciones(skCliente);
	}

	private void cargarColecciones(Socket skCliente) {
		modeloCombo.removeAllElements();
		modeloCombo2.removeAllElements();
		nombresCols.clear();
		
		HiloCliente hilo = new HiloCliente(skCliente,"cargarColecciones",modeloCombo);
		hilo.start();
		
		try {
			hilo.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for (int i=0;i<modeloCombo.getSize();i++) {
			nombresCols.add(modeloCombo.getElementAt(i).getNombre());
			
			if (modeloCombo.getElementAt(i).getNombre().length() > 45) {
				String nuevoNombre = modeloCombo.getElementAt(i).getNombre().substring(0, 41)+"...";
				modeloCombo.getElementAt(i).setNombre(nuevoNombre);
			}
		}
		
		for (int i=0;i<modeloCombo.getSize();i++) {
			Coleccion col = modeloCombo.getElementAt(i);
			
			modeloCombo2.addElement(col);
		}
		
	}

}