//Basic Game Application
// Basic Object, Image, Movement
// Threaded

//*******************************************************************************
//Import Section
//Add Java libraries needed for the game
//import java.awt.Canvas;

//Graphics Libraries
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JPanel;

//*******************************************************************************

public class BasicGameApp2 implements Runnable, KeyListener, MouseListener {

    //Variable Definition Section
    //Declare the variables used in the program
    //You can set their initial values too

    //Sets the widt
    // h and height of the program window
    final int WIDTH = 1000;
    final int HEIGHT = 700;

    //Declare the variables needed for the graphics
    public JFrame frame;
    public Canvas canvas;
    public JPanel panel;

    public BufferStrategy bufferStrategy;

    Pacman pacman;
    Image packmanImage;
    BlueGhost blueGhost;
    Image blueghostImage;
    Image pinkghostImage;
    PinkGhost pinkGhost;
    Image death = Toolkit.getDefaultToolkit().getImage("deathemoji.png");
    Image background = Toolkit.getDefaultToolkit().getImage("background.jpeg");
    Image goldcoin = Toolkit.getDefaultToolkit().getImage("goldcoin.png");
    Coin [] coinshower = new Coin[15];
    int keyNum=0;

    public boolean firstCrash;
    public boolean firstcharactercrash;
    public boolean secondCrash;
    public boolean coinPacmanCrash;
    public int HealthBar = 8;
    public int CoinCounter;
    public SoundFile pacmaneating;


    // Main method definition
    // This is the code that runs first and automatically
    public static void main(String[] args) {

        BasicGameApp2 ex = new BasicGameApp2();   //creates a new instance of the game
        new Thread(ex).start();                 //creates a threads & starts up the code in the run( ) method
    }


    // This section is the setup portion of the program
    // Initialize your variables and construct your program objects here.
    public BasicGameApp2() { // BasicGameApp constructor

        setUpGraphics();
        firstcharactercrash = true;
        secondCrash = true;
        coinPacmanCrash = true;
        for (int i = 0; i < coinshower.length; i++) {
            coinshower[i] = new Coin("nope",(int)(Math.random()*990),(int)(Math.random()*500), 0.3);
        }

        pacman = new Pacman("pacman.png", 100, 400, 0.75);
        packmanImage = Toolkit.getDefaultToolkit().getImage("pacman.png");
        pinkGhost = new PinkGhost("pinkghost.png", 300, 300, 0.75);
        pinkghostImage = Toolkit.getDefaultToolkit().getImage("pinkghost.png");
        blueGhost = new BlueGhost("blueghost.png", 500, 200, 0.25);
        blueghostImage = Toolkit.getDefaultToolkit().getImage("blueghost.png");
        pacmaneating = new SoundFile("pacmaneating.wav");

//        voldemort2 = new Voldemort ("voldemort.png", 500, 100, 0.25);
//        voldemortImage2 = Toolkit.getDefaultToolkit().getImage("voldemort.png");
        run();


    } // end BasicGameApp constructor


//*******************************************************************************
//User Method Section
// put your code to do things here.

    // main thread
    // this is the code that plays the game after you set things up
    public void run() {
        //for the moment we will loop things forever.
        while (true) {
            moveThings();//move all the game objects

            checkCrash();
            firstEvilCrash();
            pacmanpinkcrash();
            coinPacmanCrash();



            render();  // paint the graphics
            pause(30); // sleep for 10 ms
        }
    }



    public void moveThings() {

        for (int i = 0; i < coinshower.length; i++) {
            coinshower[i].move();

        }
        pacman.move();
        blueGhost.bounce();
        pinkGhost.move();
    }

    public void checkCrash(){
        if(pinkGhost.rect.intersects(blueGhost.rect) && firstCrash == true) {
            firstCrash = false;
            blueGhost.dx = -blueGhost.dx;
            blueGhost.dy = -blueGhost.dy;
            if (Math.random() > 0.5) {
                pinkGhost.dx = -pinkGhost.dx;
            } else {
                pinkGhost.dy = -pinkGhost.dy;
            }
        }
        if (pinkGhost.rect.intersects(blueGhost.rect) == false) {
            firstCrash = true;
        }


//         if (pacman.isAlive= false){
//            pacman.isAlive = true;
//        }

    }

     public void pacmanpinkcrash() {
            if(pacman.rect.intersects(pinkGhost.rect) && firstcharactercrash == true) {
                firstcharactercrash = false;
                pinkGhost.dx = -pinkGhost.dx;
                pinkGhost.dy = -pinkGhost.dy;
                pinkGhost.bounce();
//                pacman.dx = -pacman.dx;
//                pacman.dy = -pacman.dy;
                if(HealthBar > 0){
                    HealthBar = HealthBar - 1;
                }
                if(HealthBar == 0){
                    pacman.isAlive = false;
                }


//            voldemort.height += 10;
//            voldemort.width += 10;
                }
            if (pacman.rect.intersects(pinkGhost.rect) == false) {
                firstcharactercrash = true;

           }


          //  for(int x = 0; x < 1000; x = x +10){
         //       astro1.width += x;
          //      astro1.height += x;

          //  }
        }
    private void firstEvilCrash() {
        if (pacman.rect.intersects(blueGhost.rect) && secondCrash == true) {
            secondCrash = false;
            blueGhost.dx = -blueGhost.dx;
            blueGhost.dy = -blueGhost.dy;
            pacman.dx = -pacman.dx;
            pacman.dy = -pacman.dy;
        }
//        if(pacman.isAlive== false) {
//            pacman.isAlive = true;
//        }

        if (pacman.rect.intersects(blueGhost.rect) == false) {
            secondCrash = true;
        }

    }
    public void coinPacmanCrash() {
        for (int i = 0; i < coinshower.length; i++) {
            if (coinshower[i].rect.intersects(pacman.rect) == true) {
                coinshower[i].xpos = pacman.xpos;
                coinshower[i].ypos = pacman.ypos;
                coinshower[i].dx = pacman.dx;
                coinshower[i].dy = pacman.dy;
            }
            if (coinshower[i].rect.intersects(pacman.rect) == true && coinshower[i].coinCrash == false) {
                coinshower[i].coinCrash = true;
                CoinCounter = CoinCounter + 1;
                pacmaneating.play();
            }
        }

    }
//    public void CoinSound(Coin coin){
//        pacmaneating = new SoundFile("pacmaneating.wav");
//        pacmaneating.play();
//    }



