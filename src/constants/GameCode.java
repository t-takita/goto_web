package constants;

/**
 * ゲームの状況
 * 適当にコード書いておく
 */
public enum GameCode {

	/** 対局開始前 **/
	BEFORE_GAME_START,
	/** ゲーム開始 **/
	GAME_START,
	/** 局開始 **/
	KYOKU_START,
	/** ツモ番 **/
	TSUMO,
	/** 切り番 **/
	CUT,
	/** ツモ番前(鳴き等の待機状態) **/
	TSUMO_WAIT,
	/** 局終了 **/
	KYOKU_END,
	/** 対局終了 **/
	GAME_END,

}
