# 📊 Base de Datos AnimeVerse

## 🗄️ Estructura de la Base de Datos

### **Entidades Principales:**

#### 1. **UsuarioEntity** (Sistema Completo)
- **Tabla:** `usuario`
- **Campos:** id_usuario, nombre, correo, clave, nickname, foto_perfil, id_rol, id_estado
- **Relaciones:** Foreign Keys con RolEntity y EstadoEntity
- **Funcionalidades:** Sistema de roles, estados, baneos

#### 2. **User** (Sistema Simple)
- **Tabla:** `users`
- **Campos:** id, username, email, password, fullName, avatar, createdAt
- **Funcionalidades:** Sistema básico de usuarios

#### 3. **Post** (Sistema Simple)
- **Tabla:** `posts`
- **Campos:** id, title, content, authorId, authorName, themeId, createdAt, likes, comments
- **Funcionalidades:** Publicaciones con likes y comentarios

#### 4. **Entidades del Sistema Completo:**
- **RolEntity:** Roles de usuario (Admin, Usuario, Moderador)
- **EstadoEntity:** Estados de usuario (Activo, Inactivo, Baneado)
- **TemaEntity:** Categorías de publicaciones (Anime, Manga, Gaming, General)
- **PublicacionEntity:** Publicaciones con sistema de baneo
- **ComentarioEntity:** Comentarios en publicaciones
- **CalificacionEntity:** Sistema de calificaciones (1-5 estrellas)
- **HoraBaneoEntity:** Historial de baneos temporales

## 📋 Datos de Ejemplo

### **Usuarios (User):**
1. **anime_lover** - María García (anime@example.com)
2. **manga_reader** - Carlos López (manga@example.com)
3. **gamer_pro** - Ana Rodríguez (gamer@example.com)

### **Publicaciones (Post):**
1. **"¿Cuál es tu anime favorito de 2024?"** - María García (Anime)
2. **"Recomendaciones de manga para principiantes"** - Carlos López (Manga)
3. **"Mejores juegos de anime para PC"** - Ana Rodríguez (Gaming)
4. **"One Piece: ¿Vale la pena verlo completo?"** - María García (Anime)
5. **"Nuevos lanzamientos de manga en 2024"** - Carlos López (Manga)

### **Datos del Sistema Completo:**
- **Roles:** Administrador, Usuario, Moderador
- **Estados:** Activo, Inactivo, Baneado
- **Temas:** Anime, Manga, Gaming, General
- **Usuarios de ejemplo:** Juan Pérez, María García (con roles y estados)
- **Publicaciones de ejemplo:** Attack on Titan, Recomendaciones de Manga
- **Comentarios y calificaciones** de ejemplo

## 🏗️ Arquitectura

### **DAOs (Data Access Objects):**
- `RolDao`, `EstadoDao`, `UsuarioDao`, `TemaDao`
- `PublicacionDao`, `ComentarioDao`, `CalificacionDao`, `HoraBaneoDao`
- `UserDao`, `PostDao`

### **Repository Pattern:**
- `AnimeVerseRepository` - Repositorio principal
- `AnimeVerseRepositoryFactory` - Factory para inyección de dependencias

### **Características Técnicas:**
- ✅ **Room Database** con versión 3
- ✅ **Foreign Keys** y relaciones CASCADE
- ✅ **Índices optimizados** para consultas rápidas
- ✅ **Esquema exportado** para migraciones
- ✅ **Datos de ejemplo** insertados automáticamente
- ✅ **Sistema de baneo** completo (permanente y temporal)
- ✅ **Soporte para imágenes** (fotos de perfil y publicaciones)

## 🔗 Relaciones

```
RolEntity (1) ←→ (N) UsuarioEntity
EstadoEntity (1) ←→ (N) UsuarioEntity
UsuarioEntity (1) ←→ (N) PublicacionEntity
TemaEntity (1) ←→ (N) PublicacionEntity
PublicacionEntity (1) ←→ (N) ComentarioEntity
UsuarioEntity (1) ←→ (N) ComentarioEntity
PublicacionEntity (1) ←→ (N) CalificacionEntity
UsuarioEntity (1) ←→ (N) CalificacionEntity
UsuarioEntity (1) ←→ (N) HoraBaneoEntity
User (1) ←→ (N) Post
```

## 📁 Archivos de la Base de Datos

- `AppDatabase.kt` - Configuración principal de Room
- `*Entity.kt` - Entidades de datos
- `*Dao.kt` - Interfaces de acceso a datos
- `AnimeVerseRepository.kt` - Repositorio principal
- `AnimeVerseRepositoryFactory.kt` - Factory pattern
- `MockData.kt` - Datos de ejemplo
- `schemas/` - Esquemas exportados de Room

## 🎯 Funcionalidades Implementadas

- ✅ **CRUD completo** para todas las entidades
- ✅ **Consultas optimizadas** con Flow para UI reactiva
- ✅ **Sistema de autenticación** básico
- ✅ **Gestión de contenido** (publicaciones, comentarios)
- ✅ **Sistema de moderación** (baneos, calificaciones)
- ✅ **Categorización** por temas
- ✅ **Historial de actividades** de usuarios

---

**Desarrollado por:** [Tu Nombre]  
**Fecha:** Enero 2025  
**Tecnologías:** Android, Kotlin, Room Database, Jetpack Compose
