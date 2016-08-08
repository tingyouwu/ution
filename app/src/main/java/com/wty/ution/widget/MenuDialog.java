package com.wty.ution.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.wty.ution.R;


public class MenuDialog extends Dialog {

	public MenuDialog(Context context, int theme) {
		super(context, theme);
	}

	public MenuDialog(Context context) {
		super(context);
	}

	public void setLayout(double width, double height) {
		getWindow().setLayout((int) width, (int) height);
	}

	public void setAnimStyle(int styleId) {
		if (getWindow() != null) {
			getWindow().setWindowAnimations(styleId);
		}
	}

	/**
	 * Helper class for creating a custom dialog
	 */
	public static class Builder {

		private static final int DEFAULT_STYLE = R.style.MenuDialog;

		private LinearLayout mTitlePanel;
		private ImageView mIconImageView;
		private TextView mTitleTextView;
		private CharSequence mTitleText;
		private Drawable mIcon;

		private LinearLayout mContentPanel;
		private ScrollView mMessagePanel;
		private TextView mMessageTextView;
		private CharSequence mMessageText;

		private View mCustomView;

		private LinearLayout mButtonPanel;
		private Button mPositiveButton;
		private Button mNegativeButton;
		private Button mNeutralButton;
		private View mDividerLeftView;
		private View mDividerRightView;
		private CharSequence mPositiveText;
		private CharSequence mNegativeText;
		private CharSequence mNeutralText;
		private OnClickListener mPositiveButtonListener;
		private OnClickListener mNegativeButtonListener;
		private OnClickListener mNeutralButtonListener;
		private OnCancelListener mCancelListener;
		private ListView mListView;

		protected ListAdapter mAdapter;
		protected ListAdapter mSingleChoiceAdapter;
		protected CharSequence[] mItems;
		protected OnClickListener mItemListener;
		protected int mItemResourceId = -1;
		protected int mItemTextViewId = -1;
		protected int mCheckedItemIndex;
		protected boolean mIsSingleChoice;
		protected boolean mCanceledOnTouchOutside = true;

		protected Context mContext;
		protected MenuDialog mDialog;

		public Builder(Context context) {
			this.mContext = context;
		}

		/**
		 * Set the resource id of the icon to be used in the title.
		 *
		 * @param iconId
		 * @return
		 */
		public Builder setIcon(int iconId) {
//			this.mIcon = getDrawable(mContext, iconId);
			return this;
		}

		/**
		 * Set the icon to be used in the title.
		 *
		 * @param icon
		 * @return
		 */
		public Builder setIcon(Drawable icon) {
//			this.mIcon = icon;//去掉图标
			return this;
		}

		/**
		 * Set the Dialog title.
		 *
		 * @param titleId
		 * @return
		 */
		public Builder setTitle(int titleId) {
			this.mTitleText = getText(mContext, titleId);
			return this;
		}

		/**
		 * Set the Dialog title.
		 *
		 * @param title
		 * @return
		 */
		public Builder setTitle(CharSequence title) {
			this.mTitleText = title;
			return this;
		}

		/**
		 * Set the Dialog message.
		 *
		 * @param messageId
		 * @return
		 */
		public Builder setMessage(int messageId) {
			this.mMessageText = getText(mContext, messageId);
			return this;
		}

		/**
		 * Set the Dialog message.
		 *
		 * @param message
		 * @return
		 */
		public Builder setMessage(CharSequence message) {
			this.mMessageText = message;
			return this;
		}

		/**
		 * Set a custom content view for the Dialog. If a message is set, the
		 * custom view will remove message view and be added to the Dialog.
		 *
		 * @param v
		 * @return
		 */
		public Builder setContentView(View v) {
			this.mCustomView = v;
			return this;
		}

		public Builder setOnCancelListener(OnCancelListener onCancelListener){
			this.mCancelListener = onCancelListener;
			return this;
		}

		public Builder setCanceledOnTouchOutside(boolean canceled){
			this.mCanceledOnTouchOutside = canceled;
			return this;
		}

		/**
		 * Set the text to display in positive button and a listener to be
		 * invoked when it is pressed.
		 *
		 * @param textId
		 * @param listener
		 * @return
		 */
		public Builder setPositiveButton(int textId,
				OnClickListener listener) {
			this.mPositiveText = getText(mContext, textId);
			this.mPositiveButtonListener = listener;
			return this;
		}

		/**
		 * Set the text to display in positive button and a listener to be
		 * invoked when it is pressed.
		 *
		 * @param text
		 * @param listener
		 * @return
		 */
		public Builder setPositiveButton(CharSequence text,
				OnClickListener listener) {
			this.mPositiveText = text;
			this.mPositiveButtonListener = listener;
			return this;
		}

