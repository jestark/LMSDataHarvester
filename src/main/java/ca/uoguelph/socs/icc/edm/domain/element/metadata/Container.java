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

package ca.uoguelph.socs.icc.edm.domain.element.metadata;

import java.util.Map;
import java.util.HashMap;

import ca.uoguelph.socs.icc.edm.domain.Element;

/**
 * Container for <code>MetaData</code> instances.  This class acts as a
 * work-around for the lack of Generic type reification in Java.  All of the
 * operation on the <code>DomainModel</code> are expressed in terms of the
 * <code>Element</code> interfaces.  Out of necessity, the operations in the
 * builders and the <code>DataStore</code> are performed in terms of the
 * <code>Element</code> implementations.  It is necessary to bridge between the
 * interface and implementation in terms of type parameters, to avoid a
 * proliferation of <code>Element</code >implementation specific
 * builder/loader/query classes and/or unchecked casts (both of which are less
 * than ideal).
 * <p>
 * The <code>MetaData</code> instances, by their nature, know the details of
 * both the <code>Element</code> interfaces and implementations.  To bridge
 * between the <code>Element</code> interface and implementation, an interface
 * for the operation is defined in terms of the <code>Element</code> interface,
 * taking the interface type as its type parameter.  The interface is then
 * implemented by a class that will know the implementation details of the
 * <code>Element</code>, usually via a second type parameter.  As a result the
 * implementation details of the <code>Element</code> only need to be known
 * when an instance of the operation class is created.
 * <p>
 * When on operation is created, the <code>MetaData</code> is used to provide
 * necessary information about the <code>Element</code> implementation,
 * including the necessary type parameter, which is not known to the caller.
 * The type parameter is supplied via a call from the <code>Container</code> to
 * class which implements the <code>Receiver</code> interface.  Generally,
 * classes that implement the <code>Receiver</code> interface will a builder
 * and will call the <code>inject</code> method as final build operation.
 * <p>
 * Ideally a functional interface would be used instead of requiring a class
 * for callback operation, however the functional interfaces do not work with
 * the unknown type parameters.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public final class Container
{
	/**
	 * Represents a function which injects a <code>MetaData</code> instance
	 * into the target entity.  Implementations of this interface  will receive
	 * a <code>MetaData</code> instance and the type parameter for the
	 * <code>Element</code> implementation represented by the
	 * </code>MetaData</code> instance.
	 *
	 * @param <T> The <code>Element</code> interface type
	 * @param <R> The result type of the <code>Receiver</code>
	 */

	public static interface Receiver<T extends Element, R>
	{
		/**
		 * Apply the <code>MetaData</code> instance to the given argument.
		 *
		 * @param <U>      The <code>Element</code> implementation type
		 * @param metadata The <code>MetaData</code> instance, not null
		 *
		 * @return         The return value of the receiving method
		 */

		public abstract <U extends T> R apply (MetaData<T, U> metadata);
	}

	/**
	 * Container for a single <code>MetaData</code> instance.  This interface
	 * acts to bury the <code>Element</code> implementation class, so that the
	 * callers can operate based on the interface type only.
	 *
	 * @param <T> The <code>Element</code> interface type
	 */

	private static interface Cell<T extends Element>
	{
		/**
		 * Inject the <code>MetaData</code> instance into the
		 * <code>Receiver</code>.
		 *
		 * @param  <R>      The result type of the <code>Receiver</code>
		 * @param  reciever The <code>Receiver</code>, not null
		 *
		 * @return          The return value of the receiving method
		 */

		public abstract <R> R inject (Receiver<T, R> reciever);
	}

	/**
	 * Implementation of the <code>Cell</code> interface with the
	 * <code>Element</code> implementation class specified.
	 *
	 * @param <T> The <code>Element</code> interface type
	 * @param <U> The <code>Element</code> implementation type
	 */

	private static final class CellImpl<T extends Element, U extends T> implements Cell<T>
	{
		/** The <code>MetaData</code> instance */
		private final MetaData<T, U> metadata;

		/**
		 * Create the <code>CellImpl</code> with the specified
		 * <code>MetaData</code> instance.
		 *
		 * @param  metadata The <code>MetaData</code> instance
		 */

		public CellImpl (final MetaData<T, U> metadata)
		{
			this.metadata = metadata;
		}

		/**
		 * Get the <code>MetaData</code> instance.
		 *
		 * @return The <code>MetaData</code> instance
		 */

		public MetaData<T, U> get ()
		{
			return this.metadata;
		}

		/**
		 * Inject the <code>MetaData</code> instance into the
		 * <code>Receiver</code>.
		 *
		 * @param  <R>      The result type of the <code>Receiver</code>
		 * @param  reciever The <code>Receiver</code>, not null
		 *
		 * @return          The return value of the receiving method
		 */

		@Override
		public <R> R inject (final Receiver<T, R> receiver)
		{
			assert receiver != null : "receiver is NULL";

			return receiver.apply (this.metadata);
		}
	}

	/** <code>Element</code> class to <code>MetaData</code> instance mapping */
	private final Map<Class<? extends Element>, CellImpl<? extends Element, ? extends Element>> metadata;

	/**
	 * Create the <code>Container</code>
	 */

	protected Container ()
	{
		this.metadata = new HashMap<> ();
	}

	/**
	 * Insert a <code>MetaData</code> instance into the <code>Container</code>.
	 *
	 * @param  <T>      The <code>Element</code> interface type
	 * @param  <U>      The <code>Element</code> implementation type
	 * @param  metadata The <code>MetaData</code> instance, not null
	 */

	protected <T extends Element, U extends T> void put (final MetaData<T, U> metadata)
	{
		assert metadata != null : "metadata is NULL";
		assert ! this.contains (metadata.getElementClass ()) : "metadata instance is already registered";

		this.metadata.put (metadata.getElementClass (), new CellImpl<T, U> (metadata));
	}

	/**
	 * Determine if a <code>MetaData</code> instance corresponding to the
	 * specified <code>Element</code> implementation class exists in the
	 * <code>Container</code>.
	 *
	 * @param  element The <code>Element</code> implementation class, not null
	 *
	 * @return         <code>true</code> if the specified <code>Element</code>
	 *                 implementation class is in the <code>Container</code>,
	 *                 <code>false</code> otherwise
	 */

	public boolean contains (final Class<?> element)
	{
		assert element != null : "element is NULL";

		return this.metadata.containsKey (element);
	}

	/**
	 * Retrieve a <code>MetaData</code> instance from the
	 * <code>Container</code>.  The returned <code>MetaData</code> instance
	 * will correspond to the specified <code>Element</code> implementation
	 * class.
	 *
	 * @param  <T>     The <code>Element</code> interface type
	 * @param  <U>     The <code>Element</code> implementation type
	 * @param  element The <code>Element</code> implementation class, not null
	 *
	 * @return         The <code>MetaData</code> instance
	 */

	@SuppressWarnings ("unchecked")
	public <T extends Element, U extends T> MetaData<T, U> get (final Class<U> element)
	{
		assert element != null : "element is NULL";

		return ((CellImpl<T, U>) this.metadata.get (element)).get ();
	}

	/**
	 * Inject a <code>MetaData</code> instance into the <code>Receiver</code>.
	 * The <code>MetaData</code> instance will correspond to the specified
	 * <code>Element</code> implementation class, and must exist in the
	 * <code>Container</code>.
	 *
	 * @param  <R>      The result type of the <code>Receiver</code>
	 * @param  <T>      The <code>Element</code> interface type
	 * @param  element  The <code>Element</code> implementation class, not null
	 * @param  reciever The <code>Receiver</code>, not null
	 *
	 * @return          The return value of the receiving method
	 */

	@SuppressWarnings ("unchecked")
	public <T extends Element, R> R inject (final Class<?> element, final Receiver<T, R> reciever)
	{
		assert element != null : "element is NULL";
		assert reciever != null : "receiver is NULL";
		assert this.contains (element) : "element is not registered";

		return ((Cell<T>) this.metadata.get (element)).inject (reciever);
	}
}
