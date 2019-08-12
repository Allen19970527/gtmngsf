package com.example.gsf98.notebook;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.kbeanie.imagechooser.api.ChooserType;
import com.kbeanie.imagechooser.api.ChosenImage;
import com.kbeanie.imagechooser.api.FileUtils;
import com.kbeanie.imagechooser.api.ImageChooserListener;
import com.kbeanie.imagechooser.api.ImageChooserManager;
import com.example.gsf98.notebook.bean.IconTag;
import com.example.gsf98.notebook.entity.Attach;
import com.example.gsf98.notebook.entity.Diary;
import com.example.gsf98.notebook.entity.Tag;
import com.example.gsf98.notebook.logic.Logic;
import com.example.gsf98.notebook.util.ActionBarHelper;
import com.example.gsf98.notebook.util.AttachHelper;
import com.example.gsf98.notebook.util.Constant;
import com.example.gsf98.notebook.util.FileHelper;
import com.example.gsf98.notebook.view.IconColorUtil;
import com.example.gsf98.notebook.view.IconTextView;

import org.xutils.ex.DbException;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.keyboardsurfer.android.widget.crouton.Configuration;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class EditActivity extends BaseActivity implements ImageChooserListener {
	@ViewInject(value = R.id.edit_title)
	private EditText edtTitle;

	@ViewInject(value = R.id.edit_content)
	private EditText edtContent;

	@ViewInject(value = R.id.edit_add_mood)
	private IconTextView emjiMood;

	@ViewInject(value = R.id.edit_add_lay)
	private LinearLayout layEdit;

	@ViewInject(value = R.id.edit_tag_lay)
	private LinearLayout layTag;

	@ViewInject(value = R.id.edit_tag_content)
	private TextView txtTag;

	@ViewInject(value = R.id.edit_icon_lay)
	private LinearLayout layIcon;

	@ViewInject(value = R.id.edit_icon_content)
	private LinearLayout layIconContent;

	private ImageChooserManager imageChooserManager;

	private Diary diary;
	private List<Attach> attachs;

	private ArrayList<Tag> tags;

	private IconTag moodSelect;
	private IconTag weaSelect;
	private ArrayList<IconTag> tagSelect;

	private LayoutInflater inflater;

	private LocationClient mLocationClient;
	private BDLocationListener myListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.diary_edit);

		x.view().inject(this);
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		inflater = LayoutInflater.from(getApplicationContext());

		diary = new Diary();
		attachs = new ArrayList<Attach>();
		tags = new ArrayList<Tag>();

		initLocationClient();

		animateMood();

		int type = getIntent().getIntExtra("type", 0);
		switch (type) {
		case 1:
			intentCameraEvent(null);
			break;
		case 2:
			intentPhotoEvent(null);
			break;
		case 3:
			intentGpsEvent(null);
			break;
		case 4:
			intentAudioEvent(null);
			break;
		default:
			break;
		}

		String key = getIntent().getStringExtra("key");
		if (!TextUtils.isEmpty(key)) {
			diary = Logic.getInstance(getApplicationContext()).searchDiaryByID(Integer.valueOf(key));
			showData();
		}
	}

	private void initLocationClient() {
		mLocationClient = new LocationClient(getApplicationContext()); // 声明LocationClient类
		myListener = new MyLocationListener();
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);
		option.setIsNeedAddress(true);
		mLocationClient.setLocOption(option);
		mLocationClient.registerLocationListener(myListener);
	}

	private void showData() {
		moodSelect = diary.getMood();
		weaSelect = diary.getWeather();
		tagSelect = diary.getIconTags();
		tags = diary.getTags();
		attachs = diary.getAttaches();
		if (attachs == null) {
			attachs = new ArrayList<Attach>();
		}

		edtTitle.setText(diary.getTitle());
		edtContent.setText(diary.getContent());
		showMood();
		showTag();
		if (attachs != null && attachs.size() > 0) {
			for (final Attach attach : attachs) {
				showAttach(attach);
			}
		}
	}

	private void animateMood() {
		Animation animation = (AnimationSet) AnimationUtils.loadAnimation(this, R.anim.ror);
		emjiMood.startAnimation(animation);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Crouton.cancelAllCroutons();
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_edit, menu);
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
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case ChooserType.REQUEST_CAPTURE_PICTURE:
				imageChooserManager.submit(requestCode, data);
				break;
			case ChooserType.REQUEST_PICK_PICTURE:
				imageChooserManager.submit(requestCode, data);
				break;
			case 99:
				showMood(data);
				break;
			case 98:
				showTag(data);
				break;
			case 97:
				showAudio(data);
				break;
			}
		}
	}

	private void showAudio(final Intent data) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				try {
					String oldPath = getAbsolutePathFromUri(data.getData());
					File f = new File(oldPath);
					File directory = new File(FileUtils.getDirectory(Constant.SD_PATH));
					File newFile = new File(directory, f.getName());
					FileHelper.copy(newFile.getAbsolutePath(), oldPath);

					chooseAudio(newFile);
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "读取失败", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	private String getAbsolutePathFromUri(Uri imageUri) {
		String[] proj = { MediaColumns.DATA, MediaColumns.DISPLAY_NAME };
		Cursor cursor = null;
		cursor = getContentResolver().query(imageUri, proj, null, null, null);
		cursor.moveToFirst();
		String filePath = "";
		filePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaColumns.DATA));
		cursor.close();
		return filePath;
	}

	private void showMood(Intent data) {
		retriveIntent(data);
		showMood();
	}

	private void showMood() {
		int color = R.color.clr_tag0;

		List<IconTag> all = new ArrayList<IconTag>();
		if (moodSelect != null) {
			all.add(moodSelect);
		}

		if (weaSelect != null) {
			all.add(weaSelect);
		}

		if (tagSelect != null) {
			all.addAll(tagSelect);
		}

		if (all.size() > 1) {
			emjiMood.setIcon(all.get(0).getText());
			emjiMood.setTextColor(getResources().getColor(IconColorUtil.getFaMap().get(all.get(0).getText())[0]));
			color = IconColorUtil.getFaMap().get(all.get(0).getText())[0];
			layIcon.setVisibility(View.VISIBLE);

			layIconContent.removeAllViews();
			for (int i = 1; i < all.size(); i++) {
				IconTag iconTag = all.get(i);
				if (iconTag != null) {
					View v = inflater.inflate(R.layout.diary_item_icon, null);
					IconTextView iconTextView = (IconTextView) v.findViewById(R.id.diary_item_icon);
					iconTextView.setIcon(all.get(i).getText());
					iconTextView.setBackgroundColor(getResources().getColor(IconColorUtil.getFaMap().get(iconTag.getText())[0]));
					layIconContent.addView(iconTextView);
				}
			}
		} else if (all.size() == 1) {
			emjiMood.setIcon(all.get(0).getText());
			emjiMood.setTextColor(getResources().getColor(IconColorUtil.getFaMap().get(all.get(0).getText())[0]));
			color = IconColorUtil.getFaMap().get(all.get(0).getText())[0];
		} else {
			layIcon.setVisibility(View.GONE);
		}

		setActionBarColor(color);
	}

	private void retriveIntent(Intent data) {
		moodSelect = (IconTag) data.getParcelableExtra("moodSelect");
		weaSelect = (IconTag) data.getParcelableExtra("weaSelect");
		tagSelect = new ArrayList<IconTag>();

		ArrayList<Parcelable> tmp = data.getParcelableArrayListExtra("tagSelect");

		if (tmp != null) {
			for (Parcelable parcelable : tmp) {
				if (parcelable instanceof IconTag) {
					tagSelect.add((IconTag) parcelable);
				}
			}
		}
	}

	private void showTag(Intent data) {
		tags = new ArrayList<Tag>();

		ArrayList<Parcelable> tmp = data.getParcelableArrayListExtra("tagSelect");

		if (tmp != null) {
			for (Parcelable parcelable : tmp) {
				if (parcelable instanceof Tag) {
					tags.add((Tag) parcelable);
				}
			}
		}

		showTag();
	}

	private void showTag() {
		if (tags == null || tags.size() == 0) {
			layTag.setVisibility(View.GONE);
		} else {
			layTag.setVisibility(View.VISIBLE);
			txtTag.setText(AttachHelper.retriveTag(tags));
		}
	}

	private boolean checkOK() {
		Configuration croutonConfiguration = new Configuration.Builder().setDuration(500).build();

		if (TextUtils.isEmpty(edtTitle.getText().toString())) {
			Crouton.makeText(this, R.string.please_title, Style.INFO).setConfiguration(croutonConfiguration).show();
			return false;
		}

		if (TextUtils.isEmpty(edtContent.getText().toString())) {
			Crouton.makeText(this, R.string.please_content, Style.INFO).setConfiguration(croutonConfiguration).show();
			return false;
		}

		if (moodSelect == null && weaSelect == null && (tagSelect == null || tagSelect.size() == 0)) {
			animateMood();
			Crouton.makeText(this, R.string.please_mood, Style.INFO).setConfiguration(croutonConfiguration).show();
			return false;
		}

		return true;
	}

	private void save() {
		if (!checkOK())
			return;

		Date date = Calendar.getInstance().getTime();
		diary.setTitle(edtTitle.getText().toString());
		diary.setContent(edtContent.getText().toString());
		diary.setDate(date);
		diary.setMood(moodSelect);
		diary.setWeather(weaSelect);
		diary.setIconTags(tagSelect);
		diary.setTags(tags);

		if (attachs != null && attachs.size() > 0) {
			for (Attach attach : attachs) {
				attach.setDescription(attach.getEdt().getText().toString());
			}
		}
		diary.setAttaches(attachs);

		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... params) {
				try {
					Logic.getInstance(getApplicationContext()).saveDiary(diary);
				} catch (DbException e) {
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				finish();
			}
		}.execute();
	}

	@Event(value = { R.id.edit_add_mood, R.id.edit_icon_lay })
	private void intentMoodEvent(View v) {
		Intent intent = new Intent(getApplicationContext(), MoodActivity.class);
		Bundle bundle = new Bundle();
		bundle.putBoolean("editable", true);
		bundle.putParcelable("moodSelect", moodSelect);
		bundle.putParcelable("weaSelect", weaSelect);
		bundle.putParcelableArrayList("tagSelect", tagSelect);
		intent.putExtras(bundle);
		startActivityForResult(intent, 99);
	}

	@Event(value = R.id.edit_add_photo)
	private void intentPhotoEvent(View v) {
		imageChooserManager = new ImageChooserManager(this, ChooserType.REQUEST_PICK_PICTURE, Constant.SD_PATH);
		imageChooserManager.setImageChooserListener(this);
		try {
			imageChooserManager.choose();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Event(value = R.id.edit_add_camera)
	private void intentCameraEvent(View v) {
		imageChooserManager = new ImageChooserManager(this, ChooserType.REQUEST_CAPTURE_PICTURE, Constant.SD_PATH);
		imageChooserManager.setImageChooserListener(this);
		try {
			imageChooserManager.choose();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Event(value = R.id.edit_add_gps)
	private void intentGpsEvent(View v) {
		mLocationClient.start();
		Toast.makeText(getApplicationContext(), "正在查找", Toast.LENGTH_SHORT).show();
	}

	public class MyLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			Attach attach = new Attach();
			attach.setType(2);
			attach.setName(location.getLatitude() + "," + location.getLongitude());
			attach.setDescription(location.getAddrStr());
			attach.setType(2);
			addToAttchList(attach);
			showAttach(attach);

			mLocationClient.stop();
		}
	}

	private void addToAttchList(Attach attach) {
		if (attachs == null) {
			attachs = new ArrayList<Attach>();
		}

		attachs.add(attach);
	}

	@Event(value = { R.id.edit_add_tag, R.id.edit_tag_lay })
	private void intentTagEvent(View v) {
		Intent intent = new Intent(getApplicationContext(), TagActivity.class);
		Bundle bundle = new Bundle();
		bundle.putParcelableArrayList("tagSelect", tags);
		intent.putExtras(bundle);
		startActivityForResult(intent, 98);
	}

	@Event(R.id.edit_add_audio)
	private void intentAudioEvent(View v) {
		try {
			Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
			// intent.putExtra( MediaStore.EXTRA_OUTPUT, imageFileUri );
			startActivityForResult(intent, 97);
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), "不能录音", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onError(String arg0) {
		Toast.makeText(getApplicationContext(), "获取不到图片", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onImageChosen(final ChosenImage image) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (image != null) {
					chooseImage(image);
				}
			}
		});
	}

	private void chooseImage(ChosenImage image) {
		String filename = new File(image.getFilePathOriginal()).getName();
		Attach attach = new Attach();
		attach.setType(0);
		attach.setName(filename);
		addToAttchList(attach);
		showAttach(attach);
	}

	private void chooseAudio(File newFile) {
		Attach attach = new Attach();
		attach.setType(1);
		attach.setName(newFile.getName());
		addToAttchList(attach);
		showAttach(attach);
	}

	private void showAttach(final Attach attach) {
		final View sep = inflater.inflate(R.layout.diary_attach_item_sep, null);
		final View v = inflater.inflate(R.layout.diary_attach_edit_item, null);

		ImageView i1 = (ImageView) v.findViewById(R.id.diary_attach_edit_item_logo);
		IconTextView i2 = (IconTextView) v.findViewById(R.id.diary_attach_edit_item_logo2);
		EditText t1 = (EditText) v.findViewById(R.id.diary_attach_edit_item_text);
		IconTextView idel = (IconTextView) v.findViewById(R.id.diary_attach_edit_item_del);

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
		attach.setEdt(t1);
		layEdit.addView(sep);
		layEdit.addView(v);

		idel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				attachs.remove(attach);
				layEdit.removeView(sep);
				layEdit.removeView(v);
			}
		});
	}
}
