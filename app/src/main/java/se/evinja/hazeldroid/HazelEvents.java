package se.evinja.hazeldroid;

import java.util.EventListener;

public interface HazelEvents extends EventListener {
    void onError(Hazel.HazelCommand duringCommand, String errorMsg);
    void onConnected();
    void onUserSchedule();
    void onStaffSchedule();
    void onStaffDownloaded();
    void onWorkerAdded();
}
