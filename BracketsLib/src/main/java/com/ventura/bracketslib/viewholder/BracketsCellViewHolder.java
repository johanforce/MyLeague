package com.ventura.bracketslib.viewholder;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.jarvis.design_system.textview.CustomTextView;
import com.ventura.bracketslib.R;
import com.ventura.bracketslib.animation.SlideAnimation;


/**
 * Created by Emil on 21/10/17.
 */

public class BracketsCellViewHolder extends RecyclerView.ViewHolder {

    private final CustomTextView teamOneName;
    private final CustomTextView teamTwoName;
    private final CustomTextView teamOneScore;
    private final CustomTextView teamTwoScore;
    private Animation animation;
    private final LinearLayoutCompat rootLayout;
    private final LinearLayoutCompat teamOneLayout;
    private final LinearLayoutCompat teamTwoLayout;
    private final AppCompatImageView icLogo;
    private final AppCompatImageView icLogo2;
    private final CustomTextView tvMatch;

    public BracketsCellViewHolder(View itemView, int bracketColor, int textColor) {
        super(itemView);
        teamOneName = itemView.findViewById(R.id.team_one_name);
        teamTwoName = itemView.findViewById(R.id.team_two_name);
        teamOneScore = itemView.findViewById(R.id.team_one_score);
        teamTwoScore = itemView.findViewById(R.id.team_two_score);
        rootLayout = itemView.findViewById(R.id.layout_root);
        teamOneLayout = itemView.findViewById(R.id.team_one_layout);
        teamTwoLayout = itemView.findViewById(R.id.team_two_layout);
        icLogo = itemView.findViewById(R.id.icLogo);
        icLogo2 = itemView.findViewById(R.id.icLogo2);
        tvMatch = itemView.findViewById(R.id.tvTitle);
        setViewColor(bracketColor, textColor);
    }

    private void setViewColor(int bracketColor, int textColor) {
        teamOneLayout.setBackgroundColor(bracketColor);
        teamTwoLayout.setBackgroundColor(bracketColor);
        teamOneName.setTextColor(textColor);
        teamTwoName.setTextColor(textColor);
        teamOneScore.setTextColor(textColor);
        teamTwoScore.setTextColor(textColor);
        tvMatch.setTextColor(textColor);
    }

    public void setAnimation(int height) {
        animation = new SlideAnimation(rootLayout, rootLayout.getHeight(),
                height);
        animation.setInterpolator(new LinearInterpolator());
        animation.setDuration(200);
        rootLayout.setAnimation(animation);
        rootLayout.startAnimation(animation);
    }

    public TextView getTeamTwoName() {
        return teamTwoName;
    }

    public TextView getTeamOneScore() {
        return teamOneScore;
    }

    public TextView getTeamTwoScore() {
        return teamTwoScore;
    }

    public TextView getTeamOneName() {
        return teamOneName;
    }

    public AppCompatImageView getIcLogo() {
        return icLogo;
    }

    public AppCompatImageView getIcLogo2() {
        return icLogo2;
    }

    public CustomTextView getTvMatch() {
        return tvMatch;
    }

    public LinearLayoutCompat getTeamOneLayout() {
        return teamOneLayout;
    }

    public LinearLayoutCompat getTeamTwoLayout() {
        return teamTwoLayout;
    }
}
