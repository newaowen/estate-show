package com.warsong.app.estateshow.misc;

import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.AsyncTask;
import android.util.Log;

import com.warsong.app.estateshow.helper.CacheHelper;
import com.warsong.app.estateshow.info.DeviceInfo;
import com.warsong.app.estateshow.util.GeneralUtil;

/**
 * 图片加载器
 * 
 * @author newaowen@gmail.com
 * @date 2013-10-19 上午11:11:28
 */
public class ImageLoader {
	
	private static final String TAG = "ImageLoader";

	public interface ImageLoaderCallback {
		public void onSuccess(String url, Bitmap bitmap);
		public void onFail(String url);
	}
	
	/**
	 * 直接加载url,不通过
	 * 
	 * @param url
	 */
	public void load(String url, ImageLoaderCallback callback) {
		//先从缓存读取，没读取到时再从服务端获取
		Bitmap bmp = null;
		byte[] data = CacheHelper.get(url);
		if (data != null) {
			bmp = createBitmap(data, 0, 0);
		} 
		
		if (bmp != null) {
			callback.onSuccess(url, bmp);
		} else {
			new DownloadImageTask(callback).execute(url);
		}
	}
	
	private class DownloadImageTask extends AsyncTask<String, Void, Boolean> {
		private ImageLoaderCallback callback;
		private String url;
		private Bitmap bmp = null;

	    public DownloadImageTask(ImageLoaderCallback callback) {
	         this.callback = callback;
	    }

	    protected Boolean doInBackground(String... urls) {
	    	url = urls[0];
	        boolean result = false;
	        try {
	            InputStream in = new java.net.URL(url).openStream();
	            if (in != null) {
	            	byte[] bytes = GeneralUtil.streamToByte(in);
	            	bmp = createBitmap(bytes, 0, 0);
	            	CacheHelper.put(url, bytes);
	            }
	            result = true;
	        } catch (Exception e) {
	            Log.e(TAG, e.getMessage());
	            e.printStackTrace();
	        }
	        return result;
	    }

	    protected void onPostExecute(Boolean flag) {
	    	if (flag) {
	    		callback.onSuccess(url, bmp);
	    	} else {
	    		callback.onFail(url);
	    	}
	    }
	}
	
	/**
     * @param data 数据或路径
     * @param width 目标宽
     * @param height 目标高
     * @return 图片
     */
    private Bitmap createBitmap(Object data, int width, int height) {
        Options options = new Options();
        int scale = 1;

        if (width > 0 && height > 0) {//创建目标大小的图片
            options.inJustDecodeBounds = true;
            if (data instanceof String) {
                BitmapFactory.decodeFile((String) data, options);
            } else {
                BitmapFactory.decodeByteArray((byte[]) data, 0, ((byte[]) data).length, options);
            }
            int dw = options.outWidth / width;
            int dh = options.outHeight / height;
            scale = Math.max(dw, dh);
            options = new Options();
        }

        options.inDensity = DeviceInfo.getInstance().getDencity();
        options.inScaled = true;
        options.inPurgeable = true;
        options.inSampleSize = scale;

        Bitmap bitmap = null;
        if (data instanceof String) {
            bitmap = BitmapFactory.decodeFile((String) data, options);
        } else {
            bitmap = BitmapFactory.decodeByteArray((byte[]) data, 0, ((byte[]) data).length,
                options);
        }
        return bitmap;
    }

	
}


