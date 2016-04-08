package graphics;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import javax.swing.JFrame;

public class Display extends Canvas
{
	private static final long serialVersionUID = 1L;
	
	private JFrame frame;
	private Bitmap frameBuffer;
	private BufferedImage displayBuffer;
	private BufferStrategy bufferStrategy;
	private byte[] bufferData;
	
	public Display(int w, int h, String title)
	{
		this.setPreferredSize(new Dimension(w, h));
		frame = new JFrame(title);
		frame.add(this);
		frame.pack();
		
		frameBuffer = new Bitmap(w, h);
		
		createBufferStrategy(1);
		bufferData = ((DataBufferByte)displayBuffer.getRaster().getDataBuffer()).getData();
		bufferStrategy = this.getBufferStrategy();
		
	}
	
	public void swap()
	{
		frameBuffer.pixels(bufferData);
		
		bufferStrategy.show();
	}
}
