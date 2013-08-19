package main;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class RenderFrame extends JFrame {
	
	RenderCanvas canvas;
	
	public RenderFrame() {
		super();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setLayout(new BorderLayout());
		
		canvas = new RenderCanvas();
		add(canvas, BorderLayout.CENTER);
		pack();
		
		setVisible(true);
	}
	
	public Dimension getPreferredSize() {
		return new Dimension(800,600);
	}
	@Override
	public Dimension getMinimumSize() {
		return new Dimension(800,600);
		
	}
	
}
