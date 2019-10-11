"use strict";
import { NativeEventEmitter, NativeModules, Platform } from "react-native";

const NativeCie = NativeModules.NativeCieModule;
const NativeCieEmitter = new NativeEventEmitter(NativeCie);

class CieManager {
  constructor() {
    this._eventSuccessHandlers = [];
    this._eventErrorHandlers = [];
    this._eventHandlers = [];
    this._registerEventEmitter();
  }

  /**
   * private
   */
  _registerEventEmitter = () => {
    NativeCieEmitter.addListener("onEvent", e => {
      this._eventHandlers.forEach(h =>
        h({ event: e.event, attempts: e.attempts })
      );
    });
    NativeCieEmitter.addListener("onSuccess", e => {
      this._eventSuccessHandlers.forEach(h => h(e.event));
    });
    NativeCieEmitter.addListener("onError", e => {
      this._eventErrorHandlers.forEach(h => h(new Error(e.event)));
    });
  };

  onEvent = listner => {
    if (this._eventHandlers.indexOf(listner) >= 0) {
      return;
    }
    this._eventHandlers = [...this._eventHandlers, listner];
  };

  onError = listner => {
    if (this._eventErrorHandlers.indexOf(listner) >= 0) {
      return;
    }
    this._eventErrorHandlers = [...this._eventErrorHandlers, listner];
  };

  onSuccess = listner => {
    if (this._eventSuccessHandlers.indexOf(listner) >= 0) {
      return;
    }
    this._eventSuccessHandlers = [...this._eventSuccessHandlers, listner];
  };

  removeAllListeners = () => {
    this._eventSuccessHandlers.length = 0;
    this._eventErrorHandlers.length = 0;
    this._eventHandlers.length = 0;
  };

  setPin = pin => {
    NativeCie.setPin(pin);
  };

  setAuthenticationUrl = url => {
    NativeCie.setAuthenticationUrl(url);
  };

  start = () => {
    return new Promise((resolve, reject) => {
      NativeCie.start((err, _) => {
        if (err) {
          reject(err);
        } else {
          resolve();
        }
      });
    });
  };

  startListeningNFC = () => {
    return new Promise((resolve, reject) => {
      NativeCie.startListeningNFC((err, _) => {
        if (err) {
          reject(err);
        } else {
          resolve();
        }
      });
    });
  };

  stopListeningNFC = () => {
    return new Promise((resolve, reject) => {
      NativeCie.stopListeningNFC((err, _) => {
        if (err) {
          reject(err);
        } else {
          resolve();
        }
      });
    });
  };

  /**
   * Return true if the nfc is enabled, on the device in Settings screen
   * is possible enable or disable it.
   */
  isNFCEnabled = () => {
    if (Platform.OS === "ios") {
      return Promise.reject("not implemented");
    }
    return new Promise(resolve => {
      NativeCie.isNFCEnabled(result => {
        resolve(result);
      });
    });
  };

  /**
   * Check if the hardware module nfc is installed (only for Android devices)
   */
  hasNFCFeature = () => {
    if (Platform.OS === "ios") {
      return Promise.reject("not implemented");
    }
    return new Promise(resolve => {
      NativeCie.hasNFCFeature(result => {
        resolve(result);
      });
    });
  };

  /**
   * It opens OS Settings on NFC section
   *
   */
   openNFCSettings = () => {
        if (Platform.OS === 'ios') {
         return Promise.reject('not implemented');
       }
       return new Promise((resolve, reject) => {
         NativeCie.openNFCSettings((err) => {
           if (err) {
             reject(err);
           } else {
             resolve();
           }
         })
       })
   }
}

export default new CieManager();
