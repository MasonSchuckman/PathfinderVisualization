import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class PathfinderVisualization {
	static int width=450/4;
	static int height=450/4;

	//			static int startx=10;
	//			static int starty=150;
	//			
				static int endx=width/2;
				static int endy=height/2;

	static int startx=ThreadLocalRandom.current().nextInt(0, width);
	static int starty=ThreadLocalRandom.current().nextInt(0, height);
//	static int endx=ThreadLocalRandom.current().nextInt(0, width);
//	static int endy=ThreadLocalRandom.current().nextInt(0, height);

	public static void main(String [] args) {
		System.out.println("Welcome to my path finder visualizer!\n\tThis is a very rough project I quickly made, and I plan to add more advanced path finding algori"
				+ "thms such\n\tas Dijkstra's algorithm and A*, but for now it supports BFS and DFS!"
				+ "\n\n\tHope you Enjoy!!\n\nType 1 for BFS\nType 2 for DFS");
		//		Scanner sc=new Scanner(System.in); 
		//		int algo=sc.nextInt();
		//		sc.close();
		int algo=3;






		if(endy==starty)
			endy=starty--;

		int [][]boxes=new int[height][width];
		boxes[starty][startx]=1;//1 is start
		boxes[endy][endx]=2;//2 is end
		//				boxes[4][7]=3;//3 is a barrier
		//				boxes[5][7]=3;//3 is a barrier
		//				boxes[6][7]=3;//3 is a barrier
		//				boxes[1][7]=3;//3 is a barrier
		//				boxes[2][7]=3;//3 is a barrier
		//				boxes[3][7]=3;//3 is a barrier\
		//				boxes[0][7]=3;//3 is a barrier
		//
		//		boxes[5][6]=3;//3 is a barrier
		//		boxes[6][7]=3;//3 is a barrier
		//		boxes[4][5]=3;//3 is a barrier



		mazeGenerator2(boxes);


		int counter=0;
		Node[][] nodes=new Node[height][width];
		for(int i=0; i<height; i++) {
			for(int j=0; j<width; j++) {
				nodes[i][j]=new Node(boxes[i][j],counter,j,i);
				nodes[i][j].astarCost=(Math.abs(i-endy)+Math.abs(j-endx));
				nodes[i][j].dsCost=Integer.MAX_VALUE;				
				counter++;
			}
		}
		nodes[starty][startx].dsCost=0;

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
		case 1: BFS(nodes,start,false); break;
		case 2: DFS(nodes,tested,start,0); break;
		case 3: AStar(nodes,start); break;
		default: System.out.println("Invalid!");
		}
		Node current=nodes[endy][endx];
		int c=0;
		while(current.parent.parent!=null) {//drawing the used boxes			
			current=current.parent;
			c++;
		}
		System.out.println("aStar len: "+c);

		DrawBox db=new DrawBox(width,height,nodes,nodes[endy][endx],(LinkedList<Node>)searchOrder.clone());
		db.setVisible(true);


		//repeated for testing
		boolean testing=false;
		if(testing) {
			counter=0;
			nodes=new Node[height][width];
			for(int i=0; i<height; i++) {
				for(int j=0; j<width; j++) {
					nodes[i][j]=new Node(boxes[i][j],counter,j,i);
					nodes[i][j].astarCost=(Math.abs(i-endy)+Math.abs(j-endx));
					nodes[i][j].dsCost=Integer.MAX_VALUE;				
					counter++;
				}
			}
			nodes[starty][startx].dsCost=0;

			blocks=new ArrayList<Node>();
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
			tested=new ArrayList<Node>();
			start=nodes[starty][startx];
			start.parent=null;
			tested.add(nodes[starty][startx]);


			searchOrder.clear();
			BFS(nodes,start,false);
			current=nodes[endy][endx];
			c=0;
			while(current.parent.parent!=null) {//drawing the used boxes			
				current=current.parent;
				c++;
			}
			System.out.println("bfs len: "+c);
		
			DrawBox db2=new DrawBox(width,height,nodes,nodes[endy][endx],(LinkedList<Node>)searchOrder.clone());
			db2.setVisible(true);
		}
	}
	public static ArrayList<Path>paths=new ArrayList<Path>();
	static Path goodPath=new Path(null,Integer.MAX_VALUE);
	static LinkedList<Node>searchOrder=new LinkedList<Node>();

	@SuppressWarnings("unchecked")
	public static void AStar(Node[][]nodes,Node start) {
		Node current=start;

		LinkedList<Node>order=new LinkedList<Node>();
		ArrayList<Integer>visited=new ArrayList<Integer>();

		order.add(current);
		visited.add(current.num);

		int len=0;
		boolean running=true;
		current.dsCost=0;

		while(!order.isEmpty()&&running) {	
			current = order.pop();		


			if(current.parent!=null)
				searchOrder.add(current);
			if(current.type==2) {	
				searchOrder.remove(current);
				Path currentPath=new Path(current,len);
				if(currentPath.length>goodPath.length) {
					goodPath=currentPath;
				}
				running=false;
			}


			if(running) {
				for(Node n: current.children) {
					if(n.dsCost>current.dsCost+1) {
						order.push(n);
						visited.add(n.num);	
						n.dsCost=current.dsCost+1;
						n.parent=current;	
					}
				}
			}

			Collections.sort(order);			
			len++;

		}
	}

	public static void ds(Node[][]nodes,Node start) {

	}

	public static void mazeGenerator2(int[][]boxes) {
		int height=boxes.length;
		int width=boxes[0].length;

		for(int row=0; row<height-2; row++) {
			for(int col=4; col<width-5; col+=2) {
				int req=5;
				if(row>height/1.5)
					req=11;
				else
					req=3;
				int randomNum = ThreadLocalRandom.current().nextInt(0, 13 + 1);
				if(boxes[row][col]==0&&randomNum>req) {
					boxes[row][col]=3;
				}
			}
		}

		for(int row=0; row<height-2; row+=2) {
			for(int col=4; col<width-5; col++) {
				int req=5;
				if(col>width/2)
					req=12;
				else
					req=9;
				int randomNum = ThreadLocalRandom.current().nextInt(0, 13 + 1);
				if(boxes[row][col]==0&&randomNum>req) {
					boxes[row][col]=3;
				}
			}
		}
	}

	public static void mazeGenerator(int[][]boxes) {
		int height=boxes.length;
		int width=boxes[0].length;
		int min=0;
		int max=10;
		boolean generating=true;
		while(generating) {
			for(int i=0; i<height; i++) {
				for(int j=0; j<width; j++) {
					int randomNum = ThreadLocalRandom.current().nextInt(min, max + 1);
					if(boxes[i][j]==0&&randomNum>8) {
						boxes[i][j]=3;
					}				
				}
			}
			int counter=0;
			Node[][] nodes=new Node[height][width];
			for(int i=0; i<height; i++) {
				for(int j=0; j<width; j++) {
					nodes[i][j]=new Node(boxes[i][j],counter,j,i);
					nodes[i][j].astarCost=(Math.abs(i-endy)+Math.abs(j-endx));
					nodes[i][j].dsCost=Integer.MAX_VALUE;					
					counter++;
				}
			}

			nodes[starty][startx].dsCost=0;

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
			Node start=nodes[starty][startx];
			start.parent=null;
			if(BFS(nodes,start,true))
				generating=false;
		}
	}

	public static boolean BFS(Node[][]nodes,Node start,boolean testingValidMap) {
		Node current=start;
		LinkedList<Node>order=new LinkedList<Node>();
		ArrayList<Integer>visited=new ArrayList<Integer>();
		order.add(current);
		visited.add(current.num);
		int len=0;
		boolean running=true;
		while(!order.isEmpty()&&running) {			
			current = order.poll();			
			if(current.parent!=null&&!testingValidMap)
				searchOrder.add(current);
			if(current.type==2) {				
				Path currentPath=new Path(current,len);
				if(currentPath.length>goodPath.length) {
					goodPath=currentPath;
				}
				running=false;				
				return true;
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

		return false;
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





