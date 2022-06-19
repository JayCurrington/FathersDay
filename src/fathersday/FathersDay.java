package fathersday;


import java.io.*; 
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import javax.swing.*;

public class FathersDay extends JFrame implements Runnable {

    boolean animateFirstTime = true;
    Image image;
    Graphics2D g;
    Dad dad = new Dad();
    Jay jay = new Jay();
    Hug hug = new Hug();
    boolean drawHug=false;
    
    
    
    
    static FathersDay frame;
    public static void main(String[] args) {
        frame = new FathersDay();
        frame.setSize(Window.WINDOW_WIDTH, Window.WINDOW_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public FathersDay() {
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.BUTTON1 == e.getButton()) {
                    //left button

                }
                if (e.BUTTON3 == e.getButton()) {
                    //right button
                    reset();
                }
                repaint();
            }
        });

    addMouseMotionListener(new MouseMotionAdapter() {
      public void mouseDragged(MouseEvent e) {
       // repaint();
      }
    });

    addMouseMotionListener(new MouseMotionAdapter() {
      public void mouseMoved(MouseEvent e) {
          
       // repaint();
      }
    });

        addKeyListener(new KeyAdapter() {
// Character walk directions
            @Override
            public void keyPressed(KeyEvent e) {
                
                if (e.VK_LEFT == e.getKeyCode()) {
                    dad.walk=3;
                    System.out.println("left");
                } else if (e.VK_RIGHT == e.getKeyCode()) {
                    dad.walk=4;
                    System.out.println("right");
                }
                else if (e.VK_E == e.getKeyCode()){
                    if(dad.isNear()==true){
                        drawHug=true;
                    }
                }
                
                
                
            
            }
            
            @Override
            public void keyReleased(KeyEvent e)
            {
            }
        });
        init();
        start();
    }
    Thread relaxer;
////////////////////////////////////////////////////////////////////////////
    public void init() {
        requestFocus();
    }
////////////////////////////////////////////////////////////////////////////
    public void destroy() {
    }

 

////////////////////////////////////////////////////////////////////////////
    public void paint(Graphics gOld) {
        if (image == null || Window.xsize != getSize().width || Window.ysize != getSize().height) {
            Window.xsize = getSize().width;
            Window.ysize = getSize().height;
            image = createImage(Window.xsize, Window.ysize);
            g = (Graphics2D) image.getGraphics();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
        }
//fill background
        g.setColor(Color.black);
        g.fillRect(0, 0, Window.xsize, Window.ysize);

        int x[] = {Window.getX(0), Window.getX(Window.getWidth2()), Window.getX(Window.getWidth2()), Window.getX(0), Window.getX(0)};
        int y[] = {Window.getY(0), Window.getY(0), Window.getY(Window.getHeight2()), Window.getY(Window.getHeight2()), Window.getY(0)};
//fill border
        g.setColor(Color.white);
        g.fillPolygon(x, y, 4);
        

// draw border
        g.setColor(Color.red);
        g.drawPolyline(x, y, 5);
        
        
        
        jay.Draw(g,this);
        dad.Draw(g, this);
        
        if(drawHug==true)
            hug.Draw(g,this);
        if (animateFirstTime) {
            gOld.drawImage(image, 0, 0, null);
            return;
        }
        
        
        g.setColor(Color.black);
        g.setFont (new Font ("Engravers MT",Font.PLAIN, 15));
        if(dad.isNear()==true)
            g.drawString("Press E to Hug", 600, 400);
        
        
        gOld.drawImage(image, 0, 0, null);
        
    }

////////////////////////////////////////////////////////////////////////////
// needed for     implement runnable
    public void run() {
        while (true) {
            animate();
            repaint();
            double seconds = 0.02;    //time that 1 frame takes.
            int miliseconds = (int) (1000.0 * seconds);
            try {
                Thread.sleep(miliseconds);
            } catch (InterruptedException e) {
            }
        }
    }
/////////////////////////////////////////////////////////////////////////
    public void reset() {
    }
/////////////////////////////////////////////////////////////////////////
    public void animate() {
        if (animateFirstTime) {
            animateFirstTime = false;
            if (Window.xsize != getSize().width || Window.ysize != getSize().height) {
                Window.xsize = getSize().width;
                Window.ysize = getSize().height;
            }
            reset();
        }
        dad.CurrentImage();
        dad.animate();
        
        
    }

////////////////////////////////////////////////////////////////////////////
    public void start() {
        if (relaxer == null) {
            relaxer = new Thread(this);
            relaxer.start();
        }
    }
////////////////////////////////////////////////////////////////////////////
    public void stop() {
        if (relaxer.isAlive()) {
            relaxer.stop();
        }
        relaxer = null;
    }
    
    
    
    
    
class Dad {
    
    private static Image CharacterImage;
    int xpos;
    int ypos;
    int walk;
    boolean IsLeft=true;
    boolean near=false;

    Dad()
    {
// Sets character beginning location
        xpos = 200;
        ypos = 220;
        IsLeft=false;
    }
   
