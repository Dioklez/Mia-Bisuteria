const FIREBASE_CONFIG = {
  apiKey: "AIzaSyDJPxQ-L4XiWmGuNi2XYKnuEhhidnHnIhI",
  authDomain: "mia-bisuteria.firebaseapp.com",
  projectId: "mia-bisuteria",
  storageBucket: "mia-bisuteria.firebasestorage.app",
  messagingSenderId: "406918365058",
  appId: "1:406918365058:web:b444d55e4983c1ec99999b"
};

const PRODUCTOS_DEFAULT = [
  {id:0,  nombre:'Anillo de Flores Rosa',           precio:2500,  tipo:'anillo',    col:'flores',    desc:'Hecho a mano con mostacillas checas y componentes gold filled.',imgs:['images/anillo-flores-rosa.jpeg']},
  {id:1,  nombre:'Anillo de Flores Blanco',         precio:2500,  tipo:'anillo',    col:'flores',    desc:'Hecho a mano con mostacillas checas y componentes gold filled.',imgs:['images/anillo-flores-blanco.jpeg']},
  {id:2,  nombre:'Argollas Alas en Flor',           precio:11999, tipo:'argolla',   col:'mariposa',  desc:'Base de aro gold filled con dijes de flores hechos a mano.',imgs:['images/argollas-alas-flor-1.jpeg','images/argollas-alas-flor-2.jpeg','images/argollas-alas-flor-3.jpeg']},
  {id:3,  nombre:'Argollas Alma de Girasoles',      precio:11999, tipo:'argolla',   col:'girasol',   desc:'Base de aro gold filled con dijes de flores hechos a mano.',imgs:['images/argollas-alma-girasol-1.jpeg','images/argollas-alma-girasol-2.jpeg']},
  {id:4,  nombre:'Argollas Lluvia de Margaritas',   precio:13900, tipo:'argolla',   col:'flores',    desc:'Base de aro gold filled con dijes de flores hechos a mano.',imgs:['images/argollas-lluvia-margaritas.jpeg']},
  {id:5,  nombre:'Argollas Flor de Nieve',          precio:9900,  tipo:'argolla',   col:'flores',    desc:'Base de aro gold filled con dijes de flores hechos a mano.',imgs:['images/argollas-flor-nieve.jpeg']},
  {id:6,  nombre:'Collar Mariposa Lila',            precio:10500, tipo:'collar',    col:'mariposa',  desc:'Hecho a mano con mostacillas Miyuki, cristal de roca y componentes gold filled. Fino, femenino y muy liviano.',imgs:['images/collar-mariposa-lila-1.jpeg','images/collar-mariposa-lila-2.jpeg']},
  {id:7,  nombre:'Gargantilla Lluvia de Margaritas',precio:13490, tipo:'collar',    col:'flores',    desc:'Diseño delicado con mostacillas y componentes gold filled. 37 cm de largo con 5 cm de extensión.',imgs:['images/gargantilla-lluvia-margaritas-1.jpeg','images/gargantilla-lluvia-margaritas-2.jpeg','images/gargantilla-lluvia-margaritas-3.jpeg','images/gargantilla-lluvia-margaritas-4.jpeg']},
  {id:8,  nombre:'Tobillera de Caracoles',          precio:4000,  tipo:'tobillera', col:'caracoles', desc:'El accesorio que vuelve todos los veranos. Caracoles naturales, detalles en Miyuki, gold filled y cierre regulable.',imgs:['images/tobillera-caracoles-1.jpeg','images/tobillera-caracoles-2.jpeg','images/tobillera-caracoles-3.jpeg','images/tobillera-caracoles-4.jpeg']},
  {id:9,  nombre:'Pulsera Alma de Girasoles',       precio:6499,  tipo:'pulsera',   col:'girasol',   desc:'Hecha a mano con mostacillas checas, cristales checos y componentes gold filled.',imgs:['images/pulsera-alma-girasol-1.jpeg','images/pulsera-alma-girasol-2.jpeg','images/pulsera-alma-girasol-3.jpeg']},
  {id:10, nombre:'Pulsera Flor de Nieve',           precio:8700,  tipo:'pulsera',   col:'flores',    desc:'Diseño delicado con mostacillas y componentes gold filled. 16 cm de largo con 5 cm de extensión.',imgs:['images/pulsera-flor-nieve.jpeg']},
  {id:11, nombre:'Pulsera Margaritas de Amor Rosa', precio:8800,  tipo:'pulsera',   col:'flores',    desc:'Diseño delicado con mostacillas, cristales y componentes gold filled. 16 cm de largo con 5 cm de extensión.',imgs:['images/margaritas-amor-rosa-1.jpeg','images/margaritas-amor-rosa-2.jpeg']},
  {id:12, nombre:'Pulsera Patria de Flores',        precio:9900,  tipo:'pulsera',   col:'flores',    desc:'Un accesorio que representa identidad, amor y raíces. Mostacillas, cristales y gold filled. 16 cm con 5 cm de extensión.',imgs:['images/pulsera-patria-flores-1.jpeg','images/pulsera-patria-flores-2.jpeg','images/pulsera-patria-flores-3.jpeg']},
  {id:13, nombre:'Pulsera Flor Amapola Rosa',       precio:9500,  tipo:'pulsera',   col:'flores',    desc:'Diseño delicado con mostacillas, cristales y componentes gold filled. 16 cm de largo con 5 cm de extensión.',imgs:['images/pulsera-flor-amapola-rosa-1.jpeg','images/pulsera-flor-amapola-rosa-2.jpeg']},
  {id:14, nombre:'Pulsera Tierra Floral',           precio:8700,  tipo:'pulsera',   col:'flores',    desc:'Diseño delicado con mostacillas, cristales y componentes gold filled. 16 cm de largo con 5 cm de extensión.',imgs:['images/pulsera-tierra-flor-1.jpeg','images/pulsera-tierra-flor-2.jpeg']},
  {id:15, nombre:'Pulsera Margaritas',              precio:9000,  tipo:'pulsera',   col:'flores',    desc:'Hecha a mano con Miyuki rocalla 8/0 y componentes gold filled. 16 cm de largo con 5 cm de extensión.',imgs:['images/pulsera-margaritas-1.jpeg','images/pulsera-margaritas-2.jpeg']},
  {id:16, nombre:'Pulsera Cherry con Margaritas',   precio:8650,  tipo:'pulsera',   col:'cherry',    desc:'Diseño delicado con mostacillas, cristales y componentes gold filled. 16 cm de largo con 5 cm de extensión.',imgs:['images/pulsera-cherry-margaritas-1.jpeg','images/pulsera-cherry-margaritas-2.jpeg']},
  {id:17, nombre:'Pulsera Mariposa Mía',            precio:8500,  tipo:'pulsera',   col:'mariposa',  desc:'Hecha a mano con Miyuki, cristal de roca y componentes gold filled. 16 cm de largo con 5 cm de extensión.',imgs:['images/pulsera-mariposa-mia.jpeg']},
  {id:18, nombre:'Pulsera Suspiro Lila',            precio:8500,  tipo:'pulsera',   col:'flores',    desc:'Hecha a mano con mostacillas, cristales y componentes gold filled.',imgs:['images/pulsera-suspiro-lila.jpeg']},
  {id:19, nombre:'Daisy Bracelet con Cristales',    precio:6900,  tipo:'pulsera',   col:'flores',    desc:'Hecha a mano con Miyuki, cristal de roca y componentes gold filled. 16 cm de largo con 5 cm de extensión.',imgs:['images/daisy-bracelet-cristales-1.jpeg','images/daisy-bracelet-cristales-2.jpeg','images/daisy-bracelet-cristales-3.jpeg']},
  {id:20, nombre:'Pulsera Rapunzel',                precio:6900,  tipo:'pulsera',   col:'flores',    desc:'Hecha a mano con Miyuki, cristal de roca, mostacillas checas y componentes gold filled. 16 cm con 5 cm de extensión.',imgs:['images/pulsera-rapunzel.jpeg']},
  {id:21, nombre:'Pulsera Miel',                    precio:6900,  tipo:'pulsera',   col:'flores',    desc:'Hecha a mano con Miyuki, cristal de roca y componentes gold filled. 16 cm de largo con 5 cm de extensión.',imgs:['images/pulsera-miel-1.jpeg','images/pulsera-miel-2.jpeg']},
  {id:22, nombre:'Pulsera Petalina',                precio:6900,  tipo:'pulsera',   col:'flores',    desc:'Inspirada en los pétalos más suaves del jardín. Hecha a mano con mostacillas, cristales y componentes gold filled.',imgs:['images/pulsera-petalina.jpeg']},
  {id:23, nombre:'Aros Flor de Cristal',            precio:10700, tipo:'argolla',   col:'flores',    desc:'Base de aro gold filled y flores hechas a mano.',imgs:['images/aros-flor-cristal.jpeg']},
  {id:24, nombre:'Pulsera Suspiro de Primavera',    precio:7200,  tipo:'pulsera',   col:'flores',    desc:'Hecha a mano con Miyuki y componentes gold filled. 16 cm de largo con 5 cm de extensión.',imgs:['images/pulsera-suspiro-primavera.jpeg']},
  {id:25, nombre:'Aros Flor de Cristal Lila',       precio:10700, tipo:'argolla',   col:'flores',    desc:'Base de aro gold filled y flores hechas a mano.',imgs:['images/aros-flor-cristal-lila.jpeg']},
];

