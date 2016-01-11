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

package ca.uoguelph.socs.icc.edm.domain.datastore.memory;

import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.EntityManager;

import com.google.common.base.Preconditions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.DomainModel;
import ca.uoguelph.socs.icc.edm.domain.Element;
import ca.uoguelph.socs.icc.edm.domain.datastore.Query;
import ca.uoguelph.socs.icc.edm.domain.metadata.Property;
import ca.uoguelph.socs.icc.edm.domain.metadata.Selector;

/**
 * Retrieve <code>Element</code> instances from a <code>DataStore</code>.  This
 * class builds a <code>Filter</code> based on the <code>Selector</code> and
 * the input values then uses the <code>Filter</code> to query
 * <code>Element</code> instances from the <code>DataStore</code>.
 *
 * @author  James E. Stark
 * @version 1.0
 * @param   <T> The type of <code>Element</code> returned by the query
 * @see     DataStore
 * @see     ca.uoguelph.socs.icc.edm.domain.Element
 */

final class MemQuery<T extends Element> implements Query<T>
{
	/** The logger for this Query instance */
	private final Logger log;

	/** The <code>Selector</code> which defines the <code>Query</code> */
	private final Selector<T> selector;

	/** The <code>Element</code> implementation class to query */
	private final Class<? extends T> impl;

	/** The <code>DataStore</code> */
	private final MemDataStore datastore;

	/** <code>Filter</code> parameters */
	private final Map<Property<T, ?>, Object> values;

	/**
	 * Create the <code>JPAIdQuery</code>.
	 *
	 * @param  selector  The <code>Selector</code>, not null
	 * @param  impl      The <code>Element</code> implementation class, not null
	 * @param  datastore The <code>MemDataStore</code>, not null
	 */

	protected MemQuery (
			final Selector<T> selector,
			final Class<? extends T> impl,
			final MemDataStore datastore)
	{
		this.log = LoggerFactory.getLogger (this.getClass ());

		assert selector != null : "selector is NULL";
		assert impl != null : "impl is NULL";
		assert datastore != null : "datastore is NULL";

		this.selector = selector;
		this.impl = impl;
		this.datastore = datastore;

		this.values = new HashMap<> ();

		selector.getProperties ()
			.forEach (x -> this.values.put (x, null));
	}

	/**
	 * Get the <code>Selector</code>, used to create the <code>Query</code>
	 *
	 * @return The <code>Selector</code>
	 */

	public Selector<T> getSelector ()
	{
		return this.selector;
	}

	/**
	 * Get the value for the specified property.
	 *
	 * @param  <V>      The type of the value associated with the property
	 * @param  property The <code>Property</code>, not null
	 *
	 * @return          The value associated with the specified property
	 */

	public <V> V getValue (final Property<T, V> property)
	{
		Preconditions.checkNotNull (property, "property");
		Preconditions.checkArgument (this.values.containsKey (property),
				"property (%s) is not associated with this query", property.getName ());

		return property.getValueClass ().cast (this.values.get (property));
	}

	/**
	 * Set the specified property to the specified value.
	 *
	 * @param  <V>                      The type of the value associated with
	 *                                  the property
	 * @param  property                 The <code>Property</code>, not null
	 * @param  value                    The value to set for the property
	 *
	 * @return                          A reference to this <code>Query</code>
	 * @throws IllegalArgumentException if the specified <code>Property</code>
	 *                                  is not a member of the
	 *                                  <code>Selector</code> for this
	 *                                  <code>Query</code>
	 */

	public <V> Query<T> setValue (final Property<T, V> property, final V value)
	{
		this.log.trace ("setProperty: property={}, value={}", property, value);

		Preconditions.checkNotNull (property, "property");
		Preconditions.checkNotNull (value, "value");
		Preconditions.checkArgument (this.values.containsKey (property),
				"property (%s) is not associated with this query", property.getName ());

		this.values.put (property, value);

		return this;
	}

	/**
	 * Set all of the values for the <code>Property</code> instances associated
	 * with the <code>Query</code> from the specified <code>Element</code>
	 * instance.  This method loads the value associated with each
	 * <code>Property</code> in the <code>Query</code> from the specified
	 * <code>Element</code>.  For an <code>Element</code> instance which has
	 * multiple values for a <code>Property</code> in the <code>Query</code> the
	 * behaviour of this method is undefined.
	 *
	 * @param  element The <code>Element</code> instance, not null
	 *
	 * @return         A reference to this <code>Query</code>
	 */

	public final Query<T> setAllValues (final T element)
	{
		this.log.trace ("setAllProperties: element={}", element);

		Preconditions.checkNotNull (element, "element");

		this.values.replaceAll ((k, v) -> k.stream (element)
				.findAny ()
				.orElse (null));

		return this;
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
	 * @throws IllegalStateException if the <code>DataStore</code> is closed
	 * @throws IllegalStateException if any <code>Property</code> instance
	 *                               associated with the <code>Query</code> has
	 *                               a null value
	 */

	public Optional<T> query ()
	{
		this.log.trace ("query:");

		Preconditions.checkState (this.selector.getCardinality () !=
				Selector.Cardinality.MULTIPLE, "Selector must be unique");

		List<T> elements = this.queryAll ();

		T result = null;

		if (elements.size () > 1)
		{
			throw new IllegalStateException ("Query returned multiple elements");
		}

		if (! elements.isEmpty ())
		{
			result = elements.get (0);
		}

		return Optional.ofNullable (result);
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
	 * @throws IllegalStateException if the <code>DataStore</code> is closed
	 * @throws IllegalStateException if any <code>Property</code> instance
	 *                               associated with the <code>Query</code> has
	 *                               a null value
	 */

	public List<T> queryAll ()
	{
		this.log.trace ("queryAll:");

		return this.stream ()
			.collect (Collectors.toList ());
	}

	/**
	 * Get a <code>Stream</code> of <code>Element</code> instances from the
	 * <code>DataStore</code> which match the <code>Query</code>.
	 *
	 * @return                       A <code>Stream</code> containing the
	 *                               <code>Element</code> instances with match
	 *                               the <code>Query</code>
	 * @throws IllegalStateException if the <code>DataStore</code> is closed
	 * @throws IllegalStateException if any <code>Property</code> instance
	 *                               associated with the <code>Query</code> has
	 *                               a null value
	 */

	public Stream<T> stream ()
	{
		this.log.trace ("stream:");

		Preconditions.checkState (datastore.isOpen (), "DataStore is closed");
		Preconditions.checkState (this.values.entrySet ()
				.stream ()
				.allMatch (e -> e.getValue () != null), "Query Parameters must not be null");

		Stream<T> result = null;

		if (this.selector.getCardinality () != Selector.Cardinality.MULTIPLE && this.selector.isConstant ())
		{
			result = this.datastore.fetch (Index.create (this.selector, this.impl, this.values));
		}
		else
		{
			result = this.datastore.fetch (Filter.create (this.selector, this.impl, this.values));
		}

		return result;
	}
}
