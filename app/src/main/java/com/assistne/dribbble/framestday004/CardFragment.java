package com.assistne.dribbble.framestday004;

import android.media.Image;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.assistne.dribbble.R;

/**
 * Created by assistne on 16/4/26.
 */
public class CardFragment extends Fragment{
    private static final String TAG = "#CardFragment";

    private static final String KEY_CARD = "CARD";
    private static final String KEY_STAR = "STAR";
    private static final String KEY_COMMENT = "COMMENT";
    private static final String KEY_AVATAR = "AVATAR";

    private View mContainer;
    private ImageView mCard;
    private ImageView mStar;
    private TextView mComment;
    private ImageView mAvatar;

    public static CardFragment newInstance(@DrawableRes int card, @DrawableRes int star,
                                           @StringRes int comment, @DrawableRes int avatar) {
        CardFragment fragment = new CardFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_CARD, card);
        bundle.putInt(KEY_STAR, star);
        bundle.putInt(KEY_COMMENT, comment);
        bundle.putInt(KEY_AVATAR, avatar);
        fragment.setArguments(bundle);
        return fragment;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_fst_d4_card, container, false);
        mContainer = root.findViewById(R.id.fst_d4_container);
        Log.d(TAG, "onCreateView: " + this + "  " +  mContainer);
        mCard = (ImageView) root.findViewById(R.id.fst_d4_card_img);
        mStar = (ImageView) root.findViewById(R.id.fst_d4_star);
        mComment = (TextView) root.findViewById(R.id.fst_d4_card_comment);
        mAvatar = (ImageView) root.findViewById(R.id.fst_d4_card_avatar);
        Bundle bundle = getArguments();
        mCard.setImageResource(bundle.getInt(KEY_CARD));
        mStar.setImageResource(bundle.getInt(KEY_STAR));
        mComment.setText(bundle.getInt(KEY_COMMENT));
        mAvatar.setImageResource(bundle.getInt(KEY_AVATAR));
        return root;
    }

    public void show() {
        Log.d(TAG, "show: " + this + "  " +mContainer);
        if (mContainer != null) {
            mContainer.setVisibility(View.VISIBLE);
        }
    }

    public void hide() {
        if (mContainer != null) {
            mContainer.setVisibility(View.GONE);
        }
    }
}
