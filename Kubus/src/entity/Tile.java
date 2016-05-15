package entity;

import java.io.IOException;

import graphics.Bitmap;
import graphics.Matrix4f;
import graphics.Mesh;
import graphics.Renderer;
import graphics.RotationHandler;
import input.QMFLoader;
import terrain.FireTerrain;
import terrain.IceTerrain;
import terrain.NoTerrain;
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
	public static Bitmap grassTexture;
	public static Bitmap iceTexture;
	public static Bitmap waterTexture;
	
	
	static
	{
		
		squareMesh = QMFLoader.loadQMF("res/QMF/square.qmf");
		
		try 
		{
			grassTexture = new Bitmap("res/graas.jpg");
		}
		catch(IOException e) 
		{
			grassTexture = new Bitmap(1, 1);
		}
		
		try 
		{
			iceTexture = new Bitmap("res/ice.jpg");
		}
		catch(IOException e) 
		{
			iceTexture = new Bitmap(1, 1);
		}
		
		try 
		{
			waterTexture = new Bitmap("res/water.jpg");
		}
		catch(IOException e) 
		{
			waterTexture = new Bitmap(1, 1);
		}
	}
	
	
	public Tile(int xIndex, int yIndex, int height, TerrainType terrain, Kube cube, int face, RotationHandler r)
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

		this.terrain = createTerrain(terrain, r);
	}
	
	private Terrain createTerrain(TerrainType type, RotationHandler r)
	{
		if(type == null)
		{
			return new NoTerrain();
		}
		switch(type)
		{
		case SPIKES:
			return new SpikeTerrain(cubeMap, this);
		case FIRE:
			return new FireTerrain(cubeMap, this);
		case ICE:
			return new IceTerrain(cubeMap, this, r);
		default:
			return new NoTerrain();
			
		}
	}
	
	@Override
	public void render(Renderer r, Matrix4f viewProjection)
	{
		super.render(r, viewProjection);
		Bitmap texture;
		if(terrain.getTerrainType() == TerrainType.ICE)
		{
			texture = iceTexture;
		}
		else if(terrain.getTerrainType() == TerrainType.WATER)
		{
			texture = waterTexture;
		}
		else
		{
			texture = grassTexture;
		}
		squareMesh.draw(r, viewProjection, renderTransform.getTransformation(), texture);
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
