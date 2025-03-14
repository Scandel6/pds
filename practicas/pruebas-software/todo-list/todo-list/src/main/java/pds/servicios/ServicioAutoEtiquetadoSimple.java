package pds.servicios;

import java.util.Collection;
import java.util.List;

import pds.modelo.Etiqueta;
import pds.modelo.ToDoItem;

public class ServicioAutoEtiquetadoSimple implements ServicioAutoEtiquetado {

	@Override
	public Collection<Etiqueta> etiquetar(ToDoItem item, Etiqueta... relevanes) {
		return List.of(
			new Etiqueta("e1"),
			new Etiqueta("e2")
		);
	}
	
}
