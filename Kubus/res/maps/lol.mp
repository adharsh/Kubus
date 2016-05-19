// Example mp file
/*
TOP = 1;
FRONT = 2;
BOTTOM = 3;
BACK = 4;
LEFT = 5;
RIGHT = 6;

Water = 1
Spikes = 2
Fire = 3
Ice = 4

*/

<KubeInitializer> 7, 1.02 </end> 		//cubeface int, tileSize float
<PlayerStartIndex> 3,3 </end>
<Goal> 2,2,1 </end>						//x, y -> all ints
<Tile> 3,3,2,3,1 </end>					//x,y,height,terrain,face (begins at 1) -> all ints

<Wall> 0,0,-1,0,1 </end>					//x1,y1,x2,y2,face -> corner
<Wall> 0,0,1,1,0,1 </end>				//x1,y1,face,x2,y2,face between 2 tiles on 1 face
