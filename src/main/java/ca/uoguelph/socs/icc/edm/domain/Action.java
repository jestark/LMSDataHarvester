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

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;
import javax.inject.Named;

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
 * A representation of the <code>Action</code> that a <code>User</code>
 * performed upon an <code>Activity</code> as recored in a
 * <code>LogEntry</code>.  Some instances of the <code>Action</code> interface
 * are general (such as viewing the content associated with an instance of the
 * <code>Activity</code> interface) and will be associated with may instances
 * of the <code>ActivityType</code> interface.  Other instances of the
 * <code>Action</code> may be specific to one particular instance of a
 * <code>ActvityType</code>, such as submitting an assignment.
 * <p>
 * Within the domain model the <code>Action</code> interface is a root level
 * element, as such instances of the <code>Action</code> interface are not
 * dependant upon any other domain model element to exist.  An associated
 * instance of the <code>Action</code> interface is required for an instance of
 * the <code>LogEntry</code> interface to exist.  If a particular instance of
 * the <code>Action</code> interface is deleted, then all of the associated
 * instances of the <code>LogEntry</code> interface must be deleted as well.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public abstract class Action extends Element
{
	/**
	 * Create new <code>Action</code> instances.  This class extends
	 * <code>AbstractBuilder</code>, adding the functionality required to
	 * create <code>Action</code> instances.
	 *
	 * @author  James E. Stark
	 * @version 1.0
	 */

	public static class Builder extends Element.Builder<Action>
	{
		/** The <code>IdGenerator</code>*/
		private final IdGenerator idGenerator;

		/** The <code>DataStore</code> id number for the <code>Action</code> */
		private @Nullable Long id;

		/** The name of the <code>Action</code> */
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
				final Retriever<Action> retriever)
		{
			super (model, definition, retriever);

			assert idGenerator != null : "idGenerator is NULL";
			this.idGenerator = idGenerator;

			this.id = null;
			this.name = null;
		}

		/**
		 * Create an instance of the <code>Action</code>.
		 *
		 * @return The new <code>Action</code> instance
		 *
		 * @throws NullPointerException if any required field is missing
		 */

		@Override
		protected Action create ()
		{
			this.log.trace ("create:");

			return ((Definition) this.definition).creator.apply (this);
		}

		/**
		 * Implementation of the pre-insert hook to set the ID number.
		 *
		 * @param  action The <code>Action</code> to be inserted, not null
		 * @return        The <code>Action</code> to be inserted
		 */

		@Override
		protected Action preInsert (final Action action)
		{
			assert action != null : "action is NULL";

			this.log.debug ("Setting ID");
			action.setId (this.idGenerator.nextId ());

			return action;
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
		 * Load an <code>Action</code> instance into the builder.  This method
		 * resets the builder and initializes all of its parameters from
		 * the specified <code>Action</code> instance.  The  parameters are
		 * validated as they are set.
		 *
		 * @param  action The <code>Action</code>, not null
		 * @return        This <code>Builder</code>
		 *
		 * @throws IllegalArgumentException If any of the fields in the
		 *                                  <code>Action</code> instance to be
		 *                                  loaded are not valid
		 */

		@Override
		public Builder load (final Action action)
		{
			this.log.trace ("load: action={}", action);

			super.load (action);

			this.id = action.getId ();
			this.setName (action.getName ());

			return this;
		}

		/**
		 * Get the <code>DataStore</code> identifier for the <code>Action</code>
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
		 * Get the name of the <code>Action</code>.
		 *
		 * @return A String containing the name of the <code>Action</code>
		 */

		@CheckReturnValue
		public final String getName ()
		{
			return this.name;
		}

		/**
		 * Set the name of the <code>Action</code>.
		 *
		 * @param  name The name of the <code>Action</code>, not null
		 * @return      This <code>Builder</code>
		 *
		 * @throws IllegalArgumentException If the name is empty
		 */

		public final Builder setName (final String name)
		{
			this.log.trace ("setName: name={}", name);

			Preconditions.checkNotNull (name, "name");
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
	@Component (dependencies = {IdGenerator.IdGeneratorComponent.class}, modules = {ActionBuilderModule.class})
	protected static interface ActionComponent extends Element.ElementComponent<Action>
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
	public static final class ActionModule extends Element.ElementModule<Action>
	{
		/**
		 * Get the <code>Selector</code> used by the
		 * <code>QueryRetriever</code>.
		 *
		 * @return The <code>Selector</code>
		 */

		@Provides
		public Selector<Action> getSelector ()
		{
			return Action.SELECTOR_NAME;
		}
	}

	/**
	 * Dagger module for creating <code>Builder</code> instances.  This module
	 * contains implementation-dependent information.
	 *
	 * @author  James E. Stark
	 * @version 1.0
	 */

	@Module (includes = {ActionModule.class})
	public static final class ActionBuilderModule
	{
		/** The <code>Definition</code> */
		private final Definition definition;

		/**
		 * Create the <code>ActionBuilderModule</code>
		 *
		 * @param  definition The <code>Definition</code>, not null
		 */

		public ActionBuilderModule (final Definition definition)
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
				final @Named ("QueryRetriever") Retriever<Action> retriever)
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

	protected static abstract class Definition extends Element.Definition<Action>
	{
		/** Method reference to the implementation constructor  */
		private final Function<Action.Builder, Action> creator;

		/**
		 * Create the <code>Definition</code>.
		 *
		 * @param  impl    The implementation class, not null
		 * @param  creator Method reference to the constructor, not null
		 */

		public Definition (
				final Class<? extends Action> impl,
				final Function<Action.Builder, Action> creator)
		{
			super (Action.METADATA, impl);

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
		protected Action.ActionComponent getComponent (final DomainModel model)
		{
			return DaggerAction_ActionComponent.builder ()
				.idGeneratorComponent (model.getIdGeneratorComponent (this.impl))
				.domainModelModule (new DomainModel.DomainModelModule (Action.class, model))
				.actionBuilderModule (new ActionBuilderModule (this))
				.build ();
		}
	}

	/** Serial version id, required by the Serializable interface */
	private static final long serialVersionUID = 1L;

	/** The <code>MetaData</code> for the <code>Action</code>*/
	protected static final MetaData<Action> METADATA;

	/** The <code>DataStore</code> identifier of the <code>Action</code> */
	public static final Property<Action, Long> ID;

	/** The <code>DomainModel</code> which contains the <code>Action</code> */
	public static final Property<Action, DomainModel> MODEL;

	/** The name of the <code>Action</code> */
	public static final Property<Action, String> NAME;

	/** Select the <code>Action</code> instance by its id */
	public static final Selector<Action> SELECTOR_ID;

	/** Select all of the <code>Action</code> instances */
	public static final Selector<Action> SELECTOR_ALL;

	/** Select an <code>Action</code> instance by its name */
	public static final Selector<Action> SELECTOR_NAME;

	/**
	 * Initialize the <code>MetaData</code>, <code>Property</code> and
	 * <code>Selector</code> instances for the <code>Action</code>.
	 */

	static
	{
		ID = Property.of (Action.class, Long.class, "id",
				Action::getId, Action::setId);

		MODEL = Property.of (Action.class, DomainModel.class, "domainmodel",
				Action::getDomainModel, Action::setDomainModel);

		NAME = Property.of (Action.class, String.class, "name",
				Action::getName, Action::setName, Property.Flags.REQUIRED);

		SELECTOR_ID = Selector.of (Selector.Cardinality.KEY, ID);
		SELECTOR_NAME = Selector.of (Selector.Cardinality.SINGLE, NAME);

		SELECTOR_ALL = Selector.builder (Action.class)
			.setCardinality (Selector.Cardinality.MULTIPLE)
			.setName ("all")
			.build ();

		METADATA = MetaData.builder (Action.class)
			.addProperty (ID)
			.addProperty (MODEL)
			.addProperty (NAME)
			.addSelector (SELECTOR_NAME)
			.addSelector (SELECTOR_ID)
			.addSelector (SELECTOR_ALL)
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
	 *                               the <code>Action</code>
	 * @throws IllegalStateException if the <code>DomainModel</code> is
	 *                               immutable
	 */

	public static Builder builder (final DomainModel model)
	{
		Preconditions.checkNotNull (model, "model");

		return (Action.Builder) model.getElementComponent (Action.class)
			.getBuilder ();
	}

	/**
	 * Create the <code>Action</code>.
	 */

	protected Action ()
	{
		super ();
	}

	/**
	 * Create the <code>Action</code> instance from the specified
	 * <code>Builder</code>.
	 *
	 * @param  builder The <code>Builder</code>, not null
	 */

	protected Action (final Builder builder)
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
	 * Connect all of the relationships for this <code>Action</code> instance.
	 * This method is intended to be used just after the <code>Action</code> is
	 * inserted into the <code>DataStore</code>.
	 *
	 * @return <code>true</code> if all of the relationships were successfully
	 *         connected, <code>false</code> otherwise
	 */

	@Override
	protected boolean connect ()
	{
		return Action.METADATA.relationships ()
				.allMatch (r -> r.connect (this));
	}

	/**
	 * Disconnect all of the relationships for this <code>Action</code>
	 * instance.  This method is intended to be used just before the
	 * <code>Action</code> is removed from the <code>DataStore</code>.
	 *
	 * @return <code>true</code> if all of the relationships were successfully
	 *         disconnected, <code>false</code> otherwise
	 */

	@Override
	protected boolean disconnect ()
	{
		return Action.METADATA.relationships ()
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
		return Action.METADATA.dependencies ()
			.anyMatch (d -> d.isAssignableFrom (element));
	}

	/**
	 * Compare two <code>Action</code> instances, based upon their names.
	 *
	 * @param  element The <code>Action</code> to be compared
	 * @return         The value 0 if the <code>Action</code> instances are
	 *                 equal, less than 0 of the argument is is greater, and
	 *                 greater than 0 if the argument is less than the
	 *                 <code>Action</code>
	 */

	@Override
	public int compareTo (final Element element)
	{
		Preconditions.checkNotNull (element, "element");

		int result = 0;

		if (this != element)
		{
			if (element instanceof Action)
			{
				result = this.getName ().compareTo (((Action) element).getName ());
			}
			else
			{
				result = super.compareTo (element);
			}
		}

		return result;
	}

	/**
	 * Compare two <code>Action</code> instances to determine if they are
	 * equal.  The <code>Action</code> instances are compared based upon their
	 * names.
	 *
	 * @param  obj The <code>Action</code> instance to compare to the one
	 *             represented by the called instance
	 * @return     <code>True</code> if the two <code>Action</code> instances
	 *             are equal, <code>False</code> otherwise
	 */

	@Override
	public boolean equals (final Object obj)
	{
		return (obj == this) ? true : (obj instanceof Action)
			&& Objects.equals (this.getName (), ((Action) obj).getName ());
	}

	/**
	 * Compute a <code>hashCode</code> of the <code>Action</code> instance.
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
	 * Get a <code>String</code> representation of the <code>Action</code>
	 * instance, including the identifying fields.
	 *
	 * @return A <code>String</code> representation of the <code>Action</code>
	 *         instance
	 */

	@Override
	@CheckReturnValue
	public String toString ()
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
		return Action.METADATA.properties ()
			.filter (p -> p.hasFlags (Property.Flags.RELATIONSHIP))
			.flatMap (p -> p.stream (this))
			.map (e -> ((Element) e));
	}

	/**
	 * Get an <code>Builder</code> instance for the specified
	 * <code>DomainModel</code>.  This method creates an
	 * <code>Builder</code> on the specified <code>DomainModel</code>
	 * and initializes it with the contents of this <code>Enrolment</code>
	 * instance.
	 *
	 * @param  model The <code>DomainModel</code>, not null
	 * @return       The initialized <code>Builder</code>
	 */
	/**
	 * Get an <code>Builder</code> instance for the specified
	 * <code>DomainModel</code>.  This method creates an
	 * <code>Builder</code> on the specified <code>DomainModel</code> and
	 * initializes it with the contents of this <code>Action</code> instance.
	 *
	 * @param  model The <code>DomainModel</code>, not null
	 * @return       The initialized <code>Builder</code>
	 */

	@Override
	public Builder getBuilder (final DomainModel model)
	{
		return Action.builder (Preconditions.checkNotNull (model, "model"))
			.load (this);
	}

	/**
	 * Get the name of the <code>Action</code>.
	 *
	 * @return A String containing the name of the <code>Action</code>
	 */

	public abstract String getName ();

	/**
	 * Set the name of the <code>Action</code>.  This method is intended to be
	 * used to initialize a new <code>Action</code> instance
	 *
	 * @param  name The name of the <code>Action</code>
	 */

	protected abstract void setName (final String name);
}
