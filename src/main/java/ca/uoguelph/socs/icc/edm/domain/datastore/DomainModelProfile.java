/* Copyright (C) 2014 James E. Stark
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

package ca.uoguelph.socs.icc.edm.datastore;

import java.util.Map;
import java.util.Set;

import java.util.HashMap;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import ca.uoguelph.socs.icc.edm.domain.DomainModelElement;
import ca.uoguelph.socs.icc.edm.domain.DomainModelType;

/**
 * Meta-data describing the capabilities of a <code>DataStore</code>.  This
 * class describes a profile containing the implementation details of
 * particular instances of a <code>DataStore</code>.  It includes an
 * indication if the <code>DataStore</code> is mutable (writable), and for
 * each of the domain model interfaces the profile includes the following
 * information:
 * <ul>
 * 	<li>An indication if the interface is represented in the <code>DataStore</code>
 * 	<li>The class that implements the interface for the <code>DataStore</code>
 * 	<li>The class used to generate ID numbers for the interface in the
 * 	    <code>DataStore</code>
 * </ul>
 * <p>
 * All of the per-interface fields are required, for all of the
 * <code>DomainModel</code> interfaces, enumerated in
 * <code>DomainModelType</code>.  If a <code>DataStore</code> does not
 * internally represent a given interface, an implementation must be specified,
 * along with an ID generator.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     DomainModelBuilder
 */

public final class DomainModelProfile
{
	/**
	 * <code>DataStore</code> profile data for a single domain model interface.
	 * This class is intended to be used internally in
	 * <code>DataStoreProfile</code> to hold the profile data for a single domain
	 * model interface.  All access to this class should come though the enclosing
	 * class.
	 */

	private final class Entry
	{
		/** Is this interface represented in the <code>DataStore</code>? */
		private final Boolean available;

		/** <code>DataStore</code> generator for this interface */
		private final Class<? extends IdGenerator> generator;

		/** Class implementing the interface */
		private final Class<? extends DomainModelElement> implementation;

		/**
		 * Create the <code>Entry</code>.
		 *
		 * @param  available      An indication if the <code>DataStore</code>
		 *                        represents the interface represented by this entry,
		 *                        not null
		 * @param  generator      The ID generator class used by with the interface
		 *                        represented by this entry, not null
		 * @param  implementation The class implementing the interface represented by
		 *                        this entry, not null
		 */

		protected Entry (Boolean available, Class<? extends IdGenerator> generator, Class<? extends DomainModelElement> implementation)
		{
			this.available = available;
			this.generator = generator;
			this.implementation = implementation;
		}

		/**
		 * Override the equals method to determine if this <code>Entry</code> is
		 * equal to another based on its attributes.
		 *
		 * @param  obj The object to compare to this <code>Entry</code>
		 * @return     <code>true</code> if the two entries are the same,
		 *             <code>false</code> otherwise
		 */

		@Override
		public boolean equals (Object obj)
		{
			boolean result = false;

			if (obj != null)
			{
				if (obj == this)
				{
					result = true;
				}
				else if (obj.getClass () == this.getClass ())
				{
					EqualsBuilder ebuilder = new EqualsBuilder ();
					ebuilder.append (this.available, ((Entry) obj).available);
					ebuilder.append (this.generator, ((Entry) obj).generator);
					ebuilder.append (this.implementation, ((Entry) obj).implementation);

					result = ebuilder.isEquals ();
				}
			}

			return result;
		}

		/**
		 * Compute a unique <code>hashCode</code> for an <code>Entry</code> based on
		 * its attributes.
		 *
		 * @return The hash code
		 */

		@Override
		public int hashCode ()
		{
			final int base = 911;
			final int mult = 3;

			HashCodeBuilder hbuilder = new HashCodeBuilder (base, mult);
			hbuilder.append (this.available);
			hbuilder.append (this.generator);
			hbuilder.append (this.implementation);

			return hbuilder.toHashCode ();
		}

