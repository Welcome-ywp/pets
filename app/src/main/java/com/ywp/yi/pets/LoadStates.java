package com.ywp.yi.pets;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.zip.Inflater;

/**
 * 数据加载的状态指示
 * 1, 正在加载
 * 2, 加载错误
 * 3, 空数据
 * 4, 加载成功，正常显示状态
 * Created by yi on 2017/9/21.
 */

public class LoadStates {

    /**
     * 加载状态
     */
    public static final int LOADING = 1;//加载中
    public static final int LOADERROR = 2;//加载错误
    public static final int LOADEMPTY = 3;//空数据
    public static final int LOADNORMAL = 4;//加载成功，正常显示
    /**
     * 加载状态对应的View
     */
    private ViewGroup LoadingView;//正在加载的View
    private ViewGroup LoadErrorView;//加载错误的View
    private ViewGroup LoadEmptyView;//空数据的View
    private ViewGroup LoadNormalView;//正常现实的View

    private Context mContext;
    private LayoutInflater mInflater;
    private View mView;

    private boolean isViewAddToParent = false;

    private static int  LAODTYPE = LOADING;

    public void showNormal(){

    }
    public void showLoading(){

    }
    public void showEmpty(){
        LAODTYPE = LOADEMPTY;
    }

    /**
     * 构造函数
     *
     * @param context
     */
    public LoadStates(Context context,View v) {
        mContext = context;
        mView = v;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * 给每个状态设定布局
     */
    private void setDefaultView() {
        if (LoadingView == null) {
            LoadingView = (ViewGroup) mInflater.inflate(R.layout.loading_layout, null);
        }
        if (LoadErrorView == null) {
            LoadErrorView = (ViewGroup) mInflater.inflate(R.layout.loading_layout, null);
        }
        if (LoadEmptyView == null) {
            LoadErrorView = (ViewGroup) mInflater.inflate(R.layout.empty_layout, null);
        }
        if (LoadNormalView == null) {
            LoadNormalView = (ViewGroup) mInflater.inflate(R.layout.layout_normal, null);
        }
    }
    //添加布局到父布局
    private void addToParent(RelativeLayout rl) {
        if (LoadEmptyView != null) {
            rl.addView(LoadEmptyView);
        }
        if (LoadErrorView != null) {
            rl.addView(LoadErrorView);
        }
        if (LoadingView != null) {
            rl.addView(LoadingView);
        }
        if (LoadNormalView != null) {
            rl.addView(LoadNormalView);
        }
    }
    //设置布局的可见性
    private void setViewVisibility() {
        if (mView != null) {
            switch (LOADEMPTY) {
                case LOADING:{
                    if (LoadingView != null){
                        LoadingView.setVisibility(View.VISIBLE);
                    }
                    if (LoadEmptyView != null){
                        LoadEmptyView.setVisibility(View.GONE);
                    }
                    if (LoadErrorView != null){
                        LoadErrorView.setVisibility(View.GONE);
                    }
                    if (LoadNormalView != null){
                        LoadNormalView.setVisibility(View.GONE);
                    }
                }break;
                case LOADEMPTY:{
                    if (LoadingView != null){
                        LoadingView.setVisibility(View.GONE);
                    }
                    if (LoadEmptyView != null){
                        LoadEmptyView.setVisibility(View.VISIBLE);
                    }
                    if (LoadErrorView != null){
                        LoadErrorView.setVisibility(View.GONE);
                    }
                    if (LoadNormalView != null){
                        LoadNormalView.setVisibility(View.GONE);
                    }
                }break;
                case LOADERROR:{
                    if (LoadingView != null){
                        LoadingView.setVisibility(View.GONE);
                    }
                    if (LoadEmptyView != null){
                        LoadEmptyView.setVisibility(View.GONE);
                    }
                    if (LoadErrorView != null){
                        LoadErrorView.setVisibility(View.VISIBLE);
                    }
                    if (LoadNormalView != null){
                        LoadNormalView.setVisibility(View.GONE);
                    }
                }break;
                case LOADNORMAL:{
                    if (LoadingView != null){
                        LoadingView.setVisibility(View.GONE);
                    }
                    if (LoadEmptyView != null){
                        LoadEmptyView.setVisibility(View.GONE);
                    }
                    if (LoadErrorView != null){
                        LoadErrorView.setVisibility(View.GONE);
                    }
                    if (LoadNormalView != null){
                        LoadNormalView.setVisibility(View.VISIBLE);
                    }
                }break;
                default:
                    break;
            }
        }
    }
    private void changeLayoutType(){
        if (!isViewAddToParent){
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            RelativeLayout rl = new RelativeLayout(mContext);
            rl.setLayoutParams(lp);
            addToParent(rl);
            isViewAddToParent = true;
            ViewGroup parent = (ViewGroup) mView.getParent();
            parent.addView(rl);
        }
        setViewVisibility();
    }

}
