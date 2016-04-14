package terrain;

import entity.Player;
import graphics.Bitmap;

public interface Terrain
{
	public TerrainType getTerrainType();
	public Bitmap getTerrainTexture();
	public void affectPlayer(Player player);
	
}
