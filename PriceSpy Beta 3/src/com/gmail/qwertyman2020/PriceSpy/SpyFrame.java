package com.gmail.qwertyman2020.PriceSpy;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JProgressBar;
import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.SwingUtilities;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class SpyFrame extends JFrame implements Runnable {

	private static final long serialVersionUID = 1L;
	public JFrame frmPricespy;
	private JProgressBar progressBar;
	private static SpyFrame  window;
	private JButton btnNewButton;
	private volatile Status status;
	private final String version = "2.0";
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new SpyFrame();
					window.frmPricespy.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public SpyFrame() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmPricespy = new JFrame();
		frmPricespy.setBounds(100, 100, 450, 300);
		frmPricespy.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmPricespy.setTitle("PriceSpy V"+version);
				
		progressBar = new JProgressBar();
		progressBar.setAlignmentX(Component.LEFT_ALIGNMENT);
		progressBar.setSize(new Dimension(300, 100));
		frmPricespy.getContentPane().add(progressBar, BorderLayout.CENTER);
		
		btnNewButton = new JButton("Verzamel Prijzen");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				window.switchButton();
				MainSwingWorker worker = new MainSwingWorker(window);
			    worker.execute();
			}
		});
		frmPricespy.getContentPane().add(btnNewButton, BorderLayout.EAST);
	}
	public void setBar(int progress) {

	}
	
	public void updateGUI(Status status){
		   setStatus(status);
		   //use this.status for all gui data
		   progressBar.setValue((int) Math.round(this.status.getProgress()));
		   frmPricespy.setTitle(this.status.getTitle());
		   SwingUtilities.invokeLater(this);
	}
	
	private synchronized void setStatus(Status status) {
		  this.status = status;
	}

	public void switchButton(){
		btnNewButton.setEnabled(!btnNewButton.isEnabled());
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}