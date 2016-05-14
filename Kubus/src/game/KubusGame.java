package game;

import entity.Kube;
import entity.Player;
import entity.Tile;
import graphics.Camera;
import graphics.Display;
import graphics.Matrix4f;
import graphics.Renderer;
import graphics.RotationHandler;
import graphics.Vector4f;
import input.KeyInput;
import terrain.TerrainType;

public class KubusGame 
{
	private Camera camera;
	private KeyInput input;
	private RotationHandler rotHandler;
	private Player player;
	private Kube kube;
	private boolean gameOver;
	
	public KubusGame(Display display)
	{
		input = new KeyInput();
		display.addKeyListener(input);
		//temporary:
		kube = new Kube(5, 0.3f);
		
		for(int f=1;f<=6;f++)
		{
			for(int a=0;a<5;a++)
			{
				for(int b=0;b<5;b++)
				{
					if(a == 2 && b == 2)
					{
						new Tile(a, b, 0, TerrainType.FIRE, kube, f);
					}
					else
					{
						new Tile(a, b, 0, null, kube, f);
					}
				}
			}
		}
		player = new Player(1, 0, 0, kube);
		camera = new Camera(new Matrix4f().initPerspective((float)Math.toRadians(70.f), (float)display.getWidth() / (float)display.getHeight(), 
				0.1f, 1000.f));
		camera.setPosition(new Vector4f(1.5f, 1.5f, 1.5f, 1));
		camera.setRotation(new Vector4f(-2, -2, -2, 0), new Vector4f(-2, 2, -2, 0), 0);
		rotHandler = new RotationHandler(camera);
		kube.addWall(kube.getTileAt(6, 4, 3));
	}
	
	public void setGameOver(boolean over)
	{
		gameOver = over;
	}
	
	public void render(Renderer render)
	{
		Matrix4f viewProj = camera.getViewProjection();
		kube.renderFaces(render, viewProj);
		kube.renderWalls(render, viewProj);
		player.render(render, viewProj);
		//rect x bounds [100, 700]
		render.drawRectangle(100, 820, (int)((player.getHealth() / Player.MAX_HEALTH) * 700), 30, 0x0000FF);
	}
	
	public void runGameLogic(float deltaTime)
	{
		if(rotHandler.isRunning())
		{
			rotHandler.rotate(deltaTime);
		}
		else if(player.isDying())
		{
			player.dieTick(deltaTime);
			if(!player.isDying())
			{
				System.out.println("you lose");
				System.exit(0);
			}
		}
		else
		{
			if(player.isMovingToNextTile())
			{
				player.moveTick(deltaTime);
			}
			else
			{
				kube.tileAffectPlayer(player, this);
				
				if(input.isKeyDown(KeyInput.LEFT_ARROW))
				{
					player.move(RotationHandler.LEFT_EDGE, RotationHandler.MOVE_DOWN, rotHandler);
				}
				else if(input.isKeyDown(KeyInput.RIGHT_ARROW))
				{
					player.move(RotationHandler.LEFT_EDGE, RotationHandler.MOVE_UP, rotHandler);
				}
				else if(input.isKeyDown(KeyInput.UP_ARROW))
				{
					player.move(RotationHandler.RIGHT_EDGE, RotationHandler.MOVE_UP, rotHandler);
				}
				else if(input.isKeyDown(KeyInput.DOWN_ARROW))
				{
					player.move(RotationHandler.RIGHT_EDGE, RotationHandler.MOVE_DOWN, rotHandler);
				}
			}
			if(gameOver)
			{
				player.beginDeath();
			}
		}
	}
}
