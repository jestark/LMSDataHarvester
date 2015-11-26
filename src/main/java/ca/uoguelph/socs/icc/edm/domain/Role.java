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

package ca.uoguelph.socs.icc.edm.domain;

import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Stream;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.datastore.Persister;
import ca.uoguelph.socs.icc.edm.domain.metadata.MetaData;
import ca.uoguelph.socs.icc.edm.domain.metadata.Property;
import ca.uoguelph.socs.icc.edm.domain.metadata.Selector;

/**
 * A representation of the role of a user in a particular <code>Course</code>.
 * The <code>Role</code> describes the nature of the participation in the
 * <code>Course</code> by the <code>User</code>.  Common roles include:
 * Instructor, Student and Teaching Assistant.
 * <p>
 * Within the domain model the <code>Role</code> interface is a root level
 * element, as such instances of the <code>Role</code> interface are not
 * dependant upon any other domain model element to exist.  An associated
 * instance of the <code>Role</code> interface is required for an instance of
 * the <code>Enrolment</code> interface to exist.  If a particular instance of
 * the <code>Role</code> interface is deleted, then all of the associated
 * instances of the <code>Enrolment</code> interface must be deleted as well.
 * <p>
 * Once created, <code>Role</code> instances are immutable.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public abstract class Role extends Element
{
	/**
	 * Create new <code>Role</code> instances.  This class extends
	 * <code>Builder</code>, adding the functionality required to
	 * create <code>Role</code> instances.
	 *
	 * @author  James E. Stark
	 * @version 1.0
	 */

	public static final class Builder implements Element.Builder<Role>
	{
		/** The Logger */
		private final Logger log;

		/** Helper to operate on <code>Role</code> instances*/
		private final Persister<Role> persister;

		/** Method reference to the constructor of the implementation class */
		private final Supplier<Role> supplier;

		/** The loaded of previously created <code>Role</code> */
		private Role role;

		/** The <code>DataStore</code> id number for the <code>Role</code> */
		private Long id;

		/** The name of the <code>Role</code> */
		private String name;

		/**
		 * Create the <code>Builder</code>.
		 *
		 * @param  supplier  Method reference to the constructor of the
		 *                   implementation class, not null
		 * @param  persister The <code>Persister</code> used to store the
		 *                   <code>Role</code>, not null
		 */

		protected Builder (final Supplier<Role> supplier, final Persister<Role> persister)
		{
			assert supplier != null : "supplier is NULL";
			assert persister != null : "persister is NULL";

			this.log = LoggerFactory.getLogger (this.getClass ());

			this.persister = persister;
			this.supplier = supplier;

			this.role = null;
			this.id = null;
			this.name = null;
		}

		/**
		 * Create an instance of the <code>Role</code>.
		 *
		 * @return                       The new <code>Role</code> instance
		 * @throws IllegalStateException If any if the fields is missing
		 * @throws IllegalStateException If there isn't an active transaction
		 */

		@Override
		public Role build ()
		{
			this.log.trace ("build:");

			if (this.name == null)
			{
				this.log.error ("Attempting to create an Role without a name");
				throw new IllegalStateException ("name is NULL");
			}

			Role result = this.supplier.get ();
			result.setId (this.id);
			result.setName (this.name);

			this.role = this.persister.insert (this.role, result);

			return this.role;
		}

		/**
		 * Reset the builder.  This method will set all of the fields for the
		 * <code>Element</code> to be built to <code>null</code>.
		 *
		 * @return This <code>Builder</code>
		 */

		public Builder clear ()
		{
			this.log.trace ("clear:");

			this.role = null;
			this.id = null;
			this.name = null;

			return this;
		}

		/**
		 * Load a <code>Role</code> instance into the builder.  This method
		 * resets the builder and initializes all of its parameters from
		 * the specified <code>Role</code> instance.  The  parameters are
		 * validated as they are set.
		 *
		 * @param  role                     The <code>Role</code>, not null
		 *
		 * @return                          This <code>Builder</code>
		 * @throws IllegalArgumentException If any of the fields in the
		 *                                  <code>Role</code> instance to be
		 *                                  loaded are not valid
		 */

		public Builder load (final Role role)
		{
			this.log.trace ("load: role={}", role);

			if (role == null)
			{
				this.log.error ("Attempting to load a NULL Role");
				throw new NullPointerException ();
			}

			this.role = role;
			this.id = role.getId ();
			this.setName (role.getName ());

			return this;
		}

		/**
		 * Get the <code>DataStore</code> identifier for the <code>Role</code>
		 * instance.
		 *
		 * @return The <code>DataStore</code> identifier
		 */

		@CheckReturnValue
		public Long getId ()
		{
			return this.id;
		}

		/**
		 * Get the name of the <code>Role</code>.
		 *
		 * @return A <code>String</code> containing the name of the
		 *         <code>Role</code>
		 */

		public String getName ()
		{
			return this.name;
		}

		/**
		 * Set the name of the <code>Role</code>.
		 *
		 * @param  name                     The name of the <code>Role</code>,
		 *                                  not null
		 *
		 * @return                          This <code>Builder</code>
		 * @throws IllegalArgumentException If the name is empty
		 */

		public Builder setName (final String name)
		{
			this.log.trace ("setName: name={}", name);

			if (name == null)
			{
				this.log.error ("name is NULL");
				throw new NullPointerException ("Name is NULL");
			}

			if (name.length () == 0)
			{
				this.log.error ("name is an empty string");
				throw new IllegalArgumentException ("name is empty");
			}

			this.name = name;

			return this;
		}
	}

	/** Serial version id, required by the Serializable interface */
	private static final long serialVersionUID = 1L;

	/** The <code>MetaData</code> for the <code>Role</code> */
	protected static final MetaData<Role> METADATA;

	/** The <code>DataStore</code> identifier of the <code>Role</code> */
	public static final Property<Role, Long> ID;

	/** The <code>DomainModel</code> which contains the <code>Role</code> */
	public static final Property<Role, DomainModel> MODEL;

	/** The name of the <code>Role</code> */
	public static final Property<Role, String> NAME;

	/** Select the <code>Role</code> instance by its id */
	public static final Selector<Role> SELECTOR_ID;

	/** Select all of the <code>Role</code> instances */
	public static final Selector<Role> SELECTOR_ALL;

	/** Select an <code>Role</code> instance by its name */
	public static final Selector<Role> SELECTOR_NAME;

	/**
	 * Initialize the <code>MetaData</code>, <code>Property</code> and
	 * <code>Selector</code> instances for the <code>Role</code>.
	 */

	static
	{
		ID = Property.of (Role.class, Long.class, "id",
				Role::getId, Role::setId);

		MODEL = Property.of (Role.class, DomainModel.class, "domainmodel",
				Role::getDomainModel, Role::setDomainModel);

		NAME = Property.of (Role.class, String.class, "name",
				Role::getName, Role::setName,
				Property.Flags.REQUIRED);

		SELECTOR_ID = Selector.of (Selector.Cardinality.KEY, ID);
		SELECTOR_NAME = Selector.of (Selector.Cardinality.SINGLE, NAME);

		SELECTOR_ALL = Selector.builder (Role.class)
			.setCardinality (Selector.Cardinality.MULTIPLE)
			.setName ("all")
			.build ();

		METADATA = MetaData.builder (Role.class)
			.addProperty (ID)
			.addProperty (MODEL)
			.addProperty (NAME)
			.addSelector (SELECTOR_ID)
			.addSelector (SELECTOR_ALL)
			.addSelector (SELECTOR_NAME)
			.build ();
	}

	/**
	 * Get an instance of the <code>Builder</code> for the specified
	 * <code>DomainModel</code>.
	 *
	 * @param  model                 The <code>DomainModel</code>, not null
	 *
	 * @return                       The <code>Builder</code> instance
	 * @throws IllegalStateException if the <code>DomainModel</code> is closed
	 * @throws IllegalStateException if the <code>DomainModel</code> does not
	 *                               have a default implementation class for
	 *                               the <code>Role</code>
	 * @throws IllegalStateException if the <code>DomainModel</code> is
	 *                               immutable
	 */

	public static Builder builder (final DomainModel model)
	{
		Preconditions.checkNotNull (model, "model");

		return null;
	}

	/**
	 * Create the <code>Role</code>.
	 */

	protected Role ()
	{
		super ();
	}

	/**
	 * Template method to create and initialize a <code>ToStringHelper</code>.
	 *
	 * @return The <code>ToStringHelper</code>
	 */

	@Override
	@CheckReturnValue
	protected MoreObjects.ToStringHelper toStringHelper ()
	{
		return super.toStringHelper ()
			.add ("name", this.getName ());
	}

	/**
	 * Compare two <code>Role</code> instances to determine if they are
	 * equal.  The <code>Role</code> instances are compared based upon their
	 * names.
	 *
	 * @param  obj The <code>Role</code> instance to compare to the one
	 *             represented by the called instance
	 *
	 * @return     <code>True</code> if the two <code>Role</code> instances
	 *             are equal, <code>False</code> otherwise
	 */

	@Override
	public boolean equals (final Object obj)
	{
		return (obj == this) ? true : (obj instanceof Role)
			&& Objects.equals (this.getName (), ((Role) obj).getName ());
	}

	/**
	 * Compute a <code>hashCode</code> of the <code>Role</code> instance.
	 * The hash code is computed based upon the name of the instance.
	 *
	 * @return An <code>Integer</code> containing the hash code
	 */

	@Override
	public int hashCode ()
	{
		return Objects.hash (this.getName ());
	}

	/**
	 * Get a <code>String</code> representation of the <code>Role</code>
	 * instance, including the identifying fields.
	 *
	 * @return A <code>String</code> representation of the <code>Role</code>
	 *         instance
	 */

	@Override
	@CheckReturnValue
	public String toString()
	{
		return this.toStringHelper ()
			.toString ();
	}

	/**
	 * Get the <code>Set</code> of <code>Property</code> instances associated
	 * with the <code>Element</code> interface class.
	 *
	 * @return The <code>Set</code> of <code>Property</code> instances
	 *         associated with the <code>Element</code> interface class
	 */

	@Override
	public Stream<Property<? extends Element, ?>> properties ()
	{
		return Role.METADATA.properties ();
	}

	/**
	 * Get the <code>Set</code> of <code>Selector</code> instances associated
	 * with the <code>Element</code> interface class.
	 *
	 * @return The <code>Set</code> of <code>Selector</code> instances
	 *         associated with the <code>Element</code> interface class
	 */

	@Override
	public Stream<Selector<? extends Element>> selectors ()
	{
		return Role.METADATA.selectors ();
	}

	/**
	 * Get an <code>Builder</code> instance for the specified
	 * <code>DomainModel</code>.  This method creates an <code>Builder</code> on
	 * the specified <code>DomainModel</code> and initializes it with the
	 * contents of this <code>Role</code> instance.
	 *
	 * @param  model The <code>DomainModel</code>, not null
	 *
	 * @return       The initialized <code>Builder</code>
	 */

	@Override
	public Builder getBuilder (final DomainModel model)
	{
		return Role.builder (Preconditions.checkNotNull (model, "model"))
			.load (this);
	}

	/**
	 * Get the name of the <code>Role</code>.
	 *
	 * @return A <code>String</code> containing the name of the
	 *         <code>Role</code>
	 */

	public abstract String getName ();

	/**
	 * Set the name of the <code>Role</code>.  This method is intended to be
	 * used to initialize a new <code>Role</code> instance.
	 *
	 * @param name The name of the <code>Role</code>
	 */

	protected abstract void setName (String name);
}

