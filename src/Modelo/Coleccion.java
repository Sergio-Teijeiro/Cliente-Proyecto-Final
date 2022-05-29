package Modelo;

import java.awt.image.BufferedImage;
import java.io.Serializable;

/**
 * Modelo de colección. Contiene el ID de la colección asociada, su nombre y el array de bytes de su imagen
 * @author sergio
 *
 */
public class Coleccion implements Serializable{
	int id;
	String nombre;
	byte[] img;
	
	public Coleccion() {
		super();
	}

	public Coleccion(int id, String nombre, byte[] img) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.img = img;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public byte[] getImg() {
		return img;
	}

	public void setImg(byte[] img) {
		this.img = img;
	}

	@Override
	public String toString() {
		return nombre;
	}
}
