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
import ca.uoguelph.socs.icc.edm.domain.element.GenericActivity;
import ca.uoguelph.socs.icc.edm.domain.metadata.MetaData;
import ca.uoguelph.socs.icc.edm.domain.metadata.Property;
import ca.uoguelph.socs.icc.edm.domain.metadata.Selector;

/**
 * A representation of the content of a <code>Course</code>.  All of the
 * content in a <code>Course</code> is represented by a <code>Set</code> of
 * <code>Activity</code> instances.  <code>Activity</code> instances will exist
 * for assignments, quizzes, forums and lessons among many other things.  Some
 * of these will be graded and some will not be graded.  Graded
 * <code>Activity</code> instances will have <code>Grade</code> instances
 * associated with them.
 * <p>
 * Since the <code>Activity</code> interface represents all of the content of a
 * <code>Course</code>, its implementation can be complex.  Each instance of
 * the <code>Activity</code> interface must contain the
 * <code>ActivityType</code>, the associated <code>Course</code>,
 * <code>Grade</code>, and <code>LogEntry</code> instances.  Depending on the
 * <code>ActivityType</code> an <code>Activity</code> instance can contain
 * additional data including, but not limited to, <code>SubActivity</code>
 * instances.  All </code>SubActivity</code> instances must implement the
 * <code>Activity</code> interface, but may return local data where necessary.
 * <p>
 * Instances of the <code>Activity</code> interface have strong dependencies on
 * the associated instances of the <code>ActivityType</code> and
 * <code>Course</code> interfaces.  If an instance of one of these interfaces
 * is deleted, then the associated instance of the <code>Activity</code>
 * interface and its dependants must me deleted as well.  Similarly, instances
 * of the <code>Grade</code> and <code>LogEntry</code> interfaces, along with
 * any <code>SubActivity</code> instances have a strong dependency on the
 * associated instance of the <code>Activity</code> interface, and must be
 * deleted along with the instance of the <code>Activity</code> interface.
 * <p>
 * Once created, with the exception of adding and removing <code>Grade</code>
 * and <code>LogEntry</code> instances (and sub-activities if they exist),
 * <code>Activity</code> instances are immutable.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public abstract class Activity extends ParentActivity
{
	/**
	 * Create <code>Activity</code> instances. This class creates instances for
	 * the generic <code>Activity</code> implementations which only contain the
	 * associated <code>ActivityType</code> and <code>Course</code>, and acts as
	 * the common base for all of the builders which produce
	 * <code>Activity</code> instances with additional parameters.
	 * <p>
	 * To create builders for <code>Activity</code> instances, the
	 * <code>ActivityType</code> must be supplied when the builder is created.
	 * The <code>ActivityType</code> is needed to determine which
	 * <code>Activity</code> implementation class is to be created by the
	 * builder.  It is possible to specify an <code>ActivityType</code> which
	 * does not match the selected builder.  In this case the builder will be
	 * created successfully, but an exception will occur when a field is set in
	 * the builder that does not exist in the implementation, or when the
	 * implementation is built and a required field is determined to be missing.
	 *
	 * @author  James E. Stark
	 * @version 1.0
	 */

	public static class Builder extends Element.Builder<Activity>
	{
		/** Builder to create the <code>ActivityReferences</code> */
		protected final ActivityReference.Builder referenceBuilder;

		/** The name of the <code>Activity</code> */
		protected @Nullable String name;

		/**
		 * Create the <code>AbstractBuilder</code>.
		 *
		 * @param  model            The <code>DomainModel</code>, not null
		 * @param  definition       The <code>Definition</code>, not null
		 * @param  retriever        The <code>Retriever</code>, not null
		 * @param  referenceBuilder Builder for the internal
		 *                          <code>ActivityReference</code> instance, not
		 *                          null
		 */

		protected Builder (
				final DomainModel model,
				final Definition definition,
				final Retriever<Activity> retriever,
				final ActivityReference.Builder referenceBuilder)
		{
			super (model, definition, retriever);

			assert referenceBuilder != null : "referenceBuilder is NULL";
			this.referenceBuilder = referenceBuilder;

			this.name = null;
		}

		/**
		 * Create an instance of the <code>Activity</code>.
		 *
		 * @return The new <code>Activity</code> instance
		 *
		 * @throws NullPointerException if any required field is missing
		 */

		@Override
		protected Activity create ()
		{
			this.log.trace ("create:");

			return ((Definition) this.definition).creator.apply (this);
		}

		/**
		 * Reset the builder.  This method will set all of the fields for the
		 * <code>Activity</code> to be built to <code>null</code>.
		 *
		 * @return This <code>Builder</code>
		 */

		@Override
		public Builder clear ()
		{
			this.log.trace ("clear:");

			super.clear ();

			ActivityType type = this.referenceBuilder.getType ();

			this.referenceBuilder.clear ();
			this.referenceBuilder.setType (type);

			this.name = null;

			return this;
		}

		/**
		 * Load a <code>Activity</code> instance into the builder.  This method
		 * resets the builder and initializes all of its parameters from
		 * the specified <code>Activity</code> instance.  The  parameters are
		 * validated as they are set.
		 *
		 * @param  activity The <code>Activity</code>, not null
		 * @return          This <code>Builder</code>
		 *
		 * @throws IllegalArgumentException If any of the fields in the
		 *                                  <code>Activity</code> instance to be
		 *                                  loaded are not valid
		 */

		@Override
		public Builder load (final Activity activity)
		{
			this.log.trace ("load: activity={}", activity);

			super.load (activity);

			if (! (this.getType ()).equals (activity.getType ()))
			{
				this.log.error ("Invalid ActivityType:  required {}, received {}", this.getType (), activity.getType ());
				throw new IllegalArgumentException ("Invalid ActivityType");
			}

			this.referenceBuilder.load (activity.getReference ());
			this.setName (activity.getName ());

			return this;
		}

		/**
		 * Get the <code>ActivityReference</code> for the <code>Activity</code>.
		 * This method creates the <code>ActivityReference</code> instance, and
		 * is used as part of the build process.
		 *
		 * @return The <code>ActivityReference</code>
		 */

		protected final ActivityReference getReference ()
		{
			return this.referenceBuilder.build ();
		}

		/**
		 * Get the <code>ActivityType</code> for the <code>Activity</code>.
		 *
		 * @return The <code>ActivityType</code> instance
		 */

		public final ActivityType getType ()
		{
			return this.referenceBuilder.getType ();
		}

		/**
		 * Set the <code>ActivityType</code> for the <code>Activity</code>.
		 * This method exists to load the <code>ActivityType</code> when the
		 * <code>Builder</code> is created.
		 *
		 * @param  type The <code>ActivityType</code>
		 * @return      This <code>Builder</code>
		 */

		private final Builder setType (final ActivityType type)
		{
			this.log.trace ("setType: type={}");

			this.referenceBuilder.setType (type);

			return this;
		}

		/**
		 * Get the <code>Course</code> with which the <code>Activity</code> is
		 * associated.
		 *
		 * @return The <code>Course</code> instance
		 */

		@CheckReturnValue
		public final Course getCourse ()
		{
			return this.referenceBuilder.getCourse ();
		}

		/**
		 * Set the <code>Course</code> with which the <code>Activity</code> is
		 * associated.
		 *
		 * @param  course The <code>Course</code>, not null
		 * @return        This <code>Builder</code>
		 *
		 * @throws IllegalArgumentException If the <code>Course</code> does not
		 *                                  exist in the <code>DataStore</code>
		 */

		public final Builder setCourse (final Course course)
		{
			this.log.trace ("setCourse: course={}", course);

			this.referenceBuilder.setCourse (course);

			return this;
		}

		/**
		 * Get the name of the <code>Activity</code>.
		 *
		 * @return The name of the <code>Activity</code>
		 */

		@CheckReturnValue
		public final String getName ()
		{
			return this.name;
		}

		/**
		 * Set the name of the <code>Activity</code>.
		 *
		 * @param  name The name of the <code>Activity</code>, not null
		 * @return      This <code>Builder</code>
		 *
		 * @throws IllegalArgumentException if the name is empty
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

	@ActivityScope
	@Component (dependencies = {ActivityReference.ActivityReferenceComponent.class}, modules = {ActivityBuilderModule.class})
	protected interface ActivityComponent extends Element.ElementComponent<Activity>
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
	public static final class ActivityModule extends Element.ElementModule<Activity> {}

	/**
	 * Dagger module for creating <code>Builder</code> instances.  This module
	 * contains implementation-dependent information.
	 *
	 * @author  James E. Stark
	 * @version 1.0
	 */

	@Module (includes = {ActivityModule.class})
	public static final class ActivityBuilderModule
	{
		/** The <code>Definition</code> */
		private final Definition definition;

		/**
		 * Create the <code>ActivityBuilderModule</code>
		 *
		 * @param  definition The <code>Definition</code>, not null
		 */

		public ActivityBuilderModule (final Definition definition)
		{
			assert definition != null : "definition is NULL";
			this.definition = definition;
		}

		/**
		 * Create the <code>Builder</code>.
		 *
		 * @param  model            The <code>DomainModel</code>, not null
		 * @param  retriever        The <code>Retriever</code>, not null
		 * @param  referenceBuilder Builder for the internal
		 *                          <code>ActivityReference</code> instance, not
		 *                          null
		 */

		@Provides
		public Builder createBuilder (
				final DomainModel model,
				final @Named ("TableRetriever") Retriever<Activity> retriever,
				final ActivityReference.Builder referenceBuilder)
		{
			return new Builder (model, this.definition, retriever, referenceBuilder);
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

	protected static abstract class Definition extends Element.Definition<Activity>
	{
		/** Method reference to the implementation constructor  */
		private final Function<Activity.Builder, Activity> creator;

		/**
		 * Create the <code>Definition</code>.
		 *
		 * @param  impl    The implementation class, not null
		 * @param  creator Method reference to the constructor, not null
		 */

		public Definition (
				final Class<? extends Activity> impl,
				final Function<Activity.Builder, Activity> creator)
		{
			super (Activity.METADATA, impl);

			assert creator != null : "creator is NULL";
			this.creator = creator;
		}

		/**
		 * Create a new instance of the <code>Component</code> on the
		 * specified <code>DomainModel</code>.
		 *
		 * @param  model The <code>DomainModel</code>, not null
		 * @return       The <code>Component</code>
		 */

		@Override
		protected Activity.ActivityComponent getComponent (final DomainModel model)
		{
			return DaggerActivity_ActivityComponent.builder ()
				.domainModelModule (new DomainModel.DomainModelModule (Activity.class, model))
				.activityReferenceComponent ((ActivityReference.ActivityReferenceComponent) model.getElementComponent (ActivityReference.class))
				.activityBuilderModule (new ActivityBuilderModule (this))
				.build ();
		}
	}

	/**
	 * Value class to represent an <code>ActivityType</code> instance as two
	 * Strings.  It is used as the Key for the <code>ActivityType</code> to
	 * <code>Activity</code> implementation class mapping.
	 *
	 * @author  James E. Stark
	 * @version 1.0
	 */

	private static final class TypeKey
	{
		/** Name of the associated <code>ActivitySource</code> */
		private final String source;

		/** Name of the associated <code>ActivityType</code> */
		private final String type;

		/**
		 * Create the <code>TypeKey</code> for an <code>ActivityType</code>
		 * instance.
		 *
		 * @param  type The <code>ActivityType</code>, not null
		 * @return      The <code>TypeKey</code>
		 */

		public static TypeKey create (final ActivityType type)
		{
			assert type != null : "type is NULL";

			return new TypeKey (type.getSource ().getName (), type.getName ());
		}

		/**
		 * Create the <code>TypeKey</code> from strings.
		 *
		 * @param  source The name of the <code>ActivitySource</code>, not null
		 * @param  type   The name of the <code>ActivityType</code>, not null
		 * @return        The <code>TypeKey</code>
		 */

		public static TypeKey create (final String source, final String type)
		{
			assert source != null : "source is NULL";
			assert type != null : "type is NULL";

			return new TypeKey (source, type);
		}

		/**
		 * Create the <code>TypeKey</code>.
		 *
		 * @param  source The name of the <code>ActivitySource</code>, not null
		 * @param  type   The name of the <code>ActivityType</code>, not null
		 */

		private TypeKey (final String source, final String type)
		{
			this.source = source;
			this.type = type;
		}

		/**
		 * Compare two <code>TypeKey</code> instances to determine if they are
		 * equal.
		 *
		 * @param  obj The <code>TypeKey</code> instance to compare to the one
		 *             represented by the called instance
		 * @return     <code>true</code> if the two <code>TypeKey</code>
		 *             instances are equal, <code>false</code> otherwise
		 */

		@Override
		public boolean equals (final Object obj)
		{
			return (obj == this) ? true : (obj instanceof TypeKey)
				&& Objects.equals (this.source, ((TypeKey) obj).source)
				&& Objects.equals (this.type, ((TypeKey) obj).type);
		}

		/**
		 * Compute a <code>hashCode</code> of the <code>TypeKey</code> instance.
		 *
		 * @return The hash code
		 */

		@Override
		public int hashCode ()
		{
			return Objects.hash (this.source, this.type);
		}
	}

	/** Serial version id, required by the Serializable interface */
	private static final long serialVersionUID = 1L;

	/** <code>ActivityType</code> to implementation class map */
	private static final Map<TypeKey, Class<? extends Activity>> activities;

	/** The <code>MetaData</code> for the <code>Activity</code> */
	protected static final MetaData<Activity> METADATA;

	/** The Id number (needed for Moodle) */
	public static final Property<Activity, Long> ID;

	/** The <code>DomainModel</code> which contains the <code>Activity</code> */
	public static final Property<Activity, DomainModel> MODEL;

	/** The name of the <code>Activity</code> */
	public static final Property<Activity, String> NAME;

	/** The associated <code>ActivityReference</code> */
	public static final Property<Activity, ActivityReference> REFERENCE;

	/** The <code>Grade</code> instances associated with the <code>Activity</code> */
	public static final Property<Activity, Grade> GRADES;

	/** Select the <code>Activity</code> instance by its id */
	public static final Selector<Activity> SELECTOR_ID;

	/** Select all of the <code>Activity</code> instances */
	public static final Selector<Activity> SELECTOR_ALL;

	/** The associated <code>ActivityReference</code> instance */
	private ActivityReference reference;

	/**
	 * Initialize the <code>MetaData</code>, <code>Property</code> and
	 * <code>Selector</code> instances for the <code>Activity</code>.
	 */

	static
	{
		activities = new HashMap<> ();

		ID = Property.of (Activity.class, Long.class, "id",
				Activity::getId, Activity::setId);

		MODEL = Property.of (Activity.class, DomainModel.class, "domainmodel",
				Activity::getDomainModel, Activity::setDomainModel);

		NAME = Property.of (Activity.class, String.class, "name",
				Activity::getName);

		REFERENCE = Property.of (Activity.class, ActivityReference.class, "reference",
				Activity::getReference, Activity::setReference,
				Property.Flags.REQUIRED);

		GRADES = Property.of (Activity.class, Grade.class, "grade",
				Activity::getGrades, Activity::addGrade, Activity::removeGrade);

		SELECTOR_ID = Selector.of (Selector.Cardinality.KEY, ID);

		SELECTOR_ALL = Selector.builder (Activity.class)
			.setCardinality (Selector.Cardinality.MULTIPLE)
			.setName ("all")
			.build ();

		METADATA = MetaData.builder (Activity.class, ParentActivity.METADATA)
			.addProperty (ID)
			.addProperty (MODEL)
			.addProperty (NAME)
			.addProperty (GRADES)
			.addRelationship (REFERENCE, ActivityReference.METADATA, ActivityReference.ACTIVITY)
			.addSelector (SELECTOR_ID)
			.addSelector (SELECTOR_ALL)
			.build ();
	}

	/**
	 * Register an association between an <code>ActivityType</code> and the
	 * class implementing the <code>Activity</code> interface for that
	 * <code>ActivityType</code>.
	 *
	 * @param  source A <code>String</code> representation of the
	 *                <code>ActivitySource</code>, not null
	 * @param  type   A <code>String</code> representation of the
	 *                <code>ActivityType</code>, not null
	 * @param  impl   The implementation class, not null
	 */

	protected static <T extends Activity> void registerImplementation (final String source, final String type, final Class<T> impl)
	{
		assert source != null : "source is NULL";
		assert type != null : "type is NULL";
		assert impl != null : "impl is NULL";
		assert source.length () > 0 : "source is empty";

		TypeKey atype = TypeKey.create (source, type);

		assert ! Activity.activities.containsKey (atype) : "Implementation class already registered for ActivityType";

		Activity.activities.put (atype, impl);
	}

	/**
	 * Determine if an <code>Activity</code> implementation class has been
	 * registered for the specified <code>ActivityType</code>.
	 *
	 * @param  type The <code>ActivityType</code>, not null
	 * @return      <code>true</code> if an <code>Activity</code>
	 *              implementation class has been registered for the specified
	 *              <code>ActivityType</code>, <code>false</code> otherwise
	 */

	public static final boolean hasActivityClass (final ActivityType type)
	{
		assert type != null : "type is NULL";

		return Activity.activities.containsKey (TypeKey.create (type));
	}

	/**
	 * Get the <code>Activity</code> implementation class which is associated
	 * with the specified <code>ActivityType</code>.
	 *
	 * @param  type The <code>ActivityType</code>
	 * @return      The <code>Activity </code> data class for the given
	 *              <code>ActivityType</code>, may be null
	 */

	public static final Class<? extends Activity> getActivityClass (final ActivityType type)
	{
		assert type != null : "type is NULL";

		TypeKey key = TypeKey.create (type);

		return (Activity.activities.containsKey (key)) ? Activity.activities.get (key) : GenericActivity.class;
	}

	/**
	 * Get an instance of the <code>Builder</code> for the specified
	 * <code>DomainModel</code>.  The <code>Builder</code> will be initialized
	 * with the specified <code>ActivityType</code>.
	 *
	 * @param  model The <code>DomainModel</code>, not null
	 * @param  type  The <code>ActivityType</code>, not null
	 * @return       The <code>Builder</code> instance
	 *
	 * @throws IllegalStateException if the <code>DomainModel</code> is closed
	 * @throws IllegalStateException if the <code>DomainModel</code> does not
	 *                               have a default implementation class for
	 *                               the <code>Activity</code>
	 * @throws IllegalStateException if the <code>DomainModel</code> is
	 *                               immutable
	 */

	public static Builder builder (final DomainModel model, final ActivityType type)
	{
		Preconditions.checkNotNull (model, "model");
		Preconditions.checkNotNull (type, "type");

		return ((Activity.Builder) model.getElementComponent (Activity.class, Activity.getActivityClass (type))
			.getBuilder ())
			.setType (type);
	}

	/**
	 * Create the <code>Activity</code>
	 */

	protected Activity ()
	{
		super ();

		this.reference = null;
	}

	/**
	 * Create the <code>Activity</code> instance from the specified
	 * <code>Builder</code>.
	 *
	 * @param  builder The <code>Builder</code>, not null
	 */

	protected Activity (final Builder builder)
	{
		super (builder);

		this.reference = Preconditions.checkNotNull (builder.getReference ());
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
			.add ("reference", this.getReference ())
			.add ("name", this.getName ());
	}

	/**
	 * Connect all of the relationships for this <code>Activity</code> instance.
	 * This method is intended to be used just after the <code>Activity</code>
	 * is inserted into the <code>DataStore</code>.
	 *
	 * @return <code>true</code> if all of the relationships were successfully
	 *         connected, <code>false</code> otherwise
	 */

	@Override
	protected boolean connect ()
	{
		return Activity.METADATA.relationships ()
				.allMatch (r -> r.connect (this));
	}

	/**
	 * Disconnect all of the relationships for this <code>Activity</code>
	 * instance.  This method is intended to be used just before the
	 * <code>Activity</code> is removed from the <code>DataStore</code>.
	 *
	 * @return <code>true</code> if all of the relationships were successfully
	 *         disconnected, <code>false</code> otherwise
	 */

	@Override
	protected boolean disconnect ()
	{
		return Activity.METADATA.relationships ()
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
		return this.isDependencyHelper (Activity.METADATA, element);
	}

	/**
	 * Compare two <code>Activity</code> instances, based upon the associated
	 * <code>ActivityReference</code>.
	 * <p>
	 * Note:  The result from <code>compareTo</code> may not agree with the
	 * result from <code>equals</code>.
	 *
	 * @param  element The <code>Element</code> to be compared
	 * @return         The value 0 if the <code>Activity</code> instances are
	 *                 equal, less than 0 of the argument is is greater, and
	 *                 greater than 0 if the argument is less than the
	 *                 <code>Activity</code>
	 */

	@Override
	public int compareTo (final Element element)
	{
		Preconditions.checkNotNull (element, "element");

		int result = 0;

		if (this != element)
		{
			if (element instanceof Activity)
			{
				result = this.reference.compareTo (((Activity) element).reference);
			}
			else
			{
				result = super.compareTo (element);
			}
		}

		return result;
	}

	/**
	 * Compare two <code>Activity</code> instances to determine if they are
	 * equal.
	 *
	 * @param  obj The <code>Activity</code> instance to compare to the one
	 *             represented by the called instance
	 * @return     <code>True</code> if the two <code>Activity</code> instances
	 *             are equal, <code>False</code> otherwise
	 */

	@Override
	public boolean equals (final Object obj)
	{
		return (obj == this) ? true : (obj instanceof Activity)
				&& Objects.equals (this.getReference (), ((Activity) obj).getReference ())
				&& Objects.equals (this.getName (), ((Activity) obj).getName ());
	}

	/**
	 * Compute a <code>hashCode</code> of the <code>Activity</code> instance.
	 * The hash code is computed based upon the <code>ActivityType</code> and
	 * the <code>Course</code>.
	 *
	 * @return An <code>Integer</code> containing the hash code
	 */

	@Override
	public int hashCode ()
	{
		return Objects.hash (this.getReference (), this.getName ());
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
		return Activity.METADATA.properties ()
			.filter (p -> p.hasFlags (Property.Flags.RELATIONSHIP))
			.flatMap (p -> p.stream (this))
			.map (e -> ((Element) e));
	}

	/**
	 * Get a <code>String</code> representation of the <code>Activity</code>
	 * instance, including the identifying fields.
	 *
	 * @return A <code>String</code> representation of the
	 *         <code>Activity</code> instance
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
	 * <code>DomainModel</code>.  This method creates a <code>Builder</code>
	 * on the specified <code>DomainModel</code> and initializes it with the
	 * contents of this <code>Activity</code> instance.
	 *
	 * @param  model The <code>DomainModel</code>, not null
	 * @return       The initialized <code>Builder</code>
	 */

	@Override
	public Builder getBuilder (final DomainModel model)
	{
		return Activity.builder (Preconditions.checkNotNull (model, "model"), this.getType ())
			.load (this);
	}

	/**
	 * Get the <code>Course</code> with which the <code>Activity</code> is
	 * associated.
	 *
	 * @return The <code>Course</code> instance
	 */

	@Override
	public Course getCourse ()
	{
		return this.getReference ().getCourse ();
	}

	/**
	 * Get a <code>List</code> of all of the <code>LogEntry</code> instances
	 * which act upon the <code>Activity</code>.
	 *
	 * @return A <code>List</code> of <code>LogEntry</code> instances
	 */

	@Override
	public List<LogEntry> getLog ()
	{
		return this.getReference ().getLog ();
	}

	/**
	 * Get the <code>ActivityType</code> for the <code>Activity</code>.
	 *
	 * @return The <code>ActivityType</code> instance
	 */

	@Override
	public ActivityType getType()
	{
		return this.getReference ().getType ();
	}

	/**
	 * Get the associated <code>ActivityReference</code> instance.  This method
	 * is intended to be used by other <code>Element</code> implementations, as
	 * all of the data stored in the <code>ActivityReference</code> is
	 * accessible though <code>Activity</code>
	 *
	 * @return The <code>ActivityReference</code>
	 */

	@CheckReturnValue
	protected ActivityReference getReference ()
	{
		return this.propagateDomainModel (this.reference);
	}

	/**
	 * Set the associated <code>ActivityReference</code>.   This method is
	 * intended to be used to initialize a new <code>Activity</code> instance.
	 *
	 * @param  reference The <code>ActivityReference</code>, not null
	 */

	protected void setReference (final ActivityReference reference)
	{
		assert reference != null : "reference is NULL";

		this.reference = reference;
	}

	/**
	 * Set the name of the <code>Activity</code>.  This method is intended to
	 * be used to initialize a new <code>Activity</code> instance.
	 *
	 * @param  name The name of the <code>Activity</code>, not null
	 */

	protected abstract void setName (String name);

	/**
	 * Initialize the <code>Set</code> of <code>Grade</code> instances
	 * associated with the <code>Activity</code> instance.  This method is
	 * intended to be used to initialize a new <code>Activity</code> instance.
	 *
	 * @param  grades The <code>Set</code> of <code>Grade</code> instances, not
	 *                null
	 */

	protected abstract void setGrades (Set<Grade> grades);

	/**
	 * Add the specified <code>Grade</code> to the
	 * <code>Activity</code>.
	 *
	 * @param  grade    The <code>Grade</code> to add, not null
	 * @return          <code>True</code> if the <code>Grade</code> was
	 *                  successfully added, <code>False</code> otherwise
	 */

	protected abstract boolean addGrade (Grade grade);

	/**
	 * Remove the specified <code>Grade</code> from the
	 * <code>Activity</code>.
	 *
	 * @param  grade    The <code>Grade</code> to remove, not null
	 * @return          <code>True</code> if the <code>Grade</code> was
	 *                  successfully removed from, <code>False</code> otherwise
	 */

	protected abstract boolean removeGrade (Grade grade);
}
