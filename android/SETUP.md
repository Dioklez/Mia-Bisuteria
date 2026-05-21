# Mía Bisutería Admin — Setup Android

## 1. Firebase: agregar la app Android

1. Ir a https://console.firebase.google.com → proyecto `mia-bisuteria`
2. Agregar app Android:
   - Package: `com.miabisuteria.admin`
   - Nickname: `Mía Admin`
3. Descargar `google-services.json`
4. Copiar a `android/app/google-services.json`

## 2. Abrir en Android Studio

1. Abrir Android Studio
2. File → Open → seleccionar la carpeta `android/`
3. Esperar que Gradle sincronice

## 3. Keystore (firma de APK)

Generar un keystore para firmar el APK de release:

```bash
keytool -genkey -v -keystore mia-admin.jks \
  -alias mia-admin \
  -keyalg RSA -keysize 2048 \
  -validity 10000
```

Crear `android/local.properties` (ya en .gitignore):
```
storeFile=../mia-admin.jks
storePassword=TU_STORE_PASSWORD
keyAlias=mia-admin
keyPassword=TU_KEY_PASSWORD
```

## 4. GitHub Secrets (para CI/CD)

Ir a GitHub → Settings → Secrets and variables → Actions:

| Secret | Valor |
|--------|-------|
| `ANDROID_KEYSTORE_B64` | `base64 -w0 mia-admin.jks` |
| `ANDROID_KEY_ALIAS` | `mia-admin` |
| `ANDROID_KEY_PASS` | tu key password |
| `ANDROID_STORE_PASS` | tu store password |

## 5. GitHub Actions — BuildConfig

En `app/build.gradle.kts` hay dos campos que debes actualizar con tu repo real:
```kotlin
buildConfigField("String", "GITHUB_OWNER", "\"TU_USUARIO_GITHUB\"")
buildConfigField("String", "GITHUB_REPO", "\"TU_REPO\"")
```

## 6. Build manual

```bash
cd android
./gradlew assembleDebug      # APK de debug
./gradlew assembleRelease    # APK firmado (necesita keystore)
```

## 7. Auto-update

Para publicar una nueva versión:
```bash
git tag v1.0.1
git push origin v1.0.1
```
GitHub Actions compilará y creará el Release automáticamente.
La app detecta la nueva versión al abrirse y muestra el dialog de actualización.

## 8. Notas importantes

- La app SOLO funciona si el archivo `google-services.json` está presente
- La contraseña inicial se crea la primera vez que se abre la app
- `com.miabisuteria.admin.debug` es el app ID de debug (no interfiere con release)