		/**
		 * Determine if the interface represented by this entry is represented in the
		 * <code>DataStore</code>.
		 *
		 * @return <code>true</code> if the <code>DataStore</code> contains a
		 *         representation of the specified domain model interface,
		 *         <code>false</code> otherwise
		 */

		public Boolean isAvailable ()
		{
			return this.available;
		}

		/**
		 * Get the ID generator class to be used with the <code>DataStore</code>.
		 *
		 * @return The associated ID generator class
		 */

		public Class<? extends IdGenerator> getGenerator ()
		{
			return this.generator;
		}

		/**
		 * Get the implementation class.
		 *
		 * @return The class used to represent the interface in the
		 *         <code>DataStore</code>
		 */

		public Class<? extends DomainModelElement> getImplClass ()
		{
			return this.implementation;
		}
	}

	/** Is the <code>DataStore</code> mutable? */
	private Boolean mutable;

	/** Interface class to Implementation class mapping */
	private final Map<DomainModelType, Entry> entries;

	/**
	 * Create the <code>DataStoreProfile</code>.  This constructor is not intended
	 * to be called directly, the profile should be created though its builder.
	 *
	 * @param  mutable The designed mutability of the <code>DomainModel</code>,
	 *                 not null
	 */

	protected DomainModelProfile (Boolean mutable)
	{
		if (mutable == null)
		{
			throw new NullPointerException ("Mutable is NULL");
		}

		this.mutable = mutable;
		this.entries = new HashMap<DomainModelType, Entry> ();
	}

	/**
	 * Create the <code>DomainModelProfile</code>, from another profile.  This
	 * method is intended to be used by the builder to copy the profile.
	 *
	 * @param  profile The profile to copy, not null
	 */

	protected DomainModelProfile (DomainModelProfile profile)
	{
		this (profile.mutable);

		if (profile == null)
		{
			throw new NullPointerException ("The specified profile is NULL");
		}

		this.entries.putAll (profile.entries);
	}

	/**
	 *  Create the <code>DomainModelProfile</code>, from another profile, but
	 *  overriding the mutability.  This method is intended to be used by the
	 *  builder to copy the profile.
	 *
	 *  @param  mutable The designed mutability of the <code>DomainModel</code>,
	 *                  not null
	 *  @param  profile The profile to copy, not null
	 */

	protected DomainModelProfile (Boolean mutable, DomainModelProfile profile)
	{
		this (mutable);

		if (profile == null)
		{
			throw new NullPointerException ("The specified profile is NULL");
		}

		this.entries.putAll (profile.entries);
	}

	/**
	 * Override the equals method to determine if this
	 * <code>DomainModelProfile</code> is equal to another based on its
	 * attributes.
	 *
	 * @param  obj The object to compare to this <code>DomainModelProfile</code>
	 * @return     <code>true</code> if the two profiles are the same,
	 *             <code>false</code> otherwise
	 */

	@Override
	public boolean equals (Object obj)
	{
		boolean result = false;

		if (obj != null)
		{
			if (obj == this)
			{
				result = true;
			}
			else if (obj.getClass () == this.getClass ())
			{
				EqualsBuilder ebuilder = new EqualsBuilder ();
				ebuilder.append (this.mutable, ((DomainModelProfile) obj).mutable);
				ebuilder.append (this.entries, ((DomainModelProfile) obj).entries);

				result = ebuilder.isEquals ();
			}
		}

		return result;
	}

	/**
	 * Compute a unique <code>hashCode</code> for a
	 * <code>DomainModelProfile</code> based on its attributes.
	 *
	 * @return The hash code
	 */

	@Override
	public int hashCode ()
	{
		final int base = 907;
		final int mult = 5;

		HashCodeBuilder hbuilder = new HashCodeBuilder (base, mult);
		hbuilder.append (this.mutable);
		hbuilder.append (this.entries);

		return hbuilder.toHashCode ();
	}

	/**
	 * Determine if the <code>DomainModel</code> and its underlying
	 * <code>DataStore</code> can be changed.
	 *
	 * @return <code>true</code> if the <code>DomainModel</code> and its
	 *         underlying <code>DataStore</code> can be changed,
	 *         <code>false</code> otherwise
	 */

