package com.wty.ution.widget.navigation;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;

import com.wty.ution.R;
import com.wty.ution.ui.activity.BaseActivity;
import com.wty.ution.util.CommonUtil;
import com.wty.ution.widget.NoScrollListView;
import com.wty.ution.widget.titlemenu.TitleMenuItemView;
import com.wty.ution.widget.titlemenu.TitleMenuLabel;
import com.wty.ution.widget.titlemenu.TitleMenuModel;
import com.wty.ution.widget.titlemenu.TitleMenuModel.TitleMenuEvent;

import java.util.ArrayList;
import java.util.List;


/**
 * @author wty
 *	应用下拉控件
 */
public class NavigationTitleMenu extends NavigationContainer{

    private boolean initSelect = true;
    private List<TitleMenuModel> items = new ArrayList<TitleMenuModel>();
	
	private MenuAdapter adapter;
	private TitleMenuLabel label;

	private PopupWindow window;
	private View root;
	private NoScrollListView listview;
	
	private TitleMenuModel lastSelectedModel;

	public NavigationTitleMenu(Context context) {
		super(context);
	}
	
	public NavigationTitleMenu(Context context,AttributeSet attr) {
		super(context,attr);
	}
	
	@Override
	public View initCenterView() {
		View view = LayoutInflater.from(getContext()).inflate(R.layout.navigation_titlemenu, null);
		label = (TitleMenuLabel)view.findViewById(R.id.navigation_titlemenu_label);
		label.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (window != null && window.isShowing()) {
                    dismissMenu();
                    label.onNormal();
                } else {
                    showMenu();
                    label.onShow();
                }

            }
        });
		return view;
	}

	private void initMenu(){
		View view = LayoutInflater.from(getContext()).inflate(R.layout.navigation_titlemenu_window, null);
		window = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT);
		window.setAnimationStyle(R.style.AnimationFade);
		window.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				label.onNormal();
			}
		});
		window.setFocusable(true);
		window.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_alpha));
		adapter = new MenuAdapter(getContext(), items);
		listview = (NoScrollListView)view.findViewById(R.id.navigation_titlemenu_listview);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(itemClickListener);
		root = view.findViewById(R.id.navigation_titlemenu_root);
		root.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				dismissMenu();
			}
		});

        if(getContext() instanceof BaseActivity){
            ((BaseActivity)getContext()).setOnBackClickListener(new BaseActivity.OnBackClickListener() {
                @Override
                public boolean onClick() {
                    if (window != null && window.isShowing()) {
                        dismissMenu();
                        label.onNormal();
                        return true;
                    }
                    return true;
                }
            });
        }
	}
	
	public void showMenu(){
		if(window==null){
			initMenu();
		}
         //设置位置
		 window.showAsDropDown(getRootView(), 0, -CommonUtil.dip2px(getContext(), 5));
	}
	
	public void dismissMenu(){
		if(window!=null && window.isShowing()){
			window.dismiss();
		}
	}
	
	public NavigationTitleMenu addItem(TitleMenuModel item){
		this.items.add(item);
		return this;
	}

    public NavigationTitleMenu setItems(List<TitleMenuModel> items){
        this.items.clear();
        this.items.addAll(items);
        return this;
    }

    public NavigationTitleMenu setInitSelect(boolean initSelect){
        this.initSelect =  initSelect;
        return this;
    }

	@Override
	public <T extends NavigationContainer> T build() {
        label.setTitle("");
		for(TitleMenuModel model:items){
			TitleMenuEvent event = model.getEvent();
			if(model.isSelected()){
				lastSelectedModel = model;
				lastSelectedModel.setSelected(true);
				label.setTitle(model.getText());
                if(initSelect){
                    if(event!=null)event.onSelect();
                }
			}else{
				if(event!=null)event.onNormal();
			}
		}
		return (T)this;
	}
	
	class MenuAdapter extends BaseAdapter{
		
		List<TitleMenuModel> models;
		Context context;
		public MenuAdapter(Context context,List<TitleMenuModel> models) {
			this.models = models;
			this.context = context;
		}
		
		@Override
		public int getCount() {
			return models.size();
		}

		@Override
		public TitleMenuModel getItem(int position) {
			return models.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TitleMenuItemView.Builder builder = null;
			if(convertView==null){
				builder =  new TitleMenuItemView.Builder(context);
				convertView = builder.create();
				convertView.setTag(builder);
			}else{
				builder = (TitleMenuItemView.Builder) convertView.getTag();
			}
			
			TitleMenuModel model = getItem(position);
			builder.setSelected(model.isSelected());
			builder.setText(model.getText());
			builder.setTotal(model.getTotal(), model.isIgnoreTotal());
			builder.setEvent(model.getEvent());
			builder.setUnread(model.getUnread());

			((TitleMenuItemView)convertView).setBuilder(builder);
			
			return convertView;
		}
		
	}
	
	OnItemClickListener itemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			try {
				dismissMenu();
				if(lastSelectedModel!=null){
					lastSelectedModel.setSelected(false);
					TitleMenuEvent event = lastSelectedModel.getEvent(); 
					if(event !=null){
						event.onNormal();
					}
				}
				TitleMenuModel model = (TitleMenuModel) listview.getItemAtPosition(position);
				if(model!=null){
					model.setSelected(true);
					lastSelectedModel = model;
					label.setTitle(model.getText());
					TitleMenuEvent event = model.getEvent();
					if(event!=null){
						event.onSelect();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
		}
	};
}