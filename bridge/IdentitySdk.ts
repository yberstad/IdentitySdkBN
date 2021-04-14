import {NativeModules} from 'react-native';

const bridge = (NativeModules as any).IdentitySdkBridge;

class IdentitySdkBridge {
  static startAuthorization(languageCode: string, proceedToken: string) {
    return bridge.startAuthorization(
      languageCode,
      proceedToken,
    ) as Promise<string>;
  }
}

export default IdentitySdkBridge;
