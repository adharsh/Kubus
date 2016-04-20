package entity;

import graphics.Matrix4f;
import graphics.Renderer;
import graphics.Transformation;
import graphics.Vector4f;

public class Entity {
	
	private Vector4f position;
	private Vector4f velocity;
	protected Transformation renderTransform;
	
	public Entity() 
	{
		renderTransform = new Transformation();
		position = new Vector4f(0, 0, 0, 1);
		velocity = new Vector4f(0, 0, 0, 1);
	}
	
	public void tick() 
	{
		
	}

	public void render(Renderer r, Matrix4f viewProjection)
	{
		//TODO
	}
	

	public Transformation getTransform()
	{
		return renderTransform;
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
