package com.iwan.ets

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageButton
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iwan.ets.databinding.FragmentContactListBinding


class ContactListFragment : Fragment(), View.OnClickListener {
    lateinit var contactAdapter: ContactListAdapater
    lateinit var contactList: List<ContactModel>
    lateinit var rvContact: RecyclerView

    lateinit var databaseHandler: DatabaseHandler

    var activeContact: ContactModel? = null


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentContactListBinding>(inflater,
                R.layout.fragment_contact_list, container, false)

        val btnAdd: AppCompatImageButton = binding.btnAdd
        val btnEdit: AppCompatImageButton = binding.btnEdit
        val btnDelete: AppCompatImageButton = binding.btnDelete
        val btnViewContact: AppCompatImageButton = binding.btnFind

        btnAdd.setOnClickListener(this)
        btnEdit.setOnClickListener(this)
        btnDelete.setOnClickListener(this)
        btnViewContact.setOnClickListener(this)

        databaseHandler = DatabaseHandler(requireActivity())
        contactList = databaseHandler.viewContacts()

        contactAdapter = ContactListAdapater(contactList, R.layout.listview_contact_item)

        rvContact = binding.rvContact
        rvContact.adapter = contactAdapter;
        rvContact.layoutManager = LinearLayoutManager(requireContext())

        contactAdapter.onItemClick = { contact ->
            activeContact = contact
            Toast.makeText(requireContext(), "Contact ${contact.name} selected", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btnAdd -> {
                showAddDialog()
            }
            R.id.btnEdit -> {
                showEditDialog()
            }
            R.id.btnDelete -> {
                showDeleteDialog()
            }
            R.id.btnFind -> {
                val number = Uri.parse("tel:" + activeContact!!.phone)
                val intent = Intent(Intent.ACTION_DIAL, number)
                requireActivity().startActivity(intent)
            }
        }
    }

    private fun showAddDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.input_dialog)

        val etName = dialog.findViewById(R.id.etName) as EditText
        val etPhone = dialog.findViewById(R.id.etPhone) as EditText
        val databaseHandler = DatabaseHandler(requireContext())

        val btnPositive = dialog.findViewById(R.id.btnPositive) as Button
        val btnNegative = dialog.findViewById(R.id.btnNegative) as Button
        btnPositive.setOnClickListener {
            if(etName.text.toString().trim()!="" && etPhone.text.toString().trim()!=""){
                println(etName.text.toString())
                println(etPhone.text.toString())
                val status = databaseHandler.addContact(ContactModel(etName.text.toString(), etPhone.text.toString()))
                if(status > -1){
                    Toast.makeText(requireContext(), "Contact saved", Toast.LENGTH_SHORT).show()
                    contactList = contactList + listOf(ContactModel(etName.text.toString(), etPhone.text.toString()))
                    println(contactList.size)
                    contactAdapter.setData(contactList)
                    contactAdapter.notifyDataSetChanged()
                }
            }
            else{
                Toast.makeText(requireContext(), "Name and phone cannot be empty", Toast.LENGTH_LONG).show()
            }

            dialog.dismiss()
        }
        btnNegative.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    private fun showEditDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.input_dialog)

        val etName = dialog.findViewById(R.id.etName) as EditText
        val etPhone = dialog.findViewById(R.id.etPhone) as EditText

        etName.setText(activeContact?.name)
        etPhone.setText(activeContact?.phone)

        val databaseHandler = DatabaseHandler(requireContext())

        val btnPositive = dialog.findViewById(R.id.btnPositive) as Button
        val btnNegative = dialog.findViewById(R.id.btnNegative) as Button
        btnPositive.setOnClickListener {
            if(etName.text.toString().trim()!="" && etPhone.text.toString().trim()!=""){
                println(etName.text.toString())
                println(etPhone.text.toString())
                val status = databaseHandler.updateContact(activeContact!!.name, ContactModel(etName.text.toString(), etPhone.text.toString()))
                if(status > -1){
                    Toast.makeText(requireContext(), "Contact updated", Toast.LENGTH_SHORT).show()
                    contactList = databaseHandler.viewContacts()
                    println(contactList.size)
                    contactAdapter.setData(contactList)
                    contactAdapter.notifyDataSetChanged()
                }
            }
            else{
                Toast.makeText(requireContext(), "Name and phone cannot be empty", Toast.LENGTH_LONG).show()
            }

            dialog.dismiss()
        }
        btnNegative.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    private fun showDeleteDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.certainty_dialog)

        val databaseHandler = DatabaseHandler(requireContext())

        val btnPositive = dialog.findViewById(R.id.btnPositive) as Button
        val btnNegative = dialog.findViewById(R.id.btnNegative) as Button
        btnPositive.setOnClickListener {
            val status = databaseHandler.deleteContact(activeContact!!)
            if(status > -1){
                Toast.makeText(requireContext(), "Contact deleted", Toast.LENGTH_SHORT).show()
                contactList = databaseHandler.viewContacts()
                println(contactList.size)
                contactAdapter.setData(contactList)
                contactAdapter.notifyDataSetChanged()
            }

            dialog.dismiss()
        }
        btnNegative.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }
}