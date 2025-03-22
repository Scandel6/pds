package pds.modelo;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "libros")
public class Libro {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;

    @ManyToOne
    @JoinColumn(name = "autor_id")
    private Autor autor;

    @ManyToMany
    @JoinTable(
        name = "libro_categoria",
        joinColumns = @JoinColumn(name = "libro_id"),
        inverseJoinColumns = @JoinColumn(name = "categoria_id")
    )
    private Set<Categoria> categorias;

    public Libro(String titulo, Autor autor) {
    	this.titulo = titulo;
    	this.autor = autor;
    	this.categorias = new HashSet<>();
    }
    
    public String getTitulo() {
		return titulo;
	}
    
    public Autor getAutor() {
		return autor;
	}
    
    public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
    
    public void setAutor(Autor autor) {
		this.autor = autor;
	}
    
    public void addCategory(Categoria categoria) {
    	this.categorias.add(categoria);
    }
    
    public Set<? extends Categoria> getCategorias() {
		return categorias;
	}
}