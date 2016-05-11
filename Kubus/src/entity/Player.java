package entity;

import java.io.IOException;
import java.util.ArrayList;

import graphics.Bitmap;
import graphics.Matrix4f;
import graphics.Mesh;
import graphics.Renderer;
import graphics.RotationHandler;
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
		vertices.add(new Vertex(new Vector4f(0, 4.45f, 0, 1), new Vector4f(0.5f, 0.5f, 0, 0)));

		indices.add(0);
		indices.add(1);
		indices.add(2);
		
		entMesh = new Mesh(vertices, indices);
		
		try {
			solidColor = new Bitmap("res/whale.jpg");
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
		if(health < 0)
		{
			health = 0;
		}
	}
	
	public void resetHealth()
	{
		health = MAX_HEALTH;
	}
	
	public double getHealth()
	{
		return health;
	}
	
	public boolean isMovingToNextTile()
	{
		return isMoving;
	}
	
	//returns if move is done
	public boolean moveTick(float deltaTime)
	{
		float moveAmount;
		if(interpAmt + (4 * deltaTime) > 1.f)
		{
			moveAmount = 1.f - interpAmt;
			interpAmt = 1.f;
			isMoving = false;
		}
		else
		{
			moveAmount = (4 * deltaTime);
			interpAmt += moveAmount;
		}
		float xDist = moveVector.getX() * moveAmount;
		float yDist = moveVector.getY() * moveAmount;
		float zDist = moveVector.getZ() * moveAmount;
		
		setPosition(getPosition().add(new Vector4f(xDist, yDist, zDist, 0)));
		
		return !isMoving;
	}
	
	//initial condition: x goes along x axis, y goes along z axis
	
	private Vector4f getDXDY(int edge, int direction, RotationHandler r)
	{
		Vector4f dxdy;
		if(edge == RotationHandler.LEFT_EDGE)
		{
			dxdy = new Vector4f(r.getRightAxis());
		}
		else
		{
			dxdy = new Vector4f(r.getLeftAxis());
		}
		
		if(direction == RotationHandler.MOVE_UP)
		{
			dxdy = dxdy.mul(-1);
		}
		return dxdy;
	}
	
	//returns if successful move 
	//move fails if it hits wall
	public boolean move(int edge, int direction, RotationHandler r)
	{
		Vector4f dxdy = getDXDY(edge, direction, r);
		
		Tile thisTile = map.getNearestTile(getPosition());
		Tile nextTile = map.getNearestTile(getPosition().add(dxdy));
		
		int dx = nextTile.getXIndex() - thisTile.getXIndex();
		int dy = nextTile.getYIndex() - thisTile.getYIndex();
		if(map.wallInDirection(currentFace, curX, curY, dx, dy))
		{
			return false;
		}
		
		boolean failed = false;
		curX += dx;
		curY += dy;
		
		if(curX < 0 || curY < 0 || curX >= map.getFaceLength() || curY >= map.getFaceLength())
		{
			switchFace(edge, direction, dx, dy, r);
			return false;
		}
		if(!failed)
		{
			isMoving = true;
			interpAmt = 0;
			moveVector = map.getTilePosition(currentFace, curX, curY).sub(map.getTilePosition(currentFace, curX - dx, curY - dy));
		}
		return failed;
	}

	public void switchFace(int edge, int direction, int dx, int dy, RotationHandler r)
	{
		Tile t = map.getNearestTile(currentFace, curX + dx, curY + dy);
		currentFace = t.getFace();
		this.setPosition(t.getPosition());
		if(direction == RotationHandler.MOVE_UP)
		{
			direction = RotationHandler.MOVE_DOWN;
		}
		else
		{
			direction = RotationHandler.MOVE_UP;
		}
		r.initRotate(edge, direction, (float)Math.PI / 2.f, 1.f);
		curX = t.getXIndex();
		curY = t.getYIndex();
		renderTransform.setRotation(map.getFaceRotation(currentFace));
	}
}
