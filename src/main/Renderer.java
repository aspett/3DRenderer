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
	static String filename = "res/monkey.txt";
	boolean adjusted = false;
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
		PaintTimer t = new PaintTimer(r.frame);
		
		System.out.println("Light source: " + r.lightSource.toString());
		for(Polygon p : r.polygons) {
			
			System.out.println(p);
		}
		r.adjustPolygonForWindow();
		t.start();
		
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
		if(!adjusted) return;
		Graphics2D g = (Graphics2D) gr;
		g.setColor(Color.black);
		g.fillRect(0, 0, panel.getWidth(), panel.getHeight());
		g.setColor(Color.red);
		
		
		
		//System.out.printf("Canvas height: %d, bound height: %f offset: %f\n", frame.canvas.getHeight(), height, centerYoffset);

		for(Polygon p : polygons) {
			if(!p.isHidden()) {
				g.setColor(Color.green);
			}else 
				g.setColor(Color.red);

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
	
	protected void adjustPolygonForWindow() {
		getBounds();
		Transform rotate = Transform.newXRotation(1f);
		rotate.applyTransform(polygons);
		getBounds();
		
		float windowXratio = frame.canvas.getWidth()/ bounds.width ;
		float windowYRatio = frame.canvas.getHeight() / bounds.height;
		float scaleRatio = Math.min(windowXratio, windowYRatio);
		Transform scale = Transform.newScale(scaleRatio, scaleRatio, scaleRatio);
		scale.applyTransform(polygons);
		getBounds();
		float width = bounds.width;
		float height = bounds.height;
		float centerXoffset = (frame.canvas.getWidth() - width)/2;
		float centerYoffset = (frame.canvas.getHeight() - height)/2;
		Transform trans = Transform.newTranslation(0-bounds.x + centerXoffset, 0-bounds.y, 0);
		//Transform trans = Transform.newTranslation(0, 0, 0);
		System.out.printf("%d - %f = %f\n", frame.canvas.getWidth(), width, (float)frame.canvas.getWidth()-width);
		System.out.println(bounds + " Window: ["+frame.canvas.getWidth()+","+frame.canvas.getHeight()+"]");
		System.out.printf("0-bounds: %f | %f\ncenterXoff: %f, centerYoff: %f\n", 0-bounds.x, 0-bounds.y, centerXoffset, centerYoffset);
		float left = 0;
		boolean reset = true;
		for(Polygon p : polygons) {
			if(p.v1.x < left || reset) { left = p.v1.x; reset = false; }
			if(p.v2.x < left || reset) { left = p.v2.x; reset = false; }
			if(p.v3.x < left || reset) { left = p.v3.x; reset = false; }
		}
		System.out.println(left);
		trans.applyTransform(polygons);
		getBounds();
		System.out.println(bounds);
		adjusted = true;
		
	}
}
