package terrain;

import entity.Kube;
import entity.Player;
import entity.Tile;
import graphics.Bitmap;
import graphics.Mesh;

public class GoalTerrain extends Terrain
{
	private static final Mesh flag;
	private static final Bitmap fireTextureInner;
	private Kube map;
	
	static
	{
		
	}
	
	public GoalTerrain(Kube map, Tile tile) 
	{
		this.map = map;
		setPosition(tile.getPosition().add(tile.getHeightOffset()));
		renderTransform.setRotation(map.getFaceRotation(tile.getFace()));
		renderTransform.setScale(map.getTileLength(), map.getTileLength(), map.getTileLength());
	}

	@Override
	public TerrainType getTerrainType() 
	{
		return TerrainType.GOAL;
	}

	@Override
	public void affectPlayer(Player player) 
	{
		System.out.println("you lose");
	}

}
