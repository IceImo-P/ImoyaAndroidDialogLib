package net.imoya.android.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import net.imoya.android.dialog.SingleChoiceAndButtonDialogBase.Builder

/**
 * 追加ボタン付き単一項目選択ダイアログの abstract
 *
 * タイトル, 単一選択リスト, 追加ボタン, OKボタン, キャンセルボタンを持つダイアログ [Fragment] の abstract です。
 *  * [Builder]を使用して表示内容を設定し、 [Builder.show] メソッドをコールして表示してください。
 *  * ダイアログ終了時 [DialogListener.onDialogResult] メソッドがコールされます。
 *  * OKボタン押下に伴うダイアログ終了時、 [DialogListener.onDialogResult] メソッドの引数
 *  resultCode の値が [Activity.RESULT_OK] となります。このとき、引数 data の [Intent.getIntExtra] へ
 * [DialogBase.EXTRA_KEY_WHICH] を入力することで、選択された項目の位置(又は、未選択を表す -1)を取得できます。
 *  * OKボタン押下以外の理由でダイアログが終了した場合は、
 * [DialogListener.onDialogResult] メソッドの引数 resultCode の値が
 * [Activity.RESULT_CANCELED] となります。
 *  * 追加ボタンクリック時の処理は、このクラスの派生クラスを作成し、
 * [SingleChoiceAndButtonDialogBase.onClickAdditionalButton] メソッドを override して実装してください。
 */
@Suppress("unused")
abstract class SingleChoiceAndButtonDialogBase : SingleChoiceDialogBase(),
    RadioGroup.OnCheckedChangeListener, View.OnClickListener {
    /**
     * ダイアログビルダ
     *
     * @param parent    親画面
     * @param requestCode リクエストコード
     */
    abstract class Builder(parent: DialogParent, requestCode: Int) :
        SingleChoiceDialogBase.Builder(parent, requestCode) {
        /**
         * 追加ボタン文言
         */
        @JvmField
        protected var additionalButtonText: String? = null

        /**
         * タイトル文言を設定します。
         *
         * @param title タイトル文言
         * @return [Builder]
         */
        override fun setTitle(title: String): Builder {
            super.setTitle(title)
            return this
        }

        /**
         * このメソッドは使用できません。
         *
         * @param message メッセージ文言
         * @return [Builder]
         * @throws IllegalArgumentException このメソッドが呼び出されました。
         */
        override fun setMessage(message: String): Builder {
            throw IllegalArgumentException(
                "Don't use setMessage for StringArrayDialog. Use setTitle() instead."
            )
        }

        /**
         * インスタンス識別用タグを設定します。
         *
         * @param tag タグ
         * @return [Builder]
         */
        override fun setTag(tag: String): Builder {
            super.setTag(tag)
            return this
        }

        /**
         * OKボタン文言を設定します。デフォルト値は [android.R.string.ok] です。
         *
         * @param buttonTitle ボタン文言
         * @return [Builder]
         */
        override fun setPositiveButtonTitle(buttonTitle: String): Builder {
            super.setPositiveButtonTitle(buttonTitle)
            return this
        }

        /**
         * キャンセルボタン文言を設定します。デフォルト値は [android.R.string.cancel] です。
         *
         * @param buttonTitle ボタン文言
         * @return [Builder]
         */
        override fun setNegativeButtonTitle(buttonTitle: String): Builder {
            super.setNegativeButtonTitle(buttonTitle)
            return this
        }

        /**
         * 選択項目リストを設定します。
         *
         * @param items 選択項目リスト
         * @return [Builder]
         */
        override fun setItems(items: Array<String>?): Builder {
            super.setItems(items)
            return this
        }

        /**
         * 初期選択位置を設定します。
         *
         * @param selectedPosition 初期選択位置、又は未選択状態を指定する -1
         * @return [Builder]
         */
        override fun setSelectedPosition(selectedPosition: Int): Builder {
            super.setSelectedPosition(selectedPosition)
            return this
        }

        /**
         * 追加ボタン文言を設定します。
         *
         * @param additionalButtonText 追加ボタン文言
         * @return [Builder]
         */
        fun setAdditionalButtonText(additionalButtonText: String?): Builder {
            this.additionalButtonText = additionalButtonText
            return this
        }

        override fun makeArguments(): Bundle {
            val arguments = super.makeArguments()
            arguments.putString(KEY_ADDITIONAL_BUTTON_TEXT, additionalButtonText)
            return arguments
        }
    }

    override fun createDialog(savedInstanceState: Bundle?): Dialog {
        val arguments = requireArguments()
        val builder = AlertDialog.Builder(
            requireContext(), this.theme
        )
            .setTitle(arguments.getString(KEY_TITLE))
            .setPositiveButton(
                arguments.getString(KEY_POSITIVE_BUTTON_TITLE),
                DialogButtonClickListener(this, listener)
            )
            .setNegativeButton(
                arguments.getString(KEY_NEGATIVE_BUTTON_TITLE),
                DialogButtonClickListener(this, listener)
            )
        val inflater = LayoutInflater.from(builder.context)
        val view = inflater.inflate(R.layout.dialog_single_choise_and_button, null)
        val radioGroup = view.findViewById<RadioGroup>(R.id.choice)
        for (i in items.indices) {
            val itemView = inflater.inflate(
                R.layout.single_choice_item, radioGroup, false
            ) as RadioButton
            itemView.text = items[i]
            itemView.isChecked = i == selectedPosition
            itemView.id = i
            radioGroup.addView(itemView)
        }
        radioGroup.setOnCheckedChangeListener(this)
        val additionalButtonText = arguments.getString(KEY_ADDITIONAL_BUTTON_TEXT)
        val additionalButton = view.findViewById<Button>(R.id.button)
        additionalButton.visibility =
            if (additionalButtonText != null) View.VISIBLE else View.GONE
        additionalButton.text = additionalButtonText
        additionalButton.setOnClickListener(this)
        return builder.setView(view).create()
    }

    /**
     * 選択項目変化時の処理
     *
     * @param group [RadioGroup]
     * @param checkedId 選択された項目のID
     */
    override fun onCheckedChanged(group: RadioGroup, checkedId: Int) {
        // IDを保存する(IDが位置と同じ値になっている)
        selectedPosition = checkedId
    }

    /**
     * [View] クリック時の処理
     *
     * @param view クリックされた[View]
     */
    override fun onClick(view: View) {
        if (view.id == R.id.button) {
            // 追加ボタンクリック時
            onClickAdditionalButton()
        }
    }

    /**
     * 追加ボタンクリック時の処理
     */
    protected abstract fun onClickAdditionalButton()

    companion object {
        /**
         * ダイアログ引数キー:追加ボタン文言
         */
        private const val KEY_ADDITIONAL_BUTTON_TEXT = "additionalButtonText"

        /**
         * Tag for log
         */
        private const val TAG = "SingleChoiceAndButtonDialogBase"
    }
}