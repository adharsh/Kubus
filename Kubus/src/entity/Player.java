package entity;

public class Player extends Entity
{

	private double health;
	//face the player is on
	private int currentFace;
	private Kube map;
	
	
	public Player(int startFace, int startX, int startY) 
	{
		currentFace = startFace;
	}
	
	

}
