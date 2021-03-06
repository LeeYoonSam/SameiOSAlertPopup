package com.ys.popup;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomPopup extends Dialog {

	/*
	 * Define ButtonType
	 */
	public static final int TYPE_OWN_BTN = 1;
	public static final int TYPE_TWO_BTN = 2;

	/*
	 * ButtonType 
	 * ONE Button : 1
	 * TWO Button : 2
	 */
	int mBtType;

	/*
	 * Dialog State
	 */
	public static final int STATE_CREATE = 1;
	public static final int STATE_CLOSE = 2;

	Context _context;

	String mTitle;
	String mContent;
	String mBtnLeft;
	String mBtnRight;

	TextView tvPopupTitle = null;
	TextView tvPopupContent = null;
	ImageView ivPopup = null;
	EditText etPassword = null;

	CustomPopupListener mPopupListener = null;

	// Button id array
	int[] mBtnIds = {R.id.btPopupLeft, R.id.btPopupRight};

	public static void createAlert(Context context, boolean cancelable, int btType, String title, String content, String leftBtnText, String rightBtnText, CustomPopupListener listener) {
		try
		{
			CustomPopup popup = new CustomPopup(context, btType, title, content, leftBtnText, rightBtnText, listener);
			popup.setCancelable(cancelable);
			popup.show();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/*
	 * Init Dialog
	 */
	public CustomPopup(Context context, int btType, String title, String content, String leftBtnText, String rightBtnText, CustomPopupListener listener) {
		super(context , android.R.style.Theme_Translucent_NoTitleBar);

		_context = context;
		mBtType = btType;
		mTitle = title;
		mContent = content;
		mBtnLeft = leftBtnText;
		mBtnRight = rightBtnText;
		mPopupListener = listener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.popup_base);

		tvPopupTitle = (TextView) findViewById(R.id.tvPopupTitle);
		tvPopupContent = (TextView) findViewById(R.id.tvPopupContent);

		// title show & hide
		if (tvPopupTitle != null) 
		{
			if(mTitle != null && mTitle.length() > 0) 
				tvPopupTitle.setText(mTitle);
			else 
				tvPopupTitle.setVisibility(View.GONE);
		}

		// content show & hide
		if (tvPopupContent != null) 
		{
			if(mContent != null && mContent.length() > 0) 
			{
				mContent = mContent.replace("\\n", "\n");

				if(mContent.length() > 100) 
					tvPopupContent.setTextSize(12);

				tvPopupContent.setText(mContent);
			} 
			else
			{
				tvPopupContent.setVisibility(View.GONE);
			}
		}

		
		try
		{
			// set button text
			for (int ids : mBtnIds) {
				Button v = (Button)findViewById(ids);

				if(ids == R.id.btPopupLeft)
				{
					v.setText(mBtnLeft);
				}
				else if(ids == R.id.btPopupRight)
				{
					v.setText(mBtnRight);
				}
			}	
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		// button visibility setting
		if(mBtType == 1)
		{
			findViewById(mBtnIds[0]).setVisibility(View.VISIBLE);
			findViewById(mBtnIds[1]).setVisibility(View.GONE);
			
			findViewById(R.id.vHorizontalLine).setVisibility(View.GONE);
		}
		else if(mBtType == 2)
		{
			findViewById(mBtnIds[0]).setVisibility(View.VISIBLE);
			findViewById(mBtnIds[1]).setVisibility(View.VISIBLE);
			
			findViewById(R.id.vHorizontalLine).setVisibility(View.VISIBLE);
		}

		// button set cliclistener
		for (int ids : mBtnIds) 
		{
			View v = findViewById(ids);

			if(v != null) 
				v.setOnClickListener(mOnClickListener);
		}

		// popuplistener init
		if(mPopupListener != null) {
			mPopupListener.onState(this, STATE_CREATE);
		}
	}


	/*
	 * Dialog Show
	 */
	@Override
	public void show() {
		try 
		{
			super.show();
			// when show popup change scale and alpha value (animation)

			View bgView = findViewById(R.id.vPopup);
			if(bgView != null) {
				AlphaAnimation ani = new AlphaAnimation(0, 1);
				ani.setDuration(300);
				ani.setFillAfter(true);
				bgView.startAnimation(ani);
			}

			View container = findViewById(R.id.llPopupContainer);
			if(container != null) {
				Animation ani = AnimationUtils.loadAnimation(getContext(), R.anim.anim_popup_enter);
				container.startAnimation(ani);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	boolean mIsFinish = false;
	@Override
	public void dismiss() {
		int duration = 240;
		if(mIsFinish == false) 
		{
			mIsFinish = true;

			if(mPopupListener != null) {
				mPopupListener.onState(this, STATE_CLOSE);
			}

			// when hide popup change scale and alpha value (animation)
			View bgView = findViewById(R.id.vPopup);
			if(bgView != null) {
				AlphaAnimation ani = new AlphaAnimation(1, 0);
				ani.setDuration(duration);
				ani.setFillAfter(false);
				bgView.startAnimation(ani);
			}

			View container = findViewById(R.id.llPopupContainer);
			if(container != null) {
				Animation ani = AnimationUtils.loadAnimation(getContext(), R.anim.anim_popup_exit);
				ani.setDuration(duration);
				ani.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationStart(Animation animation) {}

					@Override
					public void onAnimationRepeat(Animation animation) {}

					@Override
					public void onAnimationEnd(Animation animation) {


						CustomPopup.super.dismiss();
					}
				});
				container.startAnimation(ani);
			}
			else {
				super.dismiss();
			}
		}
	}


	/************************************************************************************************************
	 * Setting Popup Listener
	 * Define PopupListener Interface
	 * @author Albert
	 *************************************************************************************************************/
	public interface PopupListener
	{
		void onLeftClick(CustomPopup dialogPopup);
		void onRightClick(CustomPopup dialogPopup);
		void onState(CustomPopup dialogPopup, int state);
		void onBackPressed();
	}

	// you need to method override
	public static class CustomPopupListener implements PopupListener 
	{
		public CustomPopupListener() {}

		@Override
		public void onLeftClick(CustomPopup dialogPopup) {}
		@Override
		public void onRightClick(CustomPopup dialogPopup) {}
		@Override
		public void onState(CustomPopup dialogPopup, int state) {}
		@Override
		public void onBackPressed() {}
	}

	/*
	 * Poppu ClickListener
	 */
	View.OnClickListener mOnClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {

			if(v.getId() == R.id.btPopupLeft)
			{
				if(mPopupListener != null)
				{
					try
					{
						mPopupListener.onLeftClick(CustomPopup.this);
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
			}
			else if(v.getId() == R.id.btPopupRight)
			{
				if(mPopupListener != null)
				{
					try
					{
						mPopupListener.onRightClick(CustomPopup.this);
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
			}
			
			CustomPopup.this.dismiss();
		}
	};

	@Override
	public void onBackPressed() {
		if(mPopupListener != null)
			mPopupListener.onBackPressed();

		super.onBackPressed();
	}
}