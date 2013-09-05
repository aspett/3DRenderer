package edgelists;

import java.awt.Color;

public class EdgeList {
	public EdgeListRow[] rows;
	public EdgeList(int maxRows) {
		rows = new EdgeListRow[maxRows+2];
		for(int i = 0; i < rows.length; i++) {
			rows[i] = new EdgeListRow(Float.POSITIVE_INFINITY, 0f, Float.NEGATIVE_INFINITY, 0f);
		}
	}
}
