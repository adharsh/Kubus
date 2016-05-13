package game;

import entity.Kube;
import entity.Player;
import graphics.Camera;
import graphics.Display;
import graphics.Renderer;
import graphics.RotationHandler;
import input.KeyInput;

public class KubusGame 
{
	private Camera camera;
	private KeyInput input;
	private RotationHandler rotHandler;
	private Player player;
	private Kube kube;
	
	public KubusGame(Display display)
	{
		input = new KeyInput();
		
		//temporary:
		kube = new Kube(5, 0.2f);
		
		for(int f=1;f<=6;f++)
		{
			for(int a=0;a<6;a++)
			{
				for(int b=0;b<6;b++)
				{
					
				}
			}
		}
		player = new Player(1, 0, 0, kube);
	}
	
	public void render(Renderer render)
	{
		kube.renderFaces(render, camera.getViewProjection());
		player.render(render, camera.getViewProjection());
	}
	
	public void runGameLogic(float deltaTime)
	{
		if(rotHandler.isRunning())
		{
			rotHandler.rotate(deltaTime);
		}
		else
		{
			if(player.isMovingToNextTile())
			{
				player.moveTick(deltaTime);
			}
			else
			{
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
		}
	}
}
