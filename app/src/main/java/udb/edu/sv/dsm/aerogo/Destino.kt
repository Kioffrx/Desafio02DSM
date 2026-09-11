package udb.edu.sv.dsm.aerogo

/**
 * Representa un destino turístico almacenado en Firestore.
 *
 * [imagenPath] guarda la ruta local (almacenamiento interno de la app) donde
 * se copió la imagen elegida en la galería, ya que la app usa almacenamiento
 * local para la multimedia en lugar de Firebase Storage.
 *
 * El constructor sin argumentos es requerido por Firestore para poder
 * mapear los documentos con toObject().
 */
data class Destino(
    var id: String = "",
    val nombre: String = "",
    val pais: String = "",
    val precio: Double = 0.0,
    val descripcion: String = "",
    val imagenPath: String = ""
)
