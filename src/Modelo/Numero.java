package Modelo;

import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.sql.Date;

public class Numero implements Serializable {
	int id;
	String titulo;
	Date fechaAdquisicion;
	String tapa;
	String estado;
	String resenha;
	byte[] img;
	//BufferedImage img;
	int idColeccion;
	
	public Numero() {
		super();
	}

	public Numero(int id, String titulo, Date fechaAdquisicion, String tapa, String estado, String resenha,
			byte[] img, int idColeccion) {
		super();
		this.id = id;
		this.titulo = titulo;
		this.fechaAdquisicion = fechaAdquisicion;
		this.tapa = tapa;
		this.estado = estado;
		this.resenha = resenha;
		this.img = img;
		this.idColeccion = idColeccion;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public Date getFechaAdquisicion() {
		return fechaAdquisicion;
	}

	public void setFechaAdquisicion(Date fechaAdquisicion) {
		this.fechaAdquisicion = fechaAdquisicion;
	}

	public String getTapa() {
		return tapa;
	}

	public void setTapa(String tapa) {
		this.tapa = tapa;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getResenha() {
		return resenha;
	}

	public void setResenha(String resenha) {
		this.resenha = resenha;
	}

	public byte[] getImg() {
		return img;
	}

	public void setImg(byte[] img) {
		this.img = img;
	}

	public int getIdColeccion() {
		return idColeccion;
	}

	public void setIdColeccion(int idColeccion) {
		this.idColeccion = idColeccion;
	}

	@Override
	public String toString() {
		return "Numero [id=" + id + ", titulo=" + titulo + ", fechaAdquisicion=" + fechaAdquisicion + ", tapa=" + tapa
				+ ", estado=" + estado + ", resenha=" + resenha + ", img=" + img + ", idColeccion=" + idColeccion + "]";
	}

}
