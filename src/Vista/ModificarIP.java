package Vista;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.event.ActionListener;
import java.net.Socket;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.Dimension;

/**
 * Pantalla que permite especificar la dirección IP a la que se va a conectar el cliente del servidor
 * @author sergio
 *
 */
public class ModificarIP extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField txtIP;
	private JLabel lblIP;
	private JButton btnAceptar, btnCancelar;
	private String error = "Error", mensajeInsertarIP = "Debes asignar una IP", mensajePatronIP = "La IP insertada no es válida", errorConectar = "Error al conectar",
			errorConexion = "No se ha podido conectar con el servidor";
	JLabel lblError = new JLabel(mensajeInsertarIP);
	JLabel lblErrorPatronIP = new JLabel(mensajePatronIP);
	JLabel lblErrorConectar = new JLabel(errorConexion);
	
	public static String ipValida;

	public static void main(String[] args) {
		try {
			ModificarIP dialog = new ModificarIP(null,null);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ModificarIP(Socket skCliente, JFrame frmPrincipal) {
		setResizable(false);
		setUndecorated(true);
		setModalityType(ModalityType.APPLICATION_MODAL); //dialogo MODAL
		
		setBounds(100, 100, 400, 150);
		getContentPane().setLayout(new BorderLayout(0, 0));
		contentPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		getContentPane().add(contentPanel);
		contentPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel panelIP = new JPanel();
		contentPanel.add(panelIP, BorderLayout.NORTH);
		panelIP.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 10));
		{
			lblIP = new JLabel("IP");
			lblIP.setFont(new Font("Caladea", Font.PLAIN, 18));
			panelIP.add(lblIP);
		}
		{
			txtIP = new JTextField("192.168.56.104");
			txtIP.setFont(new Font("Caladea", Font.PLAIN, 18));
			panelIP.add(txtIP);
			txtIP.setColumns(20);
		}
		{
			JPanel botonera = new JPanel();
			contentPanel.add(botonera, BorderLayout.SOUTH);
			botonera.setLayout(new FlowLayout(FlowLayout.RIGHT));
			{
				btnAceptar = new JButton("Aceptar");
				btnAceptar.setPreferredSize(new Dimension(100, 30));
				btnAceptar.setFont(new Font("Caladea", Font.PLAIN, 18));
				btnAceptar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (txtIP.getText().isBlank()) {
							lblError.setFont(new Font("Caladea", Font.PLAIN, 20));
							JOptionPane.showMessageDialog(contentPanel, lblError, error, JOptionPane.ERROR_MESSAGE);
						} else {
							String ip = txtIP.getText().trim();
							
							if (!isValidIPAddress(ip)) {
								lblErrorPatronIP.setFont(new Font("Caladea", Font.PLAIN, 20));
								JOptionPane.showMessageDialog(contentPanel, lblErrorPatronIP, error, JOptionPane.ERROR_MESSAGE);
							} else {
								Socket socketCliente = skCliente;
								try {
						            //Nos Conectamos a un Servidor mediante IP+PUERTO
									socketCliente = new Socket(ip, 2000);
						            
						            PantallaBusqueda p = new PantallaBusqueda(socketCliente);
						            
						            p.frmBusqueda.setVisible(true);
									
									dispose();
									
									frmPrincipal.dispose();
									
									ipValida = ip;
								} catch (Exception ex) {
						            if (ex.getClass().getName().equals("java.net.ConnectException")) {
						            	lblErrorConectar.setFont(new Font("Caladea", Font.PLAIN, 20));
						            	JOptionPane.showMessageDialog(contentPanel,lblErrorConectar, errorConectar,
						            			JOptionPane.ERROR_MESSAGE);
						            }
						            //ex.printStackTrace();
						        }

							}
									
							
						}
					}
				});
				btnAceptar.setActionCommand("OK");
				botonera.add(btnAceptar);
				getRootPane().setDefaultButton(btnAceptar);
			}
			{
				btnCancelar = new JButton("Cancelar");
				btnCancelar.setFont(new Font("Caladea", Font.PLAIN, 18));
				btnCancelar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				btnCancelar.setActionCommand("Cancel");
				botonera.add(btnCancelar);
			}
		}
		
		traducir();
	}
	
	/**
	 * Traduce la pantalla actual según el idioma establecido por el usuario.
	 */
	private void traducir() {
		ResourceBundle rb = ResourceBundle.getBundle("traduccion");
		
		errorConexion = rb.getString("errorConexion");
		errorConectar = rb.getString("errorConectar");
		mensajeInsertarIP = rb.getString("mensajeInsertarIP");
		mensajePatronIP = rb.getString("mensajePatronIP");
		
		lblErrorConectar.setText(errorConexion);
		lblError.setText(mensajeInsertarIP);
		lblErrorPatronIP.setText(mensajePatronIP);
		
	}

	/**
	 * Función que valida la dirección IP introducida
	 * @param ip Cadena de la dirección IP escrita
	 * @return Si la cadena es una dirección IP válida
	 */
    public static boolean isValidIPAddress(String ip)
    {
 
        // Regex for digit from 0 to 255.
        String zeroTo255
            = "(\\d{1,2}|(0|1)\\"
              + "d{2}|2[0-4]\\d|25[0-5])";
 
        // Regex for a digit from 0 to 255 and
        // followed by a dot, repeat 4 times.
        // this is the regex to validate an IP address.
        String regex
            = zeroTo255 + "\\."
              + zeroTo255 + "\\."
              + zeroTo255 + "\\."
              + zeroTo255;
 
        // Compile the ReGex
        Pattern p = Pattern.compile(regex);
 
        // If the IP address is empty
        // return false
        if (ip == null) {
            return false;
        }
 
        // Pattern class contains matcher() method
        // to find matching between given IP address
        // and regular expression.
        Matcher m = p.matcher(ip);
 
        // Return if the IP address
        // matched the ReGex
        return m.matches();
    }
	
}
