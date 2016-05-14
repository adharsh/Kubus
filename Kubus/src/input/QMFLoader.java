package input;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import graphics.Mesh;
import graphics.Vector4f;
import graphics.Vertex;

public class QMFLoader 
{
	private static String loadContents(String fileName) throws IOException
	{
		FileInputStream is = new FileInputStream(new File(fileName));
		int character = 0;
		StringBuilder data = new StringBuilder("");
		while((character = is.read()) != -1)
		{
			data.append((char)character);
		}
		is.close();
		return data.toString().replaceAll(" ", "").replaceAll("\r", "");
	}
	
	public static Mesh loadQMF(String fileName)
	{
		String data;
		Mesh ret = null;
		ArrayList<Vertex> vertices = new ArrayList<Vertex>();
		ArrayList<Integer> indices = new ArrayList<Integer>();
		try 
		{
			data = loadContents(fileName);
		}
		catch(IOException e)
		{
			e.printStackTrace();
			return null;
		}
		String[] sections = data.split("\n;\n");
		
		
		String[] vertexStrings = sections[0].split("\n");
		String[] indexStrings = sections[1].split("\n");
		String[] texCoordStrings = sections[2].split("\n");
		
		for(int a=0;a<vertexStrings.length;a++)
		{
			String[] xyz = vertexStrings[a].split(",");
			String[] uv = texCoordStrings[a].split(",");

			float x = Float.valueOf(xyz[0]);
			float y = Float.valueOf(xyz[1]);
			float z = Float.valueOf(xyz[2]);
			float u = Float.valueOf(uv[0]);
			float v = Float.valueOf(uv[1]);
			
			vertices.add(new Vertex(new Vector4f(x, y, z, 1), new Vector4f(u, v, 0, 0)));
		}
		
		for(int a=0;a<indexStrings.length;a++)
		{
			String[] tri = indexStrings[a].split(",");
			int i1 = Integer.valueOf(tri[0]);
			int i2 = Integer.valueOf(tri[1]);
			int i3 = Integer.valueOf(tri[2]);

			indices.add(i1);
			indices.add(i2);
			indices.add(i3);
		}
		ret = new Mesh(vertices, indices);
		return ret;
	}
}
