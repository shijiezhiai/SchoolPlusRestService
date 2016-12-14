package com.websystique.springmvc.util.push;

import com.websystique.springmvc.configuration.PushConfig;
import com.websystique.springmvc.configuration.WebAppConfig;
import com.websystique.springmvc.constant.Constants;
import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import com.websystique.springmvc.util.push.android.AndroidBroadcast;
import com.websystique.springmvc.util.push.android.AndroidCustomizedcast;
import com.websystique.springmvc.util.push.android.AndroidFilecast;
import com.websystique.springmvc.util.push.android.AndroidGroupcast;
import com.websystique.springmvc.util.push.android.AndroidUnicast;
import com.websystique.springmvc.util.push.ios.IOSBroadcast;
import com.websystique.springmvc.util.push.ios.IOSCustomizedcast;
import com.websystique.springmvc.util.push.ios.IOSFilecast;
import com.websystique.springmvc.util.push.ios.IOSGroupcast;
import com.websystique.springmvc.util.push.ios.IOSUnicast;

import java.io.File;

@Component
public class PushUtils {

    private String appKey;
	private String appMasterSecret;

	private PushClient pushClient;
	
	public void sendAndroidBroadcast(
	        String ticker, String title, String text,
            String extraKey, String extraValue,
            AndroidNotification.DisplayType displayType, boolean productionMode)
			throws Exception {

		AndroidBroadcast broadcast = new AndroidBroadcast(appKey, appMasterSecret);

		broadcast.setTicker(ticker);
		broadcast.setTitle(title);
		broadcast.setText(text);
		broadcast.goAppAfterOpen();
		broadcast.setDisplayType(displayType);
		broadcast.setProductionMode(productionMode);
		broadcast.setExtraField(extraKey, extraValue);

		pushClient.send(broadcast);

	}

	/*
	 * Set productionMode to False if it is a test device
	 */
	public void sendAndroidUnicast(
	        String deviceToken, String ticker, String title, String text,
            AndroidNotification.DisplayType displayType, Boolean productionMode,
            String customizedKey, String customizedValue)
            throws Exception {

		AndroidUnicast unicast = new AndroidUnicast(appKey, appMasterSecret);
		unicast.setDeviceToken(deviceToken);
		unicast.setTicker(ticker);
		unicast.setTitle(title);
		unicast.setText(text);
		unicast.goAppAfterOpen();
		unicast.setDisplayType(displayType);
		unicast.setProductionMode(productionMode);
		unicast.setExtraField(customizedKey, customizedValue);

		pushClient.send(unicast);

	}
	
	public void sendAndroidGroupcast(
	        JSONObject filterJson, String ticker, String title, String text,
            AndroidNotification.DisplayType displayType, boolean productionMode)
            throws Exception {

		AndroidGroupcast groupcast = new AndroidGroupcast(appKey, appMasterSecret);

		groupcast.setFilter(filterJson);
		groupcast.setTicker(ticker);
		groupcast.setTitle(title);
		groupcast.setText(text);
		groupcast.goAppAfterOpen();
		groupcast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
		groupcast.setProductionMode(productionMode);

		pushClient.send(groupcast);

	}

	// We don't want to use customized cast for now
	protected void sendAndroidCustomizedcast(
	        String alias, String ticker, String title, String text,
            AndroidNotification.DisplayType displayType, boolean productionMode)
            throws Exception {

		AndroidCustomizedcast customizedcast = new AndroidCustomizedcast(appKey, appMasterSecret);
		// TODO Set your alias here, and use comma to split them if there are multiple alias.
		// And if you have many alias, you can also upload a file containing these alias, then 
		// use file_id to send customized notification.
		customizedcast.setAlias("alias", alias);
		customizedcast.setTicker(ticker);
		customizedcast.setTitle(title);
		customizedcast.setText(text);
		customizedcast.goAppAfterOpen();
		customizedcast.setDisplayType(displayType);
		customizedcast.setProductionMode(productionMode);

		pushClient.send(customizedcast);
	}
	
	protected void sendAndroidCustomizedcastFile() throws Exception {

		AndroidCustomizedcast customizedcast = new AndroidCustomizedcast(appKey, appMasterSecret);

		// TODO Set your alias here, and use comma to split them if there are multiple alias.
		// And if you have many alias, you can also upload a file containing these alias, then 
		// use file_id to send customized notification.
		String fileId = pushClient.uploadContents(appKey, appMasterSecret, "aa"+"\n"+"bb"+"\n"+"alias");
		customizedcast.setFileId(fileId, "alias_type");
		customizedcast.setTicker("Android customizedcast ticker");
		customizedcast.setTitle( "中文的title");
		customizedcast.setText(  "Android customizedcast text");
		customizedcast.goAppAfterOpen();
		customizedcast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
		// TODO Set 'production_mode' to 'false' if it's a test device. 
		// For how to register a test device, please see the developer doc.
		customizedcast.setProductionMode();
		pushClient.send(customizedcast);
	}
	
