package graphics;

public class Camera 
{
	private static final Vector4f UP = new Vector4f(0, 1, 0, 0);
	
	private Matrix4f projection;
	private Vector4f position;
	private Matrix4f rotation;
	private Vector4f fwd;
	private Vector4f axis;

	public Camera(Matrix4f projection)
	{
		this.projection = projection;
		rotation = new Matrix4f().initRotation(new Vector4f(0, 0, 1), UP);
		position = new Vector4f(0, 0, 0, 1);
	}
	
	public void moveForward(double d)
	{
		Vector4f fwd = new Vector4f(rotation.get(2, 0), rotation.get(2, 1), rotation.get(2, 2), 0).normalized().mul((float)d);
		position = position.add(fwd);
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

	public void setRotation(float x, float y, float z)
	{
		rotation.initRotation(x, y, z);
	}
	
	public void setRotation(Vector4f f, Vector4f a, float angle)
	{
		axis = a.normalized();
		fwd = f.normalized();
		fwd = fwd.rotate(axis, angle);
		rotation.initRotation(fwd, axis);
	}
	
	public Matrix4f getViewProjection()
	{
		Matrix4f cameraTranslation = new Matrix4f().initTranslation(-position.getX(), -position.getY(), -position.getZ());

		return projection.mul(rotation.mul(cameraTranslation));
	}
	
	public float spinAroundPoint(Vector4f point, Vector4f axis, float angle, float interp, float amt)
	{
		if(interp >= amt)
		{
			interp = amt;
			return amt;
		}
		if(interp + angle > amt)
		{
			angle = amt - interp;
			interp = amt;
		}
		else
		{
			interp += angle;
		}
		Vector4f diffVec = position.sub(point);
		diffVec.fixW();
		fwd = fwd.rotate(axis, angle);
		this.axis = this.axis.rotate(axis, angle);
		rotation.initRotation(fwd, this.axis);
		diffVec = diffVec.rotate(axis, angle);
		position = point.add(diffVec);
		return interp;
	}
}