    //Paints things on the screen using bufferStrategy
    private void render() {
        Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
        g.clearRect(0, 0, WIDTH, HEIGHT);
        g.drawImage(background, 0,0, WIDTH,HEIGHT,null);
        g.setColor(Color.WHITE);
        g.setFont(new Font("plain", Font.BOLD, 20));
        g.drawString("Health" + HealthBar, 220, 40);
        g.setFont(new Font("plain", Font.BOLD, 20));
        g.drawString("Coin Count" + CoinCounter, 500, 40);


    if(HealthBar<1){
        g.setColor(Color.WHITE);
        g.setFont(new Font("plain", Font.BOLD, 100));
        g.drawString("GAME OVER" , 200, 400);

    }
    if(CoinCounter == 35){
        g.setColor(Color.WHITE);
        g.setFont(new Font("plain", Font.BOLD, 100));
        g.drawString("YOU WIN" , 200, 400);
    }

        g.setColor(new Color(50,90,80));
      //  g.fillRect(800,70,(100-voldemort.health),15, voldemort.health);
        //draw the image
        if (pacman.isAlive == false) {
            g.drawImage(death, pacman.xpos, pacman.ypos, pacman.width, pacman.height, null);
        }  else {
            g.drawImage(packmanImage, pacman.xpos, pacman.ypos, pacman.width, pacman.height, null);
        }
        g.drawImage(blueghostImage, blueGhost.xpos, blueGhost.ypos, blueGhost.width, blueGhost.height, null);
        g.drawImage(pinkghostImage, pinkGhost.xpos, pinkGhost.ypos, pinkGhost.width, pinkGhost.height, null);


        for (int i = 0; i < coinshower.length; i++) {
            g.drawImage(goldcoin, coinshower[i].xpos, coinshower[i].ypos, coinshower[i].width, coinshower[i].height, null);

        }
        g.dispose();
        bufferStrategy.show();
    }

    //Pauses or sleeps the computer for the amount specified in milliseconds
    public void pause(int time ) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
        }
    }

    //Graphics setup method
    private void setUpGraphics() {
        System.out.println("setUpGraphics");
        frame = new JFrame("Application Template");   //Create the program window or frame.  Names it.

        panel = (JPanel) frame.getContentPane();  //sets up a JPanel which is what goes in the frame
        panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));  //sizes the JPanel
        panel.setLayout(null);   //set the layout

        // creates a canvas which is a blank rectangular area of the screen onto which the application can draw
        // and trap input events (Mouse and Keyboard events)
        canvas = new Canvas();
        canvas.setBounds(0, 0, WIDTH, HEIGHT);
        canvas.setIgnoreRepaint(true);

        panel.add(canvas);  // adds the canvas to the panel.
        canvas.addKeyListener(this);

        // frame operations
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  //makes the frame close and exit nicely
        frame.pack();  //adjusts the frame and its contents so the sizes are at their default or larger
        frame.setResizable(false);   //makes it so the frame cannot be resized
        frame.setVisible(true);      //IMPORTANT!!!  if the frame is not set to visible it will not appear on the screen!

        // sets up things so the screen displays images nicely.
        canvas.createBufferStrategy(2);
        bufferStrategy = canvas.getBufferStrategy();
        canvas.requestFocus();
        System.out.println("DONE graphic setup");
    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println("press key" + e.getKeyCode());
        keyNum = e.getKeyCode();
        if (keyNum == 37) {
            pacman.dx = -10;

        }
        if (keyNum == 39) {
            pacman.dx = 10;

        }
        if (keyNum == 38) {
            pacman.dy = -10;
        }
        if (keyNum == 40) {
            pacman.dy = 10;

        }
        if (keyNum == 65) {
            pinkGhost.dx = -10;
        }
        if (keyNum == 68) {
            pinkGhost.dx = 10;
        }
        if (keyNum == 87) {
            pinkGhost.dy = -10;
        }
        if (keyNum == 83) {
            pinkGhost.dy = 10;
        }
        if (HealthBar < 1) {
            pacman.dx = 0;
            pacman.dy = 0;
            pinkGhost.dx = 0;
            pinkGhost.dy = 0;
            blueGhost.dx = 0;
            blueGhost.dy = 0;

        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(keyNum==37){
            pacman.dx= 0;
            pacman.dy=0;
        }
        if(keyNum==39){
            pacman.dx = 0;
            pacman.dy = 0;
        }
        if(keyNum==38){
            pacman.dx = 0;
            pacman.dy = 0;
        }
        if(keyNum==40){
            pacman.dx = 0;
            pacman.dy = 0;
        }
        if(keyNum==65){
            pinkGhost.dx= 0;
            pinkGhost.dy=0;
        }
        if(keyNum==68){
            pinkGhost.dx = 0;
            pacman.dy = 0;
        }
        if(keyNum==87){
            pinkGhost.dx = 0;
            pinkGhost.dy = 0;
        }
        if(keyNum==83){
            pinkGhost.dx = 0;
            pinkGhost.dy = 0;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println(e.getLocationOnScreen());

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
