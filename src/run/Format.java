package run;

/**
 * Provides ANSI escape codes for text formatting in console output.
 * <p>
 * This utility class contains constants for various text formatting options:
 * <ul>
 *   <li>Text styles (bold, underline)</li>
 *   <li>Text colors (red, green, blue, etc.)</li>
 * </ul>
 * <p>
 * These constants can be used to enhance the readability and visual appeal
 * of console output in the Personal Budgeting Application.
 *
 * @author Budget Application Team
 * @version 1.0
 */
public class Format {
    /**
     * ANSI code for bold text
     */
    public static String Bold = "\033[1m";

    /**
     * ANSI code to reset all formatting
     */
    public static String Reset = "\033[0m";

    /**
     * ANSI code for underlined text
     */
    public static String Underline = "\033[4m";

    /**
     * ANSI code for red text
     */
    public static String Red = "\033[31m";

    /**
     * ANSI code for green text
     */
    public static String Green = "\033[32m";

    /**
     * ANSI code for yellow text
     */
    public static String Yellow = "\033[33m";

    /**
     * ANSI code for blue text
     */
    public static String Blue = "\033[34m";

    /**
     * ANSI code for purple/magenta text
     */
    public static String Purple = "\033[35m";

    /**
     * ANSI code for cyan text
     */
    public static String Cyan = "\033[36m";

    /**
     * ANSI code for white text
     */
    public static String White = "\033[37m";

    /**
     * ANSI code for black text
     */
    public static String Black = "\033[30m";
}