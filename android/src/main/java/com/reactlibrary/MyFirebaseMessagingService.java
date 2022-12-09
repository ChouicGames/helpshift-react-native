import android.util.Log;
import android.content.Intent;
import com.facebook.react.ReactApplication;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;
import io.invertase.firebase.messaging.ReactNativeFirebaseMessagingService;
import com.helpshift.Helpshift;

import java.util.Map;

public class MyFirebaseMessagingService extends ReactNativeFirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private static ReactNativeFirebaseMessagingService reactNativeFirebaseMessagingService = new ReactNativeFirebaseMessagingService();

    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "register player");
        reactNativeFirebaseMessagingService.onNewToken(token);
        Helpshift.registerPushToken(token);
    }

    @Override
    public void onMessageReceived(RemoteMessage message) {
        Map<String, String> data = message.getData();
        Log.d(TAG, "onMessageReceived, data: " + data);
        String origin = data.get("origin");
        if (origin != null && origin.equals("helpshift")) {
            Helpshift.handlePush(data);
        }
        if (message.getNotification() == null && message.getData().containsKey("mp_message")) {
            Intent intent = message.toIntent();
            if (!intent.hasExtra("mp_icnm")) {
                String ANDROID_SMALL_ICON = "pw_notification";
                intent.putExtra("mp_icnm", ANDROID_SMALL_ICON);

                if (!intent.hasExtra("mp_icnm_w")) {
                    intent.putExtra("mp_icnm_w", ANDROID_SMALL_ICON);
                }
                if (!intent.hasExtra("mp_icnm_l")) {
                    String ANDROID_LARGE_ICON = "ic_launcher";
                    intent.putExtra("mp_icnm_l", ANDROID_LARGE_ICON);
                }
            }

            if (!intent.hasExtra("mp_color")) {
                String ANDROID_NOTIFICATION_ACCENT_COLOR = "#0013bc";
                intent.putExtra("mp_color", ANDROID_NOTIFICATION_ACCENT_COLOR
                );
            }
        }
        Log.d(TAG, "onMessageReceived event received");
    }
}