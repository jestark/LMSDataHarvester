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

package ca.uoguelph.socs.icc.edm.domain.datastore.jpa;

import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.EntityManager;
import javax.persistence.NonUniqueResultException;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import com.google.common.base.Preconditions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.DomainModel;
import ca.uoguelph.socs.icc.edm.domain.Element;
import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;
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

final class JPANamedQuery<T extends Element> implements Query<T>
{
	/** The logger for this Query instance */
	private final Logger log;

	/** The <code>Selector</code> which defines the <code>Query</code> */
	private final Selector<T> selector;

	/** The <code>Element</code> implementation class to query */
	private final Class<? extends T> impl;

	/** The <code>DomainModel</code> to set in the <code>Element</code> */
	private final DomainModel model;

	/** Reference for seting the <code>DomainModel</code> */
	private final BiConsumer<T, DomainModel> reference;

	/** The JPA <code>EntityManager</code> for the database */
	private final EntityManager manager;

	/** The name of the query for JPA mapping */
	private final String qname;

	/** The JPA query */
	private TypedQuery<? extends T> query;

	/**
	 * Create the <code>JPAIdQuery</code>.
	 *
	 * @param  selector  The <code>Selector</code>, not null
	 * @param  impl      The <code>Element</code> implementation class, not null
	 * @param  model     The <code>DomainModel</code>, not null
	 * @param  reference Method reference for setting the
	 *                   <code>DomainModel</code>, not null
	 * @param  manager   The <code>EntityManager</code>, not null
	 */

	JPANamedQuery (
			final Selector<T> selector,
			final Class<? extends T> impl,
			final DomainModel model,
			final BiConsumer<T, DomainModel> reference,
			final EntityManager manager)
	{
		this.log = LoggerFactory.getLogger (this.getClass ());

		assert selector != null : "selector is NULL";
		assert impl != null : "impl is NULL";
		assert model != null : "model is NULL";
		assert manager != null : "manager is NULL";

		this.selector = selector;
		this.impl = impl;
		this.model = model;
		this.reference = reference;
		this.manager = manager;

		this.qname = String.format ("%s:%s", this.selector.getElementClass ()
				.getSimpleName (), this.selector.getName ());

		this.query = this.manager.createNamedQuery (this.qname, this.impl);
	}

	private T setDomainModel (final T element)
	{
		this.reference.accept (element, this.model);
		return element;
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
		Preconditions.checkArgument (this.selector.getProperties ().contains (property),
				"property (%s) is not associated with this query", property.getName ());

		return property.getValueClass ().cast (this.query.getParameterValue (property.getName ()));
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

	public <V> Query<T> setValue (final Property<T, V> property, final V value)
	{
		this.log.trace ("setProperty: property={}, value={}", property, value);

		Preconditions.checkNotNull (property, "property");
		Preconditions.checkNotNull (value, "value");
		Preconditions.checkArgument (this.selector.getProperties ().contains (property),
				"property (%s) is not associated with this query", property.getName ());

		this.query.setParameter (property.getName (), value);

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

		this.selector.getProperties ()
			.forEach (p -> this.query.setParameter (p.getName (), p.stream (element)
						.findAny ()
						.orElse (null)));

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

		Preconditions.checkState (this.manager.isOpen (), "DataStore is Closed");
		Preconditions.checkState (this.selector.getCardinality () !=
				Selector.Cardinality.MULTIPLE, "Selector must be unique");
		Preconditions.checkState (this.query.getParameters ()
				.stream ()
				.allMatch (p -> this.query.isBound (p)), "Query Parameters must not be null");

		T result = null;

		try
		{
			result = this.query.getSingleResult ();
			result = this.setDomainModel (result);
			this.log.debug ("Loaded Element: {}", result);
		}
		catch (NoResultException ex)
		{
			this.log.debug ("Query {} did not return any results", this.qname);
			result = null;
		}
		catch (NonUniqueResultException ex)
		{
			this.log.error ("Query {} returned multiple results", this.qname);
			throw new IllegalStateException ("Query returned multiple results", ex);
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

		return this.stream ().collect (Collectors.toList ());
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

		Preconditions.checkState (this.manager.isOpen (), "DataStore is Closed");
		Preconditions.checkState (this.query.getParameters ()
				.stream ()
				.allMatch (p -> this.query.isBound (p)), "Query Parameters must not be null");

		return this.query.getResultList ()
			.stream ()
			.map (e -> this.setDomainModel (e));
	}
}
