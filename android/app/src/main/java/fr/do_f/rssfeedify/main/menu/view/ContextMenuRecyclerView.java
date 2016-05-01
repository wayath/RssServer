package fr.do_f.rssfeedify.main.menu.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ContextMenu;
import android.view.View;

/**
 * Created by do_f on 30/04/16.
 */

public class ContextMenuRecyclerView extends RecyclerView {

    private RecyclerContextMenuInfo mContextMenuInfo;

    public ContextMenuRecyclerView(Context context) {
        super(context);
        Log.d("TAMERE", "TAMERE");
    }


    public ContextMenuRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.d("TAMERE", "TAMERE");

    }

    public ContextMenuRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Log.d("TAMERE", "TAMERE");

    }

    @Override
    protected ContextMenu.ContextMenuInfo getContextMenuInfo() {
        Log.d("TAMERE", "TAMERE");
        return mContextMenuInfo;
    }

    @Override
    public boolean showContextMenuForChild(View originalView) {
        Log.d("TAMERE", "TAMERE");
        final int longPressPosition = getChildAdapterPosition(originalView);
        if (longPressPosition >= 0) {
            final long longPressId = getAdapter().getItemId(longPressPosition);
            mContextMenuInfo = new RecyclerContextMenuInfo(longPressPosition, longPressId);
            return super.showContextMenuForChild(originalView);
        }
        return false;
    }

    public static class RecyclerContextMenuInfo implements ContextMenu.ContextMenuInfo {

        public RecyclerContextMenuInfo(int position, long id) {
            this.position = position;
            this.id = id;
        }

        final public int position;
        final public long id;
    }

}