package com.xiami.logic;

import javax.swing.JLabel;

import com.xiami.core.SystemConfig;
import com.xiami.frame.PuzzleDialog;

public class PuzzleCounter implements Runnable {

	private PuzzleDialog puzzleDialog;

	private int time = Integer.parseInt(SystemConfig.getSystemProp("puzzle_time"));

	private JLabel label;

	public PuzzleCounter(PuzzleDialog puzzleDialog) {
		this.puzzleDialog = puzzleDialog;
		label = puzzleDialog.getLabel_title();
	}

	public void run() {
		while (puzzleDialog.isAlive()&&time >= 0) {
			try {
				String str = label.getText().indexOf("  Time") != -1 ? label
						.getText().substring(0, label.getText().indexOf("  Time"))
						: label.getText();

				label.setText(str + "  Time left : " + time +" secends");
				time--;
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		if(puzzleDialog.isAlive()){
			puzzleDialog.getIndexList().clear();
			puzzleDialog.popUpResult(true);
		}
		
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}
}
