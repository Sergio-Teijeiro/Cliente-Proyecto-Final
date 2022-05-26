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
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.sql.Date;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;

import Controlador.HiloCliente;
import Modelo.*;

import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JButton;
import javax.swing.JSeparator;

public class OperacionesComics {

	JFrame frmComics;
	private String[] opciones = { "Sí", "No" };
	private String mensajeSalir = "¿Deseas salir de la aplicación?", cerrarPrograma = "Cerrar programa", errorConexion = "No se ha podido conectar con el servidor";
	private String tituloPantalla = "Gestión de cómics";
	private JMenu menuComics, menuColecciones, menuInformes;
	private JMenuItem itemBusqueda,itemComics,itemColecciones,itemInformes;
	private JTable tbComics;
	private JPanel panelIDTitulo;
	private JLabel lblID;
	private JLabel lblTitulo;
	private JPanel panelFechaTapa;
	private JLabel lblFecha;
	private JLabel lblTapa;
	private JComboBox cmbTapas;
	private DefaultComboBoxModel<String> modeloComboTapas = new DefaultComboBoxModel<String>();
	private DefaultComboBoxModel<Coleccion> modeloComboColecciones = new DefaultComboBoxModel<Coleccion>();
	private JPanel panelEstadoResenha;
	private JLabel lblEstado;
	private JLabel lblResenha;
	private JPanel panelImgColeccion;
	private JLabel lblImg;
	private JButton btnEscogerImg;
	private JLabel lblColeccion;
	private JComboBox cmbColecciones;
	private JSeparator separator;
	private JPanel panelBotones;
	private JButton btnInsertar;
	private JButton btnModificar;
	private JButton btnBorrar, btnVerImg;
	private String mensajeInsertarNumero = "Debes insertar un título y seleccionar una colección", 
			formatoFecha = "La fecha debe tener el formato dd-MM-yyyy", fechaFutura = "La fecha no puede ser posterior a la actual";
	private String errorCampos = "Error con los campos", mensajeAyuda = "Comprueba la ayuda para ver la longitud máxima de cada campo";
	private String mensajeModificarNumero = "Debes insertar un ID y un título y seleccionar una colección",
			mensajeID = "Debes insertar un id mayor o igual a 0", preguntaImg = "¿Deseas mantener la imagen que ya posee el número?";
	private String insertarID = "Debes insertar un ID", mensajeEntero = "Debes insertar un número entero (separado por puntos)";
	
