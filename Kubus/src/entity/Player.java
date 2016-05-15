package entity;

import java.io.IOException;

import graphics.Bitmap;
import graphics.Matrix4f;
import graphics.Mesh;
import graphics.Renderer;
import graphics.RotationHandler;
import graphics.Vector4f;
import input.QMFLoader;
import terrain.TerrainType;
import terrain.WaterTerrain;

public class Player extends Entity
{
	public static final double MAX_HEALTH = 100;
	private static Mesh entMesh;
	private double health;
	//face the player is on
	private int currentFace;
	private Kube map;


	private int lastMoveEdge;
	private int lastMoveDirection;
	
	private int curX, curY;
	private int prevX, prevY;
	private static Bitmap playerTexture;

		private float heightInterpAmt;
		private boolean isMovingUp;
		private boolean isMovingDown;
	
		private boolean dying;
		private float deathInterpAmt;
		//1 second to die
	
		private boolean isMoving;
		//vector which describes move at inception
		private Vector4f moveVector;
		private float interpAmt;
		private boolean slowMove;
		//.25 seconds to move

	static
	{

		entMesh = QMFLoader.loadQMF("res/QMF/player.qmf");

		try 
		{
			playerTexture = new Bitmap("res/human.jpg");
		}
		catch(IOException e)
		{
			playerTexture = new Bitmap(1, 1);
		}
	}

	public Player(int startFace, int startX, int startY, Kube map) 
	{
		isMovingUp = false;
		isMovingDown = false;
		health = MAX_HEALTH;
		currentFace = startFace;
		this.map = map;
		curX = startX;
		curY = startY;
		prevX = curX;
		prevY = curY;
		lastMoveDirection = -1;
		setPosition(map.getTilePosition(startFace, startX, startY));

		renderTransform.setScale(map.getTileLength(), map.getTileLength(), map.getTileLength());
	}

