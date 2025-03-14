package pds.todolist.db;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.List;

import pds.modelo.Prioridad;
import pds.modelo.ToDoItem;
import pds.modelo.ToDoList;
import pds.modelo.ToDoListAleatoria;
import pds.modelo.ToDoListFija;
import pds.modelo.ToDoListNormal;

public class BaseDatos implements AutoCloseable {

    private Connection connection;

	public BaseDatos(File bd) {
    	iniciarConexion(bd);
        crearTablas();
    }
    
    private void iniciarConexion(File bd) {
    	try {
			Connection conn = DriverManager.getConnection(getConnectionString(bd));
			this.connection = conn;
    	} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	private String getConnectionString(File file) {
		return "jdbc:sqlite:" + file.getAbsolutePath();
    }

    private void crearTablas() {
        String sqlToDoList = """
                CREATE TABLE IF NOT EXISTS ToDoList (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    titulo TEXT NOT NULL,
                    
                    tipo VARCHAR(128) NOT NULL,
                    
                    esContinua INTEGER,
                    maxItems INTEGER
                );
                """;
        String sqlToDoItem = """
                CREATE TABLE IF NOT EXISTS ToDoItem (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nombre TEXT NOT NULL,
                    prioridad TEXT NOT NULL,
                    vencimiento TEXT,
                    terminada TEXT,
                    list_id INTEGER,
                    FOREIGN KEY (list_id) REFERENCES ToDoList(id)
                );
                """;

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sqlToDoList);
            stmt.execute(sqlToDoItem);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveToDoList(ToDoList lista) {
        String sql = "INSERT INTO ToDoList (titulo, tipo, esContinua, maxItems) VALUES (?, ?, ?, ?)";
        String tipo = lista.getClass().getName();
        Boolean esContinua = (lista instanceof ToDoListAleatoria) ? ((ToDoListAleatoria) lista).isContinua() : null;
        Integer maxItems = (lista instanceof ToDoListFija) ? ((ToDoListFija) lista).getNumMaximoItems() : null;
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, lista.getTitulo());
            pstmt.setString(2, tipo);
            if (esContinua != null)
            	pstmt.setBoolean(3, esContinua);
            if (maxItems != null)
            	pstmt.setInt(4, maxItems);
            
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                int codigo = rs.getInt(1);
                lista.setId(codigo);
            } else {
            	throw new IllegalStateException();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


	public void removeToDoList(ToDoList lista) {
    	int listId = lista.getId(); 
    	
        String sql = "DELETE FROM ToDoList WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, listId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
	}
	
	public void removeToDoItem(ToDoList actual, ToDoItem item) {
        String sql = "DELETE FROM ToDoItem WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, item.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
	}
    
    public void saveToDoItem(ToDoList lista, ToDoItem item) {
    	int listId = lista.getId(); 
    	if (item.getId() == null) {
    		insertToDoItem(item, listId);
    	} else {
    		updateToDoItem(item);
    	}	
    }
    
    private void updateToDoItem(ToDoItem item) {
		String sql = "UPDATE ToDoItem SET nombre = ?, prioridad = ?, vencimiento = ?, terminada = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, item.getNombre());
            pstmt.setString(2, item.getPrioridad().name());
            pstmt.setString(3, item.getVencimiento() != null ? item.getVencimiento().toString() : null);
            pstmt.setString(4, item.isTerminada() ? item.getVencimiento().toString() : null);
            pstmt.setLong(5, item.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
	}

	private void insertToDoItem(ToDoItem item, int listId) {
		String sql = "INSERT INTO ToDoItem (nombre, prioridad, vencimiento, terminada, list_id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, item.getNombre());
            pstmt.setString(2, item.getPrioridad().name());
            pstmt.setString(3, item.getVencimiento() != null ? item.getVencimiento().toString() : null);
            pstmt.setString(4, item.isTerminada() ? item.getVencimiento().toString() : null);
            pstmt.setInt(5, listId);
            pstmt.executeUpdate();
            
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                int codigo = rs.getInt(1);
                item.setId(codigo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
	}

    public List<ToDoList> getToDoLists() {
    	// IMPORTANTE: Esta implementación podría haberse hecho con un "join" de las dos tablas
    	//             Se implementa así para que se vea claro de donde viene cada objeto
        List<ToDoList> listas = new ArrayList<>();
        String sql = "SELECT id, titulo, tipo, esContinua, maxItems FROM ToDoList";
        String sql2 = "SELECT id, nombre, prioridad, vencimiento, terminada, list_id FROM ToDoItem WHERE list_id = ?";
        try (Statement stmt = connection.createStatement();
        	 PreparedStatement pstmt = connection.prepareStatement(sql2);
        		
        	 ResultSet rs = stmt.executeQuery(sql)) {
           	 while (rs.next()) {
           		String tipo = rs.getString("tipo");
           		String titulo = rs.getString("titulo");
           		
           		ToDoList lista;
           		if (tipo.equals(ToDoListFija.class.getName())) {
               		int maxItems = rs.getInt("maxItems");
           			lista = new ToDoListFija(titulo, maxItems);
           		} else if (tipo.equals(ToDoListNormal.class.getName())) {
           			lista = new ToDoListNormal(titulo);
           		} else if (tipo.equals(ToDoListAleatoria.class.getName())) {
               		boolean esContinua = rs.getBoolean("esContinua");
           			lista = new ToDoListAleatoria(titulo, esContinua);           			
           		} else {
           			throw new IllegalStateException();
           		}
                
                lista.setId(rs.getInt("id"));
                
                // Obtener los ToDoItems individuales
                pstmt.setInt(1, lista.getId());
                ResultSet rs2 = pstmt.executeQuery();
                while (rs2.next()) {
                	int itemId = rs2.getInt("id");
                	String nombre = rs2.getString("nombre");
                	String prioridadStr = rs2.getString("prioridad");
                	Prioridad prioridad = Prioridad.valueOf(prioridadStr);
                	String vencimientoStr = rs2.getString("vencimiento");
                	String terminadaStr = rs2.getString("terminada");
                	
                	ToDoItem item = new ToDoItem(nombre);
                	item.setId(itemId);
                	item.setPrioridad(prioridad);
                	
                	if (vencimientoStr != null) {
                		TemporalAccessor ta = DateTimeFormatter.ISO_INSTANT.parse(vencimientoStr);
                		item.setVencimiento(LocalDate.from(ta));
                	}
                	
                    if (terminadaStr != null) {
                		TemporalAccessor ta = DateTimeFormatter.ISO_INSTANT.parse(terminadaStr);
                		item.setTerminada(LocalDate.from(ta));                    	
                    }

                    lista.addItem(item);
                }
                
                listas.add(lista);
           	 }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listas;
    }
    
	@Override
	public void close() throws Exception {
		this.connection.close();
	}

}
