package net.expvp.core.plugin.modules.economy;

import java.util.Comparator;

import net.expvp.api.interfaces.player.OfflinePlayerAccount;

public class AccountBalanceComparator implements Comparator<OfflinePlayerAccount> {

	@Override
	public int compare(OfflinePlayerAccount o1, OfflinePlayerAccount o2) {
		if (o1.getBalance() == o2.getBalance()) {
			return 0;
		} else if (o1.getBalance() > o2.getBalance()) {
			return -1;
		} else {
			return 1;
		}
	}

}
