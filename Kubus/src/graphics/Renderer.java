package graphics;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Renderer extends Bitmap
{
	private float[] depthBuffer;

	public Renderer(int w, int h) 
	{
		super(w, h);
		depthBuffer = new float[w * h];
	}

	public void clearDepthBuffer()
	{
		for(int a=0;a<depthBuffer.length;a++)
		{
			depthBuffer[a] = Float.MAX_VALUE;
		}
	}

	public void drawTriangle(Vertex v1, Vertex v2, Vertex v3, Bitmap texture)
	{
		boolean v1Inside = v1.IsInsideViewFrustum();
		boolean v2Inside = v2.IsInsideViewFrustum();
		boolean v3Inside = v3.IsInsideViewFrustum();

		if(v1Inside && v2Inside && v3Inside)
		{
			fillTriangle(v1, v2, v3, texture);
			return;
		}

		if(!v1Inside && !v2Inside && !v3Inside)
		{
			return;
		}
		
		List<Vertex> vertices = new ArrayList<>();
		List<Vertex> auxList = new ArrayList<>();

		vertices.add(v1);
		vertices.add(v2);
		vertices.add(v3);

		if(clipPolygonAxis(vertices, auxList, 0) && clipPolygonAxis(vertices, auxList, 1) && clipPolygonAxis(vertices, auxList, 2))
		{
			Vertex iVertex = vertices.get(0);

			for(int a=1;a<vertices.size()-1;a++)
			{
				fillTriangle(iVertex, vertices.get(a), vertices.get(a + 1), texture);
			}
		}
	}



	private void fillTriangle(Vertex v1, Vertex v2, Vertex v3, Bitmap texture)
	{
		Matrix4f screenSpaceTransform = new Matrix4f().initScreenSpaceTransform(getWidth() / 2, getHeight() / 2);
		Vertex minY = v1.transform(screenSpaceTransform).perspectiveDivide();
		Vertex midY = v2.transform(screenSpaceTransform).perspectiveDivide();
		Vertex maxY = v3.transform(screenSpaceTransform).perspectiveDivide();

		if(minY.triangleArea(midY, maxY) <= 0)
		{
			return;
		}

		if(maxY.getY() < midY.getY())
		{
			Vertex temp = maxY;
			maxY = midY;
			midY = temp;
		}

		if(midY.getY() < minY.getY())
		{
			Vertex temp = midY;
			midY = minY;
			minY = temp;
		}

		if(maxY.getY() < midY.getY())
		{
			Vertex temp = maxY;
			maxY = midY;
			midY = temp;
		}

		scanTriangle(minY, midY, maxY, minY.triangleArea(maxY, midY) >= 0, texture);
	}

	private void scanTriangle(Vertex minY, Vertex midY, Vertex maxY, boolean side, Bitmap texture)
	{
		Gradient grad = new Gradient(minY, midY, maxY);
		Edge topToBottom = new Edge(grad, minY, maxY, 0);
		Edge topToMiddle = new Edge(grad, minY, midY, 0);
		Edge middleToBottom = new Edge(grad, midY, maxY, 1);

		scanEdges(topToBottom, topToMiddle, side, texture);
		scanEdges(topToBottom, middleToBottom, side, texture);
	}

	private void scanEdges(Edge a, Edge b, boolean side, Bitmap texture)
	{
		Edge left = a;
		Edge right = b;

		if(side)
		{
			Edge temp = left;
			left = right;
			right = temp;
		}
		int ys = b.getYStart();
		int ye = b.getYEnd();

		for(int y = ys; y < ye; y++)
		{
			drawScanLine(left, right, y, texture);
			left.step();
			right.step();
		}
	}

	private void drawScanLine(Edge left, Edge right, int y, Bitmap texture)
	{
		int xMin = (int)Math.ceil(left.getX());
		int xMax = (int)Math.ceil(right.getX());
		float xPrestep = xMin - left.getX();

		float xDist = right.getX() - left.getX();
		float texCoordXXStep = (right.getTexCoordX() - left.getTexCoordX()) / xDist;
		float texCoordYXStep = (right.getTexCoordY() - left.getTexCoordY()) / xDist;
		float oneOverZXStep = (right.getOneOverZ() - left.getOneOverZ()) / xDist;
		float depthXStep = (right.getDepth() - left.getDepth()) / xDist;

		float texCoordX = left.getTexCoordX() + texCoordXXStep * xPrestep;
		float texCoordY = left.getTexCoordY() + texCoordYXStep * xPrestep;
		float oneOverZ = left.getOneOverZ() + oneOverZXStep * xPrestep;
		float depth = left.getDepth() + depthXStep * xPrestep;

		for(int x = xMin; x < xMax;x++)
		{
			int index = x + y * getWidth();
			if(depthBuffer[index] > depth)
			{
				depthBuffer[index] = depth;
				float z = 1.f / oneOverZ;
				int srcX = (int)((texCoordX * z) * (float)(texture.getWidth() - 1) + 0.5f);
				int srcY = (int)((texCoordY * z) * (float)(texture.getHeight() - 1) + 0.5f);

				copyPixel(x, y, srcX, srcY, texture);
			}
			oneOverZ += oneOverZXStep;
			texCoordX += texCoordXXStep;
			texCoordY += texCoordYXStep;
			depth += depthXStep;
		}
	}

	private boolean clipPolygonAxis(List<Vertex> vertices, List<Vertex> auxList, int componentIndex)
	{
		clipPolygonComponent(vertices, componentIndex, 1.0f, auxList);
		vertices.clear();

		if(auxList.isEmpty())
		{
			return false;
		}

		clipPolygonComponent(auxList, componentIndex, -1.0f, vertices);
		auxList.clear();

		return !vertices.isEmpty();
	}

	private void clipPolygonComponent(List<Vertex> vertices, int componentIndex, float componentFactor, List<Vertex> result)
	{
		Vertex previousVertex = vertices.get(vertices.size() - 1);
		float previousComponent = previousVertex.get(componentIndex) * componentFactor;
		boolean previousInside = previousComponent <= previousVertex.getW();

		Iterator<Vertex> it = vertices.iterator();

		while(it.hasNext())
		{
			Vertex cur = it.next();
			float currentComponent = cur.get(componentIndex) * componentFactor;
			boolean currentInside = currentComponent <= cur.getW();

			if(currentInside ^ previousInside)
			{
				float lerp = (previousVertex.getW() - previousComponent) / 
						((previousVertex.getW() - previousComponent) - (cur.getW() - currentComponent));
				result.add(previousVertex.lerp(cur, lerp));
			}

			if(currentInside)
			{
				result.add(cur);
			}

			previousVertex = cur;
			previousComponent = currentComponent;
			previousInside = currentInside;
		}
	}
}
