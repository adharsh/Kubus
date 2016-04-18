package graphics;

public class Camera 
{
	private static final Vector4f UP = new Vector4f(0, 1, 0, 0);
	
	private Matrix4f projection;
	private Vector4f position;
	private Matrix4f rotation;

	public Camera(Matrix4f projection)
	{
		this.projection = projection;
		rotation = new Matrix4f().initRotation(new Vector4f(0, 0, 1), UP);
		position = new Vector4f(0, 0, 0, 1);
	}
	
	public Vector4f getPosition()
	{
		return position;
	}
	
	public void lookAt(Vector4f point)
	{
		rotation = new Matrix4f().initRotation(point.sub(position).normalized(), UP);
	}
	
	public void setPosition(Vector4f position)
	{
		this.position = position;
	}
	
	public Matrix4f getViewProjection()
	{
		Matrix4f cameraTranslation = new Matrix4f().initTranslation(-position.getX(), -position.getY(), -position.getZ());

		return projection.mul(rotation.mul(cameraTranslation));
	}
}
