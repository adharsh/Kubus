package graphics;



public class Vertex
{
	private Vector4f pos;
	private Vector4f texCoord;


	public Vertex(float x, float y, float z)
	{
		pos = new Vector4f(x, y, z, 1);
	}

	public Vertex(Vector4f nPos, Vector4f texCoord)
	{
		pos = nPos;
		this.texCoord = texCoord;
	}


	public Vector4f getTexCoord()
	{
		return texCoord;
	}

	public float getX()
	{
		return pos.getX();
	}

	public float getY()
	{
		return pos.getY();
	}

	public float getZ()
	{
		return pos.getZ();
	}

	public float getW()
	{
		return pos.getW();
	}

	public Vector4f getPosition()
	{
		return pos;
	}

	public Vertex transform(Matrix4f t)
	{
		return new Vertex(t.transform(pos), texCoord);
	}

	public Vertex perspectiveDivide()
	{
		return new Vertex(new Vector4f(pos.getX() / pos.getW(), pos.getY() / pos.getW(), 
				pos.getZ() / pos.getW(), pos.getW()), texCoord);
	}

	public float triangleArea(Vertex b, Vertex c)
	{
		float x1 = b.getX() - pos.getX();
		float y1 = b.getY() - pos.getY();

		float x2 = c.getX() - pos.getX();
		float y2 = c.getY() - pos.getY();

		return (x1 * y2 - x2 * y1) * 0.5f;
	}

	public float get(int index)
	{
		switch(index)
		{
		case 0:
			return pos.getX();
		case 1:
			return pos.getY();
		case 2:
			return pos.getZ();
		case 3:
			return pos.getW();
		default:
			throw new IndexOutOfBoundsException();
		}
	}

	public Vertex lerp(Vertex other, float amt)
	{
		return new Vertex(pos.lerp(other.getPosition(), amt), texCoord.lerp(other.getTexCoord(), amt));
	}

	public boolean IsInsideViewFrustum()
	{
		return Math.abs(pos.getX()) <= Math.abs(pos.getW()) &&
				Math.abs(pos.getY()) <= Math.abs(pos.getW()) &&
				Math.abs(pos.getZ()) <= Math.abs(pos.getW());
	}
}
