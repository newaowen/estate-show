package com.warsong.app.estateshow.info;

import java.io.File;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.util.DisplayMetrics;
import android.util.Log;

/**
 * 设备管理工具
 * 
 */
public class DeviceInfo {
	private static final String TAG = "DeviceInfo";

	private static DeviceInfo mInstance;

	/**
	 * 上下文
	 */
	private Context mContext;
	/**
	 * 屏幕宽度
	 */
	private int mScreenWidth;
	/**
	 * 屏幕高度
	 */
	private int mScreenHeight;
	/**
	 * 屏幕Dencity
	 */
	private int mDencity;
	/**
	 * clientid
	 */
	//private String mClientId;
	/**
	 * IMEI
	 */
	private String mImei;
	/**
	 * IMSI
	 */
	private String mImsi;
	/**
	 * 原始 IMEI
	 */
	private String defImei;
	/**
	 * 原始 IMSI
	 */
	private String defImsi;

	/** 
     * 
     *  */
	private String mMobileBrand;

	/**
	 * 运营商
	 */
	private String operator;

	/** 
     * 
     *  */
	private String mMobileModel;

	private String mSystemVersion;

	private boolean mRooted;

	// 中国移动
	public static final String CMCC = "cmcc";

	// 中国联通
	public static final String CUCC = "cucc";

	// 中国电信
	public static final String CTCC = "ctcc";

	// 未知
	public static final String UNKNOWN = "unknown";

	public static final String NULL = "null";

	/**
	 * 任意个0的字符串的正则表达式
	 */
	public static final String ANY_ZERO_STR = "[0]+";

	/**
	 * 定义硬件标志的非法长度，目前暂定为5
	 */
	public static final int HARDWARD_INVALID_LEN = 5;

	/*
	 * 字母表
	 */
	public String[] baseString = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c",
			"d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t",
			"u", "v", "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
			"L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };

	/**
	 * clientKey的最大长度
	 */
	public final static int CLIENT_KEY_MAX_LENGTH = 10;

	public final static int IMEI_LEN = 15;

	private static boolean reforceInit = false;

	@SuppressLint("SimpleDateFormat")
	private SimpleDateFormat format = new SimpleDateFormat("yyMMddHHmmssSSS");

	private DeviceInfo(Context context) {
		mContext = context;
	}

	/**
	 * 获取设备信息实例
	 * 
	 * @return 性能记录器
	 */
	public static synchronized DeviceInfo getInstance() {
		if (mInstance == null)
			throw new IllegalStateException(
					"DeviceManager must be create by call createInstance(Context context)");
		return mInstance;
	}

	/**
	 * 创建设备信息示例
	 * 
	 * @param context
	 *            上下文
	 * @return 性能记录器
	 */
	public static synchronized DeviceInfo createInstance(Context context) {
		if (mInstance == null) {
			mInstance = new DeviceInfo(context);
			mInstance.init();
		}
		return mInstance;
	}

	/**
	 * 重新强制初始化clientId
	 */
	public static synchronized void getSecurityInstance() {
		if (!reforceInit) {
			if (mInstance != null) {
				 
			}
		}
	}

	/**
	 * 初始化
	 */
	private void init() {
		DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
		mScreenWidth = displayMetrics.widthPixels;
		mScreenHeight = displayMetrics.heightPixels;
		mDencity = displayMetrics.densityDpi;
		mMobileBrand = Build.BRAND;
		mMobileModel = Build.MODEL;
		mSystemVersion = Build.VERSION.RELEASE;
		mRooted = rooted();
	}

//	/**
//	 * 生成10位随机数
//	 * 
//	 * @return
//	 */
//	private String initClientKey() {
//		String clientKey = null;
//		SharedPreferences settings = mContext.getSharedPreferences(mContext.getPackageName()
//				+ ".config", Context.MODE_PRIVATE);
//		clientKey = settings.getString("clientKey", "");
//		if (!"".equals(clientKey)) {
//			return clientKey;
//		}
//	 
//		clientKey = generateClientKey();
//		settings.edit().putString("clientKey", clientKey).commit();
//		return clientKey;
//	}
//
//	/**
//	 * 刷新clientKey
//	 * 
//	 * @return
//	 */
//	public String refleshClientKey() {
//		SharedPreferences settings = mContext.getSharedPreferences(mContext.getPackageName()
//				+ ".config", Context.MODE_PRIVATE);
//		String clientKey = generateClientKey();
//		settings.edit().putString("clientKey", clientKey).commit();
//		mClientKey = clientKey;
//		return clientKey;
//	}
//
//	/**
//	 * 先随机生成一个字符串
//	 * 
//	 * @return
//	 */
//	private String generateClientKey() {
//
//		Random random = new Random(System.currentTimeMillis());
//		int length = baseString.length;
//		String randomString = "";
//		for (int i = 0; i < CLIENT_KEY_MAX_LENGTH; i++) {
//			randomString += baseString[random.nextInt(length)];
//		}
//
//		return randomString;
//	}

