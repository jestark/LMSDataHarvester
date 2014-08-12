package ca.uoguelph.socs.icc.moodleapi;

public interface ActivityGrade
{
	public Activity getActivity();
//	void setActivity(Activity activity);
	public int getGrade();
	public Grade getStudent();
//	void setStudent(Grade student);
	public String toString();
}