package mapeditor;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class EditorMain implements KeyListener
{
	private static int curFace;
	private static ArrayList<EditorAsset> assetList;
	private static EditorMap map = new EditorMap(7, 0.5f);
	
	public static void nextFace()
	{
		curFace++;
		if(curFace > 6)
		{
			curFace = 1;
		}
		reloadAssets();
	}
	
	public static void reloadAssets()
	{
		assetList = map.fetchAssetsOnFace(curFace);
	}
	
	public static Lock mainLock = new ReentrantLock();
	
	public static void main(String[] args)
	{
		Window window = new Window(900, 900);
		window.addKeyListener(new EditorMain());
		window.setVisible(true);
		curFace = 1;
		map.addAsset(new EditorWall(0, 0, 1, 0, 1, 1));
		map.addAsset(new EditorWall(6, 1, 1));
		map.addAsset(new EditorWall(0, 0, -1, 0, 1));
		map.addAsset(new EditorWall(0, 1, 2));
		reloadAssets();
		Graphics g = window.getBufferGraphics();
		while(true)
		{
			mainLock.lock();
			BufferedPanel.frameBufferLock.lock();
			g.setColor(Color.black);
			g.fillRect(0, 0, 900, 900);
			for(EditorAsset asset : assetList)
			{
				asset.renderAsset(g, map.getSize());
			}
			g.setColor(Color.orange);
			g.drawString("Current face: " + curFace + " (press enter to go to next face)", 600, 60);
			g.drawString("Press ` to use console commands", 0, 30);
			BufferedPanel.frameBufferLock.unlock();
			
			window.repaint();
			mainLock.unlock();
			try
			{
				Thread.sleep(5);
			} 
			catch(InterruptedException e) 
			{
				e.printStackTrace();
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent arg0) 
	{
		if(arg0.getKeyCode() == KeyEvent.VK_ENTER)
		{
			mainLock.lock();
			nextFace();
			mainLock.unlock();
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0)
	{
		
	}

	@Override
	public void keyTyped(KeyEvent arg0)
	{
		
	}
}
