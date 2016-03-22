package eu.davidea.flexibleadapter.items;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;

/**
 * Basic interface to manage operations like enabling, selecting, hiding, filtering on items.
 * <p>Implements this interface or use {@link AbstractFlexibleItem}.</p>
 *
 * @author Davide Steduto
 * @since 19/01/2016 Created
 */
public interface IFlexibleItem<VH extends RecyclerView.ViewHolder> {

	/*---------------*/
	/* BASIC METHODS */
	/*---------------*/

	/**
	 * Return if the Item is enabled.
	 *
	 * @return (default) true for enabled item, false for disabled one.
	 */
	boolean isEnabled();

	/**
	 * Setter to change enabled behaviour.
	 *
	 * @param enabled false to disable all operations on this item
	 */
	void setEnabled(boolean enabled);

	/**
	 * Return if the Item is hidden.
	 *
	 * @return true for hidden item, (default) false for the shown one.
	 */
	boolean isHidden();

	/**
	 * Setter to change hidden behaviour. Useful while filtering this item.
	 * Default value is false.
	 *
	 * @param hidden true if this item should remain hidden, false otherwise
	 */
	void setHidden(boolean hidden);

	/*--------------------*/
	/* SELECTABLE METHODS */
	/*--------------------*/

	/**
	 * Return if the item can be selected.<br/>
	 *
	 * @return (default) true for a Selectable item, false otherwise
	 */
	boolean isSelectable();

	/**
	 * Setter to change selectable behaviour.
	 *
	 * @param selectable false to disable selection on this item
	 */
	void setSelectable(boolean selectable);

	boolean isSelected();

	void setSelected(boolean selected);

	/*-------------------*/
	/* TOUCHABLE METHODS */
	/*-------------------*/

	boolean isDraggable();

	void setDraggable(boolean draggable);

	boolean isSwipeable();

	void setSwipeable(boolean swipeable);

	/*---------------------*/
	/* VIEW HOLDER METHODS */
	/*---------------------*/

	/**
	 * Returns the layout resource Id to auto-map a specific ViewType on this Item.
	 * <p><b>NOTE:</b> Should identify a resource Layout reference {@link android.R.layout} used
	 * by FlexibleAdapter to auto-map the ViewTypes.</p>
	 * <b>HELP:</b> To know how to implement auto-map for ViewTypes please refer to the
	 * <a href="https://github.com/davideas/FlexibleAdapter/wiki">FlexibleAdapter WikiPage</a>
	 * on GitHub.
	 *
	 * @return Layout identifier
	 */
	@LayoutRes
	int getLayoutRes();

	/**
	 * Delegates the creation of the ViewHolder to the user, if auto-map has been implemented.
	 * <p><b>HELP:</b> To know how to implement auto-map for ViewTypes please refer to the
	 * <a href="https://github.com/davideas/FlexibleAdapter/wiki">FlexibleAdapter WikiPage</a>
	 * on GitHub.</p>
	 *
	 * @param adapter  the Adapter instance extending {@link FlexibleAdapter}
	 * @param inflater the {@link LayoutInflater} for the itemView
	 * @param parent   the ViewGroup into which the new View will be added after it is bound
	 *                 to an adapter position
	 * @return a new ViewHolder that holds a View of the given view type
	 */
	VH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent);

	/**
	 * Binds the data of this item to the given Layout, if auto-map has been implemented.
	 * <p><b>HELP:</b> To know how to implement auto-map for ViewTypes please refer to the
	 * <a href="https://github.com/davideas/FlexibleAdapter/wiki">FlexibleAdapter WikiPage</a>
	 * on GitHub.</p>
	 * How to use Payload, please refer to
	 * {@link android.support.v7.widget.RecyclerView.Adapter#onBindViewHolder(RecyclerView.ViewHolder, int, List)}.
	 *
	 * @param adapter  the FlexibleAdapter instance
	 * @param holder   the ViewHolder instance
	 * @param position the current position
	 * @param payloads a non-null list of merged payloads. Can be empty list if requires full
	 *                 update.
	 */
	void bindViewHolder(FlexibleAdapter adapter, VH holder, int position, List payloads);

}