package pds.modelo;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

// =======================
//    ENTITY: Author (1:N Book)
// =======================
@Entity
@Table(name = "authors")
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL)
    private List<Libro> books;
    
    public Autor(String name) {
    	this.name = name;
    }
    
    public String getName() {
		return name;
	}
    
    public void setName(String name) {
		this.name = name;
	}
}