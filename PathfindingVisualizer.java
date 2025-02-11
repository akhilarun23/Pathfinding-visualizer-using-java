import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.*;
import javax.swing.border.EtchedBorder;

public class PathfindingVisualizer extends JFrame implements ActionListener,ItemListener{
    public static void main(String[] args) {
        new PathfindingVisualizer();
    }

    private int cells=20;

    private final int mapSize=600;
    private int cSize=mapSize/cells;
    private int tool; //stores which tool selected(from checkboxes)

    JPanel panel;
    JButton startButton,resetButton,generateButton,clearButton;
    JComboBox<String> algorithmComboBox,toolComboBox;
    JLabel algL,toolL,gridSlideL,speedSlideL;
    JSlider gridSizeSlider,speedSlider;
    CheckboxGroup Checkgrp;
    Checkbox StartCB,FinishCB,WallCB,EraserCB;
    Map canvas;
    Node map[][];

    public PathfindingVisualizer(){
        
        this.clearMap();

        this.initial();
        
        

    }
    public void initial(){
        setSize(850,650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Path Finder");
        setLayout(null);

        panel=new JPanel();
        
        panel.setBounds(0,10,230,630);
        panel.setLayout(null);
        add(panel);
        panel.setBackground(Color.LIGHT_GRAY);
        

        startButton = new JButton("Start Search");
        startButton.setBounds(45,50,140,40);
        resetButton = new JButton("Reset");
        resetButton.setBounds(45,100,140,40);
        
        clearButton = new JButton("Clear Map");
        clearButton.setBounds(45,150,140,40);
    
        String[] algorithms = {"Dijkstra"};
        algL=new JLabel("Algorithms");
        algL.setBounds(45,200,140,25);
        algorithmComboBox = new JComboBox<>(algorithms);
        algorithmComboBox.setBounds(45,225,140,30);


        Checkgrp = new CheckboxGroup();
        StartCB = new Checkbox("Start",Checkgrp,true);
        FinishCB = new Checkbox("Finish",Checkgrp,false);
        WallCB = new Checkbox("Wall",Checkgrp,false);
        EraserCB = new Checkbox("Eraser",Checkgrp,false);
        toolL = new JLabel("Tools");                                      
        toolL.setBounds(45,275,120,25);
        StartCB.setBounds(45,300,50,25);
        FinishCB.setBounds(115,300,50,25);
        WallCB.setBounds(45,325,50,25);
        EraserCB.setBounds(115,325,50,25);
        

    
        
        gridSlideL =new JLabel("Grid:");
        gridSlideL.setBounds(10,365,40,25);
        gridSizeSlider = new JSlider(10, 50, 20);
        gridSizeSlider.setBounds(55,365,100,25);
        gridSizeSlider.setBackground(Color.LIGHT_GRAY);
        speedSlideL= new JLabel("Speed:");
        speedSlideL.setBounds(10,400,40,25);
        speedSlider = new JSlider(0, 100, 50);
        speedSlider.setBounds(55,400,100,25);
        speedSlider.setBackground(Color.LIGHT_GRAY);
        
        canvas= new Map();
        canvas.setBounds(230, 10, mapSize+1, mapSize+1);
		getContentPane().add(canvas);


        StartCB.addItemListener(this);
        FinishCB.addItemListener(this);
        WallCB.addItemListener(this);
        EraserCB.addItemListener(this);

        startButton.addActionListener(this);
        resetButton.addActionListener(this);
        clearButton.addActionListener(this);

    
        panel.add(startButton);
        panel.add(resetButton);
          
        panel.add(clearButton);
        panel.add(algL);
        panel.add(algorithmComboBox);
        panel.add(toolL);
        
        panel.add(StartCB);
        panel.add(FinishCB);
        panel.add(WallCB);
        panel.add(EraserCB);
        // panel.add(gridSlideL);
        // panel.add(gridSizeSlider);// future add
        // panel.add(speedSlideL);
        // panel.add(speedSlider);
        StartCB.setForeground(Color.RED);


        



        setVisible(true);

    }
    int startx,starty,finishx,finishy;

    public void clearMap(){
        startx=-1;
        starty=-1;
        finishx=-1;
        finishy=-1;
        map=new Node[cells][cells];//cells-length of the map panel
        for(int i=0;i<cells;i++){
            for(int j=0;j<cells;j++){
                map[i][j]=new Node(3, i, j);
            }
        }
    }
    public void resetMap() {	//RESET MAP
		for(int x = 0; x < cells; x++) {
			for(int y = 0; y < cells; y++) {
				Node current = map[x][y];
				if(current.cellType == 4 || current.cellType == 5)	//checked,finalpath
					map[x][y] = new Node(3,x,y);	//back emptynode
			}
		}
		if(startx > -1 && starty > -1) {	//reset strt,finsh
			map[startx][starty] = new Node(0,startx,starty);
			map[startx][starty].hops=0;
		}
		if(finishx > -1 && finishy > -1)
			map[finishx][finishy] = new Node(1,finishx,finishy);
		reset();	
	}
    public void reset() {	//reset
		solving = false;
		length = 0;
		checks = 0;
	}
    private boolean solving;
    private int length,checks;
    public void UpdatePaint(){
        cSize = mapSize/cells;
		canvas.repaint();
    }
    

    
    public void actionPerformed(ActionEvent e) {
        //actions
        if (e.getSource()==startButton) {
            reset();
            if (startx >-1 && starty >-1 && finishx >-1 && finishy >-1) {
                solving=true;
                new Algorithm().Dijkstra();
            }
        }
        if (e.getSource()==resetButton) {
            resetMap();
            solving=false;
            UpdatePaint();
        }
        if (e.getSource()==clearButton) {
            clearMap();
            solving=false;
            UpdatePaint();
        }


    }
    
    
    
    public void itemStateChanged(ItemEvent e) {
        StartCB.setForeground(Color.BLACK);
        FinishCB.setForeground(Color.BLACK);
        WallCB.setForeground(Color.BLACK);
        EraserCB.setForeground(Color.BLACK);
        if(StartCB.getState()==true) {
            tool=0;
            StartCB.setForeground(Color.RED);
        }
        else if(FinishCB.getState()==true){
            tool=1;
            FinishCB.setForeground(Color.RED);
        }
        else if(WallCB.getState()==true) {
            tool=2;
            WallCB.setForeground(Color.RED);
        }
        else if(EraserCB.getState()==true) {
            tool=3;
            EraserCB.setForeground(Color.RED);
        }
    }
    class Map extends JPanel implements MouseListener,MouseMotionListener{
        public Map(){
            addMouseListener(this);
            addMouseMotionListener(this);
        }
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            for(int x=0;x<cells;x++){
                for(int y=0;y<cells;y++){
                    switch (map[x][y].cellType) {
                        case 0:
                            g.setColor(Color.BLUE);
                            break;
                        case 1:
                            g.setColor(Color.RED);
                            break;
                        case 2:
                            g.setColor(Color.BLACK);
                            break;
                        case 3:
                            g.setColor(Color.WHITE);
                            break;
                        case 4:
                            g.setColor(Color.CYAN);
                            break;
                        case 5:
                            g.setColor(Color.YELLOW);
                            break;
                        
                    }
                    g.fillRect(x*cSize, y*cSize, cSize, cSize);
                    g.setColor(Color.BLACK);
                    g.drawRect(x*cSize, y*cSize, cSize, cSize);

                }
            }
    
        }
    
    
        public void mousePressed(MouseEvent e) {
            resetMap();
            try{
                int x= e.getX()/cSize;
                int y= e.getY()/cSize;
                Node current = map[x][y];
                switch (tool) {
                    case 0:
                        if(current.cellType!=2){
                            if(startx>-1 && starty>-1){
                                map[startx][starty].cellType=3;
                                map[startx][starty].hops=-1;

                            }
                            current.hops=0;
                            startx = x;
                            starty = y;
                            current.cellType=0;
                        }
                        break;

                    case 1:
                        if (current.cellType!=2) {
                            if(finishx>-1 && finishy>-1){
                                map[finishx][finishy].cellType=3;
                                
                            }
                            finishx=x;
                            finishy=y;
                            current.cellType=1;
                        }

                        break;
                
                    default:
                        if (current.cellType!=0 && current.cellType!=1) {
                            current.cellType=tool;
                            
                        }
                        break;
                }
                UpdatePaint();

            }catch(Exception exc){
                System.out.println(exc);
            }

        }
    
        public void mouseDragged(MouseEvent e) {
            try{
                int x= e.getX()/cSize;
                int y= e.getY()/cSize;
                Node current = map[x][y];
                if (tool==3||tool==2 && (current.cellType!=0 && current.cellType!=1)) {
                    current.cellType=tool;
                }
                UpdatePaint();


            }catch(Exception exc){
                System.out.println(exc);
            }
        }
    
    
        public void mouseMoved(MouseEvent e) {
           
        }
    
    
        public void mouseClicked(MouseEvent e) {
            
        }
    
    
        
    
        public void mouseReleased(MouseEvent e) {
           
        }
    
    
        public void mouseEntered(MouseEvent e) {
            
        }
    
    
        public void mouseExited(MouseEvent e) {
            
        }
        
    }
    int delay=30; 
    class Node{
        private int cellType=0,hops,x,y,lastX,lastY;
		
