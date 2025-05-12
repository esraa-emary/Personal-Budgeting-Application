package income;

/**
 * Represents a reminder in the Personal Budgeting Application.
 * <p>
 * This class stores information about a scheduled reminder including:
 * <ul>
 *   <li>The title or description of the reminder</li>
 *   <li>The date when the reminder should be triggered</li>
 * </ul>
 * <p>
 * Reminders help users track important financial deadlines such as bill due dates,
 * savings milestones, or budget review periods. They can be sent as email notifications
 * via the {@link Budget#sendReminder(Budget, java.util.Scanner)} method.
 *
 * @author Budget Application Team
 * @version 1.0
 * @see Budget#sendReminder(Budget, java.util.Scanner)
 */
public class Reminder {
    /** The date when the reminder should be triggered (format: YYYY-MM-DD) */
    public String date;

    /** The title or description of the reminder */
    public String title;

    /**
     * Creates a new reminder with the specified title and date.
     *
     * @param title The title or description of the reminder
     * @param date The date when the reminder should be triggered
     */
    public Reminder(String title, String date) {
        this.title = title;
        this.date = date;
    }
}