	private byte[] img = null;
	private String rutaAyuda = "comics";
	private JPanel panelBotones_1;
	private JButton btnIzquierda;
	private JButton btnDerecha;
	private int offset = 0;
	private String errorConectar = "Error al conectar", gestionComics = "Gestión cómics", colecciones = "Colecciones", gestionColecciones = "Gestión colecciones";
	private String error = "Error", tooltipRegistrosAnteriores = "Mostrar 100 registros anteriores";
	private String noRegistrosAntes = "No hay registros anteriores", primerosRegistros = "Primeros registros", tooltipRegistrosPosteriores = "Mostrar 100 registros posteriores";
	private String noRegistrosDespues = "No hay registros posteriores", ultimosRegistros = "Últimos registros", fechaAdquisicion = "Fecha adquisición";
	private JLabel lblPregunta = new JLabel(mensajeSalir);
	JLabel lblError = new JLabel(errorConexion);
	JLabel lblErrorAyuda = new JLabel("");
	JLabel lblNoAnteriores = new JLabel("");
	JLabel lblNoPosteriores = new JLabel("");
	private String blanda = "Blanda", resenha = "Reseña", lblImagenValor = "Imagen", btnEscogerValor = "Escoger", imgNoEscogida = "No has seleccionado ninguna imagen", imgNoEscogidaTitulo = "Imagen no escogida";
	JLabel lblImgNoEscogida = new JLabel(imgNoEscogida);
	private String lblImgGuardadaValor = "Se ha guardado la ruta de la imagen seleccionada", imgGuardada = "Imagen guardada", lblComicSinImgValor = "El cómic no posee ninguna imagen", noImg = "Imagen no disponible";
	JLabel lblImgGuardada = new JLabel(lblImgGuardadaValor);
	JLabel lblComicSinImg = new JLabel(lblComicSinImgValor);
	private String tituloEscogerImg = "Elegir imagen de número";
	JLabel lblInsertarNumero = new JLabel(mensajeInsertarNumero);
	private JLabel lblFechaFutura = new JLabel(fechaFutura);
	JLabel lblFormatoFecha = new JLabel(formatoFecha);
	JLabel lblErrorCampos = new JLabel(errorCampos);
	JLabel lblModificarNumero = new JLabel(mensajeModificarNumero);
	JLabel lblBorrarNumero = new JLabel(insertarID);

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
		//reconectar con el servidor si se desconecto
		if (skCliente.isClosed()) {
			try {
				skCliente = new Socket("192.168.56.101", 2000);
			} catch (Exception ex) {
	            if (ex.getClass().getName().equals("java.net.ConnectException")) {
	            	lblError.setFont(new Font("Caladea", Font.PLAIN, 20));
	            	JOptionPane.showMessageDialog(frmComics,lblError, errorConectar,
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
		frmComics = new JFrame();
		frmComics.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// dialogo confirmacion
				ImageIcon icono = new ImageIcon(PantallaPrincipal.class.getResource("/img/app_icon.png"));
				ImageIcon iconoEscala = new ImageIcon(
						icono.getImage().getScaledInstance(50, 50, java.awt.Image.SCALE_FAST));
				lblPregunta.setFont(new Font("Caladea", Font.PLAIN, 20));
				int respuesta = JOptionPane.showOptionDialog(frmComics, lblPregunta,
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
		
		itemComics = new JMenuItem(gestionComics);
		itemComics.setFont(new Font("Caladea", Font.PLAIN, 16));
		menuComics.add(itemComics);
		
		menuColecciones = new JMenu("Colecciones");
		menuColecciones.setFont(new Font("Caladea", Font.PLAIN, 16));
		menuBar.add(menuColecciones);
		
		itemColecciones = new JMenuItem(gestionColecciones);
		itemColecciones.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmComics.dispose();

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
				frmComics.dispose();

				PantallaInformes p = new PantallaInformes(skCliente);
				p.frmInformes.setVisible(true);
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
		
		Font headerFont = new Font("Caladea", Font.BOLD, 22);
		UIManager.put("TableHeader.font", headerFont);
		
		tbComics.setFont(new Font("Caladea", Font.PLAIN, 20));
		tbComics.setRowHeight(tbComics.getRowHeight()+5); 

		JPanel panelTabla = new JPanel();
		frmComics.getContentPane().add(panelTabla, BorderLayout.SOUTH);
		panelTabla.setLayout(new BoxLayout(panelTabla, BoxLayout.Y_AXIS));

		tbComics.setFillsViewportHeight(true);
		cabeceraTabla.setViewportView(tbComics);

		panelTabla.add(cabeceraTabla);
		
		panelBotones_1 = new JPanel();
		FlowLayout fl_panelBotones = (FlowLayout) panelBotones_1.getLayout();
		fl_panelBotones.setHgap(300);
		fl_panelBotones.setVgap(10);
		panelBotones_1.setBorder(new EmptyBorder(5, 0, 0, 0));
		panelTabla.add(panelBotones_1);
		
		btnIzquierda = new JButton("<");
		btnIzquierda.setToolTipText(tooltipRegistrosAnteriores);
		btnIzquierda.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (offset - 100 < 0) {
					lblNoAnteriores.setFont(new Font("Caladea", Font.PLAIN, 16));
					JOptionPane.showMessageDialog(frmComics, lblNoAnteriores, primerosRegistros,
							JOptionPane.INFORMATION_MESSAGE);
				} else {
					offset -= 100;
					cargarComics(skCliente);		
				}
			}
		});
		btnIzquierda.setFont(new Font("Caladea", Font.BOLD, 20));
		panelBotones_1.add(btnIzquierda);
		
		btnDerecha = new JButton(">");
		btnDerecha.setToolTipText(tooltipRegistrosPosteriores);
		btnDerecha.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lblNoPosteriores.setFont(new Font("Caladea", Font.PLAIN, 16));				

				/*consultar al servidor numero de comics*/
				HiloCliente hilo = new HiloCliente(skCliente, "getNumeroComics", null);
				hilo.start();

				try {
					hilo.join();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				if (offset + 100 > PantallaBusqueda.numComics) {
					JOptionPane.showMessageDialog(frmComics, lblNoPosteriores, ultimosRegistros,
							JOptionPane.INFORMATION_MESSAGE);
				} else {
					offset += 100;
					cargarComics(skCliente);
				}				
							
			}
		});
		btnDerecha.setFont(new Font("Caladea", Font.BOLD, 20));
		panelBotones_1.add(btnDerecha);

