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

package ca.uoguelph.socs.icc.edm.domain.core;

import ca.uoguelph.socs.icc.edm.domain.Element;
import ca.uoguelph.socs.icc.edm.domain.ElementBuilder;

import ca.uoguelph.socs.icc.edm.domain.builder.ElementFactory;

import ca.uoguelph.socs.icc.edm.domain.factory.QueryFactory;

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
	 * Common implementation of the <code>ElementFactory</code> interface.  This
	 * class provides the implementation of the <code>setId</code> method which
	 * should be common to all of the <code>ElementFactory</code> implementations.
	 *
	 * @param  <T> The interface type for the <code>Element</code>
	 */

	protected static abstract class Factory<T extends Element> implements ElementFactory<T>
	{
		/**
		 * Write the specified <code>DataStore</code> ID number into the
		 * <code>Element</code>.
		 *
		 * @param  element The <code>Element</code> to which the ID number is
		 *                 assigned, not null
		 * @param  id      The ID number assigned to the <code>Element</code>
		 */

		@Override
		public final void setId (final T element, final Long id)
		{
			assert element instanceof AbstractElement : "element is not an instance of AbstractElement";

			((AbstractElement) element).setId (id);
		}
	}

	/**
	 * Register an <code>Element</code> implementation class with the
	 * <code>QueryFactory</code>.
	 *
	 * @param  <T>  The interface type of the <code>Element</code>
	 * @param  <X>  The implementation type of the <code>Element</code>
	 * @param  type The <code>Element</code> interface class, not null
	 * @param  impl The <code>Element</code> implementation class, not null
	 */

	protected static <T extends Element, X extends T> void registerQuery (final Class<T> type, final Class<X> impl)
	{
		assert type != null : "type is NULL";
		assert impl != null : "impl is NULL";

		(QueryFactory.getInstance (type)).registerClass (impl);
	}

	/**
	 * Register the association between an <code>Element</code> implementation and
	 * the appropriate <code>ElementBuilder</code> implementation with the
	 * <code>ElementBuilder</code> factory.
	 *
	 * @param  <T>     The interface type of the <code>Element</code>
	 * @param  <X>     The implementation type of the <code>Element</code>
	 * @param  impl    The implementation class, not null
	 * @param  builder The <code>ElementBuilder</code> implementation class, not
	 *                 null
	 */

	protected static <T extends Element, X extends T> void registerBuilder (final Class<X> impl, final Class<? extends ElementBuilder<T>> builder)
	{
		assert impl != null : "impl is NULL";
		assert builder != null : "builder is NULL";
	}

	/**
	 * Register an <code>ElementFactory</code> for an <code>Element</code>
	 * implementation so that it can be used by the <code>ElementBuilder</code>
	 * instances.
	 *
	 * @param  <T>     The interface type of the <code>Element</code>
	 * @param  <X>     The implementation type of the <code>Element</code>
	 * @param  impl    The implementation class, not null
	 * @param  factory The <code>ElementFactory</code> to register, not null
	 */

	protected static <T extends Element, X extends T> void registerFactory (final Class<X> impl, final ElementFactory<T> factory)
	{
		assert impl != null : "impl is NULL";
		assert factory != null : "factory is NULL";
	}

	/**
	 * Register an <code>Element</code> implementation with the factories.
	 *
	 * @param  type    The <code>Element</code> interface class, not null
	 * @param  impl    The <code>Element</code> implementation class, not null
	 * @param  builder The <code>ElementBuilder</code> implementation, not null
	 * @param  factory The <code>ElementFactory</code>, instance not null
	 */

	protected static <T extends Element, X extends T> void registerElement (final Class<T> type, final Class<X> impl, final Class<? extends ElementBuilder<T>> builder, final ElementFactory<T> factory)
	{
		assert type != null : "type is NULL";
		assert impl != null : "impl is NULL";
		assert builder != null : "builder is NULL";
		assert factory != null : "factory is NULL";

		registerBuilder (impl, builder);
		registerFactory (impl, factory);
		registerQuery (type, impl);
	}

	/**
	 * Set the <code>DataStore</code> identifier.  This method is intended to be
	 * used by a <code>DataStore</code> when the <code>Element</code> instance is
	 * loaded, or by the <code>ElementBuilder</code> implementation to set the
	 * <code>DataStore</code> identifier, prior to storing a new <code>Element</code>
	 * instance.
	 * <p>
	 * <code>Element</code> implementations which are dependent on other
	 * <code>Element</code> interfaces for their <code>DataStore</code> identifier
	 * should throw an <code>UnsupportedOperationException</code> when this method
	 * is called.
	 *
	 * @param  id The <code>DataStore</code> identifier, not null
	 */

	protected abstract void setId (Long id);
}
