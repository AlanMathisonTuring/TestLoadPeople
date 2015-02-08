package com.nutegg.testloadpeople;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	TextView tv;
	EditText et1;
	EditText et2;
	EditText et3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		tv = (TextView)this.findViewById(R.id.tv);
		et1 = (EditText)this.findViewById(R.id.editText1);
		et2 = (EditText)this.findViewById(R.id.editText2);
		et3 = (EditText)this.findViewById(R.id.editText3);
	}

	public void loadPeople(View view){
		Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
		Uri url = Uri.parse("content://com.android.contacts/data");
		ContentResolver resolver = getContentResolver();
		Cursor cursor = resolver.query(uri, null, null, null, null);
		String contacts = "";
		if(cursor != null){
			while(cursor.moveToNext()){	
				String id = cursor.getString(cursor.getColumnIndex("contact_id"));
				Log.i("NUTEGG", "联系人ID:"+id);
				Cursor cursor2 = resolver.query(url, null, "raw_contact_id=?", new String[]{id}, null);
				String[] names = cursor2.getColumnNames();
				for(int i=0;i<names.length;i++){
					Log.i("NUTEGG", names[i]);
				}
				
				contacts += "联系人ID:"+id ;
				if(cursor2 != null){
					while(cursor2.moveToNext()){
						String data1 = cursor2.getString(cursor2.getColumnIndex("data1"));
						//因为在数据库中用的很多的视图,所以这边取mimetype_id的时候直接取mimetype_id无法找到
						String mimetype = cursor2.getString(cursor2.getColumnIndex("mimetype"));
						Log.i("NUTEGG", ",联系人内容:"+data1+",mimetype_id:"+mimetype);
						contacts += "联系人内容:"+data1+",mimetype_id:"+mimetype;
					}
					contacts += "-----------------------------";
				}else{
					tv.setText("联系人信息读取失败");
				}
				
				Log.i("NUTEGG", "-----------------------------");
				
			}
			tv.setText(contacts);
		}else{
			tv.setText("还未添加联系人信息到通讯录!");
		}
		
	}
	
	public void addPeople(View view){
		Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
		Uri url = Uri.parse("content://com.android.contacts/data");
		ContentResolver resolver = getContentResolver();
		Cursor cursor = resolver.query(uri, null, null, null, null);
		cursor.moveToLast();
		String id = cursor.getString(cursor.getColumnIndex("contact_id"));
		String contactId = (Integer.parseInt(id)+1)+"";
		ContentValues values = new ContentValues();
		values.put("contact_id", contactId);	
		resolver.insert(uri, values);
		values.clear();
		values.put("raw_contact_id", contactId);
		values.put("data1", et1.getText().toString());
		values.put("mimetype", "7");
		resolver.insert(url, values);
		values.put("raw_contact_id", contactId);
		values.put("data1", et2.getText().toString());
		values.put("mimetype", "5");
		
		values.put("raw_contact_id", contactId);
		values.put("data1", et3.getText().toString());
		values.put("mimetype", "1");
		Toast.makeText(this, "保存联系人成功", Toast.LENGTH_SHORT).show();
	}


}
