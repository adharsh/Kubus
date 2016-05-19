package mapeditor;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class EditorMain implements KeyListener, MouseListener
{
	private static int curFace;
	private static ArrayList<EditorAsset> assetList;
	private static EditorMap map = new EditorMap(7, 0.5f);
	private static int mode = 1;
	
	private static Scanner scan = new Scanner(System.in);
	
	public static int MODE_VIEW = 1;
	public static int MODE_TILEEDIT = 2;
	public static int MODE_ADDWALL = 3;
	public static int MODE_PLACEPLAYER = 4;
	public static int MODE_PLACEGOAL = 5;
	
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
	
	public static String getFaceName(int face)
	{
		switch(face)
		{
		case 1:
			return "TOP";
		case 2:
			return "FRONT";
		case 3:
			return "BOTTOM";
		case 4:
			return "BACK";
		case 5:
			return "LEFT";
		case 6:
			return "RIGHT";
		}
		return "BAD FACE";
	}
	
	public static String getModeName()
	{
		switch(mode)
		{
		case 1:
			return "VIEW";
		case 2:
			return "TILE EDIT";
		case 3:
			return "ADD WALL";
		case 4:
			return "PLACE PLAYER";
		case 5:
			return "PLACE GOAL";
		}
		return "BAD MODE";
	}
	
	public static Lock mainLock = new ReentrantLock();
	
	public static void main(String[] args)
	{
		Window window = new Window(900, 900);
		EditorMain listener = new EditorMain();
		window.addKeyListener(listener);
		window.addMouseListener(listener);
		window.setVisible(true);
		curFace = 1;
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
			g.drawString("Current face: " + getFaceName(curFace) + " (press enter to go to next face)", 600, 60);
			g.drawString("Press 1 switch mode (check console)", 0, 30);
			g.drawString("Current mode: " + getModeName(), 0, 60);
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
		mainLock.lock();
		if(arg0.getKeyCode() == KeyEvent.VK_1)
		{
			System.out.println("Enter mode change (1 = view, 2 = tile edit, 3 = add wall, 4 = place player, 5 = place goal)");
			int newMode = scan.nextInt();
			
			if(newMode == 4 && curFace != 1)
			{
				System.out.println("Player can only be placed on the top face");

				mainLock.unlock();
				return;
			}
			
			if(newMode >= 1 && newMode <= 5)
			{
				mode = newMode;
			}
			else
			{
				System.out.println("bad mode entered");
			}
		}
		if(mode == EditorMain.MODE_VIEW)
		{
			if(arg0.getKeyCode() == KeyEvent.VK_ENTER)
			{
				nextFace();
			}
			if(arg0.getKeyCode() == KeyEvent.VK_S)
			{
				MapWriter.saveFileDialog(map);
			}
		}
		mainLock.unlock();
	}

	private EditorTile createNewTile(int x, int y)
	{
		EditorTile ret = null;
		
		System.out.print("Enter height (1 = low, 2 = normal, 3 = high): ");
		int ht = scan.nextInt();
		System.out.print("Enter terrain (0 = grass, 1 = water, 2 = spikes, 3 = fire, 4 = ice): ");
		int tr = scan.nextInt();
		ret = new EditorTile(curFace, x, y, ht, tr);
		return ret;
	}

	@Override
	public void mouseReleased(MouseEvent arg0) 
	{
		mainLock.lock();
		int x = arg0.getX();
		int y = arg0.getY();
		if(mode == MODE_ADDWALL)
		{

			x -= 100;
			y -= 100;
			
			int tx = x / (700 / map.getSize()), ty = y / (700 / map.getSize());
			if(x < 0)
			{
				tx = -1;
			}
			if(y < 0)
			{
				ty = -1;
			}
			if(x > 700)
			{
				tx = map.getSize();
			}
			if(y > 700)
			{
				ty = map.getSize();
			}
			System.out.println("Selected tile: " + tx + ", " + ty);
			if(selectedx == -5)
			{
				selectedx = tx;
				selectedy = ty;
			}
			else
			{
				if(tx == selectedx && ty == selectedy && 
				(tx == 0 || ty == 0 || tx == map.getSize() - 1 || ty == map.getSize() - 1))
				{
					map.addAsset(new EditorWall(tx, ty, curFace));
					reloadAssets();
				}
				if((Math.abs(tx - selectedx) == 1 && (ty - selectedy) == 0) ||
						Math.abs(ty - selectedy) == 1 && (tx - selectedx) == 0)
				{
					if(tx == -1 || ty == -1 || tx == map.getSize() || ty == map.getSize() ||
							selectedx == -1 || selectedy == -1 || selectedx == map.getSize() ||
							selectedy == map.getSize())
					{
						System.out.println("rather here");
						map.addAsset(new EditorWall(selectedx, selectedy, tx, ty, curFace));
						reloadAssets();
					}
					else
					{
						map.addAsset(new EditorWall(selectedx, selectedy, tx, ty, curFace, curFace));
						reloadAssets();
					}
				}
				selectedx = -5;
				selectedy = -5;
			}
			mainLock.unlock();
			return;
		}
		
		if(x < 100 || x > 799 || y < 100 || y > 799)
		{
			mainLock.unlock();
			return;
		}
		x -= 100;
		y -= 100;
		int tx = x / (700 / map.getSize()), ty = y / (700 / map.getSize());
		if(mode == MODE_TILEEDIT)
		{
			System.out.println("Selected tile: " + tx + ", " + ty);
			EditorTile newTile = createNewTile(tx, ty);
			if(newTile != null)
			{
				map.addAsset(newTile);
				reloadAssets();
			}
		}
		if(mode == MODE_PLACEGOAL)
		{
			System.out.println("Selected tile: " + tx + ", " + ty);
			EditorGoal newGoal = new EditorGoal(tx, ty, curFace);
			map.addAsset(newGoal);
			reloadAssets();
		}
		if(mode == MODE_PLACEPLAYER)
		{
			System.out.println("Selected tile: " + tx + ", " + ty);
			EditorPlayerStart player = new EditorPlayerStart(tx, ty);
			map.addAsset(player);
			reloadAssets();
		}
		
		mainLock.unlock();
		
	}
	
	private static int selectedx = -5;
	private static int selectedy = -5;
	
	@Override
	public void mousePressed(MouseEvent arg0)
	{
		
	}
	
	//~~~~~~~~
	
	@Override
	public void keyReleased(KeyEvent arg0)
	{
		
	}

	@Override
	public void keyTyped(KeyEvent arg0)
	{
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	
}
