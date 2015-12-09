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

package ca.uoguelph.socs.icc.edm.domain;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import javax.inject.Scope;

/**
 * Defines the Scope (within Dagger) for the <code>Builder</code> instances.
 * <p>
 * In Dagger, <code>@Component</code> dependencies must exist between scoped
 * components or un-scoped components.  An un-scoped component is not allowed
 * to depend on a scoped component.  Furthermore, dagger required that
 * dependencies between scoped components form a tree with respect to their
 * scopes.  Such a tree would have the scopes at the nodes and
 * <code>@Singleton</code> at the root.
 * <p>
 * <code>@BuilderScope</code> exists to allow the components which produce the
 * <code>Builder</code> instances (which are un-scoped) to depend on the
 * components which produce the <code>IdGenerator</code> instances (which are
 * scoped).
 *
 * @author  James E. Stark
 * @version 1.0
 */

@Scope
@Documented
@Retention (value=RUNTIME)
public @interface BuilderScope {}