let PRODUCTOS  = [...PRODUCTOS_DEFAULT];
let db         = null;

const WA_NUM = '5491137742189';

function initFirebase() {
  try {
    if (!firebase.apps.length) firebase.initializeApp(FIREBASE_CONFIG);
    db = firebase.firestore();
    // Emuladores locales para desarrollo
    if (location.hostname === 'localhost' || location.hostname === '127.0.0.1') {
      db.useEmulator('localhost', 8080);
      firebase.app().functions('us-central1').useEmulator('localhost', 5001);
      console.log('%c[DEV] Conectado a emuladores locales', 'color:#4a6b22;font-weight:bold');
    }
  } catch(e) {
    console.warn('Firebase no disponible, usando localStorage como fallback');
  }
}

async function cargarDatosFirebase() {
  if (!db) return;
  try {
    const snap = await db.collection('config').doc('productos').get();
    if (snap.exists && snap.data().items && snap.data().items.length > 0) {
      PRODUCTOS = snap.data().items;
    }
  } catch(e) {
    console.warn('Error cargando productos de Firebase:', e);
  }
}

async function cargarConfigFirebase() {
  if (!db) return {};
  try {
    const snap = await db.collection('config').doc('site').get();
    return snap.exists ? snap.data() : {};
  } catch(e) { return {}; }
}

function cargarPedido() {
  try { return JSON.parse(localStorage.getItem('mia-pedido') || '{}'); } catch { return {}; }
}
function guardarPedido(p) {
  try { localStorage.setItem('mia-pedido', JSON.stringify(p)); } catch {}
}
let pedido = cargarPedido();

function initHeader() {
  const hdr = document.getElementById('hdr');
  if (!hdr) return;
  const onScroll = () => hdr.classList.toggle('scrolled', scrollY > 50);
  window.addEventListener('scroll', onScroll, { passive:true });
  onScroll();
  const path = location.pathname.split('/').pop() || 'index.html';
  document.querySelectorAll('.nav-links a, .mnav a').forEach(a => {
    const href = a.getAttribute('href') || '';
    if (href === path || (path === 'index.html' && href === './') || href.includes(path))
      a.classList.add('activo');
  });
  const ham = document.getElementById('ham');
  const mnav = document.getElementById('mnav');
  const mnavOver = document.getElementById('mnav-over');
  if (!ham) return;
  ham.addEventListener('click', () => {
    const open = mnav.classList.toggle('open');
    ham.classList.toggle('open', open);
    mnavOver.classList.toggle('open', open);
    document.body.style.overflow = open ? 'hidden' : '';
  });
  window.cerrarMnav = function() {
    mnav.classList.remove('open');
    ham.classList.remove('open');
    mnavOver.classList.remove('open');
    document.body.style.overflow = '';
  };
}

