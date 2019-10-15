package `fun`.gladkikh.fastpallet5.ui

import android.text.Editable


fun returnTextWatcherOnChanged(onTextChanged : (p0: CharSequence?) -> Unit):android.text.TextWatcher{
    return object : android.text.TextWatcher {
        override fun afterTextChanged(p0: Editable?) {
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            onTextChanged(p0)
        }
    }

}

