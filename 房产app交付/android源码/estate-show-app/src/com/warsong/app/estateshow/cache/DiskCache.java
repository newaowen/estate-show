package com.warsong.app.estateshow.cache;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.util.concurrent.atomic.AtomicBoolean;

import android.os.StatFs;
import android.util.Log;

import com.warsong.app.estateshow.info.AppInfo;
import com.warsong.app.estateshow.info.DeviceInfo;

/**
 * 磁盘缓存
 * 
 * 异步存储的，所以在put之后马上去get是取不出来的 使用标准http header描述来做缓存： 缓存的标识：url 缓存周期：
 * 
 * 
 * 权限？按用户？清除 list形的数据的添加 强制刷新？
 * 
 * 支持直接存取Serializable 对象
 * 
 * @author sanping.li@alipay.com
 * @author haigang.gong@alipay.com
 */
public class DiskCache {
	/**
	 * 缓存的目录
	 */
	protected String mDirectory;
	/**
	 * 缓存的最大空间
	 */
	protected long mMaxsize;
	/**
	 * 缓存的大小
	 */
	protected long mSize;
	
	/**
	 * 是否初始化
	 */
	private AtomicBoolean mInited = new AtomicBoolean();

	public DiskCache() {
		mInited.set(false);
	}

	/**
	 * 打开缓存
	 */
	public void open() {
		if (mInited.get()) {// 已经初始化了
			return;
		}
		init();//初始化
		mInited.set(true);
	}

	/**
	 * 关闭缓存
	 */
	public void close() {
	 
	}

