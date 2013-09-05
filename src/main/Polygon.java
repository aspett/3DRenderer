package main;

import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import edgelists.EdgeList;
import edgelists.EdgeListRow;



@SuppressWarnings("serial")
public class Polygon {
	public Vector3D v1,v2,v3;
	private int r,g,b;
	private Color shadedColor;
	private List<Edge> edgeCache;

	public Polygon(Vector3D v1, Vector3D v2, Vector3D v3, Color c) {
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
			this.v1 = new Vector3D(	Float.parseFloat(split[0]),
									Float.parseFloat(split[1]),
									Float.parseFloat(split[2]));

			this.v2 = new Vector3D(	Float.parseFloat(split[3]),
									Float.parseFloat(split[4]),
									Float.parseFloat(split[5]));

			this.v3 = new Vector3D(	Float.parseFloat(split[6]),
									Float.parseFloat(split[7]),
									Float.parseFloat(split[8]));

			this.r = Integer.parseInt(split[9]);
			this.g = Integer.parseInt(split[10]);
			this.b = Integer.parseInt(split[11]);

		} catch(NumberFormatException e) {
			throw new IllegalArgumentException("Invalid polygon information");
		}
	}

	public Rectangle2D.Float getBounds() {
		float left = Math.min(Math.min(v1.x, v2.x), v3.x);
		float top = Math.min(Math.min(v1.y, v2.y), v3.y);
		float right = Math.max(Math.max(v1.x,v2.x), v3.x);
		float bottom = Math.max(Math.max(v1.y, v2.y), v3.y);
		return new Rectangle2D.Float(left, top, right-left, bottom-top);
	}

	public Color getColor() {
		return new Color(r,g,b);
	}

	public Vector3D getV1() {
		return v1;
	}

	public Vector3D getV2() {
		return v2;
	}

	public Vector3D getV3() {
		return v3;
	}

	public String toString() {
		return String.format("[(%s), (%s), (%s), %s]",v1,v2,v3,getColor().toString());
	}

	public boolean isHidden() {
		Vector3D edge1 = new Vector3D(v2.x-v1.x, v2.y-v1.y, v2.z-v1.z);
		Vector3D edge2 = new Vector3D(v3.x-v2.x, v3.y-v2.y, v3.z-v2.z);
		Vector3D cross = edge1.crossProduct(edge2);
		if(cross.z < 0) return true;
		return false;
	}

	public Vector3D getSurfaceNormal() {
		Vector3D edge1 = new Vector3D(v2.x-v1.x, v2.y-v1.y, v2.z-v1.z);
		Vector3D edge2 = new Vector3D(v3.x-v2.x, v3.y-v2.y, v3.z-v2.z);
		Vector3D cross = edge1.crossProduct(edge2);
		return cross.unitVector();
	}

	private void calculateShading(Vector3D lightingDirection) {
		float costh = this.getSurfaceNormal().dotProduct(lightingDirection);
		int red = Math.max(Math.min(applyShading(this.r, costh), 255), 0);
		int green = Math.max(Math.min(applyShading(this.g,costh), 255), 0);
		int blue = Math.max(Math.min(applyShading(this.b,costh), 255), 0);
		//System.out.printf("%d %d %d\n", red, green, blue);
		this.shadedColor = new Color(red,green,blue);
	}

	public Color getShadedColor(Vector3D lightingDirection) {
		calculateShading(lightingDirection);
		return this.shadedColor;
	}

	private int applyShading(int colorComponent, float costh) {
		return (int) ((Renderer.ambience + Renderer.lightIntensity * costh) * colorComponent);
	}

	public List<Edge> getEdges() {
		//if(this.edgeCache != null) return this.edgeCache;

		List<Edge> edges = new ArrayList<Edge>();
		edges.add(new Edge(v1,v2));
		edges.add(new Edge(v1,v3));
		edges.add(new Edge(v2,v3));
		this.edgeCache = edges;
		return edges;
	}

	public int minY() {
		return (int) Math.min(v1.y, Math.min(v2.y, v3.y));
	}
	public int maxY() {
		return (int) Math.ceil(Math.max(v1.y, Math.max(v2.y, v3.y)));
	}

	public EdgeList computeEdgeList() {
		//int miny = minY();
		int maxy = maxY();
		EdgeList el = new EdgeList(maxy);
		for(Edge edge : getEdges()) {
			Vector3D va = edge.v1.compareTo(edge.v2) <= 0 ? edge.v1 : edge.v2;
			Vector3D vb = edge.otherV(va);
			float mx = (vb.x-va.x)/(vb.y-va.y);
			float mz = (vb.z-va.z)/(vb.y-va.y);
			float x = va.x, z = va.z;
			int i = Math.round(va.y), maxi = Math.round(vb.y);
			while (i < maxi) {
				EdgeListRow row = el.rows[i];
				row.insertValues(x, z);
				i++;
				x = x+mx;
				z = z+mz;
			}
			//el.rows[maxi].insertValues(vb.x, vb.y);


		}
		return el;
	}


}
