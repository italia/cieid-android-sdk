// Type definitions for react-native-cie
// Project: https://github.com/teamdigitale/io-cie-android-sdk
declare module "react-native-cie" {
  // All events returned by onEvent callback
  type CIEEvent =
    | "ON_TAG_DISCOVERED_NOT_CIE"
    | "ON_TAG_DISCOVERED"
    | "ON_TAG_LOST"
    | "ON_CARD_PIN_LOCKED"
    | "ON_PIN_ERROR"
    | "PIN_INPUT_ERROR"
    | "CERTIFICATE_EXPIRED"
    | "CERTIFICATE_REVOKED"
    | "AUTHENTICATION_ERROR"
    | "ON_NO_INTERNET_CONNECTION";

  type Event = {
    event: CIEEvent;
    attempts: number;
  };
  interface CieManager {
    // check if the device has NFC feature
    hasNFCFeature(): Promise<boolean>;
    // check if NFC is enabled
    isNFCEnabled(): Promise<boolean>;
    // register a callback to receive all Event raised while reading/writing CIE
    onEvent(callback: (event: Event) => void): void;
    // register a callback to receive errors occured while reading/writing CIE
    onError(callback: (error: Error) => void): void;
    // register a callback to receive the success event containing the consent form url
    onSuccess(callback: (url: string) => void): void;
    setAuthenticationUrl(url: string): void;
    // set the CIE pin. It has to be a 8 length string of 8 digits
    setPin(pin: string): Promise<void>;
    start(): Promise<void>;
    // command CIE SDK to start reading/writing CIE CARD
    startListeningNFC(): Promise<void>;
    // command CIE SDK to stop reading/writing CIE CARD
    stopListeningNFC(): Promise<void>;
    // Remove all events callbacks: onEvent / onError / onSuccess
    removeAllListeners(): void;
  }

  const cieManager: CieManager;
}

export default cieManager;
