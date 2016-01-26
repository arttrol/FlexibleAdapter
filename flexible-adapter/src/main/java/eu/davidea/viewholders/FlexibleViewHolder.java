package eu.davidea.viewholders;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.SelectableAdapter;
import eu.davidea.flexibleadapter.helpers.ItemTouchHelperCallback;

/**
 * Helper Class that implements:
 * <br/>- Single tap
 * <br/>- Long tap
 * <br/>- Touch for Drag and Swipe.
 * <p>You must extend and implement this class for the own ViewHolder.</p>
 *
 * @author Davide Steduto
 * @since 03/01/2016 Created<br/>23/01/2016 ItemTouch
 */
public abstract class FlexibleViewHolder extends RecyclerView.ViewHolder
		implements View.OnClickListener, View.OnLongClickListener,
		View.OnTouchListener, ItemTouchHelperCallback.ViewHolderCallback {

	private static final String TAG = FlexibleViewHolder.class.getSimpleName();

	protected final FlexibleAdapter mAdapter;

	/* These 2 fields avoid double tactile feedback triggered by Android and allow to Drag an
	   item maintaining LongClick events for ActionMode, all at the same time */
	protected int mActionState = ItemTouchHelper.ACTION_STATE_IDLE;
	private boolean mLongClickSkipped = false;

	/*--------------*/
	/* CONSTRUCTORS */
	/*--------------*/

	/**
	 * Default constructor.
	 *
	 * @param view    The {@link View} being hosted in this ViewHolder
	 * @param adapter Adapter instance of type {@link FlexibleAdapter}
	 */
	public FlexibleViewHolder(View view, FlexibleAdapter adapter) {
		super(view);
		this.mAdapter = adapter;
		this.itemView.setOnClickListener(this);
		this.itemView.setOnLongClickListener(this);
	}

	/*--------------------------------*/
	/* CLICK LISTENERS IMPLEMENTATION */
	/*--------------------------------*/

	/**
	 * {@inheritDoc}
	 */
	@Override
	@CallSuper
	public void onClick(View view) {
		//Experimented that, if LongClick is not consumed, onClick is fired. We skip the
		//call to the listener in this case, which is allowed only in ACTION_STATE_IDLE.
		if (mAdapter.mItemClickListener != null && mActionState == ItemTouchHelper.ACTION_STATE_IDLE) {
			if (FlexibleAdapter.DEBUG)
				Log.v(TAG, "onClick on position " + getAdapterPosition() + " mode=" + mAdapter.getMode());
			if (mAdapter.mItemClickListener.onItemClick(getAdapterPosition()))
				toggleActivation();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@CallSuper
	public boolean onLongClick(View view) {
		if (FlexibleAdapter.DEBUG)
			Log.v(TAG, "onLongClick on position " + getAdapterPosition() + " mode=" + mAdapter.getMode());
		//If Drag is enabled with LongPress, LongClick is therefore skipped and
		//LongClick listener will be launched in onActionStateChanged.
		if (mAdapter.mItemLongClickListener != null && !mAdapter.isLongPressDragEnabled()) {
			mAdapter.mItemLongClickListener.onItemLongClick(getAdapterPosition());
			toggleActivation();
			return true;
		}
		mLongClickSkipped = true;
		return false;
	}

	/**
	 * <b>Should be used only by the Handle View!</b><br/>
	 * {@inheritDoc}
	 *
	 * @see #setDragHandleView(View)
	 */
	@Override
	public boolean onTouch(View view, MotionEvent event) {
		if (FlexibleAdapter.DEBUG)
			Log.v(TAG, "onTouch with DragHandleView on position " + getAdapterPosition() + " mode=" + mAdapter.getMode());
		if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN &&
				mAdapter.isHandleDragEnabled()) {
			//Start Drag!
			mAdapter.getItemTouchHelper().startDrag(FlexibleViewHolder.this);
		}
		return false;
	}

	/*--------------*/
	/* MAIN METHODS */
	/*--------------*/

	/**
	 * Sets the inner view which will be used to drag the Item ViewHolder.
	 *
	 * @param view handle view
	 * @see #onTouch(View, MotionEvent)
	 */
	protected final void setDragHandleView(@NonNull View view) {
		if (view != null) view.setOnTouchListener(this);
	}

	/**
	 * Allows to change and see the activation status on the ItemView and to perform object
	 * animation in it.
	 * <p><b>IMPORTANT NOTE!</b> the change of the background is visible if you added
	 * <i>android:background="?attr/selectableItemBackground"</i> on the item layout AND
	 * in the style.xml.<br/>
	 * Adapter must have a reference to its instance to check selection state.</p>
	 * <p>
	 * This must be called every time we want the activation state visible on the ItemView,
	 * for instance, after a Click (to add the item to the selection list) or after a LongClick
	 * (to activate the ActionMode) or during a Drag (to show that we enabled the Drag).
	 * </p>
	 * If you do this, it's not necessary to invalidate the row (with notifyItemChanged):
	 * In this way <i>onBindViewHolder</i> is NOT called and inner Views can animate without
	 * interruption, so you can see the animation running still having the selection activated.
	 */
	@CallSuper
	protected void toggleActivation() {
		itemView.setActivated(mAdapter.isSelected(getAdapterPosition()));
	}

	/*--------------------------------*/
	/* TOUCH LISTENERS IMPLEMENTATION */
	/*--------------------------------*/

	/**
	 * Here we handle the event of when the ItemTouchHelper first registers an item as being
	 * moved or swiped.
	 * <p>In this implementations, View activation is automatically handled in case of Drag:
	 * The Item will be added to the selection list if not selected yet and mode MULTI is activated.</p>
	 *
	 * @param position    the position of the item touched
	 * @param actionState one of {@link ItemTouchHelper#ACTION_STATE_SWIPE} or
	 *                    {@link ItemTouchHelper#ACTION_STATE_DRAG}.
	 */
	@Override
	@CallSuper
	public void onActionStateChanged(int position, int actionState) {
		mActionState = actionState;
		if (FlexibleAdapter.DEBUG)
			Log.v(TAG, "onActionStateChanged position=" + position + " mode=" + mAdapter.getMode() +
					" actionState=" + (actionState == ItemTouchHelper.ACTION_STATE_SWIPE ? "Swipe(1)" : "Drag(2)"));
		if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
			if (!mAdapter.isSelected(getAdapterPosition())) {
				//Be sure, if MODE_MULTI is active, add this item to the selection list (call listener!)
				//Also be sure user consumes the long click event if not done in onLongClick.
				if (mAdapter.mItemLongClickListener != null &&
						(mLongClickSkipped || mAdapter.getMode() == SelectableAdapter.MODE_MULTI)) {
					mLongClickSkipped = false;
					mAdapter.mItemLongClickListener.onItemLongClick(getAdapterPosition());
				} else {
					//If not, be sure current item appears selected for the Drag transition
					mAdapter.toggleSelection(getAdapterPosition());
				}
			}
			//Activate view and make selection visible if necessary
			if (!itemView.isActivated()) toggleActivation();
		}
	}

	/**
	 * Here we handle the event of when the ItemTouchHelper has completed the move or swipe.
	 * <p>In this implementation, View activation is automatically handled.</p>
	 * In case of Drag, the state will be cleared depends by current selection mode!
	 *
	 * @param position the position of the item released
	 */
	@Override
	@CallSuper
	public void onItemReleased(int position) {
		if (FlexibleAdapter.DEBUG)
			Log.v(TAG, "onItemReleased position=" + position + " mode=" + mAdapter.getMode() +
					" actionState=" + (mActionState == ItemTouchHelper.ACTION_STATE_SWIPE ? "Swipe(1)" : "Drag(2)"));
		//Be sure to remove selection if not MODE_MULTI, otherwise keep the selection
		if (mAdapter.getMode() != FlexibleAdapter.MODE_MULTI) {
			if (mAdapter.isSelected(position)) {
				mAdapter.toggleSelection(position);
			}
			if (itemView.isActivated()) {
				toggleActivation();
			}
		}
		//Reset internal action state ready for next event
		mActionState = ItemTouchHelper.ACTION_STATE_IDLE;
	}

}