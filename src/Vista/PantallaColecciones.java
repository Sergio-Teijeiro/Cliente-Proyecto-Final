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
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;

import Controlador.HiloCliente;
import Modelo.*;

import java.awt.FlowLayout;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JSeparator;

/**
 * Pantalla que permite gestionar las colecciones (inserción, modificación y borrado). Además, se muestran los campos de cada colección tras seleccionarla en la tabla
 * @author sergio
 *
 */
public class PantallaColecciones {

	JFrame frmColecciones;
	private String[] opciones = { "Sí", "No" };
	private String mensajeSalir = "¿Deseas salir de la aplicación?", cerrarPrograma = "Cerrar programa", errorConexion = "No se ha podido conectar con el servidor";
	private String tituloPantalla = "Gestión de colecciones";
	private JMenu menuComics, menuColecciones, menuInformes;
	private JMenuItem itemBusqueda,itemComics,itemColecciones,itemInformes;
	private JTable tbColecciones;	
	private JPanel panelID;
	private JLabel lblID, lblNombre, lblImg;
	private JPanel panelNombre;
	private JPanel panelImg, panelBotones;
	private JButton btnEscogerImg, btnInsertar, btnModificar, btnBorrar;
	
	private byte[] img = null;
	JLabel lblError = new JLabel(errorConexion);
	private JSeparator separator;
	private String mensajeInsertarColeccion = "Debes insertar un nombre", errorCampos = "Error con los campos";
	private String mensajeAyuda = "Comprueba la ayuda para ver la longitud máxima de cada campo";
	private String mensajeModificarColeccion = "Debes insertar un ID y un nombre", mensajeID = "Debes insertar un id mayor o igual a 0";
	private String preguntaImg = "¿Deseas mantener la imagen que ya posee la colección?";
	private String insertarID = "Debes insertar un ID",mensajeEntero = "Debes insertar un número entero (separado por puntos)";
	private String preguntaBorrar = "¿Deseas borrar la colección y sus números?", tituloBorrarColeccion = "Colección con números";
	private String errorConectar = "Error al conectar", gestionComics = "Gestión cómics", colecciones = "Colecciones", gestionColecciones = "Gestión colecciones";
	private String lblImagenValor = "Imagen", btnEscogerValor = "Escoger", imgNoEscogida = "No has seleccionado ninguna imagen", imgNoEscogidaTitulo = "Imagen no escogida";
	JLabel lblImgNoEscogida = new JLabel(imgNoEscogida);
	private String lblImgGuardadaValor = "Se ha guardado la ruta de la imagen seleccionada", imgGuardada = "Imagen guardada";
	private String lblColeccionSinImgValor = "La colección no posee ninguna imagen", noImg = "Imagen no disponible";
	
