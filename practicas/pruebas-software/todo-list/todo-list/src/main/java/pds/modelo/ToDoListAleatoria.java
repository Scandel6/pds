package pds.modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Una lista donde las tareas se ordenan de manera aleatoria.
 */
public class ToDoListAleatoria extends ToDoList {

	private boolean esContinua;

	public ToDoListAleatoria(String titulo, boolean esContinua) {
		super(titulo);
		this.esContinua = esContinua;
	}
	
	public boolean isContinua() {
		return esContinua;
	}
	
	@Override
	public boolean addItem(ToDoItem item) {
		if (! esContinua) {
			int posicion = new Random().nextInt(items.size() + 1);
			items.add(posicion, item);
			return true;
		} else {
			return super.addItem(item);
		}
	}
	
	@Override
	public List<ToDoItem> getTareasEnOrden() {
		if (esContinua) {
			List<ToDoItem> randomized = new ArrayList<ToDoItem>(items);
			Collections.shuffle(randomized);
			return randomized;
		} else {
			return new ArrayList<ToDoItem>(items);
		}
	}

}
