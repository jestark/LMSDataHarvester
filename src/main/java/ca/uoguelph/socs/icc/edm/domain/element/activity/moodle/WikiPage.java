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

package ca.uoguelph.socs.icc.edm.domain.element.activity.moodle;

import java.io.Serializable;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import java.util.stream.Collectors;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import ca.uoguelph.socs.icc.edm.domain.Activity;
import ca.uoguelph.socs.icc.edm.domain.LogEntry;
import ca.uoguelph.socs.icc.edm.domain.LogReference;
import ca.uoguelph.socs.icc.edm.domain.ParentActivity;
import ca.uoguelph.socs.icc.edm.domain.SubActivity;

import ca.uoguelph.socs.icc.edm.domain.metadata.Implementation;

/**
 * Implementation of the <code>Activity</code> interface for the moodle/wiki
 * <code>ActivitySource</code>/<code>ActivityType</code>.  It is expected that
 * this class will be accessed though the <code>Activity</code> interface,
 * along with the relevant manager, and builder.  See the <code>Activity</code>
 * interface documentation for details.
 * <p>
 * This class was generated from the <code>SubActivity</code> template,
 * with the following values:
 * <p>
 * <ul>
 * <li>ActivitySource = moodle
 * <li>ActivityType   = wiki
 * <li>ClassName      = WikiPage
 * <li>ParentClass    = Wiki
 * <li>HashBase       = 2113
 * <li>HashMult       = 541
 * </ul>
 *
 * @author  James E. Stark
 * @version 1.3
 */

public class WikiPage extends SubActivity implements Serializable
{
	/** Serial version id, required by the Serializable interface */
	private static final long serialVersionUID = 1L;

	/** The primary key for the <code>WikiPage</code> */
	private Long id;

	/** The name of the <code>SubActivity</code> */
	private String name;

	/** The parent <code>Activity</code> */
	private ParentActivity parent;

	/** The <code>List</code> of <code>LogReference</code> instances */
	private List<LogReference> references;

	/** The <code>List</code> of <code>SubActivity</code> instances*/
	private List<SubActivity> subactivities;

	/**
	 * Register the <code>WikiPage</code> with the factories on
	 * initialization.
	 */

	static
	{
		Implementation.getInstance (SubActivity.class, WikiPage.class, WikiPage::new);
		SubActivity.registerImplementation (Wiki.class, WikiPage.class);
	}

	/**
	 * Create the <code>Activity</code> instance with Null values.
	 */

	protected WikiPage ()
	{
		this.id = null;
		this.name = null;
		this.parent = null;

		this.references = new ArrayList<LogReference> ();
		this.subactivities = new ArrayList<SubActivity> ();
	}

	/**
	 * Compare two <code>SubActivity</code> instances to determine if they are
	 * equal.  The <code>SubActivity</code> instances are compared based upon
	 * their names and the parent <code>Activity</code>.
	 *
	 * @param  obj The <code>SubActivity</code> instance to compare to the one
	 *             represented by the called instance
	 *
	 * @return     <code>True</code> if the two <code>SubActivity</code>
	 *             instances are equal, <code>False</code> otherwise
	 */

	@Override
	public boolean equals (final Object obj)
	{
		boolean result = false;

		if (obj == this)
		{
			result = true;
		}
		else if (obj instanceof WikiPage)
		{
			result = super.equals (obj);
		}

		return result;
	}

	/**
	 * Compute a <code>hashCode</code> of the <code>SubActivity</code>
	 * instance.  The hash code is computed based upon the parent
	 * <code>Activity</code> and the name of the <code>SubActivity</code>
	 * instance.
	 *
	 * @return The hash code
	 */

	@Override
	public int hashCode ()
	{
		final int base = 2113;
		final int mult = 541;

		HashCodeBuilder hbuilder = new HashCodeBuilder (base, mult);
		hbuilder.appendSuper (super.hashCode ());

		return hbuilder.toHashCode ();
	}

	/**
	 * Get the <code>DataStore</code> identifier for the <code>Activity</code>
	 * instance.
	 * <p>
	 * This method is a redefinition of the same method in the superclass.  It
	 * exists solely to allow JPA to map the relationship to the instances of
	 * the child class.
	 *
	 * @return a Long integer containing <code>DataStore</code> identifier
	 */

	@Override
	public Long getId ()
	{
		return this.id;
	}

	/**
	 * Set the <code>DataStore</code> identifier.  This method is intended to
	 * be used by a <code>DataStore</code> when the <code>Activity</code>
	 * instance is loaded, or by the <code>ActivityBuilder</code>
	 * implementation to set the <code>DataStore</code> identifier, prior to
	 * storing a new <code>Activity</code> instance.
	 * <p>
	 * This method is a redefinition of the same method in the superclass.  It
	 * exists solely to allow JPA to map the relationship to the instances of
	 * the child class.
	 *
	 * @param  id The <code>DataStore</code> identifier, not null
	 */

	@Override
	protected void setId (final Long id)
	{
		this.id = id;
	}

	/**
	 * Get the name of the <code>Activity</code>.  Not all
	 * <code>Activity</code> instances have names.  For those
	 * <code>Activity</code> instances which do not have names, the name of the
	 * associated <code>ActivityType</code> will be returned.
	 *
	 * @return A <code>String</code> containing the name of the
	 *         <code>Activity</code>
	 */

	@Override
	public String getName ()
	{
		return this.name;
	}

	/**
	 * Set the name of the <code>SubActivity</code>.  This method is intended
	 * to be used by a <code>DataStore</code> when the <code>SubActivity</code>
	 * instance is loaded.
	 *
	 * @param  name The name of the <code>SubActivity</code>, not null
	 */

