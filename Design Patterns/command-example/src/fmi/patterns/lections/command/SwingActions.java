/**
 * ������ �� ������� ������� ��� ������������ "������� �� �����������"
 */
package fmi.patterns.lections.command;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

/**
 * ����������� ������� �������, ���� ���������� �� Swing Actions
 * 
 */
public class SwingActions extends JFrame {
	private JToolBar tb;
	private JTextArea ta;
	private JMenu fileMenu;
	private Action openAction;
	private Action closeAction;

	public SwingActions() {
		super("Swing Actions");
		setupGUI();
	}

	private void setupGUI() {
		// ������� toolbar � ����.
		tb = new JToolBar();
		fileMenu = new JMenu("����");
		
		// ������� ���������� ������, � ����� �� �� ������� ���������.
		ta = new JTextArea(5, 30);
		JScrollPane scrollPane = new JScrollPane(ta);

		// ���������� �� ������������.
		JPanel contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout());
		contentPane.setPreferredSize(new Dimension(400, 150));
		contentPane.add(tb, BorderLayout.NORTH);
		contentPane.add(scrollPane, BorderLayout.CENTER);
		setContentPane(contentPane); 
		
		// ��������� �� ������.
		JMenuBar mb = new JMenuBar();
		mb.add(fileMenu);
		setJMenuBar(mb);

		// ��������� �� �������� �� "������".
		ImageIcon openIcon = new ImageIcon("open.png");
		openAction = new AbstractAction("������", openIcon) {
			public void actionPerformed(ActionEvent e) {
				ta.append("���������� ������ �� " + e.getActionCommand() + "\n");
			}
		};
		
		// ���������� �� action �� �������� �� ����� � toolbar-a.
		JButton openButton = tb.add(openAction);
		openButton.setText("");
		openButton.setActionCommand("����� ������");
		openButton.setToolTipText("���� � ������ �� ��������");

		// ���������� �� action �� �������� �� ����� ��� ������ ����.
		JMenuItem openMenuItem = fileMenu.add(openAction);
		openMenuItem.setIcon(null);
		openMenuItem.setActionCommand("����� � ������ �� ��������");
		
		// �� ������� ����� �� ������� action �� "���������" � 
		// �� �������� �� �������� �� ����� � toolbar-a � ����� � ������.
		// ����� � ������� �� ������������� ������.
	}

	public static void main(String[] args) {
		SwingActions frame = new SwingActions();
		frame.pack();
		frame.setVisible(true);
	}
}
