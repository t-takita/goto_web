package common.objects;

import java.util.ArrayList;
import java.util.List;

import utils.DispUtils;

/**
 * 自分の手
 */
public class Hand {

	private List<Integer> tehai;

	private List<Integer> nuki;

	public Hand(List<Integer> tehai, List<Integer> nuki) {
		this.tehai = tehai;
		this.nuki = nuki;
	}

	public Hand clone() {
		return new Hand(new ArrayList<>(tehai), new ArrayList<>(nuki));
	}

	public List<Integer> getTehai() {
		return tehai;
	}

	public List<Integer> getNuki() {
		return nuki;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(DispUtils.toStringPaiList(tehai));
		if(!nuki.isEmpty()) {
			builder.append(" nuki : ");
			builder.append(DispUtils.toStringPaiList(nuki));
		}
		return builder.toString();
	}

	//private List<Furo> furo;


}