		/**
		 * Set the text to display in negative button and a listener to be
		 * invoked when it is pressed.
		 *
		 * @param textId
		 * @param listener
		 * @return
		 */
		public Builder setNegativeButton(int textId,
				OnClickListener listener) {
			this.mNegativeText = getText(mContext, textId);
			this.mNegativeButtonListener = listener;
			return this;
		}

		/**
		 * Set the text to display in negative button and a listener to be
		 * invoked when it is pressed.
		 *
		 * @param text
		 * @param listener
		 * @return
		 */
		public Builder setNegativeButton(CharSequence text,
				OnClickListener listener) {
			this.mNegativeText = text;
			this.mNegativeButtonListener = listener;
			return this;
		}

		/**
		 * Set the text to display in neutral button and a listener to be
		 * invoked when it is pressed.*
		 *
		 * @param textId
		 * @param listener
		 * @return
		 */
		public Builder setNeutralButton(int textId,
				OnClickListener listener) {
			this.mNeutralText = getText(mContext, textId);
			this.mNeutralButtonListener = listener;
			return this;
		}

		/**
		 * Set the text to display in neutral button and a listener to be
		 * invoked when it is pressed.
		 *
		 * @param text
		 * @param listener
		 * @return
		 */
		public Builder setNeutralButton(CharSequence text,
				OnClickListener listener) {
			this.mNeutralText = text;
			this.mNeutralButtonListener = listener;
			return this;
		}

		/**
		 * Set a list of items to be displayed in the dialog as the content, you
		 * will be notified of the selected item via the supplied listener.
		 * 
		 * @return
		 */
		public Builder setItems(int itemsId, OnClickListener listener) {
			this.mItems = getTextArray(mContext, itemsId);
			this.mItemListener = listener;
			return this;
		}

		/**
		 * Set a list of items to be displayed in the dialog as the content, you
		 * will be notified of the selected item via the supplied listener.
		 * 
		 * @return
		 */
		public Builder setItems(CharSequence[] items, OnClickListener listener) {
			this.mItems = items;
			this.mItemListener = listener;
			return this;
		}

		/**
		 * Set a list of items to be displayed in the dialog as the content, you
		 * will be notified of the selected item via the supplied listener.
		 * 
		 * @param itemsId
		 * @param resourceId
		 *            item layout XML name.
		 * @param textviewId
		 *            TextView id setup item data.
		 * @param listener
		 * @return
		 */
		public Builder setItems(int itemsId, int resourceId, int textviewId,
				final OnClickListener listener) {
			this.mItems = getTextArray(mContext, itemsId);
			this.mItemListener = listener;
			this.mItemResourceId = resourceId;
			this.mItemTextViewId = textviewId;
			return this;
		}

		/**
		 * Set a list of items to be displayed in the dialog as the content, you
		 * will be notified of the selected item via the supplied listener.
		 * 
		 * @param items
		 * @param resourceId
		 *            item layout XML name.
		 * @param textviewId
		 *            TextView id setup item data.
		 * @param listener
		 * @return
		 */
		public Builder setItems(CharSequence[] items, int resourceId,
				int textviewId, final OnClickListener listener) {
			this.mItems = items;
			this.mItemListener = listener;
			this.mItemResourceId = resourceId;
			this.mItemTextViewId = textviewId;
			return this;
		}

		/**
		 * Set a list of items to be displayed in the dialog as the content, you
		 * will be notified of the selected item via the supplied listener. The
		 * list item will have a check mark displayed for the checked item.
		 * Clicking on an item in the list will not dismiss the dialog. Clicking
		 * on a button will dismiss the dialog.
		 * 
		 * @param itemsId
		 *            the resource id of an array i.e. R.array.foo
		 * @param checkedItem
		 *            specifies which item is checked. If -1 no items are
		 *            checked.
		 * @param listener
		 *            notified when an item on the list is clicked. The dialog
		 *            will not be dismissed when an item is clicked. It will
		 *            only be dismissed if clicked on a button, if no buttons
		 *            are supplied it's up to the user to dismiss the dialog.
		 * 
		 * @return
		 */
		public Builder setSingleChoiceItems(int itemsId, int checkedItem,
				final OnClickListener listener) {
			this.mItems = mContext.getResources().getTextArray(itemsId);
			this.mItemListener = listener;
			this.mCheckedItemIndex = checkedItem;
			this.mIsSingleChoice = true;
			return this;
		}

