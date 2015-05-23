package com.suda.java_question;

import java.util.ArrayList;
import java.util.List;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBarActivity;
import android.util.TypedValue;
import android.view.ViewGroup;

import com.suda.java_question.db.DBOpenHelper;
import com.suda.java_question.util.CopyDatabase;
import com.suda.java_question.widget.PagerSlidingTabStrip;

@SuppressWarnings("deprecation")
public class Main extends ActionBarActivity {

	private DBOpenHelper dbOpenHelper = new DBOpenHelper(this);

	private PagerSlidingTabStrip mPagerSlidingTabStrip;
	private ViewPager mViewPager;
	private List<String> tagList = new ArrayList<String>();
	private FragmentManager fm;

	private String KEY_FIRST_IN = "isFirstIn";
	private SharedPreferences sharedPreferences;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_ui);

		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		
		if (isFirstIn()) {
			CopyDatabase.copyEmbassy2Databases(this,
					"data/data/com.suda.java_question/databases/",
					"java_question.db");
		}
		
		Editor editor = sharedPreferences.edit();
		editor.putBoolean(KEY_FIRST_IN, false);
		editor.commit();

		fm = getSupportFragmentManager();

		mPagerSlidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(new MyPagerAdapter(fm));
		mPagerSlidingTabStrip.setViewPager(mViewPager);
		mPagerSlidingTabStrip
				.setOnPageChangeListener(new OnPageChangeListener() {

					@Override
					public void onPageSelected(int arg0) {
						updateFrg(arg0);
					}

					@Override
					public void onPageScrolled(int arg0, float arg1, int arg2) {
					}

					@Override
					public void onPageScrollStateChanged(int arg0) {
					}
				});
		initTabsValue();

	}

	
	// 判断是否首次启动
	private boolean isFirstIn() {
		return sharedPreferences.getBoolean(KEY_FIRST_IN, true);
	}
	
	
	private void initTabsValue() {
		// 底部游标颜色
		// mPagerSlidingTabStrip.setIndicatorColor(Color.WHITE);

		// tab的分割线颜色
		// mPagerSlidingTabStrip.setDividerColor(Color.TRANSPARENT);
		// tab背景
		// mPagerSlidingTabStrip.setBackgroundColor(Color.parseColor("#6699FF"));

		mPagerSlidingTabStrip.setUnderlineHeight((int) TypedValue
				.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, getResources()
						.getDisplayMetrics()));

		// 均分每个标题
		mPagerSlidingTabStrip.setShouldExpand(true);

		// 游标高度
		mPagerSlidingTabStrip.setIndicatorHeight((int) TypedValue
				.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources()
						.getDisplayMetrics()));
		// 选中的文字颜色
		mPagerSlidingTabStrip.setSelectedTextColor(Color.WHITE);
		// 正常文字颜色
		// mPagerSlidingTabStrip.setTextColor(Color.BLACK);
	}

	public void updateFrg(int item) {
		Fragment fragment = fm.findFragmentByTag(tagList.get(item));
		if (fragment != null) {
			switch (item) {
			case 0:
				((AllQuestionFrg) fragment).refresh();
				break;
			case 1:
				((MarkQuestionFrg) fragment).refresh();

				break;

			default:
				break;
			}
		}
	}

	/* ***************FragmentPagerAdapter***************** */
	public class MyPagerAdapter extends FragmentPagerAdapter {

		private final String[] TITLES = { "全部题目", "标记题目" };

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		public Object instantiateItem(ViewGroup container, int position) {
			tagList.add("android:switcher:" + container.getId() + ":"
					+ getItemId(position));
			return super.instantiateItem(container, position);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return TITLES[position];
		}

		@Override
		public int getCount() {
			return TITLES.length;
		}

		@Override
		public Fragment getItem(int position) {

			switch (position) {
			case 0:
				return new AllQuestionFrg(dbOpenHelper);
			case 1:
				return new MarkQuestionFrg(dbOpenHelper);

			default:
				return null;
			}

		}

	}

}