function initPanel() {
  const panel = document.getElementById('pedido-panel');
  const panelOver = document.getElementById('panel-over');
  if (!panel) return;
  window.abrirPanel = function() {
    panel.classList.add('open'); panelOver.classList.add('open');
    document.body.style.overflow = 'hidden';
  };
  window.cerrarPanel = function() {
    panel.classList.remove('open'); panelOver.classList.remove('open');
    document.body.style.overflow = '';
  };
  actualizarPanel();

  // Inyectar modal de checkout multi-paso si no existe
  if (!document.getElementById('checkout-overlay')) {
    document.body.insertAdjacentHTML('beforeend', `
      <div id="checkout-overlay" style="display:none;position:fixed;inset:0;background:rgba(0,0,0,.55);z-index:3000;overflow-y:auto;padding:16px 12px;-webkit-overflow-scrolling:touch">
        <div id="checkout-box" style="background:#fff;border-radius:20px;max-width:440px;margin:0 auto;overflow:hidden;box-shadow:0 20px 60px rgba(0,0,0,.25)">

          <!-- Header -->
          <div style="background:var(--v);color:#fff;padding:16px 20px;display:flex;align-items:center;justify-content:space-between">
            <div style="display:flex;align-items:center;gap:10px">
              <span id="checkout-step-badge" style="background:rgba(255,255,255,.2);border-radius:50%;width:24px;height:24px;display:flex;align-items:center;justify-content:center;font-size:.72rem;font-weight:700">1</span>
              <span id="checkout-titulo" style="font-family:var(--fl);font-size:1.25rem">Tus datos</span>
            </div>
            <button onclick="cerrarCheckout()" style="background:none;border:none;color:#fff;font-size:1.1rem;cursor:pointer;padding:4px 8px"><i class="fa-solid fa-xmark"></i></button>
          </div>

          <!-- Resumen siempre visible -->
          <div id="checkout-resumen" style="padding:12px 20px;background:var(--vfondo);border-bottom:1px solid var(--vmenta);font-size:.82rem"></div>

          <!-- PASO 1: Datos de contacto -->
          <div id="co-paso1" style="padding:18px 20px">
            <div style="margin-bottom:12px">
              <label style="display:block;font-size:.76rem;color:var(--txt3);margin-bottom:4px;font-family:var(--fb);font-weight:500">Tu nombre *</label>
              <input id="checkout-nombre" type="text" placeholder="Ej: Sofía" autocomplete="given-name"
                style="width:100%;padding:11px 14px;border:1px solid var(--cborde);border-radius:10px;font-size:.9rem;font-family:var(--fb);box-sizing:border-box"/>
            </div>
            <div style="margin-bottom:12px">
              <label style="display:block;font-size:.76rem;color:var(--txt3);margin-bottom:4px;font-family:var(--fb);font-weight:500">Email *</label>
              <input id="checkout-email" type="email" placeholder="sofia@email.com" autocomplete="email"
                style="width:100%;padding:11px 14px;border:1px solid var(--cborde);border-radius:10px;font-size:.9rem;font-family:var(--fb);box-sizing:border-box"/>
              <span style="font-size:.7rem;color:var(--txt3)">MercadoPago envía el comprobante aquí</span>
            </div>
            <div style="margin-bottom:16px">
              <label style="display:block;font-size:.76rem;color:var(--txt3);margin-bottom:4px;font-family:var(--fb);font-weight:500">WhatsApp *</label>
              <input id="checkout-telefono" type="tel" placeholder="5491100000000" autocomplete="tel"
                style="width:100%;padding:11px 14px;border:1px solid var(--cborde);border-radius:10px;font-size:.9rem;font-family:var(--fb);box-sizing:border-box"/>
              <span style="font-size:.7rem;color:var(--txt3)">Con código de país, para el seguimiento</span>
            </div>
            <div id="co-error1" style="display:none;color:#c62828;font-size:.78rem;margin-bottom:10px;padding:10px 14px;background:#fce4ec;border-radius:8px"></div>
            <button onclick="irAlPago()" style="width:100%;padding:14px;background:var(--v);color:#fff;border:none;border-radius:12px;font-size:.92rem;font-weight:500;cursor:pointer;font-family:var(--fb);display:flex;align-items:center;justify-content:center;gap:8px">
              Continuar al pago <i class="fa-solid fa-arrow-right"></i>
            </button>
          </div>

          <!-- PASO 2: Formulario de pago (MercadoPago Bricks) -->
          <div id="co-paso2" style="display:none;padding:18px 20px">
            <div id="co-brick-loading" style="text-align:center;padding:30px;color:var(--txt3);font-size:.85rem">
              <i class="fa-solid fa-spinner fa-spin" style="font-size:1.5rem;margin-bottom:10px"></i><br/>Cargando formulario de pago...
            </div>
            <div id="cardPaymentBrick_container"></div>
            <div id="co-error2" style="display:none;color:#c62828;font-size:.78rem;margin-top:10px;padding:10px 14px;background:#fce4ec;border-radius:8px"></div>
            <div id="co-dev-tools" style="display:none;margin-top:16px;padding:12px;background:#fffde7;border:1px dashed #f9a825;border-radius:8px;text-align:center">
              <p style="font-size:.7rem;color:#e65100;margin-bottom:8px;font-weight:600">🧪 Modo desarrollo — solo en localhost</p>
              <div style="display:flex;gap:8px">
                <button onclick="_simularPago()" style="flex:1;padding:9px;background:#388e3c;color:#fff;border:none;border-radius:8px;font-size:.78rem;font-weight:600;cursor:pointer;font-family:var(--fb)">✅ Simular aprobado</button>
                <button onclick="_simularPagoFallido()" style="flex:1;padding:9px;background:#c62828;color:#fff;border:none;border-radius:8px;font-size:.78rem;font-weight:600;cursor:pointer;font-family:var(--fb)">❌ Simular rechazo</button>
              </div>
            </div>
            <button onclick="volverPaso1()" style="width:100%;margin-top:10px;padding:10px;background:none;border:none;color:var(--txt3);font-size:.78rem;cursor:pointer;font-family:var(--fb)">
              <i class="fa-solid fa-arrow-left"></i> Volver a mis datos
            </button>
          </div>

          <!-- PASO 3: Resultado -->
          <div id="co-paso3" style="display:none;padding:32px 20px;text-align:center">
            <div id="co-resultado-icono" style="font-size:3rem;margin-bottom:12px"></div>
            <div id="co-resultado-titulo" style="font-family:var(--fn);font-size:1.3rem;font-weight:700;margin-bottom:8px;color:var(--v)"></div>
            <div id="co-resultado-msg" style="font-size:.85rem;color:var(--txt2);line-height:1.6;margin-bottom:24px"></div>
            <button id="co-resultado-btn" onclick="cerrarCheckout()" style="padding:12px 28px;background:var(--v);color:#fff;border:none;border-radius:12px;font-size:.88rem;font-weight:500;cursor:pointer;font-family:var(--fb)">
              Cerrar
            </button>
          </div>

        </div>
      </div>`);
  }
}

// Estado del checkout entre pasos
const _co = { items: [], total: 0, pedidoId: null, brickController: null };

window.abrirCheckout = function() {
  const ids = Object.keys(pedido).filter(k => pedido[k] > 0);
  if (ids.length === 0) { alert('Tu pedido está vacío.'); return; }

  _co.items = []; _co.total = 0;
  let html = '';
  ids.forEach(k => {
    const p = PRODUCTOS.find(pr => pr.id === parseInt(k)); if (!p) return;
    const qty = pedido[k];
    _co.total += p.precio * qty;
    _co.items.push({ productoId: p.id, nombre: p.nombre, cantidad: qty, precioUnitario: p.precio });
    html += `<span style="color:var(--txt2)">${p.nombre} x${qty}</span><span style="font-weight:600;margin-left:auto;padding-left:12px">$${(p.precio*qty).toLocaleString('es-AR')}</span>`;
    html = `<div style="display:flex;justify-content:space-between;padding:3px 0">${html.slice(html.lastIndexOf('<span'))}</div>` + (html.slice(0, html.lastIndexOf('<span')));
  });
  // rebuild limpio
  html = _co.items.map(i => `
    <div style="display:flex;justify-content:space-between;padding:3px 0">
      <span style="color:var(--txt2)">${i.nombre} <span style="color:var(--txt3)">x${i.cantidad}</span></span>
      <span style="font-weight:600">$${(i.precioUnitario*i.cantidad).toLocaleString('es-AR')}</span>
    </div>`).join('');
  html += `<div style="display:flex;justify-content:space-between;margin-top:6px;padding-top:6px;border-top:1px solid var(--vmenta);font-weight:700;color:var(--v)">
    <span>Total</span><span>$${_co.total.toLocaleString('es-AR')}</span>
  </div>`;

  document.getElementById('checkout-resumen').innerHTML = html;
  _mostrarPaso(1);

  const overlay = document.getElementById('checkout-overlay');
  overlay.style.display = 'block';
  overlay.scrollTop = 0;
  document.body.style.overflow = 'hidden';
  if (window.cerrarPanel) window.cerrarPanel();
};

