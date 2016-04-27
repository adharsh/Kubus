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


public class KubusMain
{
	private EntityManager manager;
	private Camera c;

	Kube kube = new Kube(5, .2f);
	public KubusMain()
	{
		manager = new EntityManager();
	}
	
	public static void main(String[] args)
	{
		new KubusMain().mainLoop();
	}
	
	public void mainLoop()
	{
		Display w = new Display(900, 900, "wee");
		w.setVisible(true);
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
		c.setPosition(new Vector4f(2, 2, 2, 1));
		
		float p = (float) (Math.PI);
		float interpAmt = 0;
		float i2 = 0;
		c.setRotation(new Vector4f(-2, -2, -2, 0), new Vector4f(-2, 2, -2, 0), 0);
		Player pl = new Player(0, 0, 0, kube);
		while(true)
		{
			interpAmt = c.spinAroundPoint(new Vector4f(0, 0, 1, 1), new Vector4f(0, 0, 1, 1), .01111f, interpAmt, p / 2);
			if(interpAmt == p / 2)
			{
				i2 = c.spinAroundPoint(new Vector4f(1, 0, 0, 1), new Vector4f(1, 0, 0, 1), .01111f, i2, p / 2);
			}
			f.fill((byte)0);
			f.clearDepthBuffer();
		//	kube.renderFaces(f, c.getViewProjection());
			pl.render(f, c.getViewProjection());
			w.swap();
		}
		
	}
}
