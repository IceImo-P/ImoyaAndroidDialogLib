package net.imoya.android.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

/**
 * OKボタン、キャンセルボタンを持つダイアログ
 * <p/>
 * タイトル、メッセージ、OKボタン、キャンセルボタンを持つ、汎用ダイアログフラグメントです。<ul>
 * <li>親画面は {@link Listener} を実装した
 * {@link Fragment} 又は {@link AppCompatActivity} でなければなりません。</li>
 * <li>{@link Builder}を使用して表示内容を設定し、 {@link Builder#show()}
 * メソッドを呼び出して表示してください。</li>
 * <li>ダイアログ終了時 {@link Listener#onDialogResult(int, int, Intent)}
 * メソッドが呼び出されます。</li>
 * <li>OKボタンがクリックされた場合は、
 * {@link Listener#onDialogResult(int, int, Intent)} メソッドの引数 resultCode の値が
 * {@link Activity#RESULT_OK} となります。</li>
 * <li>キャンセルボタンがクリックされた場合、又はダイアログがキャンセル終了した場合は、
 * {@link Listener#onDialogResult(int, int, Intent)} メソッドの引数 resultCode の値が
 * {@link Activity#RESULT_CANCELED} となります。</li>
 * </ul>
 */
public class OkCancelDialog extends TwoButtonDialog {
    /**
     * ダイアログビルダ
     */
    public static class Builder extends TwoButtonDialog.Builder {
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
         * OKボタン文言を設定します。デフォルト値は {@link android.R.string#ok } です。
         *
         * @param buttonTitle ボタン文言
         * @return {@link Builder}
         */
        public Builder setPositiveButtonTitle(@NonNull String buttonTitle) {
            super.setPositiveButtonTitle(buttonTitle);
            return this;
        }

        /**
         * キャンセルボタン文言を設定します。デフォルト値は {@link android.R.string#cancel } です。
         *
         * @param buttonTitle ボタン文言
         * @return {@link Builder}
         */
        public Builder setNegativeButtonTitle(@NonNull String buttonTitle) {
            super.setNegativeButtonTitle(buttonTitle);
            return this;
        }

        /**
         * 実装クラスのインスタンスを生成して返します。
         *
         * @return {@link DialogBase}
         */
        @Override
        protected DialogBase createFragment() {
            return new OkCancelDialog();
        }
    }

    /**
     * ボタンクリックリスナの実装
     */
    protected static class DialogButtonClickListener extends DialogItemClickListener {
        /**
         * コンストラクタ
         *
         * @param dialog   ダイアログ
         * @param listener ダイアログのリスナ
         */
        protected DialogButtonClickListener(OkCancelDialog dialog, Listener listener) {
            super(dialog, listener);
        }

        /**
         * 親画面へ通知する結果コードを返します。
         *
         * @param which クリックされたボタンの位置
         * @return 結果コード
         */
        private int getResultCode(int which) {
            return (which == AlertDialog.BUTTON_POSITIVE
                    ? Activity.RESULT_OK : Activity.RESULT_CANCELED);
        }

        /**
         * ダイアログリスナへ、ボタンクリックを通知します。
         *
         * @param dialogInterface {@link DialogInterface}
         * @param which           クリックされたボタンの位置
         */
        @Override
        protected void callListener(DialogInterface dialogInterface, int which) {
            this.listener.onDialogResult(
                    this.dialog.getRequestCode(), this.getResultCode(which), this.makeData());
        }
    }

    // private static final String TAG = "OkCancelDialog";

    @NonNull
    @Override
    public Dialog createDialog(Bundle savedInstanceState) {
        final DialogButtonClickListener buttonClickListener = new DialogButtonClickListener(
                this, this.listener);
        final Bundle arguments = this.requireArguments();
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
