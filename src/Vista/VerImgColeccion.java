package Vista;

import java.awt.EventQueue;

import javax.swing.JDialog;

public class VerImgColeccion extends JDialog {

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VerImgColeccion dialog = new VerImgColeccion(null);
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
	public VerImgColeccion(byte[] img) {
		setBounds(100, 100, 450, 300);

	}

}
