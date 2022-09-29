package com.example.oriontek
/**
OrionTek.
 *created by ASDeveloper on 27/09/2022 at 07:42.
 */

object Constantes {

    //db nombre
    const val DB_NAME = "CLIENTES_DB"

    //Version db
    const val DB_VERSION = 1

    //--------------------------------------------------- --
    //nombre de tabla
    const val TABLE_NAME_CLIENTES = "CLIENTES"

    //columnas de tabla CLIENTES
    const val ID_C = "ID"
    const val CLIENTE_C = "CLIENTE"
    const val TELEFONO_C = "TELEFONO"
    const val FECHA_NAC_C = "FECHA_NAC"
    //const val DIRECCION_C = "DIRECCION"
    const val ID_DIRECCION_C = "ID_DIRECCION"
    const val EMAIL_C = "EMAIL"
    const val NOTAS_C = "NOTAS"
    const val FECHA_ACTUALIZADA_C = "FECHA_ACTUALIZADA"
    const val FECHA_AGREGADA_C = "FECHA_AGREGADA"

    //Crear tabla query
    const val CREATE_TABLE_CLIENTES = (
            " CREATE TABLE " + TABLE_NAME_CLIENTES + "("
                    + ID_C + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + CLIENTE_C + " TEXT, "
                    + TELEFONO_C + " TEXT, "
                    + FECHA_NAC_C + " TEXT, "
                    // + DIRECCION_C + " TEXT, "
                    + ID_DIRECCION_C + " TEXT, "
                    + EMAIL_C + " TEXT, "
                    + NOTAS_C + " TEXT, "
                    + FECHA_ACTUALIZADA_C + " TEXT, "
                    + FECHA_AGREGADA_C + " TEXT "
                    +")"
            )

    //--------------------------------------------------- --
    //nombre de tabla
    const val TABLE_NAME_DIRECCIONES = "DIRECCIONES"

    //columnas de tabla
    const val ID_D = "ID"
    const val CLIENTE_D = "CLIENTE"
    const val DIRECCION_D = "DIRECCION"
    const val ID_CLIENTE_D = "ID_CLIENTE"
    const val FECHA_AGREGARDA_D = "FECHA_AGREGARDA"
    const val FECHA_ACTUALIZADA_D = "FECHA_ACTUALIZADA"

    //Crear tabla query
    const val CREATE_TABLE_DIRECCIONES = (
            " CREATE TABLE " + TABLE_NAME_DIRECCIONES + "("
                    + ID_D + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    //+ ID_D + " INTEGER, "
                    // + CLIENTE_C + " TEXT, "
                    + CLIENTE_D + " TEXT, "
                    + DIRECCION_D + " TEXT, "
                    + ID_CLIENTE_D + " TEXT, "
                    + FECHA_AGREGARDA_D + " TEXT, "
                    + FECHA_ACTUALIZADA_D + " TEXT "
                    +")"
            )

}