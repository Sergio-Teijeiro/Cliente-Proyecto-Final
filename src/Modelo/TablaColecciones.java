package Modelo;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

public class TablaColecciones extends AbstractTableModel{
	private ArrayList<Coleccion> listaColecciones;
	private String[] columnas = {"Nombre"};
	
	public TablaColecciones(ArrayList<Coleccion> colecciones) {
		super();
		this.listaColecciones = colecciones;
	}
	
	@Override
	public int getRowCount() {
		return listaColecciones.size();
	}

	@Override
	public int getColumnCount() {
		return columnas.length;
	}

	@Override
	public Object getValueAt(int row, int col) {
		if (row != -1 && row < listaColecciones.size()) {
			Coleccion c = listaColecciones.get(row);
			
			switch (col) {
			case 0:
				return c.getNombre();
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
