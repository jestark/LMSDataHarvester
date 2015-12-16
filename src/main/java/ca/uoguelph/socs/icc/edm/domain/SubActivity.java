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

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Objects;
import java.util.Set;
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
import ca.uoguelph.socs.icc.edm.domain.datastore.TableRetriever;
import ca.uoguelph.socs.icc.edm.domain.datastore.idgenerator.IdGenerator;
import ca.uoguelph.socs.icc.edm.domain.metadata.MetaData;
import ca.uoguelph.socs.icc.edm.domain.metadata.Property;
import ca.uoguelph.socs.icc.edm.domain.metadata.Selector;

/**
 * A representation of a child element in a hierarchy of <code>Activity</code>
 * instances.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public abstract class SubActivity extends ParentActivity
{
	/**
	 * Create <code>SubActivity</code> instances.  This class creates instances
	 * for the most general <code>SuabActivity</code> implementation and acts as
	 * the common the builders which produce <code>SubActivity</code> instances
	 * with additional parameters.
	 * <p>
	 * To create builders for <code>SubActivity</code> instances, the
	 * parent <code>Activity</code> must be supplied when the builder is
	 * created.  The parent <code>Activity</code> is needed to determine which
	 * <code>SubActivity</code> implementation class is to be created by the
	 * builder.  It is possible to specify a parent <code>Activity</code> which
	 * does not match the selected builder.  In this case the builder will be
	 * created successfully, but an exception will occur when a field is set in
	 * the builder that does not exist in the implementation, or when the
	 * implementation is built and a required field is determined to be missing.
	 *
	 * @author  James E. Stark
	 * @version 1.0
	 */

	public static class Builder extends Element.Builder<SubActivity>
	{
		/** The <code>IdGenerator</code>*/
		private final IdGenerator idGenerator;

		/** Helper to substitute <code>ParentActivity</code> instances */
		private final Retriever<ParentActivity> parentRetriever;

		/** The <code>DataStore</code> id number for the <code>SubActivity</code> */
		protected @Nullable Long id;

		/** The parent */
		protected @Nullable ParentActivity parent;

		/** The name of the <code>SubActivity</code> */
		protected @Nullable String name;

		/**
		 * Create the <code>Builder</code>.
		 *
		 * @param  model           The <code>DomainModel</code>, not null
		 * @param  definition      The <code>Definition</code>, not null
		 * @param  idGenerator     The <code>IdGenerator</code>, not null
		 * @param  subRetriever    <code>Retriever</code> for
		 *                         <code>SubActivity</code> instances, not null
		 * @param  parentRetriever <code>Retriever</code> for
		 *                         <code>ParentActivity</code> instances, not
		 *                         null
		 */

		protected Builder (
				final DomainModel model,
				final Definition definition,
				final IdGenerator idGenerator,
				final Retriever<SubActivity> subRetriever,
				final Retriever<ParentActivity> parentRetriever)
		{
			super (model, definition, subRetriever);

			assert idGenerator != null : "idGenerator is NULL";
			assert parentRetriever != null : "parentRetriever is NULL";

			this.idGenerator = idGenerator;
			this.parentRetriever = parentRetriever;

			this.parent = null;
			this.id = null;
			this.name = null;
		}

		/**
		 * Create an instance of the <code>SubActivity</code>.
		 *
		 * @return The new <code>SubActivity</code> instance
		 *
		 * @throws NullPointerException if any required field is missing
		 */

		@Override
		protected SubActivity create ()
		{
			this.log.trace ("create:");

			return ((Definition) this.definition).creator.apply (this);
		}

		/**
		 * Implementation of the pre-insert hook to set the ID number.
		 *
		 * @param  subActivity The <code>SubActivity</code> to be inserted, not
		 *                     null
		 * @return             The <code>SubActivity</code> to be inserted
		 */

		@Override
		protected SubActivity preInsert (final SubActivity subActivity)
		{
			assert subActivity != null : "subActivity is NULL";

			this.log.debug ("Setting ID");
			subActivity.setId (this.idGenerator.nextId ());

			return subActivity;
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
		 * Load a <code>SubActivity</code> instance into the builder.  This
		 * method resets the builder and initializes all of its parameters from
		 * the specified <code>SubActivity</code> instance.  The  parameters are
		 * validated as they are set.
		 *
		 * @param  subActivity The <code>SubActivity</code>, not null
		 * @return             This <code>Builder</code>
		 *
		 * @throws IllegalArgumentException If any of the fields in the
		 *                                  <code>SubActivity</code> instance to
		 *                                  be loaded are not valid
		 */

		@Override
		public Builder load (final SubActivity subActivity)
		{
			this.log.trace ("load: activity={}", subActivity);

			super.load (subActivity);

			if (! (this.getParent ()).equals (subActivity.getParent ()))
			{
				this.log.error ("Can not load:  Parent activity instances are not equal");
				throw new IllegalArgumentException ("Parent activity instances are different");
			}

			this.id = subActivity.getId ();
			this.setName (subActivity.getName ());

			return this;
		}

		/**
		 * Get the <code>DataStore</code> identifier for the
		 * <code>SubActivity</code> instance.
		 *
		 * @return The <code>DataStore</code> identifier
		 */

		@CheckReturnValue
		public final Long getId ()
		{
			return this.id;
		}

		/**
		 * Get the name of the <code>Activity</code>.
		 *
		 * @return A <code>String</code> containing the name of the
		 *         <code>SubActivity</code>
		 */

		@CheckReturnValue
		public final String getName ()
		{
			return this.name;
		}

		/**
		 * Set the name of the <code>SubActivity</code>.
		 *
		 * @param  name The name of the <code>SubActivity</code>, not null
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

		/**
		 * Get the parent <code>Activity</code> instance for the
		 * <code>SubActivity</code> instance.
		 *
		 * @return The parent <code>Activity</code>
		 */

		public final ParentActivity getParent ()
		{
			return this.parent;
		}

		/**
		 * Set the <code>ParentActivity</code>.
		 *
		 * @param  parent The <code>ParentActivity</code>, not null
		 * @return        This <code>Builder</code>
		 */

		private final Builder setParent (final ParentActivity parent)
		{
			this.log.trace ("setParent: parent={}", parent);

			this.parent = this.verifyRelationship (this.parentRetriever, parent, "parent");

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
	@Component (dependencies = {IdGenerator.IdGeneratorComponent.class}, modules = {SubActivityBuilderModule.class})
	protected interface SubActivityComponent extends Element.ElementComponent<SubActivity>
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
	public static final class SubActivityModule extends Element.ElementModule<SubActivity> {}

	/**
	 * Dagger module for creating <code>Builder</code> instances.  This module
	 * contains implementation-dependent information.
	 *
	 * @author  James E. Stark
	 * @version 1.0
	 */

	@Module (includes = {SubActivityModule.class, ParentActivity.ParentActivityModule.class})
	public static final class SubActivityBuilderModule
	{
		/** The <code>Definition</code> */
		private final Definition definition;

		/**
		 * Create the <code>SubActivityBuilderModule</code>
		 *
		 * @param  definition The <code>Definition</code>, not null
		 */

		public SubActivityBuilderModule (final Definition definition)
		{
			assert definition != null : "definition is null";
			this.definition = definition;
		}

		/**
		 * Create the <code>Builder</code>.
		 *
		 * @param  model           The <code>DomainModel</code>, not null
		 * @param  generator       The <code>IdGenerator</code>, not null
		 * @param  retriever       <code>Retriever</code> for
		 *                         <code>SubActivity</code> instances, not null
		 * @param  parentRetriever <code>Retriever</code> for
		 *                         <code>ParentActivity</code> instances, not
		 *                         null
		 */

		@Provides
		protected Builder createBuilder (
				final DomainModel model,
				final IdGenerator generator,
				final @Named ("TableRetriever") Retriever<SubActivity> retriever,
				final @Named ("TableRetriever") Retriever<ParentActivity> parentRetriever)
		{
			return new Builder (model, this.definition, generator, retriever, parentRetriever);
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

	protected abstract class Definition extends Element.Definition<SubActivity>
	{
		/** Method reference to the implementation constructor  */
		private final Function<SubActivity.Builder, SubActivity> creator;

		/**
		 * Create the <code>Definition</code>.
		 *
		 * @param  impl    The implementation class, not null
		 * @param  creator Method reference to the constructor, not null
		 */

		public Definition (
				final Class<? extends SubActivity> impl,
				final Function<SubActivity.Builder, SubActivity> creator)
		{
			super (SubActivity.METADATA, impl);

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
		protected SubActivity.SubActivityComponent getComponent (final DomainModel model)
		{
			return DaggerSubActivity_SubActivityComponent.builder ()
				.idGeneratorComponent (model.getIdGeneratorComponent (this.impl))
				.domainModelModule (new DomainModel.DomainModelModule (SubActivity.class, model))
				.subActivityBuilderModule (new SubActivityBuilderModule (this))
				.build ();
		}
	}

	/** Serial version id, required by the Serializable interface */
	private static final long serialVersionUID = 1L;

	/** Parent to Child <code>SubActivity</code> class mapping*/
	private static final Map<Class<? extends ParentActivity>, Class<? extends SubActivity>> subactivities;

	/** The <code>MetaData</code> for the <code>SubActivity</code> */
	protected static final MetaData<SubActivity> METADATA;

	/** The <code>DataStore</code> identifier of the <code>SubActivity</code> */
	public static final Property<SubActivity, Long> ID;

	/** The <code>DomainModel</code> which contains the <code>SubActivity</code> */
	public static final Property<SubActivity, DomainModel> MODEL;

	/** The name of the <code>SubActivity</code> */
	public static final Property<SubActivity, String> NAME;

	/** The parent <code>Activity</code> */
	public static final Property<SubActivity, ParentActivity> PARENT;

	/** The <code>LogEntry</code> instances associated with the <code>SubActivity</code> */
	public static final Property<SubActivity, LogReference> REFERENCES;

	/** Select the <code>SubActivity</code> instance by its id */
	public static final Selector<SubActivity> SELECTOR_ID;

	/** Select all of the <code>SubActivity</code> instances */
	public static final Selector<SubActivity> SELECTOR_ALL;

	/**
	 * Initialize the <code>MetaData</code>, <code>Property</code> and
	 * <code>Selector</code> instances for the <code>SubActivity</code>.
	 */

	static
	{
		subactivities = new HashMap<> ();

		ID = Property.of ( SubActivity.class, Long.class, "id",
				SubActivity::getId, SubActivity::setId);

		MODEL = Property.of (SubActivity.class, DomainModel.class, "domainmodel",
				SubActivity::getDomainModel, SubActivity::setDomainModel);

		NAME = Property.of (SubActivity.class, String.class, "name",
				SubActivity::getName, SubActivity::setName,
				Property.Flags.REQUIRED);

		PARENT = Property.of (SubActivity.class, ParentActivity.class, "parent",
				SubActivity::getParent, SubActivity::setParent,
				Property.Flags.REQUIRED);

		REFERENCES = Property.of (SubActivity.class, LogReference.class, "references",
				SubActivity::getReferences, SubActivity::addReference, SubActivity::removeReference);

		SELECTOR_ID = Selector.of (Selector.Cardinality.KEY, ID);

		SELECTOR_ALL = Selector.builder (SubActivity.class)
			.setCardinality (Selector.Cardinality.MULTIPLE)
			.setName ("all")
			.build ();

		METADATA = MetaData.builder (SubActivity.class, ParentActivity.METADATA)
			.addProperty (ID)
			.addProperty (MODEL)
			.addProperty (NAME)
			.addProperty (PARENT)
			.addProperty (REFERENCES)
			.addRelationship (PARENT, ParentActivity.METADATA, ParentActivity.SUBACTIVITIES)
			.addSelector (SELECTOR_ID)
			.addSelector (SELECTOR_ALL)
			.build ();
	}

	/**
	 * Register an association between an <code>Activity</code> implementation
	 * class and a <code>SubActivity</code> implementation class.
	 *
	 * @param  activity    The <code>Activity</code> implementation, not null
	 * @param  subactivity The <code>SubActivity</code> implementation, not null
	 */

	protected static final <T extends SubActivity> void registerImplementation (final Class<? extends ParentActivity> activity, final Class<T> subactivity)
	{
		assert activity != null : "activity is NULL";
		assert subactivity != null : "subactivity is NULL";
		assert (! SubActivity.subactivities.containsKey (activity)) : "activity is already registered";

		SubActivity.subactivities.put (activity, subactivity);
	}

	/**
	 * Get the <code>SubActivity</code> implementation class which is
	 * associated with the specified <code>Activity</code> implementation
	 * class.
	 *
	 * @param  activity The <code>Activity</code> implementation class
	 * @return          The <code>SubActivity</code> implementation class, may
	 *                  be null
	 */

	@CheckReturnValue
	public static final Class<? extends SubActivity> getSubActivityClass (final Class<? extends ParentActivity> activity)
	{
		assert activity != null : "activity is NULL";
		assert SubActivity.subactivities.containsKey (activity) : "Activity is not registered";

		return SubActivity.subactivities.get (activity);
	}

	/**
	 * Get an instance of the <code>Builder</code> for the specified
	 * <code>DomainModel</code>.  The <code>Builder</code> will be initialized
	 * with the supplied <code>ParentActivity</code> instance.
	 *
	 * @param  model  The <code>DomainModel</code>, not null
	 * @param  parent The parent <code>Activity</code>, not null
	 * @return        The <code>Builder</code> instance
	 *
	 * @throws IllegalStateException if the <code>DomainModel</code> is closed
	 * @throws IllegalStateException if the <code>DomainModel</code> is
	 *                               immutable
	 */

	public static Builder builder (final DomainModel model, final ParentActivity parent)
	{
		Preconditions.checkNotNull (model, "model");
		Preconditions.checkNotNull (parent, "parent");

		return ((SubActivity.Builder) model.getElementComponent (SubActivity.class,
					SubActivity.getSubActivityClass (parent.getClass ()))
			.getBuilder ())
			.setParent (parent);
	}

	/**
	 * Create the <code>SubActivity</code>.
	 */

	protected SubActivity ()
	{
		super ();
	}

	/**
	 * Create the <code>SubActivity</code> instance from the specified
	 * <code>Builder</code>.
	 *
	 * @param  builder The <code>Builder</code>, not null
	 */

	protected SubActivity (final Builder builder)
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
			.add ("parent", this.getParent ())
			.add ("name", this.getName ());
	}

	/**
	 * Connect all of the relationships for this <code>SubActivity</code>
	 * instance.  This method is intended to be used just after the
	 * <code>SubActivity</code> is inserted into the <code>DataStore</code>.
	 *
	 * @return <code>true</code> if all of the relationships were successfully
	 *         connected, <code>false</code> otherwise
	 */

	@Override
	protected boolean connect ()
	{
		return SubActivity.METADATA.relationships ()
			.allMatch (r -> r.connect (this));
	}

	/**
	 * Disconnect all of the relationships for this <code>SubActivity</code>
	 * instance.  This method is intended to be used just before the
	 * <code>SubActivity</code> is removed from the <code>DataStore</code>.
	 *
	 * @return <code>true</code> if all of the relationships were successfully
	 *         disconnected, <code>false</code> otherwise
	 */

	@Override
	protected boolean disconnect ()
	{
		return SubActivity.METADATA.relationships ()
			.allMatch (r -> r.disconnect (this));
	}

	/**
	 * Compare two <code>SubActivity</code> instances to determine if they are
	 * equal.  The <code>SubActivity</code> instances are compared based upon
	 * their names and the parent <code>Activity</code>.
	 *
	 * @param  obj The <code>SubActivity</code> instance to compare to the one
	 *             represented by the called instance
	 * @return     <code>True</code> if the two <code>SubActivity</code>
	 *             instances are equal, <code>False</code> otherwise
	 */

	@Override
	public boolean equals (final Object obj)
	{
		return (obj == this) ? true : (obj instanceof SubActivity)
			&& Objects.equals (this.getName (), ((SubActivity) obj).getName ())
			&& Objects.equals (this.getParent (), ((SubActivity) obj).getParent ());
	}

	/**
	 * Compute a <code>hashCode</code> of the <code>SubActivity</code>
	 * instance.  The hash code is computed based upon the parent
	 * <code>Activity</code> and the name of the <code>SubActivity</code>
	 * instance.
	 *
	 * @return The hash code
	 */

	@Override
	public int hashCode ()
	{
		return Objects.hash (this.getName (), this.getParent ());
	}

	/**
	 * Get a <code>String</code> representation of the <code>SubActivity</code>
	 * instance, including the identifying fields.
	 *
	 * @return A <code>String</code> representation of the
	 *         <code>SubActivity</code> instance
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
	 * <code>DomainModel</code>.  This method creates a <code>Builder</code> on
	 * the specified <code>DomainModel</code> and initializes it with the
	 * contents of this <code>SubActivity</code> instance.
	 *
	 * @param  model The <code>DomainModel</code>, not null
	 * @return       The initialized <code>Builder</code>
	 */

	@Override
	public Builder getBuilder (final DomainModel model)
	{
		return SubActivity.builder (model, this.getParent ())
			.load (this);
	}

	/**
	 * Get the <code>ActivityType</code> for the <code>Activity</code>.
	 *
	 * @return The <code>ActivityType</code> instance
	 */

	@Override
	public final ActivityType getType()
	{
		return this.getParent ().getType ();
	}

	/**
	 * Get the <code>Course</code> with which the <code>Activity</code> is
	 * associated.
	 *
	 * @return The <code>Course</code> instance
	 */

	@Override
	public final Course getCourse ()
	{
		return this.getParent ().getCourse ();
	}

	/**
	 * Get the <code>Set</code> of <code>Grade</code> instances which are
	 * associated with the <code>Activity</code>.  Not all
	 * <code>Activity</code> instances are graded.  If the
	 * <code>Activity</code> does is not graded then the <code>Set</code> will
	 * be empty.
	 *
	 * @return A <code>Set</code> of <code>Grade</code> instances
	 */

	@Override
	public final Set<Grade> getGrades ()
	{
		return this.getParent ().getGrades ();
	}

	/**
	 * Get the parent <code>Activity</code> instance for the
	 * <code>SubActivity</code>.
	 *
	 * @return The parent <code>Activity</code>
	 */

	public abstract ParentActivity getParent ();

	/**
	 * Set the <code>Activity</code> instance which contains the
	 * <code>SubActivity</code>.  This method is intended to be used to
	 * initialize a new <code>SubActivity</code> instance.
	 *
	 * @param  activity The <code>Activity</code> containing this
	 *                  <code>SubActivity</code> instance
	 */

	protected abstract void setParent (ParentActivity activity);

	/**
	 * Set the name of the <code>Activity</code>.  This method is intended to
	 * be used to initialize a new <code>SubActivity</code> instance.
	 *
	 * @param  name The name of the <code>SubActivity</code>, not null
	 */

	protected abstract void setName (String name);

	/**
	 * Get a <code>List</code> of all of the <code>LogReference</code>
	 * instances for the <code>SubActivity</code>.
	 *
	 * @return A <code>List</code> of <code>LogReference</code> instances
	 */

	public abstract List<LogReference> getReferences ();

	/**
	 * Initialize the <code>List</code> of <code>LogReference</code> instances
	 * associated with the <code>SubActivity</code> instance.  This method is
	 * intended to be used to initialize a new  <code>SubActivity</code>
	 * instance.
	 *
	 * @param  references The <code>List</code> of <code>LogReference</code>
	 *                    instances, not null
	 */

	protected abstract void setReferences (List<LogReference> references);

	/**
	 * Add the specified <code>LogReference</code> to the
	 * <code>SubActivity</code>.
	 *
	 * @param  reference The <code>LogReference</code> to add, not null
	 * @return           <code>True</code> if the <code>LogReference</code>
	 *                   was successfully added, <code>False</code> otherwise
	 */

	protected abstract boolean addReference (LogReference reference);

	/**
	 * Remove the specified <code>LogReference</code> from the
	 * <code>SubActivity</code>.
	 *
	 * @param  reference The <code>LogReference</code> to remove, not null
	 * @return           <code>True</code> if the <code>LogReference</code> was
	 *                   successfully removed, <code>False</code> otherwise
	 */

	protected abstract boolean removeReference (LogReference reference);
}
