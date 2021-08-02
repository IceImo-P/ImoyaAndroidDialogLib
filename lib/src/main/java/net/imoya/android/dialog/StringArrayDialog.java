package net.imoya.android.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import net.imoya.android.util.Log;

import java.util.Arrays;

/**
 * 文字列リストダイアログ
 * <p/>
 * タイトルと1個以上のクリック項目を持つ、汎用ダイアログフラグメントです。<ul>
 * <li>親画面は {@link Listener} を実装した {@link Fragment} 又は {@link AppCompatActivity}
 * でなければなりません。</li>
 * <li>{@link Builder}を使用して表示内容を設定し、 {@link Builder#show()}
 * メソッドを呼び出して表示してください。</li>
 * <li>ダイアログ終了時 {@link Listener#onDialogResult(int, int, Intent)}
 * メソッドが呼び出されます。</li>
 * <li>クリック項目がクリックされた場合、
 * {@link Listener#onDialogResult(int, int, Intent)} メソッドの引数 resultCode の値が
 * {@link Activity#RESULT_OK} となります。このとき、引数 data の
 * {@link Intent#getIntExtra(String, int)} へ {@link ListDialog#EXTRA_KEY_WHICH}
 * を入力することで、クリックされた項目の位置を取得できます。</li>
 * <li>ダイアログがキャンセル終了した場合、
 * {@link Listener#onDialogResult(int, int, Intent)} メソッドの引数 resultCode の値が
 * {@link Activity#RESULT_CANCELED} となります。</li>
 * </ul>
 */
@SuppressWarnings("unused")
public class StringArrayDialog extends ListDialog {
    /**
     * ダイアログビルダ
     */
    public static class Builder extends ListDialog.Builder<CharSequence> {
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
        @Override
        @NonNull
        public Builder setTitle(@NonNull String title) {
            super.setTitle(title);
            return this;
        }

        /**
         * このメソッドは使用できません。
         *
         * @param message メッセージ文言
         * @return {@link Builder}
         * @throws IllegalArgumentException このメソッドが呼び出されました。
         */
        @Override
        @NonNull
        public Builder setMessage(@NonNull String message) {
            throw new IllegalArgumentException(
                    "Don't use setMessage for StringArrayDialog. Use setTitle() instead.");
        }

        /**
         * インスタンス識別用タグを設定します。
         *
         * @param tag タグ
         * @return {@link Builder}
         */
        @Override
        @NonNull
        public Builder setTag(@NonNull String tag) {
            super.setTag(tag);
            return this;
        }

        /**
         * 選択項目リストを設定します。
         * {@link Builder#setItems(int)} と併用した場合、後から呼び出した内容のみが使用されます。
         *
         * @param items 選択項目リストとなる、文字列配列
         * @return {@link Builder}
         */
        @Override
        @NonNull
        public Builder setItems(@NonNull CharSequence[] items) {
            this.items = items;
            return this;
        }

        /**
         * 選択項目リストを設定します。
         * {@link Builder#setItems(CharSequence[])}
         * と併用した場合、後から呼び出した内容のみが使用されます。
         *
         * @param resourceId 選択項目リストとなる、文字列配列のリソースID
         * @return {@link Builder}
         */
        @NonNull
        public Builder setItems(int resourceId) {
            this.items = this.getContext().getResources().getStringArray(resourceId);
            return this;
        }

        /**
         * 実装クラスのインスタンスを生成して返します。
         *
         * @return {@link DialogBase}
         */
        @Override
        @NonNull
        protected DialogBase createFragment() {
            return new StringArrayDialog();
        }

        /**
         * ダイアログへ渡す引数を生成して返します。
         *
         * @return 引数を含んだ {@link Bundle}
         */
        @Override
        @NonNull
        protected Bundle makeArguments() {
            final Bundle arguments = super.makeArguments();

            arguments.putCharSequenceArray(KEY_ITEMS, this.items);

            return arguments;
        }
    }

    private static final String TAG = "StringArrayDialog";

    /**
     * ダイアログ生成処理
     *
     * @param savedInstanceState 前回強制終了時の保存データ
     * @return 生成した{@link Dialog}
     */
    @Override
    @NonNull
    public Dialog createDialog(Bundle savedInstanceState) {
        if (this.listener == null) {
            throw new RuntimeException("listener is null");
        }
        final Bundle arguments = this.requireArguments();
        final CharSequence[] items = arguments.getCharSequenceArray(KEY_ITEMS);
        Log.d(TAG, "createDialog: items = " + (items != null ? Arrays.asList(items) : "(null)"));
        return new AlertDialog.Builder(this.requireActivity(), this.getTheme())
                .setTitle(arguments.getString(KEY_TITLE))
                .setMessage(arguments.getString(KEY_MESSAGE))
                .setItems(items, new DialogItemClickListener(this, this.listener))
                .create();
    }
}
