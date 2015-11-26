/* Copyright (C) 2015 James E. Stark
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

	public static final class Builder implements Element.Builder<ActivityReference>
	{
		/** The Logger */
		private final Logger log;

		/** Helper to substitute <code>Course</code> instances */
		private final Retriever<Course> courseRetriever;

		/** Helper to substitute <code>ActivityType</code> instances */
		private final Retriever<ActivityType> typeRetriever;

		/** Helper to operate on <code>ActivityReference</code> instances*/
		private final Persister<ActivityReference> persister;

		/** Method reference to the constructor of the implementation class */
		private final Supplier<ActivityReference> supplier;

		/** The loaded or previously built <code>ActivityReference</code> */
		private ActivityReference reference;

		/** The <code>DataStore</code> id number for the <code>Activity</code> */
		private Long id;

		/** The associated <code>ActivityType</code> */
		private ActivityType type;

		/** The associated <code>Course</code> */
		private Course course;

		/**
		 * Create the <code>Builder</code>.
		 *
		 * @param  supplier        Method reference to the constructor of the
		 *                         implementation class, not null
		 * @param  persister       The <code>Persister</code> used to store the
		 *                         <code>ActivityReference</code>, not null
		 * @param  typeRetriever   <code>Retriever</code> for
		 *                         <code>ActivityType</code> instances, not null
		 * @param  courseRetriever <code>Retriever</code> for <code>Course</code>
		 *                         instances, not null
		 */

		protected Builder (final Supplier<ActivityReference> supplier, final Persister<ActivityReference> persister, final Retriever<ActivityType> typeRetriever, final Retriever<Course> courseRetriever)
		{
			assert supplier != null : "supplier is NULL";
			assert persister != null : "persister is NULL";
			assert typeRetriever != null : "typeRetriever is NULL";
			assert courseRetriever != null : "courseRetriever is NULL";

			this.log = LoggerFactory.getLogger (this.getClass ());

			this.typeRetriever = typeRetriever;
			this.courseRetriever = courseRetriever;
			this.persister = persister;
			this.supplier = supplier;

			this.id = null;
			this.course = null;
			this.type = null;
			this.reference = null;
		}

		/**
		 * Create an instance of the <code>Activity</code>.
		 *
		 * @return                       The new <code>Activity</code> instance
		 * @throws IllegalStateException If any if the fields is missing
		 * @throws IllegalStateException If there isn't an active transaction
		 */

		@Override
		public ActivityReference build ()
		{
			this.log.trace ("build:");

			if (this.course == null)
			{
				this.log.error ("course is NULL");
				throw new IllegalStateException ("course is NULL");
			}

			ActivityReference result = this.supplier.get ();
			result.setId (this.id);
			result.setType (this.type);
			result.setCourse (this.course);

			this.reference = this.persister.insert (this.reference, result);

			return this.reference;
		}

		/**
		 * Reset the builder.  This method will set all of the fields for the
		 * <code>Activity</code> to be built to <code>null</code>.
		 *
		 * @return This <code>ActionBuilder</code>
		 */

		public Builder clear ()
		{
			this.log.trace ("clear:");

			this.id = null;
			this.course = null;
			this.type = null;
			this.reference = null;

			return this;
		}

		/**
		 * Load a <code>Activity</code> instance into the builder.  This method
		 * resets the builder and initializes all of its parameters from
		 * the specified <code>Activity</code> instance.  The  parameters are
		 * validated as they are set.
		 *
		 * @param  activity                 The <code>Activity</code>, not null
		 *
		 * @throws IllegalArgumentException If any of the fields in the
		 *                                  <code>Activity</code> instance to be
		 *                                  loaded are not valid
		 */

		public Builder load (final ActivityReference reference)
		{
			this.log.trace ("load: reference={}", reference);

			if (reference == null)
			{
				this.log.error ("Attempting to load a NULL Activityreference");
				throw new NullPointerException ();
			}

			this.id = reference.getId ();
			this.setType (reference.getType ());
			this.setCourse (reference.getCourse ());
			this.reference = reference;

			return this;
		}

		/**
		 * Get the <code>DataStore</code> identifier for the
		 * <code>ActivityReference</code> instance.
		 *
		 * @return The <code>DataStore</code> identifier
		 */

		@CheckReturnValue
		public Long getId ()
		{
			return this.id;
		}

		/**
		 * Get the <code>Course</code> with which the <code>Activity</code> is
		 * associated.
		 *
		 * @return The <code>Course</code> instance
		 */

		public final Course getCourse ()
		{
			return this.course;
		}

		/**
		 * Set the <code>Course</code> with which the <code>Activity</code> is
		 * associated.
		 *
		 * @param  course                   The <code>Course</code>, not null
		 *
		 * @throws IllegalArgumentException If the <code>Course</code> does not
		 *                                  exist in the <code>DataStore</code>
		 */

		public final Builder setCourse (final Course course)
		{
			this.log.trace ("setCourse: course={}", course);

			if (course == null)
			{
				this.log.error ("Course is NULL");
				throw new NullPointerException ("Course is NULL");
			}

			this.course = this.courseRetriever.fetch (course);

			if (this.course == null)
			{
				this.log.error ("This specified Course does not exist in the DataStore");
				throw new IllegalArgumentException ("Course is not in the DataStore");
			}

			return this;
		}

		/**
		 * Get the <code>ActivityType</code> for the <code>Activity</code>.
		 *
		 * @return The <code>ActivityType</code> instance
		 */

		public final ActivityType getType ()
		{
			return this.type;
		}

		/**
		 * Set the <code>ActivityType</code> for the <code>Activity</code>.
		 *
		 * @param  type                     The <code>ActivityType</code>, not
		 *                                  null
		 *
		 * @throws IllegalArgumentException If the <code>ActivityType</code>
		 *                                  does not exist in the
		 *                                  <code>DataStore</code>
		 */

		public final Builder setType (final ActivityType type)
		{
			this.log.trace ("setType: type={}", type);

			if (type == null)
			{
				this.log.error ("type is NULL");
				throw new NullPointerException ("type is NULL");
			}

			this.type = this.typeRetriever.fetch (type);

			if (this.type == null)
			{
				this.log.error ("This specified ActivityType does not exist in the DataStore");
				throw new IllegalArgumentException ("ActivityType is not in the DataStore");
			}

			return this;
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
	 * Compare two <code>Activity</code> instances to determine if they are
	 * equal.
	 *
	 * @param  obj The <code>Activity</code> instance to compare to the one
	 *             represented by the called instance
	 *
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
	 * Get an <code>Builder</code> instance for the specified
	 * <code>DomainModel</code>.  This method creates a <code>Builder</code> on
	 * the specified <code>DomainModel</code> and initializes it with the
	 * contents of this <code>ActivityReference</code> instance.
	 * <p>
	 * <code>ActivityReference</code> instances are created though the builder
	 * for the associated <code>Activity</code> instance.
	 *
	 * @param  model The <code>DomainModel</code>, not null
	 *
	 * @return           The initialized <code>ActivityBuilder</code>
	 */

	@Override
	public Builder getBuilder (final DomainModel model)
	{
		return null; // new Builder (Preconditions.checkNotNull (model, "model"))
//			.load (this);
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
		return ActivityReference.METADATA.properties ();
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
		return ActivityReference.METADATA.selectors ();
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
	 *
	 * @return          <code>True</code> if the <code>LogEntry</code> was
	 *                  successfully added, <code>False</code> otherwise
	 */

	protected abstract boolean addLog (LogEntry entry);

	/**
	 * Remove the specified <code>LogEntry</code> from the specified
	 * <code>ActivityReference</code>.
	 *
	 * @param  entry    The <code>LogEntry</code> to remove, not null
	 *
	 * @return          <code>True</code> if the <code>LogEntry</code> was
	 *                  successfully removed, <code>False</code> otherwise
	 */

	protected abstract boolean removeLog (LogEntry entry);
}
