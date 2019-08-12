package com.example.gsf98.notebook.frag;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;

import com.example.gsf98.notebook.DetailActivity;
import com.example.gsf98.notebook.EditActivity;
import com.example.gsf98.notebook.R;
import com.example.gsf98.notebook.adapter.DiaryListAdapter;
import com.example.gsf98.notebook.bean.IconTag;
import com.example.gsf98.notebook.bean.MonthBean;
import com.example.gsf98.notebook.entity.Diary;
import com.example.gsf98.notebook.entity.Tag;
import com.example.gsf98.notebook.logic.Logic;
import com.example.gsf98.notebook.view.QuickReturnListView;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class ListFrag extends Fragment {
	private List<Diary> diaries;
	private DiaryListAdapter adapter;

	@ViewInject(value = R.id.list_diary)
	private QuickReturnListView lstDiaries;

	@ViewInject(value = R.id.list_diary_footer)
	private LinearLayout mQuickReturnView;

	// 0:left, 1:tag, 2:month
	private int filtype = 0;
	private Tag filterTag;
	private MonthBean filterMonthBean;
	private List<IconTag> conType;
	private List<IconTag> conTags;
	private int conPage;

	private int mQuickReturnHeight;
	private static final int STATE_ONSCREEN = 0;
	private static final int STATE_OFFSCREEN = 1;
	private static final int STATE_RETURNING = 2;
	private int mState = STATE_ONSCREEN;
	private int mScrollY;
	private int mMinRawY = 0;

	private TranslateAnimation anim;
	private int lastItem;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.diary_list, null);
		x.view().inject(this, v);

		lstDiaries.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				mQuickReturnHeight = mQuickReturnView.getHeight();
				lstDiaries.computeScrollY();
			}
		});

		lstDiaries.setOnScrollListener(new OnScrollListener() {
			@SuppressLint("NewApi")
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				lastItem = firstVisibleItem + visibleItemCount - 1;

				mScrollY = 0;
				int translationY = 0;

				if (lstDiaries.scrollYIsComputed()) {
					mScrollY = lstDiaries.getComputedScrollY();
				}

				int rawY = mScrollY;

				switch (mState) {
				case STATE_OFFSCREEN:
					if (rawY >= mMinRawY) {
						mMinRawY = rawY;
					} else {
						mState = STATE_RETURNING;
					}
					translationY = rawY;
					break;

				case STATE_ONSCREEN:
					if (rawY > mQuickReturnHeight) {
						mState = STATE_OFFSCREEN;
						mMinRawY = rawY;
					}
					translationY = rawY;
					break;

				case STATE_RETURNING:

					translationY = (rawY - mMinRawY) + mQuickReturnHeight;

					System.out.println(translationY);
					if (translationY < 0) {
						translationY = 0;
						mMinRawY = rawY + mQuickReturnHeight;
					}

					if (rawY == 0) {
						mState = STATE_ONSCREEN;
						translationY = 0;
					}

					if (translationY > mQuickReturnHeight) {
						mState = STATE_OFFSCREEN;
						mMinRawY = rawY;
					}
					break;
				}

				/** this can be used if the build is below honeycomb **/
				if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.HONEYCOMB) {
					anim = new TranslateAnimation(0, 0, translationY, translationY);
					anim.setFillAfter(true);
					anim.setDuration(0);
					mQuickReturnView.startAnimation(anim);
				} else {
					mQuickReturnView.setTranslationY(translationY);
				}
			}

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (lastItem + 1 == adapter.getCount() && scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					conPage += 1;
					showData(1);
				}
			}
		});

		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initAdapter();
		showData(0);
	}

	@Override
	public void onResume() {
		super.onResume();
		showData(0);
	}

	// 0:refresh, 1:add
	private void showData(int type) {
		String title = "";
		if (type == 0)
			diaries.clear();

		if (filtype == 0) {
			if (type == 0) {
				for (int page = 0; page <= conPage; page++) {
					List<Diary> tmp = Logic.getInstance(getActivity()).searchDiary(conType, conTags, page);
					diaries.addAll(tmp);
				}
			} else {
				List<Diary> tmp = Logic.getInstance(getActivity()).searchDiary(conType, conTags, conPage);
				diaries.addAll(tmp);
			}

			title = getString(R.string.app_name);
		} else if (filtype == 1) {
			if (type == 0) {
				for (int page = 0; page <= conPage; page++) {
					List<Diary> tmp = Logic.getInstance(getActivity()).searchDiary(filterTag, page);
					diaries.addAll(tmp);
				}
			} else {
				List<Diary> tmp = Logic.getInstance(getActivity()).searchDiary(filterTag, conPage);
				diaries.addAll(tmp);
			}

			title = "# " + filterTag.getName();
		} else if (filtype == 2) {
			if (type == 0) {
				for (int page = 0; page <= conPage; page++) {
					List<Diary> tmp = Logic.getInstance(getActivity()).searchDiary(filterMonthBean, page);
					diaries.addAll(tmp);
				}
			} else {
				List<Diary> tmp = Logic.getInstance(getActivity()).searchDiary(filterMonthBean, conPage);
				diaries.addAll(tmp);
			}

			title = "# " + filterMonthBean.getContent();
		}

		// if( getActivity() instanceof FragChangeActivity )
		// {
		// FragChangeActivity activity = (FragChangeActivity)getActivity();
		// activity.setCustomTitle( title );
		// if( filtype != 0 )
		// {
		// activity.getLeftFrag().clearStatus();
		// }
		// }

		adapter.notifyDataSetChanged();
	}

	private void initAdapter() {
		diaries = new ArrayList<Diary>();
		adapter = new DiaryListAdapter(getActivity(), diaries);
		lstDiaries.setAdapter(adapter);
	}

	@Event(value = R.id.list_diary, type = OnItemClickListener.class)
	private void clickItemEvent(AdapterView<?> parent, View view, int position, long id) {
		Intent intent = new Intent(getActivity(), DetailActivity.class);
		Bundle bundle = new Bundle();
		bundle.putInt("key", diaries.get(position).getId());
		intent.putExtras(bundle);
		startActivityForResult(intent, 99);
	}

	@Event(value = R.id.list_diary_footer_text)
	private void intentTextEvent(View v) {
		Intent intent = new Intent(getActivity(), EditActivity.class);
		Bundle bundle = new Bundle();
		bundle.putInt("type", 0);
		intent.putExtras(bundle);
		startActivity(intent);
	}

	@Event(value = R.id.list_diary_footer_camera)
	private void intentCameraEvent(View v) {
		Intent intent = new Intent(getActivity(), EditActivity.class);
		Bundle bundle = new Bundle();
		bundle.putInt("type", 1);
		intent.putExtras(bundle);
		startActivity(intent);
	}

	@Event(value = R.id.list_diary_footer_image)
	private void intentGalleryEvent(View v) {
		Intent intent = new Intent(getActivity(), EditActivity.class);
		Bundle bundle = new Bundle();
		bundle.putInt("type", 2);
		intent.putExtras(bundle);
		startActivity(intent);
	}

	@Event(value = R.id.list_diary_footer_gps)
	private void intentGpsEvent(View v) {
		Intent intent = new Intent(getActivity(), EditActivity.class);
		Bundle bundle = new Bundle();
		bundle.putInt("type", 3);
		intent.putExtras(bundle);
		startActivity(intent);
	}

	@Event(value = R.id.list_diary_footer_audio)
	private void intentAuioEvent(View v) {
		Intent intent = new Intent(getActivity(), EditActivity.class);
		Bundle bundle = new Bundle();
		bundle.putInt("type", 4);
		intent.putExtras(bundle);
		startActivity(intent);
	}

	public void filtByLeftMenu(List<IconTag> type, List<IconTag> tags) {
		filtype = 0;
		conType = type;
		conTags = tags;
		conPage = 0;
		filterTag = null;
		filterMonthBean = null;
		showData(0);
	}

	public void filtByTag(Tag tag) {
		filtype = 1;
		conType = null;
		conTags = null;
		conPage = 0;
		filterTag = tag;
		filterMonthBean = null;

		// if( getActivity() instanceof FragChangeActivity )
		// {
		// ( (FragChangeActivity)getActivity() ).closeMenu();
		// }

		showData(0);
	}

	public void filtByMonth(MonthBean month) {
		filtype = 2;
		conType = null;
		conTags = null;
		conPage = 0;
		filterTag = null;
		filterMonthBean = month;

		// if( getActivity() instanceof FragChangeActivity )
		// {
		// ( (FragChangeActivity)getActivity() ).closeMenu();
		// }

		showData(0);
	}
}
