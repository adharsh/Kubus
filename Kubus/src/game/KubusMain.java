package game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.paint.Color;

import graphics.Bitmap;
import graphics.Matrix4f;
import graphics.Mesh;
import graphics.Renderer;
import graphics.Vector4f;
import graphics.Vertex;
import entity.EntityManager;
import graphics.Display;

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
		
		
	}
}
