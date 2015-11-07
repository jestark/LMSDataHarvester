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

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import java.util.HashMap;
import java.util.HashSet;

import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;

import ca.uoguelph.socs.icc.edm.domain.Element;

/**
 * Builder for the <code>MetaData</code>.
 *
 * @author  James E. Stark
 * @version 1.0
 * @param   <T> The <code>Element</code> type
 */

public final class MetaDataBuilder<T extends Element>
{
	/** The Logger */
	private final Logger log;

	/** The <code>Element</code> type */
	private final Class<T> type;

	/** The <code>MetaData</code> definition for the parent <code>Element</code> */
	private final MetaData<? super T> parent;

	/** The <code>Set</code> of <code>Property</code> instances */
	private final Set<Property<? extends Element, ?>> properties;

	/** The <code>Set</code> of <code>Selector</code> instances */
	private final Set<Selector<? extends Element>> selectors;

	/** <code>Property</code> to <code>Accessor</code> mapping */
	private final Map<Property<T, ?>, Accessor<T, ?>> accessors;

	/** <code>Property</code> to <code>MultiAccessor</code> mapping */
	private final Map<Property<T, ?>, MultiAccessor<T, ?>> multiaccessors;

	/**
	 * Create the <code>MetaDataBuilder</code>.
	 *
	 * @param  type The <code>Element</code> interface class, not null
	 */

	protected MetaDataBuilder (final Class<T> type, final @Nullable MetaData<? super T> parent)
	{
		this.log = LoggerFactory.getLogger (this.getClass ());

		this.type = type;
		this.parent = parent;

		this.properties = new HashSet<> ();
		this.selectors = new HashSet<> ();

		this.accessors = new HashMap<> ();
		this.multiaccessors = new HashMap<> ();
	}

	private boolean hasProperty (final Property<T, ?> property)
	{
		assert property != null : "property is null";

		return this.properties.contains (property) || ((this.parent != null) && this.parent.hasProperty (property));
	}

	@SuppressWarnings ("unchecked")
	private <V extends Element> Accessor<T, V> getAccessor (final Property<T, V> property)
	{
		return (Accessor<T, V>) this.accessors.get (property);
	}

	@SuppressWarnings ("unchecked")
	private <V extends Element> MultiAccessor<T, V> getMultiAccessor  (final Property<T, V> property)
	{
		return (MultiAccessor<T, V>) this.multiaccessors.get (property);
	}

	private <V extends Element> InverseRelationship<T, V> createInverseRelationship (final Property<T, V> property)
	{
		return (property.hasFlags (Property.Flags.MULTIVALUED))
			? MultiInverseRelationship.of (this.getMultiAccessor (property))
			: SingleInverseRelationship.of (this.getAccessor (property));
	}

	private <V extends Element> Relationship<T, V> createRelationship (final InverseRelationship<V, T> inverse, final Property<T, V> property)
	{
		return (property.hasFlags (Property.Flags.MULTIVALUED))
			? MultiRelationship.of (inverse, this.getMultiAccessor (property))
			: SingleRelationship.of (inverse, this.getAccessor (property));
	}

	/**
	 * Set the method references for getting and setting the value associated
	 * with the specified <code>Property</code>.  All of the
	 * <code>Property</code> instances associated with the <code>Element</code>
	 * must have references attached to them in the implementation, however the
	 * <code>set</code> reference is allowed to be <code>null</code> which will
	 * result in a read-only <code>Property</code> for the <code>Element</code>
	 * implementation.
	 *
	 * @param  <V>      The <code>Element</code> type of the property
	 * @param  property The <code>Property</code>, not null
	 * @param  get      Method reference to get the value, not null
	 * @param  set      Method reference to set the value, may be null
	 *
	 * @return          This <code>MetaDataBuilder</code>
	 */

	public <V> MetaDataBuilder<T> addProperty (final Property<T, V> property, final Function<T, V> get, final @Nullable BiConsumer<T, V> set)
	{
		this.log.trace ("addProperty: property={}, get={}, set={}", property, get, set);

		assert property != null : "property is NULL";
		assert get != null : "get is NULL";
		assert ! this.properties.contains (property) : "property is already registered";

		this.properties.add (property);
		this.accessors.put (property, SingleReference.of (this.type, property, get, set));

		return this;
	}

	/**
	 * Set the method references for getting and setting the value associated
	 * with the specified <code>Property</code>.  All of the
	 * <code>Property</code> instances associated with the <code>Element</code>
	 * must have references attached to them in the implementation.
	 * <p>
	 * This method creates read-only single-valued references.
	 *
	 * @param  <V>      The <code>Element</code> type of the property
	 * @param  property The <code>Property</code>, not null
	 * @param  get      Method reference to get the value, not null
	 *
	 * @return          This <code>MetaDataBuilder</code>
	 */

