package Vista;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.JTableHeader;

import Controlador.HiloCliente;
import Fuentes.Fuentes;
import Modelo.Coleccion;
import Modelo.Numero;
import Modelo.TablaComics;

import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.JButton;
import java.awt.Insets;

public class PantallaBusqueda {

	JFrame frmBusqueda;
	private String[] opciones = { "Sí", "No" };
	private String mensajeSalir = "¿Deseas salir de la aplicación?", cerrarPrograma = "Cerrar programa", errorConexion = "No se ha podido conectar con el servidor";
	private String tituloPantalla = "Catálogo de cómics";
	private JMenu menuComics, menuColecciones, menuInformes;
	private JMenuItem itemBusqueda,itemComics,itemColecciones,itemInformes;
	private JLabel lblTitulo;
	private JPanel panelBusquedaCol;
	private JLabel lblColeccion;
	private JTextField txtColeccion;
	private JButton btnBuscarPorCol;
	private JPanel panelBusquedaComic;
	private JLabel lblComic;
	private JTextField txtComic;
	private JButton btnBuscarPorTitulo;
	private JTable tbComics;
	private String rutaAyuda = "busqueda";
	private int offset = 0;
	private boolean buscando = false;
	private JLabel lblPregunta = new JLabel(mensajeSalir);
	JLabel lblError = new JLabel(errorConexion);
	JLabel lblErrorAyuda = new JLabel("");
	JLabel lblNoAnteriores = new JLabel("");
	JLabel lblNoPosteriores = new JLabel("");
	
	public static ArrayList<Numero> listaComics = new ArrayList<>();
	public static int numComics;
	private JPanel panelBotones;
	private JButton btnIzquierda;
	private JButton btnDerecha;
	private String errorConectar = "Error al conectar", gestionComics = "Gestión cómics", colecciones = "Colecciones", gestionColecciones = "Gestión colecciones";
	private String mensajeAyuda = "Comprueba la ayuda para ver la longitud máxima de cada campo", error = "Error", tooltipRegistrosAnteriores = "Mostrar 100 registros anteriores";
	private String noRegistrosAntes = "No hay registros anteriores", primerosRegistros = "Primeros registros", tooltipRegistrosPosteriores = "Mostrar 100 registros posteriores";
	private String noRegistrosDespues = "No hay registros posteriores", ultimosRegistros = "Últimos registros";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PantallaBusqueda window = new PantallaBusqueda(null);
					window.frmBusqueda.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * @param skCliente 
	 */
	public PantallaBusqueda(Socket skCliente) {
		//reconectar con el servidor si se desconecto
		if (skCliente.isClosed()) {
			try {
				skCliente = new Socket("192.168.56.101", 2000);
			} catch (Exception ex) {
	            if (ex.getClass().getName().equals("java.net.ConnectException")) {
	            	
	            	lblError.setFont(new Font("Caladea", Font.PLAIN, 20));
	            	JOptionPane.showMessageDialog(frmBusqueda,lblError, errorConectar,
	            			JOptionPane.ERROR_MESSAGE);
	            }
			}
		}
		
		initialize(skCliente);
	}

