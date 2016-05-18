package utils;

import terrain.TerrainType;
import entity.Kube;
import graphics.Camera;
import graphics.RotationHandler;

public class Assets 
{
	private Kube kube;
	private Kube.TileIndex playerStartIndex;
	private int cubeFace;
	private float tileSize;
	private Kube.TileIndex goal; 
	private RotationHandler rotationHandler;
	private Camera camera;
	
	public static final TerrainType[] terrains = //including those that aren't used
		{
			TerrainType.ERROR_TYPE, 
			TerrainType.WATER, 
			TerrainType.SPIKES, 
			TerrainType.FIRE, 
			TerrainType.ICE
		}; 
	
	public Camera getCamera() 
	{
		return camera;
	}

	public void setCamera(Camera camera) 
	{
		this.camera = camera;
	}

	public TerrainType[] getTerrains() {
		return terrains;
	}

	public Kube getKube() 
	{
		return kube;
	}
	
	public void setKube(Kube kube) 
	{
		this.kube = kube;
	}
	
	public Kube.TileIndex  getPlayerStartIndex() 
	{
		return playerStartIndex;
	}
	
	public void setPlayerStartIndex(Kube.TileIndex playerStartIndex) 
	{
		this.playerStartIndex = playerStartIndex;
	}
	
	public float getCubeFace() 
	{
		return cubeFace;
	}
	
	public void setCubeFace(int cubeFace) 
	{
		this.cubeFace = cubeFace;
	}
	
	public float getTileSize() 
	{
		return tileSize;
	}
	
	public void setTileSize(float tileSize) 
	{
		this.tileSize = tileSize;
	}
	
	public Kube.TileIndex getGoal() 
	{
		return goal;
	}

	public void setGoal(Kube.TileIndex goal) 
	{
		this.goal = goal;
	}
	
	public RotationHandler getRotationHandler() 
	{
		return rotationHandler;
	}

	public void setRotationHandler(RotationHandler rotationHandler) 
	{
		this.rotationHandler = rotationHandler;
	}
	
	
	
	
}
