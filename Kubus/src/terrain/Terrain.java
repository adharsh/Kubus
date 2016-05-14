package terrain;

import entity.Entity;
import entity.Player;

public abstract class Terrain extends Entity
{
	public abstract TerrainType getTerrainType();
	public abstract void affectPlayer(Player player);
	
}
