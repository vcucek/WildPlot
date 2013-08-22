package si.wildplot.common.util;

import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vito
 */
public class Logging
{
//    private static final String MESSAGE_BUNDLE_NAME = Logging.class.getPackage().getName() + ".MessageStrings";

    private Logging()
    {
    } // Prevent instantiation

    /**
     * Returns the DirtWars.
     *
     * @return The logger.
     */
    public static Logger logger()
    {
		String loggerName = "DirtWarsLogger";
		return logger(loggerName);
    }

    public static Logger logger(String loggerName)
    {
        return Logger.getLogger(loggerName != null ? loggerName : "");
    }

    public static String getMessage(String property)
    {
            return property;
    }

    /**
     * Retrieves a message from the World Wind message resource bundle formatted with a single argument. The argument is
     * inserted into the message via {@link java.text.MessageFormat}.
     *
     * @param property the property identifying which message to retrieve.
     * @param arg      the single argument referenced by the format string identified <code>property</code>.
     * @return The requested string formatted with the argument.
     * @see java.text.MessageFormat
     */
    public static String getMessage(String property, String arg)
    {
        return arg != null ? getMessage(property, (Object) arg) : getMessage(property);
    }

    /**
     * Retrieves a message from the World Wind message resource bundle formatted with specified arguments. The arguments
     * are inserted into the message via {@link java.text.MessageFormat}.
     *
     * @param property the property identifying which message to retrieve.
     * @param args     the arguments referenced by the format string identified <code>property</code>.
     * @return The requested string formatted with the arguments.
     * @see java.text.MessageFormat
     */
    public static String getMessage(String property, Object... args)
    {
        String message;
        message = property;

        try
        {
            // TODO: This is no longer working with more than one arg in the message string, e.g., {1}
            return args == null ? message : MessageFormat.format(message, args);
        }
        catch (IllegalArgumentException e)
        {
            message = "Message arguments do not match format string: " + property;
            logger().log(Level.SEVERE, message, e);
            return message;
        }
    }
}
