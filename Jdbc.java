package org.apache.cordova.plugin;

import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.StrictMode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Jdbc extends CordovaPlugin {

	private Connection con = null;

	public Jdbc() {
		super();
	}

	@Override
	public boolean execute(String action, final JSONArray args,
			final CallbackContext callbackContext) throws JSONException {
		try {
			if (action.equals("open")) {
				if (android.os.Build.VERSION.SDK_INT > 9) {
					StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
							.permitAll().build();
					StrictMode.setThreadPolicy(policy);
				}
				JSONObject myArg = args.getJSONObject(0);
				int timeOut = 20;
				if(myArg.has("timeOut")) {
					timeOut = myArg.getInt("timeOut");
				}
				String url = myArg.getString("url");
				// "jdbc:jtds:sqlserver://server/database";
				String user = myArg.getString("user");// "user";
				String password = myArg.getString("password");// "pass";
				try {
					// Class.forName("com.mysql.jdbc.Driver");
					// Class.forName("net.sourceforge.jtds.jdbc.Driver");
					Class.forName(myArg.getString("class")).newInstance();
					DriverManager.setLoginTimeout(timeOut);
					con = DriverManager.getConnection(url, user, password);
				} catch (java.lang.ClassNotFoundException e) {
					callbackContext.error(e.getLocalizedMessage());
					return true;
				} catch (SQLException e) {
					callbackContext.error(e.getLocalizedMessage());
					return true;
				} catch (Exception e) {
					callbackContext.error(e.getLocalizedMessage());
					return true;
				}
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
