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

package ca.uoguelph.socs.icc.edm.domain.core;

import java.io.Serializable;

import java.util.Date;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import ca.uoguelph.socs.icc.edm.domain.Action;
import ca.uoguelph.socs.icc.edm.domain.Activity;
import ca.uoguelph.socs.icc.edm.domain.ActivityGroupMember;
import ca.uoguelph.socs.icc.edm.domain.Course;
import ca.uoguelph.socs.icc.edm.domain.Enrolment;
import ca.uoguelph.socs.icc.edm.domain.LogEntry;

import ca.uoguelph.socs.icc.edm.domain.builder.LogReferenceElementFactory;

/**
 * An abstract representation of the relationship between a
 * <code>LogEntry</code> and a sub-activity.  This class acts as the abstract
 * base class for all of the logging related to sub-activities.
 * <p>
 * Note that there exists a co-dependency between this class and
 * <code>LogData</code>. Instances of this class require the data in
 * <code>LogData</code> to be uniquely identified, and when this class is
 * present the instances of <code>LogData</code> require data in this class to
 * be uniquely identified.  The co-dependency effects the implementations for
 * the <code>equals</code>, <code>hashCode</code> and <code>toString</code>
 * methods.
 * <p>
 * Normally, the implementation of the relation ship between two classes we see
 * each class call the others <code>equals</code>, <code>hashCode</code> or
 * <code>toString</code> method when it is needed.  With the co-dependency each
 * class calling the others <code>equals</code>, <code>hashCode</code> or
 * <code>toString</code> method would lead to an infinite mutual recursion.  To
 * avoid the infinite mutual recursion, this class re-implements the 
 * <code>equals</code>, <code>hashCode</code> and <code>toString</code> methods
 * from <code>LogData</code> using the methods from the <code>LogEntry</code>
 * interface.  When this class is present, <code>LogData</code> calls the
 * <code>equals</code>, <code>hashCode</code> and <code>toString</code> methods
 * from this class rather than doing the computations itself.
 * <p>
 * Ideally, <code>LogData</code> would be encapsulated by this class and the
 * dependency would be one way (from this class to <code>LogData</code>).
 * However, limitations imposed by the database and the Java Persistence API
 * require that these classes are co-dependant.  Since this class is optional,
 * <code>LogData</code> needs to be able to stand alone.  There is no way to
 * tell JPA to encapsulate <code>LogData</code> within the instance of this
 * class, when this class is present.  So from the point of view of the
 * database, <code>LogData</code> encapsulates this class.  However, it is
 * expected that some programs will treat this class as encapsulating
 * <code>LogData</code>, so the instances of both classes need to be able to be
 * resolved starting from either class.
 *
 * @author  James E. Stark
 * @version 1.1
 */

public abstract class LogReference extends AbstractElement implements LogEntry, Serializable
{
	/** Serial version id, required by the Serializable interface */
	private static final long serialVersionUID = 1L;

	/** The associated log entry */
	private LogEntry entry;

	/** The associated sub-activity */
	private ActivityGroupMember activity;

	/**
	 * Register the <code>LogReference</code> with the factories.  This method
	 * handles the registrations for the subclasses to reduce code duplication.
	 *
	 * @param  elementImpl The <code>Element</code> implementation class, not null
	 * @param  factory     The <code>ElementFactory</code>, not null
	 */	

	protected static final <T extends LogReference, U extends LogReferenceElementFactory> void registerLog (final Class<T> elementImpl, final Class<U> factory, final U factoryImpl)
	{
		assert elementImpl != null : "elementImpl is NULL";
		assert factory != null : "factory is NULL";
		assert factoryImpl != null : "factoryImpl is NULL";

		AbstractElement.registerFactory (LogEntry.class, elementImpl, factory, factoryImpl);
	}

	/**
	 * Create the <code>LogEntry</code> with null values.
	 */

	public LogReference ()
	{
		this.entry = null;
		this.activity = null;
	}

	/**
	 * Create the <code>LogReference</code> specifying all of its values.  
	 *
	 * @param  entry    The parent <code>LogEntry</code> object
	 * @param  activity The Sub-activity
	 */

	public LogReference (final LogEntry entry, final ActivityGroupMember activity)
	{
		this ();

		assert entry != null : "entry is NULL";
		assert activity != null : "activity is NULL";

		this.entry = entry;
		this.activity = activity;
	}

	/**
	 * Compare two <code>LogEntry</code> instances to determine if they are equal.
	 * The <code>LogEntry</code> instances are compared based upon the contents of
	 * the parent <code>LogEntry</code> as determined by its <code>equals</code>
	 * method and the referenced sub-activity.
	 *
	 * @param  obj The <code>LogEntry</code> instance to compare to the one
	 *             represented by the called instance
	 *
	 * @return     <code>True</code> if the two <code>LogEntry</code> instances
	 *             are equal, <code>False</code> otherwise
	 */

	@Override
	public boolean equals (Object obj)
	{
		boolean result = false;

		if (obj == this)
		{
			result = true;
		}
		else if (obj instanceof LogReference)
		{
			EqualsBuilder ebuilder = new EqualsBuilder ();

			ebuilder.append (this.entry.getAction (), ((LogReference) obj).getAction ());
			ebuilder.append (this.entry.getActivity (), ((LogReference) obj).getActivity ());
			ebuilder.append (this.entry.getEnrolment (), ((LogReference) obj).getEnrolment ());
			ebuilder.append (this.entry.getIPAddress (), ((LogReference) obj).getIPAddress ());
			ebuilder.append (this.entry.getTime (), ((LogReference) obj).getTime ());
			ebuilder.append (this.activity, ((LogReference) obj).activity);

			result = ebuilder.isEquals ();
		}

		return result;
	}

