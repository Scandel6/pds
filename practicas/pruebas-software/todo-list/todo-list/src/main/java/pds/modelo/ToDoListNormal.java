package pds.modelo;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Una lista donde las tareas se ordenan de manera estandar.
 */
public class ToDoListNormal extends ToDoList {

	public ToDoListNormal(String titulo) {
		super(titulo);
	}

	/**
	 * Obtiene las tareas ordenadas según su prioridad. 
	 * 
	 * Por defecto, el orden de prioridad es ALTA, NORMAL y BAJA. 
	 * En caso de tener la misma prioridad, las ordena por fecha de vencimiento.
	 * @return
	 */
	@Override
	public List<ToDoItem> getTareasEnOrden() {
		// Primero por prioridad (desc)
		// Luego por fecha (asc)
		return items.stream().sorted(Comparator
            .comparing(ToDoItem::getPrioridad, Comparator.reverseOrder()) 
            .thenComparing((o1, o2) -> {
            	if (o1.getVencimiento() == null && o2.getVencimiento() == null)
            		return 0;
            	else if (o1.getVencimiento() == null) {
            		return 1;
            	} else if (o2.getVencimiento() == null){
            		return -1;
            	}
            	return o1.getVencimiento().compareTo(o2.getVencimiento());
            })).collect(Collectors.toList());
	}
}
