package com.example.gsf98.notebook;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.kbeanie.imagechooser.api.FileUtils;
import com.example.gsf98.notebook.adapter.EmojiAdapter;
import com.example.gsf98.notebook.adapter.MenuRightAdapter;
import com.example.gsf98.notebook.bean.IconTag;
import com.example.gsf98.notebook.bean.MonthBean;
import com.example.gsf98.notebook.entity.Tag;
import com.example.gsf98.notebook.frag.ListFrag;
import com.example.gsf98.notebook.logic.Logic;
import com.example.gsf98.notebook.util.Constant;
import com.example.gsf98.notebook.util.DensityUtil;
import com.example.gsf98.notebook.util.FileHelper;
import com.example.gsf98.notebook.util.ZipUtil;
import com.example.gsf98.notebook.view.KenBurnsView;
import com.umeng.analytics.MobclickAgent;

import org.xutils.ex.DbException;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class FragChangeActivity extends FragmentActivity {
	private CharSequence mTitle;
	private CharSequence mDrawerTitle;

	@ViewInject(R.id.main_layout)
	private DrawerLayout mDrawerLayout;

	@ViewInject(R.id.main_left_layout)
	private LinearLayout mDrawerLeft;

	private ActionBarDrawerToggle mDrawerToggle;
	private Vibrator vibrator;
	private ListFrag frag;

	// left start

	@ViewInject(value = R.id.menu_left_kenburns)
	private KenBurnsView kenBurnsView;

	@ViewInject(value = R.id.menu_left_grid_type)
	private GridView typeGrid;
	private EmojiAdapter typeAdapter;
	private List<IconTag> typeNames;

	@ViewInject(value = R.id.menu_left_grid_weather)
	private GridView weaGrid;
	private EmojiAdapter weaAdapter;
	private List<IconTag> weaNames;

	@ViewInject(value = R.id.menu_left_grid_mood)
	private GridView moodGrid;
	private EmojiAdapter moodAdapter;
	private List<IconTag> moodNames;

	@ViewInject(value = R.id.menu_left_grid_tag)
	private GridView tagGrid;
	private EmojiAdapter tagAdapter;
	private List<IconTag> tagNames;

	@ViewInject(value = R.id.menu_left_counta)
	private TextView txtCounta;

	@ViewInject(value = R.id.menu_left_countb)
	private TextView txtCountb;

	@ViewInject(value = R.id.menu_left_countc)
	private TextView txtCountc;

	private float density;
	private int width;

	// left end

	// right start

	@ViewInject(R.id.main_right_layout)
	private LinearLayout mDrawerRight;

	@ViewInject(value = R.id.menu_right_list)
	private ListView lstTags;

	@ViewInject(value = R.id.menu_right_tab_tag)
	private TextView tabTag;

	@ViewInject(value = R.id.menu_right_tab_month)
	private TextView tabMonth;

	private SimpleDateFormat format;
	private List<Tag> tags;
	private List<MonthBean> months;
	private MenuRightAdapter adapter;
	private int type;
	private long oldest;

	// right end

	@Override
	protected void onStop() {
		if (null != vibrator) {
			vibrator.cancel();
		}

		super.onStop();
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);

		try {
			refreshLeft();
			refreshRight();
		} catch (Exception e) {
		}
	}

	private void refreshRight() throws DbException {
		oldest = Logic.getInstance(getApplicationContext()).getOldest();
		calMonths();

		tags.clear();
		tags.addAll(Logic.getInstance(getApplicationContext()).searchTags());

		adapter.setType(type);
		adapter.notifyDataSetChanged();
	}

	private void refreshLeft() {
		long a = Logic.getInstance(this).getDiaryCount();
		long b = Logic.getInstance(this).getPhotoCount();
		long c = Logic.getInstance(this).getAudioCount();
		txtCounta.setText(a + getString(R.string.type_diary));
		txtCountb.setText(b + getString(R.string.type_photo));
		txtCountc.setText(c + getString(R.string.type_audio));
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		x.view().inject(this);

		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		density = getResources().getDisplayMetrics().density;

		kenBurnsView.setResourceIds(R.drawable.picture0, R.drawable.picture1);

		mTitle = mDrawerTitle = getTitle();

		frag = new ListFrag();
		vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		mDrawerLeft.setLayoutParams(new DrawerLayout.LayoutParams(DensityUtil.getWidth(this) * 3 / 4, LayoutParams.MATCH_PARENT, Gravity.LEFT));
		mDrawerRight.setLayoutParams(new DrawerLayout.LayoutParams(DensityUtil.getWidth(this) * 3 / 4, LayoutParams.MATCH_PARENT, Gravity.END));

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_action_drawer, R.string.drawer_open, R.string.drawer_close) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu();
				vibrate();
			}
		};

		mDrawerLayout.setDrawerListener(mDrawerToggle);

		initLeft();
		initRight();

		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					if (typeGrid.getWidth() > 0) {
						width = typeGrid.getWidth();
						width = width - DensityUtil.dip2px(FragChangeActivity.this, 40);

						typeGrid.setColumnWidth(width / 5);
						typeAdapter.setTextSize(width / 5 / 2 / (int) density);
						typeAdapter.notifyDataSetChanged();

						weaGrid.setColumnWidth(width / 5);
						weaAdapter.setTextSize(width / 5 / 2 / (int) density);
						weaAdapter.notifyDataSetChanged();

						moodGrid.setColumnWidth(width / 5);
						moodAdapter.setTextSize(width / 5 / 2 / (int) density);
						moodAdapter.notifyDataSetChanged();

						tagGrid.setColumnWidth(width / 5);
						tagAdapter.setTextSize(width / 5 / 2 / (int) density);
						tagAdapter.notifyDataSetChanged();
						break;
					}
				}
			}
		}).start();

		showFrag();

		// UmengUpdateAgent.update(this);
		//
		// Intent intent = new Intent();
		// PendingIntent pi = PendingIntent.getBroadcast( this, 0, intent, 0 );
		// AlarmManager am = (AlarmManager)getSystemService( ALARM_SERVICE );
		// am.set( AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pi );
		//
		// NotifyUtils.setNotification( getApplicationContext() );
		// TODO
		// Intent i = getIntent();
		// if( i != null )
		// {
		// boolean isNotify = i.getBooleanExtra( "isNotify", false );
		// if( isNotify )
		// {
		// getListFrag().intentText( null );
		// }
		// }
	}

	private void initLeft() {
		initTypeGrid();
		initWeaGrid();
		initMoodGrid();
		initTagGrid();
	}

	private void showFrag() {
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

		if (frag.isAdded()) {
			fragmentTransaction.show(frag);
		} else {
			fragmentTransaction.add(R.id.content_frame, frag);
		}

		fragmentTransaction.commit();
	}

	private void vibrate() {
		vibrator.vibrate(10);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		switch (item.getItemId()) {
		case R.id.menu_main_comment:
			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.liuzb.moodiary")));
			return true;
		case R.id.menu_main_export:
			export();
			break;
		case R.id.menu_main_impoort:
			imports();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void export() {
		try {
			String DATABASE_NAME = "xUtils.db";

			String oldPath = "data/data/com.liuzb.moodiary/databases/" + DATABASE_NAME;
			String newPath = FileUtils.getDirectory(Constant.SD_PATH) + File.separator + DATABASE_NAME;

			FileHelper.copy(newPath, oldPath);

			ZipUtil.zip(FileUtils.getDirectory(Constant.SD_PATH), FileUtils.getDirectory("mood_bak.zip"));

			AlertDialog.Builder builder = new Builder(FragChangeActivity.this);
			builder.setMessage("导出的文件存放于SD卡根目录的mood_bak.zip文件，请妥善保管此文件。");
			builder.setTitle("导出成功");
			builder.setPositiveButton("确认", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			builder.create().show();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void imports() {
		try {
			ZipUtil.unzip(FileUtils.getDirectory("mood_bak.zip"), FileUtils.getDirectory(Constant.SD_PATH));

			String DATABASE_NAME = "xUtils.db";

			String oldPath = "data/data/com.liuzb.moodiary/databases/" + DATABASE_NAME;
			String newPath = FileUtils.getDirectory(Constant.SD_PATH) + File.separator + DATABASE_NAME;

			FileHelper.copy(oldPath, newPath);

			AlertDialog.Builder builder = new Builder(FragChangeActivity.this);
			builder.setMessage("导入数据成功，请重启启动以正确显示数据。");
			builder.setTitle("导入成功");
			builder.setPositiveButton("重启", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					restartApplication();
				}
			});
			builder.setNegativeButton("取消", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			builder.create().show();
		} catch (IOException e) {
			AlertDialog.Builder builder = new Builder(FragChangeActivity.this);
			builder.setMessage("请确保mood_bak.zip文件存放于SD卡根目录。");
			builder.setTitle("导入失败");
			builder.setPositiveButton("确认", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			builder.create().show();
		}
	}

	private void restartApplication() {
		final Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	public void setCustomTitle(String text) {
		setTitle(text);
	}

	// left start
	private void initTypeGrid() {
		typeGrid.setSelector(new ColorDrawable(Color.TRANSPARENT));
		typeGrid.setStretchMode(GridView.NO_STRETCH);

		typeNames = new ArrayList<IconTag>();
		typeNames.add(new IconTag("fb-file"));
		typeNames.add(new IconTag("fb-image"));
		typeNames.add(new IconTag("fb-location"));
		// typeNames.add( new IconTag( "fb-camera2" ) );
		typeNames.add(new IconTag("fb-bullhorn"));
		typeAdapter = new EmojiAdapter(this, typeNames, 0);
		typeGrid.setAdapter(typeAdapter);

		typeGrid.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				if (arg2 == 0) {
					for (int i = 1; i < typeNames.size(); i++) {
						typeNames.get(i).setSelect(false);
					}
				} else {
					typeNames.get(0).setSelect(false);
				}

				typeNames.get(arg2).switchSelect();
				typeAdapter.notifyDataSetChanged();

				updateMain();
			}
		});
	}

	private void initMoodGrid() {
		moodGrid.setSelector(new ColorDrawable(Color.TRANSPARENT));
		moodGrid.setStretchMode(GridView.NO_STRETCH);

		moodNames = new ArrayList<IconTag>();
		moodNames.add(new IconTag("fb-happy"));
		moodNames.add(new IconTag("fb-sad"));
		moodNames.add(new IconTag("fb-cool"));
		moodNames.add(new IconTag("fb-wondering"));
		moodNames.add(new IconTag("fb-angry"));
		moodAdapter = new EmojiAdapter(this, moodNames, 0);
		moodGrid.setAdapter(moodAdapter);

		moodGrid.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				for (int i = 0; i < moodNames.size(); i++) {
					if (i != arg2) {
						moodNames.get(i).setSelect(false);
					}
				}

				moodNames.get(arg2).switchSelect();
				moodAdapter.notifyDataSetChanged();

				updateMain();
			}
		});
	}

	private void initWeaGrid() {
		weaGrid.setSelector(new ColorDrawable(Color.TRANSPARENT));
		weaGrid.setStretchMode(GridView.NO_STRETCH);

		weaNames = new ArrayList<IconTag>();
		weaNames.add(new IconTag("fb-sun-o"));
		weaNames.add(new IconTag("fb-cloudy-o"));
		weaNames.add(new IconTag("fb-rainy-o"));
		weaNames.add(new IconTag("fb-snowy-o"));
		weaNames.add(new IconTag("fb-lightning-o"));
		weaAdapter = new EmojiAdapter(this, weaNames, 0);
		weaGrid.setAdapter(weaAdapter);

		weaGrid.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				for (int i = 0; i < weaNames.size(); i++) {
					if (i != arg2) {
						weaNames.get(i).setSelect(false);
					}
				}

				weaNames.get(arg2).switchSelect();
				weaAdapter.notifyDataSetChanged();

				updateMain();
			}
		});
	}

	private void initTagGrid() {
		tagGrid.setSelector(new ColorDrawable(Color.TRANSPARENT));
		tagGrid.setStretchMode(GridView.NO_STRETCH);

		tagNames = new ArrayList<IconTag>();
		tagNames.add(new IconTag("fb-headphones"));
		tagNames.add(new IconTag("fb-book"));
		tagNames.add(new IconTag("fb-cart"));
		tagNames.add(new IconTag("fb-hammer"));
		tagNames.add(new IconTag("fb-bug"));
		tagNames.add(new IconTag("fb-trophy"));
		tagNames.add(new IconTag("fb-food"));
		tagNames.add(new IconTag("fb-mug"));
		tagNames.add(new IconTag("fb-airplane"));
		tagNames.add(new IconTag("fb-trunk"));
		tagNames.add(new IconTag("fb-briefcase"));
		tagNames.add(new IconTag("fb-accessibility"));
		tagNames.add(new IconTag("fb-star"));
		tagNames.add(new IconTag("fb-heart"));
		tagAdapter = new EmojiAdapter(this, tagNames, 0);
		tagGrid.setAdapter(tagAdapter);

		tagGrid.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				tagNames.get(arg2).switchSelect();
				tagAdapter.notifyDataSetChanged();

				updateMain();
			}
		});
	}

	public void clearStatus() {
		for (IconTag emojiBean : typeNames) {
			emojiBean.setSelect(false);
		}

		for (IconTag emojiBean : weaNames) {
			emojiBean.setSelect(false);
		}

		for (IconTag emojiBean : moodNames) {
			emojiBean.setSelect(false);
		}

		for (IconTag emojiBean : tagNames) {
			emojiBean.setSelect(false);
		}

		typeAdapter.notifyDataSetChanged();
		weaAdapter.notifyDataSetChanged();
		moodAdapter.notifyDataSetChanged();
		tagAdapter.notifyDataSetChanged();
	}

	private void updateMain() {
		List<IconTag> type = new ArrayList<IconTag>();
		List<IconTag> re = new ArrayList<IconTag>();

		for (IconTag emojiBean : typeNames) {
			if (emojiBean.isSelect()) {
				type.add(emojiBean);
			}
		}

		for (IconTag emojiBean : weaNames) {
			if (emojiBean.isSelect()) {
				re.add(emojiBean);
			}
		}

		for (IconTag emojiBean : moodNames) {
			if (emojiBean.isSelect()) {
				re.add(emojiBean);
			}
		}

		for (IconTag emojiBean : tagNames) {
			if (emojiBean.isSelect()) {
				re.add(emojiBean);
			}
		}

		frag.filtByLeftMenu(type, re);
	}

	// right start
	private void initRight() {
		tabTag.setTextColor(getResources().getColor(android.R.color.white));

		format = new SimpleDateFormat("yyyy/MM", Locale.ENGLISH);
		tags = new ArrayList<Tag>();
		months = new ArrayList<MonthBean>();
		adapter = new MenuRightAdapter(getApplicationContext(), tags, months, type);
		lstTags.setAdapter(adapter);
		lstTags.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				click(position);
			}
		});
	}

	@Event(value = R.id.menu_right_tab_tag)
	private void clickTagEvent(View v) {
		type = 0;
		tabTag.setTextColor(getResources().getColor(android.R.color.white));
		tabMonth.setTextColor(getResources().getColor(R.color.clr_text_front));

		adapter.setType(type);
		adapter.notifyDataSetChanged();
	}

	@Event(value = R.id.menu_right_tab_month)
	private void clickMonthEvent(View v) {
		type = 1;
		tabTag.setTextColor(getResources().getColor(R.color.clr_text_front));
		tabMonth.setTextColor(getResources().getColor(android.R.color.white));

		adapter.setType(type);
		adapter.notifyDataSetChanged();
	}

	private void calMonths() {
		months.clear();
		if (oldest > 0) {
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(oldest);
			int yc = c.get(Calendar.YEAR);
			int mc = c.get(Calendar.MONTH);

			Calendar now = Calendar.getInstance();
			int yn = now.get(Calendar.YEAR);
			int mn = now.get(Calendar.MONTH);

			int odd = (yn - yc) * 12 + (mn - mc) + 1;

			now.add(Calendar.MONTH, 1);
			for (int i = 0; i < odd; i++) {
				now.add(Calendar.MONTH, -1);
				MonthBean month = new MonthBean();
				month.setContent(format.format(now.getTime()));
				months.add(month);
			}
		}
	}

	private void click(int position) {
		if (type == 0) {
			updateMain(tags.get(position));
		} else {
			updateMain(months.get(position));
		}
	}

	private void updateMain(MonthBean month) {
		frag.filtByMonth(month);
	}

	private void updateMain(Tag tag) {
		frag.filtByTag(tag);
	}

	// rigt end
}