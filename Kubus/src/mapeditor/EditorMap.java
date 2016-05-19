package mapeditor;

import java.util.ArrayList;

public class EditorMap 
{
	//KUBEINITIALIZER ALWAYS WRITTEN FIRST !!
	private EditorGoal goal;
	private ArrayList<EditorTile> tiles;
	private EditorKubeInit kubeMeta;
	private EditorPlayerStart pStart;
	private ArrayList<EditorWall> walls;
	
	private int cubeDims;
	
	public int getSize()
	{
		return cubeDims;
	}
	
	public EditorMap(int dims, float tileSize)
	{
		tiles = new ArrayList<EditorTile>();
		walls = new ArrayList<EditorWall>();
		addKubeInitializer(new EditorKubeInit(dims, tileSize));
		pStart = new EditorPlayerStart(0, 0);
		
		for(int f=1;f<=6;f++)
		{
			for(int a=0;a<dims;a++)
			{
				for(int b=0;b<dims;b++)
				{
					tiles.add(new EditorTile(f, a, b));
				}
			}
		}
		cubeDims = dims;
	}
	
	public void addKubeInitializer(EditorKubeInit i)
	{
		kubeMeta = i;
	}
	
	public void addAsset(EditorGoal asset)
	{
		goal = asset;
	}
	
	public void addAsset(EditorPlayerStart asset)
	{
		pStart = asset;
	}
	
	public void addAsset(EditorTile asset)
	{
		for(int a=0;a<tiles.size();a++)
		{
			if(tiles.get(a).indexEquals(asset))
			{
				tiles.set(a, asset);
			}
		}
	}
	
	public void addAsset(EditorWall asset)
	{
		boolean wasFound = false;
		for(int a=0;a<walls.size();a++)
		{
			if(walls.get(a).wallEquals(asset))
			{
				wasFound = true;
				walls.set(a, asset);
			}
		}
		if(!wasFound)
		{
			walls.add(asset);
		}
	}
	
	public ArrayList<EditorAsset> getAssets()
	{
		ArrayList<EditorAsset> allAssets = new ArrayList<EditorAsset>();
		allAssets.add(kubeMeta);
		for(EditorTile t : tiles)
		{
			allAssets.add(t);
		}
		for(EditorWall w : walls)
		{
			allAssets.add(w);
		}
		allAssets.add(pStart);
		allAssets.add(goal);
		return allAssets;
	}
	
	public ArrayList<EditorAsset> fetchAssetsOnFace(int face)
	{
		ArrayList<EditorAsset> allAssets = getAssets();
		ArrayList<EditorAsset> assetsOnFace = new ArrayList<EditorAsset>();
		
		for(EditorAsset asset : allAssets)
		{
			if(asset != null && asset.getFace() == face)
			{
				assetsOnFace.add(asset);
			}
		}
		assetsOnFace.add(kubeMeta);
		return assetsOnFace;
	}
	
}
