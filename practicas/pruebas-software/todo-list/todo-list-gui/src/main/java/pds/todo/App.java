package pds.todo;

import java.awt.EventQueue;

import pds.controlador.ControladorToDo;
import pds.todo.gui.VentanaPrincipal;

public class App {

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VentanaPrincipal window = crearVentanaPrincipal();
					window.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * En este método se realiza la configuración de la aplicación
	 * y sus servicios asociados.
	 */
	protected static VentanaPrincipal crearVentanaPrincipal() {
		ControladorToDo controlador = new ControladorToDo();
		VentanaPrincipal window = new VentanaPrincipal(controlador);
		return window;
	}
}