        public Node(int cellType, int x, int y){
            this.cellType = cellType; // 0-start 1-finish 2-wall 3-empty 4-checked 5-finalpath
            this.x=x;
            this.y=y;
            hops=-1;
        }
        public void setLastNode(int x, int y) {lastX = x; lastY = y;}
    }
    class Algorithm{
        public void Dijkstra() {
    SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
        @Override
        protected Void doInBackground() {
            PriorityQueue<Node> priority = new PriorityQueue<>((n1, n2) -> n1.hops - n2.hops);
            priority.add(map[startx][starty]);

            while (!priority.isEmpty()&&solving==true) {
                Node current = priority.poll();

                if (current.cellType == 1) { // Path found
                    backtrack(current.lastX, current.lastY, current.hops);
                    publish(); 
                    return null;
                }

                int hops = current.hops + 1;
                ArrayList<Node> explored = exploreNeighbors(current, hops);

                for (Node neighbor : explored) {
                    priority.add(neighbor);
                }

                publish(); // Update visualization after exploring neighbors
                try {
                    Thread.sleep(delay); // Delay to visualize the process gradually
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            solving = false; // No path found
            publish(); // Update visualization to indicate no path
            return null;
        }

        @Override
        protected void process(List<Void> chunks) {
            UpdatePaint(); // Update the UI
        }

        @Override
        protected void done() {
            try {
                get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        
    };

    worker.execute(); // Start the SwingWorker
}
        
          

        public ArrayList<Node> exploreNeighbors(Node current, int hops) {	//to explore neighbors
			ArrayList<Node> explored = new ArrayList<Node>();	
			for(int a = -1; a <= 1; a++) {
				for(int b = -1; b <= 1; b++) {
					int xbound = current.x+a;
					int ybound = current.y+b;
					if((xbound > -1 && xbound < cells) && (ybound > -1 && ybound < cells)) {	
						Node neighbor = map[xbound][ybound];
						if((neighbor.hops==-1 || neighbor.hops > hops) && neighbor.cellType!=2) {	//not a wall or not explored
							explore(neighbor, current.x, current.y, hops);	
							explored.add(neighbor);	
						}
					}
				}
			}
			return explored;
		}
		
		public void explore(Node current, int lastx, int lasty, int hops) {	
			if(current.cellType!=0 && current.cellType != 1)	
				current.cellType=4;	//explored
			current.setLastNode(lastx, lasty);	
			current.hops=hops;	
			checks++;
			if(current.cellType == 1) {	//finish then backtrack
				backtrack(current.lastX, current.lastY,hops);
                
			}

		}
        public void backtrack(int lx, int ly, int hops) {	
			length = hops;
			while(hops > 1) {	//end to start
				Node current = map[lx][ly];
				current.cellType=5;
                
				lx = current.lastX;
				ly = current.lastY;
				hops--;
                
			}
			solving = false;
		}
    }
    
    

}
