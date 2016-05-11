/*
 * 
 */

package graphics;

public class RotationHandler 
{
	public static final int RIGHT_EDGE = 0;
	public static final int LEFT_EDGE = 1;
	public static final int MOVE_UP = 2;
	public static final int MOVE_DOWN = 3;
//	public static final float dtheta = 0.0111f; 
	
	private Camera camera;
	private boolean isRunning = false;	
	private float interpTheta;
	private float netRot;
	private float netTime;
	private static Vector4f point = new Vector4f(0, 0, 0, 0);
	private Vector4f leftAxis;
	private Vector4f rightAxis;
	
	
		private Vector4f useAxis;
	
	public Vector4f getLeftAxis()
	{
		return leftAxis;
	}
	
	public Vector4f getRightAxis()
	{
		return rightAxis;
	}
		
	public RotationHandler(Camera camera) 
	{
		this.camera = camera;
		leftAxis = new Vector4f(0, 0, 1, 0);
		rightAxis = new Vector4f(1, 0, 0, 0);
		useAxis = null;
	}

	public void initRotate(int edge, int direction, float netRotRadians, float netTime)
	{
		isRunning = true;
		this.netTime = netTime;
		interpTheta = 0;
		if(edge == LEFT_EDGE)
		{
			useAxis = new Vector4f(leftAxis);
		}
		else if(edge == RIGHT_EDGE)
		{
			useAxis = new Vector4f(rightAxis);
		}
		if(direction == MOVE_UP)
		{
			netRot = -netRotRadians;
		}
		else if(direction == MOVE_DOWN)
		{
			netRot = netRotRadians;
		}
		if(edge == RIGHT_EDGE)
		{
			netRot = -netRot;
		}
	}
	
	public void rotate(float dt)
	{
		float rotAmt = (dt * netRot) / netTime;
		interpTheta = camera.spinAroundPoint(point, useAxis, rotAmt, interpTheta, netRot);
		leftAxis = leftAxis.rotate(useAxis, rotAmt);
		rightAxis = rightAxis.rotate(useAxis, rotAmt);
		if(interpTheta == netRot)
		{
			leftAxis.round();
			rightAxis.round();
			isRunning = false;
		}
		
	}
	
	public boolean isRunning()
	{
		return isRunning;
	}

}