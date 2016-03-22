package eu.davidea.flexibleadapter.items;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;

/**
 * Generic implementation of {@link IFlexible} interface with most useful methods to manage
 * selection and view holder methods.
 *
 * @author Davide Steduto
 * @since 20/01/2016 Created
 */
public abstract class AbstractFlexibleItem<VH extends RecyclerView.ViewHolder>
		implements IFlexible<VH> {

	/* Item flags recognized by the FlexibleAdapter */
	boolean mEnabled = true, mHidden = false,
			mSelectable = true, mSelected = false,
			mDraggable = false, mSwipeable = false;

	/*---------------*/
	/* BASIC METHODS */
	/*---------------*/

	/**
	 * You <b>must</b> implement this method to compare items Identifiers.
	 * <p>Adapter needs this method to distinguish them and pick up correct items.</p>
	 *
	 * @param o instance to compare
	 * @return true if items are equals, false otherwise.
	 */
	@Override
	public abstract boolean equals(Object o);

	@Override
	public boolean isEnabled() {
		return mEnabled;
	}

	@Override
	public void setEnabled(boolean enabled) {
		mEnabled = enabled;
	}

	@Override
	public boolean isHidden() {
		return mHidden;
	}

	@Override
	public void setHidden(boolean hidden) {
		mHidden = hidden;
	}

	/*--------------------*/
	/* SELECTABLE METHODS */
	/*--------------------*/

	@Override
	public boolean isSelectable() {
		return mSelectable;
	}

	@Override
	public void setSelectable(boolean selectable) {
		this.mSelectable = selectable;
	}

	@Override
	public boolean isSelected() {
		return mSelected;
	}

	@Override
	public void setSelected(boolean selected) {
		this.mSelected = selected;
	}

	/*-------------------*/
	/* TOUCHABLE METHODS */
	/*-------------------*/

	@Override
	public boolean isDraggable() {
		return mDraggable;
	}

	@Override
	public void setDraggable(boolean draggable) {
		mDraggable = draggable;
	}

	@Override
	public boolean isSwipeable() {
		return mSwipeable;
	}

	@Override
	public void setSwipeable(boolean swipeable) {
		mSwipeable = swipeable;
	}

	/*---------------------*/
	/* VIEW HOLDER METHODS */
	/*---------------------*/

	private static final String MAPPING_ILLEGAL_STATE = "If you want FlexibleAdapter creates and binds ViewHolder for you, you must override and implement ";

//	/**
//	 * Wrapper of {#getLayoutRes()}.
//	 * <p>It DOESN'T belongs to the {@link IFlexible} interface!!</p>
//	 *
//	 * @return the layout resourceId as viewType
//	 */
//	public int getViewType() {
//		return getLayoutRes();
//	}

	@Override
	public int getLayoutRes() {
		throw new IllegalStateException(MAPPING_ILLEGAL_STATE + this.getClass().getSimpleName() + ".getLayoutRes() method.");
	}

	@Override
	public VH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
		throw new IllegalStateException(MAPPING_ILLEGAL_STATE + this.getClass().getSimpleName() + ".createViewHolder() method.");
	}

	@Override
	public void bindViewHolder(FlexibleAdapter adapter, VH holder, int position, List payloads) {
		throw new IllegalStateException(MAPPING_ILLEGAL_STATE + this.getClass().getSimpleName() + ".bindViewHolder() method.");
	}

}