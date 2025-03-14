package pds.modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.CheckForNull;

public abstract class ToDoList {

	// El identificador para la base de datos
	@CheckForNull
	private Integer id = null;
	
	private String titulo;
	protected final List<ToDoItem> items;
	
	public ToDoList(String titulo) {
		this.titulo = titulo;
		this.items = new ArrayList<ToDoItem>();
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getId() {
		return id;
	}
	
	public String getTitulo() {
		return titulo;
	}
	
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	
	public boolean addItem(ToDoItem item) {
		return this.items.add(item);
	}
	
	public List<? extends ToDoItem> getItems() {
		return items;
	}
	
	/**
	 * Dada una fecha, posiblemente la fecha actual, devuelve el número
	 * de tareas que están pendientes. No incluye aquellas tareas
	 * cuya fecha de vencimiento (si tienen) es menor que la fecha dada.
	 * De esa manera, las tareas que ya se han caducado no se consideran.
	 * 
	 * @param desdeFecha La fecha a partir de la cual se consideran tareas. Esto es, la fecha de caducidad.
	 * @return La lista de tareas pendientes
	 */
	public List<ToDoItem> getPendientes(LocalDate desdeFecha) {
		List<ToDoItem> pendientes = new ArrayList<ToDoItem>();
		for (ToDoItem item: items) {
			if (item.isTerminada()) {
				continue;
			}
			
			if (item.getVencimiento() == null || item.getVencimiento().isAfter(desdeFecha)) {
				pendientes.add(item);
			}
		}
		return pendientes;
	}
	
	/**
	 * Obtiene las tareas ordenadas según un orden definido por las subclases.
	 */
	public abstract List<ToDoItem> getTareasEnOrden();
	
	public void removeItem(ToDoItem item) {
		items.remove(item);
	}

}