	@Override
	protected void setName (final String name)
	{
		assert name != null : "name is NULL";

		this.name = name;
	}

	/**
	 * Get the parent <code>Activity</code> instance for the
	 * <code>SubActivity</code>.
	 * <p>
	 * This method is a redefinition of the same method in the superclass.  It
	 * exists solely to allow JPA to map the relationship to the instances of
	 * the parent class.
	 *
	 * @return The parent <code>Activity</code>
	 */

	@Override
	public ParentActivity getParent ()
	{
		return this.propagateDomainModel (this.parent);
	}

	/**
	 * Set the <code>Activity</code> instance which contains the
	 * <code>SubActivity</code>.  This method is intended to be used by a
	 * <code>DataStore</code> when the <code>Activity</code> instance is
	 * loaded.
	 * <p>
	 * This method is a redefinition of the same method in the superclass.  It
	 * exists solely to allow JPA to map the relationship to the instances of
	 * the parent class.
	 *
	 * @param  activity The <code>Activity</code> containing this
	 *                  <code>SubActivity</code> instance
	 */

	@Override
	protected void setParent (final ParentActivity activity)
	{
		assert activity != null : "activity is NULL";

		this.parent = activity;
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
		return this.getReferences ()
			.stream ()
			.map (LogReference::getEntry)
			.collect (Collectors.toList ());
	}

	/**
	 * Get a <code>List</code> of all of the <code>LogReference</code>
	 * instances for the <code>SubActivity</code>.
	 *
	 * @return A <code>List</code> of <code>LogReference</code> instances
	 */

	@Override
	public List<LogReference> getReferences ()
	{
		this.references.forEach (x -> this.propagateDomainModel (x));

		return Collections.unmodifiableList (this.references);
	}

	/**
	 * Initialize the <code>List</code> of <code>LogReference</code> instances
	 * associated with the <code>SubActivity</code> instance.  This method is
	 * intended to be used by a <code>DataStore</code> when the
	 * <code>SubActivity</code> instance is loaded.
	 *
	 * @param  references The <code>List</code> of <code>LogReference</code>
	 *                    instances, not null
	 */

	@Override
	protected void setReferences (final List<LogReference> references)
	{
		assert references != null : "references is NULL";

		this.references = references;
	}

	/**
	 * Add the specified <code>LogReference</code> to the
	 * <code>SubActivity</code>.
	 *
	 * @param  reference    The <code>LogReference</code> to add, not null
	 *
	 * @return              <code>True</code> if the <code>LogReference</code>
	 *                      was successfully added, <code>False</code>
	 *                      otherwise
	 */

	@Override
	protected boolean addReference (final LogReference reference)
	{
		assert reference != null : "reference is NULL";

		return this.references.add (reference);
	}

	/**
	 * Remove the specified <code>LogReference</code> from the
	 * <code>SubActivity</code>.
	 *
	 * @param  reference    The <code>LogReference</code> to remove, not null
	 *
	 * @return              <code>True</code> if the <code>LogReference</code>
	 *                      was successfully removed, <code>False</code>
	 *                      otherwise
	 */

	@Override
	protected boolean removeReference (final LogReference reference)
	{
		assert reference != null : "reference is NULL";

		return this.references.remove (reference);
	}

	/**
	 * Get the <code>List</code> of <code>SubActivity</code> instances
	 * associated with the <code>Activity</code>.
	 * <p>
	 * This method is a redefinition of the same method in the superclass.  It
	 * exists solely to allow JPA to map the relationship to the instances of
	 * the child class.
	 *
	 * @return The <code>List</code> of <code>SubActivity</code> instances
	 */

	@Override
	public List<SubActivity> getSubActivities ()
	{
		this.subactivities.forEach (x -> this.propagateDomainModel (x));

		return Collections.unmodifiableList (this.subactivities);
	}

	/**
	 * Initialize the <code>List</code> of <code>SubActivity</code> instances
	 * for the <code>Activity</code>.  This method is intended to be used by a
	 * <code>DataStore</code> when the <code>Activity</code> instance is
	 * loaded.
	 * <p>
	 * This method is a redefinition of the same method in the superclass.  It
	 * exists solely to allow JPA to map the relationship to the instances of
	 * the child class.
	 *
	 * @param  subactivities The <code>List</code> of <code>SubActivity</code>
	 *                       instances, not null
	 */

	@Override
	protected void setSubActivities (final List<SubActivity> subactivities)
	{
		assert subactivities != null : "subactivities is NULL";

		this.subactivities = subactivities;
	}

	/**
	 * Add the specified <code>SubActivity</code> to the
	 * <code>Activity</code>.
	 *
	 * @param  subactivity The <code>SubActivity</code> to add, not null
	 *
	 * @return             <code>True</code> if the <code>SubActivity</code>
	 *                     was successfully added, <code>False</code> otherwise
	 */

	@Override
	protected boolean addSubActivity (final SubActivity subactivity)
	{
		assert subactivity != null : "subactivity is NULL";

		return this.subactivities.add (subactivity);
	}

	/**
	 * Remove the specified <code>SubActivity</code> from the
	 * <code>Activity</code>.
	 *
	 * @param  subactivity The <code>SubActivity</code> to remove, not null
	 *
	 * @return             <code>True</code> if the <code>SubActivity</code>
	 *                     was successfully removed, <code>False</code>
	 *                     otherwise
	 */

	@Override
	protected boolean removeSubActivity (final SubActivity subactivity)
	{
		assert subactivity != null : "subactivity is NULL";

		return this.subactivities.remove (subactivity);
	}
}