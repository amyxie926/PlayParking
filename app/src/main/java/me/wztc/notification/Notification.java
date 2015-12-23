package me.wztc.notification;

/**
 * Notification objects encapsulate information so that it can be broadcast to
 * other objects by an NotificationCenter object. A Notification object contains
 * a name and an object. The name is a tag identifying the notification. The
 * object is any object that the poster of the notification wants to send to
 * observers of that notification (typically, it is the object that posted the
 * notification). Notification objects are immutable objects.
 */
public class Notification {

    private final String name;
    private final Object object;

    /**
     * Returns a Notification object that can be sent to observers of this
     * notification using the NotificationCenter object.
     *
     * @param name the notification name. This value must not be null.
     */
    public Notification(String name) {
        this.name = name;
        this.object = null;
    }

    /**
     * Returns a Notification object that can be sent to observers of this
     * notification using the NotificationCenter object.
     *
     * @param name   the notification name. This value must not be null.
     * @param object the object associated with the notification
     */
    public Notification(String name, Object object) {
        this.name = name;
        this.object = object;
    }

    /**
     * Returns the name of the notification.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the object associated with the notification.
     */
    public Object getObject() {
        return object;
    }

    @Override
    public String toString() {
        return "Notification [name='" + name + "', object=" + object + "]";
    }
}
