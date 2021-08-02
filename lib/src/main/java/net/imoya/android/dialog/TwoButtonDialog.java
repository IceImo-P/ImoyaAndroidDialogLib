package net.imoya.android.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

/**
 * ボタン2個のダイアログ
 * <p/>
 * タイトル、メッセージ、positiveボタン、negativeボタンを持つ、汎用ダイアログフラグメントです。<ul>
 * <li>親画面は {@link Listener} を実装した {@link Fragment} 又は {@link AppCompatActivity}
 * でなければなりません。</li>
 * <li>{@link Builder}を使用して表示内容を設定し、 {@link Builder#show()}
 * メソッドを呼び出して表示してください。</li>
 * <li>ダイアログ終了時 {@link Listener#onDialogResult(int, int, Intent)}
 * メソッドが呼び出されます。</li>
 * <li>ボタンがクリックされた場合、
 * {@link Listener#onDialogResult(int, int, Intent)} メソッドの引数 resultCode の値が
 * {@link Activity#RESULT_OK} となります。このとき、引数 data の
 * {@link Intent#getIntExtra(String, int)} へ {@link DialogBase#EXTRA_KEY_WHICH}
 * を入力することで、クリックされたボタンの種類を取得できます。</li>
 * <li>ダイアログがキャンセル終了した場合、
 * {@link Listener#onDialogResult(int, int, Intent)} メソッドの引数 resultCode の値が
 * {@link Activity#RESULT_CANCELED} となります。</li>
 * </ul>
 */
public class TwoButtonDialog extends DialogBase {
    /**
     * ダイアログビルダ
     */
    public static class Builder extends DialogBase.Builder {
        /**
         * positiveボタン文言
         */
        private String positiveButtonTitle;
        /**
         * negativeボタン文言
         */
        private String negativeButtonTitle;

        /**
         * コンストラクタ
         *
         * @param activity    親画面となる {@link AppCompatActivity}
         * @param requestCode リクエストコード
         * @param <T>         親画面は {@link Listener} を実装した {@link AppCompatActivity} であること
         */
        public <T extends AppCompatActivity & Listener> Builder(@NonNull T activity, int requestCode) {
            super(activity, requestCode);
        }

        /**
         * コンストラクタ
         *
         * @param fragment    親画面となる{@link Fragment}
         * @param requestCode リクエストコード
         * @param <T>         親画面は {@link Listener} を実装した {@link Fragment} であること
         */
        public <T extends Fragment & Listener> Builder(@NonNull T fragment, int requestCode) {
            super(fragment, requestCode);
        }

        /**
         * タイトル文言を設定します。
         *
         * @param title タイトル文言
         * @return {@link Builder}
         */
        public Builder setTitle(@NonNull String title) {
            super.setTitle(title);
            return this;
        }

        /**
         * メッセージ文言を設定します。
         *
         * @param message メッセージ文言
         * @return {@link Builder}
         */
        public Builder setMessage(@NonNull String message) {
            super.setMessage(message);
            return this;
        }

        /**
         * インスタンス識別用タグを設定します。
         *
         * @param tag タグ
         * @return {@link Builder}
         */
        public Builder setTag(@NonNull String tag) {
            super.setTag(tag);
            return this;
        }

        /**
         * positiveボタン文言を設定します。デフォルト値は {@link android.R.string#ok } です。
         *
         * @param buttonTitle ボタン文言
         * @return {@link Builder}
         */
        public Builder setPositiveButtonTitle(@NonNull String buttonTitle) {
            this.positiveButtonTitle = buttonTitle;
            return this;
        }

        /**
         * negativeボタン文言を設定します。デフォルト値は {@link android.R.string#cancel } です。
         *
         * @param buttonTitle ボタン文言
         * @return {@link Builder}
         */
        public Builder setNegativeButtonTitle(@NonNull String buttonTitle) {
            this.negativeButtonTitle = buttonTitle;
            return this;
        }

        /**
         * 実装クラスのインスタンスを生成して返します。
         *
         * @return {@link DialogBase}
         */
        @Override
        protected DialogBase createFragment() {
            return new TwoButtonDialog();
        }

        /**
         * ダイアログへ渡す引数を生成して返します。
         *
         * @return 引数を含んだ {@link Bundle}
         */
        @Override
        protected Bundle makeArguments() {
            final Bundle arguments = super.makeArguments();

            if (this.positiveButtonTitle != null) {
                arguments.putString(KEY_POSITIVE_BUTTON_TITLE, this.positiveButtonTitle);
            } else {
                arguments.putString(KEY_POSITIVE_BUTTON_TITLE,
                        this.getContext().getString(android.R.string.ok));
            }
            if (this.negativeButtonTitle != null) {
                arguments.putString(KEY_NEGATIVE_BUTTON_TITLE, this.negativeButtonTitle);
            } else {
                arguments.putString(KEY_NEGATIVE_BUTTON_TITLE,
                        this.getContext().getString(android.R.string.cancel));
            }

            return arguments;
        }
    }

//    private static final String TAG = "TwoButtonDialog";

    /**
     * ダイアログ引数キー:positiveボタン文言
     */
    protected static final String KEY_POSITIVE_BUTTON_TITLE = "positiveButtonTitle";
    /**
     * ダイアログ引数キー:negativeボタン文言
     */
    protected static final String KEY_NEGATIVE_BUTTON_TITLE = "negativeButtonTitle";

    /**
     * Called when a fragment is first attached to its context.
     * {@link Fragment#onCreate(Bundle)} will be called after this.
     *
     * @param context {@link Context}
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Listener) {
            this.listener = (Listener) context;
        }
    }

    /**
     * ダイアログ生成処理
     *
     * @param savedInstanceState 前回強制終了時の保存データ
     * @return 生成した{@link Dialog}
     */
    @Override
    @NonNull
    public Dialog createDialog(Bundle savedInstanceState) {
        final Bundle arguments = this.requireArguments();
        final DialogItemClickListener buttonClickListener = new DialogItemClickListener(
                this, this.listener);
        return new AlertDialog.Builder(this.requireActivity(), this.getTheme())
                .setTitle(arguments.getString(KEY_TITLE))
                .setMessage(arguments.getString(KEY_MESSAGE))
                .setPositiveButton(
                        arguments.getString(KEY_POSITIVE_BUTTON_TITLE), buttonClickListener)
                .setNegativeButton(
                        arguments.getString(KEY_NEGATIVE_BUTTON_TITLE), buttonClickListener)
                .create();
    }
}
