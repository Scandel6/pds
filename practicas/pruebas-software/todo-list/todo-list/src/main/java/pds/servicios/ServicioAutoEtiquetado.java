package pds.servicios;

import java.util.Collection;

import pds.modelo.Etiqueta;
import pds.modelo.ToDoItem;

public interface ServicioAutoEtiquetado {

	Collection<Etiqueta> etiquetar(ToDoItem item, Etiqueta... etiquetasRelevantes);

}
