import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.*;


public class DrawBox extends JFrame{
	
	Node[][]data;
	Node end;
	int sizex=900;
	int sizey=900;
	int width;
	int height;
	ArrayList<Node>path;
	LinkedList<Node>order;
	public DrawBox(int wid,int high,Node[][]dataPoints,Node end,LinkedList<Node>searchOrder){
		JPanel panel=new JPanel();
		getContentPane().add(panel);
		setSize(sizex+30,sizey+60);
		width=wid;
		height=high;
		JButton button =new JButton("press");
		panel.add(button);
		data=dataPoints;
		this.end=end;
		order=searchOrder;
		
	}

	public void paint(Graphics g) {
		//super.paint(g);  // fixes the immediate problem.
		g.translate(10, 40);
		int boxSize=sizex/width;
		for(int i=0; i<height; i++) {
			for(int j=0; j<width; j++) {
				if(data[i][j].type==1) {
					g.setColor(Color.green);//color of the start
					g.fillRect(j*boxSize, i*boxSize, boxSize, boxSize);
				}
				if(data[i][j].type==2) {
					g.setColor(Color.magenta);//color of the end
					g.fillRect(j*boxSize, i*boxSize, boxSize, boxSize);
				}
				if(data[i][j].type==3) {
					g.setColor(Color.black);
					g.fillRect(j*boxSize, i*boxSize, boxSize, boxSize);
				}
				if(data[i][j].type==0) {
					g.setColor(Color.black);
					g.drawRect(j*boxSize, i*boxSize, boxSize, boxSize);
				}  
			}
		}
		int sleeping=3;
		//LinkedList<Node>order=(LinkedList<Node>)this.order.clone();
		Node current=this.end;
		while(!order.isEmpty()) {//drawing the tried paths		
			current=order.poll();
			int x=current.x;
			int y=current.y;
			
			g.setColor(Color.red);
			g.fillRect(x*boxSize, y*boxSize, boxSize, boxSize);
			g.setColor(Color.black);
			g.drawRect(x*boxSize, y*boxSize, boxSize, boxSize);
			try {
				Thread.sleep(sleeping);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		current=this.end;
		int c=0;
		while(current.parent.parent!=null) {//drawing the used boxes			
			current=current.parent;
			int x=current.x;
			int y=current.y;
			c++;
			g.setColor(Color.BLUE);
			g.fillRect(x*boxSize, y*boxSize, boxSize, boxSize);
			g.setColor(Color.black);
			g.drawRect(x*boxSize, y*boxSize, boxSize, boxSize);
			try {
				Thread.sleep(sleeping/2);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("drawn len: "+c);
	}
	

}
