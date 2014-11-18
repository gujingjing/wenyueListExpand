package wenyue.expandlistview.me;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cd.R;

/**
 * 2014年十一月十八日
 * 苍狼问月
 * canglangwenyue.github.io
 * @author canglangwenyue
 *
 */
public class MainActivity extends Activity {

	private final List<String> mList = new ArrayList<String>();
	private ListView myListView;
	private CustomerListAdapter myAdapter;
	private ViewHolder mLastTouchTag = null;

	private int mLcdWidth = 0;
	private float mDensity = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		getSystemInfo();

		myAdapter = new CustomerListAdapter(this);
		myListView = (ListView) findViewById(R.id.lvMain);
		myListView.setAdapter(myAdapter);
		myListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int arg2,
					long arg3) {
				if (mLastTouchTag != null) {
					View temp = arg0.findViewWithTag(mLastTouchTag);
					if (temp != null) {
						View footTemp = temp.findViewById(R.id.footer);
						if (footTemp != null
								&& (footTemp.getVisibility() != View.GONE)) {
							footTemp.startAnimation(new ViewExpandAnimation(
									footTemp));
						}
					}
				}
				mLastTouchTag = (ViewHolder) v.getTag();
				// onion555 end
				View footer = v.findViewById(R.id.footer);
				footer.startAnimation(new ViewExpandAnimation(footer));
			}
		});

		addItems();

	}

	private void getSystemInfo() {
		DisplayMetrics dm = getResources().getDisplayMetrics();
		mLcdWidth = dm.widthPixels;
		mDensity = dm.density;
	}

	private void addItems() {
		for (int i = 0; i < 10; i++) {

			mList.add("wenyue's item" + i);
		}
		myAdapter.notifyDataSetChanged();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public class ViewHolder {
		private TextView tvName;

		public TextView getTvName() {
			return tvName;
		}

		public void setTvName(TextView tvName) {
			this.tvName = tvName;
		}

	}

	public class CustomerListAdapter extends BaseAdapter {

		private final LayoutInflater myInflater;

		public CustomerListAdapter(Context context) {
			myInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (getCount() == 0) {
				return null;
			}
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = myInflater.inflate(R.layout.expand_item, null);

				holder = new ViewHolder();
				holder.tvName = (TextView) convertView
						.findViewById(R.id.tvName);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.tvName.setText(mList.get(position));

			int btnWidth = (int) ((mLcdWidth - 20 - 10 * mDensity) / 3);
			RelativeLayout.LayoutParams lp = null;

			final Button buttonOpen = (Button) convertView
					.findViewById(R.id.btnOpen);
			lp = (RelativeLayout.LayoutParams) buttonOpen.getLayoutParams();
			lp.width = btnWidth;
			buttonOpen.setLayoutParams(lp);
			buttonOpen.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Toast.makeText(MainActivity.this, "编辑", Toast.LENGTH_LONG)
							.show();
				}
			});

			final Button startButton = (Button) convertView
					.findViewById(R.id.btnView);
			lp = (RelativeLayout.LayoutParams) startButton.getLayoutParams();
			lp.width = btnWidth;
			lp.leftMargin = 10;
			startButton.setLayoutParams(lp);
			startButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Toast.makeText(MainActivity.this, "开始", Toast.LENGTH_LONG)
							.show();
				}
			});

			final Button deletButton = (Button) convertView
					.findViewById(R.id.btnWarning);
			lp = (RelativeLayout.LayoutParams) deletButton.getLayoutParams();
			lp.width = btnWidth;
			lp.leftMargin = 10;
			deletButton.setLayoutParams(lp);
			deletButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Toast.makeText(MainActivity.this, "删除", Toast.LENGTH_LONG)
							.show();
				}
			});

			/**
			 * 省略掉其他操作，使用者可以自行扩展，例如长按操作，弹出自定义Toast等等。
			 */
			
			//自行添加。。。
			
			/**
			 * getHeight()取到的是view的实际高度，是最终显示出来的高度；
			 * getMeasuredHeight()取到的是最后一次调用measure方法后得到的高度，不一定是最终高度；
			 * 通常在创建自定义视图组件时会用到getMeasuredHeight
			 * ()，比如在onLayout(),onMeasure()等方法中。（官方文档：This should be used during
			 * measurement and layout calculations only.）
			 */
			RelativeLayout footer = (RelativeLayout) convertView
					.findViewById(R.id.footer);
			int widthSpec = MeasureSpec.makeMeasureSpec(
					(int) (mLcdWidth - 10 * mDensity), MeasureSpec.EXACTLY);
			footer.measure(widthSpec, 0);
			LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) footer
					.getLayoutParams();
			params.bottomMargin = -footer.getMeasuredHeight();
			footer.setVisibility(View.GONE);

			return convertView;
		}

	}

}
