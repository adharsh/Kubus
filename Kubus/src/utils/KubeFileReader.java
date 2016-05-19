package utils;

import java.io.BufferedReader;
import java.io.FileReader;

import entity.Kube;
import entity.Tile;
import terrain.TerrainType;
public class KubeFileReader 
{
	private final static String end = "</end>";
	private final static String ki = "<KubeInitializer>";
	private final static String psi = "<PlayerStartIndex>";
	private final static String g = "<Goal>";
	private final static String t = "<Tile>";
	private final static String w = "<Wall>";

	private static int[] retrieveData(String line)
	{
		String[] a = line.split(",");

		int[] b = new int[a.length];
		for(int i = 0; i < a.length; i++)
		{
			try
			{
				b[i] = Integer.parseInt(a[i]);
			}
			catch(Exception e) 
			{
				b[i] = Integer.MIN_VALUE;
			}

		}

		return b;
	}

	private static void processLine(String sline, Assets asset)
	{

		int[] data;
		String line = sline.trim();
		int startIndex = 0;

		if(line.equals("")) return;

		if(	line.indexOf(psi) != -1)
		{
			startIndex = psi.length();
			line = line.substring(startIndex, line.indexOf(end)).trim();
			data = retrieveData(line);
			asset.setPlayerStartIndex(new Kube.TileIndex(data[0], data[1], Kube.TOP) );
			return;
		}

		//int array, but float tileSize value
		if(	line.indexOf(ki) != -1)
		{
			startIndex = ki.length();
			line = line.substring(startIndex, line.indexOf(end)).trim();
			data = retrieveData(line);
			asset.setCubeFace(data[0]);

			float tileSize = Float.parseFloat(line.split(",")[1]);
			asset.setTileSize(tileSize);
			asset.setKube(new Kube(data[0], tileSize));

//			float dist = (float)data[0] * tileSize;

//			asset.getCamera().setPosition(new Vector4f(dist, dist, dist, 1));
			for(int f=1;f<=6;f++)
			{
				for(int a=0;a<data[0];a++)
				{
					for(int b=0;b<data[0];b++)
					{
						new Tile(a, b, Tile.TILEHEIGHT_NORMAL, TerrainType.ERROR_TYPE, 
								asset.getKube(), f, asset.getRotationHandler());
					}
				}
			}

			return;
		}


		//********************Rotation Handler for each Tile???

		if(	line.indexOf(t) != -1)
		{

			startIndex = t.length();
			line = line.substring(startIndex, line.indexOf(end)).trim();
			data = retrieveData(line);
			Tile tile = asset.getKube().getTileAt(data[4], data[0], data[1]);
			if(tile == null)
			{
				return;
			}
			tile.setHeight(data[2]);
			tile.setTerrain(Assets.terrains[data[3]]);

			return;
		}

		if(	line.indexOf(g) != -1)
		{
			startIndex = g.length();
			line = line.substring(startIndex, line.indexOf(end)).trim();
			data = retrieveData(line);
			Tile tile = asset.getKube().getTileAt(data[2], data[0], data[1]);
			if(tile == null)
			{
				return;
			}
			tile.setTerrain(TerrainType.GOAL);
			return;
		}

		//********
		//Easy Fix: how to add Walls with just x, y etc? -> getTileAt
		//How to determine which face with two tiles?
		if(	line.indexOf(w) != -1)
		{
			startIndex = t.length();
			line = line.substring(startIndex, line.indexOf(end)).trim();
			data = retrieveData(line);

			Kube kube = asset.getKube();

			if(data.length == 5)
				kube.addWall
				(
						new Kube.TileIndex(data[0], data[1], data[4]), 
						new Kube.TileIndex(data[2], data[3], data[4])
						);
			else if(data.length == 3)
				asset.getKube().addWall(
						asset.getKube().getTileAt(data[2], data[0], data[1])
						);
			else
				kube.addWall(
						asset.getKube().getTileAt(data[2], data[0], data[1]),
						asset.getKube().getTileAt(data[5], data[3], data[4])
						);

			return;
		}

	}

	public static Assets readFile(String filepath)
	{

		//create kube first in file
		BufferedReader br;
		Assets asset = new Assets();
		asset.regenerateVisualComponents();
		String line = "";

		try 
		{
			br = new BufferedReader( new FileReader(filepath));


			while(	(line = br.readLine())	!= null)
			{
				processLine( line, asset );
			}
			br.close();
		}
		catch (Exception e){}
		return asset;
	}

//	public static void main(String[] args) 
//	{
//		readFile("res/maps/lol.mp");
//	}

}


