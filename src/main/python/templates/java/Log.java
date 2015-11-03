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

import ca.uoguelph.socs.icc.edm.domain.SubActivity;
import ca.uoguelph.socs.icc.edm.domain.LogEntry;
import ca.uoguelph.socs.icc.edm.domain.LogReference;

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

	/** The associated <code>SubActivity</code> */
	private SubActivity subActivity;

	/**
	 * Register the <code>${ClassName}</code> with the factories on
	 * initialization.
	 */

	static
	{
		LogReference.registerImplementation (${SubActivityClass}.class, ${ClassName}.class, $ClassName::new);
	}

	/**
	 * Create the <code>LogEntry</code> instance with Null values.
	 */

	protected ${ClassName} ()
	{
		this.subActivity = null;
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
