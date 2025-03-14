package pds.todo.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;

import pds.controlador.ControladorToDo;
import pds.modelo.ToDoList;

public class VentanaPrincipal extends JFrame {

	private static final long serialVersionUID = 2890719421715487086L;
	
	private JTextField txtBusqueda;
    private JPanel table;
	private JTextField txtNombreLista;
	private JPanel seleccionActual;
	
	private ControladorToDo controlador;
	private ToDoListPanel toDoPanel;
	private final ButtonGroup buttonGroup = new ButtonGroup();

	private JRadioButton radioFija;

	private JRadioButton radioAleatoria;

	private JRadioButton radioNormal;

	public VentanaPrincipal(ControladorToDo controlador) {
		this.controlador = controlador;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		this.setBounds(100, 100, 721, 429);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setResizeWeight(0.3);
		this.getContentPane().add(splitPane, BorderLayout.CENTER);
		
		JPanel panelIzquierdo = new JPanel();
		panelIzquierdo.setMinimumSize(new Dimension(200, 100));
		splitPane.setLeftComponent(panelIzquierdo);
		panelIzquierdo.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_2 = new JPanel();
		panelIzquierdo.add(panel_2, BorderLayout.NORTH);
		panel_2.setLayout(new BorderLayout(0, 0));
		
		txtBusqueda = new JTextField();
		panel_2.add(txtBusqueda);
		txtBusqueda.setColumns(10);
		
		JButton btnBuscar = new JButton("Buscar");
		btnBuscar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buscar(txtBusqueda.getText());
			}
		});
		panel_2.add(btnBuscar, BorderLayout.EAST);
		
        table = new JPanel();
        table.setLayout(new BoxLayout(table, BoxLayout.Y_AXIS));
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		panelIzquierdo.add(scrollPane, BorderLayout.CENTER);
		
		JPanel panel = new JPanel();
		panelIzquierdo.add(panel, BorderLayout.SOUTH);
		panel.setLayout(new BorderLayout(0, 0));
		
		txtNombreLista = new JTextField();
		panel.add(txtNombreLista, BorderLayout.CENTER);
		txtNombreLista.setColumns(10);
		
		JButton btnNewButton = new JButton("Nueva lista");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				crearToDoList();
			}
		});
		panel.add(btnNewButton, BorderLayout.EAST);
		
		JPanel panel_1 = new JPanel();
		panel.add(panel_1, BorderLayout.SOUTH);
		
		radioNormal = new JRadioButton("Normal");
		radioNormal.setSelected(true);
		buttonGroup.add(radioNormal);
		panel_1.add(radioNormal);

		radioFija = new JRadioButton("Fija");
		buttonGroup.add(radioFija);
		panel_1.add(radioFija);
		
		radioAleatoria = new JRadioButton("Aleatoria");
		buttonGroup.add(radioAleatoria);
		panel_1.add(radioAleatoria);
				
		JPanel panelDerecho = new JPanel();
		splitPane.setRightComponent(panelDerecho);
		panelDerecho.setLayout(new BorderLayout(0, 0));
		
		toDoPanel = new ToDoListPanel(controlador);
		panelDerecho.add(toDoPanel);
		inicializarTodoList();
	}

	private void inicializarTodoList() {
		for (ToDoList lista : controlador.getToDoLists()) {
			this.table.add(createToDoList(lista));
		}
	}

	private JPanel createToDoList(ToDoList lista) {
		JLabel label;
        JButton removeButton;

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        panel.setMaximumSize(new Dimension(500, 50));
        
        label = new JLabel();
        label.setText(lista.getTitulo());
        removeButton = new JButton("Eliminar");
        
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(removeButton);
        
        panel.add(label, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.EAST);

        panel.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		controlador.setListaActual(lista);
				toDoPanel.refrescar();
        	}
        });
        
        panel.addMouseListener(new MouseAdapter() {

			@Override
        	public void mouseClicked(MouseEvent e) {
        		if (seleccionActual != null) {
        			seleccionActual.setBackground(null);
        			seleccionActual.repaint();
        		}
        		seleccionActual = panel;
        		seleccionActual.setBackground(new Color(220, 220, 220));
				controlador.setListaActual(lista);
				toDoPanel.refrescar();
        	}
        });

        removeButton.addActionListener(e -> {
        	ToDoList actual = controlador.getListaActual();
        	if (actual == null)
        		return;
        	
        	int input = JOptionPane.showConfirmDialog(null, "¿Quiere eliminar la lista " + lista.getTitulo() + "?");
        	if (input == 1) {
        		controlador.eliminarLista(actual);
        		table.remove(removeButton.getParent());
        	}
        });
        
        return panel;
	}
        
	private void buscar(String texto) {
		List<ToDoList> listas;
		if (texto == null || texto.isEmpty()) {
			 listas = controlador.getToDoLists();
		} else {
			 listas = controlador.getToDoLists(texto); 
		}
			
		
		this.table.removeAll();
		for (ToDoList lista : listas) {
			this.table.add(createToDoList(lista));
		}
		this.table.revalidate();
		this.table.repaint();
	}
	
	private void crearToDoList() {
		String nombre = txtNombreLista.getText();
		if (nombre.isBlank())
			return;
		
		ToDoList lista = null;
		if (radioFija.isSelected()) {
			lista = nuevaListaFija(nombre);
		} else if (radioAleatoria.isSelected()) {
			lista = nuevaListaAleatoria(nombre);
		} else if (radioNormal.isSelected()) {
			lista = controlador.nuevaListaNormal(nombre);
		}
		
		if (lista == null)
			return;
		
		JPanel panelItem = createToDoList(lista);
		this.table.add(panelItem);
        this.table.revalidate();
        this.table.repaint();
        
		controlador.setListaActual(lista);
		
		txtNombreLista.setText("");
		radioNormal.setSelected(true);
	}

	private ToDoList nuevaListaFija(String nombre) {
		String input = JOptionPane.showInputDialog(null, "Introduce el número máximo de items", "Número máximo de items", JOptionPane.QUESTION_MESSAGE);
		
		if (input == null) { 
		   return null;
		}

		try {
		    int numero = Integer.parseInt(input);
			return controlador.nuevaListaFija(nombre, numero);
		} catch (NumberFormatException e) {
		    JOptionPane.showMessageDialog(null, "Entrada inválida. Debe ser un número entero.", "Error", JOptionPane.ERROR_MESSAGE);
		    return null;
		}
	}
	
	private ToDoList nuevaListaAleatoria(String nombre) {
	   int option = JOptionPane.showConfirmDialog(
		            null,
		            "¿La lista debe aleatorizarse continuamente?",
		            "Confirmación",
		            JOptionPane.YES_NO_OPTION,
		            JOptionPane.QUESTION_MESSAGE
		        );

	   	boolean continua = (option == JOptionPane.YES_OPTION);
	
        try {
			return controlador.nuevaListaAleatoria(nombre, continua);
		} catch (NumberFormatException e) {
		    JOptionPane.showMessageDialog(null, "Entrada inválida. Debe ser un número entero.", "Error", JOptionPane.ERROR_MESSAGE);
		    return null;
		}
	}
	

}
