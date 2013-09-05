package main;

public class PaintTimer extends Thread{
	RenderFrame frame;
	public PaintTimer(RenderFrame f) {
		frame = f;
	}

	public void run() {
		while(true) {
		try {
			Thread.sleep(10);
			frame.repaint();
		} catch(InterruptedException e) { }
		}
	}
}
