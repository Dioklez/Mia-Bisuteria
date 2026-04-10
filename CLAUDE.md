# CLAUDE.md — Mía Bisutería

Guía de contexto para Claude Code. Léela completa antes de tocar cualquier archivo.

---

## Identidad del proyecto

**Mía Bisutería** es una tienda de accesorios artesanales hechos a mano (pulseras, argollas, collares, anillos, tobilleras). El sitio es la vitrina digital de la marca: muestra el catálogo, permite armar un pedido y enviarlo por WhatsApp, y acepta solicitudes de piezas personalizadas. No hay carrito de compra real ni pasarela de pago.

**Cliente:** Argentina  
**Moneda:** ARS (`$` con `.toLocaleString('es-AR')`)  
**Contacto WhatsApp:** `5491137742189`  
**Instagram:** `@miabisuteria.ar`  
**Crédito web:** Localink (`https://localink-online.netlify.app`)

---

## Stack técnico

| Capa | Tecnología |
|---|---|
| Markup | HTML5 semántico, sin framework |
| Estilos | CSS puro en `css/style.css` + `<style>` inline por página |
| Lógica | Vanilla JS en `js/app.js` + scripts inline en cada HTML |
| Base de datos | Firebase Firestore (compat SDK v10.12.0) |
| Almacenamiento de imágenes | Firebase Storage |
| Tipografías | Google Fonts (ver sección de marca) |
| Íconos | Font Awesome 6.5.0 (CDN) |
| Deploy | (definir — Netlify / Firebase Hosting) |

**No hay bundler, no hay npm, no hay TypeScript.** Todo es archivos directos.

---

## Estructura de archivos

```
/
├── index.html              ← Página principal (hero, colecciones, destacados, testimonios, CTA)
├── catalogo.html           ← Catálogo completo con filtros por tipo y colección
├── personalizado.html      ← Formulario de pedido personalizado → WhatsApp
├── panel-mk7x2qp9.html     ← Panel de administración (URL ofuscada a propósito)
├── css/
│   └── style.css           ← Estilos globales + variables CSS
├── js/
│   └── app.js              ← Lógica compartida: Firebase, productos, pedido, panel lateral
└── images/
    ├── logos/
    │   └── flor-verde.jpg
    └── [fotos de productos].jpeg
```

---

## Paleta de colores — NUNCA cambiar sin pedido explícito

Estas variables están definidas en `css/style.css` `:root` y replicadas inline en `panel-mk7x2qp9.html`.

```css
/* Verdes (identidad principal) */
--v:       #1e2d08   /* verde oscuro — textos, botones primarios */
--v2:      #2e440f   /* hover de verde */
--v3:      #4a6b22   /* acento medio */
--v4:      #7a9c52   /* acento suave */
--vmenta:  #c8deb0   /* menta — bordes decorativos, gradientes hero */
--vfondo:  #e8f0d8   /* fondo verde muy claro */

/* Cremas (fondos, tarjetas) */
--c:       #f9f4ec
--c2:      #f2e8d4
--c3:      #e6d8be
--cborde:  #d8c9aa

/* Dorado (acentos premium) */
--oro:     #b8862e
--oro2:    #d4a84a
--orop:    #f0d898
--orofondo:#faf3de

/* Texto */
--txt:     #1c1408
--txt2:    #5a4228
--txt3:    #8a7060
```

---

## Tipografías — NUNCA cambiar sin pedido explícito

```css
--fn: 'Playfair Display', serif      /* títulos y display */
--fs: 'Cormorant Garamond', serif    /* subtítulos elegantes, cursivas */
--fb: 'Jost', sans-serif             /* cuerpo, UI, labels */
--fl: 'Dancing Script', cursive      /* logo "Mía Bisutería" */
```

Google Fonts link incluye: `Playfair+Display`, `Cormorant+Garamond`, `Jost`, `Dancing+Script`.

---

## Firebase — Estructura de Firestore

### Colección `config`

#### Documento `productos`
```js
{
  items: [ /* array de objetos PRODUCTO */ ]
}
```

#### Documento `site`
```js
{
  destacado: { nombre, precio, img },       // producto featured en hero del index
  favoritos: [id, id, id, id],              // IDs de productos en sección "Destacados"
  testimonios: [ { id, nombre, ciudad, estrellas, texto } ],
  colecciones: [ { key, emoji, nombre, img } ]
}
```

### Objeto PRODUCTO
```js
{
  id:     Number,    // entero único, comenzando en 0
  nombre: String,
  precio: Number,    // en ARS, sin puntos ni comas
  tipo:   'pulsera' | 'argolla' | 'collar' | 'anillo' | 'tobillera',
  col:    'flores' | 'mariposa' | 'girasol' | 'cherry' | 'caracoles',
  desc:   String,
  imgs:   [String]   // array de rutas relativas o URLs de Storage
}
```

**Fallback:** Si Firestore no está disponible, el sitio usa `PRODUCTOS_DEFAULT` definido en `app.js`. Siempre mantener ese array sincronizado.

---

## Lo que está FIJO — no cambiar sin justificación muy clara

### 1. Panel de administración (`panel-mk7x2qp9.html`)
El panel es la herramienta de control de la tienda. Permite:
- CRUD de productos (crear, editar, eliminar, reordenar)
- Subida de imágenes a Firebase Storage
- Configurar producto destacado del hero
- Seleccionar favoritos para la sección de destacados
- Editar testimonios
- Editar colecciones
- Vista previa del catálogo
- Login con contraseña simple (hardcodeada en el HTML)

