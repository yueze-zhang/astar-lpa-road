package com.kesar.a;

import static java.lang.Math.min;
import static javafx.scene.input.KeyCode.O;

/**
 * 
 * ClassName: Node 
 * 
 * @Description: 路径结点
 * @author kesar
 */
public class Node implements Comparable<Node>
{

	public Coord coord; // 坐标
	public Node parent; // 父结点
	public int G; // G：是个准确的值，是起点到当前结点的代价
	public int H; // H：是个估值，当前结点到目的结点的估计代价

	public int getRHS() {
		return RHS;
	}

	public void setRHS(int RHS) {
		this.RHS = RHS;
	}

	public int RHS; // H：是个估值，当前结点到目的结点的估计代价

	public Node(int x, int y)
	{
		this.coord = new Coord(x, y);
	}

	public Node(Coord coord, Node parent, int g, int h)
	{
		this.coord = coord;
		this.parent = parent;
		G = g;
		H = h;
	}

	@Override
	public int compareTo(Node o)
	{
		if (o == null) return -1;
		//K1
		int K1 = min(G,RHS)+H;
		int OK1 = min(o.G, o.RHS)+o.H;
		int K2 = min(G,RHS);
		int OK2 = min(o.G, o.RHS);
		if (K1> OK1) return 1;
		if (K1< OK1) return -1;
		if (K2> OK2) return 1;
		if (K2< OK2) return -1;
		return 0;
	}
}
