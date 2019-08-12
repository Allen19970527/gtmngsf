package com.example.gsf98.notebook;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.example.gsf98.notebook.adapter.EmojiAdapter;
import com.example.gsf98.notebook.bean.IconTag;
import com.example.gsf98.notebook.util.ActionBarHelper;
import com.example.gsf98.notebook.util.DensityUtil;
import com.example.gsf98.notebook.view.IconColorUtil;

import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class MoodActivity extends BaseActivity {
	private GridView weaGrid;
	private EmojiAdapter weaAdapter;
	private List<IconTag> weaNames;

	private GridView moodGrid;
	private EmojiAdapter moodAdapter;
	private List<IconTag> moodNames;

	private GridView tagGrid;
	private EmojiAdapter tagAdapter;
	private List<IconTag> tagNames;

	private int width;
	private float density;
	private ArrayList<IconTag> tagSelect;
	private IconTag moodSelect;
	private IconTag weaSelect;

	private boolean editable;
	private Drawable oldDraw;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mood);

		x.view().inject(this);
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		density = getResources().getDisplayMetrics().density;
		width = getResources().getDisplayMetrics().widthPixels;
		width = width - DensityUtil.dip2px(getApplicationContext(), 60);
		width = width / 5;

		editable = getIntent().getBooleanExtra("editable", false);

		initMoodGrid();
		initTagGrid();
		initWeaGrid();

		initShow();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (editable) {
			getMenuInflater().inflate(R.menu.menu_edit, menu);
		}

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		case R.id.menu_edit_save:
			save();
			finishok();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private void initShow() {
		moodSelect = (IconTag) getIntent().getParcelableExtra("moodSelect");
		weaSelect = (IconTag) getIntent().getParcelableExtra("weaSelect");
		tagSelect = new ArrayList<IconTag>();

		ArrayList<Parcelable> tmp = getIntent().getParcelableArrayListExtra("tagSelect");
		if (tmp != null) {
			for (Parcelable parcelable : tmp) {
				if (parcelable instanceof IconTag) {
					tagSelect.add((IconTag) parcelable);
				}
			}
		}

		if (moodSelect != null) {
			for (IconTag emojiBean : moodNames) {
				if (emojiBean.getText().equals(moodSelect.getText())) {
					emojiBean.setSelect(true);
				}
			}
		}

		if (weaSelect != null) {
			for (IconTag emojiBean : weaNames) {
				if (emojiBean.getText().equals(weaSelect.getText())) {
					emojiBean.setSelect(true);
				}
			}
		}

		if (tagSelect != null && tagSelect.size() > 0) {
			for (IconTag emojiBean1 : tagSelect) {
				for (IconTag emojiBean : tagNames) {
					if (emojiBean.getText().equals(emojiBean1.getText())) {
						emojiBean.setSelect(true);
					}
				}
			}
		}

		moodAdapter.notifyDataSetChanged();
		weaAdapter.notifyDataSetChanged();
		tagAdapter.notifyDataSetChanged();

		setActionBarColor();
	}

	private void setActionBarColor() {
		save();

		List<IconTag> all = new ArrayList<IconTag>();
		if (moodSelect != null) {
			all.add(moodSelect);
		}

		if (weaSelect != null) {
			all.add(weaSelect);
		}

		if (tagSelect != null && tagSelect.size() > 0) {
			all.addAll(tagSelect);
		}

		if (all.size() > 0) {
			oldDraw = ActionBarHelper.setColor(getApplicationContext(), getActionBar(), oldDraw, getApplicationContext().getResources().getColor(IconColorUtil.getFaMap().get(all.get(0).getText())[0]),
					drawableCallback);
		} else {
			oldDraw = ActionBarHelper.setColor(getApplicationContext(), getActionBar(), oldDraw, getApplicationContext().getResources().getColor(R.color.clr_tag0), drawableCallback);
		}
	}

	private final Handler handler = new Handler();
	private Drawable.Callback drawableCallback = new Drawable.Callback() {
		@Override
		public void invalidateDrawable(Drawable who) {
			getActionBar().setBackgroundDrawable(who);
		}

		@Override
		public void scheduleDrawable(Drawable who, Runnable what, long when) {
			handler.postAtTime(what, when);
		}

		@Override
		public void unscheduleDrawable(Drawable who, Runnable what) {
			handler.removeCallbacks(what);
		}
	};

	private void initMoodGrid() {
		moodGrid = (GridView) findViewById(R.id.mood_grid_mood);
		moodGrid.setSelector(new ColorDrawable(Color.TRANSPARENT));
		moodGrid.setStretchMode(GridView.NO_STRETCH);

		moodGrid.setColumnWidth(width);

		moodNames = new ArrayList<IconTag>();
		moodNames.add(new IconTag("fb-happy"));
		moodNames.add(new IconTag("fb-sad"));
		moodNames.add(new IconTag("fb-cool"));
		moodNames.add(new IconTag("fb-wondering"));
		moodNames.add(new IconTag("fb-angry"));
		moodAdapter = new EmojiAdapter(this, moodNames, 1);
		moodAdapter.setTextSize(width / 2 / (int) density);
		moodGrid.setAdapter(moodAdapter);

		if (editable) {
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
					setActionBarColor();
				}
			});
		}
	}

	private void initWeaGrid() {
		weaGrid = (GridView) findViewById(R.id.mood_grid_weather);
		weaGrid.setSelector(new ColorDrawable(Color.TRANSPARENT));
		weaGrid.setStretchMode(GridView.NO_STRETCH);

		weaGrid.setColumnWidth(width);

		weaNames = new ArrayList<IconTag>();
		weaNames.add(new IconTag("fb-sun-o"));
		weaNames.add(new IconTag("fb-cloudy-o"));
		weaNames.add(new IconTag("fb-rainy-o"));
		weaNames.add(new IconTag("fb-snowy-o"));
		weaNames.add(new IconTag("fb-lightning-o"));
		weaAdapter = new EmojiAdapter(this, weaNames, 1);
		weaAdapter.setTextSize(width / 2 / (int) density);
		weaGrid.setAdapter(weaAdapter);

		if (editable) {
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
					setActionBarColor();
				}
			});
		}
	}

	private void initTagGrid() {
		tagGrid = (GridView) findViewById(R.id.mood_grid_tag);
		tagGrid.setSelector(new ColorDrawable(Color.TRANSPARENT));
		tagGrid.setStretchMode(GridView.NO_STRETCH);

		tagGrid.setColumnWidth(width);

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
		tagAdapter = new EmojiAdapter(this, tagNames, 1);
		tagAdapter.setTextSize(width / 2 / (int) density);
		tagGrid.setAdapter(tagAdapter);

		if (editable) {
			tagGrid.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
					tagNames.get(arg2).switchSelect();
					tagAdapter.notifyDataSetChanged();
					setActionBarColor();
				}
			});
		}
	}

	private void save() {
		moodSelect = null;
		for (IconTag emojiBean : moodNames) {
			if (emojiBean.isSelect()) {
				moodSelect = emojiBean;
			}
		}

		weaSelect = null;
		for (IconTag emojiBean : weaNames) {
			if (emojiBean.isSelect()) {
				weaSelect = emojiBean;
			}
		}

		tagSelect = new ArrayList<IconTag>();
		for (IconTag emojiBean : tagNames) {
			if (emojiBean.isSelect()) {
				tagSelect.add(emojiBean);
			}
		}
	}

	private void finishok() {
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putParcelable("moodSelect", moodSelect);
		bundle.putParcelable("weaSelect", weaSelect);
		bundle.putParcelableArrayList("tagSelect", tagSelect);
		intent.putExtras(bundle);

		setResult(RESULT_OK, intent);
		finish();
	}
}
