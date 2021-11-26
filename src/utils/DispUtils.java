package utils;

import java.util.List;
import java.util.StringJoiner;

public class DispUtils {

	private static String[] paiName = {
			"", "１萬","２萬","３萬","４萬","５萬","６萬","７萬","８萬","９萬",
			"", "１筒","２筒","３筒","４筒","５筒","６筒","７筒","８筒","９筒",
			"", "１索","２索","３索","４索","５索","６索","７索","８索","９索",
			"", "風東","風南","風西","風北","元白","元發","元中","","",
			"", "花春","花夏","花秋","花冬"
			};
	/**
	 * 牌番号を牌の名前に直す
	 * TODO ちゃんと書く
	 * @param i 牌番号
	 * @return 牌の名前
	 */
	public static String toStringPai(Integer i) {

		// nullチェック
		if (i == null) {
			throw new NullPointerException("牌番号がnullです。");
		}

		if (i < 0 || paiName.length < i) {
			throw new IllegalArgumentException("牌番号が不正です。");
		}

		return paiName[i];
	}

	public static String toStringPaiList(List<Integer> paiList) {
		StringJoiner joiner = new StringJoiner(",");
		for(Integer pai : paiList) {
			joiner.add(DispUtils.toStringPai(pai));
		}
		return joiner.toString();
	}
}
