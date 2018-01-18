package com.labelwall.mywall.ui.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.joanzapata.iconify.widget.IconTextView;
import com.labelwall.mywall.R;
import com.labelwall.mywall.delegates.base.WallDelegate;
import com.labelwall.mywall.util.callback.CallbackManager;
import com.labelwall.mywall.util.callback.CallbackType;
import com.labelwall.mywall.util.callback.IGlobalCallback;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018-01-12.
 * 封装一个照片上传展示与删除的控件
 */

public class AutoPhotoLayout extends LinearLayoutCompat {

    private int mCurrentNum = 0;//现在是第几个图片
    private int mMaxNum = 0;//最大有几张图片
    private int mMaxLineNum = 0;//一行有最多几张图片
    private IconTextView mIconAdd = null;
    private LayoutParams mParams = null;
    private int mDeleteId = 0;//要删除的图片id
    private AppCompatImageView mTargetImageView = null;
    private int mImageMargin = 0;//图片间的空隙
    private WallDelegate mDelegate = null;//“宿主”delegate
    private List<View> mLineViews = null;//一行的图片存在list中
    private AlertDialog mTargetDialog = null;//删除图片显示的dialog
    private static final String ICON_TEXT = "{fa-plus}";
    private float mIconSize = 0;

    private static final List<List<View>> ALL_VIEWS = new ArrayList<>();//存储所有的View(ImageView)
    private static final List<Integer> LINE_HEIGHTS = new ArrayList<>();//存储每一行的高度

    //防止多次的测量和布局过程
    private boolean mIsOnceInitOnMeasure = false;
    private boolean mHasInitOnLayout = false;

    private static AppCompatButton mButton;

