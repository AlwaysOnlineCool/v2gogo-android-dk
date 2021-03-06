package com.v2gogo.project.activity.shop;

import java.io.File;
import java.io.FileOutputStream;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsoluteLayout;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import cn.sharesdk.framework.ShareSDK;

import com.v2gogo.project.R;
import com.v2gogo.project.activity.BaseActivity;
import com.v2gogo.project.activity.MainTabActivity;
import com.v2gogo.project.activity.home.theme.UploadPictureActivity;
import com.v2gogo.project.domain.ShareInfo;
import com.v2gogo.project.manager.UserInfoManager;
import com.v2gogo.project.manager.upload.ThemePhotoUploadManager;
import com.v2gogo.project.utils.common.AppUtil;
import com.v2gogo.project.utils.common.PhotoUtil;
import com.v2gogo.project.utils.http.IntentExtraConstants;
import com.v2gogo.project.utils.share.CustomPlatformActionListener;
import com.v2gogo.project.utils.share.ShareUtils;
import com.v2gogo.project.views.dialog.V2gogoShareDialog;
import com.v2gogo.project.views.dialog.V2gogoShareDialog.IonItemClickCallback;
import com.v2gogo.project.views.dialog.V2gogoShareDialog.SHARE_TYPE;
import com.v2gogo.project.views.webview.ProgressWebView;
import com.v2gogo.project.views.webview.ProgressWebView.IonReceiveTitleCallback;
import com.ypy.eventbus.EventBus;

/**
 * 功能：团购商品类型
 * 
 * @ahthor：黄荣星
 * @date:2015-12-15
 * @version::V1.0
 */
@SuppressWarnings("deprecation")
public class GroupProductListWebViewActivity extends BaseActivity implements IonReceiveTitleCallback, IonItemClickCallback
{
	public static final String URL = "url";
	public static final String IS_BACK_HOME = "is_back_home";

	private String url;
	private boolean isBackHome;

	private AbsoluteLayout mAbsoluteLayout;
	private ProgressWebView mWebView;
	private ImageButton mBack;
	private TextView mTitle;

	private ImageButton mShareImgBtn;
	private TextView mCloseTv;
	private V2gogoShareDialog mShareDialog;
	private ShareInfo mShareInfo;

	private static GroupProductListWebViewActivity instance;

	@Override
	public void onInitViews()
	{
		EventBus.getDefault().register(this);

		mTitle = (TextView) findViewById(R.id.webview_title);
		mBack = (ImageButton) findViewById(R.id.common_app_action_bar_back);
		mShareImgBtn = (ImageButton) findViewById(R.id.commen_webview_share);
		mCloseTv = (TextView) findViewById(R.id.commen_webview_close);
		initHeaderView();

		mShareImgBtn.setVisibility(View.VISIBLE);
	}

