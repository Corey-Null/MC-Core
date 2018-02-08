package net.expvp.core.plugin.modules.serveraccess.data;

import java.util.UUID;

/**
 * BanData used for handling UUID bans
 * 
 * @author NullUser
 * @see BanData<T>
 */
public class UUIDBanData extends BanData<UUID> {

	public UUIDBanData(UUID user, BanType type) {
		super(user, type);
	}

}
