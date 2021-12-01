package logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import common.objects.HandBlocksObject;
import constants.PaisStatus;
import constants.RuleConfig;

public class HandLogic {

	private Map<Integer, Integer> tehaiMap;
	private final int MAX_PAI_NUM;
	private List<HandBlocksObject> handBlocksObjectList;

	public HandLogic() {
		tehaiMap = new HashMap<>();
		MAX_PAI_NUM = RuleConfig.getInstance().maxPai();
	}

	public int getShantensu(List<Integer> tehai, List<?> furo) {
		convertTehaiListToTehaiMap(tehai);
		int shanten = 8;

		// 七対子・国士のシャンテン数は副露がない場合のみ算出
		if(furo == null) {
			shanten = getChitoiShatensu();
			int kokushiShanten = getKokushiShantensu();
			shanten = kokushiShanten < shanten ? kokushiShanten : shanten;
		}
		int normalShanten = getNormalShantensu();
		return shanten < normalShanten ? shanten : normalShanten;
	}

	private void convertTehaiListToTehaiMap(List<Integer> tehai) {
		tehaiMap.clear();
		for (int i = 1; i <= MAX_PAI_NUM; i++) {
			tehaiMap.put(i,0);
		}
		for(Integer pai : tehai) {
			tehaiMap.put(pai, tehaiMap.get(pai) + 1);
		}
	}

	private int getKokushiShantensu() {
		// ヤオチュウ牌
		int[] yaochuhai = {1,9,11,19,21,29,31,32,33,34,35,36,37};
		// シャンテン数 ヤオチュウ牌0枚なら13シャンテン
		int shantensu = 13;
		// ヤオチュウ牌のトイツがあればシャンテンが1つ増える
		boolean containsToitsu = false;
		for (int pai : yaochuhai) {
			int num = tehaiMap.get(pai);
			if (1 <= num) {
				shantensu--;
				if (2 <= num) {
					containsToitsu = true;
				}
			}
		}
		if (containsToitsu) {
			shantensu--;
		}
		return shantensu;
	}

	private int getChitoiShatensu() {
		int shantensu = 6;
		for (int pai : tehaiMap.keySet()) {
			int num = tehaiMap.get(pai);
			if (2 <= num) {
				shantensu--;
			}
			// 4枚使い七対子(TODO 一旦ありで実装、ルールで変えられるように後でする想定)
			if(4 <= num) {
				shantensu--;
			}
		}
		return shantensu;
	}


	private int getNormalShantensu() {
		int shantensu = 8;
		int[] paiArray = new int[MAX_PAI_NUM + 1];
		for (int i = 1; i <= MAX_PAI_NUM; i++) {
			paiArray[i] = tehaiMap.get(i);
		}
		// 萬子
		List<HandBlocksObject> manzuArray = cutMentsu(paiArray, 1, 1, 9);
		List<HandBlocksObject> manzuHighArray = getHighScoreList(manzuArray);

		// 筒子
		List<HandBlocksObject> pinzuArray = cutMentsu(paiArray, 11, 11, 19);
		List<HandBlocksObject> pinzuHighArray = getHighScoreList(pinzuArray);

		// 索子
		List<HandBlocksObject> souzuArray = cutMentsu(paiArray, 21, 21, 29);
		List<HandBlocksObject> souzuHighArray = getHighScoreList(souzuArray);

		// 字牌
		List<HandBlocksObject> jihaiArray = cutMentsu(paiArray, 31, 31, 37);
		List<HandBlocksObject> jihaiHighArray = getHighScoreList(jihaiArray);

		manzuHighArray.add(new HandBlocksObject());
		pinzuHighArray.add(new HandBlocksObject());
		souzuHighArray.add(new HandBlocksObject());
		jihaiHighArray.add(new HandBlocksObject());

		handBlocksObjectList = new ArrayList<>();
		// ロジックがよくわからなかったので全結合でシャンテン数算出
		for (HandBlocksObject manzuObj : manzuHighArray) {
			for(HandBlocksObject pinzuObj : pinzuHighArray) {
				for(HandBlocksObject souzuObj : souzuHighArray) {
					for(HandBlocksObject jihaiObj : jihaiHighArray) {
						boolean containsAtama = manzuObj.isContainsToitsu() || pinzuObj.isContainsToitsu()
								|| souzuObj.isContainsToitsu() || jihaiObj.isContainsToitsu();
						int blockNum = manzuObj.getBlockNum() + pinzuObj.getBlockNum()
								+ souzuObj.getBlockNum() + jihaiObj.getBlockNum();
						if (4 < blockNum) {
							blockNum = containsAtama ? 5 : 4;
						}
						int MentsuNum = manzuObj.getMentsuNum() + pinzuObj.getMentsuNum()
						+ souzuObj.getMentsuNum() + jihaiObj.getMentsuNum();
						int tempShantensu = 8 - blockNum - MentsuNum;

						// シャンテン数最高の時の牌姿は保存しておく。
						if (tempShantensu <= shantensu) {
							if (tempShantensu < shantensu) {
								handBlocksObjectList.clear();
							}
								handBlocksObjectList.add(manzuObj.merge(pinzuObj).merge(souzuObj).merge(jihaiObj));
						}

						shantensu = tempShantensu < shantensu ? tempShantensu : shantensu;
					}
				}
			}
		}

		return shantensu;
	}

