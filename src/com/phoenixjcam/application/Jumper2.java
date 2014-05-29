package com.phoenixjcam.application;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.phoenixjcam.application.map.GameMap;
import com.phoenixjcam.application.movement.Movement;
import com.phoenixjcam.application.player.Player;

/**
 * contend: game loop, running at new thread (66 FPS) and main method
 * 
 * @author Bart Bien
 * 
 */
public class Jumper2 extends JPanel implements Runnable, KeyListener {
    private static final long serialVersionUID = 1L;

    // static size of frame
    public static final int WIDTH = 750;
    public static final int HEIGHT = 435;

    /** game loop run at new thread */
    private Thread thread;
    private boolean running;

    /** place to render game */
    private BufferedImage image;

    /** graphic to draw at image */
    private Graphics2D g2D;

    private GameMap map;
    private Movement movement;
    private Player player;

    private DrawTime drawTime;

    public Jumper2() {
	drawTime = new DrawTime();
	setFocusable(true);
	requestFocus();
    }

    @Override
    public void addNotify() {
	super.addNotify();
	if (thread == null) {
	    thread = new Thread(this);
	    thread.start();
	}
	addKeyListener(this);
    }

    @Override
    public void run() {
	init();

	while (running) {
	    drawTime.start = System.currentTimeMillis();

	    movement.update();
	    render();
	    draw();

	    drawTime.delay = (System.currentTimeMillis() - drawTime.start);
	    drawTime.wait = drawTime.target - drawTime.delay;

	    if (drawTime.wait > 0) {
		try {
		    Thread.sleep(drawTime.wait);
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
	    }
	}
    }

    /** initialize game components */
    private void init() {
	running = true;

	image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	g2D = (Graphics2D) image.getGraphics();

	map = new GameMap(25);
	player = new Player(new Point(25, 25), new Point2D.Double(40.0, 350.0));

	movement = new Movement(map, player);
    }

    private void render() {
	map.draw(g2D);
	movement.draw(g2D);
    }

    /** All rendered stuff in buffered image, draw at current component. */
    private void draw() {
	Graphics2D g2 = (Graphics2D) this.getGraphics();

	g2.drawImage(image, 0, 0, null);
	g2.dispose();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
	int key = e.getKeyCode();

	if (key == KeyEvent.VK_LEFT) {
	    player.setTurnedLeft(true);
	} else if (key == KeyEvent.VK_RIGHT) {
	    player.setTurnedRight(true);
	}

	if (key == KeyEvent.VK_SPACE) {
	    player.setJumping();
	}
	// new position
	if (key == KeyEvent.VK_N) {
	    player.setPosition(new Point2D.Double(40.0, 350.0));
	}
	// increase 3.0 to player speed
	else if (key == KeyEvent.VK_P) {
	    player.increaseSpeed();
	}
	// decrease 3.0 from player speed
	else if (key == KeyEvent.VK_M) {
	    player.decreaseSpeed();
	}
    }

    @Override
    public void keyReleased(KeyEvent e) {
	int key = e.getKeyCode();

	if (key == KeyEvent.VK_LEFT) {
	    player.setTurnedLeft(false);
	} else if (key == KeyEvent.VK_RIGHT) {
	    player.setTurnedRight(false);
	}
    }

    /** All fields should be initialized by using milliseconds */
    private class DrawTime {
	/**
	 * specify time that thread will be wait (milliseconds). <br>
	 * As default target = 15 ms (effect will be 1000 / 15 = 66 FPS)
	 */
	public final long target = 15;
	/** start time */
	public long start;
	/** specify time that graphic objects need to done */
	public long delay;
	/** calculated time that current thread will be wait to next iteration */
	public long wait;

	public DrawTime() {
	}
    }

    /** Game frame */
    private static void createGUI() {
	JFrame frame = new JFrame("Jumper");
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setContentPane(new Jumper2());
	frame.setResizable(false);
	frame.setLocation(200, 200);
	frame.setSize(WIDTH, HEIGHT);
	ImageIcon img = new ImageIcon(Jumper2.class.getResource("res/icoB.png"));
	frame.setIconImage(img.getImage());
	frame.setVisible(true);
    }

    public static void main(String[] args) {
	javax.swing.SwingUtilities.invokeLater(new Runnable() {
	    public void run() {
		createGUI();
	    }
	});
    }
}
