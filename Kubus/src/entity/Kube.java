package entity;

import graphics.Bitmap;
import graphics.Matrix4f;
import graphics.Mesh;
import graphics.Renderer;
import graphics.Transformation;
import graphics.Vector4f;
import graphics.Vertex;

import java.io.IOException;
import java.util.ArrayList;

/*
 * assuming there is no rotation on the cube and you are viewing the cube's center from a position of (0, -1, -1)
 * the faces are numbered:
 * 	top = 1
 * 	front = 2
 * 	bottom = 3
 * 	back = 4
 * 	left = 5
 * 	right = 6
 */

public class Kube
{
	public static final int TOP = 1;
	public static final int FRONT = 2;
	public static final int BOTTOM = 3;
	public static final int BACK = 4;
	public static final int LEFT = 5;
	public static final int RIGHT = 6;

	private float tileLength;
	//try 10x10 faces
	private int faceLength;

	private static Mesh wallMesh;

	private ArrayList<ArrayList<Tile>> tiles;
	private ArrayList<Tile[]> walls;
	private ArrayList<Tile> edgeWalls;
	private ArrayList<TileIndex[]> cornerWalls;
	private static Bitmap grass;
	static
	{
		ArrayList<Vertex> vertices = new ArrayList<Vertex>();
		ArrayList<Integer> indices = new ArrayList<Integer>();

		vertices.add(new Vertex(new Vector4f(-0.5f, 0, -0.5f, 1), new Vector4f(0.5f, 0, 0, 0)));
		vertices.add(new Vertex(new Vector4f(0.5f, 0, -0.5f, 1), new Vector4f(0, 0, 0, 0)));
		vertices.add(new Vertex(new Vector4f(-0.5f, 0, 0.5f, 1), new Vector4f(0, 0, 0, 0)));
		vertices.add(new Vertex(new Vector4f(0.5f, 0, 0.5f, 1), new Vector4f(0.5f, 0, 0, 0)));

		vertices.add(new Vertex(new Vector4f(-0.5f, 0.2f, -0.5f, 1), new Vector4f(0.5f, 0.25f, 0, 0)));
		vertices.add(new Vertex(new Vector4f(0.5f, 0.2f, -0.5f, 1), new Vector4f(0, 0.25f, 0, 0)));
		vertices.add(new Vertex(new Vector4f(-0.5f, 0.2f, 0.5f, 1), new Vector4f(0, 0.25f, 0, 0)));
		vertices.add(new Vertex(new Vector4f(0.5f, 0.2f, 0.5f, 1), new Vector4f(0.5f, 0.25f, 0, 0)));

		indices.add(0);
		indices.add(1);
		indices.add(2);

		indices.add(1);
		indices.add(3);
		indices.add(2);

		indices.add(0);
		indices.add(2);
		indices.add(4);

		indices.add(4);
		indices.add(6);
		indices.add(2);

		indices.add(4);
		indices.add(5);
		indices.add(6);

		indices.add(5);
		indices.add(6);
		indices.add(7);

		indices.add(4);
		indices.add(0);
		indices.add(1);

		indices.add(5);
		indices.add(4);
		indices.add(1);

		indices.add(5);
		indices.add(7);
		indices.add(3);

		indices.add(5);
		indices.add(1);
		indices.add(3);

		indices.add(6);
		indices.add(7);
		indices.add(2);

		indices.add(7);
		indices.add(3);
		indices.add(2);

		wallMesh = new Mesh(vertices, indices);
		try {
			grass = new Bitmap("res/brik.jpg");
		} catch (IOException e) {
			grass = new Bitmap(1, 1);
			e.printStackTrace();
		}
	}
	
	public static class TileIndex
	{
		public int x, y, face;
		public TileIndex(int x, int y, int face)
		{
			this.x = x;
			this.y = y;
			this.face = face;
		}
		
