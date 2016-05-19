package mapeditor;

public class EditorKubeInit implements EditorAsset
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
}
