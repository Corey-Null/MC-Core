package net.expvp.core.plugin.modules.serveraccess.data;

import java.util.Comparator;

public class BanDataComparator implements Comparator<BanData<?>> {

	@Override
	public int compare(BanData<?> o1, BanData<?> o2) {
		if (o1.getBegin() == o2.getBegin()) {
			return 0;
		} else if (o1.getBegin() > o2.getBegin()) {
			return -1;
		} else {
			return 1;
		}
	}

}
