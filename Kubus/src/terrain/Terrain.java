package terrain;

import entity.Entity;
import entity.Player;
import graphics.Vector4f;

public abstract class Terrain extends Entity
{
	public abstract TerrainType getTerrainType();
	public abstract void affectPlayer(Player player);
	//for cleanup purposes
	public void onPlayerLeaveTile(Player player) {}
	public Vector4f getWaterOffset(int direction)
	{
		return new Vector4f(0, 0, 0, 0);
	}
}
