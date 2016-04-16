package entity;

public class Player extends Entity
{

	private double health;
	//face the player is on
	private int currentFace;
	
	
	public Player(int startFace) 
	{
		currentFace = startFace;
	}


}
