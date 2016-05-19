package mapeditor;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.concurrent.locks.Lock;

import javax.swing.JFrame;
import javax.swing.JPanel;

class BufferedPanel extends JPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BufferedImage frameBuffer;
	private Graphics bufferGraphics;
	public static Lock frameBufferLock;
	
	public BufferedPanel(int w, int h)
	{
		frameBuffer = new BufferedImage(w, h, BufferedImage.TYPE_4BYTE_ABGR);
		bufferGraphics = frameBuffer.getGraphics();
	}
	
	public Graphics getGraphics()
	{
		return bufferGraphics;
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		frameBufferLock.lock();
		g.drawImage(frameBuffer, 0, 0, null);
		frameBufferLock.unlock();
	}
}

public class Window
{
	private JFrame window;
	private JPanel panel;
	
	
	public Window(int w, int h)
	{
		window = new JFrame("Editor");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panel = new BufferedPanel(w, h);
		window.add(panel);
		
		window.pack();
	}
	
	public void setVisible(boolean visible)
	{
		window.setVisible(visible);
	}
	
	
	
}
