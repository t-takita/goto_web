package main;

import java.util.Scanner;

import constants.GameCode;
import gamemaster.GameMaster;

public class Main {

	public static void main(String[] args) {

		/**HandLogic logic = new HandLogic();
		Integer[] handArray = {2,3,5,6,11,14,18,19,26,31,32,35,36,11};
		List<Integer> tehaiList = Arrays.asList(handArray);
		System.out.println(logic.getShantensu(tehaiList, null)); **/

		GameMaster master = new GameMaster();
		master.start();
		try(Scanner scan = new Scanner(System.in)) {
			while(true) {
				master.doProcess();
				if(master.getGameCode() == GameCode.CUT) {
					System.out.println(master.getHand(0).toString());
					System.out.println("選択肢 :" + master.getChoicable());
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