function _mostrarPaso(n) {
  [1,2,3].forEach(i => {
    document.getElementById(`co-paso${i}`).style.display = i === n ? '' : 'none';
  });
  document.getElementById('checkout-step-badge').textContent = n < 3 ? n : '✓';
  const titulos = ['Tus datos','Pago seguro',''];
  document.getElementById('checkout-titulo').textContent = titulos[n-1];
  // Mostrar panel de desarrollo solo en localhost al llegar al paso 2
  if (n === 2) {
    const devTools = document.getElementById('co-dev-tools');
    if (devTools && (location.hostname === 'localhost' || location.hostname === '127.0.0.1')) {
      devTools.style.display = 'block';
    }
  }
}

window.volverPaso1 = function() {
  if (_co.brickController) { _co.brickController.unmount(); _co.brickController = null; }
  _mostrarPaso(1);
};

window.irAlPaso = window.irAlPago = async function() {
  const nombre   = document.getElementById('checkout-nombre').value.trim();
  const email    = document.getElementById('checkout-email').value.trim();
  const telefono = document.getElementById('checkout-telefono').value.trim().replace(/\D/g,'');
  const errEl    = document.getElementById('co-error1');

  if (!nombre) { errEl.textContent = 'Ingresá tu nombre.'; errEl.style.display = 'block'; return; }
  if (!email || !email.includes('@')) { errEl.textContent = 'Ingresá un email válido.'; errEl.style.display = 'block'; return; }
  if (!telefono || telefono.length < 10) { errEl.textContent = 'Ingresá tu WhatsApp con código de país (ej: 5491100000000).'; errEl.style.display = 'block'; return; }
  errEl.style.display = 'none';

  // Mostrar spinner en el botón mientras guarda
  const btnContinuar = document.querySelector('#co-paso1 button[onclick="irAlPago()"]');
  if (btnContinuar) { btnContinuar.disabled = true; btnContinuar.innerHTML = '<i class="fa-solid fa-spinner fa-spin"></i> Guardando...'; }

  try {
    // Guardar pedido en Firestore
    const ahora  = new Date();
    const _timeout = new Promise((_, rej) => setTimeout(() => rej(new Error('timeout')), 6000));
    const docRef = await Promise.race([
      db.collection('pedidos').add({
        tipo: 'catalogo', estado: 'pendiente_pago',
        cliente: { nombre, email, telefono },
        items: _co.items,
        personalizado: null,
        costos: { materiales:0, tiempoHs:0, margenPct:0, subtotal:_co.total, envio:0, total:_co.total },
        pago:  { preferenceId:'', paymentId:'', estado:'pendiente', linkPago:'', fechaPago:null },
        envio: { tipo:'', direccion:'', nota:'' },
        creadoEn:      firebase.firestore.FieldValue.serverTimestamp(),
        actualizadoEn: firebase.firestore.FieldValue.serverTimestamp(),
        expiraEn: new Date(ahora.getTime() + 48*60*60*1000)
      }),
      _timeout
    ]);
    _co.pedidoId = docRef.id;
    _mostrarPaso(2);

    // Cargar SDK de MercadoPago si no está cargado
    await _cargarSDKMercadoPago();

    // Renderizar CardPaymentBrick
    document.getElementById('co-brick-loading').style.display = 'block';
    const mp = new MercadoPago('APP_USR-2699896373482253-042319-d76be0d0169eebecff0e6401569eb6af-2362763592', { locale: 'es-AR' });
    const bricksBuilder = mp.bricks();

    _co.brickController = await bricksBuilder.create('cardPayment', 'cardPaymentBrick_container', {
      initialization: { amount: _co.total, payer: { email } },
      customization: {
        visual: { hideFormTitle: true, style: { theme: 'default' } },
        paymentMethods: { minInstallments: 1, maxInstallments: 1 }
      },
      callbacks: {
        onReady: () => { document.getElementById('co-brick-loading').style.display = 'none'; },
        onSubmit: async (cardFormData) => {
          try {
            const procesarPago = firebase.app().functions('us-central1').httpsCallable('procesarPago');
            const result = await procesarPago({ pedidoId: _co.pedidoId, formData: cardFormData, email, nombre });
            if (result.data.status === 'approved') {
              pedido = {}; guardarPedido(pedido); actualizarPanel();
              _mostrarResultado('exito', nombre);
            } else {
              _mostrarResultado('error', null, result.data.status);
            }
          } catch(err) {
            console.error(err);
            _mostrarResultado('error', null, err.message);
          }
        },
        onError: (err) => {
          console.error('Brick error:', err);
          document.getElementById('co-error2').textContent = 'Error en el formulario de pago. Intentá de nuevo.';
          document.getElementById('co-error2').style.display = 'block';
        }
      }
    });
  } catch(err) {
    console.error(err);
    // Si el pedido no se guardó, volver al paso 1 con error
    if (!_co.pedidoId) {
      if (btnContinuar) { btnContinuar.disabled = false; btnContinuar.innerHTML = 'Continuar al pago <i class="fa-solid fa-arrow-right"></i>'; }
      errEl.textContent = 'Error al guardar el pedido. Revisá tu conexión e intentá de nuevo.';
      errEl.style.display = 'block';
    } else {
      document.getElementById('co-error2').textContent = 'Error al cargar el formulario de pago. Usá los botones de simulación.';
      document.getElementById('co-error2').style.display = 'block';
    }
  }
};

function _mostrarResultado(tipo, nombre, errorMsg) {
  if (_co.brickController) { _co.brickController.unmount(); _co.brickController = null; }
  _mostrarPaso(3);
  const ico   = document.getElementById('co-resultado-icono');
  const tit   = document.getElementById('co-resultado-titulo');
  const msg   = document.getElementById('co-resultado-msg');
  const btn   = document.getElementById('co-resultado-btn');
  if (tipo === 'exito') {
    ico.textContent = '✅';
    tit.textContent = `¡Gracias${nombre ? ', '+nombre : ''}!`;
    tit.style.color = 'var(--v3)';
    msg.textContent = 'Tu pago fue aprobado. Mía va a ponerse en contacto con vos para coordinar la entrega.';
    btn.textContent = 'Cerrar';
  } else {
    ico.textContent = '❌';
    tit.textContent = 'No se pudo procesar el pago';
    tit.style.color = '#c62828';
    const msgs = { cc_rejected_bad_filled_card_number:'Número de tarjeta incorrecto.', cc_rejected_insufficient_amount:'Fondos insuficientes.', cc_rejected_bad_filled_date:'Fecha de vencimiento incorrecta.', cc_rejected_bad_filled_security_code:'Código de seguridad incorrecto.' };
    msg.textContent = msgs[errorMsg] || 'Revisá los datos de tu tarjeta o intentá con otra. Si el problema continúa, usá la opción de WhatsApp.';
    btn.textContent = 'Intentar de nuevo';
    btn.onclick = () => { _mostrarPaso(2); _co.brickController = null; irAlPago._retry = true; };
  }
}

