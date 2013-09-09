package main;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

public class RenderCanvas extends JPanel implements MouseListener, MouseMotionListener{
	Painter painter;
	private int originalX;
	private int originalY;

	public RenderCanvas() {
		addMouseListener(this);
		addMouseMotionListener(this);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(painter != null) painter.paint(g, this);
	}

	public void setPainter(Painter p) {
		painter = p;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		Renderer.yDrag = e.getY() - originalY;
		Renderer.xDrag = e.getX() - originalX;
		repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		Renderer.isRotating = false;
		this.originalX = e.getX();
		this.originalY = e.getY();

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
