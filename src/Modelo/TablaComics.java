package Modelo;

import java.text.DateFormat;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

public class TablaComics extends AbstractTableModel {
	private ArrayList<Numero> listaComics;
	private String[] columnas = {"Título","Colección"};
	
	public TablaComics(ArrayList<Numero> listaComics,String[] columnas) {
		super();
		this.listaComics = listaComics;
		this.columnas = columnas;
	}
	
	public TablaComics(ArrayList<Numero> listaComics) {
		super();
		this.listaComics = listaComics;
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
