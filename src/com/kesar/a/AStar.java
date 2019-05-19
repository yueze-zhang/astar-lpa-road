package com.kesar.a;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import static com.kesar.a.Constant.INFINITE;
import static java.lang.Math.min;

/**
 * 
 * ClassName: AStar 
 * @Description: A星算法
 * @author kesar
 */
public class AStar
{
	public final static int BAR = 1; // 障碍值
	public final static int PATH = 2; // 路径
	public final static int DIRECT_VALUE = 10; // 横竖移动代价
	public final static int OBLIQUE_VALUE = 14; // 斜移动代价
	
	Queue<Node> openList = new PriorityQueue<Node>(); // 优先队列(升序)
	List<Node> closeList = new ArrayList<Node>();
	List<Node> succeedList = new ArrayList<Node>();
	
	/**
	 * 开始算法
	 */
	public void start(MapInfo mapInfo)
	{
		if(mapInfo==null) return;
		// clean
		openList.clear();
		closeList.clear();
		// 开始搜索
		//initialize
		mapInfo.end.G = INFINITE;
		mapInfo.end.RHS = INFINITE;
		mapInfo.start.G=INFINITE;
		mapInfo.start.RHS=0;
		openList.add(mapInfo.start);
		moveNodes(mapInfo);
		//改变其中某一个代价

		//改变Open表里所有的状态

		//寻找改变代价后的受影响的状态

		//更新新的状态

		//重新放入到Open表中

		//重新计算最短路径
	}

	/**
	 * 移动当前结点
	 */
	private void moveNodes(MapInfo mapInfo)
	{
		Node current = null;
		while (comprise(current, mapInfo)|| mapInfo.end.G != mapInfo.end.RHS)
		{
			if (isCoordInClose(mapInfo.end.coord))
			{
				drawPath(mapInfo.maps, mapInfo.end);
				break;
			}
			current = openList.poll();
			if (current.G > current.RHS) {
				current.G = current.RHS;
				addNeighborNodeInOpen(mapInfo,current);
			}else {
				current.RHS =INFINITE;
				current = Update(current,mapInfo);
				addNeighborNodeInOpen(mapInfo,current);
			}
			closeList.add(current);
		}
	}
	
	/**
	 * 在二维数组中绘制路径
	 */
	private void drawPath(int[][] maps, Node end)
	{
		if(end==null||maps==null) return;
		System.out.println("总代价：" + end.G);
		while (end != null)
		{
			Coord c = end.coord;
			maps[c.y][c.x] = PATH;
			end = end.parent;
		}
	}

	/**
	 * 添加所有邻结点到open表
	 */
	private void addNeighborNodeInOpen(MapInfo mapInfo,Node current)
	{
		int x = current.coord.x;
		int y = current.coord.y;
		// 左
		addNeighborNodeInOpen(mapInfo,current, x - 1, y, DIRECT_VALUE);
		// 上
		addNeighborNodeInOpen(mapInfo,current, x, y - 1, DIRECT_VALUE);
		// 右
		addNeighborNodeInOpen(mapInfo,current, x + 1, y, DIRECT_VALUE);
		// 下
		addNeighborNodeInOpen(mapInfo,current, x, y + 1, DIRECT_VALUE);
		// 左上
		addNeighborNodeInOpen(mapInfo,current, x - 1, y - 1, OBLIQUE_VALUE);
		// 右上
		addNeighborNodeInOpen(mapInfo,current, x + 1, y - 1, OBLIQUE_VALUE);
		// 右下
		addNeighborNodeInOpen(mapInfo,current, x + 1, y + 1, OBLIQUE_VALUE);
		// 左下
		addNeighborNodeInOpen(mapInfo,current, x - 1, y + 1, OBLIQUE_VALUE);
	}

