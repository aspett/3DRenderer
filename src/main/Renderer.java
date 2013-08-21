package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JPanel;


public class Renderer {
	public List<Polygon> polygons;
	public Vector3D lightSource;
	static String filename = "res/ball.txt";
	RenderFrame frame;
	public Renderer() {
		polygons = new ArrayList<Polygon>();
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
		
		for(Polygon p : r.polygons) {
			System.out.println(p);
		}
	}

	public void loadPolygon(String filename) {
		try {
			Scanner scan = new Scanner(new File(filename));
			if(!scan.hasNextLine()) { scan.close(); return; }
			
			lightSource = new Vector3D(scan.nextLine());
			
			while(scan.hasNextLine()) {
				polygons.add(new Polygon(scan.nextLine()));
			}
			scan.close();
		} 
		catch(FileNotFoundException e) {

		}
	}
	
	protected void renderCanvas(Graphics gr, JPanel panel) {
		Graphics2D g = (Graphics2D) gr;
		g.setColor(Color.black);
		g.fillRect(0, 0, panel.getWidth(), panel.getHeight());
		g.setColor(Color.red);
		Transform rotate = Transform.newYRotation(4);
		Transform trans = Transform.newTranslation(100, 0, 0);
		rotate = trans.compose(rotate);
		for(Polygon p : polygons) {
			
			Vector3D v1 = rotate.multiply(p.getV1()), v2 = rotate.multiply(p.getV2()), v3 = rotate.multiply(p.getV3());
			g.drawLine((int)v1.x, (int)v1.y, (int)v2.x, (int)v2.y);
			g.drawLine((int)v2.x, (int)v2.y, (int)v3.x, (int)v3.y);
			g.drawLine((int)v3.x, (int)v3.y, (int)v1.x, (int)v1.y);
		}
		
	}
}
