package terrain;

import entity.Player;

public class NoTerrain extends Terrain 
{

	@Override
	public TerrainType getTerrainType()
	{
		return TerrainType.ERROR_TYPE;
	}

	@Override
	public void affectPlayer(Player player) {}
	
}