	public void sendAndroidFilecast(File file, String ticker, String title, String text,
            AndroidNotification.DisplayType displayType, boolean productionMode)
            throws Exception {

		AndroidFilecast filecast = new AndroidFilecast(appKey, appMasterSecret);

		String fileId = pushClient.uploadContents(
		        appKey, appMasterSecret, FileUtils.readFileToString(file, "UTF-8"));
		filecast.setFileId(fileId);
		filecast.setTicker(ticker);
		filecast.setTitle(title);
		filecast.setText(text);
		filecast.goAppAfterOpen();
		filecast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);

		pushClient.send(filecast);

	}
	
	public void sendIOSBroadcast(
	        String alert, String customizedKey, String customizedValue,
            boolean productionMode)
            throws Exception {

		IOSBroadcast broadcast = new IOSBroadcast(appKey, appMasterSecret);

		broadcast.setAlert(alert);
		broadcast.setBadge(0);
		broadcast.setSound("default");
		broadcast.setProductionMode(productionMode);
		broadcast.setCustomizedField(customizedKey, customizedValue);

		pushClient.send(broadcast);
	}
	
	public void sendIOSUnicast(
	        String devcieToken, String alert, String customizedKey, String customziedValue,
			boolean productionMode)
            throws Exception {

		IOSUnicast unicast = new IOSUnicast(appKey, appMasterSecret);

		unicast.setDeviceToken(devcieToken);
		unicast.setAlert(alert);
		unicast.setBadge(0);
		unicast.setSound("default");
		unicast.setProductionMode(productionMode);
		unicast.setCustomizedField(customizedKey, customziedValue);

		pushClient.send(unicast);
	}
	
	public void sendIOSGroupcast(
	        JSONObject filterJson, String alert, boolean productionMode)
            throws Exception {

		IOSGroupcast groupcast = new IOSGroupcast(appKey, appMasterSecret);

		// Set filter condition into rootJson
		groupcast.setFilter(filterJson);
		groupcast.setAlert(alert);
		groupcast.setBadge(0);
		groupcast.setSound("default");
		groupcast.setProductionMode(productionMode);

		pushClient.send(groupcast);

	}
	
	protected void sendIOSCustomizedcast() throws Exception {
		IOSCustomizedcast customizedcast = new IOSCustomizedcast(appKey, appMasterSecret);
		// TODO Set your alias and alias_type here, and use comma to split them if there are multiple alias.
		// And if you have many alias, you can also upload a file containing these alias, then 
		// use file_id to send customized notification.
		customizedcast.setAlias("alias", "alias_type");
		customizedcast.setAlert("IOS 个性化测试");
		customizedcast.setBadge(0);
		customizedcast.setSound("default");
		// TODO set 'production_mode' to 'true' if your app is under production mode
		customizedcast.setTestMode();
		pushClient.send(customizedcast);
	}
	
	public void sendIOSFilecast(File file, String alert, boolean productionMode)
			throws Exception {

		IOSFilecast filecast = new IOSFilecast(appKey, appMasterSecret);

		String fileId = pushClient.uploadContents(
				appKey, appMasterSecret, FileUtils.readFileToString(file, "UTF-8"));
		filecast.setFileId(fileId);
		filecast.setAlert(alert);
		filecast.setBadge(0);
		filecast.setSound("default");
		filecast.setProductionMode(productionMode);

		pushClient.send(filecast);

	}

	public void sendLogoutPush(String deviceType, String deviceToken)
            throws Exception {
        if (deviceType.equals(Constants.LOGIN_DEV_ANDROID)) {
            sendAndroidUnicast(deviceToken, "", "您被登出", "您的账号已在其它设备登录",
                    AndroidNotification.DisplayType.NOTIFICATION, true, "", "");
        } else {
            sendIOSUnicast(deviceToken, "您的账号已在其它设备登录", null, null, true);
        }
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public void setAppMasterSecret(String appMasterSecret) {
        this.appMasterSecret = appMasterSecret;
    }

    public void setPushClient(PushClient pushClient) {
        this.pushClient = pushClient;
    }

}
