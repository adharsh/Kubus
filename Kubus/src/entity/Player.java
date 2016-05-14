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
import input.QMFLoader;

public class Player extends Entity
{
	public static final double MAX_HEALTH = 100;
	private static Mesh entMesh;
	private double health;
	//face the player is on
	private int currentFace;
	private Kube map;
	private int curX, curY;
	private static Bitmap solidColor;
	
		private boolean dying;
		private float deathInterpAmt;
		//1 second to die
		
		private boolean isMoving;
		//vector which describes move at inception
		private Vector4f moveVector;
		private float interpAmt;
		//.25 seconds to move
		
	static
	{
//		ArrayList<Vertex> vertices = new ArrayList<Vertex>();
//		ArrayList<Integer> indices = new ArrayList<Integer>();
//
//		vertices.add(new Vertex(new Vector4f(-0.3f, 0, 0, 1), new Vector4f(0, 0, 0, 0)));
//		vertices.add(new Vertex(new Vector4f(0.3f, 0, 0, 1), new Vector4f(0, 1, 0, 0)));
//		vertices.add(new Vertex(new Vector4f(0, .45f, 0, 1), new Vector4f(0.5f, 0.5f, 0, 0)));
//
//		indices.add(0);
//		indices.add(1);
//		indices.add(2);
		
		entMesh = QMFLoader.loadQMF("res/QMF/player.qmf");//new Mesh(vertices, indices);
		
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
	
	public boolean isDying()
	{
		return dying;
	}
	
	public void beginDeath()
	{
		dying = true;
		deathInterpAmt = 0;
	}
	
	public void dieTick(float deltaTime)
	{
		if(deathInterpAmt + deltaTime > 2.f)
		{
			deathInterpAmt = 3;
			dying = false;
		}
		else
		{
			deathInterpAmt += deltaTime;
		}
		Matrix4f rot = renderTransform.getRotation();
		
		Vector4f fwd = rot.getRow(2);
		Vector4f up = rot.getRow(1);
		Vector4f rt = rot.getRow(0);
		fwd = fwd.rotate(up, -deltaTime * 9);
		rt = rt.rotate(up, -deltaTime * 9);
		rot.set(2, 0, fwd.getX());
		rot.set(2, 1, fwd.getY());
		rot.set(2, 2, fwd.getZ());
		rot.set(0, 0, rt.getX());
		rot.set(0, 1, rt.getY());
		rot.set(0, 2, rt.getZ());
		renderTransform.setPosition(renderTransform.getPosition().add(
				up.mul((((deathInterpAmt / 2.f) * (deathInterpAmt / 2.f)) 
						/ map.getTileLength()) * deltaTime)));
		renderTransform.setScale(map.getTileLength(),
				map.getTileLength() * ((2.f - deathInterpAmt) / 2.f), map.getTileLength());
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
			dxdy = dxdy.mul(-map.getTileLength());
		}
		else
		{
			dxdy = dxdy.mul(map.getTileLength());
		}
		return dxdy;
	}
	
	//returns if successful move 
	//move fails if it hits wall
	public boolean move(int edge, int direction, RotationHandler r)
	{
		Vector4f dxdy = getDXDY(edge, direction, r);
		
		Tile thisTile = map.getNearestTile(getPosition(), currentFace);
		Tile nextTile = map.getNearestTile(getPosition().add(dxdy), currentFace);
		
		if(thisTile == null)
		{
			return false;
		}
		
		int dx;
		int dy;
		if(nextTile != null)
		{
			dx = nextTile.getXIndex() - thisTile.getXIndex();
			dy = nextTile.getYIndex() - thisTile.getYIndex();
		}
		else//solution: -dx, -dy of tile in opp dir
		{
			nextTile = map.getNearestTile(getPosition().sub(dxdy), currentFace);
			if(nextTile == null)
			{
				return false;
			}
			dx = thisTile.getXIndex() - nextTile.getXIndex();
			dy = thisTile.getYIndex() - nextTile.getYIndex();
		}
		
		if(map.wallInDirection(currentFace, curX, curY, dx, dy))
		{
			return false;
		}
		
		boolean failed = false;
		curX += dx;
		curY += dy;
		
		if(curX < 0 || curY < 0 || curX >= map.getFaceLength() || curY >= map.getFaceLength())
		{
			if(map.wallOnEdge(thisTile))
			{
				curX -= dx;
				curY -= dy;
				return false;
			}
			if(map.wallOnEdge(thisTile, dx, dy))
			{
				curX -= dx;
				curY -= dy;
				return false;
			}
			if(map.wallOnEdge(map.getNearestTile(currentFace, curX, curY)))
			{
				curX -= dx;
				curY -= dy;
				return false;
			}
			switchFace(edge, direction, r);
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

	public void switchFace(int edge, int direction, RotationHandler r)
	{
		Tile t = map.getNearestTile(currentFace, curX, curY);
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
	
	public int getX()
	{
		return curX;
	}
	
	public int getY()
	{
		return curY;
	}
	
	public int getFace()
	{
		return currentFace;
	}
}
