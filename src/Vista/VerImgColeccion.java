package Vista;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.BorderLayout;

public class VerImgColeccion extends JDialog {

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
	
	public VerImgColeccion(byte[] img) {
		setResizable(false);
		
		setBounds(0, 0, 443, 680);
		setMinimumSize(new Dimension(443, 680));
		setMaximumSize(new Dimension(443, 680));
		setLocationRelativeTo(null);
		setIconImage(
				Toolkit.getDefaultToolkit().getImage(PantallaPrincipal.class.getResource("/img/app_icon.png")));
		setModalityType(ModalityType.APPLICATION_MODAL);
		
		/*JButton btnImgColeccion = new JButton();
		btnImgColeccion.setBounds(new Rectangle(0, 0, 443, 680));*/
		
		BufferedImage bufImg = null;
		try {
			bufImg = ImageIO.read(new ByteArrayInputStream(img));
		} catch (IOException ex) {
			Logger.getLogger(DetalleComic.class.getName()).log(Level.SEVERE, null, ex);
		}
		
		JLabel prueba = new JLabel();
		prueba.setBounds(new Rectangle(0, 0, 443, 680));

		ImageIcon iconoEscala = new ImageIcon(bufImg.getScaledInstance(prueba.getWidth(),
				prueba.getHeight(), java.awt.Image.SCALE_FAST));
		
		prueba.setIcon(iconoEscala);
		getContentPane().add(prueba, BorderLayout.CENTER);
		
		/*btnImgColeccion.setFocusPainted(false);
		btnImgColeccion.setContentAreaFilled(false);
		btnImgColeccion.setBorderPainted(false);
		
		btnImgColeccion.setIcon(iconoEscala);
		
		getContentPane().add(btnImgColeccion, BorderLayout.CENTER);*/

	}

}
