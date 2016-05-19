package mapeditor;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class EditorTile extends EditorAsset
{
	private static BufferedImage grassImage;
	private static BufferedImage waterImage;
	private static BufferedImage fireImage;
	private static BufferedImage iceImage;
	private static BufferedImage spikeImage;
	
	static
	{
		grassImage = loadimg("res/graas.jpg");
		waterImage = loadimg("res/water.jpg");
		fireImage = loadimg("res/fire.jpg");
		iceImage = loadimg("res/ice.jpg");
		spikeImage = loadimg("res/spike.jpg");
	}
	
	public static final int TT_ERROR = 0;
	public static final int TT_WATER = 1;
	public static final int TT_SPIKES = 2;
	public static final int TT_FIRE = 3;
	public static final int TT_ICE = 4;
	
	private int face;
	private int x;
	private int y;
	private int height;
	private int terrain;
	
	
	public EditorTile(int f, int x, int y)
	{
		this.x = x;
		this.y = y;
		this.face = f;
		height = 2;
		terrain = TT_ERROR;
	}

	@Override
	public String getAssetString()
	{
		if(height == 2 && terrain == TT_ERROR)
		{
			return "";
		}
		return "<Tile> " + x + "," + y + "," + height + "," + terrain + "," + face + " </end>";
	}

	@Override
	public void renderAsset(Graphics g, int tileCount)
	{
		//window size = 900x900
		//RENDER AREA = 100 thru 800 x,y
		//tile size = 700 / tileCount
		int tileSize = 700 / tileCount;
		int xLoc = 100 + (tileSize * x);
		int yLoc = 100 + (tileSize * y);
		switch(terrain)
		{
		case TT_ERROR:
			g.drawImage(grassImage, xLoc, yLoc, tileSize, tileSize, null);
			break;
		case TT_WATER:
			g.drawImage(waterImage, xLoc, yLoc, tileSize, tileSize, null);
			break;
		case TT_SPIKES:
			g.drawImage(spikeImage, xLoc, yLoc, tileSize, tileSize, null);
			break;
		case TT_FIRE:
			g.drawImage(fireImage, xLoc, yLoc, tileSize, tileSize, null);
			break;
		case TT_ICE:
			g.drawImage(iceImage, xLoc, yLoc, tileSize, tileSize, null);
			break;
		}
	}
	
	@Override
	public int getFace()
	{
		return face;
	}
	
	public boolean indexEquals(EditorTile other)
	{
		if(other.face == face && other.x == x && other.y == y)
		{
			return true;
		}
		return false;
	}
}
