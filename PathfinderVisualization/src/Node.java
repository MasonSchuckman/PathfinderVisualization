import java.util.ArrayList;

public class Node{
	ArrayList<Node>children;
	Node parent;
	int type;
	int num;
	int x,y;
	int dsCost=Integer.MAX_VALUE;
	int astarCost=Integer.MAX_VALUE;
	public Node(int type,int num,int x,int y) {
		children=new ArrayList<Node>();
		this.type=type;
		this.num=num;
		this.x=x;
		this.y=y;
		
	}
	public void setConnected(Node kid) {
		children.add(kid);
		if(!kid.children.contains(this))//if the two arent already connected, connect them, else, stop.
			kid.setConnected(this);
	}
	public void removeConnections() {
		for(int i=children.size()-1; i>=0; i--) {			
			Node n=children.get(i);
			n.children.remove(this);
			children.remove(i);
		}
	}
}