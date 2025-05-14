package net.goo.armament.util.helpers;

public class CameraHelper {
    private static boolean isLocked = false;

    public static void lockCamera() {
        isLocked = true;
    }

    public static void unlockCamera() {
        isLocked = false;
    }

    public static boolean isCameraLocked() {
        return isLocked;
    }
}
