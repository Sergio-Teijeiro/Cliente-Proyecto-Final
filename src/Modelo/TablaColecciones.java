package Modelo;

import java.util.ArrayList;
import java.util.Locale;

import javax.swing.table.AbstractTableModel;

/**
 * Modelo de tabla de colecciones. Muestra su nombre.
 * @author sergio
 *
 */
public class TablaColecciones extends AbstractTableModel{
	private ArrayList<Coleccion> listaColecciones;
	private String[] columnas = new String[1];
	
	public TablaColecciones(ArrayList<Coleccion> colecciones) {
		super();
		this.listaColecciones = colecciones;
		
		if (!Locale.getDefault().getDisplayName().contains("galego")) {
			columnas[0] = "Nombre";
		} else {
			columnas[0] = "Nome";
		}
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
