<KubeInitializer> 5, 1.02 </end> 		//cubeface int, tileSize float
<PlayerStartIndex> 3,3 </end>
<Goal> 3,3,0 </end>						//x, y, face -> all ints
<Tile> 3,3,0,1,0 </end>					//x,y,height,terrain,face -> all ints

<Wall> 3,3,3,4 </end>	??which??face?SAM??		//x1,y1,x2,y2 -> all ints, two different tiles -> Same Face
<Wall> 1,2 </end>						//x,y, -> all ints -> Not A Corner
<Wall> 0,0,0,0,0,1 </end>				//x1,y1,face,x2,y2,face -> tiles with faces -> Corner
