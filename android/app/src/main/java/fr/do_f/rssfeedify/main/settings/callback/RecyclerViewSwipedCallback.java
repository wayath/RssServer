package fr.do_f.rssfeedify.main.settings.callback;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

import fr.do_f.rssfeedify.main.menu.adapter.MenuAdapter;
import fr.do_f.rssfeedify.main.settings.adapter.AdminAdapter;

/**
 * Created by do_f on 24/04/16.
 */
public class RecyclerViewSwipedCallback extends ItemTouchHelper.Callback {

    private AdminAdapter    aAdapter = null;
    private  MenuAdapter    mAdapter = null;

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    public RecyclerViewSwipedCallback(AdminAdapter adapter) {
        this.aAdapter = adapter;
    }

    public RecyclerViewSwipedCallback(MenuAdapter adapter) {
        mAdapter = adapter;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(0, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        Log.d("TEST", "onSwiped + "+direction);
        if (aAdapter != null)
            aAdapter.onItemDismiss(viewHolder.getAdapterPosition());
        else if (mAdapter != null)
            mAdapter.onItemDismiss(viewHolder.getAdapterPosition());
    }
}
