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

public class NBAStatsViewer extends WindowController implements ActionListener,
		MouseListener {

	private static final int WINDOW_SIZE = 600;
	private static final int CATEGORIES = 30;
	private static final int MAX_GAMES = 86;

	private JTextField playerNameField;
	private JPanel mainPanel;
	private JLabel[][] statsLabels;
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
			statsLabels = new JLabel[MAX_GAMES][CATEGORIES];
			currentPlayer.displayStats(this, mainPanel, getContentPane(),
					statsLabels);
		} else {

		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {
		for (int i = 0; i < statsLabels.length; i++) {
			for (int j = 0; j < statsLabels[0].length; j++) {
				if (e.getSource() == statsLabels[i][j])
					currentPlayer.displayGraph(j, canvas);
			}
		}

	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}
}