		/**
		 * Set a list of items to be displayed in the dialog as the content, you
		 * will be notified of the selected item via the supplied listener. The
		 * list item will have a check mark displayed for the checked item.
		 * Clicking on an item in the list will not dismiss the dialog. Clicking
		 * on a button will dismiss the dialog.
		 * 
		 * @param items
		 *            the items to be displayed.
		 * @param checkedItem
		 *            specifies which item is checked. If -1 no items are
		 *            checked.
		 * @param listener
		 *            notified when an item on the list is clicked. The dialog
		 *            will not be dismissed when an item is clicked. It will
		 *            only be dismissed if clicked on a button, if no buttons
		 *            are supplied it's up to the user to dismiss the dialog.
		 * 
		 * @return
		 */
		public Builder setSingleChoiceItems(CharSequence[] items,
				int checkedItem, final OnClickListener listener) {
			this.mItems = items;
			this.mItemListener = listener;
			this.mCheckedItemIndex = checkedItem;
			this.mIsSingleChoice = true;
			return this;
		}

		/**
		 * 
		 * 
		 * @param items
		 *            the items to be displayed.
		 * @param checkedItem
		 *            specifies which item is checked. If -1 no items are
		 *            checked.
		 * @param listener
		 *            notified when an item on the list is clicked. The dialog
		 *            will not be dismissed when an item is clicked. It will
		 *            only be dismissed if clicked on a button, if no buttons
		 *            are supplied it's up to the user to dismiss the dialog.
		 * 
		 * @return
		 */
		/**
		 * Set a list of items to be displayed in the dialog as the content, you
		 * should be process the item checked event in the supplied adapter.
		 * 
		 * @param adapter
		 * @return
		 */
		public Builder setItems(ListAdapter adapter) {
			this.mSingleChoiceAdapter = adapter;
			return this;
		}

		/**
		 * Create the dialog.
		 */
		public MenuDialog create() {

			mDialog = new MenuDialog(mContext, DEFAULT_STYLE);

			LayoutInflater inflater = LayoutInflater.from(mContext);
			View contentView = inflater.inflate(R.layout.menu_dialog_crm, null);

			setupTitlePanel(contentView); // set title panel

			setupContentPanel(contentView); // set content panel

			setupButtonPanel(contentView); // set button panel

            setupListView(contentView); // set list view

			setupCustomView(contentView); // set custom view

			mDialog.setContentView(contentView);
			
			if(mCancelListener!=null){
				mDialog.setOnCancelListener(mCancelListener);
			}
			setCanceledOnTouchOutside(mCanceledOnTouchOutside);
			mDialog.setCanceledOnTouchOutside(mCanceledOnTouchOutside);
			
			setupWindowParams();
			
			return mDialog;
		}
		
		private void setupWindowParams() {
			mDialog.setAnimStyle(R.style.MenuDialog);

			WindowManager wManager = mDialog.getWindow().getWindowManager();
			double width = wManager.getDefaultDisplay().getWidth() * 0.8;
			mDialog.setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT);
		}

		/**
		 * Setup title panel layout.
		 */
		private void setupTitlePanel(View contentView) {
			mTitlePanel = (LinearLayout) contentView.findViewById(R.id.title_panel);
			mIconImageView = (ImageView) contentView.findViewById(R.id.icon);
			mTitleTextView = (TextView) contentView.findViewById(R.id.title);

			if (mIcon != null) {
				mIconImageView.setImageDrawable(mIcon);
			} else {
				mIconImageView.setVisibility(View.GONE);
			}
			if (!TextUtils.isEmpty(mTitleText)) {
				mTitleTextView.setText(mTitleText);
			} else {
				mTitlePanel.setVisibility(View.GONE);
			}
		}

		/**
		 * Setup content panel layout.
		 */
		private void setupContentPanel(View contentView) {
			mContentPanel = (LinearLayout) contentView.findViewById(R.id.content_panel);
			mMessageTextView = (TextView) contentView.findViewById(R.id.message);
			mMessagePanel = (ScrollView) contentView.findViewById(R.id.message_panel);

			if (!TextUtils.isEmpty(mMessageText)) {
				mMessageTextView.setText(mMessageText);
			} else {
				mMessagePanel.setVisibility(View.GONE);
			}
		}

