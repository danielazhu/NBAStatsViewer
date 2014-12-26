/**
 * The Player object represents the NBA player and holds the stats retrieved
 * from basketball-reference.com
 * 
 * @author Daniel
 *
 */

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridLayout;
import java.io.*;
import java.net.*;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class Player {

	private static final int CATEGORIES = 30;
	private static final int MAX_GAMES = 82;
	private String[][] stats;
	private int numGames;
	private String playerName;

	/**
	 * Retrieve stats from basketball-reference.com using the player's name
	 */
	public Player(String playerName) {
		this.playerName = playerName;
		stats = new String[MAX_GAMES][CATEGORIES];

		/*
		 * Basketball-reference uses 2 letters of the first name and 5 letters
		 * of the last name
		 */
		String[] names = playerName.split(" ");
		names[0] = names[0].substring(0, 2);

		if (names[1].length() < 5)
			names[1] = names[1].substring(0, names[1].length());
		else
			names[1] = names[1].substring(0, 5);

		try {
			URL url = new URL("http://www.basketball-reference.com/players/"
					+ names[1].charAt(0) + "/" + names[1] + names[0]
					+ "01/gamelog/2015/");

			InputStream inputStream = url.openStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream));

			/* Store the data by parsing through the website HTML code */
			while (!reader.readLine().contains("Rk"))
				;
			stats[0][0] = "Rk";

			for (int i = 0; i < MAX_GAMES; i++) {
				int j = 0;
				if (i == 0)
					j = 1;
				for (; j < 30; j++) {
					String line = reader.readLine();
					if (line.contains("Did Not Play"))
						break;
					String[] tempLines = line.split("</");
					line = tempLines[0];
					tempLines = line.split(">");
					if (tempLines.length > 1)
						line = tempLines[tempLines.length - 1];
					else
						line = "";
					if (line.contains("<"))
						line = "";
					stats[i][j] = line;
				}
				reader.readLine();

				String checkLine;
				do {
					checkLine = reader.readLine();
				} while (!checkLine.contains("<tr")
						&& !checkLine.contains("</table"));
				if (checkLine.contains("</table")) {
					numGames = i;
					break;
				}
			}
			reader.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void displayStats(JPanel mainPanel, Container contentPane) {
		System.out.println(contentPane.getWidth());
		JLabel[][] statsLabels = new JLabel[stats.length][stats[0].length];

		contentPane.remove(mainPanel);
		Component[] tempComponents = mainPanel.getComponents();
		mainPanel = new JPanel(new GridLayout(numGames, CATEGORIES));

		for (int i = 0; i < numGames; i++) {
			for (int j = 0; j < CATEGORIES; j++) {
				if (stats[i][j] == null)
					stats[i][j] = "DNP";
				statsLabels[i][j] = new JLabel(stats[i][j]);
				mainPanel.add(statsLabels[i][j]);
			}
		}

		JScrollPane scrollPanel = new JScrollPane(mainPanel);
		contentPane.add(scrollPanel, BorderLayout.SOUTH);
		contentPane.validate();
	}
}
