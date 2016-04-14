package entity;

import graphics.Renderer;
import terrain.Terrain;

public class Tile extends Entity
{
	public static int TILEHEIGHT_LOW = 1;
	public static int TILEHEIGHT_NORMAL = 2;
	public static int TILEHEIGHT_HIGH = 3;
	
	private Terrain terrain;
	private int tileHeight;
	private int tileXIndex;
	private int tileYIndex;
	
	
	public Tile(int xIndex, int yIndex, int height, Terrain terrain)
	{
		this.terrain = terrain;
		tileXIndex = xIndex;
		tileYIndex = yIndex;
		tileHeight = height;
	}
	
	public void setTerrain(Terrain terrain)
	{
		this.terrain = terrain;
	}
	
	public boolean isPlayerOnTile(Player player)
	{
		return false;
	}
	
	public void setHeight()
	{
		
	}
	
	@Override
	public void render(Renderer r)
	{
		
	}
}
