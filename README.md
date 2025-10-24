# AnimeVerse 🎌 📸

Aplicación móvil para una comunidad de fans de anime, manga y gaming **con captura de fotos**.

## ✅ Proyecto Android Completo

Combina:
- 🗄️ **Base de datos Room** de AnimeVerse
- 📸 **Funcionalidad de cámara** del proyecto UINavegacion

## 📱 Características Principales

### 📸 Captura de Fotos
- ✅ **Foto de perfil** en registro (opcional)
- ✅ **HomeScreen** con captura de fotos
- ✅ Abrir cámara del dispositivo
- ✅ Vista previa circular en registro
- ✅ Tomar foto y visualizarla
- ✅ Cambiar o eliminar foto
- ✅ Almacenamiento temporal en cache

### 🗄️ Base de Datos Room
- ✅ 10 entidades (Usuario, Post, Comentario, etc.)
- ✅ Sistema completo y simple de usuarios **con foto de perfil**
- ✅ Publicaciones con categorías
- ✅ Datos de ejemplo precargados
- ✅ Campo `avatar` en UserEntity para guardar foto

### 🎨 Interfaz Material 3
- ✅ Diseño moderno con Jetpack Compose
- ✅ Tema personalizado (modo claro/oscuro)
- ✅ Cards de publicaciones
- ✅ Paleta de colores anime

## 📁 Estructura del Proyecto

```
APP-moviles-Anime/
├── app/
│   ├── src/main/
│   │   ├── java/com/example/animeverse/
│   │   │   ├── data/              # Base de datos Room
│   │   │   │   ├── local/
│   │   │   │   ├── repository/
│   │   │   │   └── mock/
│   │   │   ├── ui/                # Interfaz Compose
│   │   │   │   ├── screen/
│   │   │   │   │   ├── LoginScreen.kt     ← Pantalla de login (INICIO)
│   │   │   │   │   ├── RegisterScreen.kt  ← Pantalla de registro
│   │   │   │   │   └── HomeScreen.kt      ← Pantalla con cámara
│   │   │   │   └── theme/
│   │   │   └── MainActivity.kt    ← Navegación entre pantallas
│   │   ├── res/
│   │   │   ├── xml/
│   │   │   │   ├── file_paths.xml        ← Config FileProvider
│   │   │   │   ├── backup_rules.xml
│   │   │   │   └── data_extraction_rules.xml
│   │   │   └── values/
│   │   └── AndroidManifest.xml    ← Permisos cámara
│   └── build.gradle.kts           ← Dependencias
├── build.gradle.kts
├── settings.gradle.kts
└── .gitignore
```

## 🚀 Abrir el Proyecto

### En Android Studio:
1. **File → Open**
2. Selecciona: `APP-moviles-Anime`
3. Espera Gradle Sync
4. **Run ▶️**

## 📦 Dependencias Incluidas

```kotlin
// Compose & Material 3
implementation("androidx.compose.material3:material3")
implementation("androidx.compose.material:material-icons-extended")

// Room Database
implementation("androidx.room:room-runtime:2.6.1")
implementation("androidx.room:room-ktx:2.6.1")
ksp("androidx.room:room-compiler:2.6.1")

// Coil para imágenes
implementation("io.coil-kt:coil-compose:2.5.0")  ← Para la cámara

// Coroutines & ViewModel
implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
```

## 📱 Pantallas de la App

### 🔐 LoginScreen (Pantalla de Inicio)
- **Primera pantalla** al abrir la app
- Logo de AnimeVerse (🎌)
- Campo de **Email** con validación
- Campo de **Contraseña** con opción mostrar/ocultar
- Validaciones en tiempo real
- Botón **Entrar** con loading state
- Botón **Crear cuenta** → Navega a RegisterScreen

### 📝 RegisterScreen
- Logo de AnimeVerse (🎌)
- **📸 Foto de perfil (opcional)** 
  - Vista previa circular
  - Captura con cámara
  - Botón "Tomar foto" / "Cambiar"
  - Botón "Eliminar" si ya hay foto
- Campo **Nombre completo** (mínimo 3 caracteres)
- Campo **Email** con validación
- Campo **Contraseña** (mínimo 6 caracteres) con opción mostrar/ocultar
- Campo **Confirmar contraseña** con validación de coincidencia
- Validaciones en tiempo real
- Botón **Registrar** con loading state
- Botón **Ya tengo cuenta** → Navega a LoginScreen

### 🏠 HomeScreen - Funcionalidades

#### 🎌 Sección Principal
- Header con título "AnimeVerse"
- Card informativa de bienvenida

#### 📸 Sección de Cámara
- **Abrir Cámara** - Captura foto con la cámara del dispositivo
- **Vista previa** - Muestra la foto capturada
- **Volver a tomar** - Reemplaza la foto actual
- **Eliminar** - Borra la foto con diálogo de confirmación
- **Almacenamiento** - Guarda en cache temporal

#### 📰 Publicaciones
- Muestra las 3 publicaciones más recientes
- Cards con:
  - Autor y categoría
  - Título y contenido
  - Likes ❤️ y comentarios 💬

## 🔧 Configuración Técnica

### Permisos (AndroidManifest.xml)
**✅ NO se necesitan permisos explícitos de cámara**

Cuando usas `ActivityResultContracts.TakePicture()`, el sistema Android maneja los permisos automáticamente. NO es necesario declarar:
```xml
<!-- ❌ NO NECESARIO -->
<uses-permission android:name="android.permission.CAMERA" />
```

