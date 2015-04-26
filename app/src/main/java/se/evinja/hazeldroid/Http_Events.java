package se.evinja.hazeldroid;

import java.util.EventListener;

public interface Http_Events extends EventListener {
    void onError(String error_msg);
    void onData(String data);
}
