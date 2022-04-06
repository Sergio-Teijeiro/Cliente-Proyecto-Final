package Vista;

import java.awt.BorderLayout;
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

import Controlador.HiloCliente;
import Fuentes.Fuentes;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.JButton;

public class PantallaBusqueda {

	JFrame frmBusqueda;
	private String[] opciones = { "S�", "No" };
	private String mensajeSalir = "�Deseas salir de la aplicaci�n?", cerrarPrograma = "Cerrar programa";
	private String tituloPantalla = "Cat�logo de c�mics";
	private JMenu menuComics, menuColecciones, menuInformes;
	private JMenuItem itemBusqueda,itemComics,itemColecciones,itemInformes;
	private JLabel lblTitulo;
	private JPanel panelBusquedaCol;
	private JLabel lblColeccion;
	private JTextField txtColeccion;
	private JButton btnLupa;
	private JPanel panelBusquedaComic;
	private JLabel lblComic;
	private JTextField txtComic;
	private JButton btnLupa2;
	private JTable tbComics;

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
				int respuesta = JOptionPane.showOptionDialog(frmBusqueda, mensajeSalir,
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
		
		menuComics = new JMenu("C�mics");
		menuComics.setFont(new Font("Caladea", Font.PLAIN, 16));
		menuBar.add(menuComics);
		
		itemBusqueda = new JMenuItem("Consultar c�mics");
		itemBusqueda.setFont(new Font("Caladea", Font.PLAIN, 16));
		menuComics.add(itemBusqueda);
		
		itemComics = new JMenuItem("Gesti�n c�mics");
		itemComics.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

			}
		});
		itemComics.setFont(new Font("Caladea", Font.PLAIN, 16));
		menuComics.add(itemComics);
		
		menuColecciones = new JMenu("Colecciones");
		menuColecciones.setFont(new Font("Caladea", Font.PLAIN, 16));
		menuBar.add(menuColecciones);
		
		itemColecciones = new JMenuItem("Gesti�n colecciones");
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
		
		JPanel panelPrincipal = new JPanel();
		frmBusqueda.getContentPane().add(panelPrincipal, BorderLayout.CENTER);
		panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));

		JPanel panelTitulo = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panelTitulo.getLayout();
		flowLayout_1.setHgap(35);
		flowLayout_1.setVgap(35);
		panelPrincipal.add(panelTitulo);

		lblTitulo = new JLabel("CAT�LOGO DE C�MICS");
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
		panelBusquedaCol.add(txtColeccion);
		txtColeccion.setColumns(30);
		
		btnLupa = new JButton("");
		btnLupa.setBorder(new EmptyBorder(0, 0, 0, 0));
		btnLupa.setToolTipText("Buscar c�mic seg�n colecci�n");
		btnLupa.setMaximumSize(new Dimension(40, 40));
		btnLupa.setBounds(5, 5, 20, 20);
		btnLupa.setFocusPainted(false);
		btnLupa.setContentAreaFilled(false);
		btnLupa.setBorderPainted(false);
		
		ImageIcon icono = new ImageIcon(PantallaBusqueda.class.getResource("/img/lupa.png"));
		btnLupa.setMaximumSize(new Dimension(40, 40));
		ImageIcon iconoEscala = new ImageIcon(icono.getImage().getScaledInstance(btnLupa.getWidth(),
				btnLupa.getHeight(), java.awt.Image.SCALE_FAST));
		btnLupa.setIcon(iconoEscala);

		panelBusquedaCol.add(btnLupa);
		
		panelBusquedaComic = new JPanel();
		FlowLayout fl_panelBusquedaComic = (FlowLayout) panelBusquedaComic.getLayout();
		fl_panelBusquedaComic.setHgap(20);
		panelPrincipal.add(panelBusquedaComic);
		
		lblComic = new JLabel("C\u00F3mic");
		lblComic.setBorder(new EmptyBorder(0, 0, 0, 30));
		lblComic.setFont(new Font("Caladea", Font.PLAIN, 20));
		panelBusquedaComic.add(lblComic);
		
		txtComic = new JTextField();
		txtComic.setColumns(30);
		panelBusquedaComic.add(txtComic);
		
		btnLupa2 = new JButton("");
		btnLupa2.setToolTipText("Buscar c\u00F3mic seg\u00FAn t�tulo");
		btnLupa2.setMaximumSize(new Dimension(40, 40));
		btnLupa2.setFocusPainted(false);
		btnLupa2.setContentAreaFilled(false);
		btnLupa2.setBorderPainted(false);
		btnLupa2.setBorder(new EmptyBorder(0, 0, 0, 0));
		
		ImageIcon icono2 = new ImageIcon(PantallaBusqueda.class.getResource("/img/lupa.png"));
		btnLupa2.setMaximumSize(new Dimension(40, 40));
		ImageIcon iconoEscala2 = new ImageIcon(icono2.getImage().getScaledInstance(btnLupa.getWidth(),
				btnLupa.getHeight(), java.awt.Image.SCALE_FAST));
		btnLupa2.setIcon(iconoEscala2);
		
		panelBusquedaComic.add(btnLupa2);
		
		JScrollPane cabeceraTabla = new JScrollPane();
		cabeceraTabla.setBounds(0,0,594,299);
		
		tbComics = new JTable();
		tbComics.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
		tbComics.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tbComics.setBounds(60,23,506,209);
		tbComics.getTableHeader().setReorderingAllowed(false); //impedir mover columnas
		
		JPanel panelTabla = new JPanel();
		frmBusqueda.getContentPane().add(panelTabla, BorderLayout.SOUTH);
		panelTabla.setLayout(new BorderLayout(0, 0));
		
		tbComics.setFillsViewportHeight(true);
		cabeceraTabla.setViewportView(tbComics);
		
		panelTabla.add(cabeceraTabla, BorderLayout.CENTER);
		
	}

}
