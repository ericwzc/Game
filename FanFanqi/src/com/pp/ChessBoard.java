package com.pp;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class ChessBoard extends JPanel {
	private static final long serialVersionUID = -5107693106603836715L;
	public ChessMan[][] chessmen = new ChessMan[8][4];
	public int[] iAllchess = new int[32];
	public int iChessleft = 32;

	public boolean first = true;
	public int firstX = 0;
	public int firstY = 0;

	public JPanel panelWait = new JPanel();
	public JLabel label = new JLabel("等待连接...");

	public ChessBoard() {
		setLayout(null);
		setSize(410, 210);
		setLocation(110, 0);

		this.panelWait.setLocation(100, 70);
		this.panelWait.setSize(200, 50);
		this.label.setLocation(0, 0);
		this.label.setHorizontalAlignment(0);
		this.label.setFont(new Font("宋体", 1, 25));
		this.label.setForeground(new Color(255, 0, 0));
		this.label.setSize(this.panelWait.getSize().width, this.panelWait.getSize().height);
		this.panelWait.setOpaque(false);
		this.panelWait.add(this.label);

		this.panelWait.setVisible(false);

		add(this.panelWait);

		addMouseListener(new InnerMonitor());

		initchess();
	}

	public void initchess() {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 4; j++) {
				this.chessmen[i][j] = new ChessMan(i, j);
				this.chessmen[i][j].paintChessMan();
				add(this.chessmen[i][j]);
			}
		}
		for (int i = 0; i < 32; i++) {
			this.iAllchess[i] = 1;
		}

		this.iChessleft = 32;
	}

	public void rollback() {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 4; j++) {
				if (this.chessmen[i][j] != null) {
					this.chessmen[i][j].reset();
					this.chessmen[i][j].paintChessMan();
					this.chessmen[i][j].setVisible(false);
					this.chessmen[i][j].setVisible(true);
				}
			}
		}
		for (int i = 0; i < 32; i++) {
			this.iAllchess[i] = 1;
		}

		this.iChessleft = 32;
	}

	public void paintComponent(Graphics g) {
		g.drawImage(PublicResource.imageBox.imageMenu, 0, 0, getSize().width, getSize().height, this);
		for (int i = 5; i <= 205; i += 50) {
			g.drawLine(5, i, 405, i);
		}
		for (int j = 5; j <= 405; j += 50)
			g.drawLine(j, 5, j, 205);
	}

	public int checkEnd() {
		boolean red = false;
		boolean black = false;
		for (int i = 0; i < 16; i++) {
			if (this.iAllchess[i] != 0)
				red = true;
		}
		for (int i = 16; i < 32; i++) {
			if (this.iAllchess[i] != 0)
				black = true;
		}
		if (!red)
			return 1;
		if (!black)
			return 2;
		return 0;
	}

	public void matchend() {
		PublicResource.bPlaying = false;
		PublicResource.panelEnd.LtoRbutton.setEnabled(true);
		PublicResource.panelEnd.RtoLbutton.setEnabled(true);
		PublicResource.iColor = 0;
		PublicResource.panelDead.showChessColor.setVisible(false);
		PublicResource.panelDead.showChessRun.setVisible(false);
		PublicResource.panelMenu.setButtonEnabled();
		PublicResource.panelBoard.rollback();
		PublicResource.panelDead.clearDeadChess();
	}

	private class InnerMonitor implements MouseMotionListener, MouseListener {
		InnerMonitor() {
		}

		public void mouseDragged(MouseEvent e) {
		}

		public void mouseEntered(MouseEvent e) {
		}

		public void mouseExited(MouseEvent e) {
		}

		public void mouseMoved(MouseEvent e) {
		}

		public void mousePressed(MouseEvent e) {
			if (!PublicResource.bOk) {
				System.out.println("PublicResource.bOk" + PublicResource.bOk);
				return;
			}
			int x = e.getX();
			int y = e.getY();
			int z = 0;

			x = (x - 5) / 50;
			y = (y - 5) / 50;

			z = (e.getX() - (x * 50 + 25)) * (e.getX() - (x * 50 + 25)) + (e.getY() - (y * 50 + 25)) * (e.getY() - (y * 50 + 25));

			if ((Math.sqrt(z) < 22.0D) && (PublicResource.bPlaying)) {
				if (ChessBoard.this.first) {
					if ((ChessBoard.this.chessmen[x][y].iStatus == PublicResource.chessUnknow) && (!ChessBoard.this.chessmen[x][y].dead)) {
						showchess(x, y);
					} else if (!ChessBoard.this.chessmen[x][y].dead) {
						choosechess(x, y);
					}

				} else if (ChessBoard.this.chessmen[x][y].iStatus == PublicResource.chessSelected) {
					unchoosechess(x, y);
				} else if (ChessBoard.this.chessmen[x][y].iStatus == PublicResource.chessUnselected)
					move(x, y);
			}
		}

		public void mouseClicked(MouseEvent e) {
		}

		public void mouseReleased(MouseEvent e) {
		}

		public int getChessNum() {
			int iChessLeftnum = (int) (Math.round(Math.random() * 100.0D) % ChessBoard.this.iChessleft);
			int j = 0;

			for (int i = 0; i < 32; i++) {
				if (ChessBoard.this.iAllchess[i] == 1) {
					if (j == iChessLeftnum) {
						return i;
					}
					j++;
				}
			}

			return -1;
		}
		
		public int getChessNum(String cmd){
			String piece = cmd.substring(0, 1);
			String color = cmd.substring(1, 2);
			
			int j = 0;
			if(color.equals("r")){
				j = 0;
			}else{
				j = 1;
			}
			
			int i = j*16;
			
			if(piece.equalsIgnoreCase("p")){
				i += 0;
			}else if(piece.equalsIgnoreCase("c")){
				i += 5;
			}else if(piece.equalsIgnoreCase("h")){
				i += 7;
			}else if(piece.equalsIgnoreCase("r")){
				i += 9;
			}else if(piece.equalsIgnoreCase("e")){
				i += 11;
			}else if(piece.equalsIgnoreCase("g")){
				i += 13;
			}else if(piece.equalsIgnoreCase("k")){
				i += 15;
			}
			
			for (int s = i; s < 32; s++){
				if (ChessBoard.this.iAllchess[s] == 1) {
					return s;
				}
			}
			
			return getChessNum();
		}

		public void showchess(int x, int y) {
			String cheatCmd = "NaN";
			if(PublicResource.panelMenu.jcb.isSelected()){
				cheatCmd = JOptionPane.showInputDialog(PublicResource.panelBoard, "Choose favorite", "NaN");
			}
			
			int chessnum = cheatCmd.equalsIgnoreCase("NaN") ? getChessNum() : getChessNum(cheatCmd);

			ChessBoard.this.chessmen[x][y].setVisible(false);
			ChessBoard.this.chessmen[x][y].iStatus = PublicResource.chessUnselected;
			System.out.println("send: x:" + x + " y:" + y + " chessnum:" + chessnum);
			ChessBoard.this.iAllchess[chessnum] = 2;
			ChessBoard.this.chessmen[x][y].chessNum = chessnum;
			ChessBoard.this.iChessleft -= 1;
			String sInfo = "show," + x + "," + y + "," + ChessBoard.this.chessmen[x][y].chessNum;

			ChessBoard.this.chessmen[x][y].paintChessMan();
			ChessBoard.this.chessmen[x][y].setVisible(true);

			if ((PublicResource.iColor == 0) && (PublicResource.panelEnd.group.getSelection().getActionCommand() == "The first different")) {
				if ((PublicResource.bServer) && (PublicResource.iFirst == 2)) {
					if (chessnum > 15)
						PublicResource.panelMenu.gameserver.iCheckcolor[1] = 2;
					else {
						PublicResource.panelMenu.gameserver.iCheckcolor[1] = 1;
					}
					if (PublicResource.panelMenu.gameserver.iCheckcolor[0] != PublicResource.panelMenu.gameserver.iCheckcolor[1]) {
						PublicResource.iColor = PublicResource.panelMenu.gameserver.iCheckcolor[1];
						PublicResource.panelDead.showChessColor.paintShowChess(PublicResource.iColor == 1);
						PublicResource.panelDead.showChessColor.setVisible(true);
						PublicResource.iFirst = -2;
						String sInfo2 = "color," + PublicResource.panelMenu.gameserver.iCheckcolor[0];
						System.out.println(sInfo2);
						try {
							if (!PublicResource.bServer) {
								PublicResource.panelMenu.gameclient.dOut.writeUTF(sInfo2);
								PublicResource.panelMenu.gameclient.dOut.flush();
							} else {
								PublicResource.panelMenu.gameserver.out.writeUTF(sInfo2);
								PublicResource.panelMenu.gameserver.out.flush();
							}

							PublicResource.bOk = false;
							PublicResource.panelDead.showChessRun.setVisible(false);
							System.out.println(sInfo);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}

				} else if ((PublicResource.bServer) && (PublicResource.iFirst == 1)) {
					if (chessnum > 15)
						PublicResource.panelMenu.gameserver.iCheckcolor[0] = 2;
					else {
						PublicResource.panelMenu.gameserver.iCheckcolor[0] = 1;
					}
				}
			} else if (PublicResource.iColor == 0) {
				if (chessnum > 15) {
					PublicResource.iColor = 2;
				} else {
					PublicResource.iColor = 1;
				}
				PublicResource.iFirst = 0 - PublicResource.iFirst;
				PublicResource.panelDead.showChessColor.paintShowChess(PublicResource.iColor == 1);
				PublicResource.panelDead.showChessColor.setVisible(true);
			}

			try {
				if (!PublicResource.bServer) {
					PublicResource.panelMenu.gameclient.dOut.writeUTF(sInfo);
					PublicResource.panelMenu.gameclient.dOut.flush();
				} else {
					PublicResource.panelMenu.gameserver.out.writeUTF(sInfo);
					PublicResource.panelMenu.gameserver.out.flush();
				}

				PublicResource.bOk = false;
				PublicResource.panelDead.showChessRun.setVisible(false);
				System.out.println(sInfo);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void choosechess(int x, int y) {
			if (PublicResource.iColor == 1) {
				if (ChessBoard.this.chessmen[x][y].chessNum > 15)
					return;
			} else if (PublicResource.iColor == 2) {
				if (ChessBoard.this.chessmen[x][y].chessNum <= 15)
					return;
			} else
				return;
			ChessBoard.this.chessmen[x][y].iStatus = PublicResource.chessSelected;
			ChessBoard.this.chessmen[x][y].setVisible(false);
			ChessBoard.this.chessmen[x][y].setVisible(true);
			ChessBoard.this.firstX = x;
			ChessBoard.this.firstY = y;
			ChessBoard.this.first = false;

			String sInfo = "selected," + x + "," + y;
			try {
				if (!PublicResource.bServer) {
					PublicResource.panelMenu.gameclient.dOut.writeUTF(sInfo);
					PublicResource.panelMenu.gameclient.dOut.flush();
				} else {
					PublicResource.panelMenu.gameserver.out.writeUTF(sInfo);
					PublicResource.panelMenu.gameserver.out.flush();
				}

				System.out.println(sInfo);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void unchoosechess(int x, int y) {
			ChessBoard.this.chessmen[x][y].iStatus = PublicResource.chessUnselected;
			ChessBoard.this.chessmen[x][y].setVisible(false);
			ChessBoard.this.chessmen[x][y].setVisible(true);
			ChessBoard.this.first = true;
			String sInfo = "unselected," + x + "," + y;
			try {
				if (!PublicResource.bServer) {
					PublicResource.panelMenu.gameclient.dOut.writeUTF(sInfo);
					PublicResource.panelMenu.gameclient.dOut.flush();
				} else {
					PublicResource.panelMenu.gameserver.out.writeUTF(sInfo);
					PublicResource.panelMenu.gameserver.out.flush();
				}

				System.out.println(sInfo);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public boolean checknext(int x, int y) {
			if ((x != ChessBoard.this.firstX) && (y != ChessBoard.this.firstY))
				return false;
			if ((Math.abs(ChessBoard.this.firstX - x) > 1) || (Math.abs(ChessBoard.this.firstY - y) > 1)) {
				return false;
			}

			return true;
		}

		public boolean isPao(int i) {
			if ((i != 5) && (i != 6) && (i != 21) && (i != 22)) {
				return false;
			}
			return true;
		}

		public void exchange(int x, int y) {
			ChessBoard.this.chessmen[ChessBoard.this.firstX][ChessBoard.this.firstY].iStatus = PublicResource.chessUnselected;

			ChessBoard.this.chessmen[x][y].setVisible(false);
			ChessBoard.this.chessmen[ChessBoard.this.firstX][ChessBoard.this.firstY].setVisible(false);

			if ((ChessBoard.this.chessmen[x][y].chessNum >= 0) && (ChessBoard.this.chessmen[x][y].chessNum < 32)) {
				ChessBoard.this.iAllchess[ChessBoard.this.chessmen[x][y].chessNum] = 0;
				PublicResource.panelDead.addDeadChess(ChessBoard.this.chessmen[x][y].chessNum);
			}
			ChessBoard.this.chessmen[x][y].chessNum = ChessBoard.this.chessmen[ChessBoard.this.firstX][ChessBoard.this.firstY].chessNum;
			ChessBoard.this.chessmen[x][y].dead = ChessBoard.this.chessmen[ChessBoard.this.firstX][ChessBoard.this.firstY].dead;

			ChessBoard.this.chessmen[ChessBoard.this.firstX][ChessBoard.this.firstY].dead = true;
			ChessBoard.this.chessmen[ChessBoard.this.firstX][ChessBoard.this.firstY].chessNum = 32;

			ChessBoard.this.chessmen[x][y].paintChessMan();
			ChessBoard.this.chessmen[x][y].setVisible(true);

			ChessBoard.this.first = true;
		}

		public boolean checkPao(int x, int y) {
			if (checknext(x, y)) {
				if (checkEat(x, y)) {
					return true;
				}
				return false;
			}

			if ((x != ChessBoard.this.firstX) && (y != ChessBoard.this.firstY))
				return false;
			if ((x == ChessBoard.this.firstX) && (checkInner(x, y, true))) {
				return true;
			}
			if ((y == ChessBoard.this.firstY) && (checkInner(x, y, false))) {
				return true;
			}

			return false;
		}

		public boolean checkInner(int x, int y, boolean isX) {
			int bigY = 0;
			int litY = 0;
			int bigX = 0;
			int litX = 0;
			int num = 0;
			if (isX) {
				if (y > ChessBoard.this.firstY) {
					bigY = y;
					litY = ChessBoard.this.firstY;
				} else {
					bigY = ChessBoard.this.firstY;
					litY = y;
				}
				for (int i = litY + 1; i < bigY; i++) {
					if (!ChessBoard.this.chessmen[x][i].dead)
						num++;
				}
				System.out.println(bigY);
				System.out.println(litY);
				System.out.println(num);

				if (num == 1) {
					return true;
				}
				return false;
			}

			if (x > ChessBoard.this.firstX) {
				bigX = x;
				litX = ChessBoard.this.firstX;
			} else {
				bigX = ChessBoard.this.firstX;
				litX = x;
			}
			for (int i = litX + 1; i < bigX; i++) {
				if (!ChessBoard.this.chessmen[i][y].dead)
					num++;
			}
			System.out.println(bigX);
			System.out.println(litX);
			System.out.println(num);
			if (num == 1) {
				return true;
			}
			return false;
		}

		public boolean checkColor(int x, int y) {
			if (PublicResource.sAllChessmen[ChessBoard.this.chessmen[x][y].chessNum][1]
					.equals(PublicResource.sAllChessmen[ChessBoard.this.chessmen[ChessBoard.this.firstX][ChessBoard.this.firstY].chessNum][1])) {
				return false;
			}
			return true;
		}

		public boolean checkEat(int x, int y) {
			int black = 0;
			int red = 0;
			if (isBlack(x, y)) {
				black = ChessBoard.this.chessmen[x][y].chessNum - 16;
				red = ChessBoard.this.chessmen[ChessBoard.this.firstX][ChessBoard.this.firstY].chessNum;
				if ((red < 5) && (black < 5))
					return true;
				if ((red < 5) && (black == 15))
					return true;
				if ((red >= 5) && (black >= 5) && (red <= 6) && (black <= 6))
					return true;
				if ((red >= 7) && (black >= 7) && (red <= 8) && (black <= 8))
					return true;
				if ((red >= 9) && (black >= 9) && (red <= 10) && (black <= 10))
					return true;
				if ((red >= 11) && (black >= 11) && (red <= 12) && (black <= 12))
					return true;
				if ((red >= 13) && (black >= 13) && (red <= 14) && (black <= 14)) {
					return true;
				}
				if (black <= red) {
					if (red != 15) {
						return true;
					}
					if (black >= 5) {
						return true;
					}
					return false;
				}

				return false;
			}

			red = ChessBoard.this.chessmen[x][y].chessNum;
			black = ChessBoard.this.chessmen[ChessBoard.this.firstX][ChessBoard.this.firstY].chessNum - 16;
			if ((red < 5) && (black < 5))
				return true;
			if ((black < 5) && (red == 15))
				return true;
			if ((red >= 5) && (black >= 5) && (red <= 6) && (black <= 6))
				return true;
			if ((red >= 7) && (black >= 7) && (red <= 8) && (black <= 8))
				return true;
			if ((red >= 9) && (black >= 9) && (red <= 10) && (black <= 10))
				return true;
			if ((red >= 11) && (black >= 11) && (red <= 12) && (black <= 12))
				return true;
			if ((red >= 13) && (black >= 13) && (red <= 14) && (black <= 14))
				return true;
			if (red <= black) {
				if (black != 15) {
					return true;
				}
				if (red >= 5) {
					return true;
				}
				return false;
			}

			return false;
		}

		public void move(int x, int y) {
			boolean bMove = false;
			if (ChessBoard.this.chessmen[x][y].dead) {
				if (checknext(x, y)) {
					exchange(x, y);
					bMove = true;
				} else {
					bMove = false;
				}

			} else if (!checkColor(x, y)) {
				bMove = false;
			} else if (!isPao(ChessBoard.this.chessmen[ChessBoard.this.firstX][ChessBoard.this.firstY].chessNum)) {
				if ((checknext(x, y)) && (checkEat(x, y))) {
					exchange(x, y);
					bMove = true;
				} else {
					bMove = false;
				}
			} else if (checkPao(x, y)) {
				exchange(x, y);
				bMove = true;
			} else {
				bMove = false;
			}

			if (bMove) {
				String sInfo = "move," + x + "," + y;
				try {
					if (!PublicResource.bServer) {
						PublicResource.panelMenu.gameclient.dOut.writeUTF(sInfo);
						PublicResource.panelMenu.gameclient.dOut.flush();
					} else {
						PublicResource.panelMenu.gameserver.out.writeUTF(sInfo);
						PublicResource.panelMenu.gameserver.out.flush();
					}
					PublicResource.bOk = false;
					PublicResource.panelDead.showChessRun.setVisible(false);

					System.out.println(sInfo);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if (PublicResource.bServer) {
				int end = ChessBoard.this.checkEnd();
				String sInfo = "end,lose";
				if (end == 1) {
					try {
						if (!PublicResource.bServer) {
							PublicResource.panelMenu.gameclient.dOut.writeUTF(sInfo);
							PublicResource.panelMenu.gameclient.dOut.flush();
						} else {
							PublicResource.panelMenu.gameserver.out.writeUTF(sInfo);
							PublicResource.panelMenu.gameserver.out.flush();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
					JOptionPane.showMessageDialog(PublicResource.panelBoard, "恭喜，胜利！", "信息", 1);

					PublicResource.panelBoard.matchend();
				} else if (end == 2) {
					try {
						if (!PublicResource.bServer) {
							PublicResource.panelMenu.gameclient.dOut.writeUTF(sInfo);
							PublicResource.panelMenu.gameclient.dOut.flush();
						} else {
							PublicResource.panelMenu.gameserver.out.writeUTF(sInfo);
							PublicResource.panelMenu.gameserver.out.flush();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
					JOptionPane.showMessageDialog(PublicResource.panelBoard, "恭喜，胜利！", "信息", 1);

					PublicResource.panelBoard.matchend();
				}
			}
		}

		public boolean isBlack(int x, int y) {
			if (PublicResource.sAllChessmen[ChessBoard.this.chessmen[x][y].chessNum][1].toLowerCase().equals("black")) {
				return true;
			}
			return false;
		}
	}
}
