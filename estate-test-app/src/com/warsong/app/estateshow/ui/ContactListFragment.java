package com.warsong.app.estateshow.ui;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.warsong.app.estateshow.manager.AppDataManager;
import com.warsong.app.estateshow.model.ContactKVModel;
import com.warsong.app.estateshow.model.ContactModel;
import com.warsong.wb.estate.R;

/**
 * test用的列表fragment
 * 
 * @author newaowen@gmail.com
 * @date 2013-10-19 上午11:08:15
 */
public class ContactListFragment extends Fragment {

	private View contentView;
	private TextView titleText;
	private ListView listView;
	private CustomAdapter adapter;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		contentView = inflater.inflate(R.layout.contact_fragment, null);
		return contentView;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		initView();
	}

	private void initView() {
		ContactModel contact = AppDataManager.getInstance().getContact();
		// 设置标题
		titleText = (TextView) contentView.findViewById(R.id.title);
		titleText.setText(contact.getTitle());

		adapter = new CustomAdapter(getActivity(), contact.getKv());
		listView = (ListView) contentView.findViewById(R.id.list);
		listView.setAdapter(adapter);
	}

	public class CustomAdapter extends BaseAdapter {
		private Context context;
		List<ContactKVModel> contactKV;

		public CustomAdapter(Context context, List<ContactKVModel> contactKV) {
			this.context = context;
			this.contactKV = contactKV;
		}

		@Override
		public int getCount() {
			return contactKV.size();
		}

		@Override
		public Object getItem(int position) {
			return contactKV.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			final ContactKVModel kv = (ContactKVModel) getItem(position);
			
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(R.layout.contact_row, null);
				convertView.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						String number =  kv.getNumber();
						number = number.replaceAll("[^0-9+-]+", "");
						Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number));
						startActivity(i);
					}
				});
			}
			
			TextView name = (TextView) convertView.findViewById(R.id.name);
			name.setText(kv.getName());

			TextView value = (TextView) convertView.findViewById(R.id.phone_number);
			value.setText(kv.getNumber());

			return convertView;
		}

	}
}
