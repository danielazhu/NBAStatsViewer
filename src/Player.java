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
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.*;
import java.net.*;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import objectdraw.*;

public class Player {

	private static final int CATEGORIES = 30;
	private static final int MAX_GAMES = 86;
	private String[][] stats;
	private int numGames;
	private String playerName;
	private XYGraph graph;

	/**
	 * Retrieve stats from basketball-reference.com using the player's name
	 */
	public Player(String playerName) {
		this.playerName = playerName;
	}

	/**
	 * Retrieve 2014-2015 season data for a player from basketball-reference.com
	 * 
	 * @param urlNumber
	 *            The URL takes the format for Firstname Lastname:
	 *            basketball-reference.com/L/LastnFi01 but if there are multiple
	 *            players named Joe Johnson or John Johnson, then it could be
	 *            /L/LastnFi02 so the urlNumber tries a succession of numbers
	 */
	public boolean retrieveData(int urlNumber) {
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
					+ names[1].charAt(0) + "/" + names[1] + names[0] + "0"
					+ urlNumber + "/gamelog/2015/");
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
			return true;
		} catch (MalformedURLException e) {
			return false;
		} catch (IOException e) {
			System.out.println(e.getMessage());
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	public void displayStats(NBAStatsViewer nbaStatsViewer, JPanel mainPanel,
			Container contentPane, JLabel[][] statsLabels) {

		contentPane.remove(mainPanel);
		mainPanel = new JPanel(new GridLayout(numGames, CATEGORIES + 8));

		for (int i = 0; i < numGames; i++) {
			for (int j = 0; j < CATEGORIES; j++) {
				statsLabels[i][j] = new JLabel();
				switch (j) {
				/*
				 * Don't want to display things like age, advanced statistics,
				 * games started
				 */
				case 1:
				case 3:
				case 8:
				case 28:
					continue;

				case 2: // Game Date
					if (!stats[i][j].contains("Date"))
						stats[i][j] = stats[i][j].substring(5,
								stats[i][j].length());
				default:
					if (stats[i][j] == null)
						stats[i][j] = "DNP";
					statsLabels[i][j].setText(stats[i][j]);
					statsLabels[i][j].addMouseListener(nbaStatsViewer);
					if (j > 7)
						statsLabels[i][j].setCursor(Cursor
								.getPredefinedCursor(Cursor.HAND_CURSOR));
					mainPanel.add(statsLabels[i][j]);
					break;
				}
			}
		}

		JScrollPane scrollPanel = new JScrollPane(mainPanel);
		scrollPanel.setPreferredSize(new Dimension(contentPane.getWidth(),
				contentPane.getHeight() / 2));
		contentPane.add(scrollPanel, BorderLayout.SOUTH);
		contentPane.validate();
	}

	public void displayGraph(int category, DrawingCanvas canvas) {
		if (graph != null)
			graph.removeGraph();
		double[] dataPoints = new double[stats.length];
		String[] dates = new String[stats.length];
		int ignoredDataPoints = 0;
		for (int i = 0; i < numGames; i++) {
			if (stats[i][category].equals(stats[0][category]))
				ignoredDataPoints++;
			else {
				try {
					if (stats[0][category].contains("MP"))
						dataPoints[i - ignoredDataPoints] = Double
								.parseDouble(stats[i][category].substring(0, 2));
					else
						dataPoints[i - ignoredDataPoints] = Double
								.parseDouble(stats[i][category]);
				} catch (Exception e) {
					dataPoints[i - ignoredDataPoints] = 0;
				}
				dates[i - ignoredDataPoints] = stats[i][2];
			}
		}

		graph = new XYGraph(dataPoints, dates, numGames - ignoredDataPoints,
				canvas, playerName.concat(" - " + stats[0][category]));
	}
}