	/**
	 * 把Serializable对象转成byte［］， 然后调用 put(String owner, String group, final
	 * String url, final byte[] data,long createTime, long period, String
	 * contentType)
	 * 
	 * @param owner
	 * @param group
	 * @param url
	 * @param serializable
	 * @param createTime
	 * @param period
	 * @param contentType
	 */
	public void putSerializable(String owner, String group, final String url,
			final Serializable serializable, long createTime, long period, String contentType) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = null;
		try {

			oos = new ObjectOutputStream(bos);
			oos.writeObject(serializable);
			byte[] objBytes = bos.toByteArray();
			this.put(url, objBytes);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				bos.close();
				if (oos != null) {
					oos.close();
				}
			} catch (IOException e) {

			}
		}
	}

	/**
	 * 添加缓存数据
	 * 
	 * @param owner
	 *            所用者：g+gid或者u+uid,比如:u10000023213。如果与所有者无关可以传null。不能为-。
	 * @param group
	 *            缓存对象所在组,null:只放到缓存中,not null:以group为key将该对象归组,不能为-
	 * @param url
	 *            数据的url
	 * @param data
	 *            数据内容
	 * @throws CacheException
	 */
	public void put(final String key, final byte[] data) {
		if (!mInited.get()) {
			throw new RuntimeException("DiskCache must call open() before");
		}
		 
		final String innerKey = obtainKey(key);
		String path = getDirectory() + File.separatorChar + innerKey;
		try {
			writeFile(path, data);
		} catch (Exception e) {// 添加失败
			Log.e("DiskCache", "fail to put cache:" + e);
		}
	}

	/**
	 * 移除缓存数据
	 * 
	 * @param url
	 *            数据的url
	 */
	public void remove(String url) {
		if (!mInited.get()) {
			throw new RuntimeException("DiskCache must call open() before");
		}
		removeLocalEntity(url);
	}

	/**
	 * 删除本地缓存数据
	 * 
	 * @param url
	 *            数据url
	 */
	private void removeLocalEntity(String url) {
		if (url != null) {
			removeCacheFile(url);
		}
	}

	protected void removeCacheFile(final String url) {
		final String key = obtainKey(url);

		String path = getDirectory() + File.separatorChar + key;
		File file = new File(path);
		if (!file.exists()) {
			return;
		}
		boolean ret = file.delete();
		if (!ret) {// 删除失败
			Log.e("DiskCache", "fail to remove cache file");
		}
	}

	/**
	 * 如果得到的byte[]为null，会返回 null 对象
	 * 
	 * @param url
	 * @return
	 * @throws CacheException
	 */
	public Serializable getSerializable(String url) throws Exception {
		byte[] objBytes = get(url);
		if (objBytes == null) {
			return null;
		}
		InputStream bis = new ByteArrayInputStream(objBytes);
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(bis);
			return (Serializable) ois.readObject();
		} catch (StreamCorruptedException e) {
			throw new Exception(e.getMessage());
		} catch (IOException e) {
			throw new Exception(e.getMessage());
		} catch (ClassNotFoundException e) {
			throw new Exception(e.getMessage());
		} finally {
			try {
				bis.close();
				if (ois != null) {
					ois.close();
				}
			} catch (IOException e) {
				Log.e("DiskCache", e + "");
			}
		}
	}

	/**
	 * 获取缓存数据
	 * 
	 * @param url
	 *            数据的url
	 * @return
	 * @throws CacheException
	 */
	public byte[] get(String key) throws Exception {
		if (!mInited.get()) {
			throw new RuntimeException("DiskCache must call open() before");
		}
		 
		String innerKey = obtainKey(key);
		String path = getDirectory() + File.separatorChar + innerKey;
		byte[] data = readFile(path);
		return data;
	}

	/**
	 * @return 缓存大小限制
	 */
	public long getMaxsize() {
		return mMaxsize;
	}

	/**
	 * @param directory
	 *            缓存目录
	 */
	protected final void setDirectory(String directory) {
		mDirectory = directory;
		if (mDirectory == null)
			throw new IllegalArgumentException("Not set valid cache directory.");
		File file = new File(mDirectory);
		if (!file.exists() && !file.mkdir()) {
			throw new IllegalArgumentException("An Error occured while  cache directory.");
		} else if (!file.isDirectory()) {
			throw new IllegalArgumentException("Not set valid cache directory.");
		}
	}

	/**
	 * @param maxsize
	 *            缓存新的大小
	 */
	protected final void setMaxsize(long maxsize) {
		mMaxsize = maxsize;
		if (mMaxsize <= 0) {
			throw new IllegalArgumentException("Not set valid cache size.");
		}
	}

	/**
	 * @return 缓存目录
	 */
	public String getDirectory() {
		return mDirectory;
	}

	 
	/**
	 * 清除缓存
	 */
	void clear() {
		File file = new File(getDirectory());
		if (file.exists() && file.isDirectory()) {
			File[] files = file.listFiles();
			if (files == null)
				return;

			for (File f : files) {
				f.delete();
			}
		}
	}

	protected String obtainKey(String url) {
		return Integer.toHexString(url.hashCode());
	}

	private byte[] readFile(String path) throws Exception {
		File file = new File(path);
		FileInputStream inputStream = null;
		if (!file.exists()) {
			throw new Exception("cache file not found.");
		}
		try {
			inputStream = new FileInputStream(file);
			byte[] data = new byte[inputStream.available()];
			inputStream.read(data);
			return data;
		} catch (IOException e) {
			throw new Exception(e == null ? "" : e.getMessage());
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void writeFile(String path, byte[] data) throws Exception {
		File file = new File(path);
		FileOutputStream outputStream = null;
		try {
			if (!file.exists() && !file.createNewFile()) {// not found
				throw new Exception("cache file create error.");
			}
			outputStream = new FileOutputStream(file);
			outputStream.write(data);
			outputStream.flush();
		} catch (FileNotFoundException e) {
			throw new Exception(e == null ? "" : e.getMessage());
		} catch (IOException e) {
			throw new Exception(e == null ? "" : e.getMessage());
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {

				}
			}
		}
	}

	/**
	 * 初始化
	 */
	protected void init() {
		String path = DeviceInfo.getInstance().getExternalStoragePath("cache");
		if (path == null) {
			path = AppInfo.getInstance().getCacheDirPath();
		}
		if (path == null) {
			throw new IllegalArgumentException("cache path is invalid");
		}
		StatFs statFs = new StatFs(path);
		long size = statFs.getBlockSize() * ((long) statFs.getAvailableBlocks());
		setDirectory(path);
		long canUseSize = size - (512 * 1024);
		setMaxsize(canUseSize > 0 ? canUseSize : 512 * 1024);// 留512K的空间，防止达到峰值
	}
}
