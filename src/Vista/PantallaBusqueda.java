package Vista;

import java.awt.EventQueue;

import javax.swing.JFrame;

public class PantallaBusqueda {

	private JFrame frmBusqueda;

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
		frmBusqueda.setBounds(100, 100, 450, 300);
		frmBusqueda.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
