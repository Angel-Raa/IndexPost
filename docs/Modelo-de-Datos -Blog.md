
---

### 🔹 **Entidades principales**  

#### 1️⃣ **User (Usuario)**  

Guarda información de los usuarios, soportando autenticación tradicional y con Google.  

| Campo         | Tipo           | Descripción |
|--------------|---------------|-------------|
| `id`         | `UUID (PK)`    | Identificador único del usuario |
| `name`       | `VARCHAR(100)` | Nombre completo |
| `email`      | `VARCHAR(150)` | Correo electrónico (único) |
| `password`   | `VARCHAR(255)` | Contraseña (nula si se usa Google) |
| `google_id`  | `VARCHAR(255)` | ID de Google (nulo si usa autenticación tradicional) |
| `role`       | `ENUM('ADMIN', 'AUTHOR', 'READER')` | Rol del usuario |
| `created_at` | `TIMESTAMP`    | Fecha de creación |
| `updated_at` | `TIMESTAMP`    | Última actualización |

#### 2️⃣ **Post (Publicación del blog)**  

Representa un artículo del blog, optimizado para SEO.  

| Campo          | Tipo           | Descripción |
|---------------|---------------|-------------|
| `id`          | `UUID (PK)`    | Identificador único |
| `title`       | `VARCHAR(255)` | Título del post (optimizado para SEO) |
| `slug`        | `VARCHAR(255)` | URL amigable para SEO |
| `content`     | `TEXT`         | Contenido del post |
| `thumbnail`   | `VARCHAR(255)` | URL de la imagen destacada |
| `meta_title`  | `VARCHAR(255)` | Título meta para SEO |
| `meta_desc`   | `VARCHAR(255)` | Descripción meta para SEO |
| `author_id`   | `UUID (FK)`    | Relación con `User` |
| `category_id` | `UUID (FK)`    | Relación con `Category` |
| `published_at`| `TIMESTAMP`    | Fecha de publicación |
| `status`      | `ENUM('DRAFT', 'PUBLISHED')` | Estado del post |
| `created_at`  | `TIMESTAMP`    | Fecha de creación |
| `updated_at`  | `TIMESTAMP`    | Última actualización |

#### 3️⃣ **Category (Categoría)**  

Para clasificar los posts en categorías específicas.  

| Campo         | Tipo           | Descripción |
|--------------|---------------|-------------|
| `id`         | `UUID (PK)`    | Identificador único |
| `name`       | `VARCHAR(100)` | Nombre de la categoría |
| `slug`       | `VARCHAR(255)` | URL amigable para SEO |
| `created_at` | `TIMESTAMP`    | Fecha de creación |
| `updated_at` | `TIMESTAMP`    | Última actualización |

#### 4️⃣ **Tag (Etiqueta)**  

Permite una organización flexible de los posts con etiquetas.  

| Campo         | Tipo           | Descripción |
|--------------|---------------|-------------|
| `id`         | `UUID (PK)`    | Identificador único |
| `name`       | `VARCHAR(100)` | Nombre de la etiqueta |
| `slug`       | `VARCHAR(255)` | URL amigable para SEO |

#### 5️⃣ **Post_Tag (Relación Post-Etiqueta)**  

Relación muchos a muchos entre `Post` y `Tag`.  

| Campo       | Tipo        | Descripción |
|------------|------------|-------------|
| `post_id`  | `UUID (FK)` | Relación con `Post` |
| `tag_id`   | `UUID (FK)` | Relación con `Tag` |

#### 6️⃣ **Comment (Comentario)**  

Comentarios de los usuarios en los posts.  

| Campo        | Tipo        | Descripción |
|-------------|------------|-------------|
| `id`        | `UUID (PK)` | Identificador único |
| `post_id`   | `UUID (FK)` | Relación con `Post` |
| `user_id`   | `UUID (FK)` | Relación con `User` |
| `content`   | `TEXT`      | Contenido del comentario |
| `created_at`| `TIMESTAMP` | Fecha de creación |

#### 7️⃣ **Like (Me gusta en los posts)**  

Guarda los likes de los usuarios en los posts.  

| Campo       | Tipo        | Descripción |
|------------|------------|-------------|
| `id`       | `UUID (PK)` | Identificador único |
| `post_id`  | `UUID (FK)` | Relación con `Post` |
| `user_id`  | `UUID (FK)` | Relación con `User` |
| `created_at` | `TIMESTAMP` | Fecha del like |

---