	public static ArrayList<Coleccion> listaColecciones = new ArrayList<>();
	public static ArrayList<Numero> numerosRelacionados = new ArrayList<>();
	private JButton btnVerImg;
	private String rutaAyuda = "colecciones";
	JLabel lblImgGuardada = new JLabel(lblImgGuardadaValor);
	JLabel lblColeccionSinImg = new JLabel(lblColeccionSinImgValor);
	private JLabel lblPregunta = new JLabel(mensajeSalir);
	private String tituloEscogerImg = "Elegir imagen de número",error = "Error", lblNombreValor = "Nombre", lblNoExisteColValor = "No existe ninguna colección con ese ID";
	JLabel lblInsertarColeccion = new JLabel(mensajeInsertarColeccion);
	JLabel lblErrorAyuda = new JLabel("");
	JLabel lblErrorCampos = new JLabel(errorCampos);
	JLabel lblModificarColeccion = new JLabel(mensajeModificarColeccion);
	JLabel lblBorrarColeccion = new JLabel(insertarID);
	JLabel lblNoExisteCol = new JLabel(lblNoExisteColValor);
	JLabel lblBorrarCol = new JLabel(preguntaBorrar);
	private String borradoCol = "Se ha eliminado correctamente la colección ", borradoListo = "Borrado completado";
	JLabel lblBorradoCol = new JLabel(borradoCol);

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PantallaColecciones window = new PantallaColecciones(null);
					window.frmColecciones.setVisible(true);
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
	public PantallaColecciones(Socket skCliente) {
		//reconectar con el servidor si se desconecto
		if (skCliente.isClosed()) {
			try {
				skCliente = new Socket(ModificarIP.ipValida, 2000);
			} catch (Exception ex) {
	            if (ex.getClass().getName().equals("java.net.ConnectException")) {
	            	lblError.setFont(new Font("Caladea", Font.PLAIN, 20));
	            	JOptionPane.showMessageDialog(frmColecciones,lblError, errorConectar,
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
		frmColecciones = new JFrame();
		frmColecciones.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// dialogo confirmacion
				ImageIcon icono = new ImageIcon(PantallaPrincipal.class.getResource("/img/app_icon.png"));
				ImageIcon iconoEscala = new ImageIcon(
						icono.getImage().getScaledInstance(50, 50, java.awt.Image.SCALE_FAST));
				lblPregunta.setFont(new Font("Caladea", Font.PLAIN, 20));
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
			            //Logger.getLogger(PantallaBusqueda.class.getName()).log(Level.SEVERE, null, ex);
			        } catch (InterruptedException ex) {
			            //Logger.getLogger(PantallaBusqueda.class.getName()).log(Level.SEVERE, null, ex);
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
		
		itemComics = new JMenuItem(gestionComics);
		itemComics.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmColecciones.dispose();

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
		itemColecciones.setFont(new Font("Caladea", Font.PLAIN, 16));
		menuColecciones.add(itemColecciones);
		
		menuInformes = new JMenu("Informes");
		menuInformes.setFont(new Font("Caladea", Font.PLAIN, 16));
		menuBar.add(menuInformes);
		
		itemInformes = new JMenuItem("Crear informes");
		itemInformes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmColecciones.dispose();

				PantallaInformes p = new PantallaInformes(skCliente);
				p.frmInformes.setVisible(true);
			}
		});
		itemInformes.setFont(new Font("Caladea", Font.PLAIN, 16));
		menuInformes.add(itemInformes);
		
		JScrollPane cabeceraTabla = new JScrollPane();
		cabeceraTabla.setBounds(0, 0, 594, 299);

		tbColecciones = new JTable();
		tbColecciones.setPreferredScrollableViewportSize(new Dimension(450, 250));
		tbColecciones.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
		tbColecciones.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tbColecciones.setBounds(60, 23, 506, 209);
		tbColecciones.getTableHeader().setReorderingAllowed(false); // impedir mover columnas
		
		Font headerFont = new Font("Caladea", Font.BOLD, 22);
		UIManager.put("TableHeader.font", headerFont);
		
		tbColecciones.setFont(new Font("Caladea", Font.PLAIN, 20));
		tbColecciones.setRowHeight(tbColecciones.getRowHeight()+5); 

		JPanel panelTabla = new JPanel();
		frmColecciones.getContentPane().add(panelTabla, BorderLayout.SOUTH);
		panelTabla.setLayout(new BorderLayout(0, 0));

		tbColecciones.setFillsViewportHeight(true);
		cabeceraTabla.setViewportView(tbColecciones);

		panelTabla.add(cabeceraTabla, BorderLayout.CENTER);

		JPanel panelPrincipal = new JPanel();
		frmColecciones.getContentPane().add(panelPrincipal, BorderLayout.CENTER);
		panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));		
		
		panelID = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panelID.getLayout();
		flowLayout.setHgap(25);
		flowLayout.setAlignment(FlowLayout.LEFT);
		panelPrincipal.add(panelID);
		
		lblID = new JLabel("ID");
		lblID.setBorder(new EmptyBorder(0, 0, 0, 50));
		lblID.setFont(new Font("Caladea", Font.PLAIN, 20));
		panelID.add(lblID);
		
		JTextArea txtID = new JTextArea();
		txtID.setWrapStyleWord(true);
		txtID.setRows(1);
		txtID.setColumns(40);
		txtID.setFont(new Font("Caladea", Font.PLAIN, 20));
		txtID.setCaretPosition(0); //poner cursor al principio
		txtID.setLineWrap(true);
		
		JScrollPane scrollID = new JScrollPane();
		scrollID.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollID.setBounds(0, 0, 20, 150);
		scrollID.setViewportView(txtID);

		panelID.add(scrollID);
		
		panelNombre = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panelNombre.getLayout();
		flowLayout_1.setHgap(25);
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		panelPrincipal.add(panelNombre);
		
		lblNombre = new JLabel(lblNombreValor);
		lblNombre.setFont(new Font("Caladea", Font.PLAIN, 20));
		panelNombre.add(lblNombre);
		
		JTextArea txtNombre = new JTextArea();
		txtNombre.setWrapStyleWord(true);
		txtNombre.setRows(1);
		txtNombre.setColumns(40);
		txtNombre.setFont(new Font("Caladea", Font.PLAIN, 20));
		txtNombre.setCaretPosition(0); //poner cursor al principio
		txtNombre.setLineWrap(true);
		
