package hu.ait.shoppinglist.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import hu.ait.shoppinglist.R
import hu.ait.shoppinglist.ScrollingActivity
import hu.ait.shoppinglist.data.ShoppingItem
import hu.ait.shoppinglist.databinding.ShoppingDialogBinding

class ShoppingItemDialog: DialogFragment() {
    interface ShoppingItemDialogHandler{
        fun shoppingItemCreated(item: ShoppingItem)
        fun shoppingItemUpdated(item: ShoppingItem)
    }

    private lateinit var shoppingItemHandler: ShoppingItemDialogHandler

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is ShoppingItemDialogHandler){
            shoppingItemHandler = context
        }else{
            throw RuntimeException("The Activity does not implement the ShoppingItemDialogHandler")
        }
    }

    private lateinit var shoppingDialogBinding: ShoppingDialogBinding
    private var isEditMode = false

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())

        // Are we in edit mode? - Have we received a Todo object to edit?
        if (arguments != null && requireArguments().containsKey(
                ScrollingActivity.KEY_TODO_EDIT)) {
            isEditMode = true
            builder.setTitle("Edit ShoppingItem")
        } else {
            isEditMode = false
            builder.setTitle("New ShoppingItem")
        }

        shoppingDialogBinding = ShoppingDialogBinding.inflate(
            requireActivity().layoutInflater)
        builder.setView(shoppingDialogBinding.root)

         //pre-fill the dialog if we are in edit mode
//        if (isEditMode) {
//            val shoppingEdit =
//                requireArguments().getSerializable(
//                    ScrollingActivity.KEY_TODO_EDIT) as ShoppingItem
//
//            shoppingDialogBinding.etName.setText(shoppingEdit.name)
//            shoppingDialogBinding.etPrice.setText(shoppingEdit.price.toString())
//            shoppingDialogBinding.spinnerCategory.selectedItemPosition
//        }

        var categoryAdapter = ArrayAdapter.createFromResource(
            requireActivity(),
            R.array.category_array,
            android.R.layout.simple_spinner_item
        )

        categoryAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        shoppingDialogBinding.spinnerCategory.adapter = categoryAdapter

        builder.setPositiveButton("OK"){dialog, which ->
//            if (isEditMode) {
//                val shoppingToEdit =
//                    (requireArguments().getSerializable(
//                        ScrollingActivity.KEY_TODO_EDIT
//                    ) as ShoppingItem).copy(
//                        name = shoppingDialogBinding.etName.text.toString(),
//                        price = shoppingDialogBinding.etPrice.text.toString().toInt()
//                    )
//                shoppingItemHandler.shoppingItemUpdated(shoppingToEdit)
//            }
//            else{
//                handleItemCreate()
//            }

        }

        builder.setNegativeButton("Cancel") {
                dialog, which ->
        }

        return builder.create()
    }

    override fun onResume() {
        super.onResume()

        val dialog = dialog as AlertDialog
        val positiveButton = dialog.getButton(Dialog.BUTTON_POSITIVE)

        positiveButton.setOnClickListener {
            if (shoppingDialogBinding.etName.text.isNotEmpty()) {
                if (shoppingDialogBinding.etPrice.text.isNotEmpty()) {
                    handleItemCreate()

                    dialog.dismiss()
                } else {
                    shoppingDialogBinding.etPrice.error = "This field can not be empty"
                }
            } else {
                shoppingDialogBinding.etName.error = "This field can not be empty"
            }
        }
    }

    fun handleItemCreate(){
        shoppingItemHandler.shoppingItemCreated(
            ShoppingItem(
                shoppingDialogBinding.etName.text.toString(),
                shoppingDialogBinding.etPrice.text.toString().toInt(),
                "Demo",
                false,
                shoppingDialogBinding.spinnerCategory.selectedItemPosition
            )
        )

    }
}