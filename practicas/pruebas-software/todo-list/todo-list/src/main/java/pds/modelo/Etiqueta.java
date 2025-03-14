package pds.modelo;

public record Etiqueta(String valor) implements Comparable<Etiqueta> {

	@Override
	public int compareTo(Etiqueta o) {
		return o.valor.compareTo(o.valor);
	}
	
}
