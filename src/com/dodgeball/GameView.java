package com.dodgeball;

import java.awt.EventQueue;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class GameView extends JFrame implements KeyListener {

	private JPanel contentPane;
	JLabel cat;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GameView frame = new GameView(5);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	int catH=0;
	int catX=0;
	int level;
	
	ArrayList<JLabel> mobs;
	ArrayList<Integer> mobX;
	ArrayList<Integer> mobY;
	ArrayList<Integer> mobD;
	
	volatile boolean alive = true;
	long score = 0;
	
	public GameView(int level) {
		this.level = level;
		mobs = new ArrayList<JLabel>();
		mobX = new ArrayList<Integer>();
		mobY = new ArrayList<Integer>();
		mobD = new ArrayList<Integer>();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		cat = new JLabel("");
		cat.setBounds(250, 150, 50, 50);
		cat.setIcon(new ImageIcon(GameView.class.getResource("/com/dodgeball/assets/red.png")));
		contentPane.add(cat);   

		addKeyListener(this);
		setFocusable(true);
		
		//Gamethread start
		Thread gm = new GenerateMob();
		Thread mb = new MoveMobs();
		Thread rs = new RefreshScreen();
		Thread cd = new CheckDead();
		gm.start();
		mb.start();
		rs.start();
		cd.start();
	}
	@Override
	public void keyPressed(KeyEvent e) {                //Controls
		int keyCode = e.getKeyCode();
		switch(keyCode) {
			case KeyEvent.VK_UP:
        		catH-=5;
            	break;
			case KeyEvent.VK_RIGHT:
				catX+=5;
        		break;
			case KeyEvent.VK_LEFT:
				catX-=5;
        		break;
			case KeyEvent.VK_DOWN:
				catH+=5;
            	break;
		}
		cat.setBounds(250+catX, 150+catH, 50, 50);
	}
	@Override
	public void keyReleased(KeyEvent e) {
		
	}
	@Override
	public void keyTyped(KeyEvent e) {
		
	}
	void removeMob(int i) {
		score++;
		contentPane.remove(mobs.get(i));
		mobX.remove(i);
		mobY.remove(i);
		mobs.remove(i);
		mobD.remove(i);
	}
	class MoveMobs extends Thread {
		public void run() {
			while(alive) {
				for(int i=0;i<mobD.size();i++) {
					switch(mobD.get(i)) {
						case 0:
							mobX.set(i, mobX.get(i)+5);
							mobs.get(i).setBounds(mobX.get(i),mobY.get(i),50,50);
							if(mobX.get(i) > 550) {
								removeMob(i);
								continue;
							}
							break;
						case 1:
							mobX.set(i, mobX.get(i)-5);
							mobs.get(i).setBounds(mobX.get(i),mobY.get(i),50,50);
							if(mobX.get(i) < -50) {
								removeMob(i);
								continue;
							}
							break;
						case 2:
							mobY.set(i, mobY.get(i)+5);
							mobs.get(i).setBounds(mobX.get(i),mobY.get(i),50,50);
							if(mobY.get(i) > 350) {
								removeMob(i);
								continue;
							}
							break;
						case 3:
							mobY.set(i, mobY.get(i)-5);
							mobs.get(i).setBounds(mobX.get(i),mobY.get(i),50,50);
							if(mobY.get(i) < -50) {
								removeMob(i);
								continue;
							}
							break;
					}
				}
				try {
					Thread.sleep(40);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	class GenerateMob extends Thread {
		public void run() {
			while(alive) {
				if(mobs.size() < level) {
					Random rand = new Random();
					int k = rand.nextInt(4);
					mobs.add(new JLabel(new ImageIcon(GameView.class.getResource("/com/dodgeball/assets/black.png"))));
					mobD.add(k);
					switch(k) {
						case 0:
							mobX.add(0);
							mobY.add(rand.nextInt(300));
							mobs.get(mobs.size()-1).setBounds(-50, mobY.get(mobY.size()-1), 50, 50);
							break;
						case 1:
							mobX.add(550);
							mobY.add(rand.nextInt(300));
							mobs.get(mobs.size()-1).setBounds(550, mobY.get(mobY.size()-1), 50, 50);
							break;
						case 2:
							mobX.add(rand.nextInt(550));
							mobY.add(0);
							mobs.get(mobs.size()-1).setBounds(mobX.get(mobX.size()-1), -50, 50, 50);
							break;
						case 3:
							mobX.add(rand.nextInt(550));
							mobY.add(300);
							mobs.get(mobs.size()-1).setBounds(mobX.get(mobX.size()-1), 350, 50, 50);						
							break;
					}
					contentPane.add(mobs.get(mobs.size()-1));
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	class RefreshScreen extends Thread {
		public void run() {
			while(alive) {
				GameView.this.repaint();
				try {
					Thread.sleep(40);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	class CheckDead extends Thread {
		public void run() {
			while(alive) {
				for(int i=0;i<mobX.size();i++) {
					double d = Math.sqrt(Math.pow(250+catX-mobX.get(i),2)+Math.pow(150+catH-mobY.get(i), 2));
					if(d<50) {
						alive = false;
						GameView.this.dispose();
						new GameOver(score).setVisible(true);;
					}
				}
				try {
					Thread.sleep(40);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}