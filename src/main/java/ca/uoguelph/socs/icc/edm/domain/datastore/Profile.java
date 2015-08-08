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

import java.util.Map;

import java.util.HashMap;

import ca.uoguelph.socs.icc.edm.domain.Element;

import ca.uoguelph.socs.icc.edm.domain.datastore.idgenerator.IdGenerator;

import ca.uoguelph.socs.icc.edm.domain.metadata.Creator;
import ca.uoguelph.socs.icc.edm.domain.metadata.MetaData;

/**
 * Profile data for the <code>DataStore</code>.
 *
 * @author  James E. Stark
 * @version 2.0
 */

public final class Profile
{
	/** The <code>MetaData<code> implementations */
	private static final Map<Class<? extends Element>, MetaData<?>> creators;

	/** The <code>MetaData</code> definitions and implementations */
	private static final Map<Class<? extends Element>, MetaData<?>> metadata;

	/** The default implementation classes for each <code>Element</code> */
	private final Map<Class<? extends Element>, Class<? extends Element>> implementations;

	/** The <code>IdGenerator</code> to be used with each <code>Element</code> */
	private final Map<Class<? extends Element>, Class<? extends IdGenerator>> generators;

	/** Is the <code>DataStore</code> mutable? */
	private final boolean mutable;

	/**
	 * Static initializer to create the static maps.
	 */

	static
	{
		creators = new HashMap<> ();
		metadata = new HashMap<> ();
	}

	/**
	 * Register a <code>Creator</code>.  This method is intended to be used by
	 * the <code>Element</code> implementation classes to register their
	 * <code>MetaData</code> instance with the <code>Profile</code>.  The
	 * supplied <code>MetaData</code> instance will be registered as both a
	 * <code>Creator</code> (implementation) and <code>MetaData</code>
	 * (definition).
	 *
	 * @param  creator The <code>Creator</code>, not null
	 */

	public static <T extends Element> void registerCreator (final Creator<T> creator)
	{
		assert creator != null : "creator is NULL";
		assert ! Profile.creators.containsKey (creator.getElementClass ()) : "creator is already registered";

		Profile.creators.put (creator.getElementClass (), creator);
		Profile.registerMetaData (creator);
	}

	/**
	 * Register a <code>MetaData</code> instance. This method is intended to be
	 * used by the <code>Element</code> interface classes to register their
	 * <code>MetaData</code> definition with the <code>Profile</code>.
	 *
	 * @param  metadata The <code>MetaData</code>, not null
	 */

	public static <T extends Element> void registerMetaData (final MetaData<T> metadata)
	{
		assert metadata != null : "metadata is NULL";
		assert ! Profile.metadata.containsKey (metadata.getElementClass ()) : "metadata is already registered";

		Profile.metadata.put (metadata.getElementClass (), metadata);
	}

	/**
	 * Create the <code>Profile</code>.  This constructor is not intended
	 * to be called directly, the profile should be created though its builder.
	 *
	 * @param  mutable The designed mutability of the <code>DataStore</code>,
	 *                 not null
	 * @param  implementations The <code>Map</code> of default
	 *                         <code>Element</code> implementation classes for
	 *                         the <code>DataStore</code> instance, not null
	 * @param  generators      The <code>Map</code> of <code>IdGenerator</code>
	 *                         classes to be used for each
	 *                         <code>Element</code>, not null
	 */

	protected Profile (final boolean mutable, final Map<Class<? extends Element>, Class<? extends Element>> implementations, final Map<Class<? extends Element>, Class<? extends IdGenerator>> generators)
	{
		assert implementations != null : "implementations is NULL";
		assert generators != null : "generators is NULL";

		this.mutable = mutable;
		this.implementations = new HashMap<> (implementations);
		this.generators = new HashMap<> (generators);
	}

	/**
	 * Determine if the <code>DataStore</code> can be changed.
	 *
	 * @return <code>true</code> if the <code>DataStore</code> can be changed,
	 *         <code>false</code> otherwise
	 */

	public boolean isMutable ()
	{
		return this.mutable;
	}

	/**
	 * Determine if there is an implementation class registered for the
	 * specified <code>Element</code> interface class.
	 *
	 * @param  type The <code>Element</code> interface class, not null
	 *
	 * @return      <code>true</code> if the <code>Element</code> has a
	 *              registered implementation class, <code>false</code>
	 *              otherwise
	 */

	public boolean hasElementClass (final Class<? extends Element> type)
	{
		assert type != null : "type is NULL";

		return this.implementations.containsKey (type);
	}

	/**
	 * Get the default implementation class for the specified
	 * <code>Element</code>.
	 *
	 * @param  element The <code>Element</code> interface class, not null
	 *
	 * @return         The <code>Element</code> default implementation class
	 */

