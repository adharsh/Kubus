package terrain;

import entity.Kube;
import entity.Player;
import entity.Tile;
import graphics.Bitmap;
import graphics.Matrix4f;
import graphics.Mesh;
import graphics.Renderer;
import graphics.Transformation;
import graphics.Vector4f;
import input.QMFLoader;

public class FireTerrain extends Terrain
{
	private static final Mesh fireMesh;
	private static final Bitmap fireTextureOuter;
	private static final Bitmap fireTextureInner;
	private Kube map;
	
	static
	{
		fireMesh = QMFLoader.loadQMF("res/QMF/fire.qmf");
		fireTextureOuter = new Bitmap(1, 1);
		fireTextureInner = new Bitmap(1, 1);
		fireTextureOuter.setPixelBGR(0, 0, 0x2D38E3);
		fireTextureInner.setPixelBGR(0, 0, 0x00CCFF);
	}
	
	public FireTerrain(Kube map, Tile tile)
	{
		this.map = map;
		setPosition(tile.getPosition().add(tile.getHeightOffset()));
		renderTransform.setRotation(map.getFaceRotation(tile.getFace()));
		renderTransform.setScale(map.getTileLength(), map.getTileLength(), map.getTileLength());
	}	

	@Override
	public TerrainType getTerrainType()
	{
		return TerrainType.FIRE;
	}

	@Override
	public void affectPlayer(Player player) 
	{
		player.takeHealth(0.8);
	}
	
	@Override
	public void render(Renderer render, Matrix4f viewProjection) 
	{
		//if slow, change me!!
		super.render(render, viewProjection);
		Transformation tf = new Transformation(renderTransform);
		tf.setScale(map.getTileLength(), map.getTileLength(), map.getTileLength());
		Vector4f fwd = tf.getRotation().getRow(2).normalized().mul(map.getTileLength() / 9);
		fireMesh.draw(render, viewProjection, tf.getTransformation(), fireTextureOuter);
		tf.setScale(map.getTileLength() / 1.8f, map.getTileLength() / 1.8f, map.getTileLength() / 1.8f);
		tf.setPosition(tf.getPosition().add(fwd));
		fireMesh.draw(render, viewProjection, tf.getTransformation(), fireTextureInner);
		tf.setPosition(tf.getPosition().sub(fwd).sub(fwd));
		fireMesh.draw(render, viewProjection, tf.getTransformation(), fireTextureInner);
	}
}