async function _simularPago() {
  if (!_co.pedidoId) { alert('Primero completá tus datos y presioná "Continuar al pago".'); return; }
  try {
    const nombre = document.getElementById('checkout-nombre')?.value.trim() || 'Cliente';
    await db.collection('pedidos').doc(_co.pedidoId).update({
      estado: 'pago_confirmado',
      'pago.paymentId': 'SIM-' + Date.now(),
      'pago.estado': 'aprobado',
      'pago.fechaPago': firebase.firestore.FieldValue.serverTimestamp(),
      actualizadoEn: firebase.firestore.FieldValue.serverTimestamp()
    });
    pedido = {}; guardarPedido(pedido); actualizarPanel();
    _mostrarResultado('exito', nombre);
  } catch(err) {
    alert('Error al simular pago: ' + err.message);
  }
}

async function _simularPagoFallido() {
  _mostrarResultado('error', null, 'cc_rejected_insufficient_amount');
}

function _cargarSDKMercadoPago() {
  return new Promise((resolve, reject) => {
    if (window.MercadoPago) { resolve(); return; }
    const s = document.createElement('script');
    s.src = 'https://sdk.mercadopago.com/js/v2';
    s.onload = resolve; s.onerror = reject;
    document.head.appendChild(s);
  });
}

window.cerrarCheckout = function() {
  if (_co.brickController) { _co.brickController.unmount(); _co.brickController = null; }
  document.getElementById('checkout-overlay').style.display = 'none';
  document.body.style.overflow = '';
  // Reset para la próxima vez
  document.getElementById('cardPaymentBrick_container').innerHTML = '';
};

function actualizarPanel() {
  const container = document.getElementById('pp-items');
  const empty     = document.getElementById('pp-empty');
  const totalEl   = document.getElementById('pp-total');
  const countEl   = document.getElementById('pedido-count');
  if (!container) return;
  const ids = Object.keys(pedido).filter(k => pedido[k] > 0);
  const totalUnidades = ids.reduce((s, k) => s + pedido[k], 0);
  if (countEl) countEl.textContent = totalUnidades;
  if (ids.length === 0) {
    if (empty) empty.style.display = 'block';
    container.querySelectorAll('.pp-item').forEach(el => el.remove());
    if (totalEl) totalEl.textContent = '$0';
    return;
  }
  if (empty) empty.style.display = 'none';
  container.querySelectorAll('.pp-item').forEach(el => el.remove());
  let total = 0;
  ids.forEach(k => {
    const p = PRODUCTOS.find(pr => pr.id === parseInt(k));
    if (!p) return;
    const qty = pedido[k];
    total += p.precio * qty;
    const firstImg = (p.imgs && p.imgs[0]) || p.img || '';
    const item = document.createElement('div');
    item.className = 'pp-item';
    item.dataset.id = k;
    item.innerHTML = `
      <div class="pp-item-img">
        ${firstImg ? `<img src="${firstImg}" alt="${p.nombre}" style="width:100%;height:100%;object-fit:cover;border-radius:8px"/>` : '<i class="fa-regular fa-image" style="font-size:.8rem;color:var(--txt3)"></i>'}
      </div>
      <div class="pp-item-info">
        <div class="pp-item-nombre">${p.nombre}</div>
        <div class="pp-item-precio">$${p.precio.toLocaleString('es-AR')}</div>
        <div class="pp-item-ctrl">
          <button class="pp-qty-btn" onclick="cambiarCantidadPanel(${k},-1)">−</button>
          <span class="pp-qty-num">${qty}</span>
          <button class="pp-qty-btn" onclick="cambiarCantidadPanel(${k},1)">+</button>
        </div>
      </div>
      <button class="pp-remove" onclick="quitarDelPedido(${k})" title="Quitar">
        <i class="fa-solid fa-xmark"></i>
      </button>`;
    container.appendChild(item);
  });
  if (totalEl) totalEl.textContent = '$' + total.toLocaleString('es-AR');
}

window.cambiarCantidadPanel = function(id, delta) {
  pedido[id] = (pedido[id] || 0) + delta;
  if (pedido[id] <= 0) delete pedido[id];
  guardarPedido(pedido); actualizarPanel();
};
window.quitarDelPedido = function(id) {
  delete pedido[id]; guardarPedido(pedido); actualizarPanel();
};

function _agregar(id, qty) {
  pedido[id] = (pedido[id] || 0) + qty;
  guardarPedido(pedido); actualizarPanel();
  const cnt = document.getElementById('pedido-count');
  if (cnt) { cnt.classList.remove('bump'); void cnt.offsetWidth; cnt.classList.add('bump'); setTimeout(() => cnt.classList.remove('bump'), 400); }
  if (window.innerWidth > 768 && window.abrirPanel) window.abrirPanel();
}
window.agregarAlPedido = function(btn, id) {
  const container = btn.closest('.pcard') || btn.closest('.pd-ctrl');
  const qty  = parseInt(container.querySelector('.qty-num').value);
  _agregar(id, qty);
  btn.classList.add('added');
  btn.innerHTML = '<i class="fa-solid fa-check"></i> Agregado';
  setTimeout(() => { btn.classList.remove('added'); btn.innerHTML = '<i class="fa-solid fa-bag-shopping"></i> Agregar al pedido'; }, 1800);
};
window.agregarRapido = function(id) { _agregar(id, 1); };
window.cambiarQty = function(btn, delta) {
  const input = btn.closest('.qty').querySelector('.qty-num');
  let v = parseInt(input.value) + delta;
  if (v < 1) v = 1; if (v > 10) v = 10;
  input.value = v;
};

window.enviarPedidoWA = function() {
  const ids = Object.keys(pedido).filter(k => pedido[k] > 0);
  if (ids.length === 0) { alert('Tu pedido está vacío.'); return; }
  let lineas = '', total = 0;
  ids.forEach(k => {
    const p = PRODUCTOS.find(pr => pr.id === parseInt(k)); if (!p) return;
    const qty = pedido[k];
    lineas += `  • ${p.nombre} x${qty} — $${(p.precio * qty).toLocaleString('es-AR')}\n`;
    total += p.precio * qty;
  });
  window.open(`https://wa.me/${WA_NUM}?text=${encodeURIComponent('\u00a1Hola M\u00eda! \u{1F33F} Quiero hacer el siguiente pedido:\n\n'+lineas+'\n*Total estimado: $'+total.toLocaleString('es-AR')+' ARS*\n\n\u00bfEst\u00e1n disponibles estas piezas? \u00bfC\u00f3mo coordino el pago y env\u00edo? \u{2728}')}`, '_blank');
};

