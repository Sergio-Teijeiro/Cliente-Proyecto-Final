package Modelo;

import java.net.Socket;
import java.text.DateFormat;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import Controlador.HiloCliente;

public class TablaComics extends AbstractTableModel {
	private ArrayList<Numero> listaComics;
	private String[] columnas = {"Título","Colección"};
	private Socket socketCliente;
	
	public TablaComics(ArrayList<Numero> listaComics,String[] columnas) {
		super();
		this.listaComics = listaComics;
		this.columnas = columnas;
	}
	
	public TablaComics(ArrayList<Numero> listaComics, Socket skCliente) {
		super();
		this.listaComics = listaComics;
		this.socketCliente = skCliente;
	}
	
	@Override
	public int getRowCount() {
		return listaComics.size();
	}

	@Override
	public int getColumnCount() {
		return columnas.length;
	}

	@Override
	public Object getValueAt(int row, int col) {
		if (row != -1 && row < listaComics.size()) {
			Numero n = listaComics.get(row);
			Coleccion coleccion = null;
			
			HiloCliente hilo = new HiloCliente(socketCliente,"colByComic",n,coleccion);
			hilo.start();
			
			
			switch (col) {
			case 0:
				return n.getTitulo();
			case 1: return coleccion.getNombre();
			default:
				return null;
			}
		} else {
			return null;
		}
	}
	
	@Override
	public boolean isCellEditable (int rowIndex, int columnIndex) {
		return false;
	}
	
	public String getColumnName (int col) {
		return columnas[col];
	}
}
