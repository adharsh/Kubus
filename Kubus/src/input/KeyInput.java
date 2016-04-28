package input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyInput implements KeyListener
{
	public static final int LEFT_ARROW = 0;
	public static final int RIGHT_ARROW = 1;
	public static final int UP_ARROW = 2;
	public static final int DOWN_ARROW = 3;
	
	private boolean[] keysDown;
	private boolean ignore;
	
	public KeyInput()
	{
		keysDown = new boolean[]{false, false, false, false};
		ignore = false;
	}
	
	public void ignoreInput(boolean ignore)
	{
		this.ignore = ignore;
	}
	
	@Override
	public void keyPressed(KeyEvent arg0)
	{
		switch(arg0.getKeyCode())
		{
		case KeyEvent.VK_RIGHT:
			keysDown[RIGHT_ARROW] = true;
			break;
		case KeyEvent.VK_LEFT:
			keysDown[LEFT_ARROW] = true;
			break;
		case KeyEvent.VK_UP:
			keysDown[UP_ARROW] = true;
			break;
		case KeyEvent.VK_DOWN:
			keysDown[DOWN_ARROW] = true;
//			System.out.println(keysDown[DOWN_ARROW]);
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0)
	{
		switch(arg0.getKeyCode())
		{
		case KeyEvent.VK_RIGHT:
			keysDown[RIGHT_ARROW] = false;
			break;
		case KeyEvent.VK_LEFT:
			keysDown[LEFT_ARROW] = false;
			break;
		case KeyEvent.VK_UP:
			keysDown[UP_ARROW] = false;
			break;
		case KeyEvent.VK_DOWN:
			keysDown[DOWN_ARROW] = false;
			break;
		}
	}

	public boolean isKeyDown(int keyCode)
	{
		return (!ignore) && keysDown[keyCode];
	}
	
	public void keyTyped(KeyEvent arg0){}

}
