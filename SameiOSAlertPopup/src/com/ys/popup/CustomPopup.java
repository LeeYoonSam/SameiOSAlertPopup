package com.ys.popup;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
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
	 * Define PopupType
	 */
	public static final int POPUPTYPE_TXT = 1;

	/*
	 * Define ButtonType
	 */
	public static final int TYPE_OWN_BTN = 1;
	public static final int TYPE_TWO_BTN = 2;

	/*
	 * PopupType
	 */
	int mPopupType;
	String mImgURL;

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

	CommPopupListener mPopupListener = null;

	// Button id array
	int[] mBtnIds = {R.id.btPopupLeft, R.id.btPopupRight};

	/*
	 * SET textalign
	 */
	public void setTxtAlignCenter(boolean is) {
		if(is) {
			tvPopupTitle.setGravity(Gravity.CENTER_HORIZONTAL);
			tvPopupContent.setGravity(Gravity.CENTER_HORIZONTAL);
		}
		else {
			tvPopupTitle.setGravity(Gravity.NO_GRAVITY);
			tvPopupContent.setGravity(Gravity.NO_GRAVITY);
		}
	}

	public static void createAlert(Context context, int popupType, int btType, String title, String content, String leftBtnText, String rightBtnText, CommPopupListener listener, boolean cancelable) {
		try
		{
			CustomPopup popup = new CustomPopup(context, popupType, btType, title, content, leftBtnText, rightBtnText, listener);
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
	public CustomPopup(Context context, int popupType, int btType, String title, String content, String leftBtnText, String rightBtnText, CommPopupListener listener) {
		super(context , android.R.style.Theme_Translucent_NoTitleBar);

		_context = context;
		mPopupType = popupType;
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

		// set button text
		for (int ids : mBtnIds) {
			Button v = (Button)findViewById(ids);

			if(v != null) {
				switch (ids) {
				case R.id.btPopupLeft:
					v.setText(mBtnLeft);
					break;
				case R.id.btPopupRight:
					v.setText(mBtnRight);
					break;
				}
			}
		}

		// button visibility setting
		if(mBtType == 1)
		{
			findViewById(mBtnIds[0]).setVisibility(View.VISIBLE);
			findViewById(mBtnIds[1]).setVisibility(View.GONE);
		}
		else if(mBtType == 2)
		{
			findViewById(mBtnIds[0]).setVisibility(View.VISIBLE);
			findViewById(mBtnIds[1]).setVisibility(View.VISIBLE);
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
	public static class CommPopupListener implements PopupListener 
	{
		public CommPopupListener() {}

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

			switch (v.getId()) {

			case R.id.btPopupLeft:
				if(mPopupListener != null)
				{
					try
					{
						mPopupListener.onLeftClick(CustomPopup.this);
					}
					catch (Exception e)
					{
						e.printStackTrace();

						if(mPopupListener != null)
							mPopupListener.onLeftClick(CustomPopup.this);
					}
				}

				break;


			case R.id.btPopupRight:
				if(mPopupListener != null)
				{
					mPopupListener.onRightClick(CustomPopup.this);
				}

				break;
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