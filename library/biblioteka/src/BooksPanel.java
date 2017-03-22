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

public class BooksPanel extends JPanel {

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

	JLabel authorIDL = new JLabel("Author ID");
	JLabel titleL = new JLabel("Title");
	JLabel óearL = new JLabel("Year");
	JLabel isbnL = new JLabel("ISBN");
	JLabel byIDL = new JLabel("Search/Edit/Delete by Book ID");

	JTextField authorIDField = new JTextField(20);
	JTextField titleField = new JTextField(20);
	JTextField yearField = new JTextField(20);
	JTextField isbnField = new JTextField(20);
	JTextField byIDField = new JTextField(20);

	String[] searchCrit = { "Author ID", "Title", "Year", "ISBN", "Book ID" };
	JComboBox searchCombo = new JComboBox(searchCrit);
	JButton insertB = new JButton("Add Book");
	JButton searchB = new JButton("Search");
	JButton refreshB = new JButton("Refresh");
	JButton editB = new JButton("Edit");
	JButton deleteB = new JButton("Delete");

	public BooksPanel() {
		super();
		init();
	}

	public void init() {
		this.setLayout(new GridLayout(3, 1));
		this.add(topPanel);
		this.add(midPanel);
		this.add(downPanel);

		// top
		topPanel.setLayout(new GridLayout(5, 2));
		topPanel.add(authorIDL);
		topPanel.add(authorIDField);
		topPanel.add(titleL);
		topPanel.add(titleField);
		topPanel.add(óearL);
		topPanel.add(yearField);
		topPanel.add(isbnL);
		topPanel.add(isbnField);
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
		boolean enabled = searchCombo.getSelectedIndex() == 4;
		byIDField.setEnabled(enabled);
		byIDL.setEnabled(enabled);
	}

	public MyModel getModel() throws Exception {
		state = DBUtil.getConnected().createStatement();
		result = state.executeQuery("select * from book");
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
				prepState = conn.prepareStatement("update book set author_id=?,title=?,year=?,isbn=? where id=?");
				prepState.setString(1, authorIDField.getText());
				prepState.setString(2, titleField.getText());
				prepState.setString(3, yearField.getText());
				prepState.setString(4, isbnField.getText());
				prepState.setInt(5, Integer.parseInt(byIDField.getText()));
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
					prepState = conn.prepareStatement("select * from book where Author_ID=?");
					prepState.setInt(1,Integer.parseInt(authorIDField.getText().trim()));
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
					prepState = conn.prepareStatement("select * from book where LOWER(title) LIKE LOWER(?)");
					prepState.setString(1, "%"+titleField.getText().trim()+"%");
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
					prepState = conn.prepareStatement("select * from book where year=?");
					prepState.setInt(1,Integer.parseInt(yearField.getText().trim()));
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
					prepState = conn.prepareStatement("select * from book where REPLACE(TRIM(ISBN),'-','') LIKE ?");
					prepState.setString(1, "%"+isbnField.getText().trim().replaceAll("-", "")+"%");
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
			case 4:
				conn = DBUtil.getConnected();
				try {
					prepState = conn.prepareStatement("select * from book where id=?");
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
			String AI = authorIDField.getText().trim();
			String T = titleField.getText().trim();
			String Y = yearField.getText().trim();
			String ISBN = isbnField.getText().trim();

			try {
				conn = DBUtil.getConnected();
				prepState = conn.prepareStatement("insert into book values (null,?,?,?,?)");
				prepState.setString(1, AI);
				prepState.setString(2, T);
				prepState.setString(3, Y);
				prepState.setString(4, ISBN);
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
				prepState = conn.prepareStatement("delete from book where id=?");
				prepState.setInt(1, Integer.parseInt(byIDField.getText()));
				prepState.executeUpdate();
				refreshContent();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}

	}// end DeleteAction

}
