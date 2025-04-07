
# Práctica de persistencia con JPA

El objetivo de esta práctica es aprender a utilizar JPA para establecer un mapeo objeto-relacional
y dotar de persistencia a una aplicación escrita en Java.

Las tecnologías que se utilizarán serán:

 * Java - Lenguaje de programación
 * Maven - Configuración del proyecto
 * Eclipse - Entorno de desarrollo
 * Hibernate/JPA - Framework de persistencia
 * SQLite - Base de datos

La práctica se divide en varias tareas:

 0. [Inspeccionar proyecto](#proyecto)
 1. [Configurar Maven](#maven)
 2. [Configurar Hibernate/JPA](#hibernate)
 3. [Implementación de la persistencia](#implementacion)
 4. [Probar la persistencia](#pruebas)
  

## Ejercicio #0. Persistencia con JDBC <a name="proyecto"></a>

Para la realización de esta práctica se utilizará el proyecto la práctica
anterior de pruebas de software (https://github.com/jesusc-umu/pds/tree/main/practicas/pruebas-software).

Observa que el proyecto tiene la clase `ReposistorioToDoList` que se encarga de invocar a las operaciones
de una base de datos (clase `BaseDeDatos`). Estudia cómo se ha realizado la implementación del mapeo objeto-relacional
utilizando JDBC. 

En esta práctica deberás modificar la clase `RepositorioToDoList` para realizar la persistencia utilizando la API de JPA
en lugar de la clase `BaseDeDatos`. 

## 1. Maven. <a name="maven"></a>

Antes de comenzar a utilizar JPA es necesario configurar Maven con las dependencias correctas.
A continuación se indican estas dependencias:

* JPA API. Las interfaces y clases base que proporcionan JPA. Es necesario añadir una implementación concreta como Hibernate o EclipseLink.
* Hibernate. La implementación concreta de JPA que se utilizará.
* SQLite. El conector JDBC que utilizará Hibernate para crear una base de datos SQLite.
* `hibernate-community-dialects` es una extensión de Hibernate para dar soporte a SQLite.

```xml
	<properties>
		<jakarta.persistence.version>3.1.0</jakarta.persistence.version>
		<hibernate.version>6.2.0.Final</hibernate.version>
		<sqlite.version>3.39.2</sqlite.version>
	</properties>

  <dependencies>
		<!-- JPA API -->
		<dependency>
			<groupId>jakarta.persistence</groupId>
			<artifactId>jakarta.persistence-api</artifactId>
			<version>${jakarta.persistence.version}</version>
		</dependency>

		<!-- Hibernate Core -->
		<dependency>
			<groupId>org.hibernate.orm</groupId>
			<artifactId>hibernate-core</artifactId>
			<version>${hibernate.version}</version>
		</dependency>

		<!-- SQLite JDBC Driver -->
		<dependency>
		    <groupId>org.xerial</groupId>
		    <artifactId>sqlite-jdbc</artifactId>
		    <version>3.49.1.0</version>
			<scope>runtime</scope>
		</dependency>

		<!-- Para hacer funcionar SQLite con Hibernate -->
		<dependency>
            <groupId>org.hibernate.orm</groupId>
            <artifactId>hibernate-community-dialects</artifactId>
            <version>6.2.7.Final</version>
    </dependency>

  </dependencies>
```


## 2. Configuración de Hibernate. <a name="hibernate"></a>

Para configurar Hibernate, es necesario crear el fichero
`src/main/resources/META-INF/persistence.xml`
en el que se indica el driver JDBC que se utilizará, el tipo de base de datos (SQLite)
y la cadena de conexión a la base de datos.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<persistence xmlns="http://xmlns.jcp.org/xml/ns/persistence" version="2.1">
	<persistence-unit name="ejemplo">
		<properties>
			<property name="jakarta.persistence.jdbc.driver"
				value="org.sqlite.JDBC" />
      
      <!-- La base de datos se guardará en basedatos.db --> 
			<property name="jakarta.persistence.jdbc.url"
				value="jdbc:sqlite:basedatos.db" />
			<property name="dialect" value="org.hibernate.community.dialect.SQLiteDialect" />
			
      <!-- Crear el esquema automáticamente -->
      <property name="hibernate.hbm2ddl.auto" value="update" />
			
      <!-- Muestra información del SQL que va ejecutando -->
      <property name="hibernate.show_sql" value="true" />
		</properties>
	</persistence-unit>
</persistence>
```

## 3. Implementación de la persistencia <a name="implementacion"></a>

Para implementar la persistencia hay que realizar dos tareas.

1. Anotar las clases de dominio para hacerlas persistence. Presta especial atención a la herencia, eligiendo una estrategia.
2. Modificar la clase `RepositorioToDoList` para utilizar un `EntityManager` de JPA para guardar los objetos de dominio y realizar consultas.

Puedes probar diferentes estrategias de mapeo, en particular para la herencia. 
No olvides eliminar la base de datos si cambias el mapeo, para que el esquema se reconstruya desde cero.

Puedes comprobar que los datos se han insertado correctamente en la base de datos utilizando la línea
de comandos de SQLite. A modo de ejemplo:

```bash
$ sqlite3 basedatos.db

$ . schema
// Muestra el esquema de la base de datos

$ SELECT * FROM ToDoItem;
// Muestra los datos de los objetos ToDoItem
```

## 4. Probar la persistencia <a name="pruebas"></a>

En la práctica anterior debiste crear pruebas de integración de la base de datos.
En principio, estas pruebas de integración deben permitir probar la nueva implementación.

En este ejercicio debes ejecutar las pruebas de integración para comprobar que la nueva
implementación funciona. 

Es posible que en las pruebas de integración que creaste se construyera una nueva base de datos
con un nombre diferente cada vez. En ese caso, a la hora de obtener el `EntityManagerFactory` de JPA
es necesario sobreescribir la cadena de conexión a la base de datos que está "cableada" (harcoded) en 
`persistence.xml`, de manera que se pueda cambiar dinámicamente. El siguiente ejemplo muestra cómo hacerlo:

```java
String ficheroBd = "prueba-1.db";
Map<String, String> properties = new HashMap<>();
properties.put("hibernate.connection.url", "jdbc:sqlite:" + ficheroBd);

EntityManagerFactory emf = Persistence.createEntityManagerFactory("ejemplo", properties);
```       

