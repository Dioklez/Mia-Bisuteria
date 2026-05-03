'use strict';
const MIA_AUTH = (() => {
  const TIMEOUT = 10 * 60 * 1000; // 10 minutos
  const K = {
    pass: 'mia-pass-hash',
    pin:  'mia-pin-hash',
    wa:   'mia-wa-cred',
    exp:  'mia-session-exp',
  };

  async function sha256(str) {
    const buf = await crypto.subtle.digest('SHA-256', new TextEncoder().encode(str));
    return Array.from(new Uint8Array(buf)).map(b => b.toString(16).padStart(2, '0')).join('');
  }

  return {
    isValid()     { return Date.now() < parseInt(localStorage.getItem(K.exp) || 0); },
    refresh()     { localStorage.setItem(K.exp, String(Date.now() + TIMEOUT)); },
    revoke()      { localStorage.removeItem(K.exp); },
    isFirstTime() { return !localStorage.getItem(K.pass); },

    async savePassword(p) { localStorage.setItem(K.pass, await sha256(p)); },
    async checkPassword(p) {
      const s = localStorage.getItem(K.pass);
      if (!s) return p === 'miabisuteria2025'; // migración del sistema anterior
      return s === await sha256(p);
    },

    hasPin()        { return !!localStorage.getItem(K.pin); },
    async savePin(p) { localStorage.setItem(K.pin, await sha256(p)); },
    async checkPin(p) {
      const s = localStorage.getItem(K.pin);
      return !!s && s === await sha256(p);
    },
    removePin()     { localStorage.removeItem(K.pin); },

    hasCredential()        { return !!localStorage.getItem(K.wa); },
    isWebAuthnSupported()  { return !!(window.PublicKeyCredential && navigator.credentials?.create); },

    async registerCredential() {
      const cred = await navigator.credentials.create({ publicKey: {
        challenge: crypto.getRandomValues(new Uint8Array(32)),
        rp: { name: 'Mía Admin', id: location.hostname },
        user: {
          id: crypto.getRandomValues(new Uint8Array(16)),
          name: 'admin',
          displayName: 'Admin'
        },
        pubKeyCredParams: [
          { type: 'public-key', alg: -7 },
          { type: 'public-key', alg: -257 }
        ],
        authenticatorSelection: {
          authenticatorAttachment: 'platform',
          userVerification: 'required',
          residentKey: 'preferred'
        },
        timeout: 60000
      }});
      localStorage.setItem(K.wa, btoa(String.fromCharCode(...new Uint8Array(cred.rawId))));
    },

    async verifyCredential() {
      const raw = localStorage.getItem(K.wa);
      if (!raw) return false;
      const id = Uint8Array.from(atob(raw), c => c.charCodeAt(0));
      const res = await navigator.credentials.get({ publicKey: {
        challenge: crypto.getRandomValues(new Uint8Array(32)),
        allowCredentials: [{ type: 'public-key', id }],
        userVerification: 'required',
        timeout: 60000
      }});
      return !!res;
    },

    removeCredential() { localStorage.removeItem(K.wa); },

    // Llamar al inicio de cada página protegida
    guard() {
      if (!this.isValid()) {
        sessionStorage.setItem('mia-return', location.pathname + location.search);
        location.replace('/admin-app/');
        return false;
      }
      this._keepAlive();
      return true;
    },

    _keepAlive() {
      this.refresh();
      // Refrescar cada 60s mientras la página esté visible
      setInterval(() => { if (document.visibilityState === 'visible') this.refresh(); }, 60000);
    }
  };
})();
