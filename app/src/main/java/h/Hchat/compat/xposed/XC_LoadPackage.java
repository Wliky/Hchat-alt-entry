package h.Hchat.compat.xposed;

import android.content.pm.ApplicationInfo;

/** 旧版包加载参数的内部适配对象。 */
public final class XC_LoadPackage {
    private XC_LoadPackage() {
    }

    public static final class LoadPackageParam {
        public final String packageName;
        public final String processName;
        public final ClassLoader classLoader;
        public final ApplicationInfo appInfo;
        public final boolean isFirstApplication;

        public LoadPackageParam(String packageName,
                                String processName,
                                ClassLoader classLoader,
                                ApplicationInfo appInfo,
                                boolean isFirstApplication) {
            this.packageName = packageName;
            this.processName = processName;
            this.classLoader = classLoader;
            this.appInfo = appInfo;
            this.isFirstApplication = isFirstApplication;
        }
    }
}
