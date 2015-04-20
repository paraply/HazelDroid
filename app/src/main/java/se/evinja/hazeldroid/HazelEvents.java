package se.evinja.hazeldroid;

import java.util.EventListener;

import se.evinja.hazeldroid.Hazel;

public interface HazelEvents extends EventListener {
    void onConnectionError(Hazel.HazelCommand duringCommand, String errorMsg);
    void onConnected();
    void onUserSchedule();
    void onStaffSchedule();
    void onStaffDownloaded();
}
