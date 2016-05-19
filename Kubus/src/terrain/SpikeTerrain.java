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

public class SpikeTerrain extends Terrain
{
	private static final Mesh spikeMesh;
	private static final Bitmap spikeTexture;
	private Kube map;
	
	static
	{
		spikeMesh = QMFLoader.loadQMF("res/QMF/spike.qmf");
		
		spikeTexture = new Bitmap(1, 1);
		spikeTexture.setPixelBGR(0, 0, 0x202020);
	}
	
	public SpikeTerrain(Kube map, Tile tile)
	{
		this.map = map;
		setPosition(tile.getPosition().add(tile.getHeightOffset()));
		renderTransform.setRotation(map.getFaceRotation(tile.getFace()));
		renderTransform.setScale(map.getTileLength(), map.getTileLength(), map.getTileLength());
	}
	
	@Override
	public TerrainType getTerrainType() 
	{
		return TerrainType.SPIKES;
	}

	@Override
	public void affectPlayer(Player player)
	{
		player.takeHealth(player.getHealth());
	}

	@Override
	public void render(Renderer render, Matrix4f viewProjection) 
	{
		super.render(render, viewProjection);
		Transformation tf = new Transformation(renderTransform);
		tf.setScale(map.getTileLength() / 4, map.getTileLength(), map.getTileLength() / 4);
		Vector4f right = tf.getRotation().getRow(0).normalized().mul(map.getTileLength() / 5);
		Vector4f fwd = tf.getRotation().getRow(2).normalized().mul(map.getTileLength() / 5);
		tf.setPosition(tf.getPosition().add(fwd).add(right));
		spikeMesh.draw(render, viewProjection, tf.getTransformation(), spikeTexture);
		tf.setPosition(tf.getPosition().sub(right).sub(right));
		spikeMesh.draw(render, viewProjection, tf.getTransformation(), spikeTexture);
		tf.setPosition(tf.getPosition().sub(fwd).sub(fwd));
		spikeMesh.draw(render, viewProjection, tf.getTransformation(), spikeTexture);
		tf.setPosition(tf.getPosition().add(right).add(right));
		spikeMesh.draw(render, viewProjection, tf.getTransformation(), spikeTexture);
	}

}
