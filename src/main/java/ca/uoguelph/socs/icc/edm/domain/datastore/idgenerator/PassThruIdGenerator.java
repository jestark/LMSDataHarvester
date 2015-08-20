/* Copyright (C) 2015 James E. Stark
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

package ca.uoguelph.socs.icc.edm.domain.datastore.idgenerator;

import ca.uoguelph.socs.icc.edm.domain.Element;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;

import ca.uoguelph.socs.icc.edm.domain.metadata.MetaData;

/**
 * An <code>IdGenerator</code> which passes through the original value in the
 * <code>Element</code> instances unchanged.  This <code>IdGenerator</code> is
 * intended for situations where the application logic requires that the ID
 * number assigned to the <code>Element</code> instance is determined by a
 * process outside of the <code>DataStore</code>.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public class PassThruIdGenerator extends IdGenerator
{
	/** Singleton Instance of the <code>PassThruIdGenerator</code> */
	private static final PassThruIdGenerator INSTANCE;

	/**
	 * Static initializer to register the <code>IdGenerator</code> with the
	 * factory.
	 */

	static
	{
		IdGenerator.registerGenerator (PassThruIdGenerator.class, PassThruIdGenerator::getInstance);
		INSTANCE = new PassThruIdGenerator ();
	}

	/**
	 * Get an instance of the <code>PassThruIdGenerator</code>.
	 *
	 * @param  datastore The <code>DataStore</code>, not null
	 *
	 * @return           The <code>PassThruIdGenerator</code> instance
	 */

	public static PassThruIdGenerator getInstance (final DataStore datastore, final Class<? extends Element> element)
	{
		assert datastore != null : "datastore is NULL";
		assert element != null : "element is NULL";

		return PassThruIdGenerator.INSTANCE;
	}

	/**
	 * Create the <code>PassThruIdGenerator</code>.
	 */

	private PassThruIdGenerator ()
	{
		// Does nothing
	}

	/**
	 * Assign an ID number to the specified <code>Element</code> instance.
	 * This method overrides the <code>setIdMethod</code> in the superclass to
	 * make it do nothing, since this <code>IdGenerator</code> is supposed to
	 * leave the existing ID unchanged.
	 *
	 * @param  <T>      The type of the <code>Element</code>
	 * @param  metadata The <code>MetaData</code>, not null
	 * @param  element  The <code>Element</code>, not null
	 */

	@Override
	public <T extends Element> void setId (final MetaData<T> metadata, final T element)
	{
		assert metadata != null : "metadata is NULL";
		assert element != null : "element is NULL";

		// Does nothing
	}

	/**
	 * Return the next available ID number.  This method will always return
	 * null.
	 *
	 * @return A <code>Long</code> containing the next id number
	 */

	@Override
	public Long nextId ()
	{
		return null;
	}
}
