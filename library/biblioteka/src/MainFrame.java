
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.event.*;

public class MainFrame extends JFrame {

	public MainFrame() {

		setTitle("Library");
		setSize(600, 510); 
		JTabbedPane jtp = new JTabbedPane();

		getContentPane().add(jtp);

		final PeoplePanel jp1 = new PeoplePanel();
		final LoanedPanel jp2= new LoanedPanel();
		final BooksPanel jp3 = new BooksPanel();
		final AuthorsPanel jp4 = new AuthorsPanel();
		final QueriesPanel jp5 = new QueriesPanel();


		jtp.addTab("People", jp1);
		jtp.addTab("Loaned books", jp2);
		jtp.addTab("Books", jp3);
		jtp.addTab("Authors", jp4);
		jtp.addTab("Queries",jp5);
		jtp.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				 if (e.getSource() instanceof JTabbedPane){
					 JTabbedPane pane = (JTabbedPane) e.getSource();
					 if(pane.getSelectedIndex()==1){
						 jp2.refreshUI();
					 }
				 }
					 
			}
		});

		setVisible(true);
	}

	public static void main(String[] args) {
		MainFrame tabbedPane = new MainFrame();
		tabbedPane.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

}
