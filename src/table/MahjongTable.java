package table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;

import common.objects.Hand;
import constants.RuleConfig;
import utils.DispUtils;

/**
 * マージャン卓クラス
 *
 */
public class MahjongTable {

	private List<Integer> yama;
	private List<List<Integer>> tehais;
	private List<List<Integer>> nukis;
	private int yamaPointer = 0;

	public MahjongTable() {
		// 牌情報の初期化
		yama = this.initPais();

		// 手牌、抜牌情報の初期化
		tehais = new ArrayList<>();
		nukis = new ArrayList<>();
		// 人数分の手牌リスト、抜き牌リストを追加する。
		for(int i = 0; i < RuleConfig.getInstance().numberOfPlayer(); i++) {
			tehais.add(new ArrayList<Integer>());
			nukis.add(new ArrayList<Integer>());
		}
	}

	/**
	 * 局を開始する
	 */
	public void startKyoku() {
		// 山情報の初期化
		Collections.shuffle(yama);
		yamaPointer = 0;

		// 配牌作成
		for (List<Integer> tehai : tehais) {
			tehai.clear();
			for (int i = 0; i < RuleConfig.getInstance().handNum(); i++) {
				tehai.add(yama.get(yamaPointer++));
			}
			Collections.sort(tehai);
		}

		for (List<Integer> nuki : nukis) {
			nuki.clear();
		}
	}

	/**
	 * 手牌を取得する
	 * @param i プレイヤー番号
	 * @return 手牌
	 */
	public List<Integer> getTehai(int playerNum) {
		return tehais.get(playerNum);
	}

	public Hand getHand(int playerNum) {
		return new Hand(tehais.get(playerNum), nukis.get(playerNum));
	}

	public void tsumo(int playerNum) {
		tehais.get(playerNum).add(yama.get(yamaPointer++));
	}

	public void cut(int playerNum, int i) {
		tehais.get(playerNum).remove(i);
		Collections.sort(tehais.get(playerNum));
	}

	public void nuki(int playerNum, int i) {
		Integer nukipai = tehais.get(playerNum).remove(i);
		nukis.get(playerNum).add(nukipai);
		Collections.sort(tehais.get(playerNum));
	}

	/**
	 * 牌情報の初期化
	 * @return 初期化した牌一覧リスト
	 */
	private List<Integer> initPais() {
		List<Integer> pais = new ArrayList<>();

		for(int i = 0; i < 4; i++) {
			// 萬子
			pais.add(1);
			pais.add(9);

			// 筒子・索子
			for(int j = 0; j < 9; j++) {
				pais.add(11 + j);
				pais.add(21 + j);
			}

			// 字牌
			for(int j = 0; j < 7; j++) {
				pais.add(31 + j);
			}

			// 花牌
			pais.add(41 + i);
		}

		return pais;
	}

	public void dispYama() {
		StringJoiner joiner = new StringJoiner(",");
		for (Integer pai : yama) {
			joiner.add(DispUtils.toStringPai(pai));
		}
		System.out.println(joiner);
	}
}
