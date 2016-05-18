package terrain;

import java.io.IOException;

import entity.Kube;
import entity.Player;
import entity.Tile;
import graphics.Bitmap;
import graphics.Matrix4f;
import graphics.Mesh;
import graphics.Renderer;
import input.QMFLoader;

public class GoalTerrain extends Terrain
{
	private static final Mesh goalMesh;
	private static Bitmap meshTexture;
	
	static
	{
		goalMesh = QMFLoader.loadQMF("res/QMF/goalflag.qmf");
		try 
		{
			meshTexture = new Bitmap("res/flag.png");
		} 
		catch(IOException e) 
		{
			meshTexture = new Bitmap(1, 1);
		}
	}
	
	public GoalTerrain(Kube map, Tile tile) 
	{
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
		player.setWon(true);
	}

	@Override
	public void render(Renderer r, Matrix4f viewProjection)
	{
		super.render(r, viewProjection);
		goalMesh.draw(r, viewProjection, renderTransform.getTransformation(), meshTexture);
	}
}