		JPanel panelPrincipal = new JPanel();
		frmComics.getContentPane().add(panelPrincipal, BorderLayout.CENTER);
		panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
		
		panelIDTitulo = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panelIDTitulo.getLayout();
		flowLayout.setHgap(25);
		flowLayout.setAlignment(FlowLayout.LEFT);
		panelPrincipal.add(panelIDTitulo);
		
		lblID = new JLabel("ID");
		lblID.setFont(new Font("Caladea", Font.PLAIN, 20));
		panelIDTitulo.add(lblID);
		
		JTextArea txtID = new JTextArea();
		txtID.setWrapStyleWord(true);
		txtID.setRows(1);
		txtID.setColumns(5);
		txtID.setFont(new Font("Caladea", Font.PLAIN, 20));
		txtID.setCaretPosition(0); //poner cursor al principio
		txtID.setLineWrap(true);
		
		JScrollPane scrollID = new JScrollPane();
		scrollID.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollID.setBounds(0, 0, 20, 150);
		scrollID.setViewportView(txtID);

		panelIDTitulo.add(scrollID);
		
		lblTitulo = new JLabel("T\u00EDtulo");
		lblTitulo.setFont(new Font("Caladea", Font.PLAIN, 20));
		panelIDTitulo.add(lblTitulo);
		
		JTextArea txtTitulo = new JTextArea();
		txtTitulo.setWrapStyleWord(true);
		txtTitulo.setRows(1);
		txtTitulo.setColumns(30);
		txtTitulo.setFont(new Font("Caladea", Font.PLAIN, 20));
		txtTitulo.setCaretPosition(0); //poner cursor al principio
		txtTitulo.setLineWrap(true);
		
		JScrollPane scrollTitulo = new JScrollPane();
		scrollTitulo.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollTitulo.setBounds(0, 0, 20, 150);
		scrollTitulo.setViewportView(txtTitulo);

		panelIDTitulo.add(scrollTitulo);
		
		panelFechaTapa = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panelFechaTapa.getLayout();
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		flowLayout_1.setHgap(25);
		panelPrincipal.add(panelFechaTapa);
		
		lblFecha = new JLabel(fechaAdquisicion);
		lblFecha.setBorder(new EmptyBorder(0, 0, 0, 5));
		lblFecha.setFont(new Font("Caladea", Font.PLAIN, 20));
		panelFechaTapa.add(lblFecha);
		
		JTextField txtFecha= new JTextField();
		txtFecha.setPreferredSize(new Dimension(20,40));
		txtFecha.setColumns(7);
		txtFecha.setFont(new Font("Caladea", Font.PLAIN, 20));
		txtFecha.setCaretPosition(0); //poner cursor al principio
		panelFechaTapa.add(txtFecha);
		
		lblTapa = new JLabel("Tapa");
		lblTapa.setFont(new Font("Caladea", Font.PLAIN, 20));
		panelFechaTapa.add(lblTapa);
		
		cmbTapas = new JComboBox();
		cmbTapas.setFont(new Font("Caladea", Font.PLAIN, 20));
		
		cmbTapas.setModel(modeloComboTapas);
		
		panelFechaTapa.add(cmbTapas);
		
		panelEstadoResenha = new JPanel();
		FlowLayout flowLayout_2 = (FlowLayout) panelEstadoResenha.getLayout();
		flowLayout_2.setHgap(25);
		flowLayout_2.setAlignment(FlowLayout.LEFT);
		panelPrincipal.add(panelEstadoResenha);
		
		lblEstado = new JLabel("Estado");
		lblEstado.setFont(new Font("Caladea", Font.PLAIN, 20));
		lblEstado.setBorder(new EmptyBorder(0, 0, 0, 5));
		panelEstadoResenha.add(lblEstado);
		