	/**
	 * Initialize the contents of the frame.
	 * @param skCliente 
	 */
	private void initialize(Socket skCliente) {
		frmBusqueda = new JFrame();
		frmBusqueda.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// dialogo confirmacion
				ImageIcon icono = new ImageIcon(PantallaPrincipal.class.getResource("/img/app_icon.png"));
				ImageIcon iconoEscala = new ImageIcon(
						icono.getImage().getScaledInstance(50, 50, java.awt.Image.SCALE_FAST));
				lblPregunta.setFont(new Font("Caladea", Font.PLAIN, 20));
				int respuesta = JOptionPane.showOptionDialog(frmBusqueda, lblPregunta,
						cerrarPrograma, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, iconoEscala,
						opciones, opciones[1]);

				if (respuesta == 1) { // si elige no cerrar, especifico que no haga nada
					frmBusqueda.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

				} else if (respuesta == 0) {
					frmBusqueda.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					
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
					frmBusqueda.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				}
			}
		});
		
		frmBusqueda.setMinimumSize(new Dimension(890, 760));
		frmBusqueda.setTitle(tituloPantalla);
		frmBusqueda.setBounds(0, 0, 450, 300);
		frmBusqueda.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmBusqueda.setLocationRelativeTo(null);
		frmBusqueda.setIconImage(
				Toolkit.getDefaultToolkit().getImage(PantallaPrincipal.class.getResource("/img/app_icon.png")));
		
		JMenuBar menuBar = new JMenuBar();
		frmBusqueda.setJMenuBar(menuBar);
		
		menuComics = new JMenu("Cómics");
		menuComics.setFont(new Font("Caladea", Font.PLAIN, 16));
		menuBar.add(menuComics);
		
		itemBusqueda = new JMenuItem("Consultar cómics");
		itemBusqueda.setFont(new Font("Caladea", Font.PLAIN, 16));
		menuComics.add(itemBusqueda);
		
		itemComics = new JMenuItem(gestionComics);
		itemComics.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmBusqueda.dispose();

				OperacionesComics o = new OperacionesComics(skCliente);
				o.frmComics.setVisible(true);
			}
		});
		itemComics.setFont(new Font("Caladea", Font.PLAIN, 16));
		menuComics.add(itemComics);
		
		menuColecciones = new JMenu(colecciones);
		menuColecciones.setFont(new Font("Caladea", Font.PLAIN, 16));
		menuBar.add(menuColecciones);
		
		itemColecciones = new JMenuItem(gestionColecciones);
		itemColecciones.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmBusqueda.dispose();

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
		itemInformes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmBusqueda.dispose();

				PantallaInformes p = new PantallaInformes(skCliente);
				p.frmInformes.setVisible(true);
			}
		});
		itemInformes.setFont(new Font("Caladea", Font.PLAIN, 16));
		menuInformes.add(itemInformes);
		
		JPanel panelPrincipal = new JPanel();
		frmBusqueda.getContentPane().add(panelPrincipal, BorderLayout.CENTER);
		panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));

		JPanel panelTitulo = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panelTitulo.getLayout();
		flowLayout_1.setHgap(35);
		flowLayout_1.setVgap(20);
		panelPrincipal.add(panelTitulo);

		lblTitulo = new JLabel("CATÁLOGO DE CÓMICS");
		lblTitulo.setBorder(new EmptyBorder(25, 5, 0, 5));
		
		Fuentes fuentes = new Fuentes();
		Font fuenteTitulo = fuentes.getFuente(fuentes.BOUNCY, Font.BOLD, 60);
		
		lblTitulo.setFont(fuenteTitulo);
		panelTitulo.add(lblTitulo);
		
		panelBusquedaCol = new JPanel();
		FlowLayout fl_panelBusquedaCol = (FlowLayout) panelBusquedaCol.getLayout();
		fl_panelBusquedaCol.setHgap(20);
		panelPrincipal.add(panelBusquedaCol);
		
		lblColeccion = new JLabel("Colecci\u00F3n");
		lblColeccion.setFont(new Font("Caladea", Font.PLAIN, 20));
		panelBusquedaCol.add(lblColeccion);
		
		txtColeccion = new JTextField();
		txtColeccion.setFont(new Font("Caladea", Font.PLAIN, 20));
		panelBusquedaCol.add(txtColeccion);
		txtColeccion.setColumns(30);
		
		btnBuscarPorCol = new JButton("");
		btnBuscarPorCol.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Buscar los comics de la coleccion especificada
				txtComic.setText("");
				buscando = true;
				
				if (!txtColeccion.getText().isBlank()) {
					if (txtColeccion.getText().length() > 200) {
						lblErrorAyuda.setFont(new Font("Caladea", Font.PLAIN, 16));
						JOptionPane.showMessageDialog(frmBusqueda, lblErrorAyuda, error,
								JOptionPane.ERROR_MESSAGE);
					} else {
						cargarComicsPorColeccion(skCliente,txtColeccion.getText());
					}
				} else {
					buscando = false;
					offset = 0;
					cargarComics(skCliente);
				}
			}
		});
		btnBuscarPorCol.setBorder(new EmptyBorder(0, 0, 0, 0));
		btnBuscarPorCol.setToolTipText("Buscar por colección");
		btnBuscarPorCol.setMaximumSize(new Dimension(40, 40));
		btnBuscarPorCol.setBounds(5, 5, 20, 20);
		btnBuscarPorCol.setFocusPainted(false);
		btnBuscarPorCol.setContentAreaFilled(false);
		btnBuscarPorCol.setBorderPainted(false);
		
		ImageIcon icono = new ImageIcon(PantallaBusqueda.class.getResource("/img/lupa.png"));
		btnBuscarPorCol.setMaximumSize(new Dimension(40, 40));
		ImageIcon iconoEscala = new ImageIcon(icono.getImage().getScaledInstance(btnBuscarPorCol.getWidth(),
				btnBuscarPorCol.getHeight(), java.awt.Image.SCALE_FAST));
		btnBuscarPorCol.setIcon(iconoEscala);

		panelBusquedaCol.add(btnBuscarPorCol);
		
		panelBusquedaComic = new JPanel();
		FlowLayout fl_panelBusquedaComic = (FlowLayout) panelBusquedaComic.getLayout();
		fl_panelBusquedaComic.setHgap(20);
		panelPrincipal.add(panelBusquedaComic);
		
		lblComic = new JLabel("C\u00F3mic");
		lblComic.setBorder(new EmptyBorder(0, 0, 0, 30));
		lblComic.setFont(new Font("Caladea", Font.PLAIN, 20));
		panelBusquedaComic.add(lblComic);
		
		txtComic = new JTextField();
		txtComic.setFont(new Font("Caladea", Font.PLAIN, 20));
		txtComic.setColumns(30);
		panelBusquedaComic.add(txtComic);
		
		btnBuscarPorTitulo = new JButton("");
		btnBuscarPorTitulo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Buscar los comics del titulo especificado
				txtColeccion.setText("");
				buscando = true;
				
				if (!txtComic.getText().isBlank()) {
					if (txtComic.getText().length() > 200) {
						lblErrorAyuda.setFont(new Font("Caladea", Font.PLAIN, 16));
						JOptionPane.showMessageDialog(frmBusqueda, lblErrorAyuda, error,
								JOptionPane.ERROR_MESSAGE);
					} else {
						cargarComicsPorTitulo(skCliente,txtComic.getText());
					}
				} else {
					buscando = false;
					offset = 0;
					cargarComics(skCliente);
				}
			}
		});
		btnBuscarPorTitulo.setToolTipText("Buscar por cómic");
		btnBuscarPorTitulo.setMaximumSize(new Dimension(40, 40));
		btnBuscarPorTitulo.setFocusPainted(false);
		btnBuscarPorTitulo.setContentAreaFilled(false);
		btnBuscarPorTitulo.setBorderPainted(false);
		btnBuscarPorTitulo.setBorder(new EmptyBorder(0, 0, 0, 0));
		
		ImageIcon icono2 = new ImageIcon(PantallaBusqueda.class.getResource("/img/lupa.png"));
		btnBuscarPorTitulo.setMaximumSize(new Dimension(40, 40));
		ImageIcon iconoEscala2 = new ImageIcon(icono2.getImage().getScaledInstance(btnBuscarPorCol.getWidth(),
				btnBuscarPorCol.getHeight(), java.awt.Image.SCALE_FAST));
		btnBuscarPorTitulo.setIcon(iconoEscala2);
		
		panelBusquedaComic.add(btnBuscarPorTitulo);
		
		JScrollPane cabeceraTabla = new JScrollPane();
		cabeceraTabla.setBounds(0,0,594,299);
		
		tbComics = new JTable();
		tbComics.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					if (!listaComics.isEmpty()) {
						Numero numero = listaComics.get(tbComics.getSelectedRow());
						
						if (numero != null) {
							DetalleComic detalle = new DetalleComic(numero,skCliente);
							detalle.setVisible(true);
						}
					}

				}
			}
		});
		tbComics.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
		tbComics.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tbComics.setBounds(60,23,506,209);
		tbComics.getTableHeader().setReorderingAllowed(false); //impedir mover columnas
		
		Font headerFont = new Font("Caladea", Font.BOLD, 22);
		UIManager.put("TableHeader.font", headerFont);
		
		tbComics.setFont(new Font("Caladea", Font.PLAIN, 20));
		tbComics.setRowHeight(tbComics.getRowHeight()+5); 
		
		JPanel panelTabla = new JPanel();
		frmBusqueda.getContentPane().add(panelTabla, BorderLayout.SOUTH);
		panelTabla.setLayout(new BoxLayout(panelTabla, BoxLayout.Y_AXIS));
		
		tbComics.setFillsViewportHeight(true);
		cabeceraTabla.setViewportView(tbComics);
		
		panelTabla.add(cabeceraTabla);
		
		panelBotones = new JPanel();
		FlowLayout fl_panelBotones = (FlowLayout) panelBotones.getLayout();
		fl_panelBotones.setHgap(300);
		fl_panelBotones.setVgap(10);
		panelBotones.setBorder(new EmptyBorder(5, 0, 0, 0));
		panelTabla.add(panelBotones);
		
		btnIzquierda = new JButton("<");
		btnIzquierda.setToolTipText(tooltipRegistrosAnteriores);
		btnIzquierda.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (offset - 100 < 0 || buscando) {
					lblNoAnteriores.setFont(new Font("Caladea", Font.PLAIN, 16));
					JOptionPane.showMessageDialog(frmBusqueda, lblNoAnteriores, primerosRegistros,
							JOptionPane.INFORMATION_MESSAGE);
				} else {
					offset -= 100;
					cargarComics(skCliente);		
				}
			}
		});
		btnIzquierda.setFont(new Font("Caladea", Font.BOLD, 20));
		panelBotones.add(btnIzquierda);
		
		btnDerecha = new JButton(">");
		btnDerecha.setToolTipText(tooltipRegistrosPosteriores);
		btnDerecha.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				lblNoPosteriores.setFont(new Font("Caladea", Font.PLAIN, 16));				
				if (buscando) {
					JOptionPane.showMessageDialog(frmBusqueda, lblNoPosteriores, ultimosRegistros,
							JOptionPane.INFORMATION_MESSAGE);					
				} else {
					/*consultar al servidor numero de comics en tabla*/
					HiloCliente hilo = new HiloCliente (skCliente, "getNumeroComics", null);
					hilo.start();
					
					try {
						hilo.join();
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					if (offset + 100 > numComics) {
						JOptionPane.showMessageDialog(frmBusqueda, lblError, ultimosRegistros,
								JOptionPane.INFORMATION_MESSAGE);
					} else {
						offset += 100;
						cargarComics(skCliente);		
					}						
				}
			
			}
		});
		btnDerecha.setFont(new Font("Caladea", Font.BOLD, 20));
		panelBotones.add(btnDerecha);
		
		cargarComics(skCliente);
		
		PantallaPrincipal.helpBroker.enableHelpKey(txtColeccion, rutaAyuda, PantallaPrincipal.helpSet);
		PantallaPrincipal.helpBroker.enableHelpKey(txtComic, rutaAyuda, PantallaPrincipal.helpSet);
		PantallaPrincipal.helpBroker.enableHelpKey(btnBuscarPorCol, rutaAyuda, PantallaPrincipal.helpSet);
		PantallaPrincipal.helpBroker.enableHelpKey(btnBuscarPorTitulo, rutaAyuda, PantallaPrincipal.helpSet);
		
		traducir();
	}

	protected void cargarComicsPorTitulo(Socket skCliente, String titulo) {
		listaComics.clear();
		
		HiloCliente hilo = new HiloCliente(skCliente,"cargarComicsPorTitulo",titulo,tbComics);
		hilo.start();
		
		try {
			hilo.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void cargarComicsPorColeccion(Socket skCliente, String nombreColeccion) {
		listaComics.clear();
		
		HiloCliente hilo = new HiloCliente(skCliente,"cargarComicsPorCol",nombreColeccion,tbComics);
		hilo.start();
		
		try {
			hilo.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void cargarComics(Socket skCliente) {
		listaComics.clear();
		
		HiloCliente hilo = new HiloCliente(skCliente,"cargarComics",offset,tbComics);
		hilo.start();
		
		try {
			hilo.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

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
		mensajeAyuda = rb.getString("mensajeAyuda");
		error = rb.getString("error");
		tooltipRegistrosAnteriores = rb.getString("tooltipRegistrosAnteriores");
		noRegistrosAntes = rb.getString("noRegistrosAntes");
		primerosRegistros = rb.getString("primerosRegistros");
		tooltipRegistrosPosteriores = rb.getString("tooltipRegistrosPosteriores");
		noRegistrosDespues = rb.getString("noRegistrosDespues");
		ultimosRegistros = rb.getString("ultimosRegistros");
		
		lblPregunta.setText(mensajeSalir);
		lblError.setText(errorConexion);
		itemComics.setText(gestionComics);
		menuColecciones.setText(colecciones);
		itemColecciones.setText(gestionColecciones);
		lblErrorAyuda.setText(mensajeAyuda);
		btnIzquierda.setToolTipText(tooltipRegistrosAnteriores);
		lblNoAnteriores.setText(noRegistrosAntes);
		btnDerecha.setToolTipText(tooltipRegistrosPosteriores);
		lblNoPosteriores.setText(noRegistrosDespues);
		
	}
}
