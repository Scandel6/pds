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

	//private BaseDatos baseDatos;

	private EntityManager emf;
	protected EntityManager em;

	public RepositorioToDoList() {
		emf = Persistence.createEntityManagerFactory("ejemplo");
	}
	
	public void add(ToDoList lista) {
		this.em = emf.createEntityManager();

		EntityTransaction tx = em.getTransaction();
		tx.begin()

		try{

			em.persist(lista); // Almacenar un objeto por primera vez en la base de datos
			tx.commit()
		} catch(Exception e){
			System.err.print(e.getMessage());
			tx.rollback();
			e.printStackTrace();
		} finally{
			if (em != null && em.isOpen()){
				em.close();
			}
		}
	}
	
	public void add(ToDoList lista, ToDoItem tarea) {
		this.em = emf.createEntityManager();

		EntityTransaction tx = em.getTransaction();
		tx.begin()

		try{
			if (tarea.getId() == null){
				em.persist(tarea);
			} else {
				em.merge(tarea); // Sobrescribir el estado del objeto ya guardado en la base de datos
			}
			em.persist(lista);
			tx.commit();
		} catch(Exception e){
			System.err.print(e.getMessage());
			tx.rollback();
			e.printStackTrace();
		} finally{
			if (em != null && em.isOpen()){
				em.close();
			}
		}


	}

	public List<ToDoList> getToDoLists() {
		this.em = emf.createEntityManager();
		List<ToDoList> resultado = new ArrayList<>();
		EntityTransaction tx = em.getTransaction();
		tx.begin()

		try{
			TypedQuery<ToDoList> query = em.createQuery("SELECT l FROM ToDoList l", ToDoList.class);
			resultado = query.getResultList();

			// Si estuviese en modo LAZY para forzar la carga de items
			// for (ToDoList lista : resultado) {
            // lista.getItems().size(); // Esto fuerza a cargar la colección
        	// }

			tx.commit();
		} catch(Exception e){
			System.err.print(e.getMessage());
			tx.rollback();
			e.printStackTrace();
		} finally{
			if (em != null && em.isOpen()){
				em.close();
			}
		}
		return resultado;
	}

	public void remove(ToDoList lista) {
		this.em = emf.createEntityManager();

		EntityTransaction tx = em.getTransaction();
		tx.begin()

		try{
			// SOlamente podemos eliminar entidades manejadas en la transacción actual
			// Si usamos el objeto pasado como parámetro, JPA no sabe si forma parte de la base de datos
			ToDoList l = em.find(ToDoList.class, lista.getId());
			em.remove(l); // COmo el OneToMany pusimos CascadeType.REMOVE se elminan sus items de forma automatica
			tx.commit();
		} catch(Exception e){
			System.err.print(e.getMessage());
			tx.rollback();
			e.printStackTrace();
		} finally{
			if (em != null && em.isOpen()){
				em.close();
			}
		}
	}

	public void remove(ToDoList actual, ToDoItem item) {
		this.em = emf.createEntityManager();

		EntityTransaction tx = em.getTransaction();
		tx.begin()

		try{
			ToDoItem i = em.find(ToDoItem.class, item.getId());
			em.remove(i):

			em.merge(actual);
			tx.commit();
			
		} catch(Exception e){
			System.err.print(e.getMessage());
			tx.rollback();
			e.printStackTrace();
		} finally{
			if (em != null && em.isOpen()){
				em.close();
			}
		}
	}

	public void update(ToDoList lista, ToDoItem item) {
		this.em = emf.createEntityManager();

		EntityTransaction tx = em.getTransaction();
		tx.begin()

		try{
			if (item.getId() == null){
				em.persist(item);// Al almacenar un item más se modifica la lista (merge de abajo del if)
			}else{
				em.merge(item);
			}
			em.merge(lista);
			tx.commit();
		} catch(Exception e){
			System.err.print(e.getMessage());
			tx.rollback();
			e.printStackTrace();
		} finally{
			if (em != null && em.isOpen()){
				em.close();
			}
		}
	}



}
