package net.imoya.android.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

/**
 * 複数項目選択ダイアログ
 * <p/>
 * タイトル、複数選択リスト、OKボタン、キャンセルボタンを持つ、
 * 汎用ダイアログフラグメントです。<ul>
 * <li>親画面は {@link Listener} を実装した
 * {@link Fragment} 又は {@link AppCompatActivity} でなければなりません。</li>
 * <li>{@link Builder}を使用して表示内容を設定し、 {@link Builder#show()}
 * メソッドを呼び出して表示してください。</li>
 * <li>ダイアログ終了時 {@link Listener#onDialogResult(int, int, Intent)}
 * メソッドが呼び出されます。</li>
 * <li>OKボタンがクリックされた場合は、
 * {@link Listener#onDialogResult(int, int, Intent)} メソッドの引数 resultCode の値が
 * {@link Activity#RESULT_OK} となります。このとき、引数 data の
 * {@link Intent#getBooleanArrayExtra(String)} へ {@link #EXTRA_KEY_CHECKED_LIST}
 * を入力することで、全項目の選択状態リストを取得できます。</li>
 * <li>キャンセルボタンがクリックされた場合、又はダイアログがキャンセル終了した場合は、
 * {@link Listener#onDialogResult(int, int, Intent)} メソッドの引数 resultCode の値が
 * {@link Activity#RESULT_CANCELED} となります。</li>
 * </ul>
 */
@SuppressWarnings("unused")
public class MultiChoiceDialog extends MultiChoiceDialogBase {
    /**
     * ダイアログビルダ
     */
    public static class Builder extends MultiChoiceDialogBase.Builder {
        /**
         * コンストラクタ
         *
         * @param activity    親画面となる {@link AppCompatActivity}
         * @param requestCode リクエストコード
         * @param <T>         親画面は {@link Listener} を実装した {@link AppCompatActivity} であること
         */
        public <T extends AppCompatActivity & Listener> Builder(
                @NonNull T activity, int requestCode) {
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
         * 実装クラスのインスタンスを生成して返します。
         *
         * @return {@link DialogBase}
         */
        @Override
        @NonNull
        protected DialogBase createFragment() {
            return new MultiChoiceDialog();
        }
    }

//    private static final String TAG = "MultiChoiceDialog";

    /**
     * ダイアログ生成処理
     *
     * @param savedInstanceState 前回強制終了時の保存データ
     * @return 生成した{@link Dialog}
     */
    @NonNull
    @Override
    public Dialog createDialog(@NonNull Bundle savedInstanceState) {
        final Bundle arguments = this.requireArguments();
        return new AlertDialog.Builder(this.requireContext(), this.getTheme())
                .setTitle(arguments.getString(KEY_TITLE))
                .setMessage(arguments.getString(KEY_MESSAGE))
                .setMultiChoiceItems(this.items, this.checkedList,
                        (dialog, which, isChecked) -> this.checkedList[which] = isChecked)
                .setPositiveButton(
                        arguments.getString(KEY_POSITIVE_BUTTON_TITLE),
                        new DialogButtonClickListener(this, this.listener))
                .setNegativeButton(
                        arguments.getString(KEY_NEGATIVE_BUTTON_TITLE),
                        new DialogButtonClickListener(this, this.listener))
                .create();
    }
}