		JTextArea txtEstado = new JTextArea();
		txtEstado.setWrapStyleWord(true);
		txtEstado.setRows(1);
		txtEstado.setColumns(12);
		txtEstado.setFont(new Font("Caladea", Font.PLAIN, 20));
		txtEstado.setCaretPosition(0); //poner cursor al principio
		txtEstado.setLineWrap(true);
		
		JScrollPane scrollEstado = new JScrollPane();
		scrollEstado.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollEstado.setBounds(0, 0, 20, 150);
		scrollEstado.setViewportView(txtEstado);

		panelEstadoResenha.add(scrollEstado);
		
		lblResenha = new JLabel(resenha);
		lblResenha.setFont(new Font("Caladea", Font.PLAIN, 20));
		panelEstadoResenha.add(lblResenha);
		
		JTextArea txtAreaResenha = new JTextArea();
		txtAreaResenha.setWrapStyleWord(true);
		txtAreaResenha.setRows(5);
		txtAreaResenha.setColumns(20);
		txtAreaResenha.setFont(new Font("Caladea", Font.PLAIN, 20));
		txtAreaResenha.setCaretPosition(0); //poner cursor al principio
		txtAreaResenha.setLineWrap(true);
		
		JScrollPane scrollResenha = new JScrollPane();
		scrollResenha.setBounds(0, 0, 20, 150);
		scrollResenha.setViewportView(txtAreaResenha);

		panelEstadoResenha.add(scrollResenha);
		
		panelImgColeccion = new JPanel();
		FlowLayout flowLayout_3 = (FlowLayout) panelImgColeccion.getLayout();
		flowLayout_3.setHgap(25);
		flowLayout_3.setAlignment(FlowLayout.LEFT);
		panelPrincipal.add(panelImgColeccion);
		
		lblImg = new JLabel(lblImagenValor);
		lblImg.setFont(new Font("Caladea", Font.PLAIN, 20));
		panelImgColeccion.add(lblImg);
		
		btnEscogerImg = new JButton(btnEscogerValor);
		btnEscogerImg.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Escoger una imagen y guardar sus bytes
				
				String rutaImg = seleccionarImagen();
				
