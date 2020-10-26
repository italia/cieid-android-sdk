# CieID-android-sdk

CieID-android-sdk è un SDK Android sviluppato in Kotlin che include le funzionalità di autenticazione di "Entra con CIE". Utilizzando questo kit, gli sviluppatori di applicazioni terze Android possono integrare l'autenticazione mediante la cartà d'identità elettronica (CIE 3.0).

# Requisiti tecnici

CieID-android-sdk è compatibile dalla versione Android 6.0 (API level 23) o successive. Necessità inoltre di connessione ad internet e di smartphone con tecnologia NFC.

# Requisiti di integrazione

CieID-android-sdk necessita che il fornitore del servizio digitale sia un Service Provider federato e che integri la tecnologia abilitante al flusso di autenticazione "Entra con CIE". [Maggiori informazioni qui.](https://www.cartaidentita.interno.gov.it/CIE3.0-ManualeSP.pdf "Manuale SP")

# Come si usa

## Flusso AppToApp
Permette di completare il flusso di autenticazione mediante l'applicazione "CieID" presente sul Play Store Android.

## Flusso interno
Permette di completare il flusso di autenticazione internamente all'applicazione stessa integrando il modulo "CieIDSdk":

```gradle
implementation project(path: ':cieidsdk')
```
Nel kit è presente un'applicazione di esempio che mostra come integrare i 2 flussi facilmente. In entrambi i flussi la gestione degli errori è demandata all'applicazione integrante.

# Configurazione

## Flusso con reindirizzamento
Per integrare le funzionalità dell'SDK utilizza i seguenti metodi nell'activity di tuo interesse:
```kotlin
      val intent = Intent()
                    try {
                        //inserire package di collaudo
                        intent.setClassName(appPackageName, className)
                        //settare la url caricata dalla webview su /OpenApp
                        intent.data = Uri.parse(url)
                        intent.action = Intent.ACTION_VIEW
                        startActivityForResult(intent, 0)

                    } catch (a : ActivityNotFoundException) {
                        startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
                            )
                        )
                    }
                    return true

   override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val url = data?.getStringExtra(URL)
        webView.loadUrl(url)


    }
```

## Flusso integrato
Nel build.gradle seleziona l'ambiente server dell'identity provider (iDP) di tuo interesse utilizzando i commenti
```gradle
//AMBIENTI:
//Ambiente di produzione
//buildConfigField "String", "BASE_URL_IDP", "\"https://idserver.servizicie.interno.gov.it/idp/\""

//Ambiente di collaudo
buildConfigField "String", "BASE_URL_IDP", "\"https://preproduzione.idserver.servizicie.interno.gov.it/idp/\""
```
Per integrare le funzionalità dell'SDK utilizza i seguenti metodi nell'activity di tuo interesse:
```kotlin
//Configurazione iniziale
CieIDSdk.start(activity, callback)
//Avvio utilizzo NFC
CieIDSdk.startNFCListening(activity)
//Abilitare o disabilitare i log, da disattivare in produzione
CieIDSdk.enableLog = true
//Bisogna settare la url caricata dalla pagina web dell' SP dalla webview su /OpenApp
CieIDSdk.setUrl(url.toString())
//inserire il pin della CIE
CieIDSdk.pin = input.text.toString()
//Avviare NFC
startNFC()
```
Implementa inoltre le interfacce di Callback implementando i seguenti metodi:
```kotlin
override fun onEvent(event: Event) {
//evento 
}
override fun onError(e: Throwable) {
//caso di errore
}
override fun onSuccess(url: String) {
//caso di successo con url della pagina da caricare
}
```
# Licenza
Il codice sorgente è rilasciato sotto licenza BSD (codice SPDX: BSD-3-Clause).
