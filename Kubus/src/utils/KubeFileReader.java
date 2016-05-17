package utils;

import entity.Kube;
import entity.Tile;
import graphics.Vector4f;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import terrain.Terrain;

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
			b[i] = Integer.parseInt(a[i]);
		}
		
		return b;
	}

	private static void processLine(String sline, Assets asset)
	{
		
		int[] data;
		String line = sline.trim();
		String start = "";
		int startIndex = 0;
		
		if(line.equals("")) return;
		
		if(	line.indexOf(psi) != -1)
		{
			startIndex = psi.length();
			line = line.substring(startIndex, line.indexOf(end)).trim();
			data = retrieveData(line);
			asset.setPlayerStartIndex(new Kube.TileIndex(data[0], data[1], data[2]) );
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
			return;
		}
		
		if(	line.indexOf(g) != -1)
		{
			startIndex = g.length();
			line = line.substring(startIndex, line.indexOf(end)).trim();
			data = retrieveData(line);
			asset.setGoal(new Kube.TileIndex(data[0], data[1], data[2]));
			return;
		}
		
		
		//********************Rotation Handler for each Tile???
		
		if(	line.indexOf(t) != -1)
		{
			startIndex = t.length();
			line = line.substring(startIndex, line.indexOf(end)).trim();
			data = retrieveData(line);
			asset.getKube().addTile(new Tile(data[0], data[1], data[2], Assets.terrains[data[3]], asset.getKube(), data[4], null));
			
			return;
		}
		
		if(	line.indexOf(g) != -1)
		{
			startIndex = g.length();
			line = line.substring(startIndex, line.indexOf(end)).trim();
			data = retrieveData(line);
			asset.setGoal(new Kube.TileIndex(data[0], data[1], data[3]));
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
			
			int SAM_LOOK_AT_THIS_FACE = 0;
			if(data.length == 4)
				kube.addWall
				(
					new Kube.TileIndex(data[0], data[1], SAM_LOOK_AT_THIS_FACE), 
					new Kube.TileIndex(data[1], data[2], SAM_LOOK_AT_THIS_FACE)
				);
			else if(data.length == 2)
				asset.getKube().addWall(
						null
						);
			else
				kube.addWall(
						asset.getKube().getTileAt(SAM_LOOK_AT_THIS_FACE, data[0], data[1]),
						asset.getKube().getTileAt(SAM_LOOK_AT_THIS_FACE, data[2], data[3])
						);
			
			return;
		}
		
	}
	
	public static Assets readFile(String filepath) throws IOException 
	{
		
		//create kube first in file
		File file;
		BufferedReader br;
		Assets asset = new Assets();
		String line = "";

		br = new BufferedReader( new FileReader(filepath));

		while(	(line = br.readLine())	!= null)
		{
			processLine( line, asset );
		}

		return asset;
	}

	public static void main(String[] args) 
	{
		try {
			readFile("res/map1.lol");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}


