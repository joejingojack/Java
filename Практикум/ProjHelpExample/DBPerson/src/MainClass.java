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
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;


public class MainClass extends JFrame {

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
	
	JPanel pan1 = new JPanel();
	JPanel pan2 = new JPanel();
	JPanel pan3 = new JPanel();
	JTabbedPane tabbedPane = new JTabbedPane();
	
	JPanel topPanel = new JPanel();
	JPanel midPanel = new JPanel();
	JPanel downPanel = new JPanel();
	
	JLabel fNameL = new JLabel("First Name");
	JLabel lNameL = new JLabel("Last Name");
	JLabel ageL = new JLabel("Age");
	JLabel mailL = new JLabel("Mail");
	
	JTextField fNameField = new JTextField(20);
	JTextField lNameField = new JTextField(20);
	JTextField maileField = new JTextField(20);
	JTextField ageField = new JTextField(20);
	
	String[] comboContent = {"female","male"};
	String[] searchCrit = {"First Name","Last Name","Age","Mail","Person_ID"};
	JComboBox searchCombo =new JComboBox(searchCrit);
	
	JButton insertB = new JButton("Add Person");
	JButton searchB = new JButton("Search");
	JButton refreshB = new JButton("Fresh(All)");
	JButton edit = new JButton("Edit");
	JButton startB = new JButton("Start");
	
	public MainClass(){
		super();
		init();
	}
	
	public void init(){
		
		this.setLayout(new GridLayout(4,2));
		this.setSize(600, 500);
		this.add(tabbedPane);
		this.add(topPanel);
		this.add(midPanel);
		this.add(downPanel);
		
		//tabPanels
		tabbedPane.add("First Panel", topPanel);
		tabbedPane.add("Second Panel", pan2);
		tabbedPane.add("Third Panel", pan3);
		
		//topPanel
		topPanel.setLayout(new GridLayout(5,2));
		topPanel.add(fNameL);
		topPanel.add(fNameField);
		topPanel.add(lNameL);
		topPanel.add(lNameField);
		topPanel.add(ageL);
		topPanel.add(ageField);
		topPanel.add(mailL);
		topPanel.add(maileField);
		
		//midPanel
		midPanel.setLayout(new FlowLayout());
		midPanel.add(insertB);
		midPanel.add(searchCombo);
		midPanel.add(searchB);
		midPanel.add(refreshB);
		midPanel.add(edit);
		midPanel.add(startB);
		
		startB.addActionListener(new StartAction());
		edit.addActionListener(new EditAction());
		refreshB.addActionListener(new FreshAction());		
		insertB.addActionListener(new InsertAction());
		searchB.addActionListener(new SearchAction());
		//downPanel
		tableScroll.setPreferredSize(new Dimension(500,100));
		downPanel.add(tableScroll);

		try {
			table.setModel(getModel());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// end try/catch
		
	}// end init()
	
	public ArrayList<String> getPNames(){
		conn = DBUtil.getConnected();
		ArrayList<String> names = new ArrayList<String>();
		try {
			prepState = conn.prepareStatement("select f_name,l_name from person");
			result = prepState.executeQuery();
			
			while(result.next()){
				names.add(result.getString("f_name") +"  "+ result.getString("l_name"));
			}// end while
			return names;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // end try/catch
		return names;
	}// end getPNames()
	
	public MyModel getModel() throws Exception{
		state = DBUtil.getConnected().createStatement();
		result = state.executeQuery("select * from person");
		model = new MyModel(result);
		return model;
	}
	
	public void refreshContent(){
		try{
			model = getModel();
			model.fireTableDataChanged();
			table.setModel(model);
			table.repaint();
		}
		catch(Exception ex){}
	}// end refreshContent()
	
	class StartAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			flag = !flag;
			final ArrayList<String> temp = getPNames();
			final Random randNum = new Random();
			new Thread(){
				public void run(){
					while(flag){
						fNameField.setText(temp.get(randNum.nextInt(temp.size())));
						try {
							sleep(50);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}//end try/catch
					}// end while
				}// end run
			}.start(); // end Thread
		}// end actionPerformed
		
	}// end StartAction
	
	class FreshAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			refreshContent();
		}
		
	}// end FreshAction
	
	class EditAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			conn = DBUtil.getConnected();
			try {
				prepState = conn.prepareStatement("update person set f_name=? where person_id=?");
				prepState.setString(1, fNameField.getText());
				prepState.setInt(2, id);
				prepState.executeUpdate();
				refreshContent();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
	}// end EditAction
	
	class SearchAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if(searchCombo.getSelectedIndex() == 0){
				conn = DBUtil.getConnected();
				try {
					prepState =conn.prepareStatement("select * from person where f_name=?");
					prepState.setString(1, fNameField.getText().trim());
					result = prepState.executeQuery();
					
					model = new MyModel(result);
					model.fireTableDataChanged();
					table.setModel(model);
					table.repaint();
					
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (Exception e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				} 
			}// end if clause
			if(searchCombo.getSelectedIndex() == searchCrit.length-1){
				conn = DBUtil.getConnected();
				try {
					prepState = conn.prepareStatement("select * from person where person_id=?");
					prepState.setInt(1, Integer.parseInt(fNameField.getText().trim()));
					result = prepState.executeQuery();
					id = Integer.parseInt(fNameField.getText().trim());
					
					while(result.next()){
						fNameField.setText(result.getString("f_name"));
					}// end while
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}// end if clause
		}// end actionPerformed
		
	}// end SearchAction
	
	class InsertAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			String fN = fNameField.getText().trim();
			String lN = lNameField.getText().trim();
			String mail = maileField.getText().trim();
			int a = Integer.parseInt(ageField.getText());
			
			try {
				conn = DBUtil.getConnected();
				prepState = conn.prepareStatement("insert into person values (null,?,?,?,?,?)");
				prepState.setString(1, fN);
				prepState.setString(2, lN);
				prepState.setInt(4, a);
				prepState.setString(5, mail);
				prepState.execute();
				System.out.println("Success");
				prepState.close();
				conn.close();
				refreshContent();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}// end actionPerformed
		
	}// end InsertAction
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MainClass jFrame = new MainClass();
		jFrame.setVisible(true);
		jFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

}
