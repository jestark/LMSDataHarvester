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

import java.io.Serializable;

import java.util.Map;
import java.util.List;
import java.util.Set;

import java.util.HashMap;
import java.util.Objects;

import java.util.stream.Stream;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;
import ca.uoguelph.socs.icc.edm.domain.datastore.MemDataStore;
import ca.uoguelph.socs.icc.edm.domain.datastore.Profile;
import ca.uoguelph.socs.icc.edm.domain.datastore.ProfileBuilder;

import ca.uoguelph.socs.icc.edm.domain.datastore.idgenerator.SequentialIdGenerator;

import ca.uoguelph.socs.icc.edm.domain.element.ActivitySourceData;
import ca.uoguelph.socs.icc.edm.domain.element.ActivityTypeData;
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
 * @see     ActivityBuilder
 */

public abstract class Activity extends ParentActivity
{
	/** Serial version id, required by the Serializable interface */
	private static final long serialVersionUID = 1L;

	/** Internal <code>DomainModel</code> for sources and types */
	private static DomainModel store;

	/** <code>ActivityType</code> to implementation class map */
	private static final Map<ActivityType, Class<? extends Activity>> activities;

	/** The <code>MetaData</code> for the <code>Activity</code> */
	protected static final MetaData<Activity> METADATA;

	/** The <code>DomainModel</code> which contains the <code>Activity</code> */
	public static final Property<Activity, DomainModel> MODEL;

	/** The name of the <code>Activity</code> */
	public static final Property<Activity, String> NAME;

	/** The associated <code>ActivityReference</code> */
	public static final Property<Activity, ActivityReference> REFERENCE;

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

		MODEL = Property.of (Activity.class, DomainModel.class, "domainmodel");
		NAME = Property.of (Activity.class, String.class, "name", Property.Flags.REQUIRED);
		REFERENCE = Property.of (Activity.class, ActivityReference.class, "reference", Property.Flags.REQUIRED);

		SELECTOR_ID = Selector.of (Activity.class, Selector.Cardinality.KEY, REFERENCE);
		SELECTOR_ALL = Selector.of (Activity.class, Selector.Cardinality.MULTIPLE, "all");

