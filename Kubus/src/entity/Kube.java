package entity;

import graphics.Matrix4f;

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
	private float tileLength;
	//try 10x10 faces
	private int faceLength;
	//face that the player is on (top relative to camera)
	private int topFace;
	
	public Kube(int faceLength, float tileLength)
	{
		this.faceLength = faceLength;
		this.tileLength = tileLength;
	}
	
	
	public int getFaceLength()
	{
		return faceLength;
	}
	
	public float getTileLength()
	{
		return tileLength;
	}
	
	private Matrix4f getRelativeRotation(int relativeFace)
	{
		
	}
	
	private int getRelativeFace(int face)
	{
		if(topFace == 1)
		{
			
		}
	}
	
	public Matrix4f getFaceRotation(int face)
	{
		switch(topFace)
		{
		case 1:
			return getRelativeRotation(getRelativeFace(face));
			break;
		case 2:
			break;
		case 3:
			break;
		case 4:
			break;
		case 5:
			break;
		case 6:
			break;
		}
	}
}
