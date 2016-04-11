package graphics;

public class Edge
{
	private float x;
	private float xStep;
	private int yStart;
	private int yEnd;
	private float texCoordX;
	private float texCoordXStep;
	private float texCoordY;
	private float texCoordYStep;
	private float oneOverZ;
	private float oneOverZStep;
	private float depth;
	private float depthStep;

	public float getX() { return x; }
	public int getYStart() { return yStart; }
	public int getYEnd() { return yEnd; }
	public float getTexCoordX() { return texCoordX; }
	public float getTexCoordY() { return texCoordY; }
	public float getOneOverZ() { return oneOverZ; }
	public float getDepth() { return depth; }

	public Edge(Gradient gradients, Vertex minYVert, Vertex maxYVert, int minYVertIndex)
	{
		yStart = (int)Math.ceil(minYVert.getY());
		yEnd = (int)Math.ceil(maxYVert.getY());

		float yDist = maxYVert.getY() - minYVert.getY();
		float xDist = maxYVert.getX() - minYVert.getX();

		float yPrestep = yStart - minYVert.getY();
		xStep = (float)xDist/(float)yDist;
		x = minYVert.getX() + yPrestep * xStep;
		float xPrestep = x - minYVert.getX();

		texCoordX = gradients.getTexCoordX(minYVertIndex) +
				gradients.getTexCoordXXStep() * xPrestep +
				gradients.getTexCoordXYStep() * yPrestep;
		texCoordXStep = gradients.getTexCoordXYStep() + gradients.getTexCoordXXStep() * xStep;

		texCoordY = gradients.getTexCoordY(minYVertIndex) +
				gradients.getTexCoordYXStep() * xPrestep +
				gradients.getTexCoordYYStep() * yPrestep;
		texCoordYStep = gradients.getTexCoordYYStep() + gradients.getTexCoordYXStep() * xStep;

		oneOverZ = gradients.getOneOverZ(minYVertIndex) +
				gradients.getOneOverZXStep() * xPrestep +
				gradients.getOneOverZYStep() * yPrestep;
		oneOverZStep = gradients.getOneOverZYStep() + gradients.getOneOverZXStep() * xStep;
		
		depth = gradients.getDepth(minYVertIndex) + 
				gradients.getDepthXStep() * xPrestep +
				gradients.getDepthYStep() * yPrestep;
		depthStep = gradients.getDepthYStep() + gradients.getDepthXStep() * xStep;
	}

	public void step()
	{
		x += xStep;
		texCoordX += texCoordXStep;
		texCoordY += texCoordYStep;
		oneOverZ += oneOverZStep;
		depth += depthStep;
	}
}
