package com.android.util;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RequestBuilder {
	public static String buildComandRequest(String request) throws IOException {
		String imeistring = "Server";
		String str = "";

		if (!request.equals(" ") && !request.equals("")) {
			JSONObject jsonObject = new JSONObject();
			JSONArray jsonArray;
			str = "[" + request + "]";
			try {
				jsonObject.put("device", imeistring);
				jsonArray = new JSONArray(str);
				jsonObject.put("data", jsonArray);
				str = jsonObject.toString();

			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			return str;
		}
		return str;
	}
}
