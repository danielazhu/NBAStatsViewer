/**
 * This is the main controller for NBAStatsViewer. The applet is initialized
 * here and calls the begin() method. Please use window size 600 x 600.
 * 
 * @author Daniel Zhu
 *
 */

import objectdraw.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class NBAStatsViewer extends WindowController implements ActionListener {

	public static final int WINDOW_SIZE = 600;

	private JTextField playerNameField;
	private JPanel mainPanel;
	private Player currentPlayer;

	public void begin() {
		playerNameField = new JTextField("Enter player name.");
		mainPanel = new JPanel();
		mainPanel.add(playerNameField);
		getContentPane().add(mainPanel, BorderLayout.SOUTH);
		getContentPane().validate();
		playerNameField.addActionListener(this);
		getContentPane().setSize(WINDOW_SIZE, WINDOW_SIZE);
		resize(WINDOW_SIZE, WINDOW_SIZE);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == playerNameField) {
			currentPlayer = new Player(playerNameField.getText());
			currentPlayer.displayStats(mainPanel, getContentPane());
		}
	}

}
