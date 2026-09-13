# AeroGO ✈️

Aplicación móvil para una agencia de viajes que permite gestionar un catálogo
de destinos turísticos, con autenticación y CRUD conectado a Firebase.

Proyecto desarrollado para el **Segundo Desafío Práctico** de la materia
Desarrollo de Software para Móviles (DSM) - Universidad Don Bosco.

## Alumno
- Nombre: [TU NOMBRE AQUÍ]

## Tecnologías
- Kotlin
- Android Views (XML) + ViewBinding
- Firebase Authentication
- Firebase Firestore
- Almacenamiento local para las imágenes de los destinos
- Glide para la carga de imágenes

## Estado del proyecto
- [x] Estructura base del proyecto, ícono y `strings.xml`
- [x] Autenticación (Login / Registro) con Firebase Auth
- [x] Catálogo de destinos (RecyclerView + CardView)
- [x] Registro de destino (Create) con Spinner de país y selección de imagen
- [x] Edición de destino (Update)
- [x] Eliminación de destino con confirmación (Delete)
- [x] Validaciones (campos vacíos, precio > 0, descripción mínima, imagen obligatoria)

## Cómo ejecutar el proyecto
1. Clonar este repositorio.
2. Abrir la carpeta con Android Studio (`File > Open`).
3. Esperar a que sincronice Gradle.
4. Ejecutar en un emulador o dispositivo físico.

> El archivo `google-services.json` ya está incluido en `app/` para que el
> proyecto compile directamente.

## APK
El APK de la última versión funcional está disponible en [`apk/AeroGO.apk`](apk/AeroGO.apk).

## Video de defensa
[https://drive.google.com/file/d/1pwX4sHs2tS95RKqonAkoqitUvd1ZR-p6/view?usp=sharing]
