# **Tapjoy Plugin for Flutter**

## This is a dependency to use Tapjoy in your Flutter apps. 
## Note: Currently this only works for Android and we are looking for people who will help us with the iOS version. 

Getting started:
### 1. Initialise Tapjoy 
Call `initPlatformState()` during app initialization
Replace `tapjoy_key` with your actual Tapjoy key.

```sh
Future<void> initPlatformState() async {
    Tapjoy.setDebugEnabled(true);  //Make this true only for testing
    Tapjoy.connect(
      'tapjoy_key',
      tapjoyConnectSuccess,
      () => setState(
        () {
          _tapjoyStatus = 'failed';
        },
      ),
    );
    if (!mounted) return;
  }
```
  
### 2. Initialise tapjoyConnectSuccess callback
 
 ```sh
 void tapjoyConnectSuccess(){
    Tapjoy.setActivity();
    Tapjoy.setUserConsent('1'); //'1' indicates that you have taken the consent of your user.
    setState(() {
      _tapjoyStatus = 'connected';
    });
  }
 ```
 ### 3. Call TJPlacement 
  Replace `placement_name` with your Tapjoy placement. 
  
  ```sh
  TJPlacement tjPlacement = await Tapjoy.getPlacement('placement_name',
                    onRequestSuccess: (placement) => placement.showContent(),
                    onRequestFailure: (placement, error) => print(
                        error.errorCode.toString() +
                            ' - ' +
                            error.errorMessage));
                tjPlacement.requestContent();
 ```               
                
            
  For the complete implementation, please refer to the example repo.        
  You can refer to the Tapjoy SDK Documentation for any features you want to implement in Dart.
  
 #### Future Work
  #### Implement for iOS Platform.
