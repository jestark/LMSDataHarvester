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

package ca.uoguelph.socs.icc.edm.domain.metadata;

import java.util.function.Supplier;

import ca.uoguelph.socs.icc.edm.domain.Element;

/**
 * <code>MetaData</code> for an <code>Element</code> implementation class.
 * <p>
 * In addition to containing the meta-data for an <code>Element</code>, this
 * class acts as a work-around for the lack of Generic type reification in
 * Java.  All of the operations on the <code>DomainModel</code> are expressed
 * in terms of the <code>Element</code> interfaces.  Out of necessity, the
 * operations in the <code>DataStore</code> are performed in terms of the
 * <code>Element</code> implementations.  It is necessary to bridge between the
 * interface and implementation in terms of type parameters, to avoid a
 * proliferation of <code>Element</code >implementation specific loader/query
 * classes and/or unchecked casts (both of which are less than ideal).
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
 * @param   <T> The <code>Element</code> interface type
 */

public abstract class MetaData<T extends Element>
{
	/**
	 * Implementation of the <code>MetaData</code> interface with the
	 * <code>Element</code> implementation class specified.
	 *
	 * @author  James E. Stark
	 * @version 1.0
	 * @param   <T> The <code>Element</code> interface type
	 * @param   <U> The <code>Element</code> implementation type
	 */

	private static final class MetaDataImpl<T extends Element, U extends T> extends MetaData<T>
	{
		/** The <code>Element</code> implementation class */
		private final Class<U> element;

		/** Method reference to the <code>Element</code> constructor */
		private final Supplier<U> create;

		/**
		 * Create the <code>MetaDataImpl</code> instance.
		 *
		 * @param  definition The <code>Definition</code>, not null
		 * @param  element    The <code>Element</code> implementation class, not
		 *                    null
		 * @param  create     Method reference to the <code>Element</code>
		 *                    constructor, not null
		 */

		public MetaDataImpl (final Definition<T> definition, final Class<U> element, final Supplier<U> create)
		{
			super (definition);

			assert element != null : "element is NULL";
			assert create != null : "create is NULL";

			this.element = element;
			this.create = create;
		}

		/**
		 * Get the Java type of the <code>Element</code> implementation.
		 *
		 * @return The <code>Class</code> representing the implementation type
		 */

		@Override
		public Class<? extends Element> getElementClass ()
		{
			return this.element;
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

			return receiver.apply (this, this.element);
		}

		/**
		 * Get a new Instance of the <code>Element</code> implementation class.
		 *
		 * @return The new <code>Element</code> instance
		 */

		@Override
		public T newInstance ()
		{
			return this.create.get ();
		}
	}

	/** The <code>Defintiion</code> */
	private final Definition<T> definition;

	/**
	 * Create the <code>MetaData</code> instance for an <code>Element</code>
	 * implementation class.
	 *
	 * @param  <T>        The <code>Element</code> interface type
	 * @param  <U>        The <code>Element</code> implementation type
	 * @param  definition The <code>Definition</code>, not null
	 * @param  element    The <code>Element</code> implementation class, not
	 *                    null
	 * @param  create     Method reference to the constructor, not null
	 *
	 * @return            The <code>MetaData</code> instance
	 */

	public static <T extends Element, U extends T> MetaData<T> getInstance (final Definition<T> definition, final Class<U> element, final Supplier<U> create)
	{
		assert definition != null : "definition is NULL";
		assert element != null : "element is NULL";
		assert create != null : "create is NULL";
		assert definition.getElementType ().isAssignableFrom (element) : "element is not an implementation of the class defined by the definition";

		return new MetaDataImpl<T, U> (definition, element, create);
	}

	/**
	 * Create the <code>MeteData</code> instance.
	 *
	 * @param  definition The <code>Definition</code>, not null
	 */

	protected MetaData (final Definition<T> definition)
	{
		assert definition != null : "definition is NULL";

		this.definition = definition;
	}

	/**
	 * Get the <code>Definition</code> for the <code>Element</code> represented
	 * by the <code>MetaData</code> instance.
	 *
	 * @return The <code>Definition</code> instance
	 */

	public Definition<T> getDefinition ()
	{
		return this.definition;
	}

	/**
	 * Get the Java type of the <code>Element</code> iimplementation.
	 *
	 * @return The <code>Class</code> representing the implementation type
	 */

	public abstract Class<? extends Element> getElementClass ();

	/**
	 * Inject the <code>MetaData</code> instance into the
	 * <code>Receiver</code>.
	 *
	 * @param  <R>      The result type of the <code>Receiver</code>
	 * @param  receiver The <code>Receiver</code>, not null
	 *
	 * @return          The return value of the receiving method
	 */

	public abstract <R> R inject (Receiver<T, R> reciever);

	/**
	 * Get a new Instance of the <code>Element</code> implementation class.
	 *
	 * @return The new <code>Element</code> instance
	 */

	public abstract T newInstance ();
}
