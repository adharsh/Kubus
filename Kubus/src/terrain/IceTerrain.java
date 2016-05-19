package terrain;

import entity.Player;
import graphics.RotationHandler;

public class IceTerrain extends Terrain
{
	private RotationHandler r;
	
	public IceTerrain(RotationHandler r)
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
		System.out.println(player.getLastMoveDirection() + ", " + player.getLastMoveEdge());
		if(player.getLastMoveDirection() != -1)
		{
			player.move(player.getLastMoveEdge(), player.getLastMoveDirection(), r);
		}
	}

}
