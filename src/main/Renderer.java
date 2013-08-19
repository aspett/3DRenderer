package main;

import java.awt.Color;
import java.awt.Graphics;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JPanel;


public class Renderer {
	public List<Polygon> polygon;
	public Vector3D lightSource;
	static String filename = "res/ball.txt";
	RenderFrame frame;
	public Renderer() {
		polygon = new ArrayList<Polygon>();
		if(filename != null) loadPolygon(filename);
		
		frame = new RenderFrame();
		frame.canvas.setPainter(new Painter() {
			@Override
			public void paint(Graphics g, JPanel p) {
				renderCanvas(g, p);
			}
		});
		
	}

	public static void main(String[] args) {
		Renderer r = new Renderer();
		
		System.out.println("Light source: " + r.lightSource.toString());
		
		for(Polygon p : r.polygon) {
			System.out.println(p);
		}
	}

	public void loadPolygon(String filename) {
		try {
			Scanner scan = new Scanner(new File(filename));
			if(!scan.hasNextLine()) { scan.close(); return; }
			
			lightSource = new Vector3D(scan.nextLine());
			
			while(scan.hasNextLine()) {
				polygon.add(new Polygon(scan.nextLine()));
			}
			scan.close();
		} 
		catch(FileNotFoundException e) {

		}
	}
	
	protected void renderCanvas(Graphics g, JPanel panel) {
		g.setColor(Color.black);
		g.fillRect(0, 0, panel.getWidth(), panel.getHeight());
		
	}
}
