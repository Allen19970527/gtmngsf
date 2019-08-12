package com.example.gsf98.notebook;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gsf98.notebook.bean.IconTag;
import com.example.gsf98.notebook.entity.Attach;
import com.example.gsf98.notebook.entity.Diary;
import com.example.gsf98.notebook.logic.Logic;
import com.example.gsf98.notebook.util.ActionBarHelper;
import com.example.gsf98.notebook.util.AttachHelper;
import com.example.gsf98.notebook.view.IconColorUtil;
import com.example.gsf98.notebook.view.IconTextView;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DetailActivity extends BaseActivity {
	private Diary diary;

	@ViewInject(value = R.id.detail_date)
	private TextView txtDate;

	@ViewInject(value = R.id.detail_content)
	private TextView txtContent;

	@ViewInject(value = R.id.detail_attach_lay)
	private LinearLayout layAttach;

	@ViewInject(value = R.id.detail_tags_lay)
	private LinearLayout layTag;

	@ViewInject(value = R.id.detail_tags_content)
	private TextView txtTags;

	@ViewInject(value = R.id.detail_icon_lay)
	private LinearLayout layIcon;

	@ViewInject(value = R.id.detail_icon_content)
	private LinearLayout layIconContent;

	private LayoutInflater inflater;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.diary_detail);

		x.view().inject(this);
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		inflater = LayoutInflater.from(getApplicationContext());
	}

	@Override
	protected void onResume() {
		super.onResume();

		diary = Logic.getInstance(getApplicationContext()).searchDiaryByID(getIntent().getIntExtra("key", 0));
		if (diary == null) {
			Toast.makeText(getApplicationContext(), "错误", Toast.LENGTH_SHORT).show();
			finish();
		} else {
			showData();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_detail, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		case R.id.menu_detail_edit:
			edit();
			return true;
		case R.id.menu_detail_delete:
			delete();
			return true;
		// case R.id.menu_detail_share:
		// share();
		// return true;
		}

		return super.onOptionsItemSelected(item);
	}

	// private void share()
	// {
	// Intent intent = new Intent( Intent.ACTION_SEND );
	// intent.setType( "image/*" );
	// intent.putExtra( Intent.EXTRA_SUBJECT, diary.getTitle() );
	// intent.putExtra( Intent.EXTRA_TEXT, diary.getContent() );
	//
	// intent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
	// startActivity( Intent.createChooser( intent, getTitle() ) );
	// }

	private void edit() {
		Intent intent = new Intent(getApplicationContext(), EditActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("key", diary.getId() + "");
		intent.putExtras(bundle);
		startActivity(intent);
	}

	private void delete() {
		Logic.getInstance(getApplicationContext()).deleteDiaryByID(diary.getId());
		finish();
	}

	private void showData() {
		getActionBar().setTitle(diary.getTitle());

		txtContent.setText(diary.getContent());
		txtDate.setText(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.ENGLISH).format(diary.getDate()));

		showMood();
		showTag();

		layAttach.removeAllViews();
		if (diary.getAttaches() != null && diary.getAttaches().size() > 0) {
			for (Attach attach : diary.getAttaches()) {
				showAttach(attach);
			}
		}

		// if( diary.getAttaches() != null && diary.getAttaches().size() > 0 )
		// {
		// for( Attach attach : diary.getAttaches() )
		// {
		// if( attach.getType() == 2 )
		// {
		// layGps.setVisibility( View.VISIBLE );
		// txtGps.setText( attach.getDescription() );
		// }
		//
		// if( attach.getType() == 0 )
		// {
		// LayoutInflater inflater = LayoutInflater.from(
		// getApplicationContext() );
		// View v = inflater.inflate( R.layout.diary_attach_detail_item, null );
		// ImageView i = (ImageView)v.findViewById(
		// R.id.diary_attach_detail_item_logo );
		// TextView t = (TextView)v.findViewById(
		// R.id.diary_attach_detail_item_text );
		// bitmapUtils.display( i, AttachHelper.parseOrgThumb( attach.getName()
		// ) );
		// t.setText( attach.getDescription() );
		// layAttach.addView( v );
		// }
		// }
		// }
	}

	private void showMood() {
		int color = R.color.clr_tag0;

		List<IconTag> all = new ArrayList<IconTag>();
		if (diary.getMood() != null) {
			all.add(diary.getMood());
		}

		if (diary.getWeather() != null) {
			all.add(diary.getWeather());
		}

		if (diary.getIconTags() != null) {
			all.addAll(diary.getIconTags());
		}

		if (all.size() > 0) {
			color = IconColorUtil.getFaMap().get(all.get(0).getText())[0];
			layIcon.setVisibility(View.VISIBLE);

			layIconContent.removeAllViews();
			for (int i = 0; i < all.size(); i++) {
				IconTag iconTag = all.get(i);
				if (iconTag != null) {
					View v = inflater.inflate(R.layout.diary_item_icon, null);
					IconTextView iconTextView = (IconTextView) v.findViewById(R.id.diary_item_icon);
					iconTextView.setIcon(all.get(i).getText());
					iconTextView.setBackgroundColor(getResources().getColor(IconColorUtil.getFaMap().get(iconTag.getText())[0]));
					layIconContent.addView(iconTextView);
				}
			}

			setActionBarColor(color);
		} else {
			layIcon.setVisibility(View.GONE);
		}
	}

	private void showTag() {
		String tags = diary.retriveTags();
		if (TextUtils.isEmpty(tags)) {
			layTag.setVisibility(View.GONE);
		} else {
			layTag.setVisibility(View.VISIBLE);
			txtTags.setText(tags);
		}
	}

	private void showAttach(final Attach attach) {
		View sep = inflater.inflate(R.layout.diary_attach_item_sep, null);
		View v = inflater.inflate(R.layout.diary_attach_show_item, null);

		ImageView i1 = (ImageView) v.findViewById(R.id.diary_attach_show_item_logo);
		IconTextView i2 = (IconTextView) v.findViewById(R.id.diary_attach_show_item_logo2);
		TextView t1 = (TextView) v.findViewById(R.id.diary_attach_show_item_text);

		if (attach.getType() == 0) {
			x.image().bind(i1, "file://" + AttachHelper.parseSmallThumb(attach.getName()));
			i2.setVisibility(View.GONE);
			i1.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent();
					intent.setAction(Intent.ACTION_VIEW);
					intent.setDataAndType(Uri.fromFile(new File(AttachHelper.parseOrgThumb(attach.getName()))), "image/*");
					startActivity(intent);
				}
			});
		} else if (attach.getType() == 1) {
			i2.setIcon("fb-bullhorn");
			i1.setVisibility(View.GONE);
			i2.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.setAction(Intent.ACTION_VIEW);
					intent.setDataAndType(Uri.fromFile(new File(AttachHelper.parseAudio(attach.getName()))), "audio/*");
					startActivity(intent);
				}
			});
		} else if (attach.getType() == 2) {
			i2.setIcon("fb-location");
			i1.setVisibility(View.GONE);
			i2.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.setAction(Intent.ACTION_VIEW);
					intent.setData(Uri.parse("geo:" + attach.getName()));
					startActivity(intent);
				}
			});
		}

		t1.setText(attach.getDescription());
		layAttach.addView(sep);
		layAttach.addView(v);
	}

	@Event(value = R.id.detail_icon_lay)
	private void intentMoodEvent(View v) {
		Intent intent = new Intent(getApplicationContext(), MoodActivity.class);
		Bundle bundle = new Bundle();
		bundle.putBoolean("editable", false);
		bundle.putParcelable("moodSelect", diary.getMood());
		bundle.putParcelable("weaSelect", diary.getWeather());
		bundle.putParcelableArrayList("tagSelect", diary.getIconTags());
		intent.putExtras(bundle);
		startActivity(intent);
	}

	private void setActionBarColor(int color) {
		if (color == 0)
			color = R.color.clr_tag0;
		ActionBarHelper.setColor(getApplicationContext(), getActionBar(), getResources().getColor(color), drawableCallback);
		// ShapeDrawable normal = new ShapeDrawable( new RectShape() );
		// normal.getPaint().setColor( getResources().getColor( color ) );
		// getSupportActionBar().setBackgroundDrawable( normal );
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
}
