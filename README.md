# CieID-android-sdk

CieID-android-sdk è un SDK Android sviluppato in Kotlin che integra le funzionalità di autenticazione di "Entra con CIE". Integrando le funzionalità di questo kit, gli sviluppatori di app terze possono integrare l'autenticazione sui sistemi della pubblica amministrazione per la cartà d'identità elettronica (CIE 3.0) nelle app Android.

# Requisiti tecnici

CieID-android-sdk è compatibile dalla versione Android 6.0 (API level 23) o successive. Necessità inoltre di connessione ad internet e di smartphone con tecnologia NFC.

# Requisiti di integrazione

CieID-android-sdk necessita che il fornitore del servizio digitale sia un Service Provider federato e che integri la tecnologia abilitante al flusso di autenticazione "Entra con CIE". [Maggiori informazioni qui.](https://www.cartaidentita.interno.gov.it/CIE3.0-ManualeSP.pdf "Manuale SP")

# Come si usa

Integra il modulo "CieIDSdk" nell' applicazione:

```gradle
implementation project(path: ':cieidsdk')
```
Nel kit è presente un'applicazione di esempio che mostra come integrare l'SDK facilmente.

Configurazione
--------

Nel build.gradle seleziona l'ambiente server dell'identity provider (iDP) di tuo interesse utilizzando i commenti
```gradle
        //AMBIENTI:

        //Ambiente di produzione
        //buildConfigField "String", "BASE_URL_IDP", "\"https://idserver.servizicie.interno.gov.it/idp/\""
	    
        //Ambiente di collaudo
        buildConfigField "String", "BASE_URL_IDP", "\"https://idserver.servizicie.interno.gov.it:8443/idp/\""
```
Per integrare le funzionalità dell'SDK utilizza i seguenti metodi nell'activity di tuo interesse:
```kotlin
		//Configurazione iniziale
		CieIDSdk.start(activity, activity)
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