	private boolean rooted() {
		boolean ret = false;
		Class<?> cla = null;
		Object value = null;
		try {
			cla = Class.forName("android.os.SystemProperties");
			@SuppressWarnings("rawtypes")
			Class[] claArrayTypes = { String.class };
			Method meth = cla.getMethod("get", claArrayTypes);
			// Method meth = cla.getMethod("secure", claArrayTypes);
			Object[] arglist = { "ro.secure" };
			value = meth.invoke(null, arglist);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (value != null && "1".equals(value)) {
			ret = false;
		} else if (value != null && "0".equals(value)) {
			ret = true;
		}
		return ret;
	}

	public int getmScreenWidth() {
		return mScreenWidth;
	}

	public int getmScreenHeight() {
		return mScreenHeight;
	}

	public String getmMobileBrand() {
		return mMobileBrand;
	}

	public String getmMobileModel() {
		return mMobileModel;
	}

	public String getmSystemVersion() {
		return mSystemVersion;
	}

	public boolean ismRooted() {
		return mRooted;
	}

	/**
	 * 获取设备屏幕宽度
	 * 
	 * @return 屏幕宽度
	 */
	public int getScreenWidth() {
		return mScreenWidth;
	}

	/**
	 * 获取设备屏幕高度
	 * 
	 * @return 屏幕高度
	 */
	public int getScreenHeight() {
		return mScreenHeight;
	}

	/**
	 * 获取设备屏幕宽度
	 * 
	 * @return 屏幕宽度
	 */
	public int getDencity() {
		return mDencity;
	}

	/**
	 * 获取OS版本
	 * 
	 * @return OS版本
	 */
	public String getOsVersion() {
		return android.os.Build.VERSION.RELEASE;
	}

	/**
	 * 获取UserAgent
	 * 
	 * @return UserAgent
	 */
	public String getUserAgent() {
		return Build.MANUFACTURER + Build.MODEL;
	}

	/**
	 * IMEI
	 * 
	 * @return IMEI
	 */
	public String getImei() {
		return mImei;
	}

	/**
	 * IMSI
	 * 
	 * @return IMSI
	 */
	public String getImsi() {
		return mImsi;
	}

	/**
	 * 获取外接SD卡上context.getPackageName()下面的目录，如果不存在则创建
	 * 
	 * @param context
	 *            上下文
	 * @param dir
	 *            外接SD卡上context.getPackageName()下面的目录名
	 * @return
	 */
	public String getExternalStoragePath(String dir) {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			String path = Environment.getExternalStorageDirectory().getPath() + File.separatorChar
					+ mContext.getPackageName();
			File file = new File(path);
			if (!file.exists() && !file.mkdir()) {
				Log.i(TAG, "failed create dir");
				return path;
			} else if (!file.isDirectory()) {
				// LogCatLog.e(TAG, dir + " dir exist,but not directory:" +
				// path);
				return null;
			}

			path = path + File.separatorChar + dir;
			file = new File(path);
			if (!file.exists() && !file.mkdir()) {
				// LogCatLog.e(TAG, "fail to creat " + dir + " dir:" + path);
				return path;
			} else if (!file.isDirectory()) {
				// LogCatLog.e(TAG, dir + " dir exist,but not directory:" +
				// path);
				return null;
			} else {
				return path;
			}
		}
		return null;
	}

	public String getTimeStamp() {
		String timeStamp = this.format.format(System.currentTimeMillis());
		return timeStamp;
	}

	public String getDefImei() {
		return defImei;
	}

	public void setDefImei(String defImei) {
		this.defImei = defImei;
	}

	public String getDefImsi() {
		return defImsi;
	}

	public void setDefImsi(String defImsi) {
		this.defImsi = defImsi;
	}
	
	public String getAccessPoint() {
		String apn = "wifi";

		try {
			ConnectivityManager cm = (ConnectivityManager) mContext
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo ni = cm.getActiveNetworkInfo();

			String extra = ni.getExtraInfo();
			if (extra == null || extra.indexOf("none") != -1)
				apn = ni.getTypeName();
			else
				apn = extra;
		} catch (Exception e) {
			e.printStackTrace();
		}

		apn = apn.replace("internet", "wifi");
		apn = apn.replace("\"", "");
		return apn;
	}

	public String getCellInfo() {
		String cellInfo = "-1;-1";

		try {
			TelephonyManager tm = (TelephonyManager) mContext
					.getSystemService(Context.TELEPHONY_SERVICE);
			// haitong
			CellLocation temp = tm.getCellLocation();
			if (temp != null) {
				StringBuilder sbcellInfo = new StringBuilder();
				if (temp instanceof GsmCellLocation) {
					GsmCellLocation gsmcl = (GsmCellLocation) temp;// tm.getCellLocation();
					int cellid = gsmcl.getCid();
					int lac = gsmcl.getLac();

					sbcellInfo.append(lac);
					sbcellInfo.append(";");
					sbcellInfo.append(cellid);

					cellInfo = sbcellInfo.toString();
				} else if (temp instanceof CdmaCellLocation) {
					CdmaCellLocation cdmacl = (CdmaCellLocation) temp;
					int cellid = cdmacl.getBaseStationLatitude();
					int lac = cdmacl.getBaseStationLongitude();

					sbcellInfo.append(lac);
					sbcellInfo.append(";");
					sbcellInfo.append(cellid);

					cellInfo = sbcellInfo.toString();
				}
			}

		} catch (Exception e) {
			// LogCatLog.printStackTraceAndMore(e);
		}

		return cellInfo;
	}

	/**
	 * 初始化运营商
	 */
	public String getOperator() {
		if (operator == null) {
			TelephonyManager telephonyManager = (TelephonyManager) mContext
					.getSystemService(Context.TELEPHONY_SERVICE);
			String imsi = telephonyManager.getSubscriberId();
			if (imsi == null)
				return UNKNOWN;

			if (imsi.startsWith("46000") || imsi.startsWith("46002") || imsi.startsWith("46007")) {
				operator = CMCC;
			} else if (imsi.startsWith("46001")) {
				operator = CUCC;

			} else if (imsi.startsWith("46003")) {
				operator = CTCC;
			} else {
				operator = UNKNOWN;
			}
		}
		return operator;
	}

}
