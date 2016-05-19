package mapeditor;

import java.awt.Graphics;

public class EditorGoal extends EditorAsset
{
	private int x;
	private int y;
	private int face;
	
	public EditorGoal(int x, int y, int face)
	{
		this.x = x;
		this.y = y;
		this.face = face;
	}

	@Override
	public String getAssetString() 
	{
		return "<Goal> " + x + "," + y + "," + face + " </end>";
	}

	@Override
	public void renderAsset(Graphics g, int tileCount) 
	{
		
	}
	
	@Override
	public int getFace()
	{
		return face;
	}
}
