package mapeditor;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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
	public static Lock frameBufferLock = new ReentrantLock();
	
	public BufferedPanel(int w, int h)
	{
		this.setPreferredSize(new Dimension(w, h));
		frameBuffer = new BufferedImage(w, h, BufferedImage.TYPE_4BYTE_ABGR);
		bufferGraphics = frameBuffer.getGraphics();
	}
	
	public Graphics getBufferGraphics()
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
	private BufferedPanel panel;
	
	
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
	
	public Graphics getBufferGraphics()
	{
		return panel.getBufferGraphics();
	}
	
	public void repaint()
	{
		panel.repaint();
	}
	
	public void addKeyListener(KeyListener k)
	{
		window.addKeyListener(k);
	}
	
}
