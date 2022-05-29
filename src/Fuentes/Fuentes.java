package Fuentes;

import java.awt.Font;
import java.io.InputStream;

/**
 * Clase para gestionar fuentes personalizadas
 * @author sergio
 *
 */
public class Fuentes {
	private Font font = null;
    public static String BOUNCY = "Bouncy.otf";
    
    // Font.PLAIN = 0 , Font.BOLD = 1 , Font.ITALIC = 2
    
    /**
     * Obtiene una fuente determinada
     * @param fontName Nombre de la fuente
     * @param estilo Estilo de la fuente: 0 (Font.PLAIN), 1 (Font.BOLD) o 2 (Font.ITALIC)
     * @param tamanho Tamaño de la fuente
     * @return Fuente creada a partir de un fichero .otf con el nombre fontName
     */
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