		@Override
		public boolean equals(TileIndex other)
		{
			return (other.x == x && other.y == y && other.face == face);
		}
	}

	public Kube(int faceLength, float tileLength)
	{
		this.faceLength = faceLength;
		this.tileLength = tileLength;
		tiles = new ArrayList<ArrayList<Tile>>();
		for(int a=0;a<6;a++) { tiles.add(new ArrayList<Tile>()); }
		walls = new ArrayList<Tile[]>();
		edgeWalls = new ArrayList<Tile>();
		cornerWalls = new ArrayList<TileIndex[]>();
	}

	public void addWall(Tile t1, Tile t2)
	{

		Tile[] newWall = new Tile[]{t1, t2};
		walls.add(newWall);
	}
	
	public void addWall(TileIndex t1, TileIndex t2)
	{
		cornerWalls.add(new TileIndex[]{ t1, t2 });
	}

	public void addWall(Tile edge)
	{
		if((edge.getXIndex() + 1 == faceLength || edge.getXIndex() == 0) &&
				(edge.getYIndex() + 1 == faceLength || edge.getYIndex() == 0))
		{
			return;
		}

		edgeWalls.add(edge);
	}


	public boolean wallInDirection(int face, int x, int y, int dx, int dy)
	{
		Tile thisTile = getTileAt(face, x, y);
		Tile futureTile = getTileAt(face, x + dx, y + dy);

		for(Tile[] wall : walls)
		{
			if(thisTile == wall[0])
			{
				if(futureTile == wall[1])
				{
					return true;
				}
			}
			if(thisTile == wall[1])
			{
				if(futureTile == wall[0])
				{
					return true;
				}
			}
		}
		return false;
	}

	public int getFaceLength()
	{
		return faceLength;
	}

	public float getTileLength()
	{
		return tileLength;
	}

	public Tile getNearestTileNoNull(Vector4f position, int f)
	{
		Tile nearestTile = null;
		ArrayList<Tile> face = tiles.get(f - 1);
		float max = Float.MAX_VALUE;
		float dist;
		for(Tile nearest : face)
		{
			dist = position.sub(nearest.getPosition()).length3d();
			if(dist < max)
			{
				max = dist;
				nearestTile = nearest;
			}
		}
		return nearestTile;
	}
	
	public Tile getNearestTile(Vector4f position, int f)
	{
		Tile nearestTile = null;
		float max = Float.MAX_VALUE;
		float dist;
		for(ArrayList<Tile> face : tiles)
		{
			for(Tile nearest : face)
			{
				dist = position.sub(nearest.getPosition()).length3d();
				if(dist < max)
				{
					max = dist;
					nearestTile = nearest;
				}
			}
		}
		if(nearestTile.getFace() != f)
		{
			return null;
		}
		return nearestTile;
	}

	public boolean wallOnEdge(Tile t)
	{
		for(Tile edges : edgeWalls)
		{
			if(t == edges)
			{
				return true;
			}
		}
		return false;
	}

	public boolean wallOnEdge(Tile t, int dx, int dy)
	{
		TileIndex t1 = new TileIndex(t.getXIndex(), t.getYIndex(), t.getFace());
		TileIndex t2 = new TileIndex(t.getXIndex() + dx, t.getYIndex() + dy, t.getFace());
		for(TileIndex[] wall : cornerWalls)
		{
			if(t1.equals(wall[0]))
			{
				if(t2.equals(wall[1]))
				{
					return true;
				}
			}
			if(t1.equals(wall[1]))
			{
				if(t2.equals(wall[0]))
				{
					return true;
				}
			}
		}
		return false;
	}
	
	public Tile getNearestTile(int f, int x, int y)
	{
		float max = Float.MAX_VALUE;
		Tile nearestTile = null;
		float dist;
		Vector4f edgePos = getTilePosition(f, x, y);
		for(ArrayList<Tile> face : tiles)
		{
			for(Tile nearest : face)
			{
				dist = edgePos.sub(nearest.getPosition()).length3d();
				if(dist < max)
				{
					max = dist;
					nearestTile = nearest;
				}
			}
		}
		return nearestTile;
	}