	public <V> MetaDataBuilder<T> addProperty (final Property<T, V> property, final Function<T, V> get)
	{
		this.log.trace ("addProperty: property={} get={}", property, get);

		return this.addProperty (property, get, null);
	}

	/**
	 * Set the method references for getting, adding and removing values for the
	 * specified <code>Property</code>.  All of the <code>Property</code>
	 * instances associated with the <code>Element</code> must have references
	 * attached to them in the implementation.
	 * <p>
	 * This methods sets the references for a Multi-valued
	 * <code>Property</code>.
	 *
	 * @param  <V>      The <code>Element</code> type of the property
	 * @param  property The <code>Property</code>, not null
	 * @param  get      Method reference to get the collection, not null
	 * @param  add      Method reference to add a value to the
	 *                  <code>Collection</code>, not null
	 * @param  remove   Method reference to remove a value from the
	 *                  <code>Collection</code>, not null
	 *
	 * @return          This <code>MetaDataBuilder</code>
	 */

	public <V extends Element> MetaDataBuilder<T> addProperty (final Property<T, V> property, final Function<T, Collection<V>> get, final BiPredicate<T, V> add, final BiPredicate<T, V> remove)
	{
		this.log.trace ("addProperty: property={}, get={}, add={}, remove={}", property, get, add, remove);

		assert property != null : "property is NULL";
		assert get != null : "get is NULL";
		assert ! this.properties.contains (property) : "property is already registered";

		this.properties.add (property);
		this.multiaccessors.put (property, MultiReference.of (this.type, property, get, add, remove));

		return this;
	}

	/**
	 * Add a bi-directional <code>Relationship</code> instance for the specified
	 * <code>Property</code> instances.  This method creates a relationship
	 * between the local <code>MetaData</code> instance (which is being built by
	 * this builder) and the specified remote <code>MetaData</code> instance.
	 *
	 * @param  <V>      The <code>Element</code> type
	 * @param  metadata The <code>MetaData</code> for the remote side, not null
	 * @param  local    The <code>Property</code> for the local side, not null
	 * @param  remote   The <code>Property</code> for the remote side, not null
	 *
	 * @return          This <code>MetaDataBuilder</code>
	 */

	public <V extends Element> MetaDataBuilder<T> addRelationship (final MetaData<V> metadata, final Property<T, V> local, final Property<V, T> remote)
	{
		this.log.trace ("addRelationship:  metadata={}, local={}, remote={}", metadata, local, remote);

		assert metadata != null : "metadata is NULL";
		assert local != null : "local is NULL";
		assert remote != null : "remote is NULL";

		return this;
	}

	/**
	 * Add a uni-directional <code>Relationship</code> instance for the
	 * specified <code>Property</code>.  This method creates a relationship
	 * between the local <code>MetaData</code> instance (which is being built by
	 * this builder) and the specified remote <code>MetaData</code> instance.
	 * Since the remote side does not have a <code>Property</code> to describe
	 * it's side of the relationship, a <code>Selector</code> is used instead.
	 *
	 * @param  <V>      The <code>Element</code> type
	 * @param  metadata The <code>MetaData</code> for the remote side, not null
	 * @param  local    The <code>Property</code> for the local side, not null
	 * @param  remote   The <code>Selector</code> for the remote side, not null
	 *
	 * @return          This <code>MetaDataBuilder</code>
	 */

	public <V extends Element> MetaDataBuilder<T> addRelationship (final MetaData<V> metadata, final Property<T, V> local, final Selector<T> remote)
	{
		this.log.trace ("addRelationship: metadata={}, local={}, remote={}", metadata, local, remote);

		assert metadata != null : "metadata is NULL";
		assert local != null : "local is NULL";
		assert remote != null : "remote is NULL";

		return this;
	}

	/**
	 * Create the <code>Selector</code> using a single <code>Property</code>.
	 *
	 * @param  selector The <code>Selector</code>, not null
	 *
	 * @return          This <code>MetaDataBuilder</code>
	 */

	public MetaDataBuilder<T> addSelector (final Selector<T> selector)
	{
		this.log.trace ("addSelector: selector={}", selector);

		assert selector != null : "selector is NULL";
		assert ! this.selectors.contains (selector) : "selector is already registered";
		assert selector.getProperties ().stream ()
			.allMatch (p -> this.hasProperty (p)) : "Properties in selector missing from definition";

		this.selectors.add (selector);

		return this;
	}

	/**
	 * Build the <code>MetaData</code> for the <code>Element</code>
	 * implementation class.
	 *
	 * @return The <code>MetaData</code>
	 */

	public MetaData<T> build ()
	{
		this.log.trace ("build:");

		return new MetaData<T> (this.type, this.parent, this.properties, this.selectors,
				this.accessors, this.multiaccessors);
	}
}
