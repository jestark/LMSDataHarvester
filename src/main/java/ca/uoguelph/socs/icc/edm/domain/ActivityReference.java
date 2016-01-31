/* Copyright (C) 2015, 2016 James E. Stark
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
 * Reference/Proxy for <code>Activity</code> instances.  This class exists as a
 * work-around for broken JPA implementations and broken database schema's.  As
 * such, this class is intended for internal use only.  All of its properties
 * are accessible via <code>Activity</code>.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public abstract class ActivityReference extends Element
{
	/**
	 * Create new <code>ActivityReference</code> instances.  This is an internal
	 * class used to create <code>ActivityReference</code> instances.  Generally,
	 * it should only be used by the <code>ActivityBuilder</code>.
	 *
	 * @author  James E. Stark
	 * @version 1.0
	 */

	public static class Builder extends Element.Builder<ActivityReference>
	{
		/** The <code>IdGenerator</code>*/
		private final IdGenerator idGenerator;

		/** Helper to substitute <code>Course</code> instances */
		private final Retriever<Course> courseRetriever;

		/** Helper to substitute <code>ActivityType</code> instances */
		private final Retriever<ActivityType> typeRetriever;

		/** The <code>DataStore</code> id number for the <code>Activity</code> */
		private @Nullable Long id;

		/** The associated <code>ActivityType</code> */
		private @Nullable ActivityType type;

		/** The associated <code>Course</code> */
		private @Nullable Course course;

		/**
		 * Create the <code>Builder</code>.
		 *
		 * @param  model           The <code>DomainModel</code>, not null
		 * @param  definition      The <code>Definition</code>, not null
		 * @param  idGenerator     The <code>IdGenerator</code>, not null
		 * @param  refRetriever    <code>Retriever</code> for
		 *                         <code>ActivityReference</code>, instances not
		 *                         null
		 * @param  typeRetriever   <code>Retriever</code> for
		 *                         <code>ActivityType</code> instances, not null
		 * @param  courseRetriever <code>Retriever</code> for <code>Course</code>
		 *                         instances, not null
		 */

		protected Builder (
				final DomainModel model,
				final Definition definition,
				final IdGenerator idGenerator,
				final Retriever<ActivityReference> refRetriever,
				final Retriever<ActivityType> typeRetriever,
				final Retriever<Course> courseRetriever)
		{
			super (model, definition, refRetriever);

			assert idGenerator != null : "idGenerator is NULL";
			assert typeRetriever != null : "typeRetriever is NULL";
			assert courseRetriever != null : "courseRetriever is NULL";

			this.idGenerator = idGenerator;
			this.typeRetriever = typeRetriever;
			this.courseRetriever = courseRetriever;

			this.id = null;
			this.course = null;
			this.type = null;
		}

		/**
		 * Create an instance of the <code>ActivityReference</code>.
		 *
		 * @return The new <code>ActivityReference</code> instance
		 *
		 * @throws NullPointerException if any required field is missing
		 */

		@Override
		protected ActivityReference create ()
		{
			this.log.trace ("create:");

			return ((Definition) this.definition).creator.apply (this);
		}

		/**
		 * Implementation of the pre-insert hook to set the ID number.
		 *
		 * @param  ref The <code>ActivityReference</code> to be inserted, not
		 *             null
		 * @return     The <code>ActivityReference</code> to be inserted
		 */

		@Override
		protected ActivityReference preInsert (final ActivityReference ref)
		{
			assert ref != null : "ref is NULL";

			this.log.debug ("Setting ID");
			ref.setId (this.idGenerator.nextId ());

			return ref;
		}

		/**
		 * Reset the builder.  This method will set all of the fields for the
		 * <code>Activity</code> to be built to <code>null</code>.
		 *
		 * @return This <code>ActivityReferenceBuilder</code>
		 */

		@Override
		public Builder clear ()
		{
			this.log.trace ("clear:");

			super.clear ();

			this.id = null;
			this.course = null;
			this.type = null;

			return this;
		}

		/**
		 * Load a <code>ActivityReference</code> instance into the builder.
		 * This method resets the builder and initializes all of its parameters
		 * from the specified <code>Activity</code> instance.  The  parameters
		 * are validated as they are set.
		 *
		 * @param  reference The <code>ActivityReference</code>, not null
		 *
		 * @throws IllegalArgumentException If any of the fields in the
		 *                                  <code>Activity</code> instance to be
		 *                                  loaded are not valid
		 */

		@Override
		public Builder load (final ActivityReference reference)
		{
			this.log.trace ("load: reference={}", reference);

			super.load (reference);

			this.id = reference.getId ();
			this.setType (reference.getType ());
			this.setCourse (reference.getCourse ());

			return this;
		}

		/**
		 * Get the <code>DataStore</code> identifier for the
		 * <code>ActivityReference</code> instance.
		 *
		 * @return The <code>DataStore</code> identifier
		 */

		@CheckReturnValue
		public final Long getId ()
		{
			return this.id;
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
			return this.course;
		}

		/**
		 * Set the <code>Course</code> with which the <code>Activity</code> is
		 * associated.
		 *
		 * @param  course The <code>Course</code>, not null
		 *
		 * @throws IllegalArgumentException If the <code>Course</code> does not
		 *                                  exist in the <code>DataStore</code>
		 */

		public final Builder setCourse (final Course course)
		{
			this.log.trace ("setCourse: course={}", course);

			this.course = this.verifyRelationship (this.courseRetriever, course, "course");

			return this;
		}

		/**
		 * Get the <code>ActivityType</code> for the <code>Activity</code>.
		 *
		 * @return The <code>ActivityType</code> instance
		 */

		@CheckReturnValue
		public final ActivityType getType ()
		{
			return this.type;
		}

		/**
		 * Set the <code>ActivityType</code> for the <code>Activity</code>.
		 *
		 * @param  type The <code>ActivityType</code>, not null
		 *
		 * @throws IllegalArgumentException If the <code>ActivityType</code>
		 *                                  does not exist in the
		 *                                  <code>DataStore</code>
		 */

		public final Builder setType (final ActivityType type)
		{
			this.log.trace ("setType: type={}", type);

			this.type = this.verifyRelationship (this.typeRetriever, type, "type");

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
	@Component (dependencies = {IdGenerator.IdGeneratorComponent.class}, modules = {ActivityReferenceBuilderModule.class})
	protected interface ActivityReferenceComponent extends Element.ElementComponent<ActivityReference>
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
	public static final class ActivityReferenceModule extends Element.ElementModule<ActivityReference> {}

	/**
	 * Dagger module for creating <code>Builder</code> instances.  This module
	 * contains implementation-dependent information.
	 *
	 * @author  James E. Stark
	 * @version 1.0
	 */

	@Module (includes = {ActivityReferenceModule.class, ActivityType.ActivityTypeModule.class, Course.CourseModule.class})
	public static final class ActivityReferenceBuilderModule
	{
		/** The <code>Definition</code> */
		private final Definition definition;

		/**
		 * Create the <code>ActivityReferenceBuilderModule</code>
		 *
		 * @param  definition The <code>Definition</code>, not null
		 */

		public ActivityReferenceBuilderModule (final Definition definition)
		{
			assert definition != null : "definition is NULL";
			this.definition = definition;
		}

		/**
		 * Create the <code>Builder</code>.
		 *
		 * @param  model           The <code>DomainModel</code>, not null
		 * @param  generator       The <code>IdGenerator</code>, not null
		 * @param  refRetriever    <code>Retriever</code> for
		 *                         <code>ActivityReference</code>, instances not
		 *                         null
		 * @param  typeRetriever   <code>Retriever</code> for
		 *                         <code>ActivityType</code> instances, not null
		 * @param  courseRetriever <code>Retriever</code> for <code>Course</code>
		 *                         instances, not null
		 */

		@Provides
		public Builder createBuilder (
				final DomainModel model,
				final IdGenerator generator,
				final @Named ("TableRetriever") Retriever<ActivityReference> refRetriever,
				final @Named ("QueryRetriever") Retriever<ActivityType> typeRetriever,
				final @Named ("QueryRetriever") Retriever<Course> courseRetriever)
		{
			return new Builder (model, this.definition, generator, refRetriever, typeRetriever, courseRetriever);
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

	protected static abstract class Definition extends Element.Definition<ActivityReference>
	{
		/** Method reference to the implementation constructor  */
		private final Function<ActivityReference.Builder, ActivityReference> creator;

		/**
		 * Create the <code>Definition</code>.
		 *
		 * @param  impl    The implementation class, not null
		 * @param  creator Method reference to the constructor, not null
		 */

		public Definition (
				final Class<? extends ActivityReference> impl,
				final Function<ActivityReference.Builder, ActivityReference> creator)
		{
			super (ActivityReference.METADATA, impl);

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
		protected ActivityReference.ActivityReferenceComponent getComponent (final DomainModel model)
		{
			return DaggerActivityReference_ActivityReferenceComponent.builder ()
				.idGeneratorComponent (model.getIdGeneratorComponent (this.impl))
				.domainModelModule (new DomainModel.DomainModelModule (Activity.class, model))
				.activityReferenceBuilderModule (new ActivityReferenceBuilderModule (this))
				.build ();
		}
	}

	/** Serial version id, required by the Serializable interface */
	private static final long serialVersionUID = 1L;

	/** The <code>MetaData</code> for the <code>ActivityReference</code> */
	protected static final MetaData<ActivityReference> METADATA;

	/** The <code>DataStore</code> identifier of the <code>ActivityReference</code> */
	public static final Property<ActivityReference, Long> ID;

	/** The <code>DomainModel</code> which contains the <code>ActivityReference</code> */
	public static final Property<ActivityReference, DomainModel> MODEL;

	/** The associated <code>Activity</code> */
	public static final Property<ActivityReference, Activity> ACTIVITY;

	/** The associated <code>Course</code> */
	public static final Property<ActivityReference, Course> COURSE;

	/** The associated <code>ActivityType</code> */
	public static final Property<ActivityReference, ActivityType> TYPE;

	/** The <code>Grade</code> instances associated with the <code>Activity</code> */
	public static final Property<ActivityReference, Grade> GRADES;

	/** The <code>LogEntry</code> instances associated with the <code>Activity</code> */
	public static final Property<ActivityReference, LogEntry> LOGENTRIES;

	/** Select the <code>ActivityReference</code> instance by its id */
	public static final Selector<ActivityReference> SELECTOR_ID;

	/** Select all of the <code>ActivityReference</code> instances */
	public static final Selector<ActivityReference> SELECTOR_ALL;

	/** Select all <code>Activity</code> instances by <code>ActivityType</code> */
	public static final Selector<ActivityReference> SELECTOR_TYPE;

	/**
	 * Initialize the <code>MetaData</code>, <code>Property</code> and
	 * <code>Selector</code> instances for the <code>Activity</code>.
	 */

	static
	{
		ID = Property.of (ActivityReference.class, Long.class, "id",
				ActivityReference::getId, ActivityReference::setId);

		MODEL = Property.of (ActivityReference.class, DomainModel.class, "domainmodel",
				ActivityReference::getDomainModel, ActivityReference::setDomainModel);

		ACTIVITY = Property.of (ActivityReference.class, Activity.class, "activity",
				ActivityReference::getActivity, ActivityReference::setActivity,
				Property.Flags.RECOMMENDED);

		COURSE = Property.of (ActivityReference.class, Course.class, "course",
				ActivityReference::getCourse, ActivityReference::setCourse,
				Property.Flags.REQUIRED);

		TYPE = Property.of (ActivityReference.class, ActivityType.class, "type",
				ActivityReference::getType, ActivityReference::setType,
				Property.Flags.REQUIRED);

		GRADES = Property.of (ActivityReference.class, Grade.class, "grades",
				ActivityReference::getGrades, ActivityReference::addGrade, ActivityReference::removeGrade);

		LOGENTRIES = Property.of (ActivityReference.class, LogEntry.class, "logentries",
				ActivityReference::getLog, ActivityReference::addLog, ActivityReference::removeLog);

		SELECTOR_ID = Selector.of (Selector.Cardinality.KEY, ID);
		SELECTOR_TYPE = Selector.of (Selector.Cardinality.MULTIPLE, TYPE);

		SELECTOR_ALL = Selector.builder (ActivityReference.class)
			.setCardinality (Selector.Cardinality.MULTIPLE)
			.setName ("all")
			.build ();

		METADATA = MetaData.builder (ActivityReference.class)
			.addProperty (ID)
			.addProperty (MODEL)
			.addProperty (ACTIVITY)
			.addProperty (GRADES)
			.addProperty (LOGENTRIES)
			.addRelationship (COURSE, Course.METADATA, Course.ACTIVITIES)
			.addRelationship (TYPE, ActivityType.METADATA, SELECTOR_TYPE)
			.addSelector (SELECTOR_ID)
			.addSelector (SELECTOR_ALL)
			.build ();
	}

	/**
	 * Create the <code>ActivityReference</code>.
	 */

	protected ActivityReference ()
	{
		super ();
	}

	/**
	 * Create the <code>ActivityReference</code> instance from the specified
	 * <code>Builder</code>.
	 *
	 * @param  builder The <code>Builder</code>, not null
	 */

	protected ActivityReference (final Builder builder)
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
			.add ("type", this.getType ())
			.add ("course", this.getCourse ());
	}

	/**
	 * Connect all of the relationships for this <code>ActivityReference</code> instance.
	 * This method is intended to be used just after the <code>ActivityReference</code> is
	 * inserted into the <code>DataStore</code>.
	 *
	 * @return <code>true</code> if all of the relationships were successfully
	 *         connected, <code>false</code> otherwise
	 */

	@Override
	protected boolean connect ()
	{
		return ActivityReference.METADATA.relationships ()
				.allMatch (r -> r.connect (this));
	}

	/**
	 * Disconnect all of the relationships for this
	 * <code>ActivityReference</code> instance.  This method is intended to be
	 * used just before the <code>ActivityReference</code> is removed from the
	 * <code>DataStore</code>.
	 *
	 * @return <code>true</code> if all of the relationships were successfully
	 *         disconnected, <code>false</code> otherwise
	 */

	@Override
	protected boolean disconnect ()
	{
		return ActivityReference.METADATA.relationships ()
				.allMatch (r -> r.disconnect (this));
	}

	/**
	 * Compare two <code>ActivityReference</code> instances.
	 * <code>ActivityReference</code> instances are compared by their
	 * <code>DataStore</code> ID.
	 * <p>
	 * Note:  The result from <code>compareTo</code> may not agree with the
	 * result from <code>equals</code>.
	 *
	 * @param  element The <code>Element</code> to be compared
	 * @return         The value 0 if the <code>ActivityReference</code>
	 *                 instances are  equal, less than 0 of the argument is is
	 *                 greater, and greater than  0 if the argument is less than
	 *                 the <code>ActivityReference</code>
	 */

	@Override
	public int compareTo (final Element element)
	{
		Preconditions.checkNotNull (element, "element");

		int result = 0;

		if (this != element)
		{
			if (element instanceof ActivityReference)
			{
				result = this.getId ().compareTo (element.getId ());
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
		return (obj == this) ? true : (obj instanceof ActivityReference)
			&& Objects.equals (this.getType (), ((ActivityReference) obj).getType ())
			&& Objects.equals (this.getCourse (), ((ActivityReference) obj).getCourse ());
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
		return Objects.hash (this.getType (), this.getCourse ());
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
	 * Get a <code>Stream</code> containing all of the associated
	 * <code>Element</code> instances.
	 *
	 * @return The <code>Stream</code>
	 */

	@Override
	public Stream<Element> associations ()
	{
		return ActivityReference.METADATA.properties ()
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
		return ActivityReference.METADATA.properties ()
			.filter (p -> p.hasFlags (Property.Flags.RELATIONSHIP))
			.filter (p -> p.hasFlags (Property.Flags.REQUIRED) || p.hasFlags (Property.Flags.MUTABLE))
			.flatMap (p -> p.stream (this))
			.map (e -> (Element) e);
	}

	/**
	 * Get an <code>Builder</code> instance for the specified
	 * <code>DomainModel</code>.  This method creates a <code>Builder</code> on
	 * the specified <code>DomainModel</code> and initializes it with the
	 * contents of this <code>ActivityReference</code> instance.
	 * <p>
	 * <code>ActivityReference</code> instances are created though the builder
	 * for the associated <code>Activity</code> instance.
	 *
	 * @param  model The <code>DomainModel</code>, not null
	 * @return       The initialized <code>Builder</code>
	 */

	@Override
	public Builder getBuilder (final DomainModel model)
	{
		Preconditions.checkNotNull (model, "model");

		return ((ActivityReference.Builder) model.getElementComponent (ActivityReference.class)
			.getBuilder ())
			.load (this);
	}

	/**
	 * Get the <code>Course</code> with which the <code>Activity</code> is
	 * associated.
	 *
	 * @return The <code>Course</code> instance
	 */

	public abstract Course getCourse ();

	/**
	 * Set the <code>Course</code> with which the <code>Activity</code> is
	 * associated.  This method is intended to be used to initialize a new
	 * <code>ActivityReference</code>.
	 *
	 * @param  course The <code>Course</code>, not null
	 */

	protected abstract void setCourse (Course course);

	/**
	 * Get the <code>ActivityType</code> for the <code>Activity</code>.
	 *
	 * @return The <code>ActivityType</code> instance
	 */

	public abstract ActivityType getType ();

	/**
	 * Set the <code>ActvityType</code> with which the <code>Activity</code> is
	 * associated.  This method is intended to be used to initialize a new
	 * <code>ActivityReference</code>.
	 *
	 * @param  type The <code>ActivityType</code>, not null
	 */

	protected abstract void setType (ActivityType type);

	/**
	 * Get the <code>Activity</code> that this <code>ActivityReference</code>
	 * is representing.
	 *
	 * @return The <code>Activity</code>
	 */

	public abstract Activity getActivity ();

	/**
	 * Set the reference to the <code>Activity</code> which contains the actual
	 * data.
	 *
	 * @param  activity The <code>Activity</code>, not null
	 */

	protected abstract void setActivity (Activity activity);

	/**
	 * Get the <code>List</code> of <code>Grade</code> instances which are
	 * associated with the <code>Activity</code>.  Not all
	 * <code>Activity</code> instances are graded.  If the
	 * <code>Activity</code> does is not graded then the <code>List</code> will
	 * be empty.
	 *
	 * @return A <code>List</code> of <code>Grade</code> instances
	 */

	public abstract List<Grade> getGrades ();

	/**
	 * Initialize the <code>List</code> of <code>Grade</code> instances
	 * associated with the <code>Activity</code> instance.  This method is
	 * intended to be used to initialize a new <code>Activity</code> instance.
	 *
	 * @param  grades The <code>List</code> of <code>Grade</code> instances, not
	 *                null
	 */

	protected abstract void setGrades (List<Grade> grades);

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

	/**
	 * Get a <code>List</code> of all of the <code>LogEntry</code> instances
	 * which act upon the <code>ActivityReference</code>.
	 *
	 * @return A <code>List</code> of <code>LogEntry</code> instances
	 */

	public abstract List<LogEntry> getLog ();

	/**
	 * Initialize the <code>List</code> of <code>LogEntry</code> instances
	 * associated with the <code>ActivityReference</code> instance.  This
	 * method is intended to be used to initialize a new
	 * <code>ActivityReference</code>.
	 *
	 * @param  log The <code>List</code> of <code>LogEntry</code> instances,
	 *             not null
	 */

	protected abstract void setLog (List<LogEntry> log);

	/**
	 * Add the specified <code>LogEntry</code> to the specified
	 * <code>ActivityReference</code>.
	 *
	 * @param  entry    The <code>LogEntry</code> to add, not null
	 * @return          <code>True</code> if the <code>LogEntry</code> was
	 *                  successfully added, <code>False</code> otherwise
	 */

	protected abstract boolean addLog (LogEntry entry);

	/**
	 * Remove the specified <code>LogEntry</code> from the specified
	 * <code>ActivityReference</code>.
	 *
	 * @param  entry    The <code>LogEntry</code> to remove, not null
	 * @return          <code>True</code> if the <code>LogEntry</code> was
	 *                  successfully removed, <code>False</code> otherwise
	 */

	protected abstract boolean removeLog (LogEntry entry);
}
