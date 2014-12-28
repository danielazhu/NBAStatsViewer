/**
 * This class draws the graph after being passed the data values from Player.java
 * 
 * @author Daniel Zhu
 * 
 */

import java.awt.Color;

import objectdraw.*;

public class XYGraph {

	private static final int SIDE_DISTANCE = 20;
	private static final int DATE_DISTANCE = 15;
	private static final int OVAL_SIZE = 5;
	private static final Color COLOR = Color.BLUE;
	private static final Color AXIS_COLOR = new Color(
			Color.LIGHT_GRAY.getRed() + 30, Color.LIGHT_GRAY.getBlue() + 30,
			Color.LIGHT_GRAY.getGreen() + 30);

	private FilledOval[] dataPointOvals;
	private Line[] dataPointLines;
	private Text[] dateTexts;

	private int numDataPoints;

	private Text lowAxisValue;
	private Text highAxisValue;
	private Text mediumAxisValue;
	private Line lowAxisValueLine;
	private Line highAxisValueLine;
	private Line mediumAxisValueLine;

	private Text titleText;

	public XYGraph(double[] dataPoints, String[] dates, int numDataPoints,
			DrawingCanvas canvas, String title) {
		this.numDataPoints = numDataPoints;
		dataPointOvals = new FilledOval[numDataPoints];
		dataPointLines = new Line[numDataPoints];
		dateTexts = new Text[numDataPoints];
		double lowestVal = 0;
		double highestVal = dataPoints[0];
		for (int i = 1; i < numDataPoints; i++) {
			double tempVal = dataPoints[i];
			if (tempVal > highestVal)
				highestVal = tempVal;
			else if (tempVal < lowestVal)
				lowestVal = tempVal;
		}

		double interpointDistance = canvas.getWidth() / (numDataPoints + 1);
		double graphYScale = ((canvas.getHeight() - 2 * SIDE_DISTANCE) * 9.0 / 10.0)
				/ highestVal;

		/* Draw the data points and the date texts */
		for (int i = 0; i < numDataPoints; i++) {
			dataPointOvals[i] = new FilledOval(interpointDistance * (i + 1),
					canvas.getHeight() - graphYScale * dataPoints[i]
							- SIDE_DISTANCE, OVAL_SIZE, OVAL_SIZE, canvas);
			dataPointOvals[i].move(0, -dataPointOvals[i].getHeight() / 2);
			dataPointOvals[i].setColor(COLOR);
			dateTexts[i] = new Text(dates[i], 0, canvas.getHeight()
					- DATE_DISTANCE, canvas);
			dateTexts[i].moveTo(dataPointOvals[i].getX() + OVAL_SIZE / 2
					- dateTexts[i].getWidth() / 2, dateTexts[i].getY());
		}

		/* Draw the lines connecting the data points */
		for (int i = 0; i < numDataPoints - 1; i++) {
			dataPointLines[i] = new Line(dataPointOvals[i].getX()
					+ dataPointOvals[i].getWidth() / 2,
					dataPointOvals[i].getY() + dataPointOvals[i].getHeight()
							/ 2, dataPointOvals[i + 1].getX()
							+ dataPointOvals[i + 1].getWidth() / 2,
					dataPointOvals[i + 1].getY()
							+ dataPointOvals[i + 1].getHeight() / 2, canvas);
			dataPointLines[i].setColor(COLOR);
		}
		lowAxisValue = new Text(lowestVal, 0, canvas.getHeight()
				- SIDE_DISTANCE, canvas);
		lowAxisValue.move(0, -lowAxisValue.getHeight() / 2);
		highAxisValue = new Text(highestVal, 0, canvas.getHeight()
				- graphYScale * highestVal - SIDE_DISTANCE, canvas);
		highAxisValue.move(0, -highAxisValue.getHeight() / 2);
		mediumAxisValue = new Text((lowestVal + highestVal) / 2, 0,
				(highAxisValue.getY() + lowAxisValue.getY()) / 2, canvas);

		lowAxisValueLine = new Line(lowAxisValue.getX()
				+ lowAxisValue.getWidth() * 2, lowAxisValue.getY()
				+ lowAxisValue.getHeight() / 2, canvas.getWidth(),
				lowAxisValue.getY() + lowAxisValue.getHeight() / 2, canvas);
		mediumAxisValueLine = new Line(mediumAxisValue.getX()
				+ mediumAxisValue.getWidth() * 2, mediumAxisValue.getY()
				+ mediumAxisValue.getHeight() / 2, canvas.getWidth(),
				mediumAxisValue.getY() + mediumAxisValue.getHeight() / 2,
				canvas);
		highAxisValueLine = new Line(highAxisValue.getX()
				+ highAxisValue.getWidth() * 2, highAxisValue.getY()
				+ highAxisValue.getHeight() / 2, canvas.getWidth(),
				highAxisValue.getY() + highAxisValue.getHeight() / 2, canvas);
		lowAxisValueLine.setColor(AXIS_COLOR);
		mediumAxisValueLine.setColor(AXIS_COLOR);
		highAxisValueLine.setColor(AXIS_COLOR);
		lowAxisValueLine.sendToBack();
		mediumAxisValueLine.sendToBack();
		highAxisValueLine.sendToBack();

		titleText = new Text(title, 0, 0, canvas);
		titleText.setFontSize(24);
		titleText.moveTo(canvas.getWidth() / 2 - titleText.getWidth() / 2, 0);
	}

	public void removeGraph() {
		for (int i = 0; i < numDataPoints; i++) {
			dataPointOvals[i].removeFromCanvas();
			dateTexts[i].removeFromCanvas();
			if (i != numDataPoints - 1)
				dataPointLines[i].removeFromCanvas();
		}

		lowAxisValue.removeFromCanvas();
		highAxisValue.removeFromCanvas();
		mediumAxisValue.removeFromCanvas();
		lowAxisValueLine.removeFromCanvas();
		mediumAxisValueLine.removeFromCanvas();
		highAxisValueLine.removeFromCanvas();
		titleText.removeFromCanvas();
	}
}