	public Tile getTileAt(int face, int xIndex, int yIndex)
	{
		if(face >= tiles.size() || xIndex >= faceLength || yIndex >= faceLength)
			return null;
		face--;
		ArrayList<Tile> arrayListFace = tiles.get(face);
		for(Tile t : arrayListFace)
		{
			if(t.getXIndex() == xIndex && t.getYIndex() == yIndex)
			{
				return t;
			}
		}
		return null;
	}

	public boolean addTile(Tile tile)
	{
		int index = tile.getFace() - 1;

		if(index >= tiles.size() || (faceLength * faceLength) <= tiles.get(index).size())
		{
			return false;
		}
		tiles.get(index).add(tile);
		tile.getTransform().setRotation(getFaceRotation(tile.getFace()));
		return true;
	}

	public Vector4f getTilePosition(int face, int x, int y)
	{
		Vector4f centerOffset;
		float faceLength = this.faceLength - 1;

		float outValue = (float)this.faceLength / 2.f * tileLength;
		faceLength /= 2.f;
		float xValue = ((float)x - faceLength) * tileLength;
		float yValue = ((float)y - faceLength) * tileLength;


		switch(face)
		{
		case TOP:
			centerOffset = new Vector4f(xValue, outValue, yValue);
			break;
		case BOTTOM:
			centerOffset = new Vector4f(xValue, -outValue, -yValue);
			break;
		case BACK:
			centerOffset = new Vector4f(xValue, yValue, -outValue);
			break;
		case FRONT:
			centerOffset = new Vector4f(xValue, -yValue, outValue);
			break;
		case RIGHT:
			centerOffset = new Vector4f(outValue, -xValue, yValue);
			break;
		case LEFT:
			centerOffset = new Vector4f(-outValue, xValue, yValue);
			break;
		default:
			centerOffset = new Vector4f(0, 0, 0);	
		}
		return centerOffset;
	}


	public Matrix4f getFaceRotation(int face)
	{
		return getRelativeRotation(face);
	}
	
