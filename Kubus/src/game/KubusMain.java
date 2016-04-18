package game;

import entity.EntityManager;
import entity.Tile;
import graphics.Renderer;
import graphics.Vector4f;
import graphics.Camera;
import graphics.Display;
import graphics.Matrix4f;


public class KubusMain
{
	private EntityManager manager;
	
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
		Display w = new Display(600, 600, "wee");
		w.setVisible(true);
		Renderer f = w.getFrameBuffer();
		Tile t = new Tile(0, 0, 0, null, null, 0);
		Camera c = new Camera(new Matrix4f().initPerspective((float)Math.toRadians(90.0f),
			   	(float)f.getWidth()/(float)f.getHeight(), 0.1f, 1000.0f));
		c.setPosition(new Vector4f(2, 2, 2, 1));
		c.lookAt(new Vector4f(0, 0, 0, 1));
		while(true)
		{
			f.fill((byte)0);
			f.clearDepthBuffer();
			t.render(f, c.getViewProjection());
			w.swap();
		}
		
	}
}
