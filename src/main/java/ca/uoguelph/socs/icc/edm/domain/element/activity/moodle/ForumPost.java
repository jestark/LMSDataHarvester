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

import ca.uoguelph.socs.icc.edm.domain.Activity;
import ca.uoguelph.socs.icc.edm.domain.LogEntry;
import ca.uoguelph.socs.icc.edm.domain.SubActivity;

import ca.uoguelph.socs.icc.edm.domain.element.GenericSubActivity;

import ca.uoguelph.socs.icc.edm.domain.metadata.MetaDataBuilder;

/**
 * Implementation of the <code>Activity</code> interface for the moodle/forum
 * <code>ActivitySource</code>/<code>ActivityType</code>.  It is expected that
 * this class will be accessed though the <code>SubActivity</code>
 * interface, along with the relevant manager, and builder.  See the
 * <code>SubActivity</code> interface documentation for details.
 * <p>
 * This class was generated from the <code>SubActivity</code> template, with
 * the following values:
 * <p>
 * <ul>
 * <li>ActivitySource = moodle
 * <li>ActivityType   = forum
 * <li>ClassName      = ForumPost
 * <li>ParentClass    = ForumDiscussion
 * </ul>
 *
 * @author  James E. Stark
 * @version 1.2
 */

public class ForumPost extends GenericSubActivity
{
	/** Serial version id, required by the Serializable interface */
	private static final long serialVersionUID = 1L;

	/**
	 * Register the <code>ForumPost</code> with the factories on
	 * initialization.
	 */

	static
	{
		MetaDataBuilder<SubActivity, ForumPost> builder = MetaDataBuilder.newInstance (SubActivity.class, ForumPost.class);
		builder.setCreateMethod (ForumPost::new);

		builder.addProperty (SubActivity.Properties.ID, SubActivity::getId, ForumPost::setId);
		builder.addProperty (SubActivity.Properties.COURSE, SubActivity::getCourse, null);
		builder.addProperty (SubActivity.Properties.NAME, SubActivity::getName, ForumPost::setName);
		builder.addProperty (SubActivity.Properties.PARENT, SubActivity::getParent, ForumPost::setParent);
		builder.addProperty (SubActivity.Properties.TYPE, SubActivity::getType, null);

		GenericSubActivity.registerActivity (builder.build (), ForumDiscussion.class);
	}

	/**
	 * Create the <code>Activity</code> instance with Null values.
	 */

	protected ForumPost ()
	{
		super ();
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
		return super.getId ();
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
		super.setId (id);
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
	public Activity getParent ()
	{
		return super.getParent ();
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

	protected void setParent (final Activity activity)
	{
		assert activity != null : "activity is NULL";

		super.setParent (activity);
	}
}