window.enviarPersonalizadoCompleto = function() {
  const n   = document.getElementById('f-nombre')?.value.trim()  || 'Sin nombre';
  const t   = document.getElementById('f-tipo')?.value           || 'No especificado';
  const o   = document.getElementById('f-ocasion')?.value        || 'No especificada';
  const pr  = document.getElementById('f-pres')?.value           || 'No especificado';
  const col = document.getElementById('colores-chips') ? [...document.querySelectorAll('.color-chip.sel')].map(c=>c.textContent.trim().replace(/^\S+\s/,'')).join(', ') || 'Sin especificar' : 'Sin especificar';
  const d   = document.getElementById('f-desc')?.value.trim()    || 'Sin descripción';
  const ref = document.getElementById('f-ref')?.value.trim()     || '';
  if (!document.getElementById('f-tipo')?.value || !document.getElementById('f-ocasion')?.value || !document.getElementById('f-desc')?.value.trim()) {
    alert('Completá al menos el tipo, la ocasión y la descripción.'); return;
  }
  window.open(`https://wa.me/${WA_NUM}?text=${encodeURIComponent('\u00a1Hola M\u00eda! \u{1F33F} Quiero un pedido personalizado:\n\n\u{1F464} *Nombre:* '+n+'\n\u{1F48E} *Tipo:* '+t+'\n\u{1F389} *Ocasi\u00f3n:* '+o+'\n\u{1F4B0} *Presupuesto:* '+pr+'\n\u{1F3A8} *Colores:* '+col+(ref?'\n\u{1F5BC}\uFE0F *Referencia:* '+ref:'')+'\n\n\u{1F4DD} *Mi idea:*\n'+d+'\n\n\u00a1Espero tu respuesta! \u{2728}')}`, '_blank');
};

window.toggleColor = function(chip) { chip.classList.toggle('sel'); };

let filtroTipoActivo = 'todos';
let filtroColActivo  = 'todas';

window.filtrarTipo = function(btn, tipo) {
  document.querySelectorAll('.filtro-tipo').forEach(f => f.classList.remove('activo'));
  btn.classList.add('activo'); filtroTipoActivo = tipo; aplicarFiltros();
};
window.filtrarCol = function(btn, col) {
  document.querySelectorAll('.filtro-col').forEach(f => f.classList.remove('activo-col'));
  btn.classList.add('activo-col'); filtroColActivo = col; aplicarFiltros();
};
function aplicarFiltros() {
  const cards = document.querySelectorAll('.pcard[data-id]');
  let visible = 0;
  cards.forEach(card => {
    const tipo = card.dataset.tipo || '';
    const col  = card.dataset.col  || '';
    if (col === 'todos') { card.style.display = ''; return; }
    const ok = (filtroTipoActivo === 'todos' || tipo === filtroTipoActivo) &&
               (filtroColActivo  === 'todas' || col  === filtroColActivo);
    card.style.display = ok ? '' : 'none';
    if (ok) visible++;
  });
  const sr = document.getElementById('sin-resultados');
  if (sr) sr.style.display = visible === 0 ? 'block' : 'none';
}

let TIPO_LABEL = { pulsera:'Pulsera', argolla:'Aritos', collar:'Collar', anillo:'Anillo', tobillera:'Tobillera' };
let COL_LABEL  = { flores:'Flores', mariposa:'Mariposa', girasol:'Girasol', cherry:'Cherry', caracoles:'Caracoles' };

const TIPOS_DEFAULT = [
  {key:'pulsera',   nombre:'Pulsera'},
  {key:'argolla',   nombre:'Argolla / Arito'},
  {key:'collar',    nombre:'Collar'},
  {key:'anillo',    nombre:'Anillo'},
  {key:'tobillera', nombre:'Tobillera'},
];

function renderFiltrosTipo(tipos) {
  const cont = document.getElementById('filtros-tipo-container');
  if (!cont) return;
  cont.innerHTML = `<button class="filtro filtro-tipo activo" onclick="filtrarTipo(this,'todos')">Todos</button>`
    + tipos.map(t => `<button class="filtro filtro-tipo" onclick="filtrarTipo(this,'${t.key}')">${t.nombre}</button>`).join('');
}

function renderFiltrosCol(colecciones) {
  const cont = document.getElementById('filtros-col-container');
  if (!cont) return;
  cont.innerHTML = `<button class="filtro filtro-col activo-col" onclick="filtrarCol(this,'todas')">Todas</button>`
    + colecciones.map(col => `<button class="filtro filtro-col" onclick="filtrarCol(this,'${col.key}')">${col.emoji} ${col.nombre}</button>`).join('');
}

function crearCarrusel(producto, idProducto) {
  const imgs = producto.imgs || (producto.img ? [producto.img] : []);
  const tieneVarias = imgs.length > 1;
  const id = `car-${idProducto}`;
  if (imgs.length === 0) return `
    <div class="pcard-carousel">
      <div class="carousel-track"><div class="carousel-slide">
        <div class="carousel-placeholder">
          <i class="fa-regular fa-image" style="font-size:1.8rem;opacity:.25"></i>
          <span>${producto.nombre}</span>
        </div>
      </div></div>
    </div>`;
  const slides  = imgs.map(src => `<div class="carousel-slide"><img src="${src}" alt="${producto.nombre}" loading="lazy"/></div>`).join('');
  const dots    = tieneVarias ? `<div class="carousel-dots" id="${id}-dots">${imgs.map((_,i) => `<span class="carousel-dot${i===0?' activo':''}" onclick="irSlide('${id}',${i},event)"></span>`).join('')}</div>` : '';
  const flechas = tieneVarias ? `
    <button class="carousel-arrow prev visible" onclick="moverCarrusel('${id}',-1,event)" aria-label="Anterior"><i class="fa-solid fa-chevron-left"></i></button>
    <button class="carousel-arrow next visible" onclick="moverCarrusel('${id}',1,event)"  aria-label="Siguiente"><i class="fa-solid fa-chevron-right"></i></button>` : '';
  return `
    <div class="pcard-carousel" id="${id}" data-idx="0" data-total="${imgs.length}">
      <div class="carousel-track" id="${id}-track">${slides}</div>
      ${flechas}${dots}
    </div>`;
}

