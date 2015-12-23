package me.wztc.notification;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import me.wztc.util.Logger;

import android.os.Handler;

public final class NotificationCenter {

	private static final String TAG = NotificationCenter.class.getName();

	private static final NotificationCenter notificationCenter = new NotificationCenter();

	private final Map<String, Set<NotificationListenerWrapper>> dispatchTable = new HashMap<String, Set<NotificationListenerWrapper>>();

	private final Map<NotificationListener, Set<Notification>> dispatchQueue = new HashMap<NotificationListener, Set<Notification>>();

	/**
	 * Interface for notification listeners
	 */
	public interface NotificationListener {
		/**
		 * Handles received notification.
		 *
		 * @param notification
		 *            the received notification
		 */
		void notificationReceived(Notification notification);
	}

	/**
	 * Returns the notification center instance.
	 */
	public static NotificationCenter getInstance() {
		return notificationCenter;
	}

	/**
	 * Private constructor.
	 */
	private NotificationCenter() {

	}

	/**
	 * Registers a notification listener for the specified notification.
	 *
	 * @param notificationName
	 *            the name of the notification for which to register the
	 *            listener
	 * @param listener
	 *            the object registering as a listener
	 */
	public void addNotificationListener(String notificationName,
			NotificationListener listener) {
		Set<NotificationListenerWrapper> listeners;
		if (dispatchTable.containsKey(notificationName)) {
			listeners = dispatchTable.get(notificationName);
		} else {
			listeners = new CopyOnWriteArraySet<NotificationListenerWrapper>();
			dispatchTable.put(notificationName, listeners);
		}
		listeners.add(new NotificationListenerWrapper(listener));
	}

	/**
	 * Removes all the entries specifying a given listener from the dispatch
	 * table.
	 *
	 * @param listener
	 *            the listener to remove
	 */
	public void removeNotificationListener(NotificationListener listener) {
		for (Set<NotificationListenerWrapper> listeners : dispatchTable
				.values()) {
			NotificationListenerWrapper wrapper = new NotificationListenerWrapper(
					listener);
			listeners.remove(wrapper);
		}

		dispatchQueue.remove(listener);
	}

	/**
	 * Removes listener of the specified notification.
	 *
	 * @param notificationName
	 *            the name of the notification which listener is removed
	 * @param listener
	 *            the listener to remove
	 */
	public void removeNotificationListener(String notificationName,
			NotificationListener listener) {
		if (dispatchTable.containsKey(notificationName)) {
			Set<NotificationListenerWrapper> listeners = dispatchTable
					.get(notificationName);
			NotificationListenerWrapper wrapper = new NotificationListenerWrapper(
					listener);
			listeners.remove(wrapper);
			if (listeners.isEmpty()) {
				dispatchTable.remove(notificationName);
			}
		}

		dispatchQueue.remove(listener);
	}

	/**
	 * Removes listener of the specified notification.
	 *
	 * @param notificationName
	 *            the name of the notification which listener is removed
	 */
	public void removeNotificationListener(String notificationName) {
		if (dispatchTable.containsKey(notificationName)) {
			Set<NotificationListenerWrapper> listeners = dispatchTable
					.get(notificationName);
			listeners.clear();
			dispatchTable.remove(notificationName);
		}
	}

	/**
	 * Pause listener, after this call NotificationSystem will queue further
	 * notifications for this listener.
	 *
	 * @param listener
	 */
	public void pauseListener(NotificationListener listener) {
		NotificationListenerWrapper wrapper = new NotificationListenerWrapper(
				listener);
		wrapper.startQueueingNotifications();

		for (Set<NotificationListenerWrapper> listeners : dispatchTable
				.values()) {
			if (listeners.remove(wrapper)) {
				listeners.add(wrapper);
			}
		}
	}

	/**
	 * Resume will send all queued notifications to listener.
	 *
	 * @param listener
	 */
	public void resumeListener(NotificationListener listener) {
		postQueuedNotifications(listener);

		dispatchQueue.remove(listener);

		for (Set<NotificationListenerWrapper> listeners : dispatchTable
				.values()) {
			NotificationListenerWrapper wrapper = new NotificationListenerWrapper(
					listener);
			wrapper.stopQueueingNotifications();
			if (listeners.remove(wrapper)) {
				listeners.add(wrapper);
			}
		}
	}

	/**
	 * Posts queued notifications for listener after it has been resumed.
	 *
	 * @param listener
	 */
	private void postQueuedNotifications(NotificationListener listener) {
		Set<Notification> notifications = dispatchQueue.get(listener);
		if (notifications != null) {
			for (Notification notification : notifications) {
				listener.notificationReceived(notification);
			}
		}
	}

	/**
	 * Posts the notification to the listeners.
	 *
	 * @param notification
	 *            the notification to be posted
	 */
	public void postNotification(final Notification notification) {
		if (dispatchTable.containsKey(notification.getName())) {
			new Handler().post(new Runnable() {
				@Override
				public void run() {
					Set<NotificationListenerWrapper> listeners = dispatchTable
							.get(notification.getName());

					// exit if not found
					if (listeners == null)
						return;

					for (NotificationListenerWrapper listenerWrapper : listeners) {
						NotificationListener listener = listenerWrapper
								.getListener();
						if (!listenerWrapper.isQueueNotifications()) {
							Logger.logD(TAG, "forwarding notification "
									+ notification + " to " + listener);
							listener.notificationReceived(notification);
						} else {
							Logger.logD(TAG, "storing notification "
									+ notification + " to queue for "
									+ listener);
							Set<Notification> notificationSet = dispatchQueue
									.get(listener);
							if (notificationSet == null) {
								notificationSet = new HashSet<Notification>();
								dispatchQueue.put(listener, notificationSet);
							}
							notificationSet.add(notification);
						}
					}
				}
			});
		}
	}

	/**
	 * Utility wrapper for listeners. Contains extra field to store listener
	 * state.
	 */
	private static class NotificationListenerWrapper {
		private final NotificationListener mListener;
		private boolean mQueueNotifications;

		NotificationListenerWrapper(NotificationListener listener) {
			this.mListener = listener;
			this.mQueueNotifications = false;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}

			if (!(o instanceof NotificationListenerWrapper)) {
				return false;
			}

			if (((NotificationListenerWrapper) o).getListener() == mListener) {
				return true;
			}

			return false;
		}

		@Override
		public int hashCode() {
			return mListener.hashCode();
		}

		public boolean isQueueNotifications() {
			return mQueueNotifications;
		}

		public void startQueueingNotifications() {
			mQueueNotifications = true;
		}

		public void stopQueueingNotifications() {
			mQueueNotifications = false;
		}

		public NotificationListener getListener() {
			return mListener;
		}
	}

}
