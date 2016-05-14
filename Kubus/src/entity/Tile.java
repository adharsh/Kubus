package entity;

import java.io.IOException;

import graphics.Bitmap;
import graphics.Matrix4f;
import graphics.Mesh;
import graphics.Renderer;
import input.QMFLoader;
import terrain.SpikeTerrain;
import terrain.Terrain;
import terrain.TerrainType;

public class Tile extends Entity
{
	public static int TILEHEIGHT_LOW = 1;
	public static int TILEHEIGHT_NORMAL = 2;
	public static int TILEHEIGHT_HIGH = 3;
	
	private Kube cubeMap;
	private Terrain terrain;
	private int tileHeight;
	private int tileXIndex;
	private int tileYIndex;
	private int face;
	
	private static final Mesh squareMesh;
	public static Bitmap solidColor;
	
	static
	{
		
		squareMesh = QMFLoader.loadQMF("res/QMF/square.qmf");
		
		try {
			solidColor = new Bitmap("res/graas.jpg");
		} catch (IOException e) {
			solidColor = new Bitmap(1, 1);
			e.printStackTrace();
		}
	}
	
	
	public Tile(int xIndex, int yIndex, int height, TerrainType terrain, Kube cube, int face)
	{
		cubeMap = cube;
		tileXIndex = xIndex;
		tileYIndex = yIndex;
		tileHeight = height;
		
		if(face < 1)
		{
			face = 1;
		}
		if(face > 6)
		{
			face = 6;
		}
		this.face = face;
		
		cube.addTile(this);
		renderTransform.setScale(cube.getTileLength(), 1, cube.getTileLength());
		//assuming center is at 0, 0, 0
		
		setPosition(cube.getTilePosition(face, xIndex, yIndex));

		this.terrain = createTerrain(terrain);
	}
	
	private Terrain createTerrain(TerrainType type)
	{
		if(type == null)
		{
			return null;
		}
		switch(type)
		{
		case SPIKES:
			return new SpikeTerrain(cubeMap, this);
		default:
			return null;
			
		}
	}
	
	@Override
	public void render(Renderer r, Matrix4f viewProjection)
	{
		super.render(r, viewProjection);
		squareMesh.draw(r, viewProjection, renderTransform.getTransformation(), solidColor);
	}
	
	public boolean isPlayerOnTile(Player player)
	{
		return player.getX() == tileXIndex && player.getY() == tileYIndex && player.getFace() == face;
	}
	
	
	public void setTerrain(Terrain terrain)
	{
		this.terrain = terrain;
	}
	
	public void setHeight(int height)
	{
		tileHeight = height;
	}
	
	public void setXIndex(int xIndex)
	{
		tileXIndex = xIndex;
	}
	
	public void setYIndex(int yIndex)
	{
		tileYIndex = yIndex;
	}
	
	public void setIndex(int xIndex, int yIndex)
	{
		tileXIndex = xIndex;
		tileYIndex = yIndex;
	}
	
	public int getHeight()
	{
		return tileHeight;
	}
	
	public int getXIndex()
	{
		return tileXIndex;
	}
	
	public int getYIndex()
	{
		return tileYIndex;
	}
	
	public Terrain getTerrain()
	{
		return terrain;
	}
	
	public int getFace()
	{
		return face;
	}
}
