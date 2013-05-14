package lib.kael.util;

import java.awt.Point;

public class AoiUtil
{
	public static int AOIWIDTH = 2160;
	public static int AOIHEIGHT = 840;
	public static boolean isInAoi(int x,int y,Point aoip)
	{
		return (x >= aoip.x && y >= aoip.y && x <= AOIWIDTH + aoip.x && y <= AOIHEIGHT + aoip.y);
	}
	public static Point getScrollRectStartPoint(int x,int y,int mapid)
	{
		Point p = new Point();
//		if(StaticData.mapData != null)
//		{
//			MapData mapdata = (MapData) StaticData.mapData.get(mapid);
//			if(mapdata != null)
//			{
//				if(AOIWIDTH >= mapdata.getMapWidth())
//				{
//					p.x = 0;
//				}
//				if(AOIHEIGHT >= mapdata.getMapHeight())
//				{
//					p.y = 0;
//				}
//				//if(p.x != 0 && p.y != 0)
//				//{
//				if((x >= AOIWIDTH/2 && x <= mapdata.getMapWidth() - AOIWIDTH/2)
//				 && (y >= AOIHEIGHT/2 && y <= mapdata.getMapHeight()-AOIHEIGHT/2))
//				{
//					p.x = x - AOIWIDTH/2;
//					p.y = y - AOIHEIGHT/2;
//				}
//				else
//				{
//					p.x = x - AOIWIDTH/2;
//					if(p.x < 0)p.x = 0;
//					else if(p.x + AOIWIDTH > mapdata.getMapWidth())p.x = mapdata.getMapWidth() - AOIWIDTH;
//					p.y = y - AOIHEIGHT/2;
//					if(p.y < 0)p.y = 0;
//					else if(p.y + AOIHEIGHT > mapdata.getMapHeight())p.y = mapdata.getMapHeight() - AOIHEIGHT;
//				}
//				//}
//			}
//		}
		return p;
	}
}
