package net.imoya.android.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

/**
 * 追加ボタン付き複数項目選択ダイアログの abstract
 * <p/>
 * タイトル、複数選択リスト、追加ボタン、OKボタン、キャンセルボタンを持つ、
 * 汎用ダイアログフラグメントの abstract です。<ul>
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
 * <li>追加ボタンクリック時の処理は、このクラスの派生クラスを作成し、
 * {@link #onClickAdditionalButton()} メソッドを override して実装してください。</li>
 * </ul>
 */
@SuppressWarnings("unused")
public abstract class MultiChoiceAndButtonDialogBase extends MultiChoiceDialogBase
        implements View.OnClickListener {
    /**
     * ダイアログビルダ
     */
    public abstract static class Builder extends MultiChoiceDialogBase.Builder {
        /**
         * 追加ボタン文言
         */
        private String additionalButtonText;

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
         * タイトル文言を設定します。
         *
         * @param title タイトル文言
         * @return {@link Builder}
         */
        @Override
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
        @Override
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
        @Override
        public Builder setNegativeButtonTitle(@NonNull String buttonTitle) {
            super.setNegativeButtonTitle(buttonTitle);
            return this;
        }

        /**
         * 選択項目リストを設定します。
         *
         * @param items 選択項目リスト
         * @return {@link Builder}
         */
        public Builder setItems(@NonNull String[] items) {
            super.setItems(items);
            return this;
        }

        /**
         * 全選択項目の初期選択状態リストを設定します。
         *
         * @param checkedList 全選択項目の選択状態リスト
         * @return {@link Builder}
         */
        public Builder setCheckedList(boolean[] checkedList) {
            super.setCheckedList(checkedList);
            return this;
        }

        /**
         * 追加ボタン文言を設定します。
         *
         * @param additionalButtonText 追加ボタン文言
         * @return {@link Builder}
         */
        public Builder setAdditionalButtonText(String additionalButtonText) {
            this.additionalButtonText = additionalButtonText;
            return this;
        }

        /**
         * ダイアログへ渡す引数を生成して返します。
         *
         * @return 引数を含んだ {@link Bundle}
         */
        @Override
        protected Bundle makeArguments() {
            final Bundle arguments = super.makeArguments();

            arguments.putString(KEY_ADDITIONAL_BUTTON_TEXT, this.additionalButtonText);

            return arguments;
        }
    }

    /**
     * 複数選択項目チェック状態リスナ実装
     */
    private class CheckBoxHandler implements CompoundButton.OnCheckedChangeListener {
        /**
         * 項目の位置
         */
        private final int position;

        /**
         * コンストラクタ
         *
         * @param position 項目の位置
         */
        private CheckBoxHandler(int position) {
            this.position = position;
        }

        /**
         * チェック状態変化時の処理
         *
         * @param buttonView チェック状態が変化した {@link CompoundButton}
         * @param isChecked 変化後のチェック状態
         */
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            MultiChoiceAndButtonDialogBase.this.checkedList[this.position] = isChecked;
        }
    }

    /**
     * ダイアログ引数キー:追加ボタン文言
     */
    private static final String KEY_ADDITIONAL_BUTTON_TEXT = "additionalButtonText";

//    private static final String TAG = "MultiChoiceAndButtonDialogBase";

    /**
     * ダイアログ生成処理
     *
     * @param savedInstanceState 前回強制終了時の保存データ
     * @return 生成した{@link Dialog}
     */
    @NonNull
    @Override
    public Dialog createDialog(Bundle savedInstanceState) {
        final Bundle arguments = this.requireArguments();
        final AlertDialog.Builder builder = new AlertDialog.Builder(
                this.requireContext(), this.getTheme())
                .setTitle(arguments.getString(KEY_TITLE))
                .setPositiveButton(
                        arguments.getString(KEY_POSITIVE_BUTTON_TITLE),
                        new DialogButtonClickListener(this, this.listener))
                .setNegativeButton(
                        arguments.getString(KEY_NEGATIVE_BUTTON_TITLE),
                        new DialogButtonClickListener(this, this.listener));

        final LayoutInflater inflater = LayoutInflater.from(builder.getContext());
        final View view = inflater.inflate(R.layout.dialog_multi_choise_and_button, null);
        final LinearLayout group = view.findViewById(R.id.choice);
        for (int i = 0; i < this.items.length; i++) {
            final CheckBox itemView = (CheckBox) inflater.inflate(
                    R.layout.multi_choice_item, group, false);
            itemView.setText(this.items[i]);
            itemView.setChecked(this.checkedList[i]);
            itemView.setOnCheckedChangeListener(new CheckBoxHandler(i));
            group.addView(itemView);
        }
        final String additionalButtonText = arguments.getString(KEY_ADDITIONAL_BUTTON_TEXT);
        final Button additionalButton = view.findViewById(R.id.button);
        additionalButton.setVisibility(additionalButtonText != null ? View.VISIBLE : View.GONE);
        additionalButton.setText(additionalButtonText);
        additionalButton.setOnClickListener(this);

        return builder.setView(view).create();
    }

    /**
     * {@link View} クリック時の処理
     *
     * @param view クリックされた{@link View}
     */
    @Override
    public void onClick(View view) {
        if (view != null && view.getId() == R.id.button) {
            // 追加ボタンクリック時
            this.onClickAdditionalButton();
        }
    }

    /**
     * 追加ボタンクリック時の処理
     */
    protected abstract void onClickAdditionalButton();
}
