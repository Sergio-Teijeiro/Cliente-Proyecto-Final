package Vista;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Properties;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;

import com.jtattoo.plaf.fast.FastLookAndFeel;

import Fuentes.Fuentes;
import javax.swing.border.EmptyBorder;

public class PantallaPrincipal {

	private JFrame frmPrincipal;
	private String[] opciones = { "Sí", "No" };
	private String mensajeSalir = "¿Deseas salir de la aplicación?", cerrarPrograma = "Cerrar programa", tituloPantalla = "Comicalia";
	private JMenu menuIdioma;
	private JMenuItem itemEspanhol, itemGallego;
	private JButton btnConectar, btnInfo;
	private JLabel lblTitulo;

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
		
		JPanel panelPrincipal = new JPanel();
		frmPrincipal.getContentPane().add(panelPrincipal, BorderLayout.CENTER);
		panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));

		JPanel panelTitulo = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panelTitulo.getLayout();
		flowLayout_1.setHgap(35);
		flowLayout_1.setVgap(35);
		panelPrincipal.add(panelTitulo);

		lblTitulo = new JLabel("COMICALIA");
		lblTitulo.setBorder(new EmptyBorder(15, 5, 0, 5));
		
		Fuentes fuentes = new Fuentes();
		Font fuenteTitulo = fuentes.getFuente(fuentes.BOUNCY, Font.BOLD, 80);
		
		lblTitulo.setFont(fuenteTitulo);
		panelTitulo.add(lblTitulo);
		
		btnInfo = new JButton("");
		btnInfo.setToolTipText("Ayuda");
		btnInfo.setMaximumSize(new Dimension(40, 40));
		btnInfo.setBounds(5, 5, 20, 20);
		btnInfo.setFocusPainted(false);
		btnInfo.setContentAreaFilled(false);
		btnInfo.setBorderPainted(false);
		
		ImageIcon icono = new ImageIcon(PantallaPrincipal.class.getResource("/ayuda/img/info.png"));
		btnInfo.setMaximumSize(new Dimension(40, 40));
		ImageIcon iconoEscala = new ImageIcon(icono.getImage().getScaledInstance(btnInfo.getWidth(),
				btnInfo.getHeight(), java.awt.Image.SCALE_FAST));
		btnInfo.setIcon(iconoEscala);
		
		panelTitulo.add(btnInfo);
		
		JPanel panelImagen = new JPanel();
		panelPrincipal.add(panelImagen);

		JButton btnImagen = new JButton("");

		btnImagen.setBounds(new Rectangle(0, 0, 800, 450));

		ImageIcon icono2 = new ImageIcon(PantallaPrincipal.class.getResource("/img/avengers.jpg"));
		ImageIcon iconoEscala2 = new ImageIcon(icono2.getImage().getScaledInstance(btnImagen.getWidth(),
				btnImagen.getHeight(), java.awt.Image.SCALE_FAST));

		btnImagen.setIcon(iconoEscala2);

		btnImagen.setFocusPainted(false);
		btnImagen.setContentAreaFilled(false);
		btnImagen.setBorderPainted(false);

		panelImagen.add(btnImagen);
	}

}
