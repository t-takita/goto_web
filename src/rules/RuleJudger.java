package rules;

/**
 * ルールを守っているか確認する
 */
public class RuleJudger {

	public static boolean isNukiable(int pai) {
		// 北
		if(pai == 34) {
			return true;
		}
		// 花牌
		if(41 <= pai && pai <= 44) {
			return true;
		}
		return false;
	}

	public static boolean isCutable(int pai) {
		// 抜きドラは切れない
		return !isNukiable(pai);
	}
}