		        if (rutaImg == null) {
					
					lblImgNoEscogida.setFont(new Font("Caladea", Font.PLAIN, 16));
		            JOptionPane.showMessageDialog(null,lblImgNoEscogida , imgNoEscogidaTitulo, JOptionPane.WARNING_MESSAGE);
		        } else {
		        	File fichero = new File(rutaImg);
		        	try {
						img = Files.readAllBytes(fichero.toPath());
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
		        	
					lblImgGuardada.setFont(new Font("Caladea", Font.PLAIN, 16));
		            JOptionPane.showMessageDialog(null,lblImgGuardada , imgGuardada, JOptionPane.INFORMATION_MESSAGE);
		        }
			}
		});
		
		btnEscogerImg.setFont(new Font("Caladea", Font.PLAIN, 20));
		panelImgColeccion.add(btnEscogerImg);
		
		btnVerImg = new JButton("Ver");
		btnVerImg.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (img == null) {
					
					lblComicSinImg.setFont(new Font("Caladea", Font.PLAIN, 16));
					JOptionPane.showMessageDialog(null, lblComicSinImg, noImg, JOptionPane.ERROR_MESSAGE);
				} else {
					VerImg verImg = new VerImg(img);
					verImg.setVisible(true);
				}
			}
		});
		btnVerImg.setFont(new Font("Caladea", Font.PLAIN, 20));
		panelImgColeccion.add(btnVerImg);		
		
		lblColeccion = new JLabel("Colecci\u00F3n");
		lblColeccion.setFont(new Font("Caladea", Font.PLAIN, 20));
		panelImgColeccion.add(lblColeccion);
		
		cmbColecciones = new JComboBox();
		
		cmbColecciones.setModel(modeloComboColecciones);
		cmbColecciones.setFont(new Font("Caladea", Font.PLAIN, 20));
		
		cargarColecciones(skCliente);

		panelImgColeccion.add(cmbColecciones);
		
		separator = new JSeparator();
		panelPrincipal.add(separator);
		
		panelBotones = new JPanel();
		FlowLayout flowLayout_4 = (FlowLayout) panelBotones.getLayout();
		flowLayout_4.setHgap(25);
		panelPrincipal.add(panelBotones);
		
		btnInsertar = new JButton("Insertar");
		btnInsertar.setPreferredSize(new Dimension(100,35));
		btnInsertar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//INSERTAR UN NUMERO
				if (txtTitulo.getText().isBlank() || cmbColecciones.getSelectedItem() == null) {
					lblInsertarNumero.setFont(new Font("Caladea", Font.PLAIN, 16));
					JOptionPane.showMessageDialog(frmComics,lblInsertarNumero,error,JOptionPane.ERROR_MESSAGE);
				} else {
					try {
						//Comprobar longitudes
						if (txtTitulo.getText().length() > 200 || txtEstado.getText().length() > 50 || txtAreaResenha.getText().length() > 1500) {
							lblErrorAyuda.setFont(new Font("Caladea", Font.PLAIN, 16));
							JOptionPane.showMessageDialog(frmComics,
									lblErrorAyuda, error,
									JOptionPane.ERROR_MESSAGE);
						} else {
							// Comprobar que datos se mandan
							Date fecha = null;
							String estado = null, resenha = null;
							boolean formato = true;
							
							if (!txtFecha.getText().isBlank()) {
								try {
									String patron = "dd-MM-yyyy";
									
									DateTimeFormatter formatter = DateTimeFormatter.ofPattern(patron);
									LocalDate date = LocalDate.parse(txtFecha.getText().trim(),formatter);
									fecha = Date.valueOf(date);
									
									boolean esFechaFutura = fecha.after(Date.from(Instant.now()));
									
									if (esFechaFutura) {
										formato = false;
										lblFechaFutura.setFont(new Font("Caladea", Font.PLAIN, 16));
										JOptionPane.showMessageDialog(frmComics,
												lblFechaFutura, error,
												JOptionPane.ERROR_MESSAGE);
									}
									
								} catch (Exception e1) {
									formato = false;
									lblFormatoFecha.setFont(new Font("Caladea", Font.PLAIN, 16));
									JOptionPane.showMessageDialog(frmComics,
											lblFormatoFecha, error,
											JOptionPane.ERROR_MESSAGE);
									// e1.printStackTrace();
								}
							}
							
							if (!txtEstado.getText().isBlank()) {
								estado = txtEstado.getText();
							}
							
							if (!txtAreaResenha.getText().isBlank()) {
								resenha = txtAreaResenha.getText();
							}
							
							if (formato) {
								//Solicitud a servidor de insertar numero
								String tapa = "";
								if (cmbTapas.getSelectedIndex() == 0) {
									tapa = "Blanda";
								} else {
									tapa = "Dura";
								}
								
								Coleccion coleccion = (Coleccion) cmbColecciones.getSelectedItem();
								
								Numero numero = new Numero(0,txtTitulo.getText(),fecha,tapa,estado,resenha,img,coleccion.getId());
								
								HiloCliente hilo = new HiloCliente(skCliente, "altaNumero", numero,tbComics,offset);
								hilo.start();
								
								img = null;
							}
						}
					} catch (Exception e1) {
						
						lblErrorCampos.setFont(new Font("Caladea", Font.PLAIN, 16));
						JOptionPane.showMessageDialog(frmComics, lblErrorCampos, error,
								JOptionPane.ERROR_MESSAGE);
						 //e1.printStackTrace();
					}
				}
			}
		});
		btnInsertar.setFont(new Font("Caladea", Font.PLAIN, 20));
		panelBotones.add(btnInsertar);
		
		btnModificar = new JButton("Modificar");
		btnModificar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//MODIFICAR UN NUMERO
				if (txtID.getText().isBlank() || txtTitulo.getText().isBlank() || cmbColecciones.getSelectedItem() == null) {
					
					lblModificarNumero.setFont(new Font("Caladea", Font.PLAIN, 16));
					JOptionPane.showMessageDialog(frmComics,lblModificarNumero,error,JOptionPane.ERROR_MESSAGE);
				} else {
					try {
						//Comprobar longitudes
						if (txtTitulo.getText().length() > 200 || txtEstado.getText().length() > 50 || txtAreaResenha.getText().length() > 1500) {
							lblErrorAyuda.setFont(new Font("Caladea", Font.PLAIN, 16));
							JOptionPane.showMessageDialog(frmComics,
									lblErrorAyuda, error,
									JOptionPane.ERROR_MESSAGE);
						} else {
							// Comprobar que datos se mandan
							Date fecha = null;
							String estado = null, resenha = null;
							Integer id;
							boolean formato = true;
							
							if (!txtFecha.getText().isBlank()) {
								try {
									String patron = "dd-MM-yyyy";
									
									DateTimeFormatter formatter = DateTimeFormatter.ofPattern(patron);
									LocalDate date = LocalDate.parse(txtFecha.getText().trim(),formatter);
									fecha = Date.valueOf(date);
									
									boolean esFechaFutura = fecha.after(Date.from(Instant.now()));
									
									if (esFechaFutura) {
										formato = false;
										lblFechaFutura.setFont(new Font("Caladea", Font.PLAIN, 16));
										JOptionPane.showMessageDialog(frmComics,
												lblFechaFutura, error,
												JOptionPane.ERROR_MESSAGE);
									}
									
								} catch (Exception e1) {
									formato = false;
									lblFormatoFecha.setFont(new Font("Caladea", Font.PLAIN, 16));
									JOptionPane.showMessageDialog(frmComics,
											lblFormatoFecha, error,
											JOptionPane.ERROR_MESSAGE);
									// e1.printStackTrace();
								}
							}
							
							if (!txtEstado.getText().isBlank()) {
								estado = txtEstado.getText();
							}
							
							if (!txtAreaResenha.getText().isBlank()) {
								resenha = txtAreaResenha.getText();
							}
							
							if (formato) {
								long aux = (long) NumberFormat.getNumberInstance().parse(txtID.getText().trim());
								id = (int) aux;

								if (id < 0) {
									JOptionPane.showMessageDialog(frmComics,
											mensajeID, error,
											JOptionPane.ERROR_MESSAGE);
								} else {
									//Solicitud a servidor de modificar numero
									String tapa = "";
									if (cmbTapas.getSelectedIndex() == 0) {
										tapa = "Blanda";
									} else {
										tapa = "Dura";
									}
									
									Coleccion coleccion = (Coleccion) cmbColecciones.getSelectedItem();
									
									Numero numero = new Numero(id,txtTitulo.getText(),fecha,tapa,estado,resenha,img,coleccion.getId());
									
									if (img == null) {
										//Preguntar si desea mantener la imagen anterior o eliminarla
										ImageIcon icono = new ImageIcon(OperacionesComics.class.getResource("/img/app_icon.png"));
										ImageIcon iconoEscala = new ImageIcon(
												icono.getImage().getScaledInstance(50, 50, java.awt.Image.SCALE_FAST));
										int respuesta = JOptionPane.showOptionDialog(frmComics, preguntaImg,
												imgNoEscogidaTitulo, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, iconoEscala,
												opciones, opciones[0]);
										
										if (respuesta == 0) { //si elige si, mantengo la imagen
											Numero numAux = PantallaBusqueda.listaComics.get(tbComics.getSelectedRow());
											
											numero.setImg(numAux.getImg());
										}
									}
									
									HiloCliente hilo = new HiloCliente(skCliente, "modificarNumero", numero,tbComics,offset);
									hilo.start();
									
									img = null;
									
								}
							}
						}
						
					} catch (Exception e1) {
						lblErrorCampos.setFont(new Font("Caladea", Font.PLAIN, 16));
						JOptionPane.showMessageDialog(frmComics, lblErrorCampos, error,
								JOptionPane.ERROR_MESSAGE);
						 //e1.printStackTrace();
					}
				}
			}
		});
		btnModificar.setPreferredSize(new Dimension(114,35));
		btnModificar.setFont(new Font("Caladea", Font.PLAIN, 20));
		panelBotones.add(btnModificar);
		
		btnBorrar = new JButton("Borrar");
		btnBorrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//BORRAR UN NUMERO
				if (txtID.getText().isBlank()) {
					lblBorrarNumero.setFont(new Font("Caladea", Font.PLAIN, 16));
					JOptionPane.showMessageDialog(frmComics,lblBorrarNumero,error,JOptionPane.ERROR_MESSAGE);
				} else {
					Integer id = null;
					boolean formato = true;
					
					try {
						try {
							long aux = (long) NumberFormat.getNumberInstance().parse(txtID.getText().trim());
							id = (int) aux;
						} catch (Exception e1) {
							formato = false;
							JOptionPane.showMessageDialog(frmComics, mensajeEntero,
									error, JOptionPane.ERROR_MESSAGE);
							// e1.printStackTrace();
						}	
						
						if (formato)
						if (id < 0) {
							JOptionPane.showMessageDialog(frmComics,
								mensajeID, error,JOptionPane.ERROR_MESSAGE);
						} else {
							//Solicitud a servidor de borrar numero
							String tapa = "";
							if (cmbTapas.getSelectedIndex() == 0) {
								tapa = "Blanda";
							} else {
								tapa = "Dura";
							}
							
							Coleccion coleccion = (Coleccion) cmbColecciones.getSelectedItem();
							
							Numero numero = new Numero(id,txtTitulo.getText(),null,tapa,"","",img,coleccion.getId());

							HiloCliente hilo = new HiloCliente(skCliente, "bajaNumero", numero,tbComics,offset);
							hilo.start();
							
							img = null;
						}
						
					} catch (Exception e1) {
						lblErrorCampos.setFont(new Font("Caladea", Font.PLAIN, 16));
						JOptionPane.showMessageDialog(frmComics, lblErrorCampos, error,
								JOptionPane.ERROR_MESSAGE);
						 //e1.printStackTrace();
					}
				}
			}
		});
		btnBorrar.setPreferredSize(new Dimension(90,35));
		btnBorrar.setFont(new Font("Caladea", Font.PLAIN, 20));
		panelBotones.add(btnBorrar);
		
		cargarComics(skCliente);
		
		ListSelectionModel listSelectionModel = tbComics.getSelectionModel();
		listSelectionModel.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				ListSelectionModel lsm = (ListSelectionModel) e.getSource();
				if (!lsm.isSelectionEmpty()) {

					Numero numero = PantallaBusqueda.listaComics.get(tbComics.getSelectedRow());

					String idConvertido = NumberFormat.getInstance().format(numero.getId());
					txtID.setText(idConvertido);
					txtID.setCaretPosition(0);
					
					txtTitulo.setText(numero.getTitulo());
					txtTitulo.setCaretPosition(0);
					
					if (numero.getFechaAdquisicion() != null) {
						String fecha = DateFormat.getDateInstance().format(numero.getFechaAdquisicion());
						txtFecha.setText(fecha);
					} else {
						txtFecha.setText("");
					}
					
					if (numero.getTapa().equals("Blanda")) {
						cmbTapas.setSelectedIndex(0);
					} else {
						cmbTapas.setSelectedIndex(1);
					}

					if (numero.getEstado() != null) {
						txtEstado.setText(numero.getEstado());
						txtEstado.setCaretPosition(0);
					} else {
						txtEstado.setText("");
					}
					
					if (numero.getResenha() != null) {
						txtAreaResenha.setText(numero.getResenha());
						txtAreaResenha.setCaretPosition(0);						
					} else {
						txtAreaResenha.setText("");
					}
					
					for (int i=0;i<modeloComboColecciones.getSize();i++) {
						if (modeloComboColecciones.getElementAt(i).getId() == numero.getIdColeccion()) {
							modeloComboColecciones.setSelectedItem(modeloComboColecciones.getElementAt(i));
							break;
						}
					}
					
					img = numero.getImg();

				} else {
					txtID.setText("");
					txtTitulo.setText("");
					txtFecha.setText("");
					cmbTapas.setSelectedIndex(0);
					txtEstado.setText("");
					txtAreaResenha.setText("");
					cmbColecciones.setSelectedIndex(0);
					img = null;
				}
			}
		});
	
		PantallaPrincipal.helpBroker.enableHelpKey(txtID, rutaAyuda, PantallaPrincipal.helpSet);
		PantallaPrincipal.helpBroker.enableHelpKey(txtTitulo, rutaAyuda, PantallaPrincipal.helpSet);
		PantallaPrincipal.helpBroker.enableHelpKey(txtFecha, rutaAyuda, PantallaPrincipal.helpSet);
		PantallaPrincipal.helpBroker.enableHelpKey(txtEstado, rutaAyuda, PantallaPrincipal.helpSet);
		PantallaPrincipal.helpBroker.enableHelpKey(txtAreaResenha, rutaAyuda, PantallaPrincipal.helpSet);
		
		cmbColecciones.setPrototypeDisplayValue("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
		
		traducir();
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
		tituloPantalla = rb.getString("tituloPantallaComics");
		fechaAdquisicion = rb.getString("fechaAdquisicion");
		blanda = rb.getString("blanda");
		resenha = rb.getString("resenha");
		lblImagenValor = rb.getString("lblImagenValor");
		btnEscogerValor = rb.getString("btnEscogerValor");
		imgNoEscogida = rb.getString("imgNoEscogida");
		imgNoEscogidaTitulo = rb.getString("imgNoEscogidaTitulo");
		lblImgGuardadaValor = rb.getString("lblImgGuardadaValor");
		imgGuardada = rb.getString("imgGuardada");
		lblComicSinImgValor = rb.getString("lblComicSinImgValor");
		noImg = rb.getString("noImg");
		tituloEscogerImg = rb.getString("tituloEscogerImg");
		mensajeInsertarNumero = rb.getString("mensajeInsertarNumero");
		fechaFutura = rb.getString("fechaFutura");
		formatoFecha = rb.getString("formatoFecha");
		errorCampos = rb.getString("errorCampos");
		mensajeModificarNumero = rb.getString("mensajeModificarNumero");
		mensajeID = rb.getString("mensajeID");
		preguntaImg = rb.getString("preguntaImg");
		mensajeEntero = rb.getString("mensajeEntero");
		
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
		frmComics.setTitle(tituloPantalla);
		lblFecha.setText(fechaAdquisicion);
		modeloComboTapas.addElement(blanda);
		modeloComboTapas.addElement("Dura");
		lblResenha.setText(resenha);
		lblImg.setText(lblImagenValor);
		btnEscogerImg.setText(btnEscogerValor);
		lblImgNoEscogida.setText(imgNoEscogida);
		lblImgGuardada.setText(lblImgGuardadaValor);
		lblComicSinImg.setText(lblComicSinImgValor);
		lblInsertarNumero.setText(mensajeInsertarNumero);
		lblFechaFutura.setText(fechaFutura);
		lblFormatoFecha.setText(formatoFecha);
		lblErrorCampos.setText(errorCampos);
		lblModificarNumero.setText(mensajeModificarNumero);
		
	}

	protected String seleccionarImagen() {
        String ruta = null;

        JFileChooser fc = escogerFichero(tituloEscogerImg);

        int eleccion = fc.showDialog(null, btnEscogerValor);

        if (eleccion == JFileChooser.APPROVE_OPTION) {// si elige abrir el archivo
            File f = fc.getSelectedFile(); // obtiene el archivo seleccionado
            ruta = f.getAbsolutePath();
        }

        return ruta;		
		
	}

	private void cargarColecciones(Socket skCliente) {
		modeloComboColecciones.removeAllElements();
		
		HiloCliente hilo = new HiloCliente(skCliente, "cargarColecciones", modeloComboColecciones);
		hilo.start();
		
		try {
			hilo.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void cargarComics(Socket skCliente) {
		PantallaBusqueda.listaComics.clear();
		
		HiloCliente hilo = new HiloCliente(skCliente,"cargarComics",offset,tbComics);
		hilo.start();
		
		try {
			hilo.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

    private static JFileChooser escogerFichero(String tituloEscogerImg) {

        JFileChooser fc = new JFileChooser();
        String escritorio = System.getProperty("user.home") + "/desktop";//carpeta donde se abre por defecto (escritorio del usuario)
        File destino = new File(escritorio);

        fc.setCurrentDirectory(destino);
        fc.setDialogTitle(tituloEscogerImg);
        fc.setMultiSelectionEnabled(false);

        //el primer parametro es la descripcion de las extensiones aceptadas y el resto son las extensiones aceptadas
        FileNameExtensionFilter filtro = new FileNameExtensionFilter(".jpg, .jpeg, .png", "jpg", "jpeg", "png");

        fc.setFileFilter(filtro);// aplica un filtro de extensiones

        return fc;
    }	
}
