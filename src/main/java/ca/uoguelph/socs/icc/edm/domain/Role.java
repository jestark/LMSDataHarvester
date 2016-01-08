/* Copyright (C) 2014, 2015, 2016 James E. Stark
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
import java.util.function.Function;
import java.util.stream.Stream;
import javax.inject.Named;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;

import dagger.Component;
import dagger.Module;
import dagger.Provides;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;

import ca.uoguelph.socs.icc.edm.domain.datastore.Retriever;
import ca.uoguelph.socs.icc.edm.domain.datastore.idgenerator.IdGenerator;
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

	public static class Builder extends Element.Builder<Role>
	{
		/** The <code>IdGenerator</code>*/
		private final IdGenerator idGenerator;

		/** The <code>DataStore</code> id number for the <code>Role</code> */
		private @Nullable Long id;

		/** The name of the <code>Role</code> */
		private @Nullable String name;

		/**
		 * Create the <code>Builder</code>.
		 *
		 * @param  model       The <code>DomainModel</code>, not null
		 * @param  definition  The <code>Definition</code>, not null
		 * @param  idGenerator The <code>IdGenerator</code>, not null
		 * @param  retriever   The <code>Retriever</code>, not null
		 */

		protected Builder (
				final DomainModel model,
				final Definition definition,
				final IdGenerator idGenerator,
				final Retriever<Role> retriever)
		{
			super (model, definition, retriever);

			assert idGenerator != null : "idGenerator is NULL";
			this.idGenerator = idGenerator;

			this.id = null;
			this.name = null;
		}

		/**
		 * Create an instance of the <code>Role</code>.
		 *
		 * @return The new <code>Role</code> instance
		 *
		 * @throws NullPointerException if any required field is missing
		 */

		@Override
		protected Role create ()
		{
			this.log.trace ("create:");

			return ((Definition) this.definition).creator.apply (this);
		}

		/**
		 * Implementation of the pre-insert hook to set the ID number.
		 *
		 * @param  role The <code>Role</code> to be inserted, not null
		 * @return      The <code>Role</code> to be inserted
		 */

		@Override
		protected Role preInsert (final Role role)
		{
			assert role != null : "role is NULL";

			this.log.debug ("Setting ID");
			role.setId (this.idGenerator.nextId ());

			return role;
		}

		/**
		 * Reset the builder.  This method will set all of the fields for the
		 * <code>Element</code> to be built to <code>null</code>.
		 *
		 * @return This <code>Builder</code>
		 */

		@Override
		public Builder clear ()
		{
			this.log.trace ("clear:");

			super.clear ();

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
		 * @param  role The <code>Role</code>, not null
		 * @return      This <code>Builder</code>
		 *
		 * @throws IllegalArgumentException If any of the fields in the
		 *                                  <code>Role</code> instance to be
		 *                                  loaded are not valid
		 */

		@Override
		public Builder load (final Role role)
		{
			this.log.trace ("load: role={}", role);

			super.load (role);

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
		public final Long getId ()
		{
			return this.id;
		}

		/**
		 * Get the name of the <code>Role</code>.
		 *
		 * @return A <code>String</code> containing the name of the
		 *         <code>Role</code>
		 */

		@CheckReturnValue
		public final String getName ()
		{
			return this.name;
		}

		/**
		 * Set the name of the <code>Role</code>.
		 *
		 * @param  name The name of the <code>Role</code>, not null
		 * @return      This <code>Builder</code>
		 *
		 * @throws IllegalArgumentException If the name is empty
		 */

		public final Builder setName (final String name)
		{
			this.log.trace ("setName: name={}", name);

			Preconditions.checkNotNull (name);
			Preconditions.checkArgument (name.length () > 0, "name is empty");

			this.name = name;

			return this;
		}
	}

	/**
	 * Dagger Component interface for creating <code>Builder</code> instances.
	 *
	 * @author  James E. Stark
	 * @version 1.0
	 */

	@ElementScope
	@Component (dependencies = {IdGenerator.IdGeneratorComponent.class}, modules = {RoleBuilderModule.class})
	protected interface RoleComponent extends Element.ElementComponent<Role>
	{
		/**
		 * Create the Builder instance.
		 *
		 * @return The <code>Builder</code>
		 */

		@Override
		public abstract Builder getBuilder ();
	}

	/**
	 * Dagger module for creating <code>Retriever</code> instances.  This module
	 * contains implementation-independent information.
	 *
	 * @author  James E. Stark
	 * @version 1.0
	 */

	@Module
	public static final class RoleModule extends Element.ElementModule<Role>
	{
		/**
		 * Get the <code>Selector</code> used by the
		 * <code>QueryRetriever</code>.
		 *
		 * @return The <code>Selector</code>
		 */

		@Provides
		public Selector<Role> getSelector ()
		{
			return Role.SELECTOR_NAME;
		}
	}

	/**
	 * Dagger module for creating <code>Builder</code> instances.  This module
	 * contains implementation-dependent information.
	 *
	 * @author  James E. Stark
	 * @version 1.0
	 */

	@Module (includes = {RoleModule.class})
	public static final class RoleBuilderModule
	{
		/** The <code>Definition</code> */
		private final Definition definition;

		/**
		 * Create the <code>RoleBuilderModule</code>
		 *
		 * @param  definition The <code>Definition</code>, not null
		 */

		public RoleBuilderModule (final Definition definition)
		{
			assert definition != null : "definition is NULL";
			this.definition = definition;
		}

		/**
		 * Create the <code>Builder</code>.
		 *
		 * @param  model     The <code>DomainModel</code>, not null
		 * @param  generator The <code>IdGenerator</code>, not null
		 * @param  retriever The <code>Retriever</code>, not null
		 */

		@Provides
		public Builder createBuilder (
				final DomainModel model,
				final IdGenerator generator,
				final @Named ("QueryRetriever") Retriever<Role> retriever)
		{
			return new Builder (model, this.definition, generator, retriever);
		}
	}

	/**
	 * Abstract representation of an <code>Element</code> implementation class.
	 * Instances of this class are used to load the <code>Element</code>
	 * implementations into the JVM via the <code>ServiceLoader</code>.
	 *
	 * @author  James E. Stark
	 * @version 1.0
	 */

	protected static abstract class Definition extends Element.Definition<Role>
	{
		/** Method reference to the implementation constructor  */
		private final Function<Role.Builder, Role> creator;

		/**
		 * Create the <code>Definition</code>.
		 *
		 * @param  impl    The implementation class, not null
		 * @param  creator Method reference to the constructor, not null
		 */

		public Definition (
				final Class<? extends Role> impl,
				final Function<Role.Builder, Role> creator)
		{
			super (Role.METADATA, impl);

			assert creator != null : "creator is NULL";
			this.creator = creator;
		}

		/**
		 * Create a new instance of the <code>Component</code> on the
		 * specified <code>DomainModel</code>.
		 *
		 * @param model The <code>DomainModel</code>, not null
		 * @return      The <code>Component</code>
		 */

		@Override
		protected Role.RoleComponent getComponent (final DomainModel model)
		{
			return DaggerRole_RoleComponent.builder ()
				.idGeneratorComponent (model.getIdGeneratorComponent (this.impl))
				.domainModelModule (new DomainModel.DomainModelModule (Role.class, model))
				.roleBuilderModule (new RoleBuilderModule (this))
				.build ();
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
	 * @param  model The <code>DomainModel</code>, not null
	 * @return       The <code>Builder</code> instance
	 *
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

		return (Role.Builder) model.getElementComponent (Role.class)
			.getBuilder ();
	}

	/**
	 * Create the <code>Role</code>.
	 */

	protected Role ()
	{
		super ();
	}

	/**
	 * Create the <code>Role</code> instance from the specified
	 * <code>Builder</code>.
	 *
	 * @param  builder The <code>Builder</code>, not null
	 */

	protected Role (final Builder builder)
	{
		super (builder);
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
	 * Connect all of the relationships for this <code>Role</code> instance.
	 * This method is intended to be used just after the <code>Role</code> is
	 * inserted into the <code>DataStore</code>.
	 *
	 * @return <code>true</code> if all of the relationships were successfully
	 *         connected, <code>false</code> otherwise
	 */

	@Override
	protected boolean connect ()
	{
		return Role.METADATA.relationships ()
				.allMatch (r -> r.connect (this));
	}

	/**
	 * Disconnect all of the relationships for this <code>Role</code> instance.
	 * This method is intended to be used just before the <code>Role</code> is
	 * removed from the <code>DataStore</code>.
	 *
	 * @return <code>true</code> if all of the relationships were successfully
	 *         disconnected, <code>false</code> otherwise
	 */

	@Override
	protected boolean disconnect ()
	{
		return Role.METADATA.relationships ()
				.allMatch (r -> r.disconnect (this));
	}

	/**
	 * Determine if this <code>Element</code> depends on the specified
	 * <code>Element</code> class.  This method is used by
	 * <code>compareTo</code> to order different <code>Element</code> classes
	 * based on their dependencies.
	 *
	 * @param  element The <code>Element</code> implementation class, not null
	 * @return         <code>true</code> if this<code>Element</code> depends on
	 *                 the specified class, <code>false</code> otherwise
	 */

	@Override
	protected boolean isDependency (final Class <? extends Element> element)
	{
		return this.isDependencyHelper (Role.METADATA, element);
	}

	/**
	 * Compare two <code>Role</code> instances, based upon their names.
	 *
	 * @param  element The <code>Element</code> to be compared
	 * @return         The value 0 if the <code>Role</code> instances are equal,
	 *                 less than 0 of the argument is is greater, and greater
	 *                 than 0 if the argument is less than the <code>Role</code>
	 */

	@Override
	public int compareTo (final Element element)
	{
		Preconditions.checkNotNull (element, "element");

		int result = 0;

		if (this != element)
		{
			if (element instanceof Role)
			{
				result = this.getName ().compareTo (((Role) element).getName ());
			}
			else
			{
				result = super.compareTo (element);
			}
		}

		return result;
	}

	/**
	 * Compare two <code>Role</code> instances to determine if they are
	 * equal.  The <code>Role</code> instances are compared based upon their
	 * names.
	 *
	 * @param  obj The <code>Role</code> instance to compare to the one
	 *             represented by the called instance
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
	 * Get a <code>Stream</code> containing all of the associated
	 * <code>Element</code> instances.
	 *
	 * @return The <code>Stream</code>
	 */

	@Override
	public Stream<Element> associations ()
	{
		return Role.METADATA.properties ()
			.filter (p -> p.hasFlags (Property.Flags.RELATIONSHIP))
			.flatMap (p -> p.stream (this))
			.map (e -> ((Element) e));
	}

	/**
	 * Get an <code>Builder</code> instance for the specified
	 * <code>DomainModel</code>.  This method creates an <code>Builder</code> on
	 * the specified <code>DomainModel</code> and initializes it with the
	 * contents of this <code>Role</code> instance.
	 *
	 * @param  model The <code>DomainModel</code>, not null
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

