/**
 *Max Zirbel
 * Press the "r" key to change the tile color to red
 * Press the "b" key to change the tile color to blue
 * Press the "g" key to change the tile color to green
 * Press the space bar to change the tile color to a random color
 * This implementation also prevents the edges from grow more quickly than
 * the rest of design, allowing more of the pattern to display before
 * each resize.
 * 
 * Left Click to begin tiling from all four corners, when the tiles meet in
 * middle the canvas will clear and the grid size will double and begin to re-tile
 * Left Click again to return to the tradition implementation.
 * Right Click to exit the program
 */

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLJPanel;
import javax.media.opengl.glu.GLU;
import javax.swing.JFrame;

import com.jogamp.opengl.util.FPSAnimator;

public class SelfAssembly extends JFrame
    implements GLEventListener, KeyListener, MouseListener
{
    GLU glu;

    static GLCapabilities caps;
    static FPSAnimator animator;

    static int grid_size = 20;
    static int frame_rate = 120;

    int displaySize = grid_size;
    Tile tiles [][];
    Tile tiles2[][] = new Tile[400][400];
    Tile tiles3[][] = new Tile[400][400];
    Tile tiles4[][] = new Tile[400][400];
    Tile tempTiles[][] = new Tile[400][400];
    Tile tile = new Tile(0);
    Tile tile2 = new Tile(0);
    Tile tile3 = new Tile(0);
    Tile tile4 = new Tile(0);
    ArrayList<Location> tileChange =  new ArrayList<Location>();
    ArrayList<Location> tileChange2 =  new ArrayList<Location>();
    ArrayList<Location> tileChange3 =  new ArrayList<Location>();
    ArrayList<Location> tileChange4 =  new ArrayList<Location>();
    Location location = new Location(grid_size-1, 0);
    Location location2;
    Location location3;
    Location location4;

    Random rand = new Random();
    int randLoc = 0;
    int randLoc2 = 0;
    int randLoc3 = 0;
    int randLoc4 = 0;
    Location temp;
    Location tempLoc;
    Location tempLoc2;
    Location tempLoc3;
    Location tempLoc4;
    float green = 1;
    float red = 0;
    float blue = 0;
    boolean oneCorner = true;
    

    public SelfAssembly() {
	super("SelfAssembly");
    }

    public static void main(String[] args) {

        caps = new GLCapabilities(GLProfile.getGL2GL3());
        caps.setDoubleBuffered(true); // request double buffer display mode
        caps.setHardwareAccelerated(true);
        GLJPanel canvas = new GLJPanel();

        SelfAssembly animated_tiles = new SelfAssembly();
        canvas.addGLEventListener(animated_tiles);

        canvas.addKeyListener(animated_tiles);
        canvas.addMouseListener(animated_tiles); 
        animator = new FPSAnimator(canvas, frame_rate);

        JFrame frame = new JFrame("AnimatedTiles");
        frame.setSize(600,600); // Size in pixels of the frame we draw on
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(canvas);
        frame.setVisible(true);
        canvas.requestFocusInWindow();
        animated_tiles.run();
    }

    public void run()
    {
    	animator.start();
    }
    
    //////////////////////////////////////////////////////////
    // init, display, reshape, dispose and displayChanged comprise the
    // GLEventListener interface

    public void init(GLAutoDrawable drawable) { 

    	tiles = new Tile[400][400];
        GL2 gl = drawable.getGL().getGL2();    
        glu = new GLU();

        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluOrtho2D(0.0f, (float)grid_size, 0.0f, (float)grid_size);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
    }
    
    public void display(GLAutoDrawable drawable) { 
        GL2 gl = drawable.getGL().getGL2();    
        if(oneCorner == true) {
	        //There are no tiles on the board
	        if(tiles[grid_size -1][0] == null)
	        {
	        	reset(gl);
	        	//add location to array list
	        	location = new Location(grid_size -1, 0);
	        	tileChange.add(location);
	        }
	        
	        //randomly select a location from the array list
	        randLoc = rand.nextInt(tileChange.size());
	        location = tileChange.get(randLoc);
	        if(location.x == grid_size -1 || location.y == 0) {
	        		randLoc = rand.nextInt(tileChange.size());
	                location = tileChange.get(randLoc);
	        }
	        //remove location from array
	        tileChange.remove(randLoc);
	        
	        //decide what tile to place at selected location
	        if(location.x == grid_size -1 && location.y == 0) {
	        	tile.type = 0;
	        	tiles[location.x][location.y] = tile;
	        	//draw the tile
	        	drawTile(gl, tile, location);
	        	
	        	//add new locations to the array list
	        	location = new Location(grid_size -1, 1);
	        	tileChange.add(location);
	        	location = new Location(grid_size - 2, 0);
	        	tileChange.add(location);
	        } else if(location.x == grid_size - 1) {
	        	//tile is on top edge
	        	tile = new Tile(1);
	        	tiles[location.x][location.y] = tile;
	        	//draw the tile
	        	drawTile(gl, tile, location);
	        	//add new locations to the array
	        		tempLoc = new Location(location.x, location.y + 1);
	        		tileChange.add(tempLoc);
	        	//check if there is a tile back one down one
	        	if(tiles[location.x - 1][location.y -1] != null){// && location.x - 1 >= 0){
	        		tempLoc = new Location(location.x - 1, location.y);
	            	tileChange.add(tempLoc);
	        	}
	        }else if(location.y == 0) {
	            //tile is on left edge
	           	tile = new Tile(1);
	           	tiles[location.x][location.y] = tile;
	           	//draw the tile
	           	drawTile(gl, tile, location);
	           	//add new locations to the array
	           		tempLoc = new Location(location.x - 1, location.y);
	           		tileChange.add(tempLoc);
	           	//check if there is a tile up one right one
	           	if(tiles[location.x + 1][location.y + 1] != null) {
	           		tempLoc = new Location(location.x, location.y + 1);
	               	tileChange.add(tempLoc);
	           	}
	        } else if (location.x != grid_size && location.y != 0){
	        	//tile is NOT on an an edge
	        	//determine what tile to place
	        	//check what tile is above you
	        	if(tiles[location.x + 1][location.y].type == 1) {
	        		//the above tile is of type one
	        		//check tile type to left
	        		if(tiles[location.x][location.y -1].type == 1) {
	        			//tile should be type 2
	        			tile = new Tile(2);
	                   	tiles[location.x][location.y] = tile;
	                   	drawTile(gl, tile, location);
	        		} else if (tiles[location.x][location.y -1].type == 2) {
	        			tile = new Tile(3);
	                   	tiles[location.x][location.y] = tile;
	        		} else if (tiles[location.x][location.y -1].type == 3) {
	        			tile = new Tile(2);
	                   	tiles[location.x][location.y] = tile;
	        		}
	        	} else if (tiles[location.x + 1][location.y].type == 2) {
	        		//above tile is type 2
	        		if(tiles[location.x][location.y -1].type == 1) {
	        			//tile should be type 2
	        			tile = new Tile(5);
	                   	tiles[location.x][location.y] = tile;
	        		} else if (tiles[location.x][location.y -1].type == 2) {
	        			tile = new Tile(4);
	                   	tiles[location.x][location.y] = tile;
	        		} else if (tiles[location.x][location.y -1].type == 3) {
	        			tile = new Tile(5);
	                   	tiles[location.x][location.y] = tile;
	        		}
	        	} else if (tiles[location.x + 1][location.y].type == 3) {
	        		//above tile is type 3
	        		if(tiles[location.x][location.y -1].type == 3) {
	        			tile = new Tile(2);
	                   	tiles[location.x][location.y] = tile;
	        		} else if (tiles[location.x][location.y -1].type == 5) {
	        			tile = new Tile(2);
	                   	tiles[location.x][location.y] = tile;
	        		} else if (tiles[location.x][location.y -1].type == 4) {
	        			tile = new Tile(3);
	                   	tiles[location.x][location.y] = tile;
	        		}
	        	} else if (tiles[location.x + 1][location.y].type == 4) {
	        		//above tile is type 4
	        		if(tiles[location.x][location.y -1].type == 4) {
	        			tile = new Tile(4);
	                   	tiles[location.x][location.y] = tile;
	        		}
	                if(tiles[location.x][location.y -1].type == 5) {
	            		tile = new Tile(5);
	                    tiles[location.x][location.y] = tile;
	        		}
	        	} else if(tiles[location.x + 1][location.y].type == 5) {
	        		if(tiles[location.x][location.y -1].type == 3) {
	        			tile = new Tile(2);
	                   	tiles[location.x][location.y] = tile;
	        		} else if (tiles[location.x][location.y -1].type == 1) {
	        			tile = new Tile(2);
	                   	tiles[location.x][location.y] = tile;
	        		} else if (tiles[location.x][location.y -1].type == 2) {
	        			tile = new Tile(3);
	                   	tiles[location.x][location.y] = tile;
	        		}
	        	}
		        drawTile(gl, tile, location);
		        //add possible locations for tiles
		        if(tiles[location.x + 1][location.y + 1] != null) {
		       		tempLoc = new Location(location.x, location.y + 1);
		           	tileChange.add(tempLoc);
		        }
		    	//check if there is a tile back one down one
		    	if(tiles[location.x  - 1][location.y -1] != null) { 
		    		tempLoc = new Location(location.x - 1, location.y);
		        	tileChange.add(tempLoc);
		    	}
	        }
	        gl.glFlush ();
	        if(location.x == 0 || location.y == grid_size) {
	        	resize(gl);
	        }
        } else {
        	fourCorners(gl);
        }
    }
   
	private void fourCorners(GL2 gl) {
		lowerLeft(gl);
		upperLeft(gl);
		upperRight(gl);
		lowerRight(gl);
	}

	private void lowerLeft(GL2 gl) {
		 if(tiles4[0][0] == null)
	        {
			 	reset(gl);
	        	//add location to array list
	        	location4 = new Location(0, 0);
	        	tileChange4.add(location4);
	        }
	        //randomly select a location from the array list
	        randLoc4 = rand.nextInt(tileChange4.size());
	        location4 = tileChange4.get(randLoc4);
	        if(location4.x == 0 || location4.y == 0) {
	        		randLoc4 = rand.nextInt(tileChange4.size());
	                location4 = tileChange4.get(randLoc4);
	        }
	        //remove location from array
	        tileChange4.remove(randLoc4);
	        
	        //decide what tile to place at selected location
	        if(location4.x == 0 && location4.y == 0) {
	        	tile4.type = 0;
	        	tiles4[location4.x][location4.y] = tile4;
	        	//draw the tile
	        	System.out.println(location4.x + "  " + location4.y);
	        	drawTile4(gl, tile4, location4);
	        	
	        	//add new locations to the array list
	        	location4 = new Location(0, 1);
	        	tileChange4.add(location4);
	        	location4 = new Location(1, 0);
	        	tileChange4.add(location4);
	        } else if(location4.x == 0) {
	        	//tile is on bottom edge
	        	tile4 = new Tile(1);
	        	tiles4[location4.x][location4.y] = tile4;
	        	//draw the tile
	        	drawTile4(gl, tile4, location4);
	        	//add new locations to the array
	        	if(location4.y + 1 < grid_size /2){
	        		tempLoc4 = new Location(location4.x, location4.y + 1);
	        		tileChange4.add(tempLoc4);
	        	}
	        	//check if there is a tile back one down one
	        	if(tiles4[location4.x + 1][location4.y  - 1] != null){
	        		tempLoc4 = new Location(location4.x + 1, location4.y);
	            	tileChange4.add(tempLoc4);
	        	}
	        }else if(location4.y == 0) {
	            //tile is on left edge
	           	tile4 = new Tile(1);
	           	tiles4[location4.x][location4.y] = tile4;
	           	//draw the tile
	           	drawTile4(gl, tile4, location4);
	           	//add new locations to the array
	           	if(location4.x + 1 < grid_size /2) {
	           		tempLoc4 = new Location(location4.x + 1, location4.y);
	           		tileChange4.add(tempLoc4);
	           	}
	           	//check if there is a tile up one right one
	           	if(tiles4[location4.x - 1][location4.y + 1] != null) {
	           		tempLoc4 = new Location(location4.x, location4.y + 1);
	               	tileChange4.add(tempLoc4);
	           	}
	        } else if (location4.x != 0 && location4.y != 0){
	        	//tile is NOT on an an edge
	        	//determine what tile to place
	        	//check what tile is above you
	        	if(tiles4[location4.x - 1][location4.y].type == 1) {
	        		//the above tile is of type one
	        		//check tile type to left
	        		if(tiles4[location4.x][location4.y -1].type == 1) {
	        			//tile should be type 2
	        			tile4 = new Tile(2);
	                   	tiles4[location4.x][location4.y] = tile4;
	                   	drawTile4(gl, tile4, location4);
	        		} else if (tiles4[location4.x][location4.y -1].type == 2) {
	        			tile4 = new Tile(3);
	                   	tiles4[location4.x][location4.y] = tile4;
	        		} else if (tiles4[location4.x][location4.y -1].type == 3) {
	        			tile4 = new Tile(2);
	                   	tiles4[location4.x][location4.y] = tile4;
	        		}
	        	} else if (tiles4[location4.x - 1][location4.y].type == 2) {
	        		//above tile is type 2
	        		if(tiles4[location4.x][location4.y -1].type == 1) {
	        			//tile should be type 2
	        			tile4 = new Tile(5);
	                   	tiles4[location4.x][location4.y] = tile4;
	        		} else if (tiles4[location4.x][location4.y -1].type == 2) {
	        			tile4 = new Tile(4);
	                   	tiles4[location4.x][location4.y] = tile4;
	        		} else if (tiles4[location4.x][location4.y -1].type == 3) {
	        			tile4 = new Tile(5);
	                   	tiles4[location4.x][location4.y] = tile4;
	        		}
	        	} else if (tiles4[location4.x - 1][location4.y].type == 3) {
	        		//above tile is type 3
	        		if(tiles4[location4.x][location4.y -1].type == 3) {
	        			tile4 = new Tile(2);
	                   	tiles4[location4.x][location4.y] = tile4;
	        		} else if (tiles4[location4.x][location4.y -1].type == 5) {
	        			tile4 = new Tile(2);
	                   	tiles4[location4.x][location4.y] = tile4;
	        		} else if (tiles4[location4.x][location4.y -1].type == 4) {
	        			tile4 = new Tile(3);
	                   	tiles4[location4.x][location4.y] = tile4;
	        		}
	        	} else if (tiles4[location4.x - 1][location4.y].type == 4) {
	        		//above tile is type 4
	        		if(tiles4[location4.x][location4.y -1].type == 4) {
	        			tile4 = new Tile(4);
	                   	tiles4[location4.x][location4.y] = tile4;
	        		}
	                if(tiles4[location4.x][location4.y -1].type == 5) {
	            		tile4 = new Tile(5);
	                    tiles4[location4.x][location4.y] = tile4;
	        		}
	        	} else if(tiles4[location4.x - 1][location4.y].type == 5) {
	        		if(tiles4[location4.x][location4.y -1].type == 3) {
	        			tile4 = new Tile(2);
	                   	tiles4[location4.x][location4.y] = tile4;
	        		} else if (tiles4[location4.x][location4.y -1].type == 1) {
	        			tile4 = new Tile(2);
	                   	tiles4[location4.x][location4.y] = tile4;
	        		} else if (tiles4[location4.x][location4.y -1].type == 2) {
	        			tile4 = new Tile(3);
	                   	tiles4[location4.x][location4.y] = tile4;
	        		}
	        	}
		        drawTile4(gl, tile4, location4);
		        //add possible locations for tiles
		        if(tiles4[location4.x - 1][location4.y + 1] != null) {
		       		tempLoc4 = new Location(location4.x, location4.y + 1);
		           	tileChange4.add(tempLoc4);
		        }
		    	//check if there is a tile back one down one
		    	if(tiles4[location4.x  + 1][location4.y - 1] != null) { 
		    		tempLoc4 = new Location(location4.x + 1, location4.y);
		        	tileChange4.add(tempLoc4);
		    	}
	        }
	        gl.glFlush ();
		
	}
	private void upperRight(GL2 gl) {
		//There are no tiles on the board
        if(tiles2[grid_size -1][grid_size -1] == null)
        {
        	//add location to array list
        	location2 = new Location(grid_size -1, grid_size -1);
        	tileChange2.add(location2);
        }
        
        //randomly select a location from the array list
        randLoc2 = rand.nextInt(tileChange2.size());
        location2 = tileChange2.get(randLoc2);
        if(location2.x == grid_size -1 || location2.y == grid_size -1) {
        		randLoc2 = rand.nextInt(tileChange2.size());
                location2 = tileChange2.get(randLoc2);
        }
        //remove location from array
        tileChange2.remove(randLoc2);
        
        //decide what tile to place at selected location
        if(location2.x == grid_size -1 && location2.y == grid_size -1) {
        	tile2.type = 0;
        	tiles2[location2.x][location2.y] = tile2;
        	//draw the tile
        	drawTile2(gl, tile2, location2);
        	
        	//add new locations to the array list
        	location2 = new Location(grid_size -1, grid_size - 2);
        	tileChange2.add(location2);
        	location2 = new Location(grid_size - 2, grid_size -1);
        	tileChange2.add(location2);
        } else if(location2.x == grid_size - 1) {
        	//tile is on top edge
        	tile2 = new Tile(1);
        	tiles2[location2.x][location2.y] = tile2;
        	//draw the tile
        	drawTile2(gl, tile2, location2);
        	//add new locations to the array
        	if(location2.y - 1 >= grid_size /2){
        		tempLoc2 = new Location(location2.x, location2.y -1);
        		tileChange2.add(tempLoc2);
        	}
        	//check if there is a tile back one down one
        	if(tiles2[location2.x - 1][location2.y + 1] != null){
        		tempLoc2 = new Location(location2.x - 1, location2.y);
            	tileChange2.add(tempLoc2);
        	}
        }else if(location2.y == grid_size -1) {
            //tile is on left edge
           	tile2 = new Tile(1);
           	tiles2[location2.x][location2.y] = tile2;
           	//draw the tile
           	drawTile2(gl, tile2, location2);
           	//add new locations to the array
           	if(location2.x - 1 >= grid_size /2) {
           		tempLoc2 = new Location(location2.x - 1, location2.y);
           		tileChange2.add(tempLoc2);
           	}
           	//check if there is a tile up one right one
           	if(tiles2[location2.x + 1][location2.y - 1] != null) {
           		tempLoc2 = new Location(location2.x, location2.y - 1);
               	tileChange2.add(tempLoc2);
           	}
           	//******
        } else if (location2.x != grid_size || location2.y != grid_size -1){
        	//tile is NOT on an an edge
        	//determine what tile to place
        	//check what tile is above you
        	if(tiles2[location2.x + 1][location2.y].type == 1) {
        		//the above tile is of type one
        		//check tile type to right
        		if(tiles2[location2.x][location2.y + 1].type == 1) {
        			//tile should be type 2
        			tile2 = new Tile(2);
                   	tiles2[location2.x][location2.y] = tile2;
                   	drawTile2(gl, tile2, location2);
        		} else if (tiles2[location2.x][location2.y + 1].type == 2) {
        			tile2 = new Tile(3);
                   	tiles2[location2.x][location2.y] = tile2;
        		} else if (tiles2[location2.x][location2.y + 1].type == 3) {
        			tile2 = new Tile(2);
                   	tiles2[location2.x][location2.y] = tile2;
        		}
        	}else if (tiles2[location2.x + 1][location2.y].type == 2) {
        		//above tile is type 2
        		if(tiles2[location2.x][location2.y + 1].type == 1) {
        			//tile should be type 2
        			tile2 = new Tile(5);
                   	tiles2[location2.x][location2.y] = tile2;
        		} else if (tiles2[location2.x][location2.y + 1].type == 2) {
        			tile2 = new Tile(4);
                   	tiles2[location2.x][location2.y] = tile2;
        		} else if (tiles2[location2.x][location2.y + 1].type == 3) {
        			tile2 = new Tile(5);
                   	tiles2[location2.x][location2.y] = tile2;
        		}
        	} else if (tiles2[location2.x + 1][location2.y].type == 3) {
        		//above tile is type 3
        		if(tiles2[location2.x][location2.y + 1].type == 3) {
        			tile2 = new Tile(2);
                   	tiles2[location2.x][location2.y] = tile2;
        		} else if (tiles2[location2.x][location2.y  + 1].type == 5) {
        			tile2 = new Tile(2);
                   	tiles2[location2.x][location2.y] = tile2;
        		} else if (tiles2[location2.x][location2.y + 1].type == 4) {
        			tile2 = new Tile(3);
                   	tiles2[location2.x][location2.y] = tile2;
        		}
        	} else if (tiles2[location2.x + 1][location2.y].type == 4) {
        		//above tile is type 4
        		if(tiles2[location2.x][location2.y +1].type == 4) {
        			tile2 = new Tile(4);
                   	tiles2[location2.x][location2.y] = tile2;
        		}
                if(tiles2[location2.x][location2.y +1].type == 5) {
            		tile2 = new Tile(5);
                    tiles2[location2.x][location2.y] = tile2;
        		}
        	} else if(tiles2[location2.x + 1][location2.y].type == 5) {
        		if(tiles2[location2.x][location2.y +1].type == 3) {
        			tile2 = new Tile(2);
                   	tiles2[location2.x][location2.y] = tile2;
        		} else if (tiles2[location2.x][location2.y +1].type == 1) {
        			tile2 = new Tile(2);
                   	tiles2[location2.x][location2.y] = tile2;
        		} else if (tiles2[location2.x][location2.y + 1].type == 2) {
        			tile2 = new Tile(3);
                   	tiles2[location2.x][location2.y] = tile2;
        		}
        	}
	        drawTile2(gl, tile2, location2);
	        //add possible locations for tiles
	        if(tiles2[location2.x + 1][location2.y - 1] != null) {
	       		tempLoc2 = new Location(location2.x, location2.y - 1);
	           	tileChange2.add(tempLoc2);
	        }
	    	//check if there is a tile back one down one
	    	if(tiles2[location2.x  - 1][location2.y + 1] != null) { 
	    		tempLoc2 = new Location(location2.x - 1, location2.y);
	        	tileChange2.add(tempLoc2);
	    	}
        }
        gl.glFlush ();
		
	}
	private void lowerRight(GL2 gl) {
		//There are no tiles on the board
        if(tiles3[0][grid_size -1] == null)
        {
        	//add location to array list
        	location3 = new Location(0, grid_size -1);
        	tileChange3.add(location3);
        }
        
        //randomly select a location from the array list
        randLoc3 = rand.nextInt(tileChange3.size());
        location3 = tileChange3.get(randLoc3);
        if(location3.x == 0 || location3.y == grid_size -1) {
        		randLoc3 = rand.nextInt(tileChange3.size());
                location3 = tileChange3.get(randLoc3);
        }
        //remove location from array
        tileChange3.remove(randLoc3);
        
        //decide what tile to place at selected location
        if(location3.x == 0 && location3.y == grid_size -1) {
        	tile3.type = 0;
        	tiles3[location3.x][location3.y] = tile3;
        	//draw the tile
        	drawTile3(gl, tile3, location3);
        	//add new locations to the array list
        	location3 = new Location(0, grid_size - 2);
        	tileChange3.add(location3);
        	location3 = new Location(1, grid_size -1);
        	tileChange3.add(location3);
        } else if(location3.x == 0) {
        	//tile is on bottom edge
        	tile3 = new Tile(1);
        	tiles3[location3.x][location3.y] = tile3;
        	//draw the tile
        	drawTile3(gl, tile3, location3);
        	//add new locations to the array
        	if(location3.y - 1 >= grid_size /2){
        		tempLoc3 = new Location(location3.x, location3.y -1);
        		tileChange3.add(tempLoc3);
        	}
        	//check if there is a tile up one right one
        	if(tiles3[location3.x + 1][location3.y + 1] != null){
        		tempLoc3 = new Location(location3.x + 1, location3.y);
            	tileChange3.add(tempLoc3);
        	}
        }else if(location3.y == grid_size -1) {
            //tile is on left edge
           	tile3 = new Tile(1);
           	tiles3[location3.x][location3.y] = tile3;
           	//draw the tile
           	drawTile3(gl, tile3, location3);
           	//add new locations to the array
           	if(location3.x + 1 < grid_size /2) {
           		tempLoc3 = new Location(location3.x + 1, location3.y);
           		tileChange3.add(tempLoc3);
           	}
           	//check if there is a tile left one down one
           	if(tiles3[location3.x - 1][location3.y - 1] != null) {
           		tempLoc3 = new Location(location3.x, location3.y - 1);
               	tileChange3.add(tempLoc3);
           	}
        } else if (location3.x != 0 || location3.y != grid_size -1){
        	//tile is NOT on an an edge
        	//determine what tile to place
        	//check what tile is above you
        	if(tiles3[location3.x - 1][location3.y].type == 1) {
        		//the above tile is of type one
        		//check tile type to right
        		if(tiles3[location3.x][location3.y + 1].type == 1) {
        			//tile should be type 2
        			tile3 = new Tile(2);
                   	tiles3[location3.x][location3.y] = tile3;
                   	drawTile3(gl, tile3, location3);
        		} else if (tiles3[location3.x][location3.y + 1].type == 2) {
        			tile3 = new Tile(3);
                   	tiles3[location3.x][location3.y] = tile3;
        		} else if (tiles3[location3.x][location3.y + 1].type == 3) {
        			tile3 = new Tile(2);
                   	tiles3[location3.x][location3.y] = tile3;
        		}
        	}else if (tiles3[location3.x - 1][location3.y].type == 2) {
        		//above tile is type 2
        		if(tiles3[location3.x][location3.y + 1].type == 1) {
        			//tile should be type 2
        			tile3 = new Tile(5);
                   	tiles3[location3.x][location3.y] = tile3;
        		} else if (tiles3[location3.x][location3.y + 1].type == 2) {
        			tile3 = new Tile(4);
                   	tiles3[location3.x][location3.y] = tile3;
        		} else if (tiles3[location3.x][location3.y + 1].type == 3) {
        			tile3 = new Tile(5);
                   	tiles3[location3.x][location3.y] = tile3;
        		}
        	} else if (tiles3[location3.x - 1][location3.y].type == 3) {
        		//above tile is type 3
        		if(tiles3[location3.x][location3.y + 1].type == 3) {
        			tile3 = new Tile(2);
                   	tiles3[location3.x][location3.y] = tile3;
        		} else if (tiles3[location3.x][location3.y  + 1].type == 5) {
        			tile3 = new Tile(2);
                   	tiles3[location3.x][location3.y] = tile3;
        		} else if (tiles3[location3.x][location3.y + 1].type == 4) {
        			tile3 = new Tile(3);
                   	tiles3[location3.x][location3.y] = tile3;
        		}
        	} else if (tiles3[location3.x - 1][location3.y].type == 4) {
        		//above tile is type 4
        		if(tiles3[location3.x][location3.y +1].type == 4) {
        			tile3 = new Tile(4);
                   	tiles3[location3.x][location3.y] = tile3;
        		}
                if(tiles3[location3.x][location3.y +1].type == 5) {
            		tile3 = new Tile(5);
                    tiles3[location3.x][location3.y] = tile3;
        		}
        	} else if(tiles3[location3.x - 1][location3.y].type == 5) {
        		if(tiles3[location3.x][location3.y +1].type == 3) {
        			tile3 = new Tile(2);
                   	tiles3[location3.x][location3.y] = tile3;
        		} else if (tiles3[location3.x][location3.y +1].type == 1) {
        			tile3 = new Tile(2);
                   	tiles3[location3.x][location3.y] = tile3;
        		} else if (tiles3[location3.x][location3.y + 1].type == 2) {
        			tile3 = new Tile(3);
                   	tiles3[location3.x][location3.y] = tile3;
        		}
        	}
	        drawTile3(gl, tile3, location3);
	        //add possible locations for tiles
	        if(tiles3[location3.x + 1][location3.y + 1] != null) {
	       		tempLoc3 = new Location(location3.x + 1, location3.y);
	           	tileChange3.add(tempLoc3);
	        }
	    	//check if there is a tile back one down one
	    	if(tiles3[location3.x  - 1][location3.y - 1] != null) { 
	    		tempLoc3 = new Location(location3.x, location3.y - 1);
	        	tileChange3.add(tempLoc3);
	    	}
        }
        if(location3.x + 1 == (grid_size /2) -1 && location3.y - 1 == grid_size /2){
        	grid_size = grid_size * 2;
        	reset(gl);
        	
        }
        gl.glFlush ();
	}

	private void upperLeft(GL2 gl) {
	   //There are no tiles on the board
        if(tiles[grid_size -1][0] == null)
        {
        	
        	//add location to array list
        	location = new Location(grid_size -1, 0);
        	tileChange.add(location);
        }
        
        //randomly select a location from the array list
        randLoc = rand.nextInt(tileChange.size());
        location = tileChange.get(randLoc);
        if(location.x == grid_size -1 || location.y == 0) {
        		randLoc = rand.nextInt(tileChange.size());
                location = tileChange.get(randLoc);
        }
        //remove location from array
        tileChange.remove(randLoc);
        
        //decide what tile to place at selected location
        if(location.x == grid_size -1 && location.y == 0) {
        	tile.type = 0;
        	tiles[location.x][location.y] = tile;
        	//draw the tile
        	drawTile(gl, tile, location);
        	
        	//add new locations to the array list
        	location = new Location(grid_size -1, 1);
        	tileChange.add(location);
        	location = new Location(grid_size - 2, 0);
        	tileChange.add(location);
        } else if(location.x == grid_size - 1) {
        	//tile is on top edge
        	tile = new Tile(1);
        	tiles[location.x][location.y] = tile;
        	//draw the tile
        	drawTile(gl, tile, location);
        	//add new locations to the array
        	if(location.y + 1 < grid_size /2){
        		tempLoc = new Location(location.x, location.y + 1);
        		tileChange.add(tempLoc);
        	}
        	//check if there is a tile back one down one
        	if(tiles[location.x - 1][location.y -1] != null){
        		tempLoc = new Location(location.x - 1, location.y);
            	tileChange.add(tempLoc);
        	}
        }else if(location.y == 0) {
            //tile is on left edge
           	tile = new Tile(1);
           	tiles[location.x][location.y] = tile;
           	//draw the tile
           	drawTile(gl, tile, location);
           	//add new locations to the array
           	if(location.x - 1 >= grid_size /2) {
           		tempLoc = new Location(location.x - 1, location.y);
           		tileChange.add(tempLoc);
           	}
           	//check if there is a tile up one right one
           	if(tiles[location.x + 1][location.y + 1] != null) {
           		tempLoc = new Location(location.x, location.y + 1);
               	tileChange.add(tempLoc);
           	}
        } else if (location.x != grid_size && location.y != 0){
        	//tile is NOT on an an edge
        	//determine what tile to place
        	//check what tile is above you
        	if(tiles[location.x + 1][location.y].type == 1) {
        		//the above tile is of type one
        		//check tile type to left
        		if(tiles[location.x][location.y -1].type == 1) {
        			//tile should be type 2
        			tile = new Tile(2);
                   	tiles[location.x][location.y] = tile;
                   	drawTile(gl, tile, location);
        		} else if (tiles[location.x][location.y -1].type == 2) {
        			tile = new Tile(3);
                   	tiles[location.x][location.y] = tile;
        		} else if (tiles[location.x][location.y -1].type == 3) {
        			tile = new Tile(2);
                   	tiles[location.x][location.y] = tile;
        		}
        	} else if (tiles[location.x + 1][location.y].type == 2) {
        		//above tile is type 2
        		if(tiles[location.x][location.y -1].type == 1) {
        			//tile should be type 2
        			tile = new Tile(5);
                   	tiles[location.x][location.y] = tile;
        		} else if (tiles[location.x][location.y -1].type == 2) {
        			tile = new Tile(4);
                   	tiles[location.x][location.y] = tile;
        		} else if (tiles[location.x][location.y -1].type == 3) {
        			tile = new Tile(5);
                   	tiles[location.x][location.y] = tile;
        		}
        	} else if (tiles[location.x + 1][location.y].type == 3) {
        		//above tile is type 3
        		if(tiles[location.x][location.y -1].type == 3) {
        			tile = new Tile(2);
                   	tiles[location.x][location.y] = tile;
        		} else if (tiles[location.x][location.y -1].type == 5) {
        			tile = new Tile(2);
                   	tiles[location.x][location.y] = tile;
        		} else if (tiles[location.x][location.y -1].type == 4) {
        			tile = new Tile(3);
                   	tiles[location.x][location.y] = tile;
        		}
        	} else if (tiles[location.x + 1][location.y].type == 4) {
        		//above tile is type 4
        		if(tiles[location.x][location.y -1].type == 4) {
        			tile = new Tile(4);
                   	tiles[location.x][location.y] = tile;
        		}
                if(tiles[location.x][location.y -1].type == 5) {
            		tile = new Tile(5);
                    tiles[location.x][location.y] = tile;
        		}
        	} else if(tiles[location.x + 1][location.y].type == 5) {
        		if(tiles[location.x][location.y -1].type == 3) {
        			tile = new Tile(2);
                   	tiles[location.x][location.y] = tile;
        		} else if (tiles[location.x][location.y -1].type == 1) {
        			tile = new Tile(2);
                   	tiles[location.x][location.y] = tile;
        		} else if (tiles[location.x][location.y -1].type == 2) {
        			tile = new Tile(3);
                   	tiles[location.x][location.y] = tile;
        		}
        	}
	        drawTile(gl, tile, location);
	        //add possible locations for tiles
	        if(tiles[location.x + 1][location.y + 1] != null) {
	       		tempLoc = new Location(location.x, location.y + 1);
	           	tileChange.add(tempLoc);
	        }
	    	//check if there is a tile back one down one
	    	if(tiles[location.x  - 1][location.y -1] != null) { 
	    		tempLoc = new Location(location.x - 1, location.y);
	        	tileChange.add(tempLoc);
	    	}
        }
        gl.glFlush ();
    }

	public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {
    }
    
    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged,
			       boolean deviceChanged) {
    }

    public void dispose(GLAutoDrawable arg0) {
    }


    public void keyPressed(KeyEvent key)
    {
		switch (key.getKeyCode()) {
		case KeyEvent.VK_ESCAPE:
		    // The animator must be stopped in a different thread to
		    // make sure that it completes before exit is called.
		    new Thread()
		    {
			public void run()
			{
			    animator.stop();
			}
		    }.start();
		    System.exit(0);
		default:
		    break;
		}
    }

    public void keyReleased(KeyEvent key)
    {
    	if(key.getKeyCode() == 66){
    		green = 0;
    		red = 0;
    		blue = 1;
    	}
    	if(key.getKeyCode() == 82){
    		green = 0;
    		red = 1;
    		blue = 0;
    	}if(key.getKeyCode() == 71){
    		green = 1;
    		red = 0;
    		blue = 0;
    	}
    	if(key.getKeyCode() == 32){
    		green = rand.nextFloat();
    		red = rand.nextFloat();
    		blue = rand.nextFloat();
    	}
    }


    public void mousePressed(MouseEvent mouse)
    {
		switch (mouse.getButton()) {
		case MouseEvent.BUTTON1:
 
			if(oneCorner == true){
				oneCorner = false;
				grid_size = 40;
			}else if(oneCorner == false){
				oneCorner = true;
				grid_size = 20;
			}
		    break;
		case MouseEvent.BUTTON2:
		    break;
		case MouseEvent.BUTTON3:
		    new Thread()
		    {
			public void run()
			{
			    animator.stop();
			}
		    }.start();
		    System.exit(0);
		    break;
		}
    }
    
    public void drawTile(GL2 gl, Tile tile, Location location) {
    	
    	
    	int row = location.x;
    	int col = location.y;
    	int type = tile.type;
    	switch (type) {
			case 0: 
				gl.glColor3f(0, 0, 0);
				gl.glBegin(GL2.GL_POLYGON);
				gl.glVertex2f((float)col + .35f, (float)row + .3f);
				gl.glVertex2f((float)col + .2f, (float)row);
				gl.glVertex2f((float)col + .5f, (float)row);
				gl.glEnd();
				
				gl.glColor3f(0, 0, 0);
				gl.glBegin(GL2.GL_TRIANGLES);
				gl.glVertex2f((float)col + .65f, (float)row + .3f);
				gl.glVertex2f((float)col + .8f, (float)row);
				gl.glVertex2f((float)col + .5f, (float)row);
				gl.glEnd();
				
				gl.glColor3f(0, 0, 0);
				gl.glBegin(GL2.GL_TRIANGLES);
				gl.glVertex2f((float)col + .7f, (float)row + .35f);
				gl.glVertex2f((float)col + 1, (float)row + .2f);
				gl.glVertex2f((float)col + 1, (float)row + .5f);
				gl.glEnd();
				
				gl.glColor3f(0, 0, 0);
				gl.glBegin(GL2.GL_TRIANGLES);
				gl.glVertex2f((float)col + .7f, (float)row + .65f);
				gl.glVertex2f((float)col + 1, (float)row + .8f);
				gl.glVertex2f((float)col + 1, (float)row + .5f);
				gl.glEnd();
				break;
			case 1:
				if(col == 0) {
					gl.glColor3f(0, 0, 0);
					gl.glBegin(GL2.GL_TRIANGLES);
					gl.glVertex2f((float)col + .35f, (float)row + .7f);
					gl.glVertex2f((float)col + .2f, (float)row + 1);
					gl.glVertex2f((float)col + .5f, (float)row + 1);
					gl.glEnd();
					
					gl.glColor3f(0, 0, 0);
					gl.glBegin(GL2.GL_TRIANGLES);
					gl.glVertex2f((float)col + .65f, (float)row + .7f);
					gl.glVertex2f((float)col + .8f, (float)row + 1);
					gl.glVertex2f((float)col + .5f, (float)row + 1);
					gl.glEnd();
					
					gl.glColor3f(0, 0, 0);
					gl.glBegin(GL2.GL_TRIANGLES);
					gl.glVertex2f((float)col + .35f, (float)row + .3f);
					gl.glVertex2f((float)col + .2f, (float)row);
					gl.glVertex2f((float)col + .5f, (float)row);
					gl.glEnd();
					
					gl.glColor3f(0, 0, 0);
					gl.glBegin(GL2.GL_TRIANGLES);
					gl.glVertex2f((float)col + .65f, (float)row + .3f);
					gl.glVertex2f((float)col + .8f, (float)row);
					gl.glVertex2f((float)col + .5f, (float)row);
					gl.glEnd();
					
					gl.glColor3f(0, 0, 0);
					gl.glBegin(GL2.GL_TRIANGLES);
					gl.glVertex2f((float)col + .5f, (float)row + .5f);
					gl.glVertex2f((float)col + 1, (float)row + 1);
					gl.glVertex2f((float)col + 1f, (float)row + 0);
					gl.glEnd();
				}else{
					gl.glColor3f(0, 0, 0);
					gl.glBegin(GL2.GL_TRIANGLES);
					gl.glVertex2f((float)col + .3f, (float)row + .35f);
					gl.glVertex2f((float)col, (float)row + .2f);
					gl.glVertex2f((float)col, (float)row + .5f);
					gl.glEnd();
					
					gl.glColor3f(0, 0, 0);
					gl.glBegin(GL2.GL_TRIANGLES);
					gl.glVertex2f((float)col + .3f, (float)row + .65f);
					gl.glVertex2f((float)col, (float)row + .8f);
					gl.glVertex2f((float)col, (float)row + .5f);
					gl.glEnd();
					
					gl.glColor3f(0, 0, 0);
					gl.glBegin(GL2.GL_TRIANGLES);
					gl.glVertex2f((float)col + .7f, (float)row + .35f);
					gl.glVertex2f((float)col + 1, (float)row + .2f);
					gl.glVertex2f((float)col  +1, (float)row + .5f);
					gl.glEnd();
					
					gl.glColor3f(0, 0, 0);
					gl.glBegin(GL2.GL_TRIANGLES);
					gl.glVertex2f((float)col + .7f, (float)row + .65f);
					gl.glVertex2f((float)col + 1, (float)row + .8f);
					gl.glVertex2f((float)col + 1, (float)row + .5f);
					gl.glEnd();
					
					gl.glColor3f(0, 0, 0);
					gl.glBegin(GL2.GL_TRIANGLES);
					gl.glVertex2f((float)col + .5f, (float)row + .5f);
					gl.glVertex2f((float)col + 0, (float)row);
					gl.glVertex2f((float)col + 1, (float)row);
					gl.glEnd();
				}
				break;
			case 2:
				
				gl.glColor3f(0, 0, 0);
				gl.glBegin(GL2.GL_TRIANGLES);
				gl.glVertex2f((float)col + .5f, (float)row + .5f);
				gl.glVertex2f((float)col, (float)row + 0);
				gl.glVertex2f((float)col, (float)row + 1);
				gl.glEnd();
				
				gl.glColor3f(0, 0, 0);
				gl.glBegin(GL2.GL_TRIANGLES);
				gl.glVertex2f((float)col + .5f, (float)row + .5f);
				gl.glVertex2f((float)col + 0, (float)row + 1 );
				gl.glVertex2f((float)col + 1, (float)row + 1);
				gl.glEnd();
				
				gl.glColor3f(red, green, blue);
				gl.glBegin(GL2.GL_TRIANGLES);
				gl.glVertex2f((float)col + .5f, (float)row + .5f);
				gl.glVertex2f((float)col + 0, (float)row);
				gl.glVertex2f((float)col + 1, (float)row);
				gl.glEnd();
				
				gl.glColor3f(red, green, blue);
				gl.glBegin(GL2.GL_TRIANGLES);
				gl.glVertex2f((float)col + .5f, (float)row + .5f);
				gl.glVertex2f((float)col + 1, (float)row + 0 );
				gl.glVertex2f((float)col + 1, (float)row  +1);
				gl.glEnd();
				break;
				
			case 3:
				gl.glColor3f(red, green, blue);
				gl.glBegin(GL2.GL_TRIANGLES);
				gl.glVertex2f((float)col + .5f, (float)row + .5f);
				gl.glVertex2f((float)col, (float)row + 0);
				gl.glVertex2f((float)col, (float)row + 1);
				gl.glEnd();
				
				gl.glColor3f(0, 0, 0);
				gl.glBegin(GL2.GL_TRIANGLES);
				gl.glVertex2f((float)col + .5f, (float)row + .5f);
				gl.glVertex2f((float)col + 0, (float)row + 1 );
				gl.glVertex2f((float)col + 1, (float)row + 1);
				gl.glEnd();
				
				gl.glColor3f(0, 0, 0);
				gl.glBegin(GL2.GL_TRIANGLES);
				gl.glVertex2f((float)col + .5f, (float)row + .5f);
				gl.glVertex2f((float)col + 0, (float)row);
				gl.glVertex2f((float)col + 1, (float)row);
				gl.glEnd();
				
				gl.glColor3f(0, 0, 0);
				gl.glBegin(GL2.GL_TRIANGLES);
				gl.glVertex2f((float)col + .5f, (float)row + .5f);
				gl.glVertex2f((float)col + 1, (float)row + 0 );
				gl.glVertex2f((float)col + 1, (float)row  +1);
				gl.glEnd();
				break;
			case 4:
				gl.glColor3f(red, green, blue);
				gl.glBegin(GL2.GL_TRIANGLES);
				gl.glVertex2f((float)col + .5f, (float)row + .5f);
				gl.glVertex2f((float)col, (float)row + 0);
				gl.glVertex2f((float)col, (float)row + 1);
				gl.glEnd();
				
				gl.glColor3f(red, green, blue);
				gl.glBegin(GL2.GL_TRIANGLES);
				gl.glVertex2f((float)col + .5f, (float)row + .5f);
				gl.glVertex2f((float)col + 0, (float)row + 1 );
				gl.glVertex2f((float)col + 1, (float)row + 1);
				gl.glEnd();
				
				gl.glColor3f(red, green, blue);
				gl.glBegin(GL2.GL_TRIANGLES);
				gl.glVertex2f((float)col + .5f, (float)row + .5f);
				gl.glVertex2f((float)col + 0, (float)row);
				gl.glVertex2f((float)col + 1, (float)row);
				gl.glEnd();
				
				gl.glColor3f(red, green, blue);
				gl.glBegin(GL2.GL_TRIANGLES);
				gl.glVertex2f((float)col + .5f, (float)row + .5f);
				gl.glVertex2f((float)col + 1, (float)row + 0 );
				gl.glVertex2f((float)col + 1, (float)row  +1);
				gl.glEnd();
				break;
			case 5:
				gl.glColor3f(0, 0, 0);
				gl.glBegin(GL2.GL_TRIANGLES);
				gl.glVertex2f((float)col + .5f, (float)row + .5f);
				gl.glVertex2f((float)col, (float)row + 0);
				gl.glVertex2f((float)col, (float)row + 1);
				gl.glEnd();
				
				gl.glColor3f(red, green, blue);
				gl.glBegin(GL2.GL_TRIANGLES);
				gl.glVertex2f((float)col + .5f, (float)row + .5f);
				gl.glVertex2f((float)col + 0, (float)row + 1 );
				gl.glVertex2f((float)col + 1, (float)row + 1);
				gl.glEnd();
				
				gl.glColor3f(0, 0, 0);
				gl.glBegin(GL2.GL_TRIANGLES);
				gl.glVertex2f((float)col + .5f, (float)row + .5f);
				gl.glVertex2f((float)col + 0, (float)row);
				gl.glVertex2f((float)col + 1, (float)row);
				gl.glEnd();
				
				gl.glColor3f(0, 0, 0);
				gl.glBegin(GL2.GL_TRIANGLES);
				gl.glVertex2f((float)col + .5f, (float)row + .5f);
				gl.glVertex2f((float)col + 1, (float)row + 0 );
				gl.glVertex2f((float)col + 1, (float)row  +1);
				gl.glEnd();
				break;
    	}
    }
    
 public void drawTile2(GL2 gl, Tile tile, Location location) {
    	
    	
    	int row = location.x;
    	int col = location.y;
    	int type = tile.type;
    	switch (type) {
			case 0: 
				gl.glColor3f(0, 0, 0);
				gl.glBegin(GL2.GL_POLYGON);
				gl.glVertex2f((float)col + .35f, (float)row + .3f);
				gl.glVertex2f((float)col + .2f, (float)row);
				gl.glVertex2f((float)col + .5f, (float)row);
				gl.glEnd();
				
				gl.glColor3f(0, 0, 0);
				gl.glBegin(GL2.GL_TRIANGLES);
				gl.glVertex2f((float)col + .65f, (float)row + .3f);
				gl.glVertex2f((float)col + .8f, (float)row);
				gl.glVertex2f((float)col + .5f, (float)row);
				gl.glEnd();
				
				gl.glColor3f(0, 0, 0);
				gl.glBegin(GL2.GL_TRIANGLES);
				gl.glVertex2f((float)col + .3f, (float)row + .35f);
				gl.glVertex2f((float)col, (float)row + .2f);
				gl.glVertex2f((float)col, (float)row + .5f);
				gl.glEnd();
				
				gl.glColor3f(0, 0, 0);
				gl.glBegin(GL2.GL_TRIANGLES);
				gl.glVertex2f((float)col + .3f, (float)row + .65f);
				gl.glVertex2f((float)col, (float)row + .8f);
				gl.glVertex2f((float)col, (float)row + .5f);
				gl.glEnd();
				break;
			case 1:
				if(col == grid_size -1) {
					gl.glColor3f(0, 0, 0);
					gl.glBegin(GL2.GL_TRIANGLES);
					gl.glVertex2f((float)col + .35f, (float)row + .7f);
					gl.glVertex2f((float)col + .2f, (float)row + 1);
					gl.glVertex2f((float)col + .5f, (float)row + 1);
					gl.glEnd();
					
					gl.glColor3f(0, 0, 0);
					gl.glBegin(GL2.GL_TRIANGLES);
					gl.glVertex2f((float)col + .65f, (float)row + .7f);
					gl.glVertex2f((float)col + .8f, (float)row + 1);
					gl.glVertex2f((float)col + .5f, (float)row + 1);
					gl.glEnd();
					
					gl.glColor3f(0, 0, 0);
					gl.glBegin(GL2.GL_TRIANGLES);
					gl.glVertex2f((float)col + .35f, (float)row + .3f);
					gl.glVertex2f((float)col + .2f, (float)row);
					gl.glVertex2f((float)col + .5f, (float)row);
					gl.glEnd();
					
					gl.glColor3f(0, 0, 0);
					gl.glBegin(GL2.GL_TRIANGLES);
					gl.glVertex2f((float)col + .65f, (float)row + .3f);
					gl.glVertex2f((float)col + .8f, (float)row);
					gl.glVertex2f((float)col + .5f, (float)row);
					gl.glEnd();
					
					gl.glColor3f(0, 0, 0);
					gl.glBegin(GL2.GL_TRIANGLES);
					gl.glVertex2f((float)col + .5f, (float)row + .5f);
					gl.glVertex2f((float)col, (float)row + 1);
					gl.glVertex2f((float)col, (float)row + 0);
					gl.glEnd();
				}else{
					gl.glColor3f(0, 0, 0);
					gl.glBegin(GL2.GL_TRIANGLES);
					gl.glVertex2f((float)col + .3f, (float)row + .35f);
					gl.glVertex2f((float)col, (float)row + .2f);
					gl.glVertex2f((float)col, (float)row + .5f);
					gl.glEnd();
					
					gl.glColor3f(0, 0, 0);
					gl.glBegin(GL2.GL_TRIANGLES);
					gl.glVertex2f((float)col + .3f, (float)row + .65f);
					gl.glVertex2f((float)col, (float)row + .8f);
					gl.glVertex2f((float)col, (float)row + .5f);
					gl.glEnd();
					
					gl.glColor3f(0, 0, 0);
					gl.glBegin(GL2.GL_TRIANGLES);
					gl.glVertex2f((float)col + .7f, (float)row + .35f);
					gl.glVertex2f((float)col + 1, (float)row + .2f);
					gl.glVertex2f((float)col  +1, (float)row + .5f);
					gl.glEnd();
					
					gl.glColor3f(0, 0, 0);
					gl.glBegin(GL2.GL_TRIANGLES);
					gl.glVertex2f((float)col + .7f, (float)row + .65f);
					gl.glVertex2f((float)col + 1, (float)row + .8f);
					gl.glVertex2f((float)col + 1, (float)row + .5f);
					gl.glEnd();
					
					gl.glColor3f(0, 0, 0);
					gl.glBegin(GL2.GL_TRIANGLES);
					gl.glVertex2f((float)col + .5f, (float)row + .5f);
					gl.glVertex2f((float)col + 0, (float)row);
					gl.glVertex2f((float)col + 1, (float)row);
					gl.glEnd();
				}
				break;
			case 2:
				
				gl.glColor3f(0, 0, 0);
				gl.glBegin(GL2.GL_TRIANGLES);
				gl.glVertex2f((float)col + .5f, (float)row + .5f);
				gl.glVertex2f((float)col + 1, (float)row + 0);
				gl.glVertex2f((float)col + 1, (float)row + 1);
				gl.glEnd();
				
				gl.glColor3f(red, green, blue);
				gl.glBegin(GL2.GL_TRIANGLES);
				gl.glVertex2f((float)col + .5f, (float)row + .5f);
				gl.glVertex2f((float)col, (float)row + 0);
				gl.glVertex2f((float)col, (float)row + 1);
				gl.glEnd();
				
				gl.glColor3f(0, 0, 0);
				gl.glBegin(GL2.GL_TRIANGLES);
				gl.glVertex2f((float)col + .5f, (float)row + .5f);
				gl.glVertex2f((float)col + 0, (float)row + 1);
				gl.glVertex2f((float)col + 1, (float)row + 1);
				gl.glEnd();
				
				gl.glColor3f(red, green, blue);
				gl.glBegin(GL2.GL_TRIANGLES);
				gl.glVertex2f((float)col + .5f, (float)row + .5f);
				gl.glVertex2f((float)col + 1, (float)row );
				gl.glVertex2f((float)col, (float)row);
				gl.glEnd();
				break;
				
			case 3:
				gl.glColor3f(0, 0, 0);
				gl.glBegin(GL2.GL_TRIANGLES);
				gl.glVertex2f((float)col + .5f, (float)row + .5f);
				gl.glVertex2f((float)col, (float)row + 0);
				gl.glVertex2f((float)col, (float)row + 1);
				gl.glEnd();
				
				gl.glColor3f(0, 0, 0);
				gl.glBegin(GL2.GL_TRIANGLES);
				gl.glVertex2f((float)col + .5f, (float)row + .5f);
				gl.glVertex2f((float)col + 0, (float)row + 1 );
				gl.glVertex2f((float)col + 1, (float)row + 1);
				gl.glEnd();
				
				gl.glColor3f(0, 0, 0);
				gl.glBegin(GL2.GL_TRIANGLES);
				gl.glVertex2f((float)col + .5f, (float)row + .5f);
				gl.glVertex2f((float)col + 0, (float)row);
				gl.glVertex2f((float)col + 1, (float)row);
				gl.glEnd();
				
				gl.glColor3f(red, green, blue);
				gl.glBegin(GL2.GL_TRIANGLES);
				gl.glVertex2f((float)col + .5f, (float)row + .5f);
				gl.glVertex2f((float)col + 1, (float)row + 0 );
				gl.glVertex2f((float)col + 1, (float)row  +1);
				gl.glEnd();
				break;
			case 4:
				gl.glColor3f(red, green, blue);
				gl.glBegin(GL2.GL_TRIANGLES);
				gl.glVertex2f((float)col + .5f, (float)row + .5f);
				gl.glVertex2f((float)col, (float)row + 0);
				gl.glVertex2f((float)col, (float)row + 1);
				gl.glEnd();
				
				gl.glColor3f(red, green, blue);
				gl.glBegin(GL2.GL_TRIANGLES);
				gl.glVertex2f((float)col + .5f, (float)row + .5f);
				gl.glVertex2f((float)col + 0, (float)row + 1 );
				gl.glVertex2f((float)col + 1, (float)row + 1);
				gl.glEnd();
				
				gl.glColor3f(red, green, blue);
				gl.glBegin(GL2.GL_TRIANGLES);
				gl.glVertex2f((float)col + .5f, (float)row + .5f);
				gl.glVertex2f((float)col + 0, (float)row);
				gl.glVertex2f((float)col + 1, (float)row);
				gl.glEnd();
				
				gl.glColor3f(red, green, blue);
				gl.glBegin(GL2.GL_TRIANGLES);
				gl.glVertex2f((float)col + .5f, (float)row + .5f);
				gl.glVertex2f((float)col + 1, (float)row + 0 );
				gl.glVertex2f((float)col + 1, (float)row  +1);
				gl.glEnd();
				break;
			case 5:
				gl.glColor3f(0, 0, 0);
				gl.glBegin(GL2.GL_TRIANGLES);
				gl.glVertex2f((float)col + .5f, (float)row + .5f);
				gl.glVertex2f((float)col, (float)row + 0);
				gl.glVertex2f((float)col, (float)row + 1);
				gl.glEnd();
				
				gl.glColor3f(red, green, blue);
				gl.glBegin(GL2.GL_TRIANGLES);
				gl.glVertex2f((float)col + .5f, (float)row + .5f);
				gl.glVertex2f((float)col + 0, (float)row + 1 );
				gl.glVertex2f((float)col + 1, (float)row + 1);
				gl.glEnd();
				
				gl.glColor3f(0, 0, 0);
				gl.glBegin(GL2.GL_TRIANGLES);
				gl.glVertex2f((float)col + .5f, (float)row + .5f);
				gl.glVertex2f((float)col + 0, (float)row);
				gl.glVertex2f((float)col + 1, (float)row);
				gl.glEnd();
				
				gl.glColor3f(0, 0, 0);
				gl.glBegin(GL2.GL_TRIANGLES);
				gl.glVertex2f((float)col + .5f, (float)row + .5f);
				gl.glVertex2f((float)col + 1, (float)row + 0 );
				gl.glVertex2f((float)col + 1, (float)row  +1);
				gl.glEnd();
				break;
    	}
    }
 
 public void drawTile3(GL2 gl, Tile tile, Location location) {
 	
 	
 	int row = location.x;
 	int col = location.y;
 	int type = tile.type;
 	switch (type) {
			case 0: 
				gl.glColor3f(0, 0, 0);
				gl.glBegin(GL2.GL_TRIANGLES);
				gl.glVertex2f((float)col + .35f, (float)row + .7f);
				gl.glVertex2f((float)col + .2f, (float)row + 1);
				gl.glVertex2f((float)col + .5f, (float)row + 1);
				gl.glEnd();
				
				gl.glColor3f(0, 0, 0);
				gl.glBegin(GL2.GL_TRIANGLES);
				gl.glVertex2f((float)col + .65f, (float)row + .7f);
				gl.glVertex2f((float)col + .8f, (float)row + 1);
				gl.glVertex2f((float)col + .5f, (float)row + 1);
				gl.glEnd();
				
				gl.glColor3f(0, 0, 0);
				gl.glBegin(GL2.GL_TRIANGLES);
				gl.glVertex2f((float)col + .3f, (float)row + .35f);
				gl.glVertex2f((float)col, (float)row + .2f);
				gl.glVertex2f((float)col, (float)row + .5f);
				gl.glEnd();
				
				gl.glColor3f(0, 0, 0);
				gl.glBegin(GL2.GL_TRIANGLES);
				gl.glVertex2f((float)col + .3f, (float)row + .65f);
				gl.glVertex2f((float)col, (float)row + .8f);
				gl.glVertex2f((float)col, (float)row + .5f);
				gl.glEnd();
				break;
			case 1:
				if(col == grid_size -1) {
					gl.glColor3f(0, 0, 0);
					gl.glBegin(GL2.GL_TRIANGLES);
					gl.glVertex2f((float)col + .35f, (float)row + .7f);
					gl.glVertex2f((float)col + .2f, (float)row + 1);
					gl.glVertex2f((float)col + .5f, (float)row + 1);
					gl.glEnd();
					
					gl.glColor3f(0, 0, 0);
					gl.glBegin(GL2.GL_TRIANGLES);
					gl.glVertex2f((float)col + .65f, (float)row + .7f);
					gl.glVertex2f((float)col + .8f, (float)row + 1);
					gl.glVertex2f((float)col + .5f, (float)row + 1);
					gl.glEnd();
					
					gl.glColor3f(0, 0, 0);
					gl.glBegin(GL2.GL_TRIANGLES);
					gl.glVertex2f((float)col + .35f, (float)row + .3f);
					gl.glVertex2f((float)col + .2f, (float)row);
					gl.glVertex2f((float)col + .5f, (float)row);
					gl.glEnd();
					
					gl.glColor3f(0, 0, 0);
					gl.glBegin(GL2.GL_TRIANGLES);
					gl.glVertex2f((float)col + .65f, (float)row + .3f);
					gl.glVertex2f((float)col + .8f, (float)row);
					gl.glVertex2f((float)col + .5f, (float)row);
					gl.glEnd();
					
					gl.glColor3f(0, 0, 0);
					gl.glBegin(GL2.GL_TRIANGLES);
					gl.glVertex2f((float)col + .5f, (float)row + .5f);
					gl.glVertex2f((float)col, (float)row + 1);
					gl.glVertex2f((float)col, (float)row + 0);
					gl.glEnd();
				}else{
					gl.glColor3f(0, 0, 0);
					gl.glBegin(GL2.GL_TRIANGLES);
					gl.glVertex2f((float)col + .3f, (float)row + .35f);
					gl.glVertex2f((float)col, (float)row + .2f);
					gl.glVertex2f((float)col, (float)row + .5f);
					gl.glEnd();
					
					gl.glColor3f(0, 0, 0);
					gl.glBegin(GL2.GL_TRIANGLES);
					gl.glVertex2f((float)col + .3f, (float)row + .65f);
					gl.glVertex2f((float)col, (float)row + .8f);
					gl.glVertex2f((float)col, (float)row + .5f);
					gl.glEnd();
					
					gl.glColor3f(0, 0, 0);
					gl.glBegin(GL2.GL_TRIANGLES);
					gl.glVertex2f((float)col + .7f, (float)row + .35f);
					gl.glVertex2f((float)col + 1, (float)row + .2f);
					gl.glVertex2f((float)col  +1, (float)row + .5f);
					gl.glEnd();
					
					gl.glColor3f(0, 0, 0);
					gl.glBegin(GL2.GL_TRIANGLES);
					gl.glVertex2f((float)col + .7f, (float)row + .65f);
					gl.glVertex2f((float)col + 1, (float)row + .8f);
					gl.glVertex2f((float)col + 1, (float)row + .5f);
					gl.glEnd();
					
					gl.glColor3f(0, 0, 0);
					gl.glBegin(GL2.GL_TRIANGLES);
					gl.glVertex2f((float)col + .5f, (float)row + .5f);
					gl.glVertex2f((float)col + 0, (float)row + 1);
					gl.glVertex2f((float)col + 1, (float)row + 1);
					gl.glEnd();
				}
				break;
			case 2:
				
				gl.glColor3f(0, 0, 0);
				gl.glBegin(GL2.GL_TRIANGLES);
				gl.glVertex2f((float)col + .5f, (float)row + .5f);
				gl.glVertex2f((float)col + 1, (float)row + 0);
				gl.glVertex2f((float)col + 1, (float)row + 1);
				gl.glEnd();
				
				gl.glColor3f(red, green, blue);
				gl.glBegin(GL2.GL_TRIANGLES);
				gl.glVertex2f((float)col + .5f, (float)row + .5f);
				gl.glVertex2f((float)col, (float)row + 0);
				gl.glVertex2f((float)col, (float)row + 1);
				gl.glEnd();
				
				gl.glColor3f(red, green, blue);
				gl.glBegin(GL2.GL_TRIANGLES);
				gl.glVertex2f((float)col + .5f, (float)row + .5f);
				gl.glVertex2f((float)col + 0, (float)row + 1);
				gl.glVertex2f((float)col + 1, (float)row + 1);
				gl.glEnd();
				
				gl.glColor3f(0, 0, 0);
				gl.glBegin(GL2.GL_TRIANGLES);
				gl.glVertex2f((float)col + .5f, (float)row + .5f);
				gl.glVertex2f((float)col + 1, (float)row );
				gl.glVertex2f((float)col, (float)row);
				gl.glEnd();
				break;
				
			case 3:
				gl.glColor3f(0, 0, 0);
				gl.glBegin(GL2.GL_TRIANGLES);
				gl.glVertex2f((float)col + .5f, (float)row + .5f);
				gl.glVertex2f((float)col, (float)row + 0);
				gl.glVertex2f((float)col, (float)row + 1);
				gl.glEnd();
				
				gl.glColor3f(0, 0, 0);
				gl.glBegin(GL2.GL_TRIANGLES);
				gl.glVertex2f((float)col + .5f, (float)row + .5f);
				gl.glVertex2f((float)col + 0, (float)row + 1 );
				gl.glVertex2f((float)col + 1, (float)row + 1);
				gl.glEnd();
				
				gl.glColor3f(0, 0, 0);
				gl.glBegin(GL2.GL_TRIANGLES);
				gl.glVertex2f((float)col + .5f, (float)row + .5f);
				gl.glVertex2f((float)col + 0, (float)row);
				gl.glVertex2f((float)col + 1, (float)row);
				gl.glEnd();
				
				gl.glColor3f(red, green, blue);
				gl.glBegin(GL2.GL_TRIANGLES);
				gl.glVertex2f((float)col + .5f, (float)row + .5f);
				gl.glVertex2f((float)col + 1, (float)row + 0 );
				gl.glVertex2f((float)col + 1, (float)row  +1);
				gl.glEnd();
				break;
			case 4:
				gl.glColor3f(red, green, blue);
				gl.glBegin(GL2.GL_TRIANGLES);
				gl.glVertex2f((float)col + .5f, (float)row + .5f);
				gl.glVertex2f((float)col, (float)row + 0);
				gl.glVertex2f((float)col, (float)row + 1);
				gl.glEnd();
				
				gl.glColor3f(red, green, blue);
				gl.glBegin(GL2.GL_TRIANGLES);
				gl.glVertex2f((float)col + .5f, (float)row + .5f);
				gl.glVertex2f((float)col + 0, (float)row + 1 );
				gl.glVertex2f((float)col + 1, (float)row + 1);
				gl.glEnd();
				
				gl.glColor3f(red, green, blue);
				gl.glBegin(GL2.GL_TRIANGLES);
				gl.glVertex2f((float)col + .5f, (float)row + .5f);
				gl.glVertex2f((float)col + 0, (float)row);
				gl.glVertex2f((float)col + 1, (float)row);
				gl.glEnd();
				
				gl.glColor3f(red, green, blue);
				gl.glBegin(GL2.GL_TRIANGLES);
				gl.glVertex2f((float)col + .5f, (float)row + .5f);
				gl.glVertex2f((float)col + 1, (float)row + 0 );
				gl.glVertex2f((float)col + 1, (float)row  +1);
				gl.glEnd();
				break;
			case 5:
				gl.glColor3f(0, 0, 0);
				gl.glBegin(GL2.GL_TRIANGLES);
				gl.glVertex2f((float)col + .5f, (float)row + .5f);
				gl.glVertex2f((float)col, (float)row + 0);
				gl.glVertex2f((float)col, (float)row + 1);
				gl.glEnd();
				
				gl.glColor3f(0, 0, 0);
				gl.glBegin(GL2.GL_TRIANGLES);
				gl.glVertex2f((float)col + .5f, (float)row + .5f);
				gl.glVertex2f((float)col + 0, (float)row + 1 );
				gl.glVertex2f((float)col + 1, (float)row + 1);
				gl.glEnd();
				
				gl.glColor3f(red, green, blue);
				gl.glBegin(GL2.GL_TRIANGLES);
				gl.glVertex2f((float)col + .5f, (float)row + .5f);
				gl.glVertex2f((float)col + 0, (float)row);
				gl.glVertex2f((float)col + 1, (float)row);
				gl.glEnd();
				
				gl.glColor3f(0, 0, 0);
				gl.glBegin(GL2.GL_TRIANGLES);
				gl.glVertex2f((float)col + .5f, (float)row + .5f);
				gl.glVertex2f((float)col + 1, (float)row + 0 );
				gl.glVertex2f((float)col + 1, (float)row  +1);
				gl.glEnd();
				break;
 	}
 }
 
 public void drawTile4(GL2 gl, Tile tile, Location location) {
	 	
	 	
	 	int row = location.x;
	 	int col = location.y;
	 	int type = tile.type;
	 	switch (type) {
				case 0: 
					gl.glColor3f(0, 0, 0);
					gl.glBegin(GL2.GL_TRIANGLES);
					gl.glVertex2f((float)col + .35f, (float)row + .7f);
					gl.glVertex2f((float)col + .2f, (float)row + 1);
					gl.glVertex2f((float)col + .5f, (float)row + 1);
					gl.glEnd();
					
					gl.glColor3f(0, 0, 0);
					gl.glBegin(GL2.GL_TRIANGLES);
					gl.glVertex2f((float)col + .65f, (float)row + .7f);
					gl.glVertex2f((float)col + .8f, (float)row + 1);
					gl.glVertex2f((float)col + .5f, (float)row + 1);
					gl.glEnd();
					
					gl.glColor3f(0, 0, 0);
					gl.glBegin(GL2.GL_TRIANGLES);
					gl.glVertex2f((float)col + .7f, (float)row + .35f);
					gl.glVertex2f((float)col + 1, (float)row + .2f);
					gl.glVertex2f((float)col + 1, (float)row + .5f);
					gl.glEnd();
					
					gl.glColor3f(0, 0, 0);
					gl.glBegin(GL2.GL_TRIANGLES);
					gl.glVertex2f((float)col + .7f, (float)row + .65f);
					gl.glVertex2f((float)col + 1, (float)row + .8f);
					gl.glVertex2f((float)col + 1, (float)row + .5f);
					gl.glEnd();
					break;
				case 1:
					if(col == 0) {
						gl.glColor3f(0, 0, 0);
						gl.glBegin(GL2.GL_TRIANGLES);
						gl.glVertex2f((float)col + .35f, (float)row + .7f);
						gl.glVertex2f((float)col + .2f, (float)row + 1);
						gl.glVertex2f((float)col + .5f, (float)row + 1);
						gl.glEnd();
						
						gl.glColor3f(0, 0, 0);
						gl.glBegin(GL2.GL_TRIANGLES);
						gl.glVertex2f((float)col + .65f, (float)row + .7f);
						gl.glVertex2f((float)col + .8f, (float)row + 1);
						gl.glVertex2f((float)col + .5f, (float)row + 1);
						gl.glEnd();
						
						gl.glColor3f(0, 0, 0);
						gl.glBegin(GL2.GL_TRIANGLES);
						gl.glVertex2f((float)col + .35f, (float)row + .3f);
						gl.glVertex2f((float)col + .2f, (float)row);
						gl.glVertex2f((float)col + .5f, (float)row);
						gl.glEnd();
						
						gl.glColor3f(0, 0, 0);
						gl.glBegin(GL2.GL_TRIANGLES);
						gl.glVertex2f((float)col + .65f, (float)row + .3f);
						gl.glVertex2f((float)col + .8f, (float)row);
						gl.glVertex2f((float)col + .5f, (float)row);
						gl.glEnd();
						
						gl.glColor3f(0, 0, 0);
						gl.glBegin(GL2.GL_TRIANGLES);
						gl.glVertex2f((float)col + .5f, (float)row + .5f);
						gl.glVertex2f((float)col + 1, (float)row + 1);
						gl.glVertex2f((float)col + 1, (float)row + 0);
						gl.glEnd();
					}else{
						gl.glColor3f(0, 0, 0);
						gl.glBegin(GL2.GL_TRIANGLES);
						gl.glVertex2f((float)col + .3f, (float)row + .35f);
						gl.glVertex2f((float)col, (float)row + .2f);
						gl.glVertex2f((float)col, (float)row + .5f);
						gl.glEnd();
						
						gl.glColor3f(0, 0, 0);
						gl.glBegin(GL2.GL_TRIANGLES);
						gl.glVertex2f((float)col + .3f, (float)row + .65f);
						gl.glVertex2f((float)col, (float)row + .8f);
						gl.glVertex2f((float)col, (float)row + .5f);
						gl.glEnd();
						
						gl.glColor3f(0, 0, 0);
						gl.glBegin(GL2.GL_TRIANGLES);
						gl.glVertex2f((float)col + .7f, (float)row + .35f);
						gl.glVertex2f((float)col + 1, (float)row + .2f);
						gl.glVertex2f((float)col  +1, (float)row + .5f);
						gl.glEnd();
						
						gl.glColor3f(0, 0, 0);
						gl.glBegin(GL2.GL_TRIANGLES);
						gl.glVertex2f((float)col + .7f, (float)row + .65f);
						gl.glVertex2f((float)col + 1, (float)row + .8f);
						gl.glVertex2f((float)col + 1, (float)row + .5f);
						gl.glEnd();
						
						gl.glColor3f(0, 0, 0);
						gl.glBegin(GL2.GL_TRIANGLES);
						gl.glVertex2f((float)col + .5f, (float)row + .5f);
						gl.glVertex2f((float)col + 0, (float)row + 1);
						gl.glVertex2f((float)col + 1, (float)row + 1);
						gl.glEnd();
					}
					break;
				case 2:
					
					gl.glColor3f(red, green, blue);
					gl.glBegin(GL2.GL_TRIANGLES);
					gl.glVertex2f((float)col + .5f, (float)row + .5f);
					gl.glVertex2f((float)col + 1, (float)row + 0);
					gl.glVertex2f((float)col + 1, (float)row + 1);
					gl.glEnd();
					
					gl.glColor3f(0, 0, 0);
					gl.glBegin(GL2.GL_TRIANGLES);
					gl.glVertex2f((float)col + .5f, (float)row + .5f);
					gl.glVertex2f((float)col, (float)row + 0);
					gl.glVertex2f((float)col, (float)row + 1);
					gl.glEnd();
					
					gl.glColor3f(red, green, blue);
					gl.glBegin(GL2.GL_TRIANGLES);
					gl.glVertex2f((float)col + .5f, (float)row + .5f);
					gl.glVertex2f((float)col + 0, (float)row + 1);
					gl.glVertex2f((float)col + 1, (float)row + 1);
					gl.glEnd();
					
					gl.glColor3f(0, 0, 0);
					gl.glBegin(GL2.GL_TRIANGLES);
					gl.glVertex2f((float)col + .5f, (float)row + .5f);
					gl.glVertex2f((float)col + 1, (float)row );
					gl.glVertex2f((float)col, (float)row);
					gl.glEnd();
					break;
					
				case 3:
					gl.glColor3f(red, green, blue);
					gl.glBegin(GL2.GL_TRIANGLES);
					gl.glVertex2f((float)col + .5f, (float)row + .5f);
					gl.glVertex2f((float)col, (float)row + 0);
					gl.glVertex2f((float)col, (float)row + 1);
					gl.glEnd();
					
					gl.glColor3f(0, 0, 0);
					gl.glBegin(GL2.GL_TRIANGLES);
					gl.glVertex2f((float)col + .5f, (float)row + .5f);
					gl.glVertex2f((float)col + 0, (float)row + 1 );
					gl.glVertex2f((float)col + 1, (float)row + 1);
					gl.glEnd();
					
					gl.glColor3f(0, 0, 0);
					gl.glBegin(GL2.GL_TRIANGLES);
					gl.glVertex2f((float)col + .5f, (float)row + .5f);
					gl.glVertex2f((float)col + 0, (float)row);
					gl.glVertex2f((float)col + 1, (float)row);
					gl.glEnd();
					
					gl.glColor3f(0, 0, 0);
					gl.glBegin(GL2.GL_TRIANGLES);
					gl.glVertex2f((float)col + .5f, (float)row + .5f);
					gl.glVertex2f((float)col + 1, (float)row + 0 );
					gl.glVertex2f((float)col + 1, (float)row  +1);
					gl.glEnd();
					break;
				case 4:
					gl.glColor3f(red, green, blue);
					gl.glBegin(GL2.GL_TRIANGLES);
					gl.glVertex2f((float)col + .5f, (float)row + .5f);
					gl.glVertex2f((float)col, (float)row + 0);
					gl.glVertex2f((float)col, (float)row + 1);
					gl.glEnd();
					
					gl.glColor3f(red, green, blue);
					gl.glBegin(GL2.GL_TRIANGLES);
					gl.glVertex2f((float)col + .5f, (float)row + .5f);
					gl.glVertex2f((float)col + 0, (float)row + 1 );
					gl.glVertex2f((float)col + 1, (float)row + 1);
					gl.glEnd();
					
					gl.glColor3f(red, green, blue);
					gl.glBegin(GL2.GL_TRIANGLES);
					gl.glVertex2f((float)col + .5f, (float)row + .5f);
					gl.glVertex2f((float)col + 0, (float)row);
					gl.glVertex2f((float)col + 1, (float)row);
					gl.glEnd();
					
					gl.glColor3f(red, green, blue);
					gl.glBegin(GL2.GL_TRIANGLES);
					gl.glVertex2f((float)col + .5f, (float)row + .5f);
					gl.glVertex2f((float)col + 1, (float)row + 0 );
					gl.glVertex2f((float)col + 1, (float)row  +1);
					gl.glEnd();
					break;
				case 5:
					gl.glColor3f(0, 0, 0);
					gl.glBegin(GL2.GL_TRIANGLES);
					gl.glVertex2f((float)col + .5f, (float)row + .5f);
					gl.glVertex2f((float)col, (float)row + 0);
					gl.glVertex2f((float)col, (float)row + 1);
					gl.glEnd();
					
					gl.glColor3f(0, 0, 0);
					gl.glBegin(GL2.GL_TRIANGLES);
					gl.glVertex2f((float)col + .5f, (float)row + .5f);
					gl.glVertex2f((float)col + 0, (float)row + 1 );
					gl.glVertex2f((float)col + 1, (float)row + 1);
					gl.glEnd();
					
					gl.glColor3f(red, green, blue);
					gl.glBegin(GL2.GL_TRIANGLES);
					gl.glVertex2f((float)col + .5f, (float)row + .5f);
					gl.glVertex2f((float)col + 0, (float)row);
					gl.glVertex2f((float)col + 1, (float)row);
					gl.glEnd();
					
					gl.glColor3f(0, 0, 0);
					gl.glBegin(GL2.GL_TRIANGLES);
					gl.glVertex2f((float)col + .5f, (float)row + .5f);
					gl.glVertex2f((float)col + 1, (float)row + 0 );
					gl.glVertex2f((float)col + 1, (float)row  +1);
					gl.glEnd();
					break;
	 	}
 }
 
    public void reset(GL2 gl) {
    	gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluOrtho2D(0.0f, (float)grid_size, 0.0f, (float)grid_size);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        for(int i = 0; i < 400; i++){
        	for(int j = 0; j < 400; j++) {
        		tiles[i][j] = null;
        		tiles2[i][j] = null;
        		tiles3[i][j] = null;
        		tiles4[i][j] = null;
        		
        	}
        }
        tileChange.clear();
        tileChange3.clear();
        tileChange2.clear();
        tileChange4.clear();
        displaySize = grid_size;
        gl.glFlush ();
    }
    public void resize(GL2 gl)
    {
    	grid_size = grid_size * 2;
    	gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluOrtho2D(0.0f, (float)grid_size, 0.0f, (float)grid_size);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        
        int tempSizeX = grid_size; //new grid size
        int tempSizeY = 0;		 
        for (int i = displaySize - 1; i >= 0; i--) { //old grid size to 0
        	tempSizeX = tempSizeX -1;				//new grid size -1
        	tempSizeY = 0;							//reset y to 0
        	for (int j = 0; j <= displaySize; j++) {  //0 to old grid size
        		if(tiles[i][j] != null){			
        			tempLoc = new Location(tempSizeX , tempSizeY);  //
        			drawTile(gl, tiles[i][j], tempLoc);
        			tempTiles[tempLoc.x][tempLoc.y] = tiles[i][j];
        		}
        		tempSizeY = tempSizeY + 1;
        	}
        }
        
        for(Location l : tileChange){
        	l.x = l.x + displaySize;
        }
      
        for(int i = 0; i < 400; i++){
        	for(int j = 0; j < 400; j++) {
        		tiles[i][j] = tempTiles[i][j];
        		tempTiles[i][j] = null;
        	}
        	
        }
        displaySize = grid_size;
        gl.glFlush ();
    }

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}

class Tile {
    
    int type;

    Tile(int type) {

    	this.type = type;
    }
}

class Location {
	
	int x; 
	int y;
	
	Location(int x, int y) {
		this.x = x;
		this.y = y;
	}
}