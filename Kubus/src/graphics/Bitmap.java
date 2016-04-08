package graphics;

public class Bitmap {
	
	private byte[] pixels;
	private int width;
	private int height;
	
	public Bitmap(int width, int height){
		this.width = width;
		this.height = height;
		
		pixels = new byte[ width * height * 4];
	}
	
	
	public int height(){
		 return height;
	}
	
	
	public int width(){
		 return width;
	}
	
	
	public void clear(byte color){
		
		for(int i = 0; i < pixels.length; i++)
			pixels[i] = color;
		
	}
	
	public void setPixel(int x, int y, byte a, byte r, byte g, byte b){
		
		int index = (x + y * width) * 4;
		pixels[index] = a;
		pixels[index + 1] = r;
		pixels[index + 2] = g;
		pixels[index + 3] = b;
		
	}
	
	
	public void pixels(byte[] src){
		
		for(int i = 0; i < width*height; i++){
			
			pixels[i*4] = src[i];
			pixels[i*4 + 1] = src[i*3 + 1];
			pixels[i*4 + 2] = src[i*3 + 2];
			pixels[i*4 + 3] = src[i*3 + 3];
			
		}
		
	}
	
}
