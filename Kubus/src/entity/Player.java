package entity;

import java.io.IOException;
import java.util.ArrayList;

import graphics.Bitmap;
import graphics.Matrix4f;
import graphics.Mesh;
import graphics.Renderer;
import graphics.Vector4f;
import graphics.Vertex;

public class Player extends Entity
{
	private static final double MAX_HEALTH = 100;
	private static Mesh entMesh;
	private double health;
	//face the player is on
	private int currentFace;
	private Kube map;
	private int curX, curY;
	private static Bitmap solidColor;
	
	
		private boolean isMoving;
		//vector which describes move at inception
		private Vector4f moveVector;
		private float interpAmt;
		//.5 seconds to move
		
	static
	{
		ArrayList<Vertex> vertices = new ArrayList<Vertex>();
		ArrayList<Integer> indices = new ArrayList<Integer>();

		vertices.add(new Vertex(new Vector4f(-0.3f, 0, 0, 1), new Vector4f(0, 0, 0, 0)));
		vertices.add(new Vertex(new Vector4f(0.3f, 0, 0, 1), new Vector4f(0, 1, 0, 0)));
		vertices.add(new Vertex(new Vector4f(0, 0.45f, 0, 1), new Vector4f(0.5f, 0.5f, 0, 0)));

		indices.add(0);
		indices.add(1);
		indices.add(2);
		
		entMesh = new Mesh(vertices, indices);
		
		try {
			solidColor = new Bitmap("d:/jpeg/6eMtaHG.png");
		} catch (IOException e) {
			solidColor = new Bitmap(1, 1);
			e.printStackTrace();
		}
	}
	
	public Player(int startFace, int startX, int startY, Kube map) 
	{
		health = MAX_HEALTH;
		currentFace = startFace;
		this.map = map;
		curX = startX;
		curY = startY;
		setPosition(map.getTilePosition(startFace, startX, startY));

		renderTransform.setScale(map.getTileLength(), map.getTileLength(), map.getTileLength());
	}
	
	@Override
	public void render(Renderer r, Matrix4f viewProjection)
	{
		super.render(r, viewProjection);
		entMesh.draw(r, viewProjection, renderTransform.getTransformation(), solidColor);
	}
	
	public void takeHealth(double amount)
	{
		health -= amount;
	}
	
	public boolean isMovingToNextTile()
	{
		return isMoving;
	}
	
	//returns if move is done
	public boolean moveTick(float deltaTime)
	{
		float moveAmount;
		if(interpAmt + (2 * deltaTime) > 1.f)
		{
			moveAmount = 1.f - interpAmt;
			interpAmt = 1.f;
			isMoving = false;
		}
		else
		{
			moveAmount = (2 * deltaTime);
			interpAmt += moveAmount;
		}
		float xDist = moveVector.getX() * moveAmount;
		float yDist = moveVector.getY() * moveAmount;
		float zDist = moveVector.getZ() * moveAmount;
		
		setPosition(getPosition().add(new Vector4f(xDist, yDist, zDist, 0)));
		
		return !isMoving;
	}
	
	//returns if successful move 
	//move fails if it hits wall
	public boolean move(int dx, int dy)
	{
		//one direction at a time
		if(dx * dy != 0)
		{
			return false;
		}
		//one tile at a time
		if(Math.abs(dx) > 1 || Math.abs(dy) > 1)
		{
			return false;
		}
		
		boolean failed = false;
		curX += dx;
		curY += dy;
		
		if(curX < 0)
		{
			curX = 0;
			failed = true;
		}
		if(curY < 0)
		{
			curY = 0;
			failed = true;
		}
		if(curX >= map.getFaceLength())
		{
			curX = map.getFaceLength() - 1;
			failed = true;
		}
		if(curY >= map.getFaceLength())
		{
			curY = map.getFaceLength() - 1;
			failed = true;
		}
		if(!failed)
		{
			isMoving = true;
			interpAmt = 0;
			moveVector = map.getTilePosition(currentFace, curX, curY).sub(map.getTilePosition(currentFace, curX - dx, curY - dy));
		}
		return failed;
	}

}
