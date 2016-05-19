package utils;

import entity.Kube;
import graphics.Camera;
import graphics.Matrix4f;
import graphics.RotationHandler;
import graphics.Vector4f;
import terrain.TerrainType;

public class Assets 
{
	private Kube kube;
	private Kube.TileIndex playerStartIndex;
	private int cubeFace;
	private float tileSize;
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
	
	public void regenerateVisualComponents()
	{
		camera = new Camera(new Matrix4f().initPerspective((float)Math.toRadians(70.f), 1.0f, 
				0.1f, 1000.f));
		camera.setRotation(new Vector4f(-2, -2, -2), new Vector4f(-2, 2, -2), 0);
		rotationHandler = new RotationHandler(camera);
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
	
	public RotationHandler getRotationHandler() 
	{
		return rotationHandler;
	}
	
	
	
	
}
