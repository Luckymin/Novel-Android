package com.startsmake.novel.ui.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.startsmake.novel.R;

/**
 * User:Shine
 * Date:2015-10-21
 * Description:
 */
public class EmptyViewHolder extends RecyclerView.ViewHolder {

    public TextView tvEmptyHint;

    public EmptyViewHolder(View itemView) {
        super(itemView);
        tvEmptyHint = (TextView) itemView.findViewById(R.id.tvEmptyHint);
    }
}
