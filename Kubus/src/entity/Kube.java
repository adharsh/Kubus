package entity;

import java.util.ArrayList;
import java.util.HashMap;

import graphics.Matrix4f;
import graphics.Vector4f;

/*
 * assuming there is no rotation on the cube and you are viewing the cube's center from a position of (0, -1, -1)
 * the faces are numbered:
 * 	top = 1
 * 	front = 2
 * 	bottom = 3
 * 	back = 4
 * 	left = 5
 * 	right = 6
 */

public class Kube extends Entity
{
	private static final int TOP = 1;
	private static final int FRONT = 2;
	private static final int BOTTOM = 3;
	private static final int BACK = 4;
	private static final int LEFT = 5;
	private static final int RIGHT = 6;
	
	private float tileLength;
	//try 10x10 faces
	private int faceLength;
	
	private HashMap<Integer, Integer> faceMap;
	private ArrayList<ArrayList<Tile>> tiles;
	
	public Kube(int faceLength, float tileLength)
	{
		this.faceLength = faceLength;
		this.tileLength = tileLength;
		faceMap = new HashMap<Integer, Integer>();
		faceMap.put(TOP, TOP);
		faceMap.put(FRONT, FRONT);
		faceMap.put(BOTTOM, BOTTOM);
		faceMap.put(BACK, BACK);
		faceMap.put(LEFT, LEFT);
		faceMap.put(RIGHT, RIGHT);
		tiles = new ArrayList<ArrayList<Tile>>();
		for(int a=0;a<6;a++) { tiles.add(new ArrayList<Tile>()); }
	}
	
	
	public int getFaceLength()
	{
		return faceLength;
	}
	
	public float getTileLength()
	{
		return tileLength;
	}
	
	
	public Tile getTileAt(int face, int xIndex, int yIndex)
	{
		if(face >= tiles.size() || xIndex >= faceLength || yIndex >= faceLength)
			return null;
		ArrayList<Tile> arrayListFace = tiles.get(face);
		for(Tile t : arrayListFace)
		{
			if(t.getXIndex() == xIndex && t.getYIndex() == yIndex)
			{
				return t;
			}
		}
		return null;
	}
	
	public boolean addTile(Tile tile)
	{
		int index = tile.getFace() - 1;
		
		if(index >= tiles.size() || (faceLength * faceLength) >= tiles.get(index).size())
		{
			return false;
		}
		tiles.get(index).add(tile);
		
		return true;
	}
	
	public void rotateTopForward()
	{
		for(int a = TOP; a < RIGHT; a++)
		{
			int mappedValue = faceMap.get(a);
			if(mappedValue != LEFT && mappedValue != RIGHT)
			{
				mappedValue++;
				if(mappedValue > BACK)
				{
					mappedValue = TOP;
				}
			}
			faceMap.put(a, mappedValue);
		}
	}
	
	public void rotateTopBackward()
	{
		for(int a = TOP; a < RIGHT; a++)
		{
			int mappedValue = faceMap.get(a);
			if(mappedValue != LEFT && mappedValue != RIGHT)
			{
				mappedValue--;
				if(mappedValue < FRONT)
				{
					mappedValue = BACK;
				}
			}
			faceMap.put(a, mappedValue);
		}
	}
	
	public void rotateTopLeft()
	{
		for(int a = TOP; a < RIGHT; a++)
		{
			int mappedValue = faceMap.get(a);
			if(mappedValue != BACK && mappedValue != FRONT)
			{
				switch(mappedValue)
				{
				case TOP:
					mappedValue = LEFT;
					break;
				case LEFT:
					mappedValue = BOTTOM;
					break;
				case BOTTOM:
					mappedValue = RIGHT;
					break;
				case RIGHT:
					mappedValue = TOP;
				}
			}
			faceMap.put(a, mappedValue);
		}
	}
	
	public void rotateTopRight()
	{
		for(int a = TOP; a < RIGHT; a++)
		{
			int mappedValue = faceMap.get(a);
			if(mappedValue != BACK && mappedValue != FRONT)
			{
				switch(mappedValue)
				{
				case TOP:
					mappedValue = RIGHT;
					break;
				case LEFT:
					mappedValue = TOP;
					break;
				case BOTTOM:
					mappedValue = LEFT;
					break;
				case RIGHT:
					mappedValue = BOTTOM;
				}
			}
			faceMap.put(a, mappedValue);
		}
	}
	
	public Matrix4f getFaceRotation(int face)
	{
		return getRelativeRotation(faceMap.get(face));
	}
	
	private Matrix4f getRelativeRotation(int relativeFace)
	{
		switch(relativeFace)
		{
		case TOP:
			return new Matrix4f().initRotation(new Vector4f(0, 0, 1, 1), new Vector4f(0, 1, 0, 1));
		case FRONT:
			return new Matrix4f().initRotation(0, 0, 1);
		case BOTTOM:
			return new Matrix4f().initRotation(0, -1, 0);
		case BACK:
			return new Matrix4f().initRotation(0, 0, -1);
		case LEFT:
			return new Matrix4f().initRotation(-1, 0, 0);
		case RIGHT:
			return new Matrix4f().initRotation(1, 0, 0);
		}
		return null;
	}
}
