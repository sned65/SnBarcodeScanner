package sne.bcs.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;

import sne.bcs.R;


/**
 * Dialog Fragment "About program".
 */
public class AboutDialogFragment extends DialogFragment
{
    public static final String ARG_TITLE = "title";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        Bundle args = getArguments();
        String title = args.getString(ARG_TITLE);
        AlertDialog.Builder msgBox = new AlertDialog.Builder(getActivity());
        msgBox.setMessage(makeText(title));
        return msgBox.create();
    }

    private Spanned makeText(String title)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("<b>").append(title).append("</b><br/>");
        try
        {
            PackageInfo pinfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            sb.append("Version code ").append(pinfo.versionCode).append("<br/>");
            sb.append("Version name ").append(pinfo.versionName).append("<br/>");
            sb.append(getString(R.string.copyright)).append(" ").append(getString(R.string.author));
        }
        catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }
        return Html.fromHtml(sb.toString());
    }
}
