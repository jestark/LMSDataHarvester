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
 * Builder for the meta-data <code>Definition</code>.
 *
 * @author  James E. Stark
 * @version 1.0
 * @param   <T> The <code>Element</code> type
 */

public final class MetaDataBuilder<T extends Element>
{
	/** The Logger */
	private final Logger log;

	/** The <code>MetaData</code> definition for the parent <code>Element</code> */
	private final MetaData<? super T> parent;

	/** Set of all of the associated <code>Property</code> instances */
	private final Set<Property<?>> allprops;

	/** The <code>Set</code> of <code>Property</code> instances */
	private final Set<Property<?>> properties;

	/** The <code>Set</code> of <code>Selector</code> instances */
	private final Set<Selector> selectors;

	/** <code>Property</code> to <code>Accessor</code> mapping */
	private final Map<Property<?>, Accessor<T, ?>> accessors;

	/** <code>Property</code> to <code>Reference</code> mapping */
	private final Map<Property<?>, Reference<T, ?>> references;

	/** The <code>Relationship</code> instances for the interface */
	private final Map<Property<?>, Relationship<T, ?>> relationships;

	/**
	 * Create the <code>MetaDataBuilder</code>.
	 *
	 * @param  type The <code>Element</code> interface class, not null
	 */

	protected MetaDataBuilder (final @Nullable MetaData<? super T> parent)
	{
		this.log = LoggerFactory.getLogger (this.getClass ());

		this.parent = parent;

		this.allprops = (parent != null) ? parent.getProperties () : new HashSet<> ();

		this.properties = new HashSet<> ();
		this.selectors = new HashSet<> ();

		this.accessors = new HashMap<> ();
		this.references = new HashMap<> ();
		this.relationships = new HashMap<> ();
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

	public <V> MetaDataBuilder<T> addProperty (final Property<V> property, final Function<T, V> get, final BiConsumer<T, V> set)
	{
		this.log.trace ("addProperty: property={}, get={}, set={}", property, get, set);

		assert property != null : "property is NULL";
		assert get != null : "get is NULL";
		assert ! this.properties.contains (property) : "property is already registered";

		this.allprops.add (property);
		this.properties.add (property);
		this.prefs.put (property, new SingleReference<T, V> (get, set));

		return this;
	}

	/**
	 * Set the method references for getting and setting the value associated
	 * with the specified <code>Property</code>.  All of the
	 * <code>Property</code> instances associated with the <code>Element</code>
	 * must have references attached to them in the implementation.  This
	 * method creates read-only references.
	 *
	 * @param  <V>      The <code>Element</code> type of the property
	 * @param  property The <code>Property</code>, not null
	 * @param  get      Method reference to get the value, not null
	 *
	 * @return          This <code>MetaDataBuilder</code>
	 */

	public <V> MetaDataBuilder<T> addProperty (final Property<V> property, final Function<T, V> get)
	{
		this.log.trace ("addProperty: property={} get={}", property, get);

		return this.addProperty (property, get, null);
	}

	/**
	 * Add a <code>Relationship</code> for the specified <code>Property</code>.
	 * This method creates the references for the <code>Property</code> in the
	 * <code>Definition</code> (replacing the <code>addProperty</code> method)
	 * add adds a <code>Relationship</code> instance for the
	 * <code>Property</code>.
	 *
	 * @param  <V>      The <code>Element</code> type of the property
	 * @param  property The <code>Property</code>, not null
	 * @param  get      Method reference to get the value, not null
	 * @param  set      Method reference to set the value, may be null
	 *
	 * @return          This <code>MetaDataBuilder</code>
	 */

	public <V extends Element> MetaDataBuilder<T> addRelationship (final Property<V> property, final Function<T, V> get, final BiConsumer<T, V> set)
	{
		this.log.trace ("addRelationship: property={}, get={}, set={}", property, get, set);

		assert property != null : "property is NULL";
		assert get != null : "get is NULL";
		assert set != null : "set is NULL";
		assert ! this.properties.contains (property) : "property is already registered";

		SingleReference<T, V> pref = new SingleReference<T, V> (get, set);

		this.allprops.add (property);
		this.properties.add (property);
		this.relationships.put (property.getPropertyType (), Relationship.getInstance (this.type, property, pref));
		this.prefs.put (property, pref);

		return this;
	}

	/**
	 * Add a <code>Relationship</code> instance for the <code>Collection</code>
	 * of values, which are associated with the specified
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

	public <V extends Element> MetaDataBuilder<T> addRelationship (final Property<V> property, final Function<T, Collection<V>> get, final BiPredicate<T, V> add, final BiPredicate<T, V> remove)
	{
		this.log.trace ("addRelationship: property={}, get={}, add={}, remove={}", property, get, add, remove);

		assert property != null : "property is NULL";
		assert get != null : "get is NULL";
		assert ! this.properties.contains (property) : "property is already registered";

		MultiReference<T, V> rref = new MultiReference<T, V> (get, add, remove);

		this.allprops.add (property);
		this.properties.add (property);
		this.relationships.put (property.getPropertyType (), Relationship.getInstance (this.type, property, rref));
		this.rrefs.put (property, rref);

		return this;
	}

	/**
	 * Add a uni-directional <code>Relationship</code> instance for the
	 * specified <code>Property</code> based on the specified
	 * <code>Selector</code>.
	 *
	 * @param  <V>      The <code>Element</code> type of the property
	 * @param  value    The <code>Element</code> interface class, not null
	 * @param  property The <code>Property</code>, not null
	 * @param  selector The <code>Selector</code>, not null
	 *
	 * @return          This <code>MetaDataBuilder</code>
	 */

	public <V extends Element> MetaDataBuilder<T> addRelationship (final Class<V> value, final Property<T> property, final Selector selector)
	{
		this.log.trace ("addRelationship: property={}, selector={}", property, selector);

		assert value != null : "value is NULL";
		assert property != null : "property is NULL";
		assert selector != null : "selector is NULL";

		Relationship<T, V> rel = Relationship.getInstance (value, property, selector);
		this.relationships.put (value, rel);

		return this;
	}

	/**
	 * Create the <code>Selector</code> using a single <code>Property</code>.
	 *
	 * @param  selector The <code>Selector</code>, not null
	 *
	 * @return          This <code>MetaDataBuilder</code>
	 */

	public MetaDataBuilder<T> addSelector (final Selector selector)
	{
		this.log.trace ("addSelector: selector={}", selector);

		assert selector != null : "selector is NULL";
		assert ! this.selectors.contains (selector) : "selector is already registered";
		assert this.allprops.containsAll (selector.getProperties ()) : "Properties in selector missing from definition";

		this.selectors.add (selector);

		return this;
	}

	/**
	 * Build the <code>MetaData</code> for the <code>Element</code>
	 * implementation class.
	 *
	 * @return The <code>MetaData</code>
	 */

	public Definition<T> build ()
	{
		this.log.trace ("build:");

		return new MetaData<T> (this.parent, this.properties, this.selectors,
				this.accessors, this.references, this.relationships);
	}
}
