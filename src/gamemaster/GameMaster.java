package gamemaster;

import java.util.ArrayList;
import java.util.List;

import common.objects.Hand;
import constants.GameCode;
import rules.RuleJudger;
import table.MahjongTable;

/**
 * ゲームマスター。半荘ごとに生成され、局を監視する
 */
public class GameMaster {

	private MahjongTable table;
	private GameCode code;

	/**
	 * コンストラクタ
	 */
	public GameMaster() {
		table = new MahjongTable();
		code = GameCode.BEFORE_GAME_START;
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

	public boolean order(String str, int playerNum) {
		boolean nukiFlg = str.startsWith("n");
		str = str.replace("n", "");
		for(int i = 0; i < getTehai(playerNum).size(); i++) {
			if(Integer.toString(i+1).equals(str)) {
				if (nukiFlg) {
					if(RuleJudger.isNukiable(getTehai(playerNum).get(i))) {
						table.nuki(playerNum, i);
						code = GameCode.TSUMO;
						return true;
					}
					return false;
				}
				if(RuleJudger.isCutable(getTehai(playerNum).get(i))) {
					table.cut(playerNum, i);
					code = GameCode.TSUMO_WAIT;
					return true;
				}
				return false;
			}
		}
		return false;
	}

}
