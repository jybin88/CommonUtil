# 常用工具类

CommonUtil

---
## 使用步骤

### 1. 在project的build.gradle添加如下代码(如下图)

	allprojects {
	    repositories {
	        ...
	        maven { url "https://jitpack.io" }
	    }
	}

![](<https://github.com/jybin88/public/raw/master/dependence.png>)


### 2. 在Module的build.gradle添加依赖

    implementation 'com.github.jybin88:CommonUtil:1.6'
    
DimenUtil
---------

```java
/**
 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
 *
 * @param pContext {@link android.content.Context}
 * @param pDpValue dp值
 * @return px值
 */
public static int dip2px(Context pContext, float pDpValue)

/**
 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
 *
 * @param pContext {@link android.content.Context}
 * @param pDpRes   dp resource
 * @return px值
 */
public static int dip2px(Context pContext, @DimenRes int pDpRes)

/**
 * 根据手机的分辨率从 px 的单位 转成为 dp
 *
 * @param pContext {@link android.content.Context}
 * @param pPxValue px 值
 * @return dip值
 */
public static int px2dip(Context pContext, float pPxValue)

/**
 * 根据手机的分辨率从 sp 的单位 转成为 px(像素)
 *
 * @param pContext {@link android.content.Context}
 * @param pSpValue sp值
 * @return px值
 */
public static int sp2px(Context pContext, float pSpValue)
```

FileUtil
--------

```java
/**
 * 从Uri中获取图片真实地址
 *
 * @param pContext {@link Context}
 * @param pUri     {@link Uri}
 * @return 图片真实地址
 */
public static String getRealPathFromUri(Context pContext, Uri pUri)
```
PreferenceUtil(封装SharedPreferences的读写)
--------------
```java
public static final String SHAREDPREFERENCES_NAME = "test";
//写
PreferenceUtil.getInstance(SHAREDPREFERENCES_NAME).write(Context context, String key, Object value);
//读
PreferenceUtil.getInstance(SHAREDPREFERENCES_NAME).read(Context context, String key, Object defaultValue)
```
ScreenUtil
------------

```java
/**
 * 获取状态栏高度
 *
 * @param pActivity activity
 * @return 状态栏高度
 */
public static int getStatusBarHeight(Activity pActivity)

/**
 * 获取ActionBar高度
 * @param pWrapper {@link android.view.ContextThemeWrapper}
 * @return ActionBar高度
 */
public static int getActionBarSize(ContextThemeWrapper pWrapper)

/**
 * 屏幕高度
 *
 * @param pContext context
 * @return 屏幕高度
 */
public static int getScreenHeight(Context pContext)

/**
 * 屏幕宽度
 *
 * @param pContext context
 * @return 屏幕宽度
 */
public static int getScreenWidth(Context pContext)
```
ShortcutUtil
------------

```java
 /**
 * 创建快捷方式
 *
 * @param pContext      {@link Context}
 * @param pShortcutName 快捷方式名称
 * @param pIntent       快捷方式打开的Intent
 */
public static void createShortcut(Context pContext, String pShortcutName, Intent pIntent)

/**
 * 创建快捷方式
 *
 * @param pContext      {@link Context}
 * @param pShortcutName 快捷方式名称
 * @param pIntent       快捷方式打开的Intent
 * @param pShortcutIcon 快捷方式图标
 */
public static void createShortcut(Context pContext, String pShortcutName, Intent pIntent, 
    Bitmap pShortcutIcon)
```
AppUtil
------
```java
/**
 * 获取APP图标
 *
 * @param pContext {@link Context}
 * @return APP图标
 */
public static Drawable getAppIcon(Context pContext)
```



