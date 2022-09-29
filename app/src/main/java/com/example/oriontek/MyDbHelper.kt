package com.example.oriontek

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
/**
OrionTek.
 *created by ASDeveloper on 27/09/2022 at 07:42.
 */

//clase helper que contiene las Funciones CRUD

class MyDbHelper (context: Context?) : SQLiteOpenHelper(
    context,
    Constantes.DB_NAME,
    null,
    Constantes.DB_VERSION
){
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(Constantes.CREATE_TABLE_CLIENTES)
        db.execSQL(Constantes.CREATE_TABLE_DIRECCIONES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        //Actualizar la base de datos si hay algun tipo de cambio en la estructura, cambiar db
        //Eliminar la tabla anterior si existe

        db.execSQL("DROP TABLE IF EXISTS " + Constantes.TABLE_NAME_CLIENTES)
        db.execSQL("DROP TABLE IF EXISTS " + Constantes.TABLE_NAME_DIRECCIONES)
        onCreate(db)
    }



    //----------------CLIENTES----------------------
    //insertar en la base de datos
    fun InsertClientes(
        cliente: String?,
        telefono: String?,
        fechaNac: String?,
        idDireccion: String?,
        email: String?,
        notas: String?,
        fechaActualizada: String?,
        fechaAgregada: String?
    ):Long{
        //Base de datos que se pueda escribir
        val db = this.writableDatabase
        val values = ContentValues()

        //id es insertado automaticamete (AUTOINCREMENT)
        values.put(Constantes.CLIENTE_C, cliente)
        values.put(Constantes.TELEFONO_C, telefono)
        values.put(Constantes.FECHA_NAC_C, fechaNac)
        values.put(Constantes.ID_DIRECCION_C, idDireccion)
        values.put(Constantes.EMAIL_C, email)
        values.put(Constantes.NOTAS_C, notas)
        values.put(Constantes.FECHA_ACTUALIZADA_C, fechaActualizada)
        values.put(Constantes.FECHA_AGREGADA_C, fechaAgregada)

        //Insertar columna y retornarla
        val id = db.insert(Constantes.TABLE_NAME_CLIENTES, null, values)

        db.close()

        return id
    }

    //get obtener todos los datos, del cliente
    @SuppressLint("Range")
    fun getAllClientes(orderBy:String): ArrayList<Clientes>{
        val clientsList = ArrayList<Clientes>()

        val selectQuery = "SELECT * FROM ${Constantes.TABLE_NAME_CLIENTES} ORDER BY $orderBy"
        val db = this.writableDatabase

        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()){
            do {
                val clients = Clientes(
                    ""+cursor.getString(cursor.getColumnIndex(Constantes.ID_C)),
                    ""+cursor.getString(cursor.getColumnIndex(Constantes.CLIENTE_C)),
                    ""+cursor.getString(cursor.getColumnIndex(Constantes.TELEFONO_C)),
                    ""+cursor.getString(cursor.getColumnIndex(Constantes.FECHA_NAC_C)),
                    ""+cursor.getString(cursor.getColumnIndex(Constantes.ID_DIRECCION_C)),
                    ""+cursor.getString(cursor.getColumnIndex(Constantes.EMAIL_C)),
                    ""+cursor.getString(cursor.getColumnIndex(Constantes.NOTAS_C)),
                    ""+cursor.getString(cursor.getColumnIndex(Constantes.FECHA_ACTUALIZADA_C)),
                    ""+cursor.getString(cursor.getColumnIndex(Constantes.FECHA_AGREGADA_C))
                )
                //Agregar clientsList a lista
                clientsList.add(clients)

            }while (cursor.moveToNext())

            db.close()
        }
        return clientsList
    }


    //get obtener todos los datos, del cliente por dir
    @SuppressLint("Range")
    fun getAllClientesPorDir(idDireccion: String?, orderBy:String): ArrayList<Clientes>{
        val clientsList = ArrayList<Clientes>()

        val selectQuery = "SELECT * FROM ${Constantes.TABLE_NAME_CLIENTES} WHERE ${Constantes.ID_DIRECCION_C} = $idDireccion ORDER BY $orderBy"
        val db = this.writableDatabase

        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()){
            do {
                val clients = Clientes(
                    ""+cursor.getString(cursor.getColumnIndex(Constantes.ID_C)),
                    ""+cursor.getString(cursor.getColumnIndex(Constantes.CLIENTE_C)),
                    ""+cursor.getString(cursor.getColumnIndex(Constantes.TELEFONO_C)),
                    ""+cursor.getString(cursor.getColumnIndex(Constantes.FECHA_NAC_C)),
                    ""+cursor.getString(cursor.getColumnIndex(Constantes.ID_DIRECCION_C)),
                    ""+cursor.getString(cursor.getColumnIndex(Constantes.EMAIL_C)),
                    ""+cursor.getString(cursor.getColumnIndex(Constantes.NOTAS_C)),
                    ""+cursor.getString(cursor.getColumnIndex(Constantes.FECHA_ACTUALIZADA_C)),
                    ""+cursor.getString(cursor.getColumnIndex(Constantes.FECHA_AGREGADA_C))
                )
                //Agregar clientsList a lista
                clientsList.add(clients)

            }while (cursor.moveToNext())

            db.close()
        }
        return clientsList
    }


    //Buscar Clientes
    @SuppressLint("Range")
    fun SearchClientes(query:String): ArrayList<Clientes>{
        val clientsList = ArrayList<Clientes>()

        val selectQuery = "SELECT * FROM ${Constantes.TABLE_NAME_CLIENTES} WHERE ${Constantes.CLIENTE_C} LIKE '%$query%'"
        val db = this.writableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()){
            do {
                val clients = Clientes(
                    ""+cursor.getString(cursor.getColumnIndex(Constantes.ID_C)),
                    ""+cursor.getString(cursor.getColumnIndex(Constantes.CLIENTE_C)),
                    ""+cursor.getString(cursor.getColumnIndex(Constantes.TELEFONO_C)),
                    ""+cursor.getString(cursor.getColumnIndex(Constantes.FECHA_NAC_C)),
                    ""+cursor.getString(cursor.getColumnIndex(Constantes.ID_DIRECCION_C)),
                    ""+cursor.getString(cursor.getColumnIndex(Constantes.EMAIL_C)),
                    ""+cursor.getString(cursor.getColumnIndex(Constantes.NOTAS_C)),
                    ""+cursor.getString(cursor.getColumnIndex(Constantes.FECHA_ACTUALIZADA_C)),
                    ""+cursor.getString(cursor.getColumnIndex(Constantes.FECHA_AGREGADA_C))
                )
                //Agregar clientsList a lista
                clientsList.add(clients)

            }while (cursor.moveToNext())

            db.close()
        }
        return clientsList
    }

    //Eliminar cliente usando el id
    fun DeleteCliente(id: String){
        val db = writableDatabase
        db.delete(Constantes.TABLE_NAME_CLIENTES, "${Constantes.ID_C} = ?", arrayOf(id))

        db.close()
    }

    //Eliminar todos los Clientes
    fun DeleteAllClientes(){
        val db = writableDatabase

        db.execSQL("DELETE FROM ${Constantes.TABLE_NAME_CLIENTES}")
        db.close()
    }

    fun UpdateClientes(
        id: String,
        cliente: String?,
        telefono: String?,
        fechaNac: String?,
       // idDireccion: String?,
        email: String?,
        notas: String?,
        fechaActualizada: String?,
        //fechaAgregada: String?
    ):Long{
        val db = this.writableDatabase
        val values = ContentValues()

        //insertar datos
        values.put(Constantes.ID_C, id)
        values.put(Constantes.CLIENTE_C, cliente)
        values.put(Constantes.TELEFONO_C, telefono)
        values.put(Constantes.FECHA_NAC_C, fechaNac)
       // values.put(Constantes.ID_DIRECCION_C, idDireccion)
        values.put(Constantes.EMAIL_C, email)
        values.put(Constantes.NOTAS_C, notas)
        values.put(Constantes.FECHA_ACTUALIZADA_C, fechaActualizada)
        //  values.put(Constantes.FECHA_AGREGADA_C, fechaAgregada)

        //Actualizar
        return db.update(Constantes.TABLE_NAME_CLIENTES, values, "${Constantes.ID_C} = ?", arrayOf(id)).toLong()
    }

    //obtener total de Clientes
    fun TotalClientes() : Int{
        val countQuery = "SELECT * FROM ${Constantes.TABLE_NAME_CLIENTES}"
        val db = this.writableDatabase

        val cursor = db.rawQuery(countQuery, null)
        val count = cursor.count
        cursor.close()

        return count
    }







    ////////////////////////////DIRECCIONES////////////////////////////
    //insertar en la base de datos
    fun InsertDirecciones(
        cliente: String?,
        direccion: String?,
        idCliente: String?,
        fechaActualizada: String?,
        fechaAgregada: String?
    ):Long{
        //Base de datos que se pueda escribir
        val db = this.writableDatabase
        val values = ContentValues()

        //id es insertado automaticamete (AUTOINCREMENT)
        values.put(Constantes.CLIENTE_D, cliente)
        values.put(Constantes.DIRECCION_D, direccion)
        values.put(Constantes.ID_CLIENTE_D, idCliente)
        values.put(Constantes.FECHA_AGREGARDA_D, fechaActualizada)
        values.put(Constantes.FECHA_ACTUALIZADA_D, fechaAgregada)

        //Insertar columna y retornarla
        val id = db.insert(Constantes.TABLE_NAME_DIRECCIONES, null, values)

        db.close()

        return id
    }


    fun getAllDirecciones1w(orderBy:String): ArrayList<Direcciones>{
        val dirList = ArrayList<Direcciones>()

        val selectQuery = "SELECT * FROM ${Constantes.TABLE_NAME_DIRECCIONES} ORDER BY $orderBy"
        val db = this.writableDatabase

        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()){
            do {
                val dir = Direcciones(
                    ""+cursor.getString(cursor.getColumnIndex(Constantes.ID_D)),
                    ""+cursor.getString(cursor.getColumnIndex(Constantes.CLIENTE_D)),
                    ""+cursor.getString(cursor.getColumnIndex(Constantes.DIRECCION_D)),
                    ""+cursor.getString(cursor.getColumnIndex(Constantes.ID_CLIENTE_D)),
                    ""+cursor.getString(cursor.getColumnIndex(Constantes.FECHA_AGREGARDA_D)),
                    ""+cursor.getString(cursor.getColumnIndex(Constantes.FECHA_ACTUALIZADA_D))
                )
                //Agregar clientsList a lista
                dirList.add(dir)

            }while (cursor.moveToNext())

            db.close()
        }
        return dirList
    }


    //Obtener todas las direcciones para mostrarlas en MainAct
    fun getAllDirecciones(): MutableList<String>{
        val dirList = ArrayList<String>()
        dirList.clear()
        val selectQuery = "SELECT * FROM ${Constantes.TABLE_NAME_DIRECCIONES}"
        val db = this.writableDatabase

        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToNext()){
            do {
                //Agregar clientsList a lista

                //si esta contenido, no Add
                if (dirList.any { it == cursor.getString(cursor.getColumnIndex(Constantes.DIRECCION_D)) } ){

                }else dirList.add(""+cursor.getString(cursor.getColumnIndex(Constantes.DIRECCION_D)))


            }while (cursor.moveToNext())

            db.close()
        }
        return dirList
    }



    fun getDirPorId(id: String?): ArrayList<String>{
        val dirList = ArrayList<String>()
        dirList.clear()
        val selectQuery = "SELECT * FROM ${Constantes.TABLE_NAME_DIRECCIONES} WHERE ${Constantes.ID_CLIENTE_D} = $id"
        val db = this.writableDatabase

        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToNext()){
            do {
                //Agregar clientsList a lista
                dirList.add(""+cursor.getString(cursor.getColumnIndex(Constantes.DIRECCION_D)))

            }while (cursor.moveToNext())

            db.close()
        }
        return dirList
    }


    fun getIdDireccion(direccion: String?, cliente: String?): String {
        var id = ""
        //val selectQuery = "SELECT * FROM ${Constantes.TABLE_NAME_DIRECCIONES} WHERE ${Constantes.ID_CLIENTE_D} = $id"
        val selectQuery = "SELECT * FROM ${Constantes.TABLE_NAME_DIRECCIONES} WHERE ${Constantes.DIRECCION_D} = '$direccion' AND ${Constantes.CLIENTE_D} = '$cliente'"
        val db = this.writableDatabase

        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToNext()){
            do {
                //Agregar clientsList a lista
                id = ""+cursor.getString(cursor.getColumnIndex(Constantes.ID_D))

            }while (cursor.moveToNext())

            db.close()
        }
        return id
    }


    //Eliminar Direccion usando por id de direccion
    fun DeleteDireccionPorId(id: String){
        val db = writableDatabase
        db.delete(Constantes.TABLE_NAME_DIRECCIONES, "${Constantes.ID_D} = ?", arrayOf(id))

        db.close()
    }


    //Eliminar todos los Clientes
    fun DeleteAllDirecciones(){
        val db = writableDatabase

        db.execSQL("DELETE FROM ${Constantes.TABLE_NAME_DIRECCIONES}")
        db.close()
    }

    fun getIdCliente(direccion: String?) : String {

        var idDireccion = ""
        val db = this.writableDatabase

        val fila = db.rawQuery("SELECT ${Constantes.ID_CLIENTE_D} FROM ${Constantes.TABLE_NAME_DIRECCIONES} WHERE ${Constantes.DIRECCION_D} = '$direccion'", null)
        if (fila.moveToFirst()) {
            idDireccion = fila.getString(0)    //
        }
        db.close()

        return idDireccion
    }

    fun getAllClientesPorIdDir(idDireccion : String?, orderBy:String): MutableList<Clientes>{
        val clientsList: MutableList<Clientes> = mutableListOf()
        clientsList.clear()
        val db = this.writableDatabase

        //val cursor = db.rawQuery("SELECT ${Constantes.ID_C} FROM ${Constantes.TABLE_NAME_CLIENTES} WHERE ${Constantes.ID_DIRECCION_C} = $idDireccion", null)
        val cursor = db.rawQuery("SELECT ${Constantes.ID_C}, ${Constantes.CLIENTE_C}, ${Constantes.TELEFONO_C}, ${Constantes.FECHA_NAC_C}, ${Constantes.ID_DIRECCION_C}, ${Constantes.EMAIL_C}, ${Constantes.NOTAS_C}, ${Constantes.FECHA_ACTUALIZADA_C}, ${Constantes.FECHA_AGREGADA_C} FROM ${Constantes.TABLE_NAME_CLIENTES} WHERE ${Constantes.ID_DIRECCION_C} = $idDireccion", null)
        if (cursor.moveToFirst()){
            do {
                val clients = Clientes(
                    ""+cursor.getString(0),
                    ""+cursor.getString(1),
                    ""+cursor.getString(2),
                    ""+cursor.getString(3),
                    ""+cursor.getString(4),
                    ""+cursor.getString(5),
                    ""+cursor.getString(6),
                    ""+cursor.getString(7),
                    ""+cursor.getString(8)
                )

                //Agregar clientsList a lista
                clientsList.add(clients)
            }while (cursor.moveToNext())

            db.close()
        }

        return clientsList
    }





