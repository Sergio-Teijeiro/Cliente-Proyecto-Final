package Vista;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;

import com.jtattoo.plaf.fast.FastLookAndFeel;

public class PantallaPrincipal {

	private JFrame frmPrincipal;
	private String[] opciones = { "S�", "No" };
	private String mensajeSalir = "�Deseas salir de la aplicaci�n?", cerrarPrograma = "Cerrar programa", tituloPantalla = "Comicalia";
	private JMenu menuIdioma;
	private JMenuItem itemEspanhol, itemGallego;
	private JButton btnConectar, btnInfo;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Properties props = new Properties();
					props.put("logoString", "...");
					FastLookAndFeel.setCurrentTheme(props);
					UIManager.setLookAndFeel(new FastLookAndFeel());
					
					PantallaPrincipal window = new PantallaPrincipal();
					window.frmPrincipal.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public PantallaPrincipal() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmPrincipal = new JFrame();
		
		frmPrincipal.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// dialogo confirmacion
				ImageIcon icono = new ImageIcon(PantallaPrincipal.class.getResource("/img/app_icon.png"));
				ImageIcon iconoEscala = new ImageIcon(
						icono.getImage().getScaledInstance(50, 50, java.awt.Image.SCALE_FAST));
				int respuesta = JOptionPane.showOptionDialog(frmPrincipal, mensajeSalir,
						cerrarPrograma, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, iconoEscala,
						opciones, opciones[1]);

				if (respuesta == 1) { // si elige no cerrar, especifico que no haga nada
					frmPrincipal.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

				} else if (respuesta == 0) {
					frmPrincipal.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				} else { // si cierra el dialogo de confirmacion, no cierra la ventana principal
					frmPrincipal.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				}
			}
		});
		frmPrincipal.setMinimumSize(new Dimension(890, 760));
		frmPrincipal.setTitle(tituloPantalla);
		frmPrincipal.setBounds(100, 100, 450, 300);
		frmPrincipal.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmPrincipal.setLocationRelativeTo(null);
		frmPrincipal.setIconImage(
				Toolkit.getDefaultToolkit().getImage(PantallaPrincipal.class.getResource("/img/app_icon.png")));
		
		JMenuBar menuBar = new JMenuBar();
		frmPrincipal.setJMenuBar(menuBar);

		menuIdioma = new JMenu("Cambiar idioma");
		menuIdioma.setFont(new Font("Caladea", Font.PLAIN, 16));
		menuBar.add(menuIdioma);
		
		itemEspanhol = new JMenuItem("Espa\u00F1ol");
		itemEspanhol.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

			}
		});
		itemEspanhol.setFont(new Font("Caladea", Font.PLAIN, 16));
		menuIdioma.add(itemEspanhol);
		
		itemGallego = new JMenuItem("Gallego");
		itemGallego.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

			}
		});
		itemGallego.setFont(new Font("Caladea", Font.PLAIN, 16));
		menuIdioma.add(itemGallego);
		
		JPanel panelBotones = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panelBotones.getLayout();
		flowLayout.setVgap(15);
		frmPrincipal.getContentPane().add(panelBotones, BorderLayout.SOUTH);
		
		btnConectar = new JButton("Conectar");
		btnConectar.setPreferredSize(new Dimension(180, 30));
		btnConectar.setFont(new Font("Caladea", Font.PLAIN, 18));
		btnConectar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		btnConectar.setMargin(new Insets(2, 44, 2, 44));
		panelBotones.add(btnConectar);
	}

}