		JScrollPane scrollNombre = new JScrollPane();
		scrollNombre.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollNombre.setBounds(0, 0, 20, 150);
		scrollNombre.setViewportView(txtNombre);

		panelNombre.add(scrollNombre);
		
		panelImg = new JPanel();
		FlowLayout flowLayout_2 = (FlowLayout) panelImg.getLayout();
		flowLayout_2.setHgap(25);
		flowLayout_2.setAlignment(FlowLayout.LEFT);
		panelPrincipal.add(panelImg);
		
		lblImg = new JLabel(lblImagenValor);
		lblImg.setFont(new Font("Caladea", Font.PLAIN, 20));
		panelImg.add(lblImg);
		
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
						//e1.printStackTrace();
					}

					lblImgGuardada.setFont(new Font("Caladea", Font.PLAIN, 16));
		            JOptionPane.showMessageDialog(null,lblImgGuardada , imgGuardada, JOptionPane.INFORMATION_MESSAGE);
		        }
			}
		});
		btnEscogerImg.setFont(new Font("Caladea", Font.PLAIN, 20));
		panelImg.add(btnEscogerImg);		
		
		btnVerImg = new JButton("Ver");
		btnVerImg.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (img == null) {
					lblColeccionSinImg.setFont(new Font("Caladea", Font.PLAIN, 16));
					JOptionPane.showMessageDialog(null, lblColeccionSinImg, noImg, JOptionPane.ERROR_MESSAGE);
				} else {
					VerImg verImg = new VerImg(img);
					verImg.setVisible(true);
				}
			}
		});
		btnVerImg.setFont(new Font("Caladea", Font.PLAIN, 20));
		panelImg.add(btnVerImg);
		
		separator = new JSeparator();
		panelPrincipal.add(separator);
		
		panelBotones = new JPanel();
		FlowLayout flowLayout_4 = (FlowLayout) panelBotones.getLayout();
		flowLayout_4.setHgap(25);
		panelPrincipal.add(panelBotones);
		
		btnInsertar = new JButton("Insertar");
		btnInsertar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//INSERTAR UNA COLECCION
				if (txtNombre.getText().isBlank()) {
					lblInsertarColeccion.setFont(new Font("Caladea", Font.PLAIN, 16));
					JOptionPane.showMessageDialog(frmColecciones,lblInsertarColeccion,error,JOptionPane.ERROR_MESSAGE);
				} else {
					try {
						//Comprobar longitudes
						if (txtNombre.getText().length() > 200) {
							lblErrorAyuda.setFont(new Font("Caladea", Font.PLAIN, 16));
							JOptionPane.showMessageDialog(frmColecciones,
									lblErrorAyuda, error,
									JOptionPane.ERROR_MESSAGE);
						} else {
							//Solicitud a servidor de insertar coleccion
							Coleccion coleccion = new Coleccion(0,txtNombre.getText(),img);
							
							HiloCliente hilo = new HiloCliente(skCliente, "altaColeccion", coleccion,tbColecciones);
							hilo.start();
							
							img = null;
						}
					} catch (Exception e1) {
						lblErrorCampos.setFont(new Font("Caladea", Font.PLAIN, 16));
						JOptionPane.showMessageDialog(frmColecciones, lblErrorCampos, error,
								JOptionPane.ERROR_MESSAGE);
						 //e1.printStackTrace();
					}
				}
			}
		});
		btnInsertar.setPreferredSize(new Dimension(100,35));
		btnInsertar.setFont(new Font("Caladea", Font.PLAIN, 20));
		panelBotones.add(btnInsertar);
		
		btnModificar = new JButton("Modificar");
		btnModificar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//MODIFICAR UNA COLECCION
				if (txtID.getText().isBlank() || txtNombre.getText().isBlank()) {
					lblModificarColeccion.setFont(new Font("Caladea", Font.PLAIN, 16));
					JOptionPane.showMessageDialog(frmColecciones,lblModificarColeccion,error,JOptionPane.ERROR_MESSAGE);
				} else {
					try {
						//Comprobar longitudes
						if (txtNombre.getText().length() > 200) {
							lblErrorAyuda.setFont(new Font("Caladea", Font.PLAIN, 16));
							JOptionPane.showMessageDialog(frmColecciones,
									lblErrorAyuda, error,
									JOptionPane.ERROR_MESSAGE);
						} else {
							long aux = (long) NumberFormat.getNumberInstance().parse(txtID.getText().trim());
							Integer id = (int) aux;
							
							if (id < 0) {
								JOptionPane.showMessageDialog(frmColecciones,
										mensajeID, "Error",
										JOptionPane.ERROR_MESSAGE);
							} else {
								//Solicitud a servidor de modificar coleccion
								Coleccion coleccion = new Coleccion(id,txtNombre.getText(),img);
								
								if (img == null) {
									//Preguntar si desea mantener la imagen anterior o eliminarla
									ImageIcon icono = new ImageIcon(OperacionesComics.class.getResource("/img/app_icon.png"));
									ImageIcon iconoEscala = new ImageIcon(
											icono.getImage().getScaledInstance(50, 50, java.awt.Image.SCALE_FAST));
									int respuesta = JOptionPane.showOptionDialog(frmColecciones, preguntaImg,
											imgNoEscogidaTitulo, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, iconoEscala,
											opciones, opciones[0]);
									
									if (respuesta == 0) { //si elige si, mantengo la imagen
										Coleccion colAux = listaColecciones.get(tbColecciones.getSelectedRow());
										
										coleccion.setImg(colAux.getImg());
									}
								}
								
								HiloCliente hilo = new HiloCliente(skCliente, "modificarColeccion", coleccion,tbColecciones);
								hilo.start();
								
								img = null;
							}
						}
					} catch (Exception e1) {
						lblErrorCampos.setFont(new Font("Caladea", Font.PLAIN, 16));
						JOptionPane.showMessageDialog(frmColecciones, lblErrorCampos, error,
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
				//BORRAR UNA COLECCION
				if (txtID.getText().isBlank()) {
					lblBorrarColeccion.setFont(new Font("Caladea", Font.PLAIN, 16));
					JOptionPane.showMessageDialog(frmColecciones,lblBorrarColeccion,error,JOptionPane.ERROR_MESSAGE);
				} else {
					Integer id = null;
					boolean formato = true;
					
					try {
						try {
							long aux = (long) NumberFormat.getNumberInstance().parse(txtID.getText().trim());
							id = (int) aux;
						} catch (Exception e1) {
							formato = false;
							JOptionPane.showMessageDialog(frmColecciones, mensajeEntero,
									error, JOptionPane.ERROR_MESSAGE);
							// e1.printStackTrace();
						}	
						
						if (formato)
						if (id < 0) {
							JOptionPane.showMessageDialog(frmColecciones,
								mensajeID, error,JOptionPane.ERROR_MESSAGE);
						} else {
								Coleccion coleccion = new Coleccion(id, txtNombre.getText(), img);
								// Solicitud a servidor de borrar coleccion

								HiloCliente hilo = new HiloCliente(skCliente, "bajaColeccion", coleccion,
										tbColecciones);
								hilo.start();

								hilo.join();
								
								if (!hilo.existeColeccion) {
			                    	lblNoExisteCol.setFont(new Font("Caladea", Font.PLAIN, 16));
			                        JOptionPane.showMessageDialog(null, lblNoExisteCol, error, JOptionPane.ERROR_MESSAGE);
								} else {
									img = null;

									if (!numerosRelacionados.isEmpty()) {
										// Si hay numeros relacionados, permito que el usuario escoja si borrar todo
										lblBorrarCol.setFont(new Font("Caladea", Font.PLAIN, 16));
										ImageIcon icono = new ImageIcon(
												PantallaPrincipal.class.getResource("/img/app_icon.png"));
										ImageIcon iconoEscala = new ImageIcon(
												icono.getImage().getScaledInstance(50, 50, java.awt.Image.SCALE_FAST));
										int respuesta = JOptionPane.showOptionDialog(frmColecciones, lblBorrarCol,
												tituloBorrarColeccion, JOptionPane.YES_NO_OPTION,
												JOptionPane.QUESTION_MESSAGE, iconoEscala, opciones, opciones[1]);
										
										if (respuesta == 0) { // si elige si, mando al servidor coleccion y numeros relacionados y borra primero cada numero y luego col
											HiloCliente hilo3 = new HiloCliente(skCliente, "bajaColeccionYNumeros", coleccion,
													tbColecciones);
											hilo3.start();
										}
									} else {
				                    	lblBorradoCol.setText(borradoCol+coleccion.getNombre());
				                    	lblBorradoCol.setFont(new Font("Caladea", Font.PLAIN, 16));
										JOptionPane.showMessageDialog(null, lblBorradoCol, borradoListo, JOptionPane.INFORMATION_MESSAGE);
									}
								}
						}
						
					} catch (Exception e1) {
						lblErrorCampos.setFont(new Font("Caladea", Font.PLAIN, 16));
						JOptionPane.showMessageDialog(frmColecciones, lblErrorCampos, error,
								JOptionPane.ERROR_MESSAGE);
						 //e1.printStackTrace();
					}					
				}
			}
		});
		btnBorrar.setPreferredSize(new Dimension(90,35));
		btnBorrar.setFont(new Font("Caladea", Font.PLAIN, 20));
		panelBotones.add(btnBorrar);
		
		cargarColecciones(skCliente);
		
		ListSelectionModel listSelectionModel = tbColecciones.getSelectionModel();
		listSelectionModel.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				ListSelectionModel lsm = (ListSelectionModel) e.getSource();
				if (!lsm.isSelectionEmpty()) {

					Coleccion coleccion = listaColecciones.get(tbColecciones.getSelectedRow());

					String idConvertido = NumberFormat.getInstance().format(coleccion.getId());
					txtID.setText(idConvertido);
					txtID.setCaretPosition(0);
					
					txtNombre.setText(coleccion.getNombre());
					txtNombre.setCaretPosition(0);
					img = coleccion.getImg();
					
				} else {
					txtID.setText("");
					txtNombre.setText("");
					img = null;
				}
			}
		});
	
		PantallaPrincipal.helpBroker.enableHelpKey(txtID, rutaAyuda, PantallaPrincipal.helpSet);
		PantallaPrincipal.helpBroker.enableHelpKey(txtNombre, rutaAyuda, PantallaPrincipal.helpSet);
		
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
		mensajeAyuda = rb.getString("mensajeAyuda");
		error = rb.getString("error");
		lblImagenValor = rb.getString("lblImagenValor");
		btnEscogerValor = rb.getString("btnEscogerValor");
		imgNoEscogida = rb.getString("imgNoEscogida");
		noImg = rb.getString("noImg");
		imgNoEscogidaTitulo = rb.getString("imgNoEscogidaTitulo");
		lblImgGuardadaValor = rb.getString("lblImgGuardadaValor");
		imgGuardada = rb.getString("imgGuardada");
		tituloPantalla = rb.getString("tituloPantallaColecciones");
		lblNombreValor = rb.getString("lblNombreValor");
		lblColeccionSinImgValor = rb.getString("lblColeccionSinImgValor");
		mensajeInsertarColeccion = rb.getString("mensajeInsertarColeccion");
		errorCampos = rb.getString("errorCampos");
		mensajeModificarColeccion = rb.getString("mensajeModificarColeccion");
		mensajeID = rb.getString("mensajeID");
		preguntaImg = rb.getString("preguntaImgCol");
		mensajeEntero = rb.getString("mensajeEntero");
		lblNoExisteColValor = rb.getString("lblNoExisteColValor");
		preguntaBorrar = rb.getString("preguntaBorrarCol");
		borradoCol = rb.getString("borradoCol");
		tituloEscogerImg = rb.getString("tituloEscogerImg");
		
		lblPregunta.setText(mensajeSalir);
		lblError.setText(errorConexion);
		itemComics.setText(gestionComics);
		menuColecciones.setText(colecciones);
		itemColecciones.setText(gestionColecciones);
		lblErrorAyuda.setText(mensajeAyuda);
		lblImg.setText(lblImagenValor);
		btnEscogerImg.setText(btnEscogerValor);
		lblImgNoEscogida.setText(imgNoEscogida);
		lblImgGuardada.setText(lblImgGuardadaValor);
		lblErrorCampos.setText(errorCampos);
		frmColecciones.setTitle(tituloPantalla);
		lblNombre.setText(lblNombreValor);
		lblColeccionSinImg.setText(lblColeccionSinImgValor);
		lblInsertarColeccion.setText(mensajeInsertarColeccion);
		lblModificarColeccion.setText(mensajeModificarColeccion);
		lblNoExisteCol.setText(lblNoExisteColValor);
		lblBorrarCol.setText(preguntaBorrar);
		
	}

	/**
	 * Devuelve la ruta de una imagen seleccionada por el usuario
	 * @return Ruta de la imagen seleccionada
	 */
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
	
	/**
	 * Muestra un diálogo para escoger una image, que se muestra por defecto en el escritorio del usuario
	 * @param tituloEscogerImg Título del diálogo
	 * @return JFileChooser con las características predefinidas
	 */
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

	/**
	 * Carga todas las colecciones en la tabla
	 * @param skCliente Socket del cliente
	 */
	private void cargarColecciones(Socket skCliente) {
		listaColecciones.clear();
		
		HiloCliente hilo = new HiloCliente(skCliente, "cargarColecciones", null,tbColecciones);
		hilo.start();
		
		try {
			hilo.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
