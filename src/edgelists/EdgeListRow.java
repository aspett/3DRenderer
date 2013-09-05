package edgelists;

import java.awt.Color;

public class EdgeListRow {
	public Float lx;
	public Float lz;
	Color lc;

	public Float rx;
	public Float rz;
	Color rc;
	public EdgeListRow(Float lx, Float lz, Float rx, Float rz) {
		this.lz = lz;
		//this.lc = lc;
		this.rz = rz;
		//this.rc = rc;
		this.lx = lx;
		this.rx = rx;

	}

	public void insertValues(Float x, Float z) {
		if(x < lx) { lx = x; lz = z; }
		if(x > rx) { rx = x; rz = z; }
	}
}
