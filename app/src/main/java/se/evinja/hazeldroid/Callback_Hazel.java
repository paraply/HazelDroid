package se.evinja.hazeldroid;

import java.util.EventListener;

public interface Callback_Hazel extends EventListener {
    void onError(Hazel.HazelCommand duringCommand, String errorMsg);
    void onConnected();
    void onUserSchedule();
    void onStaffSchedule();
    void onTasksDownloaded();
    void onStaffDownloaded();
    void onQualificationsDownloaded();
    void onWorkerAdded();
}
