package main;

import java.awt.Graphics;

import javax.swing.JPanel;

public class RenderCanvas extends JPanel {
	Painter painter;
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(painter != null) painter.paint(g, this);
	}
	
	public void setPainter(Painter p) {
		painter = p;
	}
}
