package mapeditor;

public class EditorTile implements EditorAsset
{
	public static final int TT_ERROR = 0;
	public static final int TT_WATER = 1;
	public static final int TT_SPIKES = 2;
	public static final int TT_FIRE = 3;
	public static final int TT_ICE = 4;
	
	private int face;
	private int x;
	private int y;
	private int height;
	private int terrain;
	
	public EditorTile(int f, int x, int y)
	{
		this.x = x;
		this.y = y;
		this.face = f;
	}

	@Override
	public String getAssetString()
	{
		return "<Tile> " + x + "," + y + "," + height + "," + terrain + "," + face + " </end>";
	}
}