	private void initHeaderView()
	{
		View view = LayoutInflater.from(this).inflate(R.layout.webview, null);
		mWebView = (ProgressWebView) view.findViewById(R.id.webview_activity_webview);
		mAbsoluteLayout = (AbsoluteLayout) view.findViewById(R.id.webview_activity_webview_container);

		if (mPullRefreshListView != null)
		{
			mPullRefreshListView.addHeaderView(view);
			mPullRefreshListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_2, new String[] {}));
		}
	}

	@Override
	protected void onPullDownRefresh2(AbsListView pullRefreshView)
	{
		super.onPullDownRefresh2(pullRefreshView);
		mPullRefreshListView.stopPullRefresh();
		mWebView.loadUrl(url);
	}

	@Override
	public int getCurrentLayoutId()
	{
		return R.layout.webview_group_layout;
	}

	@Override
	protected void onInitIntentData(Intent intent)
	{
		instance = this;
		super.onInitIntentData(intent);
		ShareSDK.initSDK(this);
		if (null != intent)
		{
			url = intent.getStringExtra(URL);
			isBackHome = intent.getBooleanExtra(IS_BACK_HOME, false);
		}
		mShareInfo = new ShareInfo();
		mShareInfo.setHref(url);
	}

	@Override
	protected void onInitLoadDatas()
	{
		super.onInitLoadDatas();
		mWebView.loadUrl(url);
	}

	@Override
	protected void registerListener()
	{
		mWebView.setOnReceiveTitleCallback(this);
		mBack.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				clickBack();
			}
		});
		mShareImgBtn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				share();
			}
		});
		mCloseTv.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				finish();
			}
		});
	}

	public static GroupProductListWebViewActivity getInstance()
	{
		if (instance == null)
		{
			instance = new GroupProductListWebViewActivity();
		}
		return instance;
	}

	public V2gogoShareDialog getShareDialog()
	{
		return mShareDialog;
	}

	/**
	 * method desc：弹出分享对话框
	 */
	public void share()
	{
		if (null == mShareDialog)
		{
			mShareDialog = new V2gogoShareDialog(this, R.style.style_action_sheet_dialog);
			mShareDialog.setItemClickCallback(this);
		}
		if (!mShareDialog.isShowing())
		{
			mShareDialog.show();
		}
	}

	@Override
	public void finish()
	{
		mAbsoluteLayout.removeView(mWebView);
		mWebView.destoryWebview();
		if (!AppUtil.isMainIntentExist(this))
		{
			Intent intent = new Intent(this, MainTabActivity.class);
			startActivity(intent);
		}
		super.finish();
	}

	@Override
	public void onReceiveTitle(String title)
	{
		mTitle.setText(title);
		mShareInfo.setTitle(title);
		mShareInfo.setDescription(title);
	}

	@Override
	public void clearRequestTask()
	{
	}

	@Override
	protected void onPause()
	{
		if (!TextUtils.isEmpty(url) && url.contains("refreshcoin"))
		{
			UserInfoManager.updateUserInfos();
		}
		mWebView.pauseWebview();
		super.onPause();
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		mWebView.resumeWebview();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == ProgressWebView.FILECHOOSER_RESULTCODE)
		{
			if (null == mWebView.mUploadMessage)
				return;
			Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
			mWebView.mUploadMessage.onReceiveValue(result);
			mWebView.mUploadMessage = null;
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			clickBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void clickBack()
	{
		if (isBackHome)
		{
			if (mWebView.canGoBack())
			{
				mWebView.goBack();
			}
			else
			{
				Intent intent = new Intent(GroupProductListWebViewActivity.this, MainTabActivity.class);
				startActivity(intent);
				finish();
			}
		}
		else
		{
			if (mWebView.canGoBack())
			{
				mWebView.goBack();
			}
			else
			{
				finish();
			}
		}
	}

	@Override
	protected void getAlbumPath(String albumPath)
	{
		super.getAlbumPath(albumPath);
		if (null != albumPath)
		{
			PhotoUtil.forward2Crop(this, Uri.fromFile(new File(albumPath)));
		}
	}

	@Override
	protected void getCameraPath(String cameraPath)
	{
		super.getCameraPath(cameraPath);
		if (cameraPath != null)
		{
			PhotoUtil.cameraCropImageUri(this, Uri.fromFile(new File(cameraPath)));
		}
	}

	@Override
	protected void getCompressPath(Bitmap bitmap)
	{
		super.getCompressPath(bitmap);
		if (null != bitmap)
		{
			try
			{
				boolean result = bitmap.compress(CompressFormat.JPEG, 100, new FileOutputStream(ThemePhotoUploadManager.FILE_PATH));
				if (!bitmap.isRecycled())
				{
					bitmap.recycle();
				}
				if (result)
				{
					forward2Upload(ThemePhotoUploadManager.FILE_PATH);
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * 跳转到上传
	 * 
	 * @param resultPhotoPath
	 */
	private void forward2Upload(String resultPhotoPath)
	{
		Intent intent = new Intent(this, UploadPictureActivity.class);
		intent.putExtra(IntentExtraConstants.PATH, resultPhotoPath);
		intent.putExtra(IntentExtraConstants.TID, mWebView.getmTid());
		// intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	// 点击分享渠道
	@Override
	public void onShareClick(SHARE_TYPE type)
	{

		String tip = getResources().getString(R.string.share_success_tip);
		com.v2gogo.project.utils.share.ShareUtils.SHARE_TYPE shareType = com.v2gogo.project.utils.share.ShareUtils.SHARE_TYPE.SHARE_WEIXIN;
		if (type == SHARE_TYPE.SHARE_WENXI_COLLECTIONS)
		{
			shareType = com.v2gogo.project.utils.share.ShareUtils.SHARE_TYPE.SHARE_WEIXIN_FRIENDS;
		}
		else if (type == SHARE_TYPE.SHARE_QQ)
		{
			shareType = com.v2gogo.project.utils.share.ShareUtils.SHARE_TYPE.SHARE_QQ;
		}
		else if (type == SHARE_TYPE.SHARE_QZONE)
		{
			shareType = com.v2gogo.project.utils.share.ShareUtils.SHARE_TYPE.SHARE_QZONE;
		}
		else if (type == SHARE_TYPE.SHARE_MESSAGE)
		{
			shareType = com.v2gogo.project.utils.share.ShareUtils.SHARE_TYPE.SHARE_MESSAGE;
		}
		if (mWebView != null && mWebView.getShareInfo() != null)
		{
			mShareInfo = mWebView.getShareInfo();
		}
		ShareUtils.share(this, mShareInfo.getTitle(), mShareInfo.getDescription(), mShareInfo.getHref(), mShareInfo.getImageUrl(), shareType,
				new CustomPlatformActionListener(this, tip));
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

	/**
	 * 接收上传图片成功的消息
	 * 
	 * @param progress
	 */
	public void onEventMainThread(String param)
	{
		if (param != null && "finish".equals(param))
		{
			finish();
		}
	}
}
