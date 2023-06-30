/*
 * Copyright (C) 2022 IceImo-P
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.imoya.android.dialog

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentResultListener
import net.imoya.android.util.BundleUtil

/**
 * [FragmentResultListener] が受け取った結果を [DialogListener] へ通知するロジック
 */
open class FragmentResultHandler(
    @JvmField
    val requestCode: Int,
    @JvmField
    val listener: DialogListener
) : FragmentResultListener {
    override fun onFragmentResult(requestKey: String, result: Bundle) {
        DialogLog.v(TAG) { "onFragmentResult: start. requestCode = $requestCode" }

        listener.onDialogResult(
            requestCode,
            result.getInt(DialogBase.KEY_INTERNAL_RESULT_CODE),
            BundleUtil.getParcelable(result, DialogBase.KEY_INTERNAL_RESULT_DATA, Intent::class.java)
        )

        DialogLog.v(TAG) { "onFragmentResult: end. requestCode = $requestCode" }
    }

    companion object {
        /**
         * Tag for log
         */
        private const val TAG = "FragmentResultHandler"
    }
}