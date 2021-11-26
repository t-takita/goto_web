package constants;

/**
 * ルールなど いったん定数クラスとして実装し後ほど外だしプロパティ化
 *
 */
public class RuleConfig {

	private static RuleConfig config = new RuleConfig();

	// 対局人数
	private int NUMBER_OF_PLAYER = 1;
	// 手牌枚数
	private int HAND_NUM = 13;

	// インストラクタはprivateにする。
	private RuleConfig() {}

	/**
	 * インスタンスを取得する
	 * @return インスタンス
	 */
	public static RuleConfig getInstance() {
		return config;
	}

	/**
	 * 対局人数を取得する
	 * @return 対局人数
	 */
	public int numberOfPlayer() {
		return NUMBER_OF_PLAYER;
	}
	/**
	 * 手牌枚数を取得する
	 * @return
	 */
	public int handNum() {
		return HAND_NUM;
	}

}
