package co.tiagoaguiar.course.instagram.common.view

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.LinearLayout
import android.widget.TextView
import co.tiagoaguiar.course.instagram.R
import co.tiagoaguiar.course.instagram.databinding.DialogCustomBinding

class CustomDialog(context: Context) : Dialog(context){

    private lateinit var binding: DialogCustomBinding

    private lateinit var txtButtons: Array<TextView>

    private var titleId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogCustomBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun setTitle(titleId: Int) {
        this.titleId = titleId
    }

    fun addButton(vararg texts: Int, listener: View.OnClickListener){
        txtButtons = Array(texts.size){
            TextView(context)
        }

        texts.forEachIndexed { index, text ->
            txtButtons[index].apply {
                id = text
                setText(text)
                setOnClickListener{
                    listener.onClick(it)
                    dismiss()
                }
            }
        }
    }

    @SuppressLint("ResourceAsColor")
    override fun show(){
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        super.show()

        titleId?.let{
            binding.dialogTitle.setText(it)
        }

        for(textView in txtButtons){
            val layoutParamsText = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            layoutParamsText.setMargins(30, 50, 30, 50)

            textView.setTextColor(R.color.black)
            binding.dialogContainer.addView(textView, layoutParamsText)

            val view = View(context)
            val layoutParamsView = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 2)
            view.setBackgroundResource(R.color.gray_darker)
            binding.dialogContainer.addView(view, layoutParamsView)
        }

        window?.setBackgroundDrawableResource(R.drawable.dialog_custom_background)
    }

}