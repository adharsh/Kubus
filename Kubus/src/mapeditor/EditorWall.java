package mapeditor;

public class EditorWall implements EditorAsset
{
	public static final int WALL_INNER = 1;
	public static final int WALL_EDGE = 2;
	public static final int WALL_CORNER = 3;
	
	
	int type;
	
	//TYPE 1 INNER FACE
	int tilexf1;
	int tileyf1;
	int facef1;
	int tilexf2;
	int tileyf2;
	int facef2;
	
	//TYPE 1 EDGE
	int tilex;
	int tiley;
	int face;
	
	//TYPE 2 CORNER
	int tilexc1;
	int tileyc1;
	int facec;
	int tilexc2;
	int tileyc2;
	
	public EditorWall(int x1, int y1, int x2, int y2, int f1, int f2)
	{
		type = WALL_INNER;
		tilexf1 = x1;
		tilexf2 = x2;
		tileyf1 = y1;
		tileyf2 = y2;
		facef1 = f1;
		facef2 = f2;
	}
	
	public EditorWall(int x1, int y1, int x2, int y2, int f)
	{
		type = WALL_CORNER;
		tilexc1 = x1;
		tilexc2 = x2;
		tileyc1 = y1;
		tileyc2 = y2;
		facec = f;
	}
	
	public EditorWall(int x, int y, int f)
	{
		type = WALL_EDGE;
		tilex = x;
		tiley = y;
		face = f;
	}
	
	@Override
	public String getAssetString() 
	{
		switch(type)
		{
		case WALL_INNER:
			return "<Wall> " + tilexf1 + "," + tileyf1 + "," + facef1 + "," + tilexf2 + "," + tileyf2 + "," + facef2 + " </end>";
		case WALL_CORNER:
			return "<Wall> " + tilexc1 + "," + tileyc1 + "," + tilexc2 + "," + tileyc2 + "," + facec + " </end>";
		case WALL_EDGE:
			return "<Wall> " + tilex + "," + tiley + "," + face + " </end>";
		default:
			return "";
		}
	}

}
