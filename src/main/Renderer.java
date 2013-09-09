package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JPanel;

import edgelists.EdgeList;


public class Renderer {

	public static float lightIntensity = 1f;
	public static float ambience = 0.5f;
	static String filename = "res/monkey.txt";

	public List<Polygon> polygons;
	public Vector3D lightSource;
	public Rectangle2D.Float bounds;

	public static boolean adjusted = false;
	RenderFrame frame;
	private float yRotation = 0.02f;
	private float xRotation = 0.00f;

	public static float scale = 1f;
	public static int yDrag = 0;
	public static int xDrag = 0;



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

		//System.out.println("Light source: " + r.lightSource.toString());
		r.adjustPolygonForWindow();
		t.start();
		t.run();

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
			System.out.printf("File, %s, does not exist!", filename);
			System.exit(0);
		}
	}

	protected void renderCanvas(Graphics gr, JPanel panel) {
		int windowWidth = frame.canvas.getWidth();
		int windowHeight = frame.canvas.getHeight();

		/*if(xDrag != 0) { yRotation = ((float)xDrag) / -500; xDrag = 0; }
		if(yDrag != 0) { xRotation = ((float)yDrag) / -500; xDrag = 0; }*/

		//if(!adjusted) return;
		adjustPolygonForWindow();

		/*xRotation = 0f;
		yRotation = 0f;*/

		Graphics2D g = (Graphics2D) gr;
		g.setColor(Color.black);
		g.fillRect(0, 0, panel.getWidth(), panel.getHeight());
		g.setColor(Color.red);



		////System.out.printf("Canvas height: %d, bound height: %f offset: %f\n", frame.canvas.getHeight(), height, centerYoffset);

		Color[][] zBufferC = new Color[windowWidth][windowHeight];
		for(int i = 0; i < zBufferC.length; i++) {
			for(int j = 0; j < zBufferC[0].length; j++) {
				zBufferC[i][j] = new Color(0,0,0);
			}
		}
		Float[][] zBufferD = new Float[windowWidth][windowHeight];
		for(int i = 0; i < zBufferD.length; i++) {
			for(int j = 0; j < zBufferD[0].length; j++) {
				zBufferD[i][j] = Float.POSITIVE_INFINITY;
			}
		}

		//g.translate(0,Renderer.yTranslation);

		for(Polygon p : polygons) {
			if(!p.isHidden()) continue;
			EdgeList el = p.computeEdgeList();
			Color shading = p.getShadedColor(lightSource);
			for(int y = 0; y < el.rows.length-1; y++) {
				//System.out.println(y);
				int x = Math.round(el.rows[y].lx);

				if(y < 0 || y >= windowHeight) continue;
				float z = el.rows[y].lz;
				float mz = (el.rows[y].rz - el.rows[y].lz) / (el.rows[y].rx - el.rows[y].lx);
				//System.out.println(el.rows[y].rx);
				//if(el.rows[y].rx == Float.NEGATIVE_INFINITY) System.out.println("NEGINF");
				while(x <= Math.round(el.rows[y].rx) && el.rows[y].rx >= 0) {
					if(x < 0 || x >= windowWidth) {x++; continue;}
					if(z < zBufferD[x][y]) {
						zBufferD[x][y] = z;
						zBufferC[x][y] = shading;
					}
					x++;
					z = z+mz;
				}
			}
		}


		BufferedImage image = new BufferedImage(zBufferC.length, zBufferC[0].length, BufferedImage.TYPE_INT_RGB);
		for(int i = 0; i < zBufferC.length; i++) {
			for(int j = 0; j < zBufferC[0].length; j++) {
				image.setRGB(i, j, zBufferC[i][j].getRGB());
				/*g.setColor(zBufferC[i][j]);
				g.drawRect(i, j, 1, 1);*/
			}
		}
		g.drawImage(image,0,0, frame);

























/*
		for(Polygon p : polygons) {
			//p.computeEdgeList();
			if(!p.isHidden()) {
				g.setColor(p.getShadedColor(lightSource));
			}else
				g.setColor(new Color(200,50,0,25));

			g.drawLine((int)p.v1.x, (int)p.v1.y, (int)p.v2.x, (int)p.v2.y);
			g.drawLine((int)p.v2.x, (int)p.v2.y, (int)p.v3.x, (int)p.v3.y);
			g.drawLine((int)p.v3.x, (int)p.v3.y, (int)p.v1.x, (int)p.v1.y);




		}/*
		getBounds();
		g.drawRect((int)bounds.x, (int)bounds.y, (int)bounds.width, (int)bounds.height);*/
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
		//this.rotation = this.rotation  + 0.005f;
		//if(this.yRotation != 0f || this.xRotation  != 0f) {
			Transform yRotate = Transform.newYRotation(this.yRotation);
			Transform xRotate = Transform.newXRotation(this.xRotation);
			Transform rotate = yRotate.compose(xRotate);
			rotate.applyTransform(polygons);
			lightSource = rotate.multiply(lightSource);
			//getBounds();
		//}

		float windowXratio = frame.canvas.getWidth()/ bounds.width ;
		float windowYRatio = frame.canvas.getHeight() / bounds.height;
		float scaleRatio = Math.min(windowXratio, windowYRatio);
		scaleRatio = scaleRatio * Math.max(Renderer.scale, 0.05f);
		Transform scale = Transform.newScale(scaleRatio, scaleRatio, scaleRatio);
		scale.applyTransform(polygons);
		getBounds();
		float width = bounds.width;
		float height = bounds.height;
		float centerXoffset = (frame.canvas.getWidth() - width)/2;
		float centerYoffset = (frame.canvas.getHeight() - height)/2;
		Transform trans = Transform.newTranslation(0-bounds.x + centerXoffset, 0-bounds.y + centerYoffset, 0);
		trans.applyTransform(polygons);

		//Transform trans = Transform.newTranslation(0, 0, 0);
		//System.out.printf("%d - %f = %f\n", frame.canvas.getWidth(), width, (float)frame.canvas.getWidth()-width);
		//System.out.println(bounds + " Window: ["+frame.canvas.getWidth()+","+frame.canvas.getHeight()+"]");
		//System.out.printf("0-bounds: %f | %f\ncenterXoff: %f, centerYoff: %f\n", 0-bounds.x, 0-bounds.y, centerXoffset, centerYoffset);
		/*float left = 0;
		boolean reset = true;
		for(Polygon p : polygons) {
			if(p.v1.x < left || reset) { left = p.v1.x; reset = false; }
			if(p.v2.x < left || reset) { left = p.v2.x; reset = false; }
			if(p.v3.x < left || reset) { left = p.v3.x; reset = false; }
		}
		//System.out.println(left);*/

		//getBounds();
		//System.out.println(bounds);
		if(!adjusted) Renderer.scale = 1f;
		adjusted = true;

	}
}
