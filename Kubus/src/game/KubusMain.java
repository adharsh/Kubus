package game;

import entity.EntityManager;
import entity.Kube;
import entity.Player;
import entity.Tile;
import graphics.Camera;
import graphics.Display;
import graphics.Matrix4f;
import graphics.Renderer;
import graphics.Vector4f;
import input.KeyInput;


public class KubusMain
{
	private EntityManager manager;
	private Camera c;
	private KeyInput input;

	private Kube kube = new Kube(5, .2f);
	
	public KubusMain()
	{
		manager = new EntityManager();
		input = new KeyInput();
	}
	
	public static void main(String[] args)
	{
		new KubusMain().mainLoop();
	}
	
	public void mainLoop()
	{
		Display w = new Display(900, 900, "wee");
		w.setVisible(true);
		w.addKeyListener(input);
		Renderer f = w.getFrameBuffer();
		
		for(int a=1;a<=6;a++)
		{
			for(int x=0;x<5;x++)
			{
				for(int y=0;y<5;y++)
				{
					new Tile(x, y, 0, null, kube, a);
				}
			}
		}
		
		
		c = new Camera(new Matrix4f().initPerspective((float)Math.toRadians(70.0f),
			   	(float)f.getWidth()/(float)f.getHeight(), 0.1f, 1000.0f));
		c.setPosition(new Vector4f(1.5f, 1.5f, 1.5f, 1));
	
		float p = (float) (Math.PI);
		float interpAmt = 0;
		c.setRotation(new Vector4f(-2, -2, -2, 0), new Vector4f(-2, 2, -2, 0), 0);
		Player pl = new Player(Kube.TOP, 0, 0, kube);
		float dt = 0;
		long lastTime = System.currentTimeMillis();
		while(true)
		{
			interpAmt = c.spinAroundPoint(new Vector4f(0, 0, 1, 1), new Vector4f(0, 0, 1, 1), dt, interpAmt, p / 2);
			dt = (float)(System.currentTimeMillis() - lastTime) / 1000.f;
			lastTime = System.currentTimeMillis();
			if(pl.isMovingToNextTile())
			{
				pl.moveTick(dt);
			}
			else
			{
				if(input.isKeyDown(KeyInput.DOWN_ARROW))
				{
					pl.move(1, 0);
				}
				else if(input.isKeyDown(KeyInput.UP_ARROW))
				{
					pl.move(-1, 0);
				}
				else if(input.isKeyDown(KeyInput.LEFT_ARROW))
				{
					pl.move(0, -1);
				}
				else if(input.isKeyDown(KeyInput.RIGHT_ARROW))
				{
					pl.move(0, 1);
				}
			}
			
			f.fill((byte)0);
			f.clearDepthBuffer();
			kube.renderFaces(f, c.getViewProjection());
			pl.render(f, c.getViewProjection());
			w.swap();
			try
			{
				Thread.sleep(5);
			} catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
		}
		
	}
}
