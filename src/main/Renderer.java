package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Renderer {
	public List<Polygon> polygon;
	public Vector3D lightSource;
	static String filename = "res/ball.txt";
	public Renderer() {
		polygon = new ArrayList<Polygon>();
		if(filename != null) loadPolygon(filename);
	}

	public static void main(String[] args) {
		Renderer r = new Renderer();
		for(Polygon p : r.polygon) {
			System.out.println(p);
		}
	}

	public void loadPolygon(String filename) {
		try {
		Scanner scan = new Scanner(new File(filename));
		if(!scan.hasNextLine()) return;
		lightSource = new Vector3D(scan.nextLine());
		while(scan.hasNextLine()) {
			polygon.add(new Polygon(scan.nextLine()));
		}
		} catch(FileNotFoundException e) {

		}
	}
}
