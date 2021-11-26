package gamemaster;

import java.util.ArrayList;
import java.util.List;

import gamemaster.object.PlayerStatus;

public class PlayerStatusManager {

	private List<PlayerStatus> statusList;

	private PlayerStatusManager() {
		statusList = new ArrayList<>();
	}

	protected PlayerStatusManager(int num) {
		this();
		for (int i=0; i<num; i++) {
			statusList.add(new PlayerStatus());
		}
	}

	protected void updatePlayerStatus(int num) {

	}
}
