package mapeditor;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class EditorPlayerStart extends EditorAsset
{
	private int x;
	private int y;
	private static BufferedImage playerImage;
	
	static
	{
		playerImage = loadimg("res/human.jpg");
	}
	
	public EditorPlayerStart(int x, int y)
	{
		this.x = x;
		this.y = y;
	}

	@Override
	public String getAssetString() 
	{
		return "<PlayerStartIndex> " + x + "," + y + " </end>";
	}

	@Override
	public void renderAsset(Graphics g, int tileCount) 
	{
		int tileSize = 650 / tileCount;
		int xLoc = 105 + (x * tileSize);
		int yLoc = 105 + (y * tileSize);
		
		g.drawImage(playerImage, xLoc, yLoc, tileSize, tileSize, null);
	}
	@Override
	public int getFace()
	{
		return 1;
	}
	
}
