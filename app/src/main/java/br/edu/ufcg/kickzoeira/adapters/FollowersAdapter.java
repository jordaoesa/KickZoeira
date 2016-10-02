package br.edu.ufcg.kickzoeira.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import br.edu.ufcg.kickzoeira.R;

import java.util.ArrayList;
import java.util.List;

import br.edu.ufcg.kickzoeira.model.KickZoeiraUser;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by arthur on 9/28/16.
 */
public class FollowersAdapter extends ArrayAdapter {

    private Context context;
    private List<KickZoeiraUser> users;
    private List<KickZoeiraUser> checkedUsers;
    private int layoutId;
    public String a;

    public FollowersAdapter(Context context, int resource, List<KickZoeiraUser> users) {
        super(context, resource, users);
        this.context = context;
        this.users = users;
        this.layoutId = resource;
        this.checkedUsers = new ArrayList<KickZoeiraUser>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;
        if(row == null){
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutId, parent, false);
            holder = new ViewHolder(row, position);
            row.setTag(holder);
        }else{
            holder = (ViewHolder) row.getTag();
        }

        KickZoeiraUser user = users.get(position);
        System.out.println("=================" + user.getEmail());
        holder.userName.setText(user.getEmail());
//        retrieveProfilePicture(user.getObjectId(), holder.userImage, holder.progressBar);
        return row;

    }

//    private void retrieveProfilePicture(String userId, CircleImageView imageView, ProgressBar progressBar) {
//        if (!Util.getInstance().isConnected(context)) {
//            return;
//        }
//        String httpURL = context.getString(R.string.backendless_http_pictures);
//        httpURL = httpURL.replace("#app_id", context.getString(R.string.backendless_application_id));
//        httpURL = httpURL.replace("#version", context.getString(R.string.backendless_app_version));
//        httpURL = httpURL.replace("#path", context.getString(R.string.backendless_users_path) + userId);
//        httpURL = httpURL.replace("#file_name", "profile.png");
//
//        Util.getInstance().downloadImage(imageView, progressBar, httpURL);
//
//    }

    public List<KickZoeiraUser> getCheckedUsers() {
        return checkedUsers;
    }

    public class ViewHolder{
        public CircleImageView userImage;
        public TextView userName;
//        public ProgressBar progressBar;
//        public CheckBox checkBox;
        public int position;

        public ViewHolder(View view, int pos){
            position = pos;
            userImage = (CircleImageView) view.findViewById(R.id.profile_image);
            //userName = (TextView) view.findViewById(R.id.tv_name_user_grid_adapter);
//            progressBar = (ProgressBar) view.findViewById(R.id.pb_profile_picture);
//            checkBox = (CheckBox) view.findViewById(R.id.checkBox);
//            checkBox.setClickable(false);
//            checkBox.setEnabled(false);

//            view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if(checkBox.isChecked()) {
//                        for(KickZoeiraUser usr : checkedUsers){
//                            if(usr.getId().equals(users.get(position).getId())){
//                                checkedUsers.remove(usr);
//                            }
//                        }
//                        checkBox.setChecked(false);
//                    }else{
//                        checkedUsers.add(users.get(position));
//                        checkBox.setChecked(true);
//                    }
//                }
//            });
        }

    }
}
