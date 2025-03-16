package pds.modelo;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.CheckForNull;

/**
 * Un item de una lista de tareas (ToDo List).
 */
public class ToDoItem {

	private String nombre;
	private Set<Etiqueta> etiquetas;
	private Prioridad prioridad;
	
	// Un item puede tener una fecha de vencimiento
	@CheckForNull
	private LocalDate vencimiento;
	
	// Si la fecha en que ha terminado está dada, entonces la tarea ha terminado.
	@CheckForNull
	private LocalDate terminada;
	
	@CheckForNull
	private Integer id;
	
	public ToDoItem(String nombre, Etiqueta... etiquetas) {
		this(nombre, Prioridad.NORMAL, etiquetas);
	}
	
	public ToDoItem(String nombre, Prioridad prioridad, Etiqueta... etiquetas) {
		this.nombre = nombre;
		this.etiquetas = new TreeSet<Etiqueta>();
		this.prioridad = prioridad;
		Collections.addAll(this.etiquetas, etiquetas);
	}
	
	public void setId(int itemId) {
		this.id = itemId;
	}
	
	public Integer getId() {
		return id;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public void setTerminada(LocalDate terminada) {
		this.terminada = terminada;
	}
	
	public boolean isTerminada() {
		return terminada != null;
	}
	
	public Prioridad getPrioridad() {
		return prioridad;
	}
	
	public void setPrioridad(Prioridad prioridad) {
		this.prioridad = prioridad;
	}
	
	@CheckForNull
	public LocalDate getVencimiento() {
		return vencimiento;
	}

	public void setVencimiento(LocalDate vencimiento) {
		this.vencimiento = vencimiento;
	}
	
	public ToDoItem withVencimiento(LocalDate vencimiento) {
		this.setVencimiento(vencimiento);
		return this;
	}
	
	/**
	 * Comprueba si una tarea ha vencido en cierta fecha.
	 * En caso de que una tarea no tenga fecha de vencimiento,
	 * se considerará que la tarea nunca vence.
	 * 
	 * @return true si la tarea ha vencido en esa fecha.
	 */
	public boolean esVencida(LocalDate date) {
		return this.vencimiento.isBefore(date);
	}
	
	public Set<? extends Etiqueta> getEtiquetas() {
		return etiquetas;
	}
	
	@Override
	public String toString() {
		return "ToDoItem [nombre=" + nombre + ", etiquetas=" + etiquetas + ", prioridad=" + prioridad + ", vencimiento="
				+ vencimiento + ", terminada=" + terminada + "]";
	}

	public void addEtiqueta(Etiqueta etiqueta) {
		this.etiquetas.add(etiqueta);
	}
	
}
