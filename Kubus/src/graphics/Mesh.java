package graphics;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Mesh
{
	private List<Vertex>  vertices;
	private List<Integer> indices;

	public Vertex getVertex(int i) { return vertices.get(i); }
	public int getIndex(int i) { return indices.get(i); }
	public int getNumIndices() { return indices.size(); }

	public Mesh(List<Vertex> vertices, List<Integer> indices) throws IOException
	{
	}
	
	public void draw(Renderer render, Matrix4f transform, Bitmap texture)
	{
		for(int a=0;a<indices.size();a+=3)
		{
			render.drawTriangle(vertices.get(indices.get(a)).transform(transform), 
					vertices.get(indices.get(a + 1)).transform(transform), 
					vertices.get(indices.get(a + 2)).transform(transform), 
					texture);
		}
	}
}
