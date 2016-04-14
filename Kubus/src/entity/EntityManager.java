package entity;

import game.Handle;

import java.util.ArrayList;

public class EntityManager 
{
	private Handle handle;
	private Player player;
	private ArrayList<Entity> entityList;
	
	public EntityManager()
	{
		entityList = new ArrayList<Entity>();
	}

	public Entity getEntity(int index)
	{
		if(index < entityList.size())
		{
			return entityList.get(index);
		}
		return null;
	}

	
	
	public static void main(String[] args)
	{

	}

}
