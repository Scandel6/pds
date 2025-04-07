package pds.modelo;

import java.util.ArrayList;
import java.util.List;

@Entity
public class ToDoListFija extends ToDoList {

	private int numMaximoItems;

	public ToDoListFija(String titulo, int numMaximoItems) {
		super(titulo);
		this.numMaximoItems = numMaximoItems;
	}

	public ToDoListFija(){
		super();
	}

	public int getNumMaximoItems() {
		return numMaximoItems;
	}
	
	@Override
	public boolean addItem(ToDoItem item) {
		if (items.size() > numMaximoItems)
			return false;
		return super.addItem(item);
	}
	
	@Override
	public List<ToDoItem> getTareasEnOrden() {
		return new ArrayList<ToDoItem>(items);
	}
}
