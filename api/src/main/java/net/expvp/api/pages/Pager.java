package net.expvp.api.pages;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;

import net.expvp.api.interfaces.pages.IPage;
import net.expvp.api.interfaces.pages.IPager;

/**
 * Class to represent the default implemented version of IPager
 * 
 * @author NullUser
 * @see IPager<T>
 */
public class Pager<T> implements IPager<T> {

	private final static IPager<?> EMPTY_PAGER = new Pager<>();

	private final Collection<T> defaultCollection;
	private final int pageSize;
	private final List<IPage<T>> pages;

	private Pager() {
		this.defaultCollection = Lists.newArrayList();
		this.pageSize = 0;
		this.pages = Lists.newArrayList();
	}

	private Pager(Collection<T> defaultCollection, int pageSize) {
		this.defaultCollection = defaultCollection;
		this.pageSize = pageSize;
		this.pages = Lists.newArrayList();
		List<T> array = Lists.newArrayList(defaultCollection);
		while (!array.isEmpty()) {
			List<T> parsable = Lists.newArrayList();
			for (int i = 0; i < pageSize && i < array.size();) {
				parsable.add(array.get(0));
				array.remove(0);
			}
			pages.add(ImplPage.parse(parsable));
		}
	}

	/**
	 * @see IPager<T>
	 */
	@Override
	public Collection<T> getDefaultCollection() {
		return Collections.unmodifiableCollection(defaultCollection);
	}

	/**
	 * @see IPager<T>
	 */
	@Override
	public List<IPage<T>> getPages() {
		return Collections.unmodifiableList(pages);
	}

	/**
	 * @see IPager<T>
	 */
	@Override
	public IPage<T> getPage(int id) {
		if (getPageAmount() <= id) {
			return null;
		}
		return pages.get(id);
	}

	/**
	 * @see IPager<T>
	 */
	@Override
	public int getPageAmount() {
		return pages.size() == 0 ? 0 : pages.size();
	}

	/**
	 * @see IPager<T>
	 */
	@Override
	public int getItemPerPage() {
		return pageSize;
	}

	/**
	 * @param items
	 * @return pager version of the defined items
	 */
	public static final <T> IPager<T> parse(Collection<T> items) {
		return parse(items, 5);
	}

	/**
	 * @param items
	 * @param pageSize
	 * @return pager version of the defined items with the defined page size
	 */
	@SuppressWarnings("unchecked")
	public static final <T> IPager<T> parse(Collection<T> items, int pageSize) {
		if (items == null || items.isEmpty()) {
			return (IPager<T>) EMPTY_PAGER;
		}
		return new Pager<T>(items, pageSize);
	}

}