	public Boolean isMutable ()
	{
		return this.mutable;
	}

	/**
	 * Get the set of elements contained in this profile.
	 *
	 * @return A <code>Set</code> containing the <code>DomainModelType</code> of
	 *         all of the elements in the profile
	 */

	public Set<DomainModelType> getElements ()
	{
		return this.entries.keySet ();
	}

	/**
	 * Determine if the <code>DataStore</code> contains a representation of a
	 * given domain model interface.
	 *
	 * @param  element                  Domain model interface class, not null
	 * @return                          <code>true</code> if the
	 *                                  <code>DataStore</code> contains a
	 *                                  representation of the specified domain
	 *                                  model interface, <code>false</code>
	 *                                  otherwise
	 * @throws IllegalArgumentException if the element is not in the profile
	 */

	public Boolean isAvailable (DomainModelType element)
	{
		if (element == null)
		{
			throw new NullPointerException ();
		}

		if (! this.entries.containsKey (element))
		{
			throw new IllegalArgumentException ("Element is not in the profile: " + element.getName ());
		}

		return (this.entries.get (element)).isAvailable ();
	}

	/**
	 * Get the <code>DataStore</code> ID generation class for the specified domain
	 * model interface.
	 *
	 * @param  element                  Domain model interface class, not null
	 * @return                          The associated ID generator class
	 * @throws IllegalArgumentException if the element is not in the profile
	 */

	public Class<? extends IdGenerator> getGenerator (DomainModelType element)
	{
		if (element == null)
		{
			throw new NullPointerException ();
		}

		if (! this.entries.containsKey (element))
		{
			throw new IllegalArgumentException ("Element is not in the profile: " + element.getName ());
		}

		return (this.entries.get (element)).getGenerator ();
	}

	/**
	 * Get the implementation class to be used for the specified domain model
	 * interface.
	 *
	 * @param  element                  Domain model interface class, not null
	 * @return                          The class used to represent the interface
	 *                                  in the <code>DataStore</code>
	 * @throws IllegalArgumentException if the element is not in the profile
	 */

	public Class<? extends DomainModelElement> getImplClass (DomainModelType element)
	{
		if (element == null)
		{
			throw new NullPointerException ();
		}

		if (! this.entries.containsKey (element))
		{
			throw new IllegalArgumentException ("Element is not in the profile: " + element.getName ());
		}

		return (this.entries.get (element)).getImplClass ();
	}

	/**
	 * Add an entry to the profile from the specified element type.  This method
	 * is intended to be used by the builder while is constructs the profile.
	 *
	 * @param  element                  The <code>DomainModelType</code> of the
	 *                                  element which is being added, not null
	 * @param  available                Indication if the element is available in
	 *                                  the <code>DataStore</code>, not null
	 * @param  impl                     The implementation class to be used with
	 *                                  the element, not null
	 * @param  generator                The <code>IdGenerator</code> to be used
	 *                                  for the element, not null
	 * @throws IllegalArgumentException if the implementation class does not
	 *                                  implement the interface associated with
	 *                                  element
	 * @see    DomainModelBuilder#setEntry
	 */

	protected void addEntry (DomainModelType element, Boolean available, Class<? extends DomainModelElement> impl, Class<? extends IdGenerator> generator)
	{
		if (element == null)
		{
			throw new NullPointerException ("The specified element is NULL");
		}

		if (available == null)
		{
			throw new NullPointerException ("The specified availability is NULL");
		}

		if (impl == null)
		{
			throw new NullPointerException ("The specified implementation class is NULL");
		}

		if (generator == null)
		{
			throw new NullPointerException ("The specified generator is NULL");
		}

		if (! (element.getInterfaceClass ()).isAssignableFrom (impl))
		{
			throw new IllegalArgumentException (impl.getName () + " does not implement " + element.getName ());
		}

		this.entries.put (type, new Entry (available, generator, impl));
	}
}
