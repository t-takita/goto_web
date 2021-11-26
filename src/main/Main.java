package main;

import java.util.Scanner;

import constants.GameCode;
import gamemaster.GameMaster;

public class Main {

	public static void main(String[] args) {

		GameMaster master = new GameMaster();
		master.start();
		try(Scanner scan = new Scanner(System.in)) {
			while(true) {
				master.doProcess();
				if(master.getGameCode() == GameCode.CUT) {
					System.out.println(master.getHand(0).toString());
					while(true) {
				        String str = scan.nextLine();
				        if(master.order(str, 0)) {
				        	break;
				        }
				        System.out.println("もう１回");
					}
				}
			}
		}
	}
}
