package graphics;

public class Transformation 
{
	private Vector4f position;
	private Vector4f scale;
	private Matrix4f rotation;
	
	public Transformation()
	{
		position = new Vector4f(0, 0, 0, 0);
		scale = new Vector4f(1, 1, 1, 1);
		rotation = new Matrix4f().initRotation(0, 0, 0);
	}
	
	public void setPosition(Vector4f position)
	{
		this.position = position;
	}
	
	public void setRotation(Vector4f fwd, Vector4f up)
	{
		rotation.initRotation(fwd, up);
	}
	
	public void setRotation(float x, float y, float z)
	{
		rotation.initRotation(x, y, z);
	}
	
	public void lookAt(Vector4f point, Vector4f up)
	{
		rotation.initRotation(point.sub(position).normalized(), up);
	}
	
	public void setScale(float x, float y, float z)
	{
		scale.setXYZW(x, y, z, 1);
	}
	
	public Matrix4f getTransformation()
	{
		Matrix4f translation = new Matrix4f().initTranslation(position.getX(), position.getY(), position.getZ());
		Matrix4f scaleM = new Matrix4f().initScale(scale.getX(), scale.getY(), scale.getZ());
		
		return translation.mul(rotation.mul(scaleM));
		
	}
}
