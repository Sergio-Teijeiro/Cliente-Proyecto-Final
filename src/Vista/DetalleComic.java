package Vista;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JDialog;

import Modelo.Coleccion;
import Modelo.Numero;
import Modelo.TablaComics;

import javax.swing.JLabel;

import Fuentes.Fuentes;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import Controlador.HiloCliente;

import javax.imageio.ImageIO;
import javax.swing.BoundedRangeModel;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;

import java.awt.FlowLayout;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JButton;

public class DetalleComic extends JDialog {

	private String tituloPantalla = "";
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DetalleComic dialog = new DetalleComic(null,null);
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
	 * @param skCliente 
	 */
	public DetalleComic(Numero numero, Socket skCliente) {
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
		Font fuente = fuentes.getFuente(fuentes.BOUNCY, Font.PLAIN, 30);
		
		lblID.setFont(fuente);
		panelIDTitulo.add(lblID);
		
		JTextField txtID = new JTextField();
		txtID.setPreferredSize(new Dimension(10,40));
		txtID.setColumns(3);
		txtID.setText(String.valueOf(numero.getId()));
		txtID.setFont(new Font("Caladea", Font.PLAIN, 20));
		txtID.setCaretPosition(0); //poner cursor al principio
		txtID.setEditable(false);
		panelIDTitulo.add(txtID);
		
		JLabel lblTitulo = new JLabel("TÍTULO");
		lblTitulo.setBorder(new EmptyBorder(10, 0, 0, 5));
		
		lblTitulo.setFont(fuente);
		panelIDTitulo.add(lblTitulo);
		
		JTextField txtTitulo = new JTextField();
		txtTitulo.setPreferredSize(new Dimension(20,40));
		txtTitulo.setColumns(30);
		txtTitulo.setText(numero.getTitulo());
		txtTitulo.setFont(new Font("Caladea", Font.PLAIN, 20));
		txtTitulo.setCaretPosition(0); //poner cursor al principio
		txtTitulo.setEditable(false);
		panelIDTitulo.add(txtTitulo);
		
		JPanel panelFechaTapa = new JPanel();
		FlowLayout flowLayout2 = (FlowLayout) panelFechaTapa.getLayout();
		flowLayout2.setHgap(25);
		flowLayout2.setAlignment(FlowLayout.LEFT);
		flowLayout2.setVgap(25);
		panelDatos.add(panelFechaTapa);
		
		JLabel lblFecha = new JLabel("FECHA ADQUISICIÓN");
		lblFecha.setBorder(new EmptyBorder(10, 0, 10, 5));
		
		lblFecha.setFont(fuente);
		panelFechaTapa.add(lblFecha);
		
		JTextField txtFecha= new JTextField();
		txtFecha.setPreferredSize(new Dimension(20,40));
		txtFecha.setColumns(7);
		txtFecha.setText(numero.getFechaAdquisicion().toString());
		txtFecha.setFont(new Font("Caladea", Font.PLAIN, 20));
		txtFecha.setCaretPosition(0); //poner cursor al principio
		txtFecha.setEditable(false);
		panelFechaTapa.add(txtFecha);
		
		JLabel lblTapa= new JLabel("TAPA");
		lblTapa.setBorder(new EmptyBorder(5, 0, 0, 5));
		
		lblTapa.setFont(fuente);
		panelFechaTapa.add(lblTapa);
		
		JTextField txtTapa= new JTextField();
		txtTapa.setPreferredSize(new Dimension(20,40));
		txtTapa.setColumns(7);
		txtTapa.setText(numero.getTapa());
		txtTapa.setFont(new Font("Caladea", Font.PLAIN, 20));
		txtTapa.setCaretPosition(0); //poner cursor al principio
		txtTapa.setEditable(false);
		panelFechaTapa.add(txtTapa);
		
		JPanel panelEstadoResenha = new JPanel();
		FlowLayout flowLayout3 = (FlowLayout) panelEstadoResenha.getLayout();
		flowLayout3.setHgap(25);
		flowLayout3.setAlignment(FlowLayout.LEFT);
		panelDatos.add(panelEstadoResenha);
		
		JLabel lblEstado = new JLabel("ESTADO");
		lblEstado.setBorder(new EmptyBorder(10, 0, 0, 5));
		
		lblEstado.setFont(fuente);
		panelEstadoResenha.add(lblEstado);
		
		JTextField txtEstado= new JTextField();
		txtEstado.setPreferredSize(new Dimension(20,40));
		txtEstado.setColumns(7);
		txtEstado.setText(numero.getEstado());
		txtEstado.setFont(new Font("Caladea", Font.PLAIN, 20));
		txtEstado.setCaretPosition(0); //poner cursor al principio
		txtEstado.setEditable(false);
		panelEstadoResenha.add(txtEstado);
		
		JLabel lblResenha = new JLabel("RESEÑA");
		lblResenha.setBorder(new EmptyBorder(10, 0, 0, 5));
		
		lblResenha.setFont(fuente);
		panelEstadoResenha.add(lblResenha);
		
		JTextArea txtAreaResenha = new JTextArea();
		txtAreaResenha.setWrapStyleWord(true);
		txtAreaResenha.setMaximumSize(new Dimension(20,150));
		txtAreaResenha.setPreferredSize(new Dimension(20,150));
		txtAreaResenha.setColumns(20);
		txtAreaResenha.setText(numero.getResenha());
		txtAreaResenha.setFont(new Font("Caladea", Font.PLAIN, 20));
		txtAreaResenha.setCaretPosition(0); //poner cursor al principio
		txtAreaResenha.setEditable(false);
		txtAreaResenha.setLineWrap(true);
		panelEstadoResenha.add(txtAreaResenha);
		
		JPanel panelImg = new JPanel();
		FlowLayout flowLayout4 = (FlowLayout) panelImg.getLayout();
		flowLayout4.setVgap(0);
		flowLayout4.setHgap(25);
		flowLayout4.setAlignment(FlowLayout.LEFT);
		panelDatos.add(panelImg);
		
		JButton btnImgNumero = new JButton();

		btnImgNumero.setBounds(new Rectangle(0, 0, 216, 332));
		//btnImgNumero.setBounds(new Rectangle(0, 0, 400, 330));
		
		byte[] data = numero.getImg();
		BufferedImage img = null;
		try {
			img = ImageIO.read(new ByteArrayInputStream(data));
		} catch (IOException ex) {
			Logger.getLogger(DetalleComic.class.getName()).log(Level.SEVERE, null, ex);
		}

		ImageIcon iconoEscala = new ImageIcon(img.getScaledInstance(btnImgNumero.getWidth(),
				btnImgNumero.getHeight(), java.awt.Image.SCALE_FAST));

		btnImgNumero.setIcon(iconoEscala);

		btnImgNumero.setFocusPainted(false);
		btnImgNumero.setContentAreaFilled(false);
		btnImgNumero.setBorderPainted(false);
		panelImg.add(btnImgNumero);
		
		JButton btnImgColeccion = new JButton();
		btnImgColeccion.setFocusPainted(false);
		btnImgColeccion.setContentAreaFilled(false);
		//btnImgColeccion.setBounds(new Rectangle(0, 0, 400, 330));
		btnImgColeccion.setBounds(new Rectangle(0, 0, 216, 332));
		btnImgColeccion.setBorderPainted(false);
		
		Coleccion coleccion = new Coleccion();
		
		HiloCliente hilo = new HiloCliente(skCliente,"colByComic",numero,coleccion);
		hilo.start();
		
		try {
			hilo.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		coleccion = TablaComics.coleccion;
		
		byte[] dataCol = numero.getImg();
		BufferedImage imgCol = null;
		try {
			imgCol = ImageIO.read(new ByteArrayInputStream(dataCol));
		} catch (IOException ex) {
			Logger.getLogger(DetalleComic.class.getName()).log(Level.SEVERE, null, ex);
		}

		ImageIcon iconoEscala2 = new ImageIcon(imgCol.getScaledInstance(btnImgColeccion.getWidth(),
				btnImgColeccion.getHeight(), java.awt.Image.SCALE_FAST));

		btnImgColeccion.setIcon(iconoEscala2);
		
		panelImg.add(btnImgColeccion);
	}

}
