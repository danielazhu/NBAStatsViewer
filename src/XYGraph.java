/**
 * This class draws the graph after being passed the data values from Player.java
 * 
 * @author Daniel Zhu
 * 
 */

import java.awt.Color;

import objectdraw.*;

public class XYGraph {

	private static final int SIDE_DISTANCE = 10;
	private static final int OVAL_SIZE = 5;
	private static final Color color = Color.BLUE;

	private FilledOval[] dataPointOvals;
	private Line[] dataPointLines;
	private int numDataPoints;

	private Text lowAxisValue;
	private Text highAxisValue;
	private Text mediumAxisValue;
	private Text titleText;

	public XYGraph(double[] dataPoints, int numDataPoints,
			DrawingCanvas canvas, String title) {
		this.numDataPoints = numDataPoints;
		dataPointOvals = new FilledOval[numDataPoints];
		dataPointLines = new Line[numDataPoints];
		double lowestVal = 0;
		double highestVal = dataPoints[0];
		for (int i = 1; i < numDataPoints; i++) {
			double tempVal = dataPoints[i];
			System.out.println(dataPoints[i]);
			if (tempVal > highestVal)
				highestVal = tempVal;
			else if (tempVal < lowestVal)
				lowestVal = tempVal;
		}

		double interpointDistance = canvas.getWidth() / (numDataPoints + 1);
		double graphYScale = ((canvas.getHeight() - 2 * SIDE_DISTANCE) * 9.0 / 10.0)
				/ highestVal;

		for (int i = 0; i < numDataPoints; i++) {
			dataPointOvals[i] = new FilledOval(interpointDistance * (i + 1),
					canvas.getHeight() - graphYScale * dataPoints[i]
							- SIDE_DISTANCE, OVAL_SIZE, OVAL_SIZE, canvas);
			dataPointOvals[i].setColor(color);
		}

		for (int i = 0; i < numDataPoints - 1; i++) {
			dataPointLines[i] = new Line(dataPointOvals[i].getX()
					+ dataPointOvals[i].getWidth() / 2,
					dataPointOvals[i].getY() + dataPointOvals[i].getHeight()
							/ 2, dataPointOvals[i + 1].getX()
							+ dataPointOvals[i + 1].getWidth() / 2,
					dataPointOvals[i + 1].getY()
							+ dataPointOvals[i + 1].getHeight() / 2, canvas);
			dataPointLines[i].setColor(color);
		}
		lowAxisValue = new Text(lowestVal, 0, canvas.getHeight() - 2
				* SIDE_DISTANCE, canvas);
		highAxisValue = new Text(highestVal, 0, canvas.getHeight()
				- graphYScale * highestVal - SIDE_DISTANCE, canvas);
		mediumAxisValue = new Text((lowestVal + highestVal) / 2, 0,
				(highAxisValue.getY() + lowAxisValue.getY()) / 2, canvas);
		titleText = new Text(title, 0, 0, canvas);
		titleText.setFontSize(24);
		titleText.moveTo(canvas.getWidth() / 2 - titleText.getWidth() / 2, 0);
	}

	public void removeGraph() {
		for (int i = 0; i < numDataPoints; i++) {
			dataPointOvals[i].removeFromCanvas();
			if (i != numDataPoints - 1)
				dataPointLines[i].removeFromCanvas();
		}

		lowAxisValue.removeFromCanvas();
		highAxisValue.removeFromCanvas();
		mediumAxisValue.removeFromCanvas();
		titleText.removeFromCanvas();
	}
}
