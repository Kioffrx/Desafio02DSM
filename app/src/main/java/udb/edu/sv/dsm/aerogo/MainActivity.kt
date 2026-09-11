package udb.edu.sv.dsm.aerogo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * Pantalla de bienvenida temporal de AeroGO.
 *
 * En los siguientes pasos del desafío, esta actividad se reemplazará
 * por la lógica de verificación de sesión (FirebaseAuth):
 * - Si hay un usuario autenticado -> ir directo al Catálogo (CatalogActivity).
 * - Si no hay sesión -> ir a LoginActivity.
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
