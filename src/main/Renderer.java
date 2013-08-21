package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JPanel;


public class Renderer {
	public List<Polygon> polygons;
	public Vector3D lightSource;
	public Rectangle2D.Float bounds;
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
		getBounds();
		//Transform rotate = Transform.newYRotation(4);
		
		float windowXratio = frame.canvas.getWidth()/ bounds.width ;
		float windowYRatio = frame.canvas.getHeight() / bounds.height;
		float scaleRatio = Math.min(windowXratio, windowYRatio);
		Transform scale = Transform.newScale(scaleRatio, scaleRatio, scaleRatio);
		Transform rotate = scale;
		float width = bounds.width * scaleRatio;
		float height = bounds.height * scaleRatio;
		float centerXoffset = (frame.canvas.getWidth() - width) / 2;
		float centerYoffset = (frame.canvas.getHeight() - height) / 2;
		Transform trans = Transform.newTranslation(0-bounds.x + centerXoffset, 0-bounds.y + centerYoffset, 0);
		rotate = trans.compose(rotate);
		for(Polygon p : polygons) {
			
			//Vector3D v1 = rotate.multiply(p.getV1()), v2 = rotate.multiply(p.getV2()), v3 = rotate.multiply(p.getV3());
			p.v1 = rotate.multiply(p.getV1());
			p.v2 = rotate.multiply(p.getV2());
			p.v3 = rotate.multiply(p.getV3());
			g.drawLine((int)p.v1.x, (int)p.v1.y, (int)p.v2.x, (int)p.v2.y);
			g.drawLine((int)p.v2.x, (int)p.v2.y, (int)p.v3.x, (int)p.v3.y);
			g.drawLine((int)p.v3.x, (int)p.v3.y, (int)p.v1.x, (int)p.v1.y);
		}
		getBounds();
		g.drawRect((int)bounds.x, (int)bounds.y, (int)bounds.width, (int)bounds.height);
	}
	
	protected Rectangle2D.Float getBounds() {
		Float left=Float.POSITIVE_INFINITY, top=Float.POSITIVE_INFINITY, right=Float.NEGATIVE_INFINITY, bottom=Float.NEGATIVE_INFINITY;

		for(Polygon p : polygons) {
			Rectangle2D.Float polyBound = p.getBounds();
			if(polyBound.x < left) left = polyBound.x;
			if(polyBound.y < top) top = polyBound.y;
			float polyRight = polyBound.x + polyBound.width;
			float polyBottom = polyBound.y + polyBound.height;
			if(polyRight > right) right = polyRight;
			if(polyBottom > bottom) bottom = polyBottom;
		}
		
		this.bounds = new Rectangle2D.Float(left, top, right-left, bottom-top);
		return this.bounds;
	}
}
