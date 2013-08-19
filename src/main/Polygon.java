package main;

import java.awt.Color;


public class Polygon {
	private Vertex v1,v2,v3;
	private int r,g,b;
	//private Color color;

	public Polygon(Vertex v1, Vertex v2, Vertex v3, Color c) {
		this.v1 = v1;
		this.v2 = v2;
		this.v3 = v3;
		//this.color = c;
		this.r = c.getRed();
		this.g = c.getGreen();
		this.b = c.getBlue();
	}

	public Polygon(String line) {
		//  0  1  2  3  4  5  6  7  8 9 10 11
		// v1 v2 v3 v1 v2 v3 v1 v2 v3 r g  b
		String[] split = line.split(" ");
		try {
			this.v1 = new Vertex(	Float.parseFloat(split[0]),
									Float.parseFloat(split[1]),
									Float.parseFloat(split[2]));

			this.v2 = new Vertex(	Float.parseFloat(split[3]),
									Float.parseFloat(split[4]),
									Float.parseFloat(split[5]));

			this.v3 = new Vertex(	Float.parseFloat(split[6]),
									Float.parseFloat(split[7]),
									Float.parseFloat(split[8]));

			this.r = Integer.parseInt(split[9]);
			this.g = Integer.parseInt(split[10]);
			this.b = Integer.parseInt(split[11]);

		} catch(NumberFormatException e) {
			throw new IllegalArgumentException("Invalid polygon information");
		}
	}

	public Color getColor() {
		return new Color(r,g,b);
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

	public String toString() {
		return String.format("[(%s), (%s), (%s), %s]",v1,v2,v3,getColor().toString());
	}
}
