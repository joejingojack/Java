import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

public class PeoplePanel extends JPanel {

	/**
	 * @param args
	 */
	boolean flag = false;
	int id = 0;
	Connection conn;
	Statement state = null;
	PreparedStatement prepState = null;
	ResultSet result = null;
	JTable table = new JTable();
	JScrollPane tableScroll = new JScrollPane(table);
	MyModel model;

	JPanel topPanel = new JPanel();
	JPanel midPanel = new JPanel();
	JPanel downPanel = new JPanel();

	JLabel fNameL = new JLabel("First Name");
	JLabel lNameL = new JLabel("Last Name");
	JLabel emailL = new JLabel("Email");
	JLabel addressL = new JLabel("Address");
	JLabel phoneL = new JLabel("Phone");
	JLabel byIDL = new JLabel("Search/Edit/Delete by ID");

	JTextField fNameField = new JTextField(20);
	JTextField lNameField = new JTextField(20);
	JTextField emailField = new JTextField(20);
	JTextField addressField = new JTextField(20);
	JTextField phoneField = new JTextField(20);
	JTextField byIDField = new JTextField(20);

	String[] searchCrit = { "First Name", "Last Name", "Phone", "ID" };
	JComboBox searchCombo = new JComboBox(searchCrit);
	JButton insertB = new JButton("Add Person");
	JButton searchB = new JButton("Search");
	JButton refreshB = new JButton("Refresh");
	JButton editB = new JButton("Edit");
	JButton deleteB = new JButton("Delete");

	public PeoplePanel() {
		super();
		init();
	}

	public void init() {
		this.setLayout(new GridLayout(3, 1));
		this.add(topPanel);
		this.add(midPanel);
		this.add(downPanel);

		// top
		topPanel.setLayout(new GridLayout(6, 2));
		topPanel.add(fNameL);
		topPanel.add(fNameField);
		topPanel.add(lNameL);
		topPanel.add(lNameField);
		topPanel.add(emailL);
		topPanel.add(emailField);
		topPanel.add(addressL);
		topPanel.add(addressField);
		topPanel.add(phoneL);
		topPanel.add(phoneField);
		topPanel.add(byIDL);
		topPanel.add(byIDField);

		byIDField.setEnabled(false);
		byIDL.setEnabled(false);

		// middle
		midPanel.setLayout(new FlowLayout());
		midPanel.add(insertB);
		midPanel.add(deleteB);
		midPanel.add(editB);
		midPanel.add(searchCombo);
		midPanel.add(searchB);
		midPanel.add(refreshB);
		

		editB.addActionListener(new EditAction());
		refreshB.addActionListener(new FreshAction());
		insertB.addActionListener(new InsertAction());
		searchB.addActionListener(new SearchAction());
		deleteB.addActionListener(new DeleteAction());

		// bottom
		tableScroll.setPreferredSize(new Dimension(550, 100));
		downPanel.add(tableScroll);
		searchCombo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				updateState();
			}
		});
		try {
			table.setModel(getModel());
		} catch (Exception e) {
			e.printStackTrace();
		}// end try/catch

	}// end init

	protected void updateState() {
		boolean enabled = searchCombo.getSelectedIndex() == 3;
		byIDField.setEnabled(enabled);
		byIDL.setEnabled(enabled);
	}

	public MyModel getModel() throws Exception {
		state = DBUtil.getConnected().createStatement();
		result = state.executeQuery("select * from person");
		model = new MyModel(result);
		return model;
	}

	public void refreshContent() {
		try {
			model = getModel();
			model.fireTableDataChanged();
			table.setModel(model);
			table.repaint();

		} catch (Exception ex) {
		}
	}// end refreshContent

	class FreshAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			refreshContent();
		}

	}// end FreshAction

	class EditAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			conn = DBUtil.getConnected();
			try {
				prepState = conn.prepareStatement("update person set firstName=?,lastName=?,email=?,address=?,phone=? where id=?");
				prepState.setString(1, fNameField.getText());
				prepState.setString(2, lNameField.getText());
				prepState.setString(3, emailField.getText());
				prepState.setString(4, addressField.getText());
				prepState.setString(5, phoneField.getText());
				prepState.setInt(6, Integer.parseInt(byIDField.getText()));
				prepState.executeUpdate();
				refreshContent();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}

	}// end EditAction

	class SearchAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			updateState();
			switch (searchCombo.getSelectedIndex()) {
			case 0:
				conn = DBUtil.getConnected();
				try {
					prepState = conn.prepareStatement("select * from person where LOWER(firstName) LIKE LOWER(?)");
					prepState.setString(1, "%"+fNameField.getText().trim()+"%");
					result = prepState.executeQuery();
					model = new MyModel(result);
					model.fireTableDataChanged();
					table.setModel(model);
					table.repaint();
				} catch (SQLException e1) {
					e1.printStackTrace();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				break;
			case 1:
				conn = DBUtil.getConnected();
				try {
					prepState = conn.prepareStatement("select * from person where LOWER(lastName) LIKE LOWER(?)");
					prepState.setString(1, "%"+lNameField.getText().trim()+"%");
					result = prepState.executeQuery();
					model = new MyModel(result);
					model.fireTableDataChanged();
					table.setModel(model);
					table.repaint();
				} catch (SQLException e1) {
					e1.printStackTrace();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				break;
			case 2:
				conn = DBUtil.getConnected();
				try {
					prepState = conn.prepareStatement("select * from person where phone LIKE ?");
					prepState.setString(1, "%"+phoneField.getText().trim().replaceAll(" ", "")+"%");
					result = prepState.executeQuery();
					model = new MyModel(result);
					model.fireTableDataChanged();
					table.setModel(model);
					table.repaint();
				} catch (SQLException e1) {
					e1.printStackTrace();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				break;
			case 3:
				conn = DBUtil.getConnected();
				try {
					prepState = conn.prepareStatement("select * from person where id=?");
					prepState.setInt(1,Integer.parseInt(byIDField.getText().trim()));
					result = prepState.executeQuery();
					id = Integer.parseInt(byIDField.getText().trim());
					model = new MyModel(result);
					model.fireTableDataChanged();
					table.setModel(model);
					table.repaint();
				} catch (SQLException e1) {
					e1.printStackTrace();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				break;
			}// end switch
		}// end actionPerformed
	}// end SearchAction

	class InsertAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String fN = fNameField.getText().trim();
			String lN = lNameField.getText().trim();
			String email = emailField.getText().trim();
			String address = addressField.getText().trim();
			String phone = phoneField.getText().trim().replaceAll(" ", "");

			try {
				conn = DBUtil.getConnected();
				prepState = conn.prepareStatement("insert into person values (null,?,?,?,?,?)");
				prepState.setString(1, fN);
				prepState.setString(2, lN);
				prepState.setString(3, email);
				prepState.setString(4, address);
				prepState.setString(5, phone);
				prepState.execute();
				System.out.println("Success");
				prepState.close();
				conn.close();
				refreshContent();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}// end actionPerformed

	}// end InsertAction
	
	class DeleteAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			conn = DBUtil.getConnected();
			try {
				prepState = conn.prepareStatement("delete from person where id=?");
				prepState.setInt(1, Integer.parseInt(byIDField.getText()));
				prepState.executeUpdate();
				refreshContent();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}

	}// end DeleteAction

}
