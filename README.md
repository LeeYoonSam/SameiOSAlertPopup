# SameiOSAlertPopup

I wnat to make an alert popup like iOS base alert.

# instructions
It Easily available.
You can use like next.

CustomPopup.createAlert(context, cancelable, btType, title, content, leftBtnText, rightBtnText, listener);

		context - your context.
		
		btType - select button type.
  	  public static final int TYPE_OWN_BTN = 1;
	    public static final int TYPE_TWO_BTN = 2;
	    
		title - popup title.
		
		content - popup content.
		
		leftBtnText - set left button text.
		
		rightBtnText - set right button text.
		
		listener - 
      public interface PopupListener {
      	void onLeftClick(CustomPopup dialogPopup);
      	void onRightClick(CustomPopup dialogPopup);
      	void onState(CustomPopup dialogPopup, int state);
      	void onBackPressed();
    	}
    
    	public static class CustomPopupListener implements PopupListener {
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
