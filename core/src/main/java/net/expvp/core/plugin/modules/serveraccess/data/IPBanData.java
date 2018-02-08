package net.expvp.core.plugin.modules.serveraccess.data;

import net.expvp.api.interfaces.ip.IAddress;

/**
 * Ban data for handling IP bans
 * 
 * @author NullUser
 * @see BanData<T>
 */
public class IPBanData extends BanData<IAddress> {

	public IPBanData(IAddress user, BanType type) {
		super(user, type);
	}

}
