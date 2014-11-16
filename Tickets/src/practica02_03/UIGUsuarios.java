package practica02_03;

import javax.swing.JPanel;

import java.awt.BorderLayout;

import javax.swing.JTable;

import java.awt.Panel;

import javax.swing.JButton;

import java.awt.GridLayout;

import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import javax.swing.UIManager;
import javax.swing.JComboBox;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class UIGUsuarios extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7508988054079361673L;
	private JTable table;
	private JTextField text_buscar;
	private String[] depart;
	private ButtonGroup group = new ButtonGroup();
	private DefaultTableModel modelo;
	private JComboBox<String> comb_depart;
	
	
	
	/**
	 * Create the panel.
	 * @throws SQLException 
	 */
	public UIGUsuarios(Connection conexion) throws SQLException {
		setLayout(new BorderLayout(0, 0));
		
		mostrarTabla(conexion);
		
		Panel panel = new Panel();
		panel.setBackground(Color.WHITE);
		add(panel, BorderLayout.EAST);
		panel.setLayout(new GridLayout(5, 1, 0, 0));
		
		JButton btnNewButton = new JButton("New button");
		panel.add(btnNewButton);
		
		JPanel panel_filtro = new JPanel();
		panel_filtro.setBackground(Color.WHITE);
		panel_filtro.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Filtro", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		add(panel_filtro, BorderLayout.NORTH);
		GridBagLayout gbl_panel_filtro = new GridBagLayout();
		gbl_panel_filtro.columnWidths = new int[] {90, 258, 157, 0, 0};
		gbl_panel_filtro.rowHeights = new int[] {50};
		gbl_panel_filtro.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panel_filtro.rowWeights = new double[]{0.0};
		panel_filtro.setLayout(gbl_panel_filtro);
		
		JPanel panel_busqueda = new JPanel();
		panel_busqueda.setBackground(Color.WHITE);
		panel_busqueda.setBorder(new TitledBorder(null, "Buscar", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		GridBagConstraints gbc_panel_busqueda = new GridBagConstraints();
		gbc_panel_busqueda.fill = GridBagConstraints.HORIZONTAL;
		gbc_panel_busqueda.gridwidth = 2;
		gbc_panel_busqueda.insets = new Insets(0, 0, 0, 5);
		gbc_panel_busqueda.gridx = 0;
		gbc_panel_busqueda.gridy = 0;
		panel_filtro.add(panel_busqueda, gbc_panel_busqueda);
		panel_busqueda.setLayout(new GridLayout(0, 1, 0, 0));
		
		text_buscar = new JTextField();
		panel_busqueda.add(text_buscar);
		text_buscar.setColumns(30);
		
		JPanel panel_estado = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_estado.getLayout();
		flowLayout.setHgap(12);
		panel_estado.setBorder(new TitledBorder(null, "Estado", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_estado.setBackground(Color.WHITE);
		GridBagConstraints gbc_panel_estado = new GridBagConstraints();
		gbc_panel_estado.fill = GridBagConstraints.VERTICAL;
		gbc_panel_estado.anchor = GridBagConstraints.WEST;
		gbc_panel_estado.insets = new Insets(0, 0, 0, 5);
		gbc_panel_estado.gridx = 2;
		gbc_panel_estado.gridy = 0;
		panel_filtro.add(panel_estado, gbc_panel_estado);
		
		
		JRadioButton rdbtn_activas = new JRadioButton("Activas");
		rdbtn_activas.setBackground(Color.WHITE);
		rdbtn_activas.setSelected(true);
		rdbtn_activas.setActionCommand("Obert");
		panel_estado.add(rdbtn_activas);
		
		JRadioButton rdbtn_cerradas = new JRadioButton("Cerradas");
		rdbtn_cerradas.setBackground(Color.WHITE);
		rdbtn_cerradas.setSelected(false);
		rdbtn_cerradas.setActionCommand("Tancat");
		panel_estado.add(rdbtn_cerradas);
		
		JRadioButton rdbtn_todas = new JRadioButton("Todas");
		rdbtn_todas.setBackground(Color.WHITE);
		rdbtn_todas.setSelected(false);
		rdbtn_todas.setActionCommand("*");
		panel_estado.add(rdbtn_todas);
		
		group.add(rdbtn_activas);
		group.add(rdbtn_cerradas);
		group.add(rdbtn_todas);
		
		JPanel panel_departa = new JPanel();
		panel_departa.setBackground(Color.WHITE);
		panel_departa.setBorder(new TitledBorder(null, "Departamento", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_panel_departa = new GridBagConstraints();
		gbc_panel_departa.fill = GridBagConstraints.HORIZONTAL;
		gbc_panel_departa.gridx = 3;
		gbc_panel_departa.gridy = 0;
		panel_filtro.add(panel_departa, gbc_panel_departa);
		UsuarioUtil depart_busq = new UsuarioUtil();
		depart = cargarDepartamentos(depart_busq.extraerDepartamentos);
		comb_depart = new JComboBox(depart);
		panel_departa.add(comb_depart);
		

	}
	
	public void mostrarTabla(Connection conexion) throws SQLException{
		
		modelo = new DefaultTableModel();
		table = new JTable();
		table.setModel(modelo);
		
		modelo.addColumn("ID");
		modelo.addColumn("Nombre");
		modelo.addColumn("Mail");
		modelo.addColumn("Password");
		modelo.addColumn("Departamento");
		modelo.addColumn("Administrador");
		
		UsuarioUtil usuarios_bus = new UsuarioUtil();
		
		ArrayList<Usuario> usuarios = usuarios_bus.buscar(text_buscar.getText(),group.getSelection().getActionCommand(), devolverDepartamento());
		
		Usuario usuario;
		
		for(int i=0; i<usuarios.size(); i++){
			
			Object[] fila = new Object[5];
			usuario = usuarios.get(i);
			fila[0] = usuario.getId();
			fila[1] = usuario.getNombre();
			fila[2] = usuario.getMail();
			fila[3] = usuario.getDepartament();
			fila[4] = usuario.isAdmin();
					
			modelo.addRow(fila);
		}

		add(table, BorderLayout.CENTER);
		
	}
	
	public String[] cargarDepartamentos(ArrayList<String> departamentos){
		
		String[] depart = new String[departamentos.size()+1];
		
		depart[0] = "Todos";
		
		for(int i=0; i<depart.length; i++){
			
			depart[i+1] = departamentos.get(i);
			
		}
		
		return depart;
			
		
	}
	
	public String devolverDepartamento(){
		
		if(comb_depart.getSelectedIndex() == 0){
			
			return "*";
			
		}else{
			
			return depart[comb_depart.getSelectedIndex()+1];
		}
		
	}
	
	

}