	private List<HandBlocksObject> cutMentsu(int[] paiArray, int i, int startIndex, int endIndex) {
		List<HandBlocksObject> objectList = null;
		// 暗刻
		if(3 <= paiArray[i]) {
			paiArray[i] -= 3;
			objectList =
					mergeNormalShantenList(objectList,
							addPaisToNormalShantenObjectList(cutMentsu(paiArray, i, startIndex, endIndex)
									, PaisStatus.ANKO, i));
			paiArray[i] += 3;
		}

		// 順子
		if (i < 30 && 1 <= paiArray[i] &&
				1 <= paiArray[i+1] && 1<= paiArray[i+2]) {
			paiArray[i] -= 1;
			paiArray[i+1] -= 1;
			paiArray[i+2] -= 1;
			objectList =
					mergeNormalShantenList(objectList,
							addPaisToNormalShantenObjectList(cutMentsu(paiArray, i, startIndex, endIndex)
									, PaisStatus.SHUNTSU, i));
			paiArray[i] += 1;
			paiArray[i+1] += 1;
			paiArray[i+2] += 1;
		}
		if (i == endIndex) {
			objectList = mergeNormalShantenList(objectList, cutTaatsu(paiArray, startIndex, startIndex, endIndex));
		} else {
			objectList = mergeNormalShantenList(objectList, cutMentsu(paiArray, i+1, startIndex, endIndex));
		};
		return objectList;

	}

	private List<HandBlocksObject> cutTaatsu(int[] paiArray, int i, int startIndex, int endIndex) {
		List<HandBlocksObject> objectList = null;
		// 対子
		if(2 <= paiArray[i]) {
			paiArray[i] -= 2;
			objectList =
					mergeNormalShantenList(objectList,
							addPaisToNormalShantenObjectList(cutTaatsu(paiArray, i, startIndex, endIndex)
									, PaisStatus.TOITSU, i));
			paiArray[i] += 2;
		}

		// リャンメン
		if (startIndex < 30 && 2 <= i % 10 && i % 10 <= 8 &&
				1 <= paiArray[i] && 1 <= paiArray[i+1]) {
			paiArray[i] -= 1;
			paiArray[i+1] -= 1;
			objectList =
					mergeNormalShantenList(objectList,
							addPaisToNormalShantenObjectList(cutTaatsu(paiArray, i, startIndex, endIndex)
									, PaisStatus.RYANMEN, i));
			paiArray[i] += 1;
			paiArray[i+1] += 1;
		}

		// カンチャン
		if (startIndex < 30 && i % 10 <= 7 &&
				1 <= paiArray[i] && 1 <= paiArray[i+2]) {
			paiArray[i] -= 1;
			paiArray[i+2] -= 1;
			objectList =
					mergeNormalShantenList(objectList,
							addPaisToNormalShantenObjectList(cutTaatsu(paiArray, i, startIndex, endIndex)
									, PaisStatus.CANTCHAN, i));
			paiArray[i] += 1;
			paiArray[i+2] += 1;
		}

		// ペンチャン
		if (startIndex < 30 && (i % 10 == 1 || i % 10 == 8) &&
				1 <= paiArray[i] && 1 <= paiArray[i+1]) {
			paiArray[i] -= 1;
			paiArray[i+1] -= 1;
			objectList =
					mergeNormalShantenList(objectList,
							addPaisToNormalShantenObjectList(cutTaatsu(paiArray, i, startIndex, endIndex)
									, PaisStatus.PENCHAN, i));
			paiArray[i] += 1;
			paiArray[i+1] += 1;
		}

		if(i < endIndex) {
			objectList = mergeNormalShantenList(objectList, cutTaatsu(paiArray, i+1, startIndex, endIndex));
		}
		return objectList;
	}
	private List<HandBlocksObject> mergeNormalShantenList(
			List<HandBlocksObject> oldList, List<HandBlocksObject> newList) {
		if(newList == null) {
			return oldList;
		}
		else if(oldList == null) {
			return newList;
		}
		oldList.addAll(newList);
		return oldList;
	}

	private List<HandBlocksObject> addPaisToNormalShantenObjectList(
			List<HandBlocksObject> objectList, PaisStatus status, int paiNum) {
		if(objectList == null) {
			objectList = new ArrayList<>();
			objectList.add(new HandBlocksObject());
		}
		objectList.stream().forEach(o -> o.addPaisObject(status, paiNum));
		return objectList;
	}

	private List<HandBlocksObject> getHighScoreList(List<HandBlocksObject> objectList) {
		if(objectList == null || objectList.isEmpty()) {
			return new ArrayList<>();
		}
		int maxScore = objectList.stream().mapToInt(o -> o.getScore()).max().getAsInt();
		return objectList.stream().filter(o -> o.getScore() == maxScore).collect(Collectors.toList());

	}


}
