// Type definitions for react-native-cie
// Project: https://github.com/teamdigitale/io-cie-android-sdk
declare module "react-native-cie" {
  interface CieManager {
    isNFCEnabled(): Promise<boolean>;
    hasNFCFeature(): Promise<boolean>;
    setPin(pin: string): void;
    setEventListner(callback: (event: any) => void): void;
    setAuthenticationUrl(url: string): void;
    start(): Promise<never>;
    startListeningNFC(): Promise<never>;
    stopListeningNFC(): Promise<never>;
  }

  const cieManager: CieManager;
}

export default cieManager;
