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

package ca.uoguelph.socs.icc.edm.domain.element;

import ca.uoguelph.socs.icc.edm.domain.Element;
import ca.uoguelph.socs.icc.edm.domain.ElementBuilder;

import ca.uoguelph.socs.icc.edm.domain.builder.AbstractBuilder;

import ca.uoguelph.socs.icc.edm.domain.datastore.AbstractQuery;

import ca.uoguelph.socs.icc.edm.domain.element.metadata.Definition;

/**
 * Abstract base class for all of the domain model <code>Element</code>
 * implementations.  All classes implementing the <code>Element</code>
 * interface or one of its sub-interfaces must extend this class.  This class
 * provides the functionality for registering the <code>Element</code>
 * implementations during initialization, and core protected interface.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public abstract class AbstractElement implements Element
{
	/**
	 * Register an <code>Element</code> implementation class with the
	 * <code>QueryFactory</code>.
	 *
	 * @param  <T>         The interface type of the <code>Element</code>
	 * @param  <U>         The implementation type of the <code>Element</code>
	 * @param  element     The <code>Element</code> interface class, not null
	 * @param  elementImpl The <code>Element</code> implementation class, not
	 *                     null
	 */

	protected static <T extends Element, U extends T> void registerQuery (final Class<T> element, final Class<U> elementImpl)
	{
		assert element != null : "element is NULL";
		assert elementImpl != null : "elementImpl is NULL";

		AbstractQuery.registerElement (element, elementImpl);
	}

	/**
	 * Register the association between an <code>Element</code> implementation and
	 * the appropriate <code>ElementBuilder</code> implementation with the
	 * <code>ElementBuilder</code> factory.
	 *
	 * @param  <T>         The interface type of the <code>Element</code>
	 * @param  <U>         The implementation type of the <code>Element</code>
	 * @param  element     The <code>Element</code> interface class, not null
	 * @param  elementImpl The <code>Element</code> implementation class, not
	 *                     null
	 * @param  builder     The <code>ElementBuilder</code> implementation
	 *                     class, not null
	 */

	protected static <T extends ElementBuilder<U>, U extends Element> void registerBuilder (final Class<U> element, final Class<? extends U> elementImpl, final Class<T> builder)
	{
		assert element != null : "element is NULL";
		assert elementImpl != null : "elementImpl is NULL";
		assert builder != null : "builder is NULL";

		AbstractBuilder.registerElement (elementImpl, builder);
	}

	/**
	 * Register an <code>Element</code> implementation with the factories.
	 *
	 * @param  <T>        The <code>Element</code> interface type
	 * @param  <U>        The <code>Element</code> implementation type
	 * @param  <B>        The <code>ElementBuilder</code> implementation type
	 * @param  definition The <code>Element</code> meta-data definition
	 * @param  builder    The <code>ElementBuilder</code> implementation, not
	 *                    null
	 */

	protected static <T extends Element, U extends T, B extends ElementBuilder<T>> void registerElement (final Definition<T, U> definition, final Class<B> builder)
	{
		assert builder != null : "builder is NULL";
		assert definition != null : "definition is NULL";

//		registerQuery (element, elementImpl);
		registerBuilder (definition.getInterfaceClass (), definition.getImplementationClass (), builder);
	}

	/**
	 * Set the <code>DataStore</code> identifier.  This method is intended to
	 * be used by a <code>DataStore</code> when the <code>Element</code>
	 * instance is loaded, or by the <code>ElementBuilder</code> implementation
	 * to set the <code>DataStore</code> identifier, prior to storing a new
	 * <code>Element</code> instance.
	 * <p>
	 * <code>Element</code> implementations which are dependent on other
	 * <code>Element</code> interfaces for their <code>DataStore</code>
	 * identifier should throw an <code>UnsupportedOperationException</code>
	 * when this method is called.
	 *
	 * @param  id The <code>DataStore</code> identifier, not null
	 */

	protected abstract void setId (Long id);

	/**
	 * Determine if two <code>Element</code> instances are identical.  This
	 * method is a default implementation which calls the equals method, and is
	 * suitable for those <code>Element</code> implementations for which all of
	 * the fields must be unique.
	 *
	 * @param  element The <code>Element</code> to compare to the current
	 *                 instance
	 *
	 * @return         <code>True</code> if the <code>Element</code> instances
	 *                 are logically identical, <code>False</code> otherwise
	 */

	public boolean identicalTo (final Element element)
	{
		return this.equals (element);
	}
}
