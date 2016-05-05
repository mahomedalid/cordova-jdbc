package com.lizardsc.plugins;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.StrictMode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import android.util.Log;

public class Jdbc extends CordovaPlugin {

	private Connection con = null;

	public Jdbc() {
		super();
	}

	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		try {
			Log.e("Jdbc-Cordova", action);
			
			if (action.equals("open")) {
				if (android.os.Build.VERSION.SDK_INT > 9) {
					StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
							.permitAll().build();
					StrictMode.setThreadPolicy(policy);
					Log.e("Jdbc-Cordova", "Setting policy as strict");
				}
				
				Log.e("Jdbc-Cordova", "Getting args");
				JSONObject myArg = args.getJSONObject(0);
				int timeOut = 20;
				Log.e("Jdbc-Cordova", "Getting timeout");
				if(myArg.has("timeOut")) {
					timeOut = myArg.getInt("timeOut");
				}
				Log.e("Jdbc-Cordova", "Getting other args");
				String url = myArg.getString("url");
				// "jdbc:jtds:sqlserver://server/database";
				String user = myArg.getString("user");// "user";
				String password = myArg.getString("password");// "pass";
				Log.e("Jdbc-Cordova", url);
				Log.e("Jdbc-Cordova", user);
				Log.e("Jdbc-Cordova", password);
				try {
					// Class.forName("com.mysql.jdbc.Driver");
					// Class.forName("net.sourceforge.jtds.jdbc.Driver");
					Log.e("Jdbc-Cordova", myArg.getString("class"));
					Class.forName(myArg.getString("class")).newInstance();
					DriverManager.setLoginTimeout(timeOut);
					Log.e("Jdbc-Cordova", "Trying ...");
					con = DriverManager.getConnection(url, user, password);
					Log.e("Jdbc-Cordova", "Done");
				} catch (java.lang.ClassNotFoundException e) {
					Log.e("Jdbc-Cordova", "ex1");
					callbackContext.error(e.getLocalizedMessage());
					return true;
				} catch (SQLException e) {
					Log.e("Jdbc-Cordova", "ex2");
					callbackContext.error(e.getLocalizedMessage());
					return true;
				} catch (Exception e) {
					Log.e("Jdbc-Cordova", "ex3");
					callbackContext.error(e.getLocalizedMessage());
					return true;
				}
				
				Log.e("Jdbc-Cordova", "exit");
				callbackContext.success();
				return true;

			} else if (action.equals("close")) {
				try{
				con.close();
				callbackContext.success();
				return true;
				} catch(Exception e) {
					callbackContext.error(e.getLocalizedMessage());
				}

			} else if (action.equals("executeQuery")) {
				Statement st;
				try {
					JSONObject myArgs = args.getJSONObject(0);
					st = con.createStatement();
					ResultSet rs = st.executeQuery(myArgs.getString("query"));
					JSONArray jsonArray = new JSONArray();
					while (rs.next()) {
						int total_rows = rs.getMetaData().getColumnCount();
						JSONObject obj = new JSONObject();
						for (int i = 0; i < total_rows; i++) {
							obj.put(rs.getMetaData().getColumnLabel(i + 1)
									.toLowerCase(), rs.getObject(i + 1));
						}
						jsonArray.put(obj);
					}
					callbackContext.success(jsonArray);
					return true;
				} catch (SQLException e) {
					callbackContext.error(e.getLocalizedMessage());
					return true;
				} catch (JSONException e) {
					callbackContext.error(e.getLocalizedMessage());
					return true;
				}

			}
		} catch (Exception e) {
			callbackContext.error(e.getLocalizedMessage());
			return true;
		}
		return false;
	}
}