window.moverCarrusel = function(id, delta, e) {
  if (e) e.stopPropagation();
  const wrap = document.getElementById(id); if (!wrap) return;
  const total = parseInt(wrap.dataset.total);
  let idx = parseInt(wrap.dataset.idx) + delta;
  if (idx < 0) idx = total - 1; if (idx >= total) idx = 0;
  aplicarSlide(id, idx);
};
window.irSlide = function(id, idx, e) { if (e) e.stopPropagation(); aplicarSlide(id, idx); };
function aplicarSlide(id, idx) {
  const wrap   = document.getElementById(id);
  const track  = document.getElementById(id + '-track');
  const dotsEl = document.getElementById(id + '-dots');
  if (!wrap || !track) return;
  track.style.transform = `translateX(-${idx * 100}%)`;
  wrap.dataset.idx = idx;
  if (dotsEl) dotsEl.querySelectorAll('.carousel-dot').forEach((d, i) => d.classList.toggle('activo', i === idx));
}
function initTouchCarrusel(wrap) {
  if (wrap.dataset.touchInit) return;
  wrap.dataset.touchInit = '1';
  wrap.style.touchAction = 'pan-y';

  let sx = 0, sy = 0, dragging = false;
  const track = wrap.querySelector('.carousel-track');

  wrap.addEventListener('touchstart', e => {
    sx = e.touches[0].clientX;
    sy = e.touches[0].clientY;
    dragging = true;
    if (track) track.style.transition = 'none';
  }, { passive:true });

  wrap.addEventListener('touchmove', e => {
    if (!dragging) return;
    const dx = e.touches[0].clientX - sx;
    const dy = e.touches[0].clientY - sy;
    if (Math.abs(dx) > Math.abs(dy) && track) {
      const idx = parseInt(wrap.dataset.idx || '0');
      track.style.transform = `translateX(calc(-${idx * 100}% + ${dx}px))`;
    }
  }, { passive:true });

  wrap.addEventListener('touchend', e => {
    if (!dragging) return;
    dragging = false;
    if (track) track.style.transition = '';
    const dx = e.changedTouches[0].clientX - sx;
    const dy = e.changedTouches[0].clientY - sy;
    if (Math.abs(dx) > Math.abs(dy) && Math.abs(dx) > 35) {
      moverCarrusel(wrap.id, dx < 0 ? 1 : -1, null);
    } else {
      const idx = parseInt(wrap.dataset.idx || '0');
      if (track) track.style.transform = `translateX(-${idx * 100}%)`;
    }
  }, { passive:true });
}
function initCarruseles() {
  document.querySelectorAll('.pcard-carousel[data-total]').forEach(wrap => {
    if (parseInt(wrap.dataset.total) <= 1) return;
    const id = wrap.id;
    if (!wrap.querySelector('.carousel-arrow')) {
      ['prev','next'].forEach(dir => {
        const btn = document.createElement('button');
        btn.className = `carousel-arrow ${dir} visible`;
        btn.setAttribute('aria-label', dir === 'prev' ? 'Anterior' : 'Siguiente');
        btn.innerHTML = `<i class="fa-solid fa-chevron-${dir === 'prev' ? 'left' : 'right'}"></i>`;
        btn.onclick = (e) => moverCarrusel(id, dir === 'prev' ? -1 : 1, e);
        wrap.appendChild(btn);
      });
    }
    if (!wrap.querySelector('.carousel-dots')) {
      const total = parseInt(wrap.dataset.total);
      const dotsDiv = document.createElement('div');
      dotsDiv.className = 'carousel-dots'; dotsDiv.id = id + '-dots';
      for (let i = 0; i < total; i++) {
        const dot = document.createElement('span');
        dot.className = 'carousel-dot' + (i === 0 ? ' activo' : '');
        dot.onclick = (e) => irSlide(id, i, e);
        dotsDiv.appendChild(dot);
      }
      wrap.appendChild(dotsDiv);
    }
    initTouchCarrusel(wrap);
  });
}

function renderCatalogo() {
  const grid = document.getElementById('prod-grid');
  if (!grid) return;

  grid.querySelectorAll('.skel, .pcard').forEach(c => c.remove());

  const counter = document.getElementById('prod-count');
  if (counter) counter.textContent = PRODUCTOS.length + ' piezas';

  try {
    const urlParams   = new URLSearchParams(window.location.search);
    const colParam    = urlParams.get('col');
    const colGuardada = colParam || sessionStorage.getItem('mia-filtro-col');
    if (colGuardada) {
      filtroColActivo = colGuardada;
      if (!colParam) sessionStorage.removeItem('mia-filtro-col');
      document.querySelectorAll('.filtro-col').forEach(btn => {
        btn.classList.remove('activo-col');
        if (btn.getAttribute('onclick')?.includes(`'${colGuardada}'`)) btn.classList.add('activo-col');
      });
    }
  } catch {}

  const tarjetas = PRODUCTOS.map((p, idx) => {
    const delay      = ['','d1','d2','d3'][idx % 4];
    const primeraImg = (p.imgs && p.imgs.length) ? p.imgs[0] : (p.img || null);
    const imgHtml    = primeraImg
      ? `<img src="${primeraImg}" alt="${p.nombre}" loading="lazy" style="width:100%;height:100%;object-fit:cover;display:block;transition:transform .5s ease"/>`
      : `<div class="carousel-placeholder"><i class="fa-regular fa-image" style="font-size:2.5rem;opacity:.3"></i><span>Sin foto</span></div>`;
    return `
      <article class="pcard reveal ${delay}" data-id="${p.id}" data-tipo="${p.tipo}" data-col="${p.col}" onclick="navegarProducto(event,${p.id})" style="cursor:pointer">
        <div class="pcard-carousel">${imgHtml}</div>
        <div class="pcard-badges" style="position:absolute;top:12px;left:12px;z-index:9;display:flex;flex-direction:column;gap:5px">
          <span class="pcard-badge">${TIPO_LABEL[p.tipo] || p.tipo}</span>
          <span class="pcard-badge col">${COL_LABEL[p.col] || p.col}</span>
        </div>
        <div class="pcard-body">
          <h3 class="pcard-nombre">${p.nombre}</h3>
          <p class="pcard-desc">${p.desc}</p>
          <div class="pcard-footer">
            <div class="pcard-precio"><small>Precio</small>$${p.precio.toLocaleString('es-AR')}</div>
            <div class="pcard-ctrl">
              <div class="qty">
                <button class="qty-btn" onclick="cambiarQty(this,-1)">−</button>
                <input class="qty-num" type="number" value="1" min="1" max="10" readonly/>
                <button class="qty-btn" onclick="cambiarQty(this,1)">+</button>
              </div>
              <button class="add-btn" onclick="agregarAlPedido(this,${p.id})">
                <i class="fa-solid fa-bag-shopping"></i> Agregar
              </button>
            </div>
          </div>
        </div>
      </article>`;
  }).join('');

  const especial = `
    <article class="pcard pcard-special reveal" data-col="todos">
      <div class="pcard-img" style="background:var(--orofondo)">
        <div class="pcard-ph" style="color:var(--oro)">
          <i class="fa-solid fa-wand-magic-sparkles" style="font-size:2.5rem"></i>
          <span style="font-size:.85rem;color:var(--txt2);font-style:italic">Algo especial para ti</span>
        </div>
      </div>
      <div class="pcard-body">
        <h3 class="pcard-nombre">¿Tienes algo en mente?</h3>
        <p class="pcard-desc">Cuéntanos tu idea y la creamos desde cero.</p>
        <div class="pcard-footer">
          <div class="pcard-precio"><small>Desde</small>$2.000</div>
          <a href="personalizado.html" class="add-btn" style="background:var(--oro)">
            <i class="fa-solid fa-pencil"></i> Diseñar
          </a>
        </div>
      </div>
    </article>`;

  const sr = document.getElementById('sin-resultados');
  if (sr) {
    sr.insertAdjacentHTML('afterend', tarjetas + especial);
  } else {
    grid.innerHTML += tarjetas + especial;
  }

  const obsCards = new IntersectionObserver(entries => {
    entries.forEach(e => { if (e.isIntersecting) { e.target.classList.add('vis'); obsCards.unobserve(e.target); } });
  }, { threshold: 0.06 });
  grid.querySelectorAll('.reveal').forEach(el => {
    if (el.getBoundingClientRect().top < innerHeight) { el.classList.add('vis'); }
    else { obsCards.observe(el); }
  });
  aplicarFiltros();
}

