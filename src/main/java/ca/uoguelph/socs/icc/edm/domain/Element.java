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

import java.util.stream.Stream;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;

import com.google.common.base.MoreObjects;

import ca.uoguelph.socs.icc.edm.domain.metadata.Property;
import ca.uoguelph.socs.icc.edm.domain.metadata.Selector;

/**
 * Root level interface for all of the elements of the domain model.  The
 * primary purpose of the <code>Element</code> interface is to allow instances
 * of the builders and loaders to use bounded generic types when referring to
 * the domain model interfaces and their implementations.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public abstract class Element implements Serializable
{
	/** Serial version id, required by the Serializable interface */
	private static final long serialVersionUID = 1L;

	/** The <code>DomainModel</code> */
	private DomainModel model;

	/**
	 * Create the <code>Element</code> instance.
	 */

	protected Element ()
	{
		this.model = null;
	}

	/**
	 * Template method to create and initialize a <code>ToStringHelper</code>.
	 *
	 * @return The <code>ToStringHelper</code>
	 */

	@CheckReturnValue
	protected MoreObjects.ToStringHelper toStringHelper ()
	{
		return MoreObjects.toStringHelper (this)
			.add ("id", this.getId ());
	}

	/**
	 * Propagate the <code>DomainModel</code> reference to the specified
	 * <code>Element</code> instance.  This is an internal method to copy the
	 * <code>DomainModel</code> reference to the target <code>Element</code>
	 * instance.  It it used to make sure that an <code>Element</code> has a
	 * reference to the <code>DomainModel</code> before it is returned by the
	 * getter method on the containing <code>Element</code> instance.
	 *
	 * @param  element The <code>Element</code>, not null
	 *
	 * @return         The supplied <code>Element</code>
	 */

	protected final <T extends Element> T propagateDomainModel (final @Nullable T element)
	{
		if ((element != null) && (this.model != null))
		{
			((Element) element).model = this.model;
		}

		return element;
	}

	/**
	 * Compare two <code>Element</code> instances to determine if they are
	 * equal using all of the instance fields.  In most cases this will be the
	 * same as the <code>equals</code> method.
	 *
	 * @param  element The <code>Element</code> instance to compare to this
	 *                 instance
	 *
	 * @return         <code>True</code> if the two <code>Enrolment</code>
	 *                 instances are equal, <code>False</code> otherwise
	 */

	public boolean equalsAll (final @Nullable Element element)
	{
		return this.equals (element);
	}

	/**
	 * Compare two <code>Element</code> instances to determine if they are
	 * equal using the minimum set fields required to identify the
	 * <code>Element</code> instance.  In almost all cases this methods will
	 * give the same result as calling <code>equals</code>.
	 *
	 * @param  element The <code>Element</code> instance to compare to this
	 *                 instance
	 *
	 * @return         <code>True</code> if the two <code>Enrolment</code>
	 *                 instances are equal, <code>False</code> otherwise
	 */

	public boolean equalsUnique (final @Nullable Element element)
	{
		return this.equals (element);
	}

	/**
	 * Get the <code>Set</code> of <code>Property</code> instances associated
	 * with the <code>Element</code> interface class.
	 *
	 * @return The <code>Set</code> of <code>Property</code> instances
	 *         associated with the <code>Element</code> interface class
	 */

	public abstract Stream<Property<? extends Element, ?>> properties ();

	/**
	 * Get the <code>Set</code> of <code>Selector</code> instances associated
	 * with the <code>Element</code> interface class.
	 *
	 * @return The <code>Set</code> of <code>Selector</code> instances
	 *         associated with the <code>Element</code> interface class
	 */

	public abstract Stream<Selector<? extends Element>> selectors ();

	/**
	 * Get an <code>Builder</code> instance for the specified
	 * <code>DomainModel</code>.  This method creates a <code>Builder</code> on
	 * the specified <code>DomainModel</code> and initializes it with the
	 * contents of this <code>Element</code> instance.
	 *
	 * @param  model The <code>DomainModel</code>, not null
	 *
	 * @return       The initialized <code>Builder</code>
	 */

	public abstract Builder<? extends Element> getBuilder (DomainModel model);

	/**
	 * Get a reference to the <code>DomainModel</code> which contains the
	 * <code>Element</code>.
	 *
	 * @return A reference to the <code>DomainModel</code>
	 */

	public final DomainModel getDomainModel ()
	{
		return this.model;
	}

	/**
	 * Set a reference to the <code>DomainModel</code> which contains the
	 * <code>Element</code>.  This method will set the internal reference to
	 * the <code>DomainModel</code> if the internal representation of the
	 * <code>DomainModel</code> is null.
	 *
	 * @param  model The <code>DomainModel</code>, not null
	 */

	protected final void setDomainModel (final DomainModel model)
	{
		assert this.model == null || model == this.model : "Can't change the DomainModel";

		this.model = model;
	}

	/**
	 * Get the <code>DataStore</code> identifier for the <code>Element</code>
	 * instance.  Some <code>Element</code> interfaces are dependent on other
	 * <code>Element</code> interfaces for their identification.  The dependent
	 * interface implementations should return the <code>DataStore</code>
	 * identifier from the interface on which they depend.
	 *
	 * @return A <code>Long</code> containing <code>DataStore</code> identifier
	 */

	@CheckReturnValue
	public abstract Long getId ();

	/**
	 * Set the <code>DataStore</code> identifier.  This method is intended to
	 * be used by a <code>DataStore</code> when the <code>Element</code>
	 * instance is loaded, or by the <code>ElementBuilder</code> implementation
	 * to set the <code>DataStore</code> identifier, prior to storing a new
	 * <code>Element</code> instance.
	 * <p>
	 * <code>Element</code> implementations which are dependent on other
	 * <code>Element</code> interfaces for their <code>DataStore</code>
	 * identifier should throw an <code>UnsupportedOperationException</code>
	 * when this method is called.
	 *
	 * @param  id The <code>DataStore</code> identifier, not null
	 */

	protected abstract void setId (@Nullable Long id);
}
