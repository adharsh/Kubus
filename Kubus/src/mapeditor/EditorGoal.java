package mapeditor;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class EditorGoal extends EditorAsset
{
	private int x;
	private int y;
	private int face;
	private static BufferedImage goalImage;
	
	static
	{
		goalImage = loadimg("res/goal.png");
	}
	
	public EditorGoal(int x, int y, int face)
	{
		this.x = x;
		this.y = y;
		this.face = face;
	}

	@Override
	public String getAssetString() 
	{
		return "<Goal> " + x + "," + y + "," + face + " </end>";
	}

	@Override
	public void renderAsset(Graphics g, int tileCount) 
	{

		int tileSize = 700 / tileCount;
		int xLoc = 100 + (tileSize * x);
		int yLoc = 100 + (tileSize * y);
		g.drawImage(goalImage, xLoc, yLoc, tileSize, tileSize, null);
	}
	
	@Override
	public int getFace()
	{
		return face;
	}
}
