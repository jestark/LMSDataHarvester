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
	}

	private boolean hasProperty (final Property<? super T, ?> property)
	{
		assert property != null : "property is null";

		return this.properties.contains (property); // || ((this.parent != null) && this.parent.hasProperty (property));
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
	 *
	 * @return          This <code>MetaDataBuilder</code>
	 */

	public <V> MetaDataBuilder<T> addProperty (final Property<T, V> property)
	{
		this.log.trace ("addProperty: property={}", property);

		assert property != null : "property is NULL";

		this.properties.add (property);

		return this;
	}

	/**
	 * Add a bi-directional <code>Relationship</code> instance for the specified
	 * <code>Property</code> instances.  This method creates a relationship
	 * between the local <code>MetaData</code> instance (which is being built by
	 * this builder) and the specified remote <code>MetaData</code> instance.
	 *
	 * @param  <V>      The <code>Element</code> type
	 * @param  local    The <code>Property</code> for the local side, not null
	 * @param  metadata The <code>MetaData</code> for the remote side, not null
	 * @param  remote   The <code>Property</code> for the remote side, not null
	 *
	 * @return          This <code>MetaDataBuilder</code>
	 */

	public <V extends Element> MetaDataBuilder<T> addRelationship (final Property<T, V> local, final MetaData<V> metadata, final Property<V, T> remote)
	{
		this.log.trace ("addRelationship: local={}, metadata={}, remote={}",local, metadata, remote);

		assert local != null : "local is NULL";
		assert metadata != null : "metadata is NULL";
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
	 * @param  local    The <code>Property</code> for the local side, not null
	 * @param  metadata The <code>MetaData</code> for the remote side, not null
	 * @param  remote   The <code>Selector</code> for the remote side, not null
	 *
	 * @return          This <code>MetaDataBuilder</code>
	 */

	public <V extends Element> MetaDataBuilder<T> addRelationship (final Property<T, V> local, final MetaData<V> metadata, final Selector<T> remote)
	{
		this.log.trace ("addRelationship: local={}, metadata={}, remote={}", local, metadata, remote);

		assert local != null : "local is NULL";
		assert metadata != null : "metadata is NULL";
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

		return new MetaData<T> (this.type, this.parent, this.properties, this.selectors);
	}
}
