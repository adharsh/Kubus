package mapeditor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

import javax.swing.JFileChooser;

public class MapWriter
{
	public static void writeFile(File mapFile, EditorMap map)
	{
		ArrayList<EditorAsset> assets = map.getAssets();
		
		
		try
		{
			BufferedWriter fw = new BufferedWriter(new FileWriter(mapFile));
		
			for(EditorAsset asset : assets)
			{
				if(asset == null)
				{
					continue;
				}
				if(!asset.getAssetString().equals(""))
				{
					fw.write(asset.getAssetString() + "\n");
				}
			}
			fw.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void saveFileDialog(EditorMap map)
	{
		JFileChooser saveDialog = new JFileChooser("res");
		saveDialog.showSaveDialog(null);
		if(saveDialog.getSelectedFile() != null)
		{
			writeFile(saveDialog.getSelectedFile(), map);
		}
	}
}
