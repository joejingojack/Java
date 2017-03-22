package grupa3;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import com.borland.jbcl.layout.XYLayout;
import com.borland.jbcl.layout.*;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JScrollBar;
import com.borland.dbswing.TableScrollPane;
import com.borland.dbswing.JdbTable;
import com.borland.dx.sql.dataset.Database;
import com.borland.dx.sql.dataset.ConnectionDescriptor;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.dx.sql.dataset.Load;
import com.borland.dx.sql.dataset.QueryDescriptor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.sql.*;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2014</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class Frame1 extends JFrame {
    JPanel contentPane;
    JLabel lb1 = new JLabel();
    XYLayout xYLayout1 = new XYLayout();
    JLabel jLabel1 = new JLabel();
    JLabel jLabel2 = new JLabel();
    JTextField jTextField1 = new JTextField();
    JTextField jTextField2 = new JTextField();
    JTextField jTextField3 = new JTextField();
    JButton jButton1 = new JButton();
    TableScrollPane tableScrollPane1 = new TableScrollPane();
    JdbTable jdbTable1 = new JdbTable();
    Database database1 = new Database();
    QueryDataSet queryDataSet1 = new QueryDataSet();
    public Frame1() {
        try {
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            jbInit();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Component initialization.
     *
     * @throws java.lang.Exception
     */
    private void jbInit() throws Exception {
        contentPane = (JPanel) getContentPane();
        contentPane.setLayout(xYLayout1);
        setSize(new Dimension(400, 300));
        setTitle("Frame Title");
        lb1.setMaximumSize(new Dimension(16, 19));
        lb1.setMinimumSize(new Dimension(16, 19));
        lb1.setPreferredSize(new Dimension(16, 19));
        lb1.setText("ID");
        jLabel1.setText("Ime");
        jLabel2.setText("Familia");
        jButton1.setText("Save");
        jButton1.addMouseListener(new Frame1_jButton1_mouseAdapter(this));
        database1.setConnection(new ConnectionDescriptor(
                "jdbc:borland:dslocal:C:\\Borland\\JBuilder2006\\bin\\g3.jds",
                "SYSDBA", "masterkey", false,
                "com.borland.datastore.jdbc.DataStoreDriver"));
        queryDataSet1.setQuery(new QueryDescriptor(database1,
                "SELECT * FROM TABLE1", null, true, Load.ALL));
        jdbTable1.setDataSet(queryDataSet1);
        contentPane.add(lb1, new XYConstraints(25, 28, 44, 25));
        contentPane.add(jLabel2, new XYConstraints(25, 100, 35, 19));
        contentPane.add(jLabel1, new XYConstraints(25, 67, -1, 20));
        contentPane.add(tableScrollPane1, new XYConstraints(22, 182, 325, 86));
        contentPane.add(jTextField2, new XYConstraints(65, 65, 79, 28));
        contentPane.add(jTextField1, new XYConstraints(64, 31, 80, 23));
        contentPane.add(jButton1, new XYConstraints(196, 31, 60, 28));
        tableScrollPane1.getViewport().add(jdbTable1);
        contentPane.add(jTextField3, new XYConstraints(66, 102, 79, 31));
    }

    public void jButton1_mouseClicked(MouseEvent mouseEvent) {
        String sql_str="insert into TABLE1 (ID,IME,FAMILIA) values(?,?,?)";
                  PreparedStatement ps=database1.createPreparedStatement(sql_str);
                  try {
                    ps.setInt(1,Integer.parseInt(jTextField1.getText()));
                    ps.setString(2,jTextField2.getText());
                    ps.setString(3,jTextField3.getText());
                   int h=ps.executeUpdate();
                  }
                  catch(SQLException e1){
                          System.out.println("Error :"+e1);
                 }

                 queryDataSet1.refresh();
    }
}


class Frame1_jButton1_mouseAdapter extends MouseAdapter {
    private Frame1 adaptee;
    Frame1_jButton1_mouseAdapter(Frame1 adaptee) {
        this.adaptee = adaptee;
    }

    public void mouseClicked(MouseEvent mouseEvent) {
        adaptee.jButton1_mouseClicked(mouseEvent);
    }
}
