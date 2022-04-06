package Vista;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class PantallaBusqueda {

	JFrame frmBusqueda;
	private String[] opciones = { "Sí", "No" };
	private String mensajeSalir = "¿Deseas salir de la aplicación?", cerrarPrograma = "Cerrar programa";
	private String tituloPantalla = "Catálogo de cómics";
	private JMenu menuComics, menuColecciones, menuInformes;
	private JMenuItem itemBusqueda,itemComics,itemColecciones,itemInformes;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PantallaBusqueda window = new PantallaBusqueda();
					window.frmBusqueda.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public PantallaBusqueda() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
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
		
		itemComics = new JMenuItem("Gestión cómics");
		itemComics.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

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
		
		
	}

}
