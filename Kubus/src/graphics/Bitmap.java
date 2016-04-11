package graphics;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Bitmap 
{
	private final int w;
	private final int h;
	private final byte[] pixels;

	public Bitmap(int w, int h)
	{
		this.w = w;
		this.h= h;
		this.pixels = new byte[w * h * 4];
	}
	
	public Bitmap(String fileName) throws IOException
	{
		BufferedImage i = ImageIO.read(new File(fileName));
		w = i.getWidth();
		h = i.getHeight();
		
		int[] pixelInt = new int[w * h];
		i.getRGB(0,  0, w, h, pixelInt, 0, w);
		pixels = new byte[w * h * 4];
		for(int a=0;a<w * h;a++)
		{
			int pixel = pixelInt[a];

			pixels[a * 4] = (byte)((pixel >> 24) & 0xFF);
			pixels[a * 4 + 1] = (byte)((pixel) & 0xFF);
			pixels[a * 4 + 2] = (byte)((pixel >> 8) & 0xFF);
			pixels[a * 4 + 3] = (byte)((pixel >> 16) & 0xFF);
		}
	}

	public void fill(byte bColor)
	{
		for(int a=0;a<w*h*4;a++)
		{
			pixels[a] = bColor;
		}
	}

	public int getWidth()
	{
		return w;
	}
	
	public int getHeight()
	{
		return h;
	}
	
	public byte getByte(int idx)
	{
		if(idx < 0 || idx >= pixels.length)
			return (byte)0x00;
		return pixels[idx];
	}
	
	public void setPixel(int x, int y, byte a, byte b, byte g, byte r)
	{
		int idx = (x + (y * w)) * 4;
		pixels[idx] = a;
		pixels[idx + 1] = b;
		pixels[idx + 2] = g;
		pixels[idx + 3] = r;
	}
	
	public void copyPixel(int destX, int destY, int srcX, int srcY, Bitmap src)
	{
		int idx = (destX + (destY * w)) * 4;
		int srcIdx = (srcX + srcY * src.getWidth()) * 4;
		
		pixels[idx] = src.getByte(srcIdx);
		pixels[idx + 1] = src.getByte(srcIdx + 1);
		pixels[idx + 2] = src.getByte(srcIdx + 2);
		pixels[idx + 3] = src.getByte(srcIdx + 3);
	}

	public void setPixel(int x, int y, int abgr)
	{
		int idx = (x + (y * w)) * 4;
		pixels[idx] = (byte)(abgr >> 24);
		pixels[idx + 1] = (byte)(abgr >> 16);
		pixels[idx + 2] = (byte)(abgr >> 8);
		pixels[idx + 3] = (byte)(abgr);
	}

	//always opaque
	public void setPixelBGR(int x, int y, int bgr)
	{
		setPixel(x, y, bgr | 0xFF000000);
	}

	public void getIntArray(int[] arr)
	{
		for(int a=0;a<w*h;a++)
		{
			arr[a] ^= arr[a];
			arr[a] |= (pixels[a * 4]) << 24;
			arr[a] |= (pixels[a * 4 + 1]) << 16;
			arr[a] |= (pixels[a * 4 + 2]) << 8;
			arr[a] |= (pixels[a * 4 + 3]);
		}
	}

	//arr out is BGR
	public void getByteArrayBGR(byte[] arr)
	{
		for(int a=0;a<w*h;a++)
		{
			arr[a * 3] = pixels[a * 4 + 1];
			arr[a * 3 + 1] = pixels[a * 4 + 2];
			arr[a * 3 + 2] = pixels[a * 4 + 3];
		}
	}
}
