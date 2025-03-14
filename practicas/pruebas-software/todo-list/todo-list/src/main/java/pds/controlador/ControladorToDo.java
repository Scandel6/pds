package pds.controlador;

import java.util.Collection;
import java.util.List;

import javax.annotation.CheckForNull;

import com.google.common.base.Preconditions;

import pds.modelo.Etiqueta;
import pds.modelo.ToDoItem;
import pds.modelo.ToDoList;
import pds.modelo.ToDoListAleatoria;
import pds.modelo.ToDoListFija;
import pds.modelo.ToDoListNormal;
import pds.servicios.ServicioAutoEtiquetado;
import pds.servicios.ServicioAutoEtiquetadoSimple;
import pds.todolist.repositorios.RepositorioToDoList;
import pds.utils.UtilidadesFiltrado;

/**
 * 
 */
public class ControladorToDo {

	/** La lista que está actualmente configurando el usuario */
	@CheckForNull
	private ToDoList actual;

	//private ServicioAutoEtiquetado servicio = new ServicioAutoEtiquetadoOpenAI();
	private ServicioAutoEtiquetado servicio = new ServicioAutoEtiquetadoSimple();
	
	private RepositorioToDoList repositorioListas = new RepositorioToDoList();

	@CheckForNull
	public ToDoItem nuevoToDoItem(String nombreTarea) {
		Preconditions.checkNotNull(actual, "No se pueden invocar a crear items sin haber seleccionado un ToDoList");
		if (actual == null)
			throw new IllegalStateException();

		ToDoItem tarea = new ToDoItem(nombreTarea);
		if (actual.addItem(tarea)) {
			repositorioListas.add(actual, tarea);
			
			Collection<Etiqueta> etiquetas = servicio.etiquetar(tarea);
			etiquetas.forEach(tarea::addEtiqueta);
			
			return tarea;
		} else {
			return null;
		}
	}


	public void renombrarToDoItem(ToDoItem item, String nombre) {
		item.setNombre(nombre);
		repositorioListas.update(actual, item);
	}


	public void eliminarToDoItem(ToDoItem item) {
		actual.removeItem(item);
		repositorioListas.remove(actual, item);
	}


	public ToDoList nuevaListaFija(String nombre, int maximoItems) {
		ToDoList lista = new ToDoListFija(nombre, maximoItems);
		repositorioListas.add(lista);
		return lista;
	}
	
	public ToDoList nuevaListaNormal(String nombre) {
		ToDoList lista = new ToDoListNormal(nombre);
		repositorioListas.add(lista);
		return lista;
	}
	
	public ToDoList nuevaListaAleatoria(String nombre, boolean esContinua) {
		ToDoList lista = new ToDoListAleatoria(nombre, esContinua);
		repositorioListas.add(lista);
		return lista;
	}

	public List<ToDoList> getToDoLists() {
		return repositorioListas.getToDoLists();
	}

	public List<ToDoList> getToDoLists(String patron) {
		return getToDoLists().stream().filter(l -> {
			return UtilidadesFiltrado.esCompatibleConPatron(l.getTitulo(), patron);	
		}).toList();
	}
	
	public void eliminarLista(ToDoList lista) {
		if (actual == lista)
			actual = null;
		this.repositorioListas.remove(lista);
	}

	public void setListaActual(ToDoList lista) {
		// TODO: Asegurar que la lista está dentro del repositorio
		this.actual = lista;
	}

	public ToDoList getListaActual() {
		return actual;
	}

	public List<? extends ToDoItem> getTareasEnOrden() {
		return actual.getTareasEnOrden();
	}
}
