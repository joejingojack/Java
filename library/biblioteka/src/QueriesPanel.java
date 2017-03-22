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

public class QueriesPanel extends JPanel {

	private static final String overdueQuery = "select person.* from person where id in (select person_id from loaned where deadline < current_date and taken = 1)";
	private static final String takenBooksQuery = "select * from book where exists(select loaned.id from loaned where loaned.book_id=book.id and loaned.taken=1)";
	private static final String authorsAndBooksQuery = "select a.name, b.title from authors a , book b where a.id=b.author_id";

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
	JPanel bottomPanel = new JPanel();

	JButton queryOverdue = new JButton("People with overdue books");
	JButton queryTakenBooks = new JButton("Currently taken books");
	JButton queryAuthorsAndBooks = new JButton("Authors and their books");

	JLabel overdueWhere = new JLabel("Where");
	JComboBox<FieldDef> overdueField = new JComboBox<FieldDef>(new FieldDef[] { new FieldDef("ID"), new FieldDef("FIRSTNAME", "First name"),
			new FieldDef("LASTNAME", "Last name"), new FieldDef("EMAIL", "E-mail"), new FieldDef("ADDRESS", "Address"), new FieldDef("PHONE", "Phone"), });
	JComboBox<String> overdueComparison = new JComboBox<String>(new String[] { "=", "<", ">", "<>", "LIKE" });
	JTextField overdueValue = new JTextField(10);

	JLabel takenBooksWhere = new JLabel("Where");
	JComboBox<FieldDef> takenBooksField = new JComboBox<FieldDef>(new FieldDef[] { new FieldDef("ID"), new FieldDef("AUTHOR_ID", "Author id"),
			new FieldDef("TITLE", "Title"), new FieldDef("YEAR", "Year"), new FieldDef("ISBN", "ISBN") });
	JComboBox<String> takenBooksComparison = new JComboBox<String>(new String[] { "=", "<", ">", "<>", "LIKE" });
	JTextField takenBooksValue = new JTextField(10);

	JLabel authorsAndBooksWhere = new JLabel("Where");
	JComboBox<FieldDef> authorsAndBooksField = new JComboBox<FieldDef>(new FieldDef[] { new FieldDef("NAME", "Name"), new FieldDef("TITLE", "Title") });
	JComboBox<String> authorsAndBooksComparison = new JComboBox<String>(new String[] { "=", "<", ">", "<>", "LIKE" });
	JTextField authorsAndBooksValue = new JTextField(10);

	private static class FieldDef {
		String column;
		String displayName;

		public FieldDef(String column) {
			this.column = column;
			this.displayName = column;
		}

		public FieldDef(String column, String displayName) {
			this.column = column;
			this.displayName = displayName;
		}

		public String toString() {
			return displayName;
		}
	}

	public QueriesPanel() {
		super();
		init();
	}

	public void init() {
		this.setLayout(new GridLayout(2, 1));
		this.add(topPanel);
		this.add(bottomPanel);

		// top
		topPanel.setLayout(new FlowLayout());

		JPanel overduePanel = new JPanel(new GridLayout(2, 1));
		overduePanel.add(queryOverdue);
		JPanel overdueContainer = new JPanel(new FlowLayout());
		overdueContainer.add(overdueWhere);
		overdueContainer.add(overdueField);
		overdueContainer.add(overdueComparison);
		overdueContainer.add(overdueValue);
		overduePanel.add(overdueContainer);
		topPanel.add(overduePanel);

		JPanel takenBooksPanel = new JPanel(new GridLayout(2, 1));
		takenBooksPanel.add(queryTakenBooks);
		JPanel takenBooksContainer = new JPanel(new FlowLayout());
		takenBooksContainer.add(takenBooksWhere);
		takenBooksContainer.add(takenBooksField);
		takenBooksContainer.add(takenBooksComparison);
		takenBooksContainer.add(takenBooksValue);
		takenBooksPanel.add(takenBooksContainer);
		topPanel.add(takenBooksPanel);

		JPanel authorsAndBooksPanel = new JPanel(new GridLayout(2, 1));
		authorsAndBooksPanel.add(queryAuthorsAndBooks);
		JPanel authorsAndBooksContainer = new JPanel(new FlowLayout());
		authorsAndBooksContainer.add(authorsAndBooksWhere);
		authorsAndBooksContainer.add(authorsAndBooksField);
		authorsAndBooksContainer.add(authorsAndBooksComparison);
		authorsAndBooksContainer.add(authorsAndBooksValue);
		authorsAndBooksPanel.add(authorsAndBooksContainer);
		topPanel.add(authorsAndBooksPanel);

		queryOverdue.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					model = getModel(
							overdueQuery + " AND " + ((FieldDef) overdueField.getSelectedItem()).column + " " + ((String) overdueComparison.getSelectedItem()) + "?", overdueValue.getText());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				refreshContent();
			}
		});

		queryTakenBooks.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					model = getModel(
							takenBooksQuery + " AND " + ((FieldDef) takenBooksField.getSelectedItem()).column + " " + ((String) takenBooksComparison.getSelectedItem()) + "?", takenBooksValue.getText());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				refreshContent();
			}
		});

		queryAuthorsAndBooks.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					model = getModel(authorsAndBooksQuery + " AND " + ((FieldDef) authorsAndBooksField.getSelectedItem()).column + " " + ((String) authorsAndBooksComparison.getSelectedItem()) + "?", authorsAndBooksValue.getText());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				refreshContent();
			}
		});

		// bottom
		tableScroll.setPreferredSize(new Dimension(550, 174));
		bottomPanel.add(tableScroll);

		// defaults
		overdueComparison.setSelectedIndex(2);
		overdueValue.setText("0");

		takenBooksComparison.setSelectedIndex(2);
		takenBooksValue.setText("0");

		authorsAndBooksComparison.setSelectedIndex(2);
		authorsAndBooksValue.setText("0");
	}

	public MyModel getModel(String query, Object... params) throws Exception {
		PreparedStatement s = DBUtil.getConnected().prepareStatement(query);
		if (params != null) {
			for (int i = 0; i < params.length; ++i) {
				s.setObject(i + 1, params[i]);
			}
		}
		result = s.executeQuery();
		model = new MyModel(result);
		return model;
	}

	public void refreshContent() {
		try {
			model.fireTableDataChanged();
			table.setModel(model);
			table.repaint();

		} catch (Exception ex) {
		}
	}// end refreshContent

}
