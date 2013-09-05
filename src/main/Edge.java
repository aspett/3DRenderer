package main;

public class Edge {
	public final Vector3D v1, v2;
	public Edge(Vector3D v1, Vector3D v2) {
		this.v1 = v1;
		this.v2 = v2;
	}

	public Vector3D otherV(Vector3D v) {
		if(v == v1) return v2;
		else if (v == v2) return v1;
		else throw new IllegalArgumentException("Given vector is not part of this edge");
	}
}
