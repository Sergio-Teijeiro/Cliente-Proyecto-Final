package Fuentes;

import java.awt.Font;
import java.io.InputStream;

public class Fuentes {
	private Font font = null;
    public static String BOUNCY = "Bouncy.otf";
    
    // Font.PLAIN = 0 , Font.BOLD = 1 , Font.ITALIC = 2
    
    public Font getFuente(String fontName, int estilo, float tamanho)
    {
         try {
            //Se carga la fuente
            InputStream is =  getClass().getResourceAsStream(fontName);
            font = Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (Exception ex) {
            //Si existe un error se carga fuente por defecto
            System.err.println(fontName + " No se cargo la fuente");
            font = new Font("Caladea", Font.PLAIN, 60);            
        }
         
        Font tfont = font.deriveFont(estilo, tamanho);
        return tfont;
    }
}
