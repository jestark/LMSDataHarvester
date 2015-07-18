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

package ca.uoguelph.socs.icc.edm.domain.element;

import java.io.Serializable;

import java.util.Date;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import ca.uoguelph.socs.icc.edm.domain.Action;
import ca.uoguelph.socs.icc.edm.domain.Activity;
import ca.uoguelph.socs.icc.edm.domain.SubActivity;
import ca.uoguelph.socs.icc.edm.domain.Course;
import ca.uoguelph.socs.icc.edm.domain.Enrolment;
import ca.uoguelph.socs.icc.edm.domain.LogEntry;

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

public abstract class LogReference extends LogEntry implements Serializable
{
	/** Serial version id, required by the Serializable interface */
	private static final long serialVersionUID = 1L;

	/** The associated <code>LogEntry</code> */
	private LogEntry entry;

	/** The associated <code>SubActivity</code> */
	private SubActivity subactivity;

	/**
	 * Create the <code>LogEntry</code> with null values.
	 */

	protected LogReference ()
	{
		this.entry = null;
		this.subactivity = null;
	}

	/**
	 * Compare two <code>LogEntry</code> instances to determine if they are
	 * equal.  The <code>LogEntry</code> instances are compared based upon the
	 * contents of the parent <code>LogEntry</code> as determined by its
	 * <code>equals</code> method and the referenced <code>SubActivity</code>.
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
			ebuilder.append (this.subactivity, ((LogReference) obj).subactivity);

			result = ebuilder.isEquals ();
		}

		return result;
	}

	/**
	 * Compute a <code>hashCode</code> of the <code>LogEntry</code> instance.
	 * The hash code is computed based upon the contents of the parent
	 * <code>LogEntry</code> as returned by its <code>hashCode</code> method
	 * and the referenced <code>SubActivity</code>.
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
		hbuilder.append (this.subactivity);

		return hbuilder.toHashCode ();
	}

	/**
	 * Get the <code>DataStore</code> identifier for the <code>LogEntry</code>
	 * instance.  Since <code>LogReference</code> is dependent on the
	 * <code>LogEntryt</code> instance for its <code>DataStore</code>
	 * identifier, the identifier from the associated <code>LogEntry</code>
	 * will be returned.
	 *
	 * @return A <code>Long</code> containing <code>DataStore</code> identifier
	 */

	@Override
	public Long getId ()
	{
		return this.entry.getId ();
	}

	/**
	 * Set the <code>DataStore</code> identifier.   Since
	 * <code>LogReference</code> is dependent on the <code>LogEntry</code>
	 * instance for its <code>DataStore</code> identifier, this method throws
	 * an <code>UnsupportedOperationException</code>.
	 *
	 * @param  id                            The <code>DataStore</code>
	 *                                       identifier, not null
	 * @throws UnsupportedOperationException unconditionally
	 */

	@Override
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
	 * Get the <code>Activity</code> upon which the logged action was
	 * performed.
	 *
	 * @return A reference to the associated <code>Activity</code> object.
	 */

	@Override
	public Activity getActivity ()
	{
		return this.entry.getActivity ();
	}

	@Override
	protected void setActivity (final Activity activity)
	{
		throw new UnsupportedOperationException ();
	}

	/**
	 * Get the <code>SubActivity</code> upon which the logged
	 * <code>Action</code> was performed.
	 *
	 * @return A reference to the associated <code>SubActivity</code> instance,
	 *         may be null
	 */

	@Override
	public SubActivity getSubActivity ()
	{
		return this.subactivity;
	}

	/**
	 * Set the <code>SubActivity</code> upon which the logged
	 * <code>Action</code> was performed.  This method is intended to be used
	 * by a <code>DataStore</code> when the <code>LogEntry</code> instance is
	 * loaded.
	 *
	 * @param  subactivity The <code>SubActivity</code>, not null
	 */

	protected void setSubActivity (final SubActivity subactivity)
	{
		assert subactivity != null : "subactivity is NULL";

		this.subactivity = subactivity;
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

	@Override
	protected void setEnrolment (final Enrolment enrolment)
	{
		throw new UnsupportedOperationException ();
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
	 * Get the <code>Action</code> which was performed upon the logged
	 * activity.
	 *
	 * @return A reference to the logged <code>Action</code>
	 */

	@Override
	public Action getAction()
	{
		return this.entry.getAction ();
	}

	@Override
	protected void setAction (final Action acton)
	{
		throw new UnsupportedOperationException ();
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

	@Override
	protected void setTime (final Date time)
	{
		throw new UnsupportedOperationException ();
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

	@Override
	protected void setIPAddress (final String ip)
	{
		throw new UnsupportedOperationException ();
	}

	/**
	 * Get a <code>String</code> representation of the <code>LogEntry</code>
	 * instance, including the identifying fields.
	 *
	 * @return A <code>String</code> representation of the
	 *         <code>LogEntry</code> instance
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
