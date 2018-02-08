package net.expvp.core.plugin.modules.serveraccess.data;

import java.util.UUID;

/**
 * Class used to store ban data
 * 
 * @author NullUser
 * @param <T>
 *            The type of user
 */
public abstract class BanData<T> {

	private final T user;
	private final BanType type;
	private UUID hander;
	private String reason;
	private long begin;
	private long expire;
	private boolean expired;

	public BanData(T user, BanType type) {
		this.user = user;
		this.type = type;
		this.hander = null;
		this.reason = null;
		this.begin = -1;
		this.expire = -1;
		this.expired = false;
	}

	/**
	 * @return the begin date
	 */
	public long getBegin() {
		return begin;
	}

	/**
	 * @return the expire date
	 */
	public long getExpire() {
		return expire;
	}

	/**
	 * @return the hander
	 */
	public UUID getHander() {
		return hander;
	}

	/**
	 * @return the user
	 */
	public T getUser() {
		return user;
	}

	/**
	 * @return the reason
	 */
	public String getReason() {
		return reason;
	}

	/**
	 * @return the type
	 */
	public BanType getType() {
		return type;
	}

	/**
	 * @return true if the ban is expired
	 */
	public boolean isExpired() {
		return expired;
	}

	/**
	 * @param begin
	 *            To set the begin time
	 * @return this
	 */
	public BanData<T> setBegin(long begin) {
		this.begin = begin;
		return this;
	}

	/**
	 * @param expire
	 *            To set the expire time
	 * @return this
	 */
	public BanData<T> setExpire(long expire) {
		this.expire = expire;
		return this;
	}

	/**
	 * @param expired
	 *            To set the expired
	 * @return this
	 */
	public BanData<T> setExpired(boolean expired) {
		this.expired = expired;
		return this;
	}

	/**
	 * @param hander
	 *            To set the hander
	 * @return this
	 */
	public BanData<T> setHander(UUID hander) {
		this.hander = hander;
		return this;
	}

	/**
	 * @param reason
	 *            To set the reason
	 * @return this
	 */
	public BanData<T> setReason(String reason) {
		this.reason = reason;
		return this;
	}

}
