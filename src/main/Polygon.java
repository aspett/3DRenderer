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

	public Polygon(String str) {
		//Load from string by parsing.
	}
}
