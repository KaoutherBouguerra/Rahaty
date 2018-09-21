package art4muslim.macbook.rahatycustomer.fragments;


import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import art4muslim.macbook.rahatycustomer.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShareWithFriendsFragment extends Fragment {



    View v;
    Button btnCopy,btnShare;
    EditText editText10;
    boolean isRightToLeft  ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v  = inflater.inflate(R.layout.fragment_share_with_friends, container, false);
        isRightToLeft = getResources().getBoolean(R.bool.is_right_to_left);
        btnCopy  =(Button)v.findViewById(R.id.btnCopie);
        btnShare  =(Button)v.findViewById(R.id.btnShare);
        editText10  =(EditText)v.findViewById(R.id.editText10);
        getActivity().setTitle(getString(R.string.callfriends));
        btnCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setClipboard(getActivity(),editText10.getText().toString());
            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String shareBody = editText10.getText().toString();
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.lien_share)));
            }
        });
        return v;
    }
    private void setClipboard(Context context, String text) {
        Log.e("Copied text","text  "+text);
        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            ClipboardManager clipboard = ( ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
        } else {
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
            clipboard.setPrimaryClip(clip);
        }

        Toast.makeText(context, "Copied to Clipboard!", Toast.LENGTH_LONG).show();

    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_search).setVisible(false);
        menu.findItem(R.id.action_user).setVisible(false);

        if (!isRightToLeft ) {
            menu.findItem(R.id.item_back).setIcon(getResources().getDrawable(R.mipmap.backright));
        }else  menu.findItem(R.id.item_back).setIcon(getResources().getDrawable(R.mipmap.back));

        menu.findItem(R.id.item_back).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                MainFragment schedule1 = new MainFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame,schedule1,"home Fragment");
                fragmentTransaction.commit();

                return false;
            }
        });
        super.onPrepareOptionsMenu(menu);
    }
}