**Reglas:**
- El panel SIEMPRE debe poder leer y escribir en Firestore
- La subida de imágenes usa Firebase Storage y guarda la URL pública en el producto
- No romper el sistema de tabs (`tab` / `tab.activo`)
- No cambiar la lógica de login
- El nombre del archivo (`panel-mk7x2qp9.html`) está ofuscado intencionalmente — no renombrarlo

### 2. Sistema de pedido lateral
El "carrito" es un panel lateral (`#pedido-panel`) que acumula productos en `localStorage` bajo la clave `mia-pedido`. Al finalizar genera un mensaje de WhatsApp formateado.

Funciones globales que DEBEN existir en `window`:
```js
window.abrirPanel()
window.cerrarPanel()
window.agregarAlPedido(btn, id)
window.agregarRapido(id)
window.cambiarQty(btn, delta)
window.cambiarCantidadPanel(id, delta)
window.quitarDelPedido(id)
window.enviarPedidoWA()
window.enviarPersonalizadoCompleto()
window.cerrarMnav()
```

### 3. Identidad de marca
- Nombre siempre: **Mía Bisutería** (con tilde en la í)
- Logo en footer/header: clase `.t-logo` (Dancing Script)
- Crédito Localink en footer — no eliminarlo
- Favicon: `images/logos/flor-verde.jpg`
- WhatsApp FAB presente en todas las páginas públicas

### 4. `app.js` como archivo compartido
`app.js` es cargado por todas las páginas públicas. Contiene:
- `FIREBASE_CONFIG` y `PRODUCTOS_DEFAULT`
- `initFirebase()`, `cargarDatosFirebase()`, `cargarConfigFirebase()`
- `initHeader()`, `initPanel()`, `actualizarPanel()`
- `renderCatalogo()`, `crearCarrusel()`, `initCarruseles()`
- `aplicarFiltros()`
- Sistema de pedido completo
- `initReveal()` (animaciones de scroll)

No mover lógica de app.js a archivos separados sin hablar antes.

---

## Lo que es FLEXIBLE — se puede rediseñar

- Diseño del **hero** de `index.html` (decoraciones, layout, badge flotante)
- Diseño de las **tarjetas de producto** (`.pcard`) — estructura, hover, badges
- Sección de **colecciones** en index
- Sección de **testimonios** en index
- Sección **"Sobre Mía"** (`#historia`) — copy e imágenes
- Sección **CTA final** (`#cta-final`)
- Página de **pedido personalizado** (`personalizado.html`) — diseño del formulario
- Diseño del **catálogo** (`catalogo.html`) — filtros, grid, contadores
- Decoraciones SVG/CSS (flores, ondas, patrones de fondo)
- Animaciones y transiciones
- **Footer** — puede reestructurarse si se pide

Cuando se rediseñe algo flexible, respetar la paleta y tipografías fijas.

---

## Patrones de código importantes

### Carrusel de imágenes en tarjetas
```js
// Llamar después de renderizar tarjetas:
initCarruseles();
```
Las tarjetas con múltiples imágenes usan `.pcard-carrusel` con navegación por puntos.

### Animaciones reveal
```html
<div class="reveal">...</div>
```
`initReveal()` usa IntersectionObserver para agregar `.vis` cuando el elemento entra al viewport.

### Clases de delay para stagger
```html
class="reveal d1"   /* delay 100ms */
class="reveal d2"   /* delay 200ms */
/* etc. */
```

### Transición de página
Al hacer clic en links internos, se agrega `.pg-out` al body y se navega tras 300ms. No usar `window.location.href` directo para navegación interna.

### Filtros del catálogo
```js
// Pasar filtro de colección via sessionStorage:
sessionStorage.setItem('mia-filtro-col', 'flores');
// aplicarFiltros() lo lee al cargar catalogo.html
```

---

## Convenciones de estilo CSS

- Mobile-first no aplicado consistentemente — el sitio fue construido desktop-first. Revisar breakpoints al hacer cambios.
- Breakpoint principal: `@media (max-width: 768px)`
- `.wrap` = `width: 90%; max-width: 1180px; margin: 0 auto`
- Bordes redondeados asimétricos (marca artesanal): `border-radius: 4px 28px 4px 28px` o similares
- Transición estándar: `var(--e)` = `0.38s cubic-bezier(.4,0,.2,1)`
- Sombras: `var(--sh)` leve, `var(--sh2)` fuerte

---

## Qué NO hacer

- ❌ No instalar dependencias npm ni agregar un bundler
- ❌ No reemplazar Vanilla JS por React/Vue/etc.
- ❌ No cambiar `FIREBASE_CONFIG` (credenciales reales)
- ❌ No eliminar el crédito de Localink del footer
- ❌ No renombrar `panel-mk7x2qp9.html`
- ❌ No hardcodear precios o texto de productos — vienen de `PRODUCTOS` (que puede venir de Firestore)
- ❌ No usar `alert()` para errores del usuario — el sitio usa feedback visual inline

---

## Sugerencias bienvenidas

Si ves una forma mejor de hacer algo (performance, accesibilidad, UX), mencionalo antes de cambiar. Ejemplo: "Podría convertir el carrusel a CSS puro sin JS para reducir re-renders — ¿lo hacemos así?"