package net.expvp.api.interfaces.pages;

import java.util.Collection;
import java.util.List;

/**
 * Interface used for handling lists which need pages
 * 
 * @author NullUser
 *
 * @param <T>
 *            To define the type reference of the pages
 */
public interface IPager<T> {

	/**
	 * @return the default collection
	 */
	Collection<T> getDefaultCollection();

	/**
	 * @return the pages
	 */
	List<IPage<T>> getPages();

	/**
	 * @param id
	 * @return the page defined by the ID
	 */
	IPage<T> getPage(int id);

	/**
	 * @return the amount of pages in the pager
	 */
	int getPageAmount();

	/**
	 * @return the amount of items per page
	 */
	int getItemPerPage();

}
