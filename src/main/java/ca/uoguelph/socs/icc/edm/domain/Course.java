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

import java.util.List;
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
 * A representation of a course within the domain model.  Instances of the
 * <code>Course</code> interface contain the identifying information for a
 * particular offering of a course, including its name and semester (and year)
 * of offering.  The instances of the <code>Course</code> interface act as a
 * container for all of the data, via the associated instances of the
 * <code>Activity</code> and <code>Enrolment</code> interfaces, concerning a
 * particular offering of a course.
 * <p>
 * Within the domain model the <code>Course</code> interface is a root level
 * element, as such instances of the <code>Course</code> interface are not
 * dependant upon any other domain model element to exist.  An associated
 * instance of the <code>Course</code> interface is required for an instance of
 * the <code>Enrolment</code>, or <code>Activity</code> interfaces to exist.
 * If a particular instance of the <code>Course</code> interface is deleted,
 * then all of the associated instances of the <code>Activity</code> and
 * <code>Enrolment</code> interfaces must be deleted as well.
 * <p>
 * Once created, <code>Course</code> instances are immutable.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public abstract class Course extends Element
{
	/**
	 * Create new <code>Course</code> instances.  This class extends
	 * <code>AbstractBuilder</code>, adding the functionality required to
	 * create <code>Course</code> instances.
	 *
	 * @author  James E. Stark
	 * @version 1.0
	 */

	public static class Builder extends Element.Builder<Course>
	{
		/** The <code>IdGenerator</code>*/
		private final IdGenerator idGenerator;

		/** The <code>DataStore</code> id number for the <code>Course</code> */
		private @Nullable Long id;

		/** The name of the <code>Course</code> */
		private @Nullable String name;

		/** The <code>Semester</code> of offering */
		private @Nullable Semester semester;

		/** The year of offering */
		private @Nullable Integer year;

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
				final Retriever<Course> retriever)
		{
			super (model, definition, retriever);

			assert idGenerator != null : "idGenerator is NULL";
			this.idGenerator = idGenerator;

			this.id = null;
			this.name = null;
			this.semester = null;
			this.year = null;
		}

		/**
		 * Create an instance of the <code>Course</code>.
		 *
		 * @return The new <code>Course</code> instance
		 *
		 * @throws NullPointerException if any required field is missing
		 */

		@Override
		protected Course create ()
		{
			this.log.trace ("create:");

			return ((Definition) this.definition).creator.apply (this);
		}

		/**
		 * Implementation of the pre-insert hook to set the ID number.
		 *
		 * @param  course The <code>Course</code> to be inserted, not null
		 * @return        The <code>Course</code> to be inserted
		 */

		@Override
		protected Course preInsert (final Course course)
		{
			assert course != null : "course is NULL";

			this.log.debug ("Setting ID");
			course.setId (this.idGenerator.nextId ());

			return course;
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
			this.semester = null;
			this.year = null;

			return this;
		}

		/**
		 * Load a <code>Course</code> instance into the builder.  This method
		 * resets the builder and initializes all of its parameters from
		 * the specified <code>Course</code> instance.  The  parameters are
		 * validated as they are set.
		 *
		 * @param  course The <code>Course</code>, not null
		 * @return        This <code>Builder</code>
		 *
		 * @throws IllegalArgumentException If any of the fields in the
		 *                                  <code>Course</code> instance to be
		 *                                  loaded are not valid
		 */

		@Override
		public Builder load (final Course course)
		{
			this.log.trace ("load course={}", course);

			super.load (course);

			this.id = course.getId ();
			this.setName (course.getName ());
			this.setSemester (course.getSemester ());
			this.setYear (course.getYear ());

			return this;
		}

		/**
		 * Get the <code>DataStore</code> identifier for the <code>Course</code>
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
		 * Get the name of the <code>Course</code>.
		 *
		 * @return A <code>String</code> containing the name of the
		 *         <code>Course</code>
		 */

		@CheckReturnValue
		public final String getName ()
		{
			return this.name;
		}

		/**
		 * Set the name of the <code>Course</code>.
		 *
		 * @param  name The name of the <code>Course</code>,not null
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
		 * Get the <code>Semester</code> in which the <code>Course</code> was
		 * offered.
		 *
		 * @return The <code>Semester</code> of offering
		 */

		@CheckReturnValue
		public final Semester getSemester ()
		{
			return this.semester;
		}

		/**
		 * Set the <code>Semester</code> in which the <code>Course</code> was
		 * offered.
		 *
		 * @param  semester The <code>Semester</code> of offering, not null
		 * @return          This <code>Builder</code>
		 */

		public final Builder setSemester (final Semester semester)
		{
			this.log.trace ("setSemester: semester={}", semester);

			this.semester = Preconditions.checkNotNull (semester);

			return this;
		}

		/**
		 * Get the year in which the <code>Course</code> was offered.
		 *
		 * @return An <code>Integer</code> containing the year of offering
		 */

		@CheckReturnValue
		public final Integer getYear ()
		{
			return this.year;
		}

		/**
		 * Set the year in which the <code>Course</code> was offered.
		 *
		 * @param  year The year of offering, not null
		 * @return      This <code>Builder</code>
		 *
		 * @throws IllegalArgumentException If the year is negative
		 */

		public final Builder setYear (final Integer year)
		{
			this.log.trace ("setYear: year={}", year);

			Preconditions.checkNotNull (year);
			Preconditions.checkArgument (year > 0, "year must be greater than zero");

			this.year = year;

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
	@Component (dependencies = {IdGenerator.IdGeneratorComponent.class}, modules = {CourseBuilderModule.class})
	protected interface CourseComponent extends Element.ElementComponent<Course>
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
	public static final class CourseModule extends Element.ElementModule<Course>
	{
		/**
		 * Get the <code>Selector</code> used by the
		 * <code>QueryRetriever</code>.
		 *
		 * @return The <code>Selector</code>
		 */

		@Provides
		public Selector<Course> getSelector ()
		{
			return Course.SELECTOR_OFFERING;
		}
	}

	/**
	 * Dagger module for creating <code>Builder</code> instances.  This module
	 * contains implementation-dependent information.
	 *
	 * @author  James E. Stark
	 * @version 1.0
	 */

	@Module (includes = {CourseModule.class})
	public static final class CourseBuilderModule
	{
		/** The <code>Definition</code> */
		private final Definition definition;

		/**
		 * Create the <code>CourseBuilderModule</code>
		 *
		 * @param  definition The <code>Definition</code>, not null
		 */

		public CourseBuilderModule (final Definition definition)
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
				final @Named ("QueryRetriever") Retriever<Course> retriever)
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

	protected static abstract class Definition extends Element.Definition<Course>
	{
		/** Method reference to the implementation constructor  */
		private final Function<Course.Builder, Course> creator;

		/**
		 * Create the <code>Definition</code>.
		 *
		 * @param  impl    The implementation class, not null
		 * @param  creator Method reference to the constructor, not null
		 */

		public Definition (
				final Class<? extends Course> impl,
				final Function<Course.Builder, Course> creator)
		{
			super (Course.METADATA, impl);

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
		protected Course.CourseComponent getComponent (final DomainModel model)
		{
			return DaggerCourse_CourseComponent.builder ()
				.idGeneratorComponent (model.getIdGeneratorComponent (this.impl))
				.domainModelModule (new DomainModel.DomainModelModule (Course.class, model))
				.courseBuilderModule (new CourseBuilderModule (this))
				.build ();
		}
	}

	/** Serial version id, required by the Serializable interface */
	private static final long serialVersionUID = 1L;

	/** The <code>MetaData</code> for the <code>Course</code> */
	protected static final MetaData<Course> METADATA;

	/** The <code>DataStore</code> identifier of the <code>Course</code> */
	public static final Property<Course, Long> ID;

	/** The <code>DomainModel</code> which contains the <code>Course</code> */
	public static final Property<Course, DomainModel> MODEL;

	/** The name of the <code>Course</code> */
	public static final Property<Course, String> NAME;

	/** The <code>Semester</code> of offering for the <code>Course</code> */
	public static final Property<Course, Semester> SEMESTER;

	/** The year of offering for the <code>Course</code> */
	public static final Property<Course, Integer> YEAR;

	/** The <code>Activity</code> instances associated with the <code>Course</code> */
	public static final Property<Course, ActivityReference> ACTIVITIES;

	/** The <code>Enrolment</code> instances associated with the <code>Course</code> */
	public static final Property<Course, Enrolment> ENROLMENTS;

	/** Select the <code>Course</code> instance by its id */
	public static final Selector<Course> SELECTOR_ID;

	/** Select all of the <code>Course</code> instances */
	public static final Selector<Course> SELECTOR_ALL;

	/** Select an <code>Course</code> instance by its name and date of offering */
	public static final Selector<Course> SELECTOR_OFFERING;

	/**
	 * Initialize the <code>MetaData</code>, <code>Property</code> and
	 * <code>Selector</code> instances for the <code>Course</code>.
	 */

	static
	{
		ID = Property.of (Course.class, Long.class, "id",
				Course::getId, Course::setId);

		MODEL = Property.of (Course.class, DomainModel.class, "domainmodel",
				Course::getDomainModel, Course::setDomainModel);

		NAME = Property.of (Course.class, String.class, "name",
				Course::getName, Course::setName,
				Property.Flags.REQUIRED);

		SEMESTER = Property.of (Course.class, Semester.class, "semester",
				Course::getSemester, Course::setSemester,
				Property.Flags.REQUIRED);

		YEAR = Property.of (Course.class, Integer.class, "year",
				Course::getYear, Course::setYear,
				Property.Flags.REQUIRED);

		ACTIVITIES = Property.of (Course.class, ActivityReference.class, "activities",
				Course::getActivityReferences, Course::addActivityReference, Course::removeActivityReference,
				Property.Flags.RECOMMENDED);

		ENROLMENTS = Property.of (Course.class, Enrolment.class, "enrolments",
				Course::getEnrolments, Course::addEnrolment, Course::removeEnrolment,
				Property.Flags.RECOMMENDED);

		SELECTOR_ID = Selector.of (Selector.Cardinality.KEY, ID);

		SELECTOR_ALL =  Selector.builder (Course.class)
			.setCardinality (Selector.Cardinality.MULTIPLE)
			.setName ("all")
			.build ();

		SELECTOR_OFFERING =  Selector.builder (Course.class)
			.setCardinality (Selector.Cardinality.SINGLE)
			.setName ("offering")
			.addProperty (NAME)
			.addProperty (SEMESTER)
			.addProperty (YEAR)
			.build ();

		METADATA = MetaData.builder (Course.class)
			.addProperty (ID)
			.addProperty (MODEL)
			.addProperty (NAME)
			.addProperty (SEMESTER)
			.addProperty (YEAR)
			.addProperty (ACTIVITIES)
			.addProperty (ENROLMENTS)
			.addSelector (SELECTOR_ID)
			.addSelector (SELECTOR_ALL)
			.addSelector (SELECTOR_OFFERING)
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
	 *                               the <code>Course</code>
	 * @throws IllegalStateException if the <code>DomainModel</code> is
	 *                               immutable
	 */

	public static Builder builder (final DomainModel model)
	{
		Preconditions.checkNotNull (model, "model");

		return (Course.Builder) model.getElementComponent (Course.class)
			.getBuilder ();
	}

	/**
	 * Create the <code>Course</code>.
	 */

	protected Course ()
	{
		super ();
	}

	/**
	 * Create the <code>Course</code> instance from the specified
	 * <code>Builder</code>.
	 *
	 * @param  builder The <code>Builder</code>, not null
	 */

	protected Course (final Builder builder)
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
			.add ("name", this.getName ())
			.add ("semester", this.getSemester ())
			.add ("year", this.getYear ());
	}

	/**
	 * Connect all of the relationships for this <code>Course</code> instance.
	 * This method is intended to be used just after the <code>Course</code> is
	 * inserted into the <code>DataStore</code>.
	 *
	 * @return <code>true</code> if all of the relationships were successfully
	 *         connected, <code>false</code> otherwise
	 */

	@Override
	protected boolean connect ()
	{
		return Course.METADATA.relationships ()
				.allMatch (r -> r.connect (this));
	}

	/**
	 * Disconnect all of the relationships for this <code>Course</code>
	 * instance.  This method is intended to be used just before the
	 * <code>Course</code> is removed from the <code>DataStore</code>.
	 *
	 * @return <code>true</code> if all of the relationships were successfully
	 *         disconnected, <code>false</code> otherwise
	 */

	@Override
	protected boolean disconnect ()
	{
		return Course.METADATA.relationships ()
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
		return Course.METADATA.dependencies ()
			.anyMatch (d -> d.isAssignableFrom (element));
	}

	/**
	 * Compare two <code>Course</code> instances.  The comparison is based upon
	 * the names of the <code>Course</code> instances and the date of offering.
	 *
	 * @param  element The <code>Element</code> to be compared
	 * @return         The value 0 if the <code>Course</code> instances
	 *                 are equal, less than 0 of the argument is is greater, and
	 *                 greater than 0 if the argument is less than the
	 *                 <code>Course</code>
	 */

	@Override
	public int compareTo (final Element element)
	{
		Preconditions.checkNotNull (element, "element");

		int result = 0;

		if (this != element)
		{
			if (element instanceof Course)
			{
				result = this.getYear ().compareTo (((Course) element).getYear ());

				if (result == 0)
				{
					result = this.getSemester ().compareTo (((Course) element).getSemester ());

					if (result == 0)
					{
						result = this.getName ().compareTo (((Course) element).getName ());
					}
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
	 * Compare two <code>Course</code> instances to determine if they are
	 * equal.  The <code>Course</code> instances are compared based upon their
	 * names, as well as their year and <code>Semester</code> of offering.
	 *
	 * @param  obj The <code>Course</code> instance to compare to the one
	 *             represented by the called instance
	 * @return     <code>True</code> if the two <code>Course</code> instances
	 *             are equal, <code>False</code> otherwise
	 */

	@Override
	public boolean equals (final Object obj)
	{
		return (obj == this) ? true : (obj instanceof Course)
			&& Objects.equals (this.getName (), ((Course) obj).getName ())
			&& Objects.equals (this.getYear (), ((Course) obj).getYear ())
			&& Objects.equals (this.getSemester (), ((Course) obj).getSemester ());
	}

	/**
	 * Compute a <code>hashCode</code> of the <code>Course</code> instance.
	 * The hash code is computed based upon the name of the instance as well as
	 * the year and <code>Semester</code> of offering.
	 *
	 * @return An <code>Integer</code> containing the hash code
	 */

	@Override
	public int hashCode ()
	{
		return Objects.hash (this.getName (), this.getYear (), this.getSemester ());
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
		return Course.METADATA.properties ()
			.filter (p -> p.hasFlags (Property.Flags.RELATIONSHIP))
			.flatMap (p -> p.stream (this))
			.map (e -> ((Element) e));
	}

	/**
	 * Get a <code>String</code> representation of the <code>Course</code>
	 * instance, including the identifying fields.
	 *
	 * @return A <code>String</code> representation of the <code>Course</code>
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
	 * Get an <code>Builder</code> instance for the specified
	 * <code>DomainModel</code>.  This method creates a <code>Builder</code> on
	 * the specified <code>DomainModel</code> and initializes it with the
	 * contents of this <code>Course</code> instance.
	 *
	 * @param  model The <code>DomainModel</code>, not null
	 * @return       The initialized <code>Builder</code>
	 */

	@Override
	public Builder getBuilder (final DomainModel model)
	{
		return Course.builder (Preconditions.checkNotNull (model, "model"))
			.load (this);
	}

	/**
	 * Get the name of the <code>Course</code>.
	 *
	 * @return A <code>String</code> containing the name of the
	 *         <code>Course</code>
	 */

	public abstract String getName ();

	/**
	 * Set the name of the <code>Course</code>.  This method is intended to be
	 * used to be used to initialize a new <code>Course</code> instance.
	 *
	 * @param  name The name of the <code>Course</code>
	 */

	protected abstract void setName (String name);

	/**
	 * Get the <code>Semester</code> in which the <code>Course</code> was
	 * offered.
	 *
	 * @return The <code>Semester</code> of offering
	 */

	public abstract Semester getSemester ();

	/**
	 * Set the <code>Semester</code> in which the <code>Course</code> was
	 * offered.  This method is intended to be used to be used to initialize a
	 * new <code>Course</code> instance.
	 *
	 * @param  semester The <code>Semester</code> in which the
	 *                  <code>Course</code> was offered
	 */

	protected abstract void setSemester (Semester semester);

	/**
	 * Get the year in which the <code>Course</code> was offered.
	 *
	 * @return An <code>Integer</code> containing the year of offering
	 */

	public abstract Integer getYear ();

	/**
	 * Set the year in which the <code>Course</code> was offered.  This method
	 * is intended to be used to be used to initialize a new <code>Course</code>
	 * instance.
	 *
	 * @param  year The year in which the <code>Course</code> was offered
	 */

	protected abstract void setYear (Integer year);

	/**
	 * Get the <code>List</code> of <code>Activity</code> instances which are
	 * associated with the <code>Course</code>.  The <code>List</code> will be
	 * empty if there are no <code>Activity</code> instances associated with
	 * the <code>Course</code>.
	 *
	 * @return A <code>List</code> of <code>Activity</code> instances
	 */

	public abstract List<Activity> getActivities ();

	/**
	 * Get the <code>List</code> <code>ActivityReference</code> instances which
	 * are associated with the <code>Course</code>.
	 *
	 * @return A <code>List</code> of <code>ActivityReference</code> instances
	 */

	protected abstract List<ActivityReference> getActivityReferences ();

	/**
	 * Initialize the <code>List</code> of <code>Activity</code> instances
	 * associated with the <code>Course</code> instance.  This method is
	 * intended to be used to be used to initialize a new <code>Course</code>
	 * instance.
	 *
	 * @param  activities The <code>List</code> of <code>Activity</code>
	 *                    instances, not null
	 */

	protected abstract void setActivityReferences (List<ActivityReference> activities);

	/**
	 * Add the specified <code>ActivityReference</code> to the
	 * <code>Course</code>.
	 *
	 * @param  activity The <code>Activityreference</code> to add, not null
	 * @return          <code>True</code> if the <code>ActivityReference</code>
	 *                  was successfully added, <code>False</code> otherwise
	 */

	protected abstract boolean addActivityReference (ActivityReference activity);

	/**
	 * Remove the specified <code>ActivityReference</code> from the
	 * <code>Course</code>.
	 *
	 * @param  activity The <code>ActivityReference</code> to remove,  not null
	 * @return          <code>True</code> if the <code>ActivityReference</code>
	 *                  was successfully removed, <code>False</code> otherwise
	 */

	protected abstract boolean removeActivityReference (ActivityReference activity);

	/**
	 * Get the <code>List</code> of <code>Enrolment</code> instances which are
	 * associated with the <code>Course</code>.  The <code>List</code> will be
	 * empty if no one is enrolled in the <code>Course</code>.
	 *
	 * @return A <code>List</code> of <code>Enrolment</code> instances
	 */

	public abstract List<Enrolment> getEnrolments ();

	/**
	 * Initialize the <code>List</code> of <code>Enrolment</code> instances
	 * associated with the <code>Course</code> instance.  This method is
	 * intended to be used to initialize a new <code>Course</code> instance.
	 *
	 * @param  enrolments The <code>List</code> of <code>Enrolment</code>
	 *                    instances, not null
	 */

	protected abstract void setEnrolments (List<Enrolment> enrolments);

	/**
	 * Add the specified <code>Enrolment</code> to the <code>Course</code>.
	 *
	 * @param  enrolment The <code>Enrolment</code> to add, not null
	 * @return           <code>True</code> if the <code>Enrolment</code> was
	 *                   successfully added, <code>False</code> otherwise
	 */

	protected abstract boolean addEnrolment (Enrolment enrolment);

	/**
	 * Remove the specified <code>Enrolment</code> from the
	 * <code>Course</code>.
	 *
	 * @param  enrolment The <code>Enrolment</code> to remove, not null
	 * @return           <code>True</code> if the <code>Enrolment</code> was
	 *                   successfully removed, <code>False</code> otherwise
	 */

	protected abstract boolean removeEnrolment (Enrolment enrolment);
}

