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
import ca.uoguelph.socs.icc.edm.domain.datastore.Retriever;
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

	public static final class Builder implements Element.Builder<Grade>
	{
		/** The Logger */
		private final Logger log;

		/** Helper to substitute <code>Activity</code> instances */
		private final Retriever<Activity> activityRetriever;

		/** Helper to substitute <code>Enrolment</code> instances */
		private final Retriever<Enrolment> enrolmentRetriever;

		/** Helper to operate on <code>Grade</code> instances */
		private final Persister<Grade> persister;

		/** Method reference to the constructor of the implementation class */
		private final Supplier<Grade> supplier;

		/** The loaded or previously built <code>Grade</code> instance */
		private Grade grade;

		/** The associated <code>Activity</code> */
		private Activity activity;

		/** The associated <code>Enrolment</code> */
		private Enrolment enrolment;

		/** The value of the <code>Grade</code> */
		private Integer value;

		/**
		 * Create the <code>Builder</code>.
		 *
		 * @param  supplier           Method reference to the constructor of the
		 *                            implementation class, not null
		 * @param  persister          The <code>Persister</code> used to store
		 *                            the <code>Enrolment</code>, not null
		 * @param  activityRetriever  <code>Retriever</code> for
		 *                            <code>Role</code> instances, not null
		 * @param  enrolmentRetriever <code>Retriever</code> for
		 *                            <code>Enrolment</code> instances, not null
		 */

		protected Builder (
				final Supplier<Grade> supplier,
				final Persister<Grade> persister,
				final Retriever<Activity> activityRetriever,
				final Retriever<Enrolment> enrolmentRetriever)
		{
			assert supplier != null : "supplier is NULL";
			assert persister != null : "persister is NULL";
			assert activityRetriever != null : "activityRetriever is NULL";
			assert enrolmentRetriever != null : "enrolmentRetriever is NULL";

			this.log = LoggerFactory.getLogger (this.getClass ());

			this.activityRetriever = activityRetriever;
			this.enrolmentRetriever = enrolmentRetriever;
			this.persister = persister;
			this.supplier = supplier;

			this.grade = null;
			this.activity = null;
			this.enrolment = null;
			this.value = null;
		}

		/**
		 * Create an instance of the <code>Grade</code>.
		 *
		 * @return                       The new <code>Grade</code> instance
		 * @throws IllegalStateException If any if the fields is missing
		 * @throws IllegalStateException If there isn't an active transaction
		 */

		@Override
		public Grade build ()
		{
			this.log.trace ("build:");

			if (this.activity == null)
			{
				this.log.error ("Attempting to create an Grade without an Activity");
				throw new IllegalStateException ("activity is NULL");
			}

			if (this.enrolment == null)
			{
				this.log.error ("Attempting to create an Grade without an Enrolment");
				throw new IllegalStateException ("enrolment is NULL");
			}

			if (this.value == null)
			{
				this.log.error ("Attempting to create an Grade without a Grade");
				throw new IllegalStateException ("grade is NULL");
			}

			if ((this.grade == null)
					|| (this.grade.getActivity () != this.activity)
					|| (this.grade.getEnrolment () != this.enrolment))
			{
				Grade result = this.supplier.get ();
				result.setActivityReference (this.activity.getReference ());
				result.setEnrolment (this.enrolment);
				result.setGrade (this.value);

				this.grade = this.persister.insert (this.grade, result);

				if (! this.grade.equalsAll (result))
				{
					this.log.error ("Grade is already in the datastore with a value of: {} vs. the specified value: {}", this.grade.getGrade (), this.value);
					throw new IllegalStateException ("Grade already exists but with a different value");
				}
			}
			else
			{
				this.grade.setGrade (this.value);
			}

			return this.grade;
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

			this.grade = null;
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
		 * @param  grade                    The <code>Grade</code>, not null
		 *
		 * @return                          This <code>Builder</code>
		 * @throws IllegalArgumentException If any of the fields in the
		 *                                  <code>Grade</code> instance to be
		 *                                  loaded are not valid
		 */

		public Builder load (final Grade grade)
		{
			this.log.trace ("load: grade={}", grade);

			if (grade == null)
			{
				this.log.error ("Attempting to load a NULL Grade");
				throw new NullPointerException ();
			}

			this.grade = grade;
			this.setActivity (grade.getActivity ());
			this.setEnrolment (grade.getEnrolment ());
			this.setGrade (grade.getGrade ());

			return this;
		}

		/**
		 * Get the <code>Activity</code> for which the <code>Grade</code> is
		 * assigned.
		 *
		 * @return The associated <code>Activity</code>
		 */

		public Activity getActivity ()
		{
			return this.activity;
		}

		/**
		 * Set the <code>Activity</code> which is associated with the
		 * <code>Grade</code>.
		 *
		 * @param  activity                 The <code>Activity</code>, not null
		 *
		 * @return                          This <code>Builder</code>
		 * @throws IllegalArgumentException if the <code>Activity</code> is not
		 *                                  in the <code>DataStore</code>
		 * @throws IllegalArgumentException if the <code>Activity</code> is not
		 *                                  a <code>NamedActivity</code>
		 */

		public Builder setActivity (final Activity activity)
		{
			this.log.trace ("setActivity: activity={}", activity);

			if (activity == null)
			{
				this.log.error ("The specified activity is NULL");
				throw new NullPointerException ("The specified activity is NULL");
			}

			this.activity = this.activityRetriever.fetch (activity);

			if (this.activity == null)
			{
				this.log.error ("The specified Activity does not exist in the DataStore");
				throw new IllegalArgumentException ("Activity is not in the DataStore");
			}

			if (! (this.activity instanceof NamedActivity))
			{
				this.log.error ("Only NamedActivity instances can be assigned Grades");
				throw new IllegalArgumentException ("Not a NamedActivity");
			}

			return this;
		}

		/**
		 * Get the <code>Enrolment</code>, for the student, to which the
		 * <code>Grade</code> is assigned
		 *
		 * @return The associated <code>Enrolment</code>
		 */

		public Enrolment getEnrolment ()
		{
			return this.enrolment;
		}

		/**
		 * Set the <code>Enrolment</code> which is associated with the
		 * <code>Grade</code>.
		 *
		 * @param  enrolment                The <code>Enrolment</code>, not null
		 *
		 * @return                          This <code>Builder</code>
		 * @throws IllegalArgumentException if the <code>Activity</code> is not
		 *                                  in the <code>DataStore</code>
		 */

		public Builder setEnrolment (final Enrolment enrolment)
		{
			this.log.trace ("setEnrolment: enrolment={}", enrolment);

			if (enrolment == null)
			{
				this.log.error ("The specified Enrolment is NULL");
				throw new NullPointerException ("The specified Enrolment is NULL");
			}

			this.enrolment = this.enrolmentRetriever.fetch (enrolment);

			if (this.enrolment == null)
			{
				this.log.error ("The specified Enrolment does not exist in the DataStore");
				throw new IllegalArgumentException ("Enrolment is not in the DataStore");
			}

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

		public Integer getGrade ()
		{
			return this.value;
		}

		/**
		 * Set the value of the <code>Grade</code>.
		 *
		 * @param  grade                    The value of the <code>Grade</code>,
		 *                                  not null
		 *
		 * @return                          This <code>Builder</code>
		 * @throws IllegalArgumentException If the value is less than zero or
		 *                                  greater than 100
		 */

		public Builder setGrade (final Integer grade)
		{
			this.log.trace ("setGrade: grade={}", grade);

			if (grade == null)
			{
				this.log.error ("The specified grade is NULL");
				throw new NullPointerException ("The specified grade is NULL");
			}

			if (grade < 0)
			{
				this.log.error ("Grades can not be negative: {}", grade);
				throw new IllegalArgumentException ("Grade is negative");
			}

			if (grade > 100)
			{
				this.log.error ("Grades can not be greater than 100%: {}", grade);
				throw new IllegalArgumentException ("Grade is greater than 100%");
			}

			this.value = grade;

			return this;
		}
	}

	/** Serial version id, required by the Serializable interface */
	private static final long serialVersionUID = 1L;

	/** The <code>MetaData</code> for the <code>Grade</code> */
	protected static final MetaData<Grade> METADATA;

	/** The <code>DomainModel</code> which contains the <code>Grade</code> */
	public static final Property<Grade, DomainModel> MODEL;

	/** The associated <code>Activity</code> */
	public static final Property<Grade, ActivityReference> ACTIVITY;

	/** The associated <code>Enrolment</code> */
	public static final Property<Grade, Enrolment> ENROLMENT;

	/** The assigned grade */
	public static final Property<Grade, Integer> GRADE;

	/** Select all of the <code>Grade</code> instances */
	public static final Selector<Grade> SELECTOR_ALL;

	/** Select a <code>Grade</code> based the <code>Activity</code> and <code>Enrolment</code> */
	public static final Selector<Grade> SELECTOR_PKEY;

	/**
	 * Initialize the <code>MetaData</code>, <code>Property</code> and
	 * <code>Selector</code> instances for the <code>Grade</code>.
	 */

	static
	{
		MODEL = Property.of (Grade.class, DomainModel.class, "domainmodel",
				Grade::getDomainModel, Grade::setDomainModel);

		ACTIVITY = Property.of (Grade.class, ActivityReference.class, "activity",
				Grade::getActivityReference, Grade::setActivityReference,
				Property.Flags.REQUIRED);

		ENROLMENT = Property.of (Grade.class, Enrolment.class, "enrolment",
				Grade::getEnrolment, Grade::setEnrolment,
				Property.Flags.REQUIRED);

		GRADE = Property.of (Grade.class, Integer.class, "grade",
				Grade::getGrade, Grade::setGrade,
				Property.Flags.REQUIRED, Property.Flags.MUTABLE);

		SELECTOR_ALL =  Selector.builder (Grade.class)
			.setCardinality (Selector.Cardinality.MULTIPLE)
			.setName ("all")
			.build ();

		SELECTOR_PKEY = Selector.builder (Grade.class)
			.setCardinality (Selector.Cardinality.SINGLE)
			.setName ("pkey")
			.addProperty (Grade.ACTIVITY)
			.addProperty (Grade.ENROLMENT)
			.build ();

		METADATA = MetaData.builder (Grade.class)
			.addProperty (MODEL)
			.addProperty (GRADE)
//			.addRelationship (NamedActivity.METADATA, ACTIVITY, NamedActivity.GRADES)
			.addRelationship (ENROLMENT, Enrolment.METADATA, Enrolment.GRADES)
			.addSelector (SELECTOR_ALL)
			.addSelector (SELECTOR_PKEY)
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
	 *                               the <code>Grade</code>
	 * @throws IllegalStateException if the <code>DomainModel</code> is
	 *                               immutable
	 */

	public static Builder builder (final DomainModel model)
	{
		Preconditions.checkNotNull (model, "model");

		return null;
	}

	/**
	 * Create the <code>Grade</code>.
	 */

	protected Grade ()
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
			.add ("enrolment", this.getEnrolment ())
			.add ("activity", this.getActivity ())
			.add ("grade", this.getGrade ());
	}

	/**
	 * Compare two <code>Grade</code> instances to determine if they are equal.
	 * The <code>Grade</code> instances are compared based upon the associated
	 * <code>Activity</code> and the associated <code>Enrolment</code>.
	 *
	 * @param  obj The <code>Grade</code> instance to compare to the one
	 *             represented by the called instance
	 *
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
	 * <code>equals</code> methods excludes the mutable fields from the
	 * comparison.  This methods compares two <code>Grade</code> instances
	 * using all of the fields.
	 *
	 * @param  element The <code>Element</code> instance to compare to this
	 *                 instance
	 *
	 * @return         <code>True</code> if the two <code>Grade</code>
	 *                 instances are equal, <code>False</code> otherwise
	 */

	@Override
	public boolean equalsAll (final @Nullable Element element)
	{
		return (element == this) ? true : (element instanceof Grade)
			&& Objects.equals (this.getActivity (), ((Grade) element).getActivity ())
			&& Objects.equals (this.getEnrolment (), ((Grade) element).getEnrolment ())
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
	 * Get the <code>Set</code> of <code>Property</code> instances associated
	 * with the <code>Element</code> interface class.
	 *
	 * @return The <code>Set</code> of <code>Property</code> instances
	 *         associated with the <code>Element</code> interface class
	 */

	@Override
	public Stream<Property<? extends Element, ?>> properties ()
	{
		return Grade.METADATA.properties ();
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
		return Grade.METADATA.selectors ();
	}

	/**
	 * Get an <code>Builder</code> instance for the specified
	 * <code>DomainModel</code>.  This method creates a <code>Builder</code> on
	 * the specified <code>DomainModel</code> and initializes it with the
	 * contents of this <code>Grade</code> instance.
	 *
	 * @param  model The <code>DomainModel</code>, not null
	 *
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

	public abstract Activity getActivity ();

	/**
	 * Get the <code>ActivityReference</code> for which the <code>Grade</code>
	 * is assigned.
	 *
	 * @return The associated <code>ActivityReference</code>
	 */

	protected abstract ActivityReference getActivityReference ();

	/**
	 * Set the <code>ActivityReference</code> which is associated with the
	 * <code>Grade</code>.  This method is intended to be used to initialize a
	 * new <code>Grade</code> instance.
	 *
	 * @param  activity The <code>ActivityReference</code>, not null
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
