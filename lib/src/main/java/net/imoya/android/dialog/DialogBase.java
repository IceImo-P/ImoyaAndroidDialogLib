package net.imoya.android.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import net.imoya.android.util.Log;

/**
 * ダイアログフラグメントの abstract
 *
 * 汎用ダイアログフラグメントの abstract です。<ul>
 * <li>親画面は {@link Listener} を実装した
 * {@link Fragment} 又は {@link AppCompatActivity} でなければなりません。</li>
 * <li>{@link Builder}を使用して表示内容を設定し、 {@link Builder#show()}
 * メソッドを呼び出して表示してください。</li>
 * <li>ダイアログ終了時 {@link Listener#onDialogResult(int, int, Intent)}
 * メソッドが呼び出されます。</li>
 * <li>ダイアログがキャンセル終了した場合、
 * {@link Listener#onDialogResult(int, int, Intent)} メソッドの引数 resultCode の値が
 * {@link Activity#RESULT_CANCELED} となります。</li>
 * </ul>
 */
@SuppressWarnings("unused")
public abstract class DialogBase extends AppCompatDialogFragment
        implements DialogInterface.OnCancelListener {
    /**
     * 結果キー定義:タグ
     */
    public static final String EXTRA_KEY_TAG = "DialogBase.tag";
    /**
     * 結果キー定義:クリックされた項目
     */
    public static final String EXTRA_KEY_WHICH = "DialogBase.which";

    /**
     * ダイアログリスナ
     */
    public interface Listener {
        /**
         * ダイアログ終了時に呼び出されます。
         *
         * @param requestCode {@link Builder} に設定したリクエストコード
         * @param resultCode 結果コード
         * @param data 追加のデータを含む {@link Intent}
         */
        void onDialogResult(int requestCode, int resultCode, @Nullable Intent data);
    }

    /**
     * ダイアログビルダ
     */
    public static abstract class Builder {
        /**
         * 親画面(Activity)
         */
        protected final AppCompatActivity activity;
        /**
         * 親画面(Fragment)
         */
        protected final Fragment fragment;
        /**
         * リクエストコード
         */
        private final int requestCode;
        /**
         * タイトル
         */
        private String title;
        /**
         * メッセージ
         */
        private String message;
        /**
         * タグ
         */
        private String tag;
        /**
         * キャンセル可能フラグ
         */
        private boolean cancelable = true;
        /**
         * ダイアログ外クリック時キャンセル実行フラグ
         */
        private boolean canceledOnTouchOutside = true;

        /**
         * コンストラクタ
         *
         * @param activity    親画面となる {@link AppCompatActivity}
         * @param requestCode リクエストコード
         * @param <T>         親画面は {@link Listener} を実装した {@link AppCompatActivity} であること
         */
        public <T extends AppCompatActivity & Listener> Builder(
                @NonNull T activity, int requestCode) {
            this.activity = activity;
            this.fragment = null;
            this.requestCode = requestCode;
        }

        /**
         * コンストラクタ
         *
         * @param fragment    親画面となる{@link Fragment}
         * @param requestCode リクエストコード
         * @param <T>         親画面は {@link Listener} を実装した {@link Fragment} であること
         */
        public <T extends Fragment & Listener> Builder(@NonNull T fragment, int requestCode) {
            this.activity = null;
            this.fragment = fragment;
            this.requestCode = requestCode;
        }

        /**
         * タイトル文言を設定します。
         *
         * @param title タイトル文言
         * @return {@link Builder}
         */
        public Builder setTitle(@NonNull String title) {
            this.title = title;
            return this;
        }

        /**
         * メッセージ文言を設定します。
         *
         * @param message メッセージ文言
         * @return {@link Builder}
         */
        public Builder setMessage(@NonNull String message) {
            this.message = message;
            return this;
        }

        /**
         * インスタンス識別用タグを設定します。
         *
         * @param tag タグ
         * @return {@link Builder}
         */
        public Builder setTag(@NonNull String tag) {
            this.tag = tag;
            return this;
        }

        /**
         * キャンセル可能フラグを設定します。
         * デフォルト値は true です。
         *
         * @param cancelable キャンセル可能フラグ
         * @return {@link Builder}
         */
        public Builder setCancelable(boolean cancelable) {
            this.cancelable = cancelable;
            return this;
        }

        /**
         * ダイアログ外クリック時キャンセル実行フラグを設定します。
         * デフォルト値は true です。
         *
         * @param canceledOnTouchOutside ダイアログ外クリック時キャンセル実行フラグ
         * @return {@link Builder}
         */
        public Builder setCanceledOnTouchOutside(boolean canceledOnTouchOutside) {
            this.canceledOnTouchOutside = canceledOnTouchOutside;
            return this;
        }

        /**
         * アプリケーションの {@link Context} を取得します。
         *
         * @return {@link Context}
         * @throws IllegalStateException 親画面が null です。
         */
        @NonNull
        protected Context getContext() {
            if (this.activity != null) {
                return this.activity.getApplicationContext();
            } else if (this.fragment != null) {
//                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//                    return this.fragment.getActivity().getApplicationContext();
//                } else {
//                    return this.fragment.getContext().getApplicationContext();
//                }
                return this.fragment.requireContext().getApplicationContext();
            } else {
                throw new IllegalStateException("Parent screen is null");
            }
        }

        /**
         * 実装クラスのインスタンスを生成して返します。
         *
         * @return {@link DialogBase}
         */
        protected abstract DialogBase createFragment();

        /**
         * ダイアログへ渡す引数を生成して返します。
         *
         * @return 引数を含んだ {@link Bundle}
         */
        protected Bundle makeArguments() {
            final Bundle arguments = new Bundle();
            arguments.putInt(KEY_REQUEST_CODE, this.requestCode);
            arguments.putString(EXTRA_KEY_TAG, this.tag);
            if (this.title != null) {
                arguments.putString(KEY_TITLE, this.title);
            }
            if (this.message != null) {
                arguments.putString(KEY_MESSAGE, this.message);
            }
            arguments.putBoolean(KEY_CANCELABLE, this.cancelable);
            arguments.putBoolean(KEY_CANCELED_ON_TOUCH_OUTSIDE, this.canceledOnTouchOutside);
            return arguments;
        }

        /**
         * ダイアログを表示します。
         */
        public void show() {
            DialogBase fragment = this.createFragment();
            fragment.setArguments(this.makeArguments());
            if (this.fragment != null) {
//                fragment.setTargetFragment(this.fragment, this.requestCode);
                fragment.listener = (Listener) this.fragment;
            }
            fragment.show(this.getFragmentManager(), this.tag);
        }

        private FragmentManager getFragmentManager() {
            if (this.activity != null) {
                return this.activity.getSupportFragmentManager();
            } else if (this.fragment != null) {
//                return this.fragment.getFragmentManager();
                return this.fragment.getParentFragmentManager();
            } else {
                throw new IllegalArgumentException();
            }
        }
    }

    /**
     * ダイアログクリックリスナの実装
     */
    protected static class DialogItemClickListener implements DialogInterface.OnClickListener {
        /**
         * ダイアログ
         */
        protected final DialogBase dialog;
        /**
         * ダイアログのリスナ
         */
        protected final Listener listener;
        /**
         * 押された項目
         */
        protected int which;

        /**
         * コンストラクタ
         *
         * @param dialog   ダイアログ
         * @param listener ダイアログのリスナ
         */
        public DialogItemClickListener(DialogBase dialog, Listener listener) {
            this.dialog = dialog;
            this.listener = listener;
        }

        /**
         * ボタン押下時に {@link Fragment#onActivityResult(int, int, Intent)} 又は
         * {@link Listener#onDialogResult(int, int, Intent)} へ通知する
         * {@link Intent} を生成して返します。
         *
         * @return {@link Intent}
         */
        protected Intent makeData() {
            final Intent data = new Intent();
            data.putExtra(EXTRA_KEY_WHICH, this.which);
            final String tag = this.dialog.getTag();
            if (tag != null) {
                data.putExtra(EXTRA_KEY_TAG, tag);
            }
            return data;
        }

        /**
         * ダイアログリスナへ、クリックを通知します。
         *
         * @param dialogInterface {@link DialogInterface}
         * @param which           クリックされた項目
         */
        protected void callListener(DialogInterface dialogInterface, int which) {
            this.listener.onDialogResult(
                    this.dialog.getRequestCode(), Activity.RESULT_OK, this.makeData());
        }

        /**
         * 項目クリック時の処理
         *
         * @param dialogInterface {@link DialogInterface}
         * @param which           クリックされた項目
         */
        @Override
        public void onClick(DialogInterface dialogInterface, int which) {
            this.which = which;
            try {
                // 親画面へ通知する
                this.callListener(dialogInterface, which);
            } catch (Exception e) {
                Log.e(TAG, "Exception", e);
            }
        }
    }

    /**
     * ダイアログ引数キー:リクエストコード
     */
    protected static final String KEY_REQUEST_CODE = "requestCode";
    /**
     * ダイアログ引数キー:タイトル
     */
    protected static final String KEY_TITLE = "title";
    /**
     * ダイアログ引数キー:メッセージ
     */
    protected static final String KEY_MESSAGE = "message";
    /**
     * ダイアログ引数キー:カスタムビューのレイアウトリソースID
     */
    protected static final String KEY_LAYOUT_RESOURCE_ID = "layoutResourceId";
    /**
     * ダイアログ引数キー:キャンセル可能フラグ
     */
    protected static final String KEY_CANCELABLE = "cancelable";
    /**
     * ダイアログ引数キー:ダイアログ外クリック時キャンセル実行フラグ
     */
    protected static final String KEY_CANCELED_ON_TOUCH_OUTSIDE = "canceledOnTouchOutside";

    private static final String TAG = "DialogBase";

    /**
     * リスナ
     */
    protected Listener listener = null;

    /**
     * リクエストコードを取得します。
     *
     * @return リクエストコード
     */
    public int getRequestCode() {
        return this.requireArguments().getInt(KEY_REQUEST_CODE);
    }

    /**
     * Called when a fragment is first attached to its context.
     * {@link Fragment#onCreate(Bundle)} will be called after this.
     *
     * @param context {@link Context}
     */
    @SuppressWarnings("NullableProblems")
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
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Bundle arguments = this.requireArguments();
        final Dialog dialog = this.createDialog(savedInstanceState);
        dialog.setCancelable(arguments.getBoolean(KEY_CANCELABLE, true));
        dialog.setCanceledOnTouchOutside(arguments.getBoolean(KEY_CANCELED_ON_TOUCH_OUTSIDE, true));
        dialog.setOnCancelListener(this);
        return dialog;
    }

    /**
     * ダイアログ生成処理
     *
     * @param savedInstanceState 前回強制終了時の保存データ
     * @return 生成した{@link Dialog}
     */
    @NonNull
    protected abstract Dialog createDialog(Bundle savedInstanceState);

    /**
     * ダイアログキャンセル時処理
     *
     * @param dialog キャンセルされたダイアログ
     */
    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
        try {
            // 親画面へ通知する
            this.callCancelListener();
        } catch (Exception e) {
            Log.e(TAG, "Exception", e);
        }
    }

    /**
     * リスナへキャンセルを通知します。
     */
    protected void callCancelListener() {
        if (this.listener != null) {
            this.listener.onDialogResult(this.getRequestCode(), Activity.RESULT_CANCELED, null);
        }
    }
}
