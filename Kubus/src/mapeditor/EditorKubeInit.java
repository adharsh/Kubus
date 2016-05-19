package mapeditor;

import java.awt.Color;
import java.awt.Graphics;

public class EditorKubeInit extends EditorAsset
{
	private int tileOnFace;
	private float tileSize;
	
	public EditorKubeInit(int tileOnFace, float tileSize)
	{
		this.tileOnFace = tileOnFace;
		this.tileSize = tileSize;
	}

	@Override
	public String getAssetString()
	{
		return "<KubeInitializer> " + tileOnFace + "," + tileSize + " </end>";
	}

	@Override
	public void renderAsset(Graphics g, int tileCount) 
	{
		g.setColor(Color.orange);
		g.drawString("Cube face dimensions: " + tileOnFace + " x " + tileOnFace, 600, 30);
	}
}
