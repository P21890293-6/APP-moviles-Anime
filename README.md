# APP-moviles-Anime 🎌

Aplicación móvil para una comunidad de fans de anime, manga y gaming, inspirada en el patrón de arquitectura de **UINavegacion**.

## 📊 Base de Datos

Esta aplicación utiliza **Room Database** para el almacenamiento local de datos siguiendo el patrón modular de entidades y DAOs.

### ✨ Características de la Base de Datos:

- ✅ **Sistema Completo de Usuarios** con roles (Admin, Usuario, Moderador) y estados
- ✅ **Sistema Simple de Usuarios** para implementaciones básicas
- ✅ **Publicaciones** con categorías (Anime, Manga, Gaming, General)
- ✅ **Comentarios** en publicaciones
- ✅ **Calificaciones** con sistema de estrellas (1-5)
- ✅ **Sistema de Moderación** con baneos temporales y permanentes
- ✅ **Datos de Ejemplo** precargados automáticamente
- ✅ **Arquitectura Modular** con entidades y DAOs organizados por módulo

## 🏗️ Estructura del Proyecto

Siguiendo el patrón de **UINavegacion_camara**:

```
data/
├── local/
│   ├── database/         # AppDatabase - Configuración principal
│   ├── rol/             # RolEntity + RolDao
│   ├── estado/          # EstadoEntity + EstadoDao
│   ├── tema/            # TemaEntity + TemaDao
│   ├── usuario/         # UsuarioEntity + UsuarioDao (sistema completo)
│   ├── publicacion/     # PublicacionEntity + PublicacionDao
│   ├── comentario/      # ComentarioEntity + ComentarioDao
│   ├── calificacion/    # CalificacionEntity + CalificacionDao
│   ├── hora_baneo/      # HoraBaneoEntity + HoraBaneoDao
│   ├── user/            # UserEntity + UserDao (sistema simple)
│   └── post/            # PostEntity + PostDao (sistema simple)
├── repository/          # AnimeVerseRepository
└── mock/               # Datos de ejemplo para testing
```

### 📁 Ventajas de esta Estructura:

✅ **Modularidad** - Cada entidad está en su propio módulo con su DAO  
✅ **Mantenibilidad** - Fácil de encontrar y modificar código relacionado  
✅ **Escalabilidad** - Agregar nuevas entidades es simple y organizado  
✅ **Clean Architecture** - Separación clara de responsabilidades  

## 🗄️ Entidades de la Base de Datos

### Sistema Completo:
1. **RolEntity** - Roles de usuario (Administrador, Usuario, Moderador)
2. **EstadoEntity** - Estados (Activo, Inactivo, Baneado)
3. **TemaEntity** - Categorías (Anime, Manga, Gaming, General)
4. **UsuarioEntity** - Usuarios con sistema completo de roles y estados
5. **PublicacionEntity** - Publicaciones con sistema de baneo
6. **ComentarioEntity** - Comentarios en publicaciones
7. **CalificacionEntity** - Sistema de calificaciones (1-5 estrellas)
8. **HoraBaneoEntity** - Historial de baneos temporales

### Sistema Simple:
9. **UserEntity** - Usuarios básicos
10. **PostEntity** - Publicaciones simples con likes y comentarios

## 🛠️ Tecnologías

- **Kotlin** - Lenguaje de programación
- **Room Database** - Persistencia de datos local
- **Coroutines** - Programación asíncrona
- **Flow** - Streams reactivos para UI
- **Jetpack Compose** (opcional) - UI moderna

## 🚀 Cómo Usar

### 1. Agregar dependencias en `build.gradle.kts`:

```kotlin
dependencies {
    val room_version = "2.6.1"
    
    // Room
    implementation("androidx.room:room-runtime:$room_version")
    implementation("androidx.room:room-ktx:$room_version")
    ksp("androidx.room:room-compiler:$room_version")
    
    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
}
```

### 2. Inicializar la base de datos:

```kotlin
// En tu Application o Activity
val database = AppDatabase.getInstance(context)
val repository = AnimeVerseRepository(
    rolDao = database.rolDao(),
    estadoDao = database.estadoDao(),
    temaDao = database.temaDao(),
    usuarioDao = database.usuarioDao(),
    publicacionDao = database.publicacionDao(),
    comentarioDao = database.comentarioDao(),
    calificacionDao = database.calificacionDao(),
    horaBaneoDao = database.horaBaneoDao(),
    userDao = database.userDao(),
    postDao = database.postDao()
)
```

### 3. Usar el repositorio:

```kotlin
// Login de usuario
viewModelScope.launch {
    val result = repository.loginUser("anime@example.com", "123456")
    result.onSuccess { user ->
        // Usuario autenticado
    }.onFailure { error ->
        // Error de credenciales
    }
}

// Obtener todas las publicaciones
viewModelScope.launch {
    repository.getAllPublicaciones().collect { publicaciones ->
        // Actualizar UI con publicaciones
    }
}
```

## 📋 Datos de Ejemplo Precargados

La base de datos se crea automáticamente con:

- **3 Roles:** Administrador, Usuario, Moderador
- **3 Estados:** Activo, Inactivo, Baneado
- **4 Temas:** Anime, Manga, Gaming, General
- **2 Usuarios** del sistema completo
- **3 Usuarios** del sistema simple
- **2 Publicaciones** de ejemplo
- **Comentarios y calificaciones** de prueba

## 📖 Documentación Adicional

Para más detalles sobre la estructura y funcionalidades de la base de datos, consulta [DATABASE_README.md](DATABASE_README.md).

## 🎯 Próximos Pasos

1. ✅ Base de datos configurada
2. 🔲 Crear ViewModels para cada pantalla
3. 🔲 Implementar pantallas de UI (Login, Register, Home, etc.)
4. 🔲 Agregar navegación entre pantallas
5. 🔲 Implementar validaciones de formularios

---

**Desarrollado por:** Tu equipo  
**Fecha:** Octubre 2024  
**Inspirado en:** Proyecto UINavegacion_camara
