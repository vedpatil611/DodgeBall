package com.dodgeball;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class Main extends JFrame {

	private JPanel contentPane;
	private JTextField textField;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main frame = new Main();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Main() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblEnterNumberBetween = new JLabel("Enter number between 1 and 5");
		lblEnterNumberBetween.setHorizontalAlignment(SwingConstants.CENTER);
		lblEnterNumberBetween.setBounds(79, 59, 274, 14);
		contentPane.add(lblEnterNumberBetween);
		
		textField = new JTextField();
		textField.setBounds(175, 94, 86, 20);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JButton btnStart = new JButton("Start");
		btnStart.setBounds(175, 154, 89, 23);
		contentPane.add(btnStart);
		btnStart.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					int t = Integer.parseInt(textField.getText());
					if(t<=5 && t>=1) {
						Main.this.dispose();
						new GameView(t).setVisible(true);
					} else {
						new Error().setVisible(true);
					}
				} catch(Exception e) {
					new Error().setVisible(true);
				}				
			}
		});
	}
}