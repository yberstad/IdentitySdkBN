/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * Generated with the TypeScript template
 * https://github.com/react-native-community/react-native-template-typescript
 *
 * @format
 */

import React from 'react';
import {Button, StyleSheet, Text, View} from 'react-native';

import IdentitySdk from './bridge/IdentitySdk';

const App = () => {
  return (
    <View style={styles.container}>
      <Text style={styles.welcome}>
        io.signicat:signicat-identity-sdk:1.0.5
      </Text>
      <Button
        title="Start identification"
        onPress={() => {
          IdentitySdk.startAuthorization(
            'NO',
            'f7d8c981-5906-4290-baa9-78bb9371954d',
          );
        }}
      />
    </View>
  );
};
const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
  welcome: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10,
  },
  textInputStyle: {height: 40, borderColor: 'gray', borderWidth: 1},
});

export default App;