		/**
		 * Setup list view data.
		 */
		public void setupListView(View contentView) {
			setmListView((ListView) contentView.findViewById(R.id.lv));

			IOnClickItemListener itemListener = null;
			if (mItemListener != null) {
				itemListener = new IOnClickItemListener() {

					@Override
					public void onClickItem(View view, int position) {
						mItemListener.onClick(mDialog, position);
						if (!mIsSingleChoice) {
							mDialog.dismiss();
						}
					}
				};
			}

			if (mItems != null && mItems.length > 0) {
				if (!mIsSingleChoice) {
					if (mItemResourceId < 0 || mItemTextViewId < 0) {
						mAdapter = new ItemAdapter<CharSequence>(mContext,
								mItems, itemListener);
					} else {
						mAdapter = new ItemAdapter<CharSequence>(mContext,
								mItems, mItemResourceId, mItemTextViewId,
								itemListener);
					}
				} else {
					if (mSingleChoiceAdapter == null) {
						mAdapter = new ItemAdapter<CharSequence>(mContext,
								mItems, mCheckedItemIndex, true, itemListener);
					}
				}
			}
			if (mSingleChoiceAdapter != null) {
				mAdapter = mSingleChoiceAdapter;
			}
			getmListView().setAdapter(mAdapter);
			if (mAdapter == null) {
				getmListView().setVisibility(View.GONE);
			}
            mTitlePanel.setVisibility(View.GONE);
		}