		METADATA = MetaData.builder (Activity.class)
			.addProperty (MODEL, Activity::getDomainModel, Activity::setDomainModel)
			.addProperty (NAME, Activity::getName)
			.addProperty (REFERENCE, Activity::getReference, Activity::setReference)
			.addRelationship (ActivityReference.METADATA, REFERENCE, ActivityReference.ACTIVITY)
			.addSelector (SELECTOR_ID)
			.addSelector (SELECTOR_ALL)
			.build ();
	}

	private static synchronized final void initDataStore ()
	{
		if (Activity.store == null)
		{
//			Profile activityProf = new ProfileBuilder ()
//				.setName ("Activity")
//				.setMutable (true)
//				.setElementClass (ActivitySource.class, ActivitySourceData.class)
//				.setElementClass (ActivityType.class, ActivityTypeData.class)
//				.setGenerator (Element.class, SequentialIdGenerator.class)
//				.build ();

			// Activity.store = new DomainModel (DataStore.getInstance (MemDataStore.class, activityProf));
		}
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

		if (Activity.store == null)
		{
			Activity. initDataStore ();
		}

		Activity.store.getTransaction ().begin ();

		ActivityType atype = ActivityType.builder (Activity.store)
			.setActivitySource (ActivitySource.builder (Activity.store)
					.setName (source)
					.build ())
			.setName (type)
			.build ();

		Activity.store.getTransaction ().commit ();

		assert ! Activity.activities.containsKey (atype) : "Implementation class already registered for ActivityType";

		Activity.activities.put (atype, impl);
	}

	/**
	 * Determine if an <code>Activity</code> implementation class has been
	 * registered for the specified <code>ActivityType</code>.
	 *
	 * @param  type The <code>ActivityType</code>, not null
	 *
	 * @return      <code>true</code> if an <code>Activity</code>
	 *              implementation class has been registered for the specified
	 *              <code>ActivityType</code>, <code>false</code> otherwise
	 */

	public static final boolean hasActivityClass (final ActivityType type)
	{
		assert type != null : "type is NULL";

		return Activity.activities.containsKey (type);
	}

	/**
	 * Get the <code>Activity</code> implementation class which is associated
	 * with the specified <code>ActivityType</code>.
	 *
	 * @param  type The <code>ActivityType</code>
	 *
	 * @return      The <code>Activity </code> data class for the given
	 *              <code>ActivityType</code>, may be null
	 */

	public static final Class<? extends Activity> getActivityClass (final ActivityType type)
	{
		assert type != null : "type is NULL";

		return (Activity.activities.containsKey (type)) ? Activity.activities.get (type) : GenericActivity.class;
	}

	/**
	 * Get an instance of the <code>ActivityBuilder</code> for the specified
	 * <code>DomainModel</code>.
	 *
	 * @param  model                 The <code>DomainModel</code>, not null
	 * @param  type                  The <code>ActivityType</code>, not null
	 *
	 * @return                       The <code>ActivityBuilder</code> instance
	 * @throws IllegalStateException if the <code>DomainModel</code> is closed
	 * @throws IllegalStateException if the <code>DomainModel</code> does not
	 *                               have a default implementation class for
	 *                               the <code>Activity</code>
	 * @throws IllegalStateException if the <code>DomainModel</code> is
	 *                               immutable
	 */

	public static ActivityBuilder builder (final DomainModel model, final ActivityType type)
	{
		Preconditions.checkNotNull (model, "model");
		Preconditions.checkNotNull (type, "type");

		return null;
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
	 * Template method to create and initialize a <code>ToStringHelper</code>.
	 *
	 * @return The <code>ToStringHelper</code>
	 */

	@Override
	@CheckReturnValue
	protected MoreObjects.ToStringHelper toStringHelper ()
	{
		return super.toStringHelper ()
			.add ("reference", this.getReference ());
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
		return (obj == this) ? true : (obj instanceof Activity)
				&& Objects.equals (this.getReference (), ((Activity) obj).getReference ());
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
		return Objects.hash (this.getReference ());
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
	 * Get the <code>Set</code> of <code>Property</code> instances associated
	 * with the <code>Element</code> interface class.
	 *
	 * @return The <code>Set</code> of <code>Property</code> instances
	 *         associated with the <code>Element</code> interface class
	 */

	@Override
	public Stream<Property<? extends Element, ?>> properties ()
	{
		return Activity.METADATA.properties ();
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
		return Activity.METADATA.selectors ();
	}

	/**
	 * Determine if the value contained in the <code>Element</code> represented
	 * by the specified <code>Property</code> has the specified value.  If the
	 * <code>Property</code> represents a singe value, then this method will be
	 * equivalent to calling the <code>equals</code> method on the value
	 * represented by the <code>Property</code>.  This method is equivalent to
	 * calling the <code>contains</code> method for <code>Property</code>
	 * instances that represent collections.
	 *
	 * @return <code>true</code> if the value represented by the
	 *         <code>Property</code> equals/contains the specified value,
	 *         <code>false</code> otherwise.
	 */

	@Override
	public <V> boolean hasValue (final Property<V> property, final V value)
	{
		return Activity.METADATA.getReference (property)
			.hasValue (this, value);
	}

	/**
	 * Get a <code>Stream</code> containing all of the values in this
	 * <code>Element</code> instance which are represented by the specified
	 * <code>Property</code>.  This method will return a <code>Stream</code>
	 * containing zero or more values.  For a single-valued
	 * <code>Property</code>, the returned <code>Stream</code> will contain
	 * exactly zero or one values.  An empty <code>Stream</code> will be
	 * returned if the associated value is null.  A <code>Stream</code>
	 * containing all of the values in the associated collection will be
	 * returned for multi-valued <code>Property</code> instances.
	 *
	 * @param  <V>      The type of the values in the <code>Stream</code>
	 * @param  property The <code>Property</code>, not null
	 *
	 * @return          The <code>Stream</code>
	 */

	@Override
	public <V> Stream<V> stream (final Property<V> property)
	{
		return Activity.METADATA.getReference (property)
			.stream (this);
	}

	/**
	 * Get an <code>ActivityBuilder</code> instance for the specified
	 * <code>DomainModel</code>.  This method creates a <code>ActivityBuilder</code>
	 * on the specified <code>DomainModel</code> and initializes it with the
	 * contents of this <code>Activity</code> instance.
	 *
	 * @param  model The <code>DomainModel</code>, not null
	 *
	 * @return       The initialized <code>ActivityBuilder</code>
	 */

	@Override
	public ActivityBuilder getBuilder (final DomainModel model)
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
}
