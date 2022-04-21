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

import Controlador.HiloCliente;
import java.awt.FlowLayout;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

public class PantallaColecciones {

	JFrame frmColecciones;
	private String[] opciones = { "Sí", "No" };
	private String mensajeSalir = "¿Deseas salir de la aplicación?", cerrarPrograma = "Cerrar programa";
	private String tituloPantalla = "Gestión de colecciones";
	private JMenu menuComics, menuColecciones, menuInformes;
	private JMenuItem itemBusqueda,itemComics,itemColecciones,itemInformes;
	private JTable tbColecciones;	
	private JPanel panelID;
	private JLabel lblID, lblNombre, lblImg;
	private JPanel panelNombre;
	private JPanel panelImg;
	private JButton btnEscogerImg;
	
	private byte[] img = null;

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
		
		JScrollPane cabeceraTabla = new JScrollPane();
		cabeceraTabla.setBounds(0, 0, 594, 299);

		tbColecciones = new JTable();
		tbColecciones.setPreferredScrollableViewportSize(new Dimension(450, 250));
		tbColecciones.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
		tbColecciones.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tbColecciones.setBounds(60, 23, 506, 209);
		tbColecciones.getTableHeader().setReorderingAllowed(false); // impedir mover columnas

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
		lblID.setBorder(new EmptyBorder(0, 0, 0, 34));
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
		
		lblNombre = new JLabel("T\u00EDtulo");
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
		
		lblImg = new JLabel("Imagen");
		lblImg.setFont(new Font("Caladea", Font.PLAIN, 20));
		panelImg.add(lblImg);
		
		btnEscogerImg = new JButton("Escoger");
		btnEscogerImg.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Escoger una imagen y guardar sus bytes
				
				String rutaImg = seleccionarImagen();
				
		        if (rutaImg == null) {
		            JOptionPane.showMessageDialog(null, "No has seleccionado ninguna imagen", "Imagen no escogida", JOptionPane.WARNING_MESSAGE);
		        } else {
		        	File fichero = new File(rutaImg);
		        	try {
						img = Files.readAllBytes(fichero.toPath());
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
		            JOptionPane.showMessageDialog(null, "Se ha guardado la ruta de la imagen seleccionada", "Imagen guardada", JOptionPane.INFORMATION_MESSAGE);
		        }
			}
		});
		btnEscogerImg.setFont(new Font("Caladea", Font.PLAIN, 20));
		panelImg.add(btnEscogerImg);		
	}
	
	protected String seleccionarImagen() {
        String ruta = null;

        JFileChooser fc = escogerFichero();

        int eleccion = fc.showDialog(null, "Escoger");

        if (eleccion == JFileChooser.APPROVE_OPTION) {// si elige abrir el archivo
            File f = fc.getSelectedFile(); // obtiene el archivo seleccionado
            ruta = f.getAbsolutePath();
        }

        return ruta;		
		
	}	
	
    private static JFileChooser escogerFichero() {

        JFileChooser fc = new JFileChooser();
        String escritorio = System.getProperty("user.home") + "/desktop";//carpeta donde se abre por defecto (escritorio del usuario)
        File destino = new File(escritorio);

        fc.setCurrentDirectory(destino);
        fc.setDialogTitle("Elegir imagen de colección");
        fc.setMultiSelectionEnabled(false);

        //el primer parametro es la descripcion de las extensiones aceptadas y el resto son las extensiones aceptadas
        FileNameExtensionFilter filtro = new FileNameExtensionFilter(".jpg, .jpeg, .png", "jpg", "jpeg", "png");

        fc.setFileFilter(filtro);// aplica un filtro de extensiones

        return fc;
    }

}
