package pds.todolist.repositorios;

import java.io.File;
import java.util.List;

import pds.modelo.ToDoItem;
import pds.modelo.ToDoList;
import pds.todolist.db.BaseDatos;

/**
 * Un repositorio de ToDo lists.
 */
public class RepositorioToDoList {

	private BaseDatos baseDatos;

	public RepositorioToDoList() {
		this.baseDatos = new BaseDatos(new File("todolist.db"));
	}
	
	public void add(ToDoList lista) {
		this.baseDatos.saveToDoList(lista);
	}
	
	public void add(ToDoList lista, ToDoItem tarea) {
		this.baseDatos.saveToDoItem(lista, tarea);
	}

	public List<ToDoList> getToDoLists() {
		return this.baseDatos.getToDoLists();
	}

	public void remove(ToDoList lista) {
		this.baseDatos.removeToDoList(lista);
	}

	public void remove(ToDoList actual, ToDoItem item) {
		this.baseDatos.removeToDoItem(actual, item);
	}

	public void update(ToDoList lista, ToDoItem item) {
		this.baseDatos.saveToDoItem(lista, item);
	}



}