	public void renderFaces(Renderer render, Matrix4f viewProjection)
	{
		for(int a=0;a<6;a++)
		{
			for(int b=0;b<tiles.get(a).size();b++)
			{
				tiles.get(a).get(b).render(render, viewProjection);
			}
		}
		Transformation tf = new Transformation();
		Vector4f pos = new Vector4f(0, 0, 0, 1);
		tf.setScale(tileLength, tileLength, tileLength);

		for(Tile[] wall : walls)
		{
			Vector4f pos1 = wall[0].renderTransform.getPosition(), pos2 = wall[1].renderTransform.getPosition();

			if(wall[0].getXIndex() == wall[1].getXIndex())
			{
				tf.setScale(tileLength, tileLength, 0);
			}
			else
			{
				tf.setScale(0, tileLength, tileLength);
			}

			tf.setRotation(getRelativeRotation(wall[0].getFace()));
			pos.setXYZW((pos1.getX() + pos2.getX()) / 2.f, (pos1.getY() + pos2.getY()) / 2.f, 
					(pos1.getZ() + pos2.getZ()) / 2.f, 1.f);
			tf.setPosition(pos);
			wallMesh.draw(render, viewProjection, tf.getTransformation(), grass);
		}

		for(TileIndex[] wall : cornerWalls)
		{
			
			Vector4f pos1 = getTilePosition(wall[0].face, wall[0].x, wall[0].y), pos2 = getTilePosition(wall[1].face, wall[1].x, wall[1].y);
			
			if(wall[0].x == wall[1].x)
			{
				tf.setScale(tileLength, tileLength, 0);
			}
			else
			{
				tf.setScale(0, tileLength, tileLength);
			}
			
			tf.setRotation(getRelativeRotation(wall[0].face));

			pos.setXYZW((pos1.getX() + pos2.getX()) / 2.f, (pos1.getY() + pos2.getY()) / 2.f, 
					(pos1.getZ() + pos2.getZ()) / 2.f, 1.f);
			tf.setPosition(pos);
			wallMesh.draw(render, viewProjection, tf.getTransformation(), grass);
		}

		for(Tile t : edgeWalls)
		{
			if(t.getXIndex() == 0)
			{
				Vector4f pos1 = getTilePosition(t.getFace(), -1, t.getYIndex()), pos2 = t.renderTransform.getPosition();

				tf.setScale(0, tileLength, tileLength);
				tf.setRotation(getRelativeRotation(t.getFace()));
				pos.setXYZW((pos1.getX() + pos2.getX()) / 2.f, (pos1.getY() + pos2.getY()) / 2.f, 
						(pos1.getZ() + pos2.getZ()) / 2.f, 1.f);
				tf.setPosition(pos);
				wallMesh.draw(render, viewProjection, tf.getTransformation(), grass);
			}
			if(t.getXIndex() == faceLength - 1)
			{
				Vector4f pos1 = getTilePosition(t.getFace(), faceLength, t.getYIndex()), pos2 = t.renderTransform.getPosition();

				tf.setScale(0, tileLength, tileLength);
				tf.setRotation(getRelativeRotation(t.getFace()));
				pos.setXYZW((pos1.getX() + pos2.getX()) / 2.f, (pos1.getY() + pos2.getY()) / 2.f, 
						(pos1.getZ() + pos2.getZ()) / 2.f, 1.f);
				tf.setPosition(pos);
				wallMesh.draw(render, viewProjection, tf.getTransformation(), grass);
			}
			if(t.getYIndex() == 0)
			{
				Vector4f pos1 = getTilePosition(t.getFace(), t.getXIndex(), -1), pos2 = t.renderTransform.getPosition();

				tf.setScale(tileLength, tileLength, 0);
				tf.setRotation(getRelativeRotation(t.getFace()));
				pos.setXYZW((pos1.getX() + pos2.getX()) / 2.f, (pos1.getY() + pos2.getY()) / 2.f, 
						(pos1.getZ() + pos2.getZ()) / 2.f, 1.f);
				tf.setPosition(pos);
				wallMesh.draw(render, viewProjection, tf.getTransformation(), grass);
			}
			if(t.getYIndex() == faceLength - 1)
			{
				Vector4f pos1 = getTilePosition(t.getFace(), t.getXIndex(), faceLength), pos2 = t.renderTransform.getPosition();

				tf.setScale(tileLength, tileLength, 0);
				tf.setRotation(getRelativeRotation(t.getFace()));
				pos.setXYZW((pos1.getX() + pos2.getX()) / 2.f, (pos1.getY() + pos2.getY()) / 2.f, 
						(pos1.getZ() + pos2.getZ()) / 2.f, 1.f);
				tf.setPosition(pos);
				wallMesh.draw(render, viewProjection, tf.getTransformation(), grass);
			}
		}
	}

	private Matrix4f getRelativeRotation(int relativeFace)
	{
		switch(relativeFace)
		{
		case TOP:
			return new Matrix4f().initRotation(0, 0, 0);
		case FRONT:
			return new Matrix4f().initRotation((float)Math.toRadians(90), 0, 0);
		case BOTTOM:
			return new Matrix4f().initRotation((float)Math.toRadians(180), 0, 0);
		case BACK:
			return new Matrix4f().initRotation(-(float)Math.toRadians(90), 0, 0);
		case LEFT:
			return new Matrix4f().initRotation(0, 0, (float)Math.toRadians(90));
		case RIGHT:
			return new Matrix4f().initRotation(0, 0, -(float)Math.toRadians(90));
		}
		return null;
	}
}
