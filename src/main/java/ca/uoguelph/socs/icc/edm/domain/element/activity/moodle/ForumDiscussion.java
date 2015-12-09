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

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;

import com.google.auto.service.AutoService;
import com.google.common.base.Preconditions;

import ca.uoguelph.socs.icc.edm.domain.Activity;
import ca.uoguelph.socs.icc.edm.domain.Element;
import ca.uoguelph.socs.icc.edm.domain.LogEntry;
import ca.uoguelph.socs.icc.edm.domain.LogReference;
import ca.uoguelph.socs.icc.edm.domain.ParentActivity;
import ca.uoguelph.socs.icc.edm.domain.SubActivity;

/**
 * Implementation of the <code>Activity</code> interface for the moodle/forum
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
 * <li>ActivityType   = forum
 * <li>ClassName      = ForumDiscussion
 * <li>ParentClass    = Forum
 * </ul>
 *
 * @author  James E. Stark
 * @version 1.0
 */

public class ForumDiscussion extends SubActivity
{
	/**
	 * Representation of an <code>Element</code> implementation class.
	 * Instances of this class are used to load the <code>Element</code>
	 * implementations into the JVM via the <code>ServiceLoader</code>.
	 *
	 * @author  James E. Stark
	 * @version 1.0
	 */

	@AutoService (Element.Definition.class)
	public final class Definition extends SubActivity.Definition
	{
		/**
		 * Create the <code>Definition</code>.
		 */

		public Definition ()
		{
			super (ForumDiscussion.class, ForumDiscussion::new);
		}
	}

	/** Serial version id, required by the Serializable interface */
	private static final long serialVersionUID = 1L;

	/** The primary key for the <code>ForumDiscussion</code> */
	private Long id;

	/** The name of the <code>SubActivity</code> */
	private String name;

	/** The parent <code>Activity</code> */
	private ParentActivity parent;

	/** The <code>List</code> of <code>LogReference</code> instances */
	private List<LogReference> references;

	/** The <code>List</code> of <code>SubActivity</code> instances*/
	private List<SubActivity> subActivities;

	/**
	 * Register the <code>ForumDiscussion</code> with the factories on
	 * initialization.
	 */

	static
	{
		SubActivity.registerImplementation (Forum.class, ForumDiscussion.class);
	}

	/**
	 * Create the <code>Activity</code> instance with Null values.
	 */

	protected ForumDiscussion ()
	{
		this.id = null;
		this.name = null;
		this.parent = null;

		this.references = new ArrayList<LogReference> ();
		this.subActivities = new ArrayList<SubActivity> ();
	}

	/**
	 * Create an <code>Activity</code> from the supplied <code>Builder</code>.
	 *
	 * @param  builder The <code>Builder</code>, not null
	 */

	protected ForumDiscussion (final Builder builder)
	{
		super (builder);

		this.id = builder.getId ();
		this.name = Preconditions.checkNotNull (builder.getName (), "name");
		this.parent = Preconditions.checkNotNull (builder.getParent (), "parent");

		this.references = new ArrayList<LogReference> ();
		this.subActivities = new ArrayList<SubActivity> ();
	}

	/**
	 * Get the <code>DataStore</code> identifier for the <code>Activity</code>
	 * instance.
	 *
	 * @return The <code>DataStore</code> identifier
	 */

	@Override
	@CheckReturnValue
	public Long getId ()
	{
		return this.id;
	}

	/**
	 * Set the <code>DataStore</code> identifier.  This method is intended to
	 * be used to initialize the <code>DataStore</code> identifier on a new
	 * <code>SubActivity</code> instance.
	 *
	 * @param  id The <code>DataStore</code> identifier, not null
	 */

	@Override
	protected void setId (final @Nullable Long id)
	{
		this.id = id;
	}

	/**
	 * Get the name of the <code>SubActivity</code>.
	 *
	 * @return A <code>String</code> containing the name of the
	 *         <code>SubActivity</code>
	 */

	@Override
	public String getName ()
	{
		return this.name;
	}

	/**
	 * Set the name of the <code>SubActivity</code>.  This method is intended
	 * to be used to initialize a new <code>SubActivity</code> instance.
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
	 * <code>SubActivity</code>.  This method is intended to be used to
	 * initialize a new <code>SubActivity</code> instance.
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
	 * which act upon the <code>SubActivity</code>.
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
	 * intended to be used to initialize a new <code>SubActivity</code>
	 * instance.
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
	 * @return              <code>true</code> if the <code>LogReference</code>
	 *                      was successfully added, <code>false</code>
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
	 *
	 * @return The <code>List</code> of <code>SubActivity</code> instances
	 */

	@Override
	public List<SubActivity> getSubActivities ()
	{
		this.subActivities.forEach (x -> this.propagateDomainModel (x));

		return Collections.unmodifiableList (this.subActivities);
	}

	/**
	 * Initialize the <code>List</code> of <code>SubActivity</code> instances
	 * for the <code>Activity</code>.  This method is intended to be used to
	 * initialize a new <code>SubActivity</code> instance.
	 *
	 * @param  subActivities The <code>List</code> of <code>SubActivity</code>
	 *                       instances, not null
	 */

	@Override
	protected void setSubActivities (final List<SubActivity> subActivities)
	{
		assert subActivities != null : "subActivities is NULL";

		this.subActivities = subActivities;
	}

	/**
	 * Add the specified <code>SubActivity</code> to the
	 * <code>Activity</code>.
	 *
	 * @param  subActivity The <code>SubActivity</code> to add, not null
	 *
	 * @return             <code>true</code> if the <code>SubActivity</code>
	 *                     was successfully added, <code>false</code> otherwise
	 */

	@Override
	protected boolean addSubActivity (final SubActivity subActivity)
	{
		assert subActivity != null : "subActivity is NULL";

		return this.subActivities.add (subActivity);
	}

	/**
	 * Remove the specified <code>SubActivity</code> from the
	 * <code>Activity</code>.
	 *
	 * @param  subActivity The <code>SubActivity</code> to remove, not null
	 *
	 * @return             <code>true</code> if the <code>SubActivity</code>
	 *                     was successfully removed, <code>false</code>
	 *                     otherwise
	 */

	@Override
	protected boolean removeSubActivity (final SubActivity subActivity)
	{
		assert subActivity != null : "subActivity is NULL";

		return this.subActivities.remove (subActivity);
	}
}
