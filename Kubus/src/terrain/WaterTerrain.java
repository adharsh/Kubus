package terrain;

import entity.Kube;
import entity.Player;
import entity.Tile;
import graphics.Vector4f;

public class WaterTerrain extends Terrain
{
	private float floatInterp;
	private long lastCallTime;
	private Tile tile;
	private Kube map;
	private boolean firstEntry;
	
	public WaterTerrain(Kube map, Tile t)
	{
		floatInterp = 0;
		lastCallTime = System.currentTimeMillis();
		tile = t;
		this.map = map;
		firstEntry = true;
	}
	
	public TerrainType getTerrainType() 
	{
		return TerrainType.WATER;
	}

	@Override
	public Vector4f getWaterOffset(int direction)
	{
		return getUpDirection().mul(direction * (map.getTileLength() / 8));
	}
	
	private Vector4f getUpDirection()
	{
		return map.getFaceRotation(tile.getFace()).getRow(1);
	}
	
	//floating animation
	@Override
	public void affectPlayer(Player player) 
	{
		if(firstEntry)
		{
			lastCallTime = System.currentTimeMillis();
			firstEntry = false;
		}
		float deltaTime = ((float)(System.currentTimeMillis() - lastCallTime)) / 1000.f;
		Vector4f originLoc = tile.getPosition().add(tile.getHeightOffset()).add(getWaterOffset(-1));
		Vector4f floatAmt = getUpDirection();
		
		if((((int)floatInterp) % 2) == 1)
		{
			float sectionInterp = (float)((int)floatInterp + 1) - floatInterp;
			floatAmt = floatAmt.mul((map.getTileLength() * sectionInterp) / 16);
		}
		else
		{
			float sectionInterp = floatInterp - (float)((int)floatInterp);
			floatAmt = floatAmt.mul((map.getTileLength() * sectionInterp) / 16);
		}
		
		player.setPosition(originLoc.add(floatAmt));
		lastCallTime = System.currentTimeMillis();
		floatInterp += deltaTime;
	}
	
	@Override
	public void onPlayerLeaveTile(Player player)
	{
		player.setPosition(tile.getPosition().add(tile.getHeightOffset()).add(getWaterOffset(-1)));
		floatInterp = 0;
		firstEntry = true;
	}
	
}
