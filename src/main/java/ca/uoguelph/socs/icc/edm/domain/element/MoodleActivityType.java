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

import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;

import com.google.auto.service.AutoService;
import com.google.common.base.Preconditions;

import ca.uoguelph.socs.icc.edm.domain.ActivitySource;
import ca.uoguelph.socs.icc.edm.domain.ActivityType;
import ca.uoguelph.socs.icc.edm.domain.Element;

/**
 * Implementation of the <code>ActivityType</code> interface.  It is expected
 * that instances of this class will be accessed though the
 * <code>ActivityType</code> interface, along with the relevant manager, and
 * builder.  See the <code>ActivityType</code> interface documentation for
 * details.
 * <p>
 * This class implements a special version of the <code>ActivityType</code> for
 * use with the Moodle.  Since Moodle does not have a concept of an
 * <code>ActivitySource</code>, this class sets the <code>ActivitySource</code>
 * statically.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public class MoodleActivityType extends ActivityType
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
	public static final class Definition extends ActivityType.Definition
	{
		/**
		 * Create the <code>Definition</code>.
		 */

		public Definition ()
		{
			super (MoodleActivityType.class, MoodleActivityType::new);
		}
	}

	/** Serial version id, required by the Serializable interface */
	private static final long serialVersionUID = 1L;

	/** The source of the activity type */
	private static final ActivitySource source;

	/** The primary key of the activity type */
	private @Nullable Long id;

	/** The name of the activity type */
	private String name;

	/**
	 * Static initializer to create the static <code>ActivitySource</code>
	 * instance.
	 */

	static
	{
		source = new ActivitySourceData ();
		((ActivitySourceData) source).setName ("moodle");
	}

	/**
	 * Create the <code>ActivityType</code> with null values.
	 */

	protected MoodleActivityType ()
	{
		this.id = null;
		this.name = null;
	}

	/**
	 * Create an <code>ActivityType</code> from the supplied
	 * <code>Builder</code>.  Since this class is Moodle-specific, and it only
	 * intended to be loaded from the Moodle database, this method throws an
	 * <code>UnsupportedOperationException</code>.
	 *
	 * @param  builder The <code>Builder</code>, not null
	 *
	 * @throws UnsupportedOperationException unconditionally as
	 *                                       <code>MoodleActivityType</code>
	 *                                       instances should not be created via
	 *                                       a <code>Builder</code>
	 */

	protected MoodleActivityType (final ActivityType.Builder builder)
	{
		throw new UnsupportedOperationException ("Creation via a builder is not supported");
	}

	/**
	 * Get the <code>DataStore</code> identifier for the
	 * <code>ActivityType</code> instance.
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
	 * <code>ActivityType</code> instance.
	 *
	 * @param  id The <code>DataStore</code> identifier, not null
	 */

	@Override
	protected void setId (final @Nullable Long id)
	{
		this.id = id;
	}

	/**
	 * Get the name of the <code>ActivityType</code>.
	 *
	 * @return A <code>String</code> containing the name of the
	 *         <code>ActivityType</code>
	 */

	@Override
	public String getName ()
	{
		return this.name;
	}

	/**
	 * Set the name of the <code>ActivityType</code>.  This method is intended
	 * to be used to initialize a new  <code>ActivityType</code> instance.
	 *
	 * @param  name The name of the <code>ActivityType</code>
	 */

	@Override
	protected void setName (final String name)
	{
		assert name != null : "name is NULL";

		this.name = name;
	}

	/**
	 * Get the <code>ActivitySource</code> for the <code>ActivityType</code>.
	 *
	 * @return The <code>ActivitySource</code> instance
	 */

	@Override
	public ActivitySource getSource ()
	{
		return this.propagateDomainModel (MoodleActivityType.source);
	}

	/**
	 * Set the <code>ActivitySource</code> for the <code>ActivityType</code>.
	 * This method is unsupported for the <code>MoodleActivityType</code> as
	 * the <code>ActivitySoource</code> is statically set to "moodle."
	 *
	 * @param  source The <code>ActivitySource</code> for the
	 *                <code>ActivityType</code>
	 */

	@Override
	protected void setSource (final ActivitySource source)
	{
		assert source != null : "source is NULL";

		throw new UnsupportedOperationException ();
	}
}
