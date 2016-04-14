package entity;

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
}