	/**
	 * 添加一个邻结点到open表
	 */
	private void addNeighborNodeInOpen(MapInfo mapInfo,Node current, int x, int y, int value)
	{
		if (canAddNodeToOpen(mapInfo,x, y))
		{
			Node end=mapInfo.end;
			Coord coord = new Coord(x, y);
			int G = current.G + value; // 计算邻结点的G值
			int H=calcH(end.coord,coord); // 计算H值
			Node node = new Node(coord,current,G,H);
			node = Update(node,mapInfo);
			openList.add(node);

		}
	}

	/**
	 * 从Open列表中查找结点
	 */
	private Node findNodeInOpen(Coord coord)
	{
		if (coord == null || openList.isEmpty()) return null;
		for (Node node : openList)
		{
			if (node.coord.equals(coord))
			{
				return node;
			}
		}
		return null;
	}


	/**
	 * 计算H的估值：“曼哈顿”法，坐标分别取差值相加
	 */
	private int calcH(Coord end,Coord coord)
	{
		return Math.abs(end.x - coord.x)
				+ Math.abs(end.y - coord.y);
	}
	
	/**
	 * 判断结点是否是最终结点
	 */
	private boolean isEndNode(Coord end,Coord coord)
	{
		return coord != null && end.equals(coord);
	}

	/**
	 * 判断结点能否放入Open列表
	 */
	private boolean canAddNodeToOpen(MapInfo mapInfo,int x, int y)
	{
		// 是否在地图中
		if (x < 0 || x >= mapInfo.width || y < 0 || y >= mapInfo.hight) return false;
		// 判断是否是不可通过的结点
		if (mapInfo.maps[y][x] == BAR) return false;
		// 判断结点是否存在close表
		if (isCoordInClose(x, y)) return false;

		return true;
	}

	/**
	 * 判断坐标是否在close表中
	 */
	private boolean isCoordInClose(Coord coord)
	{
		return coord!=null&&isCoordInClose(coord.x, coord.y);
	}

	/**
	 * 判断坐标是否在close表中
	 */
	private boolean isCoordInClose(int x, int y)
	{
		if (closeList.isEmpty()) return false;
		for (Node node : closeList)
		{
			if (node.coord.x == x && node.coord.y == y)
			{
				return true;
			}
		}
		return false;
	}

	private boolean comprise(Node current,MapInfo mapInfo){
		if (current == null) return true;
		int K1 = min(current.G,current.RHS)+current.H;
		int OK1 = min(mapInfo.end.G, mapInfo.end.RHS)+mapInfo.end.H;
		int K2 = min(current.G,current.RHS);
		int OK2 = min(mapInfo.end.G, mapInfo.end.RHS);
		if (K1<OK1) return true;
		if (K1>OK1) return false;
		if (K2<OK2) return true;
		if (K2>OK2) return false;
		return true;
	}

	public Node Update(Node node,MapInfo mapInfo){
		if (node != mapInfo.start) {
			node.RHS = INFINITE;
			//获取从父节点到此节点的实际花费
			int GetCostToStatus = GetCost(mapInfo,node.parent.coord,node.coord);
			//计算此节点的rhs值
			int rhsValue = min(node.RHS,node.parent.G + GetCostToStatus);
			//查询之前搜索到的状态有没有此节点
			Node child = findNodeInOpen(node.coord);
			if(child == null){
				node.RHS = rhsValue;
				return node;
			}else{
				int existNodeparentGetCostToStatus = GetCost(mapInfo,child.parent.coord,child.coord);
				//判断新发现的节点是否为近路
				if(node.parent.G + GetCostToStatus <
						child.parent.G + existNodeparentGetCostToStatus){
					node.RHS = rhsValue;
					openList.remove(child);
					return node;
				}
				return node;
			}
		}
		return null;
	}

	public int GetCost(MapInfo mapInfo, Coord A,Coord B){
		int cost = 0;
		if(A.x != B.x && A.y != B.y) { cost = OBLIQUE_VALUE; }
		else{cost = DIRECT_VALUE;}
		if (mapInfo.maps[A.y][A.x] == BAR||mapInfo.maps[B.y][B.x] == BAR) cost = INFINITE;
		return cost;
	}
}