	@Override
	public void render(Renderer r, Matrix4f viewProjection)
	{
		super.render(r, viewProjection);
		entMesh.draw(r, viewProjection, renderTransform.getTransformation(), playerTexture);
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
		fwd = fwd.rotate(up, -deltaTime * 18 * (float)Math.pow(deathInterpAmt, 2));
		rt = rt.rotate(up, -deltaTime * 18 * (float)Math.pow(deathInterpAmt, 2));
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

	private boolean moveTickSlow(float deltaTime)
	{
		float moveAmount;
		
		if(interpAmt + (deltaTime) > 1.f)
		{
			moveAmount = 1.f - interpAmt;
			interpAmt = 1.f;
		}
		else
		{
			moveAmount = (deltaTime);
			interpAmt += moveAmount;
		}
		float xDist = moveVector.getX() * moveAmount;
		float yDist = moveVector.getY() * moveAmount;
		float zDist = moveVector.getZ() * moveAmount;

		setPosition(getPosition().add(new Vector4f(xDist, yDist, zDist, 0)));

		if(interpAmt != 1.f)
		{
			return false;
		}
		if(!downHeightChangeTick(deltaTime))
		{
			return false;
		}
		isMoving = false;
		return true;
	}
	
	private boolean upHeightChangeTick(float deltaTime)
	{
		if(!isMovingUp || heightInterpAmt == 1)
		{
			return true;
		}
		if(heightInterpAmt + (deltaTime * 4) >= 1.f)
		{
			heightInterpAmt = 1;
		}
		else
		{
			heightInterpAmt += (deltaTime * 4);
		}
		Tile prevTile = map.getTileAt(currentFace, prevX, prevY);
		Tile curTile = map.getTileAt(currentFace, curX, curY);
		Vector4f upDirection = curTile.getHeightOffset().sub(prevTile.getHeightOffset());
		upDirection = upDirection.mul(heightInterpAmt);
		setPosition(prevTile.getPosition().add(prevTile.getHeightOffset()).add(upDirection));
		return heightInterpAmt == 1;
	}
	
	private boolean downHeightChangeTick(float deltaTime)
	{
		if(!isMovingDown || heightInterpAmt == 1.f)
		{
			return true;
		}
		if(heightInterpAmt + (deltaTime * 4) >= 1.f)
		{
			heightInterpAmt = 1;
		}
		else
		{
			heightInterpAmt += (deltaTime * 4);
		}
		Tile prevTile = map.getTileAt(currentFace, prevX, prevY);
		Tile curTile = map.getTileAt(currentFace, curX, curY);
		Vector4f downDirection = curTile.getHeightOffset().sub(prevTile.getHeightOffset());
		downDirection = downDirection.mul(1.f - heightInterpAmt);
		setPosition(curTile.getPosition().add(curTile.getHeightOffset()).sub(downDirection));
		return heightInterpAmt == 1;
	}
	
	//returns if move is done
	public boolean moveTick(float deltaTime)
	{
		if(!upHeightChangeTick(deltaTime))
		{
			return false;
		}
		if(slowMove)
		{
			return moveTickSlow(deltaTime);
		}
		float moveAmount;
		
		
		
		if(interpAmt + (deltaTime) >= .25f)
		{
			moveAmount = .25f - interpAmt;
			interpAmt = .25f;
		}
		else
		{
			moveAmount = (deltaTime);
			interpAmt += moveAmount;
		}
		float xDist = moveVector.getX() * moveAmount * 4;
		float yDist = moveVector.getY() * moveAmount * 4;
		float zDist = moveVector.getZ() * moveAmount * 4;

		setPosition(getPosition().add(new Vector4f(xDist, yDist, zDist, 0)));

		if(interpAmt != 0.25f)
		{
			return false;
		}
		if(!downHeightChangeTick(deltaTime))
		{
			return false;
		}
		isMoving = false;
		return true;
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

	public void move(int edge, int direction, RotationHandler r)
	{
		lastMoveEdge = edge;
		lastMoveDirection = direction;
		Vector4f dxdy = getDXDY(edge, direction, r);

		Tile thisTile = map.getNearestTile(getPosition(), currentFace);
		Tile nextTile = map.getNearestTile(getPosition().add(dxdy), currentFace);

		if(thisTile == null)
		{
			return;
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
				return;
			}
			dx = thisTile.getXIndex() - nextTile.getXIndex();
			dy = thisTile.getYIndex() - nextTile.getYIndex();
		}

		if(map.wallInDirection(currentFace, curX, curY, dx, dy))
		{
			return;
		}
		if(thisTile.getHeight() == Tile.TILEHEIGHT_LOW && nextTile.getHeight() == Tile.TILEHEIGHT_HIGH)
		{
			return;
		}

		int tempX = prevX;
		int tempY = prevY;
		prevX = curX;
		prevY = curY;
		curX += dx;
		curY += dy;

		if(curX < 0 || curY < 0 || curX >= map.getFaceLength() || curY >= map.getFaceLength())
		{
			if(map.wallOnEdge(thisTile))
			{
				prevX = tempX;
				prevY = tempY;
				curX -= dx;
				curY -= dy;
				return;
			}
			if(map.wallOnEdge(thisTile, dx, dy))
			{
				prevX = tempX;
				prevY = tempY;
				curX -= dx;
				curY -= dy;
				return;
			}
			switchFace(edge, direction, r);
			return;
		}
		isMoving = true;
		interpAmt = 0;
		heightInterpAmt = 0;
		if(nextTile.getHeight() == Tile.TILEHEIGHT_HIGH && thisTile.getHeight() == Tile.TILEHEIGHT_NORMAL ||
				nextTile.getHeight() == Tile.TILEHEIGHT_NORMAL && thisTile.getHeight() == Tile.TILEHEIGHT_LOW)
		{
			isMovingUp = true;
		}
		else
		{
			isMovingUp = false;
		}
		if(nextTile.getHeight() == Tile.TILEHEIGHT_LOW && thisTile.getHeight() == Tile.TILEHEIGHT_NORMAL ||
				nextTile.getHeight() == Tile.TILEHEIGHT_NORMAL && thisTile.getHeight() == Tile.TILEHEIGHT_HIGH ||
				nextTile.getHeight() == Tile.TILEHEIGHT_LOW && thisTile.getHeight() == Tile.TILEHEIGHT_HIGH)
		{
			isMovingDown = true;
		}
		else
		{
			isMovingDown = false;
		}
		moveVector = createMoveVector(curX, curY, curX - dx, curY - dy);
		thisTile.getTerrain().onPlayerLeaveTile(this);
	}

	private Vector4f createMoveVector(int nextX, int nextY, int x, int y)
	{
		Vector4f next = map.getTilePosition(currentFace, nextX, nextY).sub(map.getTilePosition(currentFace, x, y));
		Tile nextTile = map.getTileAt(currentFace, x, y);
		Tile thisTile = map.getTileAt(currentFace, nextX, nextY);
		if(nextTile.getTerrain().getTerrainType() == TerrainType.WATER && 
				thisTile.getTerrain().getTerrainType() == TerrainType.WATER)
		{
			slowMove = true;
			return next;
		}
		if(nextTile.getTerrain().getTerrainType() == TerrainType.WATER)
		{
			Vector4f upDirection = ((WaterTerrain)nextTile.getTerrain()).getWaterOffset(1);
			upDirection.zeroW();
			slowMove = true;
			return next.add(upDirection);
		}
		if(thisTile.getTerrain().getTerrainType() == TerrainType.WATER)
		{
			Vector4f downDirection = ((WaterTerrain)thisTile.getTerrain()).getWaterOffset(-1);
			downDirection.zeroW();
			slowMove = true;
			return next.add(downDirection);
		}
		slowMove = false;
		return next;
	}
	
	public void switchFace(int edge, int direction, RotationHandler r)
	{
		//TODO: optional to change me
//		lastMoveDirection = -1;
		Tile t = map.getNearestTile(currentFace, curX, curY);
		currentFace = t.getFace();
		t.getTerrain().onPlayerLeaveTile(this);
		
		if(t.getTerrain().getTerrainType() == TerrainType.WATER)
		{
			Vector4f downDirection = ((WaterTerrain)t.getTerrain()).getWaterOffset(-1);
			downDirection.zeroW();
			setPosition(t.getPosition().add(downDirection).add(t.getHeightOffset()));
		}
		else
		{
			this.setPosition(t.getPosition().add(t.getHeightOffset()));
		}
		
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
		prevX = curX;
		prevY = curY;
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

	public int getPrevX()
	{
		return prevX;
	}

	public int getPrevY()
	{
		return prevY;
	}

	public float getHeightInterpAmt()
	{
		return heightInterpAmt;
	}
	
	public boolean isMovingUp()
	{
		return isMovingUp;
	}
	
	public boolean isMovingDown()
	{
		return isMovingDown;
	}
	
	public int getLastMoveEdge()
	{
		return lastMoveEdge;
	}

	public int getLastMoveDirection() 
	{
		return lastMoveDirection;
	}
}
