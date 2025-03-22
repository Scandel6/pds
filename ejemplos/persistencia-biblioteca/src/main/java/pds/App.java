package pds;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import pds.modelo.Admin;
import pds.modelo.Autor;
import pds.modelo.Libro;
import pds.modelo.Categoria;
import pds.modelo.Cliente;

public class App {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ejemplo");
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();

        // Crear datos de prueba
        Autor author = new Autor("George Orwell");
        em.persist(author);

        Libro book = new Libro("1984", author);
        em.persist(book);

        Categoria category = new Categoria("Distópico");
        em.persist(category);

        book.addCategory(category);
        em.persist(book);

        Admin admin = new Admin("root", "ALL");
        em.persist(admin);

        Cliente customer = new Cliente("Juan", "C/Espinardo");
        em.persist(customer);

        em.getTransaction().commit();
        em.close();
        emf.close();
    }
}