//////
//get obtener todos los datos, del cliente por dir
@SuppressLint("Range")
fun getAllClientesPorCliente(cliente: ArrayList<String>, orderBy:String): ArrayList<Clientes>{
    val clientsList = ArrayList<Clientes>()

    for (i in cliente.indices){

        val selectQuery = "SELECT * FROM ${Constantes.TABLE_NAME_CLIENTES} WHERE ${Constantes.CLIENTE_C} = '${cliente[i]}' ORDER BY $orderBy"
        val db = this.writableDatabase

        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()){
            do {
                val clients = Clientes(
                    ""+cursor.getString(cursor.getColumnIndex(Constantes.ID_C)),
                    ""+cursor.getString(cursor.getColumnIndex(Constantes.CLIENTE_C)),
                    ""+cursor.getString(cursor.getColumnIndex(Constantes.TELEFONO_C)),
                    ""+cursor.getString(cursor.getColumnIndex(Constantes.FECHA_NAC_C)),
                    ""+cursor.getString(cursor.getColumnIndex(Constantes.ID_DIRECCION_C)),
                    ""+cursor.getString(cursor.getColumnIndex(Constantes.EMAIL_C)),
                    ""+cursor.getString(cursor.getColumnIndex(Constantes.NOTAS_C)),
                    ""+cursor.getString(cursor.getColumnIndex(Constantes.FECHA_ACTUALIZADA_C)),
                    ""+cursor.getString(cursor.getColumnIndex(Constantes.FECHA_AGREGADA_C))
                )
                //Agregar clientsList a lista
                clientsList.add(clients)

            }while (cursor.moveToNext())

            db.close()
        }
    }


    return clientsList
}


    //---

    //btener clientes dond = dir
    fun getClientPorDir(direccion: String?): ArrayList<String>{
        var dirList = arrayListOf<String>()
        val selectQuery = "SELECT ${Constantes.CLIENTE_D} FROM ${Constantes.TABLE_NAME_DIRECCIONES} WHERE ${Constantes.DIRECCION_D} = '$direccion'"      //'' para string,  pelado para num
        val db = this.writableDatabase

        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToNext()){
            do {

                //si esta contenido, no Add
                if (dirList.any { it == cursor.getString(0) } ){
                }else dirList.add(cursor.getString(0))  //Agregar clientsList a lista

            }while (cursor.moveToNext())

            db.close()
                //
        }
        db.close()
        return dirList
    }

}