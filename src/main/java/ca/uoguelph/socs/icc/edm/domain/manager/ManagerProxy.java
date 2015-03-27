/* Copyright (C) 2015 James E. Stark
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package ca.uoguelph.socs.icc.edm.domain.manager;

import ca.uoguelph.socs.icc.edm.domain.Element;
import ca.uoguelph.socs.icc.edm.domain.ElementManager;

/**
 * Proxy for the <code>ElementBuilder</code> implementations to access some
 * protected methods on the <code>ElementManager</code> implementations.
 *
 * @author  James E. Stark
 * @version 1.0
 * @param   <T>  The type of <code>Element</code> to be processed
 */

public final class ManagerProxy<T extends Element>
{
	/** The <code>ElementManager</code> to which this class is a proxy */
	private final AbstractManager<T> manager;

	/** The <code>Element</code> implementation class to be created */
	private final Class<? extends Element> implClass;

	/** Addition parameters to the <code>ElementBuilder</code> instances */
	private final Element argument;

	/**
	 * Create the <code>ManagerProxy</code>.
	 *
	 * @param  manager   The <code>ElementManager</code> to operate on
	 * @param  implClass <code>Element</code> The implementation class
	 */

	protected ManagerProxy (final AbstractManager<T> manager, final Class<? extends Element> implClass)
	{
		assert manager != null : "manager is NULL";
		assert implClass != null : "implClass is NULL";

		this.manager = manager;
		this.implClass = implClass;
		this.argument = null;
	}

	/**
	 * Create the <code>ManagerProxy</code>, specifying an argument to the
	 * <code>ElementBuilder</code>.
	 *
	 * @param  manager   The <code>ElementManager</code> to operate on
	 * @param  implClass <code>Element</code> The implementation class
	 * @param  argument  The argument.
	 */

	protected ManagerProxy (final AbstractManager<T> manager, final Class<? extends Element> implClass, final Element argument)
	{
		assert manager != null : "manager is NULL";
		assert argument != null : "argument is NULL";
		assert implClass != null : "implClass is NULL";

		this.manager = manager;
		this.argument = argument;
		this.implClass = implClass;
	}

	/**
	 * Get the <code>Element</code> implementation class used by the
	 * <code>ElementManager</code> and the </code>ElementBuilder</code>.
	 *
	 * @return The <code>Element</code> implementation class
	 */

	public Class<? extends Element> getImplClass ()
	{
		return this.implClass;
	}

	/**
	 * Get the argument.
	 *
	 * @return The argument
	 */

	public Element getArgument ()
	{
		return this.argument;
	}

	/**
	 * Test an instance of an <code>Element</code> to determine if a reference
	 * to that <code>Element</code> instance exists in the
	 * <code>DataStore</code>.
	 *
	 * @param  element The <code>Element</code> instance to test, not null
	 *
	 * @return          <code>True</code> if the <code>DataStore</code>
	 *                  instance contains a reference to the
	 *                  <code>Element</code>, <code>False</code> otherwise
	 */

	public boolean contains (final T element)
	{
		assert element != null : "element is NULL";

		return this.manager.contains (element);
	}

	/**
	 * Retrieve an <code>Element</code> from the <code>DataStore</code> which
	 * identifies the same as the specified <code>Element</code>.
	 *
	 * @param  element The <code>Element</code> to retrieve, not null
	 *
	 * @return         A reference to the <code>Element</code> in the
	 *                 <code>DataStore</code>, may be null
	 */

	public T fetch (final T element)
	{
		assert element != null : "element is NULL";

		return this.manager.fetch (element);
	}

	/**
	 * Get an instance of the specified <code>ElementManager</code>.
	 *
	 * @param  <E>     The type of <code>Element</code> operated on by the
	 *                   <code>ElementManager</code>
	 * @param  <M>     The type of <code>ElementManager</code> to be returned
	 * @param  element The <code>Element</code> interface class, not null
	 * @param  manager The <code>ElementManager</code> interface class, not null
	 *
	 * @return         An instance of the requested <code>ElementManager</code>
	 */

	public <M extends ElementManager<E>, E extends Element> M getManager (final Class<E> element, final Class<M> manager)
	{
		return this.manager.getManager (element, manager);
	}

	/**
	 * Get the next available DataStore ID number.  The number will be chosen
	 * by the IdGenerator algorithm set in the <code>DataStoreProfile</code>
	 *
	 * @return A <code>Long</code> containing the ID number
	 */

	public Long nextId ()
	{
		return this.manager.nextId (this.implClass);
	}

	/**
	 * Insert the specified <code>Element</code> into the
	 * <code>DataStore</code>.  This method is intended to be used by the
	 * <code>ElementBuilder</code> instances to add new <code>Element</code>
	 * instances to the <code>DataStore</code>
	 *
	 * @param  element The <code>Element</code> to insert
	 *
	 * @return         A reference to the <code>Element</code>
	 */

	public T insertElement (final T element)
	{
		return this.manager.insertElement (element);
	}
}
