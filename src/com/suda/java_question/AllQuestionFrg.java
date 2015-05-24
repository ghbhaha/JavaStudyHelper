package com.suda.java_question;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.suda.java_question.db.DBOpenHelper;
import com.suda.java_question.model.Question;

@SuppressLint("NewApi")
public class AllQuestionFrg extends Fragment {
	private DBOpenHelper dbOpenHelper;
	private String type;

	ListView listView;
	ArrayList<Question> arrayList;

	private MyAdapter adapter;

	public AllQuestionFrg(DBOpenHelper dbOpenHelper, String type) {
		// TODO Auto-generated constructor stub
		this.dbOpenHelper = dbOpenHelper;
		this.type = type;

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	public void refresh() {
		arrayList = getQus();
		if (adapter != null) {
			adapter = null;
		}
		adapter = new MyAdapter(getActivity());
		listView.setAdapter(adapter);
	}

	public ArrayList<Question> getQus() {

		ArrayList<Question> arrayList = new ArrayList<>();
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		Cursor cursor = db.query("question", null, "Type=?",
				new String[] { type }, null, null, null);
		int i = 0;
		while (cursor.moveToNext()) {
			i++;
			int no = cursor.getInt(cursor.getColumnIndex("Id"));
			String ques = i + "¡¢"
					+ cursor.getString(cursor.getColumnIndex("Question"));
			String anw = cursor.getString(cursor.getColumnIndex("Answer"));
			int mark = cursor.getInt(cursor.getColumnIndex("Mark"));

			arrayList.add(new Question(no, ques, anw, mark));

		}

		cursor.close();
		return arrayList;

	}

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		View view = inflater.inflate(R.layout.question, container, false);

		adapter = new MyAdapter(getActivity());

		listView = (ListView) view.findViewById(R.id.que);
		arrayList = getQus();

		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				Intent intent = new Intent(getActivity(), AnswerActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("as", arrayList.get(position).getAnswer());
				intent.putExtras(bundle);
				startActivity(intent);
			}

		});
		return view;

	}

	@SuppressWarnings("deprecation")
	public void setMark(int position, int mark, View v) {
		SQLiteDatabase cursor = dbOpenHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("Mark", mark == 1 ? 0 : 1);

		cursor.update("question", values, "Id=?",
				new String[] { arrayList.get(position).getNo() + "" });

		arrayList.get(position).setMark(mark == 1 ? 0 : 1);

		v.setBackground(mark == 1 ? getResources().getDrawable(
				R.drawable.abc_btn_rating_star_off_mtrl_alpha) : getResources()
				.getDrawable(R.drawable.abc_btn_rating_star_on_mtrl_alpha));
		cursor.close();

	}

	// ×Ô¶¨Òåadapter
	public final class ViewHolder {
		public TextView qs;

		public ImageButton mark;
	}

	@SuppressLint("InflateParams")
	public class MyAdapter extends BaseAdapter {

		private LayoutInflater mInflater;

		public MyAdapter(Context context) {
			this.mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return arrayList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@SuppressWarnings("deprecation")
		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {

				holder = new ViewHolder();

				convertView = mInflater.inflate(R.layout.qsitem, null);

				holder.qs = (TextView) convertView.findViewById(R.id.qs);

				holder.mark = (ImageButton) convertView.findViewById(R.id.mark);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.qs.setText(arrayList.get(position).getQuestion());

			holder.mark
					.setBackground(arrayList.get(position).getMark() == 1 ? getResources()
							.getDrawable(
									R.drawable.abc_btn_rating_star_on_mtrl_alpha)
							: getResources()
									.getDrawable(
											R.drawable.abc_btn_rating_star_off_mtrl_alpha));

			holder.mark.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					setMark(position, arrayList.get(position).getMark(), v);

				}
			});

			return convertView;
		}
	}

}
