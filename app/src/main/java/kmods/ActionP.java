package kmods;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.preference.Preference;
import android.util.AttributeSet;
import android.widget.Toast;

import java.io.File;

import static kmods.Utils.getResID;

public class ActionP extends Preference {
    public ActionP(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ActionP(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ActionP(Context context) {
        super(context);
    }

    @Override
    protected void onClick() {
        super.onClick();
        switch (getKey()) {
            case "cfu":
                new Update(getContext()).execute((String[]) new String[0]);
                break;
            case "reset":
                getContext().getSharedPreferences("kmods_privacy", 0).edit().clear().apply();
                break;
            case "reset2":
                getContext().getSharedPreferences("com.whatsapp_preferences", 0).edit().clear().apply();
                break;
            case "backup":
                if (new File(Environment.getDataDirectory(), "data/com.whatsapp").exists()) {
                    if (!new File(Environment.getExternalStorageDirectory(), "WhatsApp/KBackup").exists()){
                        new File(Environment.getExternalStorageDirectory(), "WhatsApp/KBackup").mkdir();
                    }
                    if (!new File(Environment.getExternalStorageDirectory(), "WhatsApp/KBackup/com.whatsapp").exists()){
                        new File(Environment.getExternalStorageDirectory(), "WhatsApp/KBackup/com.whatsapp").mkdir();
                    }
                    new CopyTask(getContext(), true, new File(Environment.getDataDirectory(), "data/com.whatsapp"), new File(Environment.getExternalStorageDirectory(), "WhatsApp/KBackup/com.whatsapp")).execute(new File[0]);
                } else {
                    Toast.makeText(getContext(), "Can't find a Data!", Toast.LENGTH_SHORT).show();
                }
                break;
            case "restore":
                if (new File(Environment.getExternalStorageDirectory(), "WhatsApp/KBackup/com.whatsapp").exists() && new File(Environment.getExternalStorageDirectory(), "WhatsApp/KBackup").exists()) {
                    new CopyTask(getContext(), false, new File(Environment.getExternalStorageDirectory(), "WhatsApp/KBackup/com.whatsapp"), new File(Environment.getDataDirectory(), "data/com.whatsapp")).execute(new File[0]);
                } else {
                    Toast.makeText(getContext(), "Can't find a backup in '/sdcard/WhatsApp'!", Toast.LENGTH_SHORT).show();
                }
                break;
            case "share":
                final String string = getContext().getString(getResID("ShareBdy", "string"));
                final Intent intent3 = new Intent("android.intent.action.SEND");
                intent3.setType("text/plain");
                intent3.putExtra("android.intent.extra.SUBJECT", getContext().getString(getResID("ShareSbj", "string")));
                intent3.putExtra("android.intent.extra.TEXT", string);
                getContext().startActivity(Intent.createChooser(intent3, getContext().getString(getResID("Share", "string"))));
                break;
            case "cshort":
                Intent sIntent = new Intent(getContext(), com.whatsapp.Main.class);
                sIntent.setAction(Intent.ACTION_MAIN);
                sIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                sIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Intent intent = new Intent();
                intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, sIntent);
                intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, getContext().getString(getResID("app_name", "strings")));
                intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(getContext().getApplicationContext(), getResID("icon", "drawable")));
                intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
                getContext().sendBroadcast(intent);
                break;
            case "report":
                final Intent intent2 = new Intent("android.intent.action.SEND");
                intent2.setType("message/rfc822");
                intent2.putExtra("android.intent.extra.EMAIL", new String[] { "patel.kuldip91@gmali.com" });
                intent2.putExtra("android.intent.extra.SUBJECT", "KWhatsApp v" + Utils.v1 + "." + Utils.v2);
                intent2.putExtra("android.intent.extra.TEXT", "");
                try {
                    getContext().startActivity(Intent.createChooser(intent2,"Report..."));
                } catch (Exception ex) {
                    Toast.makeText(getContext(), "Can't find email client.", Toast.LENGTH_SHORT).show();
                }
                break;
            case "clemoji":
                final File file = new File("/data/data/com.whatsapp/files/emoji");
                if (file.exists()) {
                    file.delete();
                    Toast.makeText(getContext(), "All Recent Emojis Cleared", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "No Recent Emojis There!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