	/**
	 * Compute a <code>hashCode</code> of the <code>LogEntry</code> instance.
	 * The hash code is computed based upon the contents of the parent
	 * <code>LogEntry</code> as returned by its <code>hashCode</code> method and
	 * the referenced sub-activity.
	 *
	 * @return An <code>Integer</code> containing the hash code
	 */

	@Override
	public int hashCode ()
	{
		final int base = 1109;
		final int mult = 877;

		HashCodeBuilder hbuilder = new HashCodeBuilder (base, mult);

		hbuilder.append (this.entry.getAction ());
		hbuilder.append (this.entry.getActivity ());
		hbuilder.append (this.entry.getEnrolment ());
		hbuilder.append (this.entry.getIPAddress ());
		hbuilder.append (this.entry.getTime ());
		hbuilder.append (this.activity);

		return hbuilder.toHashCode ();
	}

	/**
	 * Get the <code>DataStore</code> identifier for the <code>LogEntry</code>
	 * instance.  Since <code>LogReference</code> is dependent on the
	 * <code>LogEntryt</code> instance for its <code>DataStore</code>
	 * identifier, the identifier from the associated <code>LogEntry</code> will
	 * be returned.
	 *
	 * @return A <code>Long</code> containing <code>DataStore</code> identifier
	 */

	public Long getId ()
	{
		return this.entry.getId ();
	}

	/**
	 * Set the <code>DataStore</code> identifier.   Since
	 * <code>LogReference</code> is dependent on the <code>LogEntry</code>
	 * instance for its <code>DataStore</code> identifier, this method throws an
	 * <code>UnsupportedOperationException</code>.
	 *
	 * @param  id                            The <code>DataStore</code>
	 *                                       identifier, not null
	 * @throws UnsupportedOperationException unconditionally
	 */

	protected void setId (final Long id)
	{
		throw new UnsupportedOperationException ();
	}

	/**
	 * Get the parent <code>LogEntry</code> instance.
	 *
	 * @return The parent <code>LogEntry</code> instance
	 */

	public LogEntry getEntry ()
	{
		return this.entry;
	}

	/**
	 * Set the reference to the parent <code>LogEntry</code>.  This method is
	 * intended to be used by a <code>DataStore</code> when the
	 * <code>LogEntry</code> instance is loaded.
	 *
	 * @param  entry The parent <code>LogEntry</code> instance, not null
	 */

	protected void setEntry (final LogEntry entry)
	{
		assert entry != null : "entry is NULL";

		this.entry = entry;
	}

	/**
	 * Get the Sub-Activity upon which the logged action was performed.
	 *
	 * @return A reference to the associated <code>Activity</code> instance
	 */

	@Override
	public Activity getActivity ()
	{
		return this.activity;
	}

	/**
	 * Set the Sub-Activity upon which the logged action was performed.  This
	 * method is intended to be used by a <code>DataStore</code> when the
	 * <code>LogEntry</code> instance is loaded.
	 *
	 * @param  activity The sub-activity, not null
	 */

	protected void setActivity (final ActivityGroupMember activity)
	{
		assert activity != null : "activity is NULL";

		this.activity = activity;
	}

	/**
	 * Get the <code>Enrolment</code> object for the user which performed the
	 * logged activity.
	 *
	 * @return A reference to the associated <code>Enrolment</code>
	 */

	@Override
	public Enrolment getEnrolment()
	{
		return this.entry.getEnrolment ();
	}

	/**
	 * Get the <code>Course</code> for which the activity was logged.
	 *
	 * @return A reference to the associated <code>Course</code> 
	 */

	@Override
	public Course getCourse()
	{
		return this.entry.getCourse ();
	}

	/**
	 * Get the <code>Action</code> which was performed upon the logged activity.
	 *
	 * @return A reference to the logged <code>Action</code>
	 */

	@Override
	public Action getAction()
	{
		return this.entry.getAction ();
	}

	/**
	 * Get the time of the logged activity.
	 *
	 * @return A <code>Date</code> object containing the logged time
	 */

	@Override
	public Date getTime()
	{
		return this.entry.getTime ();
	}

	/**
	 * Get the Internet Protocol address which is associated with the logged
	 * activity.
	 *
	 * @return A <code>String</code> containing the IP address, may be null
	 */

	@Override
	public String getIPAddress ()
	{
		return this.entry.getIPAddress ();
	}

	/**
	 * Get a <code>String</code> representation of the <code>LogEntry</code>
	 * instance, including the identifying fields.
	 *
	 * @return A <code>String</code> representation of the <code>LogEntry</code>
	 *         instance
	 */

	@Override
	public String toString ()
	{
		ToStringBuilder builder = new ToStringBuilder (this);

		builder.append ("enrolment", this.entry.getEnrolment ());
		builder.append ("action", this.entry.getAction ());
		builder.append ("activity", this.entry.getActivity ());
		builder.append ("time", this.entry.getTime ());
		builder.append ("ipaddress", this.entry.getIPAddress ());	
		builder.append ("subactivity", this.entry.getActivity ());

		return builder.toString ();
	}
}
