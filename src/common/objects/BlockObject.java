package common.objects;

import constants.PaisStatus;

public class BlockObject {

	private PaisStatus status;
	private int paiNum;

	public BlockObject(PaisStatus status, int paiNum) {
		this.status = status;
		this.paiNum = paiNum;
	}

	public PaisStatus getStatus() {
		return status;
	}

	public int getPaiNum() {
		return paiNum;
	}

}
