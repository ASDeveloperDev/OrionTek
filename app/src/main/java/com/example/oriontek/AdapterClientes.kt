package com.example.oriontek

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

/**
OrionTek.
 *created by ASDeveloper on 27/09/2022 at 07:42.
 */

class AdapterClientes() : RecyclerView.Adapter<AdapterClientes.HolderClientes>(){

    private var context : Context? = null
    private var clientsList : MutableList<Clientes>? = null

    //db Helper
    lateinit var dbHelper:MyDbHelper
    //orden / query, por id Desc y Asc
    private var NEWEST_FIRST = "${Constantes.ID_C} DESC"
    private var OLDEST_FIRST = "${Constantes.ID_C} ASC"

    private var recentSortOrder = NEWEST_FIRST


    constructor(context: Context?, clientsList: MutableList<Clientes>) : this(){
        if (context != null && clientsList != null) {
            this.context = context
            this.clientsList = clientsList
        }

        dbHelper = MyDbHelper(context)
    }


    override fun onBindViewHolder(holder: AdapterClientes.HolderClientes, position: Int) {

        val client = clientsList!![position]

        val id = client.id
        val cliente = client.cliente
        val idDireccion = client.idDireccion
        val date = client.fechaAgregada

        val telefono = client.telefono
        val dateNac = client.fechaNac
        val email = client.email
        val notas = client.notas
        val fechaActualizada = client.fechaActualizada

        //Obtener vistas,
        holder.tvId.text = Formato.No(id.toLong())  ///000001
        holder.tvCliente.text = cliente
        holder.tvDate.text = date


        holder.chipAll.visibility = View.GONE
        //fun Chip direciones
        val ChipDir = object {

            fun ChipGroupDirAddd(chipGroupDir : ChipGroup){

                //obtener lista de Direcciones
                //obtener direccion
                //No repetir
                val listDir = mutableListOf<String>()
                for (i in dbHelper.getDirPorId(id).indices){

                    if (listDir.any{ it == dbHelper.getDirPorId(id)[i]})
                    else   listDir.add(dbHelper.getDirPorId(id)[i])
                }


                //variables para aumentar id chip
                val idCount : MutableList<Int?> = mutableListOf()
                var count = 0
                val ChipFun = object {

                    @SuppressLint("ResourceType")
                    fun AddChip(text: String?){

                        val chip = Chip(context)
                        //chip.id = View.generateViewId()
                        chip.id = count++ ///toma el valor de 1,2,3....
                        idCount.add(chip.id)
                        chip.text = text
                        chip.isCheckable = false

                        //icon de location
                        chip.setChipIconResource(R.drawable.ic_location)

                        chipGroupDir.addView(chip)

                        chip.setOnClickListener {  }
                    }

                }

                for (i in listDir.indices)
                    ChipFun.AddChip(listDir[i])
            }

        }

        //mostrar func obj
        ChipDir.ChipGroupDirAddd(holder.chipGroupDir)


       /* for (i in dbHelper.getDirPorId(id).indices)
            dirDb += "${dbHelper.getDirPorId(id)[i]}, "*/



        //holder.tvDireccion.text = dirDb

        //obtener el
        holder.tvIdDir.text = id


        //PopupMenu---------------
        val popupMenu = PopupMenu(
            context, holder.BtMore
        )
        //inflar Layout popupMenu
        popupMenu.menuInflater.inflate(R.menu.menu_list_opt_client, popupMenu.menu)
        //al hacer click en el popupMenu
        popupMenu.setOnMenuItemClickListener { menuItem ->

            val idMenu = menuItem.itemId

            when(idMenu){
                R.id.BtEditC ->  {

                    //Editar cliente
                    val intent = Intent(context, AddClientActivity::class.java)

                    intent.putExtra("IsEditMode", true)
                    intent.putExtra("id", id)
                    intent.putExtra("cliente", holder.tvCliente.text.toString())
                    intent.putExtra("telefono", telefono)
                    intent.putExtra("fechaNac", dateNac)

                    intent.putExtra("idDireccion", idDireccion)
                    intent.putExtra("email", email)
                    intent.putExtra("notas", notas)
                    intent.putExtra("fechaActualizada", fechaActualizada)
                    //intent.putExtra("direccion", dbHelper.getAllClientes("ASC")[0])
                    context!!.startActivity(intent)

                }
                R.id.BtEliminarC ->  {

                    // Dialogo para confirmar eliminado de cliente
                    AlertDialog.Builder(context!!)
                        //   .setTitle("${tvNomAppM.text}")
                        .setMessage("Desea eliminar cliente: $cliente ? ")
                        .setIcon(R.drawable.ic_delete_all)
                        // .setTitle(Html.fromHtml("<font color='#5d4037'> ${tvNomAppM.text} </font>")) //Color de Titulo
                        //  mBuilder.setMessage(Html.fromHtml("<font color=''>This is a test</font>")) //Color de Mensaje

                        .setPositiveButton(android.R.string.ok,
                            DialogInterface.OnClickListener { dialog, which ->
                                //botón OK pulsado

                                //Elimanar cliente
                                dbHelper.DeleteCliente(clientsList!![position].id)
                                //Refrescar Lista clientes para cuando elimine 1
                                (context as MainActivity).onResume()

                            })
                        .setNegativeButton(android.R.string.cancel,
                            DialogInterface.OnClickListener { dialog, which ->
                                //botón cancel pulsado
                            })
                        .show()

                }
                //  R.id.BtInfo -> MostrarInfo()
            }
            false
        }
        holder.BtMore.setOnClickListener {

            popupMenu.show() ///Mostrar PopupMenu
        }
        //Fin PopupMenu-----

        holder.itemView.setOnClickListener {

            //Dialog detalle del cliente
            val mDialogView = LayoutInflater.from(context).inflate(R.layout.det_client_dialog, null)
            val mBuilder = AlertDialog.Builder(context!!)
                .setView(mDialogView)  //Mostrar detalle de Dialog
            //   .setTitle(getString(R.string.Ajustes))
            //   .setIcon(R.drawable.ic_settings)
            //    .setIcon(R.drawable.ic_ajustes_black)
            //     .setTitle(Html.fromHtml("<font color='#29AB87'> ${getString(R.string.app_name)} </font>")) //Color de Titulo
            //  mBuilder.setMessage(Html.fromHtml("<font color=''>This is a test</font>")) //Color de Mensaje

            val mAlertDialog = mBuilder.show()

            //obtener datos
            mAlertDialog.findViewById<TextView>(R.id.tvNomClientDet)!!.text = cliente
            mAlertDialog.findViewById<TextView>(R.id.tvFechaNacClientDet)!!.text = dateNac
         //   mAlertDialog.findViewById<TextView>(R.id.tvDirClientDet)!!.text = dirDb
            mAlertDialog.findViewById<TextView>(R.id.tvEmailClientDet)!!.text = email
            mAlertDialog.findViewById<TextView>(R.id.tvTelClientDet)!!.text = telefono
            mAlertDialog.findViewById<TextView>(R.id.tvNotaClientDet)!!.text = notas
            mAlertDialog.findViewById<TextView>(R.id.tvDateDetClient)!!.text = fechaActualizada

            val chipGrupDirDet = mAlertDialog.findViewById<ChipGroup>(R.id.chipGroupDirDet)!!
            val chipAllDirDet = mAlertDialog.findViewById<Chip>(R.id.chipAllDet)!!

            chipAllDirDet.visibility = View.GONE

            //mostrar func obj
            ChipDir.ChipGroupDirAddd(chipGrupDirDet)

            mAlertDialog?.findViewById<Button>(R.id.BtAceptarDetClient)!!.setOnClickListener { mAlertDialog.dismiss() }




        }



    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) : HolderClientes {

        return HolderClientes(
            LayoutInflater.from(context).inflate(R.layout.countent_item_client, parent, false)
        )
    }

    override fun getItemCount(): Int {
        //Retornar tamanio de la lista

        return clientsList!!.size
    }

    inner class HolderClientes(itemView : View) : RecyclerView.ViewHolder(itemView){

        val tvId = itemView.findViewById<TextView>(R.id.tvId_Row_C)
        val tvCliente = itemView.findViewById<TextView>(R.id.tvCliente_Row_C)
        val tvDireccion = itemView.findViewById<TextView>(R.id.tvDir_Row_C)
        val tvDate = itemView.findViewById<TextView>(R.id.tvDate_Row_C)
        val tvIdDir = itemView.findViewById<TextView>(R.id.tvIdDir_Row_C)

        val BtMore = itemView.findViewById<ImageView>(R.id.BtMoreOpt_Row_C)

        val chipGroupDir = itemView.findViewById<ChipGroup>(R.id.chipGroupDir_Row_C)
        val chipAll = itemView.findViewById<Chip>(R.id.chipAll_Row_C)


    }
    private fun Toast(message : String) = android.widget.Toast.makeText(context, message, android.widget.Toast.LENGTH_SHORT).show()

}