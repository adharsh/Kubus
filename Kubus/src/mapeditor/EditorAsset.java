package mapeditor;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public abstract class EditorAsset 
{
	public abstract String getAssetString();
	public abstract void renderAsset(Graphics g, int tileCount);
	public int getFace()
	{
		return -1;
	}
	
	protected static BufferedImage loadimg(String path)
	{
		try
		{
			return ImageIO.read(new File(path));
		}
		catch(Exception e)
		{
			return new BufferedImage(20, 20, BufferedImage.TYPE_4BYTE_ABGR);
		}
	}
}
