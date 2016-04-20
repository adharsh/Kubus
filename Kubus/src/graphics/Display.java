package graphics;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import javax.swing.JFrame;

public class Display extends Canvas
{
	private static final long serialVersionUID = 4053475848532572725L;
	/**
	 * 
	 */
	
	private JFrame frame;
	private Renderer frameBuffer;
	private BufferedImage displayImage;
	private byte[] displayComponents;
	private BufferStrategy strat;
	private Graphics g;
	
	public Display(int w, int h, String title)
	{
		setPreferredSize(new Dimension(w, h));

		frameBuffer = new Renderer(w, h);
		displayImage = new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR);
		
		displayComponents = ((DataBufferByte)displayImage.getRaster().getDataBuffer()).getData();
		
		
		frame = new JFrame(title);
		frame.add(this);
		frame.pack();
		frame.setLocationRelativeTo(null);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		
		createBufferStrategy(1);
		strat = getBufferStrategy();
		g = strat.getDrawGraphics();
	}
	
	public void setVisible(boolean v)
	{
		frame.setVisible(v);
	}
	
	public Renderer getFrameBuffer()
	{
		return frameBuffer;
	}
	
	public void swap()
	{
		frameBuffer.getByteArrayBGR(displayComponents);
		g.drawImage(displayImage, 0, 0, frameBuffer.getWidth(), frameBuffer.getHeight(), null);
		strat.show();
	}
}
