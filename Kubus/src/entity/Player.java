package entity;

import graphics.Bitmap;
import graphics.Matrix4f;
import graphics.Mesh;
import graphics.Renderer;
import graphics.Transformation;
import graphics.Vector4f;
import graphics.Vertex;

import java.io.IOException;
import java.util.ArrayList;

public class Player extends Entity
{
	private static final double MAX_HEALTH = 100;
	private static Mesh entMesh;
	private double health;
	//face the player is on
	private int currentFace;
	private Kube map;
	private int curX, curY;
	private static Bitmap solidColor;
	
	static
	{
		ArrayList<Vertex> vertices = new ArrayList<Vertex>();
		ArrayList<Integer> indices = new ArrayList<Integer>();

		vertices.add(new Vertex(new Vector4f(-0.5f, 0, -0.5f, 1), new Vector4f(0, 0, 0, 0)));
		vertices.add(new Vertex(new Vector4f(-0.5f, 0, 0.5f, 1), new Vector4f(0, 1, 0, 0)));
		vertices.add(new Vertex(new Vector4f(0.0f, 0.5f, 0.0f, 1), new Vector4f(0.5f, 0.5f, 0, 0)));
		
		entMesh = new Mesh(vertices, indices);
		
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
		renderTransform.setPosition(new Vector4f(0,0,0));

		renderTransform.setScale(map.getTileLength(), 1, map.getTileLength());
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
	}
	
	//returns if successful move 
	//move fails if it hits wall
	public boolean move(int dx, int dy)
	{
		boolean failed = false;
		curX += dx;
		curY += dy;
		
		if(curX < 0)
		{
			curX = 0;
			failed = true;
		}
		if(curY < 0)
		{
			curY = 0;
			failed = true;
		}
		if(curX >= map.getFaceLength())
		{
			curX = map.getFaceLength() - 1;
			failed = true;
		}
		if(curY >= map.getFaceLength())
		{
			curY = map.getFaceLength() - 1;
			failed = true;
		}
		return failed;
	}

}
