/* Copyright (C) 2014, 2015 James E. Stark
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

package ca.uoguelph.socs.icc.edm.domain.datastore;

import java.util.List;
import java.util.Set;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.base.Preconditions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.Element;

import ca.uoguelph.socs.icc.edm.domain.metadata.MetaData;
import ca.uoguelph.socs.icc.edm.domain.metadata.Property;
import ca.uoguelph.socs.icc.edm.domain.metadata.Selector;

/**
 * Retrieve <code>Element</code> instances from a <code>DataStore</code>.  This
 * class builds a <code>Filter</code> based on the <code>Selector</code> and
 * the input values then uses the <code>Filter</code> to query
 * <code>Element</code> instances from the <code>DataStore</code>.
 *
 * @author  James E. Stark
 * @version 2.0
 * @param   <T> The type of <code>Element</code> returned by the query
 * @see     DataStore
 * @see     ca.uoguelph.socs.icc.edm.domain.Element
 */

public final class Query<T extends Element>
{
	interface Backend<T extends Element>
	{
		public abstract Selector getSelector ();

		public abstract <V> V getValue (Property<V> property);

		public abstract <V> Backend<T> setValue (Property<V> property, V value);

		public abstract Stream<T> stream ();
	}

	/** The logger for this Query instance */
	private final Logger log;

	/** The <code>Backend</code> query that accesses the <code>DataStore</code> */
	private final Backend<T> backend;

	/**
	 * Create the <code>Query</code>.
	 *
	 */

	protected Query (final Backend<T> backend)
	{
		assert backend != null;

		this.log = LoggerFactory.getLogger (this.getClass ());

		this.backend = backend;
	}

	/**
	 * Get the <code>Selector</code>, used to create the <code>Query</code>
	 *
	 * @return The <code>Selector</code>
	 */

	public Selector getSelector ()
	{
		return this.backend.getSelector ();
	}

	/**
	 * Get the value for the specified property.
	 *
	 * @param  <V>      The type of the value associated with the property
	 * @param  property The <code>Property</code>, not null
	 *
	 * @return          The value associated with the specified property
	 */

	public final <V> V getValue (final Property<V> property)
	{
		Preconditions.checkNotNull (property, "property");
		Preconditions.checkArgument (this.backend.getSelector ()
				.getProperties ().contains (property),
				"property (%s) is not associated with this query", property.getName ());

		return this.backend.getValue (property);
	}

	/**
	 * Set the specified property to the specified value.
	 *
	 * @param  <V>      The type of the value associated with the property
	 * @param  property The <code>Property</code>, not null
	 * @param  value    The value to set for the property
	 *
	 * @return         A reference to this <code>Query</code>
	 * @throws IllegalArgumentException if the specified <code>Property</code>
	 *                                  is not a member of the
	 *                                  <code>Selector</code> for this
	 *                                  <code>Query</code>
	 */

	public final <V> Query<T> setValue (final Property<V> property, final V value)
	{
		this.log.trace ("setProperty: property={}, value={}", property, value);

		Preconditions.checkNotNull (property, "property");
		Preconditions.checkNotNull (value, "value");
		Preconditions.checkArgument (this.backend.getSelector ()
				.getProperties ().contains (property),
				"property (%s) is not associated with this query", property.getName ());

		this.backend.setValue (property, value);

		return this;
	}

	/**
	 * Set all of the values for the <code>Property</code> instances associated
	 * with the <code>Query</code> from the specified <code>Element</code>
	 * instance.
	 *
	 * @param  element The <code>Element</code> instance, not null
	 *
	 * @return         A reference to this <code>Query</code>
	 */

	public final Query<T> setAllValues (final T element)
	{
		this.log.trace ("setAllProperties: element={}", element);

		Preconditions.checkNotNull (element, "element");

//		this.backend.getSelector ().getProperties ()
//			.forEach (p -> this.backend.setValue (p, ));

		return this;
	}

	private static <X extends Element> X reduce (final X x, final X y)
	{
		if (x == null)
		{
			return y;
		}
		else
		{
			throw new IllegalStateException ("Query returned multiple results");
		}
	}

	/**
	 * Fetch the <code>Element</code> instance from the <code>DataStore</code>
	 * which matches the properties set on the <code>Query</code>.  Values must
	 * be specified for all of the properties for the <code>Query</code>.
	 *
	 * @return                       The <code>Element</code> instance which
	 *                               matches the properties specified for the
	 *                               query, null if no <code>Element</code>
	 *                               instances exist in the
	 *                               <code>DataStore</code> that match the
	 *                               <code>Query</code>
	 * @throws IllegalstateException if the <code>Selector</code> upon which
	 *                               the <code>Query</code> is based is not
	 *                               unique
	 */

	public final T query ()
	{
		this.log.trace ("query:");

		Preconditions.checkState (this.backend.getSelector ().isUnique (), "Selector is not Unique");

		return this.backend.stream ()
			.reduce (null, Query::reduce);
	}

	/**
	 * Fetch a <code>List</code> of <code>Element</code> instances from the
	 * <code>DataStore</code> which match the specified query properties.
	 * Values must be specified for all of the properties for the
	 * <code>Query</code>.
	 *
	 * @return                       The <code>List</code> of
	 *                               <code>Element</code> instances which match
	 *                               the query.  The <code>List</code> will be
	 *                               empty if no <code>Element</code> instances
	 *                               match the <code>Query</code>.
	 * @throws IllegalStateException if any <code>Property</code> instance
	 *                               associated with the <code>Query</code> has
	 *                               a null value
	 */

	public List<T> queryAll ()
	{
		this.log.trace ("queryAll:");

		return this.backend.stream ()
			.collect (Collectors.toList ());
	}

	/**
	 *
	 */

	public Stream<T> stream ()
	{
		this.log.trace ("stream:");

		return this.backend.stream ();
	}
}
