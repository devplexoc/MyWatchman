package com.plexoc.mywatchman.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.plexoc.mywatchman.Activity.LoginSignupActivity;
import com.plexoc.mywatchman.BuildConfig;
import com.plexoc.mywatchman.Fragment.AddUserFragment;
import com.plexoc.mywatchman.Fragment.AddressFragment;
import com.plexoc.mywatchman.Fragment.BillingFragment;
import com.plexoc.mywatchman.Fragment.DashboardFragment;
import com.plexoc.mywatchman.Fragment.EmergencyContactFragment;
import com.plexoc.mywatchman.Fragment.NotificationFragment;
import com.plexoc.mywatchman.Fragment.ProfileFragment;
import com.plexoc.mywatchman.Fragment.SOSHistoryFragment;
import com.plexoc.mywatchman.Fragment.StatisticsFragment;
import com.plexoc.mywatchman.Model.User;
import com.plexoc.mywatchman.R;


public class DrawerUtil {

    static Drawer drawer = null;

    public static void getDrawer(final Activity activity, Toolbar toolbar, User user) {

        overrideDrawerImageLoaderPicasso(activity);
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName("Dashboard").withSelectable(false);
        PrimaryDrawerItem item2 = new PrimaryDrawerItem().withIdentifier(2).withName("Profile").withSelectable(false);
        PrimaryDrawerItem item3 = new PrimaryDrawerItem().withIdentifier(3).withName("My Addresses").withSelectable(false);
        PrimaryDrawerItem item4 = new PrimaryDrawerItem().withIdentifier(4).withName("My Community").withSelectable(false);
        PrimaryDrawerItem item5 = new PrimaryDrawerItem().withIdentifier(5).withName("Manage Users").withSelectable(false);
        PrimaryDrawerItem item6 = new PrimaryDrawerItem().withIdentifier(6).withName("Billing").withSelectable(false);
        PrimaryDrawerItem item8 = new PrimaryDrawerItem().withIdentifier(8).withName("Notification").withSelectable(false);
        PrimaryDrawerItem item7 = new PrimaryDrawerItem().withIdentifier(7).withName("Logout").withSelectable(false);
        PrimaryDrawerItem item9 = new PrimaryDrawerItem().withIdentifier(9).withName("Statistics").withSelectable(false);
        PrimaryDrawerItem item10 = new PrimaryDrawerItem().withIdentifier(10).withName("SOS History").withSelectable(false);

        /*Prefs.getString("UserName");
        Prefs.getString("UserEmail");
        Prefs.getString("UserImage");*/
        if (user.ProfilePicture == null)
            user.ProfilePicture = "error";

        AccountHeader accountHeader = new AccountHeaderBuilder()
                .withActivity(activity)
                .addProfiles(new ProfileDrawerItem().withName(user.FirstName + " " + user.LastName).withEmail(user.Email).withIcon(user.ProfilePicture))
                .withSelectionListEnabled(false)
                .withDividerBelowHeader(false)
                .withTypeface(Typeface.SANS_SERIF)
                .withCompactStyle(true)
                .withOnAccountHeaderSelectionViewClickListener(new AccountHeader.OnAccountHeaderSelectionViewClickListener() {
                    @Override
                    public boolean onClick(View view, IProfile profile) {
                        replaceFragment(new ProfileFragment(), null, activity);
                        drawer.closeDrawer();
                        return false;
                    }
                })
                .withHeaderBackground(R.color.colorPrimaryDark)
                .withTextColor(activity.getResources().getColor(R.color.colorWhite))
                .build();

        drawer = new DrawerBuilder()
                .withActivity(activity)
                //.withHeader(R.layout.drawer_header_layout)
                .withToolbar(toolbar)
                .withAccountHeader(accountHeader)
                .addDrawerItems(
                        item1,
                        item2,
                        item3,
                        item4,
                        item5,
                        item6,
                        item10,
                        item9,
                        item8,
                        item7
                )
                .withSelectedItem(-1)
                .build();

        if (user.UserType == 2) {
            drawer.removeItem(3);
            drawer.removeItem(4);
            drawer.removeItem(5);
            drawer.removeItem(6);
        }

        drawer.addStickyFooterItem(new PrimaryDrawerItem().withName(BuildConfig.VERSION_NAME).withSelectable(false));
        drawer.setOnDrawerItemClickListener((view, position, drawerItem) -> {

            switch (String.valueOf(drawerItem.getIdentifier())) {
                case "1":
                    drawer.closeDrawer();
                    replaceFragment(new DashboardFragment(), null, activity);
                    break;
                case "2":
                    replaceFragment(new ProfileFragment(), null, activity);
                    drawer.closeDrawer();
                    break;
                case "3":
                    drawer.closeDrawer();
                    replaceFragment(new AddressFragment(), null, activity);
                    break;
                case "4":
                    replaceFragment(new EmergencyContactFragment(), EmergencyContactFragment.class.getName(), activity);
                    drawer.closeDrawer();
                    break;
                case "5":
                    replaceFragment(new AddUserFragment(), null, activity);
                    drawer.closeDrawer();
                    break;
                case "6":
                    replaceFragment(new BillingFragment(), null, activity);
                    drawer.closeDrawer();
                    break;
                case "10":
                    replaceFragment(new SOSHistoryFragment(), null, activity);
                    drawer.closeDrawer();
                    break;
                case "9":
                    replaceFragment(new StatisticsFragment(), null, activity);
                    drawer.closeDrawer();
                    break;
                case "8":
                    replaceFragment(new NotificationFragment(), null, activity);
                    drawer.closeDrawer();
                    break;
                case "7":
                    drawer.closeDrawer();
                    new AlertDialog.Builder(activity)
                            .setMessage("Are You Sure Want To Logout?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    PreferenceManager.getDefaultSharedPreferences(activity).edit().clear().apply();
                                    Intent intent = new Intent(activity, LoginSignupActivity.class);
                                    activity.startActivity(intent);
                                    activity.finish();
                                }
                            })
                            .setNegativeButton("No", null)
                            .show();
                    break;
            }
            return true;
        });
    }

    private static void replaceFragment(Fragment fragment, String fragmentTag, Activity activity) {
        FragmentManager manager = ((FragmentActivity) activity).getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(fragmentTag == null ? fragment.getClass().getName() : fragmentTag);
        transaction.commitAllowingStateLoss();

    }

    private static void overrideDrawerImageLoaderPicasso(Activity activity) {
        //initialize and create the image loader logic
        DrawerImageLoader.init(new AbstractDrawerImageLoader() {
            @Override
            public void set(ImageView imageView, Uri uri, Drawable placeholder) {
                //   Picasso.get().load(uri).placeholder(CirclurProgressDrawable.ShowProgressDrwable(activity)).error(R.drawable.user).resize(80, 80).centerCrop().into(imageView);
                Glide.with(activity)
                        .load(uri)
                        .thumbnail(0.25f)
                        .circleCrop()
                        .placeholder(CirclurProgressDrawable.ShowProgressDrwable(activity))
                        .error(R.drawable.ic_user_colorful)
                        .into(imageView);
            }

            @Override
            public void cancel(ImageView imageView) {
                //Picasso.get().cancelRequest(imageView);
            }

            @Override
            public Drawable placeholder(Context ctx) {
                return super.placeholder(ctx);
            }

            @Override
            public Drawable placeholder(Context ctx, String tag) {
                return super.placeholder(ctx, tag);
            }

        });
    }

}
