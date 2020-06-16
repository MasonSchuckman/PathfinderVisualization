import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class PathfinderVisualization {
	public static void main(String [] args) {
		System.out.println("Welcome to my path finder visualizer!\n\tThis is a very rough project I quickly made, and I plan to add more advanced path finding algori"
				+ "thms such\n\tas Dijkstra's algorithm and A*, but for now it supports BFS and DFS!"
				+ "\n\n\tHope you Enjoy!!\n\nType 1 for BFS\nType 2 for DFS");
		//		Scanner sc=new Scanner(System.in); 
		//		int algo=sc.nextInt();
		//		sc.close();
		int algo=1;

		int width=50;
		int height=50;

		int startx=20;
		int starty=20;

		int endx=25;
		int endy=25;

		int [][]boxes=new int[height][width];
		boxes[starty][startx]=1;//1 is start
		boxes[endy][endx]=2;//2 is end
		boxes[9][6]=3;//3 is a barrier
		boxes[7][7]=3;//3 is a barrier
		boxes[8][7]=3;//3 is a barrier

		boxes[5][6]=3;//3 is a barrier
		boxes[6][7]=3;//3 is a barrier
		boxes[4][5]=3;//3 is a barrier
		
		mazeGenerator(boxes);
		int counter=0;
		Node[][] nodes=new Node[height][width];
		for(int i=0; i<height; i++) {
			for(int j=0; j<width; j++) {
				nodes[i][j]=new Node(boxes[i][j],counter,j,i);
				counter++;
			}
		}
		ArrayList<Node>blocks=new ArrayList<Node>();
		for(int i=0; i<height; i++) {
			for(int j=0; j<width; j++) {
				if(boxes[i][j]!=3) {
					if(i>0) {
						nodes[i][j].setConnected(nodes[i-1][j]);
					}
					if(i<height-1) {						
						nodes[i][j].setConnected(nodes[i+1][j]);
					}
					if(j>0) {						
						nodes[i][j].setConnected(nodes[i][j-1]);
					}
					if(j<width-1) {
						nodes[i][j].setConnected(nodes[i][j+1]);
					}
				}else {
					blocks.add(nodes[i][j]);
				}
			}
		}
		for(Node n:blocks) {
			n.removeConnections();
		}
		//all connections now setup, so we can start our search algorithm
		ArrayList<Node>tested=new ArrayList<Node>();
		Node start=nodes[starty][startx];
		start.parent=null;
		tested.add(nodes[starty][startx]);

		switch(algo) {
		case 1: BFS(nodes,start); break;
		case 2: DFS(nodes,tested,start,0); break;		
		default: System.out.println("Invalid!");
		}

		DrawBox db=new DrawBox(width,height,nodes,nodes[endy][endx],searchOrder);
		db.setVisible(true);
	}
	public static ArrayList<Path>paths=new ArrayList<Path>();
	static Path goodPath=new Path(null,Integer.MAX_VALUE);
	static LinkedList<Node>searchOrder=new LinkedList<Node>();

	public static void AStar(Node[][]nodes,Node start) {
		
	}
	
	public static void ds(Node[][]nodes,Node start) {
		
	}
	
	public static void mazeGenerator(int[][]boxes) {
		int height=boxes.length;
		int width=boxes[0].length;
		int min=0;
		int max=10;
		for(int i=0; i<height; i++) {
			for(int j=0; j<width; j++) {
				int randomNum = ThreadLocalRandom.current().nextInt(min, max + 1);
				if(boxes[i][j]==0&&randomNum>7) {
					boxes[i][j]=3;
				}
				
			}
		}
//		int currentSize=Integer.MAX_VALUE;
//		while(currentSize>8) {
//			currentSize=height*width;
//			
//		}

	}

	public static void BFS(Node[][]nodes,Node start) {
		Node current=start;
		LinkedList<Node>order=new LinkedList<Node>();
		ArrayList<Integer>visited=new ArrayList<Integer>();
		order.add(current);
		visited.add(current.num);
		int len=0;
		boolean running=true;
		while(!order.isEmpty()&&running) {			
			current = order.poll();			
			if(current.parent!=null)
				searchOrder.add(current);
			if(current.type==2) {				
				Path currentPath=new Path(current,len);
				if(currentPath.length>goodPath.length) {
					goodPath=currentPath;
				}
				running=false;
			}

			if(running) {
				for(Node n: current.children) {
					if(!visited.contains(n.num)) {
						order.add(n);

						n.parent=current;
						visited.add(n.num);					
					}
				}
			}
			len++;
		}
	}


	public static boolean DFS(Node[][]nodes,ArrayList<Node>tested,Node current,int len) {
		boolean found=false;
		if(current.parent!=null)
			searchOrder.add(current);
		for(Node child:current.children) {
			if(!tested.contains(child)) {					
				child.parent=current;
				if(child.type==2) {
					System.out.println("Found! "+ len);
					Path currentPath=new Path(child,len);
					if(currentPath.length>goodPath.length) {
						goodPath=currentPath;
					}
					tested.add(child);
					return true;
				}else {
					tested.add(child);

					found=found|| DFS(nodes,tested,child,len+1);
					if(found==true)
						return found;
				}					
			}
		}
		return found;
	}
}

class Path{
	ArrayList<Node>used;
	int length;
	public Path(Node last,int len) {
		used=new ArrayList<Node>();
		used.add(last);
		length=len;
	}
}





