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

package ca.uoguelph.socs.icc.edm.domain.element.activity.${ActivitySource};

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import ca.uoguelph.socs.icc.edm.domain.SubActivity;
import ca.uoguelph.socs.icc.edm.domain.LogEntry;
import ca.uoguelph.socs.icc.edm.domain.LogReference;

import ca.uoguelph.socs.icc.edm.domain.metadata.Implementation;

/**
 * Implementation of the <code>LogEntry</code> interface for logs referencing
 * the <code>SubActivity</code> implemented by that
 * <code>${SubActivityClass}</code> class.  It is expected that this class will be
 * accessed though the <code>LogEntry</code> interface, along with the relevant
 * manager, and builder.  See the <code>LogEntry</code> interface documentation
 * for details.
 * <p>
 * This class was generated from the <code>Log</code> template, with
 * the following values:
 * <p>
 * <ul>
 * <li>ActivitySource    = ${ActivitySource}
 * <li>ActivityType      = ${ActivityType}
 * <li>ClassName         = ${ClassName}
 * <li>SubActivityClass  = ${SubActivityClass}
 * </ul>
 *
 * @author  James E. Stark
 * @version 1.1
 */

class ${ClassName} extends LogReference
{
	/** Serial version id, required by the Serializable interface */
	private static final long serialVersionUID = 1L;

	/** The associated <code>LogEntry</code> */
	private LogEntry entry;

	/** The associated <code>SubActivity</code> */
	private SubActivity subActivity;

	/**
	 * Register the <code>${ClassName}</code> with the factories on
	 * initialization.
	 */

	static
	{
		Implementation.getInstance (LogReference.class, $ClassName.class, $ClassName::new);
		LogReference.registerImplementation (${SubActivityClass}.class, ${ClassName}.class);
	}

	/**
	 * Create the <code>LogEntry</code> instance with Null values.
	 */

	protected ${ClassName} ()
	{
		this.entry = null;
		this.subActivity = null;
	}

	/**
	 * Compare two <code>LogReference</code> instances to determine if they are
	 * equal.
	 *
	 * @param  obj The <code>LogReference</code> instance to compare to the one
	 *             represented by the called instance
	 *
	 * @return     <code>True</code> if the two <code>LogReference</code>
	 *             instances are equal, <code>False</code> otherwise
	 */

	@Override
	public boolean equals (Object obj)
	{
		boolean result = false;

		if (obj == this)
		{
			result = true;
		}
		else if (obj instanceof ${ClassName})
		{
			EqualsBuilder ebuilder = new EqualsBuilder ();

			ebuilder.appendSuper (super.equals (obj));

			result = ebuilder.isEquals ();
		}

		return result;
	}

	/**
	 * Compute a <code>hashCode</code> of the <code>LogReference</code>
	 * instance.
	 *
	 * @return An <code>Integer</code> containing the hash code
	 */

	@Override
	public int hashCode ()
	{
		final int base = ${HashBase};
		final int mult = ${HashMult};

		HashCodeBuilder hbuilder = new HashCodeBuilder (base, mult);
		hbuilder.appendSuper (super.hashCode ());

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

	@Override
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

	@Override
	protected void setEntry (final LogEntry entry)
	{
		assert entry != null : "entry is NULL";

		this.entry = entry;
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
		return this.subActivity;
	}

	/**
	 * Set the <code>SubActivity</code> upon which the logged
	 * <code>Action</code> was performed.  This method is intended to be used
	 * by a <code>DataStore</code> when the <code>LogEntry</code> instance is
	 * loaded.
	 *
	 * @param  subActivity The <code>SubActivity</code>, not null
	 */

	@Override
	protected void setSubActivity (final SubActivity subActivity)
	{
		assert subActivity != null : "subactivity is NULL";

		this.subActivity = subActivity;
	}
}
