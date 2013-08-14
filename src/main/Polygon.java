package main;

import java.awt.Color;


public class Polygon {
	private Vertex v1,v2,v3;
	private Color color;

	public Polygon(Vertex v1, Vertex v2, Vertex v3, Color c) {
		this.v1 = v1;
		this.v2 = v2;
		this.v3 = v3;
		this.color = c;
	}

	public Polygon(String line) {
		//  0  1  2  3  4  5  6  7  8 9 10 11
		// v1 v2 v3 v1 v2 v3 v1 v2 v3 r g  b
		String[] split = line.split(" ");
		try {
			this.v1 = new Vertex(	Double.parseDouble(split[0]),
									Double.parseDouble(split[1]),
									Double.parseDouble(split[2]));

			this.v2 = new Vertex(	Double.parseDouble(split[3]),
									Double.parseDouble(split[4]),
									Double.parseDouble(split[5]));

			this.v3 = new Vertex(	Double.parseDouble(split[6]),
									Double.parseDouble(split[7]),
									Double.parseDouble(split[8]));

			this.color = new Color(	Integer.parseInt(split[9]),
									Integer.parseInt(split[10]),
									Integer.parseInt(split[11]));

		} catch(NumberFormatException e) {
			throw new IllegalArgumentException("Invalid polygon information");
		}
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Vertex getV1() {
		return v1;
	}

	public Vertex getV2() {
		return v2;
	}

	public Vertex getV3() {
		return v3;
	}
}
