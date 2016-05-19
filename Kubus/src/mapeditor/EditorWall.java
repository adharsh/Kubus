package mapeditor;

import java.awt.Color;
import java.awt.Graphics;

public class EditorWall extends EditorAsset
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
	
	public boolean wallEquals(EditorWall other)
	{
		if(other.type != type)
		{
			return false;
		}
		switch(face)
		{
		case WALL_INNER:
			return (other.facef1 == facef1 && other.facef2 == facef2 && other.tilexf1 == tilexf1 && 
			other.tilexf2 == tilexf2 && other.tileyf1 == tileyf1 && other.tileyf2 == tileyf2);
		case WALL_EDGE:
			return (other.tilex == tilex && other.tiley == tiley && other.face == face);
		case WALL_CORNER:
			return (other.facec == facec && other.tilexc1 == tilexc1 && other.tilexc2 == tilexc2 &&
			other.tileyc1 == tileyc1 && other.tileyc2 == tileyc2);
		}
		return false;
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

	@Override
	public void renderAsset(Graphics g, int tileCount) 
	{

		int tileSize = 700 / tileCount;
		int xLoc = 0;
		int yLoc = 0;
		int w = 0;
		int h = 0;
		switch(type)
		{
		case WALL_INNER:
			xLoc = ((tileSize * tilexf1) + (tileSize * tilexf2)) / 2;
			yLoc = ((tileSize * tileyf1) + (tileSize * tileyf2)) / 2;
			if(tilexf1 != tilexf2)
			{
				xLoc += (tileSize / 2);
				h = tileSize;
				w = 3;
			}
			else
			{
				yLoc += (tileSize / 2);
				w = tileSize;
				h = 3;
			}
			break;
		case WALL_CORNER:
			xLoc = ((tileSize * tilexc1) + (tileSize * tilexc2)) / 2;
			yLoc = ((tileSize * tileyc1) + (tileSize * tileyc2)) / 2;
			if(tilexc1 != tilexc2)
			{
				xLoc += (tileSize / 2);
				h = tileSize;
				w = 3;
			}
			else
			{
				yLoc += (tileSize / 2);
				w = tileSize;
				h = 3;
			}
			break;
		case WALL_EDGE:
			if(tilex == 0)
			{
				xLoc = tileSize * tilex;
				yLoc = tileSize * tiley;
				w = 3;
				h = tileSize;
			}
			else if(tiley == 0)
			{
				xLoc = tileSize * tilex;
				yLoc = tileSize * tiley;
				h = 3;
				w = tileSize;
			}
			else if(tilex < tiley) //bottom
			{
				xLoc = tileSize * tilex;
				yLoc = tileSize * tiley + tileSize;
				h = 3;
				w = tileSize;
			}
			else //right
			{
				xLoc = tileSize * tilex + tileSize;
				yLoc = tileSize * tiley;
				w = 3;
				h = tileSize;
			}
			
		}
		g.setColor(Color.ORANGE);
		g.drawRect(100 + xLoc, 100 + yLoc, w, h);
	}

	@Override
	public int getFace()
	{
		switch(type)
		{
		case WALL_INNER:
			return facef1;
		case WALL_EDGE:
			return face;
		case WALL_CORNER:
			return facec;
		}
		return -1;
	}
}
