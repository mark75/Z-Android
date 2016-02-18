package com.zandroid.View;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zandroid.R;

/**
 * Created by prin on 2016/2/18.
 * 项目中使用到的Dialog
 * (1)刚入工作时  使用Activity来弹出对话框形式的广告活动，其实应该使用自定义Dialog
 */

public class ZCustomDialog extends Dialog {

    public ZCustomDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public ZCustomDialog(Context context) {
        super(context);
    }

    public static class Builder {
        private Context context;
        private String title;
        private String message;
        private String positiveButtonText;
        private String negativeButtonText;
        private View contentView;

        private OnClickListener positiveButtonClickListener;
        private OnClickListener negativeButtonClickListener;

        public Builder(Context context){
            this.context=context;
        }

        public Builder setMessage(String message){
            this.message=message;
            return this;
        }

        public Builder setMessage(int message){
            this.message= (String) context.getText(message);
            return this;
        }

        public Builder setTitle(int title){
            this.title= (String) context.getText(title);
            return this;
        }

        public Builder setTitle(String title){
            this.title=title;
            return this;
        }

        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }

        public Builder setPositiveButton(int positiveButtonText,
                                         OnClickListener listener) {
            this.positiveButtonText = (String) context
                    .getText(positiveButtonText);
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setPositiveButton(String positiveButtonText,
                                         OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(int negativeButtonText,
                                         OnClickListener listener) {
            this.negativeButtonText = (String) context
                    .getText(negativeButtonText);
            this.negativeButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(String negativeButtonText,
                                         OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }

        public ZCustomDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final ZCustomDialog dialog = new ZCustomDialog(context,
                    R.style.customDialog);
            View layout = inflater.inflate(R.layout.custom_dialog, null);
            //将背景的黑色边框设置为透明
//            layout.getBackground().setAlpha(0);
            dialog.addContentView(layout, new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT));
            dialog.setCanceledOnTouchOutside(false);


            if (null != title) {
                ((TextView) layout.findViewById(R.id.titleText)).setText(title);
            }

            if (dialog!=null){
                if (positiveButtonText != null) {
                    ((Button) layout.findViewById(R.id.positiveButton))
                            .setText(positiveButtonText);

                    if (positiveButtonClickListener != null) {
                        ((Button) layout.findViewById(R.id.positiveButton))
                                .setOnClickListener(new View.OnClickListener() {
                                    public void onClick(View v) {
                                        positiveButtonClickListener.onClick(dialog,
                                                DialogInterface.BUTTON_POSITIVE);
                                    }
                                });
                    }
                } else {
                    layout.findViewById(R.id.positiveButton).setVisibility(
                            View.GONE);
                }

                if (negativeButtonText != null) {
                    ((Button) layout.findViewById(R.id.negativeButton))
                            .setText(negativeButtonText);

                    if (negativeButtonClickListener != null) {
                        ((Button) layout.findViewById(R.id.negativeButton))
                                .setOnClickListener(new View.OnClickListener() {
                                    public void onClick(View v) {
                                        negativeButtonClickListener.onClick(dialog,
                                                DialogInterface.BUTTON_NEGATIVE);
                                    }
                                });
                    }
                } else {
                    layout.findViewById(R.id.negativeButton).setVisibility(
                            View.GONE);
                }
            }


            if (message != null) {
                ((TextView) layout.findViewById(R.id.messageText))
                        .setText(message);
            } else {
                ((TextView) layout.findViewById(R.id.messageText)).setVisibility(View.GONE);

                if (contentView != null) {
                    // if no message set
                    // add the contentView to the dialog body
                    ((LinearLayout) layout.findViewById(R.id.contentLayout))
                            .removeAllViews();
                    ((LinearLayout) layout.findViewById(R.id.contentLayout))
                            .addView(contentView, new WindowManager.LayoutParams(
                                    WindowManager.LayoutParams.WRAP_CONTENT,
                                    WindowManager.LayoutParams.WRAP_CONTENT));
                }
            }
            dialog.setContentView(layout);
            return dialog;
        }
    }

}