    public void Draw(Graphics2D g,FathersDay thisObj)
    {
        drawImage(g,thisObj,CharacterImage,Window.getX(xpos),Window.getYNormal(ypos),0.0,4,4 );
        

    }
    public void drawImage(Graphics2D g,FathersDay thisObj,Image image,int xpos,int ypos,double rot,double xscale,double yscale) {
        int width = image.getWidth(thisObj);
        int height = image.getHeight(thisObj);
        g.translate(xpos,ypos);
        g.rotate(rot  * Math.PI/180.0);
        g.scale( xscale , yscale );

        g.drawImage(image,-width/2,-height/2,
        width,height,thisObj);

        g.scale( 1.0/xscale,1.0/yscale );
        g.rotate(-rot  * Math.PI/180.0);
        g.translate(-xpos,-ypos);
    }    
    public void animate ()
    {
//Sets the direction character is facing and moves character when key pressed
         if (walk==1&&ypos<405)
        {
            ypos+=5;
            walk=0;
        }
         else if (walk==2)
        {
            ypos-=5;
            walk=0;
        }
         else if (walk==3)
        {
            IsLeft=false;
            xpos-=5;
            walk=0;
        }
         else if (walk==4)
        {
            IsLeft=true;
            xpos+=5;
            walk=0;
        }
//Time used to stop animations when stopped walking

    if(xpos>=820){
        near=true;
    }
         
    }
    public boolean isNear(){
        return near;
    }
    
    
    public void CurrentImage (){
        
//Character current sprite used when standing & walking
       
            if(IsLeft==true)
                CharacterImage = Toolkit.getDefaultToolkit().getImage("./DadRight.PNG");
            else 
                CharacterImage = Toolkit.getDefaultToolkit().getImage("./DadLeft.PNG");
        
    }
}
    class Jay {
    
    private static Image CharacterImage = Toolkit.getDefaultToolkit().getImage("./Jay.PNG");
    int xpos;
    int ypos;
    Image Background;

    Jay()
    {
// Sets character beginning location
        xpos = 970;
        ypos = 222;
        Background=Toolkit.getDefaultToolkit().getImage("./Background.PNG");
    }
   
    public void Draw(Graphics2D g,FathersDay thisObj)
    {
        drawImage(g,thisObj,Background,Window.getX(Window.getWidth2()/2),Window.getYNormal(Window.getHeight2()/2)+70,0.0,0.65,0.65 );
        drawImage(g,thisObj,CharacterImage,Window.getX(xpos),Window.getYNormal(ypos),0.0,4,4 );
        

    }
    public void drawImage(Graphics2D g,FathersDay thisObj,Image image,int xpos,int ypos,double rot,double xscale,double yscale) {
        int width = image.getWidth(thisObj);
        int height = image.getHeight(thisObj);
        g.translate(xpos,ypos);
        g.rotate(rot  * Math.PI/180.0);
        g.scale( xscale , yscale );

        g.drawImage(image,-width/2,-height/2,
        width,height,thisObj);

        g.scale( 1.0/xscale,1.0/yscale );
        g.rotate(-rot  * Math.PI/180.0);
        g.translate(-xpos,-ypos);
    }    

         
    }
    
    
    
    class Hug {
    
    
    Image Background;

    Hug()
    {
// Sets character beginning location
        Background=Toolkit.getDefaultToolkit().getImage("./Card.PNG");
    }
   
    public void Draw(Graphics2D g,FathersDay thisObj)
    {
        drawImage(g,thisObj,Background,Window.getX(Window.getWidth2()/2),Window.getYNormal(Window.getHeight2()/2),0.0,0.65,0.65 );
        

    }
    public void drawImage(Graphics2D g,FathersDay thisObj,Image image,int xpos,int ypos,double rot,double xscale,double yscale) {
        int width = image.getWidth(thisObj);
        int height = image.getHeight(thisObj);
        g.translate(xpos,ypos);
        g.rotate(rot  * Math.PI/180.0);
        g.scale( xscale , yscale );

        g.drawImage(image,-width/2,-height/2,
        width,height,thisObj);

        g.scale( 1.0/xscale,1.0/yscale );
        g.rotate(-rot  * Math.PI/180.0);
        g.translate(-xpos,-ypos);
    }    

         
    }
    

}
    
    
    
    
    
    
    
    
    
    
    
    



class Window {
    
    static final int WINDOW_WIDTH = 1920;
    static final int WINDOW_HEIGHT = 1080;
    static final int XBORDER = 0;
    static final int YBORDER = 0;
    static final int WINDOW_BORDER = 0;
    static final int YTITLE = 0;
    static int xsize = -1;
    static int ysize = -1;   
    
 /////////////////////////////////////////////////////////////////////////
    public static int getX(int x) {
        return (x + XBORDER);
    }

    public static int getY(int y) {
        return (y + YBORDER + YTITLE);
    }

    public static int getYNormal(int y) {
        return (-y + YBORDER + YTITLE + getHeight2());
    }
    
    
    public static int getWidth2() {
        return (xsize - getX(0) - XBORDER);
    }

    public static int getHeight2() {
        return (ysize - getY(0) - YBORDER);
    }    
}