function navegarProducto(e, id) {
  if (e.target.closest('button, input, a')) return;
  document.body.classList.add('pg-out');
  setTimeout(() => { window.location.href = `producto.html?id=${id}`; }, 300);
}

function initReveal() {
  const obs = new IntersectionObserver(entries => {
    entries.forEach(e => { if (e.isIntersecting) { e.target.classList.add('vis'); obs.unobserve(e.target); } });
  }, { threshold: 0.08 });
  document.querySelectorAll('.reveal').forEach(el => {
    obs.observe(el);
    if (el.getBoundingClientRect().top < innerHeight) el.classList.add('vis');
  });
}

const TESTIMONIOS_DEFAULT = [
  {id:0, nombre:'Sol G.',  ciudad:'Buenos Aires', estrellas:5, texto:'La calidad es increíble y se nota que está hecha con amor. Recibo cumplidos cada vez que uso mi pulsera. ¡Ya encargué la segunda!'},
  {id:1, nombre:'Vale M.', ciudad:'Rosario',      estrellas:5, texto:'El collar Mariposa Lila es una joya. Super liviano y delicado. El empaque también fue muy cuidado. Cien por ciento recomendable.'},
  {id:2, nombre:'Lu C.',   ciudad:'Córdoba',      estrellas:5, texto:'Compré un pedido personalizado para el cumple de mi amiga y quedó fascinada. Mía fue súper atenta y entregó en tiempo. ¡La mejor!'},
];

const COLECCIONES_DEFAULT = [
  {key:'flores',    emoji:'🌸', nombre:'Flores',    img:'images/pulsera-suspiro-lila.jpeg'},
  {key:'mariposa',  emoji:'🦋', nombre:'Mariposa',  img:'images/collar-mariposa-lila-1.jpeg'},
  {key:'girasol',   emoji:'🌻', nombre:'Girasol',   img:'images/pulsera-alma-girasol-1.jpeg'},
  {key:'cherry',    emoji:'🍒', nombre:'Cherry',    img:'images/pulsera-cherry-margaritas-1.jpeg'},
  {key:'caracoles', emoji:'🐚', nombre:'Caracoles', img:'images/tobillera-caracoles-1.jpeg'},
];

function renderTestimoniosIndex(grid, testimonios) {
  if (!grid || !testimonios || !testimonios.length) return;
  const delays = ['', 'd1', 'd2', 'd3', 'd4', 'd5'];
  grid.innerHTML = testimonios.map((t, i) => {
    const partes   = t.nombre.trim().split(' ');
    const iniciales = partes.map(p => p[0] || '').slice(0, 2).join('').toUpperCase();
    const stars     = Array.from({length: t.estrellas},  () => '<i class="fa-solid fa-star"></i>').join('') +
                      Array.from({length: 5 - t.estrellas}, () => '<i class="fa-regular fa-star"></i>').join('');
    return `
      <div class="tcard reveal ${delays[i % delays.length]}">
        <div class="tcard-stars">${stars}</div>
        <p class="tcard-txt">"${t.texto}"</p>
        <div class="tcard-author">
          <div class="t-av">${iniciales}</div>
          <div><div class="t-nombre">${t.nombre}</div><div class="t-ciudad">${t.ciudad}</div></div>
        </div>
      </div>`;
  }).join('');
  const obs = new IntersectionObserver(entries => {
    entries.forEach(e => { if (e.isIntersecting) { e.target.classList.add('vis'); obs.unobserve(e.target); } });
  }, { threshold: 0.08 });
  grid.querySelectorAll('.reveal').forEach(el => {
    if (el.getBoundingClientRect().top < innerHeight) el.classList.add('vis');
    else obs.observe(el);
  });
}

const COL_GRADIENTS = {
  flores:    'linear-gradient(160deg,#1e2d08,#2e440f,#4a6b22)',
  mariposa:  'linear-gradient(160deg,#2a1a3a,#4a2a6b,#7a4aa0)',
  girasol:   'linear-gradient(160deg,#3a2800,#6b4800,#b87800)',
  cherry:    'linear-gradient(160deg,#3d1a1a,#6b2a2a,#a85a5a)',
  caracoles: 'linear-gradient(160deg,#1a2a3a,#2a3d52,#4a6a84)',
};
const COL_GRADIENT_DEFAULT = 'linear-gradient(160deg,#1e2d08,#2e3d1a,#3a5228)';

function renderColeccionesIndex(colecciones) {
  const grid = document.getElementById('col-grid');
  if (!grid || !colecciones || !colecciones.length) return;
  grid.innerHTML = colecciones.map(col => {
    const gradient = COL_GRADIENTS[col.key] || COL_GRADIENT_DEFAULT;
    const imgHtml  = col.img
      ? `<img src="${col.img}" class="col-bg" alt="${col.nombre}" loading="lazy"/>`
      : '';
    return `
      <a href="catalogo.html?col=${col.key}" class="col-card" data-col-key="${col.key}">
        ${imgHtml}
        <div class="col-ph" style="background:${gradient}">
          <i class="fa-regular fa-image" style="font-size:2rem;color:rgba(255,255,255,.2)"></i>
        </div>
        <div class="col-overlay"></div>
        <div class="col-info">
          <span class="col-nombre">${col.emoji || ''} Colección ${col.nombre}</span>
          <span class="col-cant">Ver piezas</span>
          <span class="col-btn">Ver piezas <i class="fa-solid fa-arrow-right"></i></span>
        </div>
      </a>`;
  }).join('');
}

document.addEventListener('DOMContentLoaded', async () => {
  initFirebase();
  initHeader();
  initPanel();
  initReveal();

  await cargarDatosFirebase();

  const btnVerTodas = document.getElementById('btn-ver-todas');
  if (btnVerTodas) btnVerTodas.innerHTML = `Ver las ${PRODUCTOS.length} piezas <i class="fa-solid fa-arrow-right"></i>`;
  const hstatPiezas = document.getElementById('hstat-piezas');
  if (hstatPiezas) hstatPiezas.textContent = PRODUCTOS.length + '+';

  const siteConfig = await cargarConfigFirebase();

  const testGrid = document.getElementById('test-grid');
  if (testGrid) renderTestimoniosIndex(testGrid, siteConfig.testimonios || TESTIMONIOS_DEFAULT);

  const cols  = siteConfig.colecciones || COLECCIONES_DEFAULT;
  const tipos = siteConfig.tipos       || TIPOS_DEFAULT;
  cols.forEach(col   => { COL_LABEL[col.key]   = col.nombre; });
  tipos.forEach(tipo => { TIPO_LABEL[tipo.key] = tipo.nombre; });
  renderFiltrosTipo(tipos);
  renderFiltrosCol(cols);
  renderColeccionesIndex(cols);

  aplicarFiltros();
  renderCatalogo();
  initCarruseles();
});