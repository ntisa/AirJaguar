package org.akop.airjag;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class LevelSelectorActivity extends Activity {

	private static class Map {
		public long mId;
		public String mName;
	}

	private List<Map> mMap;
	private MapAdapter mAdapter;

	private ListView mListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_level_selector);

		mMap = new ArrayList<Map>();
		mAdapter = new MapAdapter();

		mListView = (ListView)findViewById(R.id.list_view);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Map map = mMap.get(arg2);
				loadLevel(map.mId);
			}
		});

		loadLevelList();
	}

	private void loadLevel(final long id) {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				String data = download("http://10.0.1.193:8080/get?id=" + id);

				Log.v("*", " ** " + data);
//				JSONArray items;
//
//				try {
//					items = new JSONArray(data);
//				} catch (JSONException e) {
//					throw new RuntimeException(e);
//				}
//
//				for (int i = 0, n = items.length(); i < n; i++) {
//					JSONObject obj = items.optJSONObject(i);
//					if (obj != null) {
//						Map map = new Map();
//						map.mId = obj.optLong("ID", 0);
//						map.mName = obj.optString("Name");
//						mMap.add(map);
//					}
//				}
//
//				runOnUiThread(new Runnable() {
//					public void run() {
//						mAdapter.notifyDataSetChanged();
//					}
//				});
			}
		});
		t.start();
	}

	private void loadLevelList() {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				String data = download("http://10.0.1.193:8080/list");
				JSONArray items;

				try {
					items = new JSONArray(data);
				} catch (JSONException e) {
					throw new RuntimeException(e);
				}

				for (int i = 0, n = items.length(); i < n; i++) {
					JSONObject obj = items.optJSONObject(i);
					if (obj != null) {
						Map map = new Map();
						map.mId = obj.optLong("ID", 0);
						map.mName = obj.optString("Name");
						mMap.add(map);
					}
				}

				runOnUiThread(new Runnable() {
					public void run() {
						mAdapter.notifyDataSetChanged();
					}
				});
			}
		});
		t.start();
	}

	private String download(String url) {
		String content = null;
		DefaultHttpClient client = new DefaultHttpClient();
		URI uri = URI.create(url);
		HttpGet request = new HttpGet(uri);

		try {
			HttpContext context = new BasicHttpContext();
			HttpResponse response = client.execute(request, context);
			HttpEntity entity = response.getEntity();

			if (entity != null) {
				InputStream stream = entity.getContent();
				StringBuilder builder;

				try {
					BufferedReader reader = new BufferedReader(new InputStreamReader(stream), 10000);
					builder = new StringBuilder(10000);

					int read;
					char[] buffer = new char[1000];

					while ((read = reader.read(buffer)) >= 0)
						builder.append(buffer, 0, read);
				} finally {
					stream.close();
				}

				entity.consumeContent();
				content = builder.toString();
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return content;
	}

	public static void launch(Context context) {
		Intent intent = new Intent(context, LevelSelectorActivity.class);
		context.startActivity(intent);
	}

	private class MapAdapter extends BaseAdapter {

		private class ViewHolder {
			TextView mName;
		}

		@Override
		public int getCount() {
			return mMap.size();
		}

		@Override
		public Object getItem(int position) {
			return mMap.get(position);
		}

		@Override
		public long getItemId(int position) {
			return mMap.get(position).mId;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder vh;
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(R.layout.template_level, null);
				vh = new ViewHolder();
				vh.mName = (TextView)convertView.findViewById(R.id.name);
				convertView.setTag(vh);
			} else {
				vh = (ViewHolder)convertView.getTag();
			}

			vh.mName.setText(mMap.get(position).mName);

			return convertView;
		}
	}
}