    private static final RequestOptions OPTIONS = new RequestOptions()
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.NONE);

    private static LinkedHashMap<Integer, Uri> mImageMap = new LinkedHashMap<>();//保存上传的照片

    public AutoPhotoLayout(Context context) {
        this(context, null);
    }

    public AutoPhotoLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoPhotoLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.camera_flow_layout);
        mMaxNum = typedArray.getInt(R.styleable.camera_flow_layout_max_count, 1);
        mMaxLineNum = typedArray.getInt(R.styleable.camera_flow_layout_line_count, 4);
        mImageMargin = typedArray.getInt(R.styleable.camera_flow_layout_item_margin, 0);
        mIconSize = typedArray.getDimension(R.styleable.camera_flow_layout_icon_size, 20);
        typedArray.recycle();//回收
    }

    public final void setDelegate(WallDelegate delegate) {
        this.mDelegate = delegate;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initAddIcon();
        mTargetDialog = new AlertDialog.Builder(getContext()).create();
    }


    private void initAddIcon() {
        mIconAdd = new IconTextView(getContext());
        mIconAdd.setText(ICON_TEXT);
        mIconAdd.setGravity(Gravity.CENTER);
        mIconAdd.setTextSize(mIconSize);
        mIconAdd.setBackgroundResource(R.drawable.border_text);
        mIconAdd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //打开相机，相册的dialog
                mDelegate.startCameraWithCheck();
            }
        });
        this.addView(mIconAdd);
    }

    public static void setSubmitBtn(AppCompatButton button) {
        mButton = button;
        if (mButton != null) {
            final IGlobalCallback<Map<Integer, Uri>> callback =
                    CallbackManager.getInstance().getCallback(CallbackType.IMAGES);
            if (callback != null) {
                callback.executeCallback(mImageMap);
            }
        }
    }

    public final void onCropTarget(Uri uri) {
        createNewImageView();//设置显示的ImageView的属性
        Glide.with(mDelegate)//将图片加载到控件中
                .load(uri)
                .apply(OPTIONS)
                .into(mTargetImageView);
        Integer key = mTargetImageView.getId();
        mImageMap.put(key, uri);
    }

    private void createNewImageView() {
        mTargetImageView = new AppCompatImageView(getContext());
        mTargetImageView.setId(mCurrentNum);
        mTargetImageView.setLayoutParams(mParams);
        mTargetImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取要删除的图片ID
                mDeleteId = v.getId();
                //设置删除图片的的dialog的样式
                mTargetDialog.show();
                final Window window = mTargetDialog.getWindow();
                if (window != null) {
                    window.setContentView(R.layout.dialog_image_click_panel);
                    window.setGravity(Gravity.BOTTOM);
                    window.setWindowAnimations(R.style.anim_panel_up_from_buttom);
                    window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    final WindowManager.LayoutParams params = window.getAttributes();
                    params.width = WindowManager.LayoutParams.MATCH_PARENT;
                    params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
                    params.dimAmount = 0.5f;
                    window.setAttributes(params);
                    window.findViewById(R.id.dialog_image_clicked_btn_delete).
                            setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //得到要删除的图片
                                    final AppCompatImageView deleteImageView =
                                            (AppCompatImageView) findViewById(mDeleteId);
                                    mImageMap.remove(mDeleteId);
                                    //设置自图片消失的动画
                                    final AlphaAnimation animation = new AlphaAnimation(1, 0);
                                    animation.setDuration(500);
                                    animation.setRepeatCount(0);
                                    animation.setFillAfter(true);
                                    animation.setStartOffset(0);
                                    deleteImageView.setAnimation(animation);
                                    animation.start();
                                    AutoPhotoLayout.this.removeView(deleteImageView);
                                    mCurrentNum -= 1;
                                    //当数目达到上限时隐藏添加按钮，不足时显示添加按钮
                                    if (mCurrentNum < mMaxNum) {
                                        mIconAdd.setVisibility(VISIBLE);
                                    }
                                    mTargetDialog.cancel();
                                }
                            });
                    window.findViewById(R.id.dialog_image_clicked_btn_undetermined)
                            .setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mTargetDialog.cancel();
                                }
                            });
                    window.findViewById(R.id.dialog_image_clicked_btn_cancel)
                            .setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mTargetDialog.cancel();
                                }
                            });
                }
            }
        });
        this.addView(mTargetImageView, mCurrentNum);
        mCurrentNum++;
        //当添加的数量大于限制时，隐藏添加按钮
        if (mCurrentNum >= mMaxNum) {
            mIconAdd.setVisibility(View.GONE);
        }
    }


    //测量方法
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int sizeWith = MeasureSpec.getSize(widthMeasureSpec);
        final int modeWith = MeasureSpec.getMode(widthMeasureSpec);
        final int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        final int modeHeight = MeasureSpec.getMode(heightMeasureSpec);
        //wrap_content
        int width = 0;
        int height = 0;
        //记录每一行的宽度与高度
        int lineWith = 0;
        int lineHeight = 0;
        //得到内部元素个数
        int cCount = getChildCount();
        for (int i = 0; i < cCount; i++) {
            final View child = getChildAt(i);
            //测量子View的宽和高
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            //的搭配LayoutParams
            final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            //子View占据的宽度
            final int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            //子View占据的高度
            final int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
            //换行
            if (lineWith + childWidth > sizeWith - getPaddingLeft() - getPaddingRight()) {
                //对比得到最大宽度
                width = Math.max(width, lineWith);
                //重置lineWidth
                lineWith = childWidth;
                height += lineHeight;
                lineHeight = childHeight;
            } else {
                //未换行
                //叠加行宽
                lineWith += childWidth;
                //得到当前最大的高度
                lineHeight = Math.max(lineHeight, childHeight);
            }
            //最后一个子控件
            if (i == cCount - 1) {
                width = Math.max(lineWith, width);
                //判断是否超过最大拍照限制
                height += lineHeight;
            }
        }
        setMeasuredDimension(
                modeWith == MeasureSpec.EXACTLY ? sizeWith : width + getPaddingLeft() + getPaddingRight(),
                modeHeight == MeasureSpec.EXACTLY ? sizeHeight : height + getPaddingTop() + getPaddingBottom()
        );
        //设置一行所有图片的宽高
        final int imageSideLen = sizeWith / mMaxLineNum;
        //只初始化一次
        if (!mIsOnceInitOnMeasure) {
            mParams = new LayoutParams(imageSideLen, imageSideLen);
            mIsOnceInitOnMeasure = true;
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        ALL_VIEWS.clear();
        LINE_HEIGHTS.clear();
        // 当前ViewGroup的宽度
        final int width = getWidth();
        int lineWidth = 0;
        int lineHeight = 0;
        if (!mHasInitOnLayout) {
            mLineViews = new ArrayList<>();
            mHasInitOnLayout = true;
        }
        final int cCount = getChildCount();
        for (int i = 0; i < cCount; i++) {
            final View child = getChildAt(i);
            final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            final int childWith = child.getMeasuredWidth();
            final int childHeight = child.getMeasuredHeight();
            //如果需要换行
            if (childWith + lineWidth + lp.leftMargin + lp.rightMargin >
                    width - getPaddingLeft() - getPaddingRight()) {
                //记录lineHeight
                LINE_HEIGHTS.add(lineHeight);
                //记录当前一行的Views
                ALL_VIEWS.add(mLineViews);
                //重置宽和高
                lineWidth = 0;
                lineHeight = childHeight + lp.topMargin + lp.bottomMargin;
                //重置View集合
                mLineViews.clear();
            }
            lineWidth += childWith + lp.leftMargin + lp.rightMargin;
            lineHeight = Math.max(lineHeight, lineHeight + lp.topMargin + lp.bottomMargin);
            mLineViews.add(child);
        }
        //处理最后一行
        LINE_HEIGHTS.add(lineHeight);
        ALL_VIEWS.add(mLineViews);
        //设置子View位置
        int left = getPaddingLeft();
        int top = getPaddingTop();
        //行数
        final int lineNum = ALL_VIEWS.size();
        for (int i = 0; i < lineNum; i++) {
            //当前行所有的View
            mLineViews = ALL_VIEWS.get(i);
            lineHeight = LINE_HEIGHTS.get(i);
            final int size = mLineViews.size();
            for (int j = 0; j < size; j++) {
                final View child = mLineViews.get(j);
                //判断child的状态
                if (child.getVisibility() == GONE) {
                    continue;
                }
                final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
                //设置子View的边距
                final int lc = left + lp.leftMargin;
                final int tc = top + lp.topMargin;
                final int rc = lc + child.getMeasuredWidth() - mImageMargin;
                final int bc = tc + child.getMeasuredHeight();
                //为子View进行布局
                child.layout(lc, tc, rc, bc);
                left += child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            }
            left = getPaddingLeft();
            top += lineHeight;
        }
        mIconAdd.setLayoutParams(mParams);
        mHasInitOnLayout = false;
    }
}
