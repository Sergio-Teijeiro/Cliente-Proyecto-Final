package Vista;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.JDialog;

import Modelo.Numero;
import javax.swing.JLabel;

import Fuentes.Fuentes;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.BoxLayout;
import java.awt.FlowLayout;

public class DetalleComic extends JDialog {

	private String tituloPantalla = "";
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DetalleComic dialog = new DetalleComic(null);
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the dialog.
	 */
	public DetalleComic(Numero numero) {
		setResizable(false);
		tituloPantalla = numero.getTitulo();
		
		setBounds(0, 0, 450, 300);
		setMinimumSize(new Dimension(850, 720));
		setMaximumSize(new Dimension(850, 720));
		setTitle(tituloPantalla);
		setLocationRelativeTo(null);
		setIconImage(
				Toolkit.getDefaultToolkit().getImage(PantallaPrincipal.class.getResource("/img/app_icon.png")));
		setModalityType(ModalityType.APPLICATION_MODAL);
		
		JPanel panelDatos = new JPanel();
		getContentPane().add(panelDatos, BorderLayout.NORTH);
		panelDatos.setLayout(new BoxLayout(panelDatos, BoxLayout.Y_AXIS));
		
		JPanel panelIDTitulo = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panelIDTitulo.getLayout();
		flowLayout.setHgap(25);
		flowLayout.setAlignment(FlowLayout.LEFT);
		flowLayout.setVgap(25);
		panelDatos.add(panelIDTitulo);
		
		JLabel lblID = new JLabel("ID");
		lblID.setBorder(new EmptyBorder(5, 0, 0, 5));
		Fuentes fuentes = new Fuentes();
		Font fuenteID = fuentes.getFuente(fuentes.BOUNCY, Font.PLAIN, 30);
		
		lblID.setFont(fuenteID);
		panelIDTitulo.add(lblID);
		
		JLabel lblValorID = new JLabel(String.valueOf(numero.getId()));
		lblValorID.setFont(new Font("Caladea", Font.PLAIN, 20));
		panelIDTitulo.add(lblValorID);
		
		JLabel lblTitulo = new JLabel("TÍTULO");
		lblTitulo.setBorder(new EmptyBorder(10, 0, 0, 5));
		Font fuenteTitulo = fuentes.getFuente(fuentes.BOUNCY, Font.PLAIN, 30);
		
		lblTitulo.setFont(fuenteTitulo);
		panelIDTitulo.add(lblTitulo);
		
		JTextField txt = new JTextField();
		txt.setPreferredSize(new Dimension(20,40));
		txt.setColumns(30);
		txt.setText(numero.getTitulo());
		txt.setFont(new Font("Caladea", Font.PLAIN, 20));
		txt.setCaretPosition(0); //poner cursor al principio
		txt.setEditable(false);
		panelIDTitulo.add(txt);
		

	}

}
