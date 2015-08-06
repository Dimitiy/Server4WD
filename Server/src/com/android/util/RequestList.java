package com.android.util;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

public class RequestList {
	public static String sendCommandRequest(String command) {
		String request = null;
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("command", command);
			jsonObject.put("type", AppConstants.TYPE_COMMAND_REQUEST);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			request = RequestBuilder.buildComandRequest(jsonObject
					.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return request;
	}
}
