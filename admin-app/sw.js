importScripts('https://www.gstatic.com/firebasejs/10.12.0/firebase-app-compat.js');
importScripts('https://www.gstatic.com/firebasejs/10.12.0/firebase-messaging-compat.js');

firebase.initializeApp({
  apiKey: "AIzaSyDJPxQ-L4XiWmGuNi2XYKnuEhhidnHnIhI",
  authDomain: "mia-bisuteria.firebaseapp.com",
  projectId: "mia-bisuteria",
  storageBucket: "mia-bisuteria.firebasestorage.app",
  messagingSenderId: "406918365058",
  appId: "1:406918365058:web:b444d55e4983c1ec99999b"
});

const messaging = firebase.messaging();

const CACHE_NAME = 'mia-admin-v2';
const CACHE_URLS = [
  'index',
  'pedidos',
  'pedido-detalle',
  'calculadora',
  'css/admin.css'
];

// Install — cache static assets
self.addEventListener('install', event => {
  event.waitUntil(
    caches.open(CACHE_NAME).then(cache => cache.addAll(CACHE_URLS))
  );
  self.skipWaiting();
});

// Activate — clean old caches
self.addEventListener('activate', event => {
  event.waitUntil(
    caches.keys().then(keys =>
      Promise.all(keys.filter(k => k !== CACHE_NAME).map(k => caches.delete(k)))
    )
  );
  self.clients.claim();
});

// Fetch — network-first para Firestore/Firebase, cache-first para estáticos
self.addEventListener('fetch', event => {
  const url = event.request.url;
  if (url.includes('firestore.googleapis.com') || url.includes('firebase') || url.includes('googleapis.com')) {
    event.respondWith(
      fetch(event.request).catch(() => caches.match(event.request))
    );
    return;
  }
  event.respondWith(
    caches.match(event.request).then(cached => cached || fetch(event.request))
  );
});

// Push notifications en background (via Firebase Messaging)
messaging.onBackgroundMessage(payload => {
  const title = payload.notification?.title || 'Mía Admin';
  const body  = payload.notification?.body  || 'Nuevo pedido recibido';
  self.registration.showNotification(title, {
    body,
    icon: '../images/logos/flor-verde.jpg',
    badge: '../images/logos/flor-verde.jpg',
    data: { url: '/admin-app/pedidos' }
  });
});

// Click en notificación — abrir pedidos
self.addEventListener('notificationclick', event => {
  event.notification.close();
  const url = event.notification.data?.url || '/admin-app/pedidos';
  event.waitUntil(
    self.clients.matchAll({ type: 'window' }).then(clients => {
      const existing = clients.find(c => c.url.includes('admin-app'));
      if (existing) return existing.focus();
      return self.clients.openWindow(url);
    })
  );
});
