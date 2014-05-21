package com.pp;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

public class MainFrame extends JFrame {
	private static final long serialVersionUID = 1201059946750206650L;

	public MainFrame() {
		super("翻翻棋");
		frmInit();
	}

	public void frmInit() {
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		getContentPane().setLayout(null);

		getContentPane().add(PublicResource.panelBoard, null);
		getContentPane().add(PublicResource.panelMenu, null);
		getContentPane().add(PublicResource.panelDead, null);
		getContentPane().add(PublicResource.panelEnd, null);

		setSize(526, 400);
		setResizable(false);
		setVisible(true);
	}

	public static void main(String[] args) {
		new MainFrame();
//		new HashSet();
//		new Object();
	}
}
