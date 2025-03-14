package pds.todo.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import pds.controlador.ControladorToDo;
import pds.modelo.Etiqueta;
import pds.modelo.ToDoItem;

public class ToDoListPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    
    private JPanel listPanel;
    private JScrollPane scrollPane;
    private JTextField inputField;
    private JButton addButton;

	private ControladorToDo controlador;
    
    public ToDoListPanel(ControladorToDo controlador) {
    	this.controlador = controlador;
    	
        setLayout(new BorderLayout());
        setBackground(new Color(30, 30, 30));
            
        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        
        scrollPane = new JScrollPane(listPanel);
        
        inputField = new JTextField();
        inputField.setPreferredSize(new Dimension(200, 20));
        
        addButton = new JButton("Añadir To-Do");
        addButton.setFocusPainted(false);
        
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());
        inputPanel.add(inputField);
        inputPanel.add(addButton);
        
        add(scrollPane, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);
        
        addButton.addActionListener(e -> addToDoItem());
    }
    
    private void addToDoItem() {
        String tarea = inputField.getText().trim();
        if (tarea.isBlank()) return;
        
        ToDoItem item = controlador.nuevoToDoItem(tarea);
        if (item != null) {
	        JPanel itemPanel = createToDoPanel(item);
	        listPanel.add(itemPanel);
	        listPanel.revalidate();
	        listPanel.repaint();
	        
	        inputField.setText("");
        }
    }
    
    private JPanel createToDoPanel(ToDoItem item) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        panel.setBackground(new Color(60, 60, 60));
        panel.setMaximumSize(new Dimension(500, 100));
        
        JLabel label = new JLabel(item.getNombre());
        label.setForeground(Color.WHITE);
        
        JButton editButton = new JButton("Editar");
        JButton removeButton = new JButton("Eliminar");
        
        editButton.setBackground(new Color(100, 200, 100));
        removeButton.setBackground(new Color(200, 50, 50));
        
        editButton.setForeground(Color.WHITE);
        removeButton.setForeground(Color.WHITE);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(60, 60, 60));
        buttonPanel.add(editButton);
        buttonPanel.add(removeButton);
        
        panel.add(label, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.EAST);
        
        JPanel panelEtiquetas = new JPanel();
        panelEtiquetas.setLayout(new FlowLayout());
        
        Color colors[] = new Color[] {
        	Color.BLUE,
        	Color.CYAN,
        	Color.RED
        };
        
        int i = 0;
        for (Etiqueta etiqueta : item.getEtiquetas()) {
			JLabel l = new JLabel(etiqueta.valor(), SwingConstants.LEFT);
			l.setBackground(colors[i]);
			panelEtiquetas.add(l);
        	i++;
        }

        panel.add(panelEtiquetas, BorderLayout.SOUTH);
        
        editButton.addActionListener(e -> {
            String nuevoNombre = JOptionPane.showInputDialog("Nombre:", item.getNombre());
            if (nuevoNombre != null && !nuevoNombre.trim().isEmpty()) {
            	controlador.renombrarToDoItem(item, nuevoNombre);
                label.setText(nuevoNombre);
            }
        });
        
        removeButton.addActionListener(e -> {
        	controlador.eliminarToDoItem(item);
            listPanel.remove(panel);
            listPanel.revalidate();
            listPanel.repaint();
        });
        
        return panel;
    }

	public void refrescar() {
		listPanel.removeAll();
		for (ToDoItem item : controlador.getTareasEnOrden()) {
			JPanel p = createToDoPanel(item);
			listPanel.add(p);
		}
		listPanel.revalidate();
		listPanel.repaint();
	}
}
