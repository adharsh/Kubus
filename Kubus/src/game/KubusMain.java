package game;

import graphics.Display;
import graphics.Renderer;


public class KubusMain
{
	private KubusGame game;
	private Display display;
	private Renderer renderer;
	
	public KubusMain()
	{
		display = new Display(900, 900, "Kubus");
		renderer = display.getFrameBuffer();
		game = new KubusGame(display);
	}
	
	public static void main(String[] args)
	{
		new KubusMain().mainLoop();
	}
	
	public void mainLoop()
	{
		display.setVisible(true);
		float deltaTime = 0;
		long lastTime = System.currentTimeMillis();
		while(true)
		{
			deltaTime = (float)(System.currentTimeMillis() - lastTime) / 1000.f;
			game.runGameLogic(deltaTime);
			lastTime = System.currentTimeMillis();
			
			renderer.fill((byte)0);
			renderer.clearDepthBuffer();
			game.render(renderer);
			display.swap();
			try
			{
				Thread.sleep(5);
			}
			catch(InterruptedException e) 
			{
				e.printStackTrace();
			}
		}
		
	}
}
