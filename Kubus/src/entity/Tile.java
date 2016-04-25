package entity;

import graphics.Bitmap;
import graphics.Matrix4f;
import graphics.Mesh;
import graphics.Renderer;
import graphics.Vector4f;
import graphics.Vertex;

import java.io.IOException;
import java.util.ArrayList;

import terrain.Terrain;

public class Tile extends Entity
{
	public static int TILEHEIGHT_LOW = 1;
	public static int TILEHEIGHT_NORMAL = 2;
	public static int TILEHEIGHT_HIGH = 3;
	
	private Kube cubeMap;
	private Terrain terrain;
	private int tileHeight;
	private int tileXIndex;
	private int tileYIndex;
	private int face;
	
	private static final Mesh squareMesh;
	private static Bitmap solidColor;
	
	static
	{
		ArrayList<Vertex> vertices = new ArrayList<Vertex>();
		ArrayList<Integer> indices = new ArrayList<Integer>();
		vertices.add(new Vertex(new Vector4f(-0.5f, 0, -0.5f, 1), new Vector4f(0, 0, 0, 0)));
		vertices.add(new Vertex(new Vector4f(-0.5f, 0, .5f, 1), new Vector4f(0, 1, 0, 0)));
		vertices.add(new Vertex(new Vector4f(0.5f, 0, 0.5f, 1), new Vector4f(1, 1, 0, 0)));
		vertices.add(new Vertex(new Vector4f(0.5f, 0, -0.5f, 1), new Vector4f(1, 0, 0, 0)));

		indices.add(0);
		indices.add(1);
		indices.add(2);
		indices.add(2);
		indices.add(3);
		indices.add(0);
		
		squareMesh = new Mesh(vertices, indices);
		
		try {
			solidColor = new Bitmap("res/whale.jpg");
		} catch (IOException e) {
			solidColor = new Bitmap(1, 1);
			e.printStackTrace();
		}
	}
	
	
	public Tile(int xIndex, int yIndex, int height, Terrain terrain, Kube cube, int face)
	{
		this.terrain = terrain;
		cubeMap = cube;
		tileXIndex = xIndex;
		tileYIndex = yIndex;
		tileHeight = height;
		
		if(face < 1)
		{
			face = 1;
		}
		if(face > 6)
		{
			face = 6;
		}
		this.face = face;
		
		cube.addTile(this);
		renderTransform.setScale(cube.getTileLength(), 1, cube.getTileLength());
		//assuming center is at 0, 0, 0
		
		
		
		float faceLength = cube.getFaceLength() - 1;

		float outValue = (float)cube.getFaceLength() / 2.f * cube.getTileLength();
		faceLength /= 2.f;
		float xValue = ((float)xIndex - faceLength) * cube.getTileLength();
		float yValue = ((float)yIndex - faceLength) * cube.getTileLength();
		if(face == Kube.TOP)
		{
			renderTransform.setPosition(new Vector4f(xValue, outValue,
					yValue, 1));
		}
		else if(face == Kube.BOTTOM)
		{
			renderTransform.setPosition(new Vector4f(xValue, -outValue,
					-yValue, 1));
		}
		else if(face == Kube.BACK)
		{
			renderTransform.setPosition(new Vector4f(xValue, yValue, -outValue, 1));
		}
		else if(face == Kube.FRONT)
		{
			renderTransform.setPosition(new Vector4f(xValue,
					-yValue, outValue, 1));
		}
		else if(face == Kube.RIGHT)
		{
			renderTransform.setPosition(new Vector4f(outValue, -xValue,
					yValue, 1));
		}
		else if(face == Kube.LEFT)
		{
			renderTransform.setPosition(new Vector4f(-outValue, xValue,
					yValue, 1));
		}
	}
	
	
	@Override
	public void render(Renderer r, Matrix4f viewProjection)
	{
		super.render(r, viewProjection);
		squareMesh.draw(r, viewProjection, renderTransform.getTransformation(), solidColor);
	}
	
	public boolean isPlayerOnTile(Player player)
	{
		return false;
	}
	
	
	public void setTerrain(Terrain terrain)
	{
		this.terrain = terrain;
	}
	
	public void setHeight(int height)
	{
		tileHeight = height;
	}
	
	public void setXIndex(int xIndex)
	{
		tileXIndex = xIndex;
	}
	
	public void setYIndex(int yIndex)
	{
		tileYIndex = yIndex;
	}
	
	public void setIndex(int xIndex, int yIndex)
	{
		tileXIndex = xIndex;
		tileYIndex = yIndex;
	}
	
	public int getHeight()
	{
		return tileHeight;
	}
	
	public int getXIndex()
	{
		return tileXIndex;
	}
	
	public int getYIndex()
	{
		return tileYIndex;
	}
	
	public Terrain getTerrain()
	{
		return terrain;
	}
	
	public int getFace()
	{
		return face;
	}
}
