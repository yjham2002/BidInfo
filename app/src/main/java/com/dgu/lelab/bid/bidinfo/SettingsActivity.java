package com.dgu.lelab.bid.bidinfo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.tsengvn.typekit.TypekitContextWrapper;

import java.util.HashMap;

import util.Communicator;
import util.URL;

public class SettingsActivity extends PreferenceActivity {

	private AlertDialog.Builder builder;

	public String appVersion() throws PackageManager.NameNotFoundException {
		PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
		String version = pInfo.versionName;
		return version;
	}

	/**
	 @Override
	protected void attachBaseContext(Context newBase) {
	super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
	}
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.pref_settings);
		Preference pref1 = findPreference( "version" );
		try {
			pref1.setSummary(appVersion());
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		builder = new AlertDialog.Builder(this, R.style.AppTheme_Dark_Dialog);
		builder.setMessage("새로운 비밀번호를 입력하세요");
		final EditText input = new EditText(this);
		input.setInputType( InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD );
		input.setTransformationMethod(PasswordTransformationMethod.getInstance());
		builder.setView(input);
		builder.setCancelable(true);
		builder.setPositiveButton("취소", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.dismiss();
				dialog.cancel();
			}
		})
				.setNegativeButton("확인", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						if(input.getText().toString().length() < 6){
							Toast.makeText(getApplicationContext(), "6자리 이상으로 설정하세요.", Toast.LENGTH_LONG).show();
							return;
						}
						HashMap<String, String> newPass = new HashMap<String, String>();
						newPass.put("id", Integer.toString(MainActivity.userId));
						newPass.put("Pwd", input.getText().toString());
						new Communicator().postHttp(util.URL.MAIN + URL.REST_UPDATE_PASS, newPass, new Handler(){
							@Override
							public void handleMessage(Message msg){
								Toast.makeText(getApplicationContext(), "변경되었습니다.", Toast.LENGTH_LONG).show();
							}
						});
					}
				});
		final AlertDialog alert = builder.create();

		Preference myPref = findPreference("cpass");
		myPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference preference) {
				input.setText("");
				alert.show();
				return true;
			}
		});

	}

	private void setOnPreferenceChange(Preference mPreference) {
		mPreference.setOnPreferenceChangeListener(onPreferenceChangeListener);
		onPreferenceChangeListener.onPreferenceChange(mPreference, PreferenceManager.getDefaultSharedPreferences(mPreference.getContext()).getString(mPreference.getKey(), ""));
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		LinearLayout root = (LinearLayout)findViewById(android.R.id.list).getParent().getParent().getParent();
		Toolbar bar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.settings_toolbar, root, false);
		root.addView(bar, 0); // insert at top
		bar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private Preference.OnPreferenceChangeListener onPreferenceChangeListener = new Preference.OnPreferenceChangeListener() {

		@Override
		public boolean onPreferenceChange(Preference preference, Object newValue) {
			String stringValue = newValue.toString();
			if (preference instanceof EditTextPreference) {
				preference.setSummary(stringValue);
			} else if (preference instanceof ListPreference) {
				ListPreference listPreference = (ListPreference) preference;
				int index = listPreference.findIndexOfValue(stringValue);
				preference.setSummary(index >= 0 ? listPreference.getEntries()[index] : null);
			} else if (preference instanceof RingtonePreference) {
				if (TextUtils.isEmpty(stringValue)) {
					// Empty values correspond to 'silent' (no ringtone).
					preference.setSummary("Summary");
				} else {
					Ringtone ringtone = RingtoneManager.getRingtone(preference.getContext(), Uri.parse(stringValue));
					if (ringtone == null) {
						preference.setSummary(null);
					} else {
						String name = ringtone.getTitle(preference.getContext());
						preference.setSummary(name);
					}
				}
			}
			return true;
		}

	};

}
