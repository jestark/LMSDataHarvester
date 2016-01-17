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
 * A representation of the nature of a particular <code>Activity</code>.
 * Instances of the <code>ActivityType</code> interface serve to describe the
 * nature of the instances of the <code>Activity</code> interface with which
 * they are associated.  Example <code>ActivityType</code> instances include:
 * "Assignment," "Quiz," and "Presentation."  For instances of
 * <code>Activity</code> where the data was harvested from a Learning
 * Management System (such as Moodle) the <code>ActivityType</code> will be the
 * name of the module from which the data contained in the associated
 * <code>Activity</code> instances was harvested.
 * <p>
 * Instances <code>ActvityType</code> interface has a strong dependency on the
 * associated instances of the <code>ActivitySource</code> interface.  If an
 * instance of a particular <code>ActivitySource</code> is removed from the
 * <code>DomainModel</code> then all of the associated instances of the
 * <code>ActivityType</code> interface must be removed as well.  Similarly,
 * instances of the <code>Activity</code> interface are dependent on the
 * associated instance of the <code>ActivityType</code> interface.  Removing an
 * instance of the <code>ActivityType</code> interface from the
 * <code>DomainModel</code> will require the removal of the associated instances
 * of the <code>Activity</code> interface.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public abstract class ActivityType extends Element
{
	/**
	 * Create new <code>ActivityType</code> instances.  This class extends
	 * <code>AbstractBuilder</code>, adding the functionality required to
	 * create <code>ActivityType</code> instances.
	 *
	 * @author  James E. Stark
	 * @version 1.0
	 */

	public static class Builder extends Element.Builder<ActivityType>
	{
		/** The <code>IdGenerator</code>*/
		private final IdGenerator idGenerator;

		/** Helper to substitute <code>ActivitySource</code> instances */
		private final Retriever<ActivitySource> sourceRetriever;

		/** The <code>DataStore</code> id number for the <code>ActivityType</code> */
		private @Nullable Long id;

		/** The name of the <code>ActivityType</code> */
		private @Nullable String name;

		/** The <code>ActivitySource</code> */
		private @Nullable ActivitySource source;

		/**
		 * Create the <code>Builder</code>.
		 *
		 * @param  model           The <code>DomainModel</code>, not null
		 * @param  definition      The <code>Definition</code>, not null
		 * @param  idGenerator     The <code>IdGenerator</code>, not null
		 * @param  typeRetriever   <code>Retriever</code> for
		 *                         <code>ActivityType</code> instances, not null
		 * @param  sourceRetriever <code>Retriever</code> for
		 *                         <code>ActivitySource</code> instances, not
		 *                         null
		 */

		protected Builder (
				final DomainModel model,
				final Definition definition,
				final IdGenerator idGenerator,
				final Retriever<ActivityType> typeRetriever,
				final Retriever<ActivitySource> sourceRetriever)
		{
			super (model, definition, typeRetriever);

			assert idGenerator != null : "idGenerator is NULL";
			assert sourceRetriever != null : "sourceRetriever is NULL";

			this.idGenerator = idGenerator;
			this.sourceRetriever = sourceRetriever;

			this.id = null;
			this.name = null;
			this.source = null;
		}

		/**
		 * Create an instance of the <code>ActivityType</code>.
		 *
		 * @return The new <code>ActivityType</code> instance
		 *
		 * @throws NullPointerException if any required field is missing
		 */

		@Override
		protected ActivityType create ()
		{
			this.log.trace ("create:");

			return ((Definition) this.definition).creator.apply (this);
		}

		/**
		 * Implementation of the pre-insert hook to set the ID number.
		 *
		 * @param  type The <code>ActivityType</code> to be inserted, not null
		 * @return      The <code>ActivityType</code> to be inserted
		 */

		@Override
		protected ActivityType preInsert (final ActivityType type)
		{
			assert type != null : "type is NULL";

			this.log.debug ("Setting ID");
			type.setId (this.idGenerator.nextId ());

			return type;
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
			this.source = null;

			return this;
		}

		/**
		 * Load a <code>ActivityType</code> instance into the builder.  This
		 * method resets the builder and initializes all of its parameters from
		 * the specified <code>ActivityType</code> instance.  The  parameters
		 * are validated as they are set.
		 *
		 * @param  type The <code>ActivityType</code>, not null
		 * @return      This <code>Builder</code>
		 *
		 * @throws IllegalArgumentException If any of the fields in the
		 *                                  <code>ActivityType</code> instance
		 *                                  to be loaded are not valid
		 */

		@Override
		public Builder load (final ActivityType type)
		{
			this.log.trace ("load: type={}", type);

			super.load (type);

			this.id = type.getId ();
			this.setName (type.getName ());
			this.setActivitySource (type.getSource ());

			return this;
		}

		/**
		 * Get the <code>DataStore</code> identifier for the
		 * <code>ActivityType</code> instance.
		 *
		 * @return The <code>DataStore</code> identifier
		 */

		@CheckReturnValue
		public final Long getId ()
		{
			return this.id;
		}

		/**
		 * Get the name of the <code>ActivityType</code>.
		 *
		 * @return A <code>String</code> containing the name of the
		 *         <code>ActivityType</code>
		 */

		@CheckReturnValue
		public final String getName ()
		{
			return this.name;
		}

		/**
		 * Set the name of the <code>ActivityType</code>.
		 *
		 * @param  name The name of the <code>ActivityType</code>, not null
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

		/**
		 * Get the <code>ActivitySource</code> for the
		 * <code>ActivityType</code>.
		 *
		 * @return The <code>ActivitySource</code> instance
		 */

		@CheckReturnValue
		public final ActivitySource getActivitySource ()
		{
			return this.source;
		}

		/**
		 * Set the <code>ActivitySource</code> for the
		 * <code>ActivityType</code>.
		 *
		 * @param  source The <code>ActivitySource</code> for the
		 *                <code>ActivityType</code>
		 * @return        This <code>Builder</code>
		 *
		 * @throws IllegalArgumentException If the <code>AcivitySourse</code>
		 *                                  does not exist in the
		 *                                  <code>DataStore</code>
		 */

		public final Builder setActivitySource (final ActivitySource source)
		{
			this.log.trace ("setSource: source={}", source);

			this.source = this.verifyRelationship (this.sourceRetriever, source, "source");

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
	@Component (dependencies = {IdGenerator.IdGeneratorComponent.class}, modules = {ActivityTypeBuilderModule.class})
	protected interface ActivityTypeComponent extends Element.ElementComponent<ActivityType>
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
	public static final class ActivityTypeModule extends Element.ElementModule<ActivityType>
	{
		/**
		 * Get the <code>Selector</code> used by the
		 * <code>QueryRetriever</code>.
		 *
		 * @return The <code>Selector</code>
		 */

		@Provides
		public Selector<ActivityType> getSelector ()
		{
			return ActivityType.SELECTOR_NAME;
		}
	}

	/**
	 * Dagger module for creating <code>Builder</code> instances.  This module
	 * contains implementation-dependent information.
	 *
	 * @author  James E. Stark
	 * @version 1.0
	 */

	@Module (includes = {ActivityTypeModule.class, ActivitySource.ActivitySourceModule.class})
	public static final class ActivityTypeBuilderModule
	{
		/** The <code>Definition</code> */
		private final Definition definition;

		/**
		 * Create the <code>ActivityTypeBuilderModule</code>
		 *
		 * @param  definition The <code>Definition</code>, not null
		 */

		public ActivityTypeBuilderModule (final Definition definition)
		{
			assert definition != null : "definition is NULL";
			this.definition = definition;
		}

		/**
		 * Create the <code>Builder</code>.
		 *
		 * @param  model           The <code>DomainModel</code>, not null
		 * @param  generator       The <code>IdGenerator</code>, not null
		 * @param  typeRetriever   <code>Retriever</code> for
		 *                         <code>ActivityType</code> instances, not null
		 * @param  sourceRetriever <code>Retriever</code> for
		 *                         <code>ActivitySource</code> instances, not
		 *                         null
		 */

		@Provides
		public Builder createBuilder (
				final DomainModel model,
				final IdGenerator generator,
				final @Named ("QueryRetriever") Retriever<ActivityType> typeRetriever,
				final @Named ("QueryRetriever") Retriever<ActivitySource> sourceRetriever)
		{
			return new Builder (model, this.definition, generator, typeRetriever, sourceRetriever);
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

	protected static abstract class Definition extends Element.Definition<ActivityType>
	{
		/** Method reference to the implementation constructor  */
		private final Function<ActivityType.Builder, ActivityType> creator;

		/**
		 * Create the <code>Definition</code>.
		 *
		 * @param  impl    The implementation class, not null
		 * @param  creator Method reference to the constructor, not null
		 */

		public Definition (
				final Class<? extends ActivityType> impl,
				final Function<ActivityType.Builder, ActivityType> creator)
		{
			super (ActivityType.METADATA, impl);

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
		protected ActivityType.ActivityTypeComponent getComponent (final DomainModel model)
		{
			return DaggerActivityType_ActivityTypeComponent.builder ()
				.idGeneratorComponent (model.getIdGeneratorComponent (this.impl))
				.domainModelModule (new DomainModel.DomainModelModule (ActivityType.class, model))
				.activityTypeBuilderModule (new ActivityTypeBuilderModule (this))
				.build ();
		}
	}

	/** Serial version id, required by the Serializable interface */
	private static final long serialVersionUID = 1L;

	/** The <code>MetaData</code> for the <code>ActivityType</code> */
	protected static final MetaData<ActivityType> METADATA;

	/** The <code>DataStore</code> identifier of the <code>ActivityType</code> */
	public static final Property<ActivityType, Long> ID;

	/** The <code>DomainModel</code> which contains the <code>ActivityType</code> */
	public static final Property<ActivityType, DomainModel> MODEL;

	/** The name of the <code>ActivityType</code> */
	public static final Property<ActivityType, String> NAME;

	/** The associated <code>ActivitySource</code> */
	public static final Property<ActivityType, ActivitySource> SOURCE;

	/** Select the <code>ActivityType</code> instance by its id */
	public static final Selector<ActivityType> SELECTOR_ID;

	/** Select all of the <code>ActivityType</code> instances */
	public static final Selector<ActivityType> SELECTOR_ALL;

	/** Select an <code>ActivityType</code> instance by name and <code>ActivitySource</code> */
	public static final Selector<ActivityType> SELECTOR_NAME;

	/**
	 * Initialize the <code>MetaData</code>, <code>Property</code> and
	 * <code>Selector</code> instances for the <code>ActivityType</code>.
	 */

	static
	{
		ID = Property.of (ActivityType.class, Long.class, "id",
				ActivityType::getId, ActivityType::setId);

		MODEL = Property.of (ActivityType.class, DomainModel.class, "domainmodel",
				ActivityType::getDomainModel, ActivityType::setDomainModel);

		NAME = Property.of (ActivityType.class, String.class, "name",
				ActivityType::getName, ActivityType::setName,
				Property.Flags.REQUIRED);

		SOURCE = Property.of (ActivityType.class, ActivitySource.class, "source",
				ActivityType::getSource, ActivityType::setSource,
				Property.Flags.REQUIRED);

		SELECTOR_ID = Selector.of (Selector.Cardinality.KEY, ID);

		SELECTOR_ALL = Selector.builder (ActivityType.class)
			.setCardinality (Selector.Cardinality.MULTIPLE)
			.setName ("all")
			.build ();

		SELECTOR_NAME = Selector.builder (ActivityType.class)
			.setCardinality (Selector.Cardinality.SINGLE)
			.addProperty (NAME)
			.addProperty (SOURCE)
			.build ();

		METADATA = MetaData.builder (ActivityType.class)
			.addProperty (ID)
			.addProperty (MODEL)
			.addProperty (NAME)
			.addRelationship (SOURCE, ActivitySource.METADATA, ActivitySource.TYPES)
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
	 *                               the <code>ActivityType</code>
	 * @throws IllegalStateException if the <code>DomainModel</code> is
	 *                               immutable
	 */

	public static Builder builder (final DomainModel model)
	{
		Preconditions.checkNotNull (model, "model");

		return (ActivityType.Builder) model.getElementComponent (ActivityType.class)
			.getBuilder ();
	}

	/**
	 * Create the <code>ActivityType</code>.
	 */

	protected ActivityType ()
	{
		super ();
	}

	/**
	 * Create the <code>ActivityType</code> instance from the specified
	 * <code>Builder</code>.
	 *
	 * @param  builder The <code>Builder</code>, not null
	 */

	protected ActivityType (final Builder builder)
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
			.add ("source", this.getSource ())
			.add ("name", this.getName ());
	}

	/**
	 * Connect all of the relationships for this <code>ActivityType</code>
	 * instance.  This method is intended to be used just after the
	 * <code>ActivityType</code> is inserted into the <code>DataStore</code>.
	 *
	 * @return <code>true</code> if all of the relationships were successfully
	 *         connected, <code>false</code> otherwise
	 */

	@Override
	protected boolean connect ()
	{
		return ActivityType.METADATA.relationships ()
				.allMatch (r -> r.connect (this));
	}

	/**
	 * Disconnect all of the relationships for this <code>ActivityType</code>
	 * instance.  This method is intended to be used just before the
	 * <code>ActivityType</code> is removed from the <code>DataStore</code>.
	 *
	 * @return <code>true</code> if all of the relationships were successfully
	 *         disconnected, <code>false</code> otherwise
	 */

	@Override
	protected boolean disconnect ()
	{
		return ActivityType.METADATA.relationships ()
				.allMatch (r -> r.disconnect (this));
	}

	/**
	 * Compare two <code>ActivityType</code> instances. The comparison is based
	 * upon the associated <code>ActivitySource</code> instances and the names
	 * of the <code>ActivityType</code> instances.
	 *
	 * @param  element The <code>Element</code> to be compared
	 * @return         The value 0 if the <code>ActivityType</code> instances
	 *                 are equal, less than 0 of the argument is is greater, and
	 *                 greater than 0 if the argument is less than the
	 *                 <code>ActivityType</code>
	 */

	@Override
	public int compareTo (final Element element)
	{
		Preconditions.checkNotNull (element, "element");

		int result = 0;

		if (this != element)
		{
			if (element instanceof ActivityType)
			{
				result = this.getSource ().compareTo (((ActivityType) element).getSource ());

				if (result == 0)
				{
					result = this.getName ().compareTo (((ActivityType) element).getName ());
				}
			}
			else
			{
				result = super.compareTo (element);
			}
		}

		return result;
	}

	/**
	 * Compare two <code>ActivityType</code> instances to determine if they are
	 * equal.  The <code>ActivityType</code> instances are compared based upon
	 * their associated <code>ActivitySource</code> and their names.
	 *
	 * @param  obj The <code>ActivityType</code> instance to compare to the one
	 *             represented by the called instance
	 * @return     <code>True</code> if the two <code>ActivityType</code>
	 *             instances are equal, <code>False</code> otherwise
	 */

	@Override
	public boolean equals (final Object obj)
	{
		return (obj == this) ? true : (obj instanceof ActivityType)
			&& Objects.equals (this.getName (), ((ActivityType) obj).getName ())
			&& Objects.equals (this.getSource (), ((ActivityType) obj).getSource ());
	}

	/**
	 * Compute a <code>hashCode</code> of the <code>ActivityType</code>
	 * instance.  The hash code is computed based upon the associated
	 * <code>ActivitySource</code> and name of the instance.
	 *
	 * @return An <code>Integer</code> containing the hash code
	 */

	@Override
	public int hashCode ()
	{
		return Objects.hash (this.getName (), this.getSource ());
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
		return ActivityType.METADATA.properties ()
			.filter (p -> p.hasFlags (Property.Flags.RELATIONSHIP))
			.flatMap (p -> p.stream (this))
			.map (e -> ((Element) e));
	}

	/**
	 * Get a <code>Stream</code> containing all of the dependencies for this
	 * <code>Element</code> instance.
	 *
	 * @return  The <code>Stream</code>
	 */

	@Override
	public Stream<Element> dependencies ()
	{
		return ActivityType.METADATA.properties ()
			.filter (p -> p.hasFlags (Property.Flags.RELATIONSHIP))
			.filter (p -> p.hasFlags (Property.Flags.REQUIRED) || p.hasFlags (Property.Flags.MUTABLE))
			.flatMap (p -> p.stream (this))
			.map (e -> (Element) e);
	}

	/**
	 * Get a <code>String</code> representation of the
	 * <code>ActivityType</code> instance, including the identifying fields.
	 *
	 * @return A <code>String</code> representation of the
	 *         <code>ActivityType</code> instance
	 */

	@Override
	@CheckReturnValue
	public String toString ()
	{
		return this.toStringHelper ()
			.toString ();
	}

	/**
	 * Get an <code>Builder</code> instance for the specified
	 * <code>DomainModel</code>.  This method creates an <code>Builder</code> on
	 * the specified <code>DomainModel</code> and initializes it with the
	 * contents of this <code>ActivityType</code> instance.
	 *
	 * @param  model The <code>DomainModel</code>, not null
	 * @return       The initialized <code>Builder</code>
	 */

	@Override
	public Builder getBuilder (final DomainModel model)
	{
		return ActivityType.builder (Preconditions.checkNotNull (model, "model"))
			.load (this);
	}

	/**
	 * Get the name of the <code>ActivityType</code>.
	 *
	 * @return A <code>String</code> containing the name of the
	 *         <code>ActivityType</code>
	 */

	public abstract String getName ();

	/**
	 * Set the name of the <code>ActivityType</code>.  This method is intended
	 * to be used to initialize a new <code>ActivityType</code> instance.
	 *
	 * @param  name The name of the <code>ActivityType</code>
	 */

	protected abstract void setName (String name);

	/**
	 * Get the <code>ActivitySource</code> for the <code>ActivityType</code>.
	 *
	 * @return The <code>ActivitySource</code> instance
	 */

	public abstract ActivitySource getSource ();

	/**
	 * Set the <code>ActivitySource</code> for the <code>ActivityType</code>.
	 * This method is intended to be used initialize a new
	 * <code>ActivityType</code> instance.
	 *
	 * @param  source The <code>ActivitySource</code> for the
	 *                <code>ActivityType</code>
	 */

	protected abstract void setSource (ActivitySource source);
}
