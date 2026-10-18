package com.vallesol.detectorplagas.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.vallesol.detectorplagas.Reporte
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Reporte::class, ControladorBiologico::class, TipAgricola::class], version = 4, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun reporteDao(): ReporteDao
    abstract fun controladorBiologicoDao(): ControladorBiologicoDao
    abstract fun tipAgricolaDao(): TipAgricolaDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instancia = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "detector_plagas.db"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // Usamos directamente el context recibido en getInstance para hacer el insert
                            CoroutineScope(Dispatchers.IO).launch {
                                INSTANCE?.let { database ->
                                    database.controladorBiologicoDao().insertarTodos(controladoresIniciales)
                                    database.tipAgricolaDao().insertarTodos(tipsIniciales)
                                } ?: run {
                                    // Si INSTANCE aún es null en este hilo, abrimos una instancia temporal segura
                                    val dbTemp = Room.databaseBuilder(
                                        context.applicationContext,
                                        AppDatabase::class.java,
                                        "detector_plagas.db"
                                    ).build()
                                    dbTemp.controladorBiologicoDao().insertarTodos(controladoresIniciales)
                                    dbTemp.tipAgricolaDao().insertarTodos(tipsIniciales)
                                    dbTemp.close()
                                }
                            }
                        }
                    })
                    .build()
                INSTANCE = instancia
                instancia
            }
        }

        // Datos oficiales extraídos de las fichas técnicas de ValleSol S.A.C.
        private val controladoresIniciales = listOf(
            ControladorBiologico(
                nombreComun = "Mosca Nativa",
                nombreCientifico = "Billaea claripalpis",
                tipoControl = "Insecto parasitoide de larvas del barreno",
                descripcion = "Insecto parasitoide de larvas del 'barreno de la caña de azúcar', visible a simple vista, mide aprox. 15 mm. De vuelos rápidos, se encuentra en tallos y hojas de gramíneas.",
                modoAccion = "Las hembras buscan perforaciones en los tallos hechas por el barreno y colocan sus larvitas activas, las cuales penetran las galerías, se introducen en la larva plaga, se alimentan de ella y la matan evitando que complete su ciclo.",
                presentacion = "Moscas en estado adulto contenidas en envases plásticos de 1 L. Número total: 20 parejas por envase.",
                dosis = "Se liberan entre 20 a 40 parejas de moscas por hectárea, dependiendo del grado de ataque de la plaga.",
                metodoLiberacion = "Método inundativo: consiste en distribuir las moscas en el centro de la hectárea de cultivo, de preferencia por las mañanas previa evaluación.",
                precauciones = "Trasladar en un espacio fresco y ventilado. Liberar inmediatamente.",
                imagenRes = "billaea_claripalpis"
            ),
            ControladorBiologico(
                nombreComun = "Microavispa",
                nombreCientifico = "Trichogramma exiguum",
                tipoControl = "Parasitoide de huevos de lepidópteros (mariposas)",
                descripcion = "Microavispa parasitoide de huevos o posturas de diversas plagas, especialmente mariposas. Mide aproximadamente 0.5 mm y controla a más de 250 insectos plaga.",
                modoAccion = "Buscan los huevos colocados por la plaga e introducen sus propios huevos dentro de ellos. La larvita emergente se alimenta del contenido del huevo, impidiendo que nazca el gusano dañino.",
                presentacion = "Disponible en estado de huevos parasitados pegados en cartulina (para recortar en cuadrados de 1 a 4 pulgadas cuadradas) o en estado adulto en vasitos descartables.",
                dosis = "Cultivos anuales (algodón, maíz, caña): 20 a 50 pulgadas cuadradas por hectárea (~150,000 avispas). Cultivos perennes (cítricos, paltos): 100 a 200 pulg².",
                metodoLiberacion = "Liberación en cartulinas colocadas en vasitos en sitios fijos, o liberación directa de adultos distribuidos de manera uniforme.",
                precauciones = "Mantener el producto en un lugar fresco y protegido de las hormigas. Asegurar un transporte adecuado para evitar la pérdida de huevos.",
                imagenRes = "trichogramma_exiguum"
            )
        )

        private val tipsIniciales = listOf(
            TipAgricola(
                titulo = "Inspección Foliar",
                descripcion = "Revisa frecuentemente el envés de las hojas: allí suelen ocultarse las primeras fases de las larvas y posturas.",
                categoria = "Monitoreo"
            ),
            TipAgricola(
                titulo = "Condiciones de Captura",
                descripcion = "Toma las fotografías con buena iluminación natural para asegurar que el modelo de detección identifique con precisión los daños.",
                categoria = "Fotografía"
            ),
            TipAgricola(
                titulo = "Geolocalización",
                descripcion = "Registra y valida siempre la ubicación del lote al reportar una incidencia para llevar un historial geográfico exacto.",
                categoria = "Registro"
            ),
            TipAgricola(
                titulo = "Liberación Oportuna",
                descripcion = "Realiza la liberación de controladores como Trichogramma exiguum a primera hora de la mañana y apenas detectes indicios del barrenador.",
                categoria = "Control Biológico"
            )
        )
    }
}