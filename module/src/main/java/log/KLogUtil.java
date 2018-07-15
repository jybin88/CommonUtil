package log;

import android.text.TextUtils;
import android.util.Log;

public final class KLogUtil {
    private KLogUtil() {
        /*private constructor*/
    }

    public static boolean isEmpty(String line) {
        return TextUtils.isEmpty(line) || line.equals("\n") || line.equals("\t") || TextUtils.isEmpty(line.trim());
    }

    public static void printLine(String tag, boolean isTop) {
        if (isTop) {
            Log.d(tag, "╔═══════════════════════════════════════════════════════════════════════════════════════");
        } else {
            Log.d(tag, "╚═══════════════════════════════════════════════════════════════════════════════════════");
        }
    }

}
