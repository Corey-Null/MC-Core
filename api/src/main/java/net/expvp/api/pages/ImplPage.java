package net.expvp.api.pages;

import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;

import net.expvp.api.interfaces.pages.IPage;

/**
 * Class to represent the default implemented version of IPage
 * 
 * @author NullUser
 * @see IPage<T>
 */
public class ImplPage<T> implements IPage<T> {

	public final static IPage<?> EMPTY_PAGE = new ImplPage<>();

	private final List<T> items;

	private ImplPage() {
		items = Lists.newArrayList();
	}

	private ImplPage(List<T> items) {
		this.items = items;
	}

	/**
	 * @see IPage<T>
	 */
	@Override
	public int getSize() {
		return items.size();
	}

	/**
	 * @see IPage<T>
	 */
	@Override
	public List<T> getPageItems() {
		return Collections.unmodifiableList(items);
	}

	/**
	 * @see IPage<T>
	 */
	@Override
	public T getItem(int id) {
		if (id >= getSize()) {
			return null;
		}
		return items.get(id);
	}

	/**
	 * @param items
	 * @return the paged version of the list of items
	 */
	@SuppressWarnings("unchecked")
	public static final <T> IPage<T> parse(List<T> items) {
		if (items == null || items.isEmpty()) {
			return (ImplPage<T>) EMPTY_PAGE;
		}
		return new ImplPage<T>(items);
	}

}
