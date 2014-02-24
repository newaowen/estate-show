package com.warsong.app.estateshow.helper;

import android.app.Activity;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.RequestType;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.warsong.app.estateshow.util.StringUtil;

/**
 * 分享辅助
 * 
 * @author newaowen@gmail.com
 * @date 2013-10-27 下午3:31:27
 */
public class ShareHelper {

	public static String UMENG_APP_KEY = "526b220756240b8edb0863f3";
	public static String WX_APPID = "wx5966b200ed4710e3";
	public static String WX_APPKEY = "3c4a6ca4e917de92fea2109df9796769";
	
	public static String CONTENT_URL = "http://www.yangdx.com/";
	
	public static UMSocialService socialService;

	public static UMSocialService init(Activity act) {
		UMSocialService service = getSocialService();
		// 添加微信支持
		service.getConfig().supportWXCirclePlatform(act, WX_APPID, CONTENT_URL);
		service.getConfig().supportWXPlatform(act, WX_APPID, CONTENT_URL);
		// 添加QQ支持, 并且设置QQ分享内容的target url
		service.getConfig().supportQQPlatform(act, false, CONTENT_URL);

		// 添加新浪和QQ空间的SSO授权支持
		service.getConfig().setSsoHandler(new SinaSsoHandler());
		service.getConfig().setSsoHandler(new QZoneSsoHandler(act));

		// 设置对平台排序
		service.getConfig().removePlatform(SHARE_MEDIA.EMAIL, SHARE_MEDIA.RENREN,
				SHARE_MEDIA.SMS, SHARE_MEDIA.DOUBAN, SHARE_MEDIA.TENCENT);
		service.getConfig().setPlatformOrder(SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.WEIXIN,
				SHARE_MEDIA.SINA, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.NULL);
		
		return service;
	}

	public static void open(Activity act, UMSocialService service, String content, String imgUrl) {
		if (StringUtil.isEmpty(content)) {
			return;
		}
		
		UMImage mUMImgBitmap = new UMImage(act, imgUrl);
		mUMImgBitmap.setTargetUrl(CONTENT_URL);
		
		//图文分享
		service.setShareContent(content);
		if (!StringUtil.isEmpty(imgUrl)) {
			service.setShareMedia(mUMImgBitmap);
		}
		service.openShare(act, false);
	}

	public static UMSocialService getSocialService() {
		UMSocialService service  = UMServiceFactory.getUMSocialService("com.umeng.share", RequestType.SOCIAL);
		return service;
	}

}