		/**
		 * Setup button panel layout.
		 */
		private void setupButtonPanel(View contentView) {
			mPositiveButton = (Button) contentView
					.findViewById(R.id.btn_positive);
			mNegativeButton = (Button) contentView
					.findViewById(R.id.btn_negative);
			mNeutralButton = (Button) contentView
					.findViewById(R.id.btn_neutral);
			mDividerLeftView = contentView.findViewById(R.id.view_divider_left);
			mDividerRightView = contentView
					.findViewById(R.id.view_divider_right);
			mButtonPanel = (LinearLayout) contentView
					.findViewById(R.id.button_panel);

			boolean showPositive = false;
			boolean showNegative = false;
			boolean showNeutral = false;

			// set the confirm button visible
			if (!TextUtils.isEmpty(mPositiveText)) {
				showPositive = true;
				mPositiveButton.setVisibility(View.VISIBLE);
				mPositiveButton.setText(mPositiveText);
				mPositiveButton.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						if (mPositiveButtonListener != null) {
							mPositiveButtonListener.onClick(mDialog,
									DialogInterface.BUTTON_POSITIVE);
						}
						mDialog.dismiss();
					}
				});
			} else {
				mPositiveButton.setVisibility(View.GONE);
			}

			// set the cancel button visible
			if (!TextUtils.isEmpty(mNegativeText)) {
				showNegative = true;
				mNegativeButton.setVisibility(View.VISIBLE);
				mNegativeButton.setText(mNegativeText);
				mNegativeButton.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						if (mNegativeButtonListener != null) {
							mNegativeButtonListener.onClick(mDialog,
									DialogInterface.BUTTON_NEGATIVE);
						}
						mDialog.dismiss();
					}
				});
			} else {
				mNegativeButton.setVisibility(View.GONE);
			}

			// set the neutral button visible
			if (!TextUtils.isEmpty(mNeutralText)) {
				showNeutral = true;
				mNeutralButton.setVisibility(View.VISIBLE);
				mNeutralButton.setText(mNeutralText);
				mNeutralButton.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						if (mNeutralButtonListener != null) {
							mNeutralButtonListener.onClick(mDialog,
									DialogInterface.BUTTON_NEUTRAL);
						}
						mDialog.dismiss();
					}
				});
			} else {
				mNeutralButton.setVisibility(View.GONE);
			}

			// set the divider visible
			if (!showNegative) {
				mDividerLeftView.setVisibility(View.GONE);
				if (!showNeutral || !showPositive) {
					mDividerRightView.setVisibility(View.GONE);
				}else{
					mDividerLeftView.setVisibility(View.VISIBLE);
				}
			} else {
				if (showNeutral != showPositive
						&& !(showNeutral && showPositive)) {
					mDividerRightView.setVisibility(View.GONE);
				}
				if (!showNeutral && !showPositive) {
					mDividerRightView.setVisibility(View.GONE);
					mDividerLeftView.setVisibility(View.GONE);
				}
			}

			// set the button panel layout visible
			if (!showPositive && !showNegative && !showNeutral) {
				mButtonPanel.setVisibility(View.GONE);
			}
		}

		/**
		 * Setup custom view.
		 */
		private void setupCustomView(View contentView) {
			if (mCustomView != null) {
				mContentPanel.setVisibility(View.VISIBLE);
				mContentPanel.removeAllViews();
				mContentPanel.addView(mCustomView, new LayoutParams(
						LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			}
		}
		
		public MenuDialog show() {
			mDialog = create();
			mDialog.show();
			return mDialog;
		}

		private CharSequence getText(Context context, int resId) {
			if (context == null) {
				return null;
			}
			try {
				return context.getText(resId);
			} catch (Exception e) {
				android.util.Log.e("test",
						"getText exception: " + e.getMessage());
			}
			return null;
		}

		private CharSequence[] getTextArray(Context context, int resId) {
			if (context == null) {
				return null;
			}
			try {
				return context.getResources().getTextArray(resId);
			} catch (Exception e) {
				android.util.Log.e("test",
						"getTextArray exception: " + e.getMessage());
			}
			return null;
		}

		public Drawable getDrawable(Context context, int iconId) {
			if (context == null) {
				return null;
			}
			try {
				return context.getResources().getDrawable(iconId);
			} catch (Exception e) {
				android.util.Log.e("test",
						"getDrawable exception: " + e.getMessage());
			}
			return null;
		}

		public ListView getmListView() {
			return mListView;
		}

		public void setmListView(ListView mListView) {
			this.mListView = mListView;
		}
	}

	public static interface IOnClickItemListener {

		void onClickItem(View view, int position);
	}

	static class ItemAdapter<T> extends BaseAdapter {

		private static final int DEFAULT_RESOURCE_ID = R.layout.dialog_list_item;
		private static final int DEFAULT_TEXTVIEW_ID = R.id.text1;
//		private static final int DEFAULT_RADIO_BUTTON_ID = R.id.radio_button;

		private int mCheckedItem = -1;
		private int mLayoutResourceId = DEFAULT_RESOURCE_ID;
		private int mTextViewResourceId = DEFAULT_TEXTVIEW_ID;
//		private int mRadioButtonResourceId = DEFAULT_RADIO_BUTTON_ID;

		private boolean mIsSingleChoice;

		private T[] mItems;

		private IOnClickItemListener mListener;

		private Context mContext;

		public ItemAdapter(Context context, T[] objects,
				IOnClickItemListener listener) {
			this(context, objects, DEFAULT_RESOURCE_ID, DEFAULT_TEXTVIEW_ID,
					listener);
		}

		public ItemAdapter(Context context, T[] objects, int resource,
				int textViewResourceId, IOnClickItemListener listener) {
			this.mContext = context;
			this.mLayoutResourceId = resource;
			this.mTextViewResourceId = textViewResourceId;
			this.mItems = objects;
			this.mIsSingleChoice = false;
			this.mListener = listener;
		}

		/**
		 * Constructor for single choice item.
		 */
		public ItemAdapter(Context context, T[] objects, int checkedItem,
				boolean isSingleChoice, IOnClickItemListener listener) {
			this.mContext = context;
			this.mItems = objects;
			this.mCheckedItem = checkedItem;
			this.mIsSingleChoice = isSingleChoice;
			this.mListener = listener;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(mLayoutResourceId, null);
			}
            View view = convertView.findViewById(R.id.dialog_line);
			TextView textView = (TextView) convertView.findViewById(mTextViewResourceId);
			textView.setText(mItems[position].toString());

			View rootView = convertView.findViewById(R.id.root);
			rootView.setTag(position);
			rootView.setOnClickListener(mItemListener);
			textView.setTag(position);
//			textView.setOnClickListener(mItemListener);



            if (position == 0) {
                view.setVisibility(View.VISIBLE);
                if (position == (getCount() - 1)) {
                    //只有一项
                    convertView.setBackgroundResource(R.drawable.app_list_corner_round_selector);
                } else {
                    //第一项
                    convertView.setBackgroundResource(R.drawable.app_list_corner_round_top_selector);
                }
            } else if (position == (getCount() - 1)){
                view.setVisibility(View.GONE);
                //最后一项
                convertView.setBackgroundResource(R.drawable.app_list_corner_round_bottom_selector);
            }
            else {
                view.setVisibility(View.VISIBLE);
                //中间项
                convertView.setBackgroundResource(R.drawable.app_list_corner_round_center_selector);
            }

            return convertView;
		}

		private View.OnClickListener mItemListener = new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				int position = (Integer) v.getTag();
				mCheckedItem = position;
				notifyDataSetChanged();
				if (mListener != null) {
					mListener.onClickItem(v, position);
				}
			}
		};

		@Override
		public int getCount() {
			if (mItems == null) {
				return 0;
			}
			return mItems.length;
		}

		@Override
		public Object getItem(int position) {
			if (mItems == null) {
				return null;
			}
			return mItems[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
	}

}
