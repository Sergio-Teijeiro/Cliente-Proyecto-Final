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
import java.sql.Date;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

import Controlador.HiloCliente;
import Modelo.*;

import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JSeparator;

public class OperacionesComics {

	JFrame frmComics;
	private String[] opciones = { "Sí", "No" };
	private String mensajeSalir = "¿Deseas salir de la aplicación?", cerrarPrograma = "Cerrar programa";
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
	private JButton btnBorrar;
	private String mensajeInsertarNumero = "Debes insertar un título y seleccionar una colección", 
			formatoFecha = "La fecha debe tener el formato dd-MM-yyyy";
	private String errorCampos = "Error con los campos", mensajeAyuda = "Comprueba la ayuda para ver la longitud máxima de cada campo";
	
	private byte[] img = null;

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
				JLabel lblPregunta = new JLabel(mensajeSalir);
				lblPregunta.setFont(new Font("Caladea", Font.PLAIN, 16));
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
		
		lblFecha = new JLabel("Fecha adquisici\u00F3n");
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
		
		modeloComboTapas.addElement("Blanda");
		modeloComboTapas.addElement("Dura");
		
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
		
		lblResenha = new JLabel("Rese\u00F1a");
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
		
		lblImg = new JLabel("Imagen");
		lblImg.setFont(new Font("Caladea", Font.PLAIN, 20));
		panelImgColeccion.add(lblImg);
		
		btnEscogerImg = new JButton("Escoger");
		btnEscogerImg.setFont(new Font("Caladea", Font.PLAIN, 20));
		panelImgColeccion.add(btnEscogerImg);
		
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
					JLabel lblError = new JLabel(mensajeInsertarNumero);
					lblError.setFont(new Font("Caladea", Font.PLAIN, 16));
					JOptionPane.showMessageDialog(frmComics,lblError,"Error",JOptionPane.ERROR_MESSAGE);
				} else {
					try {
						//Comprobar longitudes
						if (txtTitulo.getText().length() > 200 || txtEstado.getText().length() > 50 || txtAreaResenha.getText().length() > 150) {
							JLabel lblError = new JLabel(mensajeAyuda);
							lblError.setFont(new Font("Caladea", Font.PLAIN, 16));
							JOptionPane.showMessageDialog(frmComics,
									lblError, "Error",
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
									
								} catch (Exception e1) {
									formato = false;
									JLabel lblError = new JLabel(formatoFecha);
									lblError.setFont(new Font("Caladea", Font.PLAIN, 16));
									JOptionPane.showMessageDialog(frmComics,
											lblError, "Error",
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
								
								HiloCliente hilo = new HiloCliente(skCliente, "altaNumero", numero);
								hilo.start();
								
								hilo.join();
								
								cargarComics(skCliente);
							}
						}
					} catch (Exception e1) {
						JLabel lblError = new JLabel(errorCampos);
						lblError.setFont(new Font("Caladea", Font.PLAIN, 16));
						JOptionPane.showMessageDialog(frmComics, lblError, "Error",
								JOptionPane.ERROR_MESSAGE);
						 //e1.printStackTrace();
					}
				}
			}
		});
		btnInsertar.setFont(new Font("Caladea", Font.PLAIN, 20));
		panelBotones.add(btnInsertar);
		
		btnModificar = new JButton("Modificar");
		btnModificar.setPreferredSize(new Dimension(114,35));
		btnModificar.setFont(new Font("Caladea", Font.PLAIN, 20));
		panelBotones.add(btnModificar);
		
		btnBorrar = new JButton("Borrar");
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
					}
					
					if (numero.getTapa().equals("Blanda")) {
						cmbTapas.setSelectedIndex(0);
					} else {
						cmbTapas.setSelectedIndex(1);
					}

					if (numero.getEstado() != null)
					txtEstado.setText(numero.getEstado());
					txtEstado.setCaretPosition(0);
					
					if (numero.getResenha() != null)
						txtAreaResenha.setText(numero.getResenha());
						txtAreaResenha.setCaretPosition(0);
					
					for (int i=0;i<modeloComboColecciones.getSize();i++) {
						if (modeloComboColecciones.getElementAt(i).getId() == numero.getIdColeccion()) {
							modeloComboColecciones.setSelectedItem(modeloComboColecciones.getElementAt(i));
							break;
						}
					}
					

				} else {
					txtID.setText("");
					txtTitulo.setText("");
					txtFecha.setText("");
					cmbTapas.setSelectedIndex(0);
					txtEstado.setText("");
					txtAreaResenha.setText("");
					cmbColecciones.setSelectedIndex(0);
				}
			}
		});
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
		
		for (int i=0;i<modeloComboColecciones.getSize();i++) {
			if (modeloComboColecciones.getElementAt(i).getNombre().length() > 55) {
				String nuevoNombre = modeloComboColecciones.getElementAt(i).getNombre().substring(0, 51)+"...";
				modeloComboColecciones.getElementAt(i).setNombre(nuevoNombre);
			}
		}
	}
	
	private void cargarComics(Socket skCliente) {
		PantallaBusqueda.listaComics.clear();
		
		HiloCliente hilo = new HiloCliente(skCliente,"cargarComics",null,tbComics);
		hilo.start();
		
		try {
			hilo.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