### FileProvider
```xml
<provider
    android:name="androidx.core.content.FileProvider"
    android:authorities="${applicationId}.fileprovider"
    android:grantUriPermissions="true">
    <meta-data
        android:name="android.support.FILE_PROVIDER_PATHS"
        android:resource="@xml/file_paths" />
</provider>
```

### Rutas de Archivos (file_paths.xml)
```xml
<cache-path name="camera_cache" path="images/" />
```

**Nota:** Las fotos se guardan temporalmente en `cache/images/` con nombres como:
- `IMG_yyyyMMdd_HHmmss.jpg` (HomeScreen)
- `PROFILE_yyyyMMdd_HHmmss.jpg` (RegisterScreen)

## 🎯 Estado del Proyecto

```
✅ Estructura Android completa
✅ Base de datos Room (10 entidades)
✅ HomeScreen con Material 3
✅ Funcionalidad de cámara completa
✅ FileProvider configurado
✅ Permisos de cámara
✅ Coil para cargar imágenes
✅ MainActivity funcional
✅ LoginScreen - Pantalla de inicio de sesión
✅ RegisterScreen - Pantalla de registro CON foto de perfil 📸
✅ Navegación básica entre pantallas
✅ Captura de foto de perfil en registro
⬜ ViewModels para autenticación
⬜ Conectar Login/Register con base de datos
⬜ Guardar foto de perfil en la base de datos
```

## 📱 Ejecutar la App

### En Android Studio:
1. Conecta dispositivo o abre emulador
2. Click **Run ▶️** (Shift + F10)

### Gradle:
```bash
./gradlew clean
./gradlew assembleDebug
./gradlew installDebug
```

## 🎨 Componentes Principales

### RegisterScreen.kt
- **Profile Photo Card** - Captura de foto de perfil
  - Vista previa circular (120dp)
  - Icono por defecto si no hay foto
  - Botón "Tomar foto" con icono de cámara
  - Botón "Eliminar" si ya hay foto
  - Guarda URI en `photoUriString` (persistente)

### HomeScreen.kt
- **CameraCard** - Componente de captura de fotos
- **createTempImageFile()** - Crea archivo temporal
- **getImageUriForFile()** - Obtiene URI con FileProvider

### Funcionalidad de Cámara
```kotlin
// Launcher para la cámara
val takePictureLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.TakePicture()
) { success ->
    if (success) {
        // Foto tomada exitosamente
    }
}
```

## 🗄️ Base de Datos

**10 Entidades:**
1. RolEntity - Roles de usuario
2. EstadoEntity - Estados
3. TemaEntity - Categorías
4. UsuarioEntity - Usuarios completos
5. PublicacionEntity - Publicaciones
6. ComentarioEntity - Comentarios
7. CalificacionEntity - Calificaciones
8. HoraBaneoEntity - Historial baneos
9. UserEntity - Usuarios simples
10. PostEntity - Posts simples

Ver [DATABASE_README.md](DATABASE_README.md) para más detalles.

## 🐛 Troubleshooting

### Error de Gradle Sync
```bash
./gradlew clean
./gradlew build
```

### ❌ La app se cierra al abrir la cámara
**Solución:** NO declares permisos de cámara en el `AndroidManifest.xml`

Cuando usas `ActivityResultContracts.TakePicture()`, el sistema maneja los permisos automáticamente. Si declaras `<uses-permission android:name="android.permission.CAMERA" />`, puede causar que la app se cierre.

**Correcto (sin permisos explícitos):**
```xml
<manifest>
    <application>
        <!-- Solo FileProvider, SIN permisos de cámara -->
        <provider ... />
    </application>
</manifest>
```

### Error de FileProvider
Verifica que:
1. `file_paths.xml` exista en `res/xml/`
2. El `authorities` en `AndroidManifest.xml` sea `${applicationId}.fileprovider`
3. En el código uses `"${context.packageName}.fileprovider"`

## 🧭 Navegación

La app utiliza navegación simple con `remember` y `mutableStateOf`:

```kotlin
enum class Screen {
    Login,    // Pantalla inicial
    Register, // Pantalla de registro
    Home      // Pantalla principal
}
```

**Flujo de navegación:**
1. **App inicia** → LoginScreen
2. **Login exitoso** → HomeScreen
3. **Crear cuenta (Login)** → RegisterScreen
4. **Registro exitoso** → LoginScreen
5. **Ya tengo cuenta (Register)** → LoginScreen

## 📝 Próximos Pasos

1. ⬜ Agregar ViewModels para autenticación
2. ⬜ Conectar Login/Register con Room Database
3. ⬜ Guardar foto de perfil en BD al registrarse
4. ⬜ Mostrar foto de perfil en HomeScreen
5. ⬜ Implementar sesión persistente
6. ⬜ Pantallas de detalle de posts
7. ⬜ Sistema de permisos mejorado
8. ⬜ Migrar a Jetpack Navigation Component

## 🤝 Git Workflow

```bash
git status
git add .
git commit -m "feat: HomeScreen con cámara + Base de datos Room"
git push origin main
```

## 📚 Documentación

- **[DATABASE_README.md](DATABASE_README.md)** - Base de datos completa
- **[UI_README.md](UI_README.md)** - Componentes UI
- **[ESTRUCTURA.md](ESTRUCTURA.md)** - Arquitectura

---

**🎌 AnimeVerse + 📸 Cámara**  
**Versión:** 1.0.0-alpha  
**SDK Min:** 24 (Android 7.0)  
**SDK Target:** 34 (Android 14)  
**Kotlin:** 1.9.0

**Combina:**
- Base de datos de AnimeVerse
- Funcionalidad de cámara de UINavegacion
