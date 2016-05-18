<KubeInitializer> 5, 1.02 </end> 		//cubeface int, tileSize float
<PlayerStartIndex> 3,3 </end>
<Goal> 3,3,0 </end>						//x, y -> all ints
<Tile> 3,3,0,1,1 </end>					//x,y,height,terrain,face (begins at 1) -> all ints

<Wall> 0,0,-1,0,1 </end>					//x1,y1,x2,y2,face -> corner
<Wall> 1,2,1 </end>						//x,y,face -> all ints -> Not A Corner
<Wall> 0,0,1,0,0,1 </end>				//x1,y1,face,x2,y2,face between 2 tiles on 1 face
