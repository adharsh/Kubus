package entity;

import graphics.Renderer;
import graphics.Vector4f;

public class Entity {
	
	private Vector4f position;
	private Vector4f velocity;
	
	public Entity() 
	{
		
	}
	
	public void tick() 
	{
		
	}
	
	public void render(Renderer r) 
	{
		//TODO
	}
	
	public void setVelocity(Vector4f velocity)
	{
		this.velocity = velocity;
	}

	public void setPosition(Vector4f position)
	{
		this.position = position;
	}
	
	public Vector4f getPosition()
	{
		return position;
	}
	
	public Vector4f getVelocity()
	{
		return velocity;
	}
}
