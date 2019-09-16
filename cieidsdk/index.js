"use strict";
import { NativeEventEmitter, NativeModules, Platform } from "react-native";

const NativeCie = NativeModules.NativeCieModule;
const NativeCieEmitter = new NativeEventEmitter(NativeCie);

class CieManager {
  constructor() {
    this._eventHandlers = [];
    this._registerEventEmitter();
  }

  /**
   * private
   */
  _registerEventEmitter = () => {
    NativeCieEmitter.addListener("event", e => {
      this._eventHandlers.forEach(h => h(e));
    });
  };

  setEventListner = listner => {
    if (this._eventHandlers.indexOf(listner) >= 0) {
      return;
    }
    this._eventHandlers = [...this._eventHandlers, listner];
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

  isNFCEnabled = () => {
    if (Platform.OS === "ios") {
      return Promise.reject("not implemented");
    }
    return new Promise((resolve, reject) => {
      NativeCie.isNFCEnabled((err, result) => {
        if (err) {
          reject(err);
        } else {
          resolve(result);
        }
      });
    });
  };

  hasNFCFeature = () => {
    if (Platform.OS === "ios") {
      return Promise.reject("not implemented");
    }
    return new Promise((resolve, reject) => {
      NativeCie.hasNFCFeature((err, result) => {
        if (err) {
          reject(err);
        } else {
          resolve(result);
        }
      });
    });
  };
}

export default new CieManager();
