package gamemaster;

import java.util.ArrayList;
import java.util.List;

import common.objects.Hand;
import constants.GameCode;
import logic.HandLogic;
import rules.RuleJudger;
import table.MahjongTable;

/**
 * ゲームマスター。半荘ごとに生成され、局を監視する
 */
public class GameMaster {

	private MahjongTable table;
	private GameCode code;
	private List<String> choiceable;

	/**
	 * コンストラクタ
	 */
	public GameMaster() {
		table = new MahjongTable();
		code = GameCode.BEFORE_GAME_START;
		choiceable = new ArrayList<>();
	}

	/**
	 * 対局を開始する
	 */
	public void start() {
		code = GameCode.GAME_START;
	}

	/**
	 * 実行クラス
	 */
	public void doProcess() {
		if(code == GameCode.GAME_START) {
			startKyoku();
		} else if(code == GameCode.KYOKU_START) {
			code = GameCode.TSUMO;
		} else if(code == GameCode.TSUMO) {
			// 仮実装
			table.tsumo(0);
			setChoiceableInTsumo(0);
			code = GameCode.CUT;
		} else if(code == GameCode.TSUMO_WAIT) {
			code = GameCode.TSUMO;
		}
	}

	/**
	 * 局を開始する
	 */
	private void startKyoku() {
		table.startKyoku();
		code = GameCode.KYOKU_START;
	}

	/**
	 * 選択可能な手の設定
	 * @param playerNum プレイヤー番号
	 */
	private void setChoiceableInTsumo(int playerNum) {
		choiceable.clear();
		Hand hand = getHand(playerNum);
		List<Integer> tehai = hand.getTehai();
		for (int i = 0; i < tehai.size(); i++) {
			if (RuleJudger.isCutable(tehai.get(i))) {
				choiceable.add("cut" + i);
			}
			if (RuleJudger.isNukiable(tehai.get(i))) {
				choiceable.add("nuki" + i);
			}
		}
		HandLogic logic = new HandLogic();
		int shantensu = logic.getShantensu(tehai, null);
		if (shantensu <= 0) {
			//TODO 何切るかまでちゃんと実装する。
			choiceable.add("reach");
		}
		if (shantensu == -1) {
			choiceable.add("tsumo");
		}
	}

	/**
	 * 手牌情報を渡す
	 * @param playerNum プレイヤー番号
	 * @return 手牌のディープコピー
	 */
	public List<Integer> getTehai(int playerNum) {
		return new ArrayList<>(table.getTehai(playerNum));
	}

	public Hand getHand(int playerNum) {
		return table.getHand(playerNum).clone();
	}

	public GameCode getGameCode() {
		return code;
	}

	public List<String> getChoicable() {
		return choiceable;
	}



	public boolean order(String str, int playerNum) {
		// 切る
		if (str.startsWith("cut")) {
			int paiNum = Integer.parseInt(str.replace("cut", ""));
			if(RuleJudger.isCutable(getTehai(playerNum).get(paiNum))) {
				table.cut(playerNum, paiNum);
				code = GameCode.TSUMO_WAIT;
				return true;
			}
			return false;
		}

		// 抜き
		else if (str.startsWith("nuki")) {
			int paiNum = Integer.parseInt(str.replace("nuki", ""));
			if(RuleJudger.isNukiable(getTehai(playerNum).get(paiNum))) {
				table.nuki(playerNum, paiNum);
				code = GameCode.TSUMO;
				return true;
			}
			return false;
		}

		// 立直
		else if (str.startsWith("reach")) {

		}

		// ツモ
		else if (str.startsWith("tsumo")) {
			return tsumoAgari(playerNum);
		}

		// カン
		else if (str.startsWith("kan")) {

		}


		return false;
	}

	private boolean tsumoAgari(int playerNum) {
		HandLogic logic = new HandLogic();
		Hand hand = getHand(playerNum);
		int shantensu = logic.getShantensu(hand.getTehai(), null);
		if (shantensu != -1) {
			System.err.println("あがってないのにツモあがり");
			// TODO なんかする
			return false;
		} else {
			// 点数計算などして終局処理
			System.out.println("あがりだよ");
			code = GameCode.KYOKU_END;
			return true;
		}
	}

}
