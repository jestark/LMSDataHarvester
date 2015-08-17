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

import java.util.Map;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.Element;

/**
 * Container for <code>MetaData</code> instances.  This class holds all of the
 * <code>Definition</code> and <code>Implementation</code> instances for the
 * <code>Element</code> classes.
 *
 * @author  James E. Stark
 * @version 1.0
 */

final class Container
{
	/** The <code>Container</code> singleton instance */
	private static final Container INSTANCE;

	/** The Logger */
	private final Logger log;

	/** The <code>MetaData<code> implementations */
	private final Map<Class<? extends Element>, MetaData<?>> creators;

	/** The <code>MetaData</code> definitions and implementations */
	private final Map<Class<? extends Element>, MetaData<?>> metadata;

	/**
	 * Static initializer to create the instance of the <code>Container</code>.
	 */

	static
	{
		INSTANCE = new Container ();
	}

	/**
	 * Get an instance of the <code>Container</code>.
	 */

	public static Container getInstance ()
	{
		return Container.INSTANCE;
	}

	/**
	 * Create the <code>Container</code>.
	 */

	private Container ()
	{
		this.log = LoggerFactory.getLogger (Container.class);

		this.creators = new HashMap<> ();
		this.metadata = new HashMap<> ();
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

	public <T extends Element> void registerCreator (final Creator<T> creator)
	{
		this.log.trace ("registerCreator: creator={}", creator);

		assert creator != null : "creator is NULL";
		assert ! this.creators.containsKey (creator.getElementClass ()) : "creator is already registered";

		this.creators.put (creator.getElementClass (), creator);
		this.registerMetaData (creator);
	}

	/**
	 * Register a <code>MetaData</code> instance. This method is intended to be
	 * used by the <code>Element</code> interface classes to register their
	 * <code>MetaData</code> definition with the <code>Profile</code>.
	 *
	 * @param  metadata The <code>MetaData</code>, not null
	 */

	public <T extends Element> void registerMetaData (final MetaData<T> metadata)
	{
		this.log.trace ("registerMetaData: metadata={}", metadata);

		assert metadata != null : "metadata is NULL";
		assert ! this.metadata.containsKey (metadata.getElementClass ()) : "metadata is already registered";

		this.metadata.put (metadata.getElementClass (), metadata);
	}

	/**
	 * Determine if there is a <code>MetaData</code> instance registered for
	 * the specified <code>Element</code> class.
	 *
	 * @param  cls The <code>Element</code> class, not null
	 *
	 * @return     <code>true</code> if a <code>MetaData</code> instance has
	 *             been registered, <code>false</code> otherwise
	 */

	public boolean containsMetaData (final Class<? extends Element> cls)
	{
		this.log.trace ("containsMetaData: cls={}", cls);

		assert cls != null : "cls is NULL";

		return this.metadata.containsKey (cls);
	}

	/**
	 * Determine if there is a <code>Creator</code> instance registered for the
	 * specified <code>Element</code> class.
	 *
	 * @param  cls The <code>Element</code> class, not null
	 *
	 * @return     <code>true</code> if a <code>Creator</code> instance has
	 *             been registered, <code>false</code> otherwise
	 */

	public boolean containsCreator (final Class<? extends Element> cls)
	{
		this.log.trace ("containsCreator: cls={}", cls);

		assert cls != null : "cls is NULL";

		return this.creators.containsKey (cls);
	}

	/**
	 * Get the <code>MetaData</code> instance for the specified
	 * <code>Element</code>.
	 *
	 * @param  <T>  The <code>Element</code> interface type
	 * @param  type The <code>Element</code> interface class, not null
	 *
	 * @return      The associated <code>MetaData</code> instance
	 */

	@SuppressWarnings ("unchecked")
	public <T extends Element> MetaData<T> getMetaData (final Class<T> type)
	{
		this.log.trace ("getMetaData: type={}", type);

		assert type != null : "type is NULL";
		assert this.metadata.containsKey (type) : "No MetaData for specified type";

		return (MetaData<T>) this.metadata.get (type);
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
	public <T extends Element> MetaData<T> getMetaData (final Class<T> type, final Class<? extends Element> impl)
	{
		this.log.trace ("getMetaData: type={}, impl={}", type, impl);

		assert type != null : "type is NULL";
		assert impl != null : "impl is NULL";
		assert type.isAssignableFrom (impl) : "impl is not derived from type";
		assert this.metadata.containsKey (impl) : "element is not registered";

		return (MetaData<T>) this.metadata.get (impl);
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
	public <T extends Element> Creator<T> getCreator (final Class<T> type, Class<? extends Element> impl)
	{
		this.log.trace ("getCreator: type={}, impl={}", type, impl);

		assert type != null : "type is NULL";
		assert impl != null : "impl is NULL";
		assert type.isAssignableFrom (impl) : "impl is not derived from type";
		assert this.creators.containsKey (impl) : "element is not registered";

		return (Creator<T>) this.creators.get (impl);
	}
}
