package ca.uoguelph.socs.icc.edm.moodledb;

import java.util.Set;
import java.util.HashSet;

import ca.uoguelph.socs.icc.edm.domain.Role;
import ca.uoguelph.socs.icc.edm.domain.EnrolledUser;

class MoodleEnrolmentUserData extends EnrolledUser
{
	private Set<Role> roles;

	protected MoodleEnrolmentUserData ()
	{
		super ();
		roles = null;
	}
}
