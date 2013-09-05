package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

@SuppressWarnings("serial")
public class RenderFrame extends JFrame {

	RenderCanvas canvas;

	public RenderFrame() {
		super();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setLayout(new BorderLayout());

		JPanel commandPanel = new JPanel();
		commandPanel.setPreferredSize(new Dimension(800,60));
		commandPanel.setBackground(new Color(50,10,10));

		add(commandPanel, BorderLayout.NORTH);
		addSliders(commandPanel);

		canvas = new RenderCanvas();
		add(canvas, BorderLayout.CENTER);
		pack();

		setVisible(true);
	}

	private ChangeListener ambientLightListener() {
		return new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider) e.getSource();
				Renderer.ambience = ((float) source.getValue()) / 10;

			}
		};
	}
	private ChangeListener scaleListener() {
		return new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider) e.getSource();
				Renderer.scale = ((float) source.getValue()) / 100;

			}
		};
	}

	private void addSliders(JPanel commandPanel) {
		JSlider ambientLightSlider = new JSlider(0, 15, 5);
		ambientLightSlider.setMajorTickSpacing(5);
		ambientLightSlider.setMinorTickSpacing(1);
		ambientLightSlider.setPaintTicks(true);
		ambientLightSlider.setPaintLabels(true);
		ambientLightSlider.setBackground(commandPanel.getBackground());
		ambientLightSlider.setForeground(Color.white);
		ambientLightSlider.addChangeListener(ambientLightListener());
		commandPanel.add(ambientLightSlider);

		JSlider scaleSlider = new JSlider(0, 400, 100);
		scaleSlider.setMajorTickSpacing(100);
		scaleSlider.setMinorTickSpacing(25);
		scaleSlider.setPaintTicks(true);
		scaleSlider.setPaintLabels(true);
		scaleSlider.setBackground(commandPanel.getBackground());
		scaleSlider.setForeground(Color.white);
		scaleSlider.addChangeListener(scaleListener());
		commandPanel.add(scaleSlider);

	}

	public Dimension getPreferredSize() {
		return new Dimension(800,600);
	}
	@Override
	public Dimension getMinimumSize() {
		return new Dimension(800,600);

	}

}
