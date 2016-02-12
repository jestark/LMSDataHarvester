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
 * A representation of the grade received by a <code>User</code> for a
 * particular <code>Activity</code>.  Instances of the <code>Grade</code>
 * interface are identified by the associated instances of the
 * <code>Activity</code> and <code>Enrolment</code> interfaces.  As such, a
 * loader does not exist for the <code>Grade</code> interface, as the
 * <code>Grade</code> instance may be retrieved from the associated
 * <code>Enrolment</code> or <code>Activity</code>.
 * <p>
 * Within the domain model, <code>Grade</code> is a leaf level interface.  No
 * instances of any other domain model interface depend upon the existence of
 * an instance of the <code>Grade</code> interface.  Instances of the
 * <code>Grade</code> interface have strong dependencies upon instances of the
 * <code>Activity</code> and <code>Enrolment</code> interfaces.  If an instance
 * of the <code>Enrolment</code> or <code>Activity</code> interfaces is
 * deleted, then all of the associated instances of the <code>Grade</code>
 * interface must be deleted as well.
 * <p>
 * Once created, <code>Grade</code> instances are immutable.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public abstract class Grade extends Element
{
	/**
	 * Create new <code>Grade</code> instances.  This class extends
	 * <code>AddingBuilder</code>, adding the functionality required to
	 * create <code>Grade</code> instances.  The "grade" field of existing grade
	 * instances may be modified in place.
	 *
	 * @author  James E. Stark
	 * @version 1.0
	 */

	public static class Builder extends Element.Builder<Grade>
	{
		/** The <code>IdGenerator</code>*/
		private final IdGenerator idGenerator;

		/** Helper to substitute <code>ActivityReference</code> instances */
		private final Retriever<ActivityReference> activityRetriever;

		/** Helper to substitute <code>Enrolment</code> instances */
		private final Retriever<Enrolment> enrolmentRetriever;

		/** The <code>DataStore</code> id number for the <code>Grade</code> */
		private @Nullable Long id;

		/** The associated <code>ActivityReference</code> */
		private @Nullable ActivityReference activity;

		/** The associated <code>Enrolment</code> */
		private @Nullable Enrolment enrolment;

		/** The value of the <code>Grade</code> */
		private @Nullable Integer value;

		/**
		 * Create the <code>Builder</code>.
		 *
		 * @param  model              The <code>DomainModel</code>, not null
		 * @param  definition         The <code>Definition</code>, not null
		 * @param  idGenerator        The <code>IdGenerator</code>, not null
		 * @param  gradeRetriever     <code>Retriever</code> for
		 *                            <code>Grade</code> instances, not null
		 * @param  activityRetriever  <code>Retriever</code> for
		 *                            <code>ActivityReference</code> instances,
		 *                            not null
		 * @param  enrolmentRetriever <code>Retriever</code> for
		 *                            <code>Enrolment</code> instances, not null
		 */

		protected Builder (
				final DomainModel model,
				final Definition definition,
				final IdGenerator idGenerator,
				final Retriever<Grade> gradeRetriever,
				final Retriever<ActivityReference> activityRetriever,
				final Retriever<Enrolment> enrolmentRetriever)
		{
			super (model, definition, gradeRetriever);

			assert idGenerator != null : "idGenerator is NULL";
			assert activityRetriever != null : "activityRetriever is NULL";
			assert enrolmentRetriever != null : "enrolmentRetriever is NULL";

			this.idGenerator = idGenerator;

			this.activityRetriever = activityRetriever;
			this.enrolmentRetriever = enrolmentRetriever;

			this.id = null;
			this.activity = null;
			this.enrolment = null;
			this.value = null;
		}

		/**
		 * Create an instance of the <code>Grade</code>.
		 *
		 * @return The new <code>Grade</code> instance
		 *
		 * @throws NullPointerException if any required field is missing
		 */

		@Override
		protected Grade create ()
		{
			this.log.trace ("create:");

			return ((Definition) this.definition).creator.apply (this);
		}

		/**
		 * Implementation of the pre-insert hook to set the ID number.
		 *
		 * @param  grade The <code>Grade</code> to be inserted, not null
		 * @return       The <code>Grade</code> to be inserted
		 */

		@Override
		protected Grade preInsert (final Grade grade)
		{
			assert grade != null : "grade is NULL";

			this.log.debug ("Setting ID");
			grade.setId (this.idGenerator.nextId ());

			return grade;
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
			this.activity = null;
			this.enrolment = null;
			this.value = null;

			return this;
		}

		/**
		 * Load a <code>Grade</code> instance into the builder.  This method
		 * resets the builder and initializes all of its parameters from
		 * the specified <code>Grade</code> instance.  The  parameters are
		 * validated as they are set.
		 *
		 * @param  grade The <code>Grade</code>, not null
		 * @return       This <code>Builder</code>
		 *
		 * @return                          This <code>Builder</code>
		 * @throws IllegalArgumentException If any of the fields in the
		 *                                  <code>Grade</code> instance to be
		 *                                  loaded are not valid
		 */

		@Override
		public Builder load (final Grade grade)
		{
			this.log.trace ("load: grade={}", grade);

			super.load (grade);

			this.id = grade.getId ();
			this.setActivityReference (grade.getActivityReference ());
			this.setEnrolment (grade.getEnrolment ());
			this.setGrade (grade.getGrade ());

			return this;
		}

		/**
		 * Get the <code>DataStore</code> identifier for the <code>Grade</code>
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
		 * Get the <code>ActivityReference</code> for which the
		 * <code>Grade</code> is assigned.  This method exists for the benefit
		 * of the <code>Grade</code> implementation.
		 *
		 * @return the <code>ActivityReference</code>
		 */

		@CheckReturnValue
		protected ActivityReference getActivityReference ()
		{
			return this.activity;
		}

		/**
		 * Set the <code>ActivityReference</code> which is associated with the
		 * <code>Grade</code>.
		 *
		 * @param  reference The <code>ActivityReference</code>, not null
		 * @return           This <code>Builder</code>
		 *
		 * @throws IllegalArgumentException if the
		 *                                  <code>ActivityReference</code> is
		 *                                  not in the <code>DataStore</code>
		 */

		private final Builder setActivityReference (final ActivityReference reference)
		{
			this.log.trace ("setActivityReference: reference={}", reference);

			assert reference != null : "reference is NULL";

			this.activity = this.verifyRelationship (this.activityRetriever, reference, "activity");

			return this;
		}

		/**
		 * Get the <code>Activity</code> for which the <code>Grade</code> is
		 * assigned.
		 *
		 * @return The associated <code>Activity</code>
		 */

		@CheckReturnValue
		public final Activity getActivity ()
		{
			return (this.activity != null) ? this.activity.getActivity () : null;
		}

		/**
		 * Set the <code>Activity</code> which is associated with the
		 * <code>Grade</code>.
		 *
		 * @param  activity The <code>Activity</code>, not null
		 * @return          This <code>Builder</code>
		 *
		 * @throws IllegalArgumentException if the <code>Activity</code> is not
		 *                                  in the <code>DataStore</code>
		 */

		public final Builder setActivity (final Activity activity)
		{
			this.log.trace ("setActivity: activity={}", activity);

			Preconditions.checkNotNull (activity, "activity");

			return this.setActivityReference (activity.getReference ());
		}

		/**
		 * Get the <code>Enrolment</code>, for the student, to which the
		 * <code>Grade</code> is assigned
		 *
		 * @return The associated <code>Enrolment</code>
		 */

		@CheckReturnValue
		public final Enrolment getEnrolment ()
		{
			return this.enrolment;
		}

		/**
		 * Set the <code>Enrolment</code> which is associated with the
		 * <code>Grade</code>.
		 *
		 * @param  enrolment The <code>Enrolment</code>, not null
		 * @return           This <code>Builder</code>
		 *
		 * @throws IllegalArgumentException if the <code>Activity</code> is not
		 *                                  in the <code>DataStore</code>
		 */

		public final Builder setEnrolment (final Enrolment enrolment)
		{
			this.log.trace ("setEnrolment: enrolment={}", enrolment);

			this.enrolment = this.verifyRelationship (this.enrolmentRetriever, enrolment, "enrolment");

			return this;
		}

		/**
		 * Get the grade that the student received for the
		 * <code>Activity</code>.  The grade will be an <code>Integer</code>
		 * with a value on the range of [0, 100].
		 *
		 * @return An <code>Integer</code> containing the assigned grade, may be
		 *         null
		 */

		@CheckReturnValue
		public final Integer getGrade ()
		{
			return this.value;
		}

		/**
		 * Set the value of the <code>Grade</code>.
		 *
		 * @param  grade The value of the <code>Grade</code>, not null
		 * @return       This <code>Builder</code>
		 *
		 * @throws IllegalArgumentException If the value is less than zero or
		 *                                  greater than 100
		 */

		public final Builder setGrade (final Integer grade)
		{
			this.log.trace ("setGrade: grade={}", grade);

			Preconditions.checkNotNull (grade, "grade");
			Preconditions.checkArgument (grade >= 0 && grade <= 100, "grade must be between 0 and 100");

			this.value = grade;

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
	@Component (dependencies = {IdGenerator.IdGeneratorComponent.class}, modules = {GradeBuilderModule.class})
	protected interface GradeComponent extends Element.ElementComponent<Grade>
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
	public static final class GradeModule extends Element.ElementModule<Grade> {}

	/**
	 * Dagger module for creating <code>Builder</code> instances.  This module
	 * contains implementation-dependent information.
	 *
	 * @author  James E. Stark
	 * @version 1.0
	 */

	@Module (includes = {GradeModule.class, ActivityReference.ActivityReferenceModule.class,
		Enrolment.EnrolmentModule.class})
	public static final class GradeBuilderModule
	{
		/** The <code>Definition</code> */
		private final Definition definition;

		/**
		 * Create the <code>GradeBuilderModule</code>
		 *
		 * @param  definition The <code>Definition</code>, not null
		 */

		public GradeBuilderModule (final Definition definition)
		{
			assert definition != null : "definition is NULL";
			this.definition = definition;
		}

		/**
		 * Create the <code>Builder</code>.
		 *
		 * @param  model              The <code>DomainModel</code>, not null
		 * @param  generator          The <code>IdGenerator</code>, not null
		 * @param  retriever          The <code>Retriever</code>, not null
		 * @param  activityRetriever  <code>Retriever</code> for
		 *                            <code>ActivityReference</code> instances,
		 *                            not null
		 * @param  enrolmentRetriever <code>Retriever</code> for
		 *                            <code>Enrolment</code> instances, not null
		 */

		@Provides
		public Builder createBuilder (
				final DomainModel model,
				final IdGenerator generator,
				final @Named ("TableRetriever") Retriever<Grade> retriever,
				final @Named ("TableRetriever") Retriever<ActivityReference> activityRetriever,
				final @Named ("TableRetriever") Retriever<Enrolment> enrolmentRetriever)
		{
			return new Builder (model, this.definition, generator, retriever, activityRetriever, enrolmentRetriever);
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

	protected static abstract class Definition extends Element.Definition<Grade>
	{
		/** Method reference to the implementation constructor  */
		private final Function<Grade.Builder, Grade> creator;

		/**
		 * Create the <code>Definition</code>.
		 *
		 * @param  impl    The implementation class, not null
		 * @param  creator Method reference to the constructor, not null
		 */

		public Definition (
				final Class<? extends Grade> impl,
				final Function<Grade.Builder, Grade> creator)
		{
			super (Grade.METADATA, impl);

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
		protected Grade.GradeComponent getComponent (final DomainModel model)
		{
			return DaggerGrade_GradeComponent.builder ()
				.idGeneratorComponent (model.getIdGeneratorComponent (this.impl))
				.domainModelModule (new DomainModel.DomainModelModule (Grade.class, model))
				.gradeBuilderModule (new GradeBuilderModule (this))
				.build ();
		}
	}

	/** Serial version id, required by the Serializable interface */
	private static final long serialVersionUID = 1L;

	/** The <code>MetaData</code> for the <code>Grade</code> */
	protected static final MetaData<Grade> METADATA;

	/** The <code>DataStore</code> identifier of the <code>Grade</code> */
	public static final Property<Grade, Long> ID;

	/** The <code>DomainModel</code> which contains the <code>Grade</code> */
	public static final Property<Grade, DomainModel> MODEL;

	/** The associated <code>Activity</code> */
	public static final Property<Grade, ActivityReference> ACTIVITY;

	/** The associated <code>Enrolment</code> */
	public static final Property<Grade, Enrolment> ENROLMENT;

	/** The assigned grade */
	public static final Property<Grade, Integer> GRADE;

	/** Select the <code>Grade</code> instance by its id */
	public static final Selector<Grade> SELECTOR_ID;

	/** Select all of the <code>Grade</code> instances */
	public static final Selector<Grade> SELECTOR_ALL;

	/**
	 * Initialize the <code>MetaData</code>, <code>Property</code> and
	 * <code>Selector</code> instances for the <code>Grade</code>.
	 */

	static
	{
		MODEL = Property.of (Grade.class, DomainModel.class, "domainmodel",
				Grade::getDomainModel, Grade::setDomainModel);

		ID = Property.of (Grade.class, Long.class, "id",
				Grade::getId, Grade::setId);

		ACTIVITY = Property.of (Grade.class, ActivityReference.class, "activity",
				Grade::getActivityReference, Grade::setActivityReference,
				Property.Flags.REQUIRED);

		ENROLMENT = Property.of (Grade.class, Enrolment.class, "enrolment",
				Grade::getEnrolment, Grade::setEnrolment,
				Property.Flags.REQUIRED);

		GRADE = Property.of (Grade.class, Integer.class, "grade",
				Grade::getGrade, Grade::setGrade,
				Property.Flags.REQUIRED);

		SELECTOR_ID = Selector.of (Selector.Cardinality.KEY, ID);

		SELECTOR_ALL =  Selector.builder (Grade.class)
			.setCardinality (Selector.Cardinality.MULTIPLE)
			.setName ("all")
			.build ();

		METADATA = MetaData.builder (Grade.class)
			.addProperty (MODEL)
			.addProperty (ID)
			.addProperty (GRADE)
			.addRelationship (ACTIVITY, ActivityReference.METADATA, ActivityReference.GRADES)
			.addRelationship (ENROLMENT, Enrolment.METADATA, Enrolment.GRADES)
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
	 *                               the <code>Grade</code>
	 * @throws IllegalStateException if the <code>DomainModel</code> is
	 *                               immutable
	 */

	public static Builder builder (final DomainModel model)
	{
		Preconditions.checkNotNull (model, "model");

		return (Grade.Builder) model.getElementComponent (Grade.class)
			.getBuilder ();
	}

	/**
	 * Create the <code>Grade</code>.
	 */

	protected Grade ()
	{
		super ();
	}

	/**
	 * Create the <code>Grade</code> instance from the specified
	 * <code>Builder</code>.
	 *
	 * @param  builder The <code>Builder</code>, not null
	 */

	protected Grade (final Builder builder)
	{
		super (builder);

		this.setActivityReference (Preconditions.checkNotNull (builder.getActivityReference (), "activity"));
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
			.add ("enrolment", this.getEnrolment ())
			.add ("activity", this.getActivity ())
			.add ("grade", this.getGrade ());
	}

	/**
	 * Connect all of the relationships for this <code>Grade</code> instance.
	 * This method is intended to be used just after the <code>Grade</code> is
	 * inserted into the <code>DataStore</code>.
	 *
	 * @return <code>true</code> if all of the relationships were successfully
	 *         connected, <code>false</code> otherwise
	 */

	@Override
	protected boolean connect ()
	{
		return Grade.METADATA.relationships ()
				.allMatch (r -> r.connect (this));
	}

	/**
	 * Disconnect all of the relationships for this <code>Grade</code>
	 * instance.  This method is intended to be used just before the
	 * <code>Grade</code> is removed from the <code>DataStore</code>.
	 *
	 * @return <code>true</code> if all of the relationships were successfully
	 *         disconnected, <code>false</code> otherwise
	 */

	@Override
	protected boolean disconnect ()
	{
		return Grade.METADATA.relationships ()
				.allMatch (r -> r.disconnect (this));
	}

	/**
	 * Compare two <code>Grade</code> instances.  The comparison is based upon
	 * the associated <code>Enrolment</code> and <code>Activity</code>
	 * instances.
	 *
	 * @param  element The <code>Grade</code> to be compared
	 * @return         The value 0 if the <code>Grade</code> instances
	 *                 are equal, less than 0 of the argument is is greater, and
	 *                 greater than 0 if the argument is less than the
	 *                 <code>Grade</code>
	 */

	@Override
	public int compareTo (final Element element)
	{
		Preconditions.checkNotNull (element, "element");

		int result = 0;

		if (this != element)
		{
			if (element instanceof Grade)
			{
				result = this.getEnrolment ().compareTo (((Grade) element).getEnrolment ());

				if (result == 0)
				{
					result = this.getActivity ().compareTo (((Grade) element).getActivity ());
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
	 * Compare two <code>Grade</code> instances to determine if they are equal.
	 * The <code>Grade</code> instances are compared based upon the associated
	 * <code>Activity</code> and the associated <code>Enrolment</code>.
	 *
	 * @param  obj The <code>Grade</code> instance to compare to the one
	 *             represented by the called instance
	 * @return     <code>True</code> if the two <code>Grade</code> instances
	 *             are equal, <code>False</code> otherwise
	 */

	@Override
	public boolean equals (final Object obj)
	{
		return (obj == this) ? true : (obj instanceof Grade)
			&& Objects.equals (this.getActivity (), ((Grade) obj).getActivity ())
			&& Objects.equals (this.getEnrolment (), ((Grade) obj).getEnrolment ());
	}

	/**
	 * Compare two <code>Grade</code> instances to determine if they are equal
	 * using all of the instance fields.  For <code>Grade</code> the
	 * <code>equals</code> methods excludes the grade field from the
	 * comparison.  This methods compares two <code>Grade</code> instances
	 * using all of the fields.
	 *
	 * @param  element The <code>Element</code> instance to compare to this
	 *                 instance
	 * @return         <code>True</code> if the two <code>Grade</code>
	 *                 instances are equal, <code>False</code> otherwise
	 */

	@Override
	public boolean equalsAll (final @Nullable Element element)
	{
		return (element == this) ? true : (element instanceof Grade)
			&& this.getActivity ().equalsAll (((Grade) element).getActivity ())
			&& this.getEnrolment ().equalsAll (((Grade) element).getEnrolment ())
			&& Objects.equals (this.getGrade (), ((Grade) element).getGrade ());
	}

	/**
	 * Compute a <code>hashCode</code> of the <code>Grade</code> instance.
	 * The hash code is computed based upon associated <code>Activity</code>
	 * and the associated <code>Enrolment</code>.
	 *
	 * @return An <code>Integer</code> containing the hash code
	 */

	@Override
	public int hashCode ()
	{
		return Objects.hash (this.getActivity (), this.getEnrolment ());
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
		return Grade.METADATA.properties ()
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
		return Grade.METADATA.properties ()
			.filter (p -> p.hasFlags (Property.Flags.RELATIONSHIP))
			.filter (p -> p.hasFlags (Property.Flags.REQUIRED) || p.hasFlags (Property.Flags.MUTABLE))
			.flatMap (p -> p.stream (this))
			.map (e -> (Element) e);
	}

	/**
	 * Get a <code>String</code> representation of the <code>Grade</code>
	 * instance, including the identifying fields.
	 *
	 * @return A <code>String</code> representation of the <code>Grade</code>
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
	 * Get an <code>Builder</code> instance for the specified
	 * <code>DomainModel</code>.  This method creates a <code>Builder</code> on
	 * the specified <code>DomainModel</code> and initializes it with the
	 * contents of this <code>Grade</code> instance.
	 *
	 * @param  model The <code>DomainModel</code>, not null
	 * @return       The initialized <code>Builder</code>
	 */

	@Override
	public Builder getBuilder (final DomainModel model)
	{
		return Grade.builder (Preconditions.checkNotNull (model, "model"))
			.load (this);
	}

	/**
	 * Get the <code>Activity</code> for which the <code>Grade</code> is
	 * assigned.
	 *
	 * @return The associated <code>Activity</code>
	 */

	public Activity getActivity ()
	{
		return this.getActivityReference ().getActivity ();
	}

	/**
	 * Get the <code>ActivityReference</code> for which the <code>Grade</code>
	 * is assigned.
	 *
	 * @return The associated <code>Activity</code>
	 */

	protected abstract ActivityReference getActivityReference ();

	/**
	 * Set the <code>Activity</code> which is associated with the
	 * <code>Grade</code>.  This method is intended to be used to initialize a
	 * new <code>Grade</code> instance.
	 *
	 * @param  activity The <code>Activity</code>, not null
	 */

	protected abstract void setActivityReference (ActivityReference activity);

	/**
	 * Get the <code>Enrolment</code>, for the student, to which the
	 * <code>Grade</code> is assigned
	 *
	 * @return The associated <code>Enrolment</code>
	 */

	public abstract Enrolment getEnrolment ();

	/**
	 * Set the <code>Enrolment</code> which is associated with the
	 * <code>Grade</code>.  This method is intended to be used to initialize a
	 * new <code>Grade</code> instance.
	 *
	 * @param  enrolment The <code>Enrolment</code>, not null
	 */

	protected abstract void setEnrolment (Enrolment enrolment);

	/**
	 * Get the grade that the student received for the <code>Activity</code>.
	 * The grade will be an <code>Integer</code> with a value on the range of
	 * [0, 100].
	 *
	 * @return An <code>Integer</code> containing the assigned grade.
	 */

	public abstract Integer getGrade ();

	/**
	 * Set the numeric grade assigned to the <code>Enrolment</code> for the
	 * <code>Activity</code>.  This method is intended to be used to initialize
	 * a new <code>Grade</code> instance.
	 *
	 * @param  grade The grade, on the interval [0, 100], not null
	 */

	protected abstract void setGrade (Integer grade);
}
