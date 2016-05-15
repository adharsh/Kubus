package terrain;

import entity.Kube;
import entity.Player;
import entity.Tile;
import graphics.RotationHandler;

public class IceTerrain extends Terrain
{
	private RotationHandler r;
	
	public IceTerrain(Kube map, Tile tile, RotationHandler r)
	{
		this.r = r;
	}
	
	@Override
	public TerrainType getTerrainType() 
	{
		return TerrainType.ICE;
	}

	@Override
	public void affectPlayer(Player player) 
	{
		if(player.getLastMoveDirection() != -1)
		{
			player.move(player.getLastMoveEdge(), player.getLastMoveDirection(), r);
		}
	}

}