	public Class<? extends Element> getElementClass (Class<? extends Element> element)
	{
		assert element != null : "element is NULL";
		assert this.hasElementClass (element) : "No implementation class registered for element";

		return this.implementations.get (element);
	}

	/**
	 * Get the <code>MetaData</code> instance for the specified
	 * <code>Element</code> implementation.
	 *
	 * @param  <T>  The <code>Element</code> interface type
	 * @param  type The <code>Element</code> interface class, not null
	 * @param  impl The <code>Element</code> implementation class, not null
	 *
	 * @return      The associated <code>MetaData</code> instance
	 */

	@SuppressWarnings ("unchecked")
	public <T extends Element> MetaData<T> getMetaData (final Class<T> type, final Class<? extends T> impl)
	{
		assert type != null : "type is NULL";
		assert impl != null : "impl is NULL";
		assert Profile.metadata.containsKey (impl) : "element is not registered";
		assert type == Profile.metadata.get (impl).getParentClass () : "Mismatch between type and the element interface";

		return (MetaData<T>) Profile.metadata.get (impl);
	}

	/**
	 * Get the <code>MetaData</code> instance for the specified
	 * <code>Element</code>.  This method will look up the default
	 * implementation class for the specified <code>Element</code> and return
	 * the associated <code>MetaData</code> instance.
	 *
	 * @param  <T>  The <code>Element</code> interface type
	 * @param  type The <code>Element</code> interface class, not null
	 *
	 * @return      The associated <code>MetaData</code> instance
	 */

	@SuppressWarnings ("unchecked")
	public <T extends Element> MetaData<T> getMetaData (final Class<T> type)
	{
		assert type != null : "type is NULL";
		assert this.implementations.containsKey (type) : "No implementation class for specified type";
		assert Profile.metadata.containsKey (this.implementations.get (type)) : "No MetaData for specified type";

		return (MetaData<T>) Profile.metadata.get (this.implementations.get (type));
	}

	/**
	 * Get the <code>Creator</code> instance for the specified
	 * <code>Element</code> implementation.
	 *
	 * @param  <T>  The <code>Element</code> interface type
	 * @param  type The <code>Element</code> interface class, not null
	 * @param  impl The <code>Element</code> implementation class, not null
	 *
	 * @return      The associated <code>Creator</code> instance
	 */

	@SuppressWarnings ("unchecked")
	public <T extends Element> Creator<T> getCreator (final Class<T> type, Class<? extends T> impl)
	{
		assert type != null : "type is NULL";
		assert impl != null : "impl is NULL";
		assert Profile.creators.containsKey (impl) : "element is not registered";
		assert type == Profile.creators.get (impl).getParentClass () : "Mismatch between type and the element interface";

		return (Creator<T>) Profile.creators.get (impl);
	}

	/**
	 * Get the <code>Creator</code> instance for the specified
	 * <code>Element</code>.  This method will look up the default
	 * implementation class for the specified <code>Element</code> and return
	 * the associated <code>Creator</code> instance.
	 *
	 * @param  <T>  The <code>Element</code> interface type
	 * @param  type The <code>Element</code> interface class, not null
	 *
	 * @return      The associated <code>Creator</code> instance
	 */

	@SuppressWarnings ("unchecked")
	public <T extends Element> Creator<T> getCreator (final Class<T> type)
	{
		assert type != null : "type is NULL";
		assert this.implementations.containsKey (type) : "No implementation class for specified type";
		assert Profile.creators.containsKey (this.implementations.get (type)) : "No Creator for specified type";

		return (Creator<T>) Profile.creators.get (this.implementations.get (type));
	}

	/**
	 * Get the <code>IdGenerator</code> associated with the specified
	 * <code>Element</code> class, for the <code>DataStore</code>.  This method
	 * searches for the appropriate <code>IdGenerator</code> for the specified
	 * <code>Element</code> class.
	 *
	 * @param  element               The <code>Element</code> class, not null
	 *
	 * @return                       The associated <code>IdGenerator</code>
	 * @throws IllegalStateException if there id no <code>MetaData</code>
	 *                               registered for the specified
	 *                               <code>Element</code> class or one of its
	 *                               parents
	 */

	public Class<? extends IdGenerator> getGenerator (Class<? extends Element> element)
	{
		assert element != null : "element is NULL";

		while (! this.generators.containsKey (element))
		{
			if (Profile.metadata.containsKey (element))
			{
				element = Profile.metadata.get (element).getParentClass ();
			}
			else
			{
				throw new IllegalStateException ("No MetaData registered for Element Class: " + element.getSimpleName ());
			}
		}

		return this.generators.get (element);
	}
}
