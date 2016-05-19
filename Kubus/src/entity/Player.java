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
	
	private boolean hasWon;
	
		private float climbHeightInterpAmt;
		private float fallHeightInterpAmt;
		private boolean isMovingUp;
		private boolean isClimbingOutOfWater;
		private boolean isMovingDown;
		private boolean isFallingIntoWater;
	
		private boolean dying;
		private float deathInterpAmt;
		//1 second to die
	
		private boolean isMoving;
		private float interpAmt;
		private Vector4f currentHeightOffset;
		private float moveTime;
		//move time in seconds (can be slowed)

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
		hasWon = false;
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
		currentHeightOffset = new Vector4f(0, 0, 0, 0);
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
	
	private boolean upHeightChangeTick(float deltaTime)
	{
		if((!isMovingUp && !isClimbingOutOfWater) || climbHeightInterpAmt == 1)
		{
			return true;
		}
		Tile prevTile = map.getTileAt(currentFace, prevX, prevY);
		Tile curTile = map.getTileAt(currentFace, curX, curY);
		Vector4f deltaMove = new Vector4f(0, 0, 0, 0);
		if(isClimbingOutOfWater)
		{
			deltaMove = deltaMove.add(prevTile.getTerrain().getWaterOffset(1));
		}
		if(isMovingUp)
		{
			deltaMove = deltaMove.add(curTile.getHeightOffset().sub(prevTile.getHeightOffset()));
		}
		if(climbHeightInterpAmt + (deltaTime * 4) >= 1.f)
		{
			climbHeightInterpAmt = 1;
			currentHeightOffset = deltaMove.add(prevTile.getHeightOffset().add(prevTile.getTerrain().getWaterOffset(-1)));
		}
		else
		{
			climbHeightInterpAmt += (deltaTime * 4);
		}
		deltaMove = deltaMove.mul(climbHeightInterpAmt);
		setPosition(prevTile.getPosition().add(prevTile.getHeightOffset()
				.add(prevTile.getTerrain().getWaterOffset(-1))).add(deltaMove));
		return climbHeightInterpAmt == 1;
	}
	
	private boolean downHeightChangeTick(float deltaTime)
	{
		if((!isMovingDown && !isFallingIntoWater) || fallHeightInterpAmt == 1.f)
		{
			return true;
		}
		if(fallHeightInterpAmt + (deltaTime * 4) >= 1.f)
		{
			fallHeightInterpAmt = 1;
		}
		else
		{
			fallHeightInterpAmt += (deltaTime * 4);
		}
		Tile prevTile = map.getTileAt(currentFace, prevX, prevY);
		Tile curTile = map.getTileAt(currentFace, curX, curY);
		Vector4f deltaMove = new Vector4f(0, 0, 0, 0);
		if(isFallingIntoWater)
		{
			deltaMove = deltaMove.add(curTile.getTerrain().getWaterOffset(-1));
		}
		if(isMovingDown)
		{
			deltaMove = deltaMove.add(curTile.getHeightOffset().sub(prevTile.getHeightOffset()));
		}
		deltaMove = deltaMove.mul(1.f - fallHeightInterpAmt);
		setPosition(curTile.getPosition().add(curTile.getHeightOffset().add(curTile.getTerrain().getWaterOffset(-1))).sub(deltaMove));
		return fallHeightInterpAmt == 1;
	}
	
	//returns if move is done
	public boolean moveTick(float deltaTime)
	{
		if(!upHeightChangeTick(deltaTime))
		{
			return false;
		}
		
		if(interpAmt + (deltaTime / moveTime) >= 1.f)
		{
			interpAmt = 1.f;
		}
		else
		{
			interpAmt += (deltaTime / moveTime);
		}

		Tile prevTile = map.getTileAt(currentFace, prevX, prevY);
		Tile curTile = map.getTileAt(currentFace, curX, curY);
		
		Vector4f deltaPos = curTile.getPosition()
				.sub(prevTile.getPosition())
				.mul(interpAmt);
		
		setPosition(deltaPos.add(prevTile.getPosition().add(currentHeightOffset)));

		if(interpAmt != 1.f)
		{
			return false;
		}
		if(!downHeightChangeTick(deltaTime))
		{
			return false;
		}
		isMoving = false;
		if(map.getTileAt(currentFace, curX, curY).getHeight() == Tile.TILEHEIGHT_LOW &&
				map.getTileAt(currentFace, prevX, prevY).getHeight() == Tile.TILEHEIGHT_HIGH &&
				!isFallingIntoWater)
		{
			this.takeHealth(25);
		}
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

		Tile thisTile = map.getTileAt(currentFace, curX, curY);
		Tile nextTile = map.getNearestTile(thisTile.getPosition().add(dxdy), currentFace);


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
		climbHeightInterpAmt = 0;
		fallHeightInterpAmt = 0;
		generateHeightChangeInfo(thisTile, nextTile);
		if(thisTile.getTerrain().getTerrainType() == TerrainType.WATER ||
				nextTile.getTerrain().getTerrainType() == TerrainType.WATER)
		{
			moveTime = 1.f;
		}
		else
		{
			moveTime = 0.25f;
		}
		currentHeightOffset = thisTile.getTerrain().getWaterOffset(-1).add(thisTile.getHeightOffset());
		thisTile.getTerrain().onPlayerLeaveTile(this);
	}
	
	private void generateHeightChangeInfo(Tile thisTile, Tile nextTile)
	{
		int nth = nextTile.getHeight();
		int tth = thisTile.getHeight();
		boolean ttw = thisTile.getTerrain().getTerrainType() == TerrainType.WATER;
		boolean ntw = nextTile.getTerrain().getTerrainType() == TerrainType.WATER;
		
		
		if(nth > tth)
		{
			isMovingUp = true;
			isMovingDown = false;
		}
		else if(nth == tth)
		{
			isMovingUp = false;
			isMovingDown = false;
		}
		else
		{
			isMovingUp = false;
			isMovingDown = true;
		}
		
		if(ttw && !ntw)
		{
			isClimbingOutOfWater = true;
			isFallingIntoWater = false;
		}
		else if(!ttw && ntw)
		{
			isClimbingOutOfWater = false;
			isFallingIntoWater = true;
		}
		else if(ttw && ntw)
		{
			if(isMovingDown || isMovingUp)
			{
				isClimbingOutOfWater = true;
				isFallingIntoWater = true;
			}
			else
			{
				isClimbingOutOfWater = false;
				isFallingIntoWater = false;
			}
		}
		else
		{
			isClimbingOutOfWater = false;
			isFallingIntoWater = false;
		}
		
		
	}
	
	public void switchFace(int edge, int direction, RotationHandler r)
	{
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

	public float getClimbHeightInterpAmt()
	{
		return climbHeightInterpAmt;
	}
	
	public float getFallHeightInterpAmt()
	{
		return fallHeightInterpAmt;
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
	
	public boolean getWon()
	{
		return hasWon;
	}
	
	public void setWon(boolean won)
	{
		hasWon = won;
	}
}
