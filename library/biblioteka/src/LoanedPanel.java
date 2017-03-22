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

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ListDataListener;

public class LoanedPanel extends JPanel {

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

	JLabel PersonIDL = new JLabel("Person ID");
	JLabel BookIDL = new JLabel("Book ID");
	JLabel TakenONL = new JLabel("Taken on");
	JLabel DeadlineL = new JLabel("Deadline");
	JLabel ReturnedL = new JLabel("Returned");
	JLabel byIDL = new JLabel("Search/Edit/Delete/Return by Loaned ID");

	JComboBox<Person> PersonIDField = new JComboBox<Person>(new Person[] {});
	JComboBox<Book> BookIDField = new JComboBox<Book>(new Book[] {});
	JTextField TakenONField = new JTextField(20);
	JTextField DeadlineField = new JTextField(20);
	JTextField ReturnedField = new JTextField(20);
	JTextField byIDField = new JTextField(20);

	String[] searchCrit = { "Person ID", "Book ID", "Taken on", "Deadline", "Returned on", "Loan ID" };
	JComboBox searchCombo = new JComboBox(searchCrit);
	JButton insertB = new JButton("Loan a book");
	JButton searchB = new JButton("Search");
	JButton refreshB = new JButton("Refresh");
	JButton editB = new JButton("Edit");
	JButton deleteB = new JButton("Delete");
	JButton returnB = new JButton("Return a book");

	public LoanedPanel() {
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
		topPanel.add(PersonIDL);
		topPanel.add(PersonIDField);
		topPanel.add(BookIDL);
		topPanel.add(BookIDField);
		topPanel.add(TakenONL);
		topPanel.add(TakenONField);
		topPanel.add(DeadlineL);
		topPanel.add(DeadlineField);
		topPanel.add(ReturnedL);
		topPanel.add(ReturnedField);
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
		midPanel.add(returnB);

		editB.addActionListener(new EditAction());
		refreshB.addActionListener(new FreshAction());
		insertB.addActionListener(new InsertAction());
		searchB.addActionListener(new SearchAction());
		deleteB.addActionListener(new DeleteAction());
		returnB.addActionListener(new ReturnAction());

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
		refreshUI();
	}// end init

	public static class Person {
		int id;
		String name;

		public Person(int id, String name) {
			this.id = id;
			this.name = name;
		}

		public String toString() {
			return "(" + id + ") " + name;
		}
	}

	public static class Book {
		int id;
		String name;

		public Book(int id, String name) {
			this.id = id;
			this.name = name;
		}

		public String toString() {
			return "(" + id + ") " + name;
		}
	}

	private void refreshPersonCombo() {
		try {
			Statement s = DBUtil.getConnected().createStatement();
			ResultSet rs = s.executeQuery("SELECT id,firstname,lastname FROM Person");
			rs.first();
			ArrayList<Person> people = new ArrayList<LoanedPanel.Person>();
			while (!rs.isAfterLast()) {
				Person p = new Person(rs.getInt(1), rs.getString(2) + " " + rs.getString(3));
				people.add(p);
				rs.next();
			}
			PersonIDField.setModel(new DefaultComboBoxModel<LoanedPanel.Person>(people.toArray(new Person[] {})));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void refreshBookCombo() {
		try {
			Statement s = DBUtil.getConnected().createStatement();
			ResultSet rs = s.executeQuery("select id,title from book where not exists (select loaned.id from loaned where loaned.book_id=book.id and loaned.taken=1)");
			rs.first();
			ArrayList<Book> books = new ArrayList<LoanedPanel.Book>();
			while (!rs.isAfterLast()) {
				Book b = new Book(rs.getInt(1), rs.getString(2));
				books.add(b);
				rs.next();
			}
			BookIDField.setModel(new DefaultComboBoxModel<LoanedPanel.Book>(books.toArray(new Book[] {})));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void refreshUI() {
		refreshPersonCombo();
		refreshBookCombo();
	}

	protected void updateState() {
		boolean enabled = searchCombo.getSelectedIndex() == 5;
		byIDField.setEnabled(enabled);
		byIDL.setEnabled(enabled);
	}

	public MyModel getModel() throws Exception {
		state = DBUtil.getConnected().createStatement();
		result = state.executeQuery("select * from loaned");
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
				prepState = conn.prepareStatement("update loaned set person_id=?,book_id=?,deadline=? where id=?");
				prepState.setInt(1, ((Person) PersonIDField.getSelectedItem()).id);
				prepState.setInt(2, ((Book) BookIDField.getSelectedItem()).id);
				prepState.setString(3, DeadlineField.getText());
				prepState.setInt(4, Integer.parseInt(byIDField.getText()));
				prepState.executeUpdate();
				refreshContent();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			refreshUI();
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
					prepState = conn.prepareStatement("select * from loaned where person_id=?");
					prepState.setInt(1, ((Person) PersonIDField.getSelectedItem()).id);
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
					prepState = conn.prepareStatement("select * from loaned where book_id=?");
					prepState.setInt(1, ((Book) BookIDField.getSelectedItem()).id);
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
					prepState = conn.prepareStatement("select * from loaned where taken_on=?");
					prepState.setString(1, TakenONField.getText().trim());
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
					prepState = conn.prepareStatement("select * from loaned where deadline=?");
					prepState.setString(1, DeadlineField.getText().trim());
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
					prepState = conn.prepareStatement("select * from loaned where returned=?");
					prepState.setString(1, ReturnedField.getText().trim());
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
			case 5:
				conn = DBUtil.getConnected();
				try {
					prepState = conn.prepareStatement("select * from loaned where id=?");
					prepState.setInt(1, Integer.parseInt(byIDField.getText().trim()));
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
			int PI = ((Person) PersonIDField.getSelectedItem()).id;
			int BI = ((Book) BookIDField.getSelectedItem()).id;
			String TON = TakenONField.getText().trim();
			String DL = DeadlineField.getText().trim();
			try {
				conn = DBUtil.getConnected();
				PreparedStatement checkAvailability = conn.prepareStatement("SELECT COUNT(*) FROM loaned WHERE book_id=? AND taken=?");
				checkAvailability.setInt(1, BI);
				checkAvailability.setBoolean(2, true);
				ResultSet rs = checkAvailability.executeQuery();
				rs.first();
				int count = rs.getInt(1);
				if (count > 0) {
					throw new IllegalStateException("The book with ID " + BI + " is already loaned.");
				}
				prepState = conn.prepareStatement("insert into loaned values (null,?,?,1,current_date,?,null)");
				prepState.setInt(1, PI);
				prepState.setInt(2, BI);
				// prepState.setString(3, TON);
				prepState.setString(3, DL);
				prepState.execute();
				System.out.println("Success");
				prepState.close();
				conn.close();
				refreshContent();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			refreshUI();
		}// end actionPerformed

	}// end InsertAction

	class DeleteAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			conn = DBUtil.getConnected();
			try {
				prepState = conn.prepareStatement("delete from loaned where id=?");
				prepState.setInt(1, Integer.parseInt(byIDField.getText()));
				prepState.executeUpdate();
				refreshContent();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			refreshUI();
		}

	}// end DeleteAction

	class ReturnAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			conn = DBUtil.getConnected();
			try {
				prepState = conn.prepareStatement("update loaned set taken=0,returned=current_date where id=?");
				prepState.setInt(1, Integer.parseInt(byIDField.getText()));
				prepState.executeUpdate();
				refreshContent();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			refreshUI();
		}

	}// end ReturnAction

}
