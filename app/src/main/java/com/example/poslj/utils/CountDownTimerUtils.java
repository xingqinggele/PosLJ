package com.example.poslj.utils;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.text.SpannableString;
import android.util.Log;
import android.widget.TextView;

import com.example.poslj.R;


/**
 * 作者: qgl
 * 创建日期：2020/10/24
 * 描述:实现倒计时
 */
public class CountDownTimerUtils extends CountDownTimer {
    private TextView mTextView;

    /**
     * @param millisInFuture    The number of millis in the future from the call
     *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                          is called.
     * @param countDownInterval The interval along the way to receive
     *                          {@link #onTick(long)} callbacks.
     */
    public CountDownTimerUtils(TextView textView, long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
        this.mTextView = textView;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        if (null == mTextView) {
            Log.d(CountDownTimerUtils.super.toString(), "onTick: 获取验证码出错了");
            return;
        } else {
            mTextView.setClickable(false); //设置不可点击
            mTextView.setText(millisUntilFinished / 1000 + "s");  //设置倒计时时间
            mTextView.setBackgroundResource(R.drawable.small_merchants_tv_bg1); //设置按钮为灰色，这时是不能点击的
            /**
             * 超链接 URLSpan
             * 文字背景颜色 BackgroundColorSpan
             * 文字颜色 ForegroundColorSpan
             * 字体大小 AbsoluteSizeSpan
             * 粗体、斜体 StyleSpan
             * 删除线 StrikethroughSpan
             * 下划线 UnderlineSpan
             * 图片 ImageSpan
             * http://blog.csdn.net/ah200614435/article/details/7914459
             */
            SpannableString spannableString = new SpannableString(mTextView.getText().toString());  //获取按钮上的文字
//            ForegroundColorSpan span = new ForegroundColorSpan(Color.parseColor("#1673A3"));
//            /**
//             * public void setSpan(Object what, int start, int end, int flags) {
//             * 主要是start跟end，start是起始位置,无论中英文，都算一个。
//             * 从0开始计算起。end是结束位置，所以处理的文字，包含开始位置，但不包含结束位置。
//             */
//            spannableString.setSpan(span, 0, 3, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);//将倒计时的时间设置为红色
            mTextView.setTextColor(Color.parseColor("#FFFFFF"));
            mTextView.setText(spannableString);
        }
    }

    @Override
    public void onFinish() {
        mTextView.setText("获取验证码");
        mTextView.setClickable(true);//重新获得点击
        mTextView.setTextColor(Color.parseColor("#FFFFFF"));
        mTextView.setBackgroundResource(R.drawable.small_merchants_tv_bg); //设置按钮为灰色，这时是不能点击的

    }
}
