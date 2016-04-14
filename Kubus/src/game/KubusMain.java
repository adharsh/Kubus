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
		Display window = new Display(800, 600, "wee");
		window.setVisible(true);
		Renderer render = window.getFrameBuffer();
		List<Vertex> vertices = new ArrayList<Vertex>();
		List<Integer> indices = new ArrayList<Integer>();

		vertices.add(new Vertex(new Vector4f(-1f, -1f, -1f, 1), new Vector4f(0, 0, 0, 0)));
		vertices.add(new Vertex(new Vector4f(-1f, 1f, -1f, 1), new Vector4f(0, 1, 0, 0)));
		vertices.add(new Vertex(new Vector4f(1f, -1f, -1f, 1), new Vector4f(1, 0, 0, 0)));
		vertices.add(new Vertex(new Vector4f(1f, 1f, -1f, 1), new Vector4f(1, 1, 0, 0)));
		vertices.add(new Vertex(new Vector4f(-1f, -1f, 1f, 1), new Vector4f(0, 0, 0, 0)));
		vertices.add(new Vertex(new Vector4f(-1f, 1f, 1f, 1), new Vector4f(0, 1, 0, 0)));
		vertices.add(new Vertex(new Vector4f(1f, -1f, 1f, 1), new Vector4f(1, 0, 0, 0)));
		vertices.add(new Vertex(new Vector4f(1f, 1f, 1f, 1), new Vector4f(1, 1, 0, 0)));

		indices.add(0);
		indices.add(1);
		indices.add(2);
		indices.add(2);
		indices.add(3);
		indices.add(1);
		
		indices.add(1);
		indices.add(5);
		indices.add(7);
		indices.add(1);
		indices.add(3);
		indices.add(7);
		
		indices.add(0);
		indices.add(4);
		indices.add(6);
		indices.add(0);
		indices.add(2);
		indices.add(6);
		
		indices.add(0);
		indices.add(1);
		indices.add(5);
		indices.add(0);
		indices.add(4);
		indices.add(5);
		
		indices.add(2);
		indices.add(3);
		indices.add(7);
		indices.add(2);
		indices.add(6);
		indices.add(7);
		
		indices.add(4);
		indices.add(5);
		indices.add(7);
		indices.add(4);
		indices.add(6);
		indices.add(7);
		Matrix4f projection = new Matrix4f().initPerspective((float)Math.toRadians(70.0f), 
				(float)render.getWidth() / (float)render.getHeight(), 0.1f, 1000.f);
		Bitmap texture;
		texture = new Bitmap(3, 3);
		texture.setPixel(0, 0, 0xFF00FF00);
		texture.setPixel(0, 1, 0xFF00FF00);
		texture.setPixel(0, 2, 0xFF00FF00);
		texture.setPixel(1, 0, 0xFF00FF00);
		texture.setPixel(1, 1, 0xFFFFFF00);
		texture.setPixel(1, 2, 0xFF00FF00);
		texture.setPixel(2, 0, 0xFF00FF00);
		texture.setPixel(2, 1, 0xFF00FF00);
		texture.setPixel(2, 2, 0xFF00FF00);
		Mesh cube = new Mesh(vertices, indices);
		while(true)
		{
			render.fill((byte)0);
			render.clearDepthBuffer();
			Matrix4f trans = new Matrix4f().initTranslation(0, 0, 3);
			Matrix4f rot = new Matrix4f().initRotation((float)Math.PI / 3.f, (float)Math.PI / 6.f, 0);
			Matrix4f tf = projection.mul(trans.mul(rot));
			cube.draw(render, tf, texture);
			window.swap();
		}
	}
}
