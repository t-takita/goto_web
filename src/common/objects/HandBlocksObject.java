package common.objects;

import java.util.ArrayList;
import java.util.List;

import constants.PaisStatus;

public class HandBlocksObject {

	private List<BlockObject> blockObjectList = null;
	private boolean isContainsToitsu = false;

	public HandBlocksObject() {}


	public void addPaisObject(PaisStatus status, int paiNum) {
		if(blockObjectList == null) {
			blockObjectList = new ArrayList<>();
		}
		if (PaisStatus.TOITSU == status) {
			isContainsToitsu = true;
		}
		blockObjectList.add(new BlockObject(status, paiNum));
	}

	public int getScore() {
		if (blockObjectList == null) {
			return 0;
		}
		int mentsu = getMentsuNum();
		int taatsu = getTaatsuNum();
		return mentsu * 2 + taatsu;
	}

	public int getMentsuNum() {
		if (blockObjectList == null) {
			return 0;
		}
		int mentsu = 0;
		for (BlockObject obj : blockObjectList) {
			if(PaisStatus.ANKO == obj.getStatus() || PaisStatus.SHUNTSU == obj.getStatus()) {
				mentsu++;
			}
		}
		return mentsu;
	}

	public int getTaatsuNum() {
		if (blockObjectList == null) {
			return 0;
		}
		int taatsu = 0;
		for (BlockObject obj : blockObjectList) {
			if(PaisStatus.ANKO != obj.getStatus() && PaisStatus.SHUNTSU != obj.getStatus()) {
				taatsu++;
			}
		}
		return taatsu;
	}

	public int getBlockNum() {
		if (blockObjectList == null) {
			return 0;
		}
		return blockObjectList.size();
	}

	public boolean isContainsToitsu() {
		return isContainsToitsu;
	}

	/**
	 * インスタンス同士を結合する。ディープコピーするためメモリ負荷を考慮して使用すること。
	 * @param mergeObj
	 * @return
	 */
	public HandBlocksObject merge(HandBlocksObject mergeObj) {
		HandBlocksObject obj = new HandBlocksObject();
		if (this.blockObjectList == null) {
			if (mergeObj.blockObjectList == null) {
				obj.blockObjectList = new ArrayList<>();
			} else {
				obj.blockObjectList = new ArrayList<>(mergeObj.blockObjectList);
			}
		} else {
			if (mergeObj.blockObjectList == null) {
				obj.blockObjectList = new ArrayList<>(this.blockObjectList);
			} else {
				List<BlockObject> blockObjectList = new ArrayList<>(this.blockObjectList);
				blockObjectList.addAll(mergeObj.blockObjectList);
			}
		}
		obj.isContainsToitsu = this.isContainsToitsu || mergeObj.isContainsToitsu;
		return obj;
	}

}
