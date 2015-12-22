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
import java.util.Set;
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
 * A representation of the participation of a particular <code>User</code> in a
 * <code>Course</code>.  The purpose of the <code>Enrolment</code> interface,
 * and its implementations, is to separate data about the participation in a
 * <code>Course</code> by a particular <code>User</code> from the data that
 * identifies the <code>User</code>.  As such, the <code>Enrolment</code>
 * interface act as an anonymous place holders for the <code>User</code>
 * interface, within the remainder of the domain model.
 * <p>
 * The <code>Enrolment</code> interface (and its implementations) has a strong
 * dependency on the <code>Course</code> and <code>Role</code> interfaces, and
 * a weak dependency on the <code>User</code> interface.  Associated instances
 * of the <code>Course</code> and <code>Role</code> interfaces are required for
 * an instance of the <code>Enrolment</code> interface to exist.  If one of
 * these required instances is deleted, then the <code>Enrolment</code>
 * instance must be deleted as well.  An instance of the <code>User</code>
 * interface should be present for the initial creation of the
 * <code>Enrolment</code> instance.  However, it should be possible, if
 * difficult, to create an <code>Enrolment</code> instance without an
 * associated <code>User</code> instance.  Deletion of the associated
 * <code>User</code> instance should not impact the <code>Enrolment</code>
 * instance.
 * <p>
 * Instances of the <code>Grade</code> and <code>LogEntry</code> interfaces
 * have a strong dependency on the <code>Enrolment</code> interface.  If an
 * instance of the <code>Enrolment</code> interface is deleted, then the
 * associated instances of the <code>Grade</code> and <code>LogEntry</code>
 * interfaces must be deleted as well.
 * <p>
 * A single <code>Enrolment</code> instance exists to represent the
 * participation of a single <code>User</code> in a single <code>Course</code>.
 * Normally the <code>User</code> and <code>Course</code> instances would be
 * used to uniquely identify the <code>Enrolment</code>.  Since the
 * <code>Enrolment</code> instance does not contain a link to the associated
 * <code>User</code> instance (which may not exist in the
 * <code>DomainModel</code>), <code>Enrolment</code> uses its
 * <code>DomainModel</code> ID as a stand in for the <code>User</code> during
 * comparisons.  As a result, two otherwise identical <code>Enrolment</code>
 * instances from different data stores will probably compare as different.
 * <p>
 * Once created an <code>Enrolment</code> instance is immutable.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public abstract class Enrolment extends Element
{
	/**
	 * Create new <code>Enrolment</code> instances.  This class extends
	 * <code>AbstractBuilder</code>, adding the functionality required to
	 * create <code>Enrolment</code> instances.  The "finalgrade" and "usable"
	 * fields may be modified in place.
	 *
	 * @author  James E. Stark
	 * @version 1.0
	 */

	public static class Builder extends Element.Builder<Enrolment>
	{
		/** The <code>IdGenerator</code>*/
		private final IdGenerator idGenerator;

		/** Helper to substitute <code>Course</code> instances */
		private final Retriever<Course> courseRetriever;

		/** Helper to substitute <code>Activity</code> instances */
		private final Retriever<Role> roleRetriever;

		/** The <code>DataStore</code> ID number for the <code>Enrolment</code> */
		private @Nullable Long id;

		/** The associated <code>Course</code> */
		private @Nullable Course course;

		/** The associated <code>Role</code> */
		private @Nullable Role role;

		/** The final grade */
		private @Nullable Integer finalGrade;

		/** Indication if the data is usable for research */
		private @Nullable Boolean usable;

		/**
		 * Create the <code>Builder</code>.
		 *
		 * @param  model              The <code>DomainModel</code>, not null
		 * @param  definition         The <code>Definition</code>, not null
		 * @param  idGenerator        The <code>IdGenerator</code>, not null
		 * @param  enrolmentRetriever <code>Retriever</code> for
		 *                            <code>Enrolment</code> instances, not null
		 * @param  roleRetriever      <code>Retriever</code> for
		 *                            <code>Role</code> instances, not null
		 * @param  courseRetriever    <code>Retriever</code> for
		 *                            <code>Course</code> instances, not null
		 */

		protected Builder (
				final DomainModel model,
				final Definition definition,
				final IdGenerator idGenerator,
				final Retriever<Enrolment> enrolmentRetriever,
				final Retriever<Course> courseRetriever,
				final Retriever<Role> roleRetriever)
		{
			super (model, definition, enrolmentRetriever);

			assert idGenerator != null : "idGenerator is NULL";
			assert courseRetriever != null : "courseRetriever is NULL";
			assert roleRetriever != null : "roleRetriever is NULL";

			this.idGenerator = idGenerator;
			this.courseRetriever = courseRetriever;
			this.roleRetriever = roleRetriever;

			this.id = null;
			this.course = null;
			this.role = null;
			this.finalGrade = null;
			this.usable = null;
		}

		/**
		 * Create an instance of the <code>Enrolment</code>.
		 *
		 * @return The new <code>Enrolment</code> instance
		 *
		 * @throws NullPointerException if any required field is missing
		 */

		@Override
		protected Enrolment create ()
		{
			this.log.trace ("create:");

			return ((Definition) this.definition).creator.apply (this);
		}

		/**
		 * Implementation of the pre-insert hook to set the ID number.
		 *
		 * @param  enrolment The <code>Enrolment</code> to be inserted, not null
		 * @return           The <code>Enrolment</code> to be inserted
		 */

		@Override
		protected Enrolment preInsert (final Enrolment enrolment)
		{
			assert enrolment != null : "enrolment is NULL";

			this.log.debug ("Setting ID");
			enrolment.setId (this.idGenerator.nextId ());

			return enrolment;
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
			this.course = null;
			this.role = null;
			this.finalGrade = null;
			this.usable = null;

			return this;
		}

		/**
		 * Load a <code>Enrolment</code> instance into the builder.  This method
		 * resets the builder and initializes all of its parameters from
		 * the specified <code>Enrolment</code> instance.  The  parameters are
		 * validated as they are set.
		 *
		 * @param  enrolment The <code>Enrolment</code>, not null
		 * @return           This <code>Builder</code>
		 *
		 * @throws IllegalArgumentException If any of the fields in the
		 *                                  <code>Enrolment</code> instance to
		 *                                  be loaded are not valid
		 */

		@Override
		public Builder load (final Enrolment enrolment)
		{
			this.log.trace ("load: enrolment={}", enrolment);

			super.load (enrolment);

			this.id = enrolment.getId ();
			this.setCourse (enrolment.getCourse ());
			this.setFinalGrade (enrolment.getFinalGrade ());
			this.setRole (enrolment.getRole ());
			this.setUsable (enrolment.isUsable ());

			return this;
		}

		/**
		 * Get the <code>DataStore</code> identifier for the
		 * <code>Enrolment</code> instance.
		 *
		 * @return The <code>DataStore</code> identifier
		 */

		@CheckReturnValue
		public final Long getId ()
		{
			return this.id;
		}

		/**
		 * Get the <code>Course</code> in which the <code>User</code>
		 * represented by the <code>Enrolment</code> instance is enrolled.
		 *
		 * @return The <code>Course</code> instance
		 */

		@CheckReturnValue
		public final Course getCourse ()
		{
			return this.course;
		}

		/**
		 * Set the <code>Course</code> in which the <code>User</code> is
		 * enrolled.
		 *
		 * @param  course The <code>Course</code>, not null
		 * @return        This <code>Builder</code>
		 *
		 * @throws IllegalArgumentException if the <code>Course</code> is not in
		 *                                  the <code>DataStore</code>
		 */

		public final Builder setCourse (final Course course)
		{
			this.log.trace ("setCourse: course={}", course);

			this.course = this.verifyRelationship (this.courseRetriever, course, "course");

			return this;
		}

		/**
		 * Get the <code>Role</code> of the <code>User</code> represented by
		 * this <code>Enrolment</code>, in the associated <code>Course</code>.
		 *
		 * @return The <code>Role</code> instance
		 */

		@CheckReturnValue
		public final Role getRole ()
		{
			return this.role;
		}

		/**
		 * Set the <code>Role</code> of the <code>User</code> in the
		 * <code>Course</code>.
		 *
		 * @param  role The <code>Role</code>, not null
		 * @return      This <code>Builder</code>
		 *
		 * @throws IllegalArgumentException if the <code>Role</code> is not in
		 *                                  the <code>DataStore</code>
		 */

		public final Builder setRole (final Role role)
		{
			this.log.trace ("setRole: role={}", role);

			this.role = this.verifyRelationship (this.roleRetriever, role, "role");

			return this;
		}

		/**
		 * Get the final grade for the <code>User</code> represented by this
		 * <code>Enrolment</code>, in the associated <code>Course</code>.  The
		 * final grade will be null if no final grade was assigned for the
		 * <code>User</code> associated with this <code>Enrolment</code>.
		 *
		 * @return An <code>Integer</code> containing the final grade, or null
		 *         if there is no final grade
		 */

		@CheckReturnValue
		public final Integer getFinalGrade ()
		{
			return this.finalGrade;
		}

		/**
		 * Set the final grade for the <code>User</code> in the
		 * <code>Course</code>.
		 *
		 * @param  finalgrade The final grade for the <code>User</code> in the
		 *                    course, on the interval [0, 100]
		 * @return            This <code>Builder</code>
		 *
		 * @throws IllegalArgumentException If the value is less than zero or
		 *                                  greater than 100
		 */

		public final Builder setFinalGrade (final @Nullable Integer finalgrade)
		{
			this.log.trace ("setFinalGrade: finalgrade={}", finalgrade);

			Preconditions.checkArgument (finalgrade == null || (finalgrade >= 0 && finalgrade <= 100),
					"Grade must be between 0 and 100");

			this.finalGrade = finalgrade;

			return this;
		}

		/**
		 * Determine if the <code>User</code> has given their consent for the
		 * data associated with this <code>Enrolment</code> to be used for
		 * research.
		 *
		 * @return <code>True</code> if the <code>User</code> has consented,
		 *         <code>False</code> otherwise.
		 */

		@CheckReturnValue
		public final Boolean isUsable ()
		{
			return this.usable;
		}

		/**
		 * Set the usable flag for the data related to the <code>User</code> in
		 * the <code>Course</code>. This method is intended to be used by a
		 * <code>DataStore</code> when the <code>Enrolment</code> instance is
		 * loaded.
		 *
		 * @param  usable Indication if the data may be used for research, not
		 *                null
		 * @return        This <code>Builder</code>
		 */

		public final Builder setUsable (final Boolean usable)
		{
			this.log.trace ("setUsable: usable={}", usable);

			this.usable = Preconditions.checkNotNull (usable, "usable");

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
	@Component (dependencies = {IdGenerator.IdGeneratorComponent.class}, modules = {EnrolmentBuilderModule.class})
	protected interface EnrolmentComponent extends Element.ElementComponent<Enrolment>
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
	public static final class EnrolmentModule extends Element.ElementModule<Enrolment> {}

	/**
	 * Dagger module for creating <code>Builder</code> instances.  This module
	 * contains implementation-dependent information.
	 *
	 * @author  James E. Stark
	 * @version 1.0
	 */

	@Module (includes = {EnrolmentModule.class, Course.CourseModule.class, Role.RoleModule.class})
	public static final class EnrolmentBuilderModule
	{
		/** The <code>Definition</code> */
		private final Definition definition;

		/**
		 * Create the <code>RoleBuilderModule</code>
		 *
		 * @param  definition The <code>Definition</code>, not null
		 */

		public EnrolmentBuilderModule (final Definition definition)
		{
			assert definition != null : "definition is NULL";
			this.definition = definition;
		}

		/**
		 * Create the <code>Builder</code>.
		 *
		 * @param  model           The <code>DomainModel</code>, not null
		 * @param  generator       The <code>IdGenerator</code>, not null
		 * @param  retriever       The <code>Retriever</code>, not null
		 * @param  roleRetriever   <code>Retriever</code> for <code>Role</code>
		 *                         instances, not null
		 * @param  courseRetriever <code>Retriever</code> for <code>Course</code>
		 *                         instances, not null
		 */

		@Provides
		public Builder createBuilder (
				final DomainModel model,
				final IdGenerator generator,
				final @Named ("TableRetriever") Retriever<Enrolment> retriever,
				final @Named ("QueryRetriever") Retriever<Course> courseRetriever,
				final @Named ("QueryRetriever") Retriever<Role> roleRetriever)
		{
			return new Builder (model, this.definition, generator, retriever, courseRetriever, roleRetriever);
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

	protected static abstract class Definition extends Element.Definition<Enrolment>
	{
		/** Method reference to the implementation constructor  */
		private final Function<Enrolment.Builder, Enrolment> creator;

		/**
		 * Create the <code>Definition</code>.
		 *
		 * @param  impl    The implementation class, not null
		 * @param  creator Method reference to the constructor, not null
		 */

		public Definition (
				final Class<? extends Enrolment> impl,
				final Function<Enrolment.Builder, Enrolment> creator)
		{
			super (Enrolment.METADATA, impl);

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
		protected Enrolment.EnrolmentComponent getComponent (final DomainModel model)
		{
			return DaggerEnrolment_EnrolmentComponent.builder ()
				.idGeneratorComponent (model.getIdGeneratorComponent (this.impl))
				.domainModelModule (new DomainModel.DomainModelModule (Enrolment.class, model))
				.enrolmentBuilderModule (new EnrolmentBuilderModule (this))
				.build ();
		}
	}

	/** Serial version id, required by the Serializable interface */
	private static final long serialVersionUID = 1L;

	/** The <code>MetaData</code> for the <code>Enrolment</code> */
	protected static final MetaData<Enrolment> METADATA;

	/** The <code>DataStore</code> identifier of the <code>Enrolment</code> */
	public static final Property<Enrolment, Long> ID;

	/** The <code>DomainModel</code> which contains the <code>Enrolment</code> */
	public static final Property<Enrolment, DomainModel> MODEL;

	/** The associated <code>Course</code> */
	public static final Property<Enrolment, Course> COURSE;

	/** The final grade */
	public static final Property<Enrolment, Integer> FINALGRADE;

	/** The associated <code>Role</code> */
	public static final Property<Enrolment, Role> ROLE;

	/** Has consent been given to use this data for research */
	public static final Property<Enrolment, Boolean> USABLE;

	/** The <code>Grade</code> instances assigned to the <code>Enrolment</code> */
	public static final Property<Enrolment, Grade> GRADES;

	/** The <code>LogEntry</code> instances associated with the <code>Enrolment</code> */
	public static final Property<Enrolment, LogEntry> LOGENTRIES;

	/** Select the <code>Enrolment</code> instance by its id */
	public static final Selector<Enrolment> SELECTOR_ID;

	/** Select all of the <code>Enrolment</code> instances */
	public static final Selector<Enrolment> SELECTOR_ALL;

	/** Select all <code>Enrolment</code> by <code>Role</code>*/
	public static final Selector<Enrolment> SELECTOR_ROLE;

	/**
	 * Initialize the <code>MetaData</code>, <code>Property</code> and
	 * <code>Selector</code> instances for the <code>Enrolment</code>.
	 */

	static
	{
		ID = Property.of (Enrolment.class, Long.class, "id",
				Enrolment::getId, Enrolment::setId);

		MODEL = Property.of (Enrolment.class, DomainModel.class, "domainmodel",
				Enrolment::getDomainModel, Enrolment::setDomainModel);

		COURSE = Property.of (Enrolment.class, Course.class, "course",
				Enrolment::getCourse, Enrolment::setCourse,
				Property.Flags.REQUIRED);

		FINALGRADE = Property.of (Enrolment.class, Integer.class, "finalgrade",
				Enrolment::getFinalGrade, Enrolment::setFinalGrade);

		ROLE = Property.of (Enrolment.class, Role.class, "role",
				Enrolment::getRole, Enrolment::setRole,
				Property.Flags.REQUIRED);

		USABLE = Property.of (Enrolment.class, Boolean.class, "usable",
				Enrolment::isUsable, Enrolment::setUsable,
				Property.Flags.REQUIRED);

		GRADES = Property.of (Enrolment.class, Grade.class, "grades",
				Enrolment::getGrades, Enrolment::addGrade, Enrolment::removeGrade);

		LOGENTRIES = Property.of (Enrolment.class, LogEntry.class, "logentries",
				Enrolment::getLog, Enrolment::addLog, Enrolment::removeLog);

		SELECTOR_ID = Selector.of (Selector.Cardinality.KEY, ID);
		SELECTOR_ROLE = Selector.of (Selector.Cardinality.MULTIPLE, ROLE);

		SELECTOR_ALL =  Selector.builder (Enrolment.class)
			.setCardinality (Selector.Cardinality.MULTIPLE)
			.setName ("all")
			.build ();

		METADATA = MetaData.builder (Enrolment.class)
			.addProperty (ID)
			.addProperty (MODEL)
			.addProperty (FINALGRADE)
			.addProperty (USABLE)
			.addProperty (GRADES)
			.addProperty (LOGENTRIES)
			.addRelationship (COURSE, Course.METADATA, Course.ENROLMENTS)
			.addRelationship (ROLE, Role.METADATA, SELECTOR_ROLE)
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
	 *                               the <code>Enrolment</code>
	 * @throws IllegalStateException if the <code>DomainModel</code> is
	 *                               immutable
	 */

	public static Builder builder (final DomainModel model)
	{
		Preconditions.checkNotNull (model, "model");

		return (Enrolment.Builder) model.getElementComponent (Enrolment.class)
			.getBuilder ();
	}

	/**
	 * Create the <code>Enrolment</code>.
	 */

	protected Enrolment ()
	{
		super ();
	}

	/**
	 * Create the <code>Enrolment</code> instance from the specified
	 * <code>Builder</code>.
	 *
	 * @param  builder The <code>Builder</code>, not null
	 */

	protected Enrolment (final Builder builder)
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
			.add ("usable", this.isUsable ())
			.add ("finalgrade", this.getFinalGrade ())
			.add ("course", this.getCourse ())
			.add ("role", this.getRole ());
	}

	/**
	 * Connect all of the relationships for this <code>Enrolment</code>
	 * instance.  This method is intended to be used just after the
	 * <code>Enrolment</code> is inserted into the <code>DataStore</code>.
	 *
	 * @return <code>true</code> if all of the relationships were successfully
	 *         connected, <code>false</code> otherwise
	 */

	@Override
	protected boolean connect ()
	{
		return Enrolment.METADATA.relationships ()
				.allMatch (r -> r.connect (this));
	}

	/**
	 * Disconnect all of the relationships for this <code>Enrolment</code>
	 * instance.  This method is intended to be used just before the
	 * <code>Enrolment</code> is removed from the <code>DataStore</code>.
	 *
	 * @return <code>true</code> if all of the relationships were successfully
	 *         disconnected, <code>false</code> otherwise
	 */

	@Override
	protected boolean disconnect ()
	{
		return Enrolment.METADATA.relationships ()
				.allMatch (r -> r.disconnect (this));
	}

	/**
	 * Compare two <code>Enrolment</code> instances.  <code>Enrolment</code>
	 * instances are compared by their <code>DataStore</code> ID.
	 *
	 * @param  element The <code>Element</code> to be compared
	 * @return         The value 0 if the <code>Enrolment</code> instances are
	 *                 equal, less than 0 of the argument is is greater, and
	 *                 greater than  0 if the argument is less than the
	 *                 <code>Enrolment</code>
	 */

	@Override
	public int compareTo (final Element element)
	{
		Preconditions.checkNotNull (element, "element");

		int result = 0;

		if (this != element)
		{
			if (element instanceof Enrolment)
			{
				result = this.getId ().compareTo (element.getId ());
			}
			else
			{
				result = this.getClass ().getName ().compareTo (element.getClass ().getName ());
			}
		}

		return result;
	}

	/**
	 * Compare two <code>Enrolment</code> instances to determine if they are
	 * equal.  The <code>Enrolment</code> instances are compared based upon the
	 * <code>Course</code>, <code>Role</code> and <code>DataStore</code> id.
	 *
	 * @param  obj The <code>Enrolment</code> instance to compare to the one
	 *             represented by the called instance
	 * @return     <code>True</code> if the two <code>Enrolment</code>
	 *             instances are equal, <code>False</code> otherwise
	 */

	@Override
	public boolean equals (final Object obj)
	{
		return (obj == this) ? true : (obj instanceof Enrolment)
			&& Objects.equals (this.getId (), ((Enrolment) obj).getId ())
			&& Objects.equals (this.getCourse (), ((Enrolment) obj).getCourse ())
			&& Objects.equals (this.getRole (), ((Enrolment) obj).getRole ());
	}

	/**
	 * Compare two <code>Enrolment</code> instances to determine if they are
	 * equal using all of the instance fields.  For <code>Enrolment</code> the
	 * <code>equals</code> methods excludes the usable and final grade fields
	 * from the comparison.  This methods compares two <code>Enrolment</code>
	 * instances using all of the fields.
	 *
	 * @param  element The <code>Element</code> instance to compare to this
	 *                 instance
	 * @return         <code>True</code> if the two <code>Enrolment</code>
	 *                 instances are equal, <code>False</code> otherwise
	 */

	@Override
	public boolean equalsAll (final @Nullable Element element)
	{
		return (element == this) ? true : (element instanceof Enrolment)
			&& Objects.equals (this.getCourse (), ((Enrolment) element).getCourse ())
			&& Objects.equals (this.getRole (), ((Enrolment) element).getRole ())
			&& Objects.equals (this.getFinalGrade (), ((Enrolment) element).getFinalGrade ())
			&& Objects.equals (this.isUsable (), ((Enrolment) element).isUsable ());
	}

	/**
	 * Compute a <code>hashCode</code> of the <code>Enrolment</code> instance.
	 * The hash code is computed based upon the associated <code>Course</code>
	 * and the associated <code>Role</code>.
	 *
	 * @return An <code>Integer</code> containing the hash code
	 */

	@Override
	public int hashCode ()
	{
		return Objects.hash (this.getId (), this.getCourse (), this.getRole ());
	}

	/**
	 * Get a <code>String</code> representation of the <code>Enrolment</code>
	 * instance, including the identifying fields.
	 *
	 * @return A <code>String</code> representation of the
	 *         <code>Enrolment</code> instance
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
	 * <code>DomainModel</code>.  This method creates an
	 * <code>Builder</code> on the specified <code>DomainModel</code>
	 * and initializes it with the contents of this <code>Enrolment</code>
	 * instance.
	 *
	 * @param  model The <code>DomainModel</code>, not null
	 * @return       The initialized <code>Builder</code>
	 */

	@Override
	public Builder getBuilder (final DomainModel model)
	{
		return Enrolment.builder (Preconditions.checkNotNull (model, "model"))
			.load (this);
	}

	/**
	 * Get the <code>Course</code> in which the <code>User</code> represented
	 * by the <code>Enrolment</code> instance is enrolled.
	 *
	 * @return The <code>Course</code> instance
	 */

	public abstract Course getCourse();

	/**
	 * Set the <code>Course</code> in which the <code>User</code> is enrolled.
	 * This method is intended to be used to initialize a new
	 * <code>Enrolment</code> instance.
	 *
	 * @param  course The <code>Course</code>, not null
	 */

	protected abstract void setCourse (final Course course);

	/**
	 * Get the <code>Role</code> of the <code>User</code> represented by this
	 * <code>Enrolment</code>, in the associated <code>Course</code>.
	 *
	 * @return The <code>Role</code> instance
	 */

	public abstract Role getRole();

	/**
	 * Set the <code>Role</code> of the <code>User</code> in the
	 * <code>Course</code>. This method is intended to be used to initialize a
	 * new <code>Enrolment</code> instance.
	 *
	 * @param  role The <code>Role</code>, not null
	 */

	protected abstract void setRole (Role role);

	/**
	 * Determine if the <code>User</code> has given their consent for the data
	 * associated with this <code>Enrolment</code> to be used for research.
	 *
	 * @return <code>True</code> if the <code>User</code> has consented,
	 *         <code>False</code> otherwise.
	 */

	public abstract Boolean isUsable ();

	/**
	 * Set the usable flag for the data related to the <code>User</code> in the
	 * <code>Course</code>. This method is intended to be used to initialize a
	 * new <code>Enrolment</code> instance.
	 *
	 * @param  usable Indication if the data may be used for research, not null
	 */

	protected abstract void setUsable (Boolean usable);

	/**
	 * Get the final grade for the <code>User</code> represented by this
	 * <code>Enrolment</code>, in the associated <code>Course</code>.  The
	 * final grade will be null if no final grade was assigned for the
	 * <code>User</code> associated with this <code>Enrolment</code>.
	 *
	 * @return An <code>Integer</code> containing the final grade, or null if
	 *         there is no final grade
	 */

	@CheckReturnValue
	public abstract Integer getFinalGrade ();

	/**
	 * Set the final grade for the <code>User</code> in the
	 * <code>Course</code>.  This method is intended to be used to initialize a
	 * new <code>Enrolment</code> instance.
	 *
	 * @param  finalgrade The final grade for the <code>User</code> in the
	 *                    course, on the interval [0, 100]
	 */

	protected abstract void setFinalGrade (@Nullable Integer finalgrade);

	/**
	 * Get the <code>Grade</code> for the specified <code>Activity</code>.
	 *
	 * @param  activity The <code>Activity</code> for which the grade is to be
	 *                  retrieved
	 *
	 * @return          The <code>Grade</code> for the specified
	 *                  <code>Activity</code>
	 */

	public abstract Grade getGrade (Activity activity);

	/**
	 * Get the <code>Set</code> of <code>Grade</code> instances which are
	 * associated with the <code>Enrolment</code> instance.
	 *
	 * @return A <code>Set</code> of <code>Grade</code> instances.
	 */

	public abstract Set<Grade> getGrades ();

	/**
	 * Initialize the <code>Set</code> of <code>Grade</code> instances
	 * associated with the <code>Enrolment</code> instance.  This method is
	 * intended to be used to initialize a new <code>Enrolment</code> instance.
	 *
	 * @param  grades The <code>Set</code> of <code>Grade</code> instances, not
	 *                null
	 */

	protected abstract void setGrades (Set<Grade> grades);

	/**
	 * Add the specified <code>Grade</code> to the <code>Enrolment</code>.
	 *
	 * @param  grade  The <code>Grade</code> to add, not null
	 * @return        <code>True</code> if the <code>Grade</code> was
	 *                successfully added, <code>False</code> otherwise
	 */

	protected abstract boolean addGrade (Grade grade);

	/**
	 * Remove the specified <code>Grade</code> from the <code>Enrolment</code>.
	 *
	 * @param  grade The <code>Grade</code> to remove, not null
	 * @return       <code>True</code> if the <code>Grade</code> was
	 *               successfully removed, <code>False</code> otherwise
	 */

	protected abstract boolean removeGrade (Grade grade);

	/**
	 * Get the <code>List</code> of <code>LogEntry</code> instances associated
	 * with this <code>Enrolment</code>.  The <code>List</code> will be empty
	 * if there are no associated <code>LogEntry</code> instances.
	 *
	 * @return A <code>List</code> of <code>LogEntry</code> instances
	 */

	public abstract List<LogEntry> getLog ();

	/**
	 * Initialize the <code>List</code> of <code>LogEntry</code> instances
	 * associated with the <code>Enrolment</code> instance.  This method is
	 * intended to be used to initialize a new <code>Enrolment</code> instance.
	 *
	 * @param  log The <code>List</code> of <code>LogEntry</code> instances,
	 *             not null
	 */

	protected abstract void setLog (List<LogEntry> log);

	/**
	 * Add the specified <code>LogEntry</code> to the <code>Enrolment</code>.
	 *
	 * @param  entry The <code>LogEntry</code> to add, not null
	 * @return       <code>True</code> if the <code>LogEntry</code> was
	 *               successfully added, <code>False</code> otherwise
	 */

	protected abstract boolean addLog (LogEntry entry);

	/**
	 * Remove the specified <code>LogEntry</code> from the
	 * <code>Enrolment</code>.
	 *
	 * @param  entry The <code>LogEntry</code> to remove, not null
	 * @return       <code>True</code> if the <code>LogEntry</code> was
	 *               successfully removed, <code>False</code> otherwise
	 */

	protected abstract boolean removeLog (LogEntry entry);
}
