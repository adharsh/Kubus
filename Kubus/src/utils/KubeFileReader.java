package utils;

import entity.Kube;
import entity.Tile;
import graphics.Camera;
import graphics.Matrix4f;
import graphics.RotationHandler;
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
			asset.setCamera(new Camera(new Matrix4f().initPerspective((float)Math.toRadians(70.f), 1.0f, 
					0.1f, 1000.f)));
			asset.getCamera().setRotation(new Vector4f(-2, -2, -2, 0), new Vector4f(-2, 2, -2, 0), 0);
			asset.setRotationHandler(new RotationHandler(asset.getCamera()));
			
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
			
			if(data.length == 5)
				kube.addWall
				(
					new Kube.TileIndex(data[0], data[1], data[4]), 
					new Kube.TileIndex(data[2], data[3], data[4])
				);
			else if(data.length == 3)
				asset.getKube().addWall(
						asset.getKube().getTileAt(data[0], data[1], data[2])
						);
			else
				kube.addWall(
						asset.getKube().getTileAt(data[2], data[0], data[1]),
						asset.getKube().getTileAt(data[5], data[3], data[4])
						);
			
			return;
		}
		
	}
	
	public static Assets readFile(String filepath) throws IOException 
	{
		
		//create kube first in file
		BufferedReader br;
		Assets asset = new Assets();
		String line = "";

		br = new BufferedReader( new FileReader(filepath));

		while(	(line = br.readLine())	!= null)
		{
			processLine( line, asset );
		}
		br.close();
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


