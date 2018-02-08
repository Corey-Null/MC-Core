package net.expvp.api.interfaces.pages;

import java.util.List;

/**
 * Class used for handling a single page from a pager
 * 
 * @author NullUser
 *
 * @param <T>
 *            To define the type reference of the page
 */
public interface IPage<T> {

	/**
	 * @return the size of the page
	 */
	int getSize();

	/**
	 * @return all of the items inside the page
	 */
	List<T> getPageItems();

	/**
	 * @param id
	 * @return a specific item based off the id
	 */
	T getItem(int id);

}
