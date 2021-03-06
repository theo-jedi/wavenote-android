package com.theost.wavenote;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.fragment.app.FragmentManager;
import androidx.preference.Preference;

import com.afollestad.materialdialogs.folderselector.FileChooserDialog;
import com.afollestad.materialdialogs.folderselector.FolderChooserDialog;
import com.theost.wavenote.utils.ThemeUtils;

import org.wordpress.passcodelock.PasscodePreferenceFragment;
import org.wordpress.passcodelock.PasscodePreferenceFragmentCompat;

import java.io.File;

import static com.theost.wavenote.utils.DisplayUtils.disableScreenshotsIfLocked;

public class PreferencesActivity extends ThemedAppCompatActivity implements FolderChooserDialog.FolderCallback, FileChooserDialog.FileCallback {
    private static final String EXTRA_THEME_CHANGED = "themeChanged";

    private PasscodePreferenceFragmentCompat mPasscodePreferenceFragment;
    private PreferencesFragment mPreferencesFragment;
    private boolean mIsThemeChanged;

    @Override
    public void onBackPressed() {
        if (mIsThemeChanged) {
            NavUtils.navigateUpFromSameTask(PreferencesActivity.this);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtils.setTheme(this);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_preferences);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle(R.string.settings);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        String preferencesTag = "tag_preferences";
        String passcodeTag = "tag_passcode";
        if (savedInstanceState == null) {
            Bundle passcodeArgs = new Bundle();
            passcodeArgs.putBoolean(PasscodePreferenceFragment.KEY_SHOULD_INFLATE, false);
            mPasscodePreferenceFragment = new PasscodePreferenceFragmentCompat();
            mPasscodePreferenceFragment.setArguments(passcodeArgs);

            mPreferencesFragment = new PreferencesFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.preferences_container, mPreferencesFragment, preferencesTag)
                    .add(R.id.preferences_container, mPasscodePreferenceFragment, passcodeTag)
                    .commit();
        } else {
            FragmentManager fragmentManager = getSupportFragmentManager();
            mPreferencesFragment = (PreferencesFragment) fragmentManager.findFragmentByTag(preferencesTag);
            mPasscodePreferenceFragment = (PasscodePreferenceFragmentCompat) fragmentManager.findFragmentByTag(passcodeTag);
        }

        if (getIntent().getExtras() != null && getIntent().hasExtra(EXTRA_THEME_CHANGED)) {
            mIsThemeChanged = getIntent().getExtras().getBoolean(EXTRA_THEME_CHANGED, false);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        Preference togglePref = mPreferencesFragment.findPreference(getString(R.string.pref_key_passcode_toggle));
        Preference changePref = mPreferencesFragment.findPreference(getString(R.string.pref_key_change_passcode));

        if (togglePref != null && changePref != null) {
            mPasscodePreferenceFragment.setPreferences(togglePref, changePref);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (mIsThemeChanged) {
                NavUtils.navigateUpFromSameTask(PreferencesActivity.this);
            } else {
                finish();
            }

            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        disableScreenshotsIfLocked(this);
    }

    @Override
    public void recreate() {
        Intent intent = new Intent(PreferencesActivity.this, PreferencesActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(EXTRA_THEME_CHANGED, true);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    @Override
    public void onFolderSelection(@NonNull FolderChooserDialog dialog, @NonNull File folder) {
        mPreferencesFragment.changeExportFolder(folder);
    }

    @Override
    public void onFolderChooserDismissed(@NonNull FolderChooserDialog dialog) {}

    @Override
    public void onFileSelection(@NonNull FileChooserDialog dialog, @NonNull File file) {
        mPreferencesFragment.selectImportFile(file);
    }

    @Override
    public void onFileChooserDismissed(@NonNull FileChooserDialog dialog) {}

}
