package ca.uoguelph.socs.icc.moodleapi;

public interface Course
{
	public String getName();
	public void setName(String name);
	public int getSemester();
	public void setSemester(int semester);
	public int getYear();
	public void setYear(int year);
	public ArrayList getActivities();
	public void addActivity(Activity activity);
	public ArrayList getEnrolments();
	public ArrayList getStudents();
	public ArrayList getLog();
	void addLogEntry(LogEntry entry);
	public String toString();
	void addEnrolment(Enrolment enrolment);